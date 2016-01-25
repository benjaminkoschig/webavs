package globaz.hercule.db.couverture;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Classe représentant l'écran de détail des couvertures couvertures
 * 
 * @author SCO
 * @since 1 sept. 2010
 */
public class CECouvertureEcranViewBean extends CECouvertureEcran implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String infoTiers;

    /**
     * Constructeur de CECouvertureEcranViewBean
     */
    public CECouvertureEcranViewBean() {
        super();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getInfoTiers() {

        if (getNom() == null) {
            return "";
        }

        return getNom() + "\n" + getDateDebutAffiliation() + " - " + getDateFinAffiliation();
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setInfoTiers(String infoTiers) {
        this.infoTiers = infoTiers;
    }

}
