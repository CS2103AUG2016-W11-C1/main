package linenux.command;

import java.util.ArrayList;

import linenux.command.parser.GenericParser;
import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.ArrayListUtil;

//@@author A0140702X
/**
 * Clears the schedule of all done tasks.
 */
public class ClearCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "clear";
    private static final String DESCRIPTION = "Clears the schedule of all done tasks.";
    private static final String COMMAND_FORMAT = "clear";

    private Schedule schedule;

    /**
     * Constructs an {@code ClearCommand}.
     * @param schedule The {@code Schedule} that will be cleared.
     */
    public ClearCommand(Schedule schedule) {
        this.schedule = schedule;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    /**
     * Executes the command based on {@code userInput}. This method operates under the assumption that
     * {@code respondTo(userInput)} is {@code true}.
     * @param userInput A {@code String} representing the user input.
     * @return A {@code CommandResult} representing the result of the command.
     */
    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        ArrayList<Task> doneTasks = this.schedule.getTaskList();

        String tag = extractTag(userInput);

        if (tag != null) {
            doneTasks = new ArrayListUtil.ChainableArrayListUtil<>(doneTasks)
                .filter(task -> task.hasTag(tag)).value();
        } else {
            doneTasks = new ArrayListUtil.ChainableArrayListUtil<>(doneTasks)
                .filter(Task::isDone).value();
        }

        if (doneTasks.isEmpty()) {
            return () -> "There are no done tasks to clear!";
        } else {
            this.schedule.deleteTasks(doneTasks);
            return () -> "Deleting tasks!";
        }
    }

    /**
     * @return A {@code String} representing the default command word.
     */
    @Override
    public String getTriggerWord() {
        return TRIGGER_WORD;
    }

    /**
     * @return A {@code String} describing what this {@code Command} does.
     */
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * @return A {@code String} describing the format that this {@code Command} expects.
     */
    @Override
    public String getCommandFormat() {
        return COMMAND_FORMAT;
    }

    /**
     * Extract tag from the {@code argument}.
     * @param argument A {@code String} representing the argument of the {@code Command}.
     * @return A tag, if present.
     */
    private String extractTag(String argument) {
        GenericParser parser = new GenericParser();
        GenericParser.GenericParserResult result = parser.parse(argument);

        ArrayList<String> flags = result.getArguments("#");

        if (flags.size() == 0) {
            return null;
        } else {
            return flags.get(0);
        }
    }
}
