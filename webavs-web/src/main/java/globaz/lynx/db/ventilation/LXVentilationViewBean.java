package globaz.lynx.db.ventilation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import java.math.BigDecimal;

public class LXVentilationViewBean extends LXVentilation implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idExterneCompte;

    public String getIdExterneCompte() {
        return idExterneCompte;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * Compare deux objets de ventilation.
     * 
     * @param compareWith
     * @return
     */
    public boolean isEqualsTo(LXVentilationViewBean compareWith) {
        if (!getIdCompte().equals(compareWith.getIdCompte())) {
            return false;
        } else if (!getIdCentreCharge().equals(compareWith.getIdCentreCharge())) {
            return false;
        } else if (!getLibelle().equals(compareWith.getLibelle())) {
            return false;
        } else if (!getCodeDebitCredit().equals(compareWith.getCodeDebitCredit())) {
            return false;
        } else if (new FWCurrency(getMontant()).compareTo(new FWCurrency(compareWith.getMontant())) != 0) {
            return false;
        } else if (new FWCurrency(getMontantMonnaie()).compareTo(new FWCurrency(compareWith.getMontantMonnaie())) != 0) {
            return false;
        } else if (new BigDecimal(getCoursMonnaie()).compareTo(new BigDecimal(compareWith.getCoursMonnaie())) != 0) {
            return false;
        } else {
            return true;
        }
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdExterneCompte(String idExterneCompte) {
        this.idExterneCompte = idExterneCompte;
    }
}
