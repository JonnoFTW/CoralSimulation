import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.Random;


public class CoralAnimation extends Canvas {
    /**
     * 
     */
    private static final long serialVersionUID = 946419120335674464L;
    Species[][] colonies;
    final private String LINE_SEP = System.getProperty("line.separator");
    private Simulation s;
    private int tick, rows, columns;
    private BufferStrategy bf;
    private Random rng;
    private StringBuilder report;
    public CoralAnimation(final Simulation s) {
        report = new StringBuilder();
        rng = new Random();
        bf = getBufferStrategy();
        this.setBackground(Color.white);
        this.s = s;
        reset();
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
                if(getSpecies(p) != null) { 
                        System.out.println("Removing cell at: x="+p.x+" y="+p.y+", "+colonies[p.x][p.y]);
                        removeCell(p.x, p.y);
                        repaint();
                        return;
                }
                addCell(p.x,p.y,colonies, (Species) s.sp.getSelectedSpecies()) ;
                System.out.println("Marking cell at: x="+p.x+" y="+p.y+" as "+s.sp.getSelectedSpecies());
                repaint();
            }
        });
    }
    private Species getSpecies(Pair p) {
        try {
            return colonies[p.x][p.y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    private Pair getXY(MouseEvent e) {
        int x = (int) (e.getX()/(getWidth()/s.sp.getRows()));
        int y = (int) (e.getY()/(getHeight()/s.sp.getColumns()));
        return new Pair(x,y);
    }
    private void addCell(int x, int y, Species[][] col, Species s) {
        try{
            col[x][y] = s ;
        } catch (ArrayIndexOutOfBoundsException e) {
            // TODO: handle exception
        }
    }
    private void removeCell(int x, int y) {
        // Remove a cell at coordinates if any.
        colonies[x][y] = null;
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
        for (int x = 0; x < colonies.length; x++) {
            for (int y = 0; y < colonies[x].length; y++) {
                if(colonies[x][y] != null) {
                    g.setColor(colonies[x][y].getColor());
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
        
    }
    /**
     * Iterate the simulation through 1 year
     */
    public int tick() {
        tick++;
        
        Species[][] newColonies = new Species[colonies.length][colonies[0].length];
        for (int x = 0; x < colonies.length; x++) {
            for (int y = 0; y < colonies.length; y++) {
                // Perform calculation here
             //   System.out.println("Checking "+x+" "+y);
                Species s = colonies[x][y];
                if(s== null) continue;
                if(s.getDie() <= rng.nextFloat()) {
                    addCell(y, x, newColonies, s);
                }
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
                for (int[] pair : neighbours) {
                    if(newColonies[pair[0]][pair[1]] == s)
                        continue;
                    if(newColonies[pair[0]][pair[1]] != s) {
                        // We are competing!
                        if(rng.nextInt(100) > s.getGrow()) {
                            
                        }
                    }
                    addCell(pair[0],pair[1],newColonies, s) ;
                }
                
                
            }
        }
        colonies = newColonies;
      //  s.sp.tick();
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
        colonies = new Species[s.sp.getColumns()][s.sp.getRows()];
        repaint();
    }
    
    public String getReport() {
        StringBuilder s = new StringBuilder(128);
        
        s.append("Tick:").append(tick).append(LINE_SEP);
        // Loop through each colony, report size, species
        s.append("Colony").append(LINE_SEP);
        s.append("--------------------------------------------------------------------").append(LINE_SEP);
        return s.toString();
    }
}
