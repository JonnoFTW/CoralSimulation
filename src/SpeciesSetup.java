import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

import TableHeader.ColumnGroup;
import TableHeader.GroupableTableHeader;

/**
 * @author Jonathan
 * A table for setting up species, should load from file on creation
 *
 */
public class SpeciesSetup extends JPanel implements TableModelListener {
    private static final long serialVersionUID = -599805443131295797L;
    DefaultTableModel model = new SpeciesTableModel();
    private ArrayList<Species> speciesList = new ArrayList<Species>();
    private JTable tbl;
    private SidePanel sp;
    private Random rng;
    /**
     * @param sp
     */
    public SpeciesSetup(SidePanel sp) {
        this.sp = sp;
        
        rng = new Random();
        // Load in the default species
      //  importSpecies("default.dat");
        setLayout(new BorderLayout());
      
        
        model.addTableModelListener(this);
        addEmptyRow();
        tbl = new JTable(model) {
            /**
             * 
             */
            private static final long serialVersionUID = 4222953148165159012L;

            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        tbl.getColumnModel().getColumn(12).setCellEditor(new SizeClassTableCellEditor());
     //   tbl.setDefaultEditor(ArrayList.class, new SizeClassTableCellEditor());
        TableColumnModel cm = tbl.getColumnModel();
        ColumnGroup growthAll = new ColumnGroup("Growth");
        ColumnGroup growthC = new ColumnGroup("Competing");
        ColumnGroup growthNC = new ColumnGroup("Non-competing");
        growthAll.add(growthNC);
        growthAll.add(growthC);
        
        growthNC.add(cm.getColumn(1));
        growthNC.add(cm.getColumn(2));
        
        growthC.add(cm.getColumn(3));
        growthC.add(cm.getColumn(4));
        growthAll.add(cm.getColumn(5));
        
        ColumnGroup shrinkAll = new ColumnGroup("Shrinkage");
        
        ColumnGroup shrinkNC = new ColumnGroup("Non-competing");
        shrinkNC.add(cm.getColumn(6));
        shrinkNC.add(cm.getColumn(7));
        ColumnGroup  shrinkC = new ColumnGroup("Competing");
        shrinkC.add(cm.getColumn(8));
        shrinkC.add(cm.getColumn(9));
        shrinkAll.add(shrinkNC);
        shrinkAll.add(shrinkC);
        shrinkAll.add(cm.getColumn(10));
        GroupableTableHeader header = (GroupableTableHeader)tbl.getTableHeader();
        for (ColumnGroup cg : new ColumnGroup[] {growthAll,growthNC,growthC,shrinkAll,shrinkNC,shrinkC}) {
            header.addColumnGroup(cg);
        }
        
        tbl.setFillsViewportHeight(true);
        add(new JScrollPane(tbl),BorderLayout.CENTER);
        add(new JLabel("Warning: modifying the species in any way will clear the simulation"),BorderLayout.SOUTH);      
    }

    /**
     * @param fileName
     */
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
    /**
     * 
     */
    public void importSpecies() {
        // Use the default species file
        importSpecies(sp.s.getSpeciesDir()+"default.dat");
    }
    /**
     * @param fileName
     */
    public void importSpecies(String fileName) {
        try {
                ObjectInputStream objIn = new  ObjectInputStream(new FileInputStream(fileName));
                speciesList = (ArrayList<Species>) objIn.readObject();
                objIn.close();
                setSpeciesList(speciesList);
                
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

    /**
     * @param speciesList
     */
    private void setSpeciesList(ArrayList<Species> speciesList) {
        model.setRowCount(0);
        for (Species s : speciesList) {
            model.addRow(s.getArray());
        }
        
    }

    /**
     * @return
     */
    public ArrayList<Species> getSpecies()  {
        return speciesList;
    }
    /**
     * @return
     */
    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < model.getRowCount(); i++) {
            names.add((String) model.getValueAt(i, 0));
        }
        return names;
    }
    /**
     * 
     */
    private void updateSpeciesSelection() {
        sp.setSpeciesSelections(speciesList);      
    }
    /**
     * 
     */
    private void addEmptyRow() {
        model.addRow(new Object[]  {""});
    }
    /* (non-Javadoc)
     * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
     */
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
      //      System.out.println("User filled in "+filledIn+" columns");
            if(filledIn == model.getColumnCount()) {
                names++;
                String name      = (String) model.getValueAt(i, 0);
                double grow      = (Double) model.getValueAt(i, 1);
                double growSD    = (Double) model.getValueAt(i, 2);                
                double growC     = (Double) model.getValueAt(i, 3);
                double growCSD   = (Double) model.getValueAt(i, 4);
                int   growTS     = (Integer) model.getValueAt(i, 5);
                double shrink    = (Double) model.getValueAt(i, 6);
                double shrinkSD  = (Double) model.getValueAt(i, 7);
                double shrinkC   = (Double) model.getValueAt(i, 8);
                double shrinkCSD = (Double) model.getValueAt(i, 9);
                int   shrinkTS   = (Integer) model.getValueAt(i, 10);
                int   recruits   = (Integer) model.getValueAt(i, 11);
                ArrayList<SizeClass> sizeClasses = (ArrayList<SizeClass>) model.getValueAt(i, 12);
                
                Species s = new Species(Color.getHSBColor(rng.nextFloat(),(rng.nextInt(2000) + 1000) / 10000f,0.9f),
                        grow,shrink,growC,shrinkC, name,  growSD, shrinkSD, growCSD, shrinkCSD, sizeClasses,growTS, shrinkTS, recruits);
                speciesList.add(s);
       //         System.out.println("Added "+s);
            }
        }
        if(names == model.getRowCount() && names != 0)
            addEmptyRow();
        updateSpeciesSelection();
    }
    /**
     * @author Jonathan
     *
     */
    private class SpeciesTableModel extends DefaultTableModel {
        private static final long serialVersionUID = -246523524036828973L;
        //                                            Growth   Growth(c)Shrink   Shrink(c)
        private final String[] columnTitles = {"Name","Constant","Size Dependent","Constant","Size Dependent","Time Scale","Constant","Size Dependent","Constant","Size Dependent","Time Scale","Recruits","Size Classes"};
        
        
        /**
         * 
         */
        public SpeciesTableModel() {
            // These should really be serialised
            ArrayList<SizeClass> ahyaSC = new ArrayList<SizeClass>();
            //0-200, 200.0001-800, 800.0001-2000, >2000
            ahyaSC.add(new SizeClass(0, 200,  (1-Math.pow((1-0.272),12/7.5)), 0.88, 0.89));
            ahyaSC.add(new SizeClass(201, 800,  (1-Math.pow((1-0.125),12/7.5)), 0.77, 0.77));
            ahyaSC.add(new SizeClass(800, 2000,  (1-Math.pow((1-0.0678),12/7.5)), 0.60, 0.60));
            ahyaSC.add(new SizeClass(2000, Integer.MAX_VALUE,  (1-Math.pow((1-0),12/7.5)), 0.41, 0.41));
            
            // 0-50, 50.0001-100, 100.0001-200, >200.
            ArrayList<SizeClass> pdSC = new ArrayList<SizeClass>();
            pdSC.add(new SizeClass(0, 50,  (1-Math.pow((1-0.3799),2)), 0.42,    0.54));
            pdSC.add(new SizeClass(51, 100,  (1-Math.pow((1-0.1461),2)), 0.48,  0.57));
            pdSC.add(new SizeClass(101, 200,  (1-Math.pow((1-0.0644),2)), 0.48, 0.28));
            pdSC.add(new SizeClass(200, Integer.MAX_VALUE,  (1-Math.pow((1-0.02),2)), 0.36, 0.18));
            
            addRow(new Object[]{"A Hya.", 4.23, 0d, 2.55, 0.00078,15, 2.07 , 0.0014 ,4.46 ,0d     ,12 ,1, ahyaSC});
            addRow(new Object[]{"PD",     0.38, 0d, 0.36, 0d     ,6, 1.04 , 0.0025 ,0.6  ,0.0019 ,12 ,1, pdSC});
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
    /**
     * @author Jonathan
     *
     */
    private class SizeClassTableCellEditor extends AbstractCellEditor 
                                           implements TableCellEditor, ActionListener{ 
        /**
         * 
         */
        private static final long serialVersionUID = -1579587255799355577L;
        protected static final String EDIT = "edit";
        private ArrayList<SizeClass> sizeClasses;
        private JButton button;
        private SizeClassEditor editor;
        public SizeClassTableCellEditor(){ 
            
            button = new JButton();
            button.setActionCommand(EDIT);
            button.addActionListener(this);
            button.setBorderPainted(false);
            button.setBackground(Color.white);
            
            editor = SizeClassEditor.createSizeClassEditor(button, this, null);
        }

        @Override
        public Object getCellEditorValue() {
            return sizeClasses;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(EDIT.equals(e.getActionCommand())) {
                // User clicked the cell, bring up the editor
                editor.setVisible(true);
                editor.setSizeClasses(sizeClasses);
                editor.setAlwaysOnTop(true);
            } else {
                if(editor.validateInput()) {
                    sizeClasses = editor.getSizeClasses();
                    editor.setVisible(false);
                    fireEditingStopped(); // This being here causes the cellrenderer to show a button, it not being here
                                          // causes data changes to not be saved
                    
                } else {
                    // user needs to correct input
                    JOptionPane.showMessageDialog(editor, "You have An error in your size classes", "Error In Size Class Configuration", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        }

        @Override
        public Component getTableCellEditorComponent(JTable table,
                Object value, boolean isSelected, int row, int column) {
            sizeClasses = (ArrayList<SizeClass>) value;
            return button;
        } 
    } 
}

