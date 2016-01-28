package globaz.osiris.utils;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 * Classe : type_conteneur Description : Session data container. Permet de stocker dans la session diff�rentes donn�es.
 * Date de cr�ation: 16 avr. 04
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
     * Method getData. Permet de r�cup�rer des donn�es stock�es en session, identifi� par une cl�.
     * 
     * @param session
     * @param key
     *            La cl�
     * @return String La valeur � r�cup�rer
     */
    public Object getData(HttpSession session, String key) {
        // R�cup�re l'id de la p�riode comptable s�lectionn� et stock le dans la
        // session.
        SessionData sessionData = (SessionData) session
                .getAttribute(CASessionDataContainerHelper.SESSION_DATA_CONTAINER);

        if (sessionData == null) {
            return null;
        }

        return sessionData.getData(key);
    }

    /**
     * Method removeData. Supprime des donn�es stock�es en session.
     * 
     * @param session
     * @param key
     *            la cl� servant d'identifiant.
     */
    public void removeData(HttpSession session, String key) {
        // R�cup�re l'id de la p�riode comptable s�lectionn� et stock le dans la
        // session.
        SessionData sessionData = (SessionData) session
                .getAttribute(CASessionDataContainerHelper.SESSION_DATA_CONTAINER);

        if (sessionData != null) {
            sessionData.removeData(key);
        }
    }

    /**
     * Method setData. Set des donn�es dans la session. Si la cl� existe d�j�, les anciennes donn�es seront �cras�es.
     * 
     * @param session
     * @param key
     *            la ck�
     * @param value
     *            la valeur a stock�e
     */
    public void setData(HttpSession session, String key, Object value) {

        // R�cup�re l'id de la p�riode comptable s�lectionn� et stock le dans la
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
