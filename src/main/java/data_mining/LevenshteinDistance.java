package data_mining;

import java.util.Arrays;

public class LevenshteinDistance {

    public static int calculateDistance(String str1, String str2){
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();
        int m = str1.length(), n = str2.length();
        int[] prevRow = new int[n + 1] , currRow = new int[n + 1];

        for ( int i = 0 ; i < n ; i++){
            prevRow[i] = i;
        }

        for ( int i = 1 ; i <= m ; i++){
            currRow[0] = i;
            for ( int j = 1 ; j <= n ; j++){
                if ( str1.charAt(i - 1) == str2.charAt(j - 1) ){
                    currRow[j] = prevRow[j - 1];
                }
                else{
                    currRow[j] = 1 + Math.min(currRow[j - 1], Math.min(prevRow[j], prevRow[j - 1]));
                }
            }

            prevRow = Arrays.copyOf(currRow, currRow.length );
        }

        return currRow[n];
    }

}
