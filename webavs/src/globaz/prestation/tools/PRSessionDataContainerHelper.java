package globaz.prestation.tools;

import java.io.Serializable;
import javax.servlet.http.HttpSession;

/**
 * Classe : type_conteneur
 * 
 * <p>
 * Description : Session data container. Permet de stocker dans la session différentes données.
 * </p>
 * 
 * <p>
 * Date de création: 16 avr. 04
 * </p>
 * 
 * @author scr
 */
public class PRSessionDataContainerHelper implements Serializable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * la clé identifiant le value objetc des paramètres de recherche pour l'écran de l'adaptation
     */
    public static final String KEY_ADAPTATION_DTO = "globaz.corvus.vb.adaptation.REAdaptationDTO";

    /**
     * la clé identifiant le code systeme du type de prestation stockà comme attribut dans la session
     */
    public static final String KEY_CS_TYPE_PRESTATION = "globaz.apg.util.PRSessionDataContainerHelper.CS_TYPE_PRESTATION";

    /** la clé identifiant le value object du droit sauvé dans la session */
    public static final String KEY_DROIT_DTO = "globaz.apg.util.PRSessionDataContainerHelper.DROIT_DTO";

    /**
     * la clé identifiant le value object des paramètres de recherche d'un droit sauvé dans la session
     */
    public static final String KEY_DROIT_PARAMETRES_RC_DTO = "globaz.apg.util.PRSessionDataContainerHelper.DROIT_PARAMETRES_RC_DTO";

    /** la clé identifiant le value object du droit APG sauvé dans la session */
    public static final String KEY_DROITAPG_DTO = "globaz.apg.util.PRSessionDataContainerHelper.DROITAPG_DTO";

    /**
     * la clé identifiant le value object des paramètres de recherche dans les journalisations --> LIBRA
     */
    public static final String KEY_LIBRA_DTO = "globaz.libra.utils.LIRecherchesDTO";

    /** la clé identifiant le value object du NSS sauvé dans la session --> IJ */
    public static final String KEY_NSS_DTO = "globaz.apg.util.PRSessionDataContainerHelper.NSS_DTO";

    /**
     * la clé identifiant le value object des paramètres de recherche d'une prestation sauvé dans la session --> IJ
     */
    public static final String KEY_PRESTATION_PARAMETRES_IJ_RC_DTO = "globaz.apg.util.PRSessionDataContainerHelper.PRESTATION_PARAMETRES_IJ_RC_DTO";

    /**
     * la clé identifiant le value object des paramètres de recherche d'une prestation sauvé dans la session --> APG
     */
    public static final String KEY_PRESTATION_PARAMETRES_RC_DTO = "globaz.apg.util.PRSessionDataContainerHelper.PRESTATION_PARAMETRES_RC_DTO";

    /**
     * la clé identifiant le value object des paramètres de recherche d'un prononcé sauvé dans la session
     */
    public static final String KEY_PRONONCE_PARAMETRES_RC_DTO = "globaz.apg.util.PRSessionDataContainerHelper.PRONONCE_PARAMETRES_RC_DTO";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Method getData. Permet de récupérer des données stockées en session, identifié par une clé.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param key
     *            La clé
     * 
     * @return String La valeur à récupérer
     */
    public static final Object getData(HttpSession session, String key) {

        return session.getAttribute(key);

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Method removeData. Supprime des données stockées en session.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param key
     *            la clé servant d'identifiant.
     */
    public static final void removeData(HttpSession session, String key) {
        session.removeAttribute(key);
    }

    /**
     * Method setData. Set des données dans la session. Si la clé existe déjà, les anciennes données seront écrasées.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param key
     *            la clé
     * @param value
     *            la valeur a stockée (doit être serializable)
     */
    public static final void setData(HttpSession session, String key, Serializable value) {
        session.setAttribute(key, value);
    }

    private PRSessionDataContainerHelper() {
        // ne peut être instanciée.
    }
}
