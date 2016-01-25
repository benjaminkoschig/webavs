package globaz.babel.api;

import java.util.Iterator;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * Une interface simplifi�e d'acc�s � une liste de textes.
 * </p>
 * 
 * @see globaz.babel.api.ICTDocument
 * @author vre
 */
public interface ICTListeTextes {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne le texte � la position 'idPosition'
     * 
     * @param idPosition
     *            la position du texte dont on veut le texte.
     * 
     * @return la valeur courante de l'attribut texte ou null si non trouv�
     */
    public ICTTexte getTexte(int idPosition);

    /**
     * Retourne un iterateur sur tous les textes de cette liste class� par ordre de position croissant.
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
