import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * @author Jonathan
 * The main simulation window 
 */
public class Simulation extends JPanel {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3953774251275226988L;
    public SpeciesSetup specSetup;
    public SidePanel sp;
    public CoralAnimation coralSim;
    
    public Simulation(JFrame window) {
        createDir();
        window.setJMenuBar(makeMenuBar());
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
             //   quitDialogue();
            }
        });
        
        JPanel simPanel = new JPanel();
        
        simPanel.setLayout(new BorderLayout());
        sp = new SidePanel(this);
        simPanel.add(sp, BorderLayout.EAST);
        coralSim = new CoralAnimation(this);
        simPanel.add(coralSim, BorderLayout.CENTER);
        specSetup = new SpeciesSetup(sp);
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(1102,702));
        tabbedPane.addTab("Simulation", simPanel);
        tabbedPane.addTab("Species Setup", specSetup);
        final LogPanel logPanel = new LogPanel(getLogDir());
        tabbedPane.addTab("Logs", logPanel);

        tabbedPane.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent event) {
                // Refresh the log list when we select that panel
                if(tabbedPane.getSelectedIndex() == 2) {
                        logPanel.refresh();
                }
            }
        });
        window.add(tabbedPane);
        
    }
    /**
     * @return the menu bar
     */
    private JMenuBar makeMenuBar() {
        // Make the menu bar
        JMenuBar mb = new JMenuBar();
        mb.add(makeFileMenu());
        mb.add(makeHelpMenu());
        return mb;
    }
    /**
     * Generate a help menu
     * @return the JMenu of a help menu
     */
    private JMenu makeHelpMenu() {
        // Make a help menu
        JMenu m = new JMenu("Help");
    //    JMenuItem manualItem = new JMenuItem("User Manual"); // I doubt you need a user manual to operate this application
        JMenuItem aboutItem = new JMenuItem("About");
        ActionListener menuClicked = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the help dialogue
                if (e.getActionCommand().equals("About")) {
                    JOptionPane
                            .showMessageDialog(
                                    null,
                                    "<html><h2>Jonathan Mackenzie</h2><h3>Coral Simulation</h3><h4>&copy;2012, <a href='mailto:jonmac1@gmail.com'>jonmac1@gmail.com</a></h4>"
                                            + "<p style=\"font-size:12pt; text-align:justify\"> "
                                            + "This program is distributed in the hope that it will <br/>"
                                            + "be useful, but WITHOUT ANY WARRANTY;<br/>"
                                            + "without even the implied warranty of MERCHANTABILITY<br/>"
                                            + "or FITNESS FOR A PARTICULAR PURPOSE.</p> </html>",
                                    "About Coral Simulation",
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null);
                }
                
            }
        };
        aboutItem.addActionListener(menuClicked);
   //     m.add(manualItem);
        m.add(aboutItem);
        return m;
    }
    /**
     * Generate a JMenu to with functions to save/load species and quit the application
     * @return the JMenu file menu, 
     */
    private JMenu makeFileMenu() {
        JMenu m = new JMenu("File");
        JMenuItem saveSpecies = new JMenuItem("Save Species");
        JMenuItem loadSpecies = new JMenuItem("Load Species");
        JMenuItem quit = new JMenuItem("Quit");
        m.add(saveSpecies);
        m.add(loadSpecies);
        m.addSeparator();
        m.add(quit);
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
    
    /**
     * 
     */
    private void saveDialogue() {
        // TODO Auto-generated method stub
        // Save the image?
        // Save species ?
    }
    /**
     * Set up the directories that the application will use,
     * Organises the folders in the user's home directory as such:
     *  ~/CoralSim
     *         /logs
     *         /species
     *         /images
     */
    private void createDir() {
        String name = System.getProperty("user.home");
        for (String i : new String[]{"logs","species","images"}) {
            if(new File(name+"/CoralSim/"+i).mkdirs())  
                System.out.println("Created "+name+"/CoralSim/"+i);
        }
        
    }
    /**
     * @param sub
     * @return
     */
    private String getDataDir(String sub) {
        String sep = System.getProperty("file.separator");
        return System.getProperty("user.home")+sep+"CoralSim"+sep+sub+sep;
    }
    /**
     * @return
     */
    public String getLogDir() {
        return getDataDir("logs");
    }
    /**
     * @return
     */
    public String getSpeciesDir() {
        return getDataDir("species");
    }
    /**
     * @return
     */
    public String getImageDir() {
        return getDataDir("images");
    }

}
