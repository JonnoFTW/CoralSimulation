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
    public Colony(Species s) {
        this.species = s;
    }
    public ArrayList<Pair<Integer,Integer>> getPositions(int rows, int columns) {
        ArrayList<Pair<Integer,Integer>> positions = new ArrayList<Pair<Integer,Integer>>(cells.size());
        for (Integer i : cells) {
            positions.add(new Pair<Integer,Integer>(i/rows,i%columns));
        }
        return positions;
    }
    public HashSet<Integer> getCells() {
        return cells;
    }
    public Species getSpecies() {
        return species;
    }
    public double getRemainingGrowth() {
        return remainingGrowth;
    }
    public void setRemainingGrowth(double gr) {
        remainingGrowth = gr;
    }
}