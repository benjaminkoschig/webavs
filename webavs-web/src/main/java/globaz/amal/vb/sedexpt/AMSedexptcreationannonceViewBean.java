package globaz.amal.vb.sedexpt;

import globaz.amal.process.annonce.AnnonceSedexPTGenererAnnonceProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;

/**
 * View bean permettant de lancer le processus de création par lot des annonces PT
 */
public class AMSedexptcreationannonceViewBean extends BJadePersistentObjectViewBean {

    private String email = null;
    private String annee = null;
    public String dateReductionPrimeDe = null;
    public String dateReductionPrimeA = null;
    public Boolean simulation = Boolean.TRUE;

    /**
     * Email par défaut de l'utilisateur
     *
     * @return l'adresse email par defaut de l'utilisateur
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            return JadeThread.currentUserEmail();
        }
        return email;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }
}
