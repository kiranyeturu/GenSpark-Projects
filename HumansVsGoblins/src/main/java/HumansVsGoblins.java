
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

class HumanVsGoblins {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int gridW, gridH, humanNum, goblinNum, posX, posY;

        // Print out introduction
        System.out.println("==========================================");
        System.out.println("  Welcome to Human vs. Goblin Simulator  ");
        System.out.println("==========================================");
        System.out.println();

        // Get the size of the grid
        System.out.print("Width of the grid (1-500): ");
        gridW = in.nextInt();
        if (gridW<1 || gridW>500)
            gridW = 10; // In case input is wrong, set 10 as default;

        System.out.print("Height of the grid (1-500): ");
        gridH = in.nextInt();
        if (gridH<1 || gridH>500)
            gridH = 10; // In case input is wrong, set 10 as default;

        // Init Grid
        Grid grid = new Grid(gridW, gridH);
        System.out.println();

        // Create GUI - Upcoming
        /*JFrame frame = new JFrame("Humans vs. Goblins");    
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);              
        frame.setSize(gridW+40,gridH+40);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);*/

        // Get the number of creatures
        System.out.print("Number of human (0-"+grid.getArea()+"): ");
        humanNum = in.nextInt();
        if (humanNum<0 || humanNum>grid.getArea()){
            humanNum = grid.getArea()/3;   // In case input is wrong, set one third as default;
        }

        System.out.print("Number of goblins (0-"+(grid.getArea()-humanNum)+"): ");
        goblinNum = in.nextInt();
        if (goblinNum<0 || goblinNum>grid.getArea()-humanNum){
            goblinNum = grid.getArea()/3;   // In case input is wrong, set one third as default;
        }

        // Fill Grid with creatures        
        for(int i=0;i<humanNum;i++){
            do {
                posX = Helpers.randomNum(0,grid.getWidth()-1);
                posY = Helpers.randomNum(0,grid.getHeight()-1);
            } while (!grid.addCreature(posX, posY, new Creature("human",Helpers.randomNum(1,10),1)));
        }

        for(int i=0;i<goblinNum;i++){
            do {
                posX = Helpers.randomNum(0,grid.getWidth()-1);
                posY = Helpers.randomNum(0,grid.getHeight()-1);
            } while (!grid.addCreature(posX, posY, new Creature("goblin",Helpers.randomNum(1,10),1)));
        }

        System.out.println();
        System.out.print("You have just created Human vs Goblin grid size of "+grid.getSizeString()+" ");
        System.out.println("with "+humanNum+" humans and "+goblinNum+" Goblins!");
        System.out.println();

        // Start Simulation                 
        Timer timer = new Timer();
        timer.schedule(new Simulator(grid, timer),0,500);
    }
}

class Simulator extends TimerTask {
    public Grid grid;
    public Timer timer;
    public int days=1;

    public Simulator(Grid grid, Timer timer){
        this.grid = grid;
        this.timer = timer;
    }

    public void run() {
        // First process fights
        for(int y=0;y<grid.getHeight();y++){
            for(int x=0;x<grid.getWidth();x++){
                if (grid.hasCreature(x,y)){
                    grid.proccessCreature(x,y);
                }
            }
        }

        // Then move creatures around, so its more exciting
        // @todo: its possible that some of them will move more than once, let them have that for now
        for(int y=0;y<grid.getHeight();y++){
            for(int x=0;x<grid.getWidth();x++){
                if (grid.hasCreature(x,y)){
                    grid.moveCreature(x,y);
                }
            }
        }
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        grid.printOut();

        if (grid.haveWinner()){
            timer.cancel();
            System.out.println();
            System.out.println("After "+days+" days winners are "+grid.getWinner()+"!!!!");
        }

        days++;
    }
}

class Grid {
    public int width;
    public int height;
    public int area;
    public GridPoint[][] points;
    public String winner;

    public Grid(int width, int height){
        this.width = width;
        this.height = height;
        this.area = width*height;

        this.points = new GridPoint[this.width][this.height];
    }

    public boolean addCreature(int x, int y, Creature creature){
        if (this.points[x][y] == null){
            this.points[x][y] = new GridPoint(creature);
            return true;
        } else {
            return false;
        }
    }

    public String getSizeString(){
        String size;
        size = Integer.toString(this.width)+'x'+Integer.toString(this.height);
        return size;
    }

