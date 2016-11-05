package linenux.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

//@@author A0144915A
/**
 * A set of utility functions for {@code ArrayList}.
 */
public class ArrayListUtil {
    /**
     * A layer of abstraction on top of ArrayListUtil to allow chainable calls.
     * For example, instead of writing
     *
     * <pre><code>
     * String[] numbers = {"1", "2", "3"};
     * ArrayList&lt;Integer&gt; listOfNumbers = ArrayListUtil.map(Integer.parseInt, ArrayListUtil.fromArray(numbers));
     * </code></pre>
     *
     * We can do
     *
     * <pre><code>
     * String[] numbers = {"1", "2", "3"};
     * ArrayList&lt;Integer&gt; listOfNumbers = new ChainableArrayListUtil&lt;String&gt;(numbers)
     *      .map(Integer.parseInt)
     *      .value();
     * </code></pre>
     *
     * Inspired by Lodash.
     *
     * @param <T> Any type that ArrayList allows.
     */
    public static class ChainableArrayListUtil<T> {
        private ArrayList<T> list;

        /**
         * Make a {@code ChainableArrayListUtil} out of an array.
         * @param arr The array to wrap.
         */
        public ChainableArrayListUtil(T[] arr) {
            this(ArrayListUtil.fromArray(arr));
        }

        /**
         * Make a {@code ChainableArrayListUtil} out of a {@code List}.
         * @param list The {@code list} to wrap.
         */
        public ChainableArrayListUtil(List<T> list) {
            this.list = new ArrayList<T>(list);
        }

        /**
         * @param fn The mapper function.
         * @param <R> Any type that ArrayList accepts.
         * @return A new {@code ChainableArrayListUtil} wrapping the new {@code ArrayList}.
         */
        public <R> ChainableArrayListUtil<R> map(Function<T, R> fn) {
            return new ChainableArrayListUtil<R>(ArrayListUtil.map(fn, this.list));
        }

        /**
         * @param fn The mapper function. The second argument is the index of each element.
         * @param <R> Any type that ArrayList accepts.
         * @return A new {@code ChainableArrayListUtil} wrapping the new {@code ArrayList}.
         */
        public <R> ChainableArrayListUtil<R> mapWithIndex(BiFunction<T, Integer, R> fn) {
            return new ChainableArrayListUtil<>(ArrayListUtil.mapWithIndex(fn, this.list));
        }

        /**
         * @param fn The filter predicate.
         * @return A new {@code ChainableArrayListUtil} wrapping the new {@code ArrayList}.
         */
        public ChainableArrayListUtil<T> filter(Predicate<T> fn) {
            return new ChainableArrayListUtil<T>(ArrayListUtil.filter(fn, this.list));
        }

        /**
         * Sort the {@code ArrayList} by the {@code comparator}.
         * @param comparator The comparator used to determine the weak ordering of the elements.
         * @return A new {@code ChainableArrayListUtil} wrapping the new {@code ArrayList}.
         */
        public ChainableArrayListUtil<T> sort(Comparator<T> comparator) {
            ArrayList<T> copy = new ArrayList<>(this.list);
            Collections.sort(copy, comparator);
            return new ChainableArrayListUtil<>(copy);
        }

        /**
         * Sort the {@code ArrayList} according to the value given by the specified function.
         * @param fn A pure function taking in elements of the array list and returning some {@code Comparable}.
         * @param <R> Any type implementing {@code Comparable}.
         * @return A new {@code ChainableArrayListUtil} wrapping the new {@code ArrayList}.
         */
        public <R extends Comparable<R>> ChainableArrayListUtil<T> sortBy(Function<T, R> fn) {
            return this.sort((a, b) -> fn.apply(a).compareTo(fn.apply(b)));
        }

        /**
         * Performs right fold on the current list. Notice that this method breaks the chain as it does not return
         * another instance of {@code ChainableArrayListUtil}.
         * @param reducer The reducer function.
         * @param initialValue The initial value fed into {@code reducer}.
         * @param <R> The output type.
         * @return The fold result.
         */
        public <R> R foldr(BiFunction<T, R, R> reducer, R initialValue) {
            return ArrayListUtil.foldr(reducer, initialValue, this.list);
        }

        /**
         * A special case of {@code foldr}, that is, when the output is also an {@code ArrayList}. We can wrap the
         * output in a {@code ChainableArrayListUtil}.
         * @param reducer The reducer function.
         * @param initialList The initial value fed into {@code reducer}. In this case, it must be an {@code ArrayList}.
         * @param <R> The type of the output list.
         * @return A {@code ChainableArrayListUtil} wrapping the fold result.
         */
        public <R> ChainableArrayListUtil<R> foldr(BiFunction<T, ArrayList<R>, ArrayList<R>> reducer, ArrayList<R> initialList) {
            ArrayList<R> result = ArrayListUtil.foldr(reducer, initialList, this.list);
            return new ChainableArrayListUtil<>(result);
        }

