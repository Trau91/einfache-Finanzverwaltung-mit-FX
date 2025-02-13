module com.example.finanzverwaltung {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.naming;


    opens com.example.finanzverwaltung to javafx.fxml;
    exports com.example.finanzverwaltung;
}