    public boolean haveWinner(){
        boolean hasHuman=false, hasGoblin=false;
        for(int y=0;y<this.height;y++){
            for(int x=0;x<this.width;x++){
                if (this.hasCreature(x,y)){
                    Creature creature = this.points[x][y].getContent();
                    if (creature.getStatus()>0){
                        if (creature.getType()=="human") hasHuman = true;
                        if (creature.getType()=="goblin") hasGoblin = true;
                    }
                }
            }
        }

        if (!hasHuman && hasGoblin){
            this.winner = "Goblins";
            return true;
        } else if (!hasGoblin && hasHuman){
            this.winner = "Humans";
            return true;
        } else {
            return false;
        }

    }

    public String getWinner(){
        return this.winner;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public int getArea(){
        return this.area;
    }

    public boolean hasCreature(int x, int y){
        if (this.points[x][y] != null){
            return true;
        } else {
            return false;
        }
    }

    public void proccessCreature(int posX, int posY){
        Creature creature = this.points[posX][posY].getContent();
        Creature oponent;
        // We need to check all points/fields around this creature
        for(int x=posX-1;x<=posX+1;x++){
            if (x<0 || x>this.width-1)
                continue;  // dont go out of boundaries

            for(int y=posY-1;y<=posY+1;y++){
                if (y<0 || y>this.height-1)
                    continue;  // dont go out of boundaries

                if (this.points[x][y] != null){
                    // We have found oponent
                    oponent = this.points[x][y].getContent();
                    if (creature.getType() != oponent.getType() && oponent.getStatus() >0){
                        if (creature.getStrength() > oponent.getStrength()){
                            oponent.setStatus(0);
                            if (oponent.getType() == "human") oponent.setType("goblin");
                            this.points[x][y].setContent(oponent);
                            //System.out.println(creature.getType()+" just killed "+oponent.getType()+"!");
                        } else if (creature.getStrength() < oponent.getStrength() && oponent.getStatus() >0){
                            creature.setStatus(0);
                            if (creature.getType() == "human") creature.setType("goblin");
                            this.points[posX][posY].setContent(creature);
                            //System.out.println(oponent.getType()+" just killed "+creature.getType()+"!");
                        } else if (creature.getStrength() == oponent.getStrength()){
                            oponent.setStatus(0);
                            this.points[x][y].setContent(oponent);
                            oponent.setStatus(0);
                            this.points[x][y].setContent(oponent);
                            //System.out.println(creature.getType()+" and "+oponent.getType()+" just killed each other!");
                        }
                    }
                }
            }
        }

    }

    public void moveCreature(int posX, int posY){
        int x,y,count = 1;
        boolean moved = false;
        Creature creature = this.points[posX][posY].getContent();

        do {
            x = Helpers.randomNum(posX-1,posX+1);
            y = Helpers.randomNum(posY-1,posY+1);

            if (x<0 || x>this.width-1 || y<0 || y>this.height-1)
                continue; // Dont let him out of boundaries

            // Check if there already is creature
            if (!this.hasCreature(x,y)){
                this.points[x][y] = new GridPoint(creature);
                this.points[posX][posY] = null;
                moved = true;
            }

            count++;
        } while(!moved && count<10);

    }

    public void printOut(){
        String line = "";
        for(int i=0;i<((this.width)*4)+1;i++){
            line = line+"-";
        }

        System.out.print("      ");
        for(int x=0;x<this.width;x++){
            System.out.print(" "+x+"  ");
        }
        System.out.println();

        System.out.println("     "+line);

        for(int y=0;y<this.height;y++){
            System.out.print("   "+y+" |");

            for(int x=0;x<this.width;x++){
                if(this.points[x][y] != null){
                    Creature creature = this.points[x][y].getContent();
                    if (creature.getStatus() > 0){
                        String type = creature.getType();
                        System.out.print(" "+type.charAt(0)+" |");
                    } else {
                        System.out.print("   |");
                    }
                } else {
                    System.out.print("   |");
                }
            }

            System.out.println();
            System.out.println("     "+line);
        }
    }
}

class GridPoint{
    public Creature content;

    public GridPoint(Creature creature){
        this.content = creature;
    }

    public Creature getContent(){
        return this.content;
    }

    public void setContent(Creature creature){
        this.content = creature;
    }
}

class Creature {
    public String type;
    public int strength;
    public int status;

    public Creature(String type, int strength, int status){
        this.type = type;
        this.strength = strength;
        this.status = status;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public int getStrength(){
        return this.strength;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }
}

class Helpers{
    public static int randomNum(int min, int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}