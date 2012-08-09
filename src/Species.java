import java.awt.Color;
import java.io.Serializable;
/**
 * @author Jonathan Mackenzie
 * 
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
    private float die, grow, shrink, dieC, growC, shrinkC, dieSD, growSD, shrinkSD, dieCSD, growCSD, shrinkCSD;
    private String name;


    /**
     * @param c the color of this species
     * @param die the mortality rate of this species
     * @param grow the growth rate of this species
     * @param shrink the shrinkage rate of this species
     * @param dieC the mortality rate of this species when in competition
     * @param growC the growth rate of this species when in competition
     * @param shrinkC the shrinkage rate of this species when in competition
     * @param name the name of this species
     * @param dieSD the size dependent mortality rate of this species
     * @param growSD the size dependent growth rate
     * @param shrinkSD the size dependent shrinkage rate
     * @param dieCSD the size dependent mortality rate when competinng
     * @param growCSD the size dependent growth rate when competing
     * @param shrinkCSD the size dependent shrinkage rate when competing
     */
    public Species(Color c, float die, float grow, float shrink, float dieC,float growC, float shrinkC, String name,
            float dieSD, float growSD, float shrinkSD, float dieCSD,float growCSD, float shrinkCSD ) {
        setColor(c);
        this.die = die/100;
        this.dieC = dieC/100;
        this.grow = grow;
        this.growC = growC;
        this.shrinkC = shrinkC;
        this.shrink = shrink;
        this.dieSD = dieSD;
        this.dieCSD = dieCSD;
        this.growSD = growSD;
        this.growCSD = growCSD;
        this.shrinkCSD = shrinkCSD;
        this.shrinkSD = shrinkSD;
        this.name = name;
    }

    public String toString() {
        return name;
    }
 
    private void setColor(Color c) {
        this.color = c;
    }
    /**
     * @return
     */
    public String getReport(int maxNameSize) {
        return String.format("%"+(maxNameSize)+"s | %f + %f | %f + %f | %f + %f | %f + %f | %f + %f | %f + %f %n", name,grow,growSD,growC,growCSD,shrink,shrinkSD,shrinkC,shrinkCSD,die,dieSD,dieC,dieCSD);
    }

    /**
     * @return the color of this species to use in the image representation
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param colonySize 
     * @return the mortality rate of this species
     */
    public Float getDie(int colonySize) {
        return new Float(this.die+this.dieSD*colonySize);
    }
    /**
     * @return the growth rate of this species
     */
    public Float getGrow(int colonySize) {
        return new Float(this.grow+this.growSD*colonySize);
    }

    /**
     * @return the shrinkage rate of this species
     */
    public Float getShrink(int colonySize) {
        return new Float(this.shrink+this.shrinkSD*colonySize);
    }

    /**
     * @return the the mortality rate of this species when in competition
     */
    public Float getDieC(int colonySize) {
        return new Float(this.dieC+this.dieCSD*colonySize);
    }

    /**
     * @return the growth rate of this species when in competition
     */
    public Float getGrowC(int colonySize) {
        return new Float(this.growC+this.growCSD*colonySize);
    }

    /**
     * @return the shrinkage rate of this species when in competition
     */
    public Float getShrinkC(int colonySize) {
        return new Float(this.shrinkC + this.shrinkCSD * colonySize*colonySize);
    }

    /**
     * @return the name of this species
     */
    public String getName() {
        return name;
    }
}
