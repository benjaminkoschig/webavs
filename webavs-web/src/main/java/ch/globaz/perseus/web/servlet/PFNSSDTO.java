package ch.globaz.perseus.web.servlet;

import globaz.commons.nss.NSUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class PFNSSDTO implements Serializable {
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
     * Crée une nouvelle instance de la classe PFNSSDTO
     */
    public PFNSSDTO() {
    }

    /**
     * Crée une nouvelle instance de la classe PFNSSDTO.
     * 
     * @param paramPrononceDTO
     */
    public PFNSSDTO(PFNSSDTO NSSDTO) {
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
