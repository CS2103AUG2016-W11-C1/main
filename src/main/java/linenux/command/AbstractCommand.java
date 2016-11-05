package linenux.command;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@@author A0144915A
public abstract class AbstractCommand implements Command {
    protected HashSet<String> TRIGGER_WORDS = new HashSet<>();

    @Override
    public boolean respondTo(String userInput) {
        return userInput.matches(getPattern());
    }

    @Override
    public void setAlias(String alias) {
        this.TRIGGER_WORDS.add(alias);
    }

    @Override
    public void setAliases(Collection<String> aliases) {
        this.TRIGGER_WORDS = new HashSet<>(aliases);
        this.TRIGGER_WORDS.add(this.getTriggerWord());
    }

    @Override
    public void removeAlias(String alias) {
        this.TRIGGER_WORDS.remove(alias);
    }

    @Override
    public Set<String> getTriggerWords() {
        return TRIGGER_WORDS;
    }

    @Override
    public String getPattern(){
        return "(?i)^\\s*(" + getTriggerWordsPattern() + ")(\\s+(?<argument>.*))?$";

    }

    protected String getTriggerWordsPattern() {
        return String.join("|", this.TRIGGER_WORDS);
    }

    protected String extractArgument(String userInput) {
        Matcher matcher = Pattern.compile(getPattern()).matcher(userInput);

        if (matcher.matches() && matcher.group("argument") != null) {
            return matcher.group("argument").trim();
        } else {
            return "";
        }
    }
}
