package ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class LoyersTest {

    private DonneeFinanciere DF_REQ = BuilderDf.createDF(RoleMembreFamille.REQUERANT);
    private DonneeFinanciere DF_CONJ = BuilderDf.createDF(RoleMembreFamille.CONJOINT);
    private DonneeFinanciere DF_ENF = BuilderDf.createDF(RoleMembreFamille.ENFANT);

    private Montant M_2000 = new Montant(2000);
    private Montant M_100 = new Montant(100);
    private Montant M_20 = new Montant(20);
    private Montant M_15 = new Montant(15);
    private Montant M_5 = new Montant(5);

    private Loyers donneesFinancieresList = new Loyers();

    public LoyersTest() {
        donneesFinancieresList.add(new Loyer(M_2000, M_20, M_100, M_15, LoyerType.NET_AVEC_CHARGE, 2, false, false,
                DF_REQ));
        donneesFinancieresList.add(new Loyer(M_2000, M_5, M_15, M_15, LoyerType.NET_AVEC_CHARGE, 3, false, false,
                DF_CONJ));
        donneesFinancieresList.add(new Loyer(M_2000, M_5, M_15, M_15, LoyerType.NET_AVEC_CHARGE, 3, false, false,
                DF_ENF));
    }

    @Ignore
    @Test
    public void testSumSousLocation() throws Exception {
        assertEquals(Montant.newMensuel(1560), donneesFinancieresList.sumSousLocation());
    }

}
