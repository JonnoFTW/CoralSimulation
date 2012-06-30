import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class SpeciesSetup extends JPanel implements TableModelListener {
    /**
     * forms for setting up species, should load from file on creation
     */
    DefaultTableModel model = new DefaultTableModel();
    private ArrayList<Species> species = new ArrayList<Species>();
    private JTable tbl;
    private SidePanel sp;
    public SpeciesSetup(SidePanel sp) {
        this.sp = sp;
        // Load in the default species
      //  importSpecies("default.dat");
        setLayout(new BorderLayout());
        model.addTableModelListener(this);
        String[] columnTitles = {"Name","Grow","Die","Shrink","Color"}; 
        for (String string : columnTitles) {
            model.addColumn(string);
        }
        addSpecies(new Species(Color.green, 1, 1, 1, "A hya."));
        addEmptyRow();
        updateSpeciesSelection();
        tbl = new JTable(model);
        
        
        tbl.setFillsViewportHeight(true);
        add(new JScrollPane(tbl),BorderLayout.CENTER);
        add(new JLabel("Warning: modifying the species in any way will clear the simulation"),BorderLayout.SOUTH);
        
        
    }
    
    public void exportSpecies(String fileName) {
        // Serialize the species list to a file
        try {
            ObjectOutputStream objOut= new ObjectOutputStream(new FileOutputStream(new File(fileName)));
            for (Species s : species) {
                objOut.writeObject(s);
            }
            objOut.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
    public void importSpecies() {
        // Use the default species file
        importSpecies("default.dat");
    }
    public void importSpecies(String fileName) {
        try {
                ObjectInputStream objIn = new  ObjectInputStream(new FileInputStream(fileName));
                species = (ArrayList<Species>) objIn.readObject();
                objIn.close();
            }
         catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          //  JOptionPane.showMessageDialog(this, "Error loading species file!");
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(this, "IOException!!");
        }
        catch(ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Species Class not found!");
        }
        
    }
    public void addSpecies(Species s) {
        model.addRow(new Object[]{s.getName(),1,1,1,1});
        species.add(s);
    }
    public ArrayList<Species> getSpecies()  {
        return species;
    }
    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < model.getRowCount(); i++) {
            names.add((String) model.getValueAt(i, 0));
        }
        
        return names;
    }
    private void updateSpeciesSelection() {
        sp.speciesSelected.removeAllItems();
        for (Species species : getSpecies()) {
            System.out.println("Adding "+species);
            sp.speciesSelected.addItem(species);
            
        }
        sp.speciesSelected.validate();
        
    }
    public void addEmptyRow() {
        model.insertRow(model.getRowCount(),new Object[]  {"","","","",""});
    }
    @Override
    public void tableChanged(TableModelEvent e) {
        // Save the species to the file, update the list on the sidepanel
        System.out.println("Table updated");
        updateSpeciesSelection();
        // If a row is filled out, add it to the list of species
        species.clear();
        for (int i = 0; i < model.getRowCount(); i++) {
            String name;
            for (int j = 0; j < model.getColumnCount(); j++) {
                
            }
            
        }
    }
}

