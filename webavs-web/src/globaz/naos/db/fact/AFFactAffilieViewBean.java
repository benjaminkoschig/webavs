/*
 * Created on 11-Jan-05
 */
package globaz.naos.db.fact;

/**
 * Cette classe est cr�es pour facilit� le travail du FWDispatcher.
 * 
 * Elle etend AFFactViewBean, et est utilis�e par les �crans factAffilie_rc.jsp et factAffilie_de.jsp.
 * 
 * @author sau
 */
public class AFFactAffilieViewBean extends AFFactViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Indique si l'impl�mentation des h�ritages est g�r�e automatiquement.
     * 
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }
}
