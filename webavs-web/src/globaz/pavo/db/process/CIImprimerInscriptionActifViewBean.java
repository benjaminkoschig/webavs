package globaz.pavo.db.process;

import globaz.pavo.vb.CIAbstractPersistentViewBean;

public class CIImprimerInscriptionActifViewBean extends CIAbstractPersistentViewBean {

    private String dateDebut = "";
    private String dateFin = "";

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }
}
