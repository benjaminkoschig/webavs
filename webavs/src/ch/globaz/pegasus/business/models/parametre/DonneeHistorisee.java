package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeComplexModel;

public class DonneeHistorisee extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDonneeHistorisee simpleDonneeHistorisee = null;

    @Override
    public String getId() {
        return simpleDonneeHistorisee.getId();
    }

    /**
     * @return the simpleDonneeHistorisee
     */
    public SimpleDonneeHistorisee getSimpleDonneeHistorisee() {
        return simpleDonneeHistorisee;
    }

    @Override
    public String getSpy() {
        return simpleDonneeHistorisee.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleDonneeHistorisee.setId(id);
    }

    /**
     * @param simpleDonneeHistorisee
     *            the simpleDonneeHistorisee to set
     */
    public void setSimpleDonneeHistorisee(SimpleDonneeHistorisee simpleDonneeHistorisee) {
        this.simpleDonneeHistorisee = simpleDonneeHistorisee;
    }

    @Override
    public void setSpy(String spy) {
        simpleDonneeHistorisee.setSpy(spy);
    }

}
