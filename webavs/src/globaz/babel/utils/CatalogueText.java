package globaz.babel.utils;

/**
 * Conteneur pour la description d'un catalogue de texte. <br/>
 * <b>Ne contient aucun texte.</b> Pour récupérer le texte du catalogue, utilisez un objet de type
 * {@link BabelContainer}
 * 
 * @see {@link BabelContainer}
 * @author PBA
 */
public class CatalogueText {
    private String codeIsoLangue = "";
    private String csDomaine = "";
    private String csTypeDocument = "";
    private String nomCatalogue = "";

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getCsTypeDocument() {
        return csTypeDocument;
    }

    public String getKey() {
        StringBuilder key = new StringBuilder();
        key.append(csDomaine).append(".");
        key.append(csTypeDocument).append(".");
        key.append(nomCatalogue).append(".");
        key.append(codeIsoLangue);
        return key.toString();
    }

    public String getNomCatalogue() {
        return nomCatalogue;
    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setCsTypeDocument(String csTypeDocument) {
        this.csTypeDocument = csTypeDocument;
    }

    public void setNomCatalogue(String nomCatalogue) {
        this.nomCatalogue = nomCatalogue;
    }
}