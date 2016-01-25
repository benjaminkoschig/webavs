/**
 * 
 */
package globaz.pegasus.vb.retenues;

import globaz.corvus.api.retenues.IRERetenues;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenue;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author LFO
 * 
 */
public class PCRetenuesAjaxViewBean extends JadeAbstractAjaxFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adressePaiement;
    private String csRoleMembreFamille = null;
    private String idPca;
    private String labelRole;
    private String labelTypeSection;
    private transient PCRetenuesAjaxListViewBean listeRetenuePayementAjaxListViewBean;
    private SimpleRetenuePayement simpleRetenuePayement = null;

    public PCRetenuesAjaxViewBean() {
        super();
        simpleRetenuePayement = new SimpleRetenuePayement();
        listeRetenuePayementAjaxListViewBean = new PCRetenuesAjaxListViewBean();
    }

    public PCRetenuesAjaxViewBean(SimpleRetenuePayement simpleRetenuePayement) {
        this();
        this.simpleRetenuePayement = simpleRetenuePayement;
    }

    @Override
    public void add() throws Exception {
        PegasusServiceLocator.getRetenueService().create(generateRetenue());
    }

    @Override
    public void delete() throws Exception {
        PegasusServiceLocator.getRetenueService().delete(generateRetenue());
    }

    @Override
    public void find() throws Exception {
        getListeRetenuePayementAjaxListViewBean().find();
    }

    private String findAdressePaiement() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        String adresse = "";
        if (!JadeStringUtil.isBlankOrZero(simpleRetenuePayement.getIdTiersAdressePmt())) {
            AdresseTiersDetail adresseTiersDetail = TIBusinessServiceLocator.getAdresseService()
                    .getAdressePaiementTiers(simpleRetenuePayement.getIdTiersAdressePmt(), true,
                            simpleRetenuePayement.getIdDomaineApplicatif(), JACalendar.todayJJsMMsAAAA(), null);

            adresse = (adresseTiersDetail.getAdresseFormate() != null ? adresseTiersDetail.getAdresseFormate()
                    .replaceAll("\n", "<br />") : "");
        }

        return adresse;
    }

    private PcaRetenue generateRetenue() {
        PcaRetenue retenue = new PcaRetenue();
        retenue.setCsRoleFamillePC(csRoleMembreFamille);
        retenue.setSimpleRetenue(simpleRetenuePayement);
        retenue.setIdPCAccordee(idPca);
        return retenue;
    }

    public String getAdressePaiement() {
        return adressePaiement;
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return simpleRetenuePayement;
    }

    public String getDesignation(PcaRetenue retenue) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        if (retenue.getSimpleRetenue().getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_EXISTANTE)) {
            return retenue.getSimpleRetenue().getIdExterne() + " - " + retenue.getSimpleRetenue().getNoFacture();
        }
        if (retenue.getSimpleRetenue().getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_FUTURE)) {
            return retenue.getSimpleRetenue().getIdExterne();
        }
        if (retenue.getSimpleRetenue().getCsTypeRetenue().equals(IRERetenues.CS_TYPE_ADRESSE_PMT)) {
            AdresseTiersDetail adresseTiersDetail = TIBusinessServiceLocator.getAdresseService()
                    .getAdressePaiementTiers(retenue.getSimpleRetenue().getIdTiersAdressePmt(), true,
                            retenue.getSimpleRetenue().getIdDomaineApplicatif(), JACalendar.todayJJsMMsAAAA(), null);
            if ((adresseTiersDetail != null) && (adresseTiersDetail.getFields() != null)) {
                return adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE);
            } else {
                return null;
            }
        }
        return null;
    }

    public String getIdPca() {
        return idPca;
    }

    public String getLabelRole() {
        return labelRole;
    }

    public String getLabelTypeSection() {
        return labelTypeSection;
    }

    public PCRetenuesAjaxListViewBean getListeRetenuePayementAjaxListViewBean() {
        return listeRetenuePayementAjaxListViewBean;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeRetenuePayementAjaxListViewBean;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return listeRetenuePayementAjaxListViewBean.getPcaRetenueSearch();
    }

    public SimpleRetenuePayement getSimpleRetenuePayement() {
        return simpleRetenuePayement;
    }

    @Override
    public void initList() {
        listeRetenuePayementAjaxListViewBean = new PCRetenuesAjaxListViewBean();
    }

    @Override
    public void retrieve() throws Exception {
        PcaRetenue retenu = PegasusServiceLocator.getRetenueService().read(simpleRetenuePayement.getId());
        if (!JadeStringUtil.isBlankOrZero(retenu.getSimpleRetenue().getDateDebutRetenue())) {
            simpleRetenuePayement = retenu.getSimpleRetenue();
            csRoleMembreFamille = retenu.getCsRoleFamillePC();
            adressePaiement = findAdressePaiement();
            if (simpleRetenuePayement.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_FUTURE)
                    || simpleRetenuePayement.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_EXISTANTE)) {
                labelRole = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(simpleRetenuePayement.getRole());
            }
            if (simpleRetenuePayement.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_EXISTANTE)) {
                labelTypeSection = simpleRetenuePayement.getIdTypeSection();// BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                // this.simpleRetenuePayement.getIdTypeSection());
                // CABusinessServiceLocator.getSectionService().getSectionByIdExterne(idCompteAnnexe, idTypeSection,
                // idExterne, journal)
            }
        }
    }

    public void setCsRoleMembreFamille(String csRoleMembreFamille) {
        this.csRoleMembreFamille = csRoleMembreFamille;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public void setListeSubsideanneeAjaxListViewBean(PCRetenuesAjaxListViewBean listeRetenuePayementAjaxListViewBean) {
        this.listeRetenuePayementAjaxListViewBean = listeRetenuePayementAjaxListViewBean;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeRetenuePayementAjaxListViewBean = (PCRetenuesAjaxListViewBean) fwViewBeanInterface;
    }

    public void setSimpleRetenuePayement(SimpleRetenuePayement simpleRetenuePayement) {
        this.simpleRetenuePayement = simpleRetenuePayement;
    }

    @Override
    public void update() throws Exception {
        PegasusServiceLocator.getRetenueService().update(generateRetenue());
    }

}
