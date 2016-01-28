package globaz.prestation.application;

import globaz.globall.api.BIApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BApplication;
import globaz.globall.format.IFormatData;
import globaz.pyxis.application.TIApplication;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public abstract class PRAbstractApplication extends BApplication {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static IFormatData affileFormater = null;

    private static IFormatData avsFormater = null;
    /** DOCUMENT ME! */
    public static final String PROPERTY_VERSION_ACOR = "globaz.prestation.acor.version";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Renvoie un formater de numero d'affili�.
     * 
     * @return un formateur de num�ro d'affili� de type IFormatData
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static IFormatData getAffileFormater() throws Exception {
        // cf commentaire de getAvsFormater()

        if (affileFormater == null) {
            TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
            affileFormater = app.getAffileFormater();
        }

        return affileFormater;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Renvoie la BIApplication identifi�e dans le syst�me par l'applicationId
     * 
     * @param applicationId
     *            Identifiant de l'application � retourner
     * 
     * @return la BIApplication identifi�e dans le syst�me par l'applicationId
     */
    public static BIApplication getApplication(String applicationId) throws Exception {
        return globaz.globall.db.GlobazServer.getCurrentSystem().getApplication(applicationId);
    }

    /**
     * Renvoie un formater AVS.
     * 
     * @return un formateurAVS de type IFormatData
     * 
     * @throws Exception
     *             si on ne peut pas aller chercher le formateur a partir de l'application PYXIS
     */
    public static IFormatData getAvsFormater() throws Exception {
        // Ce formateur est pris � partir de PYXIS. Ainsi, au cas o� ce
        // formateur change,
        // pas besoin de remplacer notre impl�mentation. Une bonne partie des
        // applications
        // prennent ce formateur de PYXIS. Ceci changera probablement pour le
        // centraliser et
        // le rendre ind�pendant de PYXIS
        if (avsFormater == null) {
            TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
            avsFormater = app.getAvsFormater();
        }

        return avsFormater;
    }

    /**
     * Cr�e une nouvelle instance de la classe PRAbstractApplication.
     * 
     * @param id
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public PRAbstractApplication(String id) throws Exception {
        super(id);
    }
}
