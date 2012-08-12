import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;


public class SizeClassEditor extends JFrame {

    ArrayList<SizeClassRow> sizeClasses= new ArrayList<SizeClassRow>(5);
    private JPanel inputs;
    public SizeClassEditor(ActionListener lstn){
        setTitle("Edit Size Classes");
        
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout(0, 0));
        inputs = new JPanel();
        panel.add(inputs, BorderLayout.CENTER);
        inputs.setLayout(new GridLayout(0,1));
        
        JPanel btn_panel = new JPanel();
        panel.add(btn_panel, BorderLayout.SOUTH);
        
        JButton btnRemoveRow = new JButton("Remove Row");
        btn_panel.add(btnRemoveRow);
        
        JButton btnAddRow = new JButton("Add Row");
        btn_panel.add(btnAddRow);
        btnAddRow.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                addRow(new SizeClass(0, 0, 0, 0, 0));
            }
        });
        
        JButton btnSave = new JButton("Save");
        btn_panel.add(btnSave);
        btnSave.addActionListener(lstn);
        
    }
    public void addRow(SizeClass sc) {
        inputs.add(new SizeClassRow(sc));
        pack();
    }
    public void setSizeClasses(ArrayList<SizeClass>  sc) {
        for (SizeClassRow sizeClassRow : sizeClasses) {
            inputs.remove(sizeClassRow);
        }
        for (SizeClass sizeClass : sc) {
            addRow(sizeClass);
        }
    }
    public ArrayList<SizeClass> getSizeClasses() {
         ArrayList<SizeClass> scs = new ArrayList<SizeClass>();
         for (SizeClassRow  scr : sizeClasses) {
            scs.add(scr.getSizeClass());
        }
         return scs;
    }
    
}
