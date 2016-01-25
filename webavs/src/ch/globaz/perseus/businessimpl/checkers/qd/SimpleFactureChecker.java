/**
 * 
 */
package ch.globaz.perseus.businessimpl.checkers.qd;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.models.qd.SimpleFacture;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author DDE
 * 
 */
public class SimpleFactureChecker extends PerseusAbstractChecker {

    /**
     * @param simpleFacture
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PerseusException
     */
    public static void checkForCreate(SimpleFacture simpleFacture) throws JadePersistenceException, PerseusException,
            JadeApplicationServiceNotAvailableException {
        SimpleFactureChecker.checkMandatory(simpleFacture);
        if (!PerseusAbstractChecker.threadOnError()) {
            SimpleFactureChecker.checkIntegrity(simpleFacture);
        }
    }

    /**
     * @param simpleFacture
     */
    public static void checkForDelete(SimpleFacture simpleFacture) {

    }

    /**
     * @param simpleFacture
     */
    public static void checkForUpdate(SimpleFacture simpleFacture) {
        SimpleFactureChecker.checkMandatory(simpleFacture);
    }

    /**
     * @param simpleFacture
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PerseusException
     */
    public static void checkIntegrity(SimpleFacture simpleFacture) throws JadePersistenceException, PerseusException,
            JadeApplicationServiceNotAvailableException {

        // BZ 7894 et 8397
        // Utilisation de FWCurrency a la place de Double car problème d'arrondi
        FWCurrency excedantRevenuCompense = new FWCurrency(simpleFacture.getExcedantRevenuCompense());
        FWCurrency montantRembourse = new FWCurrency(simpleFacture.getMontantRembourse());
        FWCurrency montantFacture = new FWCurrency(simpleFacture.getMontant());
        excedantRevenuCompense.add(montantRembourse);
        if (excedantRevenuCompense.compareTo(montantFacture) == 1) {
            JadeThread.logError(SimpleFacture.class.getName(),
                    "perseus.facture.montantcompense.plus.montantrembourse.plusgrand.montantfacture");
        }

        if (!JadeStringUtil.isEmpty(simpleFacture.getDateDebutTraitement())
                && !JadeDateUtil.isGlobazDate(simpleFacture.getDateDebutTraitement())) {

            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.date.debut.traitement");

        }

        if (!JadeStringUtil.isEmpty(simpleFacture.getDateFinTraitement())
                && !JadeDateUtil.isGlobazDate(simpleFacture.getDateFinTraitement())) {

            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.date.fin.traitement");

        }

        if (!JadeStringUtil.isEmpty(simpleFacture.getDateReception())
                && !JadeDateUtil.isGlobazDate(simpleFacture.getDateReception())) {

            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.date.reception");

        }

        if (!JadeStringUtil.isEmpty(simpleFacture.getDateFacture())
                && !JadeDateUtil.isGlobazDate(simpleFacture.getDateFacture())) {

            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.date.facture");

        }

    }

    /**
     * @param simpleFacture
     */
    private static void checkMandatory(SimpleFacture simpleFacture) {
        if (JadeStringUtil.isEmpty(simpleFacture.getIdGestionnaire())) {
            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.gestionnaire.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleFacture.getIdQD())) {
            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.idqd.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleFacture.getDateFacture())) {
            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.datefacture.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleFacture.getDateReception())) {
            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.datereception.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleFacture.getMontant())) {
            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.montant.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleFacture.getMontantRembourse())) {
            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.montantrembourse.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleFacture.getIdApplicationAdressePaiement())
                || JadeStringUtil.isEmpty(simpleFacture.getIdTiersAdressePaiement())) {
            JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.adressepaiement.mandatory");
        }
    }
}
