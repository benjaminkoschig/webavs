package globaz.cygnus.vb.pots;

import globaz.cygnus.db.pots.RFPotsPC;
import globaz.framework.bean.FWViewBeanInterface;

/**
 * author fha
 */
public class RFParametrageGrandeQDViewBean extends RFPotsPC implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /************ attributes ****************************/
    private String forDateDebut = "";
    private String forDateFin = "";
    private String forIdPotPC = "";

    private Boolean isUpdate = Boolean.FALSE;

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdPotPC() {
        return forIdPotPC;
    }

    public String getImageError() {
        return "/images/erreur.gif";
    }

    public String getImageSuccess() {
        return "/images/ok.gif";
    }

    /************ methods ****************************/

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdPotPC(String forIdPotPC) {
        this.forIdPotPC = forIdPotPC;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

}
