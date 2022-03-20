package HangingMan;

// Handles graphic output
public class Gallows {
    public static final int initialChances = 6;
    private int chancesRemaining;

    public Gallows() {
        reset();
    }

    public int getChancesRemaining() { return chancesRemaining; }
    public void decrementChances() { --chancesRemaining; }
    public boolean hasChances() { return chancesRemaining > 0; }
    public void reset() {chancesRemaining = initialChances;}
    public void reset(String incorrect) {
        if (incorrect == null || incorrect.isEmpty()) {
            reset();
            return;
        }
        chancesRemaining = initialChances - incorrect.length();
    }

    public void buildGallows(String hiddenWord, String incorrect) {
        System.out.println();
        System.out.println("       +--------+   ");
        displayHead();
        displayBody();
        displayLegs();
        System.out.println("                |   ");
        System.out.println("                |   ");
        System.out.println("  ==============+=  ");
        System.out.println();
        System.out.printf("%s%s%n"," ".repeat((20-hiddenWord.length())/2), hiddenWord);
        System.out.println();
        System.out.printf("%s%s%n"," ".repeat((20-incorrect.length())/2-2),incorrect);
        System.out.printf("You have %d chances remaining%n", chancesRemaining);
        System.out.println();
    }

    private void displayHead() {
        boolean drawHead = chancesRemaining < (initialChances);
        System.out.printf("       %s        |   %n", drawHead? "_":" ");
        System.out.printf("      %s %s       |   %n", drawHead? "/":" ", drawHead? "\\":" ");
        System.out.printf("      %s%s%s       |   %n", drawHead? "\\":" ", drawHead? "_":" ", drawHead? "/":" ");
    }

    private void displayBody() {
        boolean drawBody = chancesRemaining < (initialChances - 1);
        System.out.printf("    %s%s%s     |   %n", drawLeftArm()?"___":"   ", drawBody?"|":" ",drawRightArm()?"___":"   ");
        System.out.printf("       %s        |   %n", drawBody? "|":" ");
        System.out.printf("       %s        |   %n", drawBody? "|":" ");
    }

    private boolean drawLeftArm() {
        return chancesRemaining < (initialChances - 2);
    }

    private boolean drawRightArm() {
        return chancesRemaining < (initialChances - 3);
    }

    private void displayLegs() {
        System.out.printf("      %s %s       |   %n", drawLeftLeg()? "/":" ", drawRightLeg()? "\\":" ");
        System.out.printf("     %s   %s      |   %n", drawLeftLeg()? "/":" ", drawRightLeg()? "\\":" ");
    }

    private boolean drawLeftLeg() {
        return chancesRemaining < (initialChances - 4);
    }

    private boolean drawRightLeg() {
        return chancesRemaining < (initialChances - 5);
    }
}
