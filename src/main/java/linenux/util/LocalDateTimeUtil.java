package linenux.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@@author A0144915A
/**
 * Static functions for LocalDateTime.
 */
public class LocalDateTimeUtil {
    /**
     * Return the earlier of two instances of {@code LocalDateTime}.
     * @param a The first {@code LocalDateTime}.
     * @param b The second {@code LocalDateTime}.
     * @return The earlier of {@code a} and {@code b}.
     */
  //@@author A0144915A
    public static LocalDateTime min(LocalDateTime a, LocalDateTime b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    /**
     * Returns the later of two instances of {@code LocalDateTime}.
     * @param a The first {@code LocalDateTime}.
     * @param b The second {@code LocalDateTime}.
     * @return The later of {@code a} and {@code b}.
     */
  //@@author A0144915A
    public static LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    /**
     * Formats a LocalDateTime of format "dd MMM yyyy h.mma".
     */
    //@@author A0135788M
    public static String toString(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy h.mma");
        return time.format(formatter);
    }

}
