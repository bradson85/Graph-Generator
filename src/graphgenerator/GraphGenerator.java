/*
 * Graph Generator. Creates undirected wieghted graphs for test data to be used
 * for testing time complexities of certain algorithms. I.E. Prims Minimum Spanning Tree, KruskalsMST, etc.
 * NOTE: NEEDS MORE COMMENTING AND HAS DUPLICATE CODE 
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
 * @author Bradley peradotto
 * @version 1.0
 */
public class GraphGenerator {

    static final String VERSION = "Version: 1.00";

    public static void main(String[] args) {
        File file;
        Scanner in = new Scanner(System.in);
        boolean fileArgExist = false;
        String fileName = "";
        if (args.length < 4) {
            if (args.length < 1) {
                getConsoleInput(in);
            } else {
                throw new IllegalArgumentException(" Requires arugments of <vertices> <highest weight> <graph density 0.01 - 1.00> <number of graphs>. Enter -h for help.");
            }

        } else if (args.length > 6) {
            throw new IllegalArgumentException("Too many arguments. Enter -h for help.");
        } else if (args.length == 1) {
            if (args[0].charAt(0) == '-') {
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
            } else {
                fileName = args[0];
                file = new File(fileName);
                if (!checkFileName(file)) {
                    System.err.println("File name error.");
                    System.exit(1);
                } else {
                    fileArgExist = true;
                }

            }
        } else if (args.length == 4 || args.length == 5) {
            String type;
            String toFile;
            int vertInt;
            int topweightInt;
            int typeInt;
            int numGraphs;
            double densityDouble;
            boolean valid = true;
            if ((vertInt = validateInt(args[0])) < 0) {
                System.err.println("Not a valid entry for vertices.");
                valid = false;
            }
            if ((topweightInt = validateInt(args[1])) < 0) {
                System.err.println("Not a valid number for highest weight.");
                valid = false;
            }
            if ((densityDouble = validateDouble(args[2])) < 0) {
                System.err.println("Not a valid double between 0.01 and 1.0.");
                valid = false;
            }
            if ((numGraphs = validateInt(args[3])) < 0) {
                System.err.println("Not a valid entry for number of graphs.");
                valid = false;
            }
            if (!valid) {
                System.exit(1);
            } else {
                if (args.length == 5) {
                    fileName = args[4];
                    file = new File(fileName);
                    if (!checkFileName(file)) {
                        System.err.println("File name error.");
                        System.exit(1);
                    } else {
                        fileArgExist = true;
                    }

                }
                System.out.println("\nNumber of Vertices: " + args[0] + ". Weight ranges from [0-" + args[1] + "]. Graph Density: " + args[2] + "."
                        + "Number of Graphs to create: " + numGraphs + ".");
                System.out.print("\nWould you like to generate a graph of letter titled veticies or integer titled vertices? ");
                do {
                    System.out.print("(Enter 0 for letters. 1 for integers) ");
                    type = in.nextLine();
                } while ((typeInt = validateType(type)) < 0);

                Graph doneGraph = generateGraph(vertInt, topweightInt, densityDouble, typeInt, numGraphs);
                boolean correctInput = true;
                if (!fileArgExist) {
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
                } else {
                    printToFile(doneGraph, fileName);
                }
            }
        } else {
            System.err.println("Command not regognized");
            System.exit(0);
        }

    }

    private static Graph generateGraph(int verts, int toweight, double density, int type, int numGraphs) {
        // for multple graphs.
        int min = 0;
        int max = 0;
        if (type == 0) {
            Graph<String> sg = new Graph();
            for (int i = 1; i <= numGraphs; i++) {
                min = ((i * verts) - verts) + 1;
                max = i * verts;
                for (int j = min; j <= max; j++) {
                    for (int k = j + 1; k <= max; k++) {
                        if (Math.random() < density) {
                            sg.addEdgeToGraph(getLetterFromInt(j), getLetterFromInt(k), randomInt(1, toweight));
                        }
                    }
                }
                // There is a chance that no edge is formed if the density number is low enough
                // but there most be one edge for this to be a connected graph 
                //so if no edges are formed after all the random edges are added
                // then we simply check for verticies that have no edge and add them to the graph
                  int random;
                for (int m = min; m <= max; m++) {
                    if (!sg.isVertexInGraph(getLetterFromInt(m))) {
                          do {
                            random = randomInt(min, max);
                        } while (random == m);
                        sg.addEdgeToGraph(getLetterFromInt(m), getLetterFromInt(random), randomInt(1, toweight));
                    }
                }
            }
            return sg;

        } else {
            Graph<Integer> sg = new Graph();
            for (int i = 1; i <= numGraphs; i++) {
                min = ((i * verts) - verts) + 1;
                max = i * verts;
                for (int j = min; j <= max; j++) {
                    for (int k = j + 1; k <= max; k++) {
                        if (Math.random() < density) {                               //provides a probablity of an edge being formed
                            sg.addEdgeToGraph(j, k, randomInt(1, toweight));
                        }
                    }
                }
                // There is a chance that no edge is formed if the density number is low enough
                // but there most be one edge for this to be a connected graph 
                //so if no edges are formed after all the random edges are added
                // then we simply check for verticies that have no edge and add them to the graph
                int random;
                for (int m = min; m <= max; m++) {
                    if (!sg.isVertexInGraph(m)) {
                        do {
                            random = randomInt(min, max);
                        } while (random == m);
                        sg.addEdgeToGraph(m, random, randomInt(1, toweight));
                    }
                }
            }
            return sg;
        }

    }

