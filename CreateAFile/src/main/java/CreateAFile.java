import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CreateAFile {
    public static void main(String[] args) {
        String data = "null";
        try {
            File myObj = new File("C:\\GenSpark\\GenSpark-Projects\\CreateAFile\\src\\main\\filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write("test");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            File myOb = new File("filename.txt");
            Scanner myReader = new Scanner(myOb);
            //List<String> listOfLines = Files.readAllLines(myOb);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                System.out.println(data);
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            }

        }

    }

