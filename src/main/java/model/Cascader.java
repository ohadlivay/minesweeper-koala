package main.java.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Cascader {
    private Tile head;
    private final Tile[][] grid;

    public Cascader(Tile head, Tile[][] grid) {
        this.head = head;
        this.grid = grid;
    }

    /**
     * this is the main cascading method
     * it executes the cascade logic using the Strategy Pattern
     */
    public ArrayList<Tile> getTilesToReveal() {
        ArrayList<Tile> cascadees = new ArrayList<>();

        if (head == null) {
            return cascadees;
        }

        Set<Tile> visited = new HashSet<>();
        Queue<Tile> queue = new ArrayDeque<>();

        visited.add(head);
        queue.add(head);

        while (!queue.isEmpty()) {
            Tile current = queue.remove();
            cascadees.add(current);

            // STRATEGY: Ask the current tile if the cascade should stop spreading
            if (current.stopsExpansion()) {
                continue;
            }

            for (Tile neighbor : getAllNeighbors(current)) {
                // STRATEGY: Ask the neighbor if it is allowed to be revealed
                if (!visited.contains(neighbor) && neighbor.isRevealable()) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return cascadees;
    }

    // === Helpers ===

    private ArrayList<Tile> getAllNeighbors(Tile tile) {
        ArrayList<Tile> neighbors = new ArrayList<>();

        int row = getTileRow(tile);
        int col = getTileCol(tile);

        if (row == -1 || col == -1) {
            return neighbors;
        }

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;

                int nr = row + dr;
                int nc = col + dc;

                if (nr >= 0 && nr < grid.length && nc >= 0 && nc < grid[nr].length) {
                    neighbors.add(grid[nr][nc]);
                }
            }
        }

        return neighbors;
    }

    private int getTileRow(Tile target) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == target) return row;
            }
        }
        return -1;
    }

    private int getTileCol(Tile target) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == target) return col;
            }
        }
        return -1;
    }
}