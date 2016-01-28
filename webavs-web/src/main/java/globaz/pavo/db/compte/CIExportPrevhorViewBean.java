package globaz.pavo.db.compte;

import globaz.globall.util.JANumberFormatter;
import globaz.pavo.vb.CIAbstractPersistentViewBean;

public class CIExportPrevhorViewBean extends CIAbstractPersistentViewBean {

    private String annee = "";
    private String montantSeuilAnnuel = "";

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return the montantSeuilAnnuel
     */
    public String getMontantSeuilAnnuel() {
        return montantSeuilAnnuel;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @param annee
     *            the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @param montantSeuilAnnuel
     *            the montantSeuilAnnuel to set
     */
    public void setMontantSeuilAnnuel(String montantSeuilAnnuel) {
        this.montantSeuilAnnuel = JANumberFormatter.deQuote(montantSeuilAnnuel);
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
