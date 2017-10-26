package ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class RenteAvsAiTest {
    public static final RenteAvsAi df = new RenteAvsAi(new Montant(1000), RenteAvsAiType.RENTE_10,
            TypeSansRente.INDEFINIT, BuilderDf.createDF());

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.RENTE_AVS_AI, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testRenteAvsAi() throws Exception {
        assertTrue(df.getMontant().isMensuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(12000), df.computeRevenuAnnuelBrut());
    }

    @Test
    public void testIsSansRenteFalse() throws Exception {
        assertFalse(df.isSansRente());
    }

    @Test
    public void testIsSansRenteTrue() throws Exception {
        RenteAvsAi df1 = new RenteAvsAi(new Montant(1000), RenteAvsAiType.SANS_RENTE, TypeSansRente.INVALIDITE,
                BuilderDf.createDF());
        assertTrue(df1.isSansRente());
    }

}
