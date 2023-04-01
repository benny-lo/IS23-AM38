package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalCard;

import java.util.*;

/**
 * Class representing a Player.
 */

public class Player {
    private List<Item> itemsTakenFromLivingRoom;
    private final Bookshelf bookshelf;
    private PersonalGoalCard personalGoalCard;
    private final List<ScoringToken> scoringTokens;
    private boolean endingToken;

    /**
     * Player's Constructor: it initializes scores to zero, tokens to null, and it creates a Bookshelf and a PersonalGoalCard.
     */
    public Player() {
        this.itemsTakenFromLivingRoom = null;
        this.bookshelf = new Bookshelf(6, 5);
        this.personalGoalCard = null;
        this.scoringTokens = new ArrayList<>();
        this.endingToken = false;
    }

    /**
     * Add to {@code this} the items taken from the living room.
     * @param items list of the items taken from the living room.
     */
    public void takeItems(List<Item> items) {
        itemsTakenFromLivingRoom = items;
    }

    /**
     * Setter for the personal goal card.
     * @param card the card to set.
     */
    public void setPersonalGoalCard(PersonalGoalCard card) {
        this.personalGoalCard = card;
    }

    /**
     * Add a {@code ScoringToken} to {@code this} checking token's type.
     * @param token {@code ScoringToken} obtained completing a {@code CommonGoalCard}.
     */
    public void addScoringToken(ScoringToken token){
        scoringTokens.add(token);
    }

    /**
     * Add the ending token to {@code this}.
     */
    public void addEndingToken() {
        endingToken = true;
    }

    /**
     *Insert a list of {@code Item}s in the {@code Bookshelf} of {@code this} in the {@code column}.
     * @param column a {@code column} selected by the Player.
     * @param order the order to give to the items.
     */
    public void insertTiles(int column, List<Integer> order){
        List<Item> permutedItems = new ArrayList<>();
        for(int i = 0; i < order.size(); i++) {
            permutedItems.add(itemsTakenFromLivingRoom.get(order.get(i)));
        }
        if (getBookshelf().canInsert(order.size(), column))
            getBookshelf().insert(permutedItems, column);
        itemsTakenFromLivingRoom = null;
    }

    /**
     * Get the public score of {@code this}.
     * @return sum of {@code ScoringToken}s.
     */
    public int getPublicScore(){
        return (endingToken ? 1 : 0) +
                bookshelf.getBookshelfScore() +
                scoringTokens.stream().map(ScoringToken::getScore).reduce(0, Integer::sum);
    }

    /**
     * Get the total score of {@code this}.
     * @return sum of {@code ScoringToken}s, {@code personalScore} and {@code bookshelfScore}.
     */
    public int getTotalScore(){
        return getPublicScore() + getPersonalScore();
    }

    /**
     * Get the {@code Bookshelf} of {@code this}.
     * @return {@code Bookshelf} of {@code this}.
     */
    public Bookshelf getBookshelf(){
        return bookshelf;
    }

    /**
     * Get the {@code personalScore} of {@code this}.
     * @return {@code personalScore} of {@code this}.
     */
    public int getPersonalScore(){
        if (personalGoalCard == null) return 0;
        return personalGoalCard.getPersonalScore(bookshelf);
    }

    /**
     * This method tells if the {@code this} is the first to fill their {@code Bookshelf}.
     * @return It returns a boolean, true iff {@code Player} has the {@code endingToken}, else false.
     */
    public boolean firstToFinish() {
        return this.endingToken;
    }
}
