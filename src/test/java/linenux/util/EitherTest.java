package linenux.util;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//@@author A0144915A
public class EitherTest {
    @Test
    public void testLeft() {
        Either<Integer, String> either = Either.left(1);
        assertTrue(either.isLeft());
        assertFalse(either.isRight());
        assertEquals(1, (int)either.getLeft());
    }

    @Test
    public void testRight() {
        Either<Integer, String> either = Either.right("hello");
        assertFalse(either.isLeft());
        assertTrue(either.isRight());
        assertEquals("hello", either.getRight());
    }

    @Test(expected=NoSuchElementException.class)
    public void testGetLeftShouldThrow() {
        Either.right("hello").getLeft();
    }

    @Test(expected=NoSuchElementException.class)
    public void testGetRightShouldThrow() {
        Either.left("hello").getRight();
    }

    @Test
    public void testIsLeftBindReturnsLeft() {
        Either<Integer, Integer> either = Either.left(1);
        Either<Integer, Integer> result = either.bind(i -> Either.left(i + 1));

        assertTrue(result.isLeft());
        assertEquals(2, (int)result.getLeft());
    }

    @Test
    public void testIsLeftBindReturnsRight() {
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
    public void testIsRightBind() {
        MockLambda fn = new MockLambda();
        Either<Integer, Integer> either = Either.right(1);
        Either<Integer, Integer> result = either.bind(fn);

        assertFalse(fn.isExecuted());
        assertTrue(result.isRight());
        assertEquals(1, (int)result.getRight());
    }
}
