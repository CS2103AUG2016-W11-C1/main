package linenux.util;

import java.util.NoSuchElementException;

/**
 * A data structure inspired by the FP world. Can be used to represent an operation that can have two possible
 * outcomes. For example, when parsing something, the output is either the result, or an error.
 *
 * Instances of {@code Either} are immutable. The left and right values are wrapped by {@code Optional}.
 * Exactly one of them must be empty and the other present. To enforce this restriction, {@code Either} must
 * be built using the static factory methods.
 */
public class Either<L, R> {
    /**
     * A custom implementation of the {@code Optional} class. The default Java implementation does not
     * allow {@code null}, which is a good thing, but is not suitable in this context since an
     * {@code Either} should be able to store {@code null} values too.
     * @param <T> Any type.
     */
    private static class NullableOptional<T> {
        public static <T> NullableOptional<T> empty() {
            NullableOptional<T> output = new NullableOptional<>();
            output.isPresent = false;
            return output;
        }

        public static <T> NullableOptional<T> of(T value) {
            NullableOptional<T> output = new NullableOptional<>();
            output.isPresent = true;
            output.value = value;
            return output;
        }

        private boolean isPresent;
        private T value;

        public boolean isPresent() {
            return this.isPresent;
        }

        public T get() {
            if (!this.isPresent()) {
                throw new NoSuchElementException();
            }

            return this.value;
        }
    }

    private NullableOptional<L> left;
    private NullableOptional<R> right;

    /**
     * Make an {@code Either} with a left value.
     * @param <L> The left type.
     * @param <R> The right type.
     * @return An instance of {@code Either} with a left value.
     */
    public static <L, R> Either<L, R> left(L left) {
        return new Either<>(NullableOptional.of(left), NullableOptional.empty());
    }

    /**
     * Make an {@code Either} with a right value.
     * @param right The right value.
     * @param <L> The left type.
     * @param <R> The right type.
     * @return An instance of {@code Either} with a right value.
     */
    public static <L, R> Either<L, R> right(R right) {
        return new Either<>(NullableOptional.empty(), NullableOptional.of(right));
    }

    /**
     * Construct an {@code Either}. Exactly one of {@code left} and {@code right} must be empty.
     * @param left The left optional.
     * @param right The right optional.
     */
    private Either(NullableOptional<L> left, NullableOptional<R> right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns true if and only if the left value is not empty.
     * @return True if left is not empty.
     */
    public boolean isLeft() {
        return this.left.isPresent();
    }

    /**
     * Return true if and only if the right value is not empty.
     * @return True if right is not empty.
     */
    public boolean isRight() {
        return this.right.isPresent();
    }

    /**
     * Return the left value.
     * @return The left value. If the left value is empty, {@code NoSuchElementException} will be thrown.
     */
    public L getLeft() throws NoSuchElementException {
        return this.left.get();
    }

    /**
     * Return the right value.
     * @return The right value. If the right value is empty, {@code NoSuchElementException} will be thrown.
     */
    public R getRight() {
        return this.right.get();
    }
}
