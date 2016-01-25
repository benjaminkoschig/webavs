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

        // test avec une p�riode finissant le mois pr�c�dant
        entity.getPeriodes().add(
                new REPeriodeEcheances("0", "0", "31.08.2011", ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE));
        assertFalse(module, entity, "09.2011");
        entity.getPeriodes().clear();

        // test avec une p�riode finissant au d�but du mois de traitement
        entity.getPeriodes().add(
                new REPeriodeEcheances("1", "0", "01.09.2011", ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE));
        assertTrue(module, entity, "09.2011", REMotifEcheance.CertificatDeVie);
        entity.getPeriodes().clear();

        // test avec une p�riode finissant � la fin du mois de traitement
        entity.getPeriodes().add(
                new REPeriodeEcheances("2", "0", "30.09.2011", ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE));
        assertTrue(module, entity, "09.2011", REMotifEcheance.CertificatDeVie);
        entity.getPeriodes().clear();

        // test avec une p�riode d'�tude finissant au d�but du mois de traitement
        entity.getPeriodes().add(new REPeriodeEcheances("1", "0", "01.09.2011", ISFPeriode.CS_TYPE_PERIODE_ETUDE));
        assertFalse(module, entity, "09.2011");
        entity.getPeriodes().clear();

        // test avec une p�riode finissant au d�but du mois suivant
        entity.getPeriodes().add(
                new REPeriodeEcheances("3", "0", "01.10.2011", ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE));
        assertFalse(module, entity, "09.2011");
        entity.getPeriodes().clear();

        // test avec une p�riode finissant dans le mois courant avec une date invalide
        entity.getPeriodes().add(
                new REPeriodeEcheances("4", "0", "31.09.2011", ISFPeriode.CS_TYPE_PERIODE_CERTIFICAT_DE_VIE));
        assertFalse(module, entity, "09.2011");
        entity.getPeriodes().clear();
    }
}
