package java8;

import base.Demo;
import org.junit.Test;
import play.libs.F;

import java.util.Comparator;

import static org.fest.assertions.Assertions.assertThat;

public class P_1_4_MethodReferences {

    /*

    It is most likely
       object::methodName or instance::methodName
     */

    @Test
    public void instanceMethod() {
        final String standard = Demo.promiseSource().map(s -> s.toUpperCase()).get(100);
        final String methodReference = Demo.promiseSource().map(String::toUpperCase).get(100);
        assertThat(standard).isEqualTo(methodReference);
    }

    @Test
    public void instanceMethod2() {
        Demo.promiseSource().onRedeem(s -> System.out.println(s));
        Demo.promiseSource().onRedeem(System.out::println);
    }

    @Test
    public void thisReference() throws Exception {
        final String actual = Demo.promiseSource().map(this::triple).get(100);
        assertThat(actual).isEqualTo("hellohellohello");
    }

    public String triple(final String s) {
        return s + s + s;
    }

    @Test
    public void superReference() throws Exception {
        final String actual = new Child(Demo.promiseSource()).mapped().get(100);
        assertThat(actual).isEqualTo("Parent hello");
    }

    static class Parent {
        protected final F.Promise<String> promise;

        Parent(F.Promise<String> promise) {
            this.promise = promise;
        }

        public String hello(final String name) {
            return "Parent " + name;
        }

        F.Promise<String> mapped() {
            return promise.map(this::hello);
        }
    }

    static class Child extends Parent {

        Child(F.Promise<String> promise) {
            super(promise);
        }

        @Override
        F.Promise<String> mapped() {
            return promise.map(super::hello);
        }

        @Override
        public String hello(final String name) {
            return "Child " + name;
        }
    }

    @Test
    public void thisOfEnclosingClass() throws Exception {
        final String actual = new EnclosingChild(Demo.promiseSource()).mapped().get(100);
        assertThat(actual).isEqualTo("hellohellohello");
    }

    class EnclosingChild extends Child {
        EnclosingChild(F.Promise<String> promise) {
            super(promise);
        }

        @Override
        F.Promise<String> mapped() {
            return promise.map(P_1_4_MethodReferences.this::triple);
        }
    }

    @Test
    public void inForeach() throws Exception {
        //method for side effects
        //forEach has an interface default implementation in Iterable
        Demo.newMutableList().forEach(System.out::println);
    }

    @Test
    public void helpingCreatingComparators() throws Exception {
        final Comparator<String> comparator1 = Comparator.comparing(String::length);
        final Comparator<String> comparator2 = (a, b) -> Integer.compare(a.length(), b.length());
    }
}
