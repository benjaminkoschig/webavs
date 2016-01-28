package globaz.ij.acor;

import globaz.globall.db.BSession;
import globaz.ij.acor.adapter.IJAdapterFactory;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.IJPrononce;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAbstractACORBatchFilePrinter;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJACORBatchFilePrinter extends PRAbstractACORBatchFilePrinter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static IJACORBatchFilePrinter INSTANCE;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut instance.
     * 
     * @return la valeur courante de l'attribut instance
     */
    public static final synchronized IJACORBatchFilePrinter getInstance() {
        if (IJACORBatchFilePrinter.INSTANCE == null) {
            IJACORBatchFilePrinter.INSTANCE = new IJACORBatchFilePrinter();
        }

        return IJACORBatchFilePrinter.INSTANCE;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJACORBatchFilePrinter.
     */
    private IJACORBatchFilePrinter() {
        // ne peut etre instancie
    }

    public void printBatchFileDecomptes(Map fileContent, BSession session, IJBaseIndemnisation baseIndemnisation,
            IJIJCalculee ijCalculee, String dossierRacineAcor) throws PRACORException {
        try {
            printBatchFile(fileContent, session,
                    IJAdapterFactory.getInstance(session).createAdapter(session, baseIndemnisation, ijCalculee),
                    dossierRacineAcor);
        } catch (Exception e) {
            throw new PRACORException("impossible de charger l'adapteur pour cette ij calculee", e);
        }
    }

    /**
     * inscrit le fichier bat pour le calcul avec ACOR des decomptes (prestations) pour la base d'indemnisation et
     * l'ijCalculee correspondante.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @param baseIndemnisation
     *            DOCUMENT ME!
     * @param ijCalculee
     *            DOCUMENT ME!
     * @param dossierRacineAcor
     *            DOCUMENT ME!
     * 
     * @deprecated replaced by printBatchFileDecomptes(Map fileContent,...)
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    // public void printBatchFileDecomptes(PrintWriter writer, BSession session, IJBaseIndemnisation baseIndemnisation,
    // IJIJCalculee ijCalculee, String dossierRacineAcor) throws PRACORException {
    // try {
    // printBatchFile(writer, session, IJAdapterFactory.getInstance(session).createAdapter(session,
    // baseIndemnisation, ijCalculee), dossierRacineAcor);
    // } catch (Exception e) {
    // throw new PRACORException("impossible de charger l'adapteur pour cette ij calculee", e);
    // }
    // }

    @Deprecated
    public void printBatchFileIJ(Map fileContent, BSession session, IJPrononce prononce, String dossierRacineAcor)
            throws PRACORException {
        printBatchFile(fileContent, session, IJAdapterFactory.getInstance(session).createAdapter(session, prononce),
                dossierRacineAcor);
    }

    /**
     * inscrit le fichier bat pour le calcul avec ACOR des ijcalculees a partir du prononce correspondant.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * @param dossierRacineAcor
     *            DOCUMENT ME!
     * 
     * @deprecated replaced by printBatchFileIJ(Map fileContent,...)
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    // public void printBatchFileIJ(PrintWriter writer, BSession session, IJPrononce prononce, String dossierRacineAcor)
    // throws PRACORException {
    // printBatchFile(writer, session, IJAdapterFactory.getInstance(session).createAdapter(session, prononce),
    // dossierRacineAcor);
    // }

}
