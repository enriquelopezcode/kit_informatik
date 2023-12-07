package utils;

public class StringUtility {
    public static String reverse(String word) {

        char[] inputArray = word.toCharArray();
        char[] outputArray = new char[inputArray.length];

        for (int i = 0; i < inputArray.length; i++) {
            outputArray[i] = inputArray[inputArray.length - 1 - i];
        }

        return new String(outputArray);
    }

    public static boolean isPalindrome(String word) {
        String reversed = reverse(word);
        return word.equals(reversed);
    }

    public static String removeCharacter(String word, int index) {

        char[] inputArray = word.toCharArray();
        char[] outputArray = new char[inputArray.length - 1];

        //conditions are chosen so that removed index is ignored
        for (int i = 0; i < inputArray.length; i++) {
            if (i < index) {
                outputArray[i] = inputArray[i];
                
            //index is shifted to the left to replace removed index
            } else if (i > index) {
                outputArray[i - 1] = inputArray[i];

            }
        }

        return new String(outputArray);
    }

    public static boolean isAnagram(String word, String otherWord) {
        
        //If two Strings are not the same length they can't be Anagrams of each other
        if (word.length() == otherWord.length()) {
            char[] wordArray = word.toCharArray();
            char[] otherWordArray = otherWord.toCharArray();

            for (char character : wordArray) {
                
                //Searching for the character in other String
                for (int z = 0; z < otherWordArray.length; z++) {
                    if (otherWordArray[z] == character) {
                        
                        //If character is found, set index to default value, so it isn't compared again
                        otherWordArray[z] = '\u0000';
                        break;
                        
                    //If character isn't found by the end the Strings are not Anagrams of each other
                    } else if (z == otherWordArray.length - 1) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        //If all characters are contained in both Strings, they are Anagrams of each other
        return true;
    }

    public static String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static int countCharacter(String word, char character) {
        int count = 0;
        char[] wordArray = word.toCharArray();

        for (char wordCharacter : wordArray) {
            if (wordCharacter == character) {
                count += 1;
            }
        }
        return count;
    }
}
