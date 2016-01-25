package ch.globaz.osiris.business.model;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class CompteAnnexeSimpleModelSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Collection<String> forIdCompteAnnexeIn;
    private Collection<String> forIdExterneRoleIn;
    private String forIdRole;
    private Collection<String> forIdRoleIn;
    private Collection<String> forIdTiersIn;

    public Collection<String> getForIdCompteAnnexeIn() {
        return forIdCompteAnnexeIn;
    }

    public Collection<String> getForIdExterneRoleIn() {
        return forIdExterneRoleIn;
    }

    public String getForIdRole() {
        return forIdRole;
    }

    public Collection<String> getForIdRoleIn() {
        return forIdRoleIn;
    }

    public Collection<String> getForIdTiersIn() {
        return forIdTiersIn;
    }

    public void setForIdCompteAnnexeIn(Collection<String> forIdCompteAnnexeIn) {
        this.forIdCompteAnnexeIn = forIdCompteAnnexeIn;
    }

    public void setForIdExterneRoleIn(Collection<String> forIdExterneRoleIn) {
        this.forIdExterneRoleIn = forIdExterneRoleIn;
    }

    public void setForIdRole(String forIdRole) {
        this.forIdRole = forIdRole;
    }

    public void setForIdRoleIn(Collection<String> forIdRoleIn) {
        this.forIdRoleIn = forIdRoleIn;
    }

    public void setForIdTiersIn(Collection<String> forIdTiersIn) {
        this.forIdTiersIn = forIdTiersIn;
    }

    @Override
    public Class<CompteAnnexeSimpleModel> whichModelClass() {
        return CompteAnnexeSimpleModel.class;
    }

}
