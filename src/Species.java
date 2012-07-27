import java.awt.Color;
import java.io.Serializable;
/**
 * @author Jonathan
 * A species of coral. Each species tracks 3 rates:
 *  - Growth
 *  - Mortality
 *  - Shrinkage
 * Each rate is tracked in competing and non-competing situations. Each rate is a function of 2 variables
 * with respsect to the colonies size. a+bx where is x is colony size.
 */


public class Species implements Serializable{

    private static final long serialVersionUID = -6074195010790622156L;
    private Color color;
    private float die, grow, shrink, dieC, growC, shrinkC;
    private String name;

    /**
     * @param c
     * @param die
     * @param grow
     * @param shrink
     * @param dieC
     * @param growC
     * @param shrinkC
     * @param name
     */
    public Species(Color c, float die, float grow, float shrink, float dieC,float growC, float shrinkC, String name) {
        setColor(c);
        this.die = die;
        this.dieC = dieC;
        this.grow = grow;
        this.growC = growC;
        this.shrinkC = shrinkC;
        this.shrink = shrink;
        this.setName(name);
    }

    public String toString() {
        return name;
    }
 
    /**
     * @return
     */
    public String getReport() {
        return String.format("%5s | %6f | %10f | %9f | %13f | %9f | %13f%n", name,grow,growC,shrink,shrinkC,die,dieC);
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the die
     */
    public float getDie() {
        return die;
    }

    /**
     * @param die the die to set
     */
    public void setDie(float die) {
        this.die = die;
    }

    /**
     * @return the grow
     */
    public float getGrow() {
        return grow;
    }

    /**
     * @param grow the grow to set
     */
    public void setGrow(float grow) {
        this.grow = grow;
    }

    /**
     * @return the shrink
     */
    public float getShrink() {
        return shrink;
    }

    /**
     * @param shrink the shrink to set
     */
    public void setShrink(float shrink) {
        this.shrink = shrink;
    }

    /**
     * @return the dieC
     */
    public float getDieC() {
        return dieC;
    }

    /**
     * @param dieC the dieC to set
     */
    public void setDieC(float dieC) {
        this.dieC = dieC;
    }

    /**
     * @return the growC
     */
    public float getGrowC() {
        return growC;
    }

    /**
     * @param growC the growC to set
     */
    public void setGrowC(float growC) {
        this.growC = growC;
    }

    /**
     * @return the shrinkC
     */
    public float getShrinkC() {
        return shrinkC;
    }

    /**
     * @param shrinkC the shrinkC to set
     */
    public void setShrinkC(float shrinkC) {
        this.shrinkC = shrinkC;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
