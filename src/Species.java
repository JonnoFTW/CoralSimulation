import java.awt.Color;
import java.io.Serializable;
/**
 * @author Jonathan
 * 
 */
public class Species implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -6074195010790622156L;
    private Color color;
    private float die, grow, shrink;
    private String name;

    public Species(Color c, float die, float grow, float shrink, String name) {
        setColor(c);
        this.die = die;
        this.grow = grow;
        this.shrink = shrink;
        this.setName(name);
    }

    public String toString() {
        return name;
    }
    public float getDie() {
        return die;
    }

    public void setDie(float die) {
        this.die = die;
    }

    public float getGrow() {
        return grow;
    }

    public void setGrow(float grow) {
        this.grow = grow;
    }

    public float getShrink() {
        return shrink;
    }

    public void setShrink(float shrink) {
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
    public String getReport() {
        return String.format("%5s | %6f | %10f | %9f | %13f | %9f | %13f%n", name,grow,grow,shrink,shrink,die,die);
    }
}
