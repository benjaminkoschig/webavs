package globaz.naos.db.affiliation;

/**
 * Cette classe est crées pour facilité le travail du FWDispatcher.
 * 
 * Elle etend AFAffiliationViewBean, et est utilisée par l'écran affilieSelect_rc.jsp.
 * 
 * @author sau
 */
public class AFAffilieSelectViewBean extends AFAffiliationViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Indique si l'implémentation des héritages est gérée automatiquement.
     * 
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }
}
