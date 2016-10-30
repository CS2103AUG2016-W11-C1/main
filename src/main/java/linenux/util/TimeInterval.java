package linenux.util;

import java.time.LocalDateTime;

//@@author A0144915A
/**
 * Represents a time interval represented by a pair of {@code LocalDateTime}.
 */
public class TimeInterval {
    private LocalDateTime from, to;

    /**
     * Instantiate an indeterminate {@code TimeInterval}.
     */
    public TimeInterval() {
        this(null, null);
    }

    /**
     * Instantiate a {@code TimeInterval} from the given start and end time.
     * @param from The beginning of the interval.
     * @param to The end of the interval.
     */
    public TimeInterval(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    /**
     * @return The beginning of the interval.
     */
    public LocalDateTime getFrom() {
        return this.from;
    }

    /**
     * @return The end of the interval.
     */
    public LocalDateTime getTo() {
        return this.to;
    }

    /**
     * @param from The new beginning of the interval.
     * @return A new {@code TimeInterval} with the updated beginning.
     */
    public TimeInterval setFrom(LocalDateTime from) {
        return new TimeInterval(from, this.to);
    }

    /**
     * @param to The new end of the interval.
     * @return A new {@code TimeInterval} with the updated ending.
     */
    public TimeInterval setTo(LocalDateTime to) {
        return new TimeInterval(this.from, to);
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
