package it.polimi.ingsw.model.player;

import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.utils.Item;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * Class representing the bookshelf of a player. The bottom row and leftmost column have index 0.
 * Empty positions are represented with {@code null}.
 */
public class Bookshelf {
    /**
     * 2D array of {@code Item}s that are currently in {@code this}.
     */
    private final Item[][] grid;

    /**
     * No-args constructor for the class. It initializes {@code this} with all positions free (set to {@code null}).
     * It used for JSON.
     */
    public Bookshelf() {
        this.grid = new Item[6][5];
    }

    /**
     * Constructor of the class. It creates an empty bookshelf of the desired dimensions.
     * @param rows Number of rows.
     * @param columns Number of columns.
     */
    public Bookshelf(int rows, int columns){
        this.grid = new Item[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                grid[i][j] = null;
            }
        }
    }

    /**
     * Checks if a number of items can be inserted in a column of {@code this}.
     * @param itemsSize Number of {@code Item}s to insert.
     * @param column Column of {@code this} where the items need to be inserted (0-indexed).
     * @return {@code true} iff {@code itemsSize} items can be inserted in {@code column}.
     */
    public boolean canInsert(int itemsSize, int column) {
        if (itemsSize < 0 || itemsSize > getRows()) return false;
        if (column < 0 || column >= getColumns()) return false;

        for(int i = getRows() - 1; i >= getRows() - itemsSize; i--) {
            if (grid[i][column] != null) return false;
        }

        return true;
    }

    /**
     * Inserts an {@code Item} in the first available position in {@code column} in {@code this}.
     * @param item {@code Item} to insert.
     * @param column Column where to insert {@code item}.
     */
    public void insert(Item item, int column) {
        for(int i = 0; i < getRows(); i++) {
            if (grid[i][column] == null) {
                grid[i][column] = item;
                return;
            }
        }
    }

    /**
     * Inserts some {@code Item}s in {@code column} of {@code this}.
     * @param items {@code List<Item>} to insert in {@code this} in order from first to last.
     * @param column Column where to insert the {@code Item}s from {@code List<Item>}.
     */
    public void insert(List<Item> items, int column) {
        for(Item item : items) {
            insert(item, column);
        }
    }

    /**
     * Gets the {@code Item} at a position of {@code this}.
     * @param row Row where to look for.
     * @param column Column where to look for.
     * @return {@code Item} in position {@code row} and {@code column} of {@code this}. If the position is free, it
     * returns {@code null}.
     */
    public Item tileAt(int row, int column) {
        return grid[row][column];
    }

    /**
     * Gets {@code Item} at a {@code Position} of {@code this}.
     * @param position {@code Position} where to look for.
     * @return {@code Item} found at {@code Position} of {@code this}. If no {@code Item} at {@code Position} is found,
     * it returns {@code null}.
     */
    public Item tileAt(Position position) {
        return tileAt(position.getRow(), position.getColumn());
    }

    /**
     * Checks if {@code this} has no available positions.
     * @return {@code true} iff {@code this} has no available positions.
     */
    public boolean isFull() {
        for(int i = 0; i < getColumns(); i++) {
            if (grid[getRows() - 1][i] == null) return false;
        }
        return true;
    }

    /**
     * Checks if {@code column} is full.
     * @param column The column of {@code this} to check.
     * @return {@code true} iff {@code column} has no available positions.
     */
    public boolean isFullCol(int column)
    {
        return tileAt(getRows() - 1, column) != null;
    }

    /**
     * Checks if {@code row} is full.
     * @param row The row of {@code this} to check.
     * @return {@code true} iff {@code row} has no available positions.
     */
    public boolean isFullRow(int row)
    {
        for(int column = 0; column < getColumns(); column++) {
            if (tileAt(row, column) == null) return false;
        }
        return true;
    }

    /**
     * Gets the score given by islands of like {@code Item}s in {@code this}.
     * @return Total score achieved by all islands of like {@code Item}s in {@code this}.
     */
    public int getBookshelfScore() {
        boolean[][] visited = new boolean[6][5];
        Queue<Position> q = new ArrayDeque<>();

        int currentIslandSize;
        int result = 0;
        for(int i = 0; i < getRows(); i++) {
            for(int j = 0; j < getColumns(); j++) {
                if (visited[i][j] || grid[i][j] == null) continue;

                currentIslandSize = 0;
                q.add(new Position(i, j));

                while(!q.isEmpty()) {
                    Position p = q.remove();
                    int row = p.getRow();
                    int column = p.getColumn();

                    if (visited[row][column]) continue;
                    visited[row][column] = true;
                    currentIslandSize++;

                    if (row+1 < getRows() && grid[row+1][column] == grid[row][column] && !visited[row+1][column]) {
                        q.add(new Position(row+1, column));
                    }
                    if (row-1 > 0 && grid[row-1][column] == grid[row][column] && !visited[row-1][column]) {
                        q.add(new Position(row-1, column));
                    }
                    if (column+1 < getColumns() && grid[row][column+1] == grid[row][column] && !visited[row][column+1]) {
                        q.add(new Position(row, column+1));
                    }
                    if (column-1 > 0 && grid[row][column-1] == grid[row][column] && !visited[row][column-1]) {
                        q.add(new Position(row, column-1));
                    }
                }

                result += getIslandScore(currentIslandSize);
            }
        }
        return result;
    }

    /**
     * Converts the size of an island into a score.
     * @param islandSize The size of an island.
     * @return Score corresponding to {@code IslandSize}.
     */
    private int getIslandScore(int islandSize) {
        if (islandSize >= 6) return 8;
        else if (islandSize == 5) return 5;
        else if (islandSize == 4) return 3;
        else if (islandSize == 3) return 2;
        else return 0;
    }

    /**
     * Getter for the number of rows of {@code this}.
     * @return Number of rows of {@code this}.
     */
    public int getRows() {
        return grid.length;
    }

    /**
     * Getter for number of columns of {@code this}.
     * @return Number of columns of {@code this}.
     */
    public int getColumns() {
        if (grid.length == 0) return 0;
        return grid[0].length;
    }
}