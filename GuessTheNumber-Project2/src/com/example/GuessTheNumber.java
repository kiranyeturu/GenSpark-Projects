package com.example;

// Java program for the above approach
import java.util.Scanner;

public class GuessTheNumber {
public static int number;
    // Function that implements the
    // number guessing game
    public static void
    guessingNumberGame()
    {
        // Scanner Class
        Scanner sc = new Scanner(System.in);

        // Generate the numbers
        number = 1 + (int)(20
                * Math.random());

        // Given K trials
        int K = 6;

        int i, guess;


        System.out.println("Hello What is your Name:");
        String guessName = sc.nextLine();

        System.out.println("Well," + guessName+", I am thinking of a Number between 1 and 20.\n" + "Take a guess.");

        // Iterate over K Trials
        for (i = 0; i < K; i++) {

            System.out.println("Guess the number:");

            // Take input for guessing
            guess = sc.nextInt();

            // If the number is guessed
            if (number == guess) {
                System.out.println("Good Job!" + guessName + " You guessed my number in "+ i + " guesses!.");
                break;
            }
            else if (number > guess
                    && i != K - 1) {
                System.out.println("Your Guess is too Low! " + guess);
            }
            else if (number < guess
                    && i != K - 1) {
                System.out.println("Your Guess is too High!  " + guess);
            }
        }

        if (i == K) {
            System.out.println("You have exhausted K trials.");

            System.out.println("The number was " + number);
        }
    }

    // Driver Code
    public static void  main(String arg[])    {

        // Function Call
        //guessingNumberGame();
        Scanner inner = new Scanner(System.in);
        String playAgain = "yes";
        boolean play = true;
        while (play) {
            guessingNumberGame();
            System.out.println("Do you want to play again? (yes or no)");
            playAgain = inner.nextLine();
            if (playAgain.equals("yes")) {
                play = true;
            }
            else {
                play = false;
            }
        }
        inner.close();


    }

}
