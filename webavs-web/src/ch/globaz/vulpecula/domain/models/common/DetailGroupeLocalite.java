package ch.globaz.vulpecula.domain.models.common;

/**
 * Un détail de groupe de localité est un lien entre la localité et sa région / son district
 */
public class DetailGroupeLocalite implements DomainEntity {
    private String idDetailGroupeLocalite;
    private GroupeLocalite groupeLocalite;
    private String spy;
    private String idLocalite;

    public DetailGroupeLocalite() {

    }

    @Override
    public String getId() {
        return idDetailGroupeLocalite;
    }

    @Override
    public void setId(String id) {
        idDetailGroupeLocalite = id;
    }

    public GroupeLocalite getGroupeLocalite() {
        return groupeLocalite;
    }

    public void setGroupeLocalite(GroupeLocalite groupeLocalite) {
        this.groupeLocalite = groupeLocalite;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getIdLocalite() {
        return idLocalite;
    }

    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idDetailGroupeLocalite == null) ? 0 : idDetailGroupeLocalite.hashCode());
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
        DetailGroupeLocalite other = (DetailGroupeLocalite) obj;
        if (idDetailGroupeLocalite == null || other.idDetailGroupeLocalite == null) {
            return false;
        }

        if (idDetailGroupeLocalite.equals(other.idDetailGroupeLocalite)) {
            return true;
        }
        return false;
    }

    public String getTypeGroupeDetailLocalite() {
        return groupeLocalite.getTypeGroupe();
    }

    public String getNomGroupeFR() {
        return groupeLocalite.getNomGroupeFR();
    }
}