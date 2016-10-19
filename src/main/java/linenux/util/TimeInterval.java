package linenux.util;

import java.time.LocalDateTime;

/**
 * Represents a time interval represented by a pair of {@code LocalDateTime}.
 */
public class TimeInterval {
    private LocalDateTime from, to;

    public TimeInterval(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public LocalDateTime getTo() {
        return this.to;
    }

    /**
     * Check if {@code query} is in this time interval.
     * @param query The {@code LocalDateTime} to check.
     * @return {@code true} if and only if {@code query} is in the current interval.
     */
    public boolean inInterval(LocalDateTime query) {
        return this.from.compareTo(query) <= 0 && query.compareTo(this.to) <= 0;
    }

    /**
     * Check if the current interval is trivial.
     * @return {@code true} if and only if the interval is trivial.
     */
    public boolean isTrivial() {
        return this.from.compareTo(this.to) == 0;
    }
}
