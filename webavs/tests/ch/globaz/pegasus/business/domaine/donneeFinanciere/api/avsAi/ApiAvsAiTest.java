package ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresBase;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.ApiDegre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi.ApiAvsAi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.avsAi.ApiType;

public class ApiAvsAiTest extends DonneesFinancieresBase {
    private static ApiAvsAi df = new ApiAvsAi(M_50, ApiType.API_81, ApiDegre.FAIBLE, build());

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(600), df.computeRevenuAnnuel());
    }

    @Test
    public void testApiAvsAi() throws Exception {
        assertTrue(df.getMontant().isMensuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.API_AVS_AI, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(600), df.computeRevenuAnnuelBrut());
    }

}
