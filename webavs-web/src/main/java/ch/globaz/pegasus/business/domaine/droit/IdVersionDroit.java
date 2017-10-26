package ch.globaz.pegasus.business.domaine.droit;

import ch.globaz.common.domaine.Checkers;

public class IdVersionDroit {
    private String idVersionDroit = null;

    public IdVersionDroit(String idVersionDroit) {
        Checkers.checkNotNull(idVersionDroit, "The paramneter idVersionDroit can't be null");
        this.idVersionDroit = idVersionDroit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idVersionDroit == null) ? 0 : idVersionDroit.hashCode());
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
        IdVersionDroit other = (IdVersionDroit) obj;
        if (idVersionDroit == null) {
            if (other.idVersionDroit != null) {
                return false;
            }
        } else if (!idVersionDroit.equals(other.idVersionDroit)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IdVersionDroit [idVersionDroit=" + idVersionDroit + "]";
    }

    public String get() {
        return idVersionDroit;
    }

}
