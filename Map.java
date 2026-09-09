package AStarPathfinding;

import java.util.ArrayList;
import java.util.List;

// Main class for A* pathfinding algorithm implementation
public class Map {
    // Grid dimensions (10x10)
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    // 2D grid storing all cells
    private final Cell[][] grid = new Cell[HEIGHT][WIDTH];
    // Open list: cells to be evaluated
    private final List<Cell> openList = new ArrayList<>();
    // Closed list: already evaluated cells
    private final List<Cell> closedList = new ArrayList<>();

    // Start and end points for pathfinding
    private Cell start;
    private Cell finish;

    // Movement costs: diagonal movement costs 14, straight movement costs 10
    public final int diagonalCost = 14;
    public final int straightCost = 10;

    // Constructor: initializes the grid and sets up walls and start/finish points
    public Map() {
        // Create all cells in the grid
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                grid[row][col] = new Cell(row, col);
                grid[row][col].setIcon('0');
            }
        }

        // Create a wall obstacle (vertical line)
        setWall(4, 0);
        setWall(4, 1);
        setWall(4, 2);
        setWall(4, 3);

        setWall(5,5);
        setWall(6,5);
        setWall(7,5);

        setWall(7,7);
        setWall(7,6);
        setWall(7,4);
        setWall(7,3);
        setWall(7,2);

        setWall(8, 9);
        setWall(8, 8);
        setWall(8, 7);
        
        // Set start point (top-left) and end point (bottom-right)
        start = grid[0][0];
        start.setIcon('A');
        finish = grid[9][9];
        finish.setIcon('B');
    }

    // Displays the current grid to console
    public void display() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                System.out.print(grid[row][col].getIcon() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Sets a cell as a wall at the given position
    public void setWall(int row, int col) {
        if (isInBounds(row, col)) {
            grid[row][col].setWall(true);
            grid[row][col].setIcon('1');
        }
    }

    // Checks if a position is within grid boundaries
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < HEIGHT && col >= 0 && col < WIDTH;
    }

    // Calculates Manhattan distance heuristic between current and goal cells
    private int heuristic(Cell current, Cell goal) {
        int dx = Math.abs(current.getX() - goal.getX());
        int dy = Math.abs(current.getY() - goal.getY());
        return dx + dy;
    }

    // Gets all valid neighbor cells (8 directions including diagonals)
    private List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        // Check all 8 surrounding cells
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                // Skip the cell itself
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }
                int nextRow = cell.getX() + rowOffset;
                int nextCol = cell.getY() + colOffset;
                // Add neighbor if it's within bounds
                if (!isInBounds(nextRow, nextCol)) {
                    continue;
                }
                neighbors.add(grid[nextRow][nextCol]);
            }
        }
        return neighbors;
    }

    // Calculates movement cost between two cells (straight = 10, diagonal = 14)
    private int movementCost(Cell from, Cell to) {
        if (from.getX() == to.getX() || from.getY() == to.getY()) {
            return straightCost;
        }
        return diagonalCost;
    }

    // Main A* pathfinding algorithm
    public void findPath() {
        // Clear open and closed lists for fresh search
        openList.clear();
        closedList.clear();

        // Reset all cells to their initial state
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                grid[row][col].reset();
            }
        }

        // Initialize start node
        start.setGCost(0);
        start.setHCost(heuristic(start, finish));
        start.setIcon('A');
        finish.setIcon('B');
        openList.add(start);

        // Main A* loop
        while (!openList.isEmpty()) {
            // Find the cell with the lowest F-cost in open list
            Cell current = openList.get(0);
            for (Cell cell : openList) {
                if (cell.getFCost() < current.getFCost() ||
                        (cell.getFCost() == current.getFCost() && cell.getHCost() < current.getHCost())) {
                    current = cell;
                }
            }

            // Move current cell from open to closed list
            openList.remove(current);
            closedList.add(current);

            // Check if we reached the goal
            if (current == finish) {
                markPath();
                return;
            }

            // Check all neighbors
            for (Cell neighbor : getNeighbors(current)) {
                // Skip walls and already closed cells
                if (neighbor.isWall() || closedList.contains(neighbor)) {
                    continue;
                }

                // Calculate tentative G-cost through current cell
                int tentativeG = current.getGCost() + movementCost(current, neighbor);
                // If this path is better, update neighbor
                if (tentativeG < neighbor.getGCost() || !openList.contains(neighbor)) {
                    neighbor.setParent(current);
                    neighbor.setGCost(tentativeG);
                    neighbor.setHCost(heuristic(neighbor, finish));

                    // Add to open list if not already there
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        // No path found
        System.out.println("No path found from A to B.");
    }

    // Marks the final path with 'x' symbols
    private void markPath() {
        Cell current = finish.getParent();
        while (current != null && current != start) {
            current.setIcon('x');
            current = current.getParent();
        }
    }

    // Returns the grid for visualization
    public Cell[][] getGrid() {
        return grid;
    }

    // Returns grid width
    public int getWidth() {
        return WIDTH;
    }

    // Returns grid height
    public int getHeight() {
        return HEIGHT;
    }
}

/*
//A* Search Algorithm
1. Initialize the open list
2. Initialize the closed list
	Put the starting node on the open list (leave its sum at zero)
3. while the open list is not empty
	a) find the node with the least sum on the open list, call it "q"
	b) pop q off open list
	c) generate q's 8 successors and set their parents to q
	d) for each successor
		i) if successor is the goal, stop search
		ii) else, compute both g and h for successor
			successor.g = q.g + distance btwn successor and q
			successor.h = distance from goal to successor
		successor.sum = successor.g + successor.h
		
		iii) if a node with the same position as successor is in the OPEN
			list which has a lower sum than successor, skip this successor
		iv) if a node with the same position as successor is in the CLOSED
			list which has a lower sum than successor, skip this successor
			otherwise, add the node to the open list
			
			end(for loop)
	e)push q on the closed list
		
		end (while loop)

//LIST OF METHODS NEEDED
1. compute g cost
2. compute h cost
3. get parent
4. generate 8 adjacent cells
5. add to open list
6. add to closed list
*/
