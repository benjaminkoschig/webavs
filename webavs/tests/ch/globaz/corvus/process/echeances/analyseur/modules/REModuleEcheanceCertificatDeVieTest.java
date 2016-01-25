package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.REPeriodeEcheances;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.hera.business.constantes.ISFPeriode;

public class REModuleEcheanceCertificatDeVieTest extends REModuleAnalyseEcheanceTest {

    public REModuleEcheanceCertificatDeVieTest() {
        super();
    }

    @Test
    public void certificatDeViePrennantFin() {

    }

    @Before
    public void setUp() {
        module = new REModuleEcheanceCertificatDeVie(session, "09.2011");

        entity = new REEcheancesEntity();
    }

    @Test
    public void testModuleCertificatDeVie() {

        // test avec une période finissant le mois précédant
        entity.getPeriodes().add(
                new REPeriodeEcheances("0", "0", "31.08.2011", ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE));
        assertFalse(module, entity, "09.2011");
        entity.getPeriodes().clear();

        // test avec une période finissant au début du mois de traitement
        entity.getPeriodes().add(
                new REPeriodeEcheances("1", "0", "01.09.2011", ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE));
        assertTrue(module, entity, "09.2011", REMotifEcheance.CertificatDeVie);
        entity.getPeriodes().clear();

        // test avec une période finissant à la fin du mois de traitement
        entity.getPeriodes().add(
                new REPeriodeEcheances("2", "0", "30.09.2011", ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE));
        assertTrue(module, entity, "09.2011", REMotifEcheance.CertificatDeVie);
        entity.getPeriodes().clear();

        // test avec une période d'étude finissant au début du mois de traitement
        entity.getPeriodes().add(new REPeriodeEcheances("1", "0", "01.09.2011", ISFPeriode.CS_TYPE_PERIODE_ETUDE));
        assertFalse(module, entity, "09.2011");
        entity.getPeriodes().clear();

        // test avec une période finissant au début du mois suivant
        entity.getPeriodes().add(
                new REPeriodeEcheances("3", "0", "01.10.2011", ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE));
        assertFalse(module, entity, "09.2011");
        entity.getPeriodes().clear();

        // test avec une période finissant dans le mois courant avec une date invalide
        entity.getPeriodes().add(
                new REPeriodeEcheances("4", "0", "31.09.2011", ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE));
        assertFalse(module, entity, "09.2011");
        entity.getPeriodes().clear();
    }
}
