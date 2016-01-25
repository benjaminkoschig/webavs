package ch.globaz.aries.businessimpl.services;

import globaz.aries.print.ARDecisionCgas_Doc;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeProgressBarModel;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModulePassageManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModule;
import globaz.musca.db.facturation.FAPassageModuleManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import ch.globaz.aries.business.beans.decisioncgas.DecisionCGASBean;
import ch.globaz.aries.business.constantes.ARDecisionEtat;
import ch.globaz.aries.business.constantes.ARDecisionType;
import ch.globaz.aries.business.constantes.ARDetailDecisionType;
import ch.globaz.aries.business.constantes.ARParametrePlageValeur;
import ch.globaz.aries.business.constantes.ARSortieEtat;
import ch.globaz.aries.business.exceptions.AriesException;
import ch.globaz.aries.business.models.ComplexDecisionCGASAffiliationCotisation;
import ch.globaz.aries.business.models.ComplexDecisionCGASAffiliationCotisationSearchModel;
import ch.globaz.aries.business.models.ComplexSortieCGASDecisionCGASAffiliation;
import ch.globaz.aries.business.models.ComplexSortieCGASDecisionCGASAffiliationSearchModel;
import ch.globaz.aries.business.models.DecisionCGASSearchModel;
import ch.globaz.aries.business.models.DetailDecisionCGASSearchModel;
import ch.globaz.aries.business.models.SimpleDecisionCGAS;
import ch.globaz.aries.business.models.SimpleDetailDecisionCGAS;
import ch.globaz.aries.business.models.SimpleSortieCGAS;
import ch.globaz.aries.business.services.AriesServiceLocator;
import ch.globaz.aries.business.services.DecisionCGASService;
import ch.globaz.aries.businessimpl.checkers.DecisionCGASBeanChecker;
import ch.globaz.aries.businessimpl.checkers.RenouvellementDecisionCGASChecker;
import ch.globaz.aries.exceptions.AriesNotImplementedException;
import ch.globaz.aries.web.application.ARApplication;
import ch.globaz.ariesaurigacommon.utils.AriesAurigaDateUtils;
import ch.globaz.auriga.business.exceptions.AurigaException;
import ch.globaz.common.business.models.ResultatTraitementMasseCsvJournal;
import ch.globaz.common.businessimpl.models.UnitTaskResultState;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.business.services.FABusinessServiceLocator;
import ch.globaz.naos.business.model.AffiliationAssuranceComplexModel;
import ch.globaz.naos.business.model.AffiliationAssuranceSearchComplexModel;
import ch.globaz.naos.business.model.AffiliationComplexModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.model.CotisationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

public class DecisionCGASServiceImpl implements DecisionCGASService {

    @Override
    public List<SimpleDetailDecisionCGAS> beanToListDetailDecisionCGAS(DecisionCGASBean entity) {

        List<SimpleDetailDecisionCGAS> listDetailDecisionCGAS = new ArrayList<SimpleDetailDecisionCGAS>();

        if (!JadeStringUtil.isBlankOrZero(entity.getCulturePlaine().getNombre())) {
            listDetailDecisionCGAS.add(entity.getCulturePlaine());
        }

        if (!JadeStringUtil.isBlankOrZero(entity.getCultureArboricole().getNombre())) {
            listDetailDecisionCGAS.add(entity.getCultureArboricole());
        }

        if (!JadeStringUtil.isBlankOrZero(entity.getCultureMaraichere().getNombre())) {
            listDetailDecisionCGAS.add(entity.getCultureMaraichere());
        }

        if (!JadeStringUtil.isBlankOrZero(entity.getVigneNordCanton().getNombre())) {
            listDetailDecisionCGAS.add(entity.getVigneNordCanton());
        }

        if (!JadeStringUtil.isBlankOrZero(entity.getVigneEstCanton().getNombre())) {
            listDetailDecisionCGAS.add(entity.getVigneEstCanton());
        }

        if (!JadeStringUtil.isBlankOrZero(entity.getVigneLaCote().getNombre())) {
            listDetailDecisionCGAS.add(entity.getVigneLaCote());
        }

        if (!JadeStringUtil.isBlankOrZero(entity.getUgbPlaine().getNombre())) {
            listDetailDecisionCGAS.add(entity.getUgbPlaine());
        }

        if (!JadeStringUtil.isBlankOrZero(entity.getUgbMontagne().getNombre())) {
            listDetailDecisionCGAS.add(entity.getUgbMontagne());
        }

        if (!JadeStringUtil.isBlankOrZero(entity.getUgbSpecial().getNombre())) {
            listDetailDecisionCGAS.add(entity.getUgbSpecial());
        }

        if (!JadeStringUtil.isBlankOrZero(entity.getAlpage().getNombre())) {
            listDetailDecisionCGAS.add(entity.getAlpage());
        }

        return listDetailDecisionCGAS;
    }

    private void calculerDecisionCGAS(DecisionCGASBean entity, List<SimpleDetailDecisionCGAS> listDetailDecisionCGAS)
            throws Exception {

        FWCurrency theCotisationAnnuelle = new FWCurrency("0");

        for (SimpleDetailDecisionCGAS aDetailDecisionCGAS : listDetailDecisionCGAS) {
            ARParametrePlageValeur theParametreMontantUnitaire = ARDetailDecisionType.getEnumFromCodeSystem(
                    aDetailDecisionCGAS.getType()).getParametrePlageValeur();

            FWCurrency montantUnitaireDetailDecision = AriesServiceLocator.getParametreService()
                    .getValeurNumeriqueFWCurrency(theParametreMontantUnitaire, entity.getDecisionCGAS().getDateDebut());

            FWCurrency montantTotal = new FWCurrency(montantUnitaireDetailDecision.doubleValue()
                    * (new FWCurrency(aDetailDecisionCGAS.getNombre()).doubleValue()));

            FWCurrency montantTotalArrondi = new FWCurrency(JANumberFormatter.round(montantTotal.doubleValue(), 0.05,
                    2, JANumberFormatter.INF));

            aDetailDecisionCGAS.setMontant(montantUnitaireDetailDecision.toStringFormat());
            aDetailDecisionCGAS.setTotal(montantTotalArrondi.toStringFormat());

            theCotisationAnnuelle.add(montantTotalArrondi.toStringFormat());

        }

        FWCurrency cotisationPalier1[] = AriesServiceLocator.getParametreService().getPlageValeurNumeriqueFWCurrency(
                ARParametrePlageValeur.COTISATION_PALIER_1, entity.getDecisionCGAS().getDateDebut());
        FWCurrency cotisationPalier2[] = AriesServiceLocator.getParametreService().getPlageValeurNumeriqueFWCurrency(
                ARParametrePlageValeur.COTISATION_PALIER_2, entity.getDecisionCGAS().getDateDebut());
        FWCurrency cotisationPlafond = AriesServiceLocator.getParametreService().getValeurNumeriqueFWCurrency(
                ARParametrePlageValeur.COTISATION_PLAFOND, entity.getDecisionCGAS().getDateDebut());

        if ((theCotisationAnnuelle.doubleValue() > cotisationPalier1[0].doubleValue())
                && (theCotisationAnnuelle.doubleValue() <= cotisationPalier1[1].doubleValue())) {
            theCotisationAnnuelle = new FWCurrency(cotisationPalier1[2].doubleValue());
        } else if ((theCotisationAnnuelle.doubleValue() > cotisationPalier2[0].doubleValue())
                && (theCotisationAnnuelle.doubleValue() <= cotisationPalier2[1].doubleValue())) {
            theCotisationAnnuelle = new FWCurrency(cotisationPalier2[2].doubleValue());
        } else if (theCotisationAnnuelle.doubleValue() > cotisationPlafond.doubleValue()) {
            theCotisationAnnuelle = new FWCurrency(cotisationPlafond.doubleValue());
        }

        FWCurrency theCotisationAnnuelleArrondie = new FWCurrency(JANumberFormatter.round(
                theCotisationAnnuelle.doubleValue(), 0.05, 2, JANumberFormatter.INF));
        FWCurrency theCotisationTrimestrielleArrondie = new FWCurrency(JANumberFormatter.round(
                theCotisationAnnuelle.doubleValue() / 4, 0.05, 2, JANumberFormatter.INF));
        FWCurrency theCotisationMensuelleArrondie = new FWCurrency(JANumberFormatter.round(
                theCotisationAnnuelle.doubleValue() / 12, 0.05, 2, JANumberFormatter.INF));

        int theNbMoisDecision = AriesAurigaDateUtils.getNombreMoisEntreDates(entity.getDecisionCGAS().getDateDebut(),
                entity.getDecisionCGAS().getDateFin());

        FWCurrency theCotisationPeriodeArrondie = null;

        // Attention ! Les affectations ci-dessous font que theCotisationPeriodeArrondie et respectivement
        // theCotisationAnnuelleArrondie ou theCotisationTrimestrielleArrondie ou theCotisationMensuelleArrondie ont la
        // mêmme référence mémoire
        if (theNbMoisDecision == 12) {
            theCotisationPeriodeArrondie = theCotisationAnnuelleArrondie;
        } else if (theNbMoisDecision == 3) {
            theCotisationPeriodeArrondie = theCotisationTrimestrielleArrondie;
        } else if (theNbMoisDecision == 1) {
            theCotisationPeriodeArrondie = theCotisationMensuelleArrondie;
        } else {
            theCotisationPeriodeArrondie = new FWCurrency(theCotisationMensuelleArrondie.doubleValue()
                    * theNbMoisDecision);
        }

        if (entity.getDecisionCGAS().getExempte()) {
            theCotisationPeriodeArrondie = new FWCurrency(0);
            theCotisationAnnuelleArrondie = new FWCurrency(0);
            theCotisationTrimestrielleArrondie = new FWCurrency(0);
            theCotisationMensuelleArrondie = new FWCurrency(0);
        }

        entity.getDecisionCGAS().setCotisationPeriode(theCotisationPeriodeArrondie.toStringFormat());
        entity.getDecisionCGAS().setCotisationAnnuelle(theCotisationAnnuelleArrondie.toStringFormat());
        entity.getDecisionCGAS().setCotisationTrimestrielle(theCotisationTrimestrielleArrondie.toStringFormat());
        entity.getDecisionCGAS().setCotisationMensuelle(theCotisationMensuelleArrondie.toStringFormat());
    }

