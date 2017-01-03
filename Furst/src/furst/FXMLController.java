/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furst;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hakon
 */
public class FXMLController implements Initializable {

    @FXML
    private TextArea textarea;

    private ArrayList<String> linje_liste = new ArrayList();

    private int antall_linjer = 10;
    @FXML
    private TextField url_field;
    @FXML
    private TextField antall_settninger_field;
    @FXML
    private Label url_feil;
    @FXML
    private Label settninger_feil;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        url_feil.setTextFill(Color.web("#FF0000"));
        settninger_feil.setTextFill(Color.web("#FF0000"));
    }

    @FXML
    public void lesFil() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Velg fil");
        File file = chooser.showOpenDialog(new Stage());

        if (file == null) {
            return;
        }

        try {
            BufferedReader in = new BufferedReader(new FileReader(file.toString()));
            oppdaterTekst(in);
        } catch (IOException e) {
            System.out.println("Korrupt filtype");
            System.out.println(e);
        }

    }

    private void oppdaterTekst(BufferedReader in) {
        String linje;
        String[] ord;
        linje_liste.clear();
        try {
            while ((linje = in.readLine()) != null) {
                if (!linje.isEmpty()) {
                    ord = linje.split(" ");
                    Arrays.sort(ord);
                    StringJoiner settning = new StringJoiner(" ");
                    for (String s : ord) {
                        if (!s.isEmpty()) {
                            settning.add(s);
                        }
                    }
                    linje_liste.add(settning.toString());
                }
            }
            Collections.sort(linje_liste);
            oppdaterTekst();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void oppdaterTekst() {
        textarea.clear();
        int i = 0;
            for (String s : linje_liste) {
                i++;
                if (i > antall_linjer) {
                    break;
                }
                textarea.appendText(s);
                textarea.appendText("\n");

            }
    }


    @FXML
    private void lastNedUrl(ActionEvent event) {
        URL url;
        try {
            url = new URL(url_field.getText());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            oppdaterTekst(in);
            url_feil.setText("");

        } catch (MalformedURLException ex) {
            url_feil.setText("Ugyldig url");
        } catch (IOException e) {
            url_feil.setText("Ugyldig url");
        }

    }

    @FXML
    private void antallSettninger(KeyEvent event) {
        String regex = "[0-9]+";
        if(antall_settninger_field.getText().matches(regex)){
            antall_linjer = Integer.parseInt(antall_settninger_field.getText());
            oppdaterTekst();
            settninger_feil.setText("");
        }else{
            settninger_feil.setText("Kun heltall");
        }
        
    }


//scan.nextLine().replaceAll("[^a-zA-Z0-9]", ""

}
