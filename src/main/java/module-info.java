module tpo.tpo02_gk_s23161 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.google.gson;

    opens tpo.tpo02_gk_s23161 to javafx.fxml;
    exports tpo.tpo02_gk_s23161;
}