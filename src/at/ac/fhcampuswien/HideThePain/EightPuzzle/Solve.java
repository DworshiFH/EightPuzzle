package at.ac.fhcampuswien.HideThePain.EightPuzzle;

import java.util.ArrayList;
import java.util.Arrays;

public class Solve {

    private final Node node_start;

    private final int[][] node_goal = new int[3][3];

    private final ArrayList<Node> openList = new ArrayList<>();

    private final ArrayList<Node> closedList = new ArrayList<>();

    private int nodesExpanded;

    private int nodesVisited;

    public Solve(Node start){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                this.node_goal[i][j] = start.getGoalState()[i][j];
            }
        }
        this.node_start = start;
        this.nodesExpanded = 0;
    }

    public boolean solve() throws Exception {
        //pseudo code source: https://www.geeksforgeeks.org/a-search-algorithm/

        //Put node_start in the OPEN list with f (node_start) = h(node_start) (initialization)
        //Node node_q = openList.get(0).copyNode();

        //put the starting node on the open list (you can leave its f at zero)
        node_start.setF(0);
        openList.add(node_start.copyNode());

        //initialize working variables
        ArrayList<Node> nodes_successors;
        Node node_q = node_start.copyNode();

        //while the OPEN list is not empty
        whileLoop:
        while(openList.size() > 0){

            //find the node with the least f on the open list, call it "node_q"
            node_q.setF(1000);
            for(Node n: openList){
                if(n.getF() < node_q.getF()){
                    node_q = n.copyNode();
                    if(isGoal(node_q)){
                        //break whileLoop;
                        return true;
                    }
                }
            }
            //pop q off the open list
            removeFromOpenList(node_q);

            //generate q's successors
            nodes_successors = getSuccessors(node_q);

            //for each successor of node_q
            forLoopSuccessor:
            for(Node node_successor: nodes_successors){

                if(isGoal(node_successor)){
                    //break whileLoop;
                    return true;
                }

                //else, compute both g and h for the successor
                //successor.g = q.g + distance between successor and q

                node_successor.setG(
                        node_q.getG() + node_successor.distanceToParent()
                );
                //successor.h = distance from goal to successor
                node_successor.setH(node_successor.distanceToGoal());

                //successor.f = successor.g + successor.h
                node_successor.setF(node_successor.getG() + node_successor.getH());

                //if a node with the same board as successor is in the OPEN list
                //which has a lower f than successor, skip this successor
                for(Node n: openList){
                    if(equal(n.getCurrentStateAsArray(), node_successor.getCurrentStateAsArray())){
                        if(n.getF() < node_successor.getF()) continue forLoopSuccessor;
                    }
                }

                //if a node with the same board as successor is in the CLOSED list which has a lower f than successor,
                //skip this successor, otherwise add the node to the open list
                for(Node n: closedList){
                    if(equal(n.getCurrentStateAsArray(), node_successor.getCurrentStateAsArray())){
                        if(n.getF() < node_successor.getF()){
                            openList.add(node_successor);
                            removeFromClosedList(node_successor);
                            continue forLoopSuccessor;
                        }
                    }
                }

                openList.add(node_successor.copyNode());

                nodesVisited ++;
            }
            //Add node_q to the CLOSED list
            closedList.add(node_q.copyNode());

            nodesExpanded ++;
            //if(nodesExpanded % 1000 == 0) System.out.println(nodesExpanded + " nodes expanded.");
            if(nodesExpanded > 20000){ //most puzzles are solved with less than 20000 expanded nodes
                throw new Exception("Too difficult or unsolvable!");
            }
        }
        return false;
    }

    private boolean isGoal(Node node){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(node.getCurrentStateAsArray()[i][j] != node_goal[i][j]){
                    return false;
                }
            }
        }
        System.out.println("Solved Puzzle:");

        System.out.println(Arrays.toString(node.getCurrentStateAsArray()[0]));
        System.out.println(Arrays.toString(node.getCurrentStateAsArray()[1]));
        System.out.println(Arrays.toString(node.getCurrentStateAsArray()[2]));

        System.out.println("\nNodes expanded: " + nodesExpanded);
        System.out.println("Nodes visited: " + nodesVisited);

        return true;
    }

    private ArrayList<Node> getSuccessors(Node node){

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
        for(int i = 0; i < openList.size(); i++){
            if(equal(openList.get(i).getCurrentStateAsArray(), node.getCurrentStateAsArray())){
                openList.remove(i);
            }
        }
    }

    private void removeFromClosedList(Node node){
        for(int i = 0; i < closedList.size(); i++){
            if(equal(closedList.get(i).getCurrentStateAsArray(), node.getCurrentStateAsArray())){
                closedList.remove(i);
            }
        }
    }

    private boolean equal(int[][] node1, int[][] node2) {
        for (int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++){
                if( node1[i][j] != node2[i][j] ) return false;
            }
        }
        return true;
    }
}



