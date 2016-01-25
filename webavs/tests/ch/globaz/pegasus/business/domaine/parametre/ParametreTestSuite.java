package ch.globaz.pegasus.business.domaine.parametre;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereTypeTest;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierTest;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierTypeDeDonneeTest;

//@formatter:off
@RunWith(Suite.class)

@SuiteClasses({
    MonnaieEtrangereTypeTest.class,
    VariableMetierTypeDeDonneeTest.class,
    VariableMetierTest.class,
    MapWithListSortedByDateTest.class
})
public class ParametreTestSuite {

}
