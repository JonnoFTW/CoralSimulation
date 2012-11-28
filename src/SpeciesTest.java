import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.awt.Color;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class SpeciesTest {

    private Species ahya, pd;
    
    @Before
    public void setUp() {
        ArrayList<SizeClass> ahyaSC = new ArrayList<SizeClass>();
        //0-200, 200.0001-800, 800.0001-2000, >2000
        ahyaSC.add(new SizeClass(0, 200,  (1-Math.pow((1-0.272),12/7.5)), 0.88, 0.89));
        ahyaSC.add(new SizeClass(200, 800,  (1-Math.pow((1-0.125),12/7.5)), 0.77, 0.77));
        ahyaSC.add(new SizeClass(800, 2000,  (1-Math.pow((1-0.0678),12/7.5)), 0.60, 0.60));
        ahyaSC.add(new SizeClass(2000, Integer.MAX_VALUE,  (1-Math.pow((1-0),12/7.5)), 0.41, 0.41));
        
        // 0-50, 50.0001-100, 100.0001-200, >200.
        ArrayList<SizeClass> pdSC = new ArrayList<SizeClass>();
        pdSC.add(new SizeClass(0, 50,  (1-Math.pow((1-0.3799),2)), 0.42,    0.54));
        pdSC.add(new SizeClass(50, 100,  (1-Math.pow((1-0.1461),2)), 0.48,  0.57));
        pdSC.add(new SizeClass(100, 200,  (1-Math.pow((1-0.0644),2)), 0.48, 0.28));
        pdSC.add(new SizeClass(200, Integer.MAX_VALUE,  (1-Math.pow((1-0.02),2)), 0.36, 0.18));
        
        
 //     ahya = new Species(c,          grow, shrink, growC, shrinkC, name,     growSD, shrinkSD, growCSD, shrinkCSD, sizeClasses,growTS, shrinkTS, recruits);
        ahya = new Species(Color.blue, 4.23, 2.07,   2.55,  4.46,    "A Hya.", 0d,     0.00078,   0.0014, 0d,        ahyaSC,     15,     12 ,      1);
        pd   = new Species(Color.red,  0.38, 1.04,   0.36,  0.6d,    "PD",     1.04 ,  0.0025 ,   0.6  ,  0.0019,    pdSC,       6,      12 ,      1);
    }
    /**
     * Test that the getDie() method works. We don't need to test negative values
     * since the size of a @see HashSet cannot be negative and so a colony will never
     * attempt to get the mortality rate of a species of negative size. 
     * This test should also verify that the in() method of a {@link SizeClass} works correctly
     * and so the getGrowShrinkP() and getGrowShrinkPC() work correctly since the same method is used.
     */
    @Test
    public void testGetDie() {
        double dieAhya1 = 1-Math.pow((1-0.272),12/7.5);
        double dieAhya2 = 1-Math.pow((1-0.125),12/7.5);
        double dieAhya3 = 1-Math.pow((1-0.0678),12/7.5);
        double dieAhya4 = 1-Math.pow((1-0),12/7.5);
        int[] testSizes1 = new int[]{0,1,50,150,199,200,201};
        for (int i : testSizes1) {
            if(i >= 200) {
                assertTrue(dieAhya1 - ahya.getDie(i) > 1e-3);
            } else {
                assertEquals(dieAhya1, ahya.getDie(i),1e-3);
            }  
        }
        int[] testSizes2 = new int[]{201,300,799,800};
        for (int i : testSizes2) {
            if(i >= 800) {
                assertTrue(dieAhya2 - ahya.getDie(i) > 1e-3);
            } else {
                assertEquals(dieAhya2, ahya.getDie(i),1e-3);
            }  
        }
        int[] testSizes3 = new int[]{800,900,1999,2000,20001};
        for (int i : testSizes3) {
            if(i >= 2000) {
                assertTrue(dieAhya3 - ahya.getDie(i) > 1e-3);
            } else {
                assertEquals(dieAhya3, ahya.getDie(i),1e-3);
            }  
        }
        int[] testSizes4 = new int[]{2000,2500,99999};
        for (int i : testSizes4) {
            assertEquals(dieAhya4, ahya.getDie(i),1e-3);
        }
        // test the other species
        double diepd1 = 1-Math.pow((1-0.3799),2);
        double diepd2 = 1-Math.pow((1-0.1461),2);
        double diepd3 = 1-Math.pow((1-0.0644),2);
        double diepd4 = 1-Math.pow((1-0.02),2);
        int[] testSizes5 = new int[]{0,1,25,49,50,51};
        for (int i : testSizes5) {
            if(i >= 50) {
                assertTrue(diepd1 - pd.getDie(i) > 1e-3);
            } else {
                assertEquals(diepd1, pd.getDie(i),1e-3);
            }  
        }
        int[] testSizes6 = new int[]{50,51,52,75,99,100,101};
        for (int i : testSizes6) {
            if(i >= 100) {
                assertTrue(diepd2 - pd.getDie(i) > 1e-3);
            } else {
                assertEquals(diepd2, pd.getDie(i),1e-3);
            }  
        }
        int[] testSizes7 = new int[]{201,200,100,150};
        for (int i : testSizes7) {
            if(i >= 200) {
                assertTrue(diepd3 - pd.getDie(i) > 1e-3);
            } else {
                assertEquals(diepd3, pd.getDie(i),1e-3);
            }  
        }
        int[] testSizes8 = new int[]{2000,2500,99999};
        for (int i : testSizes8) {
            assertEquals(diepd4, pd.getDie(i),1e-3);
        }
    }

}
