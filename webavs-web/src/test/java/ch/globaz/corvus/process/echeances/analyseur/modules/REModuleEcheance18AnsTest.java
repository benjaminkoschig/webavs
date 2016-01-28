package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.REPeriodeEcheances;
import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.hera.business.constantes.ISFPeriode;

public class REModuleEcheance18AnsTest extends REModuleAnalyseEcheanceTest {

    private RERenteJoinDemandeEcheance rente;

    public REModuleEcheance18AnsTest() {
        super();
    }

    @Test
    public void avecDateDeFinDeDroit() {
        entity.setDateNaissanceTiers("01.01.1994");

        // 18 ans en janvier et rente en cours ==> motif de sortie
        rente.setDateFinDroit("02.2012");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        // fin de droit en janvier ==> cas traité
        rente.setDateFinDroit("01.2012");
        assertFalse(module, entity, "01.2012");

        // fin de droit en décembre ==> Plus de rente, cas déjà traité.
        rente.setDateFinDroit("12.2011");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void avecPeriodeAntierieureEtDateEcheanceDansLeMoisDeTraitement() {

        REPeriodeEcheances periodeAnterieure = new REPeriodeEcheances("1", "01.08.2012", "31.07.2013",
                ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        entity.getPeriodes().add(periodeAnterieure);

        entity.setDateNaissanceTiers("09.03.1996");

        assertTrue(module, entity, "03.2014", REMotifEcheance.Echeance18ans);
    }

    @Test
    public void avecPeriodeFutureReprennantLeDroit() {

        REPeriodeEcheances periodeFuture = new REPeriodeEcheances("1", "01.02.2012", "31.12.2012",
                ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        entity.getPeriodes().add(periodeFuture);

        // 18 ans
        entity.setDateNaissanceTiers("01.01.1994");

        assertFalse(module, entity, "01.2012");

        periodeFuture.setDateDebut("31.01.2012");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void avecTrouDansLesPeriodes() {

        REPeriodeEcheances periodeFuture = new REPeriodeEcheances("1", "01.03.2012", "31.12.2012",
                ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        entity.getPeriodes().add(periodeFuture);

        // 18 ans
        entity.setDateNaissanceTiers("01.01.1994");

        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);
    }

    @Test
    public void echeance18ans() {

        // 18 ans dans le mois précédant
        entity.setDateNaissanceTiers("31.12.1993");
        assertFalse(module, entity, "01.2012");

        // 18 ans dans le mois de traitement
        entity.setDateNaissanceTiers("01.01.1994");

        // tous les type de rente pour enfant
        rente.setCodePrestation("14");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        rente.setCodePrestation("15");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        rente.setCodePrestation("34");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        rente.setCodePrestation("35");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        rente.setCodePrestation("44");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        rente.setCodePrestation("45");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        rente.setCodePrestation("55");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        rente.setCodePrestation("56");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        rente.setCodePrestation("75");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        rente.setCodePrestation("76");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        // 18 ans avec une rente principale -> faux
        rente.setCodePrestation("50");
        entity.setDateNaissanceTiers("01.09.1993");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void periodeEtudeSansDateDeDebutEt18AnsDansLeMois() throws Exception {

        REPeriodeEcheances periodeSansDateDeDebut = new REPeriodeEcheances("1", "", "31.07.2014",
                ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        entity.getPeriodes().add(periodeSansDateDeDebut);

        entity.setDateNaissanceTiers("20.06.1996");
        rente.setCodePrestation("54");

        assertFalse(module, entity, "06.2014");
    }

    @Before
    public void setUp() {
        module = new REModuleEcheance18Ans(session, "01.2012");

        entity = new REEcheancesEntity();

        rente = new RERenteJoinDemandeEcheance();
        rente.setIdPrestationAccordee("1");
        rente.setCodePrestation("54");
        entity.getRentesDuTiers().add(rente);
    }
}
