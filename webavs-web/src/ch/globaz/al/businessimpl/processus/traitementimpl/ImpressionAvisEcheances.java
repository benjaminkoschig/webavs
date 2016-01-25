package ch.globaz.al.businessimpl.processus.traitementimpl;

import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;

/**
 * Traitement implémentant l'impression des avis d'échéances
 * 
 * @author GMO
 * 
 */
public class ImpressionAvisEcheances extends BusinessTraitement {

    /**
     * Constructeur
     */
    public ImpressionAvisEcheances() {
        super();
    }

    @Override
    protected void execute() {
        // TODO : (lot 2) à terminer
    }

    @Override
    protected void executeBack() {
        // DO NOTHING
    }

    @Override
    public String getCSTraitement() {
        return ALCSProcessus.NAME_TRAITEMENT_IMPRESSION_ECHEANCES;
    }

    @Override
    public JadePrintDocumentContainer[] getProtocole() {
        // Pas de protocole pour ce traitement
        return null;
    }

    @Override
    public ProtocoleLogger getProtocoleLogger() {
        // Pas de logger pour ce traitement
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
