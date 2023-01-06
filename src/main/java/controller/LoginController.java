package controller;

import domain.User;
import exceptii.MyException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ui.graphic.GraphicApplication;

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
        FXMLLoader fxmlLoader = new FXMLLoader(GraphicApplication.class.getClassLoader().getResource("home.fxml"));
        Scene homeScene = new Scene(fxmlLoader.load());
        Stage homeStage = new Stage();

        HomeController ctrl = fxmlLoader.getController();
        ctrl.setDataController(dataController);
        ctrl.setStage(homeStage);
        ctrl.setUser(user);

        homeStage.setTitle(user.getName()+"'s Home");
        homeStage.setScene(homeScene);
        homeStage.show();
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