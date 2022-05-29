package at.ac.fhcampuswien.HideThePain.EightPuzzle;

import java.util.Random;

public class Node {

    private int[][] board;

    private final int[][] goalState;

    private int f;

    public Node() {
        this.board = randomBoard();
        //generating a board will always return a solvable instance of an 8 Puzzle
        this.goalState = new int[][]{
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}
        };
    }

    public boolean switchTiles(int originX, int originY, int targetX, int targetY){
        if(targetX > 2 || targetX < 0) return false;
        if(targetY > 2 || targetY < 0) return false;

        if(originX == targetX || originY==targetY){
            if(originX == targetX && Math.abs(originY - targetY) == 1 || originY == targetY && Math.abs(originX - targetX) == 1){

                board[originX][originY] = board[targetX][targetY];
                board[targetX][targetY] = 0;

                return true;
            }
        }
        return false;
    }

    public int[][] getBoardArray() {
        return board;
    }

    public void setF(int f){
        this.f = f;
    }
    public int getF() {
        return f;
    }

    public int[][] getGoalState() {
        return goalState;
    }

    public void setBoard(int[][] board){
        this.board = board;
    }

    public int hamming(){ //number of tiles not in place
        int hamming = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board[i][j] != goalState[j][j]){
                    hamming ++;
                }
            }
        }
        return hamming;
    }

    public int manhattan(){
        int manhattan = 0;
        int current_num;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board[i][j] == 0) continue;

                //get the tile number at i and j
                current_num = board[i][j];

                //compare indexes of current num to indexes of goal position
                for(int i_diff = 0; i_diff < 3; i_diff++) {
                    for (int j_diff = 0; j_diff < 3; j_diff++) {
                        if(goalState[i_diff][j_diff] == current_num){
                            //add the absolute differences to int manhattan
                            manhattan = manhattan + Math.abs(i - i_diff) + Math.abs(j - j_diff);
                        }
                    }
                }
            }
        }
        return manhattan;
    }

    private int[][] randomBoard(){
        // Returns a randomly generated, solvable instance of an 8 Puzzle.
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

    private boolean Contains(int num, int[][] board) {
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

    private int getRandomInt(int min, int max) {
        max ++;
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    private boolean boardIsSolvable(int[][] board){
        //source: https://www.geeksforgeeks.org/check-instance-8-puzzle-solvable/
        //It is not possible to solve an instance of 8 puzzle if number of inversions is odd in the input state.
        //A pair of tiles form an inversion if the values on tiles are in reverse order of their appearance in goal state.
        int inv_count = 0;
        for (int i = 0; i < 3 - 1; i++)
            for (int j = i + 1; j < 3; j++)
                // 0 represents the empty tile
                if (board[j][i] > 0 && board[j][i] > board[i][j])
                    inv_count++;

        return inv_count % 2 == 0;
    }
}
