package java8.ch2streamapi;

import org.junit.Test;

import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

public class P_2_8_ReductionOperations {
    //if you have an int stream, it is better to call sum() on a stream
    final BinaryOperator<Integer> plus = (x, y) -> x + y;

    @Test
    public void reduceWithoutIdentity() throws Exception {
        assertThat(Stream.of(1, 2, 3, 4).reduce(plus)).isEqualTo(Optional.of(10));
        assertThat(Stream.of(1, 2).reduce(plus)).isEqualTo(Optional.of(3));
        assertThat(Stream.of(1).reduce(plus)).isEqualTo(Optional.of(1));
        assertThat(Stream.<Integer>empty().reduce(plus)).isEqualTo(Optional.empty());
    }

    /*
    identity is just a seed value which should be a neutral element concerning the operation
    like 0 for +
    and 1 for multiplikation
    e op x = x

    reduce is like fold left in Scala

    Benefits: you don't need to work with Optional.
     */
    @Test
    public void reduceWithIdentity() throws Exception {
        assertThat(Stream.of(1, 2, 3, 4).reduce(0, plus)).isEqualTo(10);
        assertThat(Stream.of(1, 2).reduce(0, plus)).isEqualTo(3);
        assertThat(Stream.of(1).reduce(0, plus)).isEqualTo(1);
        assertThat(Stream.<Integer>empty().reduce(0, plus)).isEqualTo(0);
    }
}
