package actors;

import akka.Done;
import akka.NotUsed;
import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import akka.stream.KillSwitches;
import akka.stream.Materializer;
import akka.stream.UniqueKillSwitch;
import akka.stream.javadsl.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.assistedinject.Assisted;
import services.SocketService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class ActorSocket extends AbstractActor {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    // Set up a flow that will let us pull out a killswitch for this specific stock,
    // and automatic cleanup for very slow subscribers (where the browser has crashed, etc).
    private final Flow<JsonNode, JsonNode, UniqueKillSwitch> killswitchFlow = Flow.of(JsonNode.class)
            .joinMat(KillSwitches.singleBidi(), Keep.right());

    private final String id;
    private final String userId;
    private final Materializer mat;

    private final Sink<JsonNode, NotUsed> hubSink;
    private final Flow<JsonNode, JsonNode, NotUsed> websocketFlow;

    @Inject
    private SocketService socketService;


    @Inject
    public ActorSocket(@Assisted String id, @Assisted String userId,
                          Materializer mat) {
        this.id = id;
        this.userId = userId;
        this.mat = mat;

        Pair<Sink<JsonNode, NotUsed>, Source<JsonNode, NotUsed>> sinkSourcePair =
                MergeHub.of(JsonNode.class, 16)
                        .toMat(BroadcastHub.of(JsonNode.class, 256), Keep.both())
                        .run(mat);

        this.hubSink = sinkSourcePair.first();
        Source<JsonNode, NotUsed> hubSource = sinkSourcePair.second();

        Sink<JsonNode, CompletionStage<Done>> jsonSink = Sink.foreach((JsonNode json) -> {
            // When the user types in a stock in the upper right corner, this is triggered,
            String type = json.findPath("type").asText();
            if(type.equals("ping")){
                heartbeatSendPongSocket(json);
            }
        });

        // Put the source and sink together to make a flow of hub source as output (aggregating all
        // stocks as JSON to the browser) and the actor as the sink (receiving any JSON messages
        // from the browse), using a coupled sink and source.
        this.websocketFlow = Flow.fromSinkAndSourceCoupled(jsonSink, hubSource)
//                .log("actorWebsocketFlow", logger)
                .watchTermination((n, stage) -> {
                    logger.debug("websocketFlow.watchTermination action");
                    // When the flow shuts down, make sure this actor also stops.
                    stage.thenAccept(f -> context().stop(self()));
                    return NotUsed.getInstance();
                });
    }

    @Override
    public void postStop(){
        logger.debug("=> ActorSocket-{}::postStop", userId);
//        self().tell(PoisonPill.getInstance(),self());
    }


    /**
     * The receive block, useful if other actors want to manipulate the flow.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.CpuSocket.class, this::cpuSocket)
                .build();
    }

    private void cpuSocket(Messages.CpuSocket cpuSocket){
        logger.debug("=> ActorSocket-{}", userId);
        logger.debug("Received message {}", cpuSocket);
        sender().tell(websocketFlow, self());
    }

    private void heartbeatSendPongSocket(JsonNode jsonReceived){

        logger.debug("=> actorParentSocket-{}::heartbeatSend reloadSocket", id);

        boolean minuteMode = jsonReceived.findPath("minuteMode").asBoolean();

        JsonNode jsonPong = socketService.returnDataListCoin(userId);
//        logger.info("jsonPong :{}", jsonPong);

        Source<JsonNode, NotUsed> sourceOut = Source.single(jsonPong);

        String name = "heartbeatSendPong-" + id;

        final RunnableGraph<UniqueKillSwitch> graph = sourceOut
                .viaMat(killswitchFlow, Keep.right())
                .to(hubSink)
                .named(name);
        // Start it up!
        UniqueKillSwitch killSwitch = graph.run(mat);
    }

    public interface Factory {
        Actor create(String id);
    }
}
