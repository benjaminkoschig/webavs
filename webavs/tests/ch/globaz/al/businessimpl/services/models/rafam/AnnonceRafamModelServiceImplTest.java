package ch.globaz.al.businessimpl.services.models.rafam;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALTestCaseJU4;

public class AnnonceRafamModelServiceImplTest extends ALTestCaseJU4 {

    @Test
    @Ignore
    public void testCreate() {
        try {

            AnnonceRafamModel annonce = ALImplServiceLocator.getInitAnnoncesRafamService().initAnnonce68a(
                    ALServiceLocator.getDossierComplexModelService().read("8844"),
                    ALServiceLocator.getDroitComplexModelService().read("61000"), RafamFamilyAllowanceType.FORMATION);

            annonce.setIdAnnonce("12345678912345678");

            ALServiceLocator.getAnnonceRafamModelService().create(annonce);
            Assert.assertNull(JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }
}
