package tpo.tpo02_gk_s23161;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class WebController implements Initializable {

    @FXML
    private WebView webView;
    @FXML
    private Label clockLabel;

    private WebEngine webEngine;

    private String uRL = "https://pl.wikipedia.org/wiki/";
    private boolean stopTimer = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        clock();

        webEngine = webView.getEngine();
        changeData();
    }

    public void changeData(){//TODO type in city name
        webEngine.load(uRL);
    }

    public void changeScene(){

    }

    private void clock(){

        Thread timeThread = new Thread(() -> {

            while (!stopTimer){
                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Calendar calendar = new GregorianCalendar();
                String currentTime;

                if (calendar.get(Calendar.SECOND)%2 == 0)
                    currentTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
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