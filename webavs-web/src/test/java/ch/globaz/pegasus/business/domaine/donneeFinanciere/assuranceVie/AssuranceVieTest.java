package ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceVie;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class AssuranceVieTest {
    private static AssuranceVie df = new AssuranceVie(new Montant(50), BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.ASSURANCE_VIE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testAssuranceVie() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testComputeFortuneBrut() throws Exception {
        assertEquals(Montant.newAnnuel(50), df.computeFortuneBrut());
    }

    @Test
    public void testComputeFortune() throws Exception {
        assertEquals(Montant.newAnnuel(50), df.computeFortuneBrut());
    }

}
