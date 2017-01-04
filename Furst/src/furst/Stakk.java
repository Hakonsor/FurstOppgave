package furst;

/**
 *
 * @author hakon
 * 
 * Denne Klassen er hentet ifra http://www.cs.hioa.no/~ulfu/appolonius/kildekode/kildekode.html
 * Jeg prøvde først å bruke kun collection, men TreSet og TreMap tillater ikke dublikater 
 */
public interface Stakk<T>          // eng: Stack
{
  public void leggInn(T t);        // eng: push
  public T kikk();                 // eng: peek
  public T taUt();                 // eng: pop
  public int antall();             // eng: size
  public boolean tom();            // eng: isEmpty
  public void nullstill();         // eng: clear

} // interface Stakk