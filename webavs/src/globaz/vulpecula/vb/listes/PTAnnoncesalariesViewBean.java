package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.domain.models.common.Date;

public class PTAnnoncesalariesViewBean extends BJadeSearchObjectELViewBean {
    private String email;
    private String periodicite;
    private String date;
    private boolean miseAJour = false;

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

    public String getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public boolean isMiseAJour() {
        return miseAJour;
    }

    public void setMiseAJour(boolean miseAJour) {
        this.miseAJour = miseAJour;
    }

    public String getDate() {
        if (date == null) {
            return Date.now().getSwissValue();
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
