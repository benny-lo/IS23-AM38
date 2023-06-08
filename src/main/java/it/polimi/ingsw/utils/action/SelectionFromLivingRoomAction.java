package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.view.server.ServerUpdateViewInterface;

import java.util.ArrayList;
import java.util.List;

public class SelectionFromLivingRoomAction extends Action {
    private final List<Position> positions;

    /**
     * Constructor for the class.
     * @param view - the view of the player selecting the positions
     * @param positions - list of the postions selected.
     */
    public SelectionFromLivingRoomAction(ServerUpdateViewInterface view, List<Position> positions) {
        super(view);
        this.positions = positions;
    }

    /**
     * Getter for the selected position
     * @return - the array List of the positions.
     */
    public List<Position> getSelectedPositions() {
        return new ArrayList<>(positions);
    }
}
