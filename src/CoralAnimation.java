import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;


public class CoralAnimation extends Canvas {
    HashMap<Integer,HashMap<Integer,Species>> colonies;
    private Simulation s;
    private int tick, rows, columns;
    private BufferStrategy bf;
    
    public CoralAnimation(final Simulation s) {
        bf = getBufferStrategy();
        colonies = new HashMap<Integer,HashMap<Integer,Species>>();
        this.setBackground(Color.white);
        this.s = s;
        tick = 0;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                // Clicking a cell will flip it,
                // Killing the cell or bringing it back to life
                int x = (int) (e.getX()/(getWidth()/s.sp.getRows()));
                int y = (int) (e.getY()/(getHeight()/s.sp.getColumns()));
                if(!colonies.containsKey(x)){
                    colonies.put(x, new HashMap<Integer, Species>());
                }
                
                colonies.get(x).put(y, (Species) s.sp.speciesSelected.getSelectedItem()) ;
                System.out.println("Marking cell at: x="+x+" y="+y+" as "+s.sp.speciesSelected.getSelectedItem());
                repaint();
            }
        });
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
        for(int x = 0; x < columns; x++ ) {
            for (int y = 0; y < rows; y++) {
                g.setColor(Color.GRAY);
                g.drawRect(x*(getWidth()/columns), y*(getHeight()/rows),getWidth()/columns ,getHeight()/rows);
            }
        }
        for (Entry<Integer, HashMap<Integer, Species>> row : colonies.entrySet()) {
            for (Map.Entry<Integer, Species> cell: row.getValue().entrySet()) {
                // Perform calculation here
                int x = row.getKey();
                int y = cell.getKey();
                g.setColor(cell.getValue().getColor());
                g.fillRect(x*(getWidth()/columns), y*(getHeight()/rows),getWidth()/columns ,getHeight()/rows);
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
            }
        }
        colonies = newColonies;
        s.sp.tick();
        repaint();
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
}
