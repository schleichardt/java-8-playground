package java8.ch1lambdaexpressions;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.function.Function;

import static org.fest.assertions.Assertions.assertThat;

public class P_1_6_VariableScope {

    /*
    Variables in closures which are not parameters or defined inside the closure are free variables.
     */


    @Test
    public void scopeIsAutomaticallyFinal() throws Exception {
        int counter = 0;//effectively a constant!

        final Function<String, Integer> function = s -> {
            System.out.println(s + " " + counter);
            return counter;
        };

//       counter++;// results in error: local variables referenced from a lambda expression must be final or effectively final

        final Integer result = Lists.newArrayList("foo").stream().map(function).findFirst().get();
        assertThat(result).isEqualTo(0);
    }
}
