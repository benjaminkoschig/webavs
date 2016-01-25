package ch.globaz.al.businessimpl.services.rubrique.fve;

import junit.framework.Assert;
import org.junit.Before;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesFVEService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.rubrique.RubriqueComptableServiceImplTest;

public class RubriqueComptableFVEServiceImplTest extends RubriqueComptableServiceImplTest {

    protected static RubriquesComptablesFVEService serviceFVE = null;

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp();
        try {
            RubriqueComptableFVEServiceImplTest.serviceFVE = (RubriquesComptablesFVEService) ALImplServiceLocator
                    .getRubriqueComptableService(RubriquesComptablesFVEService.class);
        } catch (Exception e) {
            Assert.fail("Unable to get RubriquesComptablesFVEService, reason:" + e.getMessage());
        }
        // on load les objets sur lesquels s'appluie le plan comptable pour définir la rubrique
        RubriqueComptableServiceImplTest.loadDossierBasic();

    }

}
