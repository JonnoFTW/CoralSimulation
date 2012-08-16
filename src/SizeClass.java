
/**
 * @author Jonathan
 *
 */
public class SizeClass {

    private double mortality, growshrinkP, growshrinkPC;
    
    /**
     * @param min
     * @param max
     * @param mortality the mortality as a percentage between 0 and 1
     * @param growShrinkP
     * @param growShrinkPC
     */
    public SizeClass(int min, int max, double mortality, double growShrinkP, double growShrinkPC) {
        this.min = min;
        this.max = max;
        this.mortality = mortality;
        this.growshrinkP = growShrinkP;
        this.growshrinkPC = growShrinkPC;
    }
    /**
     * @param colSize
     * @return
     */
    public boolean in(int colSize) {
        if(min <= colSize && max > colSize)
            return true;
        else
            return false;
    }
    /**
     * @return the probability of a species in this size class dieing
     */
    public double getMortality(){
        return mortality;
    }
    /**
     * @return the probability of a species in this size class growing when not in competition
     */
    public double getGrowShrinkP() {
        return growshrinkP;
    }
    /**
     * @return the probability of a species in this size class growing when in competition
     */
    public double getGrowShrinkPC() {
        return growshrinkPC;
    }
    public String toString() {
        return String.format("[%d,%d: %f, %f, %f]",min,max, mortality, growshrinkP, growshrinkPC);
    }
    
    private int max, min;
    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }
    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }
}
