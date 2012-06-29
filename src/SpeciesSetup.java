import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class SpeciesSetup extends JPanel {
    /**
     * forms for setting up species, should load from file on creation
     */
    
    private ArrayList<Species> species = new ArrayList<Species>();
    public SpeciesSetup() {
        // Load in the default species
        ;
        String[] columnTitles = {"Name","Grow","Die","Shrink","Color"}; 
        Object[][] rows = {{}};
        JTable tbl = new JTable(rows, columnTitles);
        JScrollPane scrollPane = new JScrollPane(tbl);
        tbl.setFillsViewportHeight(true);
        add(scrollPane);
        
    }
    public void exportSpecies() {
        
    }
    public void importSpecies() {
        // Use the default species file
    }
    public void importSpecies(String fileName) {
        // Import species from file, throw an error if it's improperly formatted
    }
    public void addSpeciesRow() {
        add(new JFormattedTextField());
    }
}

