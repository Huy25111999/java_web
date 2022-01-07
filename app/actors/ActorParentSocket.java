package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import com.typesafe.config.Config;
import play.libs.akka.InjectedActorSupport;
import scala.collection.JavaConverters;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;

public class ActorParentSocket extends AbstractActor implements InjectedActorSupport {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private final Timeout timeout = new Timeout(1, TimeUnit.SECONDS);

    public static class Create {
        final String id;
        final String userId;

        public Create(String id, String userId) {
            this.id = id;
            this.userId = userId;
        }
    }

    private final ActorSocket.Factory childFactory;

    @Inject
    public ActorParentSocket(ActorSocket.Factory childFactory, Config config) {
        this.childFactory = childFactory;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActorParentSocket.Create.class, this::createChild)
                .build();
    }

    private void createChild(ActorParentSocket.Create create){
        logger.info("=> ActorParentSocket::createChild");
        ActorRef child = injectedChild(() -> childFactory.create(create.userId), "ActorSocket-" + create.userId);
        logger.info("----------Show all ActorSocket in context()-------");
        for (ActorRef actorChild: JavaConverters.asJavaCollection(this.getContext().children())) {
            logger.info("actorChild:{}",actorChild.toString());
        }
        logger.info("------------------------------------------------");
        CompletionStage<Object> future = ask(child, new Messages.CpuSocket(create.id, create.userId), timeout);
        pipe(future, context().dispatcher()).to(sender());
    }

    @Override
    public void postStop(){
        logger.info("=> ActorParentSocket::postStop");
    }
}
