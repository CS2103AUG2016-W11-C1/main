package linenux.helpers;

import static org.junit.Assert.assertEquals;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * More assertions to make life (testing) easier.
 *
 * Most of these assertions can be overloaded to deal with different data types.
 */
public class Assert {
    /**
     * Assert that an integer value (supplied by {@code supplier}) does not change after performing
     * the {@code action}.
     * @param supplier Supplies the integer value to test.
     * @param action The action to perform.
     * @param <R> The return type of {@code action}.
     * @return The return value of {@code action}. This is useful to test the return value of {@code action}.
     */
    public static <R> R assertNoChange(IntSupplier supplier, Supplier<R> action) {
        return assertChangeBy(supplier, 0, action);
    }

    /**
     * Assert that an integer value (supplied by {@code supplier}) changes by {@code delta} after performing
     * the {@code action}. In other words, the final value should be the initial value + {@code delta}.
     * @param supplier Supplies the integer value to test.
     * @param delta The expected change.
     * @param action The action to perform.
     * @param <R> The return type of {@code action}.
     * @return The return value of {@code action}.
     */
    public static <R> R assertChangeBy(IntSupplier supplier, int delta, Supplier<R> action) {
        int before = supplier.getAsInt();
        R output = action.get();
        int after = supplier.getAsInt();
        assertEquals(before + delta, after);
        return output;
    }
}
