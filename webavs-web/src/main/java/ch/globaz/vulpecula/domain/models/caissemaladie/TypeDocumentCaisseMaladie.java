package ch.globaz.vulpecula.domain.models.caissemaladie;

import java.util.Arrays;
import java.util.Collection;

public enum TypeDocumentCaisseMaladie {
    AVIS_MALADIE(68026001),
    CERTIFICAT_MEDICAL(68026002),
    CERTIFICAT_FINAL(68026003),
    FACTURES(68026004),
    DIVERS(68026005),
    FICHES_ANNONCE(68026006),
    DIVERS_REPRISE(68026007);

    public static final Collection<String> STANDARD;
    public static final Collection<String> FICHES_ANNONCES;
    static {
        STANDARD = Arrays.asList(AVIS_MALADIE.getValue(), CERTIFICAT_MEDICAL.getValue(), CERTIFICAT_FINAL.getValue(),
                FACTURES.getValue(), DIVERS.getValue());
        FICHES_ANNONCES = Arrays.asList(FICHES_ANNONCE.getValue());
    }

    private int value;

    private TypeDocumentCaisseMaladie(int value) {
        this.value = value;
    }

    /**
     * Retourne le code système représentant le type de document
     * 
     * @return String représentant un code système
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Construction de l'énumération à partir d'un code système
     * 
     * @param value
     *            String représentant un code système
     */
    public static TypeDocumentCaisseMaladie fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La valeur " + value
                    + " doit correspondre à un entier représentant un code système de type de facturation");
        }

        for (TypeDocumentCaisseMaladie e : TypeDocumentCaisseMaladie.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur : " + value
                + " ne correspond à aucun type de qualification connu");
    }

    /**
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(String value) {
        try {
            TypeDocumentCaisseMaladie.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
