package ch.globaz.pegasus.business.domaine.donneeFinanciere.api.autreApi;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresBase;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.ApiDegre;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.autreApi.AutreApi;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.api.autreApi.AutreApiType;

public class AutreApiTest extends DonneesFinancieresBase {
    private static AutreApi df = new AutreApi(M_50, AutreApiType.API_ACCIDENT, ApiDegre.FAIBLE, "libelle", build());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.AUTRE_API, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testAutreApi() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(50), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(50), df.computeRevenuAnnuelBrut());
    }
}
