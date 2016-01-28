package ch.globaz.al.businessimpl.services.rubrique.cici;

import junit.framework.Assert;
import org.junit.Before;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesCICIService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.rubrique.RubriqueComptableServiceImplTest;

public class RubriqueComptableCICIServiceImplTest extends RubriqueComptableServiceImplTest {

    protected static RubriquesComptablesCICIService serviceCICI = null;

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp();
        try {
            RubriqueComptableCICIServiceImplTest.serviceCICI = (RubriquesComptablesCICIService) ALImplServiceLocator
                    .getRubriqueComptableService(RubriquesComptablesCICIService.class);
        } catch (Exception e) {
            Assert.fail("Unable to get RubriqueComptablesCICIService, reason:" + e.getMessage());
        }
        // on load les objets sur lesquels s'appluie le plan comptable pour définir la rubrique
        RubriqueComptableServiceImplTest.loadDossierBasic();

    }

}
