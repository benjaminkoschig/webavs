package ch.globaz.al.businessimpl.services.rubrique.ccju;

import junit.framework.Assert;
import org.junit.Before;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCCJUService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.rubrique.RubriqueComptableServiceImplTest;

public class RubriqueComptableCCJUServiceImplTest extends RubriqueComptableServiceImplTest {

    protected static RubriquesComptablesCCJUService serviceCCJU = null;

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp();
        try {
            RubriqueComptableCCJUServiceImplTest.serviceCCJU = (RubriquesComptablesCCJUService) ALImplServiceLocator
                    .getRubriqueComptableService(RubriquesComptablesCCJUService.class);
        } catch (Exception e) {
            Assert.fail("Unable to get RubriqueComptablesCCJUService, reason:" + e.getMessage());
        }
        // on load les objets sur lesquels s'appluie le plan comptable pour définir la rubrique
        RubriqueComptableServiceImplTest.loadDossierBasic();

    }

}