        /**
         * Reverse the list.
         * @return The reversed list wrapped in {@ChainableArrayListUtil}.
         */
        public ChainableArrayListUtil<T> reverse() {
            ArrayList<T> reversed = ArrayListUtil.reverse(this.list);
            return new ChainableArrayListUtil<>(reversed);
        }

        /**
         * Returns the underlying {@code ArrayList}.
         * @return The underlying {@code ArrayList}.
         */
        public ArrayList<T> value() {
            return this.list;
        }
    }

    /**
     * Transform the input {@code ArrayList} using {@code fn}.
     * @param fn The stateless mapper function.
     * @param list The input {@code ArrayList}.
     * @param <T> The type of the input {@code ArrayList}.
     * @param <R> The type of the output {@code ArrayList}.
     * @return The transformed {@code ArrayList}.
     */
    public static <T, R> ArrayList<R> map(Function<T, R> fn, ArrayList<T> list) {
        return mapWithIndex((x, i) -> fn.apply(x), list);
    }

    /**
     * Transform the input {@code ArrayList} using {@code fn}.
     * @param fn The stateless mapper function. The second argument of {@code fn} is the index.
     * @param list The input {@code ArrayList}.
     * @param <T> The type of the input {@code ArrayList}.
     * @param <R> The type of the output {@code ArrayList}.
     * @return The transformed {@code ArrayList}.
     */
    public static <T, R> ArrayList<R> mapWithIndex(BiFunction<T, Integer, R> fn, ArrayList<T> list) {
        ArrayList<R> output = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            output.add(fn.apply(list.get(i), i));
        }

        return output;
    }

    /**
     * Filter the input {@code ArrayList} based on the output of {@code fn}.
     * @param fn The predicate function.
     * @param list The input {@code ArrayList}.
     * @param <T> The type of the {@code ArrayList}.
     * @return The filtered {@code ArrayList}.
     */
    public static <T> ArrayList<T> filter(Predicate<T> fn, ArrayList<T> list) {
        return list.stream().filter(fn).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Performs right fold on {@code list}.
     * @param reducer The reducer function.
     * @param initialValue The initial value fed to {@code reducer}. If {@code list} is empty, this value is returned.
     * @param list The {@code ArrayList} to fold.
     * @param <T> The type of the {@code ArrayList}.
     * @param <R> The output type.
     * @return The result of the fold.
     */
    public static <T, R> R foldr(BiFunction<T, R, R> reducer, R initialValue, ArrayList<T> list) {
        R output = initialValue;

        for (int i = list.size() - 1; i >= 0; i--) {
            output = reducer.apply(list.get(i), output);
        }

        return output;
    }

    /**
     * Reverse a list.
     * @param list The {@code ArrayList} to reverse.
     * @param <T> The type of the {@code ArrayList}.
     * @return The reversed list.
     */
    public static <T> ArrayList<T> reverse(ArrayList<T> list) {
        return foldr((x, xs) -> {
            xs.add(x);
            return xs;
        }, new ArrayList<T>(), list);
    }

    /**
     * Returns a new list by removing repeated elements in {@code list}.
     *
     * @param list The Input list
     * @return The list with repeated elements removed.
     */
    public static <T> ArrayList<T> unique(ArrayList<T> list) {
        HashSet<T> set = new HashSet<>();
        ArrayList<T> output = new ArrayList<>();

        for (T val : list) {
            if (!set.contains(val)) {
                set.add(val);
                output.add(val);
            }
        }

        return output;
    }

    /**
     * Make an {@code ArrayList} out of an array.
     * @param arr The input array.
     * @param <T> The type of the array.
     * @return An {@code ArrayList} containing the same elements as {@code arr}.
     */
    public static <T> ArrayList<T> fromArray(T[] arr) {
        return new ArrayList<T>(Arrays.asList(arr));
    }

    /**
     * Make an {@code ArrayList} with only one element.
     * @param elem The only element in the array list.
     * @param <T> The type of the element.
     * @return An {@code ArrayList} containing only {@code elem}.
     */
    public static <T> ArrayList<T> fromSingleton(T elem) {
        ArrayList<T> output = new ArrayList<>();
        output.add(elem);
        return output;
    }

    /**
     * Display a list of {@code Object}.
     * @param list The {@code ArrayList} to display.
     * @return The formatted {@code String}.
     */
    public static String display(ArrayList<? extends Object> list) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append(i + 1);
            builder.append(". ");
            builder.append(list.get(i).toString());
            builder.append('\n');
        }

        return builder.toString().trim();
    }
}
