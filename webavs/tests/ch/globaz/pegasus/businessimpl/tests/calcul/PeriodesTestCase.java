/**
 * 
 */
package ch.globaz.pegasus.businessimpl.tests.calcul;

import static org.junit.Assert.*;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import org.junit.After;
import ch.globaz.pegasus.business.constantes.ConstantesCalcul;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesDroitSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.tests.util.BaseTestCase;
import ch.globaz.pegasus.tests.util.TestBaseData;
import ch.globaz.pegasus.tests.util.calcul.CalculCasTest;

/**
 * @author ECO
 * 
 */
public class PeriodesTestCase {

    /**
     * utilitaire pour manipuler le droit de test
     * 
     * @author ECO
     * 
     */
    class DroitModifier {

        void changeDateAnnonce(String newDate) throws DroitException, JadeApplicationServiceNotAvailableException,
                JadePersistenceException {
            droit.getSimpleVersionDroit().setDateAnnonce(newDate);
            PegasusImplServiceLocator.getSimpleVersionDroitService().update(droit.getSimpleVersionDroit());
        }

    }

    private Droit droit;

    private CalculDonneesDroitSearch getDonneesDroit(Map<String, JadeAbstractSearchModel> cacheDonneesCalcul) {
        CalculDonneesDroitSearch droitSearchModel = (CalculDonneesDroitSearch) cacheDonneesCalcul
                .get(ConstantesCalcul.CONTAINER_DONNEES_DROIT);
        return droitSearchModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.tests.util.BaseTestCase#setUp()
     */

    public void setUp() throws Exception {

        BaseTestCase.setUp();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.tests.util.BaseTestCase#tearDown()
     */
    @After
    public void tearDown() {
        // genere erreur pour provoquer un rollback de la db
        try {
            JadeThread.rollbackSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseTestCase.tearDown();
    }

    /**
     * Test method for
     * {@link ch.globaz.pegasus.businessimpl.services.models.calcul.PeriodesServiceImpl#recherchePeriodesCalcul(ch.globaz.pegasus.business.models.droit.Droit, java.lang.String, java.util.Map)}
     * .
     * 
     * @throws Exception
     */

    public final void testRecherchePeriodesCalcul() throws Exception {

        CalculCasTest casTest = (CalculCasTest) TestBaseData.casForTest.get("756.1234.4366.81");
        casTest.setRenteAvsAi(CalculTestsUtil.createRentes(casTest, "1000", "01.01.2011"));

        DonneesHorsDroitsProvider containerGlobal = DonneesHorsDroitsProvider.getInstance();
        containerGlobal.init();
        Map<String, JadeAbstractSearchModel> cacheDonneesCalcul = PegasusImplServiceLocator.getPeriodesService()
                .getDonneesCalculDroit(casTest.getDroit(), "01.2011", "");
        List<PeriodePCAccordee> listePerPCA = PegasusImplServiceLocator.getPeriodesService().recherchePeriodesCalcul(
                casTest.getDroit(), "01.2011", "", cacheDonneesCalcul, containerGlobal, false);
        casTest = CalculTestsUtil.calculVersionDroit(casTest);

        assertNotNull(listePerPCA);
        assertEquals(1, listePerPCA.size());
        PeriodePCAccordee periode = listePerPCA.get(0);
        assertEquals("01.2011", periode.getStrDateDebut());
        assertEquals("", periode.getStrDateFin());

    }

}
