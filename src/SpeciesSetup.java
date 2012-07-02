import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class SpeciesSetup extends JPanel implements TableModelListener {
    /**
     * 
     */
    private static final long serialVersionUID = -599805443131295797L;
    /**
     * forms for setting up species, should load from file on creation
     */
    DefaultTableModel model = new DefaultTableModel();
    private ArrayList<Species> species = new ArrayList<Species>();
    private JTable tbl;
    private SidePanel sp;
    private Random rng;
    public SpeciesSetup(SidePanel sp) {
        this.sp = sp;
        rng = new Random();
        // Load in the default species
      //  importSpecies("default.dat");
        setLayout(new BorderLayout());
        model.addTableModelListener(this);
        
        
        String[] columnTitles = {"Name","Grow Rate","Growth Rate (Competing)","Size Dependent Growth",
                "Size Dependent Growth (Competing)","Mortality","Mortality (Competing)","Shrinkage","Shrinkage (Competing)","Color"}; 
        for (String string : columnTitles) {
            model.addColumn(string);
        }
        addSpecies(new Species(Color.green, 1, 1, 1, "A hya."));
        addEmptyRow();
        updateSpeciesSelection();
        tbl = new JTable(model);
        TableColumn col = tbl.getColumnModel().getColumn(9);
        String[] values = new String[]{"item1", "item2", "item3"};
        col.setCellRenderer(new ComboBoxRenderer(values));
        
        tbl.setFillsViewportHeight(true);
        add(new JScrollPane(tbl),BorderLayout.CENTER);
        add(new JLabel("Warning: modifying the species in any way will clear the simulation"),BorderLayout.SOUTH);
        
        
    }
    public class ComboBoxRenderer extends JComboBox implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 7725363559647542811L;

        public ComboBoxRenderer(String[] items) {
            super(items);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setSelectedItem(value);
            return this;
        }
    }
    public class MyComboBoxEditor extends DefaultCellEditor {
        /**
         * 
         */
        private static final long serialVersionUID = 3338214212914073018L;

        public MyComboBoxEditor(String[] items) {
            super(new JComboBox(items));
        }
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
        System.out.println(species);
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
        sp.setSpeciesSelections(species);      
    }
    public void addEmptyRow() {
        model.insertRow(model.getRowCount(),new Object[]  {""});
    }
    @Override
    public void tableChanged(TableModelEvent e) {
        // Save the species to the file, update the list on the sidepanel
        System.out.println("Table updated");
        
        // If a row is filled out, add it to the list of species
       // species.clear();
        int names = 0;
        species.clear();
        for (int i = 0; i < model.getRowCount(); i++) {
            String name = (String) model.getValueAt(i, 0);
            if(!name.isEmpty()) {
                species.add(new Species(Color.getHSBColor(rng.nextFloat(),(rng.nextInt(2000) + 1000) / 10000f,0.9f), 1, 1, 1,name));
                System.out.println("added name");
                names++;
            }
          //  int grow = (Integer) model.getValueAt(i, 1);
          //  int die = (Integer) model.getValueAt(i, 2);
           // int shrink = (Integer) model.getValueAt(i, 3);
          //  Color c = new Color((Integer) model.getValueAt(i, 4));
           // species.add(new Species(c, die, grow, shrink, name));
            
        }
        if(names == model.getRowCount())
            addEmptyRow();
        updateSpeciesSelection();
    }
}

