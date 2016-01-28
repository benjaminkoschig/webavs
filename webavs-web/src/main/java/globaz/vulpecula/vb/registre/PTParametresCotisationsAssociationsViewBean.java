package globaz.vulpecula.vb.registre;

import globaz.globall.db.BSpy;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;

public class PTParametresCotisationsAssociationsViewBean extends BJadeSearchObjectELViewBean {
    private String forLibelle = "";

    public String getForLibelle() {
        return forLibelle;
    }

    public void setForLibelle(String forLibelle) {
        this.forLibelle = forLibelle;
    }

    public PTParametresCotisationsAssociationsViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() throws Exception {
        throw new UnsupportedOperationException();
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
}
