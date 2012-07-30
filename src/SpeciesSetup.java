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
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import TableHeader.ColumnGroup;
import TableHeader.GroupableTableHeader;

public class SpeciesSetup extends JPanel implements TableModelListener {
    /**
     * 
     */
    private static final long serialVersionUID = -599805443131295797L;
    /**
     * forms for setting up species, should load from file on creation
     */
    DefaultTableModel model = new SpeciesTableModel();
    private ArrayList<Species> speciesList = new ArrayList<Species>();
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
        addEmptyRow();
        tbl = new JTable(model) {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        
        
        TableColumnModel cm = tbl.getColumnModel();
        ColumnGroup growth = new ColumnGroup("Growth");
        growth.add(cm.getColumn(1));
        growth.add(cm.getColumn(2));
        ColumnGroup growthC = new ColumnGroup("Growth (Competing)");
        growthC.add(cm.getColumn(3));
        growthC.add(cm.getColumn(4));
        ColumnGroup mort = new ColumnGroup("Mortality");
        mort.add(cm.getColumn(5));
        mort.add(cm.getColumn(6));
        ColumnGroup mortC = new ColumnGroup("Mortality (Competing)");
        mortC.add(cm.getColumn(7));
        mortC.add(cm.getColumn(8));
        ColumnGroup shrink = new ColumnGroup("Shrinkage");
        shrink.add(cm.getColumn(9));
        shrink.add(cm.getColumn(10));
        ColumnGroup  shrinkC = new ColumnGroup("Shrinkage(Competing)");
        shrinkC.add(cm.getColumn(11));
        shrinkC.add(cm.getColumn(12));
        GroupableTableHeader header = (GroupableTableHeader)tbl.getTableHeader();
        for (ColumnGroup cg : new ColumnGroup[] {growth,growthC,mort,mortC,shrink,shrinkC}) {
            header.addColumnGroup(cg);
        }
        
        tbl.setFillsViewportHeight(true);
        add(new JScrollPane(tbl),BorderLayout.CENTER);
        add(new JLabel("Warning: modifying the species in any way will clear the simulation"),BorderLayout.SOUTH);      
    }

    public void exportSpecies(String fileName) {
        // Serialize the species list to a file
        try {
            ObjectOutputStream objOut= new ObjectOutputStream(new FileOutputStream(new File(fileName)));
            for (Species s : speciesList) {
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
                speciesList = (ArrayList<Species>) objIn.readObject();
                objIn.close();
                model.getDataVector().clear();
                
            }
         catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          //  JOptionPane.showMessageDialog(this, "Error loading species file!");
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(this, "IOException: "+e);
        }
        catch(ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Species Class not found!");
        }
        
    }
    public void addSpecies(Species s) {
        model.addRow(new Object[]{s.getName(),s.getGrow(),s.getGrowC()});
        speciesList.add(s);
        System.out.println(speciesList);
    }
    public ArrayList<Species> getSpecies()  {
        return speciesList;
    }
    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < model.getRowCount(); i++) {
            names.add((String) model.getValueAt(i, 0));
        }
        return names;
    }
    private void updateSpeciesSelection() {
        sp.setSpeciesSelections(speciesList);      
    }
    public void addEmptyRow() {
        model.addRow(new Object[]  {""});
    }
    @Override
    public void tableChanged(TableModelEvent e) {
        // Save the species to the file, update the list on the sidepanel        
        // If a row is filled out, add it to the list of species

        int names = 0;
        
        speciesList.clear();
        for (int i = 0; i < model.getRowCount(); i++) {
            int filledIn =0;
            for (int j = 0; j < model.getColumnCount(); j++) {
                // Check that each column of the row is filled out
                if(model.getValueAt(i, j) != null) {
                    filledIn++;     
                    //System.out.println(""+i+","+j+" is filled with: "+model.getValueAt(i, j));
                }
            }
            System.out.println("User filled in "+filledIn+" columns");
            if(filledIn == model.getColumnCount()) {
                names++;
                String name     = (String) model.getValueAt(i, 0);
                float grow      = (Float) model.getValueAt(i, 1);
                float growSD    = (Float) model.getValueAt(i, 2);                
                float growC     = (Float) model.getValueAt(i, 3);
                float growCSD   = (Float) model.getValueAt(i, 4);
                float die       = (Float) model.getValueAt(i, 5);
                float dieSD     = (Float) model.getValueAt(i, 6);
                float dieC      = (Float) model.getValueAt(i, 7);
                float dieCSD    = (Float) model.getValueAt(i, 8);
                float shrink    = (Float) model.getValueAt(i, 9);
                float shrinkSD  = (Float) model.getValueAt(i, 10);
                float shrinkC   = (Float) model.getValueAt(i, 11);
                float shrinkCSD = (Float) model.getValueAt(i, 12);
                
                Species s = new Species(Color.getHSBColor(rng.nextFloat(),(rng.nextInt(2000) + 1000) / 10000f,0.9f),
                        die,grow,shrink,dieC,growC,shrinkC, name, dieSD, growSD, shrinkSD, dieCSD, growCSD, shrinkCSD);
                speciesList.add(s);
                System.out.println("Added "+s);
            }
        }
        if(names == model.getRowCount() && names != 0)
            addEmptyRow();
        updateSpeciesSelection();
    }
    private class SpeciesTableModel extends DefaultTableModel {
        /**
         * 
         */
        private static final long serialVersionUID = -246523524036828973L;
        //                                           Growth   Growth(c) Mort     Mort(c)  Shrink   Shrink(c)
        private final String[] columnTitles = {"Name","A","SD","A","SD","A","SD","A","SD","A","SD","A","SD"};
        
        public SpeciesTableModel() {
            addRow(new Object[]{"A Hya.", 4.23f, 0f, 2.55f ,0.00078f,  0.39825816384942136f,0.1923691778874601f,0.10625308083644924f,0.0f ,2.07f,0.0014f,4.46f,0f});
            addRow(new Object[]{"PD", 0.38f, 0f, 0.36f, 0f,   0.61547599f,0.27085479f,0.12465263999999998f,0.0396f  ,1.04f,0.0025f,0.6f,0.0019f});
            for (String string : columnTitles) {
                addColumn(string);
            }
        }
        
        @Override
        public int getColumnCount() {
            return columnTitles.length;
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
        
    }
}

