package java8;

import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import base.Demo;
import org.junit.Test;
import play.libs.F;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class P_1_2_LambdaSyntax {

    /*
    closures use -> (speak: arrow) instead of => as in Scala to separate parameters from the body

    if no parameter is present, parenthesis before the arrow are necessary.

    If the closure contains a single statement, it never contains a semicolon or a semicolon at the end.
     */
    @Test
    public void withoutParametersWithoutReturnValue() {
        final ActorSystem system = ActorSystem.create();
        final Scheduler scheduler = system.scheduler();
        scheduler.scheduleOnce(Demo.finiteDuration(), () -> Demo.doSomething(), system.dispatcher());
        system.shutdown();
    }

    /*
    Braces are necessary for multiple statements in a lambda expression.
    Every statements must end with a semicolon.
     */
    @Test
    public void withoutParametersWith2Statements() throws Exception {
        final ActorSystem system = ActorSystem.create();
        final Scheduler scheduler = system.scheduler();
        scheduler.scheduleOnce(Demo.finiteDuration(), () -> {
                    Demo.doSomething();
                    System.out.println("hi");
                }, system.dispatcher()
        );
        system.shutdown();
    }

    /*
    If braces are used for a single statement, so a semicolon is necessary.
     */
    @Test
    public void withoutParametersWithBlock() {
        final ActorSystem system = ActorSystem.create();
        final Scheduler scheduler = system.scheduler();
        scheduler.scheduleOnce(Demo.finiteDuration(), () -> {Demo.doSomething();}, system.dispatcher());
        system.shutdown();
    }

    /*
    one liner does not need or allow a return keyword.
    You never specify the return type explicitly, the compiler infers it from the context.
     */
    @Test
    public void withReturnValue() throws Exception {
         Demo.Logger.trace(() -> Demo.bigAndComplexObject() + " String concatenation");
        //using call by name is better than this workaround to minimize String concatenation
        //since sometimes getting a value is expensive
        Demo.Logger.trace("{} String concatenation", Demo.bigAndComplexObject());
    }

    /*
    If you use a block, you need return and semicolons per statement.
     */
    @Test
    public void withReturnValueAndBlock() throws Exception {
         Demo.Logger.trace(() -> {
             return Demo.bigAndComplexObject() + " String concatenation";
         });
    }

    /*
            closures with one parameter can optionally contain parenthesis.
            The parameter can lack a Type annotation. The compiler deduced it.
            If a type annotation is given, parenthesis are necessary.
            Final keyword and Annotations are for lambda expressions possible.

            promiseSource().map(_.toLowerCase()); does not work like in Scala

            Useful to map promises.
            Useful for background tasks.
    */
    @Test
    public void withParameter() {
        Demo.promiseSource().map(x -> x.toLowerCase()).map((x) -> x.toUpperCase());
        Demo.promiseSource().map((String x) -> x.toLowerCase());
        Demo.promiseSource().map((final @Nonnull String x) -> x.toLowerCase());
    }

    /*
    Beautiful example to save classes (inner, anonymous, direct).
     */
    @Test
    public void withParameters() {
        final List<String> list = Demo.newMutableList();
        Collections.sort(list, (a, b) -> Integer.compare(a.length(), b.length()));
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
            system.scheduler().scheduleOnce(Demo.finiteDuration(), () -> Demo.doSomething(), system.dispatcher())
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

    @Test
    public void useFinalVariablesInClosure() throws Exception {
        final String hello = "hello";
        final String result = Demo.promiseSource().map(s -> s + hello).get(1000);
        assertThat(result).endsWith(hello);
    }
}
