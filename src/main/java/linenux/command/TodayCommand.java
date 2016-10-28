package linenux.command;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;

/**
 * Created by yihangho on 10/25/16.
 */
public class TodayCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "today";
    private static final String DESCRIPTION = "Lists tasks and reminders for today.";
    private static final String COMMAND_FORMAT = "today";

    private Schedule schedule;
    private Clock clock;
    private ListCommand listCommand;

    public TodayCommand(Schedule schedule) {
        this(schedule, Clock.systemDefaultZone());
    }

    public TodayCommand(Schedule schedule, Clock clock) {
        this.schedule = schedule;
        this.clock = clock;
        this.listCommand = new ListCommand(this.schedule);
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d h.mma");
        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime startOfToday = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfToday = now.withHour(23).withMinute(59).withSecond(59);

        return this.listCommand.execute("list st/" + startOfToday.format(formatter) + " et/" + endOfToday.format(formatter));
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
}
