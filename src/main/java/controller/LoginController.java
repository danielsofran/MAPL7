package controller;

import domain.User;
import exceptii.MyException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ui.graphic.LoginApplication;

import java.io.IOException;

public class LoginController {
    private DataController dataController;
    private Stage stage;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField pwdField;

    public void setDataController(DataController controller){
        this.dataController = controller;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void showHome(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getClassLoader().getResource("home.fxml"));
        Scene homeScene = new Scene(fxmlLoader.load());

        HomeController ctrl = fxmlLoader.getController();
        ctrl.setDataController(dataController);
        ctrl.setStage(stage);
        ctrl.setUser(user);

        stage.setTitle("Home");
        stage.setScene(homeScene);
    }

    public void Login() {
        String loginText = userField.getText();
        String pwdText = pwdField.getText();
        try{
            User user = dataController.getServiceUser().login(loginText, pwdText);
            showHome(user);
        }
        catch (MyException ex){
            MessageBox.showErrorMessage(stage, ex.getMessage());
        }
        catch (IOException ioex){
            ioex.printStackTrace();
        }
    }
}