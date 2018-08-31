package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.domain.models.common.Annee;

public class PTListeCPsoumisLPPViewBean extends BJadeSearchObjectELViewBean {

    private String email;
    private String annee;

    public String getEmail() {
        if (JadeStringUtil.isEmpty(email)) {
            return BSessionUtil.getSessionFromThreadContext().getUserEMail();
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAnnee() {
        if (JadeStringUtil.isEmpty(annee)) {
            return String.valueOf(Annee.getCurrentYear().getValue());
        }
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void setId(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

}
