/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furst;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hakon
 */
public class SorteringViaLister implements OppgaveLøsnig{

    FXMLController main;
    private ArrayList<String> linje_liste = new ArrayList();
    private int antall_linjer;

    public SorteringViaLister(FXMLController m) {
        main = m;
        antall_linjer = main.getAntall_linjer();
    }

    /**
     * oppdaterTekst er et førsteutkast på løsning av oppgaven slik jeg tolket
     * den. Jeg er usikker på om dere ønsket at jeg skulle skrive en egen
     * sorterings algoritme og har derfor startet med å lage en enkel variant og
     * bruker heller tid på optimalisering til slutt. Hvis jeg får tid kommer
     * det en løsning i Stakker eller trær.
     *
     *
     * Metoden tar inn en buffer og går igjennom alle setningene linje for
     * linje. Hvis fjernSpesielleTegn er haket av så blir først alle tegn utenom
     * bokstaver fjernet ifra teksten. Unødvendige mellomrom blir også fjernet
     * ifra teksten. Linjene blir splittet på mellomrom og sortert i Java sitt
     * innbygget soreringsmetode for arrays. Linjene blir deretter lagt inn i en
     * liste og sortert via Collections innebygget sorterings metode. Jeg har
     * foreløpig ikke lagt til en norsk Komparator som tar hensyn til øæå. 
     *
     * Optimaliserings muligheter: En linje blir først fjernet for alle mulige
     * mellomrom og spesielle tegn før sortering. Neste utkast vil jeg prøve å
     * gjøre disse prosessene samtidig får å unngå unødvendige kostnader. Denne
     * metoden leser av linje til linje. Alternativet skulle jeg lese av til
     * nærmeste punktum, i oppgaven er det derimot spesifisert at jeg skulle lese
     * av linjer.
     *
     * Feilhåndtering: Jeg har ikke støtt på Execption call under begrenset
     * testing, derfor er det bare utskrift til terminalen.

     *
     * @param in BufferReader som innholder txt filen
     */
    public void oppdaterTekst(BufferedReader in) {
        String linje;
        linje_liste.clear();
        try {
            while ((linje = in.readLine()) != null) {
                if (main.getSpesille_tegn_bool()) {
                    linje = fjernSpesielleTegn(linje);
                }
                if (!linje.isEmpty()) {
                    linje = sorterOrd(linje);
                    linje_liste.add(linje);
                }
            }
            Collections.sort(linje_liste, Comparator.naturalOrder());
            oppdaterTekst();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Fjerner gammel tekst ifra textarea og printer inn gitt antall linjer ifra
     * en liste. Textarea.appendText
     *
     * Alternativ: Bruken av foreach løkken med et break er kanskje en rar måte
     * å løse oppgaven på, men jeg ser ikke noen grunn til at den skal være noe
     * dårligere en annen variant.
     *
     */
    public void oppdaterTekst() {
        main.clearTextArea();
        antall_linjer = main.getAntall_linjer();
        int i = 0;
        for (String s : linje_liste) {
            i++;
            if (i > antall_linjer) {
                break;
            }
            main.appendTextArea(s);
            main.appendTextArea("\n");
        }
    }

    /**
     * Splitter en Streng på mellomrom inn i et Array og deretter sorterer via
     * Arrays.sort.
     *
     * Forbedring muligheter: Det er mulig på endre regler for sortering ved å
     * bruke Arrays.sort(setting, ( en norsk Komparator))
     *
     * @param setning
     * @return alle ordene sortert
     */

    private String sorterOrd(String settning) {
        String[] ord;
        ord = settning.split(" ");
        Arrays.sort(ord, Comparator.naturalOrder());
        StringJoiner nysettning = new StringJoiner(" ");
        for (String s : ord) {
            if (!s.isEmpty()) {
                nysettning.add(s);
            }
        }
        return nysettning.toString();
    }
    

    /**
     * Fjerner alle tegn utenom bokstaver og mellomrom. Fjerner deretter alle
     * mellomrom.
     * @param s linjen som skal fjerne alle spesielle tegn
     * @return String uten spesielle tegn
     */

    private String fjernSpesielleTegn(String s) {
        s = s.replaceAll("[^ÆØÅæøåa-zA-Z ]", "");
        return s.trim();
    }

}
