package linenux.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

//@@author A0144915A
public class ThrowableUtilTest {
    @Test
    public void testGetStackTrace() {
        Throwable throwable = new Throwable("hello");
        StackTraceElement line1 = new StackTraceElement("this class", "this method", "this file", 1);
        StackTraceElement line2 = new StackTraceElement("that class", "that method", "that file", 2);
        throwable.setStackTrace(new StackTraceElement[] {line1, line2});
        String trace = ThrowableUtil.getStackTrace(throwable);
        String expectedTrace = "java.lang.Throwable: hello\n" +
                "\tat this class.this method(this file:1)\n" +
                "\tat that class.that method(that file:2)\n";
        assertEquals(trace, expectedTrace);
    }
}