    private DecisionCGASBean clearDetailDecisionPartFromBean(DecisionCGASBean entity) {

        entity.setCultureArboricole(new SimpleDetailDecisionCGAS());
        entity.setCultureMaraichere(new SimpleDetailDecisionCGAS());
        entity.setCulturePlaine(new SimpleDetailDecisionCGAS());
        entity.setVigneEstCanton(new SimpleDetailDecisionCGAS());
        entity.setVigneLaCote(new SimpleDetailDecisionCGAS());
        entity.setVigneNordCanton(new SimpleDetailDecisionCGAS());
        entity.setUgbMontagne(new SimpleDetailDecisionCGAS());
        entity.setUgbPlaine(new SimpleDetailDecisionCGAS());
        entity.setUgbSpecial(new SimpleDetailDecisionCGAS());
        entity.setAlpage(new SimpleDetailDecisionCGAS());

        return entity;
    }

    private DecisionCGASBean clone(DecisionCGASBean decisionCGASBeanOriginale) {

        DecisionCGASBean decisionCGASBeanClone = new DecisionCGASBean();
        decisionCGASBeanClone.setDecisionCGAS(new SimpleDecisionCGAS());

        SimpleDecisionCGAS decisionCGASOriginale = decisionCGASBeanOriginale.getDecisionCGAS();
        SimpleDecisionCGAS decisionCGASClone = decisionCGASBeanClone.getDecisionCGAS();

        decisionCGASClone.setAnnee(decisionCGASOriginale.getAnnee());
        decisionCGASClone.setCotisationAnnuelle(decisionCGASOriginale.getCotisationAnnuelle());
        decisionCGASClone.setCotisationMensuelle(decisionCGASOriginale.getCotisationMensuelle());
        decisionCGASClone.setCotisationPeriode(decisionCGASOriginale.getCotisationPeriode());
        decisionCGASClone.setCotisationTrimestrielle(decisionCGASOriginale.getCotisationTrimestrielle());
        decisionCGASClone.setDateDebut(decisionCGASOriginale.getDateDebut());
        decisionCGASClone.setDateDonnees(decisionCGASOriginale.getDateDonnees());
        decisionCGASClone.setDateFin(decisionCGASOriginale.getDateFin());
        decisionCGASClone.setEtat(decisionCGASOriginale.getEtat());
        decisionCGASClone.setExempte(decisionCGASOriginale.getExempte());
        decisionCGASClone.setIdAffiliation(decisionCGASOriginale.getIdAffiliation());
        decisionCGASClone.setIdPassageFacturation(decisionCGASOriginale.getIdPassageFacturation());
        decisionCGASClone.setIdResponsable(decisionCGASOriginale.getIdResponsable());
        decisionCGASClone.setType(decisionCGASOriginale.getType());
        decisionCGASClone.setIdDecisionRectifiee(decisionCGASOriginale.getIdDecisionRectifiee());

        List<SimpleDetailDecisionCGAS> listDetailDecisionCGASOriginale = beanToListDetailDecisionCGAS(decisionCGASBeanOriginale);

        List<SimpleDetailDecisionCGAS> listDetailDecisionCGASClone = new ArrayList<SimpleDetailDecisionCGAS>();
        for (SimpleDetailDecisionCGAS aDetailDecisionCGASOriginale : listDetailDecisionCGASOriginale) {
            SimpleDetailDecisionCGAS aDetailDecisionCGASClone = new SimpleDetailDecisionCGAS();

            aDetailDecisionCGASClone.setMontant(aDetailDecisionCGASOriginale.getMontant());
            aDetailDecisionCGASClone.setNombre(aDetailDecisionCGASOriginale.getNombre());
            aDetailDecisionCGASClone.setTotal(aDetailDecisionCGASOriginale.getTotal());
            aDetailDecisionCGASClone.setType(aDetailDecisionCGASOriginale.getType());

            listDetailDecisionCGASClone.add(aDetailDecisionCGASClone);
        }

        listDetailDecisionCGASToBean(decisionCGASBeanClone, listDetailDecisionCGASClone);

        return decisionCGASBeanClone;
    }

