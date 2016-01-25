package ch.globaz.perseus.business.exceptions.models.statistique;

import ch.globaz.perseus.business.exceptions.PerseusException;

public class StatistiquesMensuellesEnfantsDemandeException extends PerseusException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public StatistiquesMensuellesEnfantsDemandeException() {
    }

    /**
     * @param m
     */
    public StatistiquesMensuellesEnfantsDemandeException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public StatistiquesMensuellesEnfantsDemandeException(String m, Throwable t) {
        super(m, t);
    }
}
