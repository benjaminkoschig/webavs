package globaz.apg.acor;

import globaz.apg.acor.adapter.APAdapterFactory;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORAdapter;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAbstractACORBatchFilePrinter;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Class d'exportation d'un droit APG ou maternité dans un fichier bat
 * </p>
 * 
 * @author vre
 */
public final class APACORBatchFilePrinter extends PRAbstractACORBatchFilePrinter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static APACORBatchFilePrinter INSTANCE = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut instance.
     * 
     * @return la valeur courante de l'attribut instance
     */
    public static synchronized APACORBatchFilePrinter getInstance() {
        if (APACORBatchFilePrinter.INSTANCE == null) {
            APACORBatchFilePrinter.INSTANCE = new APACORBatchFilePrinter();
        }

        return APACORBatchFilePrinter.INSTANCE;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private APACORBatchFilePrinter() {
        // peut pas etre instancié
    }

    /**
     * 
     * crée un adapteur adéquat pour le droit et appelle la méthode
     * {@link PRAbstractACORBatchFilePrinter#printBatchFile(Map, BSession, PRACORAdapter, String) printBatchFile de
     * PRAbstractACORBatchFilePrinter}.
     * 
     * @param fileContent
     * @param session
     * @param droit
     * @param dossierRacineAcor
     * @throws PRACORException
     */
    public void printBatchFile(Map fileContent, BSession session, APDroitLAPG droit, String dossierRacineAcor)
            throws PRACORException {

        this.printBatchFile(fileContent, session, APAdapterFactory.getInstance(session).createAdapter(session, droit),
                dossierRacineAcor);
    }

    /**
     * crée un adapteur adéquat pour le droit et appelle la méthode
     * {@link PRAbstractACORBatchFilePrinter#printBatchFile(PrintWriter, BSession, PRACORAdapter, String) printBatchFile
     * de PRAbstractACORBatchFilePrinter}.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @param droit
     *            DOCUMENT ME!
     * @param dossierRacineAcor
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     * @deprecated
     */
    // public void printBatchFile(PrintWriter writer, BSession session, APDroitLAPG droit, String dossierRacineAcor)
    // throws PRACORException {
    // printBatchFile(writer, session, APAdapterFactory.getInstance(session).createAdapter(session, droit),
    // dossierRacineAcor);
    // }
}
