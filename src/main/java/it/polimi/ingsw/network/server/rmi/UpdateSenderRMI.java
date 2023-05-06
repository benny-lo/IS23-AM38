package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.client.rmi.ClientRMIInterface;
import it.polimi.ingsw.network.server.UpdateSender;
import it.polimi.ingsw.utils.networkMessage.server.*;

import java.rmi.RemoteException;

public class UpdateSenderRMI implements UpdateSender {
    private final ClientRMIInterface client;

    public UpdateSenderRMI(ClientRMIInterface client) {
        this.client = client;
    }

    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) {
        try {
            client.sendLivingRoomUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) {
        try {
            client.sendBookshelfUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) {
        try {
            client.sendWaitingUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) {
        try {
            client.sendScoresUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) {
        try {
            client.sendEndingTokenUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }

    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardUpdate update) {
        try {
            client.sendCommonGoalCardUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        try {
            client.sendPersonalGoalCardUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendChatUpdate(ChatUpdate update) {
        try {
            client.sendChatUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) {
        try {
            client.sendStartTurnUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) {
        try {
            client.sendEndGameUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }
}
