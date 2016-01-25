package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BienImmobilierNonHabitableTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BienImmobilierNonHabitableTypeTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable.BiensImmobiliersNonHabitableTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonPrincipale.BienImmobilierNonPrincipaleTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonPrincipale.BiensImmobiliersNonPrincipaleTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale.BienImmobilierServantHabitationPrincipaleTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale.BiensImmobiliersServantHabitationPrincipaleTest;

//@formatter:off
@RunWith(Suite.class)
@SuiteClasses({
     
                BienImmobilierHabitableTypeTest.class,
                BienImmobilierNonHabitableTypeTest.class,
                
                BiensImmobiliersNonHabitableTest.class,
                BienImmobilierNonHabitableTest.class,
                
                BienImmobilierNonPrincipaleTest.class,
                BiensImmobiliersNonPrincipaleTest.class,
                
                BiensImmobiliersServantHabitationPrincipaleTest.class,
                BienImmobilierServantHabitationPrincipaleTest.class,
                
                FraisEntretiensImmeubleTest.class,
                BiensImmobiliersTest.class

            })

public class BienImmobilierTestSuite {

}
