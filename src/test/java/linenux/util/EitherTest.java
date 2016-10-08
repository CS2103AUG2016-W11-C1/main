package linenux.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by yihangho on 10/8/16.
 */
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
}
