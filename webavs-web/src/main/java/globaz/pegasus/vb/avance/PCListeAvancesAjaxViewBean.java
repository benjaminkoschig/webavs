package globaz.pegasus.vb.avance;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.models.avance.AvanceVo;
import ch.globaz.pegasus.business.models.avance.AvanceVoSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCListeAvancesAjaxViewBean extends JadeAbstractAjaxCrudFindViewBean<AvanceVo, AvanceVoSearch> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AvanceVo currentEntity = null;
    private String idDemande = null;
    private String idTiers = null;
    private AvanceVoSearch search = null;

    public PCListeAvancesAjaxViewBean() {
        currentEntity = new AvanceVo();
        search = new AvanceVoSearch();
    }

    @Override
    public AvanceVo getCurrentEntity() {
        return currentEntity;
    }

    public String getDetailAssure(AvanceVo avance) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        return avance.getNom() + " " + avance.getPrenom() + " / " + avance.getNss();
    }

    public String getDomaineAvanceAsLibelle(String csDomaine) {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(csDomaine);
    }

    public String getEtatAcompteAsLibelle(String csEtat) {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(csEtat);
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getJsonInfoDecompt() {
        return "jsonInfo";
    }

    public String getPeriode(AvanceVo avance) {
        if (!JadeStringUtil.isBlank(avance.getDateDebutAcompte())) {
            return avance.getDateDebutAcompte() + " - " + avance.getDateFinAcompte();
        } else {
            return avance.getDateDebutPmt1erAcompte() + " - " + avance.getDateFinAcompte();
        }

    }

    @Override
    public AvanceVoSearch getSearchModel() {

        return search;
    }

    @Override
    public JadeCrudService getService() throws JadeApplicationServiceNotAvailableException {
        return PegasusServiceLocator.getAvanceService();
    }

    @Override
    public void initList() {

    }

    @Override
    public void retrieve() throws Exception {
        System.out.println();
    }

    @Override
    public void setCurrentEntity(AvanceVo entite) {
        currentEntity = entite;
    }

    public void setIdDemande(String idDemande) {
        search.setIdDemande(this.idDemande);
        this.idDemande = idDemande;
    }

    public void setIdTiers(String idTiers) {
        search.setIdTiers(this.idTiers);
        this.idTiers = idTiers;
    }

    @Override
    public void setSearchModel(AvanceVoSearch search) {
        this.search = search;
    }
}
