import java.awt.Color;
import java.io.Serializable;
/**
 * @author Jonathan
 * 
 */
public class Species implements Serializable{
    private Color color;
    private int die, grow, shrink;
    private String name;

    public Species(Color c, int die, int grow, int shrink, String name) {
        setColor(c);
        this.die = die;
        this.grow = grow;
        this.shrink = shrink;
        this.setName(name);
    }

    public String toString() {
        return name;
    }
    public int getDie() {
        return die;
    }

    public void setDie(int die) {
        this.die = die;
    }

    public int getGrow() {
        return grow;
    }

    public void setGrow(int grow) {
        this.grow = grow;
    }

    public int getShrink() {
        return shrink;
    }

    public void setShrink(int shrink) {
        this.shrink = shrink;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color
     *            the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
