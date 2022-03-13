package com.example.dragoncave;


import java.util.Scanner;
public class DragonCave {

    public static void displayIntro() {
        System.out.println("You are in a land full of dragons. In front of you, " + "\n" +
                "You see two caves. In one cave, the dragon is friendly" + "\n" +
                "and will share his treasure with you. The other dragon" + "\n" +
                "is greedy and hungry, and will eat you on sight" + "\n");
    }

    public static int chooseCave() {
        int cave = 0;
        try {
            Scanner in = new Scanner(System.in);
            cave = 0;
            while (cave != 1 && cave != 2) {
                System.out.println("Which cave will you go into? (1 or 2)");
                cave = in.nextInt();
            }

        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input");
            //System.exit(0);
            chooseCave();
        }
        return cave;
    }


    public static void checkCave(int chosenCave) {
        System.out.println("You approach the cave..."+ "\n" +
                "It is dark and spooky..."+ "\n" +
                "A large dragon jumps out in front of you! He opens his jaws and...");
        double friendlyCave = Math.ceil(Math.random() * 2);

        if (chosenCave == friendlyCave) {
            System.out.println("Gives you his treasure!");
        }
        else {
            System.out.println("Gobbles you down in one bite!");
        }


    }
    public static void main(String[] args) {
        Scanner inner = new Scanner(System.in);
        String playAgain = "yes";
        boolean play = true;
        while (play) {
            displayIntro();
            int caveNumber = chooseCave();
            checkCave(caveNumber);
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