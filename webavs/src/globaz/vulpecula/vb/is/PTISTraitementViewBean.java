package globaz.vulpecula.vb.is;

import globaz.vulpecula.vb.listes.PTListeProcessViewBean;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.vulpecula.business.services.is.ProcessusAFServiceCRUD;
import ch.globaz.vulpecula.domain.models.is.ImpotSourceConstants;

public class PTISTraitementViewBean extends PTListeProcessViewBean {
    private String idProcessusPaiementDirect;
    private String idProcessusPaiementIndirect;

    public String getIdProcessusPaiementDirect() {
        return idProcessusPaiementDirect;
    }

    public void setIdProcessusPaiementDirect(String idProcessusPaiementDirect) {
        this.idProcessusPaiementDirect = idProcessusPaiementDirect;
    }

    public String getIdProcessusPaiementIndirect() {
        return idProcessusPaiementIndirect;
    }

    public void setIdProcessusPaiementIndirect(String idProcessusPaiementIndirect) {
        this.idProcessusPaiementIndirect = idProcessusPaiementIndirect;
    }

    public String getProcessusAFServiceCRUD() {
        return ProcessusAFServiceCRUD.class.getName();
    }

    public String getCsEtatProcessusTermine() {
        return ALCSProcessus.ETAT_TERMINE;
    }

    public String getConfigPaiementsDirects() {
        return ImpotSourceConstants.CONFIG_PAIEMENTS_DIRECTS;
    }

    public String getConfigPaiementsIndirects() {
        return ImpotSourceConstants.CONFIG_PAIEMENTS_INDIRECTS;
    }
}
