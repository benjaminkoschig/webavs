package ch.globaz.pegasus.businessimpl.services.models.droit;

import ch.globaz.pegasus.business.constantes.*;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.PrimeAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.SubsideAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.habitat.SejourMoisPartielHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.*;
import ch.globaz.pegasus.business.models.assurancemaladie.*;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;
import ch.globaz.pegasus.business.models.creancier.*;
import ch.globaz.pegasus.business.models.habitat.*;
import ch.globaz.pegasus.business.models.home.SimpleHomeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenue;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenueSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.*;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCUserHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.hera.business.exceptions.HeraException;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.services.HeraServiceLocator;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementFortuneException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementRevenuException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AssuranceRenteViagereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AutreFortuneMobiliereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.BetailException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.MarchandisesStockException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.NumeraireException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.PretEnversTiersException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.VehiculeException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AssuranceVieException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AutresDettesProuveesException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierNonHabitableException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CapitalLPPException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CompteBancaireCCPException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.TitreException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AllocationImpotentException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreApiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IjApgException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.decision.DecisionSuppressionSearch;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneAutoSearch;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneSearch;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuAutoSearch;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuSearch;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnellesSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciereSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroitSearch;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.pegasus.business.models.droit.VersionDroitSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagereSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliereSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Betail;
import ch.globaz.pegasus.business.models.fortuneparticuliere.BetailSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliereSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStock;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStockSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Numeraire;
import ch.globaz.pegasus.business.models.fortuneparticuliere.NumeraireSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiers;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiersSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule;
import ch.globaz.pegasus.business.models.fortuneparticuliere.VehiculeSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVie;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVieSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuvees;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuveesSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitableSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPPSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCPSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.FortuneUsuelleSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.Titre;
import ch.globaz.pegasus.business.models.fortuneusuelle.TitreSearch;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotentSearch;
import ch.globaz.pegasus.business.models.renteijapi.AutreApi;
import ch.globaz.pegasus.business.models.renteijapi.AutreApiSearch;
import ch.globaz.pegasus.business.models.renteijapi.AutreRente;
import ch.globaz.pegasus.business.models.renteijapi.AutreRenteSearch;
import ch.globaz.pegasus.business.models.renteijapi.IjApg;
import ch.globaz.pegasus.business.models.renteijapi.IjApgSearch;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAi;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAiSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAiSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;
import ch.globaz.pegasus.business.services.models.droit.DroitService;
import ch.globaz.pegasus.business.services.synchronisation.MembreFamilleToSync;
import ch.globaz.pegasus.business.services.synchronisation.MembresFamillesToSynchronise;
import ch.globaz.pegasus.business.services.synchronisation.SynchronisationMembreFamille;
import ch.globaz.pegasus.businessimpl.checkers.droit.DroitChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class DroitServiceImpl extends PegasusAbstractServiceImpl implements DroitService {

    /**
     * convertit le code systeme LIEN AVEC REQUERANT PC (PCLIEREQ)au code systeme ROLE FAMILLE PC (PCROLEFAM)
     *
     * @param csLienFamille code systeme du lien avec le requerant
     * @param isRequerant   doit être true s'il s'agit actuellement du requerant
     * @return code systeme du role en famille, ou null si le membre n'est pas dans la famille proche (conjoint ou
     * enfant)
     */
    public final static String convertCsRoleFamillePC(String csLienFamille, boolean isRequerant) {

        Map<String, String> mapping = new HashMap<String, String>();
        // requerant ou conjoint
        mapping.put(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT, "1");
        mapping.put(ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT, "1");
        // enfants
        mapping.put(ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT, IPCDroits.CS_ROLE_FAMILLE_ENFANT);

        String result = mapping.get(csLienFamille);
        if (result.equals("1")) {
            result = isRequerant ? IPCDroits.CS_ROLE_FAMILLE_REQUERANT : IPCDroits.CS_ROLE_FAMILLE_CONJOINT;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.droit.DroitService#calculerDroit
     * (ch.globaz.pegasus.business.models.droit.Droit)
     */
    @Override
    public Droit calculerDroit(Droit droit) throws JadePersistenceException, JadeApplicationException,
            PegasusException, SecurityException, NoSuchMethodException {
        return this.calculerDroit(droit, true, null);
    }

    @Override
    public String[] calculDroitPlageCalcul(Droit droit, boolean retroactif) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException, CalculException, JadePersistenceException, DemandeException {
        return PegasusImplServiceLocator.getCalculDroitService().calculDroitPlageCalcul(droit, retroactif);
    }

    private Droit calculerDroit(Droit droit, boolean isRetroactif, List<String> dfForVersion)
            throws JadePersistenceException, JadeApplicationException, PegasusException, SecurityException,
            NoSuchMethodException {
        try {

            // TODO vu que le clear fait déjà un update droit, le faire ici aussi est probablement inutile
            // met à jour le droit pour verifier une possible modification
            // concurrante
            updateDroit(droit);

            // met le droit en état Au Calcul et vide tous les résultats existant
            try {
                PegasusImplServiceLocator.getCalculDroitService().reinitialiseDroit(droit);
            } catch (Exception e) {
                throw new CalculException("An error happened during the initialization of the calcul", e);
            }

            // appelle le service de calcul
            droit = PegasusImplServiceLocator.getCalculDroitService().calculDroit(droit, isRetroactif, dfForVersion);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return droit;
    }

    @Override
    public Droit calculerDroit(String idVersionDroit) throws PegasusException, HeraException, JadePersistenceException,
            JadeApplicationException, SecurityException, NoSuchMethodException {
        return this.calculerDroit(idVersionDroit, true, null);
    }

    @Override
    public Droit calculerDroit(String idVersionDroit, boolean isRetroactif, List<String> dfForVersion)
            throws PegasusException, HeraException, JadePersistenceException, JadeApplicationException,
            SecurityException, NoSuchMethodException {

        if (idVersionDroit == null) {
            throw new DroitException("Unable to update versionDroit, the model passed is null!");
        }

        Droit droit = readDroitFromVersion(idVersionDroit);

        if (droit.getSimpleVersionDroit().getCsMotif().equals(IPCDroits.CS_MOTIF_DROIT_ADAPTATION)) {
            JadeThread.logError(this.getClass().getName(), "pegasus.calcul.interdit.adaptation");
        }

        if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            droit = this.calculerDroit(droit, isRetroactif, dfForVersion);
        }

        return droit;
    }

    /**
     * Copie seulement les donneés fiancière et les donneés personnelles
     *
     * @param newDroit
     * @throws DonneesPersonnellesException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DroitException
     */
    private void copyOldDroitInTheNewDroit(Droit newDroit) throws DonneesPersonnellesException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DroitException {
        try {

            DroitMembreFamilleSearch newdroitMembreFamilleSearch = new DroitMembreFamilleSearch();
            newdroitMembreFamilleSearch.setForIdDroit(newDroit.getSimpleDroit().getIdDroit());
            try {

                newdroitMembreFamilleSearch = PegasusImplServiceLocator.getDroitMembreFamilleService().search(
                        newdroitMembreFamilleSearch);
            } catch (DroitException e) {
                throw new DonneeFinanciereException("Unable to find the droitMembreFamille", e);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new DonneeFinanciereException("Service not available - " + e.getMessage());
            } catch (JadePersistenceException e) {
                throw new DonneeFinanciereException("Unable to find the droitMembreFamille", e);
            }
            Droit oldDroit = findOldDroit(newDroit);

            if (newDroit.getSimpleVersionDroit().getNoVersion().equals("1") && (oldDroit != null)) {
                PegasusImplServiceLocator.getDonneeFinanciereHeaderService().copyDonneeFinanciere(newDroit, oldDroit,
                        newdroitMembreFamilleSearch);
                PegasusImplServiceLocator.getDonneesPersonnellesService().copyAllDonneesPersonelleByDroit(newDroit,
                        oldDroit, newdroitMembreFamilleSearch);
            }
        } catch (DonneeFinanciereException e) {
            throw new DroitException("Unable to copy the donneeFianciere", e);
        }
    }

    @Override
    public Droit corrigerCreateVersionDroit(Droit droit, String dateAnnonce, String csMotif) throws DroitException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        DroitChecker.checkForCorriger(droit);
        Droit newDroit = new Droit();

        newDroit.setDemande(droit.getDemande());
        newDroit.setSimpleDroit(droit.getSimpleDroit());

        SimpleVersionDroit newVersionDroit = newDroit.getSimpleVersionDroit();
        newVersionDroit.setIdDroit(droit.getId());

        // le no de version vaut le numero de la version du droit courant + 1
        newVersionDroit
                .setNoVersion(String.valueOf(Integer.parseInt(droit.getSimpleVersionDroit().getNoVersion()) + 1));
        newVersionDroit.setDateAnnonce(dateAnnonce);
        newVersionDroit.setCsMotif(csMotif);
        newVersionDroit.setIdDecision("0");
        newVersionDroit.setCsEtatDroit(IPCDroits.CS_ENREGISTRE);

        newDroit.setSimpleVersionDroit(PegasusImplServiceLocator.getSimpleVersionDroitService().create(newVersionDroit));

        return newDroit;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.droit.DroitService#corrigerDroit
     * (ch.globaz.pegasus.business.models.droit.Droit)
     */
    @Override
    public Droit corrigerDroit(Droit droit, String dateAnnonce, String csMotif) throws DroitException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {

        DroitChecker.checkForCorriger(droit);

        SimpleVersionDroit newVersionDroit = new SimpleVersionDroit();
        newVersionDroit.setIdDroit(droit.getId());

        // le no de version vaut le numero de la version du droit courant + 1
        newVersionDroit
                .setNoVersion(String.valueOf(Integer.parseInt(droit.getSimpleVersionDroit().getNoVersion()) + 1));
        newVersionDroit.setDateAnnonce(dateAnnonce);
        newVersionDroit.setCsMotif(csMotif);

        newVersionDroit.setCsEtatDroit(IPCDroits.CS_ENREGISTRE);

        droit.setSimpleVersionDroit(newVersionDroit);

        droit.setSimpleVersionDroit(PegasusImplServiceLocator.getSimpleVersionDroitService().create(newVersionDroit));

        return droit;
    }

    @Override
    public Droit corrigerDroitEnCasDeDeces(Droit droit, String dateAnnonce, String csMotif, String dateSuppression,
                                           String dateDecision, String currentUserId, boolean comptabilisationAuto, String mailAdressCompta)
            throws DroitException, JadePersistenceException, JadeApplicationServiceNotAvailableException {

        BSession session = BSessionUtil.getSessionFromThreadContext();

        // vérifier les paramètres d'entrées
        Checkers.checkNotNull(droit, "droit");

        if (!IPCDroits.CS_MOTIF_DROIT_DECES.equals(csMotif)) {
            throw new DroitException("this service should be used only for motif décès ");
        }

        if (JadeStringUtil.isBlankOrZero(dateAnnonce)) {
            throw new IllegalArgumentException(session.getLabel("DROIT_VALIDE_DATE_ANNONCE"));
        }

        if (JadeStringUtil.isBlankOrZero(dateSuppression)) {
            throw new IllegalArgumentException(session.getLabel("DROIT_VALIDE_DATE_SUPPRESSION"));
        }

        if (JadeStringUtil.isBlankOrZero(dateDecision)) {
            throw new IllegalArgumentException(session.getLabel("DROIT_VALIDE_DATE_DECISION"));
        }

        if (JadeStringUtil.isBlankOrZero(currentUserId)) {
            throw new IllegalArgumentException(session.getLabel("DROIT_VALIDE_UTILISATEUR"));
        }

        // vérification du droit
        DroitChecker.checkForCorriger(droit);

        // création de la nouvelle version du droit
        SimpleVersionDroit newVersionDroit = new SimpleVersionDroit();
        newVersionDroit.setIdDroit(droit.getId());
        newVersionDroit
                .setNoVersion(String.valueOf(Integer.parseInt(droit.getSimpleVersionDroit().getNoVersion()) + 1));
        newVersionDroit.setDateAnnonce(dateAnnonce);
        newVersionDroit.setCsMotif(csMotif);
        newVersionDroit.setCsEtatDroit(IPCDroits.CS_ENREGISTRE);
        droit.setSimpleVersionDroit(newVersionDroit);
        droit.setSimpleVersionDroit(PegasusImplServiceLocator.getSimpleVersionDroitService().create(newVersionDroit));

        // création de la décision de suppression
        DecisionSuppression decisionSuppression = new DecisionSuppression();
        decisionSuppression.getSimpleDecisionSuppression().setIdDecisionSuppression(droit.getId());
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setDateDecision(dateDecision);
        decisionSuppression.getSimpleDecisionSuppression().setCsMotif(IPCDecision.CS_MOTIF_SUPPR_DECES);
        decisionSuppression.getSimpleDecisionSuppression().setDateSuppression(dateSuppression);
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setPreparationPar(currentUserId);
        decisionSuppression.setVersionDroit(PegasusServiceLocator.getDroitService().readVersionDroit(
                droit.getSimpleVersionDroit().getIdVersionDroit()));
        decisionSuppression.getSimpleDecisionSuppression().setIsRestitution(false);
        try {
            decisionSuppression = PegasusServiceLocator.getDecisionSuppressionService().create(decisionSuppression);
        } catch (Exception e) {
            throw new DroitException("unable to create decision de suppression, an error occurred while creating", e);
        }

        // validation de la décision de suppression
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setValidationPar(currentUserId);
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                .setDateValidation(JadeDateUtil.getGlobazFormattedDate(new Date()));
        try {
            PegasusServiceLocator.getValidationDecisionService().validerDecisionSuppression(decisionSuppression,
                    comptabilisationAuto, mailAdressCompta);
        } catch (Exception e) {
            throw new DroitException("unable to valid decision de suppression an error occured while validating", e);
        }

        return droit;
    }

    @Override
    public Droit corrigerDroitAnnulation(Droit droit, String dateAnnonce, String dateSuppression, String dateDecision,
                                         String currentUserId, boolean comptabilisationAuto, String mailAdressCompta) throws DroitException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {

        BSession session = BSessionUtil.getSessionFromThreadContext();

        // vérifier les paramètres d'entrées
        Checkers.checkNotNull(droit, "droit");

        if (JadeStringUtil.isBlankOrZero(currentUserId)) {
            throw new IllegalArgumentException(session.getLabel("DROIT_VALIDE_UTILISATEUR"));
        }

        // vérification du droit
        DroitChecker.checkForCorriger(droit);

        // création de la nouvelle version du droit
        SimpleVersionDroit newVersionDroit = new SimpleVersionDroit();
        newVersionDroit.setIdDroit(droit.getId());
        newVersionDroit
                .setNoVersion(String.valueOf(Integer.parseInt(droit.getSimpleVersionDroit().getNoVersion()) + 1));
        newVersionDroit.setDateAnnonce(dateAnnonce);
        newVersionDroit.setCsMotif(IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES);
        newVersionDroit.setCsEtatDroit(IPCDroits.CS_ANNULE);
        droit.setSimpleVersionDroit(PegasusImplServiceLocator.getSimpleVersionDroitService().create(newVersionDroit));

        // création de la décision de suppression
        DecisionSuppression decisionSuppression = new DecisionSuppression();
        decisionSuppression.getSimpleDecisionSuppression().setIdDecisionSuppression(droit.getId());
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setDateDecision(dateDecision);
        decisionSuppression.getSimpleDecisionSuppression().setCsMotif(IPCDecision.CS_MOTIF_SUPP_TEXTE_LIBRE);
        decisionSuppression.getSimpleDecisionSuppression().setDateSuppression(dateSuppression);
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setPreparationPar(currentUserId);
        decisionSuppression.setVersionDroit(PegasusServiceLocator.getDroitService().readVersionDroit(
                droit.getSimpleVersionDroit().getIdVersionDroit()));
        decisionSuppression.getSimpleDecisionSuppression().setIsRestitution(false);
        try {
            decisionSuppression = PegasusServiceLocator.getDecisionSuppressionService().create(decisionSuppression,
                    false);
        } catch (Exception e) {
            throw new DroitException("unable to create decision de suppression, an error occurred while creating", e);
        }

        // validation de la décision de suppression
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setValidationPar(currentUserId);
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                .setDateValidation(JadeDateUtil.getGlobazFormattedDate(new Date()));
        try {
            PegasusServiceLocator.getValidationDecisionService().validerDecisionSuppression(decisionSuppression,
                    comptabilisationAuto, mailAdressCompta, true);
        } catch (Exception e) {
            throw new DroitException("unable to valid decision de suppression an error occured while validating", e);
        }

        return droit;
    }

    @Override
    public void retourArriereAnnulation(Droit droit) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DonneeFinanciereException,
            DroitException {
        if (IPCDroits.CS_ANNULE.equals(droit.getSimpleVersionDroit().getCsEtatDroit()) || hasDejaDateReduction(droit)) {
            PegasusServiceLocator.getDecisionService().devalideDecisions(droit.getId(),
                    droit.getSimpleVersionDroit().getId(), droit.getSimpleVersionDroit().getNoVersion(), true);
            DecisionSuppressionSearch search = new DecisionSuppressionSearch();
            search.setForIdVersionDroit(droit.getSimpleVersionDroit().getId());
            PegasusImplServiceLocator.getDecisionSuppressionService().delete(search);
            droit = PegasusServiceLocator.getDroitService().supprimerVersionDroitAnnule(droit);
        }
    }

    private boolean hasDejaDateReduction(Droit droit) {
        return !JadeStringUtil.isBlankOrZero(droit.getDemande().getSimpleDemande().getDateFinInitial());
    }

    @Override
    public Droit corrigerDroitDateReduction(Droit droit, Demande demande, String dateAnnonce, String dateSuppression,
                                            String dateDecision, String currentUserId, boolean comptabilisationAuto, String mailAdressCompta)
            throws DroitException, JadePersistenceException, JadeApplicationServiceNotAvailableException,
            DecisionException, DonneeFinanciereException {
        BSession session = BSessionUtil.getSessionFromThreadContext();

        // vérifier les paramètres d'entrées
        Checkers.checkNotNull(droit, "droit");

        if (JadeStringUtil.isBlankOrZero(currentUserId)) {
            throw new IllegalArgumentException(session.getLabel("DROIT_VALIDE_UTILISATEUR"));
        }

        //
        if (JadeStringUtil.isBlankOrZero(dateSuppression) && hasDejaDateReduction(droit)) {
            dateSuppression = droit.getDemande().getSimpleDemande().getDateFinInitial();
        }

        // il y avait déjà une date de réduction, il faut annuler les restitutions et décisions précédentes
        if (hasDejaDateReduction(droit)) {
            retourArriereAnnulation(droit);
            if (dateSuppression.equals(droit.getDemande().getSimpleDemande().getDateFinInitial())) {
                return droit;
            }
            // mise à jour du droit (numéro de version, etc) suite aux suppressions précédentes
            droit = PegasusServiceLocator.getDroitService().getCurrentVersionDroit(droit.getId());
        }

        droit.setDemande(demande);

        // vérification du droit
        DroitChecker.checkForCorriger(droit);

        // création de la nouvelle version du droit
        SimpleVersionDroit newVersionDroit = new SimpleVersionDroit();
        newVersionDroit.setIdDroit(droit.getId());
        newVersionDroit
                .setNoVersion(String.valueOf(Integer.parseInt(droit.getSimpleVersionDroit().getNoVersion()) + 1));
        newVersionDroit.setDateAnnonce(dateAnnonce);
        newVersionDroit.setCsMotif(IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES);
        newVersionDroit.setCsEtatDroit(IPCDroits.CS_VALIDE);
        droit.setSimpleVersionDroit(PegasusImplServiceLocator.getSimpleVersionDroitService().create(newVersionDroit));

        // création de la décision de suppression
        DecisionSuppression decisionSuppression = new DecisionSuppression();
        decisionSuppression.getSimpleDecisionSuppression().setIdDecisionSuppression(droit.getId());
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setDateDecision(dateDecision);
        decisionSuppression.getSimpleDecisionSuppression().setCsMotif(IPCDecision.CS_MOTIF_SUPP_TEXTE_LIBRE);
        decisionSuppression.getSimpleDecisionSuppression().setDateSuppression(dateSuppression);
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setPreparationPar(currentUserId);
        decisionSuppression.setVersionDroit(PegasusServiceLocator.getDroitService().readVersionDroit(
                droit.getSimpleVersionDroit().getIdVersionDroit()));
        decisionSuppression.getVersionDroit().setDemande(droit.getDemande());
        decisionSuppression.getSimpleDecisionSuppression().setIsRestitution(false);
        try {
            decisionSuppression = PegasusServiceLocator.getDecisionSuppressionService().create(decisionSuppression,
                    false);
        } catch (Exception e) {
            throw new DroitException("unable to create decision de suppression, an error occurred while creating", e);
        }

        // validation de la décision de suppression
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setValidationPar(currentUserId);
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                .setDateValidation(JadeDateUtil.getGlobazFormattedDate(new Date()));
        try {
            PegasusServiceLocator.getValidationDecisionService().validerDecisionSuppression(decisionSuppression,
                    comptabilisationAuto, mailAdressCompta, false);
        } catch (Exception e) {
            throw new DroitException("unable to valid decision de suppression an error occured while validating", e);
        }

        return droit;
    }

    @Override
    public int count(DroitSearch search) throws DroitException, JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to count dossiers, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public AllocationImpotent createAllocationImpotent(ModificateurDroitDonneeFinanciere droit,
                                                       DroitMembreFamille droitMembreFamille, AllocationImpotent newAllocationImpotent) throws DroitException,
            JadePersistenceException, AllocationImpotentException, DonneeFinanciereException {
        if (droit.isNew()) {
            throw new DroitException("Unable to create Allocation impotent, the droit is new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            PegasusImplServiceLocator.getRenteIjApiService().createAllocationImpotent(droit.getSimpleVersionDroit(),
                    droitMembreFamille, newAllocationImpotent);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.toString());
        }

        /*
         * newAllocationImpotent.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
         * IPCDroits.CS_ALLOCATIONS_IMPOTENT); this.setDonneeFinanciereHeaderForCreation(droit, droitMembreFamille,
         * newAllocationImpotent.getSimpleDonneeFinanciereHeader()); try { newAllocationImpotent =
         * PegasusImplServiceLocator.getAllocationImpotentService().create( newAllocationImpotent); } catch
         * (JadeApplicationServiceNotAvailableException e) { throw new DroitException("Service not available - " +
         * e.getMessage()); }
         */
        return newAllocationImpotent;
    }

    @Override
    public AllocationsFamiliales createAllocationsFamiliales(ModificateurDroitDonneeFinanciere droit,
                                                             DroitMembreFamille instanceDroitMembreFamille, AllocationsFamiliales allocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create allocationsFamiliales, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            allocationsFamiliales = PegasusImplServiceLocator.getRevenusDepensesService().createAllocationsFamiliales(
                    droit.getSimpleVersionDroit(), instanceDroitMembreFamille, allocationsFamiliales);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return allocationsFamiliales;
    }

    @Override
    public void createAndCloseAllocationImpotent(Droit droit, AllocationImpotent newApi, AllocationImpotent oldApi,
                                                 boolean forClosePeriode) throws DroitException, DonneeFinanciereException, JadePersistenceException {
        if (oldApi == null) {
            throw new DroitException("Unable to create RenteAvsAi, the oldApi is null");
        }
        if (newApi == null) {
            throw new DroitException("Unable to create RenteAvsAi, the newApi model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create RenteAvsAi, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newApi = (AllocationImpotent) this
                    .updateAncienneDonneeFinanciere(newApi, oldApi, droit.getSimpleVersionDroit(),
                            AllocationImpotent.class, PegasusImplServiceLocator.getAllocationImpotentService(),
                            AllocationImpotent.class, forClosePeriode);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }

    }

    @Override
    public AllocationImpotent createAndCloseAllocationImpotent(ModificateurDroitDonneeFinanciere droit,
                                                               AllocationImpotent newDonneeFinanciere, boolean forceClose) throws AllocationImpotentException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create Allocation Impotent, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create Allocation Impotent, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (AllocationImpotent) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    AllocationImpotentSearch.class, PegasusImplServiceLocator.getAllocationImpotentService(),
                    AllocationImpotent.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public AllocationsFamiliales createAndCloseAllocationsFamiliales(ModificateurDroitDonneeFinanciere droit,
                                                                     AllocationsFamiliales newDonneeFinanciere, boolean forceClose) throws AllocationsFamilialesException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create allocations familiales, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create allocations familiales, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (AllocationsFamiliales) this.updateAncienneDonneeFinanciere(newDonneeFinanciere,
                    droit, AllocationsFamilialesSearch.class,
                    PegasusImplServiceLocator.getAllocationsFamilialesService(), AllocationsFamiliales.class,
                    forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public AssuranceRenteViagere createAndCloseAssuranceRenteViagere(ModificateurDroitDonneeFinanciere droit,
                                                                     AssuranceRenteViagere newDonneeFinanciere, boolean forceClose) throws AssuranceRenteViagereException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create assurance rente viagere, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create assurance rente viagere, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (AssuranceRenteViagere) this.updateAncienneDonneeFinanciere(newDonneeFinanciere,
                    droit, AssuranceRenteViagereSearch.class,
                    PegasusImplServiceLocator.getAssuranceRenteViagereService(), AssuranceRenteViagere.class,
                    forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public AssuranceVie createAndCloseAssuranceVie(ModificateurDroitDonneeFinanciere droit,
                                                   AssuranceVie newDonneeFinanciere, boolean forceClose) throws AssuranceVieException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create AssuranceVie, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create AssuranceVie, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (AssuranceVie) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    AssuranceVieSearch.class, PegasusImplServiceLocator.getAssuranceVieService(), AssuranceVie.class,
                    forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public AutreApi createAndCloseAutreApi(ModificateurDroitDonneeFinanciere droit, AutreApi newDonneeFinanciere,
                                           boolean forceClose) throws AutreApiException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create autreApi, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create autreApi, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (AutreApi) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    AutreApiSearch.class, PegasusImplServiceLocator.getAutreApiService(), AutreApi.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public AutreFortuneMobiliere createAndCloseAutreFortuneMobiliere(ModificateurDroitDonneeFinanciere droit,
                                                                     AutreFortuneMobiliere newDonneeFinanciere, boolean forceClose) throws AutreFortuneMobiliereException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create betail, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create betail, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (AutreFortuneMobiliere) this.updateAncienneDonneeFinanciere(newDonneeFinanciere,
                    droit, AutreFortuneMobiliereSearch.class,
                    PegasusImplServiceLocator.getAutreFortuneMobiliereService(), AutreFortuneMobiliere.class,
                    forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public AutreRente createAndCloseAutreRente(ModificateurDroitDonneeFinanciere droit, AutreRente newDonneeFinanciere,
                                               boolean forceClose) throws AutreRenteException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create autreRente, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create autreRente, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (AutreRente) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    AutreRenteSearch.class, PegasusImplServiceLocator.getAutreRenteService(), AutreRente.class,
                    forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public AutresDettesProuvees createAndCloseAutresDettesProuvees(ModificateurDroitDonneeFinanciere droit,
                                                                   AutresDettesProuvees newDonneeFinanciere, boolean forceClose) throws AutresDettesProuveesException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create AutresDettesProuvees, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create AutresDettesProuvees, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (AutresDettesProuvees) this.updateAncienneDonneeFinanciere(newDonneeFinanciere,
                    droit, AutresDettesProuveesSearch.class,
                    PegasusImplServiceLocator.getAutresDettesProuveesService(), AutresDettesProuvees.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public AutresRevenus createAndCloseAutresRevenus(ModificateurDroitDonneeFinanciere droit,
                                                     AutresRevenus newDonneeFinanciere, boolean forceClose) throws AutresRevenusException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create AutresRevenus, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create AutresRevenus, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (AutresRevenus) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    AutresRevenusSearch.class, PegasusImplServiceLocator.getAutresRevenusService(),
                    AutresRevenus.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public Betail createAndCloseBetail(ModificateurDroitDonneeFinanciere droit, Betail newDonneeFinanciere,
                                       boolean forceClose) throws BetailException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create betail, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create betail, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (Betail) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    BetailSearch.class, PegasusImplServiceLocator.getBetailService(), Betail.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public BienImmobilierHabitationNonPrincipale createAndCloseBienImmobilierHabitationNonPrincipale(
            ModificateurDroitDonneeFinanciere droit, BienImmobilierHabitationNonPrincipale newDonneeFinanciere,
            boolean forceClose) throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException,
            DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create BienImmobilierHabitationNonPrincipale, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create BienImmobilierHabitationNonPrincipale, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (BienImmobilierHabitationNonPrincipale) this.updateAncienneDonneeFinanciere(
                    newDonneeFinanciere, droit, BienImmobilierHabitationNonPrincipaleSearch.class,
                    PegasusImplServiceLocator.getBienImmobilierHabitationNonPrincipaleService(),
                    BienImmobilierHabitationNonPrincipale.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public BienImmobilierNonHabitable createAndCloseBienImmobilierNonHabitable(ModificateurDroitDonneeFinanciere droit,
                                                                               BienImmobilierNonHabitable newDonneeFinanciere, boolean forceClose)
            throws BienImmobilierNonHabitableException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create BienImmobilierNonHabitable, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create BienImmobilierNonHabitable, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (BienImmobilierNonHabitable) this.updateAncienneDonneeFinanciere(newDonneeFinanciere,
                    droit, BienImmobilierNonHabitableSearch.class,
                    PegasusImplServiceLocator.getBienImmobilierNonHabitableService(), BienImmobilierNonHabitable.class,
                    forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public BienImmobilierServantHabitationPrincipale createAndCloseBienImmobilierServantHabitationPrincipale(
            ModificateurDroitDonneeFinanciere droit, BienImmobilierServantHabitationPrincipale newDonneeFinanciere,
            boolean forceClose) throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException,
            DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create BienImmobilierServantHabitationPrincipale, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException(
                    "Unable to create BienImmobilierServantHabitationPrincipale, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (BienImmobilierServantHabitationPrincipale) this.updateAncienneDonneeFinanciere(
                    newDonneeFinanciere, droit, BienImmobilierServantHabitationPrincipaleSearch.class,
                    PegasusImplServiceLocator.getBienImmobilierServantHabitationPrincipaleService(),
                    BienImmobilierServantHabitationPrincipale.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public CapitalLPP createAndCloseCapitalLPP(ModificateurDroitDonneeFinanciere droit, CapitalLPP newDonneeFinanciere,
                                               boolean forceClose) throws CapitalLPPException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create CapitalLPP, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create CapitalLPP, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (CapitalLPP) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    CapitalLPPSearch.class, PegasusImplServiceLocator.getCapitalLPPService(), CapitalLPP.class,
                    forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    // ici Fin
    @Override
    public CompteBancaireCCP createAndCloseCompteBancaireCCP(ModificateurDroitDonneeFinanciere droit,
                                                             CompteBancaireCCP newDonneeFinanciere, boolean forceClose) throws CompteBancaireCCPException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create CompteBancaireCCP, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create CompteBancaireCCP, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (CompteBancaireCCP) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    CompteBancaireCCPSearch.class, PegasusImplServiceLocator.getCompteBancaireCCPService(),
                    CompteBancaireCCP.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public ContratEntretienViager createAndCloseContratEntretienViager(ModificateurDroitDonneeFinanciere droit,
                                                                       ContratEntretienViager newDonneeFinanciere, boolean forceClose) throws ContratEntretienViagerException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create ContratEntretienViager, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create ContratEntretienViager, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (ContratEntretienViager) this.updateAncienneDonneeFinanciere(newDonneeFinanciere,
                    droit, ContratEntretienViagerSearch.class,
                    PegasusImplServiceLocator.getContratEntretienViagerService(), ContratEntretienViager.class,
                    forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public CotisationsPsal createAndCloseCotisationsPsal(ModificateurDroitDonneeFinanciere droit,
                                                         CotisationsPsal newDonneeFinanciere, boolean forceClose) throws CotisationsPsalException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create CotisationsPsal, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create CotisationsPsal, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (CotisationsPsal) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    CotisationsPsalSearch.class, PegasusImplServiceLocator.getCotisationsPsalService(),
                    CotisationsPsal.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }

        return newDonneeFinanciere;
    }

    @Override
    public CotisationsPsal createAndCloseCotisationsPsal(ModificateurDroitDonneeFinanciere droit,
                                                         CotisationsPsal newCotisationsPsal, CotisationsPsal oldCotisationsPsal, boolean forceClose)
            throws CotisationsPsalException, JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newCotisationsPsal == null) {
            throw new DroitException("Unable to create CotisationsPsal, the newCotisationsPsal is null");
        }
        if (oldCotisationsPsal == null) {
            throw new DroitException("Unable to create CotisationsPsal, the oldCotisationsPsal is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create CotisationsPsal, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newCotisationsPsal = (CotisationsPsal) this.updateAncienneDonneeFinanciere(newCotisationsPsal,
                    oldCotisationsPsal, droit.getSimpleVersionDroit(), CotisationsPsalSearch.class,
                    PegasusImplServiceLocator.getCotisationsPsalService(), CotisationsPsal.class, false);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }

        return newCotisationsPsal;
    }

    @Override
    public DessaisissementRevenu createAndCloseDessaisissementRevenu(ModificateurDroitDonneeFinanciere droit,
                                                                     DessaisissementRevenu newDonneeFinanciere, boolean forceClose)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create DessaisissementRevenu, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create DessaisissementRevenu, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (DessaisissementRevenu) this.updateAncienneDonneeFinanciere(newDonneeFinanciere,
                    droit, DessaisissementRevenuSearch.class,
                    PegasusImplServiceLocator.getDessaisissementRevenuService(), DessaisissementRevenu.class,
                    forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public IjApg createAndCloseIjApg(ModificateurDroitDonneeFinanciere droit, IjApg newDonneeFinanciere,
                                     boolean forceClose) throws IjApgException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create IjApg, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create IjApg, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (IjApg) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    IjApgSearch.class, PegasusImplServiceLocator.getIjApgService(), IjApg.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public IndemniteJournaliereAi createAndCloseIndemniteJournaliereAi(ModificateurDroitDonneeFinanciere droit,
                                                                       IndemniteJournaliereAi newDonneeFinanciere, boolean forceClose) throws IndemniteJournaliereAiException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create indemniteJournaliereAi, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create indemniteJournaliereAi, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (IndemniteJournaliereAi) this.updateAncienneDonneeFinanciere(newDonneeFinanciere,
                    droit, IndemniteJournaliereAiSearch.class,
                    PegasusImplServiceLocator.getIndemniteJournaliereAiService(), IndemniteJournaliereAi.class,
                    forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public Loyer createAndCloseLoyer(ModificateurDroitDonneeFinanciere droit, Loyer newDonneeFinanciere,
                                     boolean forceClose) throws LoyerException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create loyer, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create loyer, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {

            newDonneeFinanciere = (Loyer) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    LoyerSearch.class, PegasusImplServiceLocator.getLoyerService(), Loyer.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public MarchandisesStock createAndCloseMarchandisesStock(ModificateurDroitDonneeFinanciere droit,
                                                             MarchandisesStock newDonneeFinanciere, boolean forceClose) throws MarchandisesStockException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create pret envers tiers, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create pret envers tiers, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {

            newDonneeFinanciere = (MarchandisesStock) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    MarchandisesStockSearch.class, PegasusImplServiceLocator.getMarchandisesStockService(),
                    MarchandisesStock.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public Numeraire createAndCloseNumeraire(ModificateurDroitDonneeFinanciere droit, Numeraire newDonneeFinanciere,
                                             boolean forceClose) throws NumeraireException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create numeraire, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create numeraire, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (Numeraire) this
                    .updateAncienneDonneeFinanciere(newDonneeFinanciere, droit, NumeraireSearch.class,
                            PegasusImplServiceLocator.getNumeraireService(), Numeraire.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public PensionAlimentaire createAndClosePensionAlimentaire(ModificateurDroitDonneeFinanciere droit,
                                                               PensionAlimentaire newDonneeFinanciere, boolean forceClose) throws PensionAlimentaireException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create PensionAlimentaire, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create PensionAlimentaire, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {

            newDonneeFinanciere = (PensionAlimentaire) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    PensionAlimentaireSearch.class, PegasusImplServiceLocator.getPensionAlimentaireService(),
                    PensionAlimentaire.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public PretEnversTiers createAndClosePretEnversTiers(ModificateurDroitDonneeFinanciere droit,
                                                         PretEnversTiers newDonneeFinanciere, boolean forceClose) throws PretEnversTiersException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create pret envers tiers, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create pret envers tiers, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (PretEnversTiers) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    PretEnversTiersSearch.class, PegasusImplServiceLocator.getPretEnversTiersService(),
                    PretEnversTiers.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public RenteAvsAi createAndCloseRenteAvsAi(Droit droit, RenteAvsAi newDonneeFinanciere,
                                               RenteAvsAi oldDonneeFinanciere, boolean forClosePeriode) throws RenteAvsAiException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (oldDonneeFinanciere == null) {
            throw new DroitException("Unable to create RenteAvsAi, the oldDRenteAvsAi is null");
        }
        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create RenteAvsAi, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create RenteAvsAi, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (RenteAvsAi) this.updateAncienneDonneeFinanciere(newDonneeFinanciere,
                    oldDonneeFinanciere, droit.getSimpleVersionDroit(), RenteAvsAiSearch.class,
                    PegasusImplServiceLocator.getRenteAvsAiService(), RenteAvsAi.class, forClosePeriode);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public RenteAvsAi createAndCloseRenteAvsAi(ModificateurDroitDonneeFinanciere droit, RenteAvsAi newDonneeFinanciere,
                                               boolean forceClose) throws RenteAvsAiException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create RenteAvsAi, the model is null");
        }

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create RenteAvsAi, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (RenteAvsAi) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    RenteAvsAiSearch.class, PegasusImplServiceLocator.getRenteAvsAiService(), RenteAvsAi.class,
                    forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public RevenuActiviteLucrativeDependante createAndCloseRevenuActiviteLucrativeDependante(
            ModificateurDroitDonneeFinanciere droit, RevenuActiviteLucrativeDependante newDonneeFinanciere,
            boolean forceClose) throws RevenuActiviteLucrativeDependanteException, JadePersistenceException,
            DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create RevenuActiviteLucrativeDependante, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create RevenuActiviteLucrativeDependante, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (RevenuActiviteLucrativeDependante) this.updateAncienneDonneeFinanciere(
                    newDonneeFinanciere, droit, RevenuActiviteLucrativeDependanteSearch.class,
                    PegasusImplServiceLocator.getRevenuActiviteLucrativeDependanteService(),
                    RevenuActiviteLucrativeDependante.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public RevenuActiviteLucrativeIndependante createAndCloseRevenuActiviteLucrativeIndependante(
            ModificateurDroitDonneeFinanciere droit, RevenuActiviteLucrativeIndependante newDonneeFinanciere,
            boolean forceClose) throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException,
            DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create RevenuActiviteLucrativeIndependante, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create RevenuActiviteLucrativeIndependante, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (RevenuActiviteLucrativeIndependante) this.updateAncienneDonneeFinanciere(
                    newDonneeFinanciere, droit, RevenuActiviteLucrativeIndependanteSearch.class,
                    PegasusImplServiceLocator.getRevenuActiviteLucrativeIndependanteService(),
                    RevenuActiviteLucrativeIndependante.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public RevenuHypothetique createAndCloseRevenuHypothetique(ModificateurDroitDonneeFinanciere droit,
                                                               RevenuHypothetique newDonneeFinanciere, boolean forceClose) throws RevenuHypothetiqueException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create RevenuHypothetique, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create RevenuHypothetique, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (RevenuHypothetique) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    RevenuHypothetiqueSearch.class, PegasusImplServiceLocator.getRevenuHypothetiqueService(),
                    RevenuHypothetique.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public TaxeJournaliereHome createAndCloseTaxeJournaliereHome(ModificateurDroitDonneeFinanciere droit,
                                                                 TaxeJournaliereHome newDonneeFinanciere, boolean forceClose) throws TaxeJournaliereHomeException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create taxeJournaliereHome, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create taxeJournaliereHome, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (TaxeJournaliereHome) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    TaxeJournaliereHomeSearch.class, PegasusImplServiceLocator.getTaxeJournaliereHomeService(),
                    TaxeJournaliereHome.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public SejourMoisPartielHome createAndCloseSejourMoisPartielHome(ModificateurDroitDonneeFinanciere droit,
                                                                     SejourMoisPartielHome newDonneeFinanciere, boolean forceClose) throws SejourMoisPartielHomeException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create taxeJournaliereHome, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create taxeJournaliereHome, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (SejourMoisPartielHome) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    SejourMoisPartielHomeSearch.class, PegasusImplServiceLocator.getSejourMoisPartielHomeService(),
                    SejourMoisPartielHome.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public Titre createAndCloseTitre(ModificateurDroitDonneeFinanciere droit, Titre newDonneeFinanciere,
                                     boolean forceClose) throws TitreException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create Titre, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create Titre, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (Titre) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    TitreSearch.class, PegasusImplServiceLocator.getTitreService(), Titre.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public Vehicule createAndCloseVehicule(ModificateurDroitDonneeFinanciere droit, Vehicule newDonneeFinanciere,
                                           boolean forceClose) throws VehiculeException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create vehicule, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create vehicule, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (Vehicule) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    VehiculeSearch.class, PegasusImplServiceLocator.getVehiculeService(), Vehicule.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public AssuranceRenteViagere createAssuranceRenteViagere(ModificateurDroitDonneeFinanciere droit,
                                                             DroitMembreFamille droitMembreFamille, AssuranceRenteViagere newAssuranceRenteViagere)
            throws DroitException, JadePersistenceException, AssuranceRenteViagereException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create assurance rente viagere, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newAssuranceRenteViagere = PegasusImplServiceLocator.getFortuneParticuliereService()
                    .createAssuranceRenteViagere(droit.getSimpleVersionDroit(), droitMembreFamille,
                            newAssuranceRenteViagere);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newAssuranceRenteViagere;
    }

    @Override
    public AssuranceVie createAssuranceVie(ModificateurDroitDonneeFinanciere droit,
                                           DroitMembreFamille instanceDroitMembreFamille, AssuranceVie assuranceVie) throws AssuranceVieException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create assuranceVie, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            assuranceVie = PegasusImplServiceLocator.getFortuneUsuelleService().createAssuranceVie(
                    droit.getSimpleVersionDroit(), instanceDroitMembreFamille, assuranceVie);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return assuranceVie;
    }

    @Override
    public AutreApi createAutreApi(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                                   AutreApi newAutreApi) throws AutreApiException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create AutreApi, the droit is null or new");
        }
        this.updateDroitForDonneeFinanciere(droit);
        try {
            newAutreApi = PegasusImplServiceLocator.getRenteIjApiService().createAutreApi(
                    droit.getSimpleVersionDroit(), droitMembreFamille, newAutreApi);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newAutreApi;
    }

    @Override
    public AutreFortuneMobiliere createAutreFortuneMobiliere(ModificateurDroitDonneeFinanciere droit,
                                                             DroitMembreFamille droitMembreFamille, AutreFortuneMobiliere newAutreFortuneMobiliere)
            throws DroitException, JadePersistenceException, AutreFortuneMobiliereException, DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create betail, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newAutreFortuneMobiliere = PegasusImplServiceLocator.getFortuneParticuliereService()
                    .createAutreFortuneMobiliere(droit.getSimpleVersionDroit(), droitMembreFamille,
                            newAutreFortuneMobiliere);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newAutreFortuneMobiliere;
    }

    @Override
    public AutreRente createAutreRente(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                                       AutreRente newAutreRente) throws DroitException, JadePersistenceException, AutreRenteException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create autreRente, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newAutreRente = PegasusImplServiceLocator.getRenteIjApiService().createAutreRente(
                    droit.getSimpleVersionDroit(), droitMembreFamille, newAutreRente);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return newAutreRente;
    }

    @Override
    public AutresDettesProuvees createAutresDettesProuvees(ModificateurDroitDonneeFinanciere droit,
                                                           DroitMembreFamille instanceDroitMembreFamille, AutresDettesProuvees autresDettesProuvees)
            throws AutresDettesProuveesException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create autresDettesProuvees, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            autresDettesProuvees = PegasusImplServiceLocator.getFortuneUsuelleService().createAutresDettesProuvees(
                    droit.getSimpleVersionDroit(), instanceDroitMembreFamille, autresDettesProuvees);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return autresDettesProuvees;
    }

    @Override
    public AutresRevenus createAutresRevenus(ModificateurDroitDonneeFinanciere droit,
                                             DroitMembreFamille instanceDroitMembreFamille, AutresRevenus autresRevenus) throws AutresRevenusException,
            JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create AutresRevenus, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            autresRevenus = PegasusImplServiceLocator.getRevenusDepensesService().createAutresRevenus(
                    droit.getSimpleVersionDroit(), instanceDroitMembreFamille, autresRevenus);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return autresRevenus;

    }

    @Override
    public Betail createBetail(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                               Betail newBetail) throws DroitException, JadePersistenceException, BetailException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create betail, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newBetail = PegasusImplServiceLocator.getFortuneParticuliereService().createBetail(
                    droit.getSimpleVersionDroit(), droitMembreFamille, newBetail);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newBetail;
    }

    @Override
    public BienImmobilierHabitationNonPrincipale createBienImmobilierHabitationNonPrincipale(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create bienImmobilierHabitationNonPrincipale, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            bienImmobilierHabitationNonPrincipale = PegasusImplServiceLocator.getFortuneUsuelleService()
                    .createBienImmobilierHabitationNonPrincipale(droit.getSimpleVersionDroit(),
                            instanceDroitMembreFamille, bienImmobilierHabitationNonPrincipale);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return bienImmobilierHabitationNonPrincipale;
    }

    @Override
    public BienImmobilierNonHabitable createBienImmobilierNonHabitable(ModificateurDroitDonneeFinanciere droit,
                                                                       DroitMembreFamille instanceDroitMembreFamille, BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create bienImmobilierNonHabitable, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            bienImmobilierNonHabitable = PegasusImplServiceLocator.getFortuneUsuelleService()
                    .createBienImmobilierNonHabitable(droit.getSimpleVersionDroit(), instanceDroitMembreFamille,
                            bienImmobilierNonHabitable);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return bienImmobilierNonHabitable;
    }

    @Override
    public BienImmobilierServantHabitationPrincipale createBienImmobilierServantHabitationPrincipale(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException(
                    "Unable to create bienImmobilierServantHabitationPrincipale, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            bienImmobilierServantHabitationPrincipale = PegasusImplServiceLocator.getFortuneUsuelleService()
                    .createBienImmobilierServantHabitationPrincipale(droit.getSimpleVersionDroit(),
                            instanceDroitMembreFamille, bienImmobilierServantHabitationPrincipale);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return bienImmobilierServantHabitationPrincipale;
    }

    @Override
    public CapitalLPP createCapitalLPP(ModificateurDroitDonneeFinanciere droit,
                                       DroitMembreFamille instanceDroitMembreFamille, CapitalLPP capitalLPP) throws CapitalLPPException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create capitalLPP, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            capitalLPP = PegasusImplServiceLocator.getFortuneUsuelleService().createCapitalLPP(
                    droit.getSimpleVersionDroit(), instanceDroitMembreFamille, capitalLPP);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return capitalLPP;
    }

    @Override
    public CompteBancaireCCP createCompteBancaireCCP(ModificateurDroitDonneeFinanciere droit,
                                                     DroitMembreFamille instanceDroitMembreFamille, CompteBancaireCCP compteBancaireCCP)
            throws CompteBancaireCCPException, JadePersistenceException, DroitException, DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create compteBancaireCCP, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            compteBancaireCCP = PegasusImplServiceLocator.getFortuneUsuelleService().createCompteBancaireCCP(
                    droit.getSimpleVersionDroit(), instanceDroitMembreFamille, compteBancaireCCP);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return compteBancaireCCP;
    }

    @Override
    public ContratEntretienViager createContratEntretienViager(ModificateurDroitDonneeFinanciere droit,
                                                               DroitMembreFamille droitMembreFamille, ContratEntretienViager newContratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create allocationsFamiliales, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newContratEntretienViager = PegasusImplServiceLocator.getRevenusDepensesService()
                    .createContratEntretienViager(droit.getSimpleVersionDroit(), droitMembreFamille,
                            newContratEntretienViager);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return newContratEntretienViager;
    }

    @Override
    public CotisationsPsal createCotisationsPsal(ModificateurDroitDonneeFinanciere droit,
                                                 DroitMembreFamille instanceDroitMembreFamille, CotisationsPsal cotisationsPsal)
            throws CotisationsPsalException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create cotisationsPsal, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            cotisationsPsal = PegasusImplServiceLocator.getRevenusDepensesService().createCotisationsPsal(
                    droit.getSimpleVersionDroit(), instanceDroitMembreFamille, cotisationsPsal);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return cotisationsPsal;
    }

    @Override
    public DessaisissementFortune createDessaisissementFortune(ModificateurDroitDonneeFinanciere droit,
                                                               DroitMembreFamille droitMembreFamille, DessaisissementFortune dessaisissementFortune)
            throws DroitException, JadePersistenceException, DessaisissementFortuneException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create dessaisissement fortune, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            dessaisissementFortune = PegasusImplServiceLocator.getDessaisissementService()
                    .createDessaisissementFortune(droit.getSimpleVersionDroit(), droitMembreFamille,
                            dessaisissementFortune);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return dessaisissementFortune;
    }

    @Override
    public DessaisissementRevenu createDessaisissementRevenu(ModificateurDroitDonneeFinanciere droit,
                                                             DroitMembreFamille droitMembreFamille, DessaisissementRevenu dessaisissementRevenu) throws DroitException,
            JadePersistenceException, DessaisissementRevenuException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create dessaisissement revenu, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            dessaisissementRevenu = PegasusImplServiceLocator.getDessaisissementService().createDessaisissementRevenu(
                    droit.getSimpleVersionDroit(), droitMembreFamille, dessaisissementRevenu);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return dessaisissementRevenu;
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.droit.DroitService#
     * createDroitInitial(ch.globaz.pegasus.business.models.demande.Demande)
     */
    @Override
    public Droit createDroitInitial(Demande demande) throws DroitException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DonneesPersonnellesException {

        // DroitChecker.checkForCreationInitial(demande);
        //
        // // creation du droit initial
        // Droit droitInitial = new Droit();
        // droitInitial.setDemande(demande);
        //
        // // creation du simple droit
        // SimpleDroit simpleDroit = new SimpleDroit();
        // simpleDroit.setIdDemandePC(demande.getSimpleDemande().getIdDemande());
        // droitInitial.setSimpleDroit(PegasusImplServiceLocator.getSimpleDroitService().create(simpleDroit));
        //
        // // creation du simple version droit
        // SimpleVersionDroit simpleVersionDroit = new SimpleVersionDroit();
        // simpleVersionDroit.setIdDroit(simpleDroit.getIdDroit());
        // simpleVersionDroit.setCsEtatDroit(IPCDroits.CS_ENREGISTRE);
        // simpleVersionDroit.setNoVersion("1");
        // // pour le droit initial la date d'annonce est la date de depot de la
        // // demande pc
        // simpleVersionDroit.setDateAnnonce(demande.getSimpleDemande().getDateDepot());
        // droitInitial.setSimpleVersionDroit(PegasusImplServiceLocator.getSimpleVersionDroitService().create(
        // simpleVersionDroit));
        Droit droitInitial = creationDroitInitial(demande);

        // creation des liens sur les membres de famille
        if (!JadeThread.logHasMessages()) {
            try {

                this.synchroniseMembresFamille(droitInitial, false);

            } catch (MembreFamilleException e) {
                throw new DroitException("Unable to create droit, the membrefamille entities can't be retrieved.", e);
            }

            copyOldDroitInTheNewDroit(droitInitial);
        }
        return droitInitial;
    }

    /**
     * Creation du droit initial pour les fratries, sans la synchronisations
     *
     * @param demande
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     * @throws DonneesPersonnellesException
     */
    @Override
    public Droit createDroitInitialForFratrie(Demande demande, ArrayList<MembreFamilleVO> fratrie)
            throws DroitException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            DonneesPersonnellesException {
        Droit newDroit = creationDroitInitial(demande);

        String idTiersRequerant = demande.getDossier().getDemandePrestation().getDemandePrestation().getIdTiers();
        // pour tous les membres de la fratrie
        for (MembreFamilleVO membreFratrie : fratrie) {
            Boolean isEnfantRequerant = false;
            if (membreFratrie.getIdTiers().equals(idTiersRequerant)) {
                isEnfantRequerant = true;
            }
            createDroitMembreFamille(newDroit.getSimpleDroit(), idTiersRequerant, membreFratrie, isEnfantRequerant);
        }
        return newDroit;
    }

    private void createDroitMembreFamille(SimpleDroit simpleDroit, String idTiersRequerant,
                                          MembreFamilleVO membreFamille, Boolean requerantIsEnfant) throws DonneesPersonnellesException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException, DroitException {
        // création de données personnelles

        DroitChecker.checkForCreateDroitMemembreFamille(membreFamille);

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            SimpleDonneesPersonnelles donneesPersonnelles = new SimpleDonneesPersonnelles();
            donneesPersonnelles.setIsEnfant(requerantIsEnfant);
            donneesPersonnelles = PegasusImplServiceLocator.getSimpleDonneesPersonnellesService().create(
                    donneesPersonnelles);

            SimpleDroitMembreFamille simpleDroitMembreFamille = new SimpleDroitMembreFamille();
            simpleDroitMembreFamille.setIdDonneesPersonnelles(donneesPersonnelles.getId());

            String csRoleFamillePC = DroitServiceImpl.convertCsRoleFamillePC(membreFamille.getRelationAuRequerant(),
                    membreFamille.getIdTiers().equals(idTiersRequerant));
            // si requérant enfant, on set le cs
            if (requerantIsEnfant) {
                csRoleFamillePC = IPCDroits.CS_ROLE_FAMILLE_REQUERANT;
            }
            if (csRoleFamillePC != null) {
                simpleDroitMembreFamille.setCsRoleFamillePC(csRoleFamillePC);
                simpleDroitMembreFamille.setIdDroit(simpleDroit.getId());

                simpleDroitMembreFamille.setIdMembreFamilleSF(membreFamille.getIdMembreFamille());

                simpleDroitMembreFamille.setIdMembreFamilleSF(membreFamille.getIdMembreFamille());

                simpleDroitMembreFamille = PegasusImplServiceLocator.getSimpleDroitMembreFamilleService().create(
                        simpleDroitMembreFamille);
            }
        }
    }

    @Override
    public IjApg createIjApg(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille, IjApg ijApg)
            throws DroitException, JadePersistenceException, DonneeFinanciereException, IjApgException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create ijApg, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            PegasusImplServiceLocator.getRenteIjApiService().createIjApg(droit.getSimpleVersionDroit(),
                    droitMembreFamille, ijApg);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return ijApg;
    }

    @Override
    public IndemniteJournaliereAi createIndemniteJournaliereAi(ModificateurDroitDonneeFinanciere droit,
                                                               DroitMembreFamille droitMembreFamille, IndemniteJournaliereAi newIndemniteJournaliereAi)
            throws DroitException, JadePersistenceException, IndemniteJournaliereAiException, DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create newIndemniteJournaliereAi, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newIndemniteJournaliereAi = PegasusImplServiceLocator.getRenteIjApiService().createIndemniteJournaliereAi(
                    droit.getSimpleVersionDroit(), droitMembreFamille, newIndemniteJournaliereAi);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newIndemniteJournaliereAi;
    }

    @Override
    public Loyer createLoyer(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille, Loyer loyer)
            throws LoyerException, JadePersistenceException, DroitException, DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create loyer, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            loyer = PegasusImplServiceLocator.getHabitatService().createLoyer(droit.getSimpleVersionDroit(),
                    droitMembreFamille, loyer);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return loyer;
    }

    @Override
    public MarchandisesStock createMarchandisesStock(ModificateurDroitDonneeFinanciere droit,
                                                     DroitMembreFamille droitMembreFamille, MarchandisesStock newMarchandisesStock)
            throws MarchandisesStockException, JadePersistenceException, DroitException, DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create marchandises/stock, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newMarchandisesStock = PegasusImplServiceLocator.getFortuneParticuliereService().createMarchandisesStock(
                    droit.getSimpleVersionDroit(), droitMembreFamille, newMarchandisesStock);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newMarchandisesStock;
    }

    @Override
    public Numeraire createNumeraire(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                                     Numeraire newNumeraire) throws DroitException, JadePersistenceException, NumeraireException,
            DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create pret envers tiers, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newNumeraire = PegasusImplServiceLocator.getFortuneParticuliereService().createNumeraire(
                    droit.getSimpleVersionDroit(), droitMembreFamille, newNumeraire);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newNumeraire;
    }

    @Override
    public PensionAlimentaire createPensionAlimentaire(ModificateurDroitDonneeFinanciere droit,
                                                       DroitMembreFamille instanceDroitMembreFamille, PensionAlimentaire pensionAlimentaire)
            throws PensionAlimentaireException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create pensionAlimentaire, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            pensionAlimentaire = PegasusImplServiceLocator.getRevenusDepensesService().createPensionAlimentaire(
                    droit.getSimpleVersionDroit(), instanceDroitMembreFamille, pensionAlimentaire);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return pensionAlimentaire;
    }

    @Override
    public PretEnversTiers createPretEnversTiers(ModificateurDroitDonneeFinanciere droit,
                                                 DroitMembreFamille droitMembreFamille, PretEnversTiers newPretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException, DroitException, DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create pret envers tiers, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newPretEnversTiers = PegasusImplServiceLocator.getFortuneParticuliereService().createPretEnversTiers(
                    droit.getSimpleVersionDroit(), droitMembreFamille, newPretEnversTiers);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newPretEnversTiers;
    }

    @Override
    public RenteAvsAi createRenteAvsAi(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                                       RenteAvsAi newRenteAvsAi) throws DroitException, JadePersistenceException, RenteAvsAiException,
            DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create Rente avs ai, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newRenteAvsAi = PegasusImplServiceLocator.getRenteIjApiService().createRenteAvsAi(
                    droit.getSimpleVersionDroit(), droitMembreFamille, newRenteAvsAi);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newRenteAvsAi;
    }

    @Override
    public RevenuActiviteLucrativeDependante createRevenuActiviteLucrativeDependante(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            RevenuActiviteLucrativeDependante newRevenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create revenuActiviteLucrativeDependante, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newRevenuActiviteLucrativeDependante = PegasusImplServiceLocator.getRevenusDepensesService()
                    .createRevenuActiviteLucrativeDependante(droit.getSimpleVersionDroit(), droitMembreFamille,
                            newRevenuActiviteLucrativeDependante);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return newRevenuActiviteLucrativeDependante;
    }

    @Override
    public RevenuActiviteLucrativeIndependante createRevenuActiviteLucrativeIndependante(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            RevenuActiviteLucrativeIndependante newRevenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create revenuActiviteLucrativeIndependante, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newRevenuActiviteLucrativeIndependante = PegasusImplServiceLocator.getRevenusDepensesService()
                    .createRevenuActiviteLucrativeIndependante(droit.getSimpleVersionDroit(), droitMembreFamille,
                            newRevenuActiviteLucrativeIndependante);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newRevenuActiviteLucrativeIndependante;
    }

    @Override
    public RevenuHypothetique createRevenuHypothetique(ModificateurDroitDonneeFinanciere droit,
                                                       DroitMembreFamille droitMembreFamille, RevenuHypothetique newRevenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException, DroitException, DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create RevenuHypothetique, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newRevenuHypothetique = PegasusImplServiceLocator.getRevenusDepensesService().createRevenuHypothetique(
                    droit.getSimpleVersionDroit(), droitMembreFamille, newRevenuHypothetique);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newRevenuHypothetique;
    }

    @Override
    public SimpleLibelleContratEntretienViager createSimpleLibelleContratEntretienViager(
            String idContratEntretienViager, SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager)
            throws DroitException, JadePersistenceException, SimpleLibelleContratEntretienViagerException {

        if (simpleLibelleContratEntretienViager == null) {
            throw new DroitException("Unable to create newSimpleTypeFraisObtentionRevenu, the model is null");
        }

        try {
            simpleLibelleContratEntretienViager.setIdContratEntretienViager(idContratEntretienViager);
            simpleLibelleContratEntretienViager = PegasusImplServiceLocator
                    .getSimpleLibelleContratEntretienViagerService().create(simpleLibelleContratEntretienViager);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return simpleLibelleContratEntretienViager;
    }

    @Override
    public SimpleTypeFraisObtentionRevenu createSimpleTypeFraisObtentionRevenu(
            String idRevenuActiviteLucrativeDependante, SimpleTypeFraisObtentionRevenu newSimpleTypeFraisObtentionRevenu)
            throws DroitException, JadePersistenceException, SimpleTypeFraisObtentionRevenuException {

        if (newSimpleTypeFraisObtentionRevenu == null) {
            throw new DroitException("Unable to create newSimpleTypeFraisObtentionRevenu, the model is null");
        }

        try {
            newSimpleTypeFraisObtentionRevenu
                    .setIdRevenuActiviteLucrativeDependante(idRevenuActiviteLucrativeDependante);
            newSimpleTypeFraisObtentionRevenu = PegasusImplServiceLocator.getSimpleTypeFraisObtentionRevenuService()
                    .create(newSimpleTypeFraisObtentionRevenu);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newSimpleTypeFraisObtentionRevenu;
    }

    @Override
    public TaxeJournaliereHome createTaxeJournaliereHome(ModificateurDroitDonneeFinanciere droit,
                                                         DroitMembreFamille droitMembreFamille, TaxeJournaliereHome taxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create taxeJournaliereHome, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            taxeJournaliereHome = PegasusImplServiceLocator.getHabitatService().createTaxeJournaliereHome(
                    droit.getSimpleVersionDroit(), droitMembreFamille, taxeJournaliereHome);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return taxeJournaliereHome;
    }

    @Override
    public SejourMoisPartielHome createSejourMoisPartielHome(ModificateurDroitDonneeFinanciere droit,
                                                             DroitMembreFamille droitMembreFamille, SejourMoisPartielHome sejourMoisPartielHome)
            throws SejourMoisPartielHomeException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create sejourMoisPartielHome, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            sejourMoisPartielHome = PegasusImplServiceLocator.getHabitatService().createSejourMoisPartielHome(
                    droit.getSimpleVersionDroit(), droitMembreFamille, sejourMoisPartielHome);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return sejourMoisPartielHome;
    }

    @Override
    public Titre createTitre(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
                             Titre titre) throws TitreException, JadePersistenceException, DroitException, DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create titre, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            titre = PegasusImplServiceLocator.getFortuneUsuelleService().createTitre(droit.getSimpleVersionDroit(),
                    instanceDroitMembreFamille, titre);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return titre;
    }

    @Override
    public TypeFraisObtentionRevenu createTypeFraisObtentionRevenu(String idRevenuActiviteLucrativeDependante,
                                                                   TypeFraisObtentionRevenu typeFraisObtentionRevenu) throws DroitException, JadePersistenceException,
            TypeFraisObtentionRevenuException, DonneeFinanciereException {
        if (typeFraisObtentionRevenu == null) {
            throw new DroitException("Unable to create TypeFraisObtentionRevenu, the model is null");
        }

        try {
            typeFraisObtentionRevenu = PegasusImplServiceLocator.getTypeFraisObtentionRevenuService().create(
                    typeFraisObtentionRevenu);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return typeFraisObtentionRevenu;
    }

    @Override
    public Vehicule createVehicule(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                                   Vehicule newVehicule) throws DroitException, JadePersistenceException, VehiculeException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create betail, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newVehicule = PegasusImplServiceLocator.getFortuneParticuliereService().createVehicule(
                    droit.getSimpleVersionDroit(), droitMembreFamille, newVehicule);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newVehicule;
    }

    private Droit creationDroitInitial(Demande demande) throws DroitException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        DroitChecker.checkForCreationInitial(demande);

        // creation du droit initial
        Droit droitInitial = new Droit();

        droitInitial.setDemande(demande);

        // creation du simple droit
        SimpleDroit simpleDroit = new SimpleDroit();
        simpleDroit.setIdDemandePC(demande.getSimpleDemande().getIdDemande());
        droitInitial.setSimpleDroit(PegasusImplServiceLocator.getSimpleDroitService().create(simpleDroit));
        if (!JadeThread.logHasMessages()) {
            // creation du simple version droit
            SimpleVersionDroit simpleVersionDroit = new SimpleVersionDroit();
            simpleVersionDroit.setIdDroit(simpleDroit.getIdDroit());
            simpleVersionDroit.setCsEtatDroit(IPCDroits.CS_ENREGISTRE);
            simpleVersionDroit.setNoVersion("1");
            simpleVersionDroit.setCsMotif(IPCDroits.CS_MOTIF_DROIT_NOUVEAU_DROIT);
            // pour le droit initial la date d'annonce est la date de depot de la
            // demande pc
            simpleVersionDroit.setDateAnnonce(demande.getSimpleDemande().getDateDepot());
            droitInitial.setSimpleVersionDroit(PegasusImplServiceLocator.getSimpleVersionDroitService().create(
                    simpleVersionDroit));
        }
        return droitInitial;
        /*
         * } return null;
         */

    }

    @Override
    public AllocationImpotent deleteAllocationImpotent(ModificateurDroitDonneeFinanciere droit,
                                                       AllocationImpotent allocationImpotent) throws DroitException, JadePersistenceException,
            AllocationImpotentException {
        if (allocationImpotent == null) {
            throw new DroitException("Unable to deleterente allocationImpotent, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        // on set la rente supprimé
        try {
            allocationImpotent.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            allocationImpotent = (AllocationImpotent) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    allocationImpotent, PegasusImplServiceLocator.getAllocationImpotentService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return allocationImpotent;
    }

    @Override
    public AllocationsFamiliales deleteAllocationsFamiliales(ModificateurDroitDonneeFinanciere droit,
                                                             AllocationsFamiliales allocationsFamiliales) throws AllocationsFamilialesException,
            JadePersistenceException, DroitException, DonneeFinanciereException {
        if (allocationsFamiliales == null) {
            throw new DroitException("Unable to delete allocationsFamiliales, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            allocationsFamiliales.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            allocationsFamiliales = (AllocationsFamiliales) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    allocationsFamiliales, PegasusImplServiceLocator.getAllocationsFamilialesService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return allocationsFamiliales;
    }

    @Override
    public AssuranceRenteViagere deleteAssuranceRenteViagere(ModificateurDroitDonneeFinanciere droit,
                                                             AssuranceRenteViagere assuranceRenteViagere) throws DroitException, JadePersistenceException,
            AssuranceRenteViagereException {
        if (assuranceRenteViagere == null) {
            throw new DroitException("Unable to delete assurance rente viagere, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            assuranceRenteViagere.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            assuranceRenteViagere = (AssuranceRenteViagere) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    assuranceRenteViagere, PegasusImplServiceLocator.getAssuranceRenteViagereService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return assuranceRenteViagere;
    }

    @Override
    public AssuranceVie deleteAssuranceVie(ModificateurDroitDonneeFinanciere droit, AssuranceVie assuranceVie)
            throws AssuranceVieException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if (assuranceVie == null) {
            throw new DroitException("Unable to delete AssuranceVie, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            assuranceVie.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            assuranceVie = (AssuranceVie) saveDonneeFinanciere(droit.getSimpleVersionDroit(), assuranceVie,
                    PegasusImplServiceLocator.getAssuranceVieService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return assuranceVie;
    }

    @Override
    public AutreApi deleteAutreApi(ModificateurDroitDonneeFinanciere droit, AutreApi autreApi) throws DroitException,
            JadePersistenceException, AutreApiException {
        if (autreApi == null) {
            throw new DroitException("Unable to delete autreApi, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            autreApi.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            autreApi = (AutreApi) saveDonneeFinanciere(droit.getSimpleVersionDroit(), autreApi,
                    PegasusImplServiceLocator.getAutreApiService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.toString(), e);
        }
        return autreApi;
    }

    @Override
    public AutreFortuneMobiliere deleteAutreFortuneMobiliere(ModificateurDroitDonneeFinanciere droit,
                                                             AutreFortuneMobiliere autreFortuneM) throws DroitException, JadePersistenceException,
            AutreFortuneMobiliereException {
        if (autreFortuneM == null) {
            throw new DroitException("Unable to delete autre fortune mobiliere, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            autreFortuneM.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            autreFortuneM = (AutreFortuneMobiliere) saveDonneeFinanciere(droit.getSimpleVersionDroit(), autreFortuneM,
                    PegasusImplServiceLocator.getAutreFortuneMobiliereService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return autreFortuneM;
    }

    @Override
    public AutreRente deleteAutreRente(ModificateurDroitDonneeFinanciere droit, AutreRente autreRente)
            throws DroitException, JadePersistenceException, AutreRenteException {
        if (autreRente == null) {
            throw new DroitException("Unable to delete autreRente, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            autreRente.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            autreRente = (AutreRente) saveDonneeFinanciere(droit.getSimpleVersionDroit(), autreRente,
                    PegasusImplServiceLocator.getAutreRenteService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return autreRente;
    }

    @Override
    public AutresDettesProuvees deleteAutresDettesProuvees(ModificateurDroitDonneeFinanciere droit,
                                                           AutresDettesProuvees autresDettesProuvees) throws AutresDettesProuveesException, JadePersistenceException,
            DroitException, DonneeFinanciereException {
        if (autresDettesProuvees == null) {
            throw new DroitException("Unable to delete AutresDettesProuvees, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            autresDettesProuvees.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            autresDettesProuvees = (AutresDettesProuvees) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    autresDettesProuvees, PegasusImplServiceLocator.getAutresDettesProuveesService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return autresDettesProuvees;
    }

    @Override
    public AutresRevenus deleteAutresRevenus(ModificateurDroitDonneeFinanciere droit, AutresRevenus autresRevenus)
            throws AutresRevenusException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if (autresRevenus == null) {
            throw new DroitException("Unable to delete AutresRevenus, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            autresRevenus.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            autresRevenus = (AutresRevenus) saveDonneeFinanciere(droit.getSimpleVersionDroit(), autresRevenus,
                    PegasusImplServiceLocator.getAutresRevenusService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return autresRevenus;
    }

    @Override
    public Betail deleteBetail(ModificateurDroitDonneeFinanciere droit, Betail betail) throws DroitException,
            JadePersistenceException, BetailException {
        if (betail == null) {
            throw new DroitException("Unable to delete betail, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            betail.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            betail = (Betail) saveDonneeFinanciere(droit.getSimpleVersionDroit(), betail,
                    PegasusImplServiceLocator.getBetailService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return betail;
    }

    @Override
    public BienImmobilierHabitationNonPrincipale deleteBienImmobilierHabitationNonPrincipale(
            ModificateurDroitDonneeFinanciere droit,
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if (bienImmobilierHabitationNonPrincipale == null) {
            throw new DroitException("Unable to delete BienImmobilierHabitationNonPrincipale, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            bienImmobilierHabitationNonPrincipale.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            bienImmobilierHabitationNonPrincipale = (BienImmobilierHabitationNonPrincipale) saveDonneeFinanciere(
                    droit.getSimpleVersionDroit(), bienImmobilierHabitationNonPrincipale,
                    PegasusImplServiceLocator.getBienImmobilierHabitationNonPrincipaleService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return bienImmobilierHabitationNonPrincipale;
    }

    @Override
    public BienImmobilierNonHabitable deleteBienImmobilierNonHabitable(ModificateurDroitDonneeFinanciere droit,
                                                                       BienImmobilierNonHabitable bienImmobilierNonHabitable) throws BienImmobilierNonHabitableException,
            JadePersistenceException, DroitException, DonneeFinanciereException {
        if (bienImmobilierNonHabitable == null) {
            throw new DroitException("Unable to delete bienImmobilierNonHabitable, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            bienImmobilierNonHabitable.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            bienImmobilierNonHabitable = (BienImmobilierNonHabitable) saveDonneeFinanciere(
                    droit.getSimpleVersionDroit(), bienImmobilierNonHabitable,
                    PegasusImplServiceLocator.getBienImmobilierNonHabitableService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return bienImmobilierNonHabitable;
    }

    @Override
    public BienImmobilierServantHabitationPrincipale deleteBienImmobilierServantHabitationPrincipale(
            ModificateurDroitDonneeFinanciere droit,
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if (bienImmobilierServantHabitationPrincipale == null) {
            throw new DroitException("Unable to delete BienImmobilierServantHabitationPrincipale, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            bienImmobilierServantHabitationPrincipale.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            bienImmobilierServantHabitationPrincipale = (BienImmobilierServantHabitationPrincipale) saveDonneeFinanciere(
                    droit.getSimpleVersionDroit(), bienImmobilierServantHabitationPrincipale,
                    PegasusImplServiceLocator.getBienImmobilierServantHabitationPrincipaleService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return bienImmobilierServantHabitationPrincipale;
    }

    @Override
    public CapitalLPP deleteCapitalLPP(ModificateurDroitDonneeFinanciere droit, CapitalLPP capitalLPP)
            throws CapitalLPPException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if (capitalLPP == null) {
            throw new DroitException("Unable to delete CapitalLPP, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            capitalLPP.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            capitalLPP = (CapitalLPP) saveDonneeFinanciere(droit.getSimpleVersionDroit(), capitalLPP,
                    PegasusImplServiceLocator.getCapitalLPPService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return capitalLPP;
    }

    @Override
    public CompteBancaireCCP deleteCompteBancaireCCP(ModificateurDroitDonneeFinanciere droit,
                                                     CompteBancaireCCP compteBancaireCCP) throws CompteBancaireCCPException, JadePersistenceException,
            DroitException, DonneeFinanciereException {
        if (compteBancaireCCP == null) {
            throw new DroitException("Unable to delete compteBancaireCCP, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            compteBancaireCCP.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            compteBancaireCCP = (CompteBancaireCCP) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    compteBancaireCCP, PegasusImplServiceLocator.getCompteBancaireCCPService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return compteBancaireCCP;
    }

    @Override
    public ContratEntretienViager deleteContratEntretienViager(ModificateurDroitDonneeFinanciere droit,
                                                               ContratEntretienViager contratEntretienViager) throws ContratEntretienViagerException,
            JadePersistenceException, DroitException, DonneeFinanciereException {
        if (contratEntretienViager == null) {
            throw new DroitException("Unable to delete allocationsFamiliales, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            contratEntretienViager.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            contratEntretienViager = (ContratEntretienViager) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    contratEntretienViager, PegasusImplServiceLocator.getContratEntretienViagerService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return contratEntretienViager;
    }

    @Override
    public CotisationsPsal deleteCotisationsPsal(ModificateurDroitDonneeFinanciere droit,
                                                 CotisationsPsal cotisationsPsal) throws CotisationsPsalException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if (cotisationsPsal == null) {
            throw new DroitException("Unable to delete CotisationsPsal, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            cotisationsPsal.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            cotisationsPsal = (CotisationsPsal) saveDonneeFinanciere(droit.getSimpleVersionDroit(), cotisationsPsal,
                    PegasusImplServiceLocator.getCotisationsPsalService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return cotisationsPsal;
    }

    @Override
    public DessaisissementFortune deleteDessaisissementFortune(ModificateurDroitDonneeFinanciere droit,
                                                               DroitMembreFamille droitMembreFamille, DessaisissementFortune dessaisissementFortune)
            throws DroitException, JadePersistenceException {
        if (dessaisissementFortune == null) {
            throw new DroitException("Unable to delete dessaisissement fortune, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {

            dessaisissementFortune.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            dessaisissementFortune = (DessaisissementFortune) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    dessaisissementFortune, PegasusImplServiceLocator.getDessaisissementFortuneService());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return dessaisissementFortune;
    }

    @Override
    public DessaisissementRevenu deleteDessaisissementRevenu(ModificateurDroitDonneeFinanciere droit,
                                                             DroitMembreFamille droitMembreFamille, DessaisissementRevenu dessaisissementRevenu) throws DroitException,
            JadePersistenceException {
        if (dessaisissementRevenu == null) {
            throw new DroitException("Unable to delete dessaisissement revenu, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {

            dessaisissementRevenu.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            dessaisissementRevenu = (DessaisissementRevenu) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    dessaisissementRevenu, PegasusImplServiceLocator.getDessaisissementRevenuService());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return dessaisissementRevenu;
    }

    @Override
    public IjApg deleteIjApg(ModificateurDroitDonneeFinanciere droit, IjApg ijApg) throws IjApgException,
            JadePersistenceException, DroitException, DonneeFinanciereException {
        if (ijApg == null) {
            throw new DroitException("Unable to delete ijApg, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            ijApg.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            ijApg = (IjApg) saveDonneeFinanciere(droit.getSimpleVersionDroit(), ijApg,
                    PegasusImplServiceLocator.getIjApgService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return ijApg;
    }

    @Override
    public IndemniteJournaliereAi deleteIndemniteJournaliereAi(ModificateurDroitDonneeFinanciere droit,
                                                               IndemniteJournaliereAi indemniteJournaliereAi) throws DroitException, JadePersistenceException,
            IndemniteJournaliereAiException {
        if (indemniteJournaliereAi == null) {
            throw new DroitException("Unable to delete indemniteJournaliereAi, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        // on set la rente supprimé
        try {
            indemniteJournaliereAi.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            indemniteJournaliereAi = (IndemniteJournaliereAi) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    indemniteJournaliereAi, PegasusImplServiceLocator.getIndemniteJournaliereAiService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return indemniteJournaliereAi;
    }

    @Override
    public Loyer deleteLoyer(ModificateurDroitDonneeFinanciere droit, Loyer loyer) throws DroitException,
            JadePersistenceException {
        if (loyer == null) {
            throw new DroitException("Unable to delete loyer, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            loyer.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            loyer = (Loyer) saveDonneeFinanciere(droit.getSimpleVersionDroit(), loyer,
                    PegasusImplServiceLocator.getLoyerService());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return loyer;
    }

    @Override
    public MarchandisesStock deleteMarchandisesStock(ModificateurDroitDonneeFinanciere droit,
                                                     MarchandisesStock marchandisesStock) throws MarchandisesStockException, JadePersistenceException,
            DroitException, DonneeFinanciereException {
        if (marchandisesStock == null) {
            throw new DroitException("Unable to delete marchandises/stock, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {

            marchandisesStock.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            marchandisesStock = (MarchandisesStock) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    marchandisesStock, PegasusImplServiceLocator.getMarchandisesStockService());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return marchandisesStock;
    }

    @Override
    public Numeraire deleteNumeraire(ModificateurDroitDonneeFinanciere droit, Numeraire numeraire)
            throws DroitException, JadePersistenceException, NumeraireException {
        if (numeraire == null) {
            throw new DroitException("Unable to delete numeraire, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            numeraire.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            numeraire = (Numeraire) saveDonneeFinanciere(droit.getSimpleVersionDroit(), numeraire,
                    PegasusImplServiceLocator.getNumeraireService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return numeraire;
    }

    @Override
    public PensionAlimentaire deletePensionAlimentaire(ModificateurDroitDonneeFinanciere droit,
                                                       PensionAlimentaire pensionAlimentaire) throws PensionAlimentaireException, JadePersistenceException,
            DroitException, DonneeFinanciereException {
        if (pensionAlimentaire == null) {
            throw new DroitException("Unable to delete pensionAlimentaire, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            pensionAlimentaire.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            pensionAlimentaire = (PensionAlimentaire) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    pensionAlimentaire, PegasusImplServiceLocator.getPensionAlimentaireService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return pensionAlimentaire;
    }

    @Override
    public PretEnversTiers deletePretEnversTiers(ModificateurDroitDonneeFinanciere droit,
                                                 PretEnversTiers pretEnversTiers) throws PretEnversTiersException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if (pretEnversTiers == null) {
            throw new DroitException("Unable to delete pret envers tiers, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {

            pretEnversTiers.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            pretEnversTiers = (PretEnversTiers) saveDonneeFinanciere(droit.getSimpleVersionDroit(), pretEnversTiers,
                    PegasusImplServiceLocator.getPretEnversTiersService());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return pretEnversTiers;
    }

    @Override
    public ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi deleteRenteAvsAi(
            ModificateurDroitDonneeFinanciere droit, ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi renteAvsAi)
            throws DroitException, JadePersistenceException, RenteAvsAiException {
        if (renteAvsAi == null) {
            throw new DroitException("Unable to deleterente avs ai, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        // on set la rente supprimé
        try {
            renteAvsAi.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            renteAvsAi = (RenteAvsAi) saveDonneeFinanciere(droit.getSimpleVersionDroit(), renteAvsAi,
                    PegasusImplServiceLocator.getRenteAvsAiService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return renteAvsAi;

    }

    @Override
    public RevenuActiviteLucrativeDependante deleteRevenuActiviteLucrativeDependante(
            ModificateurDroitDonneeFinanciere droit, RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if (revenuActiviteLucrativeDependante == null) {
            throw new DroitException("Unable to delete revenuActiviteLucrativeDependante, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            revenuActiviteLucrativeDependante.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            revenuActiviteLucrativeDependante = (RevenuActiviteLucrativeDependante) saveDonneeFinanciere(
                    droit.getSimpleVersionDroit(), revenuActiviteLucrativeDependante,
                    PegasusImplServiceLocator.getRevenuActiviteLucrativeDependanteService());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return revenuActiviteLucrativeDependante;
    }

    @Override
    public RevenuActiviteLucrativeIndependante deleteRevenuActiviteLucrativeIndependante(
            ModificateurDroitDonneeFinanciere droit,
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if (revenuActiviteLucrativeIndependante == null) {
            throw new DroitException("Unable to delete revenuActiviteLucrativeIndependante, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            revenuActiviteLucrativeIndependante.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            revenuActiviteLucrativeIndependante = (RevenuActiviteLucrativeIndependante) saveDonneeFinanciere(
                    droit.getSimpleVersionDroit(), revenuActiviteLucrativeIndependante,
                    PegasusImplServiceLocator.getRevenuActiviteLucrativeIndependanteService());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return revenuActiviteLucrativeIndependante;
    }

    @Override
    public RevenuHypothetique deleteRevenuHypothetique(ModificateurDroitDonneeFinanciere droit,
                                                       RevenuHypothetique revenuHypothetique) throws RevenuHypothetiqueException, JadePersistenceException,
            DroitException, DonneeFinanciereException {
        if (revenuHypothetique == null) {
            throw new DroitException("Unable to delete revenuHypothetique, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            revenuHypothetique.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            revenuHypothetique = (RevenuHypothetique) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    revenuHypothetique, PegasusImplServiceLocator.getRevenuHypothetiqueService());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return revenuHypothetique;
    }

    @Override
    public SimpleLibelleContratEntretienViager deleteSimpleLibelleContratEntretienViager(
            ModificateurDroitDonneeFinanciere droit,
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if (simpleLibelleContratEntretienViager == null) {
            throw new DroitException("Unable to delete assurance rente viagere, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            simpleLibelleContratEntretienViager = PegasusImplServiceLocator
                    .getSimpleLibelleContratEntretienViagerService().delete(simpleLibelleContratEntretienViager);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return simpleLibelleContratEntretienViager;
    }

    @Override
    public SimpleTypeFraisObtentionRevenu deleteSimpleTypeFraisObtentionRevenu(ModificateurDroitDonneeFinanciere droit,
                                                                               SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws SimpleTypeFraisObtentionRevenuException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if (simpleTypeFraisObtentionRevenu == null) {
            throw new DroitException("Unable to delete assurance rente viagere, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            simpleTypeFraisObtentionRevenu = PegasusImplServiceLocator.getSimpleTypeFraisObtentionRevenuService()
                    .delete(simpleTypeFraisObtentionRevenu);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return simpleTypeFraisObtentionRevenu;
    }

    @Override
    public TaxeJournaliereHome deleteTaxeJournaliereHome(ModificateurDroitDonneeFinanciere droit,
                                                         TaxeJournaliereHome taxeJournaliereHome) throws DroitException, JadePersistenceException {
        if (taxeJournaliereHome == null) {
            throw new DroitException("Unable to delete taxeJournaliereHome, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            taxeJournaliereHome.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            taxeJournaliereHome = (TaxeJournaliereHome) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    taxeJournaliereHome, PegasusImplServiceLocator.getTaxeJournaliereHomeService());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return taxeJournaliereHome;
    }

    @Override
    public SejourMoisPartielHome deleteSejourMoisPartielHome(ModificateurDroitDonneeFinanciere droit,
                                                             SejourMoisPartielHome sejourMoisPartielHome) throws DroitException, JadePersistenceException, DonneeFinanciereException, SejourMoisPartielHomeException {
        if (sejourMoisPartielHome == null) {
            throw new DroitException("Unable to delete sejourMoisPartielHome, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            sejourMoisPartielHome = PegasusImplServiceLocator.getSejourMoisPartielHomeService()
                    .delete(sejourMoisPartielHome);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return sejourMoisPartielHome;
    }

    @Override
    public Titre deleteTitre(ModificateurDroitDonneeFinanciere droit, Titre titre) throws TitreException,
            JadePersistenceException, DroitException, DonneeFinanciereException {
        if (titre == null) {
            throw new DroitException("Unable to delete Titre, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            titre.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            titre = (Titre) saveDonneeFinanciere(droit.getSimpleVersionDroit(), titre,
                    PegasusImplServiceLocator.getTitreService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return titre;
    }

    @Override
    public TypeFraisObtentionRevenu deleteTypeFraisObtentionRevenu(ModificateurDroitDonneeFinanciere droit,
                                                                   TypeFraisObtentionRevenu typeFraisObtentionRevenu) throws TypeFraisObtentionRevenuException,
            JadePersistenceException, DroitException, DonneeFinanciereException {
        if (typeFraisObtentionRevenu == null) {
            throw new DroitException("Unable to delete assurance rente viagere, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            typeFraisObtentionRevenu = PegasusImplServiceLocator.getTypeFraisObtentionRevenuService().delete(
                    typeFraisObtentionRevenu);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return typeFraisObtentionRevenu;
    }

    @Override
    public Vehicule deleteVehicule(ModificateurDroitDonneeFinanciere droit, Vehicule vehicule) throws DroitException,
            JadePersistenceException, VehiculeException {
        if (vehicule == null) {
            throw new DroitException("Unable to delete betail, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            vehicule.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            vehicule = (Vehicule) saveDonneeFinanciere(droit.getSimpleVersionDroit(), vehicule,
                    PegasusImplServiceLocator.getVehiculeService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return vehicule;
    }

    @Override
    public boolean existDroit(String idDemande) throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        return DroitChecker.existDroit(idDemande);
    }

    /**
     * Permet de trouver le droit a copier Il est possible que cette fonction ne retour pas de droit, Comme on fait un
     * filtre sur les dates.
     *
     * @param droit
     * @return
     * @throws DonneeFinanciereException
     */
    private Droit findOldDroit(Droit droit) throws DonneeFinanciereException {
        Droit oldDroit = null;
        DroitSearch droitSearch = new DroitSearch();
        droitSearch.setWhereKey(DroitSearch.OLD_DEMANDE);
        droitSearch.setForCsEtatDroitIn(Arrays.asList(IPCDemandes.CS_SUPPRIME, IPCDemandes.CS_ANNULE));
        droitSearch.setForIdDemandePc(droit.getSimpleDroit().getIdDemandePC());
        if (JadeStringUtil.isBlankOrZero(droit.getDemande().getSimpleDemande().getDateDebut())) {
            droitSearch.setForDateFinDemande(JadeDateUtil.convertDateMonthYear(droit.getDemande().getSimpleDemande()
                    .getDateDepot()));
        } else {
            droitSearch.setForDateFinDemande(droit.getDemande().getSimpleDemande().getDateDebut());
        }

        droitSearch.setForIdTiers(droit.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                .getTiers().getIdTiers());

        try {
            droitSearch = PegasusServiceLocator.getDroitService().searchDroit(droitSearch);

            if (droitSearch.getSize() > 0) {
                oldDroit = ((Droit) droitSearch.getSearchResults()[0]);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage(), e);
        } catch (JadePersistenceException e) {
            throw new DonneeFinanciereException("Unable to find the old demande (persistenc error)", e);
        } catch (DroitException e) {
            throw new DonneeFinanciereException("Unable to find the old demande", e);
        }
        return oldDroit;
    }

    @Override
    public Droit getCurrentVersionDroit(String idDroit) throws DroitException, JadePersistenceException {
        DroitSearch droitSearch = new DroitSearch();
        droitSearch.setForIdDroit(idDroit);
        droitSearch.setWhereKey(DroitSearch.CURRENT_VERSION);
        droitSearch = searchDroit(droitSearch);

        if (droitSearch.getSize() != 1) {
            throw new DroitException("Unable to find Current VersionDroit (id=" + idDroit + ")!");
        }

        return (Droit) droitSearch.getSearchResults()[0];
    }

    @Override
    public List<Droit> findCurrentVersionDroitByIdsDemande(List<String> idsDemande) throws DroitException,
            JadePersistenceException {

        DroitSearch droitSearch = new DroitSearch();
        droitSearch.setForIdsDemandeIn(idsDemande);
        droitSearch.setWhereKey(DroitSearch.CURRENT_VERSION);
        droitSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        droitSearch = searchDroit(droitSearch);

        return PersistenceUtil.typeSearch(droitSearch);
    }


    /**
     * Test si on peut synchroniser les membre de famille. Le droit doit étre en version initiale et il ne doit pas étre
     * dans l'état octroyer ou historiser, pour être syncroniser.
     *
     * @param droit
     * @return boolean
     */
    public boolean isDroitMembrefamilleSynchronisable(Droit droit) {
        return DroitChecker.isDroitMembrefamilleSynchronisableWithOutException(droit);
    }

    @Override
    public Droit processOnUpdateDonneFinanciere(Droit droit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (droit == null) {
            throw new DroitException("Unable to processOnUpdateDonneFinanciere , the droit passed is null!");
        }
        if (!IPCDroits.CS_AU_CALCUL.equals(droit.getSimpleVersionDroit().getCsEtatDroit())) {
            if (DroitChecker.isDeletable(droit.getSimpleVersionDroit())) {

                droit.getSimpleVersionDroit().setCsEtatDroit(IPCDroits.CS_AU_CALCUL);

                PegasusImplServiceLocator.getSimpleVersionDroitService().update(droit.getSimpleVersionDroit());

                try {
                    PegasusServiceLocator.getPCAccordeeService().deleteByIdVersionDroit(droit);
                } catch (PCAccordeeException e) {
                    throw new DroitException("Unable to delete the pcAccordee", e);
                } catch (JadeApplicationException e) {
                    throw new DroitException("Unable to delete the pcAccordee", e);
                }

            }
        }
        return droit;
    }

    @Override
    public AllocationImpotent readAllocationImpotent(String idAllocationImpotent) throws JadePersistenceException {
        AllocationImpotent allocationImpotent = new AllocationImpotent();
        allocationImpotent.setId(idAllocationImpotent);

        return (AllocationImpotent) JadePersistenceManager.read(allocationImpotent);

    }

    @Override
    public AllocationsFamiliales readAllocationsFamiliales(String id) throws JadePersistenceException, DroitException {
        AllocationsFamiliales allocationsFamiliales = new AllocationsFamiliales();
        allocationsFamiliales.setId(id);

        return (AllocationsFamiliales) JadePersistenceManager.read(allocationsFamiliales);
    }

    @Override
    public AssuranceRenteViagere readAssuranceRenteViagere(String id) throws JadePersistenceException {
        AssuranceRenteViagere assuranceRenteViagere = new AssuranceRenteViagere();
        assuranceRenteViagere.setId(id);

        return (AssuranceRenteViagere) JadePersistenceManager.read(assuranceRenteViagere);
    }

    @Override
    public AssuranceVie readAssuranceVie(String id) throws JadePersistenceException, DroitException {
        AssuranceVie assuranceVie = new AssuranceVie();
        assuranceVie.setId(id);

        return (AssuranceVie) JadePersistenceManager.read(assuranceVie);
    }

    @Override
    public AutreApi readAutreApi(String id) throws JadePersistenceException {
        AutreApi autreApi = new AutreApi();
        autreApi.setId(id);

        return (AutreApi) JadePersistenceManager.read(autreApi);
    }

    @Override
    public AutreFortuneMobiliere readAutreFortuneMobiliere(String id) throws JadePersistenceException {
        AutreFortuneMobiliere autreFortuneM = new AutreFortuneMobiliere();
        autreFortuneM.setId(id);

        return (AutreFortuneMobiliere) JadePersistenceManager.read(autreFortuneM);
    }

    @Override
    public AutreRente readAutreRente(String id) throws JadePersistenceException {
        AutreRente autreRente = new AutreRente();
        autreRente.setId(id);
        return (AutreRente) JadePersistenceManager.read(autreRente);
    }

    @Override
    public AutresDettesProuvees readAutresDettesProuvees(String id) throws JadePersistenceException, DroitException {
        AutresDettesProuvees autresDettesProuvees = new AutresDettesProuvees();
        autresDettesProuvees.setId(id);

        return (AutresDettesProuvees) JadePersistenceManager.read(autresDettesProuvees);
    }

    @Override
    public AutresRevenus readAutresRevenus(String id) throws JadePersistenceException, DroitException {
        AutresRevenus autresRevenus = new AutresRevenus();
        autresRevenus.setId(id);

        return (AutresRevenus) JadePersistenceManager.read(autresRevenus);

    }

    @Override
    public Betail readBetail(String id) throws JadePersistenceException {
        Betail betail = new Betail();
        betail.setId(id);

        return (Betail) JadePersistenceManager.read(betail);
    }

    @Override
    public BienImmobilierHabitationNonPrincipale readBienImmobilierHabitationNonPrincipale(String id)
            throws JadePersistenceException, DroitException {
        BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale = new BienImmobilierHabitationNonPrincipale();
        bienImmobilierHabitationNonPrincipale.setId(id);

        return (BienImmobilierHabitationNonPrincipale) JadePersistenceManager
                .read(bienImmobilierHabitationNonPrincipale);
    }

    @Override
    public BienImmobilierNonHabitable readBienImmobilierNonHabitable(String id) throws JadePersistenceException,
            DroitException {
        BienImmobilierNonHabitable bienImmobilierNonHabitable = new BienImmobilierNonHabitable();
        bienImmobilierNonHabitable.setId(id);

        return (BienImmobilierNonHabitable) JadePersistenceManager.read(bienImmobilierNonHabitable);
    }

    @Override
    public BienImmobilierServantHabitationPrincipale readBienImmobilierServantHabitationPrincipale(String id)
            throws JadePersistenceException, DroitException {
        BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale = new BienImmobilierServantHabitationPrincipale();
        bienImmobilierServantHabitationPrincipale.setId(id);

        return (BienImmobilierServantHabitationPrincipale) JadePersistenceManager
                .read(bienImmobilierServantHabitationPrincipale);
    }

    @Override
    public CapitalLPP readCapitalLPP(String id) throws JadePersistenceException, DroitException {
        CapitalLPP capitalLPP = new CapitalLPP();
        capitalLPP.setId(id);

        return (CapitalLPP) JadePersistenceManager.read(capitalLPP);
    }

    @Override
    public CompteBancaireCCP readCompteBancaireCCP(String id) throws JadePersistenceException, DroitException {
        CompteBancaireCCP compteBancaireCCP = new CompteBancaireCCP();
        compteBancaireCCP.setId(id);

        return (CompteBancaireCCP) JadePersistenceManager.read(compteBancaireCCP);
    }

    @Override
    public ContratEntretienViager readContratEntretienViager(String id) throws JadePersistenceException, DroitException {
        ContratEntretienViager contratEntretienViager = new ContratEntretienViager();
        contratEntretienViager.setId(id);

        return (ContratEntretienViager) JadePersistenceManager.read(contratEntretienViager);
    }

    @Override
    public CotisationsPsal readCotisationsPsal(String id) throws JadePersistenceException, DroitException {
        CotisationsPsal cotisationsPsal = new CotisationsPsal();
        cotisationsPsal.setId(id);

        return (CotisationsPsal) JadePersistenceManager.read(cotisationsPsal);
    }

    @Override
    public VersionDroit readCurrentVersionDroit(String idDroit) throws DroitException, JadePersistenceException {

        if (idDroit == null) {
            throw new DroitException("Unable to readCurrentVersionDroit the idDroit l passed is null!");
        }

        VersionDroitSearch search = new VersionDroitSearch();
        search.setForIdDroit(idDroit);
        search.setWhereKey(VersionDroitSearch.CURRENT_VERSION);
        search = searchVersionDroit(search);

        if (search.getSize() == 0) {
            throw new DroitException("No current 'droit' was found");
        }

        if (search.getSize() > 1) {
            throw new DroitException("Too much current 'droit' were found");
        }

        return (VersionDroit) search.getSearchResults()[0];
    }

    @Override
    public DessaisissementFortune readDessaisissementFortune(String id) throws JadePersistenceException {
        DessaisissementFortune desFor = new DessaisissementFortune();
        desFor.setId(id);

        return (DessaisissementFortune) JadePersistenceManager.read(desFor);
    }

    @Override
    public DessaisissementRevenu readDessaisissementRevenu(String id) throws JadePersistenceException {
        DessaisissementRevenu desRev = new DessaisissementRevenu();
        desRev.setId(id);

        return (DessaisissementRevenu) JadePersistenceManager.read(desRev);
    }

    @Override
    public DonneesPersonnelles readDonneesPersonnelles(String id) throws DonneesPersonnellesException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return PegasusImplServiceLocator.getDonneesPersonnellesService().read(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.droit.DroitService#readDroit (java.lang.String)
     */
    @Override
    public Droit readDroit(String idDroit) throws JadePersistenceException, DroitException {
        if (JadeStringUtil.isEmpty(idDroit)) {
            throw new DroitException("Unable to read dossier, the id passed is null!");
        }
        Droit droit = new Droit();
        droit.setId(idDroit);
        return (Droit) JadePersistenceManager.read(droit);
    }

    @Override
    public ModificateurDroitDonneeFinanciere readDroitDonneeFinanciere(String idDroit) throws DroitException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idDroit)) {
            throw new DroitException("Unable to read droit, the id passed is not defined!");
        }
        ModificateurDroitDonneeFinanciere droit = new ModificateurDroitDonneeFinanciere();
        droit.setId(idDroit);
        return (ModificateurDroitDonneeFinanciere) JadePersistenceManager.read(droit);
    }

    @Override
    public Droit readDroitFromVersion(String idVersionDroit) throws DroitException, JadePersistenceException {
        DroitSearch droitSearch = new DroitSearch();
        droitSearch.setForIdVersionDroit(idVersionDroit);
        droitSearch.setDefinedSearchSize(1);
        droitSearch = searchDroit(droitSearch);
        if (droitSearch.getSize() != 1) {
            throw new DroitException("Unable to find VersionDroit (id=" + idVersionDroit + ")!");
        }
        return (Droit) droitSearch.getSearchResults()[0];
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.droit.DroitService# readDroitMembreFamille(java.lang.String)
     */
    @Override
    public DroitMembreFamille readDroitMembreFamille(String idDroitMembreFamille) throws DroitException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idDroitMembreFamille)) {
            throw new DroitException("Unable to search droitMembreFamille, the id passed is null");
        }
        DroitMembreFamille result = new DroitMembreFamille();
        DroitMembreFamilleSearch search = new DroitMembreFamilleSearch();
        search.setForIdDroitMembreFamille(idDroitMembreFamille);
        search.setDefinedSearchSize(1);
        try {
            search = PegasusImplServiceLocator.getDroitMembreFamilleService().search(search);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        if (search.getSize() > 0) {
            result = (DroitMembreFamille) search.getSearchResults()[0];
        }
        return result;
    }

    @Override
    public IjApg readIjApg(String id) throws JadePersistenceException, DroitException {
        IjApg ijApg = new IjApg();
        ijApg.setId(id);

        return (IjApg) JadePersistenceManager.read(ijApg);
    }

    @Override
    public IndemniteJournaliereAi readIndemniteJournaliereAi(String id) throws JadePersistenceException {
        IndemniteJournaliereAi indemniteJournaliereAi = new IndemniteJournaliereAi();
        indemniteJournaliereAi.setId(id);
        return (IndemniteJournaliereAi) JadePersistenceManager.read(indemniteJournaliereAi);
    }

    @Override
    public Loyer readLoyer(String idLoyer) throws JadePersistenceException {
        Loyer loyer = new Loyer();
        loyer.setId(idLoyer);

        return (Loyer) JadePersistenceManager.read(loyer);
    }

    @Override
    public MarchandisesStock readMarchandisesStock(String id) throws JadePersistenceException, DroitException {
        AbstractDonneeFinanciereModel marchandisesStock = new MarchandisesStock();
        marchandisesStock.setId(id);

        return (MarchandisesStock) JadePersistenceManager.read(marchandisesStock);
    }

    @Override
    public Numeraire readNumeraire(String id) throws JadePersistenceException {
        Numeraire numeraire = new Numeraire();
        numeraire.setId(id);

        return (Numeraire) JadePersistenceManager.read(numeraire);
    }

    @Override
    public PensionAlimentaire readPensionAlimentaire(String id) throws JadePersistenceException, DroitException {
        PensionAlimentaire pensionAlimentaire = new PensionAlimentaire();
        pensionAlimentaire.setId(id);

        return (PensionAlimentaire) JadePersistenceManager.read(pensionAlimentaire);
    }

    @Override
    public PretEnversTiers readPretEnversTiers(String id) throws JadePersistenceException, DroitException {
        PretEnversTiers pretEnversTiers = new PretEnversTiers();
        pretEnversTiers.setId(id);

        return (PretEnversTiers) JadePersistenceManager.read(pretEnversTiers);
    }

    @Override
    public ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi readRenteAvsAi(String idRenteAvsAi)
            throws JadePersistenceException {
        RenteAvsAi renteAvsAi = new RenteAvsAi();
        renteAvsAi.setId(idRenteAvsAi);

        return (RenteAvsAi) JadePersistenceManager.read(renteAvsAi);
    }

    @Override
    public RevenuActiviteLucrativeDependante readRevenuActiviteLucrativeDependante(String id)
            throws JadePersistenceException, DroitException {
        RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante = new RevenuActiviteLucrativeDependante();
        revenuActiviteLucrativeDependante.setId(id);

        return (RevenuActiviteLucrativeDependante) JadePersistenceManager.read(revenuActiviteLucrativeDependante);
    }

    @Override
    public RevenuActiviteLucrativeIndependante readRevenuActiviteLucrativeIndependante(String id)
            throws JadePersistenceException, DroitException {
        RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante = new RevenuActiviteLucrativeIndependante();
        revenuActiviteLucrativeIndependante.setId(id);

        return (RevenuActiviteLucrativeIndependante) JadePersistenceManager.read(revenuActiviteLucrativeIndependante);
    }

    @Override
    public RevenuHypothetique readRevenuHypothetique(String id) throws JadePersistenceException, DroitException {
        RevenuHypothetique readRevenuHypothetique = new RevenuHypothetique();
        readRevenuHypothetique.setId(id);

        return (RevenuHypothetique) JadePersistenceManager.read(readRevenuHypothetique);
    }

    @Override
    public SimpleTypeFraisObtentionRevenu readSimpleTypeFraisObtentionRevenu(String id) throws JadePersistenceException {
        SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu = new SimpleTypeFraisObtentionRevenu();
        simpleTypeFraisObtentionRevenu.setId(id);

        return (SimpleTypeFraisObtentionRevenu) JadePersistenceManager.read(simpleTypeFraisObtentionRevenu);
    }

    @Override
    public TaxeJournaliereHome readTaxeJournaliereHome(String idTaxeJournaliereHome) throws JadePersistenceException {
        TaxeJournaliereHome taxeJournaliereHome = new TaxeJournaliereHome();
        taxeJournaliereHome.setId(idTaxeJournaliereHome);

        return (TaxeJournaliereHome) JadePersistenceManager.read(taxeJournaliereHome);
    }

    @Override
    public SejourMoisPartielHome readSejourMoisPartielHome(String idSejourMoisPartielHome) throws JadePersistenceException {
        SejourMoisPartielHome sejourMoisPartielHome = new SejourMoisPartielHome();
        sejourMoisPartielHome.setId(idSejourMoisPartielHome);

        return (SejourMoisPartielHome) JadePersistenceManager.read(sejourMoisPartielHome);
    }

    @Override
    public Titre readTitre(String id) throws JadePersistenceException, DroitException {
        Titre titre = new Titre();
        titre.setId(id);

        return (Titre) JadePersistenceManager.read(titre);
    }

    @Override
    public TypeFraisObtentionRevenu readTypeFraisObtentionRevenu(String id) throws JadePersistenceException {
        TypeFraisObtentionRevenu typeFraisObtentionRevenu = new TypeFraisObtentionRevenu();
        typeFraisObtentionRevenu.setId(id);

        return (TypeFraisObtentionRevenu) JadePersistenceManager.read(typeFraisObtentionRevenu);
    }

    @Override
    public Vehicule readVehicule(String id) throws JadePersistenceException {
        Vehicule vehicule = new Vehicule();
        vehicule.setId(id);

        return (Vehicule) JadePersistenceManager.read(vehicule);
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.pegasus.business.services.models.droit.DroitService# readVersionDroit(java.lang.String)
     */
    @Override
    public VersionDroit readVersionDroit(String idVersionDroit) throws JadePersistenceException, DroitException {
        if (JadeStringUtil.isEmpty(idVersionDroit)) {
            throw new DroitException("Unable to read versionDroit, the id passed is null!");
        }
        VersionDroit versionDroit = new VersionDroit();
        versionDroit.setId(idVersionDroit);
        return (VersionDroit) JadePersistenceManager.read(versionDroit);
    }

    private AbstractDonneeFinanciereModel saveDonneeFinanciere(SimpleVersionDroit versionDroit,
                                                               AbstractDonneeFinanciereModel donneeFinanciere, JadeApplicationService service) throws DroitException {
        Method method = null;
        try {
            donneeFinanciere.getSimpleDonneeFinanciereHeader().setIsCopieFromPreviousVersion(Boolean.FALSE);

            if (donneeFinanciere.getSimpleDonneeFinanciereHeader().getIdVersionDroit()
                    .equals(versionDroit.getIdVersionDroit())) {
                // Va rechercher la méthode update
                method = service.getClass().getMethod("update", new Class[]{donneeFinanciere.getClass()});
            } else {
                donneeFinanciere.setIsNew();
                donneeFinanciere.getSimpleDonneeFinanciereHeader().setIdVersionDroit(versionDroit.getIdVersionDroit());
                // Va rechercher la méthode create
                method = service.getClass().getMethod("create", new Class[]{donneeFinanciere.getClass()});
            }
            return (AbstractDonneeFinanciereModel) method.invoke(service, new Object[]{donneeFinanciere});
        } catch (Exception e) {
            throw new DroitException("Unable to save donnee financiere !", e);
        }
    }

    /**
     * Point d'entrée publique pour la creation des données fincières devant être mise à jour pour une version de droit
     *
     * @param versionDroit
     * @param donneeFinanciere
     * @param service
     * @throws DroitException
     */
    @Override
    public void saveDonneefinanciereCalculMoisSuivant(AbstractDonneeFinanciereModel donneeFinanciere,
                                                      JadeApplicationService service) throws DroitException {
        Method method = null;

        // Va rechercher la méthode create, et invocation
        try {
            method = service.getClass().getMethod("create", new Class[]{donneeFinanciere.getClass()});
            method.invoke(service, new Object[]{donneeFinanciere});

        } catch (Exception e) {
            throw new DroitException(
                    "Unable to save donnee financiere for updating DonneeFinanciere in the CalculMoisSuivant case!", e);
        }

    }

    @Override
    public TaxeJournaliereHomeDroitSearch search(TaxeJournaliereHomeDroitSearch search)
            throws TaxeJournaliereHomeException, JadePersistenceException {
        if (search == null) {
            throw new TaxeJournaliereHomeException(
                    "Unable to search TaxeJournaliereHomeDroit, the model passed is null!");
        }
        return (TaxeJournaliereHomeDroitSearch) JadePersistenceManager.search(search);
    }

    @Override
    public AllocationImpotentSearch searchAllocationImpotent(AllocationImpotentSearch searchModel)
            throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search Allocation Impotent, the serach model passed is null!");
        }
        return (AllocationImpotentSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public AllocationsFamilialesSearch searchAllocationsFamiliales(AllocationsFamilialesSearch searchModel)
            throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search AllocationsFamiliales, the search model passed is null!");
        }
        return (AllocationsFamilialesSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public AssuranceRenteViagereSearch searchAssuranceRenteViagere(AssuranceRenteViagereSearch searchModel)
            throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search assurance rente viagere, the search model passed is null!");
        }
        return (AssuranceRenteViagereSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public AssuranceVieSearch searchAssuranceVie(AssuranceVieSearch searchModel) throws DroitException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search AssuranceVie, the search model passed is null!");
        }
        return (AssuranceVieSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public AutreApiSearch searchAutreApi(AutreApiSearch searchModel) throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search autreApi, the search model passed is null!");
        }
        return (AutreApiSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public AutreFortuneMobiliereSearch searchAutreFortuneMobiliere(AutreFortuneMobiliereSearch searchModel)
            throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search autre fortune mobiliere, the search model passed is null!");
        }
        return (AutreFortuneMobiliereSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public AutreRenteSearch searchAutreRente(AutreRenteSearch searchModel) throws JadePersistenceException,
            DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search autreRente, the search model passed is null!");
        }
        return (AutreRenteSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public AutresDettesProuveesSearch searchAutresDettesProuvees(AutresDettesProuveesSearch searchModel)
            throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search AutresDettesProuveesSearch, the search model passed is null!");
        }
        return (AutresDettesProuveesSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public AutresRevenusSearch searchAutresRevenus(AutresRevenusSearch searchModel) throws DroitException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search AutresRevenus, the search model passed is null!");
        }
        return (AutresRevenusSearch) JadePersistenceManager.search(searchModel);

    }

    @Override
    public BetailSearch searchBetail(BetailSearch searchModel) throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search betail, the search model passed is null!");
        }
        return (BetailSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public BienImmobilierHabitationNonPrincipaleSearch searchBienImmobilierHabitationNonPrincipale(
            BienImmobilierHabitationNonPrincipaleSearch searchModel) throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException(
                    "Unable to search BienImmobilierHabitationNonPrincipale, the search model passed is null!");
        }
        return (BienImmobilierHabitationNonPrincipaleSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public BienImmobilierNonHabitableSearch searchBienImmobilierNonHabitable(
            BienImmobilierNonHabitableSearch searchModel) throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search BienImmobilierNonHabitable, the search model passed is null!");
        }
        return (BienImmobilierNonHabitableSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public BienImmobilierServantHabitationPrincipaleSearch searchBienImmobilierServantHabitationPrincipale(
            BienImmobilierServantHabitationPrincipaleSearch searchModel) throws DroitException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException(
                    "Unable to search BienImmobilierServantHabitationPrincipale, the search model passed is null!");
        }
        return (BienImmobilierServantHabitationPrincipaleSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public CapitalLPPSearch searchCapitalLPP(CapitalLPPSearch searchModel) throws DroitException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search CapitalLPP, the search model passed is null!");
        }
        return (CapitalLPPSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public CompteBancaireCCPSearch searchCompteBancaireCCP(CompteBancaireCCPSearch searchModel) throws DroitException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search CompteBancaireCCP, the search model passed is null!");
        }
        return (CompteBancaireCCPSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public ContratEntretienViagerSearch searchContratEntretienViager(ContratEntretienViagerSearch searchModel)
            throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search ContratEntretienViagerSearch, the search model passed is null!");
        }
        return (ContratEntretienViagerSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public CotisationsPsalSearch searchCotisationsPsal(CotisationsPsalSearch searchModel) throws DroitException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search CotisationsPsalSearch, the search model passed is null!");
        }
        return (CotisationsPsalSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public DessaisissementFortuneSearch searchDessaisissementFortune(DessaisissementFortuneSearch searchModel)
            throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search dessaisissementFortune, the search model passed is null!");
        }
        return (DessaisissementFortuneSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public DessaisissementFortuneAutoSearch searchDessaisissementFortuneAuto(
            DessaisissementFortuneAutoSearch searchModel) throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search dessaisissementFortuneAuto, the search model passed is null!");
        }
        return (DessaisissementFortuneAutoSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public DessaisissementRevenuSearch searchDessaisissementRevenu(DessaisissementRevenuSearch searchModel)
            throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search darchDessaisissementRevenu, the search model passed is null!");
        }
        return (DessaisissementRevenuSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public DessaisissementRevenuAutoSearch searchDessaisissementRevenuAuto(DessaisissementRevenuAutoSearch searchModel)
            throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException(
                    "Unable to search darchDessaisissementRevenuAuto, the search model passed is null!");
        }
        return (DessaisissementRevenuAutoSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public DonneesPersonnellesSearch searchDonneesPersonnelles(DonneesPersonnellesSearch search) throws DroitException,
            JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to search DonneesPersonnellesSearch, the search model passed is null!");
        }
        return (DonneesPersonnellesSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.droit.DroitService#searchDroit
     * (ch.globaz.pegasus.business.models.droit.DroitSearch)
     */
    @Override
    public DroitSearch searchDroit(DroitSearch droitSearch) throws JadePersistenceException, DroitException {
        if (droitSearch == null) {
            throw new DroitException("Unable to search droit, the search model passed is null!");
        }
        return (DroitSearch) JadePersistenceManager.search(droitSearch);
    }

    public Droit searchDroitByIdVersionDroit(String idVersionDroit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DroitSearch droitSearch = new DroitSearch();
        droitSearch.setForIdVersionDroit(idVersionDroit);
        Droit droit = null;
        if (droitSearch.getSize() > 0) {
            if (droitSearch.getSize() == 1) {
                droit = (Droit) PegasusServiceLocator.getDroitService().searchDroit(droitSearch).getSearchResults()[0];
            } else {
                throw new DroitException("Unable to find the droit by version droit too many values");
            }
        }
        return droit;
    }

    @Override
    public ModificateurDroitDonneeFinanciereSearch searchDroitDonneeFinanciere(
            ModificateurDroitDonneeFinanciereSearch search) throws DroitException, JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to search droit, the search model passed is null!");
        }
        return (ModificateurDroitDonneeFinanciereSearch) JadePersistenceManager.search(search);
    }

    @Override
    public DroitMembreFamilleSearch searchDroitMembreFamille(DroitMembreFamilleSearch membreSearch)
            throws JadePersistenceException, DroitException {
        if (membreSearch == null) {
            throw new DroitException("Unable to search dossier, the search model passed is null!");
        }

        return (DroitMembreFamilleSearch) JadePersistenceManager.search(membreSearch);
    }

    @Override
    public DroitMembreFamilleEtenduSearch searchDroitMemebreFamilleEtendu(
            DroitMembreFamilleEtenduSearch droitMembreFamilleEtenduSearch) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return PegasusImplServiceLocator.getDroitMembreFamilleEtenduService().search(droitMembreFamilleEtenduSearch);
    }

    @Override
    public FortuneParticuliereSearch searchFortuneParticuliere(FortuneParticuliereSearch search) throws DroitException,
            JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to search fortune particuliere, the search model passed is null!");
        }
        return (FortuneParticuliereSearch) JadePersistenceManager.search(search);
    }

    @Override
    public FortuneUsuelleSearch searchFortuneUsuelle(FortuneUsuelleSearch search) throws DroitException,
            JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to search fortune usuelle, the search model passed is null!");
        }
        return (FortuneUsuelleSearch) JadePersistenceManager.search(search);

    }

    @Override
    public HabitatSearch searchHabitat(HabitatSearch searchModel) throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search  searchHabitat, the search model passed is null!");
        }
        return (HabitatSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public IjApgSearch searchIjApg(IjApgSearch searchModel) throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search ijApg, the search model passed is null!");
        }
        return (IjApgSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public IndemniteJournaliereAiSearch searchIndemniteJournaliereAi(IndemniteJournaliereAiSearch searchModel)
            throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search indemniteJournaliereAi, the search model passed is null!");
        }
        return (IndemniteJournaliereAiSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public LoyerSearch searchLoyer(AbstractDonneeFinanciereSearchModel searchModel) throws JadePersistenceException,
            DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search loyer, the search model passed is null!");
        }
        return (LoyerSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public MarchandisesStockSearch searchMarchandisesStock(MarchandisesStockSearch searchModel) throws DroitException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search marchandises/stock, the search model passed is null!");
        }
        return (MarchandisesStockSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public MembreFamilleEtenduSearch searchMembreFamilleEtendu(MembreFamilleEtenduSearch membreSearch)
            throws JadePersistenceException, DroitException {
        if (membreSearch == null) {
            throw new DroitException("Unable to search dossier, the search model passed is null!");
        }

        return (MembreFamilleEtenduSearch) JadePersistenceManager.search(membreSearch);
    }

    @Override
    public NumeraireSearch searchNumeraire(NumeraireSearch searchModel) throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search pret envers tiers, the search model passed is null!");
        }
        return (NumeraireSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public PensionAlimentaireSearch searchPensionAlimentaire(PensionAlimentaireSearch searchModel)
            throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search PensionAlimentaire, the search model passed is null!");
        }
        return (PensionAlimentaireSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public PretEnversTiersSearch searchPretEnversTiers(PretEnversTiersSearch searchModel) throws DroitException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search pret envers tiers, the search model passed is null!");
        }
        return (PretEnversTiersSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public RenteAvsAiSearch searchRenteAvsAi(RenteAvsAiSearch searchModel) throws JadePersistenceException,
            DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search activité RenteAvsAiSearch, the search model passed is null!");
        }
        return (RenteAvsAiSearch) JadePersistenceManager.search(searchModel);

    }

    @Override
    public RenteIjApiSearch searchRenteIjApi(RenteIjApiSearch searchModel) throws DroitException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search  RenteIjApiSearch, the search model passed is null!");
        }
        return (RenteIjApiSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public RevenuActiviteLucrativeDependanteSearch searchRevenuActiviteLucrativeDependante(
            RevenuActiviteLucrativeDependanteSearch searchModel) throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search activité lucrative dépendante, the search model passed is null!");
        }
        return (RevenuActiviteLucrativeDependanteSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public RevenuActiviteLucrativeIndependanteSearch searchRevenuActiviteLucrativeIndependante(
            RevenuActiviteLucrativeIndependanteSearch searchModel) throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException(
                    "Unable to search activité lucrative indépendante, the search model passed is null!");
        }
        return (RevenuActiviteLucrativeIndependanteSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public RevenuHypothetiqueSearch searchRevenuHypothetique(RevenuHypothetiqueSearch searchModel)
            throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search RevenuHypothetique, the search model passed is null!");
        }
        return (RevenuHypothetiqueSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public RevenusDepensesSearch searchRevenusDepenses(RevenusDepensesSearch search) throws DroitException,
            JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to search revenus/depenses, the search model passed is null!");
        }
        return (RevenusDepensesSearch) JadePersistenceManager.search(search);
    }

    @Override
    public SimpleLibelleContratEntretienViagerSearch searchSimpleLibelleContratEntretienViager(
            SimpleLibelleContratEntretienViagerSearch searchModel) throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException(
                    "Unable to search SimpleLibelleContratEntretienViager, the search model passed is null!");
        }
        return (SimpleLibelleContratEntretienViagerSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SimpleTypeFraisObtentionRevenuSearch searchSimpleTypeFraisObtentionRevenu(
            SimpleTypeFraisObtentionRevenuSearch searchModel) throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException(
                    "Unable to search SimpleTypeFraisObtentionRevenu, the search model passed is null!");
        }
        return (SimpleTypeFraisObtentionRevenuSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public TaxeJournaliereHomeSearch searchTaxeJournaliereHome(TaxeJournaliereHomeSearch searchModel)
            throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search loyer, the search model passed is null!");
        }
        return (TaxeJournaliereHomeSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SejourMoisPartielHomeSearch searchSejourMoisPartielHome(SejourMoisPartielHomeSearch searchModel)
            throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search sejourMoisPartiel, the search model passed is null!");
        }
        return (SejourMoisPartielHomeSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public TitreSearch searchTitre(TitreSearch searchModel) throws DroitException, JadePersistenceException {
        if (searchModel == null) {
            throw new DroitException("Unable to search Titre, the search model passed is null!");
        }
        return (TitreSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public VehiculeSearch searchVehicule(VehiculeSearch searchModel) throws JadePersistenceException, DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search betail, the search model passed is null!");
        }
        return (VehiculeSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public VersionDroitSearch searchVersionDroit(VersionDroitSearch search) throws DroitException,
            JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to search versionDroit, the search model passed is null!");
        }
        return (VersionDroitSearch) JadePersistenceManager.search(search);
    }

    @Override
    public Droit supprimerVersionDroit(Droit droit) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DroitException, DemandeException,
            DossierException {
        if (DroitChecker.isUpdatable(droit)) {
            // suppression des données financières
            PegasusImplServiceLocator.getDonneeFinanciereHeaderService().deleteDonneFinancierByIdVersionDroit(
                    droit.getSimpleVersionDroit().getIdVersionDroit());

            // suppression des pcAccodee et des calcule
            try {
                PegasusImplServiceLocator.getCalculPersistanceService().clearPCAccordee(droit);
            } catch (PCAccordeeException e) {
                throw new DroitException("Unable to delete PCAccordee", e);
            } catch (JadeApplicationException e) {
                throw new DroitException("Unable to delete PCAccordee", e);
            }

            // suppression de la version de droit
            PegasusImplServiceLocator.getSimpleVersionDroitService().delete(droit.getSimpleVersionDroit());

            if (!JadeStringUtil.isEmpty(droit.getDemande().getSimpleDemande().getDateFinInitial())) {
                droit.getDemande().getSimpleDemande().setDateFinInitial("");
                PegasusImplServiceLocator.getSimpleDemandeService().update(droit.getDemande().getSimpleDemande());
            }

            // suppression du droit si il y une seule version
            SimpleVersionDroitSearch search = new SimpleVersionDroitSearch();
            search.setForIdDroit(droit.getSimpleDroit().getIdDroit());
            search = PegasusImplServiceLocator.getSimpleVersionDroitService().search(search);
            if (PegasusImplServiceLocator.getSimpleVersionDroitService().count(search) == 0) {
                PegasusImplServiceLocator.getDroitMembreFamilleService().deleteByIdDroit(
                        droit.getSimpleDroit().getIdDroit());
                PegasusImplServiceLocator.getSimpleDroitService().delete(droit.getSimpleDroit());
            }
            PcaRetenueSearch pcaRetenueSearch = new PcaRetenueSearch();
            pcaRetenueSearch.setForIdDroit(droit.getId());
            int noVersion = Integer.parseInt(droit.getSimpleVersionDroit().getNoVersion());
            if (noVersion > 1) {
                noVersion--;
            }
            pcaRetenueSearch.setForNoVersion(String.valueOf(noVersion));
            pcaRetenueSearch = PegasusServiceLocator.getRetenueService().search(pcaRetenueSearch);
            String dateProchainPaiement = null;
            try {
                dateProchainPaiement = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
                for (JadeAbstractModel absDonnee : pcaRetenueSearch.getSearchResults()) {
                    PcaRetenue retenueAncienne = (PcaRetenue) absDonnee;
                    CreanceAccordeeSearch creancierSearch = new CreanceAccordeeSearch();
                    creancierSearch.setForIdPCAccordee(retenueAncienne.getIdPCAccordee());
                    creancierSearch.setForIsHome("true");
                    int nbreResult = PegasusServiceLocator.getCreanceAccordeeService().count(creancierSearch);
                    if (retenueAncienne.getSimpleRetenue().getDateFinRetenue().equals(dateProchainPaiement)) {
                        retenueAncienne.getSimpleRetenue().setDateFinRetenue("");
                        PegasusServiceLocator.getRetenueService().update(retenueAncienne);
                    }

                    if(nbreResult==0){
                        SimpleCreancierHystoriqueSearch simpleCreancierHystoriqueSearch = new SimpleCreancierHystoriqueSearch();
                        simpleCreancierHystoriqueSearch.setForIdPcAccordee(retenueAncienne.getIdPCAccordee());
                        simpleCreancierHystoriqueSearch = PegasusImplServiceLocator.getSimpleCreancierHystoriqueService().search(simpleCreancierHystoriqueSearch);
                        for(JadeAbstractModel model : simpleCreancierHystoriqueSearch.getSearchResults()){
                            SimpleCreancierHystorique simpleCreancierHystorique = (SimpleCreancierHystorique) model;
                            SimpleCreancier simpleCreancier = new SimpleCreancier();
                            simpleCreancier.setId(simpleCreancierHystorique.getIdCreancier());
                            simpleCreancier.setCsEtat(simpleCreancierHystorique.getCsEtat());
                            simpleCreancier.setCsTypeCreance(simpleCreancierHystorique.getCsTypeCreance());
                            simpleCreancier.setIdDemande(simpleCreancierHystorique.getIdDemande());
                            simpleCreancier.setIdDomaineApplicatif(simpleCreancierHystorique.getIdDomaineApplicatif());
                            simpleCreancier.setIdTiers(simpleCreancierHystorique.getIdTiers());
                            simpleCreancier.setIdTiersAdressePaiement(simpleCreancierHystorique.getIdTiersAdressePaiement());
                            simpleCreancier.setIdTiersRegroupement(simpleCreancierHystorique.getIdTiersRegroupement());
                            simpleCreancier.setMontant(simpleCreancierHystorique.getMontantCreancier());
                            simpleCreancier.setIsCalcule(false);
                            simpleCreancier.setIsHome(true);
                            simpleCreancier = PegasusImplServiceLocator.getSimpleCreancierService().create(simpleCreancier);
                            SimpleCreanceAccordee simpleCreanceAccordee = new SimpleCreanceAccordee();
                            simpleCreanceAccordee.setIdCreancier(simpleCreancier.getId());
                            simpleCreanceAccordee.setIdPCAccordee(simpleCreancierHystorique.getIdPCAccordee());
                            simpleCreanceAccordee.setMontant(simpleCreancierHystorique.getMontantCreancieAccordee());
                            PegasusImplServiceLocator.getSimpleCreanceAccordeeService().create(simpleCreanceAccordee);
                        }
                    }
                }
            } catch (PmtMensuelException e) {
                throw new DroitException("Unable to delete PCAccordee", e);
            } catch (JadeApplicationException e) {
                throw new DroitException("Unable to delete PCAccordee", e);
            }
        }


        return droit;
    }

    @Override
    public Droit supprimerVersionDroitAnnule(Droit droit) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DroitException {
        // suppression des données financières
        PegasusImplServiceLocator.getDonneeFinanciereHeaderService().deleteDonneFinancierByIdVersionDroit(
                droit.getSimpleVersionDroit().getIdVersionDroit());

        // suppression des pcAccodee et des calcule
        try {
            PegasusImplServiceLocator.getCalculPersistanceService().clearPCAccordee(droit);
        } catch (PCAccordeeException e) {
            throw new DroitException("Unable to delete PCAccordee", e);
        } catch (JadeApplicationException e) {
            throw new DroitException("Unable to delete PCAccordee", e);
        }

        // suppression de la version de droit
        PegasusImplServiceLocator.getSimpleVersionDroitService().delete(droit.getSimpleVersionDroit());

        // suppression du droit si il y une seule version
        SimpleVersionDroitSearch search = new SimpleVersionDroitSearch();
        search.setForIdDroit(droit.getSimpleDroit().getIdDroit());
        if (PegasusImplServiceLocator.getSimpleVersionDroitService().count(search) == 0) {
            PegasusImplServiceLocator.getDroitMembreFamilleService().deleteByIdDroit(
                    droit.getSimpleDroit().getIdDroit());
            PegasusImplServiceLocator.getSimpleDroitService().delete(droit.getSimpleDroit());
        }

        return droit;
    }

    @Override
    public Droit synchroniseMembresFamille(Droit droit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, MembreFamilleException,
            DonneesPersonnellesException {
        return this.synchroniseMembresFamille(droit, true);

    }

    @Override
    public MembresFamillesToSynchronise resolveEnfantToSynchroniseByIdDemande(String idDemande)
            throws DonneesPersonnellesException, MembreFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DroitException {

        MembresFamillesToSynchronise membreFamille = new SynchronisationMembreFamille()
                .resolveEnfantToSynchronise(idDemande);

        BSession session = BSessionUtil.getSessionFromThreadContext();

        for (MembreFamilleVO mb : membreFamille.getToAdd()) {
            mb.setCsSexe(PCUserHelper.getLibelleCourtSexe(mb.getCsSexe()));
            mb.setCsNationalite(session.getCodeLibelle(session.getSystemCode("CIPAYORI", mb.getCsNationalite())));
        }

        return membreFamille;
    }

    @Override
    public List<SimpleDroitMembreFamille> addMembreFamilleByIdMembreFamille(MembreFamilleToSync membreFamilleToSync)
            throws DonneesPersonnellesException, MembreFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DroitException {
        return new SynchronisationMembreFamille().addMembreFamilleByIdMembreFamille(
                membreFamilleToSync.getIdsMembreFamille(), membreFamilleToSync.getIdDemande());
    }

    public Droit synchroniseMembresFamille(Droit droit, boolean withProecessOnUpdate) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, MembreFamilleException,
            DonneesPersonnellesException {

        if (droit == null) {
            throw new DonneesPersonnellesException(
                    "Unable to synchroniseMembresFamille droit, the model passed is null!");
        }
        DroitChecker.checkIsDroitMembrefamilleSynchronisable(droit);
        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            String idTiersRequerant = droit.getDemande().getDossier().getDemandePrestation().getDemandePrestation()
                    .getIdTiers();
            // Conversion dernier jour du mois de la date de dépot de la demande, afin d'aller rechercher tous les
            // membres de familles inclus DANS le mois
            String dateRechecheMembre = JadeDateUtil.getLastDateOfMonth(droit.getDemande().getSimpleDemande()
                    .getDateDepot());

            MembreFamilleVO[] searchMembresFamilleDisponibles = HeraServiceLocator.getMembreFamilleService()
                    .searchMembresFamilleRequerantDomaineRentes(idTiersRequerant, dateRechecheMembre);

            MembreFamilleVO[] mfDisponibles = HeraServiceLocator.getMembreFamilleService().filtreMembreFamilleWithDate(
                    searchMembresFamilleDisponibles, dateRechecheMembre);

            List<MembreFamilleVO> mfFiltre = new ArrayList<MembreFamilleVO>();

            for (MembreFamilleVO membreFamDispo : mfDisponibles) {
                // On filtre les enfant qui on plus de 25ans (25ans révolue).
                String dateMax = JadeDateUtil.addYears(membreFamDispo.getDateNaissance(), 25).substring(3);
                String dateDepot = droit.getDemande().getSimpleDemande().getDateDepot().substring(3);
                String csRoleFamillePC = DroitServiceImpl.convertCsRoleFamillePC(
                        membreFamDispo.getRelationAuRequerant(), membreFamDispo.getIdTiers().equals(idTiersRequerant));
                // on ne filtre pas le requérant et le conjoint. On ne filtre pas le requérant qui est une fratrie
                if (!IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(csRoleFamillePC)

                        || (droit.getDemande().getSimpleDemande().getIsFratrie() && idTiersRequerant
                        .equals(membreFamDispo.getIdTiers()))

                        || (JadeDateUtil.isDateMonthYearBefore(dateDepot, dateMax) || dateDepot.equals(dateMax))) {
                    mfFiltre.add(membreFamDispo);
                }
            }

            DroitMembreFamilleSearch searchModel = new DroitMembreFamilleSearch();
            searchModel.setForIdDroit(droit.getId());
            searchModel = PegasusImplServiceLocator.getDroitMembreFamilleService().search(searchModel);

            boolean hasUpdate = false;

            // cherche nouveaux membres famille
            for (MembreFamilleVO membreFamDispo : mfFiltre) {
                boolean membreTrouve = false;
                membreFamDispo.getRelationAuRequerant();
                for (JadeAbstractModel membreFamAbstract : searchModel.getSearchResults()) {
                    DroitMembreFamille membreFamExistant = (DroitMembreFamille) membreFamAbstract;
                    if (membreFamDispo.getIdMembreFamille().equals(membreFamExistant.getMembreFamille().getId())) {
                        membreTrouve = true;
                        break;
                    }
                }
                // si le membre n'est pas trouvé et s'il n'est pas de type conjoint inconnu, l'ajouter
                if (!membreTrouve
                        && !ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(membreFamDispo
                        .getIdMembreFamille())) {
                    hasUpdate = true;
                    createDroitMembreFamille(droit.getSimpleDroit(), idTiersRequerant, membreFamDispo, false);
                }
            }

            // Cherche les membres à supprimer
            for (JadeAbstractModel membreFamAbstract : searchModel.getSearchResults()) {
                DroitMembreFamille membreFamExistant = (DroitMembreFamille) membreFamAbstract;
                boolean membreTrouve = false;
                for (MembreFamilleVO membreFamDispo : mfFiltre) {
                    if (membreFamDispo.getIdMembreFamille().equals(membreFamExistant.getMembreFamille().getId())) {
                        membreTrouve = true;
                        break;
                    }
                }
                // si le membre de famille n'est pas dans la situation famillial il faut le supprimer
                if (!membreTrouve) {
                    hasUpdate = true;
                    PegasusImplServiceLocator.getDroitMembreFamilleService().delete(membreFamExistant);
                }
            }

            // Si on a effecuté des modifcation sur les membres de famille, on doit effacer touts les résultats lié aux
            // calcule et aux décisions.
            if (hasUpdate && withProecessOnUpdate) {
                processOnUpdateDonneFinanciere(droit);
            }
        }
        return droit;
    }

    @Override
    public AllocationImpotent updateAllocationImpotent(ModificateurDroitDonneeFinanciere droit,
                                                       DroitMembreFamille droitMembreFamille, AllocationImpotent newAllocationImpotent) throws DroitException,
            JadePersistenceException, AllocationImpotentException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update allocationsImpotents, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update allocationsImpotents, the droitMembreFamilleEtendu is null or new");
        }

        if (newAllocationImpotent == null) {
            throw new DroitException("Unable to update newAllocationImpotent, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        newAllocationImpotent.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            newAllocationImpotent = (AllocationImpotent) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    newAllocationImpotent, PegasusImplServiceLocator.getAllocationImpotentService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newAllocationImpotent;
    }

    @Override
    public AllocationsFamiliales updateAllocationsFamiliales(ModificateurDroitDonneeFinanciere droit,
                                                             DroitMembreFamille instanceDroitMembreFamille, AllocationsFamiliales allocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update allocationsFamiliales, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update allocationsFamiliales, the droitMembreFamilleEtendu is null or new");
        }

        if (allocationsFamiliales == null) {
            throw new DroitException("Unable to update allocationsFamiliales, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        allocationsFamiliales.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(
                instanceDroitMembreFamille.getId());
        try {
            allocationsFamiliales = (AllocationsFamiliales) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    allocationsFamiliales, PegasusImplServiceLocator.getAllocationsFamilialesService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return allocationsFamiliales;
    }

    private AbstractDonneeFinanciereModel updateAncienneDonneeFinanciere(
            AbstractDonneeFinanciereModel newDonneeFinanciere, AbstractDonneeFinanciereModel oldDF,
            SimpleVersionDroit simpleVersionDroit, Class<?> searchClassModel, AbstractDonneeFinanciereService service,
            Class<?> dataModelClass, boolean forClosePeriode) throws DroitException, JadePersistenceException,
            DonneeFinanciereException {

        try {

            if (JadeStringUtil.isBlank(oldDF.getSimpleDonneeFinanciereHeader().getId())) {
                throw new DroitException(
                        "Couldn't update AncienneDonneeFinanciere because the id for oldDonne is empty");
            }

            newDonneeFinanciere.getSimpleDonneeFinanciereHeader().setIdVersionDroit(simpleVersionDroit.getId());

            Method updateServiceMethod;
            Method createServiceMethod;
            try {
                updateServiceMethod = service.getClass().getDeclaredMethod("update", new Class[]{dataModelClass});
                createServiceMethod = service.getClass().getDeclaredMethod("create", new Class[]{dataModelClass});
            } catch (SecurityException e) {
                throw new DroitException("Error while trying to find method of the service "
                        + service.getClass().getName(), e);
            } catch (NoSuchMethodException e) {
                throw new DroitException("Error while trying to find method of the service "
                        + service.getClass().getName(), e);
            }

            // si on clos la période la date de fin est obligatoire
            if (forClosePeriode) {
                if (JadeStringUtil.isEmpty(newDonneeFinanciere.getSimpleDonneeFinanciereHeader().getDateFin())) {
                    JadeThread.logError(this.getClass().getName(),
                            "pegasus.donneefinanciereheader.dateFin.clore.mandatory");
                }
                try {
                    String datePmt = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
                    if (JadeDateUtil.isDateMonthYearAfter(newDonneeFinanciere.getSimpleDonneeFinanciereHeader()
                            .getDateFin(), datePmt)
                            || datePmt.equals(newDonneeFinanciere.getSimpleDonneeFinanciereHeader().getDateFin())) {
                        String[] param = new String[2];
                        param[0] = newDonneeFinanciere.getSimpleDonneeFinanciereHeader().getDateFin();
                        param[1] = datePmt;
                        JadeThread.logError(this.getClass().getName(),
                                "pegasus.donneefinanciereheader.dateFin.plusGrandQueProchainPaiement", param);
                    }
                } catch (PmtMensuelException e) {
                    new DonneeFinanciereException("Unable to execute getPmtMensuelService");
                } catch (JadeApplicationServiceNotAvailableException e) {
                    new DonneeFinanciereException("The service to execute getPmtMensuelService is not available");
                }

            }

            // crée header de la donnée financière à mettre à jour.
            // Si la nouvelle date de debut est plus grade que l'ancienn date début cela signifie que l'on valide une
            // nouvelle période
            if (JadeStringUtil.isEmpty(oldDF.getSimpleDonneeFinanciereHeader().getDateFin())
                    && (JadeDateUtil.isDateMonthYearAfter(newDonneeFinanciere.getSimpleDonneeFinanciereHeader()
                    .getDateDebut(), oldDF.getSimpleDonneeFinanciereHeader().getDateDebut()) || forClosePeriode)) {
                String oldDateFin = newDonneeFinanciere.getSimpleDonneeFinanciereHeader().getDateFin();

                // oldDF.getSimpleDonneeFinanciereHeader().getDateDebut()//.equalsIgnoreCase(oldDF.getSimpleDonneeFinanciereHeader().getDateDebut())

                if (forClosePeriode) {
                    oldDF.getSimpleDonneeFinanciereHeader().setIsCopieFromPreviousVersion(Boolean.TRUE);
                    oldDF.getSimpleDonneeFinanciereHeader().setIsPeriodeClose(Boolean.TRUE);
                } else {
                    // !! Hack pour le addMonth, on ajoute 01. et on reformatte apres pour le calcul
                    oldDateFin = JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01."
                            + newDonneeFinanciere.getSimpleDonneeFinanciereHeader().getDateDebut(), -1));
                }

                // verifie si on est dans un cas de correction de droit (version différente) et crée ou met à jour le
                // header
                if (simpleVersionDroit.getId().equals(oldDF.getSimpleDonneeFinanciereHeader().getIdVersionDroit())) {
                    oldDF.getSimpleDonneeFinanciereHeader().setDateFin(oldDateFin);

                    oldDF = (AbstractDonneeFinanciereModel) updateServiceMethod.invoke(service, new Object[]{oldDF});
                } else {
                    AbstractDonneeFinanciereModel dfCorrige = (AbstractDonneeFinanciereModel) JadePersistenceUtil
                            .clone(oldDF);
                    // crée nouveau header
                    dfCorrige.getSimpleDonneeFinanciereHeader().setDateFin(oldDateFin);
                    dfCorrige.getSimpleDonneeFinanciereHeader().setIdVersionDroit(simpleVersionDroit.getId());
                    dfCorrige.getSimpleDonneeFinanciereHeader().setIsCopieFromPreviousVersion(Boolean.TRUE);
                    dfCorrige.getSimpleDonneeFinanciereHeader().setIsCopieFromPreviousVersion(true);
                    dfCorrige = (AbstractDonneeFinanciereModel) createServiceMethod.invoke(service,
                            new Object[]{dfCorrige});
                }
            }

            // On n'est dans le cas d'une nouvelle période il faut donc la créer
            if (!forClosePeriode) {
                // newDonneeFinanciere.getSimpleDonneeFinanciereHeader().setIsCopieFromPreviousVersion(true);
                // configure le nouvau loyer selon l'ancien
                newDonneeFinanciere.setIsNew();
                newDonneeFinanciere.getSimpleDonneeFinanciereHeader().setIdEntity(
                        JadePersistenceManager.incIndentifiant(Compteurs.DONNEE_FINANCIERE_ID_ENTITY));

                // crée la nouvelle entité
                newDonneeFinanciere = (AbstractDonneeFinanciereModel) createServiceMethod.invoke(service,
                        new Object[]{newDonneeFinanciere});
            }

        } catch (JadeCloneModelException e) {
            throw new DroitException("Couldn't clone datamodel", e);
        } catch (IllegalAccessException e) {
            throw new DroitException("Illegal access to search classmodel", e);
        } catch (IllegalArgumentException e) {
            throw new DroitException("Illegal argument given to service", e);
        } catch (InvocationTargetException e) {
            throw new DroitException("Illegal target service", e);
        }

        return newDonneeFinanciere;

    }

    private AbstractDonneeFinanciereModel updateAncienneDonneeFinanciere(
            AbstractDonneeFinanciereModel newDonneeFinanciere, ModificateurDroitDonneeFinanciere droit,
            Class<?> searchClassModel, AbstractDonneeFinanciereService service, Class<?> dataModelClass,
            boolean forceClose) throws DroitException, JadePersistenceException, DonneeFinanciereException {

        try {

            if (JadeStringUtil.isBlank(newDonneeFinanciere.getSimpleDonneeFinanciereHeader().getId())) {
                throw new DroitException(
                        "Couldn't update AncienneDonneeFinanciere because the id for oldDonne is empty");
            }

            AbstractDonneeFinanciereSearchModel oldDFSearch = (AbstractDonneeFinanciereSearchModel) searchClassModel
                    .newInstance();

            oldDFSearch.setForIdDonneeFinanciereHeader(newDonneeFinanciere.getSimpleDonneeFinanciereHeader().getId());

            try {
                oldDFSearch = service.search(oldDFSearch);
            } catch (PegasusException e) {
                throw new DroitException("Error during search donnee financiere!", e);
            }

            AbstractDonneeFinanciereModel oldDF = (AbstractDonneeFinanciereModel) oldDFSearch.getSearchResults()[0];
            this.updateAncienneDonneeFinanciere(newDonneeFinanciere, oldDF, droit.getSimpleVersionDroit(),
                    searchClassModel, service, dataModelClass, forceClose);
        } catch (InstantiationException e) {
            throw new DroitException("Couldn't instanciate search classmodel", e);
        } catch (IllegalAccessException e) {
            throw new DroitException("Illegal access to search classmodel", e);
        } catch (IllegalArgumentException e) {
            throw new DroitException("Illegal argument given to service", e);
        }

        return newDonneeFinanciere;

    }

    @Override
    public AssuranceRenteViagere updateAssuranceRenteViagere(ModificateurDroitDonneeFinanciere droit,
                                                             DroitMembreFamille droitMembreFamille, AssuranceRenteViagere assuranceRenteViagere) throws DroitException,
            JadePersistenceException, AssuranceRenteViagereException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update assurance rente viagere, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update assurance rente viagere, the droitMembreFamilleEtendu is null or new");
        }
        if (assuranceRenteViagere == null) {
            throw new DroitException("Unable to update assurance rente viagere, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        assuranceRenteViagere.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            assuranceRenteViagere = (AssuranceRenteViagere) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    assuranceRenteViagere, PegasusImplServiceLocator.getAssuranceRenteViagereService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return assuranceRenteViagere;
    }

    @Override
    public AssuranceVie updateAssuranceVie(ModificateurDroitDonneeFinanciere droit,
                                           DroitMembreFamille instanceDroitMembreFamille, AssuranceVie assuranceVie) throws AssuranceVieException,
            JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update AssuranceVie, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException("Unable to update AssuranceVie, the droitMembreFamilleEtendu is null or new");
        }

        if (assuranceVie == null) {
            throw new DroitException("Unable to update AssuranceVie, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        assuranceVie.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(instanceDroitMembreFamille.getId());
        try {
            assuranceVie = (AssuranceVie) saveDonneeFinanciere(droit.getSimpleVersionDroit(), assuranceVie,
                    PegasusImplServiceLocator.getAssuranceVieService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return assuranceVie;
    }

    @Override
    public AutreApi updateAutreApi(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                                   AutreApi autreApi) throws DroitException, JadePersistenceException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update vehicule, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update vehicule, the droitMembreFamilleEtendu is null or new");
        }
        if (autreApi == null) {
            throw new DroitException("Unable to update vehicule, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        autreApi.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            autreApi = (AutreApi) saveDonneeFinanciere(droit.getSimpleVersionDroit(), autreApi,
                    PegasusImplServiceLocator.getAutreApiService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.toString(), e);
        }
        return autreApi;
    }

    @Override
    public AutreFortuneMobiliere updateAutreFortuneMobiliere(ModificateurDroitDonneeFinanciere droit,
                                                             DroitMembreFamille droitMembreFamille, AutreFortuneMobiliere autreFortuneMobiliere) throws DroitException,
            JadePersistenceException, AutreFortuneMobiliereException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update betail, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update betail, the droitMembreFamilleEtendu is null or new");
        }
        if (autreFortuneMobiliere == null) {
            throw new DroitException("Unable to update betail, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        autreFortuneMobiliere.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            autreFortuneMobiliere = (AutreFortuneMobiliere) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    autreFortuneMobiliere, PegasusImplServiceLocator.getAutreFortuneMobiliereService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return autreFortuneMobiliere;
    }

    @Override
    public AutreRente updateAutreRente(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                                       AutreRente autreRente) throws AutreRenteException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update autreRente, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update autreRente, the droitMembreFamilleEtendu is null or new");
        }
        if (autreRente == null) {
            throw new DroitException("Unable to update autreRente, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        autreRente.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            autreRente = (AutreRente) saveDonneeFinanciere(droit.getSimpleVersionDroit(), autreRente,
                    PegasusImplServiceLocator.getAutreRenteService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return autreRente;
    }

    @Override
    public AutresDettesProuvees updateAutresDettesProuvees(ModificateurDroitDonneeFinanciere droit,
                                                           DroitMembreFamille instanceDroitMembreFamille, AutresDettesProuvees autresDettesProuvees)
            throws AutresDettesProuveesException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update AutresDettesProuvees, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update AutresDettesProuvees, the droitMembreFamilleEtendu is null or new");
        }

        if (autresDettesProuvees == null) {
            throw new DroitException("Unable to update AutresDettesProuvees, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        autresDettesProuvees.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(
                instanceDroitMembreFamille.getId());
        try {
            autresDettesProuvees = (AutresDettesProuvees) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    autresDettesProuvees, PegasusImplServiceLocator.getAutresDettesProuveesService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return autresDettesProuvees;
    }

    @Override
    public AutresRevenus updateAutresRevenus(ModificateurDroitDonneeFinanciere droit,
                                             DroitMembreFamille instanceDroitMembreFamille, AutresRevenus autresRevenus) throws AutresRevenusException,
            JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update autresRevenus, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException("Unable to update autresRevenus, the droitMembreFamilleEtendu is null or new");
        }

        if (autresRevenus == null) {
            throw new DroitException("Unable to update autresRevenus, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        autresRevenus.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(instanceDroitMembreFamille.getId());
        try {
            autresRevenus = (AutresRevenus) saveDonneeFinanciere(droit.getSimpleVersionDroit(), autresRevenus,
                    PegasusImplServiceLocator.getAutresRevenusService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return autresRevenus;
    }

    @Override
    public Betail updateBetail(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                               Betail betail) throws DroitException, JadePersistenceException, BetailException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update betail, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update betail, the droitMembreFamilleEtendu is null or new");
        }
        if (betail == null) {
            throw new DroitException("Unable to update betail, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        betail.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            betail = (Betail) saveDonneeFinanciere(droit.getSimpleVersionDroit(), betail,
                    PegasusImplServiceLocator.getBetailService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return betail;
    }

    @Override
    public BienImmobilierHabitationNonPrincipale updateBienImmobilierHabitationNonPrincipale(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update BienImmobilierHabitationNonPrincipale, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update BienImmobilierHabitationNonPrincipale, the droitMembreFamilleEtendu is null or new");
        }

        if (bienImmobilierHabitationNonPrincipale == null) {
            throw new DroitException("Unable to update BienImmobilierHabitationNonPrincipale, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        bienImmobilierHabitationNonPrincipale.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(
                instanceDroitMembreFamille.getId());
        try {
            bienImmobilierHabitationNonPrincipale = (BienImmobilierHabitationNonPrincipale) saveDonneeFinanciere(
                    droit.getSimpleVersionDroit(), bienImmobilierHabitationNonPrincipale,
                    PegasusImplServiceLocator.getBienImmobilierHabitationNonPrincipaleService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return bienImmobilierHabitationNonPrincipale;
    }

    @Override
    public BienImmobilierNonHabitable updateBienImmobilierNonHabitable(ModificateurDroitDonneeFinanciere droit,
                                                                       DroitMembreFamille instanceDroitMembreFamille, BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update BienImmobilierNonHabitable, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update BienImmobilierNonHabitable, the droitMembreFamilleEtendu is null or new");
        }

        if (bienImmobilierNonHabitable == null) {
            throw new DroitException("Unable to update BienImmobilierNonHabitable, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        bienImmobilierNonHabitable.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(
                instanceDroitMembreFamille.getId());
        try {
            bienImmobilierNonHabitable = (BienImmobilierNonHabitable) saveDonneeFinanciere(
                    droit.getSimpleVersionDroit(), bienImmobilierNonHabitable,
                    PegasusImplServiceLocator.getBienImmobilierNonHabitableService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return bienImmobilierNonHabitable;
    }

    @Override
    public BienImmobilierServantHabitationPrincipale updateBienImmobilierServantHabitationPrincipale(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException(
                    "Unable to update BienImmobilierServantHabitationPrincipale, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update BienImmobilierServantHabitationPrincipale, the droitMembreFamilleEtendu is null or new");
        }

        if (bienImmobilierServantHabitationPrincipale == null) {
            throw new DroitException("Unable to update BienImmobilierServantHabitationPrincipale, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        bienImmobilierServantHabitationPrincipale.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(
                instanceDroitMembreFamille.getId());
        try {
            bienImmobilierServantHabitationPrincipale = (BienImmobilierServantHabitationPrincipale) saveDonneeFinanciere(
                    droit.getSimpleVersionDroit(), bienImmobilierServantHabitationPrincipale,
                    PegasusImplServiceLocator.getBienImmobilierServantHabitationPrincipaleService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return bienImmobilierServantHabitationPrincipale;
    }

    @Override
    public CapitalLPP updateCapitalLPP(ModificateurDroitDonneeFinanciere droit,
                                       DroitMembreFamille instanceDroitMembreFamille, CapitalLPP capitalLPP) throws CapitalLPPException,
            JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update CapitalLPP, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException("Unable to update CapitalLPP, the droitMembreFamilleEtendu is null or new");
        }

        if (capitalLPP == null) {
            throw new DroitException("Unable to update CapitalLPP, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        capitalLPP.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(instanceDroitMembreFamille.getId());
        try {
            capitalLPP = (CapitalLPP) saveDonneeFinanciere(droit.getSimpleVersionDroit(), capitalLPP,
                    PegasusImplServiceLocator.getCapitalLPPService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return capitalLPP;
    }

    @Override
    public CompteBancaireCCP updateCompteBancaireCCP(ModificateurDroitDonneeFinanciere droit,
                                                     DroitMembreFamille instanceDroitMembreFamille, CompteBancaireCCP compteBancaireCCP)
            throws CompteBancaireCCPException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update compteBancaireCCP, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException("Unable to update compteBancaireCCP, the droitMembreFamilleEtendu is null or new");
        }

        if (compteBancaireCCP == null) {
            throw new DroitException("Unable to update compteBancaireCCP, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        compteBancaireCCP.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(instanceDroitMembreFamille.getId());
        try {
            compteBancaireCCP = (CompteBancaireCCP) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    compteBancaireCCP, PegasusImplServiceLocator.getCompteBancaireCCPService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return compteBancaireCCP;
    }

    @Override
    public ContratEntretienViager updateContratEntretienViager(ModificateurDroitDonneeFinanciere droit,
                                                               DroitMembreFamille droitMembreFamille, ContratEntretienViager contratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update contratEntretienViager, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update contratEntretienViager, the droitMembreFamilleEtendu is null or new");
        }

        if (contratEntretienViager == null) {
            throw new DroitException("Unable to update contratEntretienViager, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        contratEntretienViager.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            contratEntretienViager = (ContratEntretienViager) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    contratEntretienViager, PegasusImplServiceLocator.getContratEntretienViagerService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return contratEntretienViager;
    }

    @Override
    public CotisationsPsal updateCotisationsPsal(ModificateurDroitDonneeFinanciere droit,
                                                 DroitMembreFamille instanceDroitMembreFamille, CotisationsPsal cotisationsPsal)
            throws CotisationsPsalException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update cotisationsPsal, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException("Unable to update cotisationsPsal, the droitMembreFamilleEtendu is null or new");
        }

        if (cotisationsPsal == null) {
            throw new DroitException("Unable to update cotisationsPsal, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        cotisationsPsal.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(instanceDroitMembreFamille.getId());
        try {
            cotisationsPsal = (CotisationsPsal) saveDonneeFinanciere(droit.getSimpleVersionDroit(), cotisationsPsal,
                    PegasusImplServiceLocator.getCotisationsPsalService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return cotisationsPsal;
    }

    @Override
    public DessaisissementFortune updateDessaisissementFortune(ModificateurDroitDonneeFinanciere droit,
                                                               DroitMembreFamille droitMembreFamille, DessaisissementFortune dessaisissementFortune)
            throws DessaisissementFortuneException, DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DroitException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update dessaisissement fortune, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update dessaisissement fortune, the droitMembreFamilleEtendu is null or new");
        }
        if (dessaisissementFortune == null) {
            throw new DroitException("Unable to update dessaisissement fortune, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        dessaisissementFortune.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {

            dessaisissementFortune = (DessaisissementFortune) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    dessaisissementFortune, PegasusImplServiceLocator.getDessaisissementFortuneService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return dessaisissementFortune;
    }

    @Override
    public DessaisissementRevenu updateDessaisissementRevenu(ModificateurDroitDonneeFinanciere droit,
                                                             DroitMembreFamille droitMembreFamille, DessaisissementRevenu dessaisissementRevenu)
            throws DessaisissementRevenuException, DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DroitException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update dessaisissement revenu, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update dessaisissement revenu, the droitMembreFamilleEtendu is null or new");
        }
        if (dessaisissementRevenu == null) {
            throw new DroitException("Unable to update dessaisissement revenu, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        dessaisissementRevenu.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {

            dessaisissementRevenu = (DessaisissementRevenu) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    dessaisissementRevenu, PegasusImplServiceLocator.getDessaisissementRevenuService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return dessaisissementRevenu;
    }

    @Override
    public DonneesPersonnelles updateDonneesPersonnelles(DonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, DossierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        return PegasusImplServiceLocator.getDonneesPersonnellesService().update(donneesPersonnelles);
    }

    @Override
    public Droit updateDroit(Droit droit) throws DroitException, JadePersistenceException {
        if (droit == null) {
            throw new DroitException("Unable to update droit, the model passed is null!");
        }

        try {
            droit.setSimpleDroit(PegasusImplServiceLocator.getSimpleDroitService().update(droit.getSimpleDroit()));
            droit.setSimpleVersionDroit(PegasusImplServiceLocator.getSimpleVersionDroitService().update(
                    droit.getSimpleVersionDroit()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return droit;
    }

    private void updateDroitForDonneeFinanciere(Droit droit) throws DroitException, JadePersistenceException {

        if (droit == null) {
            throw new DroitException("Unable to update droit, the model passed is null!");
        }

        try {
            droit = processOnUpdateDonneFinanciere(droit);
            droit.setSimpleDroit(PegasusImplServiceLocator.getSimpleDroitService().update(droit.getSimpleDroit()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
    }

    private ModificateurDroitDonneeFinanciere updateDroitForDonneeFinanciere(ModificateurDroitDonneeFinanciere droit)
            throws DroitException, JadePersistenceException {
        if (droit == null) {
            throw new DroitException("Unable to update droit, the model passed is null!");
        }

        Droit droit2 = new Droit();
        droit2.setSimpleDroit(droit.getSimpleDroit());
        droit2.setSimpleVersionDroit(droit.getSimpleVersionDroit());
        this.updateDroitForDonneeFinanciere(droit2);
        droit.setSimpleVersionDroit(droit2.getSimpleVersionDroit());
        droit.setSimpleDroit(droit2.getSimpleDroit());
        return droit;
    }

    @Override
    public IjApg updateIjApg(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille, IjApg ijApg)
            throws DroitException, JadePersistenceException, IjApgException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update ijApg, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update ijApg, the droitMembreFamilleEtendu is null or new");
        }
        if (ijApg == null) {
            throw new DroitException("Unable to update ijApg, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        ijApg.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            ijApg = (IjApg) saveDonneeFinanciere(droit.getSimpleVersionDroit(), ijApg,
                    PegasusImplServiceLocator.getIjApgService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return ijApg;
    }

    @Override
    public IndemniteJournaliereAi updateIndemniteJournaliereAi(ModificateurDroitDonneeFinanciere droit,
                                                               DroitMembreFamille droitMembreFamille, IndemniteJournaliereAi newIndemniteJournaliereAi)
            throws DroitException, JadePersistenceException, IndemniteJournaliereAiException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update IndemniteJournaliereAi, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update IndemniteJournaliereAi, the droitMembreFamilleEtendu is null or new");
        }
        if (newIndemniteJournaliereAi == null) {
            throw new DroitException("Unable to update IndemniteJournaliereAi, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        newIndemniteJournaliereAi.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            newIndemniteJournaliereAi = (IndemniteJournaliereAi) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    newIndemniteJournaliereAi, PegasusImplServiceLocator.getIndemniteJournaliereAiService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newIndemniteJournaliereAi;
    }

    @Override
    public Loyer updateLoyer(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille, Loyer loyer)
            throws LoyerException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update loyer, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update loyer, the droitMembreFamilleEtendu is null or new");
        }
        if (loyer == null) {
            throw new DroitException("Unable to update loyer, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        loyer.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            loyer = (Loyer) saveDonneeFinanciere(droit.getSimpleVersionDroit(), loyer,
                    PegasusImplServiceLocator.getLoyerService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return loyer;
    }

    @Override
    public MarchandisesStock updateMarchandisesStock(ModificateurDroitDonneeFinanciere droit,
                                                     DroitMembreFamille droitMembreFamille, MarchandisesStock marchandisesStock)
            throws MarchandisesStockException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update marchandises/stock, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update marchandises/stock, the droitMembreFamilleEtendu is null or new");
        }
        if (marchandisesStock == null) {
            throw new DroitException("Unable to update marchandises/stock, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        marchandisesStock.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {

            marchandisesStock = (MarchandisesStock) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    marchandisesStock, PegasusImplServiceLocator.getMarchandisesStockService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return marchandisesStock;
    }

    @Override
    public Numeraire updateNumeraire(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                                     Numeraire numeraire) throws DroitException, JadePersistenceException, NumeraireException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update numeraire, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update numeraire, the droitMembreFamilleEtendu is null or new");
        }
        if (numeraire == null) {
            throw new DroitException("Unable to update numeraire, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        numeraire.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            numeraire = (Numeraire) saveDonneeFinanciere(droit.getSimpleVersionDroit(), numeraire,
                    PegasusImplServiceLocator.getNumeraireService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return numeraire;
    }

    @Override
    public PensionAlimentaire updatePensionAlimentaire(ModificateurDroitDonneeFinanciere droit,
                                                       DroitMembreFamille instanceDroitMembreFamille, PensionAlimentaire pensionAlimentaire)
            throws PensionAlimentaireException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update pensionAlimentaire, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException("Unable to update pensionAlimentaire, the droitMembreFamilleEtendu is null or new");
        }

        if (pensionAlimentaire == null) {
            throw new DroitException("Unable to update pensionAlimentaire, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        pensionAlimentaire.getSimpleDonneeFinanciereHeader()
                .setIdDroitMembreFamille(instanceDroitMembreFamille.getId());
        try {
            pensionAlimentaire = (PensionAlimentaire) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    pensionAlimentaire, PegasusImplServiceLocator.getPensionAlimentaireService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return pensionAlimentaire;
    }

    @Override
    public PretEnversTiers updatePretEnversTiers(ModificateurDroitDonneeFinanciere droit,
                                                 DroitMembreFamille droitMembreFamille, PretEnversTiers pretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update pret envers tiers, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update pret envers tiers, the droitMembreFamilleEtendu is null or new");
        }
        if (pretEnversTiers == null) {
            throw new DroitException("Unable to update pret envers tiers, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        pretEnversTiers.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {

            pretEnversTiers = (PretEnversTiers) saveDonneeFinanciere(droit.getSimpleVersionDroit(), pretEnversTiers,
                    PegasusImplServiceLocator.getPretEnversTiersService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return pretEnversTiers;
    }

    @Override
    public RenteAvsAi updateRenteAvsAi(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                                       RenteAvsAi newRenteAvsAi) throws DroitException, JadePersistenceException, RenteAvsAiException,
            DonneeFinanciereException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update RenteAvsAi, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update RenteAvsAi, the droitMembreFamilleEtendu is null or new");
        }
        if (newRenteAvsAi == null) {
            throw new DroitException("Unable to update RenteAvsAi, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        newRenteAvsAi.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            newRenteAvsAi = (RenteAvsAi) saveDonneeFinanciere(droit.getSimpleVersionDroit(), newRenteAvsAi,
                    PegasusImplServiceLocator.getRenteAvsAiService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return newRenteAvsAi;
    }

    @Override
    public RevenuActiviteLucrativeDependante updateRevenuActiviteLucrativeDependante(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update revenuActiviteLucrativeDependante, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update revenuActiviteLucrativeDependante, the droitMembreFamilleEtendu is null or new");
        }

        if (revenuActiviteLucrativeDependante == null) {
            throw new DroitException("Unable to update revenuActiviteLucrativeDependante, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        revenuActiviteLucrativeDependante.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(
                droitMembreFamille.getId());
        try {
            revenuActiviteLucrativeDependante = (RevenuActiviteLucrativeDependante) saveDonneeFinanciere(
                    droit.getSimpleVersionDroit(), revenuActiviteLucrativeDependante,
                    PegasusImplServiceLocator.getRevenuActiviteLucrativeDependanteService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return revenuActiviteLucrativeDependante;
    }

    @Override
    public RevenuActiviteLucrativeIndependante updateRevenuActiviteLucrativeIndependante(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update revenuActiviteLucrativeIndependante, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update revenuActiviteLucrativeIndependante, the droitMembreFamilleEtendu is null or new");
        }

        if (revenuActiviteLucrativeIndependante == null) {
            throw new DroitException("Unable to update revenuActiviteLucrativeIndependante, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        revenuActiviteLucrativeIndependante.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(
                droitMembreFamille.getId());
        try {
            revenuActiviteLucrativeIndependante = (RevenuActiviteLucrativeIndependante) saveDonneeFinanciere(
                    droit.getSimpleVersionDroit(), revenuActiviteLucrativeIndependante,
                    PegasusImplServiceLocator.getRevenuActiviteLucrativeIndependanteService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return revenuActiviteLucrativeIndependante;
    }

    @Override
    public RevenuHypothetique updateRevenuHypothetique(ModificateurDroitDonneeFinanciere droit,
                                                       DroitMembreFamille droitMembreFamille, RevenuHypothetique revenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update revenuHypothetique, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update revenuHypothetique, the droitMembreFamilleEtendu is null or new");
        }

        if (revenuHypothetique == null) {
            throw new DroitException("Unable to update revenuHypothetique, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        revenuHypothetique.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            revenuHypothetique = (RevenuHypothetique) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    revenuHypothetique, PegasusImplServiceLocator.getRevenuHypothetiqueService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return revenuHypothetique;
    }

    @Override
    public SimpleLibelleContratEntretienViager updateSimpleLibelleContratEntretienViager(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (simpleLibelleContratEntretienViager == null) {
            throw new DroitException("Unable to update revenuActiviteLucrativeIndependante, the model is null");
        }
        try {
            simpleLibelleContratEntretienViager = PegasusImplServiceLocator
                    .getSimpleLibelleContratEntretienViagerService().update(simpleLibelleContratEntretienViager);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return simpleLibelleContratEntretienViager;
    }

    @Override
    public SimpleTypeFraisObtentionRevenu updateSimpleTypeFraisObtentionRevenu(ModificateurDroitDonneeFinanciere droit,
                                                                               DroitMembreFamille droitMembreFamille, SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws SimpleTypeFraisObtentionRevenuException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (simpleTypeFraisObtentionRevenu == null) {
            throw new DroitException("Unable to update revenuActiviteLucrativeIndependante, the model is null");
        }
        try {
            simpleTypeFraisObtentionRevenu = PegasusImplServiceLocator.getSimpleTypeFraisObtentionRevenuService()
                    .update(simpleTypeFraisObtentionRevenu);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return simpleTypeFraisObtentionRevenu;
    }

    @Override
    public TaxeJournaliereHome updateTaxeJournaliereHome(ModificateurDroitDonneeFinanciere droit,
                                                         DroitMembreFamille droitMembreFamille, TaxeJournaliereHome taxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update taxeJournaliereHome, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update taxeJournaliereHome, the droitMembreFamilleEtendu is null or new");
        }
        if (taxeJournaliereHome == null) {
            throw new DroitException("Unable to update taxeJournaliereHome, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        taxeJournaliereHome.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            taxeJournaliereHome = (TaxeJournaliereHome) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    taxeJournaliereHome, PegasusImplServiceLocator.getTaxeJournaliereHomeService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return taxeJournaliereHome;
    }

    @Override
    public SejourMoisPartielHome updateSejourMoisPartielHome(ModificateurDroitDonneeFinanciere droit,
                                                             DroitMembreFamille droitMembreFamille, SejourMoisPartielHome sejourMoisPartielHome)
            throws JadePersistenceException, DroitException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update taxeJournaliereHome, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException(
                    "Unable to update taxeJournaliereHome, the droitMembreFamilleEtendu is null or new");
        }
        if (sejourMoisPartielHome == null) {
            throw new DroitException("Unable to update taxeJournaliereHome, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        sejourMoisPartielHome.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            sejourMoisPartielHome = (SejourMoisPartielHome) saveDonneeFinanciere(droit.getSimpleVersionDroit(),
                    sejourMoisPartielHome, PegasusImplServiceLocator.getSejourMoisPartielHomeService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return sejourMoisPartielHome;
    }

    @Override
    public Titre updateTitre(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
                             Titre titre) throws TitreException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update Titre, the droit is null or new");
        }
        if ((instanceDroitMembreFamille == null) || instanceDroitMembreFamille.isNew()) {
            throw new DroitException("Unable to update Titre, the droitMembreFamilleEtendu is null or new");
        }

        if (titre == null) {
            throw new DroitException("Unable to update Titre, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        titre.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(instanceDroitMembreFamille.getId());
        try {
            titre = (Titre) saveDonneeFinanciere(droit.getSimpleVersionDroit(), titre,
                    PegasusImplServiceLocator.getTitreService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return titre;
    }

    @Override
    public TypeFraisObtentionRevenu updateTypeFraisObtentionRevenu(ModificateurDroitDonneeFinanciere droit,
                                                                   DroitMembreFamille droitMembreFamille, TypeFraisObtentionRevenu typeFraisObtentionRevenu)
            throws TypeFraisObtentionRevenuException, JadePersistenceException, DroitException,
            DonneeFinanciereException {

        if (typeFraisObtentionRevenu == null) {
            throw new DroitException("Unable to update TypeFraisObtentionRevenu, the model is null");
        }
        try {
            typeFraisObtentionRevenu = PegasusImplServiceLocator.getTypeFraisObtentionRevenuService().update(
                    typeFraisObtentionRevenu);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return typeFraisObtentionRevenu;
    }

    @Override
    public Vehicule updateVehicule(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
                                   Vehicule vehicule) throws DroitException, JadePersistenceException, VehiculeException,
            DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update vehicule, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update vehicule, the droitMembreFamilleEtendu is null or new");
        }
        if (vehicule == null) {
            throw new DroitException("Unable to update vehicule, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        vehicule.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            vehicule = (Vehicule) saveDonneeFinanciere(droit.getSimpleVersionDroit(), vehicule,
                    PegasusImplServiceLocator.getVehiculeService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return vehicule;
    }

    /**
     * PC-REFORME : AJOUT FRAIS DE GARDE
     */
    @Override
    public FraisGardeSearch searchFraisGarde(FraisGardeSearch searchModel) throws JadePersistenceException,
            DroitException {
        if (searchModel == null) {
            throw new DroitException("Unable to search fraisGarde, the search model passed is null!");
        }
        return (FraisGardeSearch) JadePersistenceManager.search(searchModel);
    }


    @Override
    public FraisGarde createFraisGarde(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille, FraisGarde fraisGarde)
            throws FraisGardeException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create FraisGarde, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            fraisGarde = PegasusImplServiceLocator.getRevenusDepensesService().createFraisGarde(
                    droit.getSimpleVersionDroit(), instanceDroitMembreFamille, fraisGarde);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return fraisGarde;

    }

    @Override
    public FraisGarde updateFraisGarde(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille, FraisGarde fraisGarde) throws FraisGardeException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update fraisGarde, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update fraisGarde, the droitMembreFamilleEtendu is null or new");
        }
        if (fraisGarde == null) {
            throw new DroitException("Unable to update fraisGarde, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        fraisGarde.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            fraisGarde = (FraisGarde) saveDonneeFinanciere(droit.getSimpleVersionDroit(), fraisGarde,
                    PegasusImplServiceLocator.getFraisGardeService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return fraisGarde;
    }

    @Override
    public FraisGarde deleteFraisGarde(ModificateurDroitDonneeFinanciere droit, FraisGarde fraisGarde) throws FraisGardeException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if (fraisGarde == null) {
            throw new DroitException("Unable to delete fraisGarde, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            fraisGarde.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            fraisGarde = (FraisGarde) saveDonneeFinanciere(droit.getSimpleVersionDroit(), fraisGarde,
                    PegasusImplServiceLocator.getFraisGardeService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return fraisGarde;
    }

    @Override
    public FraisGarde readFraisGarde(String id) throws JadePersistenceException, DroitException {
        FraisGarde fraisGarde = new FraisGarde();
        fraisGarde.setId(id);

        return (FraisGarde) JadePersistenceManager.read(fraisGarde);
    }

    @Override
    public FraisGarde createAndCloseFraisGarde(ModificateurDroitDonneeFinanciere droit, FraisGarde newDonneeFinanciere, boolean forceClose) throws FraisGardeException, JadePersistenceException, DroitException, DonneeFinanciereException {

        if (newDonneeFinanciere == null) {
            throw new DroitException("Unable to create FraisGarde, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create FraisGarde, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            newDonneeFinanciere = (FraisGarde) this.updateAncienneDonneeFinanciere(newDonneeFinanciere, droit,
                    FraisGardeSearch.class, PegasusImplServiceLocator.getFraisGardeService(),
                    FraisGarde.class, forceClose);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return newDonneeFinanciere;
    }

    @Override
    public AssuranceMaladieSearch searchAssuranceMaladie(AssuranceMaladieSearch search) throws DroitException,
            JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to search revenus/depenses, the search model passed is null!");
        }
        return (AssuranceMaladieSearch) JadePersistenceManager.search(search);
    }

    @Override
    public PrimeAssuranceMaladie updatePrimeAssuranceMaladie(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille, PrimeAssuranceMaladie primeAssuranceMaladie)
            throws PrimeAssuranceMaladieException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update primeAssuranceMaladie, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update primeAssuranceMaladie, the droitMembreFamilleEtendu is null or new");
        }
        if (primeAssuranceMaladie == null) {
            throw new DroitException("Unable to update primeAssuranceMaladie, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        primeAssuranceMaladie.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            primeAssuranceMaladie = (PrimeAssuranceMaladie) saveDonneeFinanciere(droit.getSimpleVersionDroit(), primeAssuranceMaladie,
                    PegasusImplServiceLocator.getPrimeAssuranceMaladieService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return primeAssuranceMaladie;
    }

    @Override
    public PrimeAssuranceMaladie deletePrimeAssuranceMaladie(ModificateurDroitDonneeFinanciere droit, PrimeAssuranceMaladie primeAssuranceMaladie) throws PrimeAssuranceMaladieException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if (primeAssuranceMaladie == null) {
            throw new DroitException("Unable to delete primeAssuranceMaladie, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            primeAssuranceMaladie.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            primeAssuranceMaladie = (PrimeAssuranceMaladie) saveDonneeFinanciere(droit.getSimpleVersionDroit(), primeAssuranceMaladie,
                    PegasusImplServiceLocator.getPrimeAssuranceMaladieService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return primeAssuranceMaladie;
    }

    @Override
    public PrimeAssuranceMaladie createPrimeAssuranceMaladie(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille, PrimeAssuranceMaladie primeAssuranceMaladie)
            throws DroitException, PrimeAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create FraisGarde, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            primeAssuranceMaladie = PegasusImplServiceLocator.getAssuranceMaladieService().createPrimeAssuranceMaladie(
                    droit.getSimpleVersionDroit(), instanceDroitMembreFamille, primeAssuranceMaladie);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return primeAssuranceMaladie;
    }

    @Override
    public PrimeAssuranceMaladie createPrimeAssuranceMaladie(PrimeAssuranceMaladie primeAssuranceMaladie)
            throws DroitException, PrimeAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException {


        try {
            primeAssuranceMaladie = PegasusImplServiceLocator.getAssuranceMaladieService().createPrimeAssuranceMaladie(primeAssuranceMaladie);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return primeAssuranceMaladie;
    }

    @Override
    public PrimeAssuranceMaladie createAndClosePrimeAssuranceMaladie(ModificateurDroitDonneeFinanciere droit, PrimeAssuranceMaladie primeAssuranceMaladie, boolean forceClose) throws DroitException, JadePersistenceException, DonneeFinanciereException {

        if (primeAssuranceMaladie == null) {
            throw new DroitException("Unable to create PrimeAssuranceMaladie, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create PrimeAssuranceMaladie, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            primeAssuranceMaladie = (PrimeAssuranceMaladie) this.updateAncienneDonneeFinanciere(primeAssuranceMaladie, droit,
                    PrimeAssuranceMaladieSearch.class, PegasusImplServiceLocator.getPrimeAssuranceMaladieService(), PrimeAssuranceMaladie.class,
                    forceClose);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return primeAssuranceMaladie;
    }

    @Override
    public SubsideAssuranceMaladie updateSubsideAssuranceMaladie(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille, SubsideAssuranceMaladie subsideAssuranceMaladie)
            throws SubsideAssuranceMaladieException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to update SubsideAssuranceMaladie, the droit is null or new");
        }
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update SubsideAssuranceMaladie, the droitMembreFamilleEtendu is null or new");
        }
        if (subsideAssuranceMaladie == null) {
            throw new DroitException("Unable to update SubsideAssuranceMaladie, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);
        subsideAssuranceMaladie.getSimpleDonneeFinanciereHeader().setIdDroitMembreFamille(droitMembreFamille.getId());
        try {
            subsideAssuranceMaladie = (SubsideAssuranceMaladie) saveDonneeFinanciere(droit.getSimpleVersionDroit(), subsideAssuranceMaladie,
                    PegasusImplServiceLocator.getSubsideAssuranceMaladieService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return subsideAssuranceMaladie;
    }

    @Override
    public SubsideAssuranceMaladie deleteSubsideAssuranceMaladie(ModificateurDroitDonneeFinanciere droit, SubsideAssuranceMaladie subsideAssuranceMaladie) throws SubsideAssuranceMaladieException, JadePersistenceException, DroitException, DonneeFinanciereException {
        if (subsideAssuranceMaladie == null) {
            throw new DroitException("Unable to delete SubsideAssuranceMaladie, the model is null");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            subsideAssuranceMaladie.getSimpleDonneeFinanciereHeader().setIsSupprime(Boolean.TRUE);

            subsideAssuranceMaladie = (SubsideAssuranceMaladie) saveDonneeFinanciere(droit.getSimpleVersionDroit(), subsideAssuranceMaladie,
                    PegasusImplServiceLocator.getSubsideAssuranceMaladieService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return subsideAssuranceMaladie;
    }

    @Override
    public SubsideAssuranceMaladie createSubsideAssuranceMaladie(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille, SubsideAssuranceMaladie subsideAssuranceMaladie)
            throws DroitException, SubsideAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException {
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create FraisGarde, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            subsideAssuranceMaladie = PegasusImplServiceLocator.getAssuranceMaladieService().createSubsideAssuranceMaladie(
                    droit.getSimpleVersionDroit(), instanceDroitMembreFamille, subsideAssuranceMaladie);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }

        return subsideAssuranceMaladie;
    }

    @Override
    public SubsideAssuranceMaladie createSubsideAssuranceMaladie(SubsideAssuranceMaladie subsideAssuranceMaladie)
            throws DroitException, SubsideAssuranceMaladieException, JadePersistenceException, DonneeFinanciereException {


        try {
            subsideAssuranceMaladie = PegasusImplServiceLocator.getAssuranceMaladieService().createSubsideAssuranceMaladie(subsideAssuranceMaladie);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        }
        return subsideAssuranceMaladie;
    }

    @Override
    public SubsideAssuranceMaladie createAndCloseSubsideAssuranceMaladie(ModificateurDroitDonneeFinanciere droit, SubsideAssuranceMaladie subsideAssuranceMaladie, boolean forceClose) throws DroitException, JadePersistenceException, DonneeFinanciereException {
        if (subsideAssuranceMaladie == null) {
            throw new DroitException("Unable to create SubsideAssuranceMaladie, the model is null");
        }
        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Unable to create SubsideAssuranceMaladie, the droit is null or new");
        }

        this.updateDroitForDonneeFinanciere(droit);

        try {
            subsideAssuranceMaladie = (SubsideAssuranceMaladie) this.updateAncienneDonneeFinanciere(subsideAssuranceMaladie, droit,
                    SubsideAssuranceMaladieSearch.class, PegasusImplServiceLocator.getSubsideAssuranceMaladieService(), SubsideAssuranceMaladie.class,
                    forceClose);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Service not available - " + e.getMessage());
        } catch (SecurityException e) {
            throw new DroitException("Security exception was raised!", e);
        }
        return subsideAssuranceMaladie;
    }

}
