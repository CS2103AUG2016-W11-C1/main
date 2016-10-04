package linenux.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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
         * @param fn The filter predicate.
         * @return A new {@code ChainableArrayListUtil} wrapping the new {@code ArrayList}.
         */
        public ChainableArrayListUtil<T> filter(Predicate<T> fn) {
            return new ChainableArrayListUtil<T>(ArrayListUtil.filter(fn, this.list));
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
        return list.stream().map(fn).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
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
     * Make an {@code ArrayList} out of an array.
     * @param arr The input array.
     * @param <T> The type of the array.
     * @return An {@code ArrayList} containing the same elements as {@code arr}.
     */
    public static <T> ArrayList<T> fromArray(T[] arr) {
        return new ArrayList<T>(Arrays.asList(arr));
    }
}
