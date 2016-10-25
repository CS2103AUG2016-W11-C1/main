package linenux.command;

import java.util.ArrayList;

/**
 * Created by yihangho on 10/22/16.
 */
public abstract class AbstractCommand implements Command {
    protected ArrayList<String> TRIGGER_WORDS = new ArrayList<>();

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

    protected String getTriggerWordsPattern() {
        if (TRIGGER_WORDS.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        builder.append(TRIGGER_WORDS.get(0));

        for (int i = 1; i < TRIGGER_WORDS.size(); i++) {
            builder.append('|');
            builder.append(TRIGGER_WORDS.get(i));
        }

        return builder.toString();
    }
}
