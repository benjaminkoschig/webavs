package ch.globaz.al.test.businessimpl.service.prestation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite de test pour les prestations
 * 
 * @author pta
 * 
 */
@RunWith(Suite.class)
@SuiteClasses(value = { PrestationBusinessServiceTest.class,
        RecapitulatifEntrepriseImpressionComplexModelServiceTest.class })
public class SuiteTestsPrestation {

}
