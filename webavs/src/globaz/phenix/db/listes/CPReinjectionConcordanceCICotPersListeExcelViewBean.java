package globaz.phenix.db.listes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.CPAbstractViewBean;

/**
 * Bean repr�sentant l'�crans de r�injection
 * 
 * @author JPA
 * @revision JPA juin 2010
 */
public class CPReinjectionConcordanceCICotPersListeExcelViewBean extends CPAbstractViewBean {
    private String eMailAddress = "";
    private String filename = "";

    /**
     * Constructeur de CEReinjectionViewBean
     */
    public CPReinjectionConcordanceCICotPersListeExcelViewBean() {
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
