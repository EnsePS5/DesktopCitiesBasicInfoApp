package tpo.tpo02_gk_s23161;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopUpController {

    @FXML
    private TextField cityTextField;
    @FXML
    private TextField currencyTextField;
    @FXML
    private Button acceptButton;

    public void acceptChanges(){
        Stage stage = (Stage) acceptButton.getScene().getWindow();

        //TODO send info to webController

        stage.close();
    }
}
