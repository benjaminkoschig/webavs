package globaz.ij.vb.controleAbsences;

import globaz.framework.bean.JadeAbstractAjaxCrudDetailViewBean;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.ij.business.models.IJSimpleDossierControleAbsences;
import ch.globaz.ij.business.models.IJSimpleDossierControleAbsencesSearchModel;
import ch.globaz.ij.business.services.IJServiceLocator;

public class IJDossierControleAbsencesAjaxViewBean extends
        JadeAbstractAjaxCrudDetailViewBean<IJSimpleDossierControleAbsences, IJSimpleDossierControleAbsencesSearchModel> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String detailAssure;
    private IJSimpleDossierControleAbsences dossier;
    private String idBaseIndemnisation;
    private String idPrononce;
    private String idTiers;
    private String noNSS;
    private boolean isImposeALaSource;

    public IJDossierControleAbsencesAjaxViewBean() {
        super();

        dossier = new IJSimpleDossierControleAbsences();
        detailAssure = "";
        idBaseIndemnisation = "";
        idTiers = "";
        noNSS = "";
    }

    public BSpy getCreationSpy() {
        return (getCurrentEntity() != null) && !getCurrentEntity().isNew() ? new BSpy(getCurrentEntity()
                .getCreationSpy()) : new BSpy((BSession) getISession());
    }

    @Override
    public IJSimpleDossierControleAbsences getCurrentEntity() {
        return dossier;
    }

    public String getDateDebutFPI() {
        return dossier.getDateDebutFPI();
    }

    public String getDateDebutIJAI() {
        return dossier.getDateDebutIJAI();
    }

    public String getDetailAssure() {
        return detailAssure;
    }

    public String getIdBaseIndemnisation() {
        return idBaseIndemnisation;
    }

    public final String getIdDossierControleAbsences() {
        return dossier.getIdDossierControleAbsences();
    }

    public final String getIdPrononce() {
        return idPrononce;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public final boolean getIsHistorise() {
        return dossier.getIsHistorise();
    }

    /**
     * Utilisé pour géré l'affichage des champs : -> CS canton imposition -> taux imposition dans l'écran de calcul des
     * des 30/60/90 jours
     * 
     * @return Si l'impôt est prélevé à la source.
     */
    public final boolean getIsImposeALaSource() {
        return isImposeALaSource;
    }

    @Override
    public JadeCrudService<IJSimpleDossierControleAbsences, IJSimpleDossierControleAbsencesSearchModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return IJServiceLocator.getDossierControleAbsenceService();
    }

    public String getNoNSS() {
        return noNSS;
    }

    public void setNoNSS(String noNSS) {
        this.noNSS = noNSS;
    }

    @Override
    public void setCurrentEntity(IJSimpleDossierControleAbsences entite) {
        if (entite == null) {
            dossier = new IJSimpleDossierControleAbsences();
        } else {
            dossier = entite;
        }
    }

    public void setDateDebutFPI(String dateDebutFPI) {
        dossier.setDateDebutFPI(dateDebutFPI);
    }

    public void setDateDebutIJAI(String dateDebutIJAI) {
        dossier.setDateDebutIJAI(dateDebutIJAI);
    }

    public void setDetailAssure(String detailAssure) {
        this.detailAssure = detailAssure;
    }

    public void setIdBaseIndemnisation(String idBaseIndemnisation) {
        this.idBaseIndemnisation = idBaseIndemnisation;
    }

    public void setIdDossierControleAbsences(String idDossierControleAbsences) {
        dossier.setIdDossierControleAbsences(idDossierControleAbsences);
    }

    public final void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public final void setIsHistorise(boolean isHistorise) {
        dossier.setIsHistorise(isHistorise);
    }

    /**
     * @param Définit
     *            si l'impôt est prélevé à la source
     */
    public final void setIsImposeALaSource(boolean isImposeALaSource) {
        this.isImposeALaSource = isImposeALaSource;
    }

}
