package java8;

import com.google.common.collect.Ordering;

import java.util.Comparator;
import java.util.List;

public class P_1_7_DefaultMethods {
    static interface Category {

    }

    static interface CategoryTree {
        List<Category> getRoots();

        /*
        Default methods contain the keyword default.
        Interfaces in Java 8 are more like traits in Scala.
         */
        default List<Category> getRoots(Comparator<Category> sorter) {
            return Ordering.from(sorter).immutableSortedCopy(getRoots());
        }

        /*
        If any body overrides getRoots() he/she has not to worry about the
        sorter method. If the sorter method has not a default implementation
        it would be easier by accident to override getRoots() but not
        update getRoots(Comparator<CategoryTree> sorter).
         */
    }

    /*

    If multiple interfaces are implemented and they contain
    the same method name, the method must be implemented manually.
    But it is possible to reference the default implementation
    by SuperClass.super.methodName().

    It is not possible to provide defaults for Object methods (toString, equals...).

    The class wins if an interface has a default implementation.
     */
}
