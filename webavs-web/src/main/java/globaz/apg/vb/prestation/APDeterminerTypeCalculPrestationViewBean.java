package globaz.apg.vb.prestation;

import globaz.apg.enums.APTypeCalculPrestation;
import globaz.framework.bean.FWViewBean;
import globaz.framework.bean.FWViewBeanInterface;

public class APDeterminerTypeCalculPrestationViewBean extends FWViewBean implements FWViewBeanInterface {
    private String idDroit;
    private APTypeCalculPrestation typeCalculPrestation;
    private String typePrestation;

    public final String getTypePrestation() {
        return typePrestation;
    }

    public final void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    public final String getIdDroit() {
        return idDroit;
    }

    public final void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public final APTypeCalculPrestation getTypeCalculPrestation() {
        return typeCalculPrestation;
    }

    public final void setTypeCalculPrestation(APTypeCalculPrestation typeCalculPrestation) {
        this.typeCalculPrestation = typeCalculPrestation;
    }

}
