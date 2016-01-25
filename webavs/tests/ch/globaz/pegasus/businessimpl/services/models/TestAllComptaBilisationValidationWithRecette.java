package ch.globaz.pegasus.businessimpl.services.models;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionAcLoaderTestCase;

@RunWith(Suite.class)
@SuiteClasses({ TestAllComptaBilisationValidation.class, TestDeRecetteValidationComptablication.class,
        ValiderDecisionAcLoaderTestCase.class })
public class TestAllComptaBilisationValidationWithRecette {

}
