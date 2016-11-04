package linenux.command;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import linenux.config.Config;
import linenux.control.CommandManager;
import linenux.control.ControlUnit;
import linenux.model.Schedule;
import linenux.storage.ScheduleStorage;
import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;

/**
 * JUnit test for invalid command.
 */
public class InvalidCommandTest {
    private ControlUnit controlUnit;
    private Command invalidCommand;

    //@@author A0144915A
    @Before
    public void setupInvalidCommand() {
        CommandManager manager = new CommandManager();
        manager.addCommand(new ListCommand());
        manager.addCommand(new ExitCommand());

        this.controlUnit = new ControlUnit(new MockStorage(), new MockConfig(), manager);

        this.invalidCommand = new InvalidCommand(this.controlUnit);

        manager.setCatchAllCommand(this.invalidCommand);
    }

    /**
     * Test correct response with suggestions.
     */
    @Test
    public void testResponseWithSuggestion() {
        CommandResult result = this.invalidCommand.execute("eit");
        assertEquals("Invalid command. Did you mean exit?", result.getFeedback());
    }

    @Test
    public void testYes() {
        CommandResult result = this.controlUnit.execute("yes");
        assertEquals("Invalid command. Did you mean list?", result.getFeedback());
    }

    @Test
    public void testFollowUp() {
        CommandResult result = this.controlUnit.execute("ls");
        assertEquals("Invalid command. Did you mean list?", result.getFeedback());
        result = this.controlUnit.execute("yes");
        assertEquals("list", result.getFeedback());
        result = this.controlUnit.execute("yes");
        assertEquals("Invalid command. Did you mean list?", result.getFeedback());
    }

    @Test
    public void testNoFollowUp() {
        this.controlUnit.execute("ls");
        CommandResult result = this.controlUnit.execute("exit");
        assertEquals("exit", result.getFeedback());
        result = this.controlUnit.execute("yes");
        assertEquals("Invalid command. Did you mean list?", result.getFeedback());
    }

    /**
     * Test correct response without any suggestions.
     */
    @Test
    public void testResponseWithoutSuggestions() {
        this.invalidCommand = new InvalidCommand(new ControlUnit(new MockStorage(), new MockConfig(), new CommandManager()));
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
        public CommandResult execute(String input) {
            return this::getTriggerWord;
        }
    }

    //@@author A0144915A
    private static class ListCommand extends BaseMockCommand {
        public ListCommand() {
            this.TRIGGER_WORDS.add("list");
        }

        @Override
        public String getTriggerWord() {
            return "list";
        }
    }

    private static class ExitCommand extends BaseMockCommand {
        public ExitCommand() {
            this.TRIGGER_WORDS.add("exit");
        }

        @Override
        public String getTriggerWord() {
            return "exit";
        }
    }

    private static class MockStorage implements ScheduleStorage {
        @Override
        public Schedule loadScheduleFromFile() {
            return new Schedule();
        }

        @Override
        public void saveScheduleToFile(Schedule schedule) {
        }

        @Override
        public boolean hasScheduleFile() {
            return true;
        }
    }

    private static class MockConfig implements Config {
        @Override
        public String getScheduleFilePath() {
            return "fakepath";
        }

        @Override
        public void setScheduleFilePath(String path){
        }

        @Override
        public boolean hasConfigFile() {
            return true;
        }

        @Override
        public Collection<String> getAliases(String triggerWord) {
            return null;
        }

        @Override
        public void setAliases(String triggerWord, Collection<String> aliases) {
        }
    }
}
