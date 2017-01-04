
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
 * Jeg må beklage om det ikke er med genersike metoder, overlasting og polymorfisme.
 * Jeg erkjenner at det finnes mer elegante løsnigner.
 *
 * @author Håkon Sørby
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
    @FXML
    private CheckBox spesielle_tegn;

    private boolean spesille_tegn_bool = true;
    private boolean popupbox_bool = true;
    @FXML
    private CheckBox popupbox;

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
            oppdaterTekst(in);
        } catch (IOException e) {
            System.out.println("Korrupt filtype");
            System.out.println(e);
        }
    }

    /**
     * Fjerner alle tegn utenom nokstaver og mellomrom. Fjerner deretter alle
     * mellomrom og sjekker om strenger er tom.
     *
     * Optimaliserting muligheter: Denne metoden sjekker stenger to ganger og
     * jeg anser dette får å være unødvendig kostbart TODO: Finne bedre løsning
     *
     * @param s linjen som skal fjerne alle spesielle tegn
     * @return String uten spesielle tegn
     */
    private String fjernSpesielleTegn(String s) {
        String emty;
        s = s.replaceAll("[^ÆØÅæøåa-zA-Z ]", "");
        emty = s.replaceAll("[ ]", "");
        if (emty.isEmpty()) {
            return emty;
        }
        return s;
    }

    /**
     * oppdaterTekst er et førsteutkast på lønsning av oppgaven slik jeg tolket
     * den. Jeg er usikker på om dere ønsket at jeg skulle skrive en egen
     * sorterings algoritme og har derfor startet med å lage en ekel variant og
     * bruker heller tid på omtimalisering til slutt. Hvis jeg får tid kommer
     * det en løsning i Stakker eller trær.
     *
     * Metoden tar inn en buffer og går igjennom alle settningene linje for
     * linje. Hvis fjernSpesielleTegn er haket av så blir først alle tegn utenom
     * bokstaver fjernet ifra teksten. Unødvendige mellomrom blir også fjernet
     * ifra teksten. Linjene blir splittet på mellomrom og sortert i Java sitt
     * innbygget soreringsmetode for arrays. Linjene blir deretter lagt inn i en
     * liste og sortert via colletions innebygget sorterings metode. Jeg har
     * forløpig ikke lagt til en norsk Comparator som tar hesyn til øæå. Siden
     * den var spesifisert at brukeren skal bli spørret om antall linjer har
     * lagt til en popup box som spørr om dette.
     *
     * Optimalisertings muligheter: En linje blir først fjernet for alle mulige
     * mellomrom og spesielle tegn før sortering. Neste utkast vil jeg prøve å
     * gjøre disse prosessene samtidtig får å unngå unødvendige kostnader. Denne
     * metoden leser av linje til linje. Alternativet skulle jeg lese av til
     * nermeste punktum, i oppgaven er det derimor spesifiert at jeg skulle lese
     * av linjer.
     *
     * Feilhåndering: Jeg har ikke støtt på Execption call under begrenset
     * testing, derfor er det bare utskrift til terminalern.
     *
     * @param in BufferReader som innholder txt filen
     */
    private void oppdaterTekst(BufferedReader in) {
        String linje;
        linje_liste.clear();
        try {
            while ((linje = in.readLine()) != null) {
                if (spesille_tegn_bool) {
                    linje = fjernSpesielleTegn(linje);
                }
                if (!linje.isEmpty()) {
                    linje = sorterOrd(linje);
                    linje_liste.add(linje);
                }
            }
            Collections.sort(linje_liste);
            if (popupbox_bool) {
                Popup.display();
                antall_linjer = Popup.antall_linjer;
                antall_settninger_field.setText(Integer.toString(antall_linjer));
            }
            oppdaterTekst();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Splitter en Streng på mellomrom inn i et Array og deretter sorterer via
     * Arrays.sort.
     *
     * Forbedring muligheter: Det er mulig på endre regler for sortering ved å
     * bruke Arrays.sort(setting, ( en norsk Comparator))
     *
     * @param settning
     * @return alle ordene sortert
     */
    private String sorterOrd(String settning) {
        String[] ord;
        ord = settning.split(" ");
        Arrays.sort(ord);
        StringJoiner nysettning = new StringJoiner(" ");
        for (String s : ord) {
            if (!s.isEmpty()) {
                nysettning.add(s);
            }
        }
        return nysettning.toString();
    }

    /**
     * Fjerner gammel tekst ifra textarea og printer inn gitt antall linjer ifra
     * en liste.
     *
     * Altervativ: Bruken av foreach løkken med en break er kanskje en rar måte
     * å løse oppgaven på, men jeg ser ikke noen grun til at den skal være noe
     * dårligere en andre varianter.
     *
     */
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
            oppdaterTekst(in);
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
                oppdaterTekst();
                settninger_feil.setText("");
            } catch (NumberFormatException feil) {
                settninger_feil.setText("For stort tall");
            }
        } else {
            settninger_feil.setText("Kun heltall");
        }

    }

    /**
     * Endrer verdien til boolean som avgjør om Spesielle tegn skal fjernes
     * eller ikke
     *
     * @param event
     */

    @FXML
    private void setSpesielleTegn(ActionEvent event) {
        if (spesille_tegn_bool) {
            spesille_tegn_bool = false;
        } else {
            spesille_tegn_bool = true;
        }
    }

    /**
     * Endrer verdien til boolean som avgjør om popupbox skal vises eller ikke
     *
     * @param event
     */

    @FXML
    private void setPopupbox(ActionEvent event) {
        if (popupbox_bool) {
            popupbox_bool = false;
        } else {
            popupbox_bool = true;
        }
    }
}
