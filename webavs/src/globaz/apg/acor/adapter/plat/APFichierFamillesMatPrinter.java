package globaz.apg.acor.adapter.plat;

import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import java.io.PrintWriter;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Sous-classe permettant la création du fichier FAMILLES pour une prestation maternité.
 * </p>
 * 
 * @author vre
 */
public class APFichierFamillesMatPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private boolean hasLignes = true;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APAbstractFichierDroitPrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    protected APFichierFamillesMatPrinter(APACORDroitMatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected APACORDroitMatAdapter adapter() {
        return (APACORDroitMatAdapter) parent;
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
        return hasLignes;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param writer
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     * @deprecated
     */
    @Deprecated
    public void printLigne(PrintWriter writer) throws PRACORException {
        if (adapter().isMariSaisi()) {
            // 1. numéro AVS de l'homme
            writeAVS(writer, adapter().mari().getNoAVS());
        } else {
            writeAVS(writer, adapter().noAVSPere());
        }

        // 2. numéro AVS femme
        writeAVS(writer, adapter().numeroAVSAssure());

        // 3. type de lien
        writeEntier(writer, PRACORConst.CA_MARIE);

        // 4. date de début du mariage
        writeDate(writer, PRACORConst.CA_DATE_VIDE);

        // 5. date de fin de lien
        writeDate(writer, PRACORConst.CA_DATE_VIDE);

        // 6. pension alimentaire
        writeBoolean(writer, false);

        // 7. demi-rente de couple demandée
        writeBooleanSansFinDeChamp(writer, false);

        hasLignes = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.PRFichierACORPrinter#printLigne(java.lang.StringBuffer )
     */
    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {
        if (adapter().isMariSaisi()) {
            // 1. numéro AVS de l'homme
            writeAVS(writer, adapter().mari().getNoAVS());
        } else {
            writeAVS(writer, adapter().noAVSPere());
        }

        // 2. numéro AVS femme
        writeAVS(writer, adapter().numeroAVSAssure());

        // 3. type de lien
        writeEntier(writer, PRACORConst.CA_MARIE);

        // 4. date de début du mariage
        writeDate(writer, PRACORConst.CA_DATE_VIDE);

        // 5. date de fin de lien
        writeDate(writer, PRACORConst.CA_DATE_VIDE);

        // 6. pension alimentaire
        writeBoolean(writer, false);

        // 7. demi-rente de couple demandée
        writeBooleanSansFinDeChamp(writer, false);

        hasLignes = false;
    }
}
