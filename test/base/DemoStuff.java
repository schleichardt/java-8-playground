package base;

import play.libs.F;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public final class DemoStuff {
    private DemoStuff() {
        //utility class
    }

    public static void doSomething() {
        System.out.println("do something");
    }

    public static F.Promise<String> promiseSource() {
        return F.Promise.pure("hello");
    }

    public static FiniteDuration finiteDuration() {
        return Duration.create(0, TimeUnit.MILLISECONDS);
    }
}
