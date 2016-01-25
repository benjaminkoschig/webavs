package globaz.perseus.vb.creancier;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import java.util.Iterator;
import ch.globaz.perseus.business.models.creancier.Creancier;
import ch.globaz.perseus.business.models.creancier.CreancierSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class PFCreancierAjaxViewBean extends JadeAbstractAjaxFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adressePaiement;
    private Creancier creancier;
    private transient PFCreancierAjaxListViewBean list = null;

    public PFCreancierAjaxViewBean() {
        super();
        setList(new PFCreancierAjaxListViewBean());
        creancier = new Creancier();
    }

    /**
     * @param creancier
     */
    public PFCreancierAjaxViewBean(Creancier creancier) {
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
        // this.creancier.getSimpleCreancier().setCsEtat();
        creancier = PerseusServiceLocator.getCreancierService().create(creancier);

        list.find();
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void delete() throws Exception {
        creancier = PerseusServiceLocator.getCreancierService().delete(creancier);

        list.find();
    }

    public String displayCreancierTier() {
        return creancier.getSimpleTiers().getDesignation1() + " " + creancier.getSimpleTiers().getDesignation2();
    }

    @Override
    public void find() throws Exception {
        list.find();
    }

    public String getAdressePaiement() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail detailTiers = null;
        if (!JadeStringUtil.isEmpty(creancier.getSimpleCreancier().getIdTiers())) {
            detailTiers = PFUserHelper.getAdressePaiementAssure(creancier.getSimpleCreancier().getIdTiers(), creancier
                    .getSimpleCreancier().getIdDomaineApplicatif(), JACalendar.todayJJsMMsAAAA());
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

    public String getInfoTiers() {
        String infos = "";
        TiersSimpleModel simpleTiersSimpleModel = creancier.getSimpleTiers();
        if ((simpleTiersSimpleModel != null) && !JadeStringUtil.isEmpty(simpleTiersSimpleModel.getIdTiers())) {
            infos = simpleTiersSimpleModel.getDesignation1() + " " + simpleTiersSimpleModel.getDesignation2();
        }
        return infos;
    }

    public PFCreancierAjaxListViewBean getList() {
        return list;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return list;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return list.getCreancierSearchModel();
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
    public void initList() {
        setList(new PFCreancierAjaxListViewBean());
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
        creancier = PerseusServiceLocator.getCreancierService().read(creancier.getId());

        adressePaiement = getAdressePaiement();
    }

    /**
     * @param adressePaiement
     *            the adressePaiement to set
     */
    public void setAdressePaiement(String adressePaiement) {
        this.adressePaiement = adressePaiement;
    }

    /**
     * @param creancier
     *            the creancier to set
     */
    public void setCreancier(Creancier creancier) {
        this.creancier = creancier;

    }

    public void setList(PFCreancierAjaxListViewBean list) {
        this.list = list;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        list = (PFCreancierAjaxListViewBean) fwViewBeanInterface;
    }

    public void setSearchModel(JadeAbstractSearchModel search) {
        list.setCreancierSearchModel((CreancierSearchModel) search);
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void update() throws Exception {
        creancier = PerseusServiceLocator.getCreancierService().update(creancier);
        list.find();
    }
}
