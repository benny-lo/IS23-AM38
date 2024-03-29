package it.polimi.ingsw.controller.modelListenerTest;

import it.polimi.ingsw.controller.modellistener.LivingRoomListener;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class testing {@code LivingRoomListener}.
 */
public class LivingRoomListenerTest {
    /**
     * Testing for the constructor for the class
     */
    @Test
    public void testConstructor()
    {
        LivingRoomListener livingRoomListener = new LivingRoomListener();
        assertFalse(livingRoomListener.hasChanged());
        assertNotNull(livingRoomListener);
        assertNotNull(livingRoomListener.getLivingRoomUpdates());
        assertTrue(livingRoomListener.getLivingRoomUpdates().isEmpty());
    }
    /**
     * Testing for the updateState method working as intended
     */
    @Test
    public void testUpdateState()
    {
        LivingRoomListener livingRoomListener = new LivingRoomListener();
        livingRoomListener.updateState(new Position(0,0), Item.CAT);
        livingRoomListener.updateState(new Position(1,1), Item.BOOK);
        livingRoomListener.updateState(new Position(2,2), Item.CUP);
        assertTrue(livingRoomListener.hasChanged());
        Map<Position,Item> map = livingRoomListener.getLivingRoomUpdates();
        assertFalse(livingRoomListener.hasChanged());
        assertEquals(Item.CAT,map.get(new Position(0,0)));
        assertEquals(Item.BOOK,map.get(new Position(1,1)));
        assertEquals(Item.CUP,map.get(new Position(2,2)));
        assertTrue(livingRoomListener.getLivingRoomUpdates().isEmpty());
    }

}
