package com.uecpe20231122784;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Main {
    
    final static int DIE_SIDES = 6;
    final static int EXIT_SIG = 0;
    final static String WRITE_TO_FILE = "DiceGameLog.txt";

    /*
      * Exit codes:
      * 0 -- Success
      * 50 -- User terminated program
      * 61 -- Write failed
    */

    public static void main(String[] args) throws IOException {

        // Print program title/description
        println("Dice Game\nPlease give a non-negative number. Type 0 to exit.");

        // Define initial variables and resources
        Scanner stdin = new Scanner(System.in);
        Random random = new Random();
        boolean invalid_input = false;
        int input = 0;
        
        // Input validation and error handling
        do {
            try {
                print("How many times would you like to roll? ");
                input = (int) waitForInput(stdin, "int");
                if (input == EXIT_SIG) {
                    println("<*> Received exit signal");
                    System.exit(50);
                }
                else if (input < 0) {
                    println("<!> ERROR: Input must not be negative");
                    invalid_input = true;
                }
                else {
                    // Clear file
                    resetFile(WRITE_TO_FILE);
                    invalid_input = false;
                }
            } catch (Exception e) {
                println("<!> ERROR: Invalid input");
                invalid_input = true;
            }
        } while (invalid_input);

        // Initiate arrays with appropriate sizes
        int[] player_rolls = new int[input], computer_rolls = new int[input];
        String[] winner = new String[input];
        int player_win_count = 0, computer_win_count = 0;
        
        // Print table headers
        printTableSeparator();
        printTableHeader();
        printTableSeparator();

        // Iterate
        for (int i = 0; i < input; i++) {
            player_rolls[i] = rollDie(random);
            computer_rolls[i] = rollDie(random);
            if (player_rolls[i] > computer_rolls[i]) {
                winner[i] = "Player";
                player_win_count++;
            } 
            else if (player_rolls[i] == computer_rolls[i]) {
                winner[i] = "Tie";
            }
            else {
                winner[i] = "Computer";
                computer_win_count++;
            }
            printRow(i + 1, player_rolls[i], computer_rolls[i], winner[i]);
        }

        // Print table footer
        printTableSeparator();
        println("Player wins " + player_win_count + " times");
        println("Computer wins " + computer_win_count + " times");

        // Determine grand winner
        if (player_win_count > computer_win_count) {
            println("Player wins!");
        }
        else if (player_win_count == computer_win_count) {
            println("It's a tie.");
        }
        else {
            println("Computer wins.");
        }

    }

    public static <PrintableToString> void println(PrintableToString... args) {
        for (PrintableToString pts : args) {
            System.out.print(pts);
            writeln(pts.toString());
        }
        System.out.println();
    }

    public static <PrintableToString> void print(PrintableToString... args) {
        for(PrintableToString pts: args) {
            System.out.print(pts);
            writeln(pts.toString());
        }
            
    }

    public static <PrintableToString> void printf(String format, PrintableToString... args) {
        System.out.println(String.format(format, args));
        writeln(String.format(format, args));
    }

    public static void printTableHeader() {
        printf("| %-6s | %-4s | %-4s |    %-6s    |", "Roll #", "Player Roll", "Computer Roll", "Winner");
    }

    public static void printTableSeparator() {
        printf("+%-6s+%-4s+%-4s+%-6s+", "--------", "-------------", "---------------", "--------------");
    }

    public static void printRow(int roll_num, int player_roll, int computer_roll, String winner) {
        printf("| %-6d | %-11d | %-13d | %-12s |", roll_num, player_roll, computer_roll, winner);
    }

    public static void writeln(String string) {
        try(FileWriter fw = new FileWriter(WRITE_TO_FILE, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(string);
        } catch (IOException e) {
            println("<!> ERROR: Write failed, check I/O permissions");
            System.exit(61);
        }
    }

    public static void resetFile(String file) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.write("");
        } catch (IOException e) {
            println("<!> ERROR: Write failed, check I/O permissions");
            System.exit(61);
        }
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param random Instance of <code>java.util.Random</code>.
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
    */
    public static int randInt(Random r, int min, int max) {
        int rand = r.nextInt((max - min) + 1) + min;
        return rand;
    }

    public static int rollDie(Random r) {
        int rand = randInt(r, 1, DIE_SIDES);
        return rand;
    }

    /**
     * Waits for user input and returns the value.
     *
     * @param scanner Instance of <code>java.util.Scanner</code>
     * @param type One of the following types: <code>int</code>, <code>double</code>, <code>float</code>, <code>bool</code>. Defaults to <code>String</code> otherwise.
     * @return User input.
    */
    public static Object waitForInput(Scanner scanner, String type) {
        String i = scanner.nextLine();
        switch (type) {
            case "int":
                return Integer.parseInt(i);
            case "double":
                return Double.parseDouble(i);
            case "float":
                return Float.parseFloat(i);
            case "boolean":
                return Boolean.parseBoolean(i);
            default:
                return i;
        }
    }
    
}