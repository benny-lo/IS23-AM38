package it.polimi.ingsw.model.commongoalcardtest.pattern;
import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPatternDistinctItems;
import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utils.Item;

/**
 * Unit test for common goal card pattern 7.
 */
public class CommonGoalCardPattern7Test {
    /**
     * Test of pattern on an empty bookshelf.
     */
    @Test
    public void testEmptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(4, true, 1, 3);
        Bookshelf bookshelf = new Bookshelf();
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern with four different kinds in four rows.
     */
    @Test
    public void testFourKindsOnFourRows()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(4, true, 1, 3);
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0;i<4;i++)
        {
            bookshelf.insert(Item.CUP,0);
            bookshelf.insert(Item.CAT,1);
            bookshelf.insert(Item.FRAME,2);
            bookshelf.insert(Item.PLANT,3);
            bookshelf.insert(Item.CAT,4);
        }
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test of pattern with three different kinds in four rows.
     */
    @Test
    public void testThreeKindsOnFourRows()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(4, true, 1, 3);
        Bookshelf bookshelf = new Bookshelf(4,4);
        for(int i=0;i<4;i++)
        {
            bookshelf.insert(Item.CUP,0);
            bookshelf.insert(Item.CAT,1);
            bookshelf.insert(Item.FRAME,2);
            bookshelf.insert(Item.FRAME,3);
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * Test of pattern with three different kinds in three rows.
     */
    @Test
    public void testThreeKindsOnThreeRows()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(4, true, 1, 3);
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0;i<3;i++)
        {
            bookshelf.insert(Item.CUP,0);
            bookshelf.insert(Item.CAT,1);
            bookshelf.insert(Item.FRAME,2);
            bookshelf.insert(Item.FRAME,3);
        }
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test of pattern with three different kinds in four rows in not full columns.
     */
    @Test
    public void testThreeKindsOnFourRowsNotFullColumns()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(4, true, 1, 3);
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0;i<4;i++)
        {
            bookshelf.insert(Item.CUP,0);
            bookshelf.insert(Item.CAT,1);
            bookshelf.insert(Item.FRAME,2);
        }
        assertFalse(pattern.check(bookshelf));
    }
}
