package at.ac.fhcampuswien.HideThePain.EightPuzzle;

public class Node {

    private final int[][] currentState;

    private final int[][] goalState;

    private final int[][] initialState;

    private final boolean useManhattan;

    private int f = 0;
    private int g = 0;
    private int h = 0;

    public Node(int[][] currentState, int[][] initialState, boolean useManhattan) {
        this.currentState = currentState;
        //generating a board will always return a solvable instance of an 8 Puzzle
        this.goalState = new int[][]{
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}
        };
        this.initialState = initialState;
        this.useManhattan = useManhattan;
    }

    public int[][] getCurrentStateAsArray() {
        return currentState;
    }

    public void setF(int f){
        this.f = f;
    }

    public int getF() {
        return f;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int[][] getGoalState() {
        return goalState;
    }

    public boolean isUseManhattan() {
        return useManhattan;
    }

    public Node switchTiles(int originX, int originY, int targetX, int targetY){

        int[][] retBoard = new int[3][3];

        for(int i = 0; i < 3; i++){
            System.arraycopy(currentState[i], 0, retBoard[i], 0, 3);
        }

        int[][] newParenState = new int[3][3];

        for(int i = 0; i < 3; i++){
            System.arraycopy(currentState[i], 0, newParenState[i], 0, 3);
        }

        if(targetX > 2 || targetX < 0) return null;
        if(targetY > 2 || targetY < 0) return null;

        if(originX == targetX || originY==targetY){
            if(originX == targetX && Math.abs(originY - targetY) == 1 || originY == targetY && Math.abs(originX - targetX) == 1){

                retBoard[originX][originY] = currentState[targetX][targetY];
                retBoard[targetX][targetY] = 0;

                return new Node(retBoard, this.initialState, this.useManhattan);
            }
        }
        return null;
    }

    public Node copyNode(){
        //cloning Node manually, .clone() referenced the inner objects
        //of new Node's board to the inner objects of previous Node's board.
        int[][] newCurrentState = new int[3][3];

        for(int i = 0; i < 3; i++){
            System.arraycopy(this.currentState[i], 0, newCurrentState[i], 0, 3);
        }

        Node ret = new Node(newCurrentState, this.initialState, this.useManhattan);

        ret.setF(this.getF());
        ret.setG(this.getG());
        ret.setH(this.getH());

        return ret;
    }

    public int distanceToGoal(){
        if(useManhattan){
            return manhattanToGoal();
        } else {
            return hammingToGoal();
        }
    }

    private int manhattanToGoal(){

        int manhattan = 0;
        int current_num;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(currentState[i][j] == 0) continue;

                //get the tile number at i and j
                current_num = currentState[i][j];

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

    private int hammingToGoal() { //number of tiles not in place
        int hamming = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentState[i][j] != goalState[i][j]) {
                    hamming++;
                }
            }
        }
        return hamming;
    }
}
