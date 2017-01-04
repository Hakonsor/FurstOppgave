/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furst;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 *
 * @author hakon
 * 
 * Denne Klassen er hentet ifra http://www.cs.hioa.no/~ulfu/appolonius/kildekode/kildekode.html
 * Jeg prøvde først å bruke kun collection, men TreSet og TreMap tillater ikke dublikater 
 */
public class SBinTre<T> implements Iterable<T>
{
  private static final class Node<T> // en indre nodeklasse
  {
    private T verdi;                 // nodens verdi
    private Node<T> venstre, høyre;  // venstre og høyre barn

    private Node(T verdi, Node<T> v, Node<T> h)  // konstruktør
    {
      this.verdi = verdi;
      venstre = v; høyre = h;
    }

    private Node(T verdi)  // konstruktør
    {
      this(verdi, null, null);
    }

    public String toString(){ return "" + verdi;}

  } // class Node

  private Node<T> rot;                       // peker til rotnoden
  private int antall;                        // antall noder
  private final Comparator<? super T> comp;  // komparator

  public SBinTre(Comparator<? super T> c)    // konstruktør
  {
    rot = null;
    antall = 0;
    comp = c;
  }

  public int antall()        // antall verdier i treet
  {
    return antall;
  }

  public boolean tom()       // er treet tomt?
  {
    return antall == 0;
  }

  public void leggInn(T verdi)    // skal ligge i class SBinTre
  {
    Node<T> p = rot, q = null;               // p starter i roten
    int cmp = 0;                             // hjelpevariabel

    while (p != null)       // fortsetter til p er ute av treet
    {
      q = p;                                 // q forelder til p
      cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
      p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
    }

    p = new Node<>(verdi);                   // oppretter en ny node

    if (q == null) rot = p;                  // rotnoden
    else if (cmp < 0) q.venstre = p;         // til venstre for q
    else q.høyre = p;                        // til høyre for q

    antall++;                                // én verdi mer i treet
  }

  public boolean inneholder(T verdi)     // skal ligge i klassen SBinTre
  {
    Node<T> p = rot;                            // starter i roten
    while (p != null)                           // sjekker p
    {
      int cmp = comp.compare(verdi,p.verdi);    // sammenligner
      if (cmp < 0) p = p.venstre;               // går til venstre
      else if (cmp > 0) p = p.høyre;            // går til høyre
      else return true;                         // cmp == 0, funnet
    }
    return false;                               // ikke funnet
  }



  public boolean fjern(T verdi)  // hører til klassen SBinTre
  {
    Node<T> p = rot, q = null;   // q skal være forelder til p

    while (p != null)            // leter etter verdi
    {
      int cmp = comp.compare(verdi,p.verdi);      // sammenligner
      if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
      else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
      else break;    // den søkte verdien ligger i p
    }
    if (p == null) return false;   // finner ikke verdi

    if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
    {
      Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
      if (p == rot) rot = b;
      else if (p == q.venstre) q.venstre = b;
      else q.høyre = b;
    }
    else  // Tilfelle 3)
    {
      Node<T> s = p, r = p.høyre;   // finner neste i inorden
      while (r.venstre != null)
      {
        s = r;    // s er forelder til r
        r = r.venstre;
      }

      p.verdi = r.verdi;   // kopierer verdien i r til p

      if (s != p) s.venstre = r.høyre;
      else s.høyre = r.høyre;
    }

    antall--;   // det er nå én node mindre i treet
    return true;
  }
    
   private class InordenIterator implements Iterator<T>
  {
    private Stakk<Node<T>> s = new TabellStakk<>();  // for traversering
    private Node<T> p = null;                        // nodepeker

    private Node<T> først(Node<T> q)   // en hjelpemetode
    {
      while (q.venstre != null)        // starter i q
      {
        s.leggInn(q);                  // legger q på stakken
        q = q.venstre;                 // går videre mot venstre
      }
      return q;                        // q er lengst ned til venstre
    }

    public InordenIterator()  // konstruktør
    {
      if (rot == null) return;         // treet er tomt
      s.leggInn(null);                 // legger null på bunnen av stakken
      p = først(rot);                  // bruker hjelpemetoden
    }

    public T next()
    {
      T verdi = p.verdi;               // tar vare på verdien i noden p

      if (p.høyre != null) p = først(p.høyre);  // p har høyre subtre
      else p = s.taUt();                        // p har ikke høyre subtre

      return verdi;                    // returnerer verdien
    }

    public boolean hasNext()
    {
      return p != null;
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }

  }  // InordenIterator

  public Iterator<T> iterator()
  {
    return new InordenIterator();
  }
}
