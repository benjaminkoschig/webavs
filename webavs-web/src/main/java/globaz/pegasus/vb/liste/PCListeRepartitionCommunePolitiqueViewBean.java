package globaz.pegasus.vb.liste;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.common.domaine.Date;

public class PCListeRepartitionCommunePolitiqueViewBean extends BJadePersistentObjectViewBean {

    private String typeListe = "";
    private String dateMonthDebut = "";
    private String dateMonthFin = "";
    private String email = "";

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

    public String getTypeListe() {
        return typeListe;
    }

    public void setTypeListe(String typeListe) {
        this.typeListe = typeListe;
    }

    public String getDateMonthDebut() {
        if (JadeStringUtil.isEmpty(dateMonthDebut)) {
            dateMonthDebut = "01." + new Date().getYear();
        }
        return dateMonthDebut;
    }

    public void setDateMonthDebut(String dateMonthDebut) {
        this.dateMonthDebut = dateMonthDebut;
    }

    public String getDateMonthFin() {
        if (JadeStringUtil.isEmpty(dateMonthFin)) {
            dateMonthFin = new Date().getSwissMonthValue();
        }
        return dateMonthFin;
    }

    public void setDateMonthFin(String dateMonthFin) {
        this.dateMonthFin = dateMonthFin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
