package globaz.naos.db.affiliation;

/**
 * Cette classe est cr�es pour facilit� le travail du FWDispatcher.
 * 
 * Elle etend AFAffiliationViewBean, et est utilis�e par l'�cran affilieSelect_rc.jsp.
 * 
 * @author sau
 */
public class AFAffilieSelectViewBean extends AFAffiliationViewBean {

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
