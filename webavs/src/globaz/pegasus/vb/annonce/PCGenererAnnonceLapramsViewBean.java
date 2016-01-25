package globaz.pegasus.vb.annonce;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

public class PCGenererAnnonceLapramsViewBean extends BJadePersistentObjectViewBean {

    private String annee = null;

    private String mailAddress = null;

    private String noSemaine = null;

    public PCGenererAnnonceLapramsViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getAnnee() {
        return annee;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public String getNoSemaine() {
        return noSemaine;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        // return new BSpy(this.getSession());
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setMailAddress(String eMailAddress) {
        mailAddress = eMailAddress;
    }

    public void setNoSemaine(String noSemaine) {
        this.noSemaine = noSemaine;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
