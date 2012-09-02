
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
    public boolean equals(Pair<T,V> p) {
        return (p.x == x && p.y == y) || (p.x == y && p.y == x);
    }
}
