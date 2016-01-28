package globaz.perseus.vb.statistiques;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

public class PFStatsDetailsViewBean extends BJadePersistentObjectViewBean {

    private String dateDebut = null;
    private String dateFin = null;
    private String eMailAdresse = null;

    public PFStatsDetailsViewBean() {
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

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getDateFin() {
        return dateFin;
    }

}
