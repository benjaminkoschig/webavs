package ch.globaz.corvus.businessimpl.checkers.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.corvus.business.exceptions.models.SimpleRetenuePayementException;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.corvus.businessimpl.checkers.CorvusAbstractChecker;
import ch.globaz.pegasus.business.constantes.IPCRetenues;

public class SimpleRetenuePayementChecker extends CorvusAbstractChecker {

    public static void checkForCreate(SimpleRetenuePayement simpleRetenuePayement)
            throws SimpleRetenuePayementException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleRetenuePayementChecker.checkMandatory(simpleRetenuePayement);
        SimpleRetenuePayementChecker.checkIntegrity(simpleRetenuePayement);
        /*
         * if (!CorvusAbstractChecker.threadOnError()) {
         * SimpleRetenuePayementChecker.checkIntegrity(simpleRetenuePayement); }
         */
    }

    public static void checkForUpdate(SimpleRetenuePayement simpleRetenuePayement)
            throws SimpleRetenuePayementException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleRetenuePayementChecker.checkMandatory(simpleRetenuePayement);

    }

    private static void checkIntegrity(SimpleRetenuePayement simpleRetenuePayement)
            throws SimpleRetenuePayementException {
        if (new Float(simpleRetenuePayement.getMontantTotalARetenir()) < new Float(
                simpleRetenuePayement.getMontantRetenuMensuel())) {
            JadeThread
                    .logError(
                            simpleRetenuePayement.getClass().getName(),
                            "SimpleRetenuePayementException: le montant de la retenue mensuel ne doit pas être supérieur au montant de la retenue totale");

        }

    }

    private static void checkMandatory(SimpleRetenuePayement simpleRetenuePayement) {
        // champs toujours obligatoires
        if (JadeStringUtil.isBlankOrZero(simpleRetenuePayement.getDateDebutRetenue())) {
            JadeThread.logError(simpleRetenuePayement.getClass().getName(),
                    "corvus.simpleRetenuePayemente.dateDebutRetenue.mandatory");
        }

        if (JadeStringUtil.isBlankOrZero(simpleRetenuePayement.getMontantRetenuMensuel())) {
            JadeThread.logError(simpleRetenuePayement.getClass().getName(),
                    "corvus.simpleRetenuePayemente.montantRetenuMensuel.mandatory");
        }

        if (JadeStringUtil.isBlankOrZero(simpleRetenuePayement.getMontantTotalARetenir())) {
            JadeThread.logError(simpleRetenuePayement.getClass().getName(),
                    "corvus.simpleRetenuePayemente.montantRetenuTotalARetenir.mandatory");
        }

        //
        if (simpleRetenuePayement.getCsTypeRetenue().equalsIgnoreCase(IPCRetenues.CS_FACTURE_FUTURE)) {

            if (JadeStringUtil.isBlankOrZero(simpleRetenuePayement.getIdExterne())) {
                JadeThread.logError(simpleRetenuePayement.getClass().getName(),
                        "corvus.simpleRetenuePayemente.idExterne.mandatory");
            }

        }

        if (simpleRetenuePayement.getCsTypeRetenue().equalsIgnoreCase(IPCRetenues.CS_ADRESSE_PAIEMENT)) {

            if (JadeStringUtil.isBlankOrZero(simpleRetenuePayement.getIdTiersAdressePmt())) {
                JadeThread.logError(simpleRetenuePayement.getClass().getName(),
                        "corvus.simpleRetenuePayemente.idTiersAdressePmt.mandatory");
            }

        }

        if (simpleRetenuePayement.getCsTypeRetenue().equalsIgnoreCase(IPCRetenues.CS_FACTURE_EXISTANTE)) {

            if (JadeStringUtil.isBlankOrZero(simpleRetenuePayement.getIdExterne())) {
                JadeThread.logError(simpleRetenuePayement.getClass().getName(),
                        "corvus.simpleRetenuePayemente.idExterne.mandatory");
            }

            if (JadeStringUtil.isBlankOrZero(simpleRetenuePayement.getIdTypeSection())) {
                JadeThread.logError(simpleRetenuePayement.getClass().getName(),
                        "corvus.simpleRetenuePayemente.idTypeSection.mandatory");
            }

            if (JadeStringUtil.isBlankOrZero(simpleRetenuePayement.getNoFacture())) {
                JadeThread.logError(simpleRetenuePayement.getClass().getName(),
                        "corvus.simpleRetenuePayemente.noFacture.mandatory");
            }

        }

    }
}
