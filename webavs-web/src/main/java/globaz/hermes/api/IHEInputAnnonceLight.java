package globaz.hermes.api;

import globaz.globall.api.BITransaction;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public interface IHEInputAnnonceLight extends IHEAnnoncesViewBean {
    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si l'ajout a échoué
     */
    @Override
    public void add(BITransaction transaction) throws java.lang.Exception;
}
