package globaz.hermes.db.gestion;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.hermes.utils.DateUtils;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HEImpressionciViewBean extends HEAnnoncesViewBean implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String email = "";
    private String forNumeroCaisse = "";

    /**
     * Constructor for HEImpressionciViewBean.
     */
    public HEImpressionciViewBean() {
        super();
    }

    public String getDateFormattee() {
        return DateUtils.convertDate(getDateAnnonce(), DateUtils.AAAAMMJJ, DateUtils.JJMMAAAA_DOTS);
    }

    /**
     * Returns the email.
     * 
     * @return String
     */
    public String getEmail() {
        if (email.trim().length() == 0) {
            return getSession().getUserEMail();
        }
        return email;
    }

    /**
     * Returns the forNumeroCaisse.
     * 
     * @return String
     */
    public String getForNumeroCaisse() {
        return forNumeroCaisse;
    }

    /**
     * Sets the email.
     * 
     * @param email
     *            The email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the forNumeroCaisse.
     * 
     * @param forNumeroCaisse
     *            The forNumeroCaisse to set
     */
    public void setForNumeroCaisse(String forNumeroCaisse) {
        this.forNumeroCaisse = forNumeroCaisse;
    }

}
