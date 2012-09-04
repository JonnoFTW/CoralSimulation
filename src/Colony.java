import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Jonathan
 *
 */
public class Colony {
    private HashSet<Integer> cells = new HashSet<Integer>();
    private Species species;
    private  double remainingGrowth;
    /**
     * @param s
     */
    public Colony(Species s) {
        this.species = s;
    }
    /**
     * @param rows
     * @param columns
     * @return
     */
    public ArrayList<Pair<Integer,Integer>> getPositions(int rows, int columns) {
        ArrayList<Pair<Integer,Integer>> positions = new ArrayList<Pair<Integer,Integer>>(cells.size());
        for (Integer i : cells) {
            positions.add(new Pair<Integer,Integer>(i/rows,i%columns));
        }
        return positions;
    }
    /**
     * @return
     */
    public HashSet<Integer> getCells() {
        return cells;
    }
    /**
     * @return
     */
    public Species getSpecies() {
        return species;
    }
    /**
     * @return
     */
    public double getRemainingGrowth() {
        return remainingGrowth;
    }
    /**
     * @param gr
     */
    public void setRemainingGrowth(double gr) {
        remainingGrowth = gr;
    }
}