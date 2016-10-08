package linenux.command;

import linenux.command.result.CommandResult;

import java.util.ArrayList;

/**
 * Created by yihangho on 10/8/16.
 */
public class HelpCommand implements Command {
    private static final String TRIGGER_WORD = "help";
    private static final String DESCRIPTION = "Shows this help message.";
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
