package java8.ch2streamapi;

import base.Demo;
import org.junit.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;


public class P_2_6_SimpleReductions {
    /*
    after a terminal operation a stream is not usable anymore
     */

    @Test
    public void count() throws Exception {
        final long count = Demo.newMutableList().stream().filter(s -> s.startsWith("b")).count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void minAndMax() throws Exception {
        final Optional<Integer> max = Stream.of(1, 4, 5, 8, 2).max(Integer::compare);
        assertThat(max).isEqualTo(Optional.of(8));
    }

    /*
    It is not for searching, it is more like in Scala list.headOption.
    It is mostly used with filters.
     */
    @Test
    public void findFirst() throws Exception {
        final Stream<String> haystack = Stream.of("hay", "needle", "hay", "needle", "hay");
        final Optional<String> needle = haystack.filter(s -> s.equals("needle")).findFirst();
        assertThat(needle).isEqualTo(Optional.of("needle"));
        assertThat(Stream.<String>of().findFirst()).isEqualTo(Optional.empty());
    }

    /*
    findAny if you want just one value and it is not necessary the first element, mostly important in parallel streams
     */
    @Test
    public void findAny() throws Exception {
        final Optional<Double> any = Stream.generate(() -> Math.random()).parallel().filter(x -> x > 0.5).findAny();

    }
}
