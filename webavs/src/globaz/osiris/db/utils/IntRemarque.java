package globaz.osiris.db.utils;

import globaz.globall.api.BISession;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (28.08.2002 15:21:28)
 * 
 * @author: Administrator
 */
public interface IntRemarque {
    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.08.2002 15:24:27)
     */
    public String getIdRemarque();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.08.2002 15:25:07)
     */
    public BISession getISession();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.08.2002 15:25:07)
     */
    public String getTexteRemarque();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.08.2002 15:31:05)
     * 
     * @param newIdRemarque
     *            java.lang.String
     */
    void setIdRemarque(String newIdRemarque);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.08.2002 15:31:05)
     * 
     * @param newIdRemarque
     *            java.lang.String
     */
    public void setISession(BISession newSession);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.08.2002 15:32:38)
     * 
     * @param newTexteRemarque
     *            java.lang.String
     */
    void setTexteRemarque(String newTexteRemarque);
}
