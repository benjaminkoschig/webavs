package ch.globaz.al.businessimpl.processus.traitementimpl;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.exceptions.processus.ALProcessusCtrlException;
import ch.globaz.al.business.exceptions.processus.ALProcessusException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * 
 * Prépare les prestations avant facturation. Cela consiste à passer l'état des récaps et des en-tête de prestation
 * concernée par la période de traitement de 'SA' à 'TR'.
 * 
 * @author jts
 * 
 */
public class PreparationCompensationTraitement extends BusinessTraitement {

    /**
     * Constructeur du traitement génération
     */
    public PreparationCompensationTraitement() {
        super();
    }

    @Override
    public void execute() throws JadeApplicationException, JadePersistenceException {
        if (getProcessusConteneur().isPartiel()
                && JadeNumericUtil.isEmptyOrZero(getProcessusConteneur().getProcessusPeriodiqueModel()
                        .getIdPassageFactu())) {
            getTraitementPeriodiqueModel().setEtat(ALCSProcessus.ETAT_ERREUR);
            setTraitementPeriodiqueModel(ALServiceLocator.getTraitementPeriodiqueModelService().update(
                    getTraitementPeriodiqueModel()));
            // on commit ici pour car le traitement est définitivement lancé
            try {
                JadeThread.commitSession();
            } catch (Exception e) {
                throw new ALProcessusException(
                        "PreparationCompensationTraitement#execute: Unable to update traitement in error state ("
                                + e.getMessage() + ")", e);
            }
            throw new ALProcessusCtrlException(
                    "PreparationCompensationTraitement#execute: il faut choisir un journal de facturation avant de préparer les prestations");

        }

        if (getProcessusConteneur().isPartiel()) {
            ALImplServiceLocator.getCompensationFactureService().preparerCompensationByNumProcessus(
                    getProcessusConteneur().getProcessusPeriodiqueModel().getId());
        } else {
            ALImplServiceLocator.getCompensationFactureService().preparerCompensation(
                    getProcessusConteneur().getDataCriterias().periodeCriteria,
                    getProcessusConteneur().getDataCriterias().cotisationCriteria);
        }

    }

    @Override
    protected void executeBack() throws JadeApplicationException, JadePersistenceException {

        if (getProcessusConteneur().isPartiel()) {
            ALImplServiceLocator.getCompensationFactureService().annulerPreparationByNumProcessus(
                    getProcessusConteneur().getProcessusPeriodiqueModel().getId());
        } else {
            ALImplServiceLocator.getCompensationFactureService().annulerPreparation(
                    getProcessusConteneur().getDataCriterias().periodeCriteria,
                    getProcessusConteneur().getDataCriterias().cotisationCriteria);
        }
    }

    @Override
    public String getCSTraitement() {
        return ALCSProcessus.NAME_TRAITEMENT_PREPARATION_COMPENSATION;
    }

    @Override
    public JadePrintDocumentContainer[] getProtocole() {
        // Pas de protocole dans ce traitement
        return null;
    }

    @Override
    public ProtocoleLogger getProtocoleLogger() {
        // Pas de protocole dans ce traitement
        return null;
    }

    @Override
    public JadePublishDocumentInfo getPubInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
