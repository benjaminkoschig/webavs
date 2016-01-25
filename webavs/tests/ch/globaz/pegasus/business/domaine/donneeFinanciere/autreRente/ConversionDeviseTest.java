package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.BuilderDf;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangere;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereType;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaiesEtrangere;

public class ConversionDeviseTest {

    @Test
    public void testConvertRenteEtrangere() throws Exception {
        MonnaiesEtrangere monnaiesEtrangere = new MonnaiesEtrangere();

        // MonnaiesEtrangere monnaiesEtrangere = mock(MonnaiesEtrangere.class);
        ConversionDevise conversionDevise = new ConversionDevise(monnaiesEtrangere);
        AutreRente autreRente = new AutreRente(new Montant(100), AutreRenteType.INVALIDITE,
                AutreRenteGenre.RENTE_ETRANGERE, "libelle", MonnaieEtrangereType.EURO, BuilderDf.createDF());

        MonnaieEtrangere monnaieEtrangere = new MonnaieEtrangere();
        monnaieEtrangere.setDateDebut(new Date("01.2015"));
        monnaieEtrangere.setDateFin(null);
        monnaieEtrangere.setTaux(new Taux(1.1));
        monnaieEtrangere.setType(MonnaieEtrangereType.EURO);

        assertEquals(new Montant(110).addAnnuelPeriodicity(), conversionDevise.compute(autreRente, monnaieEtrangere));
    }

    @Test
    public void testConvertRenteEtrangereWithFrancSuisse() throws Exception {
        MonnaiesEtrangere monnaiesEtrangere = new MonnaiesEtrangere();

        // MonnaiesEtrangere monnaiesEtrangere = mock(MonnaiesEtrangere.class);
        ConversionDevise conversionDevise = new ConversionDevise(monnaiesEtrangere);
        AutreRente autreRente = new AutreRente(new Montant(100), AutreRenteType.INVALIDITE,
                AutreRenteGenre.RENTE_ETRANGERE, "libelle", MonnaieEtrangereType.FRANC_SUISSE, BuilderDf.createDF());

        MonnaieEtrangere monnaieEtrangere = new MonnaieEtrangere();
        monnaieEtrangere.setDateDebut(new Date("01.2015"));
        monnaieEtrangere.setDateFin(null);
        monnaieEtrangere.setTaux(new Taux(1.1));
        monnaieEtrangere.setType(MonnaieEtrangereType.FRANC_SUISSE);

        assertEquals(new Montant(100).addAnnuelPeriodicity(), conversionDevise.compute(autreRente, monnaieEtrangere));
    }

    @Test
    public void testConvertRenteAutreRente() throws Exception {
        MonnaiesEtrangere monnaiesEtrangere = new MonnaiesEtrangere();

        // MonnaiesEtrangere monnaiesEtrangere = mock(MonnaiesEtrangere.class);
        ConversionDevise conversionDevise = new ConversionDevise(monnaiesEtrangere);
        AutreRente autreRente = new AutreRente(new Montant(100), AutreRenteType.INVALIDITE, AutreRenteGenre.AUTRES,
                "libelle", MonnaieEtrangereType.INDEFINIT, BuilderDf.createDF());

        MonnaieEtrangere monnaieEtrangere = new MonnaieEtrangere();
        monnaieEtrangere.setDateDebut(new Date("01.2015"));
        monnaieEtrangere.setDateFin(null);
        monnaieEtrangere.setTaux(new Taux(1.1));
        monnaieEtrangere.setType(MonnaieEtrangereType.INDEFINIT);

        assertEquals(new Montant(100).addAnnuelPeriodicity(), conversionDevise.compute(autreRente, monnaieEtrangere));
    }
}
