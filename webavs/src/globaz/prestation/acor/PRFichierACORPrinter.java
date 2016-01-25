package globaz.prestation.acor;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * Cette interface rend possible l'�criture ligne par ligne d'un contenu dans un fichier devant porter un certain nom.
 * </p>
 * 
 * @author vre
 */
public interface PRFichierACORPrinter {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /** libere les ressources associees avec ce fichier. */
    public void dispose();

    /**
     * retourne le nom que devrait porter le fichier.
     * 
     * @return la valeur courante de l'attribut nom fichier
     */
    public String getNomFichier();

    /**
     * Retourne vrai s'il y a encore une ligne � �crire.
     * 
     * @return vrai si...
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public boolean hasLignes() throws PRACORException;

    /**
     * retourne vrai s'il faut tout de meme creer un fichier vide portant le nom donn� m�me si ce fichier n'a aucune
     * ligne.
     * 
     * @return vrai s'il faut cr�er un fichier m�me vide.
     */
    public boolean isForcerFichierVide();

    /**
     * Ecrit une ligne du contenu du fichier dans le stringBuffer.
     * 
     * @param StringBuffer
     *            le buffer dans lequel �crire
     * 
     * @throws PRACORException
     *             Si la ligne ne peut �tre �crite ou s'il n'y a plus de lignes � �crire.
     * 
     * 
     */

    public void printLigne(StringBuffer cmd) throws PRACORException;
}
