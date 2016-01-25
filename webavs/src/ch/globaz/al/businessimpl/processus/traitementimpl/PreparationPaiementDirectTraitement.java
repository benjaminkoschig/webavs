package ch.globaz.al.businessimpl.processus.traitementimpl;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * 
 * Prépare les prestations avant facturation. Cela consiste à passer l'état des récaps et des en-tête de prestation
 * concernée par la période de traitement de 'SA' à 'TR'. La méthode d'annulation les passe de 'TR' à 'SA'
 * 
 * @author jts
 * 
 */
public class PreparationPaiementDirectTraitement extends BusinessTraitement {

    /**
     * Constructeur du traitement génération
     */
    public PreparationPaiementDirectTraitement() {
        super();
    }

    @Override
    public void execute() throws JadeApplicationException, JadePersistenceException {

        if (getProcessusConteneur().isPartiel()) {
            ALImplServiceLocator.getPaiementDirectService().preparerPaiementDirectByNumProcessus(
                    getProcessusConteneur().getProcessusPeriodiqueModel().getId());
        } else {
            ALImplServiceLocator.getPaiementDirectService().preparerPaiementDirect(
                    getProcessusConteneur().getDataCriterias().periodeCriteria);
        }

    }

    @Override
    protected void executeBack() throws JadeApplicationException, JadePersistenceException {
        ALImplServiceLocator.getPaiementDirectService().annulerPreparationPaiementDirect(
                getProcessusConteneur().getDataCriterias().periodeCriteria);
    }

    @Override
    public String getCSTraitement() {
        return ALCSProcessus.NAME_TRAITEMENT_PREPARATION_VERSEMENT_DIRECTS;
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
