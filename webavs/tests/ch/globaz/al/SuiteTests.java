package ch.globaz.al;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = { ch.globaz.al.businessimpl.rafam.SuiteTests.class,
        ch.globaz.al.businessimpl.services.affiliation.RadiationAffilieServiceTest.class,
        ch.globaz.al.businessimpl.services.calcul.SuiteTests.class,
        ch.globaz.al.businessimpl.services.dossiers.RadiationAutomatiqueServiceTest.class,
        ch.globaz.al.businessimpl.services.echeances.SuiteTests.class,
        ch.globaz.al.businessimpl.services.generation.SuiteTests.class,
        ch.globaz.al.businessimpl.services.models.droit.SuiteTests.class,
        ch.globaz.al.businessimpl.services.models.rafam.SuiteTests.class,
        ch.globaz.al.businessimpl.services.rafam.SuiteTests.class,
        ch.globaz.al.businessimpl.services.dossiers.SuiteTests.class })
public class SuiteTests {
}