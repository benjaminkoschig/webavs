package globaz.phenix.db.principale;

// FRAM
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.phenix.documentsItext.CPImputation_DOC;

public class CPDecisionImpressionViewBean extends CPImputation_DOC implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idAffiliation;
    private java.lang.String idTiers = "";

    /**
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    public CPDecisionImpressionViewBean() throws Exception {
        super(new BSession(globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:03:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:03:19)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:03:34)
     * 
     * @param newIdAffiliation
     *            java.lang.String
     */
    public void setIdAffiliation(java.lang.String newIdAffiliation) {
        idAffiliation = newIdAffiliation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.05.2003 13:03:19)
     * 
     * @param newIdTiers
     *            java.lang.String
     */
    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
    }
}
