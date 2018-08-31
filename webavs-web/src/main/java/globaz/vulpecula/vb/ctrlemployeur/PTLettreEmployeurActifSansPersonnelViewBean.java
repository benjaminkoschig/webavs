package globaz.vulpecula.vb.ctrlemployeur;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;

/**
 * 
 * @author jwe
 * 
 * 
 */
public class PTLettreEmployeurActifSansPersonnelViewBean extends BJadeSearchObjectELViewBean {
    private String dateReference = defineDateRef();
    private String dateEnvoi = Date.now().getSwissValue();
    private String email;
    private boolean launched = false;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {

    }

    @Override
    public void setId(String newId) {

    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getDateReference() {
        return dateReference;
    }

    public void setDateReference(String dateReference) {
        this.dateReference = dateReference;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getMessageProcessusLance() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("LISTE_PROCESSUS_LANCE");
    }

    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    private String defineDateRef() {
        Date.now().getYear();
        return String.valueOf(Date.now().getYear() - 1);
    }

}
