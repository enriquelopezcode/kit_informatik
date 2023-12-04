package utils;

public class MainZigZag {
    public static void main(String[] args) {
        int depth = Integer.parseInt(args[0]);

        if (depth == 0) {
            int lines = args.length;
            String[] stringArray = new String[lines - 1];

            for (int i = 1; i < lines; i++) {
                stringArray[i - 1] = args[i];
            }

            String outputString = ZigZag.reverseZigZag(stringArray);

            System.out.print(outputString);
        } else if (depth > 0) {
            String string = args[1];
            String outputString = ZigZag.zigZag(string, depth);
            System.out.print(outputString);

        } else {
            System.out.print("Invalid Depth: " + depth + "\n");
        }
    }
}
