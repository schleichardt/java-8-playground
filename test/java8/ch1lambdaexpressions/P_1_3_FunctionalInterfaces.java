package java8.ch1lambdaexpressions;

import base.Demo;
import com.google.common.base.Strings;
import org.junit.Test;

import java.io.IOException;
import java.util.function.Function;

import static org.fest.assertions.Assertions.assertThat;

public class P_1_3_FunctionalInterfaces {

    /*
    Places where lambdas can be used:
    object of an interface with a single abstract method is expected
    => backwards compatible with Java 7
    => interface with single ABSTRACT method is a functional interface
       since Java 8 interfaces can have implementations

    => Runnable, Comparator, F.Function.X (for example F.Function in F.Promise.map), F.CallbackX

    Lambdas can only be used on functional interfaces.

    But interfaces can be used as parameters or return values.
    So they are just syntactic sugar.

    http://docs.oracle.com/javase/8/docs/api/java/util/function/package-frame.html
    java.util.function provides a lot of functional interfaces with default methods.
    It looks functional.


    TODO:
    Function
    Consumer
    Predicate
    Supplier
     */

    @Test
    public void functionAsParameter() throws Exception {
        assertThat(compute("hello", s -> s.length())).isEqualTo(5);
    }

    public int compute(final String string, final Function<String, Integer> function) {
        return function.apply(string);
    }

    @Test
    public void functionAsReturnValueAndVariable() throws Exception {
        final Function<Integer, String> function = createFunction();
        assertThat(function.apply(5)).isEqualTo("*****");
    }

    public Function<Integer, String> createFunction() {
        return i -> Strings.repeat("*", i);
    }

    //function as member or variable
    final Function<String, Integer> function = s -> s.length();

    /*
    Compiler checks with the annotation that you have exactly one abstract method.

    In addition it generates javadoc like this:

    This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.
     */
    @FunctionalInterface
    static interface Closeable {
        void close();
    }

    @Test
    public void checkedExceptionsNeedToBeCatchedIfInterfaceDoesNotThrowIt() throws Exception {
        final Runnable task = () -> {
            try {
                throw new IOException("x");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        // does not work in Java, but Scala:
        // final Runnable task = () -> throw new IOException("x");
    }
}