    @Override
    public DecisionCGASBean create(DecisionCGASBean entity) throws Exception {

        AffiliationSimpleModel theAffiliation = AFBusinessServiceLocator.getAffiliationService().read(
                entity.getDecisionCGAS().getIdAffiliation());

        List<String> listExceptedIdDecisionActive = new ArrayList<String>();

        if (!JadeStringUtil.isBlankOrZero(entity.getDecisionCGAS().getIdDecisionRectifiee())) {
            listExceptedIdDecisionActive.add(entity.getDecisionCGAS().getIdDecisionRectifiee());
        }

        DecisionCGASBeanChecker.checkForCreate(
                entity,
                theAffiliation,
                isAlreadyADecisionActive(theAffiliation.getId(), entity.getDecisionCGAS().getDateDebut(), entity
                        .getDecisionCGAS().getDateFin(), listExceptedIdDecisionActive));

        List<SimpleDetailDecisionCGAS> listDetailDecisionCGAS = beanToListDetailDecisionCGAS(entity);

        calculerDecisionCGAS(entity, listDetailDecisionCGAS);

        entity.getDecisionCGAS().setEtat(ARDecisionEtat.VALIDEE.getCodeSystem());

        entity.setDecisionCGAS((SimpleDecisionCGAS) JadePersistenceManager.add(entity.getDecisionCGAS()));

        createDetailDecisionCGAS(listDetailDecisionCGAS, entity.getDecisionCGAS().getIdDecision());

        entity = listDetailDecisionCGASToBean(entity, listDetailDecisionCGAS);

        if (JACalendar.getYear(JACalendar.todayJJsMMsAAAA()) <= Integer.valueOf(entity.getDecisionCGAS().getAnnee())
                .intValue()) {
            updateCotisationAffiliation(entity);
        }

        // si il s'agit d'une décision réctificative
        if (!JadeStringUtil.isBlankOrZero(entity.getDecisionCGAS().getIdDecisionRectifiee())) {

            // vérification de la décision rectifiée
            DecisionCGASBean decisionRectifieeBean = read(entity.getDecisionCGAS().getIdDecisionRectifiee());
            DecisionCGASBeanChecker.checkDecisionRectifieeForCreate(decisionRectifieeBean);
            // check spécifique au décision réctificative
            DecisionCGASBeanChecker.checkForRectificative(entity, decisionRectifieeBean);

            // mettre la décision réctifiée en état rectfiée

            decisionRectifieeBean.getDecisionCGAS().setEtat(ARDecisionEtat.RECTIFIEE.getCodeSystem());
            decisionRectifieeBean.getDecisionCGAS().setIdPassageFacturation("0");
            JadePersistenceManager.update(decisionRectifieeBean.getDecisionCGAS());

            // créer une sortie CGAS
            // rechercher le passage de facturation à utiliser (génère une exception si aucun passage valide ouvert)
            String idPassageFacturation = findPassageFacturation();
            if (!JadeStringUtil.isBlankOrZero(idPassageFacturation)) {
                // calcul du montant de l'extourne (cotiRectificative - cotiRectifiee)
                FWCurrency cotisationPeriodeRectifiee = new FWCurrency(decisionRectifieeBean.getDecisionCGAS()
                        .getCotisationPeriode());
                FWCurrency cotisationPeriodeRectificative = new FWCurrency(entity.getDecisionCGAS()
                        .getCotisationPeriode());
                FWCurrency montantExtourne = new FWCurrency(JANumberFormatter.round(
                        cotisationPeriodeRectificative.doubleValue() - cotisationPeriodeRectifiee.doubleValue(), 0.05,
                        2, JANumberFormatter.INF));

                // création de la sortie
                SimpleSortieCGAS sortie = new SimpleSortieCGAS();
                sortie.setIdDecision(entity.getDecisionCGAS().getIdDecision());
                sortie.setDateSortie(JACalendar.todayJJsMMsAAAA());
                sortie.setMontantExtourne(montantExtourne.toStringFormat());
                sortie.setEtat(ARSortieEtat.NON_COMPTABILISEE.getCodeSystem());
                sortie.setIdPassageFacturation(idPassageFacturation);
                AriesServiceLocator.getSortieCGASService().create(sortie);
            } else {
                throw new Exception("idPassage facturation is null or zero");
            }
        }

        return entity;
    }

    @Override
    public DecisionCGASBean createDecisionCGASBeanFromDecisionCGAS(SimpleDecisionCGAS decisionCGAS)
            throws JadePersistenceException {
        DecisionCGASBean decisionCGASBean = new DecisionCGASBean();
        decisionCGASBean.setDecisionCGAS(decisionCGAS);

        List<SimpleDetailDecisionCGAS> listDetailDecisionCGAS = searchDetailDecisionCGAS(decisionCGASBean
                .getDecisionCGAS().getIdDecision());

        decisionCGASBean = listDetailDecisionCGASToBean(decisionCGASBean, listDetailDecisionCGAS);

        return decisionCGASBean;
    }

    private void createDetailDecisionCGAS(List<SimpleDetailDecisionCGAS> listDetailDecisionCGAS,
            String theIdDecisionCGAS) throws JadePersistenceException {

        for (SimpleDetailDecisionCGAS aDetailDecisionCGAS : listDetailDecisionCGAS) {
            aDetailDecisionCGAS.setIdDecision(theIdDecisionCGAS);
            aDetailDecisionCGAS = (SimpleDetailDecisionCGAS) JadePersistenceManager.add(aDetailDecisionCGAS);
        }

    }

    @Override
    public DecisionCGASBean delete(DecisionCGASBean entity) throws Exception {
        DecisionCGASBeanChecker.checkForDelete(entity);

        // si la décision est de type réctificative et à l'état validée on supprime la sortie liée et on réactive la
        // décision rectifiée
        if (DecisionCGASBeanChecker.isTypeRectif(entity.getDecisionCGAS().getType())
                && ARDecisionEtat.VALIDEE.getCodeSystem().equals(entity.getDecisionCGAS().getEtat())) {
            deleteSortieForDecision(entity.getDecisionCGAS().getIdDecision());

            // réactivation de la décision rectifiée
            reactivateDecisionRectifiee(entity);
        }

        // suppression de la décision
        entity.getDecisionCGAS().setEtat(ARDecisionEtat.SUPPRIMEE.getCodeSystem());
        entity.getDecisionCGAS().setIdPassageFacturation("0");
        entity.setDecisionCGAS((SimpleDecisionCGAS) JadePersistenceManager.update(entity.getDecisionCGAS()));

        return entity;
    }

    /**
     * Permet de supprimer les décisions lors de l'extourne (radiation d'un affilié). Cette méthode supprime également
     * les sorties non-comptablisée liées aux décisions qui sont à l'état "validée". Les sorties à l'état "comptablisée"
     * sont conservées mais la décisions liée est mise à l'état "supprimée".
     * 
     * @param decisionCgasBean
     * @return
     * @throws Exception
     */
    private DecisionCGASBean deleteDecisionForExtourne(DecisionCGASBean decisionCgasBean) throws Exception {

        // suppression de la décision
        decisionCgasBean.getDecisionCGAS().setEtat(ARDecisionEtat.SUPPRIMEE.getCodeSystem());
        decisionCgasBean.getDecisionCGAS().setIdPassageFacturation("0");
        decisionCgasBean.setDecisionCGAS((SimpleDecisionCGAS) JadePersistenceManager.update(decisionCgasBean
                .getDecisionCGAS()));

        return decisionCgasBean;
    }

    private int deleteDetailDecisionCGAS(String theIdDecisionCGAS) throws JadePersistenceException {

        DetailDecisionCGASSearchModel theDetailDecisionSearchModel = new DetailDecisionCGASSearchModel();
        theDetailDecisionSearchModel.setForIdDecision(theIdDecisionCGAS);

        return JadePersistenceManager.delete(theDetailDecisionSearchModel);
    }

    /**
     * Supprime les sorties à l'état "non-comptablisée" pour une décision donnée. Il ne doit y avoir qu'une seule sortie
     * par décision
     * 
     * @param idDecisionCgas
     * @throws JadeNoBusinessLogSessionError
     * @throws Exception
     */
    private void deleteSortieForDecision(String idDecisionCgas) throws JadeNoBusinessLogSessionError, Exception {
        ComplexSortieCGASDecisionCGASAffiliationSearchModel searchComplexSortie = new ComplexSortieCGASDecisionCGASAffiliationSearchModel();
        searchComplexSortie.setForIdDecision(idDecisionCgas);
        searchComplexSortie.setForEtat(ARSortieEtat.NON_COMPTABILISEE.getCodeSystem());
        searchComplexSortie = AriesServiceLocator.getSortieCGASService().search(searchComplexSortie);
        // il ne doit y avoir qu'une seule sortie non-comptabilisée pour une décision validée
        if (searchComplexSortie.getSize() == 1) {
            ComplexSortieCGASDecisionCGASAffiliation complexSortie = (ComplexSortieCGASDecisionCGASAffiliation) searchComplexSortie
                    .getSearchResults()[0];
            AriesServiceLocator.getSortieCGASService().delete(complexSortie.getSortieCgas());
        } else if (searchComplexSortie.getSize() > 1) {
            throw new Exception("unable to delete sortie, a lot of sortie found for idDecision : " + idDecisionCgas);
        }
    }

