package globaz.prestation.acor.plat;

import globaz.prestation.acor.PRACORException;
import java.io.PrintWriter;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe permettant l'�criture d'un fichier vide. Cette classe est par exemple utile pour l'�criture du fichier
 * DEM_GEDO d'ACOR. En effet ce fichier est sp�cifique � la CSC. Cependant ACOR exige sa pr�sence pour faire le calcul,
 * un fichier vide doit alors �tre cr��.
 * </p>
 * 
 * @author vre
 */
public class PRFichierVidePrinter extends PRAbstractFichierPlatPrinter {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe FichierDemGdoWriter.
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
        // cette m�thode ne devrait jamais �tre appell�e
    }

    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        // cette m�thode ne devrait jamais �tre appell�e
    }
}
