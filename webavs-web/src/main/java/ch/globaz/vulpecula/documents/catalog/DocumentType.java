package ch.globaz.vulpecula.documents.catalog;

/**
 * Représente les types de document définies pour vulpecula dans Babel.
 * 
 * @author Arnaud Geiser (AGE) | Créé le 5 juin 2014
 * 
 */
public enum DocumentType {
    DECOMPTE_BVR("68017001"),
    DECOMPTE_SOMMATION("68017002"),
    DECOMPTE_TAXATION_OFFICE("68017003"),
    RECTIFICATIF("68017004"),
    AF("68017005"),
    PRESTATIONS("68017006"),
    LETTRE_CONTROLE_EMPLOYEUR("68017007"),
    AP("68017008"),
    LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL("68017009");

    private final String csCode;

    private DocumentType(final String csCode) {
        this.csCode = csCode;
    }

    /**
     * Retourne le code système représentant le type de document Babel.
     * 
     * @return String représentant le code système.
     */
    public String getCsCode() {
        return csCode;
    }
}
