package globaz.prestation.tools;

import java.io.Serializable;
import javax.servlet.http.HttpSession;

/**
 * Classe : type_conteneur
 * 
 * <p>
 * Description : Session data container. Permet de stocker dans la session diff�rentes donn�es.
 * </p>
 * 
 * <p>
 * Date de cr�ation: 16 avr. 04
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
     * la cl� identifiant le value objetc des param�tres de recherche pour l'�cran de l'adaptation
     */
    public static final String KEY_ADAPTATION_DTO = "globaz.corvus.vb.adaptation.REAdaptationDTO";

    /**
     * la cl� identifiant le code systeme du type de prestation stock� comme attribut dans la session
     */
    public static final String KEY_CS_TYPE_PRESTATION = "globaz.apg.util.PRSessionDataContainerHelper.CS_TYPE_PRESTATION";

    /** la cl� identifiant le value object du droit sauv� dans la session */
    public static final String KEY_DROIT_DTO = "globaz.apg.util.PRSessionDataContainerHelper.DROIT_DTO";

    /**
     * la cl� identifiant le value object des param�tres de recherche d'un droit sauv� dans la session
     */
    public static final String KEY_DROIT_PARAMETRES_RC_DTO = "globaz.apg.util.PRSessionDataContainerHelper.DROIT_PARAMETRES_RC_DTO";

    /** la cl� identifiant le value object du droit APG sauv� dans la session */
    public static final String KEY_DROITAPG_DTO = "globaz.apg.util.PRSessionDataContainerHelper.DROITAPG_DTO";

    /**
     * la cl� identifiant le value object des param�tres de recherche dans les journalisations --> LIBRA
     */
    public static final String KEY_LIBRA_DTO = "globaz.libra.utils.LIRecherchesDTO";

    /** la cl� identifiant le value object du NSS sauv� dans la session --> IJ */
    public static final String KEY_NSS_DTO = "globaz.apg.util.PRSessionDataContainerHelper.NSS_DTO";

    /**
     * la cl� identifiant le value object des param�tres de recherche d'une prestation sauv� dans la session --> IJ
     */
    public static final String KEY_PRESTATION_PARAMETRES_IJ_RC_DTO = "globaz.apg.util.PRSessionDataContainerHelper.PRESTATION_PARAMETRES_IJ_RC_DTO";

    /**
     * la cl� identifiant le value object des param�tres de recherche d'une prestation sauv� dans la session --> APG
     */
    public static final String KEY_PRESTATION_PARAMETRES_RC_DTO = "globaz.apg.util.PRSessionDataContainerHelper.PRESTATION_PARAMETRES_RC_DTO";

    /**
     * la cl� identifiant le value object des param�tres de recherche d'un prononc� sauv� dans la session
     */
    public static final String KEY_PRONONCE_PARAMETRES_RC_DTO = "globaz.apg.util.PRSessionDataContainerHelper.PRONONCE_PARAMETRES_RC_DTO";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Method getData. Permet de r�cup�rer des donn�es stock�es en session, identifi� par une cl�.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param key
     *            La cl�
     * 
     * @return String La valeur � r�cup�rer
     */
    public static final Object getData(HttpSession session, String key) {

        return session.getAttribute(key);

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Method removeData. Supprime des donn�es stock�es en session.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param key
     *            la cl� servant d'identifiant.
     */
    public static final void removeData(HttpSession session, String key) {
        session.removeAttribute(key);
    }

    /**
     * Method setData. Set des donn�es dans la session. Si la cl� existe d�j�, les anciennes donn�es seront �cras�es.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param key
     *            la cl�
     * @param value
     *            la valeur a stock�e (doit �tre serializable)
     */
    public static final void setData(HttpSession session, String key, Serializable value) {
        session.setAttribute(key, value);
    }

    private PRSessionDataContainerHelper() {
        // ne peut �tre instanci�e.
    }
}
