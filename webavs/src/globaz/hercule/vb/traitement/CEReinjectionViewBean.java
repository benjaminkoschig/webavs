package globaz.hercule.vb.traitement;

import globaz.hercule.db.CEAbstractViewBean;

/**
 * Bean représentant l'écrans de réinjection
 * 
 * @author JPA
 * @revision JPA juin 2010
 */
public class CEReinjectionViewBean extends CEAbstractViewBean {
    private String eMailAddress = "";
    private String filename = "";

    /**
     * Constructeur de CEReinjectionViewBean
     */
    public CEReinjectionViewBean() {
        super();
    }

    public String getEMailAddress() {
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
