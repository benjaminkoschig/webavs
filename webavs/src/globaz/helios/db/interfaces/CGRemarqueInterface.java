package globaz.helios.db.interfaces;

import globaz.helios.db.comptes.CGRemarque;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (06.03.2003 09:34:12)
 * 
 * @author: Administrator
 */
public interface CGRemarqueInterface {
    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    String getRemarque();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.03.2003 09:35:03)
     * 
     * @return boolean
     */
    boolean hasCGRemarque();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.03.2003 09:35:03)
     * 
     * @return boolean
     */
    boolean hasRemarque();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    CGRemarque retrieveCGRemarque();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    void setRemarque(String rem);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    void updateRemarque(globaz.globall.db.BTransaction transaction) throws Exception;
}
