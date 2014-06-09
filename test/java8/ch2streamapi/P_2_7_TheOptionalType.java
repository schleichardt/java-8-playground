package java8.ch2streamapi;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class P_2_7_TheOptionalType {
    /*
    like Guava Optional or Scala Option

    => sometimes it feels like a list with a most one value
     */

    @Test
    public void creation() throws Exception {
        assertThat(Optional.of("value").get()).isEqualTo("value");

        assertThat(Optional.ofNullable("value").get()).isEqualTo("value");

        assertThat(Optional.ofNullable(null)).isEqualTo(Optional.empty());

        try {
            assertThat(Optional.of(null).get()).isEqualTo("value");
            fail("previous statement should throw exception");
        } catch (final NullPointerException e) {
            System.out.println("use of only if you are really sure it is not null!");
        }
    }

    @Test(expected=NoSuchElementException.class)
    public void getEmpty() throws Exception {
        Optional.empty().get();
    }

    @Test
    public void triggerSideEffects() throws Exception {
        Optional.of(1).ifPresent(System.out::println);

        final List<Integer> list = new ArrayList<>();
        Optional.of(1).ifPresent(e -> list.add(e));
    }

    @Test
    public void map() throws Exception {
        assertThat(Optional.of("foo").map(String::toUpperCase)).isEqualTo(Optional.of("FOO"));
    }

    @Test
    public void orElse() throws Exception {
        final String actual = Optional.of("foo").orElse("default");
        assertThat(actual).isEqualTo("foo");

        assertThat(Optional.empty().orElse("default")).isEqualTo("default");
    }

    @Test
    public void orElseGet() throws Exception {
        final Supplier<Object> function = () -> "" + (200 * 55);
        assertThat(Optional.empty().orElseGet(function)).isEqualTo("11000");
    }

    @Test(expected=IllegalStateException.class)
    public void orElseThrow() throws Exception {
        final Supplier<? extends RuntimeException> supplier = () -> new IllegalStateException("not present");
        Optional.empty().orElseThrow(supplier);
    }

    @Test
    public void flatMap() throws Exception {
        assertThat(Optional.of("foo").flatMap(this::methodReturningOptional)).isEqualTo(Optional.empty());
        assertThat(Optional.of("1").flatMap(this::methodReturningOptional)).isEqualTo(Optional.of(1));

        //map instead
        final Optional<Optional<Integer>> actual = Optional.of("1").map(this::methodReturningOptional);
        final Optional<Optional<Integer>> expected = Optional.of(Optional.of(1));
        assertThat(actual).isEqualTo(expected);

    }

    public Optional<Integer> methodReturningOptional(final String s) {
        Optional<Integer> result;
        try {
            result = Optional.of(Integer.parseInt(s));
        } catch (final NumberFormatException e) {
            result = Optional.empty();
        }
        return result;
    }
}
