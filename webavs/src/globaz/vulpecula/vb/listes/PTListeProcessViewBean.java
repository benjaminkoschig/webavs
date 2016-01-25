package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;

public class PTListeProcessViewBean extends BJadeSearchObjectELViewBean {
    private String email;
    private boolean launched;

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
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

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String arg0) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

}
