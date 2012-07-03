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
import javax.swing.SwingWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
    private SimulationRunner smltnRnr;
    private JToggleButton tglbtnStart;
    private ArrayList<JFormattedTextField> inputsArray;
    private JRadioButton animateButton;
    private JRadioButton rdbtnSkip;
    public SidePanel(final Simulation s) {
        this.s = s;
        smltnRnr = new SimulationRunner();
        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMinimumFractionDigits(1);
        setBorder(new TitledBorder(null, "Setup", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        setLayout(new MigLayout("", "[][30.00][grow]", "[][][][][][][][][][][][][][]"));
        
        JLabel lblIterations = new JLabel("Iterations");
        add(lblIterations, "cell 0 0");
        progressBar = new JProgressBar();
        iterationsInput = new JFormattedTextField(fmt);
        iterationsInput.setText("100");
        iterationsInput.validate();
        add(iterationsInput, "cell 2 0,growx");
        ActionListener inputChangeListener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent evt) {
                s.coralSim.repaint();
            }
        };
        iterationsInput.addActionListener(inputChangeListener);
        
        JLabel lblRows = new JLabel("Rows");
        add(lblRows, "cell 0 1");
        
        rowsInput = new JFormattedTextField(fmt);
        rowsInput.setText("100");
        rowsInput.addActionListener(inputChangeListener);
        add(rowsInput, "cell 2 1,growx");
        
        JLabel lblColumns = new JLabel("Columns");
        add(lblColumns, "cell 0 2");
        
        columnsInput = new JFormattedTextField(fmt);
        columnsInput.setText("100");
        columnsInput.addActionListener(inputChangeListener);
        add(columnsInput, "cell 2 2,growx");
        
        lblIteration = new JLabel("Iteration: 0/"+iterationsInput.getValue());
        add(lblIteration, "cell 2 12,alignx left");
        
        JLabel lblSpecies = new JLabel("Species");
        add(lblSpecies, "cell 0 3");
        
        speciesSelected = new JComboBox();
        add(speciesSelected, "cell 2 3,growx");
        
        JLabel lblNewLabel = new JLabel("Simulation");
        add(lblNewLabel, "cell 0 4");
        
        animateButton = new JRadioButton("Animate");
        add(animateButton, "flowy,cell 2 4");
        
        rdbtnSkip = new JRadioButton("Skip");
        add(rdbtnSkip, "cell 2 4");

        ButtonGroup animationMode = new ButtonGroup();
        animationMode.add(rdbtnSkip);
        animationMode.add(animateButton);
        rdbtnSkip.setSelected(true);
        
        JButton btnStep = new JButton("Step");
        btnStep.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                tick(s.coralSim.tick());
                s.coralSim.repaint();
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
        
        tglbtnStart = new JToggleButton("Start");
        add(tglbtnStart, "cell 0 12,growx");
        tglbtnStart.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent arg0) {
                // TODO Auto-generated method stub
                if(tglbtnStart.isSelected())  {
                    tglbtnStart.setText("Stop");
                    // STart the simulation
                    runSimulation();
                }
                else {
                    tglbtnStart.setText("Start");
                    // Start the timer
                    stopSimulation();
                }
            }
        });
        progressBar.setStringPainted(true);
        add(progressBar, "cell 0 13 3 1,growx");
        try {
            columnsInput.commitEdit();
            rowsInput.commitEdit();
            iterationsInput.commitEdit();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        inputsArray = new ArrayList<JFormattedTextField>(3);
        inputsArray.add(iterationsInput);
        inputsArray.add(columnsInput);
        inputsArray.add(rowsInput);

    }
    public void tick(int t) {
        // Increase the tick count
        lblIteration.setText("Iteration: "+t+"/"+iterationsInput.getValue());
        progressBar.setValue(t);
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
        tick(s.coralSim.getTick());
    }

    private void runSimulation() {
        if(smltnRnr.isDone())
            smltnRnr = new SimulationRunner();
        smltnRnr.execute();
    }
    private void stopSimulation() {
        smltnRnr.cancel(true);
    }
    private void enableInputs() {
        for (JFormattedTextField i : inputsArray) {
            i.setEnabled(true);
        }
    }
    private void disableInputs() {
        for (JFormattedTextField i : inputsArray) {
            i.setEnabled(false);
        }
    }
    class SimulationRunner extends SwingWorker<String, Object> {
        public SimulationRunner() {
            // TODO Auto-generated constructor stub
            addPropertyChangeListener(new PropertyChangeListener() {
                
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    // TODO Auto-generated method stub
                    if("progress".equals(evt.getPropertyName())){
                        tick((Integer) evt.getNewValue());
                    }
                }
            });
        }
        @Override
        protected String doInBackground() throws Exception {
            // TODO Auto-generated method stub
            long start = 0;
            try{
                start = System.nanoTime();
                int max = ((Number) iterationsInput.getValue()).intValue();
                disableInputs();
                while(!isCancelled() && s.coralSim.getTick() < max) {
                    setProgress(max * s.coralSim.tick() / 100);
                    if(animateButton.isSelected()) {
                        Thread.sleep(100);
                        s.coralSim.repaint();
                    }
                }
            } catch(Exception e) {
                System.err.println(e);
            } finally {
                System.out.println("Time elapsed: "+((System.nanoTime()-start)/1000000000.0));
            }
            return null;
        }
        @Override
        protected void done() {
            s.coralSim.repaint();
            enableInputs();
            tglbtnStart.setSelected(false);
        }
    }
}
