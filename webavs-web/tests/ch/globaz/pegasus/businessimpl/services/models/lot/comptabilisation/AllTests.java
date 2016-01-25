package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.CompensationInfoRestiutionTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.CompteAnnexeResolverTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.EcritureRequerantConjointPeriodeTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.EricturePeriodeTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GenerateEcrituresCompensationForDecisionAcTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GenerateEcrituresDetteTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GenerateEcrituresDispatcherTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GenerateEcrituresResitutionBeneficiareForDecisionAcTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GenerateOperationsAllocationsNoelTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GenerateOperationsApresCalculTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GenerateOperationsBlocageTest;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GenerateOperationsCreancierTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GenerateOvBeneficiaireTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GeneratePrestationOperationsTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.MontantAdispositionTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.AddAmountJourAppointToOvTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationCheckerTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationLoaderTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationTreatTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationUtilTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.GeneratePerstationPeriodeDecompteTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.SectionRubriqueResolverTestCase;

//@formatter:off
@RunWith(Suite.class)
@SuiteClasses({ CompensationInfoRestiutionTestCase.class,
				CompteAnnexeResolverTestCase.class,
				EcritureRequerantConjointPeriodeTestCase.class,
				EricturePeriodeTestCase.class,
				GenerateEcrituresCompensationForDecisionAcTestCase.class,
				GenerateEcrituresDetteTestCase.class,
				GenerateEcrituresDispatcherTestCase.class,
				GenerateEcrituresResitutionBeneficiareForDecisionAcTestCase.class,
				GenerateOperationsAllocationsNoelTestCase.class,
				GenerateOperationsApresCalculTestCase.class,
				GenerateOperationsCreancierTestCase.class,
				GenerateOperationsBlocageTest.class,
				GenerateOvBeneficiaireTestCase.class,
				GeneratePrestationOperationsTestCase.class,
				MontantAdispositionTestCase.class,
				ComptabilisationCheckerTestCase.class,
				ComptabilisationLoaderTestCase.class,
				ComptabilisationTreatTestCase.class,
				ComptabilisationUtilTestCase.class,
				GeneratePerstationPeriodeDecompteTestCase.class,
				SectionRubriqueResolverTestCase.class,
				AddAmountJourAppointToOvTestCase.class
				})

public class AllTests {}
