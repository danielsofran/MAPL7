package controller;

import domain.Mesaj;
import domain.User;
import exceptii.NotExistentException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import utils.events.ChangeEventType;
import utils.events.ChangedEvent;
import utils.observer.MyObserver;

import java.util.Collection;

public class MessageController implements MyObserver<ChangedEvent<Mesaj>> {
    private DataController dataController = null;
    private User userFrom = null, userTo = null;

    private final ObservableList<Mesaj> mesajeList = FXCollections.observableArrayList();
    private Stage stage;

    private void getMessages(){
        if(dataController == null) return;
        if(userFrom == null) return;
        if(userTo == null) return;
        loadData();
        try{
            // TODO: is prieteni?
        }
        catch (NotExistentException ignored){}
    }

    void loadData(){
        Collection<Mesaj> mesaje = dataController.getServiceMesaje().findAll(userFrom, userTo);
        mesaje.forEach(msg -> {
            GridPane gridPane = createMessageContainer(msg.getContent(),
                    msg.isFrom(userFrom) ? TextAlignment.RIGHT : TextAlignment.LEFT);
            addMessageContainer(gridPane);
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setDataController(DataController dataController) {
        this.dataController = dataController;
        dataController.getServiceMesaje().addObserver(this);
        getMessages();
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
        getMessages();
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
        labelName.setText(userTo.getName());
        getMessages();
    }

    @FXML
    private Label labelName;

    @FXML
    private TextArea msgArea;

    @FXML
    private VBox container;

    private GridPane new1x2GridPane(){
        GridPane gridPane = new GridPane();
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        ColumnConstraints col1 = new ColumnConstraints(), col2 = new ColumnConstraints();
        col1.setPercentWidth(50); col2.setPercentWidth(50);
        gridPane.getRowConstraints().add(row);
        gridPane.getColumnConstraints().addAll(col1, col2);
        return gridPane;
    }

    private GridPane createMessageContainer(String msgText, TextAlignment alignment) {
        GridPane gridPane = new1x2GridPane();
        Label txtLabel = new Label();
        txtLabel.setText(msgText);
        txtLabel.setStyle("-fx-font-size: 16px; -fx-wrap-text: true; -fx-pref-height: 2000");
        switch (alignment){
            case LEFT:
                gridPane.add(txtLabel, 0, 0);
                break;
            case RIGHT:
                gridPane.add(txtLabel, 1, 0);
                break;
        }
        gridPane.setPrefHeight(50*msgText.length()/20);
        gridPane.setPrefWidth(2000);
        return gridPane;
    }

    private void addMessageContainer(GridPane msgContainer) {
        double totalHeight = container.getChildren().stream().map(gridpane -> gridpane.getBoundsInLocal().getHeight()).reduce(Double::sum).orElse(0.0);
        if (totalHeight + msgContainer.getPrefHeight() > container.getHeight()) {
            container.setPrefHeight(container.getPrefHeight() + 200);
        }
        container.getChildren().add(msgContainer);
    }

    public void sendMsg() {
        String msg = msgArea.getText();
        msgArea.clear();
        GridPane msgContainer = createMessageContainer(msg, TextAlignment.RIGHT);
        addMessageContainer(msgContainer);
        // TODO: controller send
        dataController.getServiceMesaje().sendMesaj(userFrom, userTo, msg);
    }

    @Override
    public void update(ChangedEvent<Mesaj> mesajChangedEvent) {
        if (mesajChangedEvent.getType() == ChangeEventType.ADDED &&
            mesajChangedEvent.getData().isFrom(userTo)) {
            GridPane msgContainer = createMessageContainer(mesajChangedEvent.getData().getContent(), TextAlignment.LEFT);
            addMessageContainer(msgContainer);
        }
    }
}
