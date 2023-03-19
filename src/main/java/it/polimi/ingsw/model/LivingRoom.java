package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the LivingRoom of the game. The top row and leftmost column have index 0.
 */
public class LivingRoom {
    private final Item [][] grid;

    /**
     * Class constructor: it sets the prohibited squares and fills the rest of the squares with items.
     * @param numberPlayers number of players in the game.
     * @param bag the bag used to extract items.
     */
    public LivingRoom(int numberPlayers, Bag bag) {
        this.grid = new Item[9][9];

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                grid[i][j] = Item.LOCKED;
            }
        }

        for(int j = 3; j <= 4; j++) grid[1][j] = Item.FREE;
        for(int j = 3; j <= 5; j++) grid[2][j] = Item.FREE;
        for(int j = 2; j <= 7; j++) grid[3][j] = Item.FREE;
        for(int j = 1; j <= 7; j++) grid[4][j] = Item.FREE;
        for(int j = 1; j <= 6; j++) grid[5][j] = Item.FREE;
        for(int j = 3; j <= 5; j++) grid[6][j] = Item.FREE;
        for(int j = 4; j <= 5; j++) grid[7][j] = Item.FREE;

        if (numberPlayers >= 3) {
            grid[0][3] = Item.FREE;
            grid[2][2] = Item.FREE;
            grid[2][6] = Item.FREE;
            grid[3][8] = Item.FREE;
            grid[5][0] = Item.FREE;
            grid[6][2] = Item.FREE;
            grid[6][6] = Item.FREE;
            grid[8][5] = Item.FREE;
        }

        if (numberPlayers == 4) {
            grid[0][4] = Item.FREE;
            grid[1][5] = Item.FREE;
            grid[3][1] = Item.FREE;
            grid[4][0] = Item.FREE;
            grid[4][8] = Item.FREE;
            grid[5][7] = Item.FREE;
            grid[7][3] = Item.FREE;
            grid[8][4] = Item.FREE;
        }
        fill(bag);
    }

    /**
     * Fill the free squares of the board with items.
     * @param bag {@code Bag} object used to extract items from.
     */
    public void fill(Bag bag){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(grid[i][j] == Item.FREE){
                    grid[i][j] = bag.extract();
                }
            }
        }
    }

    /**
     * Get the item in a position of {@code this}.
     * @param row the row.
     * @param column the column.
     * @return the item in position ({@code row}, {@code column}).
     */
    public Item tileAt(int row, int column) {
        return this.grid[row][column];
    }

    /**
     * Check if a square of {@code this} in a specific position contains an item.
     * @param row the row.
     * @param column the column.
     * @return {@code true} iff an item is NOT found at position ({@code row}, {@code column}).
     */
    private boolean isEmpty(int row, int column) {
        return (grid[row][column] == Item.LOCKED || grid[row][column] == Item.FREE);
    }

    /**
     * Check if the content of square of {@code this} in a specific position contains a selectable item.
     * @param row the row.
     * @param column the column.
     * @return {@code true} iff the content of the square in position ({@code row}, {@code column}) can be selected.
     */
    public boolean selectable(int row, int column){
        if (isEmpty(row, column)) return false;

        if (row+1 < 9 && !isEmpty(row+1, column)) return true;
        if (row > 0 && !isEmpty(row-1, column)) return true;
        if (column+1 < 9 && !isEmpty(row, column+1)) return true;
        if (column > 0 && !isEmpty(row, column-1)) return true;

        return false;
    }

    /**
     * Selection of items from {@code this}.
     * @param positions the positions to select the items from.
     * @return list of selected items.
     */
    public List<Item> selectTiles(List<Position> positions) {
        List<Item> selection = new ArrayList<>();
        for(Position p : positions) {
            selection.add(grid[p.getRow()][p.getColumn()]);
        }
        return selection;
    }
}
