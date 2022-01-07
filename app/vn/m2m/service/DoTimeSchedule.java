package vn.m2m.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.duration.Duration;
import vn.m2m.models.ScheduleJob;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class DoTimeSchedule {

    @Inject
    public DoTimeSchedule(final ActorSystem system, @Named("actorSchedule") final ActorRef actorSchedule) {
        final int timeDelayFromAppStart = 0;
        final int timeGapInMinutes = 1;
        system.scheduler().schedule(Duration.create(timeDelayFromAppStart, TimeUnit.SECONDS),
                Duration.create(timeGapInMinutes, TimeUnit.MINUTES),
                actorSchedule,
                new ScheduleJob("actorSchedule scheduleJob"),
                system.dispatcher(),
                null);
    }
}
