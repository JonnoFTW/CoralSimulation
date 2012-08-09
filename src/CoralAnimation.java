import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;


/**
 * @author Jonathan Mackenzie
 * 
 * The animation canvas for the simulation.
 *
 */
public class CoralAnimation extends Canvas {
    /**
     * 
     */
    private static final long serialVersionUID = 946419120335674464L;
    // A map from colony number to a species, and a set of integers indicating the positions of cells belonging to that 
    // colony
    private HashMap<Integer, Colony> colonies = new HashMap<Integer, Colony>();
    private int colCount = 0;
    final private String LINE_SEP = System.getProperty("line.separator");
    private Simulation sim;
    private int tick, rows, columns;
    private BufferStrategy bf;
    private Random rng;
    private StringBuilder notes; // Comments about the simulation for a  tick
    
    public CoralAnimation(final Simulation s) {
        rng = new Random();
        notes = new StringBuilder();
        bf = getBufferStrategy();
        this.setBackground(Color.white);
        this.sim = s;
        reset();
        addMouseMotionListener(new MouseMotionAdapter() {
            
            @Override
            public void mouseMoved(MouseEvent e) {
                // Report information about the cell we are mousing over to 
                // this sidebar
                Pair<Integer, Integer> p = getXY(e);
                Pair<Integer, Species> cell = getSpecies(p);
                if(cell == null)
                    sim.sp.setXY(p,null,0);
                else
                    sim.sp.setXY(p,cell.y,cell.x);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                // Clicking a cell will flip it,
                // Killing the cell or bringing it back to life
                Pair<Integer,Integer> p = getXY(e);
                Pair<Integer, Species> cell = getSpecies(p);
                if(cell != null) { 
                    notes.append("Removing cell at: x="+p.x+" y="+p.y+", "+cell.y).append(LINE_SEP);
                    removeCell(p.x, p.y, cell.x);
                } else {
                    notes.append("Marking cell at: x="+p.x+" y="+p.y+" as "+sim.sp.getSelectedSpecies()+ ", colony no. "+colCount).append(LINE_SEP);
                    addCell(p.x,p.y, sim.sp.getSelectedSpecies(),++colCount) ;
                    
                }
                repaint();
            }
        });
    }
    private Pair<Integer,Species> getSpecies(Pair<Integer,Integer> p) {
        for (Entry<Integer, Colony> c : colonies.entrySet()) {
            if(c.getValue().cells.contains(p.x*columns + p.y)) 
                return new Pair<Integer,Species>(c.getKey(),c.getValue().species);
        }
        return null;
    }
    private Pair<Integer,Integer> getXY(MouseEvent e) {
        int x = (int) (e.getX()/(getWidth()/sim.sp.getRows()));
        int y = (int) (e.getY()/(getHeight()/sim.sp.getColumns()));
        return new Pair<Integer,Integer>(x,y);
    }
    private void addCell(int x, int y, Species s, int colonyNo) {
        if(!colonies.containsKey(colonyNo)) {
            colonies.put(colonyNo, new Colony(s));
        }
        colonies.get(colonyNo).cells.add(x*columns +y);
    }
    /**
     * Merge 2 colony counts, as selected when performing a step
     */
    private void mergeColonies(int colony1, int colony2) {
        notes.append(String.format("Merging colony %d with %d\n",colony1,colony2));
        if(colonies.containsKey(colony1) && colonies.containsKey(colony2)) {
            colonies.get(colony1).cells.addAll(colonies.get(colony2).cells);
            colonies.remove(colony2);
        }
    }
    
    private void killColony(int colonyNumber) {
        notes.append("Colony ").append(colonyNumber).append(" died").append(LINE_SEP);
        colonies.remove(colonyNumber);
    }
    
    private void removeCell(int x, int y, int colonyNumber) {
        // Remove a cell at coordinates if any
        System.out.printf("Removing cell at %d,%d\n",x,y);
        colonies.get(colonyNumber).cells.remove(x*columns + y);
        if(colonies.get(colonyNumber).cells.isEmpty()) {
            colonies.remove(colonyNumber);
            notes.append("Colony ").append(colonyNumber).append(" has died out due to shrinkage");
        }
    }
    public void paint(Graphics g) {
        rows = sim.sp.getRows();
        columns = sim.sp.getColumns();
        createBufferStrategy(1);
        
        bf = getBufferStrategy();
        g = null;
        try{
            g = bf.getDrawGraphics();
            render(g);
        } finally {
            g.dispose();
        }
        bf.show();
        Toolkit.getDefaultToolkit().sync();
        
    }
    
