package ch.globaz.pegasus;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ch.globaz.common.domaine.DateTest;
import ch.globaz.common.domaine.MontantTest;
import ch.globaz.common.domaine.MontantTypePeriodeTest;
import ch.globaz.common.domaine.PartTest;
import ch.globaz.common.domaine.TauxTest;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereTestSuite;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresListTest;
import ch.globaz.pegasus.business.domaine.membreFamille.MembresFamillesTestSuite;
import ch.globaz.pegasus.business.domaine.parametre.ParametreTestSuite;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierTypeDeDonneeTest;
import ch.globaz.pegasus.business.domaine.pca.PcaSuite;
import ch.globaz.pegasus.business.domaine.revisionquadriennale.DemandeAReviserTest;
import ch.globaz.pegasus.businessimpl.services.donneeFinanciere.ConvertAllDonneeFinanciereTest;
import ch.globaz.pegasus.businessimpl.services.donneeFinanciere.DonneeFinanciereConverterTest;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.RevisionQuadriennaleLoaderTest;

//@formatter:off
@RunWith(Suite.class)

@SuiteClasses({
    MontantTest.class, 
    DateTest.class,
    TauxTest.class,
    MontantTypePeriodeTest.class, 
    PartTest.class,
    MembresFamillesTestSuite.class,
    DonneeFinanciereConverterTest.class, 
    ConvertAllDonneeFinanciereTest.class, 
    DonneesFinancieresListTest.class,
    DonneeFinanciereTestSuite.class,
    DemandeAReviserTest.class,
    PcaSuite.class,
    RevisionQuadriennaleLoaderTest.class,
    VariableMetierTypeDeDonneeTest.class,
    ParametreTestSuite.class
    })

public class RevisionQuadriennaleAllTestSuit {

}
