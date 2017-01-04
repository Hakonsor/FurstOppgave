/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furst;

import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Comparator som tar utgangspunt i norsk alfabet
 * Dette var en norsk comparator, men får å sammeligne lettere blir det brukt 
 * Comparator.naturalOrder() istedenfor
 * @author hakon
 */
public class SBinTreKomparator implements Comparator<SBinTre> {

    private String rekkefølge = "<\0<0<1<2<3<4<5<6<7<8<9"
            + "<A,a<B,b<C,c<D,d<E,e<F,f<G,g<H,h<I,i<J,j"
            + "<K,k<L,l<M,m<N,n<O,o<P,p<Q,q<R,r<S,s<T,t"
            + "<U,u<V,v<W,w<X,x<Y,y<Z,z<Æ,æ<Ø,ø<Å=AA,å=aa;AA,aa";

    private RuleBasedCollator kollator;

    public SBinTreKomparator() {
        try {
            kollator = new RuleBasedCollator(rekkefølge);
        } catch (ParseException pe) {
            System.out.println("Feil i komperator");
            System.exit(0);
        }
    }

    @Override
    public int compare(SBinTre s1, SBinTre s2) {
        Comparator c = Comparator.naturalOrder();
        Iterator iter;
        Iterator iter2;
        if(s1 == null || s2 == null){
            return 0;
        }
        iter = s1.iterator();
        iter2 = s2.iterator();
        int result = 0;
        while (iter.hasNext() && iter2.hasNext()) {
            String en = (String)iter.next();
            String to = (String)iter2.next();
            result = c.compare(en, to);

            if(result != 0) {
                return result;
                
            }
        }
        return result;
    }

}
