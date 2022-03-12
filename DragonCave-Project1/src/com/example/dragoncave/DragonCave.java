package com.example.dragoncave;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DragonCave {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("You are in a land full of dragons. In front of you,");
        System.out.println("you see two caves. In one cave, the dragon is friendly");
        System.out.println("and will share his treasure with you. The other dragon");
        System.out.println("is greedy and hungry and will eat you on sight.");


        int gamerInput;
        Scanner in = new Scanner(System.in);

        //int userInput = sc.nextInt();

        try
        {
            System.out.println("Which cave will you go into?(1 or 2)");
            gamerInput = in.nextInt();
            if(gamerInput == 1){
                System.out.println("You approach the cave...");
                System.out.println("It is dark and spooky...");
                System.out.println("A large dragon jumps out in front of you! He opens his jaws and ...");
                System.out.println("Gobbles you down in one bite!");
            }
            else if (gamerInput == 2){
                System.out.println("the dragon wants to say Hi!!");
            }
            else{
                System.out.println("Please enter a value 1 or 2 !");
            }
        }
        catch (InputMismatchException e)
        {
            System.out.print("Please Enter Valid Numbers.");
        }

    }
}