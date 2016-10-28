package linenux.time.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TodayWithTimeParser implements TimeParser {
    private static final String TODAY_TIME_PATTERN = "(?i)^today (1[012]|[1-9]).[0-5][0-9](\\s)?(am|pm)";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d h.mma");

    @Override
    public boolean respondTo(String input) {
        return input.matches(TODAY_TIME_PATTERN);
    }

    @Override
    public LocalDateTime parse(String input) {
        assert input.matches(TODAY_TIME_PATTERN);

        String todayDate = LocalDate.now().toString();
        String time = input.split("\\s+")[1];
        String todayDateTime = todayDate + " " + time;

        return LocalDateTime.parse(todayDateTime.toUpperCase(), formatter);
    }

}
