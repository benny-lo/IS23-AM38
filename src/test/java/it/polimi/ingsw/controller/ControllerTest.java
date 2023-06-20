package it.polimi.ingsw.controller;



import it.polimi.ingsw.controller.modelListener.BookshelfListener;
import it.polimi.ingsw.utils.action.*;
import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;

import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.server.*;

import it.polimi.ingsw.view.server.ServerUpdateViewInterface;
import it.polimi.ingsw.view.server.VirtualView;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Controller.
 */

public class ControllerTest {
    /**
     * Test {@code Controller}'s constructor.
     */
    @Test
    public void testControllerConstructor(){
        Controller controller = new Controller(4, 2, 0);
        assertEquals(4, controller.getNumberPlayers());
        assertEquals(2, controller.getNumberCommonGoalCards());
        assertFalse(controller.isStarted());
    }
    /**
     * Test {@code perform(JoinAction action)}'s method with only 2 players.
     */
    @Test
    public void testSuccessfulJoinAction(){
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));
        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));
        assertTrue(controller.getConnectedPlayers().contains(view0.getNickname()));
        assertTrue(controller.getConnectedPlayers().contains(view1.getNickname()));

        assertTrue(controller.getViews().contains(view0));
        assertTrue(controller.getViews().contains(view1));

        assertTrue(controller.getGameBuilder().getPlayers().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getPlayers().contains(view1.getNickname()));

        assertTrue(controller.getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getBookshelfListenersOwners().contains(view1.getNickname()));

        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view1.getNickname()));

    }

    /**
     * Test {@code perform(JoinAction action)}'s method trying to join while the game is already started.
     */
    @Test
    public void testUnsuccessfulJoinAction1() {
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        VirtualView view2 = new VirtualView(new MockServerConnection());
        view2.setNickname("tick");
        controller.perform(new JoinAction(view2));

        assertTrue(controller.isStarted());

        assertTrue(controller.getViews().contains(view0));
        assertTrue(controller.getViews().contains(view1));

        assertTrue(controller.getGameBuilder().getPlayers().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getPlayers().contains(view1.getNickname()));

        assertTrue(controller.getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getBookshelfListenersOwners().contains(view1.getNickname()));

        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view1.getNickname()));

        for(String player : controller.getConnectedPlayers()) {
            assertNotEquals(player, view2.getNickname());
        }

        assertFalse(controller.getViews().contains(view2));

        assertFalse(controller.getGameBuilder().getPlayers().contains(view2.getNickname()));

        assertFalse(controller.getBookshelfListenersOwners().contains(view2.getNickname()));

        assertFalse(controller.getGameBuilder().getBookshelfListenersOwners().contains(view2.getNickname()));
    }

    /**
     * Test {@code perform(JoinAction action)}'s method trying to join but the game is ended.
     */
    @Test
    public void testUnsuccessfulJoinAction2() {
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setEnded();

        VirtualView view2 = new VirtualView(new MockServerConnection());
        view2.setNickname("tick");
        controller.perform(new JoinAction(view2));

        assertTrue(controller.getViews().contains(view0));
        assertTrue(controller.getViews().contains(view1));

        assertTrue(controller.getGameBuilder().getPlayers().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getPlayers().contains(view1.getNickname()));

        assertTrue(controller.getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getBookshelfListenersOwners().contains(view1.getNickname()));

        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view1.getNickname()));

        for(String player : controller.getConnectedPlayers()) {
            assertNotEquals(player, view2.getNickname());
        }

        assertFalse(controller.getViews().contains(view2));

        assertFalse(controller.getGameBuilder().getPlayers().contains(view2.getNickname()));

        assertFalse(controller.getBookshelfListenersOwners().contains(view2.getNickname()));

        assertFalse(controller.getGameBuilder().getBookshelfListenersOwners().contains(view2.getNickname()));
    }

    /**
     * Test {@code perform(SelectionFromLivingRoomAction action)}'s method but the game is ended.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction1() {
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));
        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.setEnded();
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>()));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());

    }

    /**
     * Test {@code perform(SelectionFromLivingRoomAction action)}'s method when the sender is not the current player.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction2() {
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view1, new LinkedList<>()));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionFromLivingRoomAction action)}'s method when it's not the right game phase.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction3() {
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>()));

        assertEquals(TurnPhase.BOOKSHELF, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionFromLivingRoomAction action)}'s method but the item tiles aren't selectable.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction4() {
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(0,0)))));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionFromLivingRoomAction action)}'s method picking a selectable tile.
     */
    @Test
    public void testSuccessfulSelectionFromLivingRoomAction() {
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(1,3)))));

        assertEquals(TurnPhase.BOOKSHELF, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionColumnAndOrder1 action)}'s method but the game is ended.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder1() {
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.setEnded();
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>()));

        assertEquals(TurnPhase.BOOKSHELF, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionColumnAndOrder action)}'s method when the sender is not the current player.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder2() {
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view1,0, new LinkedList<>()));

        assertEquals(TurnPhase.BOOKSHELF, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionColumnAndOrder action)}'s method when it's not the right game phase.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder3() {
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionColumnAndOrderAction(view0,0, new LinkedList<>()));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionColumnAndOrder action)}'s method but the column isn't selectable.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder4() {
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(1,3), new Position(2, 3)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>(List.of(0, 1))));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(4,7), new Position(3, 7)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>(List.of(1, 0))));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(4,1), new Position(5, 1)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>(List.of(1, 0))));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(3,2)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>(List.of(0))));

        assertEquals(TurnPhase.BOOKSHELF, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionColumnAndOrder action)}'s method choosing a right column.
     */
    @Test
    public void testSuccessfulSelectionColumnAndOrder(){
        Controller controller = new Controller(2, 2, 0);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(1,3), new Position(2, 3)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>(List.of(0, 1))));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());
    }

    /**
     * Testing content of Join Action Messages when it fails with the nickname tick, because it's the third player in
     * a game that has 2 maximum players, and for both of the two other players as it succeeds
     */
    @Test
    public void testContentJoinAction() {
        Controller controller = new Controller(2, 2, 0);
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        MockServerConnection mockServerConnection2 = new MockServerConnection();
        VirtualView view2 = new VirtualView(mockServerConnection2);
        view2.setNickname("tick");
        controller.perform(new JoinAction(view2));

        GameData failedJoin = (GameData) mockServerConnection2.list.get(0);
        assertEquals(-1,failedJoin.getNumberPlayers());
        assertEquals(-1,failedJoin.getNumberCommonGoalCards());
        assertEquals(-1,failedJoin.getBookshelvesRows());
        assertEquals(-1,failedJoin.getBookshelvesColumns());
        assertEquals(-1,failedJoin.getLivingRoomColumns());
        assertEquals(-1,failedJoin.getLivingRoomRows());
        assertNull(failedJoin.getConnectedPlayers());

        for (Message message : mockServerConnection0.list)
            {
                if (message instanceof GameData gameData) {
                    assertEquals(0, gameData.getConnectedPlayers().size());
                    assertEquals(controller.getNumberPlayers(), gameData.getNumberPlayers());
                    assertEquals(9, gameData.getLivingRoomColumns());
                    assertEquals(9, gameData.getLivingRoomRows());
                    assertEquals(5, gameData.getBookshelvesColumns());
                    assertEquals(6, gameData.getBookshelvesRows());
                }

                if (message instanceof WaitingUpdate waitingUpdate) {
                    assertTrue(waitingUpdate.isConnected());

                    assertTrue(view0.getNickname().equals(waitingUpdate.getNickname()) || view1.getNickname().equals(waitingUpdate.getNickname()));

                    if (waitingUpdate.getNickname().equals(view0.getNickname())) {
                        assertEquals(1, waitingUpdate.getMissing());
                    } else {
                        assertEquals(0, waitingUpdate.getMissing());
                    }
                }

                if (message instanceof BookshelfUpdate bookshelfUpdate) {
                    assertTrue(bookshelfUpdate.getOwner().equals(view0.getNickname()) || bookshelfUpdate.getOwner().equals(view1.getNickname()));
                    for (Position position : bookshelfUpdate.getBookshelf().keySet()) {
                        assertNull(bookshelfUpdate.getBookshelf().get(position));
                    }
                }

                if (message instanceof CommonGoalCardsUpdate commonGoalCardsUpdate) {
                    assertNotNull(commonGoalCardsUpdate.getCommonGoalCardsUpdate());
                    assertEquals(controller.getNumberCommonGoalCards(), commonGoalCardsUpdate.getCommonGoalCardsUpdate().size());
                }

                if (message instanceof EndingTokenUpdate endingTokenUpdate) {
                    assertNull(endingTokenUpdate.getOwner());
                }

                if (message instanceof LivingRoomUpdate livingRoomUpdate) {
                    assertNotNull(livingRoomUpdate);
                    assertEquals(81, livingRoomUpdate.getLivingRoomUpdate().size());
                }

                if (message instanceof PersonalGoalCardUpdate personalGoalCardUpdate) {
                    assertEquals(personalGoalCardUpdate.getId(), controller.getGame().getPersonalID(view0.getNickname()));
                }

                if (message instanceof  ScoresUpdate scoresUpdate)
                {
                    assertEquals(2,scoresUpdate.getScores().size());
                    for(String string : scoresUpdate.getScores().keySet())
                    {
                        assertEquals(0,scoresUpdate.getScores().get(string));
                    }
                }

                if (message instanceof  StartTurnUpdate startTurnUpdate)
                {
                    assertEquals(startTurnUpdate.getCurrentPlayer(),controller.getConnectedPlayers().get(0));
                }

            }


        }

    /**
     * Testing for content of SelectionFromLivingRoomAction Messages when the action fails.
     */
    @Test
    public void testContentUnsuccessfulSelectionFromLivingRoom()
    {
        Controller controller = new Controller(2, 2, 0);
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        List<Position> pos = new ArrayList<>();
        List<Message> list1 = new ArrayList<>(mockServerConnection0.list);
        controller.perform(new SelectionFromLivingRoomAction(view0,pos));
        assertEquals(list1.size(),mockServerConnection0.list.size()-1);
        for(Message message : mockServerConnection0.list)
        {
            if(!(message instanceof SelectedItems))
            {
                assertEquals(message,list1.get(mockServerConnection0.list.indexOf(message)));
            }
        }
        SelectedItems selectedItems = (SelectedItems) mockServerConnection0.list.get(mockServerConnection0.list.size()-1);
        assertNull(selectedItems.getItems());
    }

    /**
     * Testing for content of SelectionFromLivingRoomAction Messages when the action succeeds.
     */
    @Test
    public void testContentSuccessfulSelectionFromLivingRoom()
    {
        int index = 0;
        Controller controller = new Controller(2, 2, 0);
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        List<Position> pos = new ArrayList<>();
        pos.add(new Position(1, 3));
        controller.setCurrentPlayer(view0.getNickname());
        LivingRoomUpdate oldLivingRoom ;
        for (Message message : mockServerConnection0.list) {
            if (message instanceof LivingRoomUpdate) {
                index = mockServerConnection0.list.indexOf(message);
            }
        }
        oldLivingRoom = (LivingRoomUpdate) mockServerConnection0.list.get(index);

        controller.perform(new SelectionFromLivingRoomAction(view0, pos));
        SelectedItems selectedItems = (SelectedItems) mockServerConnection0.list.get(mockServerConnection0.list.size() - 1);
        LivingRoomUpdate adjournedLivingRoom = (LivingRoomUpdate) mockServerConnection0.list.get(mockServerConnection0.list.size() - 2);
        assertNull(adjournedLivingRoom.getLivingRoomUpdate().get(new Position(1, 3)));
        assertEquals(oldLivingRoom.getLivingRoomUpdate().get(new Position(1,3)),selectedItems.getItems().get(0));

        for(Map.Entry<Position, Item> entry: ((LivingRoomUpdate) mockServerConnection0.list.get(index)).getLivingRoomUpdate().entrySet())
        {
            if(entry.getKey().getRow() != 1 && entry.getKey().getColumn() != 3)
            {
                assertEquals(entry.getValue(),oldLivingRoom.getLivingRoomUpdate().get(entry.getKey()));
            }
        }

        SelectedItems otherView = (SelectedItems) mockServerConnection1.list.get(mockServerConnection1.list.size()-1);
        assertEquals(otherView.getItems().get(0), selectedItems.getItems().get(0));
        LivingRoomUpdate livingRoomUpdate2 = (LivingRoomUpdate) mockServerConnection1.list.get(mockServerConnection1.list.size()-2);
        assertNull(livingRoomUpdate2.getLivingRoomUpdate().get(new Position(1,3)));
    }


    /**
     * Testing for the content of the Selection of Column and Order Action messages, when it fails
     */
    @Test
    public void testContentUnsuccessfulSelectionColumnAndOrder()
    {
        Controller controller = new Controller(2,2, 0);
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        List<Integer> order = new ArrayList<>();
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionColumnAndOrderAction(view0,0,order));

        AcceptedInsertion acceptedInsertion = (AcceptedInsertion) mockServerConnection0.list.get(mockServerConnection0.list.size()-1);
        assertFalse(acceptedInsertion.isAccepted());
    }

    /**
     * Testing for the content of the Selection of Column and Order Action messages, when it succeeds.
     */
    @Test
    public void testContentSuccessfulSelectionColumnAndOrder()
    {
        int index = 0;
        Controller controller = new Controller(2,2, 0);
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));
        List<Position> pos = new ArrayList<>();
        pos.add(new Position(1,3));
        List<Integer> order = new ArrayList<>();
        order.add(0);
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0,pos));
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view0,0,order));
        AcceptedInsertion acceptedInsertion;
        for(Message message : mockServerConnection0.list)
        {
            if(message instanceof AcceptedInsertion)
            {
                index = mockServerConnection0.list.indexOf(message);
            }
        }
        acceptedInsertion = (AcceptedInsertion) mockServerConnection0.list.get(index);

        assertTrue(acceptedInsertion.isAccepted());


        for (Message message : mockServerConnection0.list)
        {

            if (message instanceof BookshelfUpdate bookshelfUpdate) {
                assertTrue(bookshelfUpdate.getOwner().equals(view0.getNickname()) || bookshelfUpdate.getOwner().equals(view1.getNickname()));
                for (Position position : bookshelfUpdate.getBookshelf().keySet()) {
                    if(position.getColumn() != 0 && position.getRow() != 0)
                    {
                        assertNull(bookshelfUpdate.getBookshelf().get(position));
                    }

                }
            }

            if (message instanceof CommonGoalCardsUpdate commonGoalCardsUpdate) {
                assertNotNull(commonGoalCardsUpdate.getCommonGoalCardsUpdate());
                assertTrue(commonGoalCardsUpdate.getCommonGoalCardsUpdate().size() == 2 || commonGoalCardsUpdate.getCommonGoalCardsUpdate().size() == 0);
            }

            if (message instanceof EndingTokenUpdate endingTokenUpdate) {
                assertNull(endingTokenUpdate.getOwner());
            }

            if (message instanceof PersonalGoalCardUpdate personalGoalCardUpdate) {
                assertEquals(personalGoalCardUpdate.getId(), controller.getGame().getPersonalID(view0.getNickname()));
            }

            if (message instanceof ScoresUpdate scoresUpdate)
            {
                assertEquals(2,scoresUpdate.getScores().size());

            }

            if (message instanceof StartTurnUpdate startTurnUpdate)
            {
                assertTrue(startTurnUpdate.getCurrentPlayer().equals("nick") || startTurnUpdate.getCurrentPlayer().equals("rick"));
            }

        }

    }

    /**
     * Testing for the content of the ChatMessageAction , when it fails.
     */
    @Test
    public void testContentUnsuccessfulChatMessage()
    {
        Controller controller = new Controller(2,2, 0);
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setEnded();
        controller.perform(new ChatMessageAction(view0,"Good Game","all"));
        ChatAccepted chatAccepted = (ChatAccepted) mockServerConnection0.list.get(mockServerConnection0.list.size()-1);
        assertFalse(chatAccepted.isAccepted());
    }

    /**
     * Testing for the content of the successful ChatMessageAction when sent to all.
     */
    @Test
    public void testContentSuccessfulAllChatMessage()
    {
        int index0 = 0;
        int index1 = 0;
        Controller controller = new Controller(2,2, 0);
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.perform(new ChatMessageAction(view0,"Hello","all"));
        ChatAccepted chatAccepted = (ChatAccepted) mockServerConnection0.list.get(mockServerConnection0.list.size()-2);
        assertTrue(chatAccepted.isAccepted());
        for(Message message : mockServerConnection0.list)
        {
            if(message instanceof ChatUpdate)
            {
                index0 = mockServerConnection0.list.indexOf(message);
            }
        }
        for(Message message: mockServerConnection1.list)
        {
            if(message instanceof ChatUpdate)
            {
                index1 = mockServerConnection1.list.indexOf(message);
            }
        }
        ChatUpdate chatUpdate0 = (ChatUpdate) mockServerConnection0.list.get(index0);
        ChatUpdate chatUpdate1 = (ChatUpdate) mockServerConnection1.list.get(index1);
        assertTrue(chatUpdate0.getReceiver().equals("all") && chatUpdate1.getReceiver().equals("all"));
        assertTrue(chatUpdate0.getText().equals("Hello") && chatUpdate1.getText().equals("Hello"));
        assertTrue(chatUpdate0.getSender().equals("nick") && chatUpdate1.getSender().equals("nick"));
    }

    /**
     * Testing for the content of the successful ChatMessageAction when it is not sent to all.
     */
    @Test
    public void testContentSuccessfulChatMessage()
    {
        Controller controller = new Controller(2,2, 0);
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));
        controller.perform(new ChatMessageAction(view0,"Hello","john"));
        ChatAccepted chatAccepted = (ChatAccepted) mockServerConnection0.list.get(mockServerConnection0.list.size()-1);
        assertFalse(chatAccepted.isAccepted());

        controller.perform(new ChatMessageAction(view0,"Hello", "rick"));
        ChatUpdate chatUpdate0 = (ChatUpdate) mockServerConnection0.list.get(mockServerConnection0.list.size()-1);
        ChatUpdate chatUpdate1 = (ChatUpdate) mockServerConnection1.list.get(mockServerConnection1.list.size()-1);
        assertTrue(chatUpdate0.getSender().equals("nick") && chatUpdate1.getSender().equals("nick"));
        assertTrue(chatUpdate0.getReceiver().equals("rick") && chatUpdate1.getReceiver().equals("rick"));
        assertTrue(chatUpdate0.getText().equals("Hello") && chatUpdate1.getText().equals("Hello"));
    }

    /**
     * Testing for DisconnectionAction after the game is started.
     */
    @Test
    public void testBeforeDisconnectionAction()
    {
        Controller controller = new Controller(2,2, 0);
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.perform(new DisconnectionAction(view0));
        assertTrue(controller.getEnded());
        for(ServerUpdateViewInterface v : controller.getViews())
        {
            assertNotEquals(v,view0);
        }
        EndGameUpdate endGameUpdate = (EndGameUpdate) mockServerConnection1.list.get(mockServerConnection1.list.size()-1);
        assertNull(endGameUpdate.getWinner());
    }

    /**
     * Testing for DisconnectionAction before the game is started.
     */
    @Test
    public void testAfterDisconnectionAction()
    {
        Controller controller = new Controller(4,2, 0);
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("john");
        controller.perform(new JoinAction(view1));

        MockServerConnection mockServerConnection2 = new MockServerConnection();
        VirtualView view2 = new VirtualView(mockServerConnection2);
        view2.setNickname("rick");
        controller.perform(new JoinAction(view2));

        controller.perform(new DisconnectionAction(view0));
        for(ServerUpdateViewInterface v : controller.getViews())
        {
            assertNotEquals(v,view0);
        }
        for(String s : controller.getGameBuilder().getPlayers())
        {
            assertNotEquals(s,view0.getNickname());
        }
        for(BookshelfListener b : controller.getGameBuilder().getBookshelfListeners())
        {
            assertNotEquals(b.getOwner(), view0.getNickname());
        }
        WaitingUpdate waitingUpdate0 = (WaitingUpdate) mockServerConnection1.list.get(mockServerConnection1.list.size()-1);
        WaitingUpdate waitingUpdate1 = (WaitingUpdate) mockServerConnection2.list.get(mockServerConnection2.list.size()-1);
        assertTrue(waitingUpdate0.getNickname().equals("nick") && waitingUpdate1.getNickname().equals("nick"));
        assertTrue(waitingUpdate0.getMissing() == 2 && waitingUpdate1.getMissing() == 2);
        assertFalse(waitingUpdate0.isConnected() && waitingUpdate1.isConnected());
    }


}



