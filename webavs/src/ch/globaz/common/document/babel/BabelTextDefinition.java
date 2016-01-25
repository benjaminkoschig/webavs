package ch.globaz.common.document.babel;

/**
 * 
 * Permet de d�finir la d�finition d'un texte qui utilise Babel
 * 
 * @author dma
 * 
 */
public interface BabelTextDefinition {

    /**
     * D�finit le niveau du texte
     * 
     * @return le niveau du texte
     */
    public int getNiveau();

    /**
     * D�finit la position du texte
     * 
     * @return la position du texte
     */
    public int getPosition();

    /**
     * 
     * @return La cl� utilis� dans le document, Identifiant du texte
     */
    public String getKey();

    /**
     * Utilis� en cas d'erreur
     * Peut �tre un exemple de texte
     * 
     * @return La description du texte
     */
    public String getDescription();

    /**
     * Permet de d�finir si on a forc� une valeur afin de contourner l'extraction du texte par le catalogue de texte et
     * d'utiliser la valeur pass�e en param�tre.
     * 
     * @return true si on a forc� la valeur, false si on reprend la valeur du catalogue de texte.
     */
    public boolean isForcedValue();

    /**
     * Permet de r�cup�rer la valeur forc�e en param�tre.
     * isForcedValue() doit �tre � true pour pouvoir utiliser getValue()
     * 
     * @return
     */
    public String getValue();

}
