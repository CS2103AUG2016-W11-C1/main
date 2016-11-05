package linenux.util;

import java.io.PrintWriter;
import java.io.StringWriter;

//@@author A0144915A
public class ThrowableUtil {
    /**
     * Display the stack trace of {@code throwable} as a {@code String}.
     * @param throwable The {@code Throwable} to display.
     * @return The {@code String} representing the stack trace.
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
