package tpo.tpo02_gk_s23161;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class WebController implements Initializable {

    @FXML
    private WebView webView;

    private WebEngine webEngine;

    private String uRL = "https://pl.wikipedia.org/wiki/";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        webEngine = webView.getEngine();
        changeData();
    }

    public void changeData(){//TODO type in city name
        webEngine.load(uRL);
    }
}