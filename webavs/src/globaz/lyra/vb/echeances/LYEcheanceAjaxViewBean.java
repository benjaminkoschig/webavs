package globaz.lyra.vb.echeances;

import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.JadeAbstractAjaxListFindViewBean;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.lyra.api.ILYEcheances;
import globaz.prestation.utils.ged.PRGedUtils;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.lyra.business.exceptions.LYTechnicalException;
import ch.globaz.lyra.business.models.echeance.LYSimpleEcheance;
import ch.globaz.lyra.business.models.echeance.LYSimpleEcheanceSearchModel;
import ch.globaz.lyra.business.services.LYEcheanceService;
import ch.globaz.lyra.business.services.LYServiceLocator;

public class LYEcheanceAjaxViewBean extends JadeAbstractAjaxListFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient LYEcheanceLineViewBean detailViewBean;
    private boolean isSendToGedAjournement;
    private boolean isSendToGedEnfant18ans;
    private boolean isSendToGedEnfant25ans;
    private boolean isSendToGedEtude;
    private boolean isSendToGedFemmeVieillesse;
    private boolean isSendToGedHommeVieillesse;
    private boolean isSendToGedRenteVeuf;
    private transient LYSimpleEcheanceSearchModel searchModel;
    private boolean isValidationDecisionAutorise;

    public LYEcheanceAjaxViewBean() {
        super();
        initList();
        detailViewBean = new LYEcheanceLineViewBean(new LYSimpleEcheance());

        isSendToGedAjournement = false;
        isSendToGedEnfant18ans = false;
        isSendToGedEnfant25ans = false;
        isSendToGedEtude = false;
        isSendToGedFemmeVieillesse = false;
        isSendToGedHommeVieillesse = false;
        isSendToGedRenteVeuf = false;
        isValidationDecisionAutorise = true;
    }

    private void checkGED() throws Exception {

        if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.ECHEANCE_AJOURNEMENT,
                BSessionUtil.getSessionFromThreadContext())) {
            setSendToGedAjournement(true);
        }
        if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.ECHEANCE_ENFANT_18_ANS,
                BSessionUtil.getSessionFromThreadContext())) {
            setSendToGedEnfant18ans(true);
        }
        if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.ECHEANCE_ENFANT_25_ANS,
                BSessionUtil.getSessionFromThreadContext())) {
            setSendToGedEnfant25ans(true);
        }
        if (PRGedUtils
                .isDocumentInGed(IRENoDocumentInfoRom.ECHEANCE_ETUDES, BSessionUtil.getSessionFromThreadContext())) {
            setSendToGedEtude(true);
        }
        if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.ECHEANCE_FEMME_VIEILLESSE,
                BSessionUtil.getSessionFromThreadContext())) {
            setSendToGedFemmeVieillesse(true);
        }
        if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.ECHEANCE_HOMME_VIEILLESSE,
                BSessionUtil.getSessionFromThreadContext())) {
            setSendToGedHommeVieillesse(true);
        }
        if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.ECHEANCE_RENTE_DE_VEUF,
                BSessionUtil.getSessionFromThreadContext())) {
            setSendToGedRenteVeuf(true);
        }
    }

    @Override
    public void find() throws Exception {
        searchModel = LYServiceLocator.getEcheancesService().search(searchModel);
    }

    public String getCsDomaineApplicatif() {
        return detailViewBean.getCsDomaineApplicatif();
    }

    @Override
    public LYSimpleEcheance getCurrentEntity() {
        return detailViewBean.getEcheance();
    }

    public String getDescriptionEcheance() {
        return detailViewBean.getDescriptionEcheance();
    }

    public String getIdEcheance() {
        return detailViewBean.getIdEcheance();
    }

    public String getJspProcessEcheance() {
        return detailViewBean.getJspProcessEcheance();
    }

    public String getLibelleDomaineApplicatif() {
        return detailViewBean.getLibelleDomaineApplicatif();
    }

    public List<LYEcheanceLineViewBean> getLineViewBeans() throws JadeApplicationServiceNotAvailableException {
        List<LYEcheanceLineViewBean> list = new ArrayList<LYEcheanceLineViewBean>();

        if (searchModel.getSearchResults() != null) {
            for (JadeAbstractModel abstractModel : searchModel.getSearchResults()) {
                LYSimpleEcheance echeance = (LYSimpleEcheance) abstractModel;

                LYEcheanceLineViewBean line = new LYEcheanceLineViewBean(echeance);

                list.add(line);
            }
        }

        return list;
    }

    public String getMoisCourant() {
        if (ILYEcheances.CS_DOMAINE_APPLICATIF_RENTES.equals(getCsDomaineApplicatif())) {
            return detailViewBean.getMoisCourantRente();
        } else if (ILYEcheances.CS_DOMAINE_APPLICATIF_PCF.equals(getCsDomaineApplicatif())) {
            return detailViewBean.getMoisCourantPCF();
        }
        return detailViewBean.getMoisCourant();
    }

    public String getNumeroOrdre() {
        return detailViewBean.getNumeroOrdre();
    }

    public String getProcessEcheance() {
        return detailViewBean.getProcessEcheance();
    }

    @Override
    public LYSimpleEcheanceSearchModel getSearchModel() {
        return searchModel;
    }

    @Override
    public void initList() {
        searchModel = new LYSimpleEcheanceSearchModel();
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
    }

    public boolean isSendToGedAjournement() {
        return isSendToGedAjournement;
    }

    public boolean isSendToGedEnfant18ans() {
        return isSendToGedEnfant18ans;
    }

    public boolean isSendToGedEnfant25ans() {
        return isSendToGedEnfant25ans;
    }

    public boolean isSendToGedEtude() {
        return isSendToGedEtude;
    }

    public boolean isSendToGedFemmeVieillesse() {
        return isSendToGedFemmeVieillesse;
    }

    public boolean isSendToGedHommeVieillesse() {
        return isSendToGedHommeVieillesse;
    }

    public boolean isSendToGedRenteVeuf() {
        return isSendToGedRenteVeuf;
    }

    public boolean isValidationDecisionAutorise() {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        isValidationDecisionAutorise = REPmtMensuel.isValidationDecisionAuthorise(session);
        return isValidationDecisionAutorise;
    }

    @Override
    public void retrieve() throws Exception {
        if (JadeStringUtil.isBlank(getId())) {
            throw new LYTechnicalException("Identifiant nécessaire pour rechercher une échéance");
        }
        searchModel.setForIdEcheance(getId());

        LYEcheanceService echeanceService = LYServiceLocator.getEcheancesService();
        searchModel = echeanceService.search(searchModel);

        if (searchModel.getSearchResults().length != 1) {
            throw new LYTechnicalException("L'échéance ayant l'ID " + getId()
                    + " n'a pas été trouvée en base de données");
        }

        detailViewBean = new LYEcheanceLineViewBean((LYSimpleEcheance) searchModel.getSearchResults()[0]);

        checkGED();
    }

    public void setSendToGedAjournement(boolean isSendToGedAjournement) {
        this.isSendToGedAjournement = isSendToGedAjournement;
    }

    public void setSendToGedEnfant18ans(boolean isSendToGedEnfant18ans) {
        this.isSendToGedEnfant18ans = isSendToGedEnfant18ans;
    }

    public void setSendToGedEnfant25ans(boolean isSendToGedEnfant25ans) {
        this.isSendToGedEnfant25ans = isSendToGedEnfant25ans;
    }

    public void setSendToGedEtude(boolean isSendToGedEtude) {
        this.isSendToGedEtude = isSendToGedEtude;
    }

    public void setSendToGedFemmeVieillesse(boolean isSendToGedFemmeVieillesse) {
        this.isSendToGedFemmeVieillesse = isSendToGedFemmeVieillesse;
    }

    public void setSendToGedHommeVieillesse(boolean isSendToGedHommeVieillesse) {
        this.isSendToGedHommeVieillesse = isSendToGedHommeVieillesse;
    }

    public void setSendToGedRenteVeuf(boolean isSendToGedRenteVeuf) {
        this.isSendToGedRenteVeuf = isSendToGedRenteVeuf;
    }

    public void setValidationDecisionAuthorise(boolean isValidDecAut) {
        isValidationDecisionAutorise = isValidDecAut;
    }
}
