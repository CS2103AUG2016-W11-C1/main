package linenux.command.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yihangho on 10/22/16.
 */
public class GenericParser {
    private static final Pattern FIRST_FLAG_PATTERN = Pattern.compile("(^|\\s+)\\S+/");
    private static final Pattern FLAG_PATTERN = Pattern.compile("\\S+/");
    private static final Pattern NEXT_FLAG_PATTERN = Pattern.compile("\\s+\\S+/");

    public GenericParserResult parse(String input) {
        GenericParserResult output = new GenericParserResult();

        String keywords = this.extractKeywords(input);
        output.setKeywords(keywords.trim());

        this.extractFlags(input, keywords.length(), output);

        return output;
    }

    private String extractKeywords(String input) {
        Matcher matcher = FIRST_FLAG_PATTERN.matcher(input);

        // Find the first flag. If there is no flag, the end index is at the end of string.
        int earliestIndex = input.length();
        if (matcher.find()) {
            earliestIndex = matcher.start();
        }

        return input.substring(0, earliestIndex);
    }

    private void extractFlags(String input, int index, GenericParserResult result) {
        for (int i = index; i < input.length(); ) {
            // Find where the next flag starts. This is necessary as i might be pointing to a
            // space.
            int startingIndex = input.length();
            Matcher matcher = FLAG_PATTERN.matcher(input);
            if (matcher.find(i)) {
                startingIndex = matcher.start();
            }

            if (startingIndex >= input.length()) {
                return;
            }

            int endingIndex = input.length();
            matcher = NEXT_FLAG_PATTERN.matcher(input);
            if (matcher.find(startingIndex + 1)) {
                endingIndex = matcher.start();
            }

            String chunk = input.substring(startingIndex, endingIndex);
            String[] chunks = chunk.split("/", 2);
            result.addArgument(chunks[0], chunks[1].trim());

            i = endingIndex + 1;
        }
    }

    public static class GenericParserResult {
        // Perhaps we can use Optionals to signal the presence of these values.
        private String keywords;
        private HashMap<String, ArrayList<String>> arguments = new HashMap<>();

        public String getKeywords() {
            return this.keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public ArrayList<String> getArguments(String flag) {
            return this.arguments.getOrDefault(flag, new ArrayList<>());
        }

        public void addArgument(String flag, String value) {
            if (!this.arguments.containsKey(flag)) {
                this.arguments.put(flag, new ArrayList<>());
            }

            this.arguments.get(flag).add(value);
        }
    }
}
