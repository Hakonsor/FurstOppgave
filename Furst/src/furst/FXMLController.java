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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
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
 * FXML Controller class. Jeg har laget loggikken til opggaven i denne klassen,
 * de andre klassene er i stor grad bare til pynt. Jeg er klar over at oppgaven
 * ikke speifiserta at jeg skulle lage et grafisk gresnsesnitt. Jeg antar at
 * hensikten med oppgaven er å vise frem hva jeg kan og har derfor tatt det med.
 * Jeg må beklage om det ikke er med genersike metoder, overlasting og
 * polymorfisme. Jeg erkjenner at det finnes mer elegante løsnigner.
 *
 * @author Håkon Sørby
 */
public class FXMLController implements Initializable {

    @FXML
    private TextArea textarea;

    private int antall_linjer = 10;
    @FXML
    private TextField url_field;
    @FXML
    private TextField antall_settninger_field;
    @FXML
    private Label url_feil;
    @FXML
    private Label settninger_feil;
    @FXML
    private CheckBox spesielle_tegn;

    private boolean spesille_tegn_bool = true;
    private boolean popupbox_bool = true;
    private boolean aktiver_trær = false;
    @FXML
    private CheckBox popupbox;

    private SorteringViaLister svl = new SorteringViaLister(this);
    private SorteringViaTrær svt = new SorteringViaTrær(this);

    /**
     * Initializes the controller class. Denne metoden setter checkboxene til
     * trykket og sette feilmeldingene til rød når programmet starter.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        url_feil.setTextFill(Color.web("#FF0000"));
        settninger_feil.setTextFill(Color.web("#FF0000"));
        spesielle_tegn.setSelected(true);
        popupbox.setSelected(true);
    }

    /**
     *
     * Åpner et vindu der brukeren kan navigere til filen som skal bli åpnet.
     * Etter at filen har blitt valg blir oppdaterTekst kalt
     *
     * Feilhåndering: Jeg har ikke støtt på Execption call under begrenset
     * testing, derfor er det bare utskrift til terminalern.
     */
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
            popupbox();
            long tid = System.currentTimeMillis();
            if (aktiver_trær) {
                svt.SBinTre(in);
                System.out.println(System.currentTimeMillis() - tid);
            } else {
                svl.oppdaterTekst(in);
                System.out.println(System.currentTimeMillis() - tid);
            }
        } catch (IOException e) {
            System.out.println("Korrupt filtype");
            System.out.println(e);
        }
    }

    /**
     * Når "hent fil fra url" knappen blir trykket blir teksten ifra url_field
     * og omgjort til en url objekt. Deretter blir det gjort et forsøk på å
     * laste inn innholde ifra andressen til et buffer. Hvis det skjer en feil
     * under lesing kan brukeren se det dukke opp Ugyldig url. Kaller på
     * oppdatert tekst hvis alt gikk bra.
     *
     * @param event blir ikke brukt
     */
    @FXML
    private void lastNedUrl(ActionEvent event) {
        URL url;
        try {
            url = new URL(url_field.getText());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            popupbox();
            if (aktiver_trær) {
                svt.SBinTre(in);
            } else {
                svl.oppdaterTekst(in);
            }
            url_feil.setText("");
        } catch (MalformedURLException ex) {
            url_feil.setText("Ugyldig url");
        } catch (IOException e) {
            url_feil.setText("Ugyldig url");
        }

    }

    /**
     * Henter altall settinger ifra antall_settninger_field, sjekker om den
     * innholder kun tall. Hvis den innholder ulovelig karakterer så vil en
     * label med skriften "Kun heltall" lyse under antall_settninger_field. Hvis
     * tallet er for stort så blir numberformatexeption kasten og Skriften for
     * stort tall vil lyse istedenfor.
     *
     * Forbedrlinger: regex utrykket kan bli endret til å kun godkjenne tall
     * under en vis lengde, men da trengs den blir det kun en feilmelding.
     * Altervativ kan den være en eksta funskjon som sider att alle settinger i
     * listen skal skrives ut.
     *
     * @param event
     */
    @FXML
    private void antallSettninger(KeyEvent event) {
        String regex = "[0-9]+";
        if (antall_settninger_field.getText().matches(regex)) {
            try {
                antall_linjer = Integer.parseInt(antall_settninger_field.getText());
                if (aktiver_trær) {
                    svt.oppdaterTekst();
                } else {
                    svl.oppdaterTekst();
                }
                settninger_feil.setText("");
            } catch (NumberFormatException feil) {
                settninger_feil.setText("For stort tall");
            }
        } else {
            settninger_feil.setText("Kun heltall");
        }

    }

    /**
     * Spør brukeren om hvis mange linjer han ønsker å bruke
     */
    private void popupbox() {
        if (getPopupbox_bool()) {
            Popup.display();
            antall_linjer = Popup.antall_linjer;
            setAntall_settninger_field(Integer.toString(antall_linjer));

        }
    }

    /**
     * Endrer verdien til boolean som avgjør om Spesielle tegn skal fjernes
     * eller ikke
     *
     * @param event
     */
    @FXML
    private void setSpesielleTegn(ActionEvent event
    ) {
        spesille_tegn_bool = !spesille_tegn_bool;
    }

    /**
     * Endrer verdien til boolean som avgjør om popupbox skal vises eller ikke
     *
     * @param event
     */
    @FXML
    private void setPopupbox(ActionEvent event
    ) {
        popupbox_bool = !popupbox_bool;
    }

    public void clearTextArea() {
        textarea.clear();
    }

    public boolean getSpesille_tegn_bool() {
        return spesille_tegn_bool;
    }

    public int getAntall_linjer() {
        return antall_linjer;
    }

    public void setAntall_linjer(int linjer) {
        antall_linjer = linjer;
    }

    public boolean getPopupbox_bool() {
        return popupbox_bool;
    }

    public void setAntall_settninger_field(String s) {

        antall_settninger_field.setText(s);
    }
    
    /**
     * Under veldig store filer skulle det egentlig skrives ut en linje om gangen.
     * Men får å oppnå det må jeg implementer tråder. Jeg har sett litt på løsninger får dette 
     * men har ikke funnet en som fungerte bra med fx.
     * 
     * @param linje teksten som skal skrives ut til vindu
     */

    public void appendTextArea(String linje) {
        textarea.appendText(linje);
    }

    @FXML
    private void aktiver_trær(ActionEvent event) {
        if (aktiver_trær = !aktiver_trær) {
            spesielle_tegn.disarm();
            spesielle_tegn.setVisible(false);
        } else {
            spesielle_tegn.arm();
            spesielle_tegn.setVisible(true);
        }

    }

}
