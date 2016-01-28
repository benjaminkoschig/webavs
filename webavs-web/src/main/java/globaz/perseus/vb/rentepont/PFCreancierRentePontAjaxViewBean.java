package globaz.perseus.vb.rentepont;

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
import ch.globaz.perseus.business.models.rentepont.CreancierRentePont;
import ch.globaz.perseus.business.models.rentepont.CreancierRentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class PFCreancierRentePontAjaxViewBean extends JadeAbstractAjaxFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adressePaiement;
    private CreancierRentePont creancierRentePont;
    private transient PFCreancierRentePontAjaxListViewBean list = null;

    public PFCreancierRentePontAjaxViewBean() {
        super();
        setList(new PFCreancierRentePontAjaxListViewBean());
        creancierRentePont = new CreancierRentePont();
    }

    /**
     * @param creancierRentePont
     */
    public PFCreancierRentePontAjaxViewBean(CreancierRentePont creancierRentePont) {
        super();
        this.creancierRentePont = creancierRentePont;
    }

    /**
     * @throws CreancierRentePontException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void add() throws Exception {
        // this.creancierRentePont.getSimpleCreancierRentePont().setCsEtat();
        creancierRentePont = PerseusServiceLocator.getCreancierRentePontService().create(creancierRentePont);

        list.find();
    }

    /**
     * @throws CreancierRentePontException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void delete() throws Exception {
        creancierRentePont = PerseusServiceLocator.getCreancierRentePontService().delete(creancierRentePont);

        list.find();
    }

    public String displayCreancierRentePontTier() {
        return creancierRentePont.getSimpleTiers().getDesignation1() + " "
                + creancierRentePont.getSimpleTiers().getDesignation2();
    }

    @Override
    public void find() throws Exception {
        list.find();
    }

    public String getAdressePaiement() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail detailTiers = null;
        if (!JadeStringUtil.isEmpty(creancierRentePont.getSimpleCreancierRentePont().getIdTiers())) {
            detailTiers = PFUserHelper.getAdressePaiementAssure(creancierRentePont.getSimpleCreancierRentePont()
                    .getIdTiers(), creancierRentePont.getSimpleCreancierRentePont().getIdDomaineApplicatif(),
                    JACalendar.todayJJsMMsAAAA());
        }

        return detailTiers != null ? detailTiers.getAdresseFormate() : "";
    }

    /**
     * @return creancierRentePont
     */
    public CreancierRentePont getCreancierRentePont() {
        return creancierRentePont;
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return creancierRentePont;
    }

    public String getInfoTiers() {
        String infos = "";
        TiersSimpleModel simpleTiersSimpleModel = creancierRentePont.getSimpleTiers();
        if ((simpleTiersSimpleModel != null) && !JadeStringUtil.isEmpty(simpleTiersSimpleModel.getIdTiers())) {
            infos = simpleTiersSimpleModel.getDesignation1() + " " + simpleTiersSimpleModel.getDesignation2();
        }
        return infos;
    }

    public PFCreancierRentePontAjaxListViewBean getList() {
        return list;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return list;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return list.getCreancierRentePontSearchModel();
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
        setList(new PFCreancierRentePontAjaxListViewBean());
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
        creancierRentePont = PerseusServiceLocator.getCreancierRentePontService().read(creancierRentePont.getId());

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
     * @param creancierRentePont
     *            the creancierRentePont to set
     */
    public void setCreancierRentePont(CreancierRentePont creancierRentePont) {
        this.creancierRentePont = creancierRentePont;

    }

    public void setList(PFCreancierRentePontAjaxListViewBean list) {
        this.list = list;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        list = (PFCreancierRentePontAjaxListViewBean) fwViewBeanInterface;
    }

    public void setSearchModel(JadeAbstractSearchModel search) {
        list.setCreancierRentePontSearchModel((CreancierRentePontSearchModel) search);
    }

    /**
     * @throws CreancierRentePontException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void update() throws Exception {
        creancierRentePont = PerseusServiceLocator.getCreancierRentePontService().update(creancierRentePont);
        list.find();
    }
}
