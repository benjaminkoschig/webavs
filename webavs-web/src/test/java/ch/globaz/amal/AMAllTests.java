/**
 *
 */
package ch.globaz.amal;

import ch.globaz.amal.businessimpl.services.libra.SuiteTestsServicesLibra;
import ch.globaz.amal.businessimpl.services.models.SuiteTestsServiceModels;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author DHI
 *
 */
@RunWith(Suite.class)
@SuiteClasses(value = {SuiteTestsServiceModels.class, SuiteTestsServicesLibra.class})
public class AMAllTests {

}
