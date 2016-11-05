package linenux.command;

import java.util.ArrayList;

import linenux.command.parser.GenericParser;
import linenux.command.result.CommandResult;
import linenux.command.result.SearchResults;
import linenux.model.Schedule;
import linenux.model.Task;

/**
 * Renames all instances of a specified tag in schedule.
 */
// @@author A0127694U
public class RenameCommand extends AbstractCommand {
    private static final String TRIGGER_WORD = "rename";
    private static final String DESCRIPTION = "Changes the name of specified tag in all tasks.";
    public static final String COMMAND_FORMAT = "rename TAG_NAME... #/TAG...";

    private Schedule schedule;

    public RenameCommand(Schedule schedule) {
        this.schedule = schedule;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String argument = extractArgument(userInput);
        GenericParser parser = new GenericParser();
        GenericParser.GenericParserResult result = parser.parse(argument);
        String tagName = result.getKeywords();
        String newName = result.getArguments("#").size() > 0 ? result.getArguments("#").get(0) : "";

        if (tagName.trim().isEmpty() || newName.trim().isEmpty()) {
            return makeNoKeywordsResult();
        }

        ArrayList<Task> results = this.schedule.searchTag(tagName);

        if (results.size() == 0) {
            return SearchResults.makeTagNotFoundResult(tagName);
        } else {
            return implementRename(results, tagName, newName);
        }
    }

    // @@author A0135788M
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

    private CommandResult implementRename(ArrayList<Task> original, String originalName, String newName) {
        ArrayList<Task> originalTasks = original;
        ArrayList<Task> modifiedTasks = new ArrayList<Task>();

        for (Task t : original) {
            assert (t.hasTag(originalName));
            for (int i = 0; i < t.getTags().size(); i++) {
                if (t.getTags().get(i).equalsIgnoreCase(originalName)) {
                    t.getTags().set(i, newName);
                    break;
                }
            }
            modifiedTasks.add(t);
        }

        this.schedule.updateTask(originalTasks, modifiedTasks);
        return makeRenameTag(originalName, newName);
    }

    private CommandResult makeNoKeywordsResult() {
        return () -> "Invalid arguments.\n\n" + COMMAND_FORMAT + "\n\n" + CALLOUTS;
    }

    private CommandResult makeRenameTag(String original, String newTag) {
        return () -> "Edited tag \"" + original + "\".\nNew tag name: " + newTag;
    }

}
