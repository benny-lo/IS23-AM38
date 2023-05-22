package it.polimi.ingsw.view.client;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.network.client.rmi.ClientConnectionRMI;
import it.polimi.ingsw.network.client.socket.ClientConnectionTCP;
import it.polimi.ingsw.network.server.rmi.ConnectionEstablishmentRMIInterface;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;
import it.polimi.ingsw.view.InputViewInterface;

import java.io.IOException;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ClientView implements ClientUpdateViewInterface, InputViewInterface {
    protected String nickname;
    protected String currentPlayer;
    protected String winner;
    protected int numberPlayers;
    protected int numberCommonGoalCards;
    protected Item[][] livingRoom;
    protected Map<String, Item[][]> bookshelves;
    protected int bookshelvesRows;
    protected int bookshelvesColumns;
    protected String endingToken;
    protected int personalGoalCard;
    protected Map<Integer, Integer> commonGoalCards;
    protected Map<String, Integer> scores;
    protected boolean endGame;
    protected List<Message> chat;
    protected List<Item> itemsChosen;
    protected ClientConnection clientConnection;

    public ClientView() {
        bookshelves = new HashMap<>();
        commonGoalCards = new HashMap<>();
        scores = new HashMap<>();
        chat = new ArrayList<>();
    }

    public void startRMI() {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(ServerSettings.getHostName(), ServerSettings.getRmiPort());
        } catch (RemoteException e) {
            System.out.println("Registry not found.");
            System.exit(0);
        }

        ConnectionEstablishmentRMIInterface stub = null;
        try {
            stub = (ConnectionEstablishmentRMIInterface) registry.lookup("ConnectionEstablishmentRMIInterface");
        } catch (AccessException e) {
            System.out.println("Wrong access privileges for RMI.");
            System.exit(0);
        } catch (NotBoundException e) {
            System.out.println("Name not found for RMI.");
            System.exit(0);
        } catch (RemoteException e) {
            System.out.println("Something went wrong with RMI.");
            System.exit(0);
        }

        ClientConnectionRMI clientConnectionRMI = null;
        try {
            clientConnectionRMI = new ClientConnectionRMI(this);
        } catch (RemoteException e) {
            System.out.println("Failed to instantiate RMI client.");
            System.exit(0);
        }

        ServerConnectionRMIInterface serverConnection = null;
        try {
            serverConnection = stub.init(clientConnectionRMI);
        } catch (RemoteException e) {
            System.out.println("Failed to get RMI server.");
            System.exit(0);
        }

        clientConnectionRMI.setServerConnectionRMIInterface(serverConnection);
        this.clientConnection = clientConnectionRMI;
        clientConnectionRMI.startTimers();
    }

    public void startTCP() {
        Socket socket = null;
        try {
            socket = new Socket(ServerSettings.getHostName(), ServerSettings.getSocketPort());
        } catch (IOException e) {
            System.out.println("Server has not been started yet.");
            System.exit(0);
        }

        ClientConnectionTCP sender = null;
        try {
            sender = new ClientConnectionTCP(socket, this);
        } catch (IOException e) {
            System.out.println("Failed to get socket streams from server.");
            System.exit(0);
        }
        this.clientConnection = sender;
        (new Thread(sender)).start();
    }

    public abstract void start();

    public static boolean isValidNickname(String nickname){
        String regex = "^[A-Za-z]\\w{0,29}$";
        Pattern pattern = Pattern.compile(regex);

        if (nickname == null) {
            return false;
        }

        Matcher matcher = pattern.matcher(nickname);

        return matcher.matches();
    }
}