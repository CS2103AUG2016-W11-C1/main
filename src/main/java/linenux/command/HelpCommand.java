package linenux.command;

import java.util.ArrayList;

import linenux.command.result.CommandResult;

/**
 * Created by yihangho on 10/8/16.
 */
public class HelpCommand implements Command {
    private static final String TRIGGER_WORD = "help";
    private static final String DESCRIPTION = "Shows this help message.";
    private static final String COMMAND_FORMAT = "help";

    private static final String HELP_PATTERN = "(?i)^\\s*help\\s*$";

    private ArrayList<Command> commands;

    public HelpCommand(ArrayList<Command> commands) {
        this.commands = commands;
    }

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

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(HELP_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        return () -> {
            StringBuilder builder = new StringBuilder();
            for (Command command: this.commands) {
                builder.append(command.getTriggerWord());
                builder.append(" - ");
                builder.append(command.getDescription());
                builder.append('\n');
            }
            return builder.toString();
        };
    }
}
