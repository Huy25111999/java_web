package controllers;

import actors.ActorParentSocket;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.stream.javadsl.Flow;
import akka.util.Timeout;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.WebSocket;
import scala.concurrent.duration.Duration;
import vn.m2m.common.models.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;

public class SocketController extends AbstractController {
    private final Timeout timeout = new Timeout(Duration.create(1, TimeUnit.SECONDS));
    private final ActorRef actorParentSocket;

    @Inject
    public SocketController(@Named("actorParentSocket") ActorRef actorParentSocket) {
        this.actorParentSocket = actorParentSocket;
    }

    public WebSocket wsSocket() {
        return WebSocket.Json.acceptOrResult(request -> {
            User userSession = getUserSession();
            if (userSession == null) {
                logger.debug("user is null");
                return forbiddenResult();
            }

            String userId = userSession.getId();
            if (sameOriginCheck(request)) {

                final CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> future = wsFutureFlow(request, userId);
                final CompletionStage<F.Either<Result, Flow<JsonNode, JsonNode, ?>>> stage = future.thenApply(F.Either::Right);

                return stage.exceptionally(this::logException);

//                return forbiddenResult();
            } else {
                return forbiddenResult();
            }
        });
    }

    private CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> wsFutureFlow(Http.RequestHeader request, String userId) {
        long id = request.asScala().id();
        logger.info("CompletionStage userId :{}", userId);
        ActorParentSocket.Create create = new ActorParentSocket.Create(Long.toString(id), userId);

        return ask(actorParentSocket, create, timeout).thenApply((Object flow) -> {
            final Flow<JsonNode, JsonNode, NotUsed> f = (Flow<JsonNode, JsonNode, NotUsed>) flow;
            return f.named("websocket");
        });
    }


    private CompletionStage<F.Either<Result, Flow<JsonNode, JsonNode, ?>>> forbiddenResult() {
        final Result forbidden = Results.forbidden("forbidden");
        final F.Either<Result, Flow<JsonNode, JsonNode, ?>> left = F.Either.Left(forbidden);
        return CompletableFuture.completedFuture(left);
    }

    private F.Either<Result, Flow<JsonNode, JsonNode, ?>> logException(Throwable throwable) {
        logger.error("Cannot create websocket", throwable);
        Result result = Results.internalServerError("error");
        return F.Either.Left(result);
    }

    /**
     * Checks that the WebSocket comes from the same origin.  This is necessary to protect
     * against Cross-Site WebSocket Hijacking as WebSocket does not implement Same Origin Policy.
     * <p>
     * See https://tools.ietf.org/html/rfc6455#section-1.3 and
     * http://blog.dewhurstsecurity.com/2013/08/30/security-testing-html5-websockets.html
     */
    private boolean sameOriginCheck(Http.RequestHeader rh) {
        final Optional<String> origin = rh.header("Origin");

        if (! origin.isPresent()) {
            logger.error("originCheck: rejecting request because no Origin header found");
            return false;
        } else if (originMatches(origin.get())) {
            logger.debug("originCheck: originValue = " + origin);
            return true;
        } else {
            logger.error("originCheck: rejecting request because Origin header value " + origin + " is not in the same origin");
            return false;
        }
    }

    private boolean originMatches(String origin) {
        return origin.contains("vtag") || origin.contains(":4444");
    }
}
