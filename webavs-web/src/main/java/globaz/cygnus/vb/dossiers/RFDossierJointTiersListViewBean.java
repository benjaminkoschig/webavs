/*
 * Créé le 30 novembre 2009
 */
package globaz.cygnus.vb.dossiers;

import globaz.cygnus.db.dossiers.RFDossierJointTiersManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author jje
 */
public class RFDossierJointTiersListViewBean extends RFDossierJointTiersManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDernierPaiement;

    public RFDossierJointTiersListViewBean() {
        super();

        dateDernierPaiement = null;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDossierJointTiersViewBean();
    }

    public String getDateDernierPaiement() {
        return dateDernierPaiement;
    }

    public void setDateDernierPaiement(String dateDernierPaiement) {
        this.dateDernierPaiement = dateDernierPaiement;
    }
}