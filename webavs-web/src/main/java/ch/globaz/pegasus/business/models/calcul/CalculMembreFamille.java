package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;

public class CalculMembreFamille extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateNaissance = null;
    private SimpleDroitMembreFamille simpleDroitMembreFamille = null;

    public CalculMembreFamille() {
        super();
        simpleDroitMembreFamille = new SimpleDroitMembreFamille();
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    @Override
    public String getId() {
        return simpleDroitMembreFamille.getId();
    }

    public SimpleDroitMembreFamille getSimpleDroitMembreFamille() {
        return simpleDroitMembreFamille;
    }

    @Override
    public String getSpy() {
        return simpleDroitMembreFamille.getSpy();
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public void setId(String id) {
        simpleDroitMembreFamille.setId(id);
    }

    public void setSimpleDroitMembreFamille(SimpleDroitMembreFamille simpleDroitMembreFamille) {
        this.simpleDroitMembreFamille = simpleDroitMembreFamille;
    }

    @Override
    public void setSpy(String spy) {
        simpleDroitMembreFamille.setSpy(spy);

    }

}
