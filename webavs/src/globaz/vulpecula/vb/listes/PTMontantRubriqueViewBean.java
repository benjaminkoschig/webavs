package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.domain.models.common.Date;

public class PTMontantRubriqueViewBean extends BJadeSearchObjectELViewBean {
    private String email;
    private String date;

    private boolean launched;

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

    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        if (date == null) {
            return Date.now().getMoisAnneeFormatte();
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public boolean isLaunched() {
        return launched;
    }
}
