package globaz.perseus.vb.statistiques;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;

public class PFStatsOFSViewBean extends BJadePersistentObjectViewBean {

    private String anneeEnquete = null;
    private String eMailAdresse = null;

    public PFStatsOFSViewBean() {
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

    public String getAnneeEnquete() {
        if ((anneeEnquete == null) || (anneeEnquete == "")) {
            anneeEnquete = JACalendar.todayJJsMMsAAAA().substring(6);
        }
        return anneeEnquete;
    }

    public String geteMailAdresse() {
        return eMailAdresse;
    }

    @Override
    public String getId() {
        return null;
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setAnneeEnquete(String anneeEnquete) {
        this.anneeEnquete = anneeEnquete;
    }

    public void seteMailAdresse(String eMailAdresse) {
        this.eMailAdresse = eMailAdresse;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
