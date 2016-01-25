package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilierHabitableType;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class BiensImmobiliersServantHabitationPrincipaleTest {

    private static ProprieteType PROPRIETAIRE = ProprieteType.PROPRIETAIRE;
    private static ProprieteType USUFRUITIER = ProprieteType.USUFRUITIER;
    private static Part PART_1_2 = new Part(1, 2);

    private DonneeFinanciere DF_REQ = BuilderDf.createDF(RoleMembreFamille.REQUERANT);
    private DonneeFinanciere DF_CONJ = BuilderDf.createDF(RoleMembreFamille.CONJOINT);
    private DonneeFinanciere DF_ENF = BuilderDf.createDF(RoleMembreFamille.ENFANT);

    private Montant M_VALEURLOCATIVE = new Montant(1000);
    private Montant M_300000 = new Montant(300000);
    private Montant M_100000 = new Montant(100000);
    private Montant M_20 = new Montant(20);
    private Montant M_INTERET = new Montant(40);
    private Montant M_SOUS_LOCATION = new Montant(5);
    private Montant M_DETTE = new Montant(10000);

    private BiensImmobiliersServantHabitationPrincipale donneesFinancieresListBase = new BiensImmobiliersServantHabitationPrincipale();

    public BiensImmobiliersServantHabitationPrincipaleTest() {
        donneesFinancieresListBase.add(new BienImmobilierServantHabitationPrincipale(M_300000, M_VALEURLOCATIVE,
                M_INTERET, M_20, M_SOUS_LOCATION, M_DETTE, 1, BienImmobilierHabitableType.APPARTEMENT, PART_1_2,
                PROPRIETAIRE, DF_REQ));
        donneesFinancieresListBase.add(new BienImmobilierServantHabitationPrincipale(M_100000, M_VALEURLOCATIVE,
                M_INTERET, M_20, M_SOUS_LOCATION, M_DETTE, 1, BienImmobilierHabitableType.APPARTEMENT, PART_1_2,
                PROPRIETAIRE, DF_ENF));
        donneesFinancieresListBase.add(new BienImmobilierServantHabitationPrincipale(M_100000, M_VALEURLOCATIVE,
                M_INTERET, M_20, M_SOUS_LOCATION, M_DETTE, 1, BienImmobilierHabitableType.APPARTEMENT, PART_1_2,
                USUFRUITIER, DF_CONJ));
    }

    @Test
    public void testGetMontantLoyerEncaisse() throws Exception {
        assertEquals(Montant.newAnnuel(60), donneesFinancieresListBase.sumMontantLoyerEncaisse());
    }

    @Test
    public void testSumMontantValeurLocativePartPropriete() throws Exception {
        assertEquals(Montant.newAnnuel(1500), donneesFinancieresListBase.sumMontantValeurLocativePartPropriete());
    }

    @Test
    public void testSumMontantValeurLocative() throws Exception {
        assertEquals(Montant.newAnnuel(3000), donneesFinancieresListBase.sumMontantValeurLocative());
    }

    @Test
    public void testSumInteretHypothecaire() throws Exception {
        assertEquals(Montant.newAnnuel(60), donneesFinancieresListBase.sumInteretHypothecaire());
    }

    @Test
    public void testSumMontantLoyerEncaisse() throws Exception {
        assertEquals(Montant.newAnnuel(60), donneesFinancieresListBase.sumMontantLoyerEncaisse());
    }

    @Test
    public void testSumSousLocation() throws Exception {
        assertEquals(Montant.newAnnuel(15), donneesFinancieresListBase.sumSousLocation());
    }

}
