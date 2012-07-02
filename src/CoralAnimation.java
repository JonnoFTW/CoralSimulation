import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


public class CoralAnimation extends Canvas {
    /**
     * 
     */
    private static final long serialVersionUID = 946419120335674464L;
    HashMap<Integer,HashMap<Integer,Species>> colonies;
    private Simulation s;
    private int tick, rows, columns;
    private BufferStrategy bf;
    private Random rng;
    public CoralAnimation(final Simulation s) {
        rng = new Random();
        bf = getBufferStrategy();
        colonies = new HashMap<Integer,HashMap<Integer,Species>>();
        this.setBackground(Color.white);
        this.s = s;
        tick = 0;
        addMouseMotionListener(new MouseMotionListener() {
            
            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub
                Pair p = getXY(e);
                s.sp.setXY(p,getSpecies(p));
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
                Pair p = getXY(e);
                if(colonies.containsKey(p.x)) {
                    if(colonies.get(p.x).containsKey(p.y)) {
                        System.out.println("Removing cell at: x="+p.x+" y="+p.y+", "+colonies.get(p.x).get(p.y));
                        removeCell(p.x, p.y);
                        return;
                    }
                }
                addCell(p.x,p.y,colonies, (Species) s.sp.getSelectedSpecies()) ;
                System.out.println("Marking cell at: x="+p.x+" y="+p.y+" as "+s.sp.getSelectedSpecies());
                repaint();
            }
        });
    }
    private Species getSpecies(Pair p) {
        if(!colonies.containsKey(p.x))
            return null;
        else 
            return colonies.get(p.x).get(p.y);
    }
    private Pair getXY(MouseEvent e) {
        int x = (int) (e.getX()/(getWidth()/s.sp.getRows()));
        int y = (int) (e.getY()/(getHeight()/s.sp.getColumns()));
        return new Pair(x,y);
    }
    private void addCell(int x, int y, HashMap<Integer,HashMap<Integer,Species>> col, Species s) {
        if(!col.containsKey(x)){
            col.put(x, new HashMap<Integer, Species>());
        }
        col.get(x).put(y, s) ;
    }
    private void removeCell(int x, int y) {
        // Remove a cell at coordinates if any.
        colonies.get(x).remove(y);
        if(colonies.get(x).isEmpty())
            colonies.remove(x);
        repaint();
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
        for (Entry<Integer, HashMap<Integer, Species>> row : colonies.entrySet()) {
            for (Map.Entry<Integer, Species> cell: row.getValue().entrySet()) {
                // Perform calculation here
                int x = row.getKey();
                int y = cell.getKey();
                g.setColor(cell.getValue().getColor());
                g.fillRect(x*(getWidth()/columns), y*(getHeight()/rows),getWidth()/columns ,getHeight()/rows);
            }
        }
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
    public void tick() {
        tick++;
        
        HashMap<Integer,HashMap<Integer,Species>> newColonies = new HashMap<Integer,HashMap<Integer, Species>>();
        for (Entry<Integer, HashMap<Integer, Species>> row : colonies.entrySet()) {
            for (Map.Entry<Integer, Species> cell: row.getValue().entrySet()) {
                // Perform calculation here
                int y = row.getKey();
                int x = cell.getKey();
             //   System.out.println("Checking "+x+" "+y);
                Species s = cell.getValue();
                int ip = (((x+1)%columns)+columns)%columns;
                int im = (((x-1)%columns)+columns)%columns;
                int jp = (((y+1)%rows)+rows)%rows;
                int jm = (((y-1)%rows)+rows)%rows;
                
                Pair[] neighbours = {
                    new Pair(im,y),
                    new Pair(ip,y),
                    new Pair(x,jp),
                    new Pair(x,jm),
                    new Pair(ip,jp),
                    new Pair(ip,jm),
                    new Pair(im,jp),
                    new Pair(im,jm)
                };
                for (Pair pair : neighbours) {
                    if(newColonies.containsKey(pair.x) && newColonies.get(pair.x).get(pair.y) != s) {
                        // We are competing!
                        if(rng.nextInt(100) > s.getGrow()) {
                            
                        }
                    }
                    addCell(pair.y,pair.x,newColonies, s) ;
                }
                
                
            }
        }
        colonies = newColonies;
        s.sp.tick();
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
        colonies.clear();
        repaint();
    }
}
