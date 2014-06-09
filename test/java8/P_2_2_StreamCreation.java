package java8;

import base.Demo;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

public class P_2_2_StreamCreation {
    private final String[] array = {"foo", "bar", "baz"};

    @Test
    public void fromValues() throws Exception {
        //using varargs
        Stream<String> stream = Stream.of("foo", "bar", "baz");
    }

    @Test
    public void fromArray() throws Exception {
        Stream<String> stream = Stream.of(array);
    }

    @Test
    public void fromPartOfArray() throws Exception {
        //0: "foo", 1: "bar", 2: "baz"
        final int from = 1;
        final int endExclusive = 2;
        Stream<String> stream = Arrays.stream(array, from, endExclusive);
        assertThat(stream.toArray(String[]::new)).isEqualTo(new String[]{"bar"});
    }

    @Test
    public void fromSupplierStateless() throws Exception {
        final Supplier<String> supplier = () -> "foo";
        final Stream<String> constantStream = Stream.generate(supplier);
        assertThat(constantStream.limit(4).toArray(String[]::new)).isEqualTo(new String[]{"foo", "foo", "foo", "foo"});
        assertThat(Stream.of("foo").limit(4).toArray(String[]::new)).isEqualTo(new String[]{"foo"});
        // => Stream.generate produces infinite streams. of produces hard coed Streams

       assertThat(newRandomStream()).isNotEqualTo(newRandomStream());
    }

    private Double[] newRandomStream() {
        return Stream.generate(Math::random).limit(2).toArray(Double[]::new);
    }

    /*
    UnaryOperator<T> is Function<T,T>
     */
    @Test
    public void fromUnaryOperator() throws Exception {
        final int seed = 0;
        final UnaryOperator<Integer> unaryOperator = n -> n + 1;
        final Stream<Integer> stream = Stream.iterate(seed, unaryOperator);
        assertThat(stream.limit(4).toArray(Integer[]::new)).isEqualTo(new Integer[]{0, 1, 2, 3});
    }

    @Test
    public void fromRegex() throws Exception {
        final Stream<String> stream = Pattern.compile("\\D").splitAsStream("input string");
    }

    @Test
    public void fromFileLines() throws Exception {
        final Stream<String> lines = Files.lines(Paths.get("./README.md"));
    }

    @Test
    public void createStreamInTryBlock() throws Exception {
        try (final Stream<String> elements = Demo.newMutableList().stream()) {
            final long count = elements.count();
        }

    }
}
