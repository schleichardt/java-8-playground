package futures;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class F_1_OldJavaFutures {
    //the important parts of java.util.concurrent.Future without cancellation
    public interface Future<V> {
        /*
        block indefinitely to retrieve result
         */
        V get();

        /*
        case value present before timeout => value
        case timeout => null
         */
        V get(long timeout, TimeUnit unit);

        /*
        method, so you can use polling to check if value is present
        => need more code to maintain this future
         */
        boolean isDone();
    }

    /*
    this future has several flaws:
        - maintaining requires more infrastructure
        - composition of futures not easy possible
        - conversion to other future implementations
     */

    /*
    java.util.concurrent.FutureTask<V>
    (it is a concrete class!), implementation code imagined/simplified/without synchronization

    it implements Runnable, it is supposed to maintained by an Executor
     */
    abstract class FutureTask<V> implements Future<V>, Runnable {
        private boolean isDone = false;
        private V value = null;
        private ExecutionException error = null;
        private final Callable<V> callable;

        public FutureTask(Callable<V> callable){
            this.callable = callable;
        }

        @Override
        public void run() {
            try {
                final V value = callable.call();
                set(value);
            } catch (final Throwable t) {
                setException(t);
            }
        }

        protected void set(V v) {
            if (!isDone()) {
                value = v;
                isDone = true;
            } else {
                throw new IllegalStateException("result already set");
            }
        }

        protected void setException(Throwable t) {
            if (!isDone()) {
                error = new ExecutionException(t);//it is always ExecutionException, but what failed is in cause
                isDone = true;
            } else {
                throw new IllegalStateException("result already set");
            }
        }

        @Override
        public boolean isDone() {
            return isDone;
        }
    }



    /*
        com.google.common.util.concurrent.ListenableFuture
        or
        com.ning.http.client.ListenableFuture
     */
    public interface ListenableFuture<V> extends Future<V> {
        /*
        call listener if the future completes successfully
         */
        void addListener(Runnable listener, Executor executor);
    }

    /*
    backwarts compatible, makes composition possible,
    since runnable can fill/call set on another future
     */
}