package linenux.command;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;

/**
 * JUnit test for invalid command.
 */
public class InvalidCommandTest {
    private Command invalidCommand;

    //@@author A0144915A
    @Before
    public void setupInvalidCommand() {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new ListCommand());
        commands.add(new ExitCommand());
        this.invalidCommand = new InvalidCommand(commands);
    }

    /**
     * Test correct response with suggestions.
     */
    @Test
    public void testResponseWithSuggestion() {
        CommandResult result = this.invalidCommand.execute("eit");
        assertEquals("Invalid command. Did you mean exit?", result.getFeedback());
    }

    /**
     * Test correct response without any suggestions.
     */
    @Test
    public void testResponseWithoutSuggestions() {
        this.invalidCommand = new InvalidCommand(new ArrayList<>());
        CommandResult result = this.invalidCommand.execute("eit");
        assertEquals("Invalid command.", result.getFeedback());
    }

    private static class BaseMockCommand extends AbstractCommand {
        @Override
        public String getTriggerWord() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        //@@author A0135788M
        @Override
        public String getCommandFormat() {
            return null;
        }

        //@@author A0144915A
        @Override
        public boolean respondTo(String input) {
            return true;
        }

        @Override
        public CommandResult execute(String input) {
            return null;
        }

        //@@author A0135788M
        @Override
        public String getPattern() {
            return null;
        }
    }

    //@@author A0144915A
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