    /**
     * Render the cells in the frame with a neat border around each cell
     * @param g
     */
    private void render(Graphics g) {
        for (Entry<Integer,Colony> colony : colonies.entrySet()) {
            for (Pair<Integer, Integer> i : colony.getValue().getPositions(rows, columns)) {
                int x = i.x;
                int y = i.y;
                g.setColor(colony.getValue().species.getColor());
                g.fillRect(x*(getWidth()/columns), y*(getHeight()/rows),getWidth()/columns ,getHeight()/rows);
                // Draw the colony number
                if(sim.sp.chckbxShowColNo.isSelected()) {
                    g.setColor(Color.black);
                    g.drawString(colony.getKey()+"",x*(getWidth()/columns), y*(getHeight()/rows)+g.getFontMetrics().getHeight());
                }
            }
        }
        // Draw the grid
        for(int x = 0; x < columns; x++ ) {
            for (int y = 0; y < rows; y++) {
                g.setColor(Color.GRAY);
                g.drawRect(x*(getWidth()/columns), y*(getHeight()/rows),getWidth()/columns ,getHeight()/rows);
            }
        } 
        
        
    }
    /**
     * Iterate the simulation through 1 year
     */
    public int tick() {
        tick++;
        // merge any colonies who have neighbouring cells and of the same species
        ArrayList<Pair<Integer,Integer>> toMerge = new ArrayList<Pair<Integer,Integer>>();
        for (Entry<Integer, Colony> colony : colonies.entrySet()) {
            for (Integer cell : colony.getValue().cells) {
                Pair<Integer,Integer> xy = toXY(cell);
                for(int i : getNeighbours(xy.x, xy.y)) {
                    for (Entry<Integer, Colony> otherColony : colonies.entrySet()) {
                        if(otherColony.getKey() != colony.getKey()
                            && otherColony.getValue().species ==  colony.getValue().species
                            && otherColony.getValue().cells.contains(i)) {
                                toMerge.add(new Pair<Integer,Integer>(otherColony.getKey() , colony.getKey()));
                                break;
                        }
                    }
                }
            }
        }
        System.out.println("Merging: "+toMerge);
        for (Pair<Integer, Integer> pair : toMerge) {
            mergeColonies(pair.x, pair.y);
        }
        for (Entry<Integer, Colony> colony : colonies.entrySet()) {
            boolean competing = false;
            // Detect which colonies are competing
            for (Integer cell : colony.getValue().cells) {
                Pair<Integer,Integer> xy = toXY(cell);
                for(int i : getNeighbours(xy.x, xy.y)) {
                    for (Entry<Integer, Colony> otherColony : colonies.entrySet()) {
                        if(otherColony.getKey() != colony.getKey()
                                && otherColony.getValue().cells.contains(i)
                                && otherColony.getValue().species != colony.getValue().species) {
                            competing = true;
                            break;
                        }
                    }
                    if(competing) break;
                }
            }
            
            Float newColonySize = 0f;
            Species s = colony.getValue().species;
            Integer colonySize = colony.getValue().cells.size();
            Integer colonyNumber = colony.getKey();
            System.out.println("Colony "+colonyNumber+" is competing: "+competing);
            float timeScaling = 12/12;
            
            float mortality,shrinkage,growth;
            
            if(competing) {
                mortality = s.getDieC(colonySize);
                shrinkage = s.getShrinkC(colonySize);
                growth    = s.getShrinkC(colonySize);
            } else {
                mortality = s.getDie(colonySize);
                shrinkage = s.getShrink(colonySize);
                growth    = s.getShrink(colonySize);
            }
            if(mortality > rng.nextFloat()) {
                System.out.println(s +" died");
                killColony(colonyNumber);
                continue;
            } else {
                // Adjust the size of the colony
             //    System.out.println("Colony "+colonyNumber+" is at size "+colonySize);
                 newColonySize = colonySize * ( (s.getGrowC(colonySize)*timeScaling - s.getShrinkC(colonySize)*timeScaling));
             //    System.out.println("New size: "+newColonySize);
                 
                // Grow out the colony until it reaches the new size
            }
          //  while(colonies.get(colonyNumber).cells.size() < newColonySize) {
                // Add more cells
                // Use a recursive grow after we've found 1 cell of the current colony,
                // repeat growing until new size is met
                // or if there is net shrinkage, kill cells
        //    }
        } 
        return tick;
    }
    
    public int getTick() {
        return tick;
    }

    /**
     * @return
     */
    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
    public void reset() {
        tick = 0;
        colonies = new HashMap<Integer, Colony>();
        repaint();
        colCount = 0;
    }
    
    private int[] getNeighbours(int x, int y) {
        int ip = (((x+1)%columns)+columns)%columns;
        int im = (((x-1)%columns)+columns)%columns;
        int jp = (((y+1)%rows)+rows)%rows;
        int jm = (((y-1)%rows)+rows)%rows;
        
        int[] neighbours = {
            im*columns + y,
            ip*columns + y,
            x*columns + jp,
            x*columns + jm,
            ip*columns +jp,
            ip*columns +jm,
            im*columns +jp,
            im*columns +jm
        };
        return neighbours;
    }
    private Pair<Integer,Integer> toXY(Integer z) {
        return new Pair<Integer, Integer>(z/rows, z%columns);
    }
    public String getReport() {
        StringBuilder s = new StringBuilder(128);
        
        s.append("Tick:").append(tick).append(LINE_SEP);
        // Loop through each colony, report size, species
        for (Entry<Integer, Colony> p: colonies.entrySet()) {
            s.append("Colony id:").append(p.getKey()).append(", ").append(p.getValue().species).append(", size: ").append(p.getValue().cells.size()).append(LINE_SEP);
        }
        s.append(notes);
        notes = new StringBuilder();
        s.append("--------------------------------------------------------------------").append(LINE_SEP);
        return s.toString();
    }
}
