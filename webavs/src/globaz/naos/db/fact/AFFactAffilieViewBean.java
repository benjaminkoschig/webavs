/*
 * Created on 11-Jan-05
 */
package globaz.naos.db.fact;

/**
 * Cette classe est crées pour facilité le travail du FWDispatcher.
 * 
 * Elle etend AFFactViewBean, et est utilisée par les écrans factAffilie_rc.jsp et factAffilie_de.jsp.
 * 
 * @author sau
 */
public class AFFactAffilieViewBean extends AFFactViewBean {

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
