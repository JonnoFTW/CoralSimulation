import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
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
    private float grow, shrink, growC, shrinkC,  growSD, shrinkSD, growCSD, shrinkCSD;
    private String name;
    
    // Size classes map a lower and upper bound to a mortality probability, and growth/shrink probability
    private ArrayList<SizeClass> sizeClasses;


    /**
     * @param c the color of this species
     * @param grow the growth rate of this species
     * @param shrink the shrinkage rate of this species
     * @param growC the growth rate of this species when in competition
     * @param shrinkC the shrinkage rate of this species when in competition
     * @param name the name of this species
     * @param growSD the size dependent growth rate
     * @param shrinkSD the size dependent shrinkage rate
     * @param growCSD the size dependent growth rate when competing
     * @param shrinkCSD the size dependent shrinkage rate when competing
     */
    public Species(Color c, float grow, float shrink, float growC, float shrinkC, String name,
            float growSD, float shrinkSD, float growCSD, float shrinkCSD,  ArrayList<SizeClass> sizeClasses  ) {
        setColor(c);
        this.grow = grow;
        this.growC = growC;
        this.shrinkC = shrinkC;
        this.shrink = shrink;
        this.growSD = growSD;
        this.growCSD = growCSD;
        this.shrinkCSD = shrinkCSD;
        this.shrinkSD = shrinkSD;
        this.name = name;
        this.sizeClasses = sizeClasses;
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
        return String.format("%"+(maxNameSize)+"s | %f + %f | %f + %f | %f + %f | %f + %f %n", name,grow,growSD,growC,growCSD,shrink,shrinkSD,shrinkC,shrinkCSD);
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
        for (SizeClass c : sizeClasses) {
            if(c.in(colonySize))
                return c.getMortality();
        }
        return null;
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
     * @return the growth rate of this species when in competition
     */
    public Float getGrowC(int colonySize) {
        return new Float(this.growC+this.growCSD*colonySize);
    }

    /**
     * @param colonySize the size of the colony
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

    /**
     * 
     * @param colonySize the size of the colony
     * @return the probability that a colony of this species of the specified size
     *  will grow 
     */
    public float getGrowShrinkP(Integer colonySize) {
        for (SizeClass c : sizeClasses) {
            if(c.in(colonySize))
                return c.getGrowShrinkP();
        }
        return 0;
    }
    /**
     * 
     * @param colonySize the size of the colony
     * @return the probability that a colony of this species of the specified size
     *  will grow when in competition
     */
    public float getGrowShrinkPC(Integer colonySize) {
        for (SizeClass c : sizeClasses) {
            if(c.in(colonySize))
                return c.getGrowShrinkPC();
        }
        return 0;
    }
}
