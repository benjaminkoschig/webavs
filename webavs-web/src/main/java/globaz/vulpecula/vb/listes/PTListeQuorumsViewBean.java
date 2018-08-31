package globaz.vulpecula.vb.listes;

import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import globaz.jade.context.JadeThread;

public class PTListeQuorumsViewBean extends PTListeProcessViewBean {

    private String email;
    private String dateFrom = initialisationDateDebut();
    private String dateTo = initialisationDateFin();
    private Boolean detail = false;
    private List<Convention> conventions;
    private String codeConvention;

    @Override
    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void retrieve() throws Exception {
        conventions = VulpeculaRepositoryLocator.getConventionRepository().findAll();
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Boolean getDetail() {
        return detail;
    }

    public void setDetail(Boolean detail) {
        this.detail = detail;
    }

    public List<Convention> getConventions() {
        return conventions;
    }

    public String getCodeConvention() {
        return codeConvention;
    }

    public void setCodeConvention(String codeConvention) {
        this.codeConvention = codeConvention;
    }

    /**
     * Permet d'initaliser les dates de début.
     *
     * Correspond au 1er janvier de l'année précédente
     */
    public String initialisationDateDebut() {
        Date date = new Date();
        int annee = Integer.parseInt(date.getAnnee());

        return "01.01." + (annee - 1);
    }

    /**
     * Permet d'initaliser les dates de fin.
     *
     * Correspond au 31 décembre de l'années précédente
     */
    public String initialisationDateFin() {
        Date date = new Date();
        int annee = Integer.parseInt(date.getAnnee());

        return "31.12." + (annee - 1);
    }

}
