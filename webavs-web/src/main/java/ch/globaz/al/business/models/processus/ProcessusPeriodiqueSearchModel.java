package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeAbstractSearchModel;

/**
 * Mod�le de recherche sur les processus p�riodiques
 * 
 * @author GMO
 * 
 */
public class ProcessusPeriodiqueSearchModel extends JadeAbstractSearchModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crit�re de recherche passage facturation li� au processus
     */
    private String forIdPassageFactu = null;

    private String forPeriode = null;

    /**
     * 
     * @return forIdPassageFactu
     */
    public String getForIdPassageFactu() {
        return forIdPassageFactu;
    }

    /**
     * 
     * @param forIdPassageFactu
     *            l'id du passage facturation li� au processus
     */
    public void setForIdPassageFactu(String forIdPassageFactu) {
        this.forIdPassageFactu = forIdPassageFactu;
    }

    @Override
    public Class whichModelClass() {
        return ProcessusPeriodiqueModel.class;
    }

}
