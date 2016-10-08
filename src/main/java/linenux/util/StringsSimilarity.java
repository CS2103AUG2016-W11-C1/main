package linenux.util;

/**
 * Computes the similarity between two strings.
 */
public class StringsSimilarity {
    /**
     * Computes the similarity between two strings.
     * @param a The first string.
     * @param b The second string.
     * @return The Levenshtein distance between {@code a} and {@code b}.
     */
    public static int compute(String a, String b) {
        int l1 = a.length();
        int l2 = b.length();
        int[][] dp = new int[l1+1][l2+1];

        for (int i = 1; i <= l1; i++) {
            dp[i][0] = i;
        }

        for (int i = 1; i <= l2; i++) {
            dp[0][i] = i;
        }

        for (int i = 1; i <= l1; i++) {
            for (int j = 1; j <= l2; j++) {
                int add = dp[i-1][j] + 1;
                int delete = dp[i][j-1] + 1;
                int swap = dp[i-1][j-1] + (a.charAt(i-1) == b.charAt(j-1) ? 0 : 2);
                dp[i][j] = Math.min(Math.min(add, delete), swap);
            }
        }

        return dp[l1][l2];
    }
}
