package ch.globaz.pegasus.business.exceptions.models.renteijapi;

import ch.globaz.pegasus.business.exceptions.PegasusException;

/**
 * Classe de gestion des exception pour les Indemintes journalieres AI 6.2010
 * 
 * @author SCE
 * 
 */
public class IndemniteJournaliereAiException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IndemniteJournaliereAiException() {
        super();
    }

    public IndemniteJournaliereAiException(String m) {
        super(m);
    }

    public IndemniteJournaliereAiException(String m, Throwable t) {
        super(m, t);
    }
}
