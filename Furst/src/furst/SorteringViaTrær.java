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

/**
 *
 * @author hakon
 */
public class SorteringViaTrær {

private FXMLController main;
SBinTreKomparator sbintrekomparator = new SBinTreKomparator();
private SBinTre<SBinTre> linjer = new SBinTre(sbintrekomparator);
public SorteringViaTrær(FXMLController main){
    this.main = main;
}

    /**
     * Alternativt til lister brukte prøvde jeg å lage en løsning med trær.
     * Tanken var at jeg setter alle ordene inn i et tre forløpende som jeg henter de ut at 
     * bufferet. Ved å bruke et set og sjekke om bokstaven er dem jeg leter,
     * finner jeg substenget jeg skal plukke ut og sette inn i treet. 
     * 
     * Denne prossess krever litt minnebruk og har egentlig ikke oppnådd noe raskere prosessering.
     * 
     * En annen medot jeg hadde lyst å pøve med var priortert kø, 
     * men siden jeg bare hadde 3 korte dager må nok dette vente.
     *
     * Bugs: det ser også ut som at enkelte ord ikke blir lagt inn
     * 
     * 
     */
    public void SBinTre(BufferedReader in) throws IOException {
        linjer = new SBinTre(sbintrekomparator);
        String linje;
        int substringStart = 0;
        int substringSlutt = 0;
        boolean start = true;
        HashSet bokstaver = lovligeBokstaver();
        SBinTre<String> ord;
        while ((linje = in.readLine()) != null) {
            if (!linje.isEmpty()) {
                ord = new SBinTre(Comparator.naturalOrder());
                substringStart = 0;
                substringSlutt = 0;
                start = true;
                for (int i = 0; i < linje.length(); i++) {
                    if (bokstaver.contains(Character.toString(linje.charAt(i)))) {
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
                if(!ord.tom()){
                   linjer.leggInn(ord);  
                }
            }
        }
            oppdaterTekst();
    }

    private HashSet lovligeBokstaver() {
        HashSet hashset = new HashSet();
        String bokstaver[] = {"A", "a", "B", "b", "C", "c", "D", "d", "E", "e", "F", "f",
            "G", "g", "H", "h", "I", "i", "J", "j", "K", "k", "L", "l", "M", "m", "N", "n",
            "O", "o", "P", "p", "Q", "q", "R", "r", "S", "s", "T", "t", "U", "u", "V", "v",
            "W", "w", "X", "x", "Y", "y", "Z", "z", "Æ", "æ", "Ø", "ø", "å", "Å" };
        hashset.addAll(Arrays.asList(bokstaver));
        return hashset;
    }

    /**
     * Iterer alle trærene i inorden. Siden den er sortert blir utskriften alfabetisk 
     */
    public void oppdaterTekst() {
        Iterator linjeriterator = linjer.iterator();
        Iterator orditerator;
        SBinTre<String> ord;
        StringBuilder sb = new StringBuilder();
        main.clearTextArea();
        int antall_linjer = main.getAntall_linjer();
        int i = 0;
        
         while(linjeriterator.hasNext()){
             i++;
             if(i > antall_linjer){
                 break;
             }
             ord = (SBinTre)linjeriterator.next();
             orditerator = ord.iterator();
             while(orditerator.hasNext()){
                 sb.append(orditerator.next()).append(" ");
             }
             sb.append("\n");
             main.appendTextArea(sb.toString());
             sb.setLength(0);
             
         }
       
    }
    
}
