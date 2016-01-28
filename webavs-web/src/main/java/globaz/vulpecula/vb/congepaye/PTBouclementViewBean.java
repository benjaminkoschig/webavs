package globaz.vulpecula.vb.congepaye;

import globaz.vulpecula.vb.listes.PTListeProcessViewBean;
import java.util.List;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.util.I18NUtil;

public class PTBouclementViewBean extends PTListeProcessViewBean {
    private String annee;
    private String idConvention;
    private boolean miseAJour;

    private List<Convention> conventions;

    @Override
    public void retrieve() throws Exception {
        conventions = VulpeculaRepositoryLocator.getConventionRepository().findAll();
    }

    public List<Convention> getConventions() {
        return conventions;
    }

    public void setConventions(List<Convention> conventions) {
        this.conventions = conventions;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public boolean isMiseAJour() {
        return miseAJour;
    }

    public void setMiseAJour(boolean miseAJour) {
        this.miseAJour = miseAJour;
    }

    public boolean getMiseAJour() {
        return miseAJour;
    }

    public String getMessageAnneeRequise() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.BOUCLEMENT_CP_ANNEE_REQUISE);
    }

    public String getCurrentYear() {
        return Date.now().getAnnee();
    }
}
