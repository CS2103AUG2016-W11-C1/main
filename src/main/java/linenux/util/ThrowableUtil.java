package linenux.util;

import java.io.PrintWriter;
import java.io.StringWriter;

//@@author A0144915A
public class ThrowableUtil {
    public static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
