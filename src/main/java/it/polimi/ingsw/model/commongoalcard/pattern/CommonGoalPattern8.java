package it.polimi.ingsw.model.commongoalcard.pattern;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * four tiles of the same type in the four corners of the bookshelf.
 */
public class CommonGoalPattern8 implements CommonGoalPatternInterface {
    /**
     * {@inheritDoc}
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern is present in {@code bookshelf}.
     */
    @Override
    public boolean check(Bookshelf bookshelf) {
        return bookshelf.tileAt(0, 0) == bookshelf.tileAt(0, bookshelf.getColumns() - 1) &&
                bookshelf.tileAt(0, 0) == bookshelf.tileAt(bookshelf.getRows() - 1, 0) &&
                bookshelf.tileAt(0, 0) == bookshelf.tileAt(bookshelf.getRows() - 1, bookshelf.getColumns() - 1) &&
                bookshelf.tileAt(0, 0) != null;
    }
}
