package it.polimi.ingsw.view.client;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.network.client.rmi.ClientConnectionRMI;
import it.polimi.ingsw.network.client.socket.ClientConnectionTCP;
import it.polimi.ingsw.network.server.rmi.ConnectionEstablishmentRMIInterface;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;
import it.polimi.ingsw.view.UpdateViewInterface;

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

public abstract class ClientView implements UpdateViewInterface {
    protected String nickname;
    protected String currentPlayer;
    protected String winner;
    protected int numberPlayers;
    protected int numberCommonGoalCards;
    protected Item[][] livingRoom;
    protected Map<String, Item[][]> bookshelves;
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
        // TODO: exceptions
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(ServerSettings.getHostName(), ServerSettings.getRmiPort());
        } catch (RemoteException e) {
            System.err.println("RMI registry error");
            throw new RuntimeException(e);
        }

        ConnectionEstablishmentRMIInterface stub;
        try {
            stub = (ConnectionEstablishmentRMIInterface) registry.lookup("ConnectionEstablishmentRMIInterface");
        } catch (AccessException e) {
            System.err.println("wrong access privileges for RMI");
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            System.err.println("name not found for RMI");
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            System.err.println("something went wrong for RMI");
            throw new RuntimeException(e);
        }

        ClientConnectionRMI clientConnectionRMI;
        try {
            clientConnectionRMI = new ClientConnectionRMI(this);
        } catch (RemoteException e) {
            System.err.println("ClientView, line 78: failed to instantiate RequestSender");
            throw new RuntimeException(e);
        }

        ServerConnectionRMIInterface serverConnection;
        try {
            serverConnection = stub.init(clientConnectionRMI);
        } catch (RemoteException e) {
            System.err.println("ClientView, line 86: failed to start rmi connection");
            throw new RuntimeException(e);
        }

        clientConnectionRMI.setServerConnectionRMIInterface(serverConnection);
        this.clientConnection = clientConnectionRMI;
    }

    public void startTCP() {
        // TODO: exceptions
        Socket socket;
        try {
            socket = new Socket(ServerSettings.getHostName(), ServerSettings.getSocketPort());
        } catch (IOException e) {
            System.err.println("not able to open TCP connection to Server");
            throw new RuntimeException(e);
        }

        ClientConnectionTCP sender = null;
        try {
            sender = new ClientConnectionTCP(socket, this);
        } catch (IOException e) {
            System.out.println("failed to get streams from Socket");
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
