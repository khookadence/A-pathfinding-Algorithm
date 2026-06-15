package AStarPathfinding;

// Represents a single cell in the pathfinding grid
public class Cell {
    // X-coordinate (row) of the cell
    private final int x;
    // Y-coordinate (column) of the cell
    private final int y;
    // gCost: cost from start node to this cell
    private int gCost = Integer.MAX_VALUE;
    // hCost: heuristic cost from this cell to goal (estimated distance)
    private int hCost;
    // Parent cell used for path reconstruction
    private Cell parent;
    // Whether this cell is a wall (obstacle)
    private boolean isWall;
    // Character used to display the cell in visualization
    private char icon = '0';

    // Constructor: initializes a cell at the given coordinates
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Returns the row coordinate
    public int getX() {
        return x;
    }

    // Returns the column coordinate
    public int getY() {
        return y;
    }

    // Returns the cost from start to this cell
    public int getGCost() {
        return gCost;
    }

    // Sets the cost from start to this cell
    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    // Returns the heuristic cost estimate to the goal
    public int getHCost() {
        return hCost;
    }

    // Sets the heuristic cost estimate to the goal
    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    // Returns the total cost (f = g + h) used for pathfinding decisions
    public int getFCost() {
        return gCost + hCost;
    }

    // Returns the parent cell in the path
    public Cell getParent() {
        return parent;
    }

    // Sets the parent cell for path reconstruction
    public void setParent(Cell parent) {
        this.parent = parent;
    }

    // Checks if this cell is a wall
    public boolean isWall() {
        return isWall;
    }

    // Sets whether this cell is a wall
    public void setWall(boolean wall) {
        isWall = wall;
    }

    // Returns the display character for this cell
    public char getIcon() {
        return icon;
    }

    // Sets the display character for this cell
    public void setIcon(char icon) {
        this.icon = icon;
    }

    // Resets the cell for a new pathfinding search
    public void reset() {
        gCost = Integer.MAX_VALUE;
        hCost = 0;
        parent = null;
        if (!isWall) {
            icon = '0';
        }
    }
}