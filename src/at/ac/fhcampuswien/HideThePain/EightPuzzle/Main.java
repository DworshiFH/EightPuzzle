package at.ac.fhcampuswien.HideThePain.EightPuzzle;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Which heuristic function do you wish to use?");
        System.out.println("Press 0 to use Hamming");
        System.out.println("Press 1 to use Manhattan (recommended)");
        System.out.println("Press 2 to quit the program.");

        Scanner scanner = new Scanner(System.in);

        while(true){
            int[][] randomBoard = GetRandomBoard.getRandomBoard();

            int[][] initialState = new int[3][3];

            //cloning initialState to random Board manually, .clone() referenced the inner objects
            //of initialState to the inner objects of randomBoard.
            for(int i = 0; i < 3; i++){
                System.arraycopy(randomBoard[i], 0, initialState[i], 0, 3);
            }
            Node node;
            System.out.print("input: ");
            int in = scanner.nextInt();
            if(in == 0){
                node = new Node(randomBoard, initialState, false);
            } else if (in == 1) {
                node = new Node(randomBoard, initialState, true);
            } else if(in == 2){
                break;
            } else {
                System.out.println("Invalid input, defaulting to Manhattan Distance.");
                node = new Node(randomBoard, initialState, true);
            }

            Solve solver = new Solve(node);

            long startTime = System.nanoTime();

            boolean retry = false;

            while(true){
                System.out.println("Randomly shuffled puzzle:");

                System.out.println(Arrays.toString(node.getCurrentStateAsArray()[0]));
                System.out.println(Arrays.toString(node.getCurrentStateAsArray()[1]));
                System.out.println(Arrays.toString(node.getCurrentStateAsArray()[2]));
                try{
                    retry = solver.solve();
                }catch (Exception e){
                    System.out.println(e);
                }
                if(!retry){
                    System.out.println("Trying another puzzle");
                    randomBoard = GetRandomBoard.getRandomBoard();
                    node = new Node(randomBoard, initialState, node.isUseManhattan());
                    solver = new Solve(node);
                } else {
                    break;
                }
            }

            long elapsedTime = System.nanoTime() - startTime;

            System.out.println("Elapsed Time: " + elapsedTime/1000000 + " milliseconds");
        }
        System.out.println("\nThank you for choosing \"EightPuzzle\", by \"Hide The Pain\".");
    }
}
