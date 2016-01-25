package ch.globaz.pegasus.businessimpl.services.models.decision;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValidationAcCorrectionDroitTestCase;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValidationAcDroitInitialTestCase;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionAcCheckerTestCase;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionUtilsTest;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression.CalculRestitutionTestCase;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression.GenerateOvsForSuppressionTestCase;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression.GeneratePrestationTestCase;

//@formatter:off
@RunWith(Suite.class)
@SuiteClasses({ ValidationAcCorrectionDroitTestCase.class,
				ValidationAcDroitInitialTestCase.class,
				ValiderDecisionAcCheckerTestCase.class,
				ValiderDecisionUtilsTest.class,
				CalculRestitutionTestCase.class,
				GenerateOvsForSuppressionTestCase.class,
				GeneratePrestationTestCase.class
				})
public class AllTests {

}
