package controller;

import domain.Prietenie;
import domain.PrietenieState;
import domain.User;
import utils.Utils;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController {
    private DataController dataController;
    private User user;

    private ObservableList<User> useriList = FXCollections.observableArrayList();
    private ObservableList<Prietenie> cereriList = FXCollections.observableArrayList();
    private Stage stage;

    public void setDataController(DataController controller){
        this.dataController = controller;
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

    }

    private void onPrietenieRejected(Prietenie prietenie){

    }

    private void openProfile(User user){

    }

    /**
     * init the lists and load data
     */
    @FXML
    public void initialize() {
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
                    rez.setStyle("-fx-font-family: Verdana; -fx-font-size: 12px; -fx-background-color: rgba(28,167,227,0.5)");
                    rez.setText("Vizualizare");

                    rez.setOnAction(ev -> {
                        openProfile(param.getValue());
                    });

                    return rez;
                }
            };
        });

        tableCereri.setItems(cereriList);
        numePrieten.setCellValueFactory(param -> {
            return new ObservableValueBase<String>() {
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
                        rez.setStyle("-fx-font-family: Verdana; -fx-font-size: 12px; -fx-background-color: rgba(50,222,50,0.5)");
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
                        rez.setStyle("-fx-font-family: Verdana; -fx-font-size: 12px; -fx-background-color: rgba(220,32,32,0.5)");
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
}
