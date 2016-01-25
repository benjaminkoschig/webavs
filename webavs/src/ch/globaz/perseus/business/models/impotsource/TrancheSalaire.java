package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeComplexModel;

public class TrancheSalaire extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleTrancheSalaire simpleTrancheSalaire = null;

    public TrancheSalaire() {
        super();
        simpleTrancheSalaire = new SimpleTrancheSalaire();
    }

    @Override
    public String getId() {
        return simpleTrancheSalaire.getId();
    }

    public SimpleTrancheSalaire getSimpleTrancheSalaire() {
        return simpleTrancheSalaire;
    }

    @Override
    public String getSpy() {
        return simpleTrancheSalaire.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleTrancheSalaire.setId(id);

    }

    public void setSimpleTrancheSalaire(SimpleTrancheSalaire simpleTrancheSalaire) {
        this.simpleTrancheSalaire = simpleTrancheSalaire;
    }

    @Override
    public void setSpy(String spy) {
        simpleTrancheSalaire.setSpy(spy);

    }

}
