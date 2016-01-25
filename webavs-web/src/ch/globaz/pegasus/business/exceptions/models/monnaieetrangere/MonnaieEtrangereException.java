package ch.globaz.pegasus.business.exceptions.models.monnaieetrangere;

import ch.globaz.pegasus.business.exceptions.PegasusException;

/**
 * Classe de gestion des exceptions pour le modele MonnaieEtrangere
 * 
 * @author SCE
 * 
 */
public class MonnaieEtrangereException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MonnaieEtrangereException() {
        super();
    }

    public MonnaieEtrangereException(String m) {
        super(m);
    }

    public MonnaieEtrangereException(String m, Throwable t) {
        super(m, t);
    }

}
