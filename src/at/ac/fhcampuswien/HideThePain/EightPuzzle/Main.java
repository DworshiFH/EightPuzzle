package at.ac.fhcampuswien.HideThePain.EightPuzzle;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Which heuristic function do you wish to use?");
        System.out.println("Press 0 to use Hamming");
        System.out.println("Press 1 to use Manhattan (recommended)");
        System.out.println("Press 2 to compare Manhattan and Hamming using the same initial board");
        System.out.println("Press 3 to quit the program.");

        Scanner scanner = new Scanner(System.in);

        while(true){
            int[][] randomBoard = getRandomBoard();

            Node initialNode;
            System.out.print("input: ");
            int in = scanner.nextInt();
            if(in == 0){
                initialNode = new Node(randomBoard, false);
                callSolve(initialNode);

            } else if (in == 1) {
                initialNode = new Node(randomBoard, true);
                callSolve(initialNode);

            } else if(in == 2){
                System.out.println("Using Manhattan");
                initialNode = new Node(randomBoard, true);
                callSolve(initialNode);

                System.out.println("Using Hamming");
                Node initialNode2 = new Node(randomBoard, false);
                callSolve(initialNode2);

            } else if(in == 3){
                break;
            } else {
                System.out.println("Invalid input, defaulting to Manhattan Distance.");
                initialNode = new Node(randomBoard, true);
                callSolve(initialNode);
            }
        }
        System.out.println("\nThank you for choosing \"EightPuzzle\", by \"Hide The Pain\".");
    }

    private static void callSolve(Node initialNode){
        //takes a node and calls solve, also prints the node before calling solve
        Solve solver = new Solve(initialNode);

        long startTime = System.nanoTime();

        System.out.println("Randomly shuffled puzzle:");

        System.out.println(Arrays.toString(initialNode.getCurrentStateAsArray()[0]));
        System.out.println(Arrays.toString(initialNode.getCurrentStateAsArray()[1]));
        System.out.println(Arrays.toString(initialNode.getCurrentStateAsArray()[2]));

        solver.solve();

        long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Elapsed Time: " + elapsedTime/1000000 + " milliseconds");
    }

    private static int[][] getRandomBoard(){
        //generates a randomly shuffled board
        int moveHere;
        int[][] board = new int[][]{
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}
        };
        Node node = new Node(board, true);
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
}
