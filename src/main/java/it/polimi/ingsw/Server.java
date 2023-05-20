package it.polimi.ingsw;

import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.view.server.VirtualView;
import it.polimi.ingsw.controller.Lobby;
import it.polimi.ingsw.network.server.rmi.ConnectionEstablishmentRMI;
import it.polimi.ingsw.network.server.rmi.ConnectionEstablishmentRMIInterface;
import it.polimi.ingsw.network.server.socket.ServerConnectionTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Server {
    public static void launch(List<String> args) {
        startConnectionRMI();
        startConnectionTCP();

        System.out.println("server is ready ...");
    }

    private static void startConnectionRMI() {
        ConnectionEstablishmentRMIInterface stub = null;
        ConnectionEstablishmentRMI connection = new ConnectionEstablishmentRMI();
        try {
            stub = (ConnectionEstablishmentRMIInterface)
                    UnicastRemoteObject.exportObject(connection, ServerSettings.getRmiPort());
        } catch (RemoteException e) {
            System.err.println("failed to export serverRMI");
            e.printStackTrace();
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(ServerSettings.getRmiPort());
        } catch (RemoteException e) {
            System.err.println("failed to create registry");
            e.printStackTrace();
        }

        try {
            registry.bind("ConnectionEstablishmentRMIInterface", stub);
        } catch (AccessException e) {
            System.err.println("no permission to perform action");
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.err.println("already bound to registry object with name ServerRMIInterface");
            e.printStackTrace();
        } catch (RemoteException e) {
            System.err.println("binding failed");
            e.printStackTrace();
        }
    }

    private static void startConnectionTCP() {
        (new Thread(() -> {
            ServerSocket server = null;

            try {
                server = new ServerSocket(ServerSettings.getSocketPort());
            } catch (IOException e) {
                System.err.println("failed to start server socket");
                e.printStackTrace();
            }

            while(true) {
                try {
                    Socket socket = server.accept();
                    ServerConnectionTCP serverConnectionTCP = new ServerConnectionTCP(socket);
                    VirtualView view = new VirtualView(serverConnectionTCP);
                    serverConnectionTCP.setInputViewInterface(view);
                    Lobby.getInstance().addVirtualView(view);

                    (new Thread(serverConnectionTCP)).start();
                } catch (IOException e) {
                    System.err.println("server closed");
                    e.printStackTrace();
                }
            }
        })).start();
    }
}
