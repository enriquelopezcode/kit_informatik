package utils;

public class ZigZag {
    private static final char SPACE = ' ';
    private static final String LINEBREAK = "\n";

    public static String zigZag(String string, int lines) {

        int stringLength = string.length();
        char[] stringArray = string.toCharArray();
        char[][] stringMatrix = new char[lines][stringLength];

        //distance between indices of chars in the first row of the output string
        int firstRowDistance = 2 * lines - 2;

        for (int i = 0; i < lines; i++) {
            //computing the two different distances between chars in the current row of the loop
            int firstCurrentRowSpace = firstRowDistance - (2 * i);
            int secondCurrentRowSpace = firstRowDistance - firstCurrentRowSpace;

            //first char appears at the index that is equal to the current line
            int currentIndex = i;
            while (currentIndex < stringLength) {

                //Adding first index distance
                stringMatrix[i][currentIndex] = stringArray[currentIndex];
                currentIndex += firstCurrentRowSpace;

                //Checking if index out of bounds, if not adding second index distance
                if (currentIndex < stringLength) {
                    stringMatrix[i][currentIndex] = stringArray[currentIndex];
                    currentIndex += secondCurrentRowSpace;
                }
            }
        }

        // replacing all empty chars with spaces
        for (int m = 0; m < lines; m++) {
            for (int n = 0; n < stringLength; n++) {
                if (stringMatrix[m][n] == '\u0000') {
                    stringMatrix[m][n] = SPACE;
                }
            }
        }

        // joining all lines with a linebreak
        String outputString = "";
        for (int m = 0; m < lines; m++) {
            outputString += new String(stringMatrix[m]);
            outputString += LINEBREAK;

        }
        return outputString;
    }


    public static String reverseZigZag(String[] stringArray) {
        int stringLength = stringArray[0].length();
        int lines = stringArray.length;
        int firstRowDistance = 2 * lines - 2;

        char[] outputArray = new char[stringLength];

        for (int i = 0; i < lines; i++) {

            char[] currentLineArray = stringArray[i].toCharArray();

            //computing the two different distances between chars in the current row of the loop
            int firstCurrentRowSpace = firstRowDistance - (2 * i);
            int secondCurrentRowSpace = firstRowDistance - firstCurrentRowSpace;


            //first char appears at the index that is equal to the current line
            int currentIndex = i;
            while (currentIndex < stringLength) {

                //Adding first index distance
                outputArray[currentIndex] = currentLineArray[currentIndex];
                currentIndex += firstCurrentRowSpace;

                //Checking if index out of bounds, if not adding second index distance
                if (currentIndex < stringLength) {
                    outputArray[currentIndex] = currentLineArray[currentIndex];
                    currentIndex += secondCurrentRowSpace;
                }
            }

        }
        String outputString = new String(outputArray) + LINEBREAK;
        return outputString;
    }

}

