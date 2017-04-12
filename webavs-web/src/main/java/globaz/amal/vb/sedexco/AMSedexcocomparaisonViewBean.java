package globaz.amal.vb.sedexco;

import globaz.amal.process.annonce.AnnonceSedexCOProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;

public class AMSedexcocomparaisonViewBean extends BJadePersistentObjectViewBean {
    /**
     * email (par défaut celle de l'utilisateur)
     */
    private String email = null;
    private String idTiersCM = null;
    private String annee = null;

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdTiersCM() {
        return idTiersCM;
    }

    public void setIdTiersCM(String idTiersCM) {
        this.idTiersCM = idTiersCM;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void launchGenerationComparaison() {
        AnnonceSedexCOProcess process = new AnnonceSedexCOProcess();
        process.setSession(BSessionUtil.getSessionFromThreadContext());
        process.setEmail(getEmail());
        process.setAnnee(getAnnee());
        process.setIdTiersCM(getIdTiersCM());
        process.run();
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

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
    public void update() throws Exception {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

}
