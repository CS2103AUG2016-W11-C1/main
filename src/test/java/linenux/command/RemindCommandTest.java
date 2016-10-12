package linenux.command;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import linenux.model.Schedule;

/**
 * JUnit test for remind command.
 */
public class RemindCommandTest {
	private Schedule schedule;
	private RemindCommand remindCommand;

	@Before
	public void setupRemindCommand() {
		this.schedule = new Schedule();
		this.remindCommand = new RemindCommand(this.schedule);
	}

	/**
     * Test that respondTo detects various versions of the commands. It should return true even if
     * the format of the arguments are invalid.
     */
	@Test
	public void testRespondToRemindCommand() {
		assertTrue(this.remindCommand.respondTo("remind"));
		assertTrue(this.remindCommand.respondTo("remind task"));
		assertTrue(this.remindCommand.respondTo("remind task t/2016-01-01"));
		assertTrue(this.remindCommand.respondTo("remind task t/2016-01-01 n/notes"));
	}

	/**
	 * Test respondTo is case-insensitive.
	 */
	@Test
	public void testCaseInsensitiveRemindCommand() {
		assertTrue(this.remindCommand.respondTo("ReMiNd task t/2016-01-01"));
	}
}