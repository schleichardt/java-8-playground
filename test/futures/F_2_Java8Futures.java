package futures;

import org.junit.Test;
import play.libs.F;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.*;
import java.util.stream.Collectors;

import play.libs.ws.*;

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

        final F.Promise<String> promise = promise();
        final F.Callback<String> callback = s -> System.out.println(s);
        promise.onRedeem(callback);//returns void
    }


    /*

    A --> B

     */
    @Test
    public void startAnotherFutureWhenThisOneBecomesAvailable() throws Exception {
        final CompletableFuture<String> future = future();
        final Function<String, CompletionStage<HttpResponse>> function = s -> futureWebServiceCall(s);//uses the result of the future
        final CompletableFuture<HttpResponse> futureResult = future.thenCompose(function);

        final F.Promise<String> promise = promise();
        final F.Function<String, F.Promise<WSResponse>> promiseFunction = s -> WS.url(s).get();//the get is not a future get, but a HTTP GET
        final F.Promise<WSResponse> promiseResult = promise.flatMap(promiseFunction);
    }

    /*
    A or already started B as fallback, if A fails
    A -->   or } A or B is winner
    B -->      }
     */
    @Test
    public void parallelOr() throws Exception {
        final CompletableFuture<String> future = future();
        final CompletableFuture<String> other = otherFuture();
        final CompletableFuture<String> winnerFuture = future.applyToEither(other, Function.identity());

        //the second parameter can be used like in thenApply
        final CompletableFuture<Integer> winnerResponseFuture = future.applyToEither(other, s -> s.length());

        final F.Promise<String> promise = promise();
        final F.Promise<String> otherPromise = otherPromise();
        final F.Promise<String> winnerPromise = promise.fallbackTo(otherPromise);
    }

     /*
    A -->   and } both A and B are required, A and B can be of different types
    B -->       }
     */
    @Test
    public void parallelAnd() throws Exception {
        final CompletableFuture<String> future = future();
        final CompletableFuture<Integer> other = intFuture();
        final CompletableFuture<String> combined = future.thenCombine(other, (String a, Integer b) -> a + b);

        final F.Promise<String> promise = promise();
        final F.Promise<Integer> intPromise = intPromise();
        final F.Promise<F.Tuple<String, Integer>> tuplePromise = promise.zip(intPromise);
        final F.Promise<String> combinedPromise = tuplePromise.map(tuple -> tuple._1 + tuple._2);
    }

    /*
    List<Future<A>> to Future<List<A>>, useful for 3 or more futures to combine
     */
    @Test
    public void listMapping() throws Exception {
        final CompletableFuture<String> future = future();
        final CompletableFuture<String> otherFuture = otherFuture();
        final List<CompletableFuture<String>> listOfFutures = Arrays.asList(future, otherFuture);
        final CompletableFuture<Void> markerIfAllDone = CompletableFuture.allOf(future, otherFuture);//fail, not usable with list and problem of generic array creation
        //see  sequence(List<CompletableFuture<T>> futures)

        final F.Promise<String> promise = promise();
        final F.Promise<String> otherPromise = otherPromise();
        final List<F.Promise<String>> promiseList = Arrays.asList(promise, otherPromise);
        final F.Promise<List<String>> listPromise = F.Promise.sequence(promiseList);
    }

    //from http://www.nurkiewicz.com/2013/05/java-8-completablefuture-in-action.html
    @SuppressWarnings("unchecked")
    private static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v ->
                        futures.stream().
                                map(future -> future.join()).//it is like get()
                                collect(Collectors.<T>toList())
        );
    }

    @Test
    public void recoverWithoutNewFuture() {
        final CompletableFuture<String> future = future();
        final CompletableFuture<String> hardenedFuture = future.exceptionally(t -> {
            if (t instanceof FooBarException) {
                return "";
            } else {
                throw new RuntimeException(t);//DANGER can't throw Throwable, not declared
            }
        });

        final F.Promise<String> promise = promise();
        final F.Function<Throwable, String> function = (Throwable t) -> { //F.Function declared throws Throwable
            if (t instanceof FooBarException) {
                return "";//return a default value
            } else {
                throw t;//error unexpected, so throw it again
            }
        };
        final F.Promise<String> hardenedPromise = promise.recover(function);
    }

    @Test
    public void recoverWithProducingNewFuture() throws Exception {
        //there is no default way, but a workaround
        final CompletableFuture<String> hardenedFuture = new CompletableFuture<>();//this constructor is used for event-driven computation
        final CompletableFuture<String> future = future();
        final BiConsumer<String, Throwable> action = (String s, Throwable t) -> {
            if (s != null) {
                hardenedFuture.complete(s);
            } else if(t instanceof FooBarException) {//only use alternative for specific errors
                final CompletableFuture<String> alternative = otherFuture();
                alternative.whenComplete((alternativeValue, alternativeError) -> {
                    if (alternativeValue != null) {
                        hardenedFuture.complete(alternativeValue);
                    } else {
                        hardenedFuture.completeExceptionally(alternativeError);
                    }
                });
            } else {
                hardenedFuture.completeExceptionally(t);//with this style you can work with the Throwable
            }
        };
        future.whenComplete(action);



        final F.Promise<String> promise = promise();
        final F.Function<Throwable, F.Promise<String>> function = t -> {
            if(t instanceof FooBarException) {
                final F.Promise<String> otherPromise = otherPromise();
                return otherPromise;
            } else {
                throw t;
            }
        };
        final F.Promise<String> hardenedPromise = promise.recoverWith(function);
    }

    //best practices like separating future and domain code
    //thenAccept vs. thenAcceptAsync
    //http://blog.krecan.net/2013/12/25/completablefutures-why-to-use-async-methods/
    //without async the mapped task may use the same thread pool/thread as the previous/thiw CompletableFuture one, with async it is added to the default thread pool or configured one
    //-> important for futures running in parallel
    //without is like use previous settings, with async: amy new rules

    //check all methods on CompletableFuture and F.Promise the concept is already present

    //dummy
    private CompletableFuture<Integer> intFuture() {
        final Supplier<Integer> supplier = () -> 1;
        return CompletableFuture.supplyAsync(supplier);
    }

    private CompletableFuture<String> future() {
        final Supplier<String> supplier = () -> expensiveOperation();
        return CompletableFuture.supplyAsync(supplier);
    }

    private CompletableFuture<String> otherFuture() {
        return future();
    }

    private static class HttpResponse {

    }

    private static class FooBarException extends RuntimeException {
        private static final long serialVersionUID = 2L;
    }

    //dummy impl
    private CompletableFuture<HttpResponse> futureWebServiceCall(final String arg) {
        final Supplier<HttpResponse> supplier = () -> new HttpResponse();
        return CompletableFuture.supplyAsync(supplier);
    }

    private F.Promise<Integer> intPromise() {
        return promise().map(String::length);
    }

    private F.Promise<String> otherPromise() {
        return promise();
    }

    private F.Promise<String> promise() {
        final F.Function0<String> function = () -> expensiveOperation();
        return F.Promise.promise(function);
    }

    private String expensiveOperation() {
        return "a" + "b";
    }
//
//    ////////////////////////////////
//    final BiFunction<String, Throwable, String> recoverFunction = (String valueOrNull, Throwable throwableOrNull) -> {
//        final String result;
//        if(valueOrNull != null) {
//            result = valueOrNull;
//        } else if (throwableOrNull instanceof FooBarException) {
//            result = "";
//        } else {
//            throw new RuntimeException(throwableOrNull);//DANGER can't throw Throwable since it is checked
//        }
//        return result;
//    };
//    final CompletableFuture<String> hardenedFuture = future.handle(recoverFunction);
//    ////////////////////
}
