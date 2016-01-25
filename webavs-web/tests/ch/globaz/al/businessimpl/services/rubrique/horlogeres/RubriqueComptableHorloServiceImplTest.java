package ch.globaz.al.businessimpl.services.rubrique.horlogeres;

import junit.framework.Assert;
import org.junit.Before;
import ch.globaz.al.business.services.rubriques.comptables.RubriquesComptablesHorlogeresService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.rubrique.RubriqueComptableServiceImplTest;

public class RubriqueComptableHorloServiceImplTest extends RubriqueComptableServiceImplTest {

    protected static RubriquesComptablesHorlogeresService serviceHorlo = null;

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp();
        try {
            RubriqueComptableHorloServiceImplTest.serviceHorlo = (RubriquesComptablesHorlogeresService) ALImplServiceLocator
                    .getRubriqueComptableService(RubriquesComptablesHorlogeresService.class);
        } catch (Exception e) {
            Assert.fail("Unable to get RubriquesComptablesHorlogeresService, reason:" + e.getMessage());
        }
        // on load les objets sur lesquels s'appluie le plan comptable pour définir la rubrique
        RubriqueComptableServiceImplTest.loadDossierBasic();

    }

}
