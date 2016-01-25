package globaz.ij.acor.adapter.plat;

import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import java.io.PrintWriter;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe qui ecrit un fichier decompte pour la premiere phase du calcul d'un IJ avec ACOR.
 * </p>
 * 
 * @author vre
 */
public class IJFichierDecompteVidePrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private boolean fini = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJFichierDecomptePrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public IJFichierDecompteVidePrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() throws PRACORException {
        return !fini;
    }

    /**
     * @param writer
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     * 
     * @deprecated
     */
    @Deprecated
    public void printLigne(PrintWriter writer) throws PRACORException {
        // 1. debut de periode concernee
        writeDate(writer, PRACORConst.CA_DATE_VIDE);

        // 2. fin de periode concernee
        writeDate(writer, PRACORConst.CA_DATE_VIDE);

        try {
            // 3. nombre de jours en tant qu'interne (on inscrit 1 pour forcer
            // le calcul)
            writeEntier(writer, "1");

            // 4. nombre de jours en tant qu'externe (on inscrit 1 pour forcer
            // le calcul)
            writeEntier(writer, "1");
        } catch (Exception e) {
            throw new PRACORException("impossible de charger les mesures", e);
        }

        // 5. canton imposition source
        writeChaine(writer, PRACORConst.CA_CODE_3_VIDE);

        // TODO: 6. versement séparé, tester si doit mettre faux ou vrai
        writeBoolean(writer, false);

        // 7. decompte rectificatif (information non utilisée par ACOR)
        writeBoolean(writer, false);

        // 8. detail des jours a payer
        writeEntier(writer, PRACORConst.CA_ENTIER_VIDE);

        // 9. nombre de jours d'interruption
        writeEntier(writer, PRACORConst.CA_ENTIER_VIDE);

        // 10. raison de l'interruption
        writeChaine(writer, PRACORConst.CA_NON_RENSEIGNE);

        fini = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.PRFichierACORPrinter#printLigne(java.lang.StringBuffer )
     */
    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        // 1. debut de periode concernee
        writeDate(cmd, PRACORConst.CA_DATE_VIDE);

        // 2. fin de periode concernee
        writeDate(cmd, PRACORConst.CA_DATE_VIDE);

        try {
            // 3. nombre de jours en tant qu'interne (on inscrit 1 pour forcer
            // le calcul)
            writeEntier(cmd, "1");

            // 4. nombre de jours en tant qu'externe (on inscrit 1 pour forcer
            // le calcul)
            writeEntier(cmd, "1");
        } catch (Exception e) {
            throw new PRACORException("impossible de charger les mesures", e);
        }

        // 5. canton imposition source
        writeChaine(cmd, PRACORConst.CA_CODE_3_VIDE);

        // TODO: 6. versement séparé, tester si doit mettre faux ou vrai
        writeBoolean(cmd, false);

        // 7. decompte rectificatif (information non utilisée par ACOR)
        writeBoolean(cmd, false);

        // 8. detail des jours a payer
        writeEntier(cmd, PRACORConst.CA_ENTIER_VIDE);

        // 9. nombre de jours d'interruption
        writeEntier(cmd, PRACORConst.CA_ENTIER_VIDE);

        // 10. raison de l'interruption
        writeChaine(cmd, PRACORConst.CA_NON_RENSEIGNE);

        fini = true;
    }
}
