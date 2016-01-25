package globaz.hercule.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;

public class CEAttributionPtsViewBean extends CEAttributionPts implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String infoTiers;
    private String oldIdAttributionPts;

    /**
     * Constructeur de CEAttributionPtsViewBean
     */
    public CEAttributionPtsViewBean() {
        super();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getInfoTiers() {

        String _infoTiers = "";

        if (getNom() == null) {
            return _infoTiers;
        }

        if (JadeStringUtil.isBlankOrZero(getAnneeFinAffiliation())) {
            _infoTiers = getNom() + "\n" + getAnneeDebutAffiliation() + " - *";
        } else {
            _infoTiers = getNom() + "\n" + getAnneeDebutAffiliation() + " - " + getAnneeFinAffiliation();
        }

        return getNom() + "\n";
    }

    public String getOldIdAttributionPts() {
        return oldIdAttributionPts;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setInfoTiers(String infoTiers) {
        this.infoTiers = infoTiers;
    }

    public void setOldIdAttributionPts(String oldIdAttributionPts) {
        this.oldIdAttributionPts = oldIdAttributionPts;
    }
}