package ch.globaz.common.document;

/**
 * Définit les fonction qui seront utilisé pour ajouter les textes dans le document
 * 
 * @author dma
 * 
 * @param <T> Définition du texte
 */
public interface TextMerger<T> {

    /**
     * Recherche le texte dans un catalogue et ajoute le texte trouvé dans le document
     * 
     * @param textDefinition
     */
    public void addTextToDocument(T textDefinition);

    /**
     * Ajoute le texte donnée en paramétrer dans le document
     * 
     * @param textDefinition
     * @param textToMerge
     */
    public void addTextToDocument(T textDefinition, String textToMerge);

    /**
     * Doit retourner le gestionnaire de texte
     * 
     * @return TextGiver
     */
    public TextGiver<T> getTextGiver();
}
