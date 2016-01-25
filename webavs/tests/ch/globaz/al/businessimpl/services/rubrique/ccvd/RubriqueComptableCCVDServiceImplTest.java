package ch.globaz.al.businessimpl.services.rubrique.ccvd;

import junit.framework.Assert;
import org.junit.Before;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCCVDService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.rubrique.RubriqueComptableServiceImplTest;

public class RubriqueComptableCCVDServiceImplTest extends RubriqueComptableServiceImplTest {

    protected static RubriquesComptablesCCVDService serviceCCVD = null;

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp();
        try {
            RubriqueComptableCCVDServiceImplTest.serviceCCVD = (RubriquesComptablesCCVDService) ALImplServiceLocator
                    .getRubriqueComptableService(RubriquesComptablesCCVDService.class);
        } catch (Exception e) {
            Assert.fail("Unable to get RubriqueComptablesCCVDService, reason:" + e.getMessage());
        }
        // on load les objets sur lesquels s'appluie le plan comptable pour définir la rubrique
        RubriqueComptableServiceImplTest.loadDossierBasic();

    }

}
