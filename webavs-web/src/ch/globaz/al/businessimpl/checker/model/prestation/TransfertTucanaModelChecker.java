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
 * classe de validation des donn�es de TransferTucanaModel
 * 
 * @author jts
 * @see ch.globaz.al.business.models.prestation.TransfertTucanaModel
 */
public abstract class TransfertTucanaModelChecker extends ALAbstractChecker {

    /**
     * Validation de l'int�grit� m�tier des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(TransfertTucanaModel model) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // v�rification de l'existence du d�tail de la prestation
        DetailPrestationSearchModel sd = new DetailPrestationSearchModel();
        sd.setForIdDetailPrestation(model.getIdDetailPrestation());
        if (0 == ALImplServiceLocator.getDetailPrestationModelService().count(sd)) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.idDetailPrestation.businessIntegrity.ExistingId");
        }
    }

    /**
     * V�rification de l'int�grit� DB des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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

        // rubrique suppl�ment l�gal
        if (!JadeNumericUtil.isInteger(model.getRubriqueSupplementLegal())) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.rubriqueSupplementLegal.databaseIntegrity.type");
        }

        // rubrique suppl�ment conventionnel
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
     * v�rifie l'obligation des donn�es de TransfertTucanaModel, si non respect�e lance un message sur l'obligation de
     * ces donn�es
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(TransfertTucanaModel model) throws JadePersistenceException {

        // id detail prestation
        if (JadeStringUtil.isEmpty(model.getIdDetailPrestation())) {
            JadeThread.logError(TransfertTucanaModelChecker.class.getName(),
                    "al.prestation.transfertTucanaModel.idDetailPrestation.mandatory");
        }

        // rubrique suppl�ment conventionnel
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
     * valide les donn�es de TransfertTucanaModel
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(TransfertTucanaModel model) throws JadePersistenceException, JadeApplicationException {
        TransfertTucanaModelChecker.checkMandatory(model);
        TransfertTucanaModelChecker.checkDatabaseIntegrity(model);
        TransfertTucanaModelChecker.checkBusinessIntegrity(model);
    }
}
