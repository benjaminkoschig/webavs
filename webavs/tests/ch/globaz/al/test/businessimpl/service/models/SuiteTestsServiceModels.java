package ch.globaz.al.test.businessimpl.service.models;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ch.globaz.al.test.businessimpl.service.models.allocataire.SuiteTestsAllocataire;
import ch.globaz.al.test.businessimpl.service.models.dossier.SuiteTestsDossier;
import ch.globaz.al.test.businessimpl.service.models.prestation.SuiteTestsPrestation;
import ch.globaz.al.test.businessimpl.service.models.tarif.SuiteTestsTarif;

/**
 * Suite de test pour les modèles
 * 
 * @author jts
 * 
 */
@RunWith(Suite.class)
@SuiteClasses(value = { SuiteTestsAllocataire.class, SuiteTestsDossier.class, SuiteTestsPrestation.class,
        SuiteTestsTarif.class })
public class SuiteTestsServiceModels {

}
