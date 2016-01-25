package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereType;

public class AutreRenteTest {
    private static AutreRente df = new AutreRente(new Montant(50), AutreRenteType.INVALIDITE, AutreRenteGenre.AUTRES,
            "test", MonnaieEtrangereType.INDEFINIT, BuilderDf.createDF());

    @Test
    public void testAutreRente() throws Exception {
        assertTrue(df.getMontant().isAnnuel());
    }

    @Test
    public void testDefinedTypeDonneeFinanciere() throws Exception {
        assertEquals(DonneeFinanciereType.AUTRE_RENTE, df.getTypeDonneeFinanciere());
    }

    @Test
    public void testComputeRevenuAnnuel() throws Exception {
        assertEquals(Montant.newAnnuel(50), df.computeRevenuAnnuel());
    }

    @Test
    public void testComputeRevenuAnnuelBrut() throws Exception {
        assertEquals(Montant.newAnnuel(50), df.computeRevenuAnnuel());
    }

    @Test
    public void testMustConvertToFrancSuisseTrue() throws Exception {

        AutreRente autreRente = new AutreRente(new Montant(100), AutreRenteType.INVALIDITE,
                AutreRenteGenre.RENTE_ETRANGERE, "libelle", MonnaieEtrangereType.EURO, BuilderDf.createDF());

        assertTrue(autreRente.mustConvertToFrancSuisse());
    }

    @Test
    public void testMustConvertToFrancSuisseFalseFrancSuisse() throws Exception {

        AutreRente autreRente = new AutreRente(new Montant(100), AutreRenteType.INVALIDITE,
                AutreRenteGenre.RENTE_ETRANGERE, "libelle", MonnaieEtrangereType.FRANC_SUISSE, BuilderDf.createDF());

        assertFalse(autreRente.mustConvertToFrancSuisse());
    }

    @Test
    public void testMustConvertToFrancSuisseFalseAutreRente() throws Exception {

        AutreRente autreRente = new AutreRente(new Montant(100), AutreRenteType.INVALIDITE, AutreRenteGenre.AUTRES,
                "libelle", MonnaieEtrangereType.EURO, BuilderDf.createDF());

        assertFalse(autreRente.mustConvertToFrancSuisse());
    }

    @Test
    public void testMustConvertToFrancSuisseFalseAutreRenteFrancSuisse() throws Exception {

        AutreRente autreRente = new AutreRente(new Montant(100), AutreRenteType.INVALIDITE, AutreRenteGenre.AUTRES,
                "libelle", MonnaieEtrangereType.FRANC_SUISSE, BuilderDf.createDF());

        assertFalse(autreRente.mustConvertToFrancSuisse());
    }
}
