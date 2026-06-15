package AStarPathfinding;

import javax.swing.*;
import java.awt.*;

// Main class that runs the A* pathfinding algorithm with GUI visualization
public class Main extends JFrame {
    // Reference to the map for pathfinding
    private final Map terrain;
    // Grid panel for drawing the visualization
    private final GridPanel gridPanel;

    // Constructor: initializes the GUI and runs pathfinding
    public Main() {
        // Create the pathfinding map
        terrain = new Map();

        // Display initial map to console
        System.out.println("Initial map:");
        terrain.display();

        // Run the A* pathfinding algorithm
        terrain.findPath();

        // Display final path to console
        System.out.println("Final path visualization:");
        terrain.display();

        // Set up the GUI window
        setTitle("A* Pathfinding Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Create and add the grid panel for visualization
        gridPanel = new GridPanel(terrain);
        add(gridPanel);

        // Pack window to fit content and display it
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Inner class for rendering the grid visualization
    private static class GridPanel extends JPanel {
        // Size of each cell in pixels
        private static final int CELL_SIZE = 40;
        // Reference to the terrain map
        private final Map map;

        // Constructor: stores reference to map
        public GridPanel(Map map) {
            this.map = map;
        }

        // Returns the preferred size of the panel
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(map.getWidth() * CELL_SIZE, map.getHeight() * CELL_SIZE);
        }

        // Paints the grid and cells
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw each cell in the grid
            Cell[][] grid = map.getGrid();
            for (int row = 0; row < map.getHeight(); row++) {
                for (int col = 0; col < map.getWidth(); col++) {
                    Cell cell = grid[row][col];
                    int x = col * CELL_SIZE;
                    int y = row * CELL_SIZE;

                    // Choose color based on cell type
                    if (cell.getIcon() == 'A') {
                        // Start point - green
                        g2d.setColor(new Color(0, 200, 0));
                    } else if (cell.getIcon() == 'B') {
                        // End point - red
                        g2d.setColor(new Color(200, 0, 0));
                    } else if (cell.getIcon() == '1') {
                        // Wall - black
                        g2d.setColor(Color.BLACK);
                    } else if (cell.getIcon() == 'x') {
                        // Path - yellow
                        g2d.setColor(Color.YELLOW);
                    } else {
                        // Empty cell - white
                        g2d.setColor(Color.WHITE);
                    }

                    // Draw filled rectangle for the cell
                    g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    // Draw border around the cell
                    g2d.setColor(Color.GRAY);
                    g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);

                    // Draw the cell icon/label
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Arial", Font.BOLD, 16));
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = String.valueOf(cell.getIcon());
                    int textX = x + (CELL_SIZE - fm.stringWidth(text)) / 2;
                    int textY = y + ((CELL_SIZE - fm.getHeight()) / 2) + fm.getAscent();
                    g2d.drawString(text, textX, textY);
                }
            }
        }
    }

    // Main method: creates and displays the visualization
    public static void main(String[] args) {
        // Create the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(Main::new);
    }
}