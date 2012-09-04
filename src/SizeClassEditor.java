import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Component;


public class SizeClassEditor extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 6909421034651884335L;
    private JTable table;
    private SizeClassTableModel model;
    public SizeClassEditor(ActionListener saveListener, ActionListener cancelListener){
        setTitle("Edit Size Classes");
        model = new SizeClassTableModel();
        model.addTableModelListener(new TableModelListener() {
            
            @Override
            public void tableChanged(TableModelEvent arg0) {
                int filledRows = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    int filledIn =0;
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        // Check that each column of the row is filled out
                        if(model.getValueAt(i, j) != null) {
                            filledIn++;
                        }
                    }
                    if(filledIn == model.getColumnCount())
                        filledRows++;
                }
                if(filledRows == model.getRowCount()-1 )
                    addEmptyRow();
                
            }
        });
        
        table = new JTable(model);
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(new BorderLayout());
        
        panel.add(new JScrollPane(table),BorderLayout.CENTER);
        
        JPanel btn_panel = new JPanel();
        panel.add(btn_panel, BorderLayout.SOUTH);
        
        JButton btnSave = new JButton("Save");
        btn_panel.add(btnSave);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(cancelListener);
        btn_panel.add(btnCancel);
        btnSave.addActionListener(saveListener);
        pack();
    }
    
    public static SizeClassEditor createSizeClassEditor(Component c,
            ActionListener okListener, 
            ActionListener cancelListener) {
                return new SizeClassEditor(okListener, cancelListener);
        
    }
    public void addRow(SizeClass sc) {
        model.addRow(new Object[] {sc.getMin(),sc.getMax(),sc.getMortality(),sc.getGrowShrinkP(),sc.getGrowShrinkPC()});
    }
    private void addEmptyRow() {
        model.addRow(new Object[]  {});
    }
    public void setSizeClasses(ArrayList<SizeClass>  sc) {
        if(sc == null) {
            model.addRow(new Object[] {0,50,0d,0d,0d,0d});
            return;
        }
        model.setRowCount(0);
        for (SizeClass sizeClass : sc) {
            addRow(sizeClass);
        }
        addEmptyRow();
    }
    public boolean validateInput() {
        boolean valid = true;
        //try {
            for(int i = 0; i < model.getRowCount(); i++) {
                
                // Should probably set background colours for the cell
                System.out.println("validating row "+i);
                if(table.getValueAt(i, 0) == null) {
                    continue;
                }
                valid &= (Integer) table.getValueAt(i, 0) < (Integer) table.getValueAt(i, 1);
                for(int j = 2; j < 4; j++) 
                    valid &= ((Double) table.getValueAt(i, j)) < 1 && ((Double) table.getValueAt(i, j)) >= 0;
            }
       /* } catch (NullPointerException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(this, "An error was encountered in validation");
            return false;
        }*/
        return valid;
    }
    
    public ArrayList<SizeClass> getSizeClasses() {
         ArrayList<SizeClass> scs = new ArrayList<SizeClass>();
         System.out.println("Gettting size classes");
         for (int i = 0; i < table.getRowCount() -1; i++) {
             if(table.getValueAt(i, 0) == null) {
                 continue;
             }
            scs.add(new SizeClass(
                  (Integer)  table.getValueAt(i, 0),
                  (Integer)  table.getValueAt(i, 1),
                  (Double)  table.getValueAt(i, 2),
                  (Double)  table.getValueAt(i, 3),
                  (Double)  table.getValueAt(i, 4)));
        }
         return scs;
    }
    private class SizeClassTableModel  extends DefaultTableModel{
        /**
         * 
         */
        private static final long serialVersionUID = 6771833776664184864L;
        private final Object[] columns = new Object[] {"Min","Max","Mortality","growshrinkp","growshrinkp (c)"}; 
        public SizeClassTableModel() {
            for (Object i : columns) {
                addColumn(i);
            }
        } 
        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
        
    }
    
}
