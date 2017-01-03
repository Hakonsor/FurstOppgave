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
import java.util.List;

/**
 *
 * @author hakon
 */
public class Komperator implements Comparator<List<String>> {

    private String rekkefølge = "<\0<0<1<2<3<4<5<6<7<8<9"
            + "<A,a<B,b<C,c<D,d<E,e<F,f<G,g<H,h<I,i<J,j"
            + "<K,k<L,l<M,m<N,n<O,o<P,p<Q,q<R,r<S,s<T,t"
            + "<U,u<V,v<W,w<X,x<Y,y<Z,z<Æ,æ<Ø,ø<Å=AA,å=aa;AA,aa";

    private RuleBasedCollator kollator;

    public Komperator() {
        try {
            kollator = new RuleBasedCollator(rekkefølge);
        } catch (ParseException pe) {
            System.out.println("Feil i komperator");
            System.exit(0);
        }
    }

    @Override
    public int compare(List<String> l1, List<String> l2) {
        int antall_settninger;
        if( l1.size() > l2.size()) antall_settninger = l2.size();
        else antall_settninger = l1.size();
        
        for (int i = 0; i < antall_settninger; i++) {
        int d = kollator.compare(l2.get(i), l1.get(i));
                if (d != 0) {
                    return d;
                }
            
        }
        return 0;
    }

}
