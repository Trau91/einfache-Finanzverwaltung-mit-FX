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

    private double kontostand;
    private boolean unsavedChanges = false;
    private int actionCount = 0;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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

    public void addAktion(String aktion) {
            aktionenView.getItems().add(aktion);
            actionCount++;

        if (actionCount > 5) {
            Platform.runLater(() -> {
                aktionenView.getItems().remove(0);
            });
            }
    }


    public void setUnsavedChanges(boolean hasUnsavedChanges) {
        this.unsavedChanges = hasUnsavedChanges;
    }

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
                saveChanges();
                System.exit(0);


            }
        } else {
            System.exit(0);
        }

    }

    @FXML
    public void btnDown_Click(ActionEvent event) {

              try {
                double betrag = Double.parseDouble(inputBetrag.getText());
                kontostand -= betrag;
                outputKontostand.setText(String.format("%.2f", kontostand));

                if (kontostand < 500) {
                    Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Achtung!");
                    confirmationDialog.setHeaderText("Ihr Kontostand beträgt weniger als 500 Euro.");
                    confirmationDialog.setContentText("Drücken Sie ok um zu beenden.");

                    ButtonType buttonTypeOk = new ButtonType("Ok");
                    Optional<ButtonType> result = confirmationDialog.showAndWait();
                    if (result.get() == buttonTypeOk) {
                        setUnsavedChanges(true);

                    }
                }else {
                    addAktion("Abzug: " + betrag + " €");
                    aktionenView.getItems().addAll();
                    setUnsavedChanges(true);
                }


            } catch (NumberFormatException e) {
                System.out.println("Bitte geben Sie einen gültigen Betrag ein.");
            }
        }

    @FXML
    public void btnSave_Click(ActionEvent event) {
        try {
            FileWriter writer = new FileWriter("kontostand.txt");
            writer.write(String.valueOf(kontostand));
            writer.close();
            System.out.println("Kontostand gespeichert.");
            setUnsavedChanges(false);
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern: " + e.getMessage());
        }
    }

    @FXML
    public void btnUp_Click(ActionEvent event) {
        try {
            double betrag = Double.parseDouble(inputBetrag.getText());
            kontostand += betrag;
            outputKontostand.setText(String.format("%.2f", kontostand));
            addAktion("Erhöhung: " + betrag + " €");
            aktionenView.getItems().addAll();
            setUnsavedChanges(true);
        } catch (NumberFormatException e) {
            System.out.println("Bitte geben Sie einen gültigen Betrag ein.");
        }
    }

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

    @FXML
    public void btnLoad_Click(ActionEvent event) {
        ladeKontostand();
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        ladeKontostand();
        ObservableList<String> items = FXCollections.observableArrayList(aktionenView.getItems());
        aktionenView.setItems(items);


    }

}
