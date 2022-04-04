package tpo.tpo02_gk_s23161;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.MalformedURLException;
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
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField currencyTextField;
    @FXML
    private Label currenciesCountry;
    @FXML
    private Label currenciesPLN;
    @FXML
    private Label weatherText;
    @FXML
    private Label tempText;
    @FXML
    private Label countryToGivenText;
    @FXML
    private Label countryToPLNText;
    @FXML
    private ImageView weatherIcon;
    @FXML
    private ImageView tempIcon;
    @FXML
    private ImageView countryToGivenIcon;
    @FXML
    private ImageView countryToPLNIcon;

    private WebEngine webEngine;
    private JSONReader reader;
    private String imageURL;

    private String uRL = "https://pl.wikipedia.org/wiki/";
    private boolean isSecondSceneActive = false;

    // Program starting method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        clock();

        try {
            imageURL = "file:///C:/Users/citio/OneDrive/Pulpit/Programowanie%20Java/TPO02_GK_S23161/src/main/resources/Icons/";

        reader = new JSONReader(new String[]{"Warszawa", "Poland"});

        this.weatherPreparation(reader);

        } catch (IOException e) {
            e.printStackTrace();
        }

        webEngine = webView.getEngine();
        webEngine.load(uRL + "Warszawa");
    }
    // update that happens after button "ok" clicking
    public void acceptChanges() throws IOException {

        String[] cityData = cityTextField.getText().split(",");
        String currency = currencyTextField.getText();

        if (cityData.length == 1)
            reader = new JSONReader(cityData[0]);
        else
            reader = new JSONReader(cityData);

        this.weatherPreparation(reader);

       //TODO SERVICE CONSTRUCTOR?

        //wiki load
        webEngine.load(uRL + cityData[0]);//TODO add country
    }

    // prepares all needed information about weather in current city.
    private void weatherPreparation(JSONReader reader) throws IOException {
        reader.readData();

        Image image = new Image(new URL(imageURL + reader.weatherStatusImage()).toString());
        weatherIcon.setImage(image);

        Image temperature = new Image(new URL(imageURL + reader.tempStatusImage()).toString());
        tempIcon.setImage(temperature);

        weatherText.setText(reader.weatherStatusInfo());
        tempText.setText(reader.tempStatusInfo());
    }
    // Changes scene from wikipedia site to weather and currencies info
    public void changeScene(){
        if (isSecondSceneActive){

            webView.setOpacity(1);

            secondScene.setOpacity(0);
            secondScene.setDisable(true);

            sceneChangeButton.setText("weather and currencies");

            isSecondSceneActive = false;
        }else {

            webView.setOpacity(0);

            secondScene.setOpacity(1);
            secondScene.setDisable(false);

            sceneChangeButton.setText("wikipedia site");

            isSecondSceneActive = true;
        }
    }
    // working clock in left top corner
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