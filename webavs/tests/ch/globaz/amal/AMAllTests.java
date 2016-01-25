/**
 * 
 */
package ch.globaz.amal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ch.globaz.amal.businessimpl.services.libra.SuiteTestsServicesLibra;
import ch.globaz.amal.businessimpl.services.models.SuiteTestsServiceModels;
import ch.globaz.amal.businessimpl.services.pyxis.SuiteTestsServicesPyxis;

/**
 * @author DHI
 * 
 */
@RunWith(Suite.class)
@SuiteClasses(value = { SuiteTestsServiceModels.class, SuiteTestsServicesPyxis.class, SuiteTestsServicesLibra.class })
public class AMAllTests {

}
