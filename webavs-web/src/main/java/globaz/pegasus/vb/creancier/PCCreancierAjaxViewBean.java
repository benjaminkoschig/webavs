package globaz.pegasus.vb.creancier;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCCreancierHandler;
import java.util.Iterator;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.creancier.Creancier;
import ch.globaz.pegasus.business.models.creancier.CreancierSearch;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class PCCreancierAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface,
        FWAJAXFindInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresse;
    private Creancier creancier;

    protected boolean getListe = false;
    private transient PCCreancierAjaxListViewBean list = null;

    public PCCreancierAjaxViewBean() {
        super();
        setList(new PCCreancierAjaxListViewBean());
        creancier = new Creancier();
    }

    /**
     * @param creancier
     */
    public PCCreancierAjaxViewBean(Creancier creancier) {
        super();
        this.creancier = creancier;
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void add() throws Exception {
        creancier.getSimpleCreancier().setCsEtat(IPCDroits.CS_ETAT_CREANCE_A_PAYE);
        creancier = PegasusServiceLocator.getCreancierService().create(creancier);
        list.find();
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void delete() throws Exception {
        creancier = PegasusServiceLocator.getCreancierService().delete(creancier);
        list.find();
    }

    public String displayCreancierTier() {
        return PCCreancierHandler.displayCreancierTiers(creancier.getSimpleTiers());
    }

    @Override
    public void find() throws Exception {
        list.find();
    }

    public String getAdresse() {
        return adresse;
    }

    public String getAdressePaiement() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail detailTiers = null;
        if (!JadeStringUtil.isEmpty(creancier.getSimpleCreancier().getIdTiers())) {
            detailTiers = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                    creancier.getSimpleCreancier().getIdTiers(), Boolean.TRUE,
                    creancier.getSimpleCreancier().getIdDomaineApplicatif(), JACalendar.todayJJsMMsAAAA(),
                    creancier.getSimpleCreancier().getIdAffilieAdressePaiment());
        }

        return detailTiers != null ? detailTiers.getAdresseFormate() : "";
    }

    /**
     * @return creancier
     */
    public Creancier getCreancier() {
        return creancier;
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return creancier;
    }

    @Override
    public String getId() {
        return creancier.getId();
    }

    public String getInfoTiers() {
        String infos = "";
        TiersSimpleModel simpleTiersSimpleModel = creancier.getSimpleTiers();
        if ((simpleTiersSimpleModel != null) && !JadeStringUtil.isEmpty(simpleTiersSimpleModel.getIdTiers())) {
            infos = simpleTiersSimpleModel.getDesignation1() + " " + simpleTiersSimpleModel.getDesignation2();
        }
        return infos;
    }

    public PCCreancierAjaxListViewBean getList() {
        return list;
    }

    public JadeAbstractModel[] getListAssure() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        DroitMembreFamilleEtenduSearch membreSearch = new DroitMembreFamilleEtenduSearch();

        membreSearch.setForIdDemande(creancier.getSimpleCreancier().getIdDemande());

        membreSearch = PegasusServiceLocator.getDroitService().searchDroitMemebreFamilleEtendu(membreSearch);

        return membreSearch.getSearchResults();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return list;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return list.getCreancierSearch();
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (creancier != null) && !creancier.isNew() ? new BSpy(creancier.getSpy()) : new BSpy(getSession());
    }

    @Override
    public boolean hasList() {
        return true;
    }

    @Override
    public void initList() {
        setList(new PCCreancierAjaxListViewBean());
    }

    @Override
    public Iterator iterator() {
        return list.iterator();
    }

    /**
     * @throws Exception
     */
    @Override
    public void retrieve() throws Exception {
        creancier = PegasusServiceLocator.getCreancierService().read(creancier.getId());

        if (JadeStringUtil.isBlankOrZero(creancier.getSimpleCreancier().getIdDomaineApplicatif())) {
            creancier.getSimpleCreancier().setIdDomaineApplicatif(
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
            JadeLogger.info(this, "IdDomaineApplicatif setted with default value: ["
                    + IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE + "]");
        }

        JadeLogger.info(this, "retrieving adresseTiersDetail: idTiersAdrressePaiement["
                + creancier.getSimpleCreancier().getIdTiersAdressePaiement() + "], idDomaineApplicatif");

        AdresseTiersDetail adresse = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                creancier.getSimpleCreancier().getIdTiersAdressePaiement(), true,
                creancier.getSimpleCreancier().getIdDomaineApplicatif(), JACalendar.todayJJsMMsAAAA(), null);
        this.adresse = adresse.getAdresseFormate();
        if (this.adresse != null) {
            this.adresse = adresse.getAdresseFormate().replaceAll("[\r\n]", "<br />");
        }
    }

    /**
     * @param creancier
     *            the creancier to set
     */
    public void setCreancier(Creancier creancier) {
        this.creancier = creancier;

    }

    @Override
    public void setDefinedSearchSize(int definedSearchSize) {
        list.getCreancierSearch().setDefinedSearchSize(definedSearchSize);
    }

    @Override
    public void setGetListe(boolean getListe) {
        this.getListe = getListe;
    }

    /**
     * @param idCreancier
     */
    @Override
    public void setId(String id) {
        creancier.setId(id);
    }

    public void setList(PCCreancierAjaxListViewBean list) {
        this.list = list;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        list = (PCCreancierAjaxListViewBean) fwViewBeanInterface;
    }

    @Override
    public void setOffset(int offset) {
        list.getCreancierSearch().setOffset(offset);
    }

    public void setSearchModel(JadeAbstractSearchModel search) {
        list.setCreancierSearch((CreancierSearch) search);
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void update() throws Exception {
        creancier = PegasusServiceLocator.getCreancierService().update(creancier);
        list.find();
    }
}
