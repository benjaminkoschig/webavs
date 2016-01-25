package globaz.prestation.acor.plat;

import globaz.prestation.acor.PRACORException;
import java.io.PrintWriter;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe permettant l'écriture par défaut des fichiers de demandes ACOR. Ces fichiers ont la même structure quel que
 * soit le type de prestation (rentes, IJ, APG).
 * </p>
 * 
 * @author vre
 */
public class PRFichierDemandeDefautPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDepot;
    private String dateTraitement;
    protected boolean hasLignes = true; // il y a forcément un demandeur

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe FichierDemandeDefautPrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public PRFichierDemandeDefautPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date depot
     * 
     * @return la valeur courante de l'attribut date depot
     */
    public String getDateDepot() {
        return dateDepot;
    }

    /**
     * getter pour l'attribut date traitement
     * 
     * @return la valeur courante de l'attribut date traitement
     */
    public String getDateTraitement() {

        return dateTraitement;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() {
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
     */
    public void printLigne(PrintWriter writer) throws PRACORException {
        // 1. le no AVS de l'assuré faisant la demande de prestation
        writeAVS(writer, parent.numeroAVSAssure());

        // 2. le type de demande
        writeChaine(writer, parent.getTypeDemande());

        // 3. la date de traitement
        writeDate(writer, parent.getDateTraitement());

        // 4. la date de depot de la demande
        writeDate(writer, parent.getDateDepot());

        // 5. le type de calcul
        writeChaineSansFinDeChamp(writer, parent.getTypeCalcul());

        hasLignes = false;
    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {

        // 1. le no AVS de l'assuré faisant la demande de prestation
        writeAVS(writer, parent.numeroAVSAssure());

        // 2. le type de demande
        writeChaine(writer, parent.getTypeDemande());

        // 3. la date de traitement
        writeDate(writer, parent.getDateTraitement());

        // 4. la date de depot de la demande
        writeDate(writer, parent.getDateDepot());

        // 5. le type de calcul
        writeChaineSansFinDeChamp(writer, parent.getTypeCalcul());

        hasLignes = false;
    }

    /**
     * setter pour l'attribut date depot
     * 
     * @param dateDepot
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDepot(String dateDepot) {
        this.dateDepot = dateDepot;
    }

    /**
     * setter pour l'attribut date traitement
     * 
     * @param dateTraitement
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }
}
