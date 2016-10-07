package linenux.command;

import linenux.command.result.CommandResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by yihangho on 10/8/16.
 */
public class InvalidCommandTest {
    private Command invalidCommand;

    @Before
    public void setupInvalidCommand() {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new ListCommand());
        commands.add(new ExitCommand());
        this.invalidCommand = new InvalidCommand(commands);
    }

    @Test
    public void testResponseWithSuggestion() {
        CommandResult result = this.invalidCommand.execute("eit");
        assertEquals("Invalid command. Did you mean exit?", result.getFeedback());
    }

    @Test
    public void testResponseWithoutSuggestions() {
        this.invalidCommand = new InvalidCommand(new ArrayList<>());
        CommandResult result = this.invalidCommand.execute("eit");
        assertEquals("Invalid command.", result.getFeedback());
    }

    private static class BaseMockCommand implements Command {
        @Override
        public String getTriggerWord() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public boolean respondTo(String input) {
            return true;
        }

        @Override
        public CommandResult execute(String input) {
            return null;
        }
    }

    private static class ListCommand extends BaseMockCommand {
        @Override
        public String getTriggerWord() {
            return "List";
        }
    }

    private static class ExitCommand extends BaseMockCommand {
        @Override
        public String getTriggerWord() {
            return "exit";
        }
    }
}
