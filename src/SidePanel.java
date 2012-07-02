import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JProgressBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class SidePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 3781284619710031148L;
    /**
     * Create the panel.
     */
    private JComboBox speciesSelected;
    private JLabel lblIteration;
    private Simulation s;
    final private JProgressBar progressBar;
    private JFormattedTextField rowsInput, columnsInput, iterationsInput;
    private JLabel lblY;
    private JLabel lblX;
    private JLabel lblSpecies_1;
    public SidePanel(final Simulation s) {
        this.s = s;
        NumberFormat fmt = NumberFormat.getNumberInstance();
        setBorder(new TitledBorder(null, "Setup", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        setLayout(new MigLayout("", "[][30.00][grow]", "[][][][][][][][][][][][][][]"));
        
        JLabel lblIterations = new JLabel("Iterations");
        add(lblIterations, "cell 0 0");
        progressBar = new JProgressBar();
        iterationsInput = new JFormattedTextField(fmt);
        iterationsInput.setText("100");
        iterationsInput.validate();
        add(iterationsInput, "cell 2 0,growx");
        iterationsInput.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent ev) {
                // TODO Auto-generated method stub
                progressBar.setMaximum(Integer.parseInt(iterationsInput.getText()));
            }
        });
        
        JLabel lblRows = new JLabel("Rows");
        add(lblRows, "cell 0 1");
        
        rowsInput = new JFormattedTextField(fmt);
        rowsInput.setText("50");
        
        add(rowsInput, "cell 2 1,growx");
        
        JLabel lblColumns = new JLabel("Columns");
        add(lblColumns, "cell 0 2");
        
        columnsInput = new JFormattedTextField(fmt);
        columnsInput.setText("50");
        
        add(columnsInput, "cell 2 2,growx");
        
        try {
            columnsInput.commitEdit();
            rowsInput.commitEdit();
            iterationsInput.commitEdit();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        JLabel lblSpecies = new JLabel("Species");
        add(lblSpecies, "cell 0 3");
        
        speciesSelected = new JComboBox();
        add(speciesSelected, "cell 2 3,growx");
        
        JLabel lblNewLabel = new JLabel("Simulation");
        add(lblNewLabel, "cell 0 4");
        
        JRadioButton animateButton = new JRadioButton("Animate");
        add(animateButton, "flowy,cell 2 4");
        
        JButton btnStep = new JButton("Step");
        btnStep.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                s.coralSim.tick();
            }
        });
        
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // Clear the grid, reset iterations
                resetSim();
            }
        });
        add(btnClear, "cell 0 9,growx");
        
        lblSpecies_1 = new JLabel("Species:");
        add(lblSpecies_1, "cell 2 9");
        add(btnStep, "cell 0 10,growx");
        
        lblY = new JLabel("y=");
        lblY.setToolTipText("Indicates the location you are about to add a cell to");
        add(lblY, "cell 2 10");
        
        JButton tglbtnSaveImage = new JButton("Save Image");
        add(tglbtnSaveImage, "cell 0 11,growx");
        
        lblX = new JLabel("x=");
        lblX.setToolTipText("Indicates the location you are about to add a cell to");
        add(lblX, "cell 2 11");
        
        final JToggleButton tglbtnStart = new JToggleButton("Start");
        add(tglbtnStart, "cell 0 12,growx");
        tglbtnStart.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent arg0) {
                // TODO Auto-generated method stub
                if(tglbtnStart.isSelected()) 
                    tglbtnStart.setText("Stop");
                else 
                    tglbtnStart.setText("Start");
            }
        });
        
        lblIteration = new JLabel("Iteration: 0/"+iterationsInput.getValue());
        add(lblIteration, "cell 2 12,alignx left");
        
        
        progressBar.setStringPainted(true);
        add(progressBar, "cell 0 13 3 1,growx");
        
        JRadioButton rdbtnSkip = new JRadioButton("Skip");
        add(rdbtnSkip, "cell 2 4");

        ButtonGroup animationMode = new ButtonGroup();
        animationMode.add(rdbtnSkip);
        animationMode.add(animateButton);
        rdbtnSkip.setSelected(true);
    }
    public void tick() {
        // Increase the tick count
        lblIteration.setText("Iteration: "+s.coralSim.getTick()+"/"+iterationsInput.getValue());
        progressBar.setValue(s.coralSim.getTick());
    }
    public int getRows() {
        Long r =  (Long) rowsInput.getValue();
        return r.intValue();
    }
    public int getColumns() {
        Long l = (Long) columnsInput.getValue();
        return l.intValue();
    }
    public void setSpeciesSelections(ArrayList<Species> ss) {
        
        speciesSelected.removeAllItems();
        for (Species s : ss) {
            System.out.println("Adding to list: "+s);
            speciesSelected.addItem(s);
        }
        resetSim();
        
    }
    public Species getSelectedSpecies() {
        return (Species) speciesSelected.getSelectedItem();
    }
    public void setXY(Pair p,Species s) {
        lblX.setText("x="+p.x);
        lblY.setText("y="+p.y);
        if(s == null)
            lblSpecies_1.setText("");
        else
            lblSpecies_1.setText(s+"");
    }
    private void resetSim() {
        s.coralSim.reset();
        tick();
    }
}