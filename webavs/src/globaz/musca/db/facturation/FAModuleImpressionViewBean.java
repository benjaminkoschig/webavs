package globaz.musca.db.facturation;

public class FAModuleImpressionViewBean extends FAModuleImpression implements globaz.framework.bean.FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "FAMOIMP AS FAMOIMP ";
    }

    public java.lang.String getAction() {
        return action;
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

}
