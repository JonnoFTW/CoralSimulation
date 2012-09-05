
/**
 * @author Jonathan
 * Hold a pair of items x and y of any class
 * @param <T>
 * @param <V>
 */
public class Pair<T,V> {
    public final T x; 
    public final V y; 
    /**
     * @param x the x item
     * @param y the y item
     */
    public Pair(T x, V y) { 
      this.x = x; 
      this.y = y; 
    }
    public String toString() {
        return String.format("(%s,%s)", x,y);
    }
}