    @Override
    public void extournerDecisions(String idAffiliation, String fromDate) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idAffiliation)) {
            throw new Exception("idAffiliation can not be null or zero !");
        }
        if (JadeStringUtil.isBlankOrZero(fromDate)) {
            throw new Exception("fromDate can not be null or zero !");
        }

        // rechercher le passage de facturation à utiliser (génère une exception si aucun passage valide ouvert)
        String idPassageFacturation = findPassageFacturation();

        if (!JadeStringUtil.isBlankOrZero(idPassageFacturation)) {
            // récupération des décisions dont l'année est >= à l'année de fromDate
            // sauf si la radiation est au 31.12
            // dans ce cas particulier il suffit de récupérer les décisions dont l'année est > à l'année de fromDate
            String fromAnnee = String.valueOf(JACalendar.getYear(fromDate));
            if (("31.12." + fromAnnee).equalsIgnoreCase(fromDate)) {
                fromAnnee = String.valueOf(JACalendar.getYear(fromDate) + 1);
            }
            DecisionCGASSearchModel decisionCgasSearchModel = new DecisionCGASSearchModel();
            decisionCgasSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            decisionCgasSearchModel.setForIdAffiliation(idAffiliation);
            decisionCgasSearchModel.setFromAnnee(fromAnnee);
            decisionCgasSearchModel.setForNotEtat(ARDecisionEtat.SUPPRIMEE.getCodeSystem());
            decisionCgasSearchModel = this.search(decisionCgasSearchModel);
            for (JadeAbstractModel abstractModel : decisionCgasSearchModel.getSearchResults()) {
                SimpleDecisionCGAS decision = (SimpleDecisionCGAS) abstractModel;
                // si la décision est à l'état comptablilisé on crée une sortie
                boolean wantCreateSortie = false;
                if (ARDecisionEtat.COMPTABILISEE.getCodeSystem().equals(decision.getEtat())
                        || ARDecisionEtat.REPRISE.getCodeSystem().equals(decision.getEtat())) {
                    wantCreateSortie = true;
                }

                // on supprime la décision
                deleteDecisionForExtourne(createDecisionCGASBeanFromDecisionCGAS(decision));

                if (wantCreateSortie) {
                    // création de la sortie correspondante
                    FWCurrency montantExtourne = new FWCurrency(decision.getCotisationPeriode());
                    montantExtourne.negate();
                    SimpleSortieCGAS sortie = new SimpleSortieCGAS();
                    sortie.setIdDecision(decision.getIdDecision());
                    sortie.setDateSortie(JACalendar.todayJJsMMsAAAA());
                    sortie.setMontantExtourne(montantExtourne.toStringFormat());
                    sortie.setEtat(ARSortieEtat.NON_COMPTABILISEE.getCodeSystem());
                    sortie.setIdPassageFacturation(idPassageFacturation);
                    AriesServiceLocator.getSortieCGASService().create(sortie);
                }
            }
        } else {
            throw new Exception("idPassage facturation is null or zero");
        }
    }

    @Override
    public void extournerDecisionsRollback(String idAffiliation) throws Exception {

        ComplexSortieCGASDecisionCGASAffiliationSearchModel complexSortieCgasSearchModel = new ComplexSortieCGASDecisionCGASAffiliationSearchModel();
        complexSortieCgasSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        complexSortieCgasSearchModel.setForIdAffiliation(idAffiliation);
        complexSortieCgasSearchModel.setForEtat(ARSortieEtat.NON_COMPTABILISEE.getCodeSystem());
        complexSortieCgasSearchModel = AriesServiceLocator.getSortieCGASService().search(complexSortieCgasSearchModel);

        // parcours des sorties CGAS
        for (JadeAbstractModel abstractModel : complexSortieCgasSearchModel.getSearchResults()) {
            ComplexSortieCGASDecisionCGASAffiliation complexSortieCgas = (ComplexSortieCGASDecisionCGASAffiliation) abstractModel;

            SimpleDecisionCGAS decisionCGAS = complexSortieCgas.getDecisionCgas();
            decisionCGAS.setEtat(ARDecisionEtat.COMPTABILISEE.getCodeSystem());

            decisionCGAS = updateWithoutCalculCotis(decisionCGAS);

            String idDecisionRectifiee = decisionCGAS.getIdDecisionRectifiee();
            while (!JadeStringUtil.isBlankOrZero(idDecisionRectifiee)) {
                DecisionCGASBean decisionCGASBeanRectifiee = read(idDecisionRectifiee);
                SimpleDecisionCGAS decisionCGASRectifiee = decisionCGASBeanRectifiee.getDecisionCGAS();
                decisionCGASRectifiee.setEtat(ARDecisionEtat.RECTIFIEE.getCodeSystem());
                decisionCGASRectifiee = updateWithoutCalculCotis(decisionCGASRectifiee);

                idDecisionRectifiee = decisionCGASRectifiee.getIdDecisionRectifiee();

            }

            AriesServiceLocator.getSortieCGASService().delete(complexSortieCgas.getSortieCgas());

        }

    }

    private String findPassageFacturation() throws Exception {
        FAPassageModuleManager modulePassageManager = new FAPassageModuleManager();
        modulePassageManager.setSession(BSessionUtil.getSessionFromThreadContext());
        modulePassageManager.setForStatus(FAPassage.CS_ETAT_OUVERT);
        modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_DECISION_CAP_CGAS);
        modulePassageManager.find();

        if (modulePassageManager.size() > 0) {
            FAPassageModule passageFacturation = (FAPassageModule) modulePassageManager.getFirstEntity();
            return passageFacturation.getIdPassage();
        } else {
            // erreur metier
            throw new AriesException("aries.passage.facturation.notfound");
        }
    }

    private boolean isAlreadyADecisionActive(String theIdAffiliation, String theDateDebut, String theDateFin,
            List<String> listExceptedIdDecision) throws JadePersistenceException {

        DecisionCGASSearchModel decisionCGASSearchModel = new DecisionCGASSearchModel();

        decisionCGASSearchModel.setForIdAffiliation(theIdAffiliation);
        decisionCGASSearchModel.setForDateDebutLessEqual(theDateFin);
        decisionCGASSearchModel.setForDateFinGreaterEqual(theDateDebut);
        decisionCGASSearchModel.setInEtat(ARDecisionEtat.getListEtatActif());
        decisionCGASSearchModel.setNotInIdDecision(listExceptedIdDecision);

        return JadePersistenceManager.count(decisionCGASSearchModel) >= 1;
    }

    private DecisionCGASBean listDetailDecisionCGASToBean(DecisionCGASBean entity,
            List<SimpleDetailDecisionCGAS> listDetailDecisionCGAS) {

        entity = clearDetailDecisionPartFromBean(entity);

        for (SimpleDetailDecisionCGAS aDetailDecisionCGAS : listDetailDecisionCGAS) {

            if (ARDetailDecisionType.CULTURE_PLAINE.equals(aDetailDecisionCGAS.getType())) {
                entity.setCulturePlaine(aDetailDecisionCGAS);
            } else if (ARDetailDecisionType.CULTURE_ARBORICOLE.equals(aDetailDecisionCGAS.getType())) {
                entity.setCultureArboricole(aDetailDecisionCGAS);
            } else if (ARDetailDecisionType.CULTURE_MARAICHERE.equals(aDetailDecisionCGAS.getType())) {
                entity.setCultureMaraichere(aDetailDecisionCGAS);
            } else if (ARDetailDecisionType.VIGNE_NORD_CANTON.equals(aDetailDecisionCGAS.getType())) {
                entity.setVigneNordCanton(aDetailDecisionCGAS);
            } else if (ARDetailDecisionType.VIGNE_EST_CANTON.equals(aDetailDecisionCGAS.getType())) {
                entity.setVigneEstCanton(aDetailDecisionCGAS);
            } else if (ARDetailDecisionType.VIGNE_LA_COTE.equals(aDetailDecisionCGAS.getType())) {
                entity.setVigneLaCote(aDetailDecisionCGAS);
            } else if (ARDetailDecisionType.UGB_PLAINE.equals(aDetailDecisionCGAS.getType())) {
                entity.setUgbPlaine(aDetailDecisionCGAS);
            } else if (ARDetailDecisionType.UGB_MONTAGNE.equals(aDetailDecisionCGAS.getType())) {
                entity.setUgbMontagne(aDetailDecisionCGAS);
            } else if (ARDetailDecisionType.UGB_SPECIAL.equals(aDetailDecisionCGAS.getType())) {
                entity.setUgbSpecial(aDetailDecisionCGAS);
            } else if (ARDetailDecisionType.ALPAGE.equals(aDetailDecisionCGAS.getType())) {
                entity.setAlpage(aDetailDecisionCGAS);
            } else {
                throw new AriesNotImplementedException();
            }
        }

        return entity;

    }

    @Override
    public String printDecision(String idDecisionCgas) throws Exception {

        DecisionCGASBean decisionCgasBean = AriesServiceLocator.getDecisionCGASService().read(idDecisionCgas);

        String theDateFacturation = JACalendar.todayJJsMMsAAAA();
        String theIdPassage = decisionCgasBean.getDecisionCGAS().getIdPassageFacturation();

        if (!JadeStringUtil.isBlankOrZero(theIdPassage)) {
            PassageModel thePassage = FABusinessServiceLocator.getPassageModelService().read(theIdPassage);
            theDateFacturation = thePassage.getDateFacturation();
        }

        return printUnitaire(decisionCgasBean, theDateFacturation).getDocumentLocation().replace("\\", "/");

    }

    @Override
    public List<JadePublishDocument> printDecisionPassage(String idPassage) throws Exception {

        List<JadePublishDocument> listPrintedDocument = new ArrayList<JadePublishDocument>();

        DecisionCGASSearchModel decisionCGASSearchModel = new DecisionCGASSearchModel();
        decisionCGASSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionCGASSearchModel.setForIdPassageFacturation(idPassage);

        decisionCGASSearchModel = this.search(decisionCGASSearchModel);

        PassageModel thePassage = FABusinessServiceLocator.getPassageModelService().read(idPassage);
        String theDateFacturation = thePassage.getDateFacturation();

        if (decisionCGASSearchModel.getSearchResults() != null) {

            for (JadeAbstractModel abstractModel : decisionCGASSearchModel.getSearchResults()) {

                SimpleDecisionCGAS aDecisionCGAS = (SimpleDecisionCGAS) abstractModel;

                DecisionCGASBean aDecisionCGASBean = createDecisionCGASBeanFromDecisionCGAS(aDecisionCGAS);

                listPrintedDocument.add(printUnitaire(aDecisionCGASBean, theDateFacturation));
            }

        }

        return listPrintedDocument;

    }

    /**
     * Imprime de manière unitaire un document décision en fonction de la décision reçue en paramètre et retourne le
     * premier JadePublishDocument
     * 
     * @param theDecisionCGAS
     * @return
     * @throws Exception
     */
    private JadePublishDocument printUnitaire(DecisionCGASBean decisionCgasBean, String dateFacturation)
            throws Exception {

        AffiliationComplexModel affiliationComplex = AFBusinessServiceLocator.getAffiliationComplexService().read(
                decisionCgasBean.getDecisionCGAS().getIdAffiliation());

        String plafond = AriesServiceLocator
                .getParametreService()
                .getValeurNumeriqueFWCurrency(ARParametrePlageValeur.COTISATION_PLAFOND,
                        decisionCgasBean.getDecisionCGAS().getDateDebut(), ARApplication.DEFAULT_APPLICATION_ARIES)
                .toString();

        if (JadeStringUtil.isBlank(plafond)) {
            throw new AriesException("plafond cgas is blank !!!");
        }

        // si il s'agit d'une décision rectificative on calcul le solde en notre ou votre faveur
        String montantCotisationRectifiee = "";
        String montantTotalNotreVotreFaveur = "";
        if (!JadeStringUtil.isBlankOrZero(decisionCgasBean.getDecisionCGAS().getIdDecisionRectifiee())) {
            DecisionCGASBean decisionRectifieeBean = read(decisionCgasBean.getDecisionCGAS().getIdDecisionRectifiee());

            FWCurrency cotisationPeriodeRectifiee = new FWCurrency(decisionRectifieeBean.getDecisionCGAS()
                    .getCotisationPeriode());
            FWCurrency cotisationPeriodeRectificative = new FWCurrency(decisionCgasBean.getDecisionCGAS()
                    .getCotisationPeriode());
            FWCurrency totalNotreVotreFaveur = new FWCurrency(JANumberFormatter.round(
                    cotisationPeriodeRectificative.doubleValue() - cotisationPeriodeRectifiee.doubleValue(), 0.05, 2,
                    JANumberFormatter.INF));
            montantCotisationRectifiee = cotisationPeriodeRectifiee.toStringFormat();
            montantTotalNotreVotreFaveur = totalNotreVotreFaveur.toStringFormat();
        }

        // impression de la décision
        ARDecisionCgas_Doc decisionCgasDoc = new ARDecisionCgas_Doc();
        decisionCgasDoc.setDateFacturation(dateFacturation);
        decisionCgasDoc.setDecisionCgasBean(decisionCgasBean);
        decisionCgasDoc.setAffiliationComplex(affiliationComplex);
        decisionCgasDoc.setMontantCotisationRectifiee(montantCotisationRectifiee);
        decisionCgasDoc.setMontantTotalNotreVotreFaveur(montantTotalNotreVotreFaveur);
        decisionCgasDoc.setPlafondCotiAnnuelle(plafond);
        decisionCgasDoc.executeProcess();

        // récupération du document
        List<JadePublishDocument> attachedDocuments = decisionCgasDoc.getAttachedDocuments();
        if ((attachedDocuments.size() > 0) && (attachedDocuments.get(0) != null)) {
            return attachedDocuments.get(0);
        } else {
            throw new Exception("no attached documents found for decision cgas !");
        }

    }

    private void reactivateDecisionRectifiee(DecisionCGASBean decisionCgasBean) throws JadePersistenceException,
            Exception {
        if (JadeStringUtil.isBlankOrZero(decisionCgasBean.getDecisionCGAS().getIdDecisionRectifiee())) {
            throw new IllegalStateException(
                    "unable to reactive decision, idDecisionRectifiee can not be blank or zero !");
        }

        DecisionCGASBean decisionRectifieeBean = read(decisionCgasBean.getDecisionCGAS().getIdDecisionRectifiee());
        decisionRectifieeBean.getDecisionCGAS().setEtat(ARDecisionEtat.COMPTABILISEE.getCodeSystem());
        updateWithoutCalculCotis(decisionRectifieeBean.getDecisionCGAS());

        if (JACalendar.getYear(JACalendar.todayJJsMMsAAAA()) <= Integer.valueOf(
                decisionRectifieeBean.getDecisionCGAS().getAnnee()).intValue()) {
            // mise à jour des cotisations dans l'affiliation
            updateCotisationAffiliation(decisionRectifieeBean);
        }
    }

    @Override
    public DecisionCGASBean read(String idEntity) throws JadeApplicationException, JadePersistenceException {

        DecisionCGASBean decisionCGASBean = new DecisionCGASBean();

        decisionCGASBean.getDecisionCGAS().setIdDecision(idEntity);
        decisionCGASBean.setDecisionCGAS((SimpleDecisionCGAS) JadePersistenceManager.read(decisionCGASBean
                .getDecisionCGAS()));

        List<SimpleDetailDecisionCGAS> listDetailDecisionCGAS = searchDetailDecisionCGAS(decisionCGASBean
                .getDecisionCGAS().getIdDecision());

        decisionCGASBean = listDetailDecisionCGASToBean(decisionCGASBean, listDetailDecisionCGAS);

        return decisionCGASBean;

    }

    @Override
    public DecisionCGASBean renouvelerDecisionCGAS(DecisionCGASBean decisionCGASBeanARenouveler,
            PassageModel thePassageFacturation, boolean hasPassageModuleFacturationDecisionCGAS,
            List<SimpleDecisionCGAS> theListDecisionCGASDejaExistanteDansAnneeDuRenouvellement,
            List<String> theListAffiliePlusieursCotisationDansAnneeDuRenouvellement) throws Exception {

        RenouvellementDecisionCGASChecker.checkDecisionCGASARenouveler(decisionCGASBeanARenouveler,
                theListDecisionCGASDejaExistanteDansAnneeDuRenouvellement,
                theListAffiliePlusieursCotisationDansAnneeDuRenouvellement);
        RenouvellementDecisionCGASChecker.checkPassageFacturation(thePassageFacturation,
                hasPassageModuleFacturationDecisionCGAS);

        DecisionCGASBean nouveauDecisionCGASBean = this.clone(decisionCGASBeanARenouveler);

        int anneeNouvelleDecisionCGAS = Integer.valueOf(decisionCGASBeanARenouveler.getDecisionCGAS().getAnnee())
                .intValue() + 1;

        SimpleDecisionCGAS nouvelleDecisionCGAS = nouveauDecisionCGASBean.getDecisionCGAS();
        nouvelleDecisionCGAS.setAnnee(String.valueOf(anneeNouvelleDecisionCGAS));
        nouvelleDecisionCGAS.setDateDebut("01.01." + anneeNouvelleDecisionCGAS);
        nouvelleDecisionCGAS.setDateFin("31.12." + anneeNouvelleDecisionCGAS);
        nouvelleDecisionCGAS.setDateDonnees(JACalendar.todayJJsMMsAAAA());
        nouvelleDecisionCGAS.setIdPassageFacturation(thePassageFacturation.getIdPassage());
        nouvelleDecisionCGAS.setType(ARDecisionType.DEFINITIVE.getCodeSystem());
        nouvelleDecisionCGAS.setIdDecisionRectifiee("0");

        create(nouveauDecisionCGASBean);

        return nouveauDecisionCGASBean;
    }

    @Override
    public ResultatTraitementMasseCsvJournal renouvelerDecisionCGASMasse(JadeProgressBarModel jadeProgressBarModel,
            boolean simulation, String annee, String numeroAffilieDebut, String numeroAffilieFin,
            String numeroPassageFacturation) throws Exception {

        PassageModel thePassage = FABusinessServiceLocator.getPassageModelService().read(numeroPassageFacturation);

        FAModulePassageManager modulePassageManager = new FAModulePassageManager();
        modulePassageManager.setSession(BSessionUtil.getSessionFromThreadContext());
        modulePassageManager.setForIdPassage(numeroPassageFacturation);
        modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_DECISION_CAP_CGAS);
        boolean hasPassageModuleFacturationDecisionCGAS = modulePassageManager.getCount() >= 1;

        int anneeReferenceRenouvellement = Integer.valueOf(annee).intValue() - 1;

        ComplexDecisionCGASAffiliationCotisationSearchModel theSearchModel = new ComplexDecisionCGASAffiliationCotisationSearchModel();
        theSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        theSearchModel.setInEtatDecision(ARDecisionEtat.getListEtatRenouvelable());
        theSearchModel.setForDateFinDecision("31.12." + anneeReferenceRenouvellement);

        if (!JadeStringUtil.isBlankOrZero(numeroAffilieDebut)) {
            theSearchModel.setForNumeroAffilieGreaterEqual(numeroAffilieDebut);
        }

        if (!JadeStringUtil.isBlankOrZero(numeroAffilieFin)) {
            theSearchModel.setForNumeroAffilieLessEqual(numeroAffilieFin);
        }

        theSearchModel.setForDateFinAffiliationGreater("31.12." + anneeReferenceRenouvellement);
        theSearchModel.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
        theSearchModel.setForTypeAssurance(CodeSystem.TYPE_ASS_CGAS);
        theSearchModel.setForDateFinCotisationGreater("31.12." + anneeReferenceRenouvellement);

        theSearchModel = this.search(theSearchModel);

        // Le tag <order-by> par défaut du ComplexDecisionCGASAffiliationCotisationSearchModel
        // retourne les données triées par numéro d'affilié ascendant puis par date de décision descendante
        List<ComplexDecisionCGASAffiliationCotisation> listDecisionCGASAffiliationCotisation = new ArrayList<ComplexDecisionCGASAffiliationCotisation>();
        List<String> theListAffiliePlusieursCotisationDansAnneeDuRenouvellement = new ArrayList<String>();
        if (theSearchModel.getSearchResults() != null) {
            String theNumeroAffilieCourant = "";
            String theIdCotisationCourante = "";
            for (JadeAbstractModel abstractModel : theSearchModel.getSearchResults()) {
                ComplexDecisionCGASAffiliationCotisation theModel = (ComplexDecisionCGASAffiliationCotisation) abstractModel;

                if (!theNumeroAffilieCourant.equalsIgnoreCase(theModel.getAffiliation().getAffilieNumero())) {
                    listDecisionCGASAffiliationCotisation.add(theModel);
                    theNumeroAffilieCourant = theModel.getAffiliation().getAffilieNumero();
                    theIdCotisationCourante = theModel.getCotisation().getCotisationId();
                } else {
                    if (!theIdCotisationCourante.equalsIgnoreCase(theModel.getCotisation().getCotisationId())
                            && !theListAffiliePlusieursCotisationDansAnneeDuRenouvellement.contains(theModel
                                    .getAffiliation().getAffiliationId())) {
                        theListAffiliePlusieursCotisationDansAnneeDuRenouvellement.add(theModel.getAffiliation()
                                .getAffiliationId());
                    }
                }
            }
        }

        List<SimpleDecisionCGAS> listDecisionCGASDejaExistanteDansAnneeDuRenouvellement = new ArrayList<SimpleDecisionCGAS>();

        DecisionCGASSearchModel decisionCGASSearchModel = new DecisionCGASSearchModel();
        decisionCGASSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionCGASSearchModel.setForAnnee(annee);
        decisionCGASSearchModel.setInEtat(ARDecisionEtat.getListEtatActif());

        decisionCGASSearchModel = this.search(decisionCGASSearchModel);

        if (decisionCGASSearchModel.getSearchResults() != null) {

            for (JadeAbstractModel abstractModel : decisionCGASSearchModel.getSearchResults()) {
                SimpleDecisionCGAS aDecisionCGAS = (SimpleDecisionCGAS) abstractModel;
                listDecisionCGASDejaExistanteDansAnneeDuRenouvellement.add(aDecisionCGAS);
            }
        }

        List<RenouvellementDecisionCGASUnitTask> listUnitTask = new ArrayList<RenouvellementDecisionCGASUnitTask>();
        for (ComplexDecisionCGASAffiliationCotisation aDecisionCGASAffiliationCotisation : listDecisionCGASAffiliationCotisation) {
            DecisionCGASBean theDecisionCGASBeanInput = new DecisionCGASBean();
            theDecisionCGASBeanInput.setDecisionCGAS(aDecisionCGASAffiliationCotisation.getDecisionCGAS());

            List<SimpleDetailDecisionCGAS> listDetailDecisionCGAS = searchDetailDecisionCGAS(theDecisionCGASBeanInput
                    .getDecisionCGAS().getIdDecision());

            theDecisionCGASBeanInput = listDetailDecisionCGASToBean(theDecisionCGASBeanInput, listDetailDecisionCGAS);

            listUnitTask.add(new RenouvellementDecisionCGASUnitTask(theDecisionCGASBeanInput,
                    aDecisionCGASAffiliationCotisation.getAffiliation().getAffilieNumero(),
                    aDecisionCGASAffiliationCotisation.getAffiliation().getRaisonSocialeCourt(), thePassage,
                    hasPassageModuleFacturationDecisionCGAS, listDecisionCGASDejaExistanteDansAnneeDuRenouvellement,
                    theListAffiliePlusieursCotisationDansAnneeDuRenouvellement));
        }

        AriesServiceLocator.getTraitementMasseService().traiter(jadeProgressBarModel, listUnitTask);

        List<String> listCsvLine = new ArrayList<String>();

        StringBuffer csvHeader = new StringBuffer();
        csvHeader.append(JadeThread.getMessage("aries.printing.numero.affilie"));
        csvHeader.append(";");
        csvHeader.append(JadeThread.getMessage("aries.printing.affilie"));
        csvHeader.append(";");
        csvHeader.append(JadeThread.getMessage("aries.printing.periode"));
        csvHeader.append(";");
        csvHeader.append(JadeThread.getMessage("aries.printing.date.decision"));
        csvHeader.append(";");
        csvHeader.append(JadeThread.getMessage("aries.printing.statut"));
        csvHeader.append(";");
        csvHeader.append(JadeThread.getMessage("aries.printing.remarque"));
        csvHeader.append(";");
        csvHeader.append(IOUtils.LINE_SEPARATOR);

        listCsvLine.add(csvHeader.toString());

        boolean hasErreurTechnique = false;
        for (RenouvellementDecisionCGASUnitTask aUnitTask : listUnitTask) {

            if (UnitTaskResultState.ERREUR_TECHNNIQUE.equals(aUnitTask.getResultState())) {
                hasErreurTechnique = true;
            }

            StringBuffer csvLine = new StringBuffer();

            csvLine.append(aUnitTask.getNumeroAffilie());
            csvLine.append(";");
            csvLine.append(aUnitTask.getNomPrenomAffilie());
            csvLine.append(";");
            csvLine.append(aUnitTask.getDecisionCGASBeanInput().getDecisionCGAS().getDateDebut() + " - "
                    + aUnitTask.getDecisionCGASBeanInput().getDecisionCGAS().getDateFin());
            csvLine.append(";");
            csvLine.append(aUnitTask.getDecisionCGASBeanInput().getDecisionCGAS().getDateDonnees());
            csvLine.append(";");
            csvLine.append(aUnitTask.getResultStateLibelle());
            csvLine.append(";");
            csvLine.append(aUnitTask.getErrorMessage() != null ? aUnitTask.getErrorMessage() : "");
            csvLine.append(";");
            csvLine.append(IOUtils.LINE_SEPARATOR);

            listCsvLine.add(csvLine.toString());
        }

        return new ResultatTraitementMasseCsvJournal(hasErreurTechnique, listCsvLine);

    }

    @Override
    public ComplexDecisionCGASAffiliationCotisationSearchModel search(
            ComplexDecisionCGASAffiliationCotisationSearchModel searchModel) throws JadePersistenceException {
        return (ComplexDecisionCGASAffiliationCotisationSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public DecisionCGASSearchModel search(DecisionCGASSearchModel searchModel) throws JadePersistenceException {
        return (DecisionCGASSearchModel) JadePersistenceManager.search(searchModel);
    }

    private List<SimpleDetailDecisionCGAS> searchDetailDecisionCGAS(String theIdDecisionCGAS)
            throws JadePersistenceException {

        List<SimpleDetailDecisionCGAS> listDetailDecisionCGAS = new ArrayList<SimpleDetailDecisionCGAS>();

        DetailDecisionCGASSearchModel theDetailDecisionSearchModel = new DetailDecisionCGASSearchModel();
        theDetailDecisionSearchModel.setForIdDecision(theIdDecisionCGAS);

        theDetailDecisionSearchModel = (DetailDecisionCGASSearchModel) JadePersistenceManager
                .search(theDetailDecisionSearchModel);

        if (theDetailDecisionSearchModel.getSearchResults() != null) {

            for (JadeAbstractModel abstractModel : theDetailDecisionSearchModel.getSearchResults()) {
                SimpleDetailDecisionCGAS aDetailDecisionCGAS = (SimpleDetailDecisionCGAS) abstractModel;
                listDetailDecisionCGAS.add(aDetailDecisionCGAS);
            }
        }

        return listDetailDecisionCGAS;
    }

    @Override
    public DecisionCGASBean update(DecisionCGASBean entity) throws Exception {

        AffiliationSimpleModel theAffiliation = AFBusinessServiceLocator.getAffiliationService().read(
                entity.getDecisionCGAS().getIdAffiliation());

        List<String> listExceptedIdDecisionActive = new ArrayList<String>();
        listExceptedIdDecisionActive.add(entity.getDecisionCGAS().getIdDecision());

        DecisionCGASBeanChecker.checkForUpdate(
                entity,
                theAffiliation,
                isAlreadyADecisionActive(theAffiliation.getId(), entity.getDecisionCGAS().getDateDebut(), entity
                        .getDecisionCGAS().getDateFin(), listExceptedIdDecisionActive));

        List<SimpleDetailDecisionCGAS> listDetailDecisionCGAS = beanToListDetailDecisionCGAS(entity);

        calculerDecisionCGAS(entity, listDetailDecisionCGAS);

        entity.setDecisionCGAS((SimpleDecisionCGAS) JadePersistenceManager.update(entity.getDecisionCGAS()));

        deleteDetailDecisionCGAS(entity.getDecisionCGAS().getIdDecision());

        createDetailDecisionCGAS(listDetailDecisionCGAS, entity.getDecisionCGAS().getIdDecision());

        entity = listDetailDecisionCGASToBean(entity, listDetailDecisionCGAS);

        if (JACalendar.getYear(JACalendar.todayJJsMMsAAAA()) <= Integer.valueOf(entity.getDecisionCGAS().getAnnee())
                .intValue()) {
            updateCotisationAffiliation(entity);
        }

        // si il s'agit d'une décision réctificative
        if (!JadeStringUtil.isBlankOrZero(entity.getDecisionCGAS().getIdDecisionRectifiee())) {

            // lecture de la décision rectifiée
            DecisionCGASBean decisionRectifieeBean = read(entity.getDecisionCGAS().getIdDecisionRectifiee());
            // check spécifique au décision réctificative
            DecisionCGASBeanChecker.checkForRectificative(entity, decisionRectifieeBean);

            // rechercher le passage de facturation à utiliser (génère une exception si aucun passage valide ouvert)
            String idPassageFacturation = findPassageFacturation();
            if (!JadeStringUtil.isBlankOrZero(idPassageFacturation)) {
                // calcul du montant de l'extourne (cotiRectificative - cotiRectifiee)
                FWCurrency cotisationPeriodeRectifiee = new FWCurrency(decisionRectifieeBean.getDecisionCGAS()
                        .getCotisationPeriode());
                FWCurrency cotisationPeriodeRectificative = new FWCurrency(entity.getDecisionCGAS()
                        .getCotisationPeriode());
                FWCurrency montantExtourne = new FWCurrency(JANumberFormatter.round(
                        cotisationPeriodeRectificative.doubleValue() - cotisationPeriodeRectifiee.doubleValue(), 0.05,
                        2, JANumberFormatter.INF));

                // lecture et mise à jour de la sortie
                ComplexSortieCGASDecisionCGASAffiliationSearchModel searchComplexSortie = new ComplexSortieCGASDecisionCGASAffiliationSearchModel();
                searchComplexSortie.setForIdDecision(entity.getDecisionCGAS().getIdDecision());
                searchComplexSortie = AriesServiceLocator.getSortieCGASService().search(searchComplexSortie);
                // il ne doit y avoir qu'une seule sortie pour une décision
                if (searchComplexSortie.getSize() == 1) {
                    ComplexSortieCGASDecisionCGASAffiliation complexSortie = (ComplexSortieCGASDecisionCGASAffiliation) searchComplexSortie
                            .getSearchResults()[0];
                    SimpleSortieCGAS sortie = complexSortie.getSortieCgas();
                    sortie.setIdDecision(entity.getDecisionCGAS().getIdDecision());
                    sortie.setDateSortie(JACalendar.todayJJsMMsAAAA());
                    sortie.setMontantExtourne(montantExtourne.toStringFormat());
                    sortie.setEtat(ARSortieEtat.NON_COMPTABILISEE.getCodeSystem());
                    sortie.setIdPassageFacturation(idPassageFacturation);
                    AriesServiceLocator.getSortieCGASService().update(sortie);
                } else if (searchComplexSortie.getSize() == 0) {
                    throw new Exception("no sortieCap found for idDecision : "
                            + entity.getDecisionCGAS().getIdDecision());
                } else {
                    throw new Exception("a lot of sortieCap found for idDecision : "
                            + entity.getDecisionCGAS().getIdDecision());
                }

            } else {
                throw new Exception("idPassage facturation is null or zero");
            }
        }

        return entity;
    }

    private void updateCotisationAffiliation(DecisionCGASBean entity) throws Exception {

        AffiliationAssuranceSearchComplexModel affiliationAssuranceSearchModel = new AffiliationAssuranceSearchComplexModel();
        affiliationAssuranceSearchModel.setWhereKey("searchCotisationCAPCGASToUpdateAfterDecision");
        affiliationAssuranceSearchModel.setForIdAffiliation(entity.getDecisionCGAS().getIdAffiliation());
        affiliationAssuranceSearchModel.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
        affiliationAssuranceSearchModel.setForTypeAssurance(CodeSystem.TYPE_ASS_CGAS);
        affiliationAssuranceSearchModel.setForDateCotisationDebutLessEqualFinGreaterNull(entity.getDecisionCGAS()
                .getDateDebut());
        affiliationAssuranceSearchModel = AFBusinessServiceLocator.getAffiliationService().searchAffiliationAssurance(
                affiliationAssuranceSearchModel);

        if (affiliationAssuranceSearchModel.getSize() <= 0) {
            throw new AriesException("aries.update.cotisation.affiliation.aucune.cotisation.active", entity
                    .getDecisionCGAS().getDateDebut());
        }

        // Le modèle complexe AffiliationAssuranceSearchComplexModel retourne plusieurs lignes dans le cas où
        // l'assurance a plusieurs paramètres
        // mais toutes les lignes retournées concernent la même cotisation
        // C'est pourquoi il suffit de prendre la première
        CotisationSimpleModel cotisationToUpdate = ((AffiliationAssuranceComplexModel) affiliationAssuranceSearchModel
                .getSearchResults()[0]).getCotisation();

        int anneeCotisationToUpdate = JACalendar.getYear(cotisationToUpdate.getDateDebut());
        int anneeDecisionCGAS = Integer.valueOf(entity.getDecisionCGAS().getAnnee());

        if (anneeDecisionCGAS > anneeCotisationToUpdate
                && anneeDecisionCGAS > JACalendar.getYear(JACalendar.todayJJsMMsAAAA())) {

            if (JadeStringUtil.isBlankOrZero(cotisationToUpdate.getDateFin())) {

                cotisationToUpdate.setDateFin(new JACalendarGregorian().addDays(
                        entity.getDecisionCGAS().getDateDebut(), -1));
                cotisationToUpdate.setMotifFin(CodeSystem.MOTIF_FIN_MASSE);

                cotisationToUpdate = (CotisationSimpleModel) JadePersistenceManager.update(cotisationToUpdate);

            }

            AffiliationAssuranceSearchComplexModel affiliationAssuranceChevauchementSearchModel = new AffiliationAssuranceSearchComplexModel();
            affiliationAssuranceChevauchementSearchModel.setWhereKey("searchCotisationCAPCGASChevauchement");
            affiliationAssuranceChevauchementSearchModel.setForIdAffiliation(entity.getDecisionCGAS()
                    .getIdAffiliation());
            affiliationAssuranceChevauchementSearchModel.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
            affiliationAssuranceChevauchementSearchModel.setForTypeAssurance(CodeSystem.TYPE_ASS_CGAS);
            affiliationAssuranceChevauchementSearchModel.setForDateFinCotisationGreaterEqual(entity.getDecisionCGAS()
                    .getDateDebut());

            affiliationAssuranceChevauchementSearchModel = AFBusinessServiceLocator.getAffiliationService()
                    .searchAffiliationAssurance(affiliationAssuranceChevauchementSearchModel);

            if (affiliationAssuranceChevauchementSearchModel.getSize() >= 1) {
                throw new AurigaException(
                        "aries.update.cotisation.affiliation.creation.cotisation.impossible.car.deja.cotisation.active",
                        entity.getDecisionCGAS().getDateDebut());
            }

            BSession session = BSessionUtil.getSessionFromThreadContext();

            Boolean isExceptionPeriodicite = BConstants.DB_BOOLEAN_TRUE.equalsIgnoreCase(cotisationToUpdate
                    .getExceptionPeriodicite());
            Boolean isMaisonMere = BConstants.DB_BOOLEAN_TRUE.equalsIgnoreCase(cotisationToUpdate.getMaisonMere());

            AFCotisation newCotisation = new AFCotisation();
            newCotisation.setSession(session);
            newCotisation.setAdhesionId(cotisationToUpdate.getAdhesionId());
            newCotisation.setAnneeDecision(cotisationToUpdate.getAnneeDecision());
            newCotisation.setAssuranceId(cotisationToUpdate.getAssuranceId());
            newCotisation.setCategorieTauxId(cotisationToUpdate.getCategorieTauxId());
            newCotisation.setDateDebut(entity.getDecisionCGAS().getDateDebut());
            newCotisation.setExceptionPeriodicite(isExceptionPeriodicite);
            newCotisation.setMaisonMere(isMaisonMere);
            newCotisation.setMasseAnnuelle(cotisationToUpdate.getMasseAnnuelle());
            newCotisation.setMontantAnnuel(entity.getDecisionCGAS().getCotisationAnnuelle());
            newCotisation.setMontantTrimestriel(entity.getDecisionCGAS().getCotisationTrimestrielle());
            newCotisation.setMontantMensuel(entity.getDecisionCGAS().getCotisationMensuelle());
            newCotisation.setMontantSemestriel(cotisationToUpdate.getMontantSemestriel());
            newCotisation.setPeriodicite(cotisationToUpdate.getPeriodicite());
            newCotisation.setPlanAffiliationId(cotisationToUpdate.getPlanAffiliationId());
            newCotisation.setPlanCaisseId(cotisationToUpdate.getPlanCaisseId());
            newCotisation.setTauxAssuranceId(cotisationToUpdate.getTauxAssuranceId());
            newCotisation.setTraitementMoisAnnee(cotisationToUpdate.getTraitementMoisAnnee());

            newCotisation.add();

        } else {

            cotisationToUpdate.setMontantAnnuel(entity.getDecisionCGAS().getCotisationAnnuelle());
            cotisationToUpdate.setMontantTrimestriel(entity.getDecisionCGAS().getCotisationTrimestrielle());
            cotisationToUpdate.setMontantMensuel(entity.getDecisionCGAS().getCotisationMensuelle());

            JadePersistenceManager.update(cotisationToUpdate);
        }

    }

    @Override
    public SimpleDecisionCGAS updateWithoutCalculCotis(SimpleDecisionCGAS decisionCgas)
            throws JadePersistenceException, Exception {
        return (SimpleDecisionCGAS) JadePersistenceManager.update(decisionCgas);
    }
}
