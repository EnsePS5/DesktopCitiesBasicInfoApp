package tpo.tpo02_gk_s23161;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class TPOApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TPOApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cities Information Application (CIA)");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.getIcons().add(new Image(new URL
                ("file:///C:/Users/citio/OneDrive/Pulpit/Programowanie%20Java/TPO02_GK_S23161/src/main/resources/Icons/CoveredSun.png").toString()));
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        Service s = new Service("Poland");
        String weatherJson = s.getWeather("Warsaw");
        Double rate1 = s.getRateFor("USD");
        Double rate2 = s.getNBPRate();

        launch();

        System.exit(0);
    }
}