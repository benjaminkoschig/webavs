/*
 * Crée le 4 septembre 2006
 */

package globaz.ij.vb.process;

import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author hpe
 */

public class IJRecapitulationAnnonceViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String eMailAddress = "";
    private String forMoisAnneeComptable = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        if (JadeStringUtil.isEmpty(eMailAddress)) {
            eMailAddress = getSession().getUserEMail();
        }

        return eMailAddress;
    }

    /**
     * getter pour l'attribut mois annee
     * 
     * @return la valeur courante de l'attribut mois annee
     */
    public String getForMoisAnneeComptable() {
        return forMoisAnneeComptable;
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

    /**
     * setter pour l'attribut mois annee
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnneeComptable(String string) {
        forMoisAnneeComptable = string;
    }

    /**
     * @see globaz.apg.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        boolean retValue = true;

        if (JadeStringUtil.isEmpty(eMailAddress)) {
            _addError("EMAIL_REQUIS");
            retValue = false;
        }

        if (JAUtil.isDateEmpty(forMoisAnneeComptable)) {
            _addError("FORMAT_DATEMOISANNEECOMPTABLE_INCORECT");
            retValue = false;
        }

        return retValue;
    }
}
