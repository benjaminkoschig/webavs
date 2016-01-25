package ch.globaz.al.businessimpl.services.rubrique.cvci;

import junit.framework.Assert;
import org.junit.Before;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCVCIService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.rubrique.RubriqueComptableServiceImplTest;

public class RubriqueComptableCVCIServiceImplTest extends RubriqueComptableServiceImplTest {

    protected static RubriquesComptablesCVCIService serviceCVCI = null;

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp();
        try {
            RubriqueComptableCVCIServiceImplTest.serviceCVCI = (RubriquesComptablesCVCIService) ALImplServiceLocator
                    .getRubriqueComptableService(RubriquesComptablesCVCIService.class);
        } catch (Exception e) {
            Assert.fail("Unable to get RubriquesComptablesCVCIService, reason:" + e.getMessage());
        }
        // on load les objets sur lesquels s'appluie le plan comptable pour définir la rubrique
        RubriqueComptableServiceImplTest.loadDossierBasic();

    }

}
