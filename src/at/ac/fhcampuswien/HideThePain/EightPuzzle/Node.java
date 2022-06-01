package at.ac.fhcampuswien.HideThePain.EightPuzzle;

public class Node {

    private final int[][] currentState;

    private final int[][] goalState;

    private final int[][] initialState;

    private int[][] parentState;

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

        /*if(useManhattan){
            this.h = manhattan_H();
            this.g = manhattan_G();
        } else {
            this.h = hamming_H();
            this.g = hamming_G();
        }
        this.f = g + h;*/
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

    public void setParentState(int[][] parentState) {
        this.parentState = parentState;
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

                Node ret = new Node(retBoard, this.initialState, this.useManhattan);
                ret.setParentState(newParenState);

                return ret;
            }
        }
        return null;
    }



    public Node copyNode(){
        //cloning Node manually, .clone() referenced the inner objects
        //of new Node's board to the inner objects of previous Node's board.
        int[][] newCurrentState = new int[3][3];
        int[][] newParentState = new int[3][3];

        for(int i = 0; i < 3; i++){
            System.arraycopy(this.currentState[i], 0, newCurrentState[i], 0, 3);
        }

        if(this.parentState != null){
            for(int i = 0; i < 3; i++){
                System.arraycopy(this.parentState[i], 0, newParentState[i], 0, 3);
            }
        } else {
            newParentState = null;
        }


        Node ret = new Node(newCurrentState, this.initialState, this.useManhattan);
        ret.setParentState(newParentState);

        ret.setF(this.getF());
        ret.setG(this.getG());
        ret.setH(this.getH());

        return ret;
    }

    public int distanceToInitial(){
        if(useManhattan){
            return manhattan_G();
        } else {
            return hamming_G();
        }
    }

    public int distanceToParent(){
        if(useManhattan){
            return manhattanToParent();
        } else {
            return hammingToParent();
        }
    }

    public int distanceToGoal(){
        if(useManhattan){
            return manhattanToGoal();
        } else {
            return hammingToGoal();
        }
    }

    private int manhattan_G(){
        int manhattan = 0;
        int node_current_num;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(initialState[i][j] == 0) continue;

                //get the tile number at i and j from initial
                node_current_num = initialState[i][j];

                //compare indexes of current num to indexes of goal position
                for(int i_diff = 0; i_diff < 3; i_diff++) {
                    for (int j_diff = 0; j_diff < 3; j_diff++) {
                        if(currentState[i_diff][j_diff] == node_current_num){
                            //add the absolute differences to int manhattan
                            manhattan = manhattan + Math.abs(i - i_diff) + Math.abs(j - j_diff);
                        }
                    }
                }
            }
        }
        return manhattan;
    }

    private int hamming_G(){
        int hamming = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(currentState[i][j] != initialState[j][j]){
                    hamming ++;
                }
            }
        }
        return hamming;
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

    private int manhattanToParent(){
        if(this.parentState != null){
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
                            if(parentState[i_diff][j_diff] == current_num){
                                //add the absolute differences to int manhattan
                                manhattan = manhattan + Math.abs(i - i_diff) + Math.abs(j - j_diff);
                            }
                        }
                    }
                }
            }
            return manhattan;
        }
        return 1000;
    }

    public int hammingToGoal() { //number of tiles not in place
        int hamming = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentState[i][j] != goalState[j][j]) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    private int hammingToParent () {
        if (this.parentState != null) {
            int hamming = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (currentState[i][j] != parentState[j][j]) {
                        hamming++;
                    }
                }
            }
            return hamming;
        }
        return 1000;
    }
}
