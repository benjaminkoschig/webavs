/*
 * Cr?? le 15 janv. 07
 */
package globaz.corvus.vb.demandes;

import globaz.corvus.db.demandes.REPeriodeAPI;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JADate;

/**
 * @author hpe
 * 
 */

public class REPeriodeAPIViewBean extends REPeriodeAPI implements FWViewBeanInterface, Comparable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int idProvisoire = 0;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object arg0) {
        try {
            return new JADate(getDateDebutInvalidite()).toAMJ().compareTo(
                    new JADate(((REPeriodeAPI) arg0).getDateDebutInvalidite()).toAMJ());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * @return
     */
    public int getIdProvisoire() {
        return idProvisoire;
    }

    /**
     * @param i
     */
    public void setIdProvisoire(int i) {
        idProvisoire = i;
    }

}
