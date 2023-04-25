package network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.utils.GameInfo;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {
    /**
     * Map of id -> games (the controller of that game).
     */
    private final Map<Integer, Controller> controllers;

    /**
     * List of virtual views, representing all players connected either waiting for a game or in game.
     */
    private List<VirtualView> views;

    public Lobby() {
        controllers = new HashMap<>();
        views = new ArrayList<>();
    }

    public List<GameInfo> getGameInfo() {
        List<GameInfo> ret = new ArrayList<>();
        for (Integer id : controllers.keySet()) {
            ret.add(new GameInfo(id, controllers.get(id).getNumberPlayers(), controllers.get(id).getNumberCommonGoalCards()));
        }
        return ret;
    }

    public void addVirtualView(VirtualView view) {
        views.add(view);
    }
}
