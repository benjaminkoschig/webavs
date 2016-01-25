/*
 * Créé le 03 sept. 07
 */
package globaz.corvus.vb.process;

import globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author BSC
 */
public class REgenererRecapitulationRentesViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String eMailAddress = "";
    private REDetailRecapMensuelleViewBean reDetRecMenViewBean = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    public REDetailRecapMensuelleViewBean getReDetRecMenViewBean() {
        return reDetRecMenViewBean;
    }

    /**
     * setter pour l'attribut EMail address
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setReDetRecMenViewBean(REDetailRecapMensuelleViewBean reDetRecMenViewBean) {
        this.reDetRecMenViewBean = reDetRecMenViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }

}
