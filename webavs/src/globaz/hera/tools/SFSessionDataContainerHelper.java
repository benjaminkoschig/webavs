/*
 * Cr�� le 8 sept. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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

    /** la cl� identifiant le value object du requerant sauv� dans la session */
    public static final String KEY_REQUERANT_DTO = "globaz.hera.tools.SFSessionDataContainerHelper.REQUERANT_DTO";

    /** la cl� identifiant le value object du requerant sauv� dans la session */
    public static final String KEY_VALEUR_RETOUR = "globaz.hera.tools.SFSessionDataContainerHelper.VALEUR_RETOUR";

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

    private SFSessionDataContainerHelper() {
        // ne peut �tre instanci�e.
    }
}
