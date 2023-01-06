package controller;

import domain.Prietenie;
import domain.PrietenieState;
import domain.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.util.Callback;
import ui.graphic.GraphicApplication;
import utils.Utils;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.events.ChangeEventType;
import utils.events.ChangedEvent;
import utils.observer.MyObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomeController implements MyObserver<ChangedEvent<Prietenie>> {
    private DataController dataController;
    private User user;

    private ObservableList<User> useriList = FXCollections.observableArrayList();
    private ObservableList<Prietenie> cereriList = FXCollections.observableArrayList();
    private Stage stage;

    public void setDataController(DataController controller){
        this.dataController = controller;
        dataController.getServicePrietenii().addObserver(this);
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
        loadData();
    }

    /// Persoane Tab
    @FXML
    private TextField searchField;
    @FXML
    private TableView<User> tableUseri;
    @FXML
    private TableColumn<User, String> numeColumn;
    @FXML
    private TableColumn<User, Button> profileColumn;
    @FXML
    private TableColumn<User, Button> msgColumn;
    @FXML
    private ToggleButton tooglePrieteni;

    /// Cereri Tab
    @FXML
    private TableView<Prietenie> tableCereri;
    @FXML
    private TableColumn<Prietenie, String> numePrieten;
    @FXML
    private TableColumn<Prietenie, String> momentColumn;
    @FXML
    private TableColumn<Prietenie, String> stateColumn;
    @FXML
    private TableColumn<Prietenie, Button> acceptColumn;
    @FXML
    private TableColumn<Prietenie, Button> rejectColumn;

    /**
     * loads data from db and populate lists
     */
    private void loadData(){
        useriList.setAll(dataController.getServiceUser().findAll());
        cereriList.setAll(dataController.getServicePrietenii().findCereri(user));
    }

    public void searchChanged() {
        String searchtext = searchField.getText();
        List<User> useriShown;
        if(tooglePrieteni.isSelected()){
            useriShown = dataController.getServicePrietenii().findPrieteni(user);
        }
        else useriShown = new ArrayList<>(dataController.getServiceUser().findAll());

        useriShown = useriShown.stream().filter(luser -> luser.getName().startsWith(searchtext)).collect(Collectors.toList());
        useriList.setAll(useriShown);
    }

    private void onPrietenieAccepted(Prietenie prietenie){
        dataController.getServicePrietenii().acceptRequest(prietenie);
        loadData();
    }

    private void onPrietenieRejected(Prietenie prietenie){
        dataController.getServicePrietenii().rejectRequest(prietenie);
        loadData();
    }

    private void openProfile(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GraphicApplication.class.getClassLoader().getResource("profile.fxml"));
        Scene profileScene = new Scene(fxmlLoader.load());
        Stage profileStage = new Stage();

        ProfileController ctrl = fxmlLoader.getController();
        ctrl.setDataController(dataController);
        ctrl.setCurrentUser(this.user);
        ctrl.setOtherUser(user);
        ctrl.setStage(profileStage);

        profileStage.setTitle(user.getName()+"'s Profile");
        profileStage.setScene(profileScene);
        profileStage.show();
    }

    private void openMesaje(User user) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(GraphicApplication.class.getClassLoader().getResource("message.fxml"));
        Scene messageScene = new Scene(fxmlLoader.load());
        Stage messageStage = new Stage();

        MessageController ctrl = fxmlLoader.getController();
        ctrl.setDataController(dataController);
        ctrl.setUserFrom(this.user);
        ctrl.setUserTo(user);
        ctrl.setStage(messageStage);

        messageStage.setTitle(user.getName()+"'s Messages");
        messageStage.setScene(messageScene);
        messageStage.show();
    }

    private void initializePersoaneTab(){
        tableUseri.setItems(useriList);
        numeColumn.setCellValueFactory(param -> {
            ObservableValue<String> val = new ObservableValueBase<>() {
                @Override
                public String getValue() {
                    return param.getValue().getName();
                }
            };
            return val;
        });
        profileColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<>() {
                @Override
                public Button getValue() {
                    Button rez = new Button();
                    rez.getStylesheets().add("stylesheet.css");
                    rez.getStyleClass().setAll("vizualizare");
                    rez.setText("Vizualizare");

                    rez.setOnAction(ev -> {
                        try {
                            openProfile(param.getValue());
                        }
                        catch (IOException ex){
                            ex.printStackTrace();
                        }
                    });

                    return rez;
                }
            };
        });
        msgColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<Button>() {
                @Override
                public Button getValue() {
                    Button rez = new Button("Mesaj");
                    rez.getStylesheets().add("stylesheet.css");
                    rez.getStyleClass().setAll("vizualizare");
                    rez.setOnAction(ev -> {
                        try{
                            openMesaje(param.getValue());
                        }
                        catch (IOException ex){
                            ex.printStackTrace();
                        }
                    });
                    return rez;
                }
            };
        });
    }

    private void initializeCereriTab(){
        tableCereri.setItems(cereriList);
        numePrieten.setCellValueFactory(param -> {
            return new ObservableValueBase<>() {
                @Override
                public String getValue() {
                    Prietenie prietenie = param.getValue();
                    return dataController.getServicePrietenii().getOther(prietenie, user).getName();
                }
            };
        });
        momentColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<>() {
                @Override
                public String getValue() {
                    return param.getValue().getFriendsFrom().format(Utils.DATE_TIME_FORMATTER);
                }
            };
        });
        stateColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<String>() {
                @Override
                public String getValue() {
                    return param.getValue().getState().toString();
                }
            };
        });

        acceptColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<Button>() {
                @Override
                public Button getValue() {
                    if(param.getValue().getState() == PrietenieState.Pending) {
                        Button rez = new Button();
                        rez.getStylesheets().add("stylesheet.css");
                        rez.getStyleClass().setAll("acceptBtn");
                        rez.setText("Accept");

                        rez.setOnAction(ev -> {
                            onPrietenieAccepted(param.getValue());
                        });

                        return rez;
                    }
                    return null;
                }
            };
        });
        rejectColumn.setCellValueFactory(param -> {
            return new ObservableValueBase<Button>() {
                @Override
                public Button getValue() {
                    if(param.getValue().getState() == PrietenieState.Pending) {
                        Button rez = new Button();
                        rez.getStylesheets().add("stylesheet.css");
                        rez.getStyleClass().add("rejectBtn");
                        rez.setText("Reject");

                        rez.setOnAction(ev -> {
                            onPrietenieRejected(param.getValue());
                        });

                        return rez;
                    }
                    return null;
                }
            };
        });
    }
    /**
     * init the lists and load data
     */
    @FXML
    public void initialize() {
        initializePersoaneTab();
        initializeCereriTab();
    }

    @Override
    public void update(ChangedEvent<Prietenie> prietenieChangedEvent)
    {
        Prietenie prietenie = prietenieChangedEvent.getData();
        //MessageBox.showSuccessMessage(stage, user.getName()+" received updated prietenie with type "+prietenieChangedEvent.getType().toString());
        switch (prietenieChangedEvent.getType()) {
            case ADDED:
                if((Objects.equals(prietenie.getReceiverId(), user.getId()) || prietenie.getState() == PrietenieState.Accepted) &&
                    cereriList.stream().filter(pr -> pr.contains(prietenie.getFirst(), prietenie.getSecond())).count() == 0)
                        cereriList.add(prietenie);
                break;
            case REMOVED:
                cereriList.remove(prietenieChangedEvent.getOldData());
                break;
            case UPDATED:
                cereriList.remove(prietenieChangedEvent.getOldData());
                cereriList.add(prietenie);
                break;
        }
    }
}
