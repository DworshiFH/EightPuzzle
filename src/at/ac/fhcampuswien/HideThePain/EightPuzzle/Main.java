package at.ac.fhcampuswien.HideThePain.EightPuzzle;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        System.out.println("Which heuristic function do you wish to use?");
        System.out.println("Press 0 to use Hamming.");
        System.out.println("Press 1 to use Manhattan (recommended).");
        System.out.println("Press 2 to compare Manhattan and Hamming using the same initial board.");
        System.out.println("Press 3 to do satistically relevant comparison.");
        System.out.println("Press 4 to quit.");

        Scanner scanner = new Scanner(System.in);

        while(true){
            int[][] randomBoard = getRandomBoard();

            Node initialNode;
            System.out.print("input: ");
            int in = scanner.nextInt();
            if(in == 0){
                initialNode = new Node(randomBoard);
                callSolve(initialNode, true, false);

            } else if (in == 1) {
                initialNode = new Node(randomBoard);
                callSolve(initialNode, true, true);

            } else if(in == 2){
                System.out.println("Using Manhattan");
                initialNode = new Node(randomBoard);
                callSolve(initialNode, true, true);

                System.out.println("Using Hamming");
                Node initialNode2 = new Node(randomBoard);
                callSolve(initialNode2, true, false);

            } else if(in == 3){
                System.out.println("Doing statistically relevant comparison");

                ArrayList<Statistics> statsListManhattan = new ArrayList<>();

                ArrayList<Statistics> statsListHamming = new ArrayList<>();

                for(int i = 0; i < 100; i++){
                    //do 100 games with manhattan
                    System.out.println("Manhattan #" + i);

                    randomBoard = getRandomBoard();
                    initialNode = new Node(randomBoard);
                    Solve solver = new Solve(initialNode, true);

                    statsListManhattan.add(
                            i , solver.solve(false)
                    );

                    //do the same 100 games with hamming
                    System.out.println("Hamming #" + i);

                    initialNode = new Node(randomBoard);
                    solver = new Solve(initialNode, false);

                    statsListHamming.add(
                            solver.solve(false)
                    );
                }

                writeStatsToCSV(statsListManhattan, "manhattan");

                writeStatsToCSV(statsListHamming, "hamming");

            } else if(in == 4){
                //quit while loop and quit program
                break;
            } else {
                System.out.println("Invalid input, defaulting to Manhattan Distance.");
                initialNode = new Node(randomBoard);
                callSolve(initialNode, true, true);
            }
        }
        System.out.println("\nThank you for choosing \"EightPuzzle\", by \"Hide The Pain\".");
    }

    private static Statistics callSolve(Node initialNode, boolean doResultPrint, boolean useManhattan){
        //takes a node and calls solve, also prints the node before calling solve
        Solve solver = new Solve(initialNode, useManhattan);

        long startTime = System.nanoTime();

        System.out.println("Randomly shuffled puzzle:");

        System.out.println(Arrays.toString(initialNode.getCurrentStateAsArray()[0]));
        System.out.println(Arrays.toString(initialNode.getCurrentStateAsArray()[1]));
        System.out.println(Arrays.toString(initialNode.getCurrentStateAsArray()[2]));

        Statistics ret = solver.solve(doResultPrint);

        long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Elapsed Time: " + elapsedTime/1000000 + " milliseconds");

        return ret;
    }

    private static int[][] getRandomBoard(){
        //generates a randomly shuffled board
        int moveHere;
        int[][] board = new int[][]{
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}
        };
        Node node = new Node(board);
        Node tmp;

        int indexZeroX = 0;
        int indexZeroY = 0;

        for(int i = 0; i < 1000 + getRandomInt(0, 1000); i++){

            for(int ii = 0; ii < 3; ii++){
                for(int j = 0; j < 3; j++){
                    if(node.getCurrentStateAsArray()[ii][j] == 0) {
                        indexZeroX = ii;
                        indexZeroY = j;

                        break;
                    }
                }
            }

            moveHere = getRandomInt(0, 3);

            switch (moveHere) {
                case 0 -> {
                    tmp = node.switchTiles(indexZeroX, indexZeroY, indexZeroX + 1, indexZeroY);
                    if (tmp != null) {
                        node = tmp.copyNode();
                    }
                }
                case 1 -> {
                    tmp = node.switchTiles(indexZeroX, indexZeroY, indexZeroX - 1, indexZeroY);
                    if (tmp != null) {
                        node = tmp.copyNode();
                    }
                }
                case 2 -> {
                    tmp = node.switchTiles(indexZeroX, indexZeroY, indexZeroX, indexZeroY + 1);
                    if (tmp != null) {
                        node = tmp.copyNode();
                    }
                }
                case 3 -> {
                    tmp = node.switchTiles(indexZeroX, indexZeroY, indexZeroX, indexZeroY - 1);
                    if (tmp != null) {
                        node = tmp.copyNode();
                    }
                }
                default -> {
                }
            }
            tmp = null;
        }
        return node.getCurrentStateAsArray();
    }

    private static int getRandomInt(int min, int max) {
        max ++;
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    private static void writeStatsToCSV(ArrayList<Statistics> statistics, String filename){
        ArrayList<String[]> data = new ArrayList<>();

        for(Statistics s : statistics){
            String[] temp = new String[3];

            temp[0] = Arrays.deepToString(s.getInitialState());
            temp[1] = String.valueOf(s.getNodesExpanded());
            temp[2] = String.valueOf(s.getTimeExpended());

            data.add(temp);
        }

        try{
            File file = new File("out/"+filename+".csv");
            if(!file.exists()) file = new File("out/"+filename+".csv");

            PrintWriter csvWriter = new PrintWriter(new FileWriter(file, true));

            for(String[] s: data){
                csvWriter.println(s[0] + "," + s[1] + ","+ s[2]);
            }

            csvWriter.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
