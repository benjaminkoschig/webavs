package ch.globaz.pegasus.business.vo.blocage;

import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class VersementBeneficiaire extends LigneDeblocage {
    private String csRoleMembreFamille;
    private String description;
    private PersonneEtendueComplexModel personne;

    public String getCsRoleMembreFamille() {
        return csRoleMembreFamille;
    }

    public String getDescription() {
        return description;
    }

    public String getIdTiers() {
        return personne.getTiers().getIdTiers();
    }

    public PersonneEtendueComplexModel getPersonne() {
        return personne;
    }

    public void setCsRoleMembreFamille(String csRoleMembreFamille) {
        this.csRoleMembreFamille = csRoleMembreFamille;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPersonne(PersonneEtendueComplexModel personne) {
        this.personne = personne;
    }
}
