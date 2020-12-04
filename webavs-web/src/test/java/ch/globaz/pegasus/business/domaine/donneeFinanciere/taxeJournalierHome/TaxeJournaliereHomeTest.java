package ch.globaz.pegasus.business.domaine.donneeFinanciere.taxeJournalierHome;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class TaxeJournaliereHomeTest {
    private static final TaxeJournaliereHome df = new TaxeJournaliereHome(new Montant(100), new Montant(50), true,
            new Date("01.01.2015"), "1", new Montant(10), new Montant(0), BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.TAXE_JOURNALIERE_HOME, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testTaxeJournaliereHome() throws Exception {
        assertTrue(df.getMontantJournalierLca().isJouranlier());
        assertTrue(df.getPrimeAPayer().isMensuel());
    }

}
