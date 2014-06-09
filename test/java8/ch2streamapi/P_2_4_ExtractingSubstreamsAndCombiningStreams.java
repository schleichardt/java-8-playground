package java8.ch2streamapi;

import base.Demo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

public class P_2_4_ExtractingSubstreamsAndCombiningStreams {

    @Test
    public void limitClosesTheStreamAfterNElements() throws Exception {
        //100 random numbers
        final Stream<Double> limit = Stream.generate(Math::random).limit(100);
    }

    @Test
    public void skipForOffsets() throws Exception {
        final List<Integer> actual = Stream.of(0, 1, 2, 3, 4, 5, 6).skip(3).collect(Collectors.toList());
        assertThat(actual).isEqualTo(Arrays.asList(3, 4, 5, 6));
    }

    @Test
    public void peekForSpying() throws Exception {
        //it could also named as onElementRetrieved
        final Consumer<String> sideEffectForDebugging = element -> System.out.println(element);
        Demo.newMutableList().stream().peek(sideEffectForDebugging);
        //with peek you can make the lazyness of a stream visible
    }
}
