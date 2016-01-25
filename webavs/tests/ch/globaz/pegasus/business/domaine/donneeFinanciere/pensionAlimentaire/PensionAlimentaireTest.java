package ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class PensionAlimentaireTest {
    private final static PensionAlimentaire dfDepense = new PensionAlimentaire(new Montant(1000), new Montant(150),
            PensionAlimentaireType.VERSEE, PensionAlimentaireLienParente.AUTRES, false, BuilderDf.createDF());

    private final static PensionAlimentaire dfRevenue = new PensionAlimentaire(new Montant(1000), new Montant(150),
            PensionAlimentaireType.DUE, PensionAlimentaireLienParente.AUTRES, false, BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.PENSION_ALIMENTAIRE, dfDepense.getTypeDonneeFinanciere());
    }

    @Test
    public void testPensionAlimentaire() throws Exception {
        assertTrue(dfDepense.getMontant().isMensuel());
        assertTrue(dfDepense.getMontantRenteEnfant().isMensuel());
    }

    @Test
    public void testComputeRevenuAnnuelWithMontant() throws Exception {
        assertEquals(Montant.newAnnuel(12000), dfRevenue.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrutWithMontant() throws Exception {
        assertEquals(Montant.newAnnuel(12000), dfRevenue.computeRevenuAnnuelBrut());
    }

    @Test
    public void testComputeDepenseWithMontant() throws Exception {
        assertEquals(Montant.newAnnuel(12000), dfDepense.computeDepense());
    }

    @Test
    public void testComputeDepenseWithMontantAndDeductionRenteEnfant() throws Exception {
        PensionAlimentaire dfDepense1 = new PensionAlimentaire(new Montant(1000), new Montant(150),
                PensionAlimentaireType.VERSEE, PensionAlimentaireLienParente.AUTRES, true, BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(10200), dfDepense1.computeDepense());
    }

    @Test
    public void testComputeRevenuAnnuelWithOutMontant() throws Exception {
        assertEquals(Montant.ZERO_ANNUEL, dfDepense.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrutWithOutMontant() throws Exception {
        assertEquals(Montant.ZERO_ANNUEL, dfDepense.computeRevenuAnnuelBrut());
    }

    @Test
    public void testComputeDepenseWithOutMontant() throws Exception {
        assertEquals(Montant.ZERO_ANNUEL, dfRevenue.computeDepense());
    }

}
