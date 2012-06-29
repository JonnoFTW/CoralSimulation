import java.awt.Color;


public class Species {
    private Color color;
    private int die, grow, shrink;
    private String name;
    
    public Species(Color c, int die, int grow, int shrink, String name) {
        color = c;
        this.die = die;
        this.grow = grow;
        this.shrink = shrink;
        this.name = name;
    }
}
