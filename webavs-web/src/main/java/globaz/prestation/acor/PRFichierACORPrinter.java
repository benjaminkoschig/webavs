package globaz.prestation.acor;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * Cette interface rend possible l'écriture ligne par ligne d'un contenu dans un fichier devant porter un certain nom.
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
     * Retourne vrai s'il y a encore une ligne à écrire.
     * 
     * @return vrai si...
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public boolean hasLignes() throws PRACORException;

    /**
     * retourne vrai s'il faut tout de meme creer un fichier vide portant le nom donné même si ce fichier n'a aucune
     * ligne.
     * 
     * @return vrai s'il faut créer un fichier même vide.
     */
    public boolean isForcerFichierVide();

    /**
     * Ecrit une ligne du contenu du fichier dans le stringBuffer.
     * 
     * @param StringBuffer
     *            le buffer dans lequel écrire
     * 
     * @throws PRACORException
     *             Si la ligne ne peut être écrite ou s'il n'y a plus de lignes à écrire.
     * 
     * 
     */

    public void printLigne(StringBuffer cmd) throws PRACORException;
}
