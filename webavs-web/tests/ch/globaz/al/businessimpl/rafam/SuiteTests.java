package ch.globaz.al.businessimpl.rafam;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = { AnnonceRafamEnregistrementsTest.class, RafamFamilyAllowanceTypeTest.class,
        AnnonceRafamCreationServiceImplTest.class })
public class SuiteTests {
}