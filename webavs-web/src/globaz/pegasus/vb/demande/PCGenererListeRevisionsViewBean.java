package globaz.pegasus.vb.demande;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

public class PCGenererListeRevisionsViewBean extends BJadePersistentObjectViewBean {

    private String annee = null;
    private String eMailAddress = null;
    private String moisAnnee = null;

    public PCGenererListeRevisionsViewBean() {
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public void execute() {

    }

    public String getAnnee() {
        return annee;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMoisAnnee() {
        return moisAnnee;
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
        return new BSpy(getSession());
    }

    @Override
    public void retrieve() throws Exception {

    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }

}
