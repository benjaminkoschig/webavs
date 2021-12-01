package globaz.ij.vb.controleAbsences;

import ch.globaz.ij.business.models.IJSimpleBaseIndemnisation;
import ch.globaz.ij.business.models.IJSimpleBaseIndemnisationSearchModel;
import ch.globaz.ij.business.services.IJBaseIndemnisationService;
import ch.globaz.ij.business.services.IJServiceLocator;
import globaz.framework.bean.JadeAbstractAjaxCrudDetailViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class IJBaseIndemnisationAjaxViewBean extends
        JadeAbstractAjaxCrudDetailViewBean<IJSimpleBaseIndemnisation, IJSimpleBaseIndemnisationSearchModel> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private IJSimpleBaseIndemnisation baseIndemnisation;
    private String calculerBaseIndemnisation;

    public IJBaseIndemnisationAjaxViewBean() {
        super();
        baseIndemnisation = new IJSimpleBaseIndemnisation();
    }

    public IJSimpleBaseIndemnisation getBaseIndemnisation() {
        return baseIndemnisation;
    }

    /**
     * Renseigne si la base d'indemnisation doit être calculée
     * 
     * @return si la base d'indemnisation doit être calculée
     */
    public final String getCalculerBaseIndemnisation() {
        return calculerBaseIndemnisation;
    }

    /**
     * @return
     * @see IJSimpleBaseIndemnisation#getCantonImposition()
     */
    public String getCsCantonImposition() {
        return baseIndemnisation.getCsCantonImposition();
    }

    @Override
    public IJSimpleBaseIndemnisation getCurrentEntity() {
        return baseIndemnisation;
    }

    public String getDateDeDebut() {
        return baseIndemnisation.getDateDeDebut();
    }

    public String getDateDeFin() {
        return baseIndemnisation.getDateDeFin();
    }

    public String getIdBaseIndemnisation() {
        return baseIndemnisation.getIdBaseIndemnisation();
    }

    public String getIdParent() {
        return baseIndemnisation.getIdParent();
    }

    public final String getIdPrononce() {
        return baseIndemnisation.getIdPrononce();
    }

    public String getJoursExternes() {
        return baseIndemnisation.getJoursExternes();
    }

    public String getJoursInternes() {
        return baseIndemnisation.getJoursInternes();
    }

    public String getJoursInterruption() {
        return baseIndemnisation.getJoursInterruption();
    }

    public String getMotifInterruption() {
        return baseIndemnisation.getMotifInterruption();
    }

    public String getJoursNonCouvert() {
        return baseIndemnisation.getJoursNonCouverts();
    }

    /**
     * @return
     * @see IJSimpleBaseIndemnisation#getRemarque()
     */
    public String getRemarque() {
        return baseIndemnisation.getRemarque();
    }

    @Override
    public JadeCrudService<IJSimpleBaseIndemnisation, IJSimpleBaseIndemnisationSearchModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return IJServiceLocator.getBaseIndemnisationService();
    }

    /**
     * @return
     * @see IJSimpleBaseIndemnisation#getTauxImposition()
     */
    public String getTauxImposition() {
        return baseIndemnisation.getTauxImposition();
    }

    /**
     * Comportement particulier : -> Si l'id de la base d'indemnisation est null ou 0 on va renvoyer une nouvelle entité
     * (non crée en db) mais pré-initialiser avec certaine valeur récupérés depuis le prononcé. L'id du prononcé doit
     * être connu dans le context de ce viewBean. Si l'id de la base d'indemnisation n'est pas null ou 0, c'est une
     * recherche standard qui est effectuée.
     */
    @Override
    public void retrieve() throws Exception {
        if (getCurrentEntity() != null) {
            String idBaseIndemnisation = getCurrentEntity().getId();
            // Traitement particulier, on retourne une nouvelle entité pré-initialisé
            if (JadeStringUtil.isBlankOrZero(idBaseIndemnisation)) {
                String idPrononce = getBaseIndemnisation().getIdPrononce();
                setCurrentEntity(((IJBaseIndemnisationService) getService()).getNewEntity(idPrononce));
            }
            // Traitement normal
            else {
                setCurrentEntity(getService().read(idBaseIndemnisation));
            }
        }
    }

    public void setBaseIndemnisation(IJSimpleBaseIndemnisation baseIndemnisation) {
        this.baseIndemnisation = baseIndemnisation;
    }

    public final void setCalculerBaseIndemnisation(String calculerBaseIndemnisation) {
        this.calculerBaseIndemnisation = calculerBaseIndemnisation;
    }

    /**
     * @param cantonImposition
     * @see IJSimpleBaseIndemnisation#setCantonImposition(String)
     */
    public void setCantonImposition(String cantonImposition) {
        baseIndemnisation.setCsCantonImposition(cantonImposition);
    }

    @Override
    public void setCurrentEntity(IJSimpleBaseIndemnisation entite) {
        baseIndemnisation = entite;
    }

    public void setDateDeDebut(String dateDeDebut) {
        baseIndemnisation.setDateDeDebut(dateDeDebut);
    }

    public void setDateDeFin(String dateDeFin) {
        baseIndemnisation.setDateDeFin(dateDeFin);
    }

    public void setIdBaseIndemnisation(String idBaseIndemnisation) {
        baseIndemnisation.setIdBaseIndemnisation(idBaseIndemnisation);
    }

    public void setIdParent(String idParent) {
        baseIndemnisation.setIdParent(idParent);
    }

    public final void setIdPrononce(String idPrononce) {
        baseIndemnisation.setIdPrononce(idPrononce);
    }

    public void setJoursExternes(String joursExternes) {
        baseIndemnisation.setJoursExternes(joursExternes);
    }

    public void setJoursInternes(String joursInternes) {
        baseIndemnisation.setJoursInternes(joursInternes);
    }

    public void setJoursInterruption(String joursInterruption) {
        baseIndemnisation.setJoursInterruption(joursInterruption);
    }

    public void setJoursNonCouverts(String joursNonCouverts) {
        baseIndemnisation.setJoursNonCouverts(joursNonCouverts);
    }

    public void setMotifInterruption(String motifInterruption) {
        baseIndemnisation.setMotifInterruption(motifInterruption);
    }

    /**
     * @param remarque
     * @see IJSimpleBaseIndemnisation#setRemarque(String)
     */
    public void setRemarque(String remarque) {
        baseIndemnisation.setRemarque(remarque);
    }

    /**
     * @param tauxImposition
     * @see IJSimpleBaseIndemnisation#setTauxImposition(String)
     */
    public void setTauxImposition(String tauxImposition) {
        baseIndemnisation.setTauxImposition(tauxImposition);
    }

}
