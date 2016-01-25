/*
 * Créé le 27 août 2010
 */
package globaz.cygnus.vb.qds;

import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipaleManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author jje
 */
public class RFQdSaisiePeriodeValiditeQdPrincipaleListViewBean extends RFPeriodeValiditeQdPrincipaleManager implements
        FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeQd = "";
    private String idTiers = "";

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdSaisiePeriodeValiditeQdPrincipaleViewBean();
    }

    public String getAnneeQd() {
        return anneeQd;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setAnneeQd(String anneeQd) {
        this.anneeQd = anneeQd;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}