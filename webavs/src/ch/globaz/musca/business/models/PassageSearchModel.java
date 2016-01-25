package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche sur les modèles simples des passages de facturation
 * 
 * @author GMO
 * 
 */
public class PassageSearchModel extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Critère type de facturation à exclure
     */

    private String forNotTypeFacturation = null;
    /**
     * Critère status dans la recherche de passage
     */
    private String forStatus = null;

    public String getForNotTypeFacturation() {
        return forNotTypeFacturation;
    }

    public String getForStatus() {
        return forStatus;
    }

    public void setForNotTypeFacturation(String forNotTypeFacturation) {
        this.forNotTypeFacturation = forNotTypeFacturation;
    }

    /**
     * 
     * @param forStatus
     *            critère status de la rechercher
     */
    public void setForStatus(String forStatus) {
        this.forStatus = forStatus;
    }

    @Override
    public Class whichModelClass() {
        return PassageModel.class;
    }

}
