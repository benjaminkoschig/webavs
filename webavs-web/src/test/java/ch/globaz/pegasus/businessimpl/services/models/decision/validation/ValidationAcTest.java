package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//@formatter:off
@RunWith(Suite.class)
@SuiteClasses({ ValidationAcCorrectionDroitTestCase.class,
				ValidationAcDroitInitialTestCase.class,
				ValiderDecisionAcCheckerTestCase.class,
				ValiderDecisionUtilsTest.class,
				EtatDemandeResolverTestCase.class
		        })
public class ValidationAcTest {

}
