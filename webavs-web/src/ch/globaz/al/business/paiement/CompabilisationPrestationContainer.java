package ch.globaz.al.business.paiement;

import ch.globaz.al.business.loggers.ProtocoleLogger;

/**
 * Conteneur utilisé par le service de versement direct des prestations pour retourner le numéro de journal et le logger
 * d'erreurs
 * 
 * @author jts
 * 
 */
public class CompabilisationPrestationContainer {

    /**
     * id du journal comptabilisé
     */
    private String idJournal = null;
    /**
     * Logger contenant les erreurs survenues pendant la comptabilisation
     */
    private ProtocoleLogger logger = null;

    /**
     * Constructeur
     * 
     * @param idJournal
     *            id du journal comptabilisé
     * @param logger
     *            Logger contenant les erreurs survenues pendant la comptabilisation
     */
    public CompabilisationPrestationContainer(String idJournal, ProtocoleLogger logger) {
        this.idJournal = idJournal;
        this.logger = logger;
    }

    /**
     * @return the idJournal
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * @return the logger
     */
    public ProtocoleLogger getLogger() {
        return logger;
    }
}
