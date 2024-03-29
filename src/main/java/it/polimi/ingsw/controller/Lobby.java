package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.ServerUpdateViewInterface;

import java.util.*;


/**
 * Class containing all {@code Controller}s of all games currently being played and all {@code ServerUpdateViewInterface}s.
 * It uses the Singleton Pattern. Some public methods take the lock on {@code this}.
 */
public class Lobby {
    /**
     * Map of id -> games (the controller of that game).
     */
    private final Map<Integer, ControllerInterface> controllers;

    /**
     * {@code Map} of all logged in (with a nickname) {@code ServerUpdateViewInterface}s,
     * representing all players connected either waiting for a game or in game.
     * They can be used by {@code this} to send updates to a player.
     */
    private final Map<String, ServerUpdateViewInterface> views;

    /**
     * {@code Map} of nicknames still bound to a game.
     */
    private final Map<String, Integer> boundToGame;

    /**
     * Smallest available id for the next game to be created.
     */
    private int availableId;

    /**
     * Attribute used to implement the Singleton Pattern.
     */
    private static Lobby instance;

    /**
     * Object only used as a lock by the {@code getInstance} method for synchronization.
     */
    private static final Object lock = new Object();

    /**
     * The constructor for the class. Initializes the lobby with an empty map of {@code Controller}s
     * and an empty set of {@code ServerUpdateViewInterface}s. The first available id is 0.
     */
    private Lobby() {
        this.controllers = new HashMap<>();
        this.views = new HashMap<>();
        this.boundToGame = new HashMap<>();
        this.availableId = 0;
    }

    /**
     * Getter for the instance of {@code Lobby}. If no instance exists, it creates a new one.
     * @return The instance of the lobby, either already existing or newly created.
     */
    public static Lobby getInstance() {
        synchronized (lock) {
            if (instance == null) instance = new Lobby();
        }
        return instance;
    }

    /**
     * Getter for the list of games information for every match in pre-game phase.
     * @return A list containing the game's info.
     */
    private List<GameInfo> getGameInfo() {
        List<GameInfo> ret = new ArrayList<>();
        for (Map.Entry<Integer, ControllerInterface> e : controllers.entrySet()) {
            if (!e.getValue().isStarted())   //If a game has already started, it's not displayed.
                ret.add(new GameInfo(e.getKey(), e.getValue().getNumberPlayers(), e.getValue().getNumberCommonGoalCards()));
        }
        return ret;
    }

    /**
     * Adds a new controller to {@code Lobby}.
     * @param controller The controller to add.
     */
    private void addController(ControllerInterface controller) {
        controllers.put(availableId, controller);
        availableId++;
    }

    /**
     * Unsubscribes a client from updates sent by {@code this}.
     * It synchronizes on {@code this}.
     * @param nickname The player to remove.
     */
    public synchronized void removeConnection(String nickname) {
        views.remove(nickname);
    }

    /**
     * Processes a login request from a {@code ServerUpdateViewInterface} and registers that view.
     * It synchronizes on {@code this}.
     * @param nickname The nickname chosen by the {@code ServerUpdateViewInterface}.
     * @param view The {@code ServerUpdateViewInterface} performing the request.
     */
    public synchronized void login(String nickname, ServerUpdateViewInterface view) {
        if(views.containsKey(nickname)) {
            // A view with the same nickname has already logged in.
            view.onGamesList(new GamesList(null));
            return;
        }

        // login done.
        Logger.login(nickname);

        // register the view and the nickname.
        views.put(nickname, view);
        view.setNickname(nickname);

        synchronized (boundToGame) {
            if (boundToGame.containsKey(nickname)) {
                int id = boundToGame.get(nickname);

                // try reconnecting.
                boolean connected = false;
                if (controllers.containsKey(id)) {
                    connected = controllers.get(id).reconnection(view, nickname);
                }

                if (!connected) {
                    view.setNickname(null);
                    views.remove(nickname);

                    view.onGamesList(new GamesList(null));
                }
                return;
            }
        }

        view.onGamesList(new GamesList(getGameInfo()));
    }

