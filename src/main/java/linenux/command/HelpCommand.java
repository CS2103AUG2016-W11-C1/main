package linenux.command;

import java.util.ArrayList;

import linenux.command.result.CommandResult;

/**
 * Displays available command and their formats.
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
    public boolean respondTo(String userInput) {
        return userInput.matches(HELP_PATTERN);
    }

    @Override
    public CommandResult execute(String userInput) {
        return () -> {
            int maxLength = 0;
            for (Command command: this.commands) {
                if (command.getTriggerWord().length() > maxLength) {
                    maxLength = command.getTriggerWord().length();
                }
            }

            StringBuilder builder = new StringBuilder();
            for (Command command: this.commands) {
                builder.append(command.getTriggerWord());
                builder.append(" - ");
                builder.append(new String(new char[maxLength - command.getTriggerWord().length()]).replace("\0", " "));

                builder.append("Description: ");
                builder.append(command.getDescription());
                builder.append('\n');
                builder.append(new String(new char[maxLength + 3]).replace("\0", " "));

                builder.append("Format: ");
                builder.append(command.getCommandFormat());
                builder.append("\n\n");
            }
            return builder.toString();
        };
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
}
