package globaz.naos.db.taxeCo2;

import globaz.naos.db.AFAbstractViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class AFLettreTaxeCo2ViewBean extends AFAbstractViewBean {

    private java.lang.String annee = new String();
    private java.lang.String email = new String();
    private java.lang.String idTaxeCo2 = new String();

    public AFLettreTaxeCo2ViewBean() throws java.lang.Exception {
    }

    public java.lang.String getAnnee() {
        return annee;
    }

    public java.lang.String getEmail() {
        return email;
    }

    public java.lang.String getIdTaxeCo2() {
        return idTaxeCo2;
    }

    public void setAnnee(java.lang.String annee) {
        this.annee = annee;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public void setIdTaxeCo2(java.lang.String idTaxeCo2) {
        this.idTaxeCo2 = idTaxeCo2;
    }

}
