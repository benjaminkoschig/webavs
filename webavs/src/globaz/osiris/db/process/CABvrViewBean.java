package globaz.osiris.db.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.osiris.process.CAProcessBVR;

/**
 * @author user
 */
public class CABvrViewBean extends CAProcessBVR implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String distantDirectoryName;
    private String userSelectedFileName;

    /**
     * Constructor for CABvrViewBean.
     */
    public CABvrViewBean() {
        super();
    }

    /**
     * Constructor for CABvrViewBean.
     * 
     * @param parent
     */
    public CABvrViewBean(BProcess parent) {
        super(parent);
    }

    /**
     * Le nom du répertoire distant à lister.
     * 
     * @return
     */
    public String getDistantDirectoryName() {
        return distantDirectoryName;
    }

    public String getUserSelectedFileName() {
        return userSelectedFileName;
    }

    /**
     * Défini le nom du répertoire distant à lister.
     * 
     * @value FTP_OPAE_REMOTE_DIRECTORY or FTP_LSV_REMOTE_DIRECTORY
     * @param string
     */
    public void setDistantDirectoryName(String string) {
        distantDirectoryName = string;
    }

    public void setUserSelectedFileName(String userSelectedFileName) {
        this.userSelectedFileName = userSelectedFileName;
    }
}
