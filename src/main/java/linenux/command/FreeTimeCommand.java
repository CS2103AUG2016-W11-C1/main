package linenux.command;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import linenux.command.parser.FreeTimeArgumentParser;
import linenux.command.result.CommandResult;
import linenux.control.TimeParserManager;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.time.parser.ISODateWithTimeParser;
import linenux.time.parser.StandardDateWithTimeParser;
import linenux.time.parser.TodayWithTimeParser;
import linenux.util.ArrayListUtil;
import linenux.util.Either;
import linenux.util.LocalDateTimeUtil;
import linenux.util.TimeInterval;

/**
 * Created by yihangho on 10/15/16.
 */
//@@author A0144915A
public class FreeTimeCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "freetime";
    private static final String DESCRIPTION = "Find a free time slot.";
    private static final String COMMAND_FORMAT = "freetime [st/START_TIME] et/END_TIME";

    private Schedule schedule;
    private TimeParserManager timeParserManager;
    private Clock clock;
    private FreeTimeArgumentParser argumentParser;

    public FreeTimeCommand(Schedule schedule) {
        this(schedule, Clock.systemDefaultZone());
    }

    public FreeTimeCommand(Schedule schedule, Clock clock) {
        this.schedule = schedule;
        this.timeParserManager = new TimeParserManager(new ISODateWithTimeParser(), new StandardDateWithTimeParser(), new TodayWithTimeParser());
        this.argumentParser = new FreeTimeArgumentParser(this.timeParserManager, clock);
        this.clock = clock;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String argument = extractArgument(userInput);
        Either<TimeInterval, CommandResult> queryInterval = this.argumentParser.parse(argument);

        if (queryInterval.isRight()) {
            return queryInterval.getRight();
        }

        ArrayList<TimeInterval> freetime = getFreeTime(queryInterval.getLeft());

        return makeResult(freetime);
    }

    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }

    private String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("keywords") != null) {
            return matcher.group("keywords");
        } else {
            return "";
        }
    }

    private ArrayList<TimeInterval> getFreeTime(TimeInterval queryInterval) {
        ArrayList<TimeInterval> eventIntervals = eventIntervals(queryInterval, this.schedule.getTaskList());
        ArrayList<TimeInterval> busyIntervals = flattenIntervals(eventIntervals);
        return timeIntervalSubtraction(queryInterval, busyIntervals);
    }

    /**
     * Return the time intervals of all events happening within {@code queryInterval}. It is guaranteed that all
     * intervals are bounded by queryInterval, that is, for all x in output, x intersect queryInterval == x.
     * @param queryInterval The {@code TimeInterval} to bound the query.
     * @param tasks The {@code ArrayList} of tasks.
     * @return An {@code ArrayList} of {@code TimeInterval} for the events.
     */
    private ArrayList<TimeInterval> eventIntervals(TimeInterval queryInterval, ArrayList<Task> tasks) {
        return new ArrayListUtil.ChainableArrayListUtil<>(tasks)
                .filter(Task::isEvent)
                .filter(task -> {
                    boolean endsBefore = task.getEndTime().compareTo(queryInterval.getFrom()) <= 0;
                    boolean startsAfter = task.getStartTime().compareTo(queryInterval.getTo()) >= 0;
                    return !(endsBefore || startsAfter);
                })
                .map(task -> {
                    LocalDateTime startTime = LocalDateTimeUtil.max(queryInterval.getFrom(), task.getStartTime());
                    LocalDateTime endTime = LocalDateTimeUtil.min(queryInterval.getTo(), task.getEndTime());
                    return new TimeInterval(startTime, endTime);
                })
                .value();
    }

    /**
     * Merge time intervals that intersect. The output is ordered.
     * @param input The input time intervals.
     * @return The output time intervals.
     */
    private ArrayList<TimeInterval> flattenIntervals(ArrayList<TimeInterval> input) {
        ArrayList<TimeInterval> sortedIntervals = new ArrayListUtil.ChainableArrayListUtil<>(input)
                .sortBy(TimeInterval::getFrom)
                .value();

        ArrayList<TimeInterval> output = new ArrayList<>();

        if (sortedIntervals.size() == 0) {
            return output;
        }

        TimeInterval interval = new TimeInterval(sortedIntervals.get(0).getFrom(), sortedIntervals.get(0).getTo());
        for (TimeInterval currentInterval: sortedIntervals) {
            if (interval.inInterval(currentInterval.getFrom())) {
                interval = new TimeInterval(interval.getFrom(),
                        LocalDateTimeUtil.max(interval.getTo(), currentInterval.getTo()));
            } else {
                output.add(interval);
                interval = new TimeInterval(currentInterval.getFrom(), currentInterval.getTo());
            }
        }
        output.add(interval);

        return output;
    }

    /**
     * Mathematically, returns {@code query} - {@code intervals}.
     * @param query The superset.
     * @param intervals The smaller subsets.
     * @return Return an {@code ArrayList} of time intervals that are not in {@code intervals} but in {@code query}.
     */
    private ArrayList<TimeInterval> timeIntervalSubtraction(TimeInterval query, ArrayList<TimeInterval> intervals) {
        if (intervals.size() == 0) {
            return ArrayListUtil.fromSingleton(query);
        }

        ArrayList<TimeInterval> output = new ArrayList<>();
        TimeInterval firstInterval = new TimeInterval(query.getFrom(), intervals.get(0).getFrom());
        if (!firstInterval.isTrivial()) {
            output.add(firstInterval);
        }
        for (int i = 1; i < intervals.size(); i++) {
            output.add(new TimeInterval(intervals.get(i-1).getTo(), intervals.get(i).getFrom()));
        }
        TimeInterval lastInterval = new TimeInterval(intervals.get(intervals.size() - 1).getTo(), query.getTo());
        if (!lastInterval.isTrivial()) {
            output.add(lastInterval);
        }
        return output;
    }

    private CommandResult makeResult(ArrayList<TimeInterval> freetimes) {
        return () -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");
            StringBuilder builder = new StringBuilder();
            builder.append("You are free at the following time slots:\n");
            for (TimeInterval freetime: freetimes) {
                builder.append(" - ");
                builder.append(freetime.getFrom().format(formatter));
                builder.append(" - ");
                builder.append(freetime.getTo().format(formatter));
                builder.append("\n");
            }
            return builder.toString();
        };
    }
}
