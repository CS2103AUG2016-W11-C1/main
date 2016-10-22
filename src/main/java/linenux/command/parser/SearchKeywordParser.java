package linenux.command.parser;

import java.util.ArrayList;

import linenux.command.result.CommandResult;
import linenux.model.Reminder;
import linenux.model.Schedule;
import linenux.model.Task;
import linenux.util.Either;

public class SearchKeywordParser {
    private Schedule schedule;

    public SearchKeywordParser(Schedule schedule) {
        this.schedule = schedule;
    }

    public Either<ArrayList<Task>, CommandResult> parse(String keywords) {
        assert this.schedule != null;

        String[] keywordsArr = keywords.split("\\s+");
        ArrayList<Task> taskList = this.schedule.search(keywordsArr);

        if (taskList.size() > 0) {
            return Either.left(taskList);
        } else {
            return Either.right(makeNotFoundResult(keywords));
        }
    }

    private CommandResult makeNotFoundResult(String keywords) {
        return () -> "Cannot find task names with \"" + keywords + "\".";
    }

    public Either<ArrayList<Reminder>, CommandResult> parseReminder(String keywords) {
        assert this.schedule != null;

        String[] keywordsArr = keywords.split("\\s+");
        ArrayList<Reminder> reminderList = this.schedule.searchReminder(keywordsArr);

        if (reminderList.size() > 0) {
            return Either.left(reminderList);
        } else {
            return Either.right(makeNotFoundResult(keywords));
        }
    }
}
