package ch.globaz.pegasus.businessimpl.services.models;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

// @formatter:off
@RunWith(Suite.class)
@SuiteClasses({ 
	ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.AllTests.class,
	ch.globaz.pegasus.businessimpl.services.models.decision.AllTests.class
	})
public class TestAllComptaBilisationValidation {

}
