package futures;

import org.junit.Test;
import play.libs.F;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class F_2_Java8Futures {

    /*
        http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
        interface java.util.concurrent.CompletionStage<T>
        => like scala.concurrent.Future or play.libs.F.Promise<A> API to work with and combine possibly later computed values

        http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
        java.util.concurrent.CompletableFuture<T> implements both Future<T> and CompletionStage<T>
     */

/*
important: most methods have another version with the thread pool as argument, it is often wise to use this variant,
for simplicity it is omitted.
 */


    @Test
    public void creatingFromComputation() throws Exception {
        final Supplier<String> supplier = () -> expensiveOperation();
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(supplier);//no blocking in this thread

        final F.Function0<String> function = () -> expensiveOperation();
        final F.Promise<String> promise = F.Promise.promise(function);//no blocking in this thread
    }

    @Test
    public void workWithOneResult() throws Exception {
        final CompletableFuture<String> future = future();
        final Function<String, Integer> function = s -> s.length();
        final CompletableFuture<Integer> length = future.thenApply(function);//a new future

        final F.Promise<String> promise = promise();
        final F.Function<String, Integer> playFunction = s -> s.length();
        final F.Promise<Integer> integerPromise = promise.map(playFunction);
    }

    @Test
    public void triggerSideEffects() throws Exception {
        final CompletableFuture<String> future = future();
        final Consumer<String> action = s -> System.out.println(s);//side effect, could be save to database or return HTTP response
        final CompletableFuture<Void> completableFuture = future.thenAccept(action);

    }

    //best practices like separating future and domain code
    //thenAccept vs. thenAcceptAsync
    //http://blog.krecan.net/2013/12/25/completablefutures-why-to-use-async-methods/
    //without async the mapped task may use the same thread pool/thread as the initial one, with async it is added to the default thread pool or configured one
    //-> important for futures running in parallel

    private CompletableFuture<String> future() {
        final Supplier<String> supplier = () -> expensiveOperation();
        return CompletableFuture.supplyAsync(supplier);
    }

    private F.Promise<String> promise() {
        final F.Function0<String> function = () -> expensiveOperation();
        return F.Promise.promise(function);
    }

    private String expensiveOperation() {
        return "a" + "b";
    }
}
