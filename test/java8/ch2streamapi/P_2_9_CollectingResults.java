package java8.ch2streamapi;

import base.Demo;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

public class P_2_9_CollectingResults {

    @Test
    public void iterator() throws Exception {
        final Iterator<String> iterator =
                Demo.newStringList().stream().iterator();
    }

    @Test
    public void array() throws Exception {
        //useless
        final Object[] objects = Demo.newStringList().stream().toArray();

        final String[] words = Demo.newStringList().stream().toArray(String[]::new);
    }

    @Test
    public void collectForDataContainers() throws Exception {
        final Supplier<HashSet<String>> supplier = HashSet::new;//creates a new MUTABLE result container
        final BiConsumer<HashSet<String>, String> accumulator = HashSet::add;//adds an element to the container
        final BiConsumer<HashSet<String>, HashSet<String>> combiner = HashSet::addAll;//merges two containers (partial results)
        final Set<String> reduced = Demo.newStringList().stream().collect(supplier, accumulator, combiner);
        assertThat(reduced).isEqualTo(new HashSet<>(Demo.newStringList()));
    }

    @Test
    public void Collector() throws Exception {
        final Supplier<HashSet<String>> supplier = HashSet::new;
        final BiConsumer<HashSet<String>, String> accumulator = HashSet::add;
        final BinaryOperator<HashSet<String>> combiner = (x, y) -> {
            x.addAll(y);
            return x;
        };
        final Function<HashSet<String>, HashSet<String>> finisher = Function.identity();//converts accumulator to result
        //=> mutable accumulator, but result can be immutable, but accumulator can be result

        final Set<String> reduced = Demo.newStringList().stream().
                collect(Collector.<String, HashSet<String>, HashSet<String>>of(supplier, accumulator, combiner, finisher));
        assertThat(reduced).isEqualTo(new HashSet<>(Demo.newStringList()));
    }

    @Test
    public void predefinedCollectors() throws Exception {
        final List<String> reduced = Demo.newStringList().stream().collect(Collectors.toList());
        /*
        averagingLong, toMap, toSet

         */
        assertThat(reduced).isEqualTo(Demo.newStringList());
        final Supplier<TreeSet<String>> supplier = TreeSet::new;
        final Set<String> reduced2 = Demo.newStringList().stream().collect(Collectors.toCollection(supplier));
        assertThat(reduced2).isEqualTo(new HashSet<>(Demo.newStringList()));

        final String asString = Demo.newStringList().stream().collect(Collectors.joining(", "));
        assertThat(asString).isEqualTo("foo, bar, baz");

        //use map to debug all streams
        final String numbersAsString = Stream.of(1, 2, 3).map(Object::toString).collect(Collectors.joining(", "));
        assertThat(numbersAsString).isEqualTo("1, 2, 3");

        //there are some more for statistics!
    }

    /*
    use forEach, for example to save it to a database or send it anywhere

    after foreach stream is not usable anymore, use peek (non terminal) to provide hooks
     */
    @Test
    public void sideEffects() throws Exception {
        //does not care about ordering in parallel streams!
        Stream.of(1, 2, 3).forEach(System.out::println);

        //if this stream is parallel, order of elements is maintained
        //so in doubt use this method
        Stream.of(1, 2, 3).forEachOrdered(System.out::println);
    }
}
