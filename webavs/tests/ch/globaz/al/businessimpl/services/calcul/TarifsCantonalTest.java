package ch.globaz.al.businessimpl.services.calcul;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Classe permettant le test des tarifs se trouvant en base de données. Les cas de test se trouvent dans le fichier
 * tarifsTests.xml à la racine du projet aftest
 * 
 * @author jts
 */
public class TarifsCantonalTest {

    @Ignore
    @Test
    public void testBusinessServiceIsTarifCantonal() {
        try {

            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_AG));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_AI));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_AR));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_BE));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_BL));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_BS));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_FR));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_GE));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_GL));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_GR));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_JU));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_LU));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_NE));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_NW));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_OW));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_SG));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_SH));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_SO));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_SZ));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_TG));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_TI));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_UR));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_VD));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(
                    ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_VS));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_ZG));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_VS));
            Assert.assertTrue(ALServiceLocator.getTarifBusinessService().isTarifCantonal(
                    ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS));
            Assert.assertFalse(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_ALKO));
            Assert.assertFalse(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_CATMP));
            Assert.assertFalse(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_FED));
            Assert.assertFalse(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_FPV_AT));
            Assert.assertFalse(ALServiceLocator.getTarifBusinessService().isTarifCantonal(
                    ALCSTarif.CATEGORIE_FPV_BANQUES));
            Assert.assertFalse(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_LFM13));
            Assert.assertFalse(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_H513));
            Assert.assertFalse(ALServiceLocator.getTarifBusinessService().isTarifCantonal(ALCSTarif.CATEGORIE_LJU));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            // doFinally();
        }
    }
}