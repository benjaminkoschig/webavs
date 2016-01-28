package globaz.vulpecula.vb.statistiques;

import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.util.I18NUtil;

public class PTSalaireQualificationViewBean extends BJadeSearchObjectELViewBean {

    // Attributs utilisés pour la JSP
    private String email = JadeThread.currentUserEmail();

    private List<Convention> conventions;
    private String idConvention = null;

    private List<String> codesQualifications;

    private String periodeDebut = Date.now().getMoisAnneeFormatte();
    private String periodeFin = Date.now().getMoisAnneeFormatte();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        conventions = VulpeculaRepositoryLocator.getConventionRepository().findAll();
    }

    @Override
    public void setId(String arg0) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public List<Convention> getConventions() {
        return conventions;
    }

    public void setConventions(List<Convention> conventions) {
        this.conventions = conventions;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public List<String> getCodesQualifications() {
        return codesQualifications;
    }

    public void setCodesQualifications(List<String> codesQualifications) {
        this.codesQualifications = codesQualifications;
    }

    public String getMessageQualificationRequis() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.MESSAGE_QUALIFICATION_REQUIS);
    }

}
