/*
 * Créé le 09 août 2011
 */
package globaz.cygnus.vb;

import globaz.commons.nss.NSUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * @author fha
 * 
 *         DTO permettant de stocker le NSS. Chaque fois que l'utilisateur rentre dans le détail d'un dossier, le NSS
 *         est mis en mémoire dans ce DTO TODO extension à d'autres pages
 */
public class RFNSSDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String NSS = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFNSSDTO
     */
    public RFNSSDTO() {
    }

    /**
     * Crée une nouvelle instance de la classe RFNSSDTO
     * 
     * @param RFNSSDTO
     */
    public RFNSSDTO(RFNSSDTO NSSDTO) {
        NSS = NSSDTO.NSS;
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public String getNSS() {
        try {
            if (JadeStringUtil.isBlankOrZero(NSS)) {
                return "";
            }

            return NSUtil.formatAVSUnknown(NSS);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param string
     */
    public void setNSS(String string) {
        NSS = string;
    }

}
