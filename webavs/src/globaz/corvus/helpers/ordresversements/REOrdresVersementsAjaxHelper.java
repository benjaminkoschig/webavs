package globaz.corvus.helpers.ordresversements;

import globaz.babel.utils.BabelContainer;
import globaz.babel.utils.CatalogueText;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.ordresversements.IRESoldePourRestitution;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.exceptions.REBusinessException;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.vb.ordresversements.IREOrdreVersementAjaxViewBean;
import globaz.corvus.vb.ordresversements.RECompensationInterDecisionAjaxViewBean;
import globaz.corvus.vb.ordresversements.REGestionRestitutionAjaxViewBean;
import globaz.corvus.vb.ordresversements.REOrdresVersementsAjaxViewBean;
import globaz.corvus.vb.ordresversements.REOrdresVersementsDetailAjaxViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.helpers.PRHybridHelper;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import ch.globaz.corvus.business.models.ordresversements.OrdreVersementComplexModel;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.business.services.models.decisions.DecisionService;
import ch.globaz.corvus.business.services.models.decisions.SoldePourRestitutionCrudService;
import ch.globaz.corvus.business.services.models.ordresversements.CompensationInterDecisionCrudService;
import ch.globaz.corvus.business.services.models.ordresversements.OrdresVersementCrudService;
import ch.globaz.corvus.domaine.CompensationInterDecision;
import ch.globaz.corvus.domaine.Decision;
import ch.globaz.corvus.domaine.OrdreVersement;
import ch.globaz.corvus.domaine.SoldePourRestitution;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.corvus.domaine.constantes.TypeSoldePourRestitution;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;

public class REOrdresVersementsAjaxHelper extends PRHybridHelper {

    @Override
    protected void _add(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {
        RECompensationInterDecisionAjaxViewBean ajaxViewBean = (RECompensationInterDecisionAjaxViewBean) viewBean;

        DecisionService decisionService = CorvusServiceLocator.getDecisionService();
        Decision decisionDeficitaire = decisionService.getDecision(ajaxViewBean.getIdDecisionDeficitaire());
        Decision decisionPonctionnee = decisionService.getDecision(ajaxViewBean.getIdDecisionPonctionne());

        Set<OrdreVersement> ovsDecisionDeficitaire = decisionDeficitaire
                .getOrdresVersementPourType(TypeOrdreVersement.DETTE);

        if (ovsDecisionDeficitaire.size() == 0) {
            throw new REBusinessException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "ERREUR_CID_IMPOSSIBLE_SI_UNIQUEMENT_DETTE_COMPTA"));
        }

        Iterator<OrdreVersement> iterator = ovsDecisionDeficitaire.iterator();

        OrdreVersement unOvDetteDecisionCompensee = null;

        while (iterator.hasNext() && (unOvDetteDecisionCompensee == null)) {
            OrdreVersement ordreVersement = iterator.next();
            if (ordreVersement.isCompense()) {
                unOvDetteDecisionCompensee = ordreVersement;
            }
        }

        // Là où on ponctionne, l'ancienne architecture (qui n'a pas été changé à ce niveau) demandait à ce qu'un OV
        // soit créé pour la décision ponctionnée
        OrdreVersement unOvDetteDecisionPonctionee = new OrdreVersement();
        unOvDetteDecisionPonctionee.setCompensationInterDecision(true);
        unOvDetteDecisionPonctionee.setCompense(true);
        unOvDetteDecisionPonctionee.setMontantCompense(ajaxViewBean.getMontantCompense());
        unOvDetteDecisionPonctionee.setPrestation(decisionPonctionnee.getPrestation());
        unOvDetteDecisionPonctionee.setTitulaire(decisionPonctionnee.getBeneficiairePrincipal());
        unOvDetteDecisionPonctionee.setType(TypeOrdreVersement.DETTE);

        unOvDetteDecisionPonctionee = CorvusCrudServiceLocator.getOrdresVersementCrudService().create(
                unOvDetteDecisionPonctionee);

        CompensationInterDecision nouvelleCID = new CompensationInterDecision();
        nouvelleCID.setMontantCompense(ajaxViewBean.getMontantCompense());
        nouvelleCID.setBeneficiaireCompensationInterDecision(decisionPonctionnee.getBeneficiairePrincipal());
        nouvelleCID.setOrdreVersementDecisionCompensee(unOvDetteDecisionCompensee);
        nouvelleCID.setOrdreVersementDecisionPonctionnee(unOvDetteDecisionPonctionee);

        nouvelleCID = CorvusCrudServiceLocator.getCompensationInterDecisionCrudService().create(nouvelleCID);

