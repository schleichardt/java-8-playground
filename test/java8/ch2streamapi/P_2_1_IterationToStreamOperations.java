package java8.ch2streamapi;

import base.Demo;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

public class P_2_1_IterationToStreamOperations {
    /*
    Processing Iterable/Collection

    stream does not store its elements, may uses internal helper collections

    streams don't mutate their source, returns a stream with results

    streams can be lazy, nothing is executed until result is needed
    => if you have a long list and take only the first 7 elements
      of the stream, the filtering and transforming needs only be
      done for the first 7 elements

      infinite streams are possible

      using parrallelStream() instead of stream() may parallelizes work
     */

    @Test
    public void pipeline() throws Exception {
        final Stream<String> stream = Demo.newMutableList().stream();//1. create a stream
        final Stream<String> mappedStream = stream.map(String::toUpperCase);//2. specify intermediate operations (can be more than one)
        final List<String> result = mappedStream.collect(Collectors.toList());//3. terminal operation to produce a result
        assertThat(result).isEqualTo(Lists.newArrayList("FOO", "BAR", "BAZ"));
    }
}
