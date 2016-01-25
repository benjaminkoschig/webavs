package globaz.osiris.db.irrecouvrable;

import java.io.Serializable;

/**
 * Classe représentant la key de la map CACumulMontantsOperationsSectionMap. La key est composée de année, idRubrique et
 * idRubriqueRecouvrement
 * 
 * @author sch
 * 
 */
public class CAKeyCumulMontantsOperationsSections implements Serializable,
        Comparable<CAKeyCumulMontantsOperationsSections> {
    private static final long serialVersionUID = -2430570481635550332L;

    private Integer annee;
    private String idRubriqueRecouvrement;

    public CAKeyCumulMontantsOperationsSections(Integer annee, String idRubriqueRecouvrement) {
        this.annee = annee;
        this.idRubriqueRecouvrement = idRubriqueRecouvrement;
    }

    @Override
    public int compareTo(CAKeyCumulMontantsOperationsSections o) {
        if (annee.compareTo(o.getAnnee()) < 0) {
            return -1;
        } else if (annee.equals(o.getAnnee())) {
            int idRubriqueRecouvrement = Integer.parseInt(this.idRubriqueRecouvrement);
            int iOIdRubriqueRecouvrement = Integer.parseInt(o.getIdRubriqueRecouvrement());
            if (idRubriqueRecouvrement < iOIdRubriqueRecouvrement) {
                return -1;
            } else if (idRubriqueRecouvrement == iOIdRubriqueRecouvrement) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public Integer getAnnee() {
        return annee;
    }

    public String getIdRubriqueRecouvrement() {
        return idRubriqueRecouvrement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((annee == null) ? 0 : annee.hashCode());
        result = prime * result + ((idRubriqueRecouvrement == null) ? 0 : idRubriqueRecouvrement.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        CAKeyCumulMontantsOperationsSections other = (CAKeyCumulMontantsOperationsSections) obj;
        if (annee == null) {
            if (other.annee != null) {
                return false;
            }
        } else if (!annee.equals(other.annee)) {
            return false;
        }
        if (idRubriqueRecouvrement == null) {
            if (other.idRubriqueRecouvrement != null) {
                return false;
            }
        } else if (!idRubriqueRecouvrement.equals(other.idRubriqueRecouvrement)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String result = "";
        result += "annee = " + annee + " \n";
        result += "idRubrique = " + idRubriqueRecouvrement + " \n";
        return result;
    }
}
