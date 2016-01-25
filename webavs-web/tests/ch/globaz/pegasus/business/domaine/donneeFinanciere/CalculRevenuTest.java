package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;

@RunWith(Parameterized.class)
//@formatter:off
public class CalculRevenuTest {

    private Montant expected;
    private Montant montant;
    private Part part;
    private ProprieteType proprieteType;

    
    public CalculRevenuTest(ProprieteType proprieteType,  Part part, Montant montant, Montant expected) {
        this.expected = expected;
        this.montant = montant;
        this.part = part;
        this.proprieteType = proprieteType;
    }

    @Parameterized.Parameters()
    public static Collection<Object[]> parameters() {  Object[][] data = new Object[][] { 
            { ProprieteType.NU_PROPRIETAIRE, new Part(1, 2), new Montant(100),Montant.ZERO_ANNUEL},
            { ProprieteType.USUFRUITIER, new Part(1, 2), new Montant(100),Montant.ZERO_ANNUEL},
            { ProprieteType.DROIT_HABITATION, new Part(1, 2), new Montant(100),Montant.ZERO_ANNUEL},
            { ProprieteType.PROPRIETAIRE, new Part(1, 2), new Montant(100), new Montant(50)}

            };
        return Arrays.asList(data);
    };

    @Test
    public void testCalcul() throws Exception {
        assertEquals(expected,  CalculRevenu.calcul(proprieteType, part,montant));
    }


}
