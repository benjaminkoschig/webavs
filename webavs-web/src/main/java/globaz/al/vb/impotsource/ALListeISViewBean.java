package globaz.al.vb.impotsource;

import ch.globaz.al.business.services.ALRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import globaz.vulpecula.vb.listes.PTListeProcessViewBean;
import java.util.List;
import ch.globaz.al.business.constantes.ALCSCantons;

public class ALListeISViewBean extends PTListeProcessViewBean {
    private static final String CS_GROUPE_CANTONS_AF = "ALCANTON";
    private static final String CS_GROUPE_LISTES_AF = "PTLISTAF";
    private List<CodeSystem> cantons;
    private List<CodeSystem> listes;
    private List<Administration> caissesAF;

    private String caisseAF;
    private String dateDebut;
    private String dateFin;
    private String canton;
    private String typeListe;

    @Override
    public void retrieve() throws Exception {
        cantons = CodeSystemUtil.getCodesSystemesForFamille(CS_GROUPE_CANTONS_AF);
        listes = CodeSystemUtil.getCodesSystemesForFamille(CS_GROUPE_LISTES_AF);
        caissesAF = ALRepositoryLocator.getAdministrationRepository().findAllCaissesAF();
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

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
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
