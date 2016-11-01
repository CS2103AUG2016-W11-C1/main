package linenux.command;

import java.util.HashSet;
import java.util.Set;

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
    public void removeAlias(String alias) {
        this.TRIGGER_WORDS.remove(alias);
    }

    @Override
    public String getPattern(){
        return "(?i)^\\s*(" + getTriggerWordsPattern() + ")(\\s+(?<keywords>.*))?$";
    }

    @Override
    public Set<String> getTriggerWords() {
        return TRIGGER_WORDS;
    }

    protected String getTriggerWordsPattern() {
        return String.join("|", this.TRIGGER_WORDS);
    }
}
