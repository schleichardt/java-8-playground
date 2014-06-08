package java8;

import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Scheduler;
import org.junit.Test;
import play.libs.Akka;
import play.libs.F;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

import static base.DemoStuff.*;
import static org.fest.assertions.Assertions.assertThat;

public class LambdaExpressions {

    /*
    closures use -> (speak: arrow) instead of => as in Scala to separate parameters from the body

    if no parameter is present, parenthesis before the arrow are necessary.

    If the closure contains a single statement, i never contains a semicolon at the end.
     */
    @Test
    public void withoutParameters() {
        final ActorSystem system = ActorSystem.create();
        final Scheduler scheduler = system.scheduler();
        scheduler.scheduleOnce(finiteDuration(), () -> doSomething(), system.dispatcher());
        system.shutdown();
    }

    /*
    closures with one parameter can optionally contain parenthesis

    promiseSource().map(_.toLowerCase()); does not work like in Scala
     */
    @Test
    public void withParameter() {
        promiseSource().map(x -> x.toLowerCase()).map((x) -> x.toUpperCase());
    }


    /*
    If you need resources only for a limited scope you can use Java 7 try-with-resources Statement:
    http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
    {@code
        static String readFirstLineFromFile(String path) throws IOException {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                return br.readLine();
            }
        }
    }

    But for complex life cycles and things that to not implement java.lang.AutoCloseable,
    you can use a function call an a closure.

    Unfortunately Java supports only one parameter list, so it is not possible to provide new control structures
    like in Scala.
     */
    @Test
    public void automaticResourceManagement() {
        withActorSystem(system ->
            system.scheduler().scheduleOnce(finiteDuration(), () -> doSomething(), system.dispatcher())
        );
    }

    public void withActorSystem(F.Callback<ActorSystem> test) {
        final ActorSystem system = ActorSystem.create();
        try {
            test.invoke(system);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            system.shutdown();
        }
    }
}
