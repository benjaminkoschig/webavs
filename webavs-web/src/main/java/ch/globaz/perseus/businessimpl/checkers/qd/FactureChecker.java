/**
 * 
 */
package ch.globaz.perseus.businessimpl.checkers.qd;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author DDE
 * 
 */
public class FactureChecker extends PerseusAbstractChecker {

    /**
     * @param facture
     * @throws Exception
     */
    public static void checkForCreate(Facture facture) throws Exception {
        FactureChecker.checkQD(facture);
    }

    /**
     * @param facture
     */
    public static void checkForDelete(Facture facture) {

    }

    /**
     * @param facture
     */
    public static void checkForUpdate(Facture facture) {

    }

    private static void checkQD(Facture facture) throws Exception {
        float qdMax = PerseusServiceLocator.getQDService().getMontantMaximalRemboursable(facture.getQd());

        if (!JadeStringUtil.isEmpty(facture.getSimpleFacture().getMontantRembourse())
                && !JadeStringUtil.isEmpty(facture.getSimpleFacture().getMontant())) {
            if (Double.parseDouble(facture.getSimpleFacture().getMontantRembourse()) > qdMax) {
                JadeThread.logError(FactureChecker.class.getName(), "perseus.facture.qd.montanttropfaible");
            }
            if (Float.parseFloat(facture.getSimpleFacture().getMontantRembourse()) > Float.parseFloat(facture
                    .getSimpleFacture().getMontant())) {
                JadeThread.logError(FactureChecker.class.getName(), "perseus.facture.montants.incoherents");
            }
        }
    }

    public static void checkNumReferenceFacture(Facture facture) throws JadePersistenceException,
            JadeApplicationException {
        if (!JadeStringUtil.isEmpty(facture.getSimpleFacture().getNumRefFacture())) {
            boolean bvrOk = true;
            boolean ccpOk = true;

            String bvrReturn = PerseusServiceLocator.getBVRService().validationNumeroBVR(
                    facture.getSimpleFacture().getNumRefFacture());

            if (bvrReturn.equals("")) {
                bvrOk = false;
            } else {
                facture.getSimpleFacture().setNumRefFacture(bvrReturn);
            }

            if (JadeStringUtil.isEmpty(facture.getSimpleFacture().getIdTiersAdressePaiement())
                    || JadeStringUtil.isEmpty(facture.getSimpleFacture().getIdApplicationAdressePaiement())) {
                ccpOk = false;
            } else if (!PerseusServiceLocator.getBVRService().validationCCP(
                    facture.getSimpleFacture().getIdTiersAdressePaiement(),
                    facture.getSimpleFacture().getIdApplicationAdressePaiement())) {
                ccpOk = false;
            }

            if (!bvrOk && !ccpOk) {
                JadeThread
                        .logError(FactureChecker.class.getName(), "perseus.facture.numero.reference.et.ccp.incorrect");
            } else if (!bvrOk) {
                JadeThread.logError(FactureChecker.class.getName(), "perseus.facture.numero.reference.incorrect");
            } else if (!ccpOk) {
                JadeThread.logError(FactureChecker.class.getName(), "perseus.facture.ccp.incorrect");
            }
        }
    }

}
