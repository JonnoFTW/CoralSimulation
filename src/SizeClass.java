
/**
 * @author Jonathan
 *
 */
public class SizeClass {

    private float mortality, growshrinkP, growshrinkPC;
    
    /**
     * @param min
     * @param max
     * @param mort
     * @param gsp
     * @param gspc
     */
    public SizeClass(int min, int max, float mort, float gsp, float gspc) {
        this.min = min;
        this.max = max;
        this.mortality = mort;
        this.growshrinkP = gsp;
        this.growshrinkPC = gspc;
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
    public float getMortality(){
        return mortality;
    }
    /**
     * @return the probability of a species in this size class growing when not in competition
     */
    public float getGrowShrinkP() {
        return growshrinkP;
    }
    /**
     * @return the probability of a species in this size class growing when in competition
     */
    public float getGrowShrinkPC() {
        return growshrinkPC;
    }
    public String toString() {
        return String.format("(%d,%d)",min,max);
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
