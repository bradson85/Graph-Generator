/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphgenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bperado
 */
public class GraphGenerator {

    static final String VERSION = "Version: 1.00";

    public static void main(String[] args) {
        File file;
        Scanner in = new Scanner(System.in);
        if (args.length < 3) {
            if (args.length < 1) {
                getConsoleInput(in);
            } else {
                throw new IllegalArgumentException(" Requires arugments of <vertices> <highest weight> <graph density 0.01 - 1.00>. Enter -h for help.");
            }

        } else if (args.length > 5) {
            throw new IllegalArgumentException("Too many arguments. Enter -h for help.");
        } else if (args.length == 1 && args[0].charAt(0) == '-') {
            switch (args[0].charAt(1)) {
                case 'h':
                    System.out.println(helpMenu());
                    break;
                case 'v':
                    System.out.println("Version: " + VERSION);
                    break;
                default:
                    System.err.println("Command option not regognized");
                    System.exit(0);
            }
        } else if (args.length == 3 || args.length == 4) {
            String type;
            boolean valid = true;
            if (validateInt(args[0]) < 0) {
                System.err.println("Not a valid entry for vertices.");
                valid = false;
            }
            if (validateInt(args[1]) < 0) {
                System.err.println("Not a valid number for highest weight.");
                valid = false;
            }
            if (validateDouble(args[2]) < 0) {
                System.err.println("Not a valid double between 0.01 and 1.0.");
                valid = false;
            }
            if (!valid) {
                System.exit(1);
            } else {
                if (args.length == 4) {
                    file = new File(args[4]);
                    if (!checkFileName(file)) {
                        System.err.println("File name error.");
                        System.exit(1);
                    }

                }
                System.out.println("\nNumber of Vertices: " + args[0] + ". Weight ranges from [0-" + args[1] + "]. Graph Density: " + args[2] + ".");
                System.out.print("Would you like to generate a graph of letter titled veticies or integer titled vertices? ");

                do {
                    System.out.print("(Enter 0 for letters. 1 for integers) ");
                    type = in.nextLine();
                } while (validateType(type) < 0);
            }
        } else {
            System.err.println("Command not regognized");
            System.exit(0);
        }

    }

    private static Graph generateGraph(int verts, int toweight, double density, int type) {

        if (type == 0) {
            Graph<String> sg = new Graph();
            for (int i = 1; i <= verts; i++) {
                for (int j = i + 1; j <= verts; j++) {
                    if (Math.random() < density) {
                        sg.addEdgeToGraph(getLetterFromInt(i), getLetterFromInt(j), randomInt(toweight));
                    }
                }
            }
            return sg;

        } else {
            Graph<String> sg = new Graph();
            for (int i = 1; i <= verts; i++) {
                for (int j = i + 1; j <= verts; j++) {
                    if (Math.random() < density) {
                        sg.addEdgeToGraph(i, j, randomInt(toweight));
                    }
                }
            }
            return sg;
        }

    }

    private static String helpMenu() {

        return "Usage:\n"
                + "  GraphGenerator [<file output path/name>] \n"
                + "  GraphGenerator [(<vertices> <highest weight> <graph density 0.01 - 1.00>)] [<file output path/name>]\n"
                + "  GraphGenerator -h \n"
                + "  GraphGenerator -v \n\n"
                + "  Options: \n "
                + "     -h                 Show Help. This sceen\n"
                + "     -v                 Show Version";
    }

    private static int randomInt(int topend) {
        Random rand = new Random();
        return rand.nextInt((topend - 1) + 1) + 1;
    }

    private static int validateInt(String testing) {

        int type;
        try {
            type = Integer.parseInt(testing);
        } catch (NumberFormatException ex) {
            System.err.println(testing + " is not an integer.");
            return -1;
        }
        if (type < 1) {
            System.err.println("Entered: " + testing + ". Must enter a number greater than 1.");
            return -1;
        } else {
            return type;
        }
    }

