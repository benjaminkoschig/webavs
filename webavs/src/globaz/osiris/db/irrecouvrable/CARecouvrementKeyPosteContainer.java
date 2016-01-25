package globaz.osiris.db.irrecouvrable;

import java.io.Serializable;

/**
 * Classe représentant la key de la map contenue dans l'objet CAPosteContainer. La key est composée d'un numero de
 * rubrique de recouvrement, d'un ordre de priorite et d'un type
 * 
 * @author sch
 * 
 */
public class CARecouvrementKeyPosteContainer implements Comparable<CARecouvrementKeyPosteContainer>, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer annee;
    private String numeroRubriqueRecouvrement;
    private String ordrePriorite;

    public CARecouvrementKeyPosteContainer(Integer annee, String numeroRubriqueRecouvrement, String ordrePriorite) {
        this.annee = annee;
        this.numeroRubriqueRecouvrement = numeroRubriqueRecouvrement;
        this.ordrePriorite = ordrePriorite;
    }

    @Override
    public int compareTo(CARecouvrementKeyPosteContainer o) {
        int ordrePrioriteInt = new Integer(ordrePriorite);
        int o_ordrePrioriteInt = new Integer(o.getOrdrePriorite());

        if (ordrePrioriteInt < o_ordrePrioriteInt) {
            return -1;
        } else if (ordrePrioriteInt == o_ordrePrioriteInt) {
            if (numeroRubriqueRecouvrement.compareTo(o.getNumeroRubriqueRecouvrement()) < 0) {
                return -1;
            } else if (numeroRubriqueRecouvrement.equals(o.getNumeroRubriqueRecouvrement())) {
                if (annee.compareTo(o.getAnnee()) < 0) {
                    return -1;
                } else if (annee.equals(o.getAnnee())) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        } else {
            return 1;
        }
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
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        CARecouvrementKeyPosteContainer other = (CARecouvrementKeyPosteContainer) obj;
        if (annee == null) {
            if (other.annee != null) {
                return false;
            }
        } else if (!annee.equals(other.annee)) {
            return false;
        }
        if (numeroRubriqueRecouvrement == null) {
            if (other.numeroRubriqueRecouvrement != null) {
                return false;
            }
        } else if (!numeroRubriqueRecouvrement.equals(other.numeroRubriqueRecouvrement)) {
            return false;
        }
        if (ordrePriorite == null) {
            if (other.ordrePriorite != null) {
                return false;
            }
        } else if (!ordrePriorite.equals(other.ordrePriorite)) {
            return false;
        }
        return true;
    }

    public Integer getAnnee() {
        return annee;
    }

    public String getNumeroRubriqueRecouvrement() {
        return numeroRubriqueRecouvrement;
    }

    public String getOrdrePriorite() {
        return ordrePriorite;
    }

    public String getStringValue(String separator) {
        String stringValue = "";
        stringValue += getOrdrePriorite();
        stringValue += separator + getNumeroRubriqueRecouvrement();
        return stringValue;
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
        result = (prime * result) + ((numeroRubriqueRecouvrement == null) ? 0 : numeroRubriqueRecouvrement.hashCode());
        result = (prime * result) + ((ordrePriorite == null) ? 0 : ordrePriorite.hashCode());
        return result;
    }
}
