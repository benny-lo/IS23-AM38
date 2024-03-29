package it.polimi.ingsw.model.commongoalcard.pattern;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * eight or more tiles of the same type with no restrictions about the
 * position of these tiles.
 */
public class CommonGoalPattern9 implements CommonGoalPatternInterface {
    private static final int THRESHOLD = 8;

    /**
     * {@inheritDoc}
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern is present in {@code bookshelf}.
     */
    @Override
    public boolean check(Bookshelf bookshelf) {
        int[] counts = new int[Item.values().length];
        for(int i=0; i< bookshelf.getRows(); i++){
            for(int j=0; j< bookshelf.getColumns(); j++){
                if(bookshelf.tileAt(i,j)!=null) {
                    counts[bookshelf.tileAt(i, j).ordinal()]++;
                }
            }
        }

        for (int count : counts) {
            if (count >= THRESHOLD) return true;
        }
        return false;
    }
}
