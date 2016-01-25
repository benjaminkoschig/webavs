/*
 * Cr�� le 9 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.prestation.tools;

/**
 * @author vre
 */
public interface IPRCache {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Cherche dans le cache pour une valeur identifiee par la cl� transmise en argument.
     * 
     * @param key
     *            la cle d'identification de la valeur dans le cache.
     * 
     * @return la valeur identifiee par la cl� ou null si non trouv� ou cl� null.
     */
    public Object fetch(Object key);

    /**
     * getter pour l'attribut capacity.
     * 
     * @return la capacit� maximale de ce cache.
     */
    public int getCapacity();

    /**
     * getter pour l'attribut time out.
     * 
     * @return la temps en millisecondes avant l'expiration des donn�es contenues dans ce cache.
     */
    public long getTimeOut();

    /**
     * setter pour l'attribut time out.
     * 
     * @param timeOut
     *            une nouvelle valeur pour cet attribut ou 0 pour pas de time out.
     */
    public void setTimeOut(long timeOut);

    /**
     * Enregistre un objet dans le cache avec l'identifiant indiqu� par la cl�.
     * 
     * @param key
     *            une instance non nulle d'une classe QUI RETOURNE UNE VALEUR VALIDE DE HACHAGE !!!
     * @param value
     *            une valeur.
     * 
     * @throws NullPointerException
     *             si la cl� est nulle.
     * 
     * @see java.lang.Object#hashCode()
     */
    public void store(Object key, Object value) throws NullPointerException;
}
