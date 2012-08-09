
public class Pair<T,V> {
    public final T x; 
    public final V y; 
    public Pair(T x, V y) { 
      this.x = x; 
      this.y = y; 
    }
    public String toString() {
        return String.format("(%s,%s)", x,y);
    }
    
}
