package java8.ch1lambdaexpressions;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class P_1_8_StaticMethodInInterfaces {
    /*
    Interface can have static method implementations.
    It is like in Scala to have a trait for the childs to
    implement stuff and a companion object for factory methods.

    In Java 8 this can reduce the number of companion classes.

    It is sometimes not a good idea that a base class knows it's children.
     */

    static interface Category {
        String getName();

        public static Category create(final String name) {
            return new CategoryImpl(name);
        }
    }

    static class CategoryImpl implements Category {
        final String name;

        CategoryImpl(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Test
    public void testStaticFactoryMethod() throws Exception {
        assertThat(Category.create("foo").getName()).isEqualTo("foo");
    }
}
