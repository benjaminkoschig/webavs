package ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class IndemniteJournaliereApgTest {
    private static IndemniteJournaliereApg df = new IndemniteJournaliereApg(new Montant(0), new Montant(5),
            new Montant(20), new Montant(50), IndemniteJournaliereApgGenre.APG, 5, new Taux(1.1), new Taux(5.5),
            "libelle", BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.INDEMNITE_JOURNLIERE_APG, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testIndeminteJournaliereApg() throws Exception {
        assertTrue(df.getMontant().isJouranlier());
        assertTrue(df.getMontantChomage().isJouranlier());
        assertTrue(df.getGainIntermediaireAnnuel().isAnnuel());
        assertTrue(df.getCotisationLpp().isMensuel());
    }

    @Test
    public void testComputeRevenuAnnuelAc() throws Exception {
        IndemniteJournaliereApg dfAc = new IndemniteJournaliereApg(new Montant(40), new Montant(0), new Montant(5),
                new Montant(0), IndemniteJournaliereApgGenre.APG, 5, new Taux(0), new Taux(0), "libelle",
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(200), dfAc.computeRevenuAnnuel());

        IndemniteJournaliereApg dfAc2 = new IndemniteJournaliereApg(new Montant(0), new Montant(40), new Montant(5),
                new Montant(8), IndemniteJournaliereApgGenre.IJ_CHOMAGE, 5, new Taux(1.1), new Taux(5.5), "libelle",
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(9460.144), dfAc2.computeRevenuAnnuel());

    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {

        IndemniteJournaliereApg df1 = new IndemniteJournaliereApg(new Montant(40), new Montant(0), new Montant(0),
                new Montant(0), IndemniteJournaliereApgGenre.APG, 5, new Taux(0), new Taux(0), "libelle",
                BuilderDf.createDF());
        assertEquals(Montant.newAnnuel(200), df1.computeRevenuAnnuel());
    }

}
