package ch.globaz.vulpecula.domain.models.common;

/***
 * ValueObject représentant un entier positif
 * 
 * @author Arnaud Geiser (AGE) | Créé le 10 déc. 2013
 */
public class EntierPositif implements ValueObject {
    private final int value;

    public EntierPositif(final String entier) {
        if (!this.isValid(entier)) {
            throw new IllegalArgumentException("Ce n'est pas un entier strictement positif");
        }
        value = Integer.parseInt(entier);
    }

    public EntierPositif(final int entier) {
        if (!this.isValid(entier)) {
            throw new IllegalArgumentException("Ce n'est pas un entier strictement positif");
        }
        value = entier;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof EntierPositif) {
            EntierPositif entier = (EntierPositif) obj;
            if (entier.getValue() == getValue()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value;
    }

    /**
     * Retourne le nombre d'heures
     * 
     * @return int représentant le nombre d'heures
     */
    public int getValue() {
        return value;
    }

    private boolean isValid(final String entier) {
        try {
            int entierAsInt = Integer.parseInt(entier);
            return isValid(entierAsInt);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValid(final int entier) {
        return entier > 0;
    }
}
