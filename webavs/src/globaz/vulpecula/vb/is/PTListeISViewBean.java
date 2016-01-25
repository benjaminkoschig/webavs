package globaz.vulpecula.vb.is;

import globaz.vulpecula.vb.listes.PTListeProcessViewBean;
import java.util.List;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.web.servlet.PTConstants;

public class PTListeISViewBean extends PTListeProcessViewBean {
    private List<CodeSystem> cantons;
    private List<CodeSystem> listes;
    private List<Administration> caissesAF;

    private String caisseAF;
    private String annee;
    private String canton;
    private String typeListe;

    @Override
    public void retrieve() throws Exception {
        cantons = CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_CANTONS_AF);
        listes = CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_LISTES_AF);
        caissesAF = VulpeculaRepositoryLocator.getAdministrationRepository().findAllCaissesAF();
    }

    public List<CodeSystem> getCantons() {
        return cantons;
    }

    public List<CodeSystem> getListes() {
        return listes;
    }

    public String getCaisseAF() {
        return caisseAF;
    }

    public void setCaisseAF(String caisseAF) {
        this.caisseAF = caisseAF;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getTypeListe() {
        return typeListe;
    }

    public void setTypeListe(String typeListe) {
        this.typeListe = typeListe;
    }

    public int getCurrentYear() {
        return Annee.getCurrentYear().getValue();
    }

    public List<Administration> getCaissesAF() {
        return caissesAF;
    }

    public String getCantonDefaut() {
        // WARNING: Il faudra peut-être un jour externaliser cette propriété
        return ALCSCantons.VS;
    }
}