    private static String helpMenu() {

        return "Usage:\n"
                + "  GraphGenerator [<file output path/name>] \n"
                + "  GraphGenerator [(<vertices> <highest weight> <graph density 0.01 - 1.00> <number of graphs>)] [<file output path/name>]\n"
                + "  GraphGenerator -h \n"
                + "  GraphGenerator -v \n\n"
                + "  Options: \n "
                + "     -h                 Show Help. This sceen\n"
                + "     -v                 Show Version";
    }

    private static int randomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
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
            System.err.println("Entered: " + testing + ". Must enter a number greater or equal to 1.");
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
        String numGraphs;
        String toFile;
        String fileName;
        int vertInt;
        int topweightInt;
        int typeInt;
        double densityDouble;
        int numGraphsInt;
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
        do {
            System.out.print("\nEnter the number of non-connected Graphs... ");
            numGraphs = in.nextLine();
        } while ((numGraphsInt = validateInt(numGraphs)) < 0);
        System.out.println("\nNumber of Vertices: " + verts + ". Weight ranges from [0-" + topweight + "]. Graph Density: " + densityDouble + ". "
                + "Number of Graphs to create: " + numGraphs + ".");
        System.out.print("\nWould you like to generate a graph of letter titled veticies or integer titled vertices?");
        do {
            System.out.print("(Enter 0 for letters. 1 for integers) ");
            type = in.nextLine();
        } while ((typeInt = validateType(type)) < 0);

        Graph doneGraph = generateGraph(vertInt, topweightInt, densityDouble, typeInt, numGraphsInt);
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

    private static void getConsoleInputWithFile(Scanner in, String fileName) {
        String verts;
        String topweight;
        String density;
        String type;
        String toFile;
        String numGraphs;
        int vertInt;
        int topweightInt;
        int typeInt;
        int numGraphsInt;
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
        do {
            System.out.print("\nEnter the number of non-connected Graphs... ");
            numGraphs = in.nextLine();
        } while ((numGraphsInt = validateInt(numGraphs)) < 0);
        System.out.println("\nNumber of Vertices: " + verts + ". Weight ranges from [0-" + topweight + "]. Graph Density: " + densityDouble + ". "
                + "Number of Graphs to create: " + numGraphs + ".");
        System.out.print("\nWould you like to generate a graph of letter titled veticies or integer titled vertices?");
        do {
            System.out.print("(Enter 0 for letters. 1 for integers) ");
            type = in.nextLine();
        } while ((typeInt = validateType(type)) < 0);

        Graph doneGraph = generateGraph(vertInt, topweightInt, densityDouble, typeInt, numGraphsInt);
        boolean correctInput = true;
        do {
            System.out.print(" Do you want to save graph to a file? (Enter Y for yes. N for no) ");
            toFile = in.nextLine();
            if (toFile.compareToIgnoreCase("Y") == 0) {
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

    private static void printToFile(Graph g, String file) {
        FileWriter fileWriter = null;
        String fileName = file;                // initalize as just enitre filename
        String directoryName = "";
        if (file.lastIndexOf("/") > 0) {
            fileName = file.substring(file.lastIndexOf("/"));            // if it contains a directory then split if off
            directoryName = file.substring(0, file.lastIndexOf("/"));    // get just the directory names before the lasst file slash "/"
            File dir = new File(directoryName);
            if (!dir.exists()) {                                        // make directory if not exists
                System.out.println("Making new directory(s) in: " + directoryName);
                dir.mkdirs();
            }
        }
        String saveFile = directoryName + "v" + g.getNumVertices() + "e" + g.getNumEdges() + fileName;           // create new save file name that adds the number of vertices 
        try {                                                                                                 // note if not directroy then directory will be empty sting
            System.out.println("Writing graph to file.");
            fileWriter = new FileWriter(saveFile);
            try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
                printWriter.print(g.getFormattedGraph() + " ");
                printWriter.close();
            }
            fileWriter.close();
            System.out.println("Successfully printed to file. Filename: " + saveFile + ".");
        } catch (IOException ex) {
            System.err.println("Error writing graph to file.");
            Logger.getLogger(GraphGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
