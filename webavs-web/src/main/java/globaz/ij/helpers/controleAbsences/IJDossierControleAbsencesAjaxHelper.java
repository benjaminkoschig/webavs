package globaz.ij.helpers.controleAbsences;

import globaz.corvus.utils.RETiersForJspUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.vb.controleAbsences.IJDossierControleAbsencesAjaxViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import ch.globaz.ij.business.models.IJSimpleDossierControleAbsences;
import ch.globaz.ij.business.models.IJSimpleDossierControleAbsencesSearchModel;
import ch.globaz.ij.business.services.IJServiceLocator;

public class IJDossierControleAbsencesAjaxHelper extends FWHelper {

    public IJDossierControleAbsencesAjaxHelper() {
        super();
    }

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        System.out.println("TODO");
        super._add(viewBean, action, session);
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJDossierControleAbsencesAjaxViewBean vb = (IJDossierControleAbsencesAjaxViewBean) viewBean;
        if (action.getActionPart().endsWith("afficher")) {
            rechercherDossierControleAbsencesPourAssure(vb);
            chargerDetailAssure(vb);
            chargerPrononce(vb, session);
        }
    }

    private void chargerDetailAssure(IJDossierControleAbsencesAjaxViewBean vb) throws Exception {
        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(BSessionUtil.getSessionFromThreadContext(), vb.getIdTiers());
        vb.setNoNSS(tiers.getNSS());
        vb.setDetailAssure(RETiersForJspUtils.getInstance(BSessionUtil.getSessionFromThreadContext()).getDetailsTiers(
                tiers, false));
    }

    private void chargerPrononce(IJDossierControleAbsencesAjaxViewBean vb, BISession session) throws Exception {
        if (JadeStringUtil.isBlankOrZero(vb.getIdPrononce())) {
            throw new Exception("Unable to retreive the prononce because id is empty in viewBean : "
                    + vb.getIdPrononce());
        }
        IJPrononce prononce = new IJPrononce();
        prononce.setSession((BSession) session);
        prononce.setIdPrononce(vb.getIdPrononce());
        prononce.retrieve();

        if (prononce.isNew()) {
            throw new Exception("Unable to retreive the prononce with id : " + vb.getIdPrononce());
        }
        if (Boolean.valueOf(prononce.getSoumisImpotSource())) {
            vb.setIsImposeALaSource(true);
        } else {
            vb.setIsImposeALaSource(false);
        }
    }

    /**
     * <p>
     * Recherche du dossier de l'assuré.
     * </p>
     * <p>
     * Si l'assuré n'a pas de dossier (ou que tous ses dossiers sont historisés), on en créer un nouveau pour lui. (il
     * ne peut y avoir qu'un dossier non historisé par personne en même temps)
     * </p>
     * 
     * @param vb
     * @throws Exception
     */
    private void rechercherDossierControleAbsencesPourAssure(IJDossierControleAbsencesAjaxViewBean vb) throws Exception {
        if (JadeStringUtil.isBlankOrZero(vb.getIdTiers())) {
            throw new Exception(BSessionUtil.getSessionFromThreadContext().getLabel("ERREUR_ID_TIERS_NON_DEFINI"));
        }
        IJSimpleDossierControleAbsencesSearchModel searchModel = new IJSimpleDossierControleAbsencesSearchModel();
        searchModel.setForIdTiers(vb.getIdTiers());
        searchModel.setForIsHistorise(false);
        searchModel = (IJSimpleDossierControleAbsencesSearchModel) JadePersistenceManager.search(searchModel);

        if (searchModel.getSize() > 1) {
            throw new Exception(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "ERREUR_PLUSIEURS_DOSSIERS_OUVERT_POUR_UN_ASSURE"));
        }

        IJSimpleDossierControleAbsences dossier = null;
        if (searchModel.getSize() == 1) {
            dossier = (IJSimpleDossierControleAbsences) searchModel.getSearchResults()[0];
        } else {
            dossier = new IJSimpleDossierControleAbsences();
            dossier.setIdTiers(vb.getIdTiers());
            dossier.setIsHistorise(false);
            dossier = IJServiceLocator.getDossierControleAbsenceService().create(dossier);
        }
        vb.setCurrentEntity(dossier);
    }
}
