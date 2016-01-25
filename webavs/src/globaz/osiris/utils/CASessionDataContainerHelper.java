package globaz.osiris.utils;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 * Classe : type_conteneur Description : Session data container. Permet de stocker dans la session différentes données.
 * Date de création: 16 avr. 04
 * 
 * @author scr
 */
public class CASessionDataContainerHelper {

    private class SessionData {
        private Map map = new HashMap();

        /**
         * Constructor for SessionData.
         */
        SessionData() {
            super();
        }

        Object getData(String key) {
            if (map.containsKey(key)) {
                return map.get(key);
            } else {
                return null;
            }
        }

        void removeData(String key) {
            map.remove(key);
        }

        void setData(String key, Object value) {
            map.put(key, value);
        }
    }

    public static final String KEY_LAST_PAIEMENT_ETRANGER = "1000";

    private static final String SESSION_DATA_CONTAINER = "osirisSessionDataContainer";

    /**
     * Constructor for CGSessionDataContainerHelper.
     */
    public CASessionDataContainerHelper() {
        super();
    }

    /**
     * Method getData. Permet de récupérer des données stockées en session, identifié par une clé.
     * 
     * @param session
     * @param key
     *            La clé
     * @return String La valeur à récupérer
     */
    public Object getData(HttpSession session, String key) {
        // Récupère l'id de la période comptable sélectionné et stock le dans la
        // session.
        SessionData sessionData = (SessionData) session
                .getAttribute(CASessionDataContainerHelper.SESSION_DATA_CONTAINER);

        if (sessionData == null) {
            return null;
        }

        return sessionData.getData(key);
    }

    /**
     * Method removeData. Supprime des données stockées en session.
     * 
     * @param session
     * @param key
     *            la clé servant d'identifiant.
     */
    public void removeData(HttpSession session, String key) {
        // Récupère l'id de la période comptable sélectionné et stock le dans la
        // session.
        SessionData sessionData = (SessionData) session
                .getAttribute(CASessionDataContainerHelper.SESSION_DATA_CONTAINER);

        if (sessionData != null) {
            sessionData.removeData(key);
        }
    }

    /**
     * Method setData. Set des données dans la session. Si la clé existe déjà, les anciennes données seront écrasées.
     * 
     * @param session
     * @param key
     *            la cké
     * @param value
     *            la valeur a stockée
     */
    public void setData(HttpSession session, String key, Object value) {

        // Récupère l'id de la période comptable sélectionné et stock le dans la
        // session.
        SessionData sessionData = (SessionData) session
                .getAttribute(CASessionDataContainerHelper.SESSION_DATA_CONTAINER);
        if (sessionData == null) {
            sessionData = new SessionData();
        }

        sessionData.setData(key, value);
        session.setAttribute(CASessionDataContainerHelper.SESSION_DATA_CONTAINER, sessionData);
    }

}
