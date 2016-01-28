package ch.globaz.vulpecula.documents.catalog;

/**
 * Retourne les domaines d�finies pour vulpecula dans Babel.
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 5 juin 2014
 * 
 */
public enum DocumentDomaine {
    METIER("68016001");

    private final String csCode;

    private DocumentDomaine(final String csCode) {
        this.csCode = csCode;
    }

    /**
     * Retourne le code syst�me repr�sentant le domaine Babel.
     * 
     * @return String repr�sentant le code syst�me.
     */
    public String getCsCode() {
        return csCode;
    }
}
