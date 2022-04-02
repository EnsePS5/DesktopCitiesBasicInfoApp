package tpo.tpo02_gk_s23161;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.ResourceBundle;

public class WebController implements Initializable {

    @FXML
    private WebView webView;
    @FXML
    private Label clockLabel;
    @FXML
    private GridPane secondScene;
    @FXML
    private Button sceneChangeButton;


    private WebEngine webEngine;


    private String uRL = "https://pl.wikipedia.org/wiki/Warszawa";
    private boolean isSecondSceneActive = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        clock();

        webEngine = webView.getEngine();
        webEngine.load(uRL);
    }

    public void changeDataWindow() throws IOException {//TODO type in city name

        Scene scene = new Scene(FXMLLoader.load((Objects.requireNonNull(getClass().getResource("popUp.fxml")))));

        Stage stage = new Stage();
        stage.setTitle("Type in city name and currency code");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(){
        if (isSecondSceneActive){

            webView.setOpacity(1);
            secondScene.setOpacity(0);

            sceneChangeButton.setText("weather and currencies");

            isSecondSceneActive = false;
        }else {

            webView.setOpacity(0);
            secondScene.setOpacity(1);

            sceneChangeButton.setText("wikipedia site");

            isSecondSceneActive = true;
        }
    }

    private void clock(){

        Thread timeThread = new Thread(() -> {

            while (true){
                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Calendar calendar = new GregorianCalendar();
                String currentTime;

                if (calendar.get(Calendar.SECOND)%2 == 0) {
                    if (calendar.get(Calendar.MINUTE) < 10)
                        currentTime = calendar.get(Calendar.HOUR_OF_DAY) + ":0" + calendar.get(Calendar.MINUTE);
                    else
                        currentTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                }
                else if (calendar.get(Calendar.MINUTE) < 10)
                    currentTime = calendar.get(Calendar.HOUR_OF_DAY) + " 0" + calendar.get(Calendar.MINUTE);
                else
                    currentTime = calendar.get(Calendar.HOUR_OF_DAY) + " " + calendar.get(Calendar.MINUTE);

                Platform.runLater(()-> {
                    clockLabel.setText(currentTime);
                });
            }
        });
        timeThread.start();
    }
}