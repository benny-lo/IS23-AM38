package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.VirtualView;

import java.util.*;

// TODO: propagate exceptions in Model and (add methods get dimensions --> where?).

// TODO: remove VirtualViews from lobby and controller with ServerUpdateView ?
// TODO: idea = make both lobby and game action listeners.

// TODO: better GameData creation.

public class Lobby {
    /**
     * Map of id -> games (the controller of that game).
     */
    private final Map<Integer, Controller> controllers;

    /**
     * Set of virtual views, representing all players connected either waiting for a game or in game.
     */
    private final Set<VirtualView> views;

    /**
     * Smallest available id for the next game to be created.
     */
    private int availableId;

    private static Lobby instance;
    private final static Object lock = new Object();

    private Lobby() {
        controllers = new HashMap<>();
        views = new HashSet<>();
        availableId = 0;
    }

    public static Lobby getInstance() {
        synchronized (lock) {
            if (instance == null) instance = new Lobby();
        }
        return instance;
    }

    private List<GameInfo> getGameInfo() {
        List<GameInfo> ret = new ArrayList<>();
        for (Integer id : controllers.keySet()) {
            if (!controllers.get(id).isStarted())   //If a game has already started, it's not displayed.
                ret.add(new GameInfo(id, controllers.get(id).getNumberPlayers(), controllers.get(id).getNumberCommonGoalCards()));
        }
        return ret;
    }

    private void addController(Controller controller) {
        controllers.put(availableId, controller);
        availableId++;
    }

    public synchronized void removeController(Controller controller) {
        Integer id = controllers.entrySet().stream().filter(entry -> controller.equals(entry.getValue())).findFirst().map(Map.Entry::getKey).orElse(-1);

        if (id != -1) controllers.remove(id);

        // notify not playing views that id is no longer available.
        (new Thread(() -> {
            List<GameInfo> list = new ArrayList<>();
            list.add(new GameInfo(id, -1, -1));
            for (VirtualView v : views) {
                if (v.isLoggedIn() && !v.isInGame()) v.onGamesList(new GamesList(list));
            }
        })).start();
    }

    public synchronized void addVirtualView(VirtualView view) {
        views.add(view);
    }

    public synchronized void removeVirtualView(VirtualView view) {
        views.remove(view);
    }

    public synchronized void login(String nickname, VirtualView view) {
        if(view.isLoggedIn() || view.isInGame()) {
            // the nickname is already chosen.
            view.onGamesList(new GamesList(null));
            return;
        }

        views.add(view);
        view.setNickname(nickname);
        view.onGamesList(new GamesList(getGameInfo()));
    }

    public synchronized void createGame(int numberPlayers, int numberCommonGoals, VirtualView view) {
        // not yet registered.
        if (!view.isLoggedIn() || view.isInGame()) {
            view.onGameData(new GameData(-1, -1, -1, -1, -1, -1));
            return;
        }

        // incorrect parameters.
        if (numberPlayers < 2 || numberPlayers > 4 || numberCommonGoals < 1 || numberCommonGoals > 2) {
            view.onGameData(new GameData(-1, -1, -1, -1, -1, -1));
            return;
        }

        // create controller + join operation in controller + set controller in view.
        Controller controller = new Controller(numberPlayers, numberCommonGoals);
        controller.perform(new JoinAction(view));
        addController(controller);
        // the controller is sending the player the game dimensions.

        view.setController(controller);

        // send to client without game
        (new Thread(() -> {
            for (VirtualView v : views) {
                List<GameInfo> list = new ArrayList<>();
                list.add(new GameInfo(availableId - 1, numberPlayers, numberCommonGoals));
                GamesList gamesList = new GamesList(list);
                if (v.isLoggedIn() && !v.isInGame()) {
                    v.onGamesList(gamesList);
                }
            }
        })).start();
    }


    public synchronized void selectGame(int id, VirtualView view) {
        if (!view.isLoggedIn() || view.isInGame()) {
            view.onGameData(new GameData(-1, -1, -1, -1, -1, -1));
        }

        if (!controllers.containsKey(id) || controllers.get(id).isStarted()) {
            view.onGameData(new GameData(-1, -1, -1, -1, -1, -1));
            return;
        }

        controllers.get(id).perform(new JoinAction(view));
        // the controller is sending the player the game dimensions.
    }
}