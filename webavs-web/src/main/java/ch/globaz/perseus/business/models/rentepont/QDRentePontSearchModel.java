/**
 * 
 */
package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author JSI
 * 
 */
public class QDRentePontSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forIdDossier = null;

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    @Override
    public Class whichModelClass() {
        return QDRentePont.class;
    }

}
