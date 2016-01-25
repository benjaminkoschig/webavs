package ch.globaz.perseus.businessimpl.tests.services.calcul;

import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class CalculTestCase {

    // @Override
    // public void setUp() throws Exception {
    // super.setUp();
    // }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.tests.util.BaseTestCase#tearDown()
     */
    // s

    public final void test01ReCalculDemande() throws Exception {
        PCFAccordeeSearchModel searchModel = new PCFAccordeeSearchModel();
        searchModel = PerseusServiceLocator.getPCFAccordeeService().search(searchModel);
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            try {
                PCFAccordee pcfa = (PCFAccordee) model;
                // pcfa.getSimplePCFAccordee().setDateTimeDecisionValidation(pcfa.getSpy().substring(0, 14));
                // PCFAccordee pcfa2 =
                // PerseusServiceLocator.getPCFAccordeeService().calculer(pcfa.getDemande().getId());
                // System.out.println("PCFA num : " + pcfa.getId() + " : " + pcfa.getSimplePCFAccordee().getMontant()
                // + " = " + pcfa2.getSimplePCFAccordee().getMontant());
                // JadeThread.rollbackSession();
                // pcfa.setCalcul(pcfa2.getCalcul());
                // pcfa = PerseusServiceLocator.getPCFAccordeeService().update(pcfa);
                // JadeThread.commitSession();

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        // System.out.println(outputCalcul.getCalcul().get(OutputData.));
    }

}