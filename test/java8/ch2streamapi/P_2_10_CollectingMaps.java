package java8.ch2streamapi;

import base.Demo;
import org.junit.Test;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static base.Demo.Person;
import static org.fest.assertions.Assertions.assertThat;

public class P_2_10_CollectingMaps {

    @Test
    public void mapDifferentThings() throws Exception {
        final Map<Integer, String> idToName = Demo.newPersonStream().collect(Collectors.toMap(Person::getId, Person::getName));
        //identity if result is the streamed object itself
        final Map<Integer, Person> idToPerson = Demo.newPersonStream().collect(Collectors.toMap(Person::getId, Function.identity()));
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void duplicateValuesInStream() throws Exception {
        final Map<String, Integer> stringToLength = Stream.of("foo", "bar", "bar", "baz").collect(Collectors.toMap(Function.identity(), s -> s.length()));
    }

    @Test
    public void handleDuplicateValuesInStream() throws Exception {
        final BinaryOperator<Integer> mergeFunction = (oldValue, newValue) -> oldValue;//for example on sets it can be merged
        final Map<String, Integer> stringToLength = Stream.of("foo", "bar", "bar", "baz").
                collect(Collectors.toMap(Function.identity(), s -> s.length(), mergeFunction));
        assertThat(stringToLength.get("bar")).isEqualTo(3);
    }
}
