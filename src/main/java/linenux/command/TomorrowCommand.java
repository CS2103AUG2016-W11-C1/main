package linenux.command;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by yihangho on 10/25/16.
 */
public class TomorrowCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "tomorrow";
    private static final String DESCRIPTION = "Lists tasks and reminders for tomorrow.";
    private static final String COMMAND_FORMAT = "tomorrow";

    private Schedule schedule;
    private Clock clock;
    private ListCommand listCommand;

    public TomorrowCommand(Schedule schedule) {
        this(schedule, Clock.systemDefaultZone());
    }

    public TomorrowCommand(Schedule schedule, Clock clock) {
        this.schedule = schedule;
        this.clock = clock;
        this.listCommand = new ListCommand(this.schedule);
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d h:mma");
        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime startOfTomorrow = now.withHour(0).withMinute(0).withSecond(0).plusDays(1);
        LocalDateTime endOfTomorrow = now.withHour(23).withMinute(59).withSecond(59).plusDays(1);

        return this.listCommand.execute("list st/" + startOfTomorrow.format(formatter) + " et/" + endOfTomorrow.format(formatter));
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