    private static int validateType(String testing) {
        int type;
        try {
            type = Integer.parseInt(testing);
        } catch (NumberFormatException ex) {
            System.err.println(testing + " is not an integer.");
            return -1;
        }
        if (type < 0 || type > 1) {
            System.err.println(testing + " is not a 0 or 1");
            return -1;
        } else {
            return type;
        }
    }

    private static double validateDouble(String testing) {
        double result;
        try {
            result = Double.parseDouble(testing);
        } catch (NumberFormatException ex) {
            System.err.println(testing + " is not a double.");
            return -1.1;
        }
        if (result < 0.01 || result > 1.0) {
            System.err.println(testing + " is not in the range [0.01 -1.0]");
            return -1.1;
        } else {
            return result;
        }
    }

    private static void getConsoleInput(Scanner in) {
        String verts;
        String topweight;
        String density;
        String type;
        String toFile;
        String fileName;
        int vertInt;
        int topweightInt;
        int typeInt;
        double densityDouble;
        do {
            System.out.print("Enter Number of Vertices... ");
            verts = in.nextLine();
        } while ((vertInt = validateInt(verts)) < 0);
        do {
            System.out.print("\nEnter Highest Edge Weight... ");
            topweight = in.nextLine();
        } while ((topweightInt = validateInt(topweight)) < 0);
        do {
            System.out.print("\nEnter graph density (between 0.01 and 1.00)... ");
            density = in.nextLine();
        } while ((densityDouble = validateDouble(density)) < 0);
        System.out.println("\nNumber of Vertices: " + verts + ". Weight ranges from [0-" + topweight + "]. Graph Density: " + densityDouble + ".");

        System.out.print("\nWould you like to generate a graph of letter titled veticies or integer titled vertices?");
        do {
            System.out.print("(Enter 0 for letters. 1 for integers) ");
            type = in.nextLine();
        } while ((typeInt = validateType(type)) < 0);

        Graph doneGraph = generateGraph(vertInt, topweightInt, densityDouble, typeInt);
        boolean correctInput = true;
        do {
            System.out.print(" Do you want to save graph to a file? (Enter Y for yes. N for no) ");
            toFile = in.nextLine();
            if (toFile.compareToIgnoreCase("Y") == 0) {
                System.out.print("\n Please enter the file path/name. ");
                fileName = in.nextLine();
                printToFile(doneGraph, fileName);
                correctInput = true;

            } else if (toFile.compareToIgnoreCase("N") == 0) {
                System.out.print(doneGraph.getFormattedGraph());
                break;
            } else {
                System.out.println("\nPlease enter Y or N.");
                correctInput = false;
            }
        } while (!correctInput);
        System.out.println();
    }

    private static boolean checkFileName(File name) {
        try {
            //name.getCanonicalPath();
            System.out.println("Checking File Name Format: " + name.getCanonicalPath() + " is valid.");
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private static String getLetterFromInt(int t) {
        if (t < 1) {
            System.err.println("Negative integer in integer to ascii letter conversion");
            System.exit(1);
        }
        String finalString = "";
        int multiplier = (t - 1) / 26;                               // this is a multipler to make nodes lik AA BB, CCC etc.
        int letterNum = ((t - 1) % 26) + 65;                       //-1 to adjsut for non zero entries. plus 65 to get Ascii char A is 45
        String letter = Character.toString((char) letterNum); // mod 26 since 26 letters will give the code for letter
        for (int i = 0; i <= multiplier; i++) {
            finalString += letter;
        }
        return finalString;
    }

    private static void printToFile(Graph g, String fileName) {
        FileWriter fileWriter = null;
        
        if (fileName.lastIndexOf("/") > 0) {
            String directoryName = fileName.substring(0, fileName.lastIndexOf("/"));
            File dir = new File(directoryName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
       
        try {
            fileWriter = new FileWriter(fileName);
            try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
                printWriter.print(g.getFormattedGraph() + " ");
                printWriter.close();
            }
            fileWriter.close();
        } catch (IOException ex) {
            System.err.println("Error writing graph to file.");
            Logger.getLogger(GraphGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
