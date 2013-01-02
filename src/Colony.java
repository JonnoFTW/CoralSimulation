import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Jonathan
 *
 */
public class Colony {
    private HashSet<Integer> cells = new HashSet<Integer>();
    final private Species species;
    private  double remainingGrowth, remainingShrinkage;
    private double radiusDelta;
    /**
     * Resets remaining growth and shrinkage to 0
     */
    public void resetRemaining() {
        remainingGrowth = remainingShrinkage = 0;
    }
   
    /**
     * @return the remainingShrinkage
     */
    public double getRemainingShrinkage() {
        return remainingShrinkage;
    }
    /**
     * @param remainingShrinkage the remainingShrinkage to set
     */
    public void setRemainingShrinkage(double remainingShrinkage) {
        this.remainingShrinkage = remainingShrinkage;
    }
    /**
     * @param s the species of this colony
     */
    public Colony(Species s) {
        this.species = s;
    }
    /**
     * Returns an arraylist of x,y locations used by this colony
     * @param rows the number of rows in the simulation
     * @param columns the number of columns in the simulation
     * @return an arraylist of pairs of x,y locations used by thi colony
     */
    public ArrayList<Pair<Integer,Integer>> getPositions(int rows, int columns) {
        ArrayList<Pair<Integer,Integer>> positions = new ArrayList<Pair<Integer,Integer>>(cells.size());
        for (Integer i : cells) {
            positions.add(new Pair<Integer,Integer>(i/rows,i%columns));
        }
        return positions;
    }
    /**
     * @return the hashset of cells used by this colony
     */
    public HashSet<Integer> getCells() {
        return cells;
    }
    /**
     * @return the species of this colony
     */
    public Species getSpecies() {
        return species;
    }
    /**
     * Get the remaining growth for this colony
     * @return the remaining growth for this colony, may be positive or negative (when there is leftover shrinkage)
     */
    public double getRemainingGrowth() {
        return remainingGrowth;
    }
    /**
     * Set the remaining growth for this colony should fall in the range (-1,1)
     * @param gr the remaining growth for this colony in the next iteration
     */
    public void setRemainingGrowth(double gr) {
        remainingGrowth = gr;
    }
    /**
     * 
     * @return the most recent change in this colony's radius in cm
     */
    public double getRadiusDelta() {
        // TODO Auto-generated method stub
        return radiusDelta;
    }
    public void setRadiusDelta(double d) {
        radiusDelta = d;
    }
}