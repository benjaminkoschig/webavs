package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import static org.junit.Assert.*;
import org.junit.Test;

public class DonneeFinanciereTypeTest {

    @Test
    public void testIsRenteAvsAi() throws Exception {
        assertTrue(DonneeFinanciereType.RENTE_AVS_AI.isRenteAvsAi());
    }

    @Test
    public void testIsIjAi() throws Exception {
        assertTrue(DonneeFinanciereType.IJAI.isIjAi());
    }

    @Test
    public void testIsApiAvsAi() throws Exception {
        assertTrue(DonneeFinanciereType.API_AVS_AI.isApiAvsAi());
    }

    @Test
    public void testIsAutreRente() throws Exception {
        assertTrue(DonneeFinanciereType.AUTRE_RENTE.isAutreRente());
    }

    @Test
    public void testIsIndeminteJournaliereApg() throws Exception {
        assertTrue(DonneeFinanciereType.INDEMNITE_JOURNLIERE_APG.isIndeminteJournaliereApg());
    }

    @Test
    public void testIsAutreApi() throws Exception {
        assertTrue(DonneeFinanciereType.AUTRE_API.isAutreApi());
    }

    @Test
    public void testIsLoyer() throws Exception {
        assertTrue(DonneeFinanciereType.LOYER.isLoyer());
    }

    @Test
    public void testIsTaxeJournalierHome() throws Exception {
        assertTrue(DonneeFinanciereType.TAXE_JOURNALIERE_HOME.isTaxeJournalierHome());
    }

    @Test
    public void testIsCompteBancairePostal() throws Exception {
        assertTrue(DonneeFinanciereType.COMPTE_BANCAIRE_POSTAL.isCompteBancairePostal());
    }

    @Test
    public void testIsTitre() throws Exception {
        assertTrue(DonneeFinanciereType.TITRE.isTitre());
    }

    @Test
    public void testIsAssuranceVie() throws Exception {
        assertTrue(DonneeFinanciereType.ASSURANCE_VIE.isAssuranceVie());
    }

    @Test
    public void testIsCapitalLpp() throws Exception {
        assertTrue(DonneeFinanciereType.CAPITAL_LPP.isCapitalLpp());
    }

    @Test
    public void testIsAutreDetteProuvee() throws Exception {
        assertTrue(DonneeFinanciereType.AUTRE_DETTE_PROUVEE.isAutreDetteProuvee());
    }

    @Test
    public void testIsPretEnversTiers() throws Exception {
        assertTrue(DonneeFinanciereType.PRET_ENVERS_TIERS.isPretEnversTiers());
    }

    @Test
    public void testIsAssuranceRenteViagere() throws Exception {
        assertTrue(DonneeFinanciereType.ASSURANCE_RENTE_VIAGERE.isAssuranceRenteViagere());
    }

    @Test
    public void testIsNumeraire() throws Exception {
        assertTrue(DonneeFinanciereType.NUMERAIRE.isNumeraire());
    }

    @Test
    public void testIsMarchandiseStock() throws Exception {
        assertTrue(DonneeFinanciereType.MARCHANDISE_STOCK.isMarchandiseStock());
    }

    @Test
    public void testIsVehicule() throws Exception {
        assertTrue(DonneeFinanciereType.VEHICULE.isVehicule());
    }

    @Test
    public void testIsBetail() throws Exception {
        assertTrue(DonneeFinanciereType.BETAIL.isBetail());
    }

    @Test
    public void testIsAutreFortuneMobiliere() throws Exception {
        assertTrue(DonneeFinanciereType.AUTRE_FORTUNE_MOBILIERE.isAutreFortuneMobiliere());
    }

    @Test
    public void testIsRevenuActiviteLucrativeIndependante() throws Exception {
        assertTrue(DonneeFinanciereType.REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE.isRevenuActiviteLucrativeIndependante());
    }

    @Test
    public void testIsRevenueHypothtique() throws Exception {
        assertTrue(DonneeFinanciereType.REVENU_HYPOTHETIQUE.isRevenueHypothtique());
    }

    @Test
    public void testIsAllocationFamilliale() throws Exception {
        assertTrue(DonneeFinanciereType.ALLOCATION_FAMILIALLE.isAllocationFamilliale());
    }

    @Test
    public void testIsContratEntretienViager() throws Exception {
        assertTrue(DonneeFinanciereType.CONTRAT_ENTRETIEN_VIAGER.isContratEntretienViager());
    }

    @Test
    public void testIsAutreRevenue() throws Exception {
        assertTrue(DonneeFinanciereType.AUTRE_REVENU.isAutreRevenue());
    }

    @Test
    public void testIsCotisationPsal() throws Exception {
        assertTrue(DonneeFinanciereType.COTISATION_PSAL.isCotisationPsal());
    }

    @Test
    public void testIsPensionAlimentaire() throws Exception {
        assertTrue(DonneeFinanciereType.PENSION_ALIMENTAIRE.isPensionAlimentaire());
    }

    @Test
    public void testIsDessaississementFortune() throws Exception {
        assertTrue(DonneeFinanciereType.DESSAISISSEMENT_FORTUNE.isDessaississementFortune());
    }

    @Test
    public void testIsDessaisissementRevenu() throws Exception {
        assertTrue(DonneeFinanciereType.DESSAISISSEMENT_REVENU.isDessaisissementRevenu());
    }

    @Test
    public void testIsRevenueActiviteLucrativeDependante() throws Exception {
        assertTrue(DonneeFinanciereType.REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE.isRevenueActiviteLucrativeDependante());
    }

    @Test
    public void testIsBienImmobilierServantHbitationPrincipale() throws Exception {
        assertTrue(DonneeFinanciereType.BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE
                .isBienImmobilierServantHbitationPrincipale());
    }

    @Test
    public void testIsBienImmobilierNonHabitable() throws Exception {
        assertTrue(DonneeFinanciereType.BIEN_IMMOBILIER_NON_HABITABLE.isBienImmobilierNonHabitable());
    }

    @Test
    public void testIsBienImmobilierNonPrincipale() throws Exception {
        assertTrue(DonneeFinanciereType.BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE.isBienImmobilierNonPrincipale());
    }

    // @Test
    // public void testTypeDonneeFinanciere() throws Exception {
    // throw new RuntimeException("not yet implemented");
    // }
    //
    // @Test
    // public void testFromValue() throws Exception {
    // throw new RuntimeException("not yet implemented");
    // }
    //
    // @Test
    // public void testGetValue() throws Exception {
    // throw new RuntimeException("not yet implemented");
    // }

}
