package at.ac.fhcampuswien.HideThePain.EightPuzzle;

public class Statistics {
    private final int nodesExpanded;
    private final long timeExpended;
    private final int[][] initialState;

    public Statistics(int nodesExpanded, long timeExpended, int[][] initialState) {
        this.nodesExpanded = nodesExpanded;
        this.timeExpended = timeExpended;
        this.initialState = initialState;
    }

    public int getNodesExpanded() {
        return nodesExpanded;
    }

    public long getTimeExpended() {
        return timeExpended;
    }

    public int[][] getInitialState() {
        return initialState;
    }
}
