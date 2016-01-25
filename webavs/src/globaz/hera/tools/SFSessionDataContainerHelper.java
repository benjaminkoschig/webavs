/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.tools;

import java.io.Serializable;
import javax.servlet.http.HttpSession;

/**
 * @author mmu
 * 
 *         Copie de globaz.prestation.tools.PRSessionDataContainerHelper
 */
public class SFSessionDataContainerHelper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** la clé identifiant le value object du requerant sauvé dans la session */
    public static final String KEY_REQUERANT_DTO = "globaz.hera.tools.SFSessionDataContainerHelper.REQUERANT_DTO";

    /** la clé identifiant le value object du requerant sauvé dans la session */
    public static final String KEY_VALEUR_RETOUR = "globaz.hera.tools.SFSessionDataContainerHelper.VALEUR_RETOUR";

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

    private SFSessionDataContainerHelper() {
        // ne peut être instanciée.
    }
}
