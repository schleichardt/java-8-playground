package java8;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class P_1_5_ConstructorReferences {
    /*
    like method references, but method name is "new"
     */

    @Test
    public void paremeterlessConstructorReference() throws Exception {
        final Runnable task = String::new;// :-D
    }

    @Test
    public void oneParameterConstructorReference() throws Exception {
        final List<Something> somethings = Lists.newArrayList("foo", "bar", "baz").stream()
                .map(Something::new).collect(Collectors.toList());
    }

    static class Something {
        private final String s;

        Something(String s) {
            this.s = s;
        }
    }

    @Test
    public void arrayConstructorForStreamApi() throws Exception {
        final Something[] somethings = Lists.newArrayList("foo", "bar", "baz").stream()
                .map(Something::new).toArray(Something[]::new);
    }
}
