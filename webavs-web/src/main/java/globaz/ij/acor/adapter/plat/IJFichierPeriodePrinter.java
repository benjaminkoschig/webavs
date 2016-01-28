package globaz.ij.acor.adapter.plat;

import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFPeriode;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJFichierPeriodePrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private ISFMembreFamilleRequerant enfant;
    private Iterator enfants;
    private int idPeriode;
    private ISFPeriode[] periodes;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJFichierPeriodePrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public IJFichierPeriodePrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private IJACORPrononceAdapter adapter() {
        return (IJACORPrononceAdapter) parent;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     * 
     * 
     */
    @Override
    public boolean hasLignes() throws PRACORException {
        if (enfants == null) {
            enfants = adapter().enfants();
        }

        if ((periodes == null) || (idPeriode >= periodes.length)) {
            // s'il n'y a plus d'enfants, on arrete le processus
            if (!enfants.hasNext()) {
                return false;
            }

            // sinon on passe aux periodes de l'enfant suivant.
            idPeriode = 0;
            periodes = null;

            while (enfants.hasNext() && (periodes == null)) {
                enfant = (ISFMembreFamilleRequerant) enfants.next();

                try {
                    periodes = adapter().situationFamiliale(adapter().idTiersAssure()).getPeriodes(
                            enfant.getIdMembreFamille());
                } catch (Exception e) {
                    throw new PRACORException("impossible de trouver les periodes de l'enfant", e);
                }

                if ((periodes != null) && (periodes.length == 0)) {
                    periodes = null;
                }
            }
        }

        return (periodes != null) && (idPeriode < periodes.length);
    }

    /**
     * @param writer
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     * @deprecated
     */
    @Deprecated
    public void printLigne(PrintWriter writer) throws PRACORException {
        ISFPeriode periode = periodes[idPeriode++];

        // 1. le no AVS de l'assure
        writeAVS(writer, enfant.getNss());

        // 2. type de periode
        // writeAVS(writer, PRACORConst.CA_PERIODE_ETUDE);
        writeAVS(writer, periode.getType());

        // 3. date début
        writeDate(writer, periode.getDateDebut());

        // 4. date fin
        writeDate(writer, periode.getDateFin());

        // 5. inutilisé dans les ij
        writeChampVide(writer);

        // 6. champ inutilisé dans les ij
        writeChampVideSansFinDeChamp(writer);
    }

    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        ISFPeriode periode = periodes[idPeriode++];

        // 1. le no AVS de l'assure
        writeAVS(cmd, enfant.getNss());

        // 2. type de periode
        // writeAVS(writer, PRACORConst.CA_PERIODE_ETUDE);
        writeAVS(cmd, periode.getType());

        // 3. date début
        writeDate(cmd, periode.getDateDebut());

        // 4. date fin
        writeDate(cmd, periode.getDateFin());

        // 5. inutilisé dans les ij
        writeChampVide(cmd);

        // 6. champ inutilisé dans les ij
        writeChampVideSansFinDeChamp(cmd);
    }
}
