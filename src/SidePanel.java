import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JCheckBox;


/**
 * @author Jonathan
 * The sidepanel for configuring the simulation
 */
public class SidePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 3781284619710031148L;
    /**
     * Create the panel and the components inside it.
     */
    final private String LINE_SEP = System.getProperty("line.separator");
    private JComboBox speciesSelected;
    private JLabel lblIteration;
    public Simulation s;
    final private JProgressBar progressBar;
    private JFormattedTextField rowsInput, columnsInput, iterationsInput;
    private JLabel lblXY;
    private SimulationRunner smltnRnr;
    private JToggleButton tglbtnStart;
    private ArrayList<JFormattedTextField> inputsArray;
    private JRadioButton animateButton;
    private JRadioButton rdbtnSkip;
    private JCheckBox chckbxShowColNo;
    private JLabel lblSizeval;
    private JLabel lblColonyval;
    private JLabel lblGrowprobval;
    private JLabel lblGrowprobcval;
    private JLabel lblSpeciesVal;
    private JSeparator separator;
    private JCheckBox chckbxDisableMortality;
    private JCheckBox chckbxForceCompetition;
    private JCheckBox chckbxDisableGrowth;
    private JCheckBox chckbxDisableShrinkage;
    public SidePanel(final Simulation s) {
        setToolTipText("");
        this.s = s;
        smltnRnr = new SimulationRunner();
        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMinimumIntegerDigits(1);
        setBorder(new TitledBorder(null, "Setup", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        setLayout(new MigLayout("", "[][21.00][100px:n,grow]", "[][][][][][][][][][][][][][][]"));
        
        JLabel lblIterations = new JLabel("Iterations");
        add(lblIterations, "cell 0 0");
        progressBar = new JProgressBar(0,100);
        progressBar.setToolTipText("Percentage of simulation completed");
        iterationsInput = new JFormattedTextField(fmt);
        iterationsInput.setValue(100);
        iterationsInput.validate();
        add(iterationsInput, "cell 2 0,growx");
        ActionListener inputChangeListener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent evt) {
            //    progressBar.setMaximum(((Number)iterationsInput.getValue()).intValue());
                tick(100* s.coralSim.getTick()/((Number)iterationsInput.getValue()).intValue());
                s.coralSim.repaint();
            }
        };
        iterationsInput.addActionListener(inputChangeListener);
        
        JLabel lblRows = new JLabel("Rows");
        add(lblRows, "cell 0 1");
        
        rowsInput = new JFormattedTextField(fmt);
        rowsInput.setValue(100);
        add(rowsInput, "cell 2 1,growx");
        
        JLabel lblColumns = new JLabel("Columns");
        add(lblColumns, "cell 0 2");
        
        columnsInput = new JFormattedTextField(fmt);
        columnsInput.setValue(100);
        add(columnsInput, "cell 2 2,growx");
        
        JButton btnStep = new JButton("Step");
        btnStep.setToolTipText("Complete 1 step of the simulation");
        btnStep.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // Force the simulation to perform 1 tick
                tick(s.coralSim.tick());
                s.coralSim.repaint();
            }
        });
        
        JButton btnClear = new JButton("Reset");
        btnClear.setToolTipText("Clear the cells and reset the simulation");
        btnClear.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // Clear the grid, reset iterations
                resetSim();
            }
        });
        
        separator = new JSeparator();
        add(separator, "cell 0 5 3 1,growx");
        
        JLabel lblSpecies_1 = new JLabel("Species:");
        lblSpecies_1.setToolTipText("The current species");
        add(lblSpecies_1, "cell 0 6");
        
        lblSpeciesVal = new JLabel("speciesVal");
        add(lblSpeciesVal, "cell 2 6");
        
        JLabel lblSize = new JLabel("Size: ");
        lblSize.setToolTipText("size in square centimeters of the current colony");
        add(lblSize, "cell 0 7");
        
        lblSizeval = new JLabel("sizeVal");
        add(lblSizeval, "cell 2 7");
        
        JLabel lblColony = new JLabel("Colony:");
        add(lblColony, "cell 0 8");
        lblColony.setToolTipText("id of colony currently selected");
        
        lblColonyval = new JLabel("colonyVal");
        add(lblColonyval, "cell 2 8");
        
        JLabel lblGrowthProb = new JLabel("Growth Prob:");
        add(lblGrowthProb, "cell 0 9");
        lblGrowthProb.setToolTipText("Probability that this colony will grow when in isolation");
        
        lblGrowprobval = new JLabel("growProbVal");
        add(lblGrowprobval, "cell 2 9");
        
        JLabel lblGrowthProbC = new JLabel("Growth Prob. (c):");
        add(lblGrowthProbC, "cell 0 10");
        lblGrowthProbC.setToolTipText("Probability that this colony will grow when in competition");
        
        lblGrowprobcval = new JLabel("growProbCVal");
        add(lblGrowprobcval, "cell 2 10");
        add(btnClear, "cell 0 11,growx");
        add(btnStep, "cell 0 12,growx");
        
        lblIteration = new JLabel("Iteration: 0/"+iterationsInput.getValue());
        add(lblIteration, "cell 2 13,alignx left");
        
        JLabel lblSpecies = new JLabel("Species");
        lblSpecies.setToolTipText("Select the species to place when manually placing cells");
        add(lblSpecies, "cell 0 3");
        
        speciesSelected = new JComboBox();
        speciesSelected.setToolTipText("Select the species to place when manually placing cells");
        add(speciesSelected, "cell 2 3,growx");
        
        JLabel lblNewLabel = new JLabel("Simulation");
        add(lblNewLabel, "cell 0 4");
        
        animateButton = new JRadioButton("Animate");
        animateButton.setToolTipText("Selecting this will add a delay between each iteration and update the animation");
        add(animateButton, "flowy,cell 2 4");
        
        rdbtnSkip = new JRadioButton("Skip");
        rdbtnSkip.setToolTipText("Will not update the animation and have no pauses between iterations");
        add(rdbtnSkip, "cell 2 4");

        ButtonGroup animationMode = new ButtonGroup();
        animationMode.add(rdbtnSkip);
        animationMode.add(animateButton);
        rdbtnSkip.setSelected(true);
        
        lblXY = new JLabel("x=");
        lblXY.setToolTipText("Indicates the location you are about to add a cell to");
        add(lblXY, "cell 2 12");
        
        tglbtnStart = new JToggleButton("Start");
        tglbtnStart.setToolTipText("Run the simulation up to the specified number of iterations");
        add(tglbtnStart, "cell 0 13,growx");
        tglbtnStart.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent arg0) {
                if(tglbtnStart.isSelected())  {
                    tglbtnStart.setText("Stop");
                    // Start the simulation
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
        add(progressBar, "cell 0 14 3 1,growx");

        inputsArray = new ArrayList<JFormattedTextField>(3);
        inputsArray.add(iterationsInput);
        inputsArray.add(columnsInput);
        inputsArray.add(rowsInput);
        
        chckbxShowColNo = new JCheckBox("Show col. no.");
        chckbxShowColNo.setToolTipText("Display colony numbers");
        chckbxShowColNo.setSelected(true);
        add(chckbxShowColNo, "cell 2 4");
        
        chckbxDisableMortality = new JCheckBox("Disable Mortality");
        chckbxDisableMortality.setToolTipText("Prevent colonies from dying");
        add(chckbxDisableMortality, "cell 2 4");

        chckbxDisableGrowth = new JCheckBox("Disable Growth");
        chckbxDisableGrowth.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chckbxDisableShrinkage.isSelected())
                    chckbxDisableShrinkage.setSelected(false);
                
            }
        });
        add(chckbxDisableGrowth, "cell 2 4");
        
        chckbxDisableShrinkage = new JCheckBox("Disable Shrinkage");
        chckbxDisableShrinkage.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chckbxDisableGrowth.isSelected())
                    chckbxDisableGrowth.setSelected(false);
                
            }
        });
        add(chckbxDisableShrinkage, "cell 2 4");

        
        chckbxForceCompetition = new JCheckBox("Force Competition");
        chckbxForceCompetition.setToolTipText("the species will always act as though they are in competition");
        add(chckbxForceCompetition, "cell 2 4");
        chckbxShowColNo.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // Repaint the simulation when we toggle colony number visibility
                s.coralSim.repaint();
            }
        });
        try {
            for (JFormattedTextField i : inputsArray) {
                i.commitEdit();
                i.addActionListener(inputChangeListener);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    public boolean isColonyNumberDisplayed() {
        return chckbxShowColNo.isSelected();
    }
    
    /**
     * 
     * @return true if mortality is disabled
     */
    public boolean isMortalityDisabled() {
        return chckbxDisableMortality.isSelected();
    }
    
    public boolean isCompetitionForced() {
        return chckbxForceCompetition.isSelected();
    }
    /**
     * 
     * @return true if shrinkage is disabled
     */
    public boolean isShrinkageDisabled() {
        return chckbxDisableShrinkage.isSelected();
    }
    public boolean isGrowthDisabled() {
        return chckbxDisableGrowth.isSelected();
    }
    /**
     * I'm not sure if this even used
     * @param t
     */
    public void tick(int t) {
        // Increase the tick count
        lblIteration.setText("Iteration: "+s.coralSim.getTick()+"/"+iterationsInput.getValue());
        progressBar.setValue(t);
    }
    /**
     * Gets the number of rows specified
     * @return the number of rows for the simulation to use
     */
    public int getRows() {
        Long r =  (Long) rowsInput.getValue();
        return r.intValue();
    }
    /**
     * Gets the number of columns specified in the configuation
     * @return the number of columns
     */
    public int getColumns() {
        Long l = (Long) columnsInput.getValue();
        return l.intValue();
    }
    
    /**
     * Update the species available in the species selection menu 
     * @param ss the arraylist of species to use the in the menu
     */
    public void setSpeciesSelections(ArrayList<Species> ss) {   
        speciesSelected.removeAllItems();
        for (Species s : ss) {
            //System.out.println("Adding to list: "+s);
            speciesSelected.addItem(s);
        }
        s.coralSim.setSpecies(ss);
        resetSim();
    }
    
    /**
     * @return the species that is currently selected in the dropdown menu
     */
    public Species getSelectedSpecies() {
        return (Species) speciesSelected.getSelectedItem();
    }
    /**
     * Updates the labels to display information about the cell currently being moused over
     * @param p the (X,Y)  location that is being pointed to on the grid
     * @param s The species of the focal cell
     * @param colony The colony number of the focal cell
     * @param colony2 the size of the focal cell
     */
    public void setXY(Pair<Integer, Integer> p,int colonyId, Colony c) {
        lblXY.setText(p.x+", "+p.y);
        if(c == null) {
            for(JLabel lbl : new JLabel[]{lblColonyval,lblGrowprobcval,lblGrowprobval,lblSpeciesVal,lblSizeval})
                lbl.setText("");
        } else {
            Species s = c.getSpecies();
            int size = c.getCells().size();
            lblColonyval.setText(colonyId+"");
            lblSpeciesVal.setText(s.toString());
            lblSizeval.setText("<html>"+(size+c.getRemainingGrowth())+"cm<sup>2</sup></html>");
            lblGrowprobval.setText(s.getGrowShrinkP(size)+"");
            lblGrowprobcval.setText(s.getGrowShrinkPC(size)+"");
        }
    }
    /**
     * Reset the simulation 
     */
    private void resetSim() {
        s.coralSim.reset();
        tick(s.coralSim.getTick());
    }

    /**
     *  Run the simulation
     */
    private void runSimulation() {
        if(smltnRnr.isDone())
            smltnRnr = new SimulationRunner();
        smltnRnr.execute();
    }
    
    /**
     * Stop the simulation 
     */
    private void stopSimulation() {
        smltnRnr.cancel(true);
    }
    /**
     * Enable inputs after the simulation has finished
     */
    private void enableInputs() {
        for (JFormattedTextField i : inputsArray) {
            i.setEnabled(true);
        }
    }
    /**
     *Disable inputs
     */
    private void disableInputs() {
        for (JFormattedTextField i : inputsArray) {
            i.setEnabled(false);
        }
    }
    
    /**
     * @author Jonathan
     * Run the simulation in it's own swingworker thread.
     * Updates the progress bar accordingly.
     */
    class SimulationRunner extends SwingWorker<String, Object> {
        private BufferedWriter outFile,csvFile;
        private String logName,imageName,csvName;
        private Date now;
        public SimulationRunner() {
            now = Calendar.getInstance().getTime();
            logName = s.getLogDir()+(new SimpleDateFormat("ddMMyyyy-HHmmss'.log'").format(now));
            csvName = s.getLogDir()+(new SimpleDateFormat("ddMMyyyy-HHmmss'.csv'").format(now));
            imageName = s.getImageDir()+(new SimpleDateFormat("ddMMyyyy-HHmmss'.png'").format(now));
            addPropertyChangeListener(new PropertyChangeListener() {
                
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    // Update the progress bar with the current tick
                    if("progress".equals(evt.getPropertyName())){
                        tick((Integer) evt.getNewValue());
                    }
                }
            });
        }
        @Override
        protected String doInBackground() throws Exception {
            try {
                // Set the name of the logfile to be the current date and time
                
                
                outFile = new BufferedWriter(new FileWriter(logName));
                csvFile = new BufferedWriter(new FileWriter(csvName));
                int maxNameSize = "Name".length();
                StringBuilder speciesReport = new StringBuilder();
                StringBuilder sizeClassReport = new StringBuilder("Size Class Report"+LINE_SEP);
                sizeClassReport.append("Min,Max, Mortality, GrowShrinkP, GrowShrinkCP").append(LINE_SEP);
             //   System.out.println("Writing to: "+logName);
                int num = speciesSelected.getItemCount();
                for (int i = 0;i<num;i++) {
                    Species spec =  (Species)speciesSelected.getItemAt(i);
                    maxNameSize = Math.max(spec.getName().length(), maxNameSize);
                }
                for (int i = 0;i<num;i++) {
                    Species spec =  (Species)speciesSelected.getItemAt(i);
                    speciesReport.append(spec.getReport(maxNameSize));
                    sizeClassReport.append(spec.getName()).append(LINE_SEP).append(spec.sizeClassReport()).append(LINE_SEP);
                }
                speciesReport.insert(0,String.format("%"+(maxNameSize)+"s | %19s | %19s | %19s | %19s | %s%n","Name", "Growth","Growth (c)", "Shrinkage", "Shrinkage (c)","Recruits"));
                speciesReport.insert(0,"Species Report"+LINE_SEP);
              //  System.out.println(speciesReport);
              //  System.out.println(sizeClassReport);
                outFile.write("Image: "+imageName+LINE_SEP);
                outFile.write("Running Simulation at "+(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(now))+LINE_SEP);
                outFile.write(speciesReport.toString());
                outFile.write(sizeClassReport.toString());
                csvFile.write("Tick,ColonyNo,Size,Species"+LINE_SEP);
                // These exceptions are required
            } catch (IOException e) {
                e.printStackTrace();   
            } catch (Exception e) {
                e.printStackTrace();   
            }
            long start = 0;
            try{
                start = System.nanoTime();
                int max = ((Number) iterationsInput.getValue()).intValue();
                disableInputs();
                while(!isCancelled() && s.coralSim.getTick() < max) {

                    outFile.write(s.coralSim.getReport());
                    csvFile.write(s.coralSim.getCSVReport());
                    setProgress(100*s.coralSim.tick()/max);
                    if(animateButton.isSelected()) {
                        Thread.sleep(100);
                        s.coralSim.repaint();
                    }
                }
            } catch(InterruptedException e) {
                System.out.println("Cancelled");
                outFile.write("Cancelled");
            } catch(Exception e) {
                //System.err.println(e);
                e.printStackTrace();
            } finally {
                outFile.write("Time elapsed: "+((System.nanoTime()-start)/1000000000.0)+"s"+LINE_SEP);
                outFile.close();
                csvFile.close();
                s.coralSim.exportImage(imageName);
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
