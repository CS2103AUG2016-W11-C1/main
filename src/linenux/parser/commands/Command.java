package linenux.parser.commands;

import linenux.model.Schedule;

/**
 * Represents an executable command.
 */

public abstract class Command {
   public abstract String[] execute(Schedule schedule);
}
