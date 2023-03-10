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

        // fin de droit en janvier ==> cas trait?
        rente.setDateFinDroit("01.2012");
        assertFalse(module, entity, "01.2012");

        // fin de droit en d?cembre ==> Plus de rente, cas d?j? trait?.
        rente.setDateFinDroit("12.2011");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void renteEnCoursPasPeriodeEtudesDateEcheanceFutur() {
        // l'enfant a 18 ans dans le mois courant, n'a pas de p?riode d'?tudes et a une date ?ch?ance dans le futur
        entity.setDateNaissanceTiers("01.01.1994");
        rente.setDateEcheance("02.2012");
        assertFalse(module, entity, "01.2012");

        // l'enfant a 18 ans dans le mois courant, n'a pas de p?riode d'?tudes et a une date ?ch?ance dans le mois
        // courant
        rente.setDateEcheance("01.2012");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);

        // l'enfant a d?j? eu 18 ans, n'a pas de p?riode d'?tudes et la date d'?ch?ance de sa rente arrive ? terme dans
        // le mois courant -> c'est une enqu?te interm?diaire
        entity.setDateNaissanceTiers("01.01.1993");
        rente.setDateEcheance("01.2012");
        assertTrue(module, entity, "01.2012", REMotifEcheance.EnqueteIntermediaire);

        // l'enfant a d?j? eu 18 ans, n'a pas de p?riode d'?tudes et la date d'?ch?ance de sa rente n'arrive pas encore
        // ? terme dans le mois courant
        entity.setDateNaissanceTiers("01.01.1993");
        rente.setDateEcheance("02.2012");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void avecPeriodeAntierieureEtDateEcheanceDansLeMoisDeTraitement() {

        // l'enfant a 18 ans dans le mois courant et a une p?riode d'?tude d?pass?e
        entity.getPeriodes().add(
                new REPeriodeEcheances("1", "01.08.2012", "31.07.2013", ISFPeriode.CS_TYPE_PERIODE_ETUDE));
        entity.setDateNaissanceTiers("09.03.1996");
        assertTrue(module, entity, "03.2014", REMotifEcheance.Echeance18ans);
    }

    @Test
    public void avecPeriodeFutureReprennantLeDroit() {

        REPeriodeEcheances periodeFuture = new REPeriodeEcheances("1", "01.02.2012", "31.12.2012",
                ISFPeriode.CS_TYPE_PERIODE_ETUDE);
        entity.getPeriodes().add(periodeFuture);

        // l'enfant a 18 ans dans le mois courant, ? une p?riode d'?tude dans le futur sans trou
        entity.setDateNaissanceTiers("01.01.1994");
        assertFalse(module, entity, "01.2012");

        // l'enfant a 18 ans dans le mois courant, ? une p?riode d'?tude qui reprend le droit
        periodeFuture.setDateDebut("31.01.2012");
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void avecTrouDansLesPeriodes() {

        // l'enfant a 18 ans dans le mois courant, ? une p?riode d'?tude dans le futur (plus loin)
        entity.getPeriodes().add(
                new REPeriodeEcheances("1", "01.03.2012", "31.12.2012", ISFPeriode.CS_TYPE_PERIODE_ETUDE));
        entity.setDateNaissanceTiers("01.01.1994");
        assertTrue(module, entity, "01.2012", REMotifEcheance.Echeance18ans);
    }

    @Test
    public void echeance18ans() {

        // 18 ans dans le mois pr?c?dant
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
