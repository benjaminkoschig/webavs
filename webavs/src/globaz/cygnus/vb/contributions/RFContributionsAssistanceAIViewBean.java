package globaz.cygnus.vb.contributions;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * @author PBA
 */
public class RFContributionsAssistanceAIViewBean extends PRAbstractViewBeanSupport {

    private String detailRequerant = null;
    private String etatDossier = null;
    private String idDossier = null;
    private String idTierRequerant = null;
    private String periodeDossier = null;

    public String getDetailRequerant() {
        return detailRequerant;
    }

    public String getEtatDossier() {
        return etatDossier;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdTierRequerant() {
        return idTierRequerant;
    }

    public String getPeriodeDossier() {
        return periodeDossier;
    }

    public void setDetailRequerant(String detailRequerant) {
        this.detailRequerant = detailRequerant;
    }

    public void setEtatDossier(String etatDossier) {
        this.etatDossier = etatDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdTierRequerant(String idTierRequerant) {
        this.idTierRequerant = idTierRequerant;
    }

    public void setPeriodeDossier(String periodeDossier) {
        this.periodeDossier = periodeDossier;
    }

    @Override
    public boolean validate() {
        return false;
    }
}
