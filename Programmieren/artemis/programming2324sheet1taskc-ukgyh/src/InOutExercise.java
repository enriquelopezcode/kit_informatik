import java.util.Scanner;

public class InOutExercise {
    private static final int AMOUNT_LINES = 3;
    public static void main(String[] args) {

        // separating symbol is declared
        String separator = args[0];

        Scanner scanner = new Scanner(System.in);

        // this string will contain the 3 joined sentences
        String concatenatedWord = "";

        // reading three lines
        for (int i = 0; i < AMOUNT_LINES; i++) {

            String input = scanner.nextLine();

            // convert input string to Uppercase
            input = input.toUpperCase();

            // if not final iteration add processed input and separator symbol
            if (i <  2) {
                concatenatedWord += input + separator;

                // if final iteration only add input string
            } else {
                concatenatedWord += input;
            }

        }

        scanner.close();

        // printing the final String
        System.out.println(concatenatedWord);
    }
}
