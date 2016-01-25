package ch.globaz.pegasus.business.domaine.dossier;

import ch.globaz.common.domaine.Checkers;

public class IdDossier {

    private String idDossier = null;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idDossier == null) ? 0 : idDossier.hashCode());
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
        IdDossier other = (IdDossier) obj;
        if (idDossier == null) {
            if (other.idDossier != null) {
                return false;
            }
        } else if (!idDossier.equals(other.idDossier)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "idDossier [idDossier=" + idDossier + "]";
    }

    public IdDossier(String idDossier) {
        Checkers.checkNotNull(idDossier, "The parameter idDossier can't be null");
        this.idDossier = idDossier;
    }

    public String get() {
        return idDossier;
    }

}
