package linenux.util;

import java.time.LocalDateTime;

/**
 * Created by yihangho on 10/15/16.
 */
//@@author A0144915A
public class LocalDateTimeUtil {
    /**
     * Return the earlier of two instances of {@code LocalDateTime}.
     * @param a The first {@code LocalDateTime}.
     * @param b The second {@code LocalDateTime}.
     * @return The earlier of {@code a} and {@code b}.
     */
    public static LocalDateTime min(LocalDateTime a, LocalDateTime b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    /**
     * Returns the later of two instances of {@code LocalDateTime}.
     * @param a The first {@code LocalDateTime}.
     * @param b The second {@code LocalDateTime}.
     * @return The later of {@code a} and {@code b}.
     */
    public static LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        return a.compareTo(b) > 0 ? a : b;
    }
}
