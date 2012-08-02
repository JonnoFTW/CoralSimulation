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
import java.util.Map.Entry;
import java.util.Random;


public class CoralAnimation extends Canvas {
    /**
     * 
     */
    private static final long serialVersionUID = 946419120335674464L;
    private Species[][] cells;
    private int[][] colonies;
    private HashMap<Integer, Pair<Species,Integer>> colonyCount = new HashMap<Integer, Pair<Species,Integer>>();
    private int colCount = 1;
    final private String LINE_SEP = System.getProperty("line.separator");
    private Simulation s;
    private int tick, rows, columns;
    private BufferStrategy bf;
    private Random rng;
    
    public CoralAnimation(final Simulation s) {
        rng = new Random();
        bf = getBufferStrategy();
        this.setBackground(Color.white);
        this.s = s;
        reset();
        addMouseMotionListener(new MouseMotionListener() {
            
            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub
                Pair<Integer, Integer> p = getXY(e);
                try {
                s.sp.setXY(p,getSpecies(p),colonies[p.x][p.y]);
                } catch( ArrayIndexOutOfBoundsException err){
                    
                }
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
                if(getSpecies(p) != null) { 
                        System.out.println("Removing cell at: x="+p.x+" y="+p.y+", "+cells[p.x][p.y]);
                        removeCell(p.x, p.y);
                        repaint();
                        return;
                }
                addCell(p.x,p.y,cells, (Species) s.sp.getSelectedSpecies(),colCount++) ;
                System.out.println("Marking cell at: x="+p.x+" y="+p.y+" as "+s.sp.getSelectedSpecies()+ ", colony no. "+colCount);
                repaint();
            }
        });
    }
    private Species getSpecies(Pair<Integer,Integer> p) {
        try {
            return cells[p.x][p.y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    private Pair<Integer,Integer> getXY(MouseEvent e) {
        int x = (int) (e.getX()/(getWidth()/s.sp.getRows()));
        int y = (int) (e.getY()/(getHeight()/s.sp.getColumns()));
        return new Pair<Integer,Integer>(x,y);
    }
    private void addCell(int x, int y, Species[][] col, Species s, int colonyNo) {
        try{
            col[x][y] = s;
            colonies[x][y] = colonyNo;
        } catch (ArrayIndexOutOfBoundsException e) {
            // TODO: handle exception
        }
    }
    /**
     * Merge 2 colony counts, as selected when performing a step
     */
    private void mergeColonies(int colony1, int colony2,int x, int y) {
    //    System.out.printf("Merging %d with %d\n",colony1,colony2);
        mergeColonies_(colony2, x, y);
    }
    
    private void mergeColonies_(int newColony,int x, int y) {
        if(colonies[x][y] != newColony) return;
        colonies[x][y] = newColony;
        for (int[] p : getNeighbours(x, y)) {
            if(colonies[p[0]][p[1]] != newColony && cells[p[0]][p[1]] == cells[x][x]) {
                mergeColonies_(newColony, p[0], p[1]);
            } 
        }
    }
    
    private void removeCell(int x, int y) {
        // Remove a cell at coordinates if any.
        cells[x][y] = null;
        colonies[x][y] = 0;
    }
    public void paint(Graphics g) {
        rows = s.sp.getRows();
        columns = s.sp.getColumns();
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
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                if(cells[x][y] != null) {
                    g.setColor(cells[x][y].getColor());
                    g.fillRect(x*(getWidth()/columns), y*(getHeight()/rows),getWidth()/columns ,getHeight()/rows);
                }
            }
        }
        for(int x = 0; x < columns; x++ ) {
            for (int y = 0; y < rows; y++) {
                g.setColor(Color.GRAY);
                g.drawRect(x*(getWidth()/columns), y*(getHeight()/rows),getWidth()/columns ,getHeight()/rows);
            }
        } 
        if(s.sp.chckbxShowColNo.isSelected()) {
            for(int x = 0; x < columns; x++ ) {
                for (int y = 0; y < rows; y++) {
                    g.setColor(Color.black);
                    g.drawString(colonies[x][y]+"",x*(getWidth()/columns), y*(getHeight()/rows)+g.getFontMetrics().getHeight());
                }
            } 
        }
        
    }
    /**
     * Iterate the simulation through 1 year
     */
    public int tick() {
        tick++;
        
        colonyCount.clear();
        
        Species[][] newColonies = makeCells();
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                // Perform calculation here
             //   System.out.println("Checking "+x+" "+y);
                Species s = cells[x][y];
                int colony = colonies[x][y];
                if(s== null) continue;
                if(s.getDie().x <= rng.nextFloat()) {
                    addCell(x, y, newColonies, s,colony);
                }
                int[][] neighbours = getNeighbours(x, y);
                for (int[] pair : neighbours) {
                    // Homogeneous neighbours
                    if(newColonies[pair[0]][pair[1]] == s){
                        int neighbourColony = colonies[pair[0]][pair[1]];
                        if(neighbourColony != 0 && neighbourColony != colony)
                            // Merge the 2
                            mergeColonies(neighbourColony, colony,x,y);
                        continue;
                    }
                    if(newColonies[pair[0]][pair[1]] != s) {
                        // We are competing!
                        if(rng.nextInt(100) > s.getGrow().x) {
                            
                        }
                    }
                    addCell(pair[0],pair[1],newColonies, s,colony);
                }
                
                
            }
        }
        cells = newColonies;
        // This loop should probably be in the previous loop
        for (int x = 0; x < newColonies.length; x++) {
            for (int y = 0; y < newColonies[x].length; y++) {
                if(colonies[x][y] == 0) continue;
                if(colonyCount.get(colonies[x][y]) == null) {
                        colonyCount.put(colonies[x][y], new Pair<Species,Integer>(cells[x][y],1) );
                } else {
                    Integer c = colonyCount.get(colonies[x][y]).y;
                    colonyCount.put(colonies[x][y], new Pair<Species,Integer>(cells[x][y],c+1));
                }
            }
        }
        return tick;
    }
    public int getTick() {
        return tick;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
    public void reset() {
        tick = 0;
        cells  = makeCells();
        colonies = new int[s.sp.getRows()][s.sp.getColumns()];
        repaint();
    }
    private Species[][] makeCells() {
        return new Species[s.sp.getRows()][s.sp.getColumns()];
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
        for (Entry<Integer, Pair<Species, Integer>> p: colonyCount.entrySet()) {
            s.append("Colony id:").append(p.getKey()).append(", ").append(p.getValue().x).append(", size: ").append(p.getValue().y).append(LINE_SEP);
        }
        s.append("--------------------------------------------------------------------").append(LINE_SEP);
        return s.toString();
    }
}
