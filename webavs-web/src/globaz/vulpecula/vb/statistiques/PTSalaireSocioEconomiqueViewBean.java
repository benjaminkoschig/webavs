package globaz.vulpecula.vb.statistiques;

import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.domain.models.common.Date;

public class PTSalaireSocioEconomiqueViewBean extends BJadeSearchObjectELViewBean {

    // Attributs utilisés pour la JSP
    private String email = JadeThread.currentUserEmail();
    private String periodeDebut = Date.now().getMoisAnneeFormatte();
    private String periodeFin = Date.now().getMoisAnneeFormatte();

    public String getEmail() {
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

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

}
