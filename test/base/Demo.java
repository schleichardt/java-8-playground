package base;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import play.libs.F;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scalaz.Alpha;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public final class Demo {

    private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Demo.class);

    private Demo() {
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

    public static List<String> newStringList() {
        return Lists.newArrayList("foo", "bar", "baz");
    }

    public static Object bigAndComplexObject() {
        return "foo";
    }

    public static class Logger {
        public static void trace(F.Function0<String> callByName) {
            if (LOGGER.isTraceEnabled()) {
                try {
                    LOGGER.trace(callByName.apply());
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        }

        public static void trace(String s, Object o) {

        }
    }

    public static Stream<Person> newPersonStream() {
        return Stream.of(new Person("Max Mustermann", 1));
    }

    public static class Person {
        private final String name;
        private final int id;

        public Person(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
}
