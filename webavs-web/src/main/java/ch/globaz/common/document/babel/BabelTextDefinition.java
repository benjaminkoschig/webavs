package ch.globaz.common.document.babel;

/**
 * 
 * Permet de définir la définition d'un texte qui utilise Babel
 * 
 * @author dma
 * 
 */
public interface BabelTextDefinition {

    /**
     * Définit le niveau du texte
     * 
     * @return le niveau du texte
     */
    public int getNiveau();

    /**
     * Définit la position du texte
     * 
     * @return la position du texte
     */
    public int getPosition();

    /**
     * 
     * @return La clé utilisé dans le document, Identifiant du texte
     */
    public String getKey();

    /**
     * Utilisé en cas d'erreur
     * Peut être un exemple de texte
     * 
     * @return La description du texte
     */
    public String getDescription();

    /**
     * Permet de définir si on a forcé une valeur afin de contourner l'extraction du texte par le catalogue de texte et
     * d'utiliser la valeur passée en paramètre.
     * 
     * @return true si on a forcé la valeur, false si on reprend la valeur du catalogue de texte.
     */
    public boolean isForcedValue();

    /**
     * Permet de récupérer la valeur forcée en paramètre.
     * isForcedValue() doit être à true pour pouvoir utiliser getValue()
     * 
     * @return
     */
    public String getValue();

}
