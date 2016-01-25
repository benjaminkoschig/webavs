package globaz.corvus.utils.pmt.mensuel;

import globaz.framework.util.FWCurrency;

/**
 * 
 * @author SCR
 * 
 */
public class RERetenueInfoUtil {

    String csTypeRetenue = null;
    String idRetenue = null;
    FWCurrency montantRetenu = null;

    public RERetenueInfoUtil(String csTypeRetenue, String idRetenue, FWCurrency montantRetenu) {
        this.csTypeRetenue = csTypeRetenue;
        this.idRetenue = idRetenue;
        this.montantRetenu = montantRetenu;
    }

    public String getCsTypeRetenue() {
        return csTypeRetenue;
    }

    public String getIdRetenue() {
        return idRetenue;
    }

    public FWCurrency getMontantRetenu() {
        return montantRetenu;
    }

    public void setCsTypeRetenue(String csTypeRetenue) {
        this.csTypeRetenue = csTypeRetenue;
    }

    public void setIdRetenue(String idRetenue) {
        this.idRetenue = idRetenue;
    }

    public void setMontantRetenu(FWCurrency montantRetenu) {
        this.montantRetenu = montantRetenu;
    }

}
