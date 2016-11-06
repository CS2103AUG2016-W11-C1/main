package linenux.command;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@@author A0144915A
/**
 * A convenient super class that can be used by all classes implementing {@code Command}. Please do not use this class
 * to declare variables. Use the {@code Command} interface instead.
 */
public abstract class AbstractCommand implements Command {
    protected HashSet<String> TRIGGER_WORDS = new HashSet<>();

    /**
     * @param userInput A {@code String} representing user input.
     * @return {@code true} if and only if this {@code Command} can handle {@code userInput}.
     */
    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(getPattern());
    }

    /**
     * Make this {@code Command} respond to {@code alias}
     * @param alias A {@code String} representing the new alias.
     */
    @Override
    public void setAlias(String alias) {
        this.TRIGGER_WORDS.add(alias);
    }

    /**
     * Make this {@code Command} respond to {@code aliases}
     * @param aliases A {@code String} representing the new aliases.
     */
    @Override
    public void setAliases(Collection<String> aliases) {
        this.TRIGGER_WORDS = new HashSet<>(aliases);
        this.TRIGGER_WORDS.add(this.getTriggerWord());
    }

    /**
     * Make this {@code Command} to stop responding to {@code alias}.
     * @param alias A {@code String} representing the alias to remove.
     */
    @Override
    public void removeAlias(String alias) {
        this.TRIGGER_WORDS.remove(alias);
    }

    /**
     * @return A {@code Set} of all trigger words.
     */
    @Override
    public Set<String> getTriggerWords() {
        return TRIGGER_WORDS;
    }

    /**
     * @return A {@code String} representing a regular expression for this {@code Command}.
     */
    @Override
    public String getPattern(){
        return "(?i)^\\s*(" + getTriggerWordsPattern() + ")(\\s+(?<argument>.*))?$";
    }

    /**
     * @return A {@code String} representing a regular expression that matches all aliases.
     */
    protected String getTriggerWordsPattern() {
        return String.join("|", this.TRIGGER_WORDS);
    }

    /**
     * Extract the user argument from user input.
     * @param userInput A {@code String} to extract the argument from.
     * @return The user argument.
     */
    protected String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("argument") != null) {
            return matcher.group("argument").trim();
        } else {
            return "";
        }
    }
}
