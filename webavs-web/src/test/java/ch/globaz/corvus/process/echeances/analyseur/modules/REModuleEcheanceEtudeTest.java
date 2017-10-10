package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.REPeriodeEcheances;
import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import globaz.pyxis.api.ITIPersonne;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.hera.business.constantes.ISFPeriode;

public class REModuleEcheanceEtudeTest extends REModuleAnalyseEcheanceTest {

    private RERenteJoinDemandeEcheance rente;

    public REModuleEcheanceEtudeTest() {
        super();
    }

    @Test
    public void echeanceForceeEnqueteIntermediaire() {
        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode.setDateDebut("01.10.2010");
        periode.setDateFin("31.12.2011");
        entity.getPeriodes().add(periode);

        // Date d'échéance de la rente < fin de période d'étude
        // Date d'échéance de la rente < mois de traitement
        // Date de fin période < mois de traitement
        // -> fin d'étude dépassée
        periode.setDateFin("31.12.2011");
        rente.setDateEcheance("11.2011");
        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceFinEtudesDepassees);

        // Date d'échéance de la rente = date de fin période
        // Date d'échéance de la rente < mois de traitement
        // Date de fin période < mois de traitement
        // -> fin d'étude dépassée
        periode.setDateFin("31.12.2011");
        rente.setDateEcheance("12.2011");
        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceFinEtudesDepassees);

        // Date d'échéance de la rente > date de fin période
        // Date d'échéance de la rente = mois de traitement
        // Date de fin période < mois de traitement
        // -> enquête intermédiaire
        periode.setDateFin("31.12.2011");
        rente.setDateEcheance("01.2012");
        assertTrue(module, entity, "01.2012", REMotifEcheance.EnqueteIntermediaire);

        // Date d'échéance de la rente > date de fin période
        // Date d'échéance de la rente > mois de traitement
        // Date de fin période < mois de traitement
        // -> rien
        periode.setDateFin("31.12.2011");
        rente.setDateEcheance("02.2012");
        assertFalse(module, entity, "01.2012");

        // Date d'échéance de la rente > date de fin période
        // Date d'échéance de la rente = mois de traitement
        // Date de fin période < mois de traitement
        // -> rien
        periode.setDateFin("31.12.2011");
        rente.setDateEcheance("02.2012");
        assertFalse(module, entity, "01.2012");

        // Date d'échéance de la rente < date de fin période
        // Date d'échéance de la rente < mois de traitement
        // Date de fin période = mois de traitement
        // -> fin d'étude
        periode.setDateFin("01.01.2012");
        rente.setDateEcheance("12.2011");
        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceFinEtudes);

        // Date d'échéance de la rente = date de fin période
        // Date d'échéance de la rente = mois de traitement
        // Date de fin période = mois de traitement
        // -> fin d'étude
        periode.setDateFin("01.01.2012");
        rente.setDateEcheance("01.2012");
        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceFinEtudes);

        // Date d'échéance de la rente > date de fin période
        // Date d'échéance de la rente > mois de traitement
        // Date fin période = mois de traitement
        // -> rien
        periode.setDateFin("01.01.2012");
        rente.setDateEcheance("02.2012");
        assertFalse(module, entity, "01.2012");

        // Date d'échéance de la rente < date de fin période
        // Date d'échéance de la rente < mois de traitement
        // Date fin période > mois de traitement
        // -> rien
        periode.setDateFin("01.02.2012");
        rente.setDateEcheance("12.2011");
        assertFalse(module, entity, "01.2012");

        // Date d'échéance de la rente = date de fin période
        // Date d'échéance de la rente = mois de traitement
        // Date fin période > mois de traitement
        // -> échéance forcée
        periode.setDateFin("01.02.2012");
        rente.setDateEcheance("01.2012");
        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceForcee);

        // Date d'échéance de la rente > date de fin période
        // Date d'échéance de la rente > mois de traitement
        // Date fin période > mois de traitement
        // -> rien
        periode.setDateFin("01.02.2012");
        rente.setDateEcheance("02.2012");
        assertFalse(module, entity, "01.2012");

    }

    @Test
    public void multipleRentePourLeMemeBeneficiaire() {
        rente.setDateFinDroit("03.2012");

        RERenteJoinDemandeEcheance rente2 = new RERenteJoinDemandeEcheance();
        rente2.setIdPrestationAccordee("2");
        rente2.setCodePrestation("15");
        rente2.setDateDebutDroit("04.2012");
        entity.getRentesDuTiers().add(rente2);

        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceEtudesAucunePeriode);
        // check de la rente passé en réponse
        REReponseModuleAnalyseEcheance reponse = module.eval(entity);
        Assert.assertEquals("Mauvaise rente prise en commpte", rente2.getIdPrestationAccordee(), reponse.getRente()
                .getIdPrestationAccordee());
    }

    @Test
    public void periodeAutreQueEtude() {
        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE);
        periode.setDateDebut("01.2011");
        periode.setDateFin("01.2012");
        entity.getPeriodes().add(periode);

        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void periodeEtudeFinissantApresMoisTraitement() {
        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode.setDateDebut("01.2011");
        periode.setDateFin("02.2012");
        entity.getPeriodes().add(periode);

        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void periodeEtudeFinissantAvantMoisTraitement() {
        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode.setDateDebut("01.2011");
        periode.setDateFin("12.2011");
        entity.getPeriodes().add(periode);

        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceFinEtudesDepassees);
    }

    @Test
    public void periodeEtudeFinissantDansMoisTraitement() {
        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode.setDateDebut("01.2011");
        periode.setDateFin("01.2012");
        entity.getPeriodes().add(periode);

        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceFinEtudes);
    }

    @Test
    public void periodeEtudeFuture() {
        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode.setDateDebut("02.2012");
        periode.setDateFin("12.2012");
        entity.getPeriodes().add(periode);

        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceEtudesAucunePeriode);
    }

    @Test
    public void periodeEtudeReprennantDroit() {
        REPeriodeEcheances periode1 = new REPeriodeEcheances();
        periode1.setIdPeriode("1");
        periode1.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode1.setDateDebut("01.2011");
        periode1.setDateFin("01.2012");
        entity.getPeriodes().add(periode1);

        REPeriodeEcheances periode2 = new REPeriodeEcheances();
        periode2.setIdPeriode("2");
        periode2.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode2.setDateDebut("02.2012");
        periode2.setDateFin("12.2012");
        entity.getPeriodes().add(periode2);

        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void periodeEtudeSansDateDebut() {
        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode.setDateFin("01.2012");
        entity.getPeriodes().add(periode);

        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceFinEtudes);
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

    @Test
    public void periodeEtudeSansDateDeFin() {
        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode.setDateDebut("01.2011");
        entity.getPeriodes().add(periode);

        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void renteDejaDiminuee() {
        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode.setDateDebut("01.2011");
        periode.setDateFin("01.2012");

        entity.getPeriodes().add(periode);

        entity.setDateNaissanceTiers("01.01.1992");
        rente.setDateFinDroit("01.2012");
        rente.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);

        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void sansPeriodeEtude() {
        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceEtudesAucunePeriode);
    }

    @Before
    public void setUp() {
        module = new REModuleEcheanceEtude(session, "01.2012");

        entity = new REEcheancesEntity();
        entity.setCsSexeTiers(ITIPersonne.CS_HOMME);
        entity.setDateNaissanceTiers("01.01.1990");

        rente = new RERenteJoinDemandeEcheance();
        rente.setIdPrestationAccordee("1");
        rente.setCodePrestation("54");
        rente.setDateDebutDroit("01.2011");
        entity.getRentesDuTiers().add(rente);
    }

    @Test
    public void tiers18ansAvecPeriodeAvecPeriodeEtudeEchueEtDatEcheanceDansLeMois() {

        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode.setDateDebut("01.08.2012");
        periode.setDateFin("31.07.2013");
        entity.getPeriodes().add(periode);

        // 18 ans en mars 2014
        entity.setDateNaissanceTiers("09.03.1996");
        // date d'échéance en mars 2014
        rente.setDateEcheance("03.2014");

        // 18 ans + 1 mois : motif sans période d'étude
        assertTrue(module, entity, "04.2014", REMotifEcheance.EcheanceFinEtudesDepassees);
    }

    @Test
    public void tiersTropJeune() {
        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode.setDateDebut("01.2011");
        periode.setDateFin("01.2012");
        entity.getPeriodes().add(periode);

        // tiers ayant 18ans dans le mois de traitement (traité par un autre module)
        entity.setDateNaissanceTiers("01.01.1994");
        assertFalse(module, entity, "01.2012");

        // tiers ayant moins de 18ans dans le mois de traitement (cas pas traité)
        entity.setDateNaissanceTiers("01.02.1994");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void tiersTropVieux() {
        REPeriodeEcheances periode = new REPeriodeEcheances();
        periode.setIdPeriode("1");
        periode.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode.setDateDebut("01.2011");
        periode.setDateFin("01.2012");
        entity.getPeriodes().add(periode);

        // tiers ayant 25ans dans le mois de traitement (traité par un autre module)
        entity.setDateNaissanceTiers("01.01.1987");
        assertFalse(module, entity, "01.2012");

        // tiers ayant plus de 25ans dans le mois de traitement (traité par un autre module)
        entity.setDateNaissanceTiers("31.12.1986");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void trouDansLesPeriodesEtude() {
        REPeriodeEcheances periode1 = new REPeriodeEcheances();
        periode1.setIdPeriode("1");
        periode1.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode1.setDateDebut("01.2011");
        periode1.setDateFin("01.2012");
        entity.getPeriodes().add(periode1);

        REPeriodeEcheances periode2 = new REPeriodeEcheances();
        periode2.setIdPeriode("2");
        periode2.setCsTypePeriode(ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        periode2.setDateDebut("03.2012");
        periode2.setDateFin("12.2012");
        entity.getPeriodes().add(periode2);

        assertTrue(module, entity, "01.2012", REMotifEcheance.EcheanceFinEtudes);
    }
}
