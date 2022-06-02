package at.ac.fhcampuswien.HideThePain.EightPuzzle;

import java.util.Random;

public class GetRandomBoard {

    public static int[][] getRandomBoard(){

        int moveHere;
        int[][] board = new int[][]{
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}
        };
        Node node = new Node(board, board, true);
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
