package ui.graphic;

import controller.DataController;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getClassLoader().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        LoginController ctrl = fxmlLoader.getController();
        ctrl.setDataController(new DataController());
        ctrl.setStage(stage);

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}