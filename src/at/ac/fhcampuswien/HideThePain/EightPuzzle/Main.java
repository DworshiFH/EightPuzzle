package at.ac.fhcampuswien.HideThePain.EightPuzzle;

import java.util.Arrays;

public class Main {


    public static void main(String[] args) {

        System.out.println("Puzzle solved, thank you for choosing \"EightPuzzle\", by \"Hide The Pain\"");

        Node node = new Node();
        System.out.println(Arrays.toString(node.getBoardArray()[0]));
        System.out.println(Arrays.toString(node.getBoardArray()[1]));
        System.out.println(Arrays.toString(node.getBoardArray()[2]));


        System.out.println(node.manhattan());






    }
}
