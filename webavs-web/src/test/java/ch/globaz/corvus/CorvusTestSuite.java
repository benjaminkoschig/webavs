package ch.globaz.corvus;

import globaz.corvus.helpers.acor.RECalculACORDemandeRenteHelperTest;
import globaz.corvus.helpers.decisions.REPreValiderDecisionHelperTest;
import globaz.corvus.helpers.decisions.REPreparerDecisionSpecifiqueHelperTest;
import globaz.corvus.helpers.rentesaccordees.RERenteVerseeATortHelperTest;
import globaz.corvus.process.liste.rentedouble.REAnalyseurRenteDoubleTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ch.globaz.common.domaine.GroupePeriodesTest;
import ch.globaz.common.domaine.PeriodeTest;
import ch.globaz.common.domaine.UniqueIDEqualHashCodeTest;
import ch.globaz.corvus.business.services.models.rentesaccordees.RenteAccordeeServiceTest;
import ch.globaz.corvus.domaine.DecisionTest;
import ch.globaz.corvus.domaine.DemandeRenteTest;
import ch.globaz.corvus.domaine.RepartitionCreanceTest;
import ch.globaz.corvus.domaine.SoldePourRestitutionTest;
import ch.globaz.corvus.process.attestationsfiscales.REAnalyseurLotTest;
import ch.globaz.corvus.process.attestationsfiscales.REAttestationsFiscalesUtilsTest;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleAnalyseEcheanceUtilsTest;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheance18AnsTest;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheance25AnsTest;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceCertificatDeVieTest;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceConjointAgeAvsTest;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceEtudeTest;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceFemmeAgeAvsTest;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceHommeAgeAvsTest;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceRenteDeVeufTest;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheanceRentePourEnfantTest;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleEcheancesForceesTest;
import ch.globaz.corvus.utils.rentesverseesatort.RECalculRentesVerseesATortTest;
import ch.globaz.corvus.utils.rentesverseesatort.REDetailCalculRenteVerseeATortTest;
import ch.globaz.corvus.utils.rentesverseesatort.RELigneDetailCalculRenteVerseeATortTest;
import ch.globaz.prestation.domaine.CodePrestationTest;
import ch.globaz.prestation.domaine.EnTeteBlocageTest;
import ch.globaz.pyxis.domaine.NumeroSecuriteSocialeTest;

/**
 * regroupement de tous les testes unitaires ajoutés par le refactoring récent des rentes
 * 
 * @author PBA
 */
@RunWith(Suite.class)
// @formatter:off
@SuiteClasses({ EnTeteBlocageTest.class,
                CodePrestationTest.class,
                DecisionTest.class,
                DemandeRenteTest.class,
                GroupePeriodesTest.class,
                NumeroSecuriteSocialeTest.class,
                PeriodeTest.class,
                REAnalyseurLotTest.class,
                REAnalyseurRenteDoubleTest.class,
                REAttestationsFiscalesUtilsTest.class,
                RECalculACORDemandeRenteHelperTest.class,
                REModuleAnalyseEcheanceUtilsTest.class,
                REModuleEcheance18AnsTest.class,
                REModuleEcheance25AnsTest.class,
                REModuleEcheanceCertificatDeVieTest.class,
                REModuleEcheanceConjointAgeAvsTest.class,
                REModuleEcheanceEtudeTest.class,
                REModuleEcheanceFemmeAgeAvsTest.class,
                REModuleEcheanceHommeAgeAvsTest.class,
                REModuleEcheanceRenteDeVeufTest.class,
                REModuleEcheanceRentePourEnfantTest.class,
                REModuleEcheancesForceesTest.class,
                RECalculRentesVerseesATortTest.class,
                REDetailCalculRenteVerseeATortTest.class,
                RELigneDetailCalculRenteVerseeATortTest.class,
                RenteAccordeeServiceTest.class,
                RepartitionCreanceTest.class,
                REPreparerDecisionSpecifiqueHelperTest.class,
                REPreValiderDecisionHelperTest.class,
                RERenteVerseeATortHelperTest.class,
                SoldePourRestitutionTest.class,
                UniqueIDEqualHashCodeTest.class })
// @formatter:on
public class CorvusTestSuite {
}
