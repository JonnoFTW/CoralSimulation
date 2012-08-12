import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;


public class SizeClassRow extends JPanel {
    private JFormattedTextField minInput;
    private JFormattedTextField maxInput;
    private JFormattedTextField mortInput;
    private JFormattedTextField growShrinkPInput;
    private JFormattedTextField growShrinkPCInput;
    public SizeClassRow(SizeClass sc) {
        setLayout(new GridLayout(2, 5,5, 5));
        
        JLabel lblMin = new JLabel("min");
        add(lblMin);
        
        JLabel lblMax = new JLabel("max");
        add(lblMax);
        
        JLabel lblMortality = new JLabel("mortality");
        add(lblMortality);
        
        JLabel lblGrowshrinkp = new JLabel("growshrinkP");
        add(lblGrowshrinkp);
        
        JLabel lblGrowshrinkpcompeting = new JLabel("growshrinkp (competing)");
        add(lblGrowshrinkpcompeting);
        
        minInput = new JFormattedTextField(sc.getMin());
        add(minInput);
        
        maxInput = new JFormattedTextField(sc.getMax());
        add(maxInput);
        
        mortInput = new JFormattedTextField(sc.getMortality());
        add(mortInput);
        
        growShrinkPInput = new JFormattedTextField(sc.getGrowShrinkP());
        add(growShrinkPInput);
        
        growShrinkPCInput = new JFormattedTextField(sc.getGrowShrinkPC());
        add(growShrinkPCInput);
        
        
    }
    

    public SizeClass getSizeClass() {
        // TODO Auto-generated method stub
        // Should probably check that these values are sane
        return new SizeClass((Integer) minInput.getValue(),
                (Integer) maxInput.getValue(),
                (Float) mortInput.getValue(),
                (Float) growShrinkPInput.getValue(),
                (Float) growShrinkPCInput.getValue());
    }
    
}
