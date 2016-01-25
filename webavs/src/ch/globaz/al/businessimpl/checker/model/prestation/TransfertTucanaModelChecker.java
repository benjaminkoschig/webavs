package ch.globaz.al.businessimpl.checker.model.prestation;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.prestation.DetailPrestationSearchModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe de validation des données de TransferTucanaModel
 * 
 * @author jts
 * @see ch.globaz.al.business.models.prestation.TransfertTucanaModel
 */
public abstract class TransfertTucanaModelChecker extends ALAbstractChecker {

    /**
     * Validation de l'intégrité métier des données
     * 
     * @param model
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(TransfertTucanaModel model) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // vérification de l'existence du détail de la prestation
        DetailPrestationSearchModel sd = new DetailPrestationSearchModel();
        sd.setForIdDetailPrestation(model.getIdDetailPrestation());
        if (0 == ALImplServiceLocator.getDetailPrestationModelService().count(sd)) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.idDetailPrestation.businessIntegrity.ExistingId");
        }
    }

    /**
     * Vérification de l'intégrité DB des données
     * 
     * @param model
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(TransfertTucanaModel model) throws JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id detail prestation
        if (!JadeNumericUtil.isIntegerPositif(model.getIdDetailPrestation())) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.idDetailPrestation.databaseIntegrity.type");
        }

        // rubrique supplément légal
        if (!JadeNumericUtil.isInteger(model.getRubriqueSupplementLegal())) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.rubriqueSupplementLegal.databaseIntegrity.type");
        }

        // rubrique supplément conventionnel
        if (!JadeNumericUtil.isIntegerPositif(model.getRubriqueSupplementConventionnel())) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.rubriqueSupplementConventionnel.databaseIntegrity.type");
        }

        // rubrique allocation
        if (!JadeNumericUtil.isIntegerPositif(model.getRubriqueAllocation())) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.rubriqueAllocation.databaseIntegrity.type");
        }

        // numBouclement
        if (!JadeNumericUtil.isEmptyOrZero(model.getNumBouclement())
                && !JadeNumericUtil.isIntegerPositif(model.getNumBouclement())) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.numBouclement.databaseIntegrity.type");
        }
    }

    /**
     * vérifie l'obligation des données de TransfertTucanaModel, si non respectée lance un message sur l'obligation de
     * ces données
     * 
     * @param model
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(TransfertTucanaModel model) throws JadePersistenceException {

        // id detail prestation
        if (JadeStringUtil.isEmpty(model.getIdDetailPrestation())) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.idDetailPrestation.mandatory");
        }

        // rubrique supplément conventionnel
        if (JadeStringUtil.isEmpty(model.getRubriqueSupplementConventionnel())) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.rubriqueSupplementConventionnel.mandatory");
        }

        // rubrique allocation
        if (JadeStringUtil.isEmpty(model.getRubriqueAllocation())) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.rubriqueAllocation.mandatory");
        }
    }

    /**
     * valide les données de TransfertTucanaModel
     * 
     * @param model
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(TransfertTucanaModel model) throws JadePersistenceException, JadeApplicationException {
        TransfertTucanaModelChecker.checkMandatory(model);
        TransfertTucanaModelChecker.checkDatabaseIntegrity(model);
        TransfertTucanaModelChecker.checkBusinessIntegrity(model);
    }
}
