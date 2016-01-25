package globaz.naos.db.taxeCo2;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * Bean représentant l'écrans de réinjection
 * 
 * @author JPA
 * @revision JPA juin 2010
 */
public class AFReinjectionListeExcelViewBean extends AFAbstractViewBean {
    private String eMailAddress = "";
    private String filename = "";

    /**
     * Constructeur de CEReinjectionViewBean
     */
    public AFReinjectionListeExcelViewBean() {
        super();
    }

    public String getEMailAddress() {
        if (JadeStringUtil.isBlank(eMailAddress)) {
            eMailAddress = getSession().getUserEMail();
        }
        return eMailAddress;
    }

    public String getFilename() {
        return filename;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
