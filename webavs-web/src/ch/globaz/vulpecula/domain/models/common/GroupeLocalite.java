package ch.globaz.vulpecula.domain.models.common;

/**
 * Un groupe de localité est un ensemble de localité regroupées sous une même région.
 */
public class GroupeLocalite implements DomainEntity {
    private String idGroupeLocalite;
    private String nomGroupeFR;
    private String nomGroupeDE;
    private String nomGroupeIT;
    private String noGroupe;
    private String typeGroupe;
    private String spy;

    public GroupeLocalite() {

    }

    public String getNomGroupeFR() {
        return nomGroupeFR;
    }

    public void setNomGroupeFR(String nomGroupeFR) {
        this.nomGroupeFR = nomGroupeFR;
    }

    public String getNomGroupeDE() {
        return nomGroupeDE;
    }

    public void setNomGroupeDE(String nomGroupeDE) {
        this.nomGroupeDE = nomGroupeDE;
    }

    public String getNomGroupeIT() {
        return nomGroupeIT;
    }

    public void setNomGroupeIT(String nomGroupeIT) {
        this.nomGroupeIT = nomGroupeIT;
    }

    public String getNoGroupe() {
        return noGroupe;
    }

    public void setNoGroupe(String noGroupe) {
        this.noGroupe = noGroupe;
    }

    @Override
    public String getId() {
        return idGroupeLocalite;
    }

    @Override
    public void setId(String id) {
        idGroupeLocalite = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getTypeGroupe() {
        return typeGroupe;
    }

    public void setTypeGroupe(String typeGroupe) {
        this.typeGroupe = typeGroupe;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idGroupeLocalite == null) ? 0 : idGroupeLocalite.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GroupeLocalite other = (GroupeLocalite) obj;
        if (idGroupeLocalite == null || other.idGroupeLocalite == null) {
            return false;
        }

        if (idGroupeLocalite.equals(other.idGroupeLocalite)) {
            return true;
        }
        return false;
    }
}
