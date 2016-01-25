package globaz.osiris.db.irrecouvrable;

import java.io.Serializable;

/**
 * Classe représentant la key de la map contenue dans l'objet CAPosteContainer. La key est composée d'une année, d'un
 * numero de rubrique irrecouvrable, d'un ordre de priorite et d'un type
 * 
 * @author bjo
 * 
 */
public class CAKeyPosteContainer implements Comparable<CAKeyPosteContainer>, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer annee;
    private String numeroRubriqueIrrecouvrable;
    private String ordrePriorite;
    private CATypeLigneDePoste type;

    public CAKeyPosteContainer(Integer annee, String numeroRubriqueIrrecouvrable, String ordrePriorite,
            CATypeLigneDePoste type) {
        this.annee = annee;
        this.numeroRubriqueIrrecouvrable = numeroRubriqueIrrecouvrable;
        this.ordrePriorite = ordrePriorite;
        this.type = type;
    }

    @Override
    public int compareTo(CAKeyPosteContainer o) {
        int ordrePrioriteInt = new Integer(ordrePriorite);
        int o_ordrePrioriteInt = new Integer(o.getOrdrePriorite());

        if (ordrePrioriteInt < o_ordrePrioriteInt) {
            return -1;
        } else if (ordrePrioriteInt == o_ordrePrioriteInt) {
            if (numeroRubriqueIrrecouvrable.compareTo(o.getNumeroRubriqueIrrecouvrable()) < 0) {
                return -1;
            } else if (numeroRubriqueIrrecouvrable.equals(o.getNumeroRubriqueIrrecouvrable())) {
                if (annee.compareTo(o.getAnnee()) < 0) {
                    return -1;
                } else if (annee.equals(o.getAnnee())) {
                    int iType = Integer.parseInt(type.getCodeSystem());
                    int iOType = Integer.parseInt(o.getType().getCodeSystem());
                    if (iType < iOType) {
                        return -1;
                    } else if (iType == iOType) {
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
        } else {
            return 1;
        }
    }

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
        CAKeyPosteContainer other = (CAKeyPosteContainer) obj;
        if (annee == null) {
            if (other.annee != null) {
                return false;
            }
        } else if (!annee.equals(other.annee)) {
            return false;
        }
        if (numeroRubriqueIrrecouvrable == null) {
            if (other.numeroRubriqueIrrecouvrable != null) {
                return false;
            }
        } else if (!numeroRubriqueIrrecouvrable.equals(other.numeroRubriqueIrrecouvrable)) {
            return false;
        }
        if (ordrePriorite == null) {
            if (other.ordrePriorite != null) {
                return false;
            }
        } else if (!ordrePriorite.equals(other.ordrePriorite)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    public Integer getAnnee() {
        return annee;
    }

    public String getNumeroRubriqueIrrecouvrable() {
        return numeroRubriqueIrrecouvrable;
    }

    public String getOrdrePriorite() {
        return ordrePriorite;
    }

    public String getStringValue(String separator) {
        String stringValue = "";
        stringValue += getOrdrePriorite();
        stringValue += separator + getNumeroRubriqueIrrecouvrable();
        stringValue += separator + getAnnee();
        stringValue += separator + getType();
        return stringValue;
    }

    public CATypeLigneDePoste getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((annee == null) ? 0 : annee.hashCode());
        result = (prime * result)
                + ((numeroRubriqueIrrecouvrable == null) ? 0 : numeroRubriqueIrrecouvrable.hashCode());
        result = (prime * result) + ((ordrePriorite == null) ? 0 : ordrePriorite.hashCode());
        result = (prime * result) + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        result += "annee = " + annee + " \n";
        result += "numeroRubriqueIrrecouvrable = " + numeroRubriqueIrrecouvrable + " \n";
        result += "ordrePriorite = " + ordrePriorite + " \n";
        result += "type = " + type + " \n";
        return result;
    }

}