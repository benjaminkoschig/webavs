package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereType;

public class AutresRentesTest {

    private DonneeFinanciere DF_REQ = BuilderDf.createDF(RoleMembreFamille.REQUERANT);
    private DonneeFinanciere DF_CONJ = BuilderDf.createDF(RoleMembreFamille.CONJOINT);
    private DonneeFinanciere DF_ENF = BuilderDf.createDF(RoleMembreFamille.ENFANT);

    private Montant M_1000 = new Montant(1000);

    private AutresRentes donneesFinancieresList = new AutresRentes();

    public AutresRentesTest() {
        donneesFinancieresList.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.LPP, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_REQ));

        donneesFinancieresList.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.LPP, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_CONJ));
        donneesFinancieresList.add(new AutreRente(M_1000, AutreRenteType.INVALIDITE, AutreRenteGenre.AUTRES, "libelle",
                MonnaieEtrangereType.INDEFINIT, DF_ENF));
    }

    @Test
    public void testGetAutresRentesByGenre() throws Exception {
        assertEquals(2, donneesFinancieresList.getAutresRentesByGenre(AutreRenteGenre.LPP).size());
    }

}
