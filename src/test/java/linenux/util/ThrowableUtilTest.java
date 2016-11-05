package linenux.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

//@@author A0144915A
public class ThrowableUtilTest {
    @Test
    public void testGetStackTrace() {
        Throwable throwable = new Throwable("hello");
        StackTraceElement line1 = new StackTraceElement("this class", "this method", "this file", 1);
        StackTraceElement line2 = new StackTraceElement("that class", "that method", "that file", 2);
        throwable.setStackTrace(new StackTraceElement[] {line1, line2});
        String trace = ThrowableUtil.getStackTrace(throwable);
        String expectedPattern = "^java\\.lang\\.Throwable: hello\\r?\\n" +
                "\\tat this class\\.this method\\(this file:1\\)\\r?\\n" +
                "\\tat that class\\.that method\\(that file:2\\)\\r?\\n$";
        assertTrue(trace.matches(expectedPattern));
    }
}
