package controller;

import domain.Prietenie;
import domain.PrietenieState;
import domain.User;
import exceptii.NotExistentException;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import utils.events.ChangeEventType;
import utils.events.ChangedEvent;
import utils.observer.MyObserver;

import java.util.Objects;

public class ProfileController implements MyObserver<ChangedEvent<Prietenie>> {
    private DataController dataController;
    private User currentUser = null;
    private User otherUser = null;
    private Prietenie prietenie = null;

    private final ObservableList<User> prieteniList = FXCollections.observableArrayList();
    private Stage stage;

    private void getPrietenie(){
        if(dataController == null) return;
        if(currentUser == null) return;
        if(otherUser == null) return;
        loadData();
        try{
            prietenie = dataController.getServicePrietenii().findByUserIds(currentUser, otherUser);
        }
        catch (NotExistentException ignored){}
        updateButton();
    }

    public void setDataController(DataController controller){
        this.dataController = controller;
        dataController.getServicePrietenii().addObserver(this);
        getPrietenie();
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
        getPrietenie();
    }

    public void setOtherUser(User user){
        this.otherUser = user;
        getPrietenie();
        labelNume.setText(otherUser.getName());
        labelEmail.setText(otherUser.getEmail());
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    Label labelNume;

    @FXML
    Label labelEmail;

    @FXML
    Button sendBtn;

    @FXML
    Button removeBtn;

    @FXML
    TableView<User> prieteniTable;

    @FXML
    TableColumn<User, String> numeColumn;

    @FXML
    TableColumn<User, String> emailColumn;

    private void loadData(){
        prieteniList.setAll(dataController.getServicePrietenii().findPrieteni(otherUser));
    }

    private void sendRequest(){
        prietenie = dataController.getServicePrietenii().sendPrietenieRequest(currentUser, otherUser);
    }

    public void deletePrietenie(){
        dataController.getServicePrietenii().remove(prietenie.getId());
        prietenie = null;
    }

    @Override
    public void update(ChangedEvent<Prietenie> prietenieChangedEvent) {
        prietenie = prietenieChangedEvent.getData();
        updateButton();
        switch (prietenieChangedEvent.getType())
        {
            case ADDED:
            case UPDATED:
                if(prietenie.getState() == PrietenieState.Accepted && !prieteniList.contains(currentUser))
                    prieteniList.add(currentUser);
                break;
            case REMOVED:
                prieteniList.remove(currentUser);
                break;
        }
        //loadData();
    }

    private void updateButton(){
        if(prietenie == null){
            if(!otherUser.equals(currentUser)) {
                sendBtn.setDisable(false);
                sendBtn.setText("Cerere prietenie");
                sendBtn.setOnAction(event -> sendRequest());
            }
            else {
                sendBtn.setVisible(false);
                sendBtn.setDisable(true);
            }
            removeBtn.setVisible(false);
            removeBtn.setDisable(true);
        }
        else if(prietenie.getState() == PrietenieState.Pending){
            if(Objects.equals(prietenie.getSenderId(), currentUser.getId())) {
                sendBtn.setText("Cererea a fost trimisa");
                sendBtn.setDisable(true);
                sendBtn.setVisible(true);
                removeBtn.setVisible(true);
                removeBtn.setDisable(false);
                removeBtn.setText("Retrage cererea");
            }
            else {
                sendBtn.setText("Aveti o cerere de prietenie!");
                sendBtn.setDisable(true);
                sendBtn.setVisible(true);
                removeBtn.setVisible(false);
                removeBtn.setDisable(true);
            }
        }
        else{
            sendBtn.setDisable(true);
            sendBtn.setVisible(true);
            sendBtn.setText("✓ Prieteni");
            removeBtn.setVisible(true);
            removeBtn.setDisable(false);
            removeBtn.setText("Sterge prieten");
        }
    }

    @FXML
    private void initialize(){
        prieteniTable.setItems(prieteniList);

        numeColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return param.getValue().getName();
                }
            };
        });
        emailColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return param.getValue().getEmail();
                }
            };
        });
    }
}
