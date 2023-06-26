package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.view.CommonGoalCardDescription;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.utils.message.client.BookshelfInsertion;
import it.polimi.ingsw.utils.message.client.LivingRoomSelection;
import it.polimi.ingsw.view.client.gui.GUInterface;
import it.polimi.ingsw.view.client.gui.GameControllerStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class GameController implements Initializable {
    private static GUInterface guInterface;
    final private static String CUP= "gui/myShelfieImages/item_tiles/Trofei1.1.png";
    final private static String CAT= "gui/myShelfieImages/item_tiles/Gatti1.1.png";
    final private static String BOOK= "gui/myShelfieImages/item_tiles/Libri1.1.png";
    final private static String PLANT= "gui/myShelfieImages/item_tiles/Piante1.1.png";
    final private static String GAME= "gui/myShelfieImages/item_tiles/Giochi1.1.png";
    final private static String FRAME= "gui/myShelfieImages/item_tiles/Cornici1.1.png";
    private final List<Position> selectedItems = new ArrayList<>(numberSelectedItems);
    private String nickname;
    private String currentPlayer;
    private final static int numberSelectedItems = 3;
    private final Map<String, GridPane> otherPlayersBookshelf = new HashMap<>();
    private final Map<String, ImageView> otherPlayersEndingToken = new HashMap<>();
    private final static int cellSizeLivingRoom = 39;
    private final static int cellSizeOthersBookshelf = 14;
    private final static int cellSizeBookshelf = 29;
    private final static int livingRoomGap = 0;
    private final static int bookshelfGap = 0;
    private final static int othersBookshelfGap = 0;
    private List<Item> chosenItems = new ArrayList<>(numberSelectedItems);
    private final List<ImageView> orderItems = new ArrayList<>(numberSelectedItems);
    private final List<Integer> selectedOrder = new ArrayList<>(numberSelectedItems);
    private int selectedColumn;
    private final static double selectedOpacity = 0.3;
    private final static double notSelectedOpacity = 1.0;
    private final Alert warningAlert = new Alert(Alert.AlertType.WARNING);
    private final Stage chatStage = new Stage();
    private final Map <String, Integer> scores = new HashMap<>();
    private GameControllerStatus status = GameControllerStatus.WAITING;
    private final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    private int firstCommonGoalCardId;
    private int secondCommonGoalCardId;
    @FXML
    private GridPane livingRoomGridPane;
    @FXML
    public Button sendButton;
    @FXML
    public Button enterChatButton;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private ImageView personalGoalCardImageView;
    @FXML
    private Label firstBookshelfLabel;
    @FXML
    private Label secondBookshelfLabel;
    @FXML
    private Label thirdBookshelfLabel;
    @FXML
    private ImageView firstBookshelfImageView;
    @FXML
    private ImageView secondBookshelfImageView;
    @FXML
    private ImageView thirdBookshelfImageView;
    @FXML
    private GridPane firstBookshelfGridPane;
    @FXML
    private GridPane secondBookshelfGridPane;
    @FXML
    private GridPane thirdBookshelfGridPane;
    @FXML
    private ImageView firstCommonGoalCardTopImageView;
    @FXML
    private ImageView secondCommonGoalCardTopImageView;
    @FXML
    private ImageView firstCommonGoalCardImageView;
    @FXML
    private ImageView secondCommonGoalCardImageView;
    @FXML
    private ImageView firstChosenItemImageView;
    @FXML
    private ImageView secondChosenItemImageView;
    @FXML
    private ImageView thirdChosenItemImageView;
    @FXML
    private Label firstChosenItemLabel;
    @FXML
    private Label secondChosenItemLabel;
    @FXML
    private Label thirdChosenItemLabel;
    @FXML
    private Button insertColumn0Button;
    @FXML
    private Button insertColumn1Button;
    @FXML
    private Button insertColumn2Button;
    @FXML
    private Button insertColumn3Button;
    @FXML
    private Button insertColumn4Button;
    @FXML
    private GridPane bookshelfGridPane;
    @FXML
    private ImageView currentEndingTokenImageView;
    @FXML
    private ImageView firstEndingTokenImageView;
    @FXML
    private ImageView secondEndingTokenImageView;
    @FXML
    private ImageView thirdEndingTokenImageView;
    @FXML
    private Label rankingLabel;

    public static void startGameController(GUInterface guInterface){
        GameController.guInterface=guInterface;
    }

    public void setCurrentPlayer(String currentPlayer){
        this.currentPlayer = currentPlayer;
        if (currentPlayer.equals(nickname)) {
            status = GameControllerStatus.LIVING_ROOM;
            currentPlayerLabel.setText("It's your turn!");
        }
        else
            currentPlayerLabel.setText("It's " + currentPlayer + "'s turn!");
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        nicknameLabel.setText("Hi " + nickname + "!");
    }

    private Image getImage(Item item) {
        String image;
        if(item == null) return null;
        switch (item) {
            case CAT -> image = CAT;
            case CUP -> image = CUP;
            case FRAME -> image = FRAME;
            case GAME -> image = GAME;
            case PLANT -> image = PLANT;
            case BOOK -> image = BOOK;
            default -> image = null;
        }
        if(image == null) return null;
        return new Image(image);
    }

    private int getRealRow(int row) {
        if (row == 0) return 5;
        if (row == 1) return 4;
        if (row == 2) return 3;
        if (row == 3) return 2;
        if (row == 4) return 1;
        return 0;
    }
    //ENDGAME
    public void disconnectionInGame() {
        errorAlert.setHeaderText("Error!");
        errorAlert.setContentText("You have been disconnected from the server.\n");
        errorAlert.showAndWait();
        System.exit(0);
    }

    public void playerDisconnectionInGame() {
        errorAlert.setHeaderText("Error!");
        errorAlert.setContentText("A player has disconnected, the game has ended.\n");
        errorAlert.showAndWait();
        System.exit(0);
    }

    public void endGame(String winner){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("The Game has ended!");
        alert.setContentText("The winner is: " + winner + ".\nRanking: ");
        for (Map.Entry<String, Integer> e : scores.entrySet()) {
            alert.setContentText(alert.getContentText() + "\n" + e.getKey() + ": " + e.getValue() + " points");
        }
        alert.showAndWait();
        System.exit(0);
    }

    //GRID
    public void setLivingRoomGridPane(Map<Position, Item> livingRoom) {
        if (livingRoom == null) return;
        for (Position position : livingRoom.keySet()) {
            setLivingRoomTile(livingRoom.get(new Position(position.getRow(), position.getColumn())), position.getColumn(), position.getRow());
        }
    }

    private void setLivingRoomTile(Item item, int column, int row) {
        ImageView imageView = new ImageView(getImage(item));
        if (imageView.getImage() == null)
            clearNodeByColumnRow(column, row);
        imageView.setOnMouseClicked(mouseEvent -> selectItem(new Position(row, column)));
        imageView.setFitWidth(cellSizeLivingRoom);
        imageView.setFitHeight(cellSizeLivingRoom);
        imageView.setPreserveRatio(true);
        livingRoomGridPane.add(imageView, column, row);
    }

    public void selectItem(Position position) {
        if (!guInterface.getNickname().equals(currentPlayer)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("Wait for your turn!");
            warningAlert.showAndWait();
            return;
        }
        if (!status.equals(GameControllerStatus.LIVING_ROOM)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You cannot do this now!");
            warningAlert.showAndWait();
            return;
        }
        if (selectedItems.contains(position)) {
            selectedItems.remove(position);
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).forEach(n -> n.setOpacity(notSelectedOpacity));
            return;
        }
        if (selectedItems.size() <= 2) {
            selectedItems.add(position);
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).toList().forEach(n -> n.setOpacity(selectedOpacity));
            return;
        }
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("You have already selected 3 items!");
        warningAlert.showAndWait();
    }

    public void selectFromLivingRoom() throws IOException {
        if (!guInterface.getNickname().equals(currentPlayer)){
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("Wait for your turn!");
            warningAlert.showAndWait();
            return;
        }
        if (!status.equals(GameControllerStatus.LIVING_ROOM)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You cannot do this now!");
            warningAlert.showAndWait();
            return;
        }
        if (selectedItems.size() < 1){
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You have to select at least one item!");
            warningAlert.showAndWait();
            return;
        }
        guInterface.selectFromLivingRoom(new LivingRoomSelection(selectedItems));
    }

    public void setChosenItems(List<Item> chosenItems){
        if (!currentPlayer.equals(nickname)) return;
        status = GameControllerStatus.BOOKSHELF;
        this.chosenItems = chosenItems;
        updateChosenItemsImageView();
    }

    private void clearNodeByColumnRow(int column, int row){
        livingRoomGridPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row);
    }

    public void clearTilesList() {
        if (!currentPlayer.equals(nickname)) return;
        for (Position items : selectedItems) clearNodeByColumnRow(items.getColumn(), items.getRow());
    }

    public void resetOpacity() {
        for (Position position : selectedItems) {
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).toList().get(0).setOpacity(notSelectedOpacity);
        }
    }

    public void clearSelectedItems() {
        selectedItems.clear();
    }

    public void failedSelection() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("Failed selection, retry!");
        warningAlert.showAndWait();
    }

    //BOOKSHELF

    private void updateChosenItemsImageView() {
        firstChosenItemImageView.setImage(getImage(chosenItems.get(0)));
        firstChosenItemImageView.setOnMouseClicked(mouseEvent -> this.selectOrder(firstChosenItemImageView));
        if (chosenItems.size() > 1) {
            secondChosenItemImageView.setImage(getImage(chosenItems.get(1)));
            secondChosenItemImageView.setOnMouseClicked(mouseEvent -> this.selectOrder(secondChosenItemImageView));
        }
        if (chosenItems.size() > 2) {
            thirdChosenItemImageView.setImage(getImage(chosenItems.get(2)));
            thirdChosenItemImageView.setOnMouseClicked(mouseEvent -> this.selectOrder(thirdChosenItemImageView));
        }
    }

    public void selectOrder(ImageView selected) {
        if (!guInterface.getNickname().equals(currentPlayer)) return;
        if (!status.equals(GameControllerStatus.BOOKSHELF)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You cannot do this now!");
            warningAlert.showAndWait();
            return;
        }
        if (!orderItems.contains(selected)) {
            orderItems.add(selected);
            selected.setOpacity(selectedOpacity);
        } else {
            orderItems.remove(selected);
            selected.setOpacity(notSelectedOpacity);
        }
        updateOrderItems();
    }

    private void updateOrderItems() {
        boolean first = false, second = false, third = false;

        for (ImageView imageView : orderItems) {
            if (imageView.equals(firstChosenItemImageView)) {
                firstChosenItemLabel.setText(String.valueOf(orderItems.indexOf(imageView)+1));
                first = true;
            } else if (imageView.equals(secondChosenItemImageView)) {
                secondChosenItemLabel.setText(String.valueOf(orderItems.indexOf(imageView)+1));
                second = true;
            } else if (imageView.equals(thirdChosenItemImageView)) {
                thirdChosenItemLabel.setText(String.valueOf(orderItems.indexOf(imageView)+1));
                third = true;
            }
        }

        selectedOrder.clear();

        if (orderItems.contains(firstChosenItemImageView))
            selectedOrder.add(orderItems.indexOf(firstChosenItemImageView));
        if (orderItems.contains(secondChosenItemImageView))
            selectedOrder.add(orderItems.indexOf(secondChosenItemImageView));
        if (orderItems.contains(thirdChosenItemImageView))
            selectedOrder.add(orderItems.indexOf(thirdChosenItemImageView));

        if (!first)
            firstChosenItemLabel.setText("");
        if (!second)
            secondChosenItemLabel.setText("");
        if (!third)
            thirdChosenItemLabel.setText("");
    }
    public void selectColumn(int column) {
        if (!currentPlayer.equals(nickname)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("Wait for your turn!");
            warningAlert.showAndWait();
            return;
        }
        if (!status.equals(GameControllerStatus.BOOKSHELF)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You cannot do this now!");
            warningAlert.showAndWait();
            return;
        }
        if (chosenItems.size() == 0) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You have to select items from the Living Room!");
            warningAlert.showAndWait();
            return;
        }
        if (!(orderItems.size() == selectedItems.size())) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You have to choose an order for all the items!");
            warningAlert.showAndWait();
            return;
        }
        selectedColumn = column;
        guInterface.insertInBookshelf(new BookshelfInsertion(selectedColumn, selectedOrder));
    }

    public void insertItems() {
        for (ImageView item : orderItems) {
            ImageView imageView = new ImageView(item.getImage());
            bookshelfGridPane.add(imageView, selectedColumn, findFreeRowBookshelf(bookshelfGridPane, selectedColumn));
            imageView.setOpacity(notSelectedOpacity);
            imageView.setFitWidth(cellSizeBookshelf);
            imageView.setFitHeight(cellSizeBookshelf);
            clearChosenItemLabels();
        }
        endTurnClear();
        status = GameControllerStatus.WAITING;
    }

    private int findFreeRowBookshelf(GridPane bookshelfGridPane, int column) {
        for (int i = bookshelfGridPane.getColumnCount(); i >= 0; i--) {
            int finalI = i;
            if (bookshelfGridPane.getChildren().stream().noneMatch(n -> (GridPane.getColumnIndex(n) == column && GridPane.getRowIndex(n) == finalI)))
                return finalI;
        }
        return -1;
    }

    private void clearChosenItemLabels() {
        firstChosenItemLabel.setText("");
        secondChosenItemLabel.setText("");
        thirdChosenItemLabel.setText("");
    }

    private void endTurnClear() {
        selectedItems.clear();
        selectedOrder.clear();
        chosenItems.clear();
        orderItems.clear();
        firstChosenItemImageView.setOnMouseClicked(null);
        secondChosenItemImageView.setOnMouseClicked(null);
        thirdChosenItemImageView.setOnMouseClicked(null);
    }

    public void clearChosenItemsImageView() {
        firstChosenItemImageView.setImage(null);
        firstChosenItemImageView.setOpacity(notSelectedOpacity);
        secondChosenItemImageView.setImage(null);
        secondChosenItemImageView.setOpacity(notSelectedOpacity);
        thirdChosenItemImageView.setImage(null);
        thirdChosenItemImageView.setOpacity(notSelectedOpacity);
    }

    public void failedInsertion() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("Failed insertion, retry!");
        warningAlert.showAndWait();
    }

    //PERSONAL GOAL CARD

    public void setPersonalGoalCard(int id){
        personalGoalCardImageView.setImage(new Image("/gui/myShelfieImages/personal_goal_cards/personal_goal_card_" + id + ".png"));
    }

    //BOOKSHELVES

    public void initializeBookshelves(List<String> nicknames) {
        int numberPlayers = nicknames.size();
        nicknames.remove(nickname);

        firstBookshelfImageView.setImage(new Image("/gui/myShelfieImages/boards/bookshelf.png"));
        firstBookshelfLabel.setText(nicknames.get(0));
        otherPlayersBookshelf.put(nicknames.get(0), firstBookshelfGridPane);
        otherPlayersEndingToken.put(nicknames.get(0), firstEndingTokenImageView);

        if (numberPlayers > 2) {
            secondBookshelfImageView.setImage(new Image("/gui/myShelfieImages/boards/bookshelf.png"));
            secondBookshelfLabel.setText(nicknames.get(1));
            otherPlayersBookshelf.put(nicknames.get(1), secondBookshelfGridPane);
            otherPlayersEndingToken.put(nicknames.get(1), secondEndingTokenImageView);

        }
        if (numberPlayers > 3) {
            thirdBookshelfImageView.setImage(new Image("/gui/myShelfieImages/boards/bookshelf.png"));
            thirdBookshelfLabel.setText(nicknames.get(2));
            otherPlayersBookshelf.put(nicknames.get(2), thirdBookshelfGridPane);
            otherPlayersEndingToken.put(nicknames.get(2), thirdEndingTokenImageView);
        }
    }

    public void updateBookshelf(String owner, Map<Position, Item> bookshelf) {
        if (owner == null) return;
        if (bookshelf == null) return;
        if (!otherPlayersBookshelf.containsKey(owner)) return;
        for (Position position : bookshelf.keySet()) {
            ImageView imageView = new ImageView(getImage(bookshelf.get(position)));
            imageView.setFitHeight(cellSizeOthersBookshelf);
            imageView.setFitWidth(cellSizeOthersBookshelf);
            otherPlayersBookshelf.get(owner).add(imageView, position.getColumn(), getRealRow(position.getRow()));
        }
    }

    //COMMON GOAL CARDS

    public void updateCommonGoalCards(Map<Integer, Integer> commonGoalCards) {
        if (commonGoalCards == null) return;
        String filename;
        String topName;
        boolean first = true;
        for (Map.Entry<Integer, Integer> card : commonGoalCards.entrySet()) {
            filename = "/gui/myShelfieImages/common_goal_cards/common_goal_card_" + card.getKey() + ".jpg";
            topName = "/gui/myShelfieImages/scoring_tokens/scoring_" + card.getValue() + ".jpg";
            if (first) {
                firstCommonGoalCardTopImageView.setImage(new Image(topName));
                firstCommonGoalCardImageView.setImage(new Image(filename));
                firstCommonGoalCardId = card.getKey();
                first = false;
            } else {
                secondCommonGoalCardTopImageView.setImage(new Image(topName));
                secondCommonGoalCardImageView.setImage(new Image(filename));
                secondCommonGoalCardId = card.getKey();
            }
        }
    }

    private void showCommonGoalCardDescription(int id) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setHeaderText("Common Goal Card Description");
        infoAlert.setContentText(CommonGoalCardDescription.getDescription(id));
        infoAlert.showAndWait();
    }

    //ENDING TOKEN

    public void setEndingToken(String owner) {
        if (owner == null) return;
        if (owner.equals(nickname))
            currentEndingTokenImageView.setImage(new Image("/gui/myShelfieImages/scoring_tokens/end_game.jpg"));
        else
            otherPlayersEndingToken.get(owner).setImage(new Image("/gui/myShelfieImages/scoring_tokens/end_game.jpg"));
    }


    //SCORES

    public void setScores(Map<String, Integer> newScores) {
        if (newScores == null) return;
        this.scores.putAll(newScores);
        rankingLabel.setText("");
        for(String player : this.scores.keySet()) {
            rankingLabel.setText(rankingLabel.getText() + player + ": " + this.scores.get(player) + " points\n");
        }
    }

    //CHAT
    private void createChat() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Chat.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        chatStage.setScene(scene);
        chatStage.setTitle("MyShelfieChat");
        chatStage.hide();
        chatStage.setResizable(false);
        chatStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/myShelfieImages/publisher_material/icon_50x50px.png"))));

        chatStage.setOnCloseRequest(event -> {
            event.consume();
            chatStage.hide();
        });
    }
    public void enterChat() {
        chatStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this);
        livingRoomGridPane.setHgap(livingRoomGap);
        livingRoomGridPane.setVgap(livingRoomGap);
        bookshelfGridPane.setHgap(bookshelfGap);
        bookshelfGridPane.setVgap(bookshelfGap);
        firstBookshelfGridPane.setHgap(othersBookshelfGap);
        firstBookshelfGridPane.setVgap(othersBookshelfGap);
        secondBookshelfGridPane.setHgap(othersBookshelfGap);
        secondBookshelfGridPane.setVgap(othersBookshelfGap);
        thirdBookshelfGridPane.setHgap(othersBookshelfGap);
        thirdBookshelfGridPane.setVgap(othersBookshelfGap);
        insertColumn0Button.setOnMouseClicked(mouseEvent -> selectColumn(0));
        insertColumn1Button.setOnMouseClicked(mouseEvent -> selectColumn(1));
        insertColumn2Button.setOnMouseClicked(mouseEvent -> selectColumn(2));
        insertColumn3Button.setOnMouseClicked(mouseEvent -> selectColumn(3));
        insertColumn4Button.setOnMouseClicked(mouseEvent -> selectColumn(4));
        firstCommonGoalCardImageView.setOnMouseClicked(mouseEvent -> showCommonGoalCardDescription(firstCommonGoalCardId));
        secondCommonGoalCardImageView.setOnMouseClicked(mouseEvent -> showCommonGoalCardDescription(secondCommonGoalCardId));
        try {
            createChat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
