import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;


public class Simulation extends JPanel {
    
    private SpeciesSetup specSetup;
    private JLabel tick;
    private int iteration;
    private JPanel coralsPanel;
    
    public Simulation(JFrame window) {
       
        window.setJMenuBar(makeMenuBar());
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quitDialogue();
            }
        });
        specSetup = new SpeciesSetup();
        JPanel simPanel = new JPanel();
        simPanel.setLayout(new BorderLayout());
        simPanel.add(makeSidePanel(), BorderLayout.EAST);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(500,500));
        tabbedPane.addTab("Simulation", simPanel);
        tabbedPane.addTab("SpeciesSetup", specSetup);

        window.add(tabbedPane);
        
    }
    private JMenuBar makeMenuBar() {
        // TODO Auto-generated method stub
        JMenuBar mb = new JMenuBar();
        mb.add(makeFileMenu());
        mb.add(makeHelpMenu());
        return mb;
    }
    private JMenu makeHelpMenu() {
        // TODO Auto-generated method stub
        JMenu m = new JMenu("Help");
        return m;
    }
    private JMenu makeFileMenu() {
        // TODO Auto-generated method stub
        JMenu m = new JMenu("File");
        return m;
    }
    /**
     * Shows the quitting Dialogue, presents the user
     * with the option to quit, cancel or save their current
     * game
     */
    private void quitDialogue() {
        int s = JOptionPane.showConfirmDialog(null,
                "Would you like to save your game before quitting?", "Quit",
                JOptionPane.YES_NO_CANCEL_OPTION);
        switch (s) {
        case JOptionPane.YES_OPTION:
            saveDialogue();
        case JOptionPane.NO_OPTION:
            System.exit(0);
            break;
        default:
            break;
        }
    }
    
    private void saveDialogue() {
        // TODO Auto-generated method stub
        // Save the image?
        // Save species ?
    }
    private void setTick(int t) {
        iteration = t;
        tick.setText("Iteration: "+t);
    }
    
    private JPanel makeSidePanel() {
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JToggleButton startStop = new JToggleButton("Start");
        JButton exportImage = new JButton("Save Image");
        tick = new JLabel("Iteration: ");
        
        
        p.add(startStop);
        p.add(exportImage);
        p.add(tick);
        return p;
    }
}