        mettreAJourSoldesPourRestitution(nouvelleCID.getOrdreVersementDecisionCompensee());
        mettreAJourSoldesPourRestitution(nouvelleCID.getOrdreVersementDecisionPonctionnee());
    }

    @Override
    protected void _delete(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {

        IREOrdreVersementAjaxViewBean ajaxViewBean = (IREOrdreVersementAjaxViewBean) viewBean;

        if ("CID".equals(ajaxViewBean.getProvenance())) {

            RECompensationInterDecisionAjaxViewBean cidAjaxViewBean = (RECompensationInterDecisionAjaxViewBean) ajaxViewBean;

            CompensationInterDecision cid = new CompensationInterDecision();
            cid.setId(cidAjaxViewBean.getIdCompensationInterDecision());
            cid = CorvusCrudServiceLocator.getCompensationInterDecisionCrudService().read(cid);

            cid.setMontantCompense(BigDecimal.ZERO);
            cid = CorvusCrudServiceLocator.getCompensationInterDecisionCrudService().update(cid);
            mettreAJourSoldesPourRestitution(cid.getOrdreVersementDecisionCompensee());
            mettreAJourSoldesPourRestitution(cid.getOrdreVersementDecisionPonctionnee());

            CorvusCrudServiceLocator.getCompensationInterDecisionCrudService().delete(cid);
        } else {
            throw new RETechnicalException("can't delete this type of entity");
        }
    }

    @Override
    protected void _findAjax(final Object viewBean, final FWAction action, final BISession session) throws Exception {

        REOrdresVersementsAjaxViewBean ajaxViewBean = (REOrdresVersementsAjaxViewBean) viewBean;

        JadeCodeSystemeService codeSystemeService = JadeBusinessServiceLocator.getCodeSystemeService();

        BabelContainer babelContainer = new BabelContainer();
        babelContainer.setSession((BSession) session);

        CatalogueText catalogeDeTexteDecision = new CatalogueText();
        catalogeDeTexteDecision = new CatalogueText();
        catalogeDeTexteDecision.setCodeIsoLangue(session.getIdLangueISO());
        catalogeDeTexteDecision.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        catalogeDeTexteDecision.setCsTypeDocument(IRECatalogueTexte.CS_DECISION);
        catalogeDeTexteDecision.setNomCatalogue("openOffice");

        babelContainer.addCatalogueText(catalogeDeTexteDecision);
        babelContainer.load();

        // récupération de ce qui sera affiché dans la décision imprimée pour les dettes en compta
        ajaxViewBean.setDesignationPourDettesEnCompta(babelContainer.getTexte(catalogeDeTexteDecision, 5, 9));

        ajaxViewBean.setCodesSystemeTypeOrdreVersement(codeSystemeService
                .getFamilleCodeSysteme(IREOrdresVersements.CS_GROUPE_TYPE_ORDRE_VERSEMENT));
        ajaxViewBean.setCodesSystemeTypeRenteVerseeATort(codeSystemeService
                .getFamilleCodeSysteme(RERenteVerseeATort.FAMILLE_CODE_SYSTEME_TYPE_RENTE_VERSEE_A_TROT));
        ajaxViewBean.find();

        if (ajaxViewBean.isOrdresVersementPresents()) {
            DecisionService decisionService = CorvusServiceLocator.getDecisionService();

            Decision decision = decisionService.getDecision(ajaxViewBean.getIdDecision());
            SoldePourRestitution soldePourRestitution = decisionService
                    .getSoldePourRestitutionDuBeneficiairePrincipal(decision);

            ajaxViewBean.setDecision(decision);
            if (soldePourRestitution != null) {
                ajaxViewBean.setSoldePourRestitutionBeneficiairePrincipal(soldePourRestitution);
            }

            ajaxViewBean.setCompensationInterDecisionPossible((decision.getSolde().compareTo(BigDecimal.ZERO) < 0)
                    && decisionService.isCompensationInterDecisionPossible(decision.getId()));
        }
    }

    @Override
    protected void _retrieve(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {

        IREOrdreVersementAjaxViewBean ajaxViewBean = (IREOrdreVersementAjaxViewBean) viewBean;

        if ("CID".equals(ajaxViewBean.getProvenance())) {

            // pour la modification d'une CID, il nous faut l'ID de l'ordre de versement sur lequel est crochée cette
            // CID (vu que la modification n'est possible quand là où la CID compense, cela veut dire que l'ordre de
            // versement sur lequel pointe la CID n'est pas le CID exclusif CID, il faut donc le laisser tel quel)
            // le montant du CID ainsi que le montant disponible sur la décision qui a été choisie à la création du CID.

            RECompensationInterDecisionAjaxViewBean cidAjaxViewBean = (RECompensationInterDecisionAjaxViewBean) ajaxViewBean;

            Decision decision = CorvusServiceLocator.getDecisionService().getDecisionPourIdOrdreVersement(
                    cidAjaxViewBean.getIdOrdreVersement());
            CompensationInterDecision cid = null;

            for (CompensationInterDecision uneCID : decision.getCompensationsInterDecision()) {
                if (uneCID.getOrdreVersementDecisionCompensee().getId().equals(cidAjaxViewBean.getIdOrdreVersement())) {
                    cid = uneCID;
                }
            }
            if (cid == null) {
                throw new RETechnicalException("Entity not found");
            }

            Decision decisionPonctionnee = CorvusServiceLocator.getDecisionService().getDecisionPourIdOrdreVersement(
                    cid.getOrdreVersementDecisionPonctionnee().getId());

            cidAjaxViewBean.setIdCompensationInterDecision(cid.getId().toString());
            cidAjaxViewBean.setDecisionPonctionnee(decisionPonctionnee);
            cidAjaxViewBean.setMontantCompense(cid.getMontantCompense().toString());
            cidAjaxViewBean.setSoldeDecisionDeficitaire(decision.getSolde());
        } else if ("OV".equals(ajaxViewBean.getProvenance())) {
            REOrdresVersementsDetailAjaxViewBean ovAjaxViewBean = (REOrdresVersementsDetailAjaxViewBean) ajaxViewBean;
            OrdreVersementComplexModel ordreVersement = new OrdreVersementComplexModel();
            ordreVersement.setIdOrdreVersement(ovAjaxViewBean.getIdOrdreVersement().toString());
            JadePersistenceManager.read(ordreVersement);

            ovAjaxViewBean.setCodesSystemeTypeOrdreVersement(JadeBusinessServiceLocator.getCodeSystemeService()
                    .getFamilleCodeSysteme(IREOrdresVersements.CS_GROUPE_TYPE_ORDRE_VERSEMENT));

            ovAjaxViewBean.setCsTypeOrdreVersement(ordreVersement.getCsType());

            if (!JadeStringUtil.isBlankOrZero(ordreVersement.getIdPrestationAccordeeCompensee())) {
                ovAjaxViewBean.setCodePrestation(ordreVersement.getCodePrestationCompensee());
            } else {
                ovAjaxViewBean.setCodePrestation(ordreVersement.getCodePrestationDiminuee());
            }

            if (!JadeStringUtil.isBlankOrZero(ordreVersement.getDateDebutRenteVerseeATort())) {
                ovAjaxViewBean.setDateDebutDroit(ordreVersement.getDateDebutRenteVerseeATort());
                ovAjaxViewBean.setDateFinDroit(ordreVersement.getDateFinRenteVerseeATort());
            } else {
                if (!JadeStringUtil.isBlankOrZero(ordreVersement.getIdPrestationAccordeeCompensee())) {
                    ovAjaxViewBean.setDateDebutDroit(ordreVersement.getDateDebutDroitCompensee());
                    ovAjaxViewBean.setDateFinDroit(ordreVersement.getDateFinDroitCompensee());
                } else {
                    ovAjaxViewBean.setDateDebutDroit(ordreVersement.getDateDebutDroitDiminuee());
                    ovAjaxViewBean.setDateFinDroit(ordreVersement.getDateFinDroitDiminuee());
                }
            }

            ovAjaxViewBean.setCompense(ordreVersement.getIsCompense());
            ovAjaxViewBean.setMontantCompense(ordreVersement.getMontant());
            ovAjaxViewBean.setMontantDette(ordreVersement.getMontantDette());
            ovAjaxViewBean.setNom(ordreVersement.getNomTiersOrdreVersement());
            ovAjaxViewBean.setPrenom(ordreVersement.getPrenomTiersOrdreVersement());
        }
    }

    @Override
    protected void _update(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {

        IREOrdreVersementAjaxViewBean ajaxViewBean = (IREOrdreVersementAjaxViewBean) viewBean;

        if ("CID".equals(ajaxViewBean.getProvenance())) {

            CompensationInterDecisionCrudService daoService = CorvusCrudServiceLocator
                    .getCompensationInterDecisionCrudService();

            RECompensationInterDecisionAjaxViewBean cidAjaxViewBean = (RECompensationInterDecisionAjaxViewBean) ajaxViewBean;

            CompensationInterDecision cid = new CompensationInterDecision();
            cid.setId(cidAjaxViewBean.getIdCompensationInterDecision());
            cid = daoService.read(cid);

            cid.setMontantCompense(cidAjaxViewBean.getMontantCompense());

            cid = daoService.update(cid);

            mettreAJourSoldesPourRestitution(cid.getOrdreVersementDecisionCompensee());
            mettreAJourSoldesPourRestitution(cid.getOrdreVersementDecisionPonctionnee());
        } else if ("OV".equals(ajaxViewBean.getProvenance())) {

            OrdresVersementCrudService daoService = CorvusCrudServiceLocator.getOrdresVersementCrudService();

            REOrdresVersementsDetailAjaxViewBean ordreVersementsAjaxViewBean = (REOrdresVersementsDetailAjaxViewBean) viewBean;

            OrdreVersement ordreVersement = new OrdreVersement();
            ordreVersement.setId(ordreVersementsAjaxViewBean.getIdOrdreVersement());
            ordreVersement = daoService.read(ordreVersement);

            ordreVersement.setCompense(ordreVersementsAjaxViewBean.isCompense());
            ordreVersement.setMontantCompense(ordreVersementsAjaxViewBean.getMontantCompense());
            ordreVersement = daoService.update(ordreVersement);

            mettreAJourSoldesPourRestitution(ordreVersement);
        } else if ("Restitution".equals(ajaxViewBean.getProvenance())) {

            SoldePourRestitutionCrudService daoService = CorvusCrudServiceLocator.getSoldePourRestitutionCrudService();

            REGestionRestitutionAjaxViewBean restitutionAjaxViewBean = (REGestionRestitutionAjaxViewBean) ajaxViewBean;

            SoldePourRestitution soldePourRestitution = new SoldePourRestitution();
            soldePourRestitution.setId(restitutionAjaxViewBean.getIdSoldePourRestitution());
            soldePourRestitution = daoService.read(soldePourRestitution);

            soldePourRestitution.setType(restitutionAjaxViewBean.getTypeSoldePourRestitution());
            if (soldePourRestitution.getType() == TypeSoldePourRestitution.RESTITUTION) {
                soldePourRestitution.setMontantRetenueMensuelle(BigDecimal.ZERO);
            } else if (soldePourRestitution.getType() == TypeSoldePourRestitution.RETENUES) {
                soldePourRestitution.setMontantRetenueMensuelle(restitutionAjaxViewBean.getMontantRetenueMensuelle());
            }
            soldePourRestitution = daoService.update(soldePourRestitution);
        }
    }

    @Override
    protected FWViewBeanInterface execute(final FWViewBeanInterface viewBean, final FWAction action,
            final BISession session) {
        if ("creerCIDAJAX".equals(action.getActionPart())) {
            try {
                RECompensationInterDecisionAjaxViewBean ajaxViewBean = (RECompensationInterDecisionAjaxViewBean) viewBean;

                DecisionService decisionService = CorvusServiceLocator.getDecisionService();
                ajaxViewBean.setDecisionsPourCompensationInterDecision(decisionService
                        .getDecisionsAvecSoldePositifDeLaFamille(ajaxViewBean.getIdDecisionDeficitaire()));
                ajaxViewBean.setSoldeDecisionDeficitaire(decisionService.getDecision(
                        ajaxViewBean.getIdDecisionDeficitaire()).getSolde());

                return ajaxViewBean;
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else if ("gererRestitutionAJAX".equals(action.getActionPart())) {
            try {
                REGestionRestitutionAjaxViewBean ajaxViewBean = (REGestionRestitutionAjaxViewBean) viewBean;

                DecisionService decisionService = CorvusServiceLocator.getDecisionService();
                Decision decision = decisionService.getDecision(ajaxViewBean.getIdDecision());
                SoldePourRestitution soldePourRestitution = decisionService
                        .getSoldePourRestitutionDuBeneficiairePrincipal(decision);

                ajaxViewBean.setDecision(decision);
                ajaxViewBean.setSoldePourRestitution(soldePourRestitution);

                JadeCodeSystemeService codeSystemeService = JadeBusinessServiceLocator.getCodeSystemeService();
                ajaxViewBean.setCodesSystemeTypeSoldePourRestitution(codeSystemeService
                        .getFamilleCodeSysteme(IRESoldePourRestitution.CS_GROUPE_TYPE_RESTITUTION));

            } catch (Exception ex) {
                viewBean.setMessage(ex.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            throw new UnsupportedOperationException("unknown type of view bean");
        }
        return viewBean;
    }

    private void mettreAJourSoldesPourRestitution(final OrdreVersement ordreVersement)
            throws JadeApplicationServiceNotAvailableException {

        DecisionService decisionService = CorvusServiceLocator.getDecisionService();
        Decision decision = decisionService.getDecisionPourIdOrdreVersement(ordreVersement.getId());
        decisionService.recalculerSoldePourRestitution(decision.getId());
    }
}
