/**
 * 
 */
package ch.globaz.al.liste.process;

/**
 * Objet avec le revenu minimal et l'année
 * 
 * @author est
 * 
 */
public class ALRevenuMinAnnee implements Comparable<ALRevenuMinAnnee> {
    private int revenuMinimal;
    private Integer annee;

    public ALRevenuMinAnnee(int annee, int revenuMinimal) {
        this.annee = annee;
        this.revenuMinimal = revenuMinimal;
    }

    /**
     * Créé un nouvel objet ALRevenuMinAnnee en augmentant l'année de 1 par rapport à l'appelant, garde le même revenu
     * minimal
     * Permet de peupler/compléter la liste des année liées a un revenu minimal
     * 
     * @param anneePrecedente
     * @return
     */
    public ALRevenuMinAnnee generateProchainRevenuMinAnnee() {

        return new ALRevenuMinAnnee(getAnnee() + 1, getRevenuMinimal());
    }

    public int getAnnee() {
        return annee;
    }

    public int getRevenuMinimal() {
        return revenuMinimal;
    }

    public void setRevenuMinimal(int revenuMinimal) {
        this.revenuMinimal = revenuMinimal;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    /**
     * Retourne l'année et le revenu minimal * 6 dans une chaine de caractère
     */
    @Override
    public String toString() {
        return "(" + annee + "," + revenuMinimal * 6 + ")";
    }

    @Override
    public int compareTo(ALRevenuMinAnnee o) {
        return annee.compareTo(o.getAnnee());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((annee == null) ? 0 : annee.hashCode());
        result = prime * result + revenuMinimal;
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
        ALRevenuMinAnnee other = (ALRevenuMinAnnee) obj;
        if (annee == null) {
            if (other.annee != null) {
                return false;
            }
        } else if (!annee.equals(other.annee)) {
            return false;
        }
        if (revenuMinimal != other.revenuMinimal) {
            return false;
        }
        return true;
    }

}