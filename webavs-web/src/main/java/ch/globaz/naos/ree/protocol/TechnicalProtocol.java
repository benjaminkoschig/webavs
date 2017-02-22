package ch.globaz.naos.ree.protocol;

import java.util.Date;

/**
 * Protocole technique concernant la configuration Sedex pour l'ex�cution du process
 * 
 * @author lga
 * 
 */
public class TechnicalProtocol {

    /**
     * Date de d�but d'ex�cution du traitement
     */
    private Date debutTraitement;

    /**
     * Date de fin d'ex�cution du traitement
     */
    private Date finTraitement;

    /**
     * @param sedexSenderId
     * @param sedexRecipientId
     * @param sedexAdapteurIP
     * @param sedexOutboxFolder
     */
    public TechnicalProtocol(Date debutTraitement, Date finTraitement) {
        super();
        this.debutTraitement = debutTraitement;
        this.finTraitement = finTraitement;
    }

    /**
     * Retourne la <code>Date</code> de d�marrage du traitement
     * 
     * @return la <code>Date</code> de d�marrage du traitement
     */
    public final Date getDateDebutdeTraitement() {
        return debutTraitement;
    }

    /**
     * Retourne le temps total en ms
     * 
     * @return le temps total en ms
     */
    public long getTotalTime() {
        return finTraitement.getTime() - debutTraitement.getTime();
    }

}
