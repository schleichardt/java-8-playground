package java8.ch2streamapi;

import base.Demo;
import org.junit.Test;

import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

public class P_2_12_PrimitiveTypeStreams {

    /*
    Stream<Integer> is inefficient, so there is a class IntStream that uses the unboxed value.


     */

    @Test
    public void intStream() throws Exception {
        final IntStream intStream = IntStream.of(1, 2, 3);
        final Stream<Integer> integerStream = Stream.of(1, 2, 3);
    }

    @Test
    public void intStreamRange() throws Exception {
        final int excludedUpperBound = 100;
        final long count = IntStream.range(0, excludedUpperBound).count();
        assertThat(count).isEqualTo(excludedUpperBound);
    }

    @Test
    public void mapToInt() throws Exception {
        //more efficient
        final IntStream intStream = Demo.newStringList().stream().mapToInt(String::length);
    }

    @Test
    public void boxingAndUnboxing() throws Exception {
        final IntStream intStream = Demo.newStringList().stream().mapToInt(String::length);
        final Stream<Integer> boxed = intStream.boxed();
        final IntStream unboxed = boxed.mapToInt(i -> i);
    }

    @Test
    public void unboxedOptional() throws Exception {
        final IntStream intStream = Demo.newStringList().stream().mapToInt(String::length);
        final OptionalInt first = intStream.findFirst();//it has a similar API than Optional but is a class for itself
        final int valueIfPresent = first.getAsInt();
    }
}
