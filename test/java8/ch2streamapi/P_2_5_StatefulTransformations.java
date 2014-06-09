package java8.ch2streamapi;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

public class P_2_5_StatefulTransformations {

    /*
    previous stream transformation were stateless, answer is not dependent of the previous elements

     */

    @Test
    public void distinctEliminatesDuplicates() throws Exception {
        final List<Integer> actual = Stream.of(1, 2, 3, 2, 1).distinct().collect(Collectors.toList());
        assertThat(actual).isEqualTo(Arrays.asList(1, 2, 3));
    }

    @Test
    public void sorted() throws Exception {
        final List<Integer> actual = Stream.of(1, 2, 3, 2, 1).sorted().collect(Collectors.toList());
        assertThat(actual).isEqualTo(Arrays.asList(1, 1, 2, 2, 3));
    }

    /*
    Collection.sort sorts in-place, where the stream sort creates a new stream

     */
    @Test
    public void sortedByComparator() throws Exception {
        final List<Integer> actual = Stream.of(1, 2, 3, 2, 1).sorted((a, b) -> Integer.compare(b, a)).collect(Collectors.toList());
        assertThat(actual).isEqualTo(Arrays.asList(3, 2, 2, 1, 1));
    }
}
