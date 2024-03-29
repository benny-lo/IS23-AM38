package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.view.UpdateViewInterface;

/**
 * Interface processing updates sent to the client from the server-side.
 */
public interface ServerUpdateViewInterface extends UpdateViewInterface {
    /**
     * Setter for the nickname.
     * @param nickname The nickname to set.
     */
    void setNickname(String nickname);

    /**
     * Setter for the {@code Controller} that {@code this} joined.
     * @param controller The {@code Controller} joined.
     */
    void setController(ControllerInterface controller);

    /**
     * Checks if the client is joined in game (i.e. has a controller).
     * @return {@code true} iff the client is joined in a game.
     */
    boolean inGame();
}
