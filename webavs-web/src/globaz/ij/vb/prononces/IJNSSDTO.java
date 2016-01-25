/*
 * Créé le 14 mars 06
 */
package globaz.ij.vb.prononces;

import globaz.commons.nss.NSUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * @author hpe
 * 
 *         DTO permettant de stocker le NSS. Chaque fois que l'utilisateur rentre dans le détail d'un prononcé ou d'une
 *         prestation, ainsi que sur les autres options les permettant, le NSS est mis en mémoire dans ce DTO
 */
public class IJNSSDTO implements Serializable {

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
     * Crée une nouvelle instance de la classe IJPrononceParametresRCDTO.
     */
    public IJNSSDTO() {
    }

    /**
     * Crée une nouvelle instance de la classe IJPrononceParametresRCDTO.
     * 
     * @param paramPrononceDTO
     */
    public IJNSSDTO(IJNSSDTO NSSDTO) {
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