    /**
     * Creates a new {@code Controller} with the parameters requested and logs the
     * {@code ServerUpdateViewInterface} that performed the request into the newly created game.
     * Moreover, all clients that have not joined in a match yet are notified of the creation of the
     * match.
     * It synchronizes on {@code this}.
     * @param numberPlayers The number of players in the game to create.
     * @param numberCommonGoals The number of common goal cards in the game to create.
     * @param view The view of the client creating a new game.
     * @param nickname The nickname that performed the request.
     */
    public synchronized void createGame(int numberPlayers, int numberCommonGoals, ServerUpdateViewInterface view, String nickname) {
        // not yet registered or already playing.
        boolean bound;
        synchronized (boundToGame) {
            bound = boundToGame.containsKey(nickname);
        }
        if (!views.containsKey(nickname) || bound) {
            view.onGameData(new GameData(-1,
                            null,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1));
            return;
        }

        // incorrect parameters.
        if (numberPlayers < 2 || numberPlayers > 4 || numberCommonGoals < 1 || numberCommonGoals > 2) {
            view.onGameData(new GameData(-1,
                    null,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1));
            return;
        }

        // create controller + join operation in controller
        Controller controller = new Controller(numberPlayers, numberCommonGoals, availableId);

        if (!controller.join(view, nickname)) return;

        // The join worked.
        addController(controller);

        Logger.createGame(numberPlayers, numberCommonGoals, availableId - 1, nickname);

        // send to client without game
        GamesList gamesList = new GamesList(List.of(new GameInfo(availableId-1, numberPlayers, numberCommonGoals)));
        for(ServerUpdateViewInterface u : views.values()) {
            if (u.inGame()) continue;
            u.onGamesList(gamesList);
        }
    }

    /**
     * Joins the {@code ServerUpdateViewInterface} that performed the request in the selected match (by id).
     * @param id The id of the selected game.
     * @param view The view of the client selecting a game.
     * @param nickname The nickname of the client that performed the request.
     */
    public synchronized void selectGame(int id, ServerUpdateViewInterface view, String nickname) {
        // not yet registered or already in a match.
        boolean bound;
        synchronized (boundToGame) {
            bound = boundToGame.containsKey(nickname);
        }
        if (!views.containsKey(nickname) || bound) {
            view.onGameData(new GameData(-1,
                            null,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1));
            return;
        }

        // selected match does not exist or is already started.
        if (!controllers.containsKey(id) || controllers.get(id).isStarted()) {
            view.onGameData(new GameData(-1,
                    null,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1));
            return;
        }

        if (!controllers.get(id).join(view, nickname)) return;
        // join successful.

        Logger.selectGame(id, nickname);

        // the controller is sending the player the game dimensions.
    }

    /**
     * Binds the nicknames of some players to the id of a game. In this way, the {@code Lobby} knows, in case of a
     * reconnection, to which game the players should be reconnected to. It synchronizes internally and on {@code this}
     * separately: with the lock on {@code this}, no sensitive lock is acquired.
     * @param nicknames List of the nicknames of the players to bind.
     * @param id The id of the game the players are getting bound to.
     */
    public void bind(List<String> nicknames, int id) {
        synchronized (boundToGame) {
            for(String nickname : nicknames) boundToGame.put(nickname, id);
        }
        synchronized (this) {
            notifyGameNotAvailable(id);
        }
    }

    /**
     * Unbinds the nicknames of some players to the id of a game. It synchronizes internally and on {@code this}
     * separately: with the lock on {@code this}, no sensitive lock is acquired.
     * @param nicknames list of the nicknames of the players to unbind.
     * @param id The id of the game the players are unbinding from.
     */
    public void unbind(List<String> nicknames, int id) {
        synchronized (boundToGame) {
            for(String nickname : nicknames) boundToGame.remove(nickname);
        }
        synchronized (this) {
            controllers.remove(id);
            notifyGameNotAvailable(id);
        }
    }

    private void notifyGameNotAvailable(int id) {
        GamesList gamesList = new GamesList(List.of(new GameInfo(id, -1, -1)));
        for(ServerUpdateViewInterface u : views.values()) {
            if (u.inGame()) continue;
            u.onGamesList(gamesList);
        }
    }

    //METHODS EXCLUSIVELY FOR TESTING

    /**
     * This method sets the Singleton instance to null, allowing us to instance it again, used only for testing.
     */
    public static void setNull() {
        instance = null;
    }
}
