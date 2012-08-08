import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        addMouseMotionListener(new MouseMotionListener() {
            
            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub
                Pair<Integer, Integer> p = getXY(e);
                Pair<Integer, Species> cell = getSpecies(p);
                if(cell != null)
                    sim.sp.setXY(p,cell.y,cell.x);
            }
            
            @Override
            public void mouseDragged(MouseEvent arg0) {
                // TODO Auto-generated method stub
                
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
        colonies.get(colony1).cells.addAll(colonies.get(colony2).cells);
        colonies.remove(colony2);
    }
    
    private void killColony(int colonyNumber) {
        notes.append("Colony ").append(colonyNumber).append(" died").append(LINE_SEP);
        colonies.remove(colonyNumber);
    }
    
    private void removeCell(int x, int y, int colonyNumber) {
        // Remove a cell at coordinates if any
        System.out.printf("Removing cell at %d,$d\n",x,y);
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
        // This loop should probably be in the previous loop
        for (Entry<Integer, Colony> colony : colonies.entrySet()) {
            boolean competing = true;
            Float newColonySize = 0f;
            Species s = colony.getValue().species;
            Integer colonySize = colony.getValue().cells.size();
            Integer colonyNumber = colony.getKey();
            float timeScaling = 12/12;
            // Find if this colony is competing
            // find some way to do this efficiently
            if(competing) {
                if(s.getDieC(colonySize)*(timeScaling) < rng.nextFloat()) {
                    killColony(colonyNumber);
                    continue;
                } else {
                    // Adjust the size of the colony
                     newColonySize = colonySize * ( 1+ (s.getGrowC(colonySize)*timeScaling - s.getShrinkC(colonySize)*timeScaling));
                    // Grow out the colony until it reaches the new size
                }
            } else {
                
            }
            while(colonies.get(colonyNumber).cells.size() < newColonySize) {
                // Add more cells
                // Use a recursive grow after we've found 1 cell of the current colony,
                // repeat growing until new size is met
                // or if there is net shrinkage, kill cells
            }
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
    
    private int[][] getNeighbours(int x, int y) {
        int ip = (((x+1)%columns)+columns)%columns;
        int im = (((x-1)%columns)+columns)%columns;
        int jp = (((y+1)%rows)+rows)%rows;
        int jm = (((y-1)%rows)+rows)%rows;
        
        int[][] neighbours = {
            {im,y},
            {ip,y},
            {x,jp},
            {x,jm},
            {ip,jp},
            {ip,jm},
            {im,jp},
            {im,jm}
        };
        return neighbours;
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
