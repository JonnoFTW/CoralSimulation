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
        JMenuItem manualItem = new JMenuItem("User Manual");
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
        m.add(manualItem);
        m.add(aboutItem);
        return m;
    }
    private JMenu makeFileMenu() {
        // TODO Auto-generated method stub
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
    
    private void saveDialogue() {
        // TODO Auto-generated method stub
        // Save the image?
        // Save species ?
    }
    private void createDir() {
        // Create a file in users home dir,
        // ~/CoralSim
        //      /logs
        //      /species
        //      /images
        String name = System.getProperty("user.home");
        for (String i : new String[]{"logs","species","images"}) {
            if(new File(name+"/CoralSim/"+i).mkdirs())  
                System.out.println("Created "+name+"/CoralSim/"+i);
        }
        
    }
    private String getDataDir(String sub) {
        String sep = System.getProperty("file.separator");
        return System.getProperty("user.home")+sep+"CoralSim"+sep+sub+sep;
    }
    public String getLogDir() {
        return getDataDir("logs");
    }
    public String getSpeciesDir() {
        return getDataDir("species");
    }
    public String getImageDir() {
        return getDataDir("images");
    }

}
