package globaz.helios.db.interfaces;

import globaz.helios.db.comptes.CGRemarque;

/**
 * Insérez la description du type ici. Date de création : (06.03.2003 09:34:12)
 * 
 * @author: Administrator
 */
public interface CGRemarqueInterface {
    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    String getRemarque();

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:03)
     * 
     * @return boolean
     */
    boolean hasCGRemarque();

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:03)
     * 
     * @return boolean
     */
    boolean hasRemarque();

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    CGRemarque retrieveCGRemarque();

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    void setRemarque(String rem);

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    void updateRemarque(globaz.globall.db.BTransaction transaction) throws Exception;
}
