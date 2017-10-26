package ch.globaz.pegasus.business.domaine.donneeFinanciere.regime;

import static org.junit.Assert.*;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class RegimeTest {
    DonneeFinanciere df = new DonneeFinanciereHeader(RoleMembreFamille.REQUERANT, new Date(), new Date(), "1", "2");
    Regime regime = new Regime(new Montant(150), IRFCodeTypesDeSoins.SOUS_TYPE_2_1_REGIME_ALIMENTAIRE, df);

    @Test
    public void testRFRegimePrestationAccordee() throws Exception {
        assertTrue(regime.getMontant().isMensuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.UNDIFINED, regime.getTypeDonneeFinanciere());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(1800), regime.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(1800), regime.computeRevenuAnnuelBrut());
    }

}
