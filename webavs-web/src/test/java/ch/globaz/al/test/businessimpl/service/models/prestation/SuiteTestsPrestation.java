package ch.globaz.al.test.businessimpl.service.models.prestation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite de tests liés au modèles des prestations
 * 
 * @author jts
 * 
 */
@RunWith(Suite.class)
@SuiteClasses(value = { EntetePrestationModelServiceImplTest.class, RecapitulatifEntrepriseModelServiceImplTest.class,
        DetailPrestationModelServiceImplTest.class, TransfertTucanaModelServiceImplTest.class })
public class SuiteTestsPrestation {

}
