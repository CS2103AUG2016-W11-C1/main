package linenux.command;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final String NUMBER_PATTERN = "^\\d+$";
    private static final String CANCEL_PATTERN = "^cancel$";

    private Schedule schedule;

    public RenameCommand(Schedule schedule) {
        this.schedule = schedule;
        this.TRIGGER_WORDS.add(TRIGGER_WORD);
    }

    @Override
    public CommandResult execute(String userInput) {
        assert userInput.matches(getPattern());
        assert this.schedule != null;

        String tagName = extractTagName(userInput);
        String newName = extractNewName(userInput);

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

    // @@author A0127694U
    @Override
    public String getPattern() {
        return "(?i)^\\s*(" + getTriggerWordsPattern() + ")(\\s+(?<tagName>.*?)(#/(?<newName>.*)?)?)?";
    }

    // @@author A0135788M
    private String extractTagName(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("tagName") != null) {
            return matcher.group("tagName").trim(); // TODO
        } else {
            return "";
        }
    }

    // @@author A0127694U
    private String extractNewName(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("newName") != null) {
            return matcher.group("newName");
        } else {
            return "";
        }
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
