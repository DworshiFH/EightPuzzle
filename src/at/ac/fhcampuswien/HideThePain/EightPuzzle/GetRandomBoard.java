package at.ac.fhcampuswien.HideThePain.EightPuzzle;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

            switch (moveHere){
                case 0:
                    tmp = node.switchTiles(indexZeroX, indexZeroY, indexZeroX + 1, indexZeroY);
                    if(tmp != null){
                        node = tmp.copyNode();
                    }
                    break;
                case 1:
                    tmp = node.switchTiles(indexZeroX, indexZeroY, indexZeroX - 1, indexZeroY);
                    if(tmp != null){
                        node = tmp.copyNode();
                    }
                    break;
                case 2:
                    tmp = node.switchTiles(indexZeroX, indexZeroY, indexZeroX, indexZeroY + 1);
                    if(tmp != null){
                        node = tmp.copyNode();
                    }
                    break;
                case 3:
                    tmp = node.switchTiles(indexZeroX, indexZeroY, indexZeroX, indexZeroY - 1);
                    if(tmp != null){
                        node = tmp.copyNode();
                    }
                    break;
                default:
                    break;
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

    private static int[][] getRandomBoardOld(){
        // WARNING: deprecated, may not always return a solvable instance
        // Returns a randomly generated instance of an 8 Puzzle.
        int setTileToZeroHereI = getRandomInt(0, 2); //specifies which tile is to be set to 0, 0 represents the empty tile.
        int setTileToZeroHereJ = getRandomInt(0, 2);
        int randInt;
        int i;
        int j;
        int[][] newBoard;

        do{
            newBoard = new int[3][3];
            i = 0;
            while(i < 3){
                j = 0;
                while(j < 3){
                    if(i == setTileToZeroHereI && j == setTileToZeroHereJ){
                        //On the specified tile, set Tile to 0, 0 represents the empty tile.
                        newBoard[i][j] = 0;
                        j++;
                        continue;
                    }

                    randInt = getRandomInt(1, 8); // get a random Int between 1-8
                    if(!Contains(randInt, newBoard)){ // if the random Int already in the board, do nothing
                        // if it is not in the board, add it to the board
                        newBoard[i][j] = randInt;
                        j++;
                    }
                }
                i++;
            }
        } while (!boardIsSolvable(newBoard));

        return newBoard;
    }

    private static boolean Contains(int num, int[][] board) {
        //takes a number as well as a board and checks whether the number is contained within the board
        //returns true if contained
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                if(num == board[i][j]){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean boardIsSolvable(int[][] board){
        //source: https://www.geeksforgeeks.org/check-instance-8-puzzle-solvable/
        //It is not possible to solve an instance of 8 puzzle if number of inversions is odd in the input state.
        //A pair of tiles form an inversion if the values on tiles are in reverse order of their appearance in goal state.

        int inv_count = 0;
        for (int i = 0; i < 3 - 1; i++)
            for (int j = i + 1; j < 3; j++)
                // 0 represents the empty tile
                if ((board[j][i] > 0) && (board[j][i] > board[i][j]))
                    inv_count++;

        return (inv_count % 2 == 0);
    }
}
