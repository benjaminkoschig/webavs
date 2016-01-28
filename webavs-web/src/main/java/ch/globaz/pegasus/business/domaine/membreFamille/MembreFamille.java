package ch.globaz.pegasus.business.domaine.membreFamille;

import ch.globaz.pyxis.domaine.PersonneAVS;

public class MembreFamille {
    private RoleMembreFamille roleMembreFamille = RoleMembreFamille.INDEFINIT;
    private PersonneAVS personne = new PersonneAVS();

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

    @Override
    public String toString() {
        return "MembreFamille [roleMembreFamille=" + roleMembreFamille + ", personne=" + personne + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
