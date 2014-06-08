package java8;

public class P_1_3_FunctionalInterfaces {

    /*
    Places where lambdas can be used:
    object of an interface with a single abstract method is expected
    => backwards compatible with Java 7
    => interface with single ABSTRACT method is a functional interface
       since Java 8 interfaces can have implementations

    => Runnable, Comparator, F.Function.X (for example F.Function in F.Promise.map), F.CallbackX

    Lambdas can only be used on functional interfaces.
    Functions are no first class members, not as parameters nor as return values as in Scala:
    trait X {
        def foo(param: Function[A,B): Function[C,D]
    }

    But interfaces can be used as parameters or return values.
    So they are just syntactic sugar.

    http://docs.oracle.com/javase/8/docs/api/java/util/function/package-frame.html
    java.util.function provides a lot of functional interfaces with default methods.
    It looks functional.


    TODO:
    Function
    Consumer
    Predicate
    Supplier
     */
}
