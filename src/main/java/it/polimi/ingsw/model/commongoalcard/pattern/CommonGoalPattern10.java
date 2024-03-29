package it.polimi.ingsw.model.commongoalcard.pattern;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * five tiles of the same type forming an X.
 */
public class CommonGoalPattern10 implements CommonGoalPatternInterface{
    /**
     * {@inheritDoc}
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern is present in {@code bookshelf}.
     */
    @Override
    public boolean check(Bookshelf bookshelf) {
        for(int i=0; i< bookshelf.getRows()-2; i++){
            for(int j=0; j< bookshelf.getColumns()-2; j++){
                if (
                        bookshelf.tileAt(i,j)==bookshelf.tileAt(i+2,j) &&
                        bookshelf.tileAt(i,j)==bookshelf.tileAt(i+1,j+1) &&
                        bookshelf.tileAt(i,j)==bookshelf.tileAt(i,j+2) &&
                        bookshelf.tileAt(i,j)==bookshelf.tileAt(i+2,j+2) &&
                        bookshelf.tileAt(i,j)!=null
                ) return true;
            }
        }
        return false;
    }
}
