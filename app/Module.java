import actors.ActorParentSocket;
import actors.ActorSchedule;
import actors.ActorSocket;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;
import services.AtomicCounter;
import services.Counter;
import vn.m2m.service.ApplicationTimer;
import vn.m2m.service.DoTimeSchedule;

import java.time.Clock;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule implements AkkaGuiceSupport {

    @Override
    public void configure() {
        // Use the system clock as the default implementation of Clock
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        // Ask Guice to create an instance of ApplicationTimer when the
        // application starts.
        bind(ApplicationTimer.class).asEagerSingleton();
        // Set AtomicCounter as the implementation for Counter.
        bind(Counter.class).to(AtomicCounter.class);

        bindActor(ActorSchedule.class, "actorSchedule");
        bind(DoTimeSchedule.class).asEagerSingleton();

        bindActor(ActorParentSocket.class, "actorParentSocket");
        bindActorFactory(ActorSocket.class, ActorSocket.Factory.class);
    }

}
