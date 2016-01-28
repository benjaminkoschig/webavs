package ch.globaz.common.document;

/**
 * D�finit les fonction qui seront utilis� pour ajouter les textes dans le document
 * 
 * @author dma
 * 
 * @param <T> D�finition du texte
 */
public interface TextMerger<T> {

    /**
     * Recherche le texte dans un catalogue et ajoute le texte trouv� dans le document
     * 
     * @param textDefinition
     */
    public void addTextToDocument(T textDefinition);

    /**
     * Ajoute le texte donn�e en param�trer dans le document
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
