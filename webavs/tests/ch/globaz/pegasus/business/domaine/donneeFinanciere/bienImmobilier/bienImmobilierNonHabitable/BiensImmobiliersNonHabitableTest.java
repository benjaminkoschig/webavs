package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class BiensImmobiliersNonHabitableTest {

    private static ProprieteType PROPRIETAIRE = ProprieteType.PROPRIETAIRE;
    private static ProprieteType USUFRUITIER = ProprieteType.USUFRUITIER;
    private static Part PART_1_2 = new Part(1, 2);

    private DonneeFinanciere DF_REQ = BuilderDf.createDF(RoleMembreFamille.REQUERANT);
    private DonneeFinanciere DF_CONJ = BuilderDf.createDF(RoleMembreFamille.CONJOINT);
    private DonneeFinanciere DF_ENF = BuilderDf.createDF(RoleMembreFamille.ENFANT);

    private Montant M_1000 = new Montant(1000);
    private Montant M_INTERET = new Montant(40);
    private Montant M_300000 = new Montant(300000);
    private Montant M_100000 = new Montant(100000);
    private Montant M_DETTE = new Montant(10000);

    private BiensImmobiliersNonHabitable DonneesFinancieresListBase = new BiensImmobiliersNonHabitable();

    public BiensImmobiliersNonHabitableTest() {
        DonneesFinancieresListBase.add(new BienImmobilierNonHabitable(M_1000, M_300000, M_INTERET, M_DETTE,
                BienImmobilierNonHabitableType.FORET, PART_1_2, PROPRIETAIRE, DF_REQ));
        DonneesFinancieresListBase.add(new BienImmobilierNonHabitable(M_1000, M_100000, M_INTERET, M_DETTE,
                BienImmobilierNonHabitableType.FORET, PART_1_2, PROPRIETAIRE, DF_CONJ));
        DonneesFinancieresListBase.add(new BienImmobilierNonHabitable(M_1000, M_100000, M_INTERET, M_DETTE,
                BienImmobilierNonHabitableType.FORET, PART_1_2, USUFRUITIER, DF_ENF));
    }

    @Test
    public void testGetMontantRendementBrut() throws Exception {
        assertEquals(Montant.newAnnuel(3000), DonneesFinancieresListBase.sumMontantRendementBrut());
    }

    @Test
    public void testSumInteretHypotecaire() throws Exception {
        assertEquals(Montant.newAnnuel(60), DonneesFinancieresListBase.sumInteretHypotecaire());
    }

    @Test
    public void testSumMontantRendementBrut() throws Exception {
        assertEquals(Montant.newAnnuel(3000), DonneesFinancieresListBase.sumMontantRendementBrut());
    }

    @Test
    public void testSumMontantRendementPartPropriete() throws Exception {
        assertEquals(Montant.newAnnuel(1500), DonneesFinancieresListBase.sumMontantRendementPartPropriete());
    }

}
