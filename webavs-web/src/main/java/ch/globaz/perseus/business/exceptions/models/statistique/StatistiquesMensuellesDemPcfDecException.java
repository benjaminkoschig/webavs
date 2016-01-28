package ch.globaz.perseus.business.exceptions.models.statistique;

import ch.globaz.perseus.business.exceptions.PerseusException;

public class StatistiquesMensuellesDemPcfDecException extends PerseusException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public StatistiquesMensuellesDemPcfDecException() {
    }

    /**
     * @param m
     */
    public StatistiquesMensuellesDemPcfDecException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public StatistiquesMensuellesDemPcfDecException(String m, Throwable t) {
        super(m, t);
    }
}
