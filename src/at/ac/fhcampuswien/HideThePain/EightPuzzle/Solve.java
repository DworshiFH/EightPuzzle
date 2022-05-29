package at.ac.fhcampuswien.HideThePain.EightPuzzle;

import java.util.ArrayList;

public class Solve {
    private Node initial;
    private Node node_start;
    private int[][] node_goal;

    private ArrayList<Node> openList = new ArrayList<>();


    public Solve(Node initial, Node start, boolean useHeuristicOne){
        this.initial = initial;
        this.node_start = start;
        this.node_goal = initial.getGoalState();
        if(useHeuristicOne){
            start.setF(start.manhattan());
        } else {
            start.setF(start.hamming());
        }
        this.openList.add(start);
    }

    private int manhattan_G(){
        int manhattan = 0;
        int node_current_num;

        int[][] initial_board = initial.getBoardArray();
        int[][] node_board = node_start.getBoardArray();

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(node_board[i][j] == 0) continue;

                //get the tile number at i and j from initial
                node_current_num = node_board[i][j];

                //compare indexes of current num to indexes of goal position
                for(int i_diff = 0; i_diff < 3; i_diff++) {
                    for (int j_diff = 0; j_diff < 3; j_diff++) {
                        if(initial_board[i_diff][j_diff] == node_current_num){
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
                if(initial.getBoardArray()[i][j] != node_start.getBoardArray()[j][j]){
                    hamming ++;
                }
            }
        }
        return hamming;
    }

    //expand
    //get possible moves
    //

    private void solve(){

        Node node_current = openList.get(0);
        Node node_successor_tmp;
        ArrayList<Node> nodes_successor = new ArrayList<>();
        int indexOfLowest;
        int indexZeroX = 0;
        int indexZeroY = 0;

        while(this.openList.size() > 0){
            for(Node c: openList){
                if(node_current.getF() > c.getF()){
                    node_current = c;
                }
            }

            if(isGoal(node_current)) break;

            //Generate each state node_successor that comes after node_current
            //first find the empty tile (value is 0)
            forLoopFindZero:
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(node_current.getBoardArray()[i][j] == 0) {
                        indexZeroX = i;
                        indexZeroY = j;

                        break forLoopFindZero;
                    }
                }
            }

            node_successor_tmp = node_current;
            if(node_successor_tmp.switchTiles(indexZeroX, indexZeroY, indexZeroX + 1, indexZeroY + 1)) nodes_successor.add(node_successor_tmp);
            node_successor_tmp = node_current;
            if(node_successor_tmp.switchTiles(indexZeroX, indexZeroY, indexZeroX + 1, indexZeroY - 1)) nodes_successor.add(node_successor_tmp);
            node_successor_tmp = node_current;
            if(node_successor_tmp.switchTiles(indexZeroX, indexZeroY, indexZeroX - 1, indexZeroY - 1)) nodes_successor.add(node_successor_tmp);
            node_successor_tmp = node_current;
            if(node_successor_tmp.switchTiles(indexZeroX, indexZeroY, indexZeroX - 1, indexZeroY + 1)) nodes_successor.add(node_successor_tmp);

            for(Node node: nodes_successor){
                //TODO dis shite m8
            }



        }

    }

    private boolean isGoal(Node node){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(node.getBoardArray()[i][j] != node_goal[j][j]){
                    return false;
                }
            }
        }
        return true;
    }
}



