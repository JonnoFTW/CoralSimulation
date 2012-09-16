import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Jonathan Mackenzie
 * 
 *         A species of coral. Each species tracks 3 rates: - Growth - Mortality
 *         - Shrinkage Each rate is tracked in competing and non-competing
 *         situations. Each rate is a function of 2 variables with respsect to
 *         the colonies size. a+bx where is x is colony size.
 */

public class Species implements Serializable {

    private static final long serialVersionUID = -6074195010790622156L;
    private Color color;
    private double grow, shrink, growC, shrinkC, growSD, shrinkSD, growCSD,
            shrinkCSD, growTS, shrinkTS;
    private String name;
    private int recruits, growMonths, shrinkMonths;
    // Size classes map a lower and upper bound to a mortality probability, and
    // growth/shrink probability
    private ArrayList<SizeClass> sizeClasses;

    /**
     * @param c
     *            the color of this species
     * @param grow
     *            the growth rate of this species
     * @param shrink
     *            the shrinkage rate of this species
     * @param growC
     *            the growth rate of this species when in competition
     * @param shrinkC
     *            the shrinkage rate of this species when in competition
     * @param name
     *            the name of this species
     * @param growSD
     *            the size dependent growth rate
     * @param shrinkSD
     *            the size dependent shrinkage rate
     * @param growCSD
     *            the size dependent growth rate when competing
     * @param shrinkCSD
     *            the size dependent shrinkage rate when competing
     * @param shrinkTS
     *            the shrinkage time scaling
     * @param growTS
     *            the growth timescaling
     * @param recruits
     *            the number of recruits for this species each iteration
     */
    public Species(Color c, double grow, double shrink, double growC,
            double shrinkC, String name, double growSD, double shrinkSD,
            double growCSD, double shrinkCSD, ArrayList<SizeClass> sizeClasses,
            int growTS, int shrinkTS, int recruits) {
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
        this.growTS = growTS / 12d;
        this.growMonths = growTS;
        // Set the months for exporting
        this.shrinkMonths = shrinkTS;
        this.shrinkTS = shrinkTS / 12d;
        this.recruits = recruits;
    }

    public String toString() {
        return name;
    }

    /**
     * @return
     */
    public int getRecruits() {
        return recruits;
    }

    /**
     * @param c
     */
    private void setColor(Color c) {
        this.color = c;
    }

    /**
     * @return
     */
    public String getReport(int maxNameSize) {
        return String.format("%" + (maxNameSize)
                + "s | %f + %f | %f + %f | %f + %f | %f + %f | %d %n", name,
                grow, growSD, growC, growCSD, shrink, shrinkSD, shrinkC,
                shrinkCSD, recruits);
    }

    /**
     * @return
     */
    public String sizeClassReport() {
        StringBuilder sb = new StringBuilder();
        for (SizeClass sc : sizeClasses) {
            sb.append(sc.toString()).append("\n");
        }
        return sb.toString();
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
    public double getDie(int colonySize) {
        for (SizeClass c : sizeClasses) {
            if (c.in(colonySize))
                return c.getMortality();
        }
        return 0;
    }

    /**
     * @return the growth rate of this species
     */
    public double getGrow(int colonySize) {
        return (this.grow + this.growSD * colonySize) * growTS;
    }

    /**
     * @return the shrinkage rate of this species
     */
    public double getShrink(int colonySize) {
        return (this.shrink + this.shrinkSD * colonySize) * shrinkTS;
    }

    /**
     * @return the growth rate of this species when in competition
     */
    public double getGrowC(int colonySize) {
        return (this.growC + this.growCSD * colonySize) * growTS;
    }

    /**
     * @param colonySize
     *            the size of the colony
     * @return the shrinkage rate of this species when in competition
     */
    public double getShrinkC(int colonySize) {
        return (this.shrinkC + this.shrinkCSD * colonySize * colonySize)
                * shrinkTS;
    }

    /**
     * @return the name of this species
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param colonySize
     *            the size of the colony
     * @return the probability that a colony of this species of the specified
     *         size will grow
     */
    public double getGrowShrinkP(Integer colonySize) {
        for (SizeClass c : sizeClasses) {
            if (c.in(colonySize))
                return c.getGrowShrinkP();
        }
        return 0;
    }

    /**
     * 
     * @param colonySize
     *            the size of the colony
     * @return the probability that a colony of this species of the specified
     *         size will grow when in competition
     */
    public double getGrowShrinkPC(Integer colonySize) {
        for (SizeClass c : sizeClasses) {
            if (c.in(colonySize))
                return c.getGrowShrinkPC();
        }
        return 0;
    }

    /**
     * @return
     */
    public Object[] getArray() {
        // ,"Constant","Size Dependent","Constant","Size Dependent","Time Scale","Constant",
        // "Size Dependent","Constant","Size Dependent","Time Scale","Recruits","Size Classes"}
        return new Object[] { getName(), grow, growSD, growC, growCSD,
                growMonths, shrink, shrinkSD, shrinkC, shrinkCSD, shrinkMonths,
                recruits, sizeClasses,color };
    }
}
