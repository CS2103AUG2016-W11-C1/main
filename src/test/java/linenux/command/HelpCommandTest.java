package linenux.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.util.ArrayListUtil;

// @@author A0140702X
public class HelpCommandTest {
    private HelpCommand helpCommand;
    private AddCommand addCommand;
    private EditCommand editCommand;
    private Schedule schedule;

    @Before
    public void setupHelpCommand() {
        this.schedule = new Schedule();
        this.addCommand = new AddCommand(this.schedule);
        this.editCommand = new EditCommand(this.schedule);
        this.helpCommand = new HelpCommand(
                ArrayListUtil.fromArray(new Command[] { this.addCommand, this.editCommand }));
    }

    /**
     * Test that help responds to the different versions of the command. Should
     * return true even if invalid.
     */
    @Test
    public void respondTo_inputThatStartsWithHelp_trueReturned() {
        assertTrue(this.helpCommand.respondTo("help"));
        assertTrue(this.helpCommand.respondTo("help add"));
        assertTrue(this.helpCommand.respondTo("help edit"));
        assertTrue(this.helpCommand.respondTo("help a a"));
    }

    /**
     * Test that respondTo is case-insensitive
     */
    @Test
    public void respondTo_upperCase_trueReturned() {
        assertTrue(this.helpCommand.respondTo("hElP"));
    }

    /**
     * Test does not respond to other commands
     */
    @Test
    public void respondTo_otherCommand_falseReturned() {
        assertFalse(this.helpCommand.respondTo("wat"));
    }

    /**
     * Test too many arguments given
     */
    @Test
    public void execute_invalidArgument_commandResultReturned() {
        CommandResult result = this.helpCommand.execute("help add 2");
        assertEquals("Too many arguments given. Please only search for one command at a time.", result.getFeedback());
    }

    /**
     * Test suggestion given for invalid command.
     */
    @Test
    public void execute_invalidCommand_suggestionGiven() {
        CommandResult result = this.helpCommand.execute("help addd");
        assertEquals("Invalid command given for help. Did you mean \'add\'?", result.getFeedback());
    }

    /**
     * Test execution of help command without specifying any command.
     */
    @Test
    public void execute_withoutArgument_commandResultReturned() {
        CommandResult result = this.helpCommand.execute("help");
        String addDescription = addCommand.getTriggerWord() + " - \nDescription: " + addCommand.getDescription()
                + "\nFormat: "
                + addCommand.getCommandFormat();
        String editDescription = editCommand.getTriggerWord() + " - \nDescription: " + editCommand.getDescription()
                + "\nFormat: "
                + editCommand.getCommandFormat();

        assertEquals(addDescription + "\n\n" + editDescription + "\n\n" + this.helpCommand.CALLOUTS,
                result.getFeedback());
    }

    /**
     * Test execution of help command with specified command
     */
    @Test
    public void execute_specificCommnad_commandResultReturned() {
        CommandResult result = this.helpCommand.execute("help add");
        String addDescription = addCommand.getTriggerWord() + " - \nDescription: " + addCommand.getDescription()
                + "\nFormat: " + addCommand.getCommandFormat();
        assertEquals(addDescription + "\n\n", result.getFeedback());
    }

    /**
     * Test execution of help command with for command with alias
     */
    @Test
    public void execute_commandAlias_commandResultReturned() {
        this.addCommand.setAlias("a");
        this.addCommand.setAlias("ad");
        this.editCommand.setAlias("e");
        this.editCommand.setAlias("ed");

        CommandResult result = this.helpCommand.execute("help");
        String addDescription = addCommand.getTriggerWord() + ", a, ad - \nDescription: " + addCommand.getDescription()
                + "\nFormat: " + addCommand.getCommandFormat();
        String editDescription = editCommand.getTriggerWord() + ", e, ed - \nDescription: "
                + editCommand.getDescription() + "\nFormat: " + editCommand.getCommandFormat();

        assertEquals(addDescription + "\n\n" + editDescription + "\n\n" + this.helpCommand.CALLOUTS,
                result.getFeedback());
    }
}
