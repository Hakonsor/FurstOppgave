/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furst;

import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.Comparator;
import java.util.Iterator;


/**
 * Comparator som tar utgangspunt i norsk alfabet
 * På grunn av sammenligning mellom trær og lister bruker jeg Comparator.naturalOrder()
 *
 * @author hakon
 */
public class Komparator2 implements Comparator<String> {

    private String rekkefølge = "<\0<0<1<2<3<4<5<6<7<8<9"
            + "<A,a<B,b<C,c<D,d<E,e<F,f<G,g<H,h<I,i<J,j"
            + "<K,k<L,l<M,m<N,n<O,o<P,p<Q,q<R,r<S,s<T,t"
            + "<U,u<V,v<W,w<X,x<Y,y<Z,z<Æ,æ<Ø,ø<Å=AA,å=aa;AA,aa";

    private RuleBasedCollator kollator;

    public Komparator2() {
        try {
            kollator = new RuleBasedCollator(rekkefølge);
        } catch (ParseException pe) {
            System.out.println("Feil i komperator");
            System.exit(0);
        }
    }

    @Override
    public int compare(String s1, String s2) {
        Comparator com = Comparator.naturalOrder();
        return com.compare(s1, s2);
    }

}

