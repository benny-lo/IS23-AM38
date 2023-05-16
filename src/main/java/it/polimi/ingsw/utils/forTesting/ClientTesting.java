package it.polimi.ingsw.utils.forTesting;

import it.polimi.ingsw.utils.networkMessage.server.*;

/**
 * This class is exclusively for testing, and it mimics a Client class.
 */

public class ClientTesting implements ClientInterfaceTesting {
    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update){

    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update){

    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update){

    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update){

    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update){

    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardsUpdate update){

    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update){

    }

    @Override
    public void sendChatUpdate(ChatUpdate update){

    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update){

    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update){

    }

    @Override
    public void sendListOfGames(GamesList gamesList) {

    }

    @Override
    public void sendAcceptedAction(AcceptedAction acceptedAction) {

    }

    @Override
    public void sendItemsSelected(ItemsSelected itemsSelected) {

    }
}