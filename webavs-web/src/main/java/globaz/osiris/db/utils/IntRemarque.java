package globaz.osiris.db.utils;

import globaz.globall.api.BISession;

/**
 * Insérez la description du type ici. Date de création : (28.08.2002 15:21:28)
 * 
 * @author: Administrator
 */
public interface IntRemarque {
    /**
     * Insérez la description de la méthode ici. Date de création : (28.08.2002 15:24:27)
     */
    public String getIdRemarque();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.08.2002 15:25:07)
     */
    public BISession getISession();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.08.2002 15:25:07)
     */
    public String getTexteRemarque();

    /**
     * Insérez la description de la méthode ici. Date de création : (28.08.2002 15:31:05)
     * 
     * @param newIdRemarque
     *            java.lang.String
     */
    void setIdRemarque(String newIdRemarque);

    /**
     * Insérez la description de la méthode ici. Date de création : (28.08.2002 15:31:05)
     * 
     * @param newIdRemarque
     *            java.lang.String
     */
    public void setISession(BISession newSession);

    /**
     * Insérez la description de la méthode ici. Date de création : (28.08.2002 15:32:38)
     * 
     * @param newTexteRemarque
     *            java.lang.String
     */
    void setTexteRemarque(String newTexteRemarque);
}
