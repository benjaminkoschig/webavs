package globaz.apg.module.calcul;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.vb.prestation.APPrestationViewBean;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import java.util.List;

public class APPrestationStandardLamatAcmAlphaData {

    private FWAction action;
    private List basesCalcul;
    private APDroitLAPG droit;
    private FWCurrency fraisDeGarde;
    private APPrestationViewBean viewBean;

    public APPrestationStandardLamatAcmAlphaData(APDroitLAPG droit, FWCurrency fraisDeGarde, List basesCalcul,
            FWAction action, APPrestationViewBean viewBean) {
        super();
        this.droit = droit;
        this.fraisDeGarde = fraisDeGarde;
        this.basesCalcul = basesCalcul;
        this.action = action;
        this.viewBean = viewBean;
    }

    public FWAction getAction() {
        return action;
    }

    public List getBasesCalcul() {
        return basesCalcul;
    }

    public APDroitLAPG getDroit() {
        return droit;
    }

    public FWCurrency getFraisDeGarde() {
        return fraisDeGarde;
    }

    public APPrestationViewBean getViewBean() {
        return viewBean;
    }

    public void setAction(FWAction action) {
        this.action = action;
    }

    public void setBasesCalcul(List basesCalcul) {
        this.basesCalcul = basesCalcul;
    }

    public void setDroit(APDroitLAPG droit) {
        this.droit = droit;
    }

    public void setFraisDeGarde(FWCurrency fraisDeGarde) {
        this.fraisDeGarde = fraisDeGarde;
    }

    public void setViewBean(APPrestationViewBean viewBean) {
        this.viewBean = viewBean;
    }

}
