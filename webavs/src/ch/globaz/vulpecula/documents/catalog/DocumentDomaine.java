package ch.globaz.vulpecula.documents.catalog;

/**
 * Retourne les domaines définies pour vulpecula dans Babel.
 * 
 * @author Arnaud Geiser (AGE) | Créé le 5 juin 2014
 * 
 */
public enum DocumentDomaine {
    METIER("68016001");

    private final String csCode;

    private DocumentDomaine(final String csCode) {
        this.csCode = csCode;
    }

    /**
     * Retourne le code système représentant le domaine Babel.
     * 
     * @return String représentant le code système.
     */
    public String getCsCode() {
        return csCode;
    }
}
