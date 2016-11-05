package linenux.command;

import linenux.command.result.CommandResult;
import linenux.model.Schedule;
import linenux.util.ArrayListUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//@@author A0144915A
public class UnaliasCommandTest {
    private Schedule schedule;
    private AddCommand addCommand;
    private DeleteCommand deleteCommand;
    private UnaliasCommand unaliasCommand;

    @Before
    public void setupCommands() {
        this.schedule = new Schedule();
        this.addCommand = new AddCommand(this.schedule);
        this.deleteCommand = new DeleteCommand(this.schedule);
        this.unaliasCommand = new UnaliasCommand(ArrayListUtil.fromArray(new Command[] {this.addCommand, this.deleteCommand}));

        this.addCommand.setAlias("a");
    }

    @Test
    public void respondTo_inputThatBeginsWithUnalias_trueReturned() {
        assertTrue(this.unaliasCommand.respondTo("unalias"));
        assertTrue(this.unaliasCommand.respondTo("unalias bla"));
        assertTrue(this.unaliasCommand.respondTo("unalias foo bar"));
        assertTrue(this.unaliasCommand.respondTo("uNaLiaS"));
    }

    @Test
    public void respondTo_otherCommands_falseReturned() {
        assertFalse(this.unaliasCommand.respondTo("notalias"));
        assertFalse(this.unaliasCommand.respondTo("aunalias"));
        assertFalse(this.unaliasCommand.respondTo("unaliasa"));
    }

    @Test
    public void execute_validInput_aliasRemoved() {
        assertTrue(this.addCommand.respondTo("a"));
        CommandResult result = this.unaliasCommand.execute("unalias a");
        String expectedResult = "\"a\" is removed as an alias.";
        assertEquals(expectedResult, result.getFeedback());
        assertFalse(this.addCommand.respondTo("a"));
    }

    @Test
    public void execute_nonExistentAlias_commandResultReturned() {
        CommandResult result = this.unaliasCommand.execute("unalias b");
        String expectedResult = "\"b\" is not an alias.";
        assertEquals(expectedResult, result.getFeedback());
        assertTrue(this.addCommand.respondTo("a"));
        assertTrue(this.addCommand.respondTo("add"));
    }

    @Test
    public void execute_defaultCommand_commandResultReturned() {
        CommandResult result = this.unaliasCommand.execute("unalias delete");
        String expectedResult = "\"delete\" cannot be removed as an alias.";
        assertEquals(expectedResult, result.getFeedback());
    }
}
