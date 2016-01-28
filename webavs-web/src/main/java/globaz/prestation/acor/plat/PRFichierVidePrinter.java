package globaz.prestation.acor.plat;

import globaz.prestation.acor.PRACORException;
import java.io.PrintWriter;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe permettant l'écriture d'un fichier vide. Cette classe est par exemple utile pour l'écriture du fichier
 * DEM_GEDO d'ACOR. En effet ce fichier est spécifique à la CSC. Cependant ACOR exige sa présence pour faire le calcul,
 * un fichier vide doit alors être créé.
 * </p>
 * 
 * @author vre
 */
public class PRFichierVidePrinter extends PRAbstractFichierPlatPrinter {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe FichierDemGdoWriter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public PRFichierVidePrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() {
        return false;
    }

    /**
     * retourne vrai.
     * 
     * @return la valeur courante de l'attribut forcer fichier vide
     */
    @Override
    public boolean isForcerFichierVide() {
        return true;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param writer
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public void printLigne(PrintWriter writer) throws PRACORException {
        // cette méthode ne devrait jamais être appellée
    }

    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        // cette méthode ne devrait jamais être appellée
    }
}
