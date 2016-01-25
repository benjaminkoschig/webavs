package ch.globaz.perseus.businessimpl.services.models.demande;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.businessimpl.services.models.pmtmensuel.PmtMensuelServiceImpl;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.exceptions.models.dossier.DossierException;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.creancier.Creancier;
import ch.globaz.perseus.business.models.creancier.CreancierSearchModel;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.perseus.business.models.demande.SimpleDemandeSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.Dette;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;
import ch.globaz.perseus.business.models.dossier.DossierSearchModel;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeSearchModel;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Requerant;
import ch.globaz.perseus.business.models.situationfamille.SituationFamiliale;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.demande.DemandeService;
import ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleAddCheckMessage;
import ch.globaz.perseus.businessimpl.checkers.demande.DemandeChecker;
import ch.globaz.perseus.businessimpl.checkers.situationfamille.SimpleEnfantFamilleChecker;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class DemandeServiceImpl implements DemandeService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#annuler(ch.globaz.perseus.business.models.demande
     * .Demande)
     */
    @Override
    public Demande annuler(Demande demande) throws JadePersistenceException, DemandeException {
        if ((demande == null) || demande.isNew()) {
            throw new DemandeException("Unable to undo demande, the model passed is null or new!");
        }
        try {
            // Contrôle des conditions pour qu'une demande puisse être annulée
            // (la date de début correspond au mois courant)
            if (!JadeDateUtil.areDatesEquals(demande.getSimpleDemande().getDateDebut(), "01."
                    + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt())) {
                JadeThread.logError(this.getClass().getName(), "perseus.demande.annuler.notincurrentmonth");
            }

            // On passe la demande en état enregistré
            demande.getSimpleDemande().setCsEtatDemande(CSEtatDemande.ENREGISTRE.getCodeSystem());

            // Supprimer la prestation si elle est à 0
            PrestationSearchModel prestationSearchModel = new PrestationSearchModel();
            prestationSearchModel.setForIdDemande(demande.getId());
            prestationSearchModel.getInTypeLot().add(CSTypeLot.LOT_DECISION.getCodeSystem());
            prestationSearchModel = PerseusServiceLocator.getPrestationService().search(prestationSearchModel);
            for (JadeAbstractModel m : prestationSearchModel.getSearchResults()) {
                Prestation prestation = (Prestation) m;
                if (JadeNumericUtil.isZeroValue(prestation.getSimplePrestation().getMontantTotal())) {
                    PerseusServiceLocator.getPrestationService().delete(prestation);
                } else {
                    throw new DemandeException(
                            "La demande ne peut pas être annulée, une prestation avec un montant différent de 0 est présente dans un lot");
                }
            }
            demande.getSimpleDemande().setDateTimeDecisionValidation("");
            // On surpasse tous les contrôles en passant par le service simpleDemande
            demande.setSimpleDemande(PerseusImplServiceLocator.getSimpleDemandeService().update(
                    demande.getSimpleDemande()));

            suppressionDesDecisions(demande);
            suppressionDesPCFAccordee(demande);
            suppressionDesCreanciers(demande);

        } catch (DecisionException e) {
            throw new DemandeException("DecisionException in DemandeService#updateAndClean : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available in DemandeService#updateAndClean : " + e.toString(), e);
        } catch (PCFAccordeeException e) {
            throw new DemandeException("PCFAccordeeException in DemandeService#updateAndClean : " + e.toString(), e);
        } catch (LotException e) {
            throw new DemandeException("LotException in DemandeService#updateAndClean : " + e.toString(), e);
        } catch (CreancierException e) {
            throw new DemandeException("CreancierException in DemandeService#updateAndClean : " + e.toString(), e);
        } catch (PaiementException e) {
            throw new DemandeException("PaiementException during annulerDemande : " + e.toString(), e);
        }

        return demande;
    }

    private void suppressionDesCreanciers(Demande demande) throws JadePersistenceException, CreancierException,
            JadeApplicationServiceNotAvailableException {
        CreancierSearchModel creancierSearch = new CreancierSearchModel();
        creancierSearch.setForIdDemande(demande.getId());
        creancierSearch = PerseusServiceLocator.getCreancierService().search(creancierSearch);
        for (JadeAbstractModel model : creancierSearch.getSearchResults()) {
            Creancier creancier = (Creancier) model;
            PerseusServiceLocator.getCreancierService().delete(creancier);
        }
    }

    private void suppressionDesPCFAccordee(Demande demande) throws JadePersistenceException, PCFAccordeeException,
            JadeApplicationServiceNotAvailableException {
        // On supprimer les pcfAccordées
        PCFAccordeeSearchModel pcfAccordeeSearchModel = new PCFAccordeeSearchModel();
        pcfAccordeeSearchModel.setForIdDemande(demande.getId());
        pcfAccordeeSearchModel = PerseusServiceLocator.getPCFAccordeeService().search(pcfAccordeeSearchModel);
        for (JadeAbstractModel model : pcfAccordeeSearchModel.getSearchResults()) {
            PCFAccordee pcfAcc = (PCFAccordee) model;
            PerseusServiceLocator.getPCFAccordeeService().deleteNoUpdateEtatDemande(pcfAcc);
        }
    }

    private void suppressionDesDecisions(Demande demande) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException {
        // On supprime les décisions
        DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
        decisionSearchModel.setForIdDemande(demande.getId());
        decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);
        for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
            PerseusServiceLocator.getDecisionService().delete((Decision) model);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#calculerMontantVerseImpotSource(ch.globaz.perseus
     * .business.models .demande.Demande)
     */
    @Override
    public Float calculerMontantVerseImpotSource(Demande demande) throws DemandeException, JadePersistenceException {

        Float retro = new Float(0);
        try {
            String date = "01." + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

            // Si la date de fin de la demande est avant prendre la date de fin de la demande
            if (JadeDateUtil.isDateBefore(demande.getSimpleDemande().getDateFin(), date)) {
                date = demande.getSimpleDemande().getDateFin();
            }

            retro = this.calculerMontantVerseImpotSource(demande, date);

        } catch (PaiementException e) {
            throw new DemandeException("PaiementException during calculerImpotSourceVerse : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available during calculerImpotSourceVerse : " + e.toString(), e);
        }

        return retro;
    }

    @Override
    public Float calculerMontantVerseImpotSource(Demande demande, String date) throws JadePersistenceException,
            DemandeException {
        if (demande == null) {
            throw new DemandeException("Unable to calculate retro, the model passed is null (demande)");
        }

        // Si la date de fin de la demande est avant prendre la date de fin de la demande
        if (JadeDateUtil.isDateBefore(demande.getSimpleDemande().getDateFin(), date)) {
            date = demande.getSimpleDemande().getDateFin();
        }

        // Montants à définir le retro dû et ce qui a déjà été versé
        Float montantDu = new Float(0);
        Float montantDejaVerse = new Float(0);

        try {
            // Définir le montant dû
            PCFAccordee pcfa = PerseusServiceLocator.getPCFAccordeeService().readForDemande(demande.getId());
            // TODO déterminer le montant de la retenue
            if (null != pcfa) {
                SimpleRetenueSearchModel retenueSearch = new SimpleRetenueSearchModel();
                retenueSearch.setForIdPcfAccordee(pcfa.getSimplePCFAccordee().getIdPCFAccordee());
                retenueSearch.setForCsTypeRetenue(CSTypeRetenue.IMPOT_SOURCE.getCodeSystem());
                retenueSearch = PerseusImplServiceLocator.getSimpleRetenueService().search(retenueSearch);

                SimpleRetenue retenue = null;
                for (JadeAbstractModel model : retenueSearch.getSearchResults()) {
                    retenue = (SimpleRetenue) model;
                }

                // Si il n'y a pas de pcfa le montant dû reste à 0
                if (null != retenue) {
                    int nbMois = JadeDateUtil.getNbMonthsBetween(demande.getSimpleDemande().getDateDebut(), date);
                    if (nbMois < 0) {
                        nbMois = 0;
                    }
                    montantDu = nbMois * Float.parseFloat(retenue.getMontantRetenuMensuel());
                }
            }

            // Voir ce qui a déjà été versé et restitué pour la période
            montantDejaVerse = PerseusServiceLocator.getDossierService().calculerMontantVerseImpotSource(
                    demande.getDossier(), demande.getSimpleDemande().getDateDebut().substring(3), date.substring(3));

        } catch (DossierException e) {
            throw new DemandeException("DossierException during calculerMontantVerseImpotSource : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException(
                    "Service not available during calculerMontantVerseImpotSource : " + e.toString(), e);
        } catch (PCFAccordeeException e) {
            throw new DemandeException("PCFAccordeeException during calculerMontantVerseImpotSource : " + e.toString(),
                    e);
        } catch (RetenueException e) {
            throw new DemandeException("RetenueException during calculerMontantVerseImpotSource : " + e.toString(), e);
        }

        if (0 == montantDu.intValue()) {
            return montantDejaVerse;
        } else {
            return montantDejaVerse - montantDu;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#calculerRetro(ch.globaz.perseus.business.models
     * .demande.Demande)
     */
    @Override
    public Float calculerRetro(Demande demande) throws JadePersistenceException, DemandeException {

        Float retro = new Float(0);
        try {
            String date = "01." + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

            retro = this.calculerRetro(demande, date);
        } catch (PaiementException e) {
            throw new DemandeException("PaiementException during calculerRetro : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available during calculerRetro : " + e.toString(), e);
        }

        return retro;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#calculerRetro(ch.globaz.perseus.business.models
     * .demande.Demande, java.lang.String)
     */
    @Override
    public Float calculerRetro(Demande demande, String date) throws JadePersistenceException, DemandeException {
        if (demande == null) {
            throw new DemandeException("Unable to calculate retro, the model passed is null (demande)");
        }

        // Si la date de fin de la demande est avant prendre la date de fin de la demande
        if (JadeDateUtil.isDateBefore(demande.getSimpleDemande().getDateFin(), date)) {
            date = demande.getSimpleDemande().getDateFin();
        }

        // Montants à définir le retro dû et ce qui a déjà été versé
        Float montantDu = new Float(0);
        Float montantDejaVerse = new Float(0);

        try {
            // Définir le montant dû
            PCFAccordee pcfa = PerseusServiceLocator.getPCFAccordeeService().readForDemande(demande.getId());
            // Si il n'y a pas de pcfa le montant dû reste à 0
            if (pcfa != null) {
                int nbMois = JadeDateUtil.getNbMonthsBetween(demande.getSimpleDemande().getDateDebut(), date);
                if (nbMois < 0) {
                    nbMois = 0;
                }
                montantDu = nbMois * Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant());
            }

            // Voir ce qui a déjà été versé et restitué pour la période
            montantDejaVerse = PerseusServiceLocator.getDossierService().calculerMontantVerse(demande.getDossier(),
                    demande.getSimpleDemande().getDateDebut().substring(3), date.substring(3));

        } catch (DossierException e) {
            throw new DemandeException("DossierException during calculerRetro : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available during calculerRetro : " + e.toString(), e);
        } catch (PCFAccordeeException e) {
            throw new DemandeException("PCFAccordeeException during calculerRetro : " + e.toString(), e);
        }

        return montantDu - montantDejaVerse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#calculerRetro(ch.globaz.perseus.business.models
     * .demande.Demande)
     */
    @Override
    public Float calculerRetroMesureCoaching(Demande demande) throws JadePersistenceException, DemandeException {

        Float retro = new Float(0);
        try {
            String date = "01." + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

            retro = this.calculerRetroMesureCoaching(demande, date);
        } catch (PaiementException e) {
            throw new DemandeException("PaiementException during calculerRetro : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available during calculerRetro : " + e.toString(), e);
        }

        return retro;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#calculerRetro(ch.globaz.perseus.business.models
     * .demande.Demande, java.lang.String)
     */
    @Override
    public Float calculerRetroMesureCoaching(Demande demande, String date) throws JadePersistenceException,
            DemandeException {
        if (demande == null) {
            throw new DemandeException("Unable to calculate retro mesure coaching, the model passed is null (demande)");
        }

        // Si la date de fin de la demande est avant prendre la date de fin de la demande
        if (JadeDateUtil.isDateBefore(demande.getSimpleDemande().getDateFin(), date)) {
            date = demande.getSimpleDemande().getDateFin();
        }

        // Montants à définir le retro dû et ce qui a déjà été versé
        Float montantDu = new Float(0);
        Float montantDejaVerse = new Float(0);

        try {
            // Définir le montant dû
            PCFAccordee pcfa = PerseusServiceLocator.getPCFAccordeeService().readForDemande(demande.getId());
            // Si il n'y a pas de pcfa le montant dû reste à 0
            if (pcfa != null) {
                int nbMois = JadeDateUtil.getNbMonthsBetween(demande.getSimpleDemande().getDateDebut(), date);
                if (nbMois < 0) {
                    nbMois = 0;
                }
                montantDu = nbMois * pcfa.getCalcul().getDonnee(OutputData.MESURE_COACHING);
            }

            // Voir ce qui a déjà été versé et restitué pour la période
            montantDejaVerse = PerseusServiceLocator.getDossierService().calculerMontantMesureCoaching(
                    demande.getDossier(), demande.getSimpleDemande().getDateDebut().substring(3), date.substring(3));

        } catch (DossierException e) {
            throw new DemandeException("DossierException during calculerRetro : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available during calculerRetro : " + e.toString(), e);
        } catch (PCFAccordeeException e) {
            throw new DemandeException("PCFAccordeeException during calculerRetro : " + e.toString(), e);
        }

        return montantDu - montantDejaVerse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#calculerRetroPourCreanciers(ch.globaz.perseus
     * .business.models.demande.Demande)
     */
    @Override
    public Float calculerRetroPourCreanciers(Demande demande) throws JadePersistenceException, DemandeException {
        Float retro = new Float(0);
        try {
            String date = "01." + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

            // ---> Correction après mise en prod, finalement le mois de la décision peut être réparti aux créanciers
            // // Si le paiement du mois en cours a déjà été fait, ne pas permettre de répartir le mois en cours
            // if (JadeDateUtil.isDateBefore(JadeDateUtil.getGlobazFormattedDate(new Date()), date)) {
            // date = JadeDateUtil.addMonths(date, -1);
            // }

            retro = this.calculerRetro(demande, date);
        } catch (PaiementException e) {
            throw new DemandeException("PaiementException during calculerRetro : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available during calculerRetro : " + e.toString(), e);
        }

        return retro;
    }

    @Override
    public boolean checkCalculable(Demande demande) throws DemandeException, JadePersistenceException {
        Boolean calculable = true;
        // Si la personne n'est pas dans le canton depuis plus de 3 ans
        // Si la personne est arrivée dans le canton le 20 décembre 2008, elle a droit au PC Familles
        // depuis le 1e décembre 2011. Cette comparaison doit ce faire entre la date d'arrivée dans le canton
        // et la date de début de la demande

        int anneeDansCanton = JadeDateUtil.getNbYearsBetween(demande.getSimpleDemande().getDateArrivee(), demande
                .getSimpleDemande().getDateDebut(), JadeDateUtil.YEAR_MONTH_COMPARISON);

        if (anneeDansCanton < IPFConstantes.MINIMUM_ANNEES_CANTON) {
            calculable = false;
        } else {
            // Si il n'y a pas d'enfants de moins de 16 ans dans la situation familiale
            try {
                EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
                enfantFamilleSearchModel.setForIdSituationFamiliale(demande.getSituationFamiliale().getId());
                enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(
                        enfantFamilleSearchModel);

                calculable = false;
                for (JadeAbstractModel model : enfantFamilleSearchModel.getSearchResults()) {
                    EnfantFamille enfantFamille = (EnfantFamille) model;
                    String dateNaissance = enfantFamille.getEnfant().getMembreFamille().getPersonneEtendue()
                            .getPersonne().getDateNaissance();
                    int ageEnfant = JadeDateUtil.getNbYearsBetween(dateNaissance, demande.getSimpleDemande()
                            .getDateDebut());
                    if (ageEnfant < IPFConstantes.AGE_16ANS) {
                        calculable = true;
                    }
                }

            } catch (SituationFamilleException e) {
                throw new DemandeException("SituationFamilleException during demande check : " + e.getMessage(), e);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new DemandeException("Service not available during demande check : " + e.getMessage(), e);
            }
        }

        return calculable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#copier(ch.globaz.perseus.business.models.demande
     * .Demande)
     */
    @Override
    public Demande copier(Demande demande) throws Exception {
        Demande newDemande = (Demande) JadePersistenceUtil.clone(demande);
        newDemande = copier(demande, newDemande);

        return newDemande;
    }

    /**
     * Check afin de savoir si on a un conjoint qui est aussi un requérant
     * 
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DossierException
     */
    @Override
    public boolean iSConjointDossier(Demande demande) throws DossierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (JadeStringUtil.isBlank(demande.getSituationFamiliale().getConjoint().getMembreFamille()
                .getPersonneEtendue().getId())) {
            return false;
        }

        DossierSearchModel searchModel = new DossierSearchModel();
        searchModel.setForIdTiers(demande.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue()
                .getId());
        searchModel = PerseusServiceLocator.getDossierService().search(searchModel);

        if (searchModel.getSize() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Copie utilisé par le traitement d'adaptation optimisé afin d'échaper au clone
     * 
     * @throws JadeCloneModelException
     * @throws JadePersistenceException
     * @throws DemandeException
     * 
     * @throws Exception
     */
    @Override
    public Demande copier(Demande demande, Demande newDemande) throws JadeCloneModelException,
            JadePersistenceException, DemandeException {
        newDemande.setDossier(demande.getDossier());

        try {
            // Copie de la situation familiale
            SituationFamiliale newSituationFamiliale = (SituationFamiliale) JadePersistenceUtil.clone(demande
                    .getSituationFamiliale());
            newSituationFamiliale.setRequerant(demande.getSituationFamiliale().getRequerant());
            newSituationFamiliale.setConjoint(demande.getSituationFamiliale().getConjoint());
            newSituationFamiliale = PerseusServiceLocator.getSituationFamilialeService().create(newSituationFamiliale);
            newDemande.setSituationFamiliale(newSituationFamiliale);
            // Insertion de la demande
            SimpleDemande simpleDemande = newDemande.getSimpleDemande();
            simpleDemande.setIdDossier(newDemande.getDossier().getId());
            simpleDemande.setIdSituationFamiliale(newSituationFamiliale.getId());
            simpleDemande.setCsEtatDemande(CSEtatDemande.ENREGISTRE.getCodeSystem());
            simpleDemande.setDateTimeDecisionValidation("");
            simpleDemande = PerseusImplServiceLocator.getSimpleDemandeService().create(simpleDemande);
            newDemande.setSimpleDemande(simpleDemande);

            // Copier les enfants de la demande
            EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
            enfantFamilleSearchModel.setForIdSituationFamiliale(demande.getSituationFamiliale().getId());
            enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(
                    enfantFamilleSearchModel);
            for (JadeAbstractModel model : enfantFamilleSearchModel.getSearchResults()) {
                EnfantFamille enfantFamille = (EnfantFamille) model;
                EnfantFamille newEnfantFamille = (EnfantFamille) JadePersistenceUtil.clone(enfantFamille);
                newEnfantFamille.getSimpleEnfantFamille().setIdSituationFamiliale(newSituationFamiliale.getId());
                newEnfantFamille.setEnfant(enfantFamille.getEnfant());

                PerseusImplServiceLocator.getEnfantFamilleService().create(newEnfantFamille);
            }

            // Copier les données financières de la demande
            // Revenus
            RevenuSearchModel revenuSearchModel = new RevenuSearchModel();
            revenuSearchModel.setForIdDemande(demande.getId());
            revenuSearchModel = PerseusServiceLocator.getRevenuService().search(revenuSearchModel);
            for (JadeAbstractModel model : revenuSearchModel.getSearchResults()) {
                Revenu revenu = (Revenu) model;
                Revenu newRevenu = (Revenu) JadePersistenceUtil.clone(revenu);
                newRevenu.setDemande(newDemande);
                newRevenu.setMembreFamille(revenu.getMembreFamille());
                PerseusServiceLocator.getRevenuService().create(newRevenu);
            }
            // Fortune
            FortuneSearchModel fortuneSearchModel = new FortuneSearchModel();
            fortuneSearchModel.setForIdDemande(demande.getId());
            fortuneSearchModel = PerseusServiceLocator.getFortuneService().search(fortuneSearchModel);
            for (JadeAbstractModel model : fortuneSearchModel.getSearchResults()) {
                Fortune fortune = (Fortune) model;
                Fortune newFortune = (Fortune) JadePersistenceUtil.clone(fortune);
                newFortune.setDemande(newDemande);
                newFortune.setMembreFamille(fortune.getMembreFamille());
                PerseusServiceLocator.getFortuneService().create(newFortune);
            }
            // Dettes
            DetteSearchModel detteSearchModel = new DetteSearchModel();
            detteSearchModel.setForIdDemande(demande.getId());
            detteSearchModel = PerseusServiceLocator.getDetteService().search(detteSearchModel);
            for (JadeAbstractModel model : detteSearchModel.getSearchResults()) {
                Dette dette = (Dette) model;
                Dette newDette = (Dette) JadePersistenceUtil.clone(dette);
                newDette.setDemande(newDemande);
                newDette.setMembreFamille(dette.getMembreFamille());
                PerseusServiceLocator.getDetteService().create(newDette);
            }
            // Dépenses reconnues
            DepenseReconnueSearchModel depenseReconnueSearchModel = new DepenseReconnueSearchModel();
            depenseReconnueSearchModel.setForIdDemande(demande.getId());
            depenseReconnueSearchModel = PerseusServiceLocator.getDepenseReconnueService().search(
                    depenseReconnueSearchModel);
            for (JadeAbstractModel model : depenseReconnueSearchModel.getSearchResults()) {
                DepenseReconnue depenseReconnue = (DepenseReconnue) model;
                DepenseReconnue newDepenseReconnue = (DepenseReconnue) JadePersistenceUtil.clone(depenseReconnue);
                newDepenseReconnue.setDemande(newDemande);
                newDepenseReconnue.setMembreFamille(depenseReconnue.getMembreFamille());
                PerseusServiceLocator.getDepenseReconnueService().create(newDepenseReconnue);
            }

        } catch (SituationFamilleException e) {
            throw new DemandeException("SituationFamilleException during demande copy : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available during demande copy : " + e.toString(), e);
        } catch (DonneesFinancieresException e) {
            throw new DemandeException("DonneesFinancieresException during demande copy : " + e.toString(), e);
        }
        return newDemande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.demande.DemandeService#count
     * (ch.globaz.perseus.business.models.demande.DemandeSearch)
     */
    @Override
    public int count(DemandeSearchModel search) throws DemandeException, JadePersistenceException {
        if (search == null) {
            throw new DemandeException("Unable to count demandes, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#create(ch.globaz.perseus.business.models.demande
     * .Demande)
     */
    @Override
    public Demande create(Demande demande) throws JadePersistenceException, DemandeException {
        if (demande == null) {
            throw new DemandeException("Unable to create demande, the given model is null!");
        }

        try {
            // Création du requerant
            Requerant requerant = new Requerant();
            requerant
                    .getMembreFamille()
                    .getSimpleMembreFamille()
                    .setIdTiers(
                            demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonne().getIdTiers());
            requerant = PerseusServiceLocator.getRequerantService().create(requerant);

            // Création de la situation familiale
            SituationFamiliale situationFamiliale = new SituationFamiliale();
            situationFamiliale.setRequerant(requerant);
            situationFamiliale = PerseusServiceLocator.getSituationFamilialeService().create(situationFamiliale);
            demande.setSituationFamiliale(situationFamiliale);

            setCalculable(demande);

            DemandeChecker.checkForCreate(demande);

            // Si NonEntreeEnMatiere pas coché alors on vide la liste
            if (!demande.getSimpleDemande().getNonEntreeEnMatiere()) {
                demande.getSimpleDemande().setDateListeNonEntreeEnMatiere("");
            }

            // Création de la demande
            SimpleDemande simpleDemande = demande.getSimpleDemande();
            simpleDemande.setIdDossier(demande.getDossier().getId());
            simpleDemande.setIdSituationFamiliale(situationFamiliale.getId());
            simpleDemande.setCsEtatDemande(CSEtatDemande.ENREGISTRE.getCodeSystem());

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            simpleDemande.setDateDemandeSaisie(sdf.format(new Date()));

            sdf = new SimpleDateFormat("hh:mm:ss");
            simpleDemande.setTimeDemandeSaisie(sdf.format(new Date()));

            simpleDemande = PerseusImplServiceLocator.getSimpleDemandeService().create(simpleDemande);
            demande.setSimpleDemande(simpleDemande);
            if (simpleDemande.getTypeDemande().equals(CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem())
                    && JadeStringUtil.isNull(simpleDemande.getDateFin())) {
                metAJourDateDeFinDerniereDemande(demande);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available - " + e.getMessage());
        } catch (SituationFamilleException e) {
            throw new DemandeException("SituationFamilleException during creating : " + e.getMessage(), e);
        }

        return demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#delete(ch.globaz.perseus.business.models.demande
     * .Demande)
     */
    @Override
    public Demande delete(Demande demande) throws JadePersistenceException, DemandeException {
        SimpleDemande simpleDemande = demande.getSimpleDemande();
        try {
            // Suppression de la simple demande
            simpleDemande = PerseusImplServiceLocator.getSimpleDemandeService().delete(simpleDemande);
            demande.setSimpleDemande(simpleDemande);

            // Si la demande a été supprimée
            if (simpleDemande.isNew()) {
                // Suppression de la situation familiale
                demande.setSituationFamiliale(PerseusServiceLocator.getSituationFamilialeService().delete(
                        demande.getSituationFamiliale()));
                // Suppression des données financières
                PerseusServiceLocator.getDonneeFinanciereService().deleteForDemande(demande.getId());
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available : " + e.getMessage());
        } catch (SituationFamilleException e) {
            throw new DemandeException("SituationFamilleException during demande deleting : " + e.getMessage());
        } catch (DonneesFinancieresException e) {
            throw new DemandeException("DonneeFinancieresException during demande deleting : " + e.getMessage());
        }

        return null;
    }

    private String derterminerNumeroOFSAvecDemandePrecedente(Demande demande, Demande demandePrecedente)
            throws JadePersistenceException {
        if (hasSixMoisEntreDate(demandePrecedente.getSimpleDemande().getDateFin(), demande.getSimpleDemande()
                .getDateDebut())) {
            return getNumeroOFSCalculee();
        } else {
            return getNumeroOFSDemandePrecedente(demandePrecedente);
        }
    }

    private String determinerNumeroOFS(Demande demande) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Demande demandePrecedente = PerseusServiceLocator.getDecisionService()
                .getDemandePrecedenteValideDecisionOCtroiPrecedanteForNumOFS(demande);

        if (null != demandePrecedente) {
            return derterminerNumeroOFSAvecDemandePrecedente(demande, demandePrecedente);
        } else {
            return getNumeroOFSCalculee();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#getDemandePrecedante(ch.globaz.perseus.business
     * .models.demande.Demande)
     */
    @Override
    public Demande getDemandePrecedante(Demande demande) throws DemandeException, JadePersistenceException {
        if (demande == null) {
            throw new DemandeException("Unable to find demande precedante, the model passed is null");
        }
        Demande demandePrecedante = null;
        try {

            // Retrouver la date à laquelle on va rechercher une demande active
            String dateFin = "";
            if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateFin())) {
                // Cas où la demande sera active après
                dateFin = JadeDateUtil.addDays(JadeDateUtil.addMonths("01."
                        + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt(), 1), -1);
            } else if (!JadeDateUtil.isDateMonthYearAfter(demande.getSimpleDemande().getDateFin().substring(3),
                    PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt())) {
                // Cas où la demande est purement retro active
                dateFin = demande.getSimpleDemande().getDateFin();
            } else {
                // cas normalement impossible puisque ca voudrait dire que la demande se ferme dans le future et se
                // n'est pas possible
                // mais on prend quand même le dernier paiement mensuel
                dateFin = JadeDateUtil.addDays(JadeDateUtil.addMonths("01."
                        + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt(), 1), -1);
            }

            DemandeSearchModel ds = new DemandeSearchModel();
            ds.setForIdDossier(demande.getSimpleDemande().getIdDossier());
            ds.setForNotIdDemande(demande.getId());
            ds.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
            ds.setForDateValable(dateFin);
            ds.setOrderBy(DemandeSearchModel.ORDER_BY_DATETIME_DECISION_DESC);
            ds = PerseusServiceLocator.getDemandeService().search(ds);
            // On peut considérer que la première est la dernière décision prise
            if (ds.getSize() > 0) {
                demandePrecedante = (Demande) ds.getSearchResults()[0];
            }

        } catch (Exception e) {
            throw new DemandeException("Exception during getDemandePrecedante : " + e.toString(), e);
        }

        return demandePrecedante;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.demande.DemandeService#getDerniereDemande(java.lang.String)
     */
    @Override
    public Demande getDerniereDemande(String idDossier) throws DemandeException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new DemandeException("Unable to getDerniereDemande, the idDossier passed is empty !");
        }

        try {
            Demande lastDemande = null;
            DemandeSearchModel demandeSearchModel = new DemandeSearchModel();
            demandeSearchModel.setForIdDossier(idDossier);
            demandeSearchModel.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
            demandeSearchModel = PerseusServiceLocator.getDemandeService().search(demandeSearchModel);
            // On regarde si il y'a une demande sans date de fin
            for (JadeAbstractModel model : demandeSearchModel.getSearchResults()) {
                Demande demande = (Demande) model;
                if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateFin())) {
                    // On est content c'est celle là
                    lastDemande = demande;
                }
            }

            // Si on a pas trouvé de demande sans date de fin, on reboucle pour trouver la demande avec la plus vieille
            // date
            // de fin
            if (lastDemande == null) {
                for (JadeAbstractModel model : demandeSearchModel.getSearchResults()) {
                    Demande demande = (Demande) model;
                    // Si on passe pour la première fois, la dernière demande est celle-ci (au cas il y'en a une seule)
                    if (lastDemande == null) {
                        lastDemande = demande;
                    } else {
                        // Si non regarder si la demande en cours est plus récente que la dernière demande
                        if (JadeDateUtil.isDateAfter(demande.getSimpleDemande().getDateFin(), lastDemande
                                .getSimpleDemande().getDateFin())) {
                            lastDemande = demande;
                        } else {
                            // Dans le cas où les dates de fin sont égales, on va prendre la demande avec la décision la
                            // plus récente
                            if (JadeDateUtil.areDatesEquals(demande.getSimpleDemande().getDateFin(), lastDemande
                                    .getSimpleDemande().getDateFin())) {
                                if (Double.parseDouble(demande.getSimpleDemande().getDateTimeDecisionValidation()) > Double
                                        .parseDouble(lastDemande.getSimpleDemande().getDateTimeDecisionValidation())) {
                                    lastDemande = demande;
                                }
                            }
                        }
                    }
                }
            }

            return lastDemande;
        } catch (Exception e) {
            throw new DemandeException("DemandeServiceImpl - erreur dans la methode getDerniereDemande :"
                    + e.toString(), e);

        }
    }

    @Override
    public List<Enfant> getListEnfants(Demande demande) throws JadePersistenceException, DemandeException {
        try {
            ArrayList<Enfant> listEnfants = new ArrayList<Enfant>();
            EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
            enfantFamilleSearchModel.setForIdSituationFamiliale(demande.getSituationFamiliale().getId());
            enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(
                    enfantFamilleSearchModel);

            for (JadeAbstractModel abstractModel : enfantFamilleSearchModel.getSearchResults()) {
                EnfantFamille enfantfamille = (EnfantFamille) abstractModel;
                listEnfants.add(enfantfamille.getEnfant());
            }

            return listEnfants;

        } catch (SituationFamilleException e) {
            throw new DemandeException("SituationFamilleException during getListEnfants : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available : " + e.toString(), e);
        }
    }

    @Override
    public String getNumeroOFSCalculee() throws JadePersistenceException {
        String cleDeCalcul = IPFConstantes.OFS_CLE_INCREMENTATION;
        // Recuperation de l'increment
        String increment = JadePersistenceManager.incIndentifiant(cleDeCalcul);
        return increment = JadeStringUtil.fillWithZeroes(increment, 8);

    }

    @Override
    public String getNumeroOFSCalculeeForDemande(Demande demande) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        String numeroOFS = null;
        if (null != demande) {
            numeroOFS = determinerNumeroOFS(demande);
        }

        return numeroOFS;

    }

    private String getNumeroOFSDemandePrecedente(Demande demandePrecedente) throws JadePersistenceException {
        String numeroOFSDemandePrecedente = demandePrecedente.getSimpleDemande().getNumeroOFS();

        if (JadeStringUtil.isEmpty(numeroOFSDemandePrecedente)) {
            numeroOFSDemandePrecedente = getNumeroOFSCalculee();
        }

        return numeroOFSDemandePrecedente;
    }

    @Override
    public Boolean hasCreanciers(String idDemande) throws JadePersistenceException, DemandeException {
        CreancierSearchModel creancierSearchModel = new CreancierSearchModel();
        creancierSearchModel.setForIdDemande(idDemande);
        Boolean has;
        try {
            has = PerseusServiceLocator.getCreancierService().count(creancierSearchModel) > 0;
        } catch (CreancierException e) {
            throw new DemandeException("CreancierException during hasCreanciers count : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available : " + e.toString(), e);
        }

        return has;
    }

    private boolean hasSixMoisEntreDate(String dateFinDemandePrecedente, String dateDebutDemandeActuelle) {
        boolean hasSixMoisEntreDate = false;

        int nbMois = JadeDateUtil.getNbMonthsBetween(dateFinDemandePrecedente, dateDebutDemandeActuelle);
        if (nbMois >= 6) {
            hasSixMoisEntreDate = true;
        }

        return hasSixMoisEntreDate;
    }

    @Override
    public String isCalculable(String idDemande) throws JadePersistenceException, DemandeException {
        Demande demande = read(idDemande);
        return demande.getSimpleDemande().getCalculable().toString();
    }

    private void metAJourDateDeFinDerniereDemande(Demande demande) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        SimpleDemandeSearchModel simpleDemandeSearchModel = new SimpleDemandeSearchModel();

        simpleDemandeSearchModel.setForDateFinCheck("");
        simpleDemandeSearchModel.setForIdDossier(demande.getSimpleDemande().getIdDossier());
        simpleDemandeSearchModel.setForNotIdDemande(demande.getId());
        simpleDemandeSearchModel = PerseusImplServiceLocator.getSimpleDemandeService().search(simpleDemandeSearchModel);

        for (JadeAbstractModel model : simpleDemandeSearchModel.getSearchResults()) {
            SimpleDemande simpleDemande = (SimpleDemande) model;
            if (JadeStringUtil.isBlank(simpleDemande.getDateFin())
                    && CSEtatDemande.VALIDE.getCodeSystem().equals(simpleDemande.getCsEtatDemande())) {
                // Update de la date de fin de la demande
                simpleDemande.setIdDossier(demande.getDossier().getId());

                PmtMensuelServiceImpl pmtMensuelServiceImpl = new PmtMensuelServiceImpl();
                String dateDeFin = "";

                try {
                    dateDeFin = pmtMensuelServiceImpl.getDateDernierPmt();

                    JACalendar CALENDAR = new JACalendarGregorian();
                    int mois = Integer.parseInt(dateDeFin.substring(0, 2));
                    int annee = Integer.parseInt(dateDeFin.substring(3, 7));
                    int joursDansLeMois = CALENDAR.daysInMonth(mois, annee);

                    dateDeFin = String.valueOf(joursDansLeMois) + "." + dateDeFin;

                } catch (PmtMensuelException e) {
                    JadeLogger.error(this, e);
                }

                simpleDemande.setDateFin(dateDeFin);
                simpleDemande = PerseusImplServiceLocator.getSimpleDemandeService().update(simpleDemande);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.demande.DemandeService#read(java.lang.String)
     */
    @Override
    public Demande read(String idDemande) throws JadePersistenceException, DemandeException {
        if (JadeStringUtil.isEmpty(idDemande)) {
            throw new DemandeException("Unable to read a demande, the id passed is null!");
        }
        Demande demande = new Demande();
        demande.setId(idDemande);
        return (Demande) JadePersistenceManager.read(demande);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.DemandeService#search(ch.globaz.perseus.business.models.demande
     * .DemandeSearchModel)
     */
    @Override
    public DemandeSearchModel search(DemandeSearchModel searchModel) throws JadePersistenceException, DemandeException {
        if (searchModel == null) {
            throw new DemandeException("Unable to search demandes, the search model passed is null!");
        }
        return (DemandeSearchModel) JadePersistenceManager.search(searchModel);
    }

    private void setCalculable(Demande demande) throws DemandeException, JadePersistenceException {
        demande.getSimpleDemande().setCalculable(checkCalculable(demande));
    }

    @Override
    public Demande update(Demande demande) throws JadePersistenceException, DemandeException, SituationFamilleException {
        return this.update(demande, false, false);
    }

    @Override
    public Demande update(Demande demande, Boolean renonciation, Boolean checkEnfant) throws JadePersistenceException,
            DemandeException, SituationFamilleException {
        if ((demande == null) || demande.isNew()) {
            throw new DemandeException("Unable to update demande, the model passed is null or new!");
        }
        try {

            if (checkEnfant) {
                List<EnfantFamille> enfants = PerseusServiceLocator.getEnfantFamilleService().findEnfantByIdSF(
                        demande.getSituationFamiliale().getId());

                for (EnfantFamille enfant : enfants) {
                    // check afin de mettre le JadeThread en warn ou erreur
                    try {
                        SimpleEnfantFamilleChecker.checkInegrityForCopie(enfant.getSimpleEnfantFamille(), demande);
                        // PerseusServiceLocator.getEnfantFamilleService().checkForAjaxAdd(enfant);
                    } catch (Exception e) {
                        throw new SituationFamilleException(e.getMessage());
                    }
                }

                if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
                    EnfantFamilleAddCheckMessage msg = EnfantFamilleAddCheckMessage.warn();
                }
            }

            DemandeChecker.checkForUpdate(demande);
            Demande oldDemande = PerseusServiceLocator.getDemandeService().read(demande.getId());
            // Si la demande est validée et qu'une date de fin a été ajoutée
            if (CSEtatDemande.VALIDE.getCodeSystem().equals(oldDemande.getSimpleDemande().getCsEtatDemande())) {
                // Si la demande a déjà une date de fin, elle ne peut pas être modifiée, à moins que ce soit une
                // renonciation
                if (!JadeStringUtil.isEmpty(oldDemande.getSimpleDemande().getDateFin()) && !renonciation) {
                    JadeThread.logError(this.getClass().getName(), "perseus.demande.update.datefin.already");
                }
                // Contrôler que uniquement la date de fin a été modifiée
                String dateFin = demande.getSimpleDemande().getDateFin();
                demande.getSimpleDemande().setDateFin(oldDemande.getSimpleDemande().getDateFin());
                // Si le statut de séjour de la demande est à blanc ou zero, je set la même chose dans old demande pour
                // la comparaison
                if (JadeStringUtil.isBlankOrZero(demande.getSimpleDemande().getStatutSejour())) {
                    oldDemande.getSimpleDemande().setStatutSejour(demande.getSimpleDemande().getStatutSejour());
                }
                if (!demande.getSimpleDemande().areEquals(oldDemande.getSimpleDemande())) {
                    JadeThread.logError(this.getClass().getName(), "perseus.demande.update.valide.seuldatefin");
                }
                demande.getSimpleDemande().setDateFin(dateFin);

                // Contrôler que la date de fin ne soit pas avant le dernier paiement mensuel
                // Sauf si c'est une renonciation dans quel cas il s'agissait avant d'un projet donc rien n'aurait été
                // payé
                String dateDernierPaiement = PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt();
                if (!JadeStringUtil.isEmpty(dateFin)
                        && JadeDateUtil.isDateMonthYearBefore(dateFin.substring(3), dateDernierPaiement)
                        && !renonciation) {
                    String[] param = new String[1];
                    param[0] = dateDernierPaiement;
                    JadeThread.logError(this.getClass().getName(),
                            "perseus.demande.update.valide.paiementdejaeffectue", param);
                }
                // Contrôler que la date de fin ne soit pas dans le future
                if (!JadeStringUtil.isEmpty(dateFin)
                        && JadeDateUtil.isDateMonthYearAfter(dateFin.substring(3), dateDernierPaiement)
                        && !renonciation) {
                    String[] param = new String[1];
                    param[0] = dateDernierPaiement;
                    JadeThread.logError(this.getClass().getName(), "perseus.demande.update.valide.future", param);
                }

                // Si tout s'est bien passé et que la date de fin a été modifiée -> Modifié la PCFA
                if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)
                        && JadeStringUtil.isEmpty(oldDemande.getSimpleDemande().getDateFin())
                        && !JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateFin())
                        && !CSTypeDemande.PC_AVS_AI.getCodeSystem().equals(demande.getSimpleDemande().getTypeDemande())) {
                    // Retrouver la pcfAccordee
                    PCFAccordee pcfa = PerseusServiceLocator.getPCFAccordeeService().readForDemande(demande.getId());
                    if (pcfa == null) {
                        JadeThread.logError(this.getClass().getName(), "perseus.demande.update.valide.pcfanotfound");
                    }
                    // Si il y'a déjà une date dans la diminiution de la pcfa c'est très bizarre
                    if (!JadeStringUtil.isEmpty(pcfa.getSimplePCFAccordee().getDateDiminution())) {
                        JadeThread
                                .logError(
                                        this.getClass().getName(),
                                        "Il y'a un problème pour fermer la PCFAccordée, ce cas ne devrait jamais arrivé, merci de contacter Globaz avec le message suivant : dateDiminution déjà définie à la fermeture d'une demande");
                    }
                    pcfa.getSimplePCFAccordee().setDateDiminution(demande.getSimpleDemande().getDateFin().substring(3));
                    pcfa = PerseusServiceLocator.getPCFAccordeeService().updateWithoutCalcul(pcfa);
                }

            } else {
                if (demande.getSimpleDemande().getRefusForce()) {
                    demande.getSimpleDemande().setNonEntreeEnMatiere(false);
                }

                if (demande.getSimpleDemande().getNonEntreeEnMatiere()) {
                    demande.getSimpleDemande().setRefusForce(false);
                }

                if (!demande.getSimpleDemande().getNonEntreeEnMatiere()) {
                    demande.getSimpleDemande().setDateListeNonEntreeEnMatiere("");
                }

                setCalculable(demande);
            }

            demande.setSimpleDemande(PerseusImplServiceLocator.getSimpleDemandeService().update(
                    demande.getSimpleDemande()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not avaiable : " + e.toString(), e);
        } catch (PaiementException e) {
            throw new DemandeException("PaiementException in Demande update : " + e.toString(), e);
        } catch (PCFAccordeeException e) {
            throw new DemandeException("PCFAccordeeException in Demande update : " + e.toString(), e);
        }
        return demande;
    }

    /**
     * Permet de cleaner une demande lors de modifications. Cette méthode est aussi utilisée pour annulé une demande
     * validée
     * 
     * @param demande
     * @return Demande
     * @throws JadePersistenceException
     * @throws DemandeException
     * @throws SituationFamilleException
     */
    @Override
    public Demande updateAndClean(Demande demande, Boolean checkEnfant) throws JadePersistenceException,
            DemandeException, SituationFamilleException {
        try {
            // On passe la demande en état enregistré
            demande.getSimpleDemande().setCsEtatDemande(CSEtatDemande.ENREGISTRE.getCodeSystem());

            demande = this.update(demande, false, checkEnfant);

            suppressionDesDecisions(demande);
            suppressionDesPCFAccordee(demande);
            suppressionDesCreanciers(demande);

        } catch (DecisionException e) {
            throw new DemandeException("DecisionException in DemandeService#updateAndClean : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available in DemandeService#updateAndClean : " + e.toString(), e);
        } catch (PCFAccordeeException e) {
            throw new DemandeException("PCFAccordeeException in DemandeService#updateAndClean : " + e.toString(), e);
        } catch (CreancierException e) {
            throw new DemandeException("CreancierException in DemandeService#updateAndClean : " + e.toString(), e);
        }

        return demande;
    }

}
