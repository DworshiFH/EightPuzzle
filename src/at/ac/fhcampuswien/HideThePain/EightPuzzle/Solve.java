package at.ac.fhcampuswien.HideThePain.EightPuzzle;

import java.util.ArrayList;
import java.util.Arrays;

public class Solve {

    private final Node node_start;

    private final int[][] node_goal = new int[][]{
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}
    };

    private final ArrayList<Node> openList = new ArrayList<>();

    private final ArrayList<Node> closedList = new ArrayList<>();

    private int nodesExpanded;

    private int nodesVisited;

    private final boolean useManhattan;

    public Solve(Node initial, boolean useManhattan){
        this.node_start = initial;
        this.nodesExpanded = 0;
        this.nodesVisited = 0;
        this.useManhattan = useManhattan;
    }

    public Statistics solve(boolean doResultPrint) {
        //pseudo code source: https://www.geeksforgeeks.org/a-search-algorithm/

        //Put node_start in the OPEN list with f (node_start) = h(node_start) (initialization)
        //Node node_q = openList.get(0).copyNode();

        //put the starting node on the open list (you can leave its f at zero)
        node_start.setF(0);
        openList.add(node_start.copyNode());

        //initialize working variables
        ArrayList<Node> nodes_successors;
        Node node_q = node_start.copyNode();

        long startTime = System.nanoTime();
        //while the OPEN list is not empty
        while (openList.size() > 0) {

            //find the node with the least f on the open list, call it "node_q"
            node_q.setF(1000);
            for (Node n : openList) {
                if (n.getF() < node_q.getF()) {
                    node_q = n.copyNode();
                    if (isGoal(node_q, doResultPrint)) {
                        long elapsedTime = System.nanoTime() - startTime;
                        return new Statistics(nodesExpanded, elapsedTime, node_start.getCurrentStateAsArray());
                    }
                }
            }
            //pop q off the open list
            removeFromOpenList(node_q);

            //generate q's successors, expand the node
            nodes_successors = getSuccessors(node_q);

            nodesExpanded++;

            //for each successor of node_q
            loopThroughSuccessors:
            for (Node node_successor : nodes_successors) {

                if (isGoal(node_successor, doResultPrint)) {
                    long elapsedTime = System.nanoTime() - startTime;
                    return new Statistics(nodesExpanded, elapsedTime, node_start.getCurrentStateAsArray());
                }

                //else, compute both g and h for the successor
                //successor.g = q.g + distance between successor and q (in our case is always one)
                node_successor.setG(node_q.getG() + 1);

                //successor.h = distance from goal to successor
                if(useManhattan){
                    node_successor.setH(manhattanToGoal(node_successor));
                } else {
                    node_successor.setH(hammingToGoal(node_successor));
                }

                //successor.f = successor.g + successor.h
                node_successor.setF(node_successor.getG() + node_successor.getH());

                //if a node with the same board as successor is in the OPEN list
                //which has a lower f than successor, skip this successor
                for (Node n : openList) {
                    if (equal(n.getCurrentStateAsArray(), node_successor.getCurrentStateAsArray())) {
                        if (n.getF() < node_successor.getF()) continue loopThroughSuccessors;
                    }
                }

                //if a node with the same board as successor is in the CLOSED list which has a lower f than successor, skip this successor
                for (Node n : closedList) {
                    if (equal(n.getCurrentStateAsArray(), node_successor.getCurrentStateAsArray())) {
                        if (n.getF() < node_successor.getF()) {
                            continue loopThroughSuccessors;
                        }
                    }
                }

                openList.add(node_successor.copyNode());

                nodesVisited++;
            }
            //Add node_q to the CLOSED list
            closedList.add(node_q.copyNode());
        }
        long elapsedTime = System.nanoTime() - startTime;
        return new Statistics(nodesExpanded, elapsedTime, node_start.getCurrentStateAsArray());
    }

    private boolean isGoal(Node node, boolean doResultPrint){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(node.getCurrentStateAsArray()[i][j] != node_goal[i][j]){
                    return false;
                }
            }
        }

        if(doResultPrint){
            System.out.println("Solved Puzzle:");

            System.out.println(Arrays.toString(node.getCurrentStateAsArray()[0]));
            System.out.println(Arrays.toString(node.getCurrentStateAsArray()[1]));
            System.out.println(Arrays.toString(node.getCurrentStateAsArray()[2]));

            System.out.println("\nNodes expanded: " + nodesExpanded);
            System.out.println("Nodes visited: " + nodesVisited);
        }

        return true;
    }

    private ArrayList<Node> getSuccessors(Node node){
        //returns all possible successors of a given node

        int indexZeroX = 0;
        int indexZeroY = 0;

        ArrayList<Node> successors = new ArrayList<>();

        //first find the empty tile (value is 0)
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(node.getCurrentStateAsArray()[i][j] == 0) {
                    indexZeroX = i;
                    indexZeroY = j;

                    break;
                }
            }
        }

        //then generate the successor node list
        Node node_successor_tmp1 = node.switchTiles(indexZeroX, indexZeroY, indexZeroX + 1, indexZeroY);
        if(node_successor_tmp1 != null){
            successors.add(node_successor_tmp1.copyNode());
        }

        Node node_successor_tmp2 = node.switchTiles(indexZeroX, indexZeroY, indexZeroX - 1, indexZeroY);
        if(node_successor_tmp2 != null){
            successors.add(node_successor_tmp2.copyNode());
        }

        Node node_successor_tmp3 = node.switchTiles(indexZeroX, indexZeroY, indexZeroX, indexZeroY + 1);
        if(node_successor_tmp3 != null){
            successors.add(node_successor_tmp3.copyNode());
        }

        Node node_successor_tmp4 = node.switchTiles(indexZeroX, indexZeroY, indexZeroX, indexZeroY - 1);
        if(node_successor_tmp4 != null){
            successors.add(node_successor_tmp4.copyNode());
        }
        return successors;
    }

    private void removeFromOpenList(Node node){
        //removes ALL elements in openList that are equal to node.
        for(int i = 0; i < openList.size(); i++){
            if(equal(openList.get(i).getCurrentStateAsArray(), node.getCurrentStateAsArray())){
                openList.remove(i);
            }
        }
    }

    private boolean equal(int[][] node1, int[][] node2) {
        //takes the respective board arrays of two nodes
        for (int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++){
                if( node1[i][j] != node2[i][j] ) return false;
            }
        }
        return true;
    }

    private int manhattanToGoal(Node state){

        int manhattan = 0;
        int current_num;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(state.getCurrentStateAsArray()[i][j] == 0) continue;

                //get the tile number at i and j
                current_num = state.getCurrentStateAsArray()[i][j];

                //compare indexes of current num to indexes of goal position
                for(int i_diff = 0; i_diff < 3; i_diff++) {
                    for (int j_diff = 0; j_diff < 3; j_diff++) {
                        if(node_goal[i_diff][j_diff] == current_num){
                            //add the absolute differences to int manhattan
                            manhattan = manhattan + Math.abs(i - i_diff) + Math.abs(j - j_diff);
                        }
                    }
                }
            }
        }
        return manhattan;
    }

    private int hammingToGoal(Node state) { //number of tiles not in place
        int hamming = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state.getCurrentStateAsArray()[i][j] != node_goal[i][j]) {
                    hamming++;
                }
            }
        }
        return hamming;
    }
}