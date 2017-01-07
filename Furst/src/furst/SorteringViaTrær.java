/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furst;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hakon
 */
public class SorteringViaTrær implements OppgaveLøsnig {

    private FXMLController main;
    SBinTreKomparator sbintrekomparator = new SBinTreKomparator();
    private SBinTre<SBinTre> linjer = new SBinTre(sbintrekomparator);
    private static HashSet<Character> lovligeBokstaver = lovligeBokstaver();
    

    public SorteringViaTrær(FXMLController main) {
        this.main = main;
    }

    /**
     * Alternativt til lister brukte prøvde jeg å lage en løsning med trær.
     * Tanken var at jeg setter alle ordene inn i et tre forløpende som jeg
     * henter de ut at bufferet. Ved å bruke et Set og sjekke om bokstaven er
     * dem jeg leter, finner jeg substenget jeg skal plukke ut og sette inn i
     * treet.
     *
     * Denne prossess krever litt minnebruk og har egentlig ikke oppnådd noe
     * raskere prosessering.
     *
     * En annen metode jeg hadde lyst å prøve med var prioritert kø, men siden
     * jeg bare hadde 3 korte dager må nok dette vente.
     *
     * Bugs: det ser også ut som at enkelte ord ikke blir lagt inn
     *
     *
     */
    public void SBinTre(BufferedReader in) throws IOException {
        linjer = new SBinTre(sbintrekomparator);
        String linje;
        SBinTre<String> ord;
        while ((linje = in.readLine()) != null) {
            if (!linje.isEmpty()) {
                ord = sorterOrd(linje);
                if (!ord.tom()) {
                    linjer.leggInn(ord);
                }
            }
        }
        oppdaterTekst();
    }

    @Override
    public void oppdaterTekst(BufferedReader in){ 
        try {
            sorterLinjer(in);
            oppdaterTekst();
        } catch (IOException ex) {
            Logger.getLogger(SorteringViaTrær.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    private void sorterLinjer(BufferedReader in) throws IOException{
        linjer = new SBinTre(sbintrekomparator);
        String linje;
        SBinTre<String> ord;
        while ((linje = in.readLine()) != null) {
            if (!linje.isEmpty()) {
                ord = sorterOrd(linje);
                if (!ord.tom()) {
                    linjer.leggInn(ord);
                }
            }
        }
    }

    private SBinTre<String> sorterOrd(String linje) {
        SBinTre<String> ord = new SBinTre(Comparator.naturalOrder());
        int substringStart = 0;
        int substringSlutt = 0;
        boolean start = true;
        for (int i = 0; i < linje.length(); i++) {
            if (lovligeBokstaver.contains(linje.charAt(i))) {
                if (start) {
                    substringStart = i;
                    start = false;
                }
            } else if (!start) {
                substringSlutt = i;
                ord.leggInn(linje.substring(substringStart, substringSlutt));
                start = true;
            }
        }
        return ord;
    }

    private static HashSet lovligeBokstaver() {
        HashSet hashset = new HashSet();
        char bokstaver[] = {'A', 'a', 'B', 'b', 'C', 'c', 'D', 'd', 'E', 'e', 'F', 'f',
            'G', 'g', 'H', 'h', 'I', 'i', 'J', 'j', 'K', 'k', 'L', 'l', 'M', 'm', 'N', 'n',
            'O', 'o', 'P', 'p', 'Q', 'q', 'R', 'r', 'S', 's', 'T', 't', 'U', 'u', 'V', 'v',
            'W', 'w', 'X', 'x', 'Y', 'y', 'Z', 'z', 'Æ', 'æ', 'Ø', 'ø', 'å', 'Å'};
        for (char c : bokstaver) {
            hashset.add(c);
        }
        return hashset;
    }

    /**
     * Iterer alle trærene i inorden. Siden den er sortert blir utskriften
     * alfabetisk
     */
    @Override
    public void oppdaterTekst() {
        Iterator linjeriterator = linjer.iterator();
        Iterator orditerator;
        SBinTre<String> ord;
        StringBuilder sb = new StringBuilder();
        main.clearTextArea();
        int antall_linjer = main.getAntall_linjer();
        int i = 0;
        while (linjeriterator.hasNext()) {
            i++;
            if (i > antall_linjer) {
                break;
            }
            ord = (SBinTre) linjeriterator.next();
            orditerator = ord.iterator();
            while (orditerator.hasNext()) {
                sb.append(orditerator.next()).append(" ");
            }
            sb.append("\n");
            main.appendTextArea(sb.toString());
            sb.setLength(0);

        }

    }

}
