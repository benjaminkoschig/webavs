package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonPrincipale;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilierHabitableType;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class BiensImmobiliersNonPrincipaleTest {

    private static ProprieteType PROPRIETAIRE = ProprieteType.PROPRIETAIRE;
    private static ProprieteType USUFRUITIER = ProprieteType.USUFRUITIER;
    private static Part PART_1_2 = new Part(1, 2);

    private DonneeFinanciere DF_REQ = BuilderDf.createDF(RoleMembreFamille.REQUERANT);
    private DonneeFinanciere DF_CONJ = BuilderDf.createDF(RoleMembreFamille.CONJOINT);
    private DonneeFinanciere DF_ENF = BuilderDf.createDF(RoleMembreFamille.ENFANT);

    private Montant M_LOYER_ENCAISSE = new Montant(1000);
    private Montant M_INTERET = new Montant(40);
    private Montant M_300000 = new Montant(300000);
    private Montant M_100000 = new Montant(100000);
    private Montant M_DETTE = new Montant(10000);
    private Montant M_15 = new Montant(15);
    private Montant M_SOUS_LOCATION = new Montant(5);

    private BiensImmobiliersNonPrincipale donneesFinancieresListBase = new BiensImmobiliersNonPrincipale();

    public BiensImmobiliersNonPrincipaleTest() {
        donneesFinancieresListBase
                .add(new BienImmobilierNonPrincipale(M_300000, M_INTERET, M_LOYER_ENCAISSE, M_SOUS_LOCATION, M_15,
                        M_DETTE, BienImmobilierHabitableType.APPARTEMENT, PART_1_2, PROPRIETAIRE, DF_REQ));
        donneesFinancieresListBase
                .add(new BienImmobilierNonPrincipale(M_100000, M_INTERET, M_LOYER_ENCAISSE, M_SOUS_LOCATION, M_15,
                        M_DETTE, BienImmobilierHabitableType.APPARTEMENT, PART_1_2, PROPRIETAIRE, DF_ENF));
        donneesFinancieresListBase
                .add(new BienImmobilierNonPrincipale(M_100000, M_INTERET, M_LOYER_ENCAISSE, M_SOUS_LOCATION, M_15,
                        M_DETTE, BienImmobilierHabitableType.APPARTEMENT, PART_1_2, USUFRUITIER, DF_CONJ));
    }

    @Test
    public void testGetMontantLoyerEncaisseReq() throws Exception {
        assertEquals(Montant.newAnnuel(3000), donneesFinancieresListBase.sumMontantLoyerEncaisse());
    }

    @Test
    public void testSumInteretHypotecaire() throws Exception {
        assertEquals(Montant.newAnnuel(60), donneesFinancieresListBase.sumInteretHypotecaire());
    }

    @Test
    public void testSumMontantValeurLocative() throws Exception {
        assertEquals(Montant.newAnnuel(45), donneesFinancieresListBase.sumMontantValeurLocative());
    }

    @Test
    public void testSumMontantValeurLocativePartPropriete() throws Exception {
        assertEquals(Montant.newAnnuel(22.5), donneesFinancieresListBase.sumMontantValeurLocativePartPropriete());
    }

    @Test
    public void testSumMontantLoyerEncaisse() throws Exception {
        assertEquals(Montant.newAnnuel(3000), donneesFinancieresListBase.sumMontantLoyerEncaisse());
    }

    @Test
    public void testSumSousLocation() throws Exception {
        assertEquals(Montant.newAnnuel(15), donneesFinancieresListBase.sumSousLocation());
    }
}
