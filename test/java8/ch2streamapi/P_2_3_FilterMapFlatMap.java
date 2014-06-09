package java8.ch2streamapi;

import base.Demo;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class P_2_3_FilterMapFlatMap {

    @Test
    public void StreamsCanNotBeReused() throws Exception {

        final Stream<String> stream = Demo.newStringList().stream();
        final Stream<String> upperCased = stream.map(String::toUpperCase);
        try {
            final Stream<Integer> lengths = stream.map(String::length);
            fail("second call on stream should fail");
        } catch (final IllegalStateException e) {
            System.out.println("you can't reuse streams!");
        }
    }

    @Test
    public void filter() throws Exception {
        final Predicate<String> f = w -> w.startsWith("f");//T -> boolean
        Demo.newStringList().stream().filter(f);
    }

    @Test
    public void map() throws Exception {
        //stream not of String, but of char
        final Stream<Character> stream = Demo.newStringList().stream().map(s -> s.charAt(0));
    }

    @Test
    public void flatMap() throws Exception {
        final Stream<char[]> charArrayStream = Demo.newStringList().stream().map(s -> s.toCharArray());
//        final Stream<Stream<Character>> stream = charArrayStream.map(this::charStream);//maybe not that useful
        final Stream<Character> characterStream = charArrayStream.flatMap(this::charStream);
        assertThat(characterStream.collect(Collectors.toList())).isEqualTo(Lists.newArrayList('f', 'o', 'o', 'b', 'a', 'r', 'b', 'a', 'z'));
    }

    /*
    JDK lacks function to create stream for char
    int, long, double, T are present
     */
    public Stream<Character> charStream(final char[] array) {
        final List<Character> list = new ArrayList<>();
        for (final char c : array) {
            list.add(c);
        }
        return list.stream();
    }
}
