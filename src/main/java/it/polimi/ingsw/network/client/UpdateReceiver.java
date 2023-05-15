package it.polimi.ingsw.network.client;

import it.polimi.ingsw.utils.networkMessage.server.*;

import java.io.Serializable;

public interface UpdateReceiver {
    void onGamesList(GamesList message);
    void onAcceptedAction(AcceptedAction message);
    void onItemsSelected(ItemsSelected message);
    void onLivingRoomUpdate(LivingRoomUpdate update);
    void onBookshelfUpdate(BookshelfUpdate update);
    void onWaitingUpdate(WaitingUpdate update);
    void onScoresUpdate(ScoresUpdate update);
    void onEndingTokenUpdate(EndingTokenUpdate update);
    void onCommonGoalCardUpdate(CommonGoalCardUpdate update);
    void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update);
    void onChatUpdate(ChatUpdate update);
    void onStartTurnUpdate(StartTurnUpdate update);
    void onEndGameUpdate(EndGameUpdate update);
}
