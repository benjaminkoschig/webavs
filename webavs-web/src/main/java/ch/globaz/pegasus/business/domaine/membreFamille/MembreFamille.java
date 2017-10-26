package ch.globaz.pegasus.business.domaine.membreFamille;

import ch.globaz.pyxis.domaine.PersonneAVS;

public class MembreFamille {
    private String id; // / correspond à l'idDroitMembreFamille
    private RoleMembreFamille roleMembreFamille = RoleMembreFamille.INDEFINIT;
    private PersonneAVS personne = new PersonneAVS();
    private DonneesPersonnelles donneesPersonnelles;

    public DonneesPersonnelles getDonneesPersonnelles() {
        return donneesPersonnelles;
    }

    public void setDonneesPersonnelles(DonneesPersonnelles donneesPersonnelles) {
        this.donneesPersonnelles = donneesPersonnelles;
    }

    public RoleMembreFamille getRoleMembreFamille() {
        return roleMembreFamille;
    }

    public PersonneAVS getPersonne() {
        return personne;
    }

    public void setRoleMembreFamille(RoleMembreFamille roleMembreFamille) {
        this.roleMembreFamille = roleMembreFamille;
    }

    public void setPersonne(PersonneAVS personne) {
        this.personne = personne;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MembreFamille [id=" + id + ", roleMembreFamille=" + roleMembreFamille + ", personne=" + personne
                + ", donneesPersonnelles=" + donneesPersonnelles + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((donneesPersonnelles == null) ? 0 : donneesPersonnelles.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((personne == null) ? 0 : personne.hashCode());
        result = prime * result + ((roleMembreFamille == null) ? 0 : roleMembreFamille.hashCode());
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
        MembreFamille other = (MembreFamille) obj;
        if (donneesPersonnelles == null) {
            if (other.donneesPersonnelles != null) {
                return false;
            }
        } else if (!donneesPersonnelles.equals(other.donneesPersonnelles)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (personne == null) {
            if (other.personne != null) {
                return false;
            }
        } else if (!personne.equals(other.personne)) {
            return false;
        }
        if (roleMembreFamille != other.roleMembreFamille) {
            return false;
        }
        return true;
    }

}
