package com.example.finanzverwaltung;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import javafx.scene.control.Alert.AlertType;



public class Controller implements Initializable {

    // Speichert den aktuellen Kontostand.
    private double kontostand;

    // Flag, das angibt, ob ungespeicherte Änderungen vorliegen.
    private boolean unsavedChanges = false;

    // Zählt die Anzahl der Aktionen, um die Listview zu begrenzen.
    private int actionCount = 0;

    // FXML-Ressourcen und Location.
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    // FXML-Elemente (Buttons, Textfelder usw. die mit der FXML-Datei verbunden sind.
    @FXML
    private Button btnClose;

    @FXML
    private Button btnDown;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUp;

    @FXML
    private Button btnLoad;

    @FXML
    private TextField inputBetrag;

    @FXML
    private TextField outputKontostand;

    @FXML
    private ListView<String> aktionenView;

    /**
     * Fügt eine Aktion zur ListView hinzu und begrenzt die Anzahl der Einträge auf 5
     * @param aktion die hinzugefügt werden soll.
     */
    public void addAktion(String aktion) {
            aktionenView.getItems().add(aktion);
            actionCount++;

        if (actionCount > 5) {
            Platform.runLater(() -> {
                aktionenView.getItems().remove(0);
            });
            }
    }

    /**
     * Setzt das Flag für ungespeicherte Änderungen.
     * @param hasUnsavedChanges True, wenn ungespeicherte Änderungen vorliegen, ansonsten False.
     */
    public void setUnsavedChanges(boolean hasUnsavedChanges) {
        this.unsavedChanges = hasUnsavedChanges;
    }

    /**
     * Speichert den aktuellen Kontostand in der Datei "kontostand.txt".
     */
    public void saveChanges() {
        try {
            FileWriter writer = new FileWriter("kontostand.txt");
            writer.write(String.valueOf(kontostand));
            writer.close();
            System.out.println("Kontostand gespeichert.");

        } catch (IOException e) {
            System.out.println("Fehler beim Speichern: " + e.getMessage());

        }
    }

    /**
     * Behandelt den klick auf dem "btnClose"- Button.
     * Fragt nach, ob ungespeicherte Änderungen gespeichert werden sollen, bevor die Anwendung geschlossen wird.
     * @param event ActionEvent
     */
    @FXML
    public void btnClose_Click(ActionEvent event) {
        if (unsavedChanges) {
            Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Achtung!");
            confirmationDialog.setHeaderText("Ungespeicherte Änderungen vorhanden.");
            confirmationDialog.setContentText("Möchten Sie speichern?");

            ButtonType buttonTypeYes = new ButtonType("Ja");
            ButtonType buttonTypeNo = new ButtonType("Nein");
            confirmationDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> result = confirmationDialog.showAndWait();
            if (result.get() == buttonTypeYes) {
                saveChanges(); // Speichert die Änderungen
                System.exit(0); // Beendet die Anwendung.


            }
        } else {
            System.exit(0); // Beendet die Anwendung, wenn keine ungespeicherten Änderungen vorliegen.
        }

    }

    /**
     * Behandelt den klick auf dem "btnDown"- Button.
     * Zieht den eingegebenen Betrag vom Kontostand ab und aktualisiert die Benutzeroberfläche.
     * @param event
     */
    @FXML
    public void btnDown_Click(ActionEvent event) {

              try {
                double betrag = Double.parseDouble(inputBetrag.getText());
                kontostand -= betrag;
                outputKontostand.setText(String.format("%.2f", kontostand));

                // Zeigt eine Warnung an, wenn der Kontostand unter 500 Euro fällt
                if (kontostand < 500) {
                    Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Achtung!");
                    confirmationDialog.setHeaderText("Ihr Kontostand beträgt weniger als 500 Euro.");
                    confirmationDialog.setContentText("Drücken Sie ok um zu beenden.");

                    ButtonType buttonTypeOk = new ButtonType("Ok");
                    Optional<ButtonType> result = confirmationDialog.showAndWait();
                    if (result.get() == buttonTypeOk) {
                        setUnsavedChanges(true); // Markiert Änderungen als ungespeichert.

                    }
                }else {
                    addAktion("Abzug: " + betrag + " €"); // Fügt die Aktion zur ListView hinzu.
                    aktionenView.getItems().addAll(); // Aktualisiert die ListView.
                    setUnsavedChanges(true); // Hier werden Änderungen als ungespeichert Markiert.
                }


            } catch (NumberFormatException e) {
                System.out.println("Bitte geben Sie einen gültigen Betrag ein.");
            }
        }

    /**
     * Behandelt den "btnSave"- Button.
     * Speichert den aktuellen Kontostand und setzt das Flag für ungesicherte Änderungen zurück.
     * @param event
     */
    @FXML
    public void btnSave_Click(ActionEvent event) {
        saveChanges(); // Speichert die Änderungen.
        setUnsavedChanges(false); // Setzt das Flag zurück.
    }

    /**
     * Behandelt den Klick auf den "btnUP"-Button.
     * Addiert den eingegebenen Betrag zum Kontostand und aktualisiert die Benutzeroberfläche.
     *
     * @param event
     */
    @FXML
    public void btnUp_Click(ActionEvent event) {
        try {
            double betrag = Double.parseDouble(inputBetrag.getText());
            kontostand += betrag;
            outputKontostand.setText(String.format("%.2f", kontostand));
            addAktion("Erhöhung: " + betrag + " €"); // Fügt die Aktion zur ListView hinzu.
            aktionenView.getItems().addAll(); // Aktualisiert die ListView.
            setUnsavedChanges(true); // Markiert Änderungen als ungespeichert.
        } catch (NumberFormatException e) {
            System.out.println("Bitte geben Sie einen gültigen Betrag ein.");
        }
    }

    /**
     * Lädt den Kontostand aus der Datei "kontostand.txt" und aktualisiert das Textfeld.
     */
    public void ladeKontostand() {
        try {
            // Lese den Kontostand aus der Datei
            String filePath = "kontostand.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            kontostand = Double.parseDouble(line);
            outputKontostand.setText(String.format("%.2f", kontostand));
        } catch (IOException | NumberFormatException e) {
            // Fehler beim Laden oder Parsen
            outputKontostand.setText("Fehler beim Laden des Kontostands");
        }
    }

    /**
     * Behandelt den Klick auf den "Laden"-Button.
     * Lädt den Kontostand aus der Datei.
     *
     * @param event Das ActionEvent.
     */
    @FXML
    public void btnLoad_Click(ActionEvent event) {
        ladeKontostand(); // Lädt den Kontostand.
    }

    /**
     * Initialisiert den Controller.
     * Lädt den Kontostand und aktualisiert die ListView beim Start der Anwendung.
     *
     * @param url Die URL des FXML-Dokuments
     * @param rb  ResourceBundle
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        ladeKontostand();
        ObservableList<String> items = FXCollections.observableArrayList(aktionenView.getItems());
        aktionenView.setItems(items);


    }

}
