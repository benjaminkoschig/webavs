package globaz.ij.acor.adapter.plat;

import globaz.ij.acor.adapter.IJAttestationsJoursAdapter;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import java.io.PrintWriter;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * un printer qui cree les donnees pour le fichier decompte d'ACOR.
 * </p>
 * 
 * <p>
 * Les donnees
 * </p>
 * 
 * @author vre
 */
public class IJFichierDecomptePrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private boolean fini;

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
    public IJFichierDecomptePrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private IJACORBaseIndemnisationAdapter adapter() {
        return (IJACORBaseIndemnisationAdapter) parent;
    }

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
        if (hasLignes()) {
            IJBaseIndemnisation base = adapter().getBaseIndemnistation();
            IJIJCalculee ij = adapter().getIjCalculee();
            IJAttestationsJoursAdapter attestationsJours = new IJAttestationsJoursAdapter(base, ij);

            // 1. debut de date concernee
            writeDate(writer, attestationsJours.getDateDebutPeriode());

            // 2. fin de periode concernee
            writeDate(writer, attestationsJours.getDateFinPeriode());

            if (!attestationsJours.isCalendrier()) {
                // 3. nombre de jours en tant qu'interne
                writeEntier(writer, attestationsJours.getNbJoursInternes());

                // 4. nombre de jours en tant qu'externe
                writeEntier(writer, attestationsJours.getNbJoursExternes());
            } else {
                // 3. nombre de jours en tant qu'interne
                writeEntier(writer, "0");

                // 4. nombre de jours en tant qu'externe
                writeEntier(writer, "0");
            }

            // 5. imposition a la source
            writeEntier(writer, PRACORConst.csCantonToAcor(base.getCsCantonImpotSource()));

            // 6. versement séparé
            writeBoolean(writer, false); // ne pas separer les versement

            // 7. decompte rectificatif
            writeBoolean(writer, false); // ce champ n'a de toutes facons pas
            // d'effet sur le calcul dans
            // globaz.ij.acor

            // 8. détail des jours
            writeChaine(writer, attestationsJours.getAttestationsJours());

            // 9. nombre de jours d'interruption
            writeEntier(writer, attestationsJours.getNbJoursInterruption());

            // 10. raison de l'interruption
            writeEntierSansFinDeChamp(writer,
                    PRACORConst.csMotifInterruptionToAcor(getSession(), base.getCsMotifInterruption()));

            fini = true;
        }
    }

    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        if (hasLignes()) {
            IJBaseIndemnisation base = adapter().getBaseIndemnistation();
            IJIJCalculee ij = adapter().getIjCalculee();
            IJAttestationsJoursAdapter attestationsJours = new IJAttestationsJoursAdapter(base, ij);

            // 1. debut de date concernee
            writeDate(cmd, attestationsJours.getDateDebutPeriode());

            // 2. fin de periode concernee
            writeDate(cmd, attestationsJours.getDateFinPeriode());

            if (!attestationsJours.isCalendrier()) {
                // 3. nombre de jours en tant qu'interne
                writeEntier(cmd, attestationsJours.getNbJoursInternes());

                // 4. nombre de jours en tant qu'externe
                writeEntier(cmd, attestationsJours.getNbJoursExternes());
            } else {
                // 3. nombre de jours en tant qu'interne
                writeEntier(cmd, "0");

                // 4. nombre de jours en tant qu'externe
                writeEntier(cmd, "0");
            }

            // 5. imposition a la source
            writeEntier(cmd, PRACORConst.csCantonToAcor(base.getCsCantonImpotSource()));

            // 6. versement séparé
            writeBoolean(cmd, false); // ne pas separer les versement

            // 7. decompte rectificatif
            writeBoolean(cmd, false); // ce champ n'a de toutes facons pas
            // d'effet sur le calcul dans
            // globaz.ij.acor

            // 8. détail des jours
            writeChaine(cmd, attestationsJours.getAttestationsJours());

            // 9. nombre de jours d'interruption
            writeEntier(cmd, attestationsJours.getNbJoursInterruption());

            // 10. raison de l'interruption
            writeEntierSansFinDeChamp(cmd,
                    PRACORConst.csMotifInterruptionToAcor(getSession(), base.getCsMotifInterruption()));

            fini = true;
        }
    }
}
