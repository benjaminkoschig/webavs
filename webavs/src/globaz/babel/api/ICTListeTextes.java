package globaz.babel.api;

import java.util.Iterator;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * Une interface simplifiée d'accès à une liste de textes.
 * </p>
 * 
 * @see globaz.babel.api.ICTDocument
 * @author vre
 */
public interface ICTListeTextes {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne le texte à la position 'idPosition'
     * 
     * @param idPosition
     *            la position du texte dont on veut le texte.
     * 
     * @return la valeur courante de l'attribut texte ou null si non trouvé
     */
    public ICTTexte getTexte(int idPosition);

    /**
     * Retourne un iterateur sur tous les textes de cette liste classé par ordre de position croissant.
     * 
     * @return un iterateur jamais null
     */
    public Iterator iterator();

    /**
     * Retourne le nombre de textes a ce niveau
     * 
     * @return le nombre de textes a ce niveau.
     */
    public int size();
}
