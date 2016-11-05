package linenux.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;
import java.util.function.Function;

import org.junit.Test;

//@@author A0144915A
public class EitherTest {
    @Test
    public void getLeft_eitherWithLeftValue_expectedValueReturned() {
        Either<Integer, String> either = Either.left(1);
        assertTrue(either.isLeft());
        assertFalse(either.isRight());
        assertEquals(1, (int)either.getLeft());
    }

    @Test
    public void getRight_eitherWithRightValue_expectedValueReturned() {
        Either<Integer, String> either = Either.right("hello");
        assertFalse(either.isLeft());
        assertTrue(either.isRight());
        assertEquals("hello", either.getRight());
    }

    @Test(expected=NoSuchElementException.class)
    public void getLeft_noLeftValue_noSuchElementExceptionThrown() {
        Either.right("hello").getLeft();
    }

    @Test(expected=NoSuchElementException.class)
    public void getRight_noRightValue_noSuchElementExceptionThrown() {
        Either.left("hello").getRight();
    }

    @Test
    public void bind_bindLeftValueToLeft_leftValueModifiedAndBoundToLeft() {
        Either<Integer, Integer> either = Either.left(1);
        Either<Integer, Integer> result = either.bind(i -> Either.left(i + 1));

        assertTrue(result.isLeft());
        assertEquals(2, (int)result.getLeft());
    }

    @Test
    public void bind_bindLeftValueToRight_leftValueModifiedAndBoundToRight() {
        Either<Integer, Integer> either = Either.left(1);
        Either<Integer, Integer> result = either.bind(i -> Either.right(i + 1));

        assertTrue(result.isRight());
        assertEquals(2, (int)result.getRight());
    }

    private class MockLambda implements Function<Integer, Either<Integer, Integer>> {
        private boolean executed = false;

        @Override
        public Either<Integer, Integer> apply(Integer integer) {
            this.executed = true;
            return Either.left(integer + 1);
        }

        public boolean isExecuted() {
            return this.executed;
        }
    }

    @Test
    public void bind_bindRightValueToRight_rightValueModifiedAndBoundToRight() {
        MockLambda fn = new MockLambda();
        Either<Integer, Integer> either = Either.right(1);
        Either<Integer, Integer> result = either.bind(fn);

        assertFalse(fn.isExecuted());
        assertTrue(result.isRight());
        assertEquals(1, (int)result.getRight());
    }
}
