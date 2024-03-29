package it.polimi.ingsw.model.commongoalcard.pattern;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.HashSet;
import java.util.Set;

/**
 * Class representing patterns 2, 5, 6, 7. The general pattern is the following:
 * {@code numLines} full lines (rows or columns exclusively) containing each a number of distinct item types between
 * {@code minTypes} and {@code maxTypes}.
 */
public class CommonGoalPatternDistinctItems implements CommonGoalPatternInterface {
    private final int numLines;
    private final boolean rowOrColumn;
    private final int minTypes;
    private final int maxTypes;

    /**
     * Constructor for {@code this} class. It sets all private fields of {@code this}.
     * @param numLines The number of rows/columns required by the pattern.
     * @param rowOrColumn {@code true} if the pattern considers rows, else {@code false} if patterns considers columns.
     * @param minTypes The minimum number of distinct types per line.
     * @param maxTypes The maximum number of distinct types per line.
     */
    public CommonGoalPatternDistinctItems(int numLines, boolean rowOrColumn, int minTypes, int maxTypes) {
        this.numLines = numLines;
        this.rowOrColumn = rowOrColumn;
        this.minTypes = minTypes;
        this.maxTypes = maxTypes;
    }

    /**
     * {@inheritDoc}
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern is present in {@code bookshelf}.
     */
    @Override
    public boolean check(Bookshelf bookshelf) {
        if (rowOrColumn) return checkRow(bookshelf);
        else return checkColumn(bookshelf);
    }

    /**
     * Checks if pattern is satisfied on the rows.
     * @param bookshelf The bookshelf to check the pattern on.
     * @return {@code true} iff the pattern is satisfied by {@code bookshelf} on the rows.
     */
    private boolean checkRow(Bookshelf bookshelf) {
        int counter = 0;
        Set<Item> itemsRow = new HashSet<>();
        for(int row = 0; row < bookshelf.getRows(); row++) {
            if (!bookshelf.isFullRow(row)) continue;
            itemsRow.clear();
            for(int column = 0; column < bookshelf.getColumns(); column++) {
                if (bookshelf.tileAt(row, column) == null) continue;
                itemsRow.add(bookshelf.tileAt(row, column));
            }
            if (itemsRow.size() >= minTypes && itemsRow.size() <= maxTypes) counter++;
        }
        return counter >= numLines;
    }

    /**
     * Checks if the pattern is satisfied on the columns.
     * @param bookshelf The bookshelf to check the pattern on.
     * @return {@code true} iff the pattern is satisfied by {@code bookshelf} on the columns.
     */
    private boolean checkColumn(Bookshelf bookshelf) {
        int counter = 0;
        Set<Item> itemsColumn = new HashSet<>();
        for(int column = 0; column < bookshelf.getColumns(); column++) {
            if (!bookshelf.isFullCol(column)) continue;
            itemsColumn.clear();
            for(int row = 0; row < bookshelf.getRows(); row++) {
                if (bookshelf.tileAt(row, column) == null) continue;
                itemsColumn.add(bookshelf.tileAt(row, column));
            }
            if (itemsColumn.size() >= minTypes && itemsColumn.size() <= maxTypes) counter++;
        }
        return counter >= numLines;
    }
}
