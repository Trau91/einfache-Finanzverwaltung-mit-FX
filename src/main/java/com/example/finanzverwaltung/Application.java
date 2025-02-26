package com.example.finanzverwaltung;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Application extends javafx.application.Application {

    /**
     * Die Hauptmethode, die beim Start der JavaFX- Anwendung aufgerufen wird.
     * Sie lädt die FXML-Datei, erstellt die Szene und zeigt das Fenster an.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Erstellt einen FXMLLoader, um die FXML-Datei "hello-view-fxml" zu laden.
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));

        // Lädt die FXML-Datei und erstellt eine Szene mit den geladenen Elementen.
        // Die Szene hat eine Breite von 550 Pixeln und eine Höhe von 450 Pixeln.
        Scene scene = new Scene(fxmlLoader.load(), 550, 450);

        // Setzt den Titel des Fensters auf "Finanzverwaltung".
        stage.setTitle("Finanzverwaltung");

        // Setzt die Szene des Fenster auf die erstellte Szene.
        stage.setScene(scene);

        //Zeigt das Fenster an.
        stage.show();

    }

    /**
     * Die Hauptmethode der Anwendung.
     * Sie startet die JavaFX-Anwendung.
     *
     * @param args werden hier nicht verwendet
     */

    public static void main(String[] args) {
        launch();
    }
}