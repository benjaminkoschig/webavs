/**
 * 
 */
package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.hera.business.exceptions.HeraException;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
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
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AllocationImpotentException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreApiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IjApgException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AutresRevenusException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.ContratEntretienViagerException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.PensionAlimentaireException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeDependanteException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuHypothetiqueException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleLibelleContratEntretienViagerException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleTypeFraisObtentionRevenuException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.TypeFraisObtentionRevenuException;
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
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;
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
import ch.globaz.pegasus.business.models.habitat.HabitatSearch;
import ch.globaz.pegasus.business.models.habitat.Loyer;
import ch.globaz.pegasus.business.models.habitat.LoyerSearch;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeDroitSearch;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeSearch;
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
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamiliales;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.AutresRevenus;
import ch.globaz.pegasus.business.models.revenusdepenses.AutresRevenusSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViagerSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsal;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsalSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaire;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaireSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependanteSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetique;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetiqueSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepensesSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViagerSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenu;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenuSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.TypeFraisObtentionRevenu;
import ch.globaz.pegasus.business.services.synchronisation.MembreFamilleToSync;
import ch.globaz.pegasus.business.services.synchronisation.MembresFamillesToSynchronise;

/**
 * @author BSC
 */
/**
 * @author FHA
 */
public interface DroitService extends JadeApplicationService {

    /**
     * Lance le calcul des Prestations Complementaires pour le droit donné en paramètre.<br/>
     * <b style="color:red">!! IMPORTANT : La session est committée lors de l'initialisation du calcul !! </b>
     * 
     * @param droit
     *            le droit à calculer
     * @return le droit calculé et mis à jour
     * @throws PegasusException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws HeraException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public Droit calculerDroit(Droit droit) throws PegasusException, HeraException, JadePersistenceException,
            JadeApplicationException, SecurityException, NoSuchMethodException;

    /**
     * Lance le calcul des Prestations Complementaires pour le droit donné en paramètre.
     * 
     * @param versionDroit
     *            l'id du version droit à calculer
     * @return le droit calculé et mis à jour
     * @throws PegasusException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws HeraException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public Droit calculerDroit(String idVersionDroit) throws PegasusException, HeraException, JadePersistenceException,
            JadeApplicationException, SecurityException, NoSuchMethodException;

    /**
     * Lance le calcul des Prestations Complementaires pour le droit donné en paramètre.
     * 
     * @param versionDroit
     *            l'id du version droit à calculer
     * @param isRetroactif
     *            indique si le calcul doit tenir compte de l'historique des données
     * @return le droit calculé et mis à jour
     * @throws PegasusException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws HeraException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public Droit calculerDroit(String idVersionDroit, boolean isRetroactif, List<String> dfForVersion)
            throws PegasusException, HeraException, JadePersistenceException, JadeApplicationException,
            SecurityException, NoSuchMethodException;

    /**
     * Donne une nouvelle version du droit passe en parametrecorrigerCreateVersionDroit pour correction ne change pas le
     * droit passé en paramétre Il n'est pas possible de CORRIGER une version d'un droit qui n'est pas validé par une
     * decision
     * 
     * @param droit
     *            Le droit à corriger
     * @param dateAnnonce
     *            La date d'annonce de la modification de situation de l'assure
     * @param csMotif
     *            TODO
     * @return La nouvelle version du droit a corriger
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public Droit corrigerCreateVersionDroit(Droit droit, String dateAnnonce, String csMotif) throws DroitException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Donne une nouvelle version du droit passé en paramètre pour correction Il n'est pas possible de CORRIGER une
     * version d'un droit qui n'est pas validée par une decision
     * 
     * @param droit
     *            Le droit a corriger
     * @param dateAnnonce
     *            La date d'annonce de la modification de situation de l'assure
     * @param csMotif
     *            TODO
     * @return La nouvelle version du droit a corriger
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public Droit corrigerDroit(Droit droit, String dateAnnonce, String csMotif) throws DroitException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Crée une nouvelle version du droit passé en paramètre, génère et valide directement la décision de suppression.
     * Ce service ne doit être utilisé que en cas de décès (csMotif = DECES) et lèvera une exception si ce n'est pas le
     * cas
     * 
     * @param droit
     * @param dateAnnonce
     * @param csMotif
     * @param dateSuppression
     * @param dateDecision
     * @param currentUserId
     * @return
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public Droit corrigerDroitEnCasDeDeces(Droit droit, String dateAnnonce, String csMotif, String dateSuppression,
            String dateDecision, String currentUserId, boolean comptabilisationAuto, String mailProcessCompta)
            throws DroitException, JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(DroitSearch search) throws DroitException, JadePersistenceException;

    /**
     * Création d'une allocation impotent
     * 
     * @param droit
     * @param newAllocationImpotent
     * @return
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws AllocationImpotentException
     * @throws DonneeFinanciereException
     */
    AllocationImpotent createAllocationImpotent(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, AllocationImpotent newAllocationImpotent) throws DroitException,
            JadePersistenceException, AllocationImpotentException, DonneeFinanciereException;

    public AllocationsFamiliales createAllocationsFamiliales(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, AllocationsFamiliales allocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException, DroitException, DonneeFinanciereException;

    public void createAndCloseAllocationImpotent(Droit droit, AllocationImpotent newApi, AllocationImpotent oldApi,
            boolean forClosePeriode) throws DroitException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Création et fermeture d'une allocation impotent
     * 
     * @param droit
     * @param newAllocationImpotent
     * @return L'allocation crée et fermé
     * @throws AllocationImpotentException
     * @throws JadePersistenceException
     * @throws DroitException
     * @throws DonneeFinanciereException
     */
    public AllocationImpotent createAndCloseAllocationImpotent(ModificateurDroitDonneeFinanciere droit,
            AllocationImpotent newAllocationImpotent, boolean forceClose) throws AllocationImpotentException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    public AllocationsFamiliales createAndCloseAllocationsFamiliales(ModificateurDroitDonneeFinanciere droit,
            AllocationsFamiliales newAllocationsFamiliales, boolean forceClose) throws AllocationsFamilialesException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    AssuranceRenteViagere createAndCloseAssuranceRenteViagere(ModificateurDroitDonneeFinanciere droit,
            AssuranceRenteViagere newAssuranceRenteViagere, boolean forceClose) throws AssuranceRenteViagereException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    AssuranceVie createAndCloseAssuranceVie(ModificateurDroitDonneeFinanciere droit, AssuranceVie newAssuranceVie,
            boolean forceClose) throws AssuranceVieException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    public AutreApi createAndCloseAutreApi(ModificateurDroitDonneeFinanciere droit, AutreApi newAutreApi,
            boolean forceClo) throws AutreApiException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    AutreFortuneMobiliere createAndCloseAutreFortuneMobiliere(ModificateurDroitDonneeFinanciere droit,
            AutreFortuneMobiliere newAutreFortuneMobiliere, boolean forceClose) throws AutreFortuneMobiliereException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    public AutreRente createAndCloseAutreRente(ModificateurDroitDonneeFinanciere droit, AutreRente newAutreRente,
            boolean forceClose) throws AutreRenteException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    AutresDettesProuvees createAndCloseAutresDettesProuvees(ModificateurDroitDonneeFinanciere droit,
            AutresDettesProuvees newAutresDettesProuvees, boolean forceClose) throws AutresDettesProuveesException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    public AutresRevenus createAndCloseAutresRevenus(ModificateurDroitDonneeFinanciere droit,
            AutresRevenus newAutresRevenus, boolean forceClose) throws AutresRevenusException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    Betail createAndCloseBetail(ModificateurDroitDonneeFinanciere droit, Betail newBetail, boolean forceClose)
            throws BetailException, JadePersistenceException, DroitException, DonneeFinanciereException;

    BienImmobilierHabitationNonPrincipale createAndCloseBienImmobilierHabitationNonPrincipale(
            ModificateurDroitDonneeFinanciere droit,
            BienImmobilierHabitationNonPrincipale newBienImmobilierHabitationNonPrincipale, boolean forceClose)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    BienImmobilierNonHabitable createAndCloseBienImmobilierNonHabitable(ModificateurDroitDonneeFinanciere droit,
            BienImmobilierNonHabitable newBienImmobilierNonHabitable, boolean forceClose)
            throws BienImmobilierNonHabitableException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    BienImmobilierServantHabitationPrincipale createAndCloseBienImmobilierServantHabitationPrincipale(
            ModificateurDroitDonneeFinanciere droit,
            BienImmobilierServantHabitationPrincipale newBienImmobilierServantHabitationPrincipale, boolean forceClose)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    CapitalLPP createAndCloseCapitalLPP(ModificateurDroitDonneeFinanciere droit, CapitalLPP newCapitalLPP,
            boolean forceClose) throws CapitalLPPException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    public CompteBancaireCCP createAndCloseCompteBancaireCCP(ModificateurDroitDonneeFinanciere droit,
            CompteBancaireCCP newCompteBancaireCCP, boolean forceClose) throws CompteBancaireCCPException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    public ContratEntretienViager createAndCloseContratEntretienViager(ModificateurDroitDonneeFinanciere droit,
            ContratEntretienViager newContratEntretienViager, boolean forceClose)
            throws ContratEntretienViagerException, JadePersistenceException, DroitException, DonneeFinanciereException;

    public CotisationsPsal createAndCloseCotisationsPsal(ModificateurDroitDonneeFinanciere droit,
            CotisationsPsal newCotisationsPsal, boolean forceClose) throws CotisationsPsalException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Permet de mettre a jour une nouvelle cotisation et de fermer l'ancien cotisation Cette fonction demande
     * l'ancienne cotisation pour éviter de faire un select
     * 
     * @param droit
     * @param newCotisationsPsal
     * @param oldCotisationsPsal
     * @return
     * @throws CotisationsPsalException
     * @throws JadePersistenceException
     * @throws DroitException
     * @throws DonneeFinanciereException
     */
    public CotisationsPsal createAndCloseCotisationsPsal(ModificateurDroitDonneeFinanciere droit,
            CotisationsPsal newCotisationsPsal, CotisationsPsal oldCotisationsPsal, boolean forceClose)
            throws CotisationsPsalException, JadePersistenceException, DroitException, DonneeFinanciereException;

    public DessaisissementRevenu createAndCloseDessaisissementRevenu(ModificateurDroitDonneeFinanciere droit,
            DessaisissementRevenu newDonneeFinanciere, boolean forceClose)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    public IjApg createAndCloseIjApg(ModificateurDroitDonneeFinanciere droit, IjApg newIjApg, boolean forceClose)
            throws IjApgException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Création et fermeture d'une indemnite journaliere AI
     * 
     * @param droit
     * @param newIndemniteJOurnaliereAi
     * @return L'indemnité crée
     * @throws IndemniteJournaliereAiException
     * @throws JadePersistenceException
     * @throws DroitException
     * @throws DonneeFinanciereException
     */
    public IndemniteJournaliereAi createAndCloseIndemniteJournaliereAi(ModificateurDroitDonneeFinanciere droit,
            IndemniteJournaliereAi newIndemniteJOurnaliereAi, boolean forceClose)
            throws IndemniteJournaliereAiException, JadePersistenceException, DroitException, DonneeFinanciereException;

    public Loyer createAndCloseLoyer(ModificateurDroitDonneeFinanciere droit, Loyer newLoyer, boolean forceClose)
            throws LoyerException, JadePersistenceException, DroitException, DonneeFinanciereException;

    MarchandisesStock createAndCloseMarchandisesStock(ModificateurDroitDonneeFinanciere droit,
            MarchandisesStock newMarchandisesStock, boolean forceClose) throws MarchandisesStockException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    Numeraire createAndCloseNumeraire(ModificateurDroitDonneeFinanciere droit, Numeraire newNumeraire,
            boolean forceClose) throws NumeraireException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    public PensionAlimentaire createAndClosePensionAlimentaire(ModificateurDroitDonneeFinanciere droit,
            PensionAlimentaire newPensionAlimentaire, boolean forceClose) throws PensionAlimentaireException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    public PretEnversTiers createAndClosePretEnversTiers(ModificateurDroitDonneeFinanciere droit,
            PretEnversTiers newPretEnversTiers, boolean forceClose) throws PretEnversTiersException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    public RenteAvsAi createAndCloseRenteAvsAi(Droit droit, RenteAvsAi newRenteAvsAi, RenteAvsAi oldRenteAvsAi,
            boolean forClosePeriode) throws RenteAvsAiException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Création et fermeture d'une rente AVSAi
     * 
     * @param droit
     * @param newRenteAvsAi
     * @return La rente créer et fermé
     * @throws RenteAvsAiException
     * @throws JadePersistenceException
     * @throws DroitException
     * @throws DonneeFinanciereException
     */
    public RenteAvsAi createAndCloseRenteAvsAi(ModificateurDroitDonneeFinanciere droit, RenteAvsAi newRenteAvsAi,
            boolean forceClose) throws RenteAvsAiException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    public RevenuActiviteLucrativeDependante createAndCloseRevenuActiviteLucrativeDependante(
            ModificateurDroitDonneeFinanciere droit,
            RevenuActiviteLucrativeDependante newRevenuActiviteLucrativeDependante, boolean forceClose)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    public RevenuActiviteLucrativeIndependante createAndCloseRevenuActiviteLucrativeIndependante(
            ModificateurDroitDonneeFinanciere droit,
            RevenuActiviteLucrativeIndependante newRevenuActiviteLucrativeIndependante, boolean forceClose)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    public RevenuHypothetique createAndCloseRevenuHypothetique(ModificateurDroitDonneeFinanciere droit,
            RevenuHypothetique newRevenuHypothetique, boolean forceClose) throws RevenuHypothetiqueException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    public TaxeJournaliereHome createAndCloseTaxeJournaliereHome(ModificateurDroitDonneeFinanciere droit,
            TaxeJournaliereHome taxeJournaliereHome, boolean forceClose) throws TaxeJournaliereHomeException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    Titre createAndCloseTitre(ModificateurDroitDonneeFinanciere droit, Titre newTitre, boolean forceClose)
            throws TitreException, JadePersistenceException, DroitException, DonneeFinanciereException;

    Vehicule createAndCloseVehicule(ModificateurDroitDonneeFinanciere droit, Vehicule newVehicule, boolean forceClose)
            throws VehiculeException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Creation d'une entitee marchandise en stock pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param assuranceRenteViagere
     *            L'entite assuranceRenteViagere a creer
     * @return L'entitee assuranceRenteViagere creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AssuranceRenteViagere createAssuranceRenteViagere(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, AssuranceRenteViagere assuranceRenteViagere)
            throws DroitException, JadePersistenceException, AssuranceRenteViagereException, DonneeFinanciereException;

    /**
     * Creation d'une entitee assurance vie pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param assuranceVie
     *            L'entite assuranceVie a creer
     * @return L'entitee assuranceVie creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    AssuranceVie createAssuranceVie(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, AssuranceVie assuranceVie) throws AssuranceVieException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Creation d'une entitee AllocationsFamiliales pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param droitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param allocationsFamiliales
     *            L'entite allocationsFamiliales a creer
     * @return L'entitee allocationsFamiliales creee
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutreApi createAutreApi(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            AutreApi newAutreApi) throws AutreApiException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    AutreFortuneMobiliere createAutreFortuneMobiliere(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, AutreFortuneMobiliere newAutreFortuneMobiliere)
            throws DroitException, JadePersistenceException, AutreFortuneMobiliereException, DonneeFinanciereException;

    public AutreRente createAutreRente(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            AutreRente newAutreRente) throws DroitException, JadePersistenceException, AutreRenteException,
            DonneeFinanciereException;

    /**
     * Creation d'une entitee AutresDettesProuvees pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param autresDettesProuvees
     *            L'entite autresDettesProuvees a creer
     * @return L'entitee autresDettesProuvees creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    AutresDettesProuvees createAutresDettesProuvees(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, AutresDettesProuvees autresDettesProuvees)
            throws AutresDettesProuveesException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Creation d'une entitee AutresRevenus pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param autresRevenus
     *            L'entite autresRevenus a creer
     * @return L'entitee autresRevenus creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutresRevenus createAutresRevenus(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, AutresRevenus autresRevenus) throws AutresRevenusException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Creation d'une entitee betail pour une version de droit et un membre de famille Il n'est pas possible de modifier
     * une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite betail doit etre cree
     * @param newBetail
     *            L'entite betail a creer
     * @return L'entitee betail creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Betail createBetail(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            Betail newBetail) throws DroitException, JadePersistenceException, BetailException,
            DonneeFinanciereException;

    /**
     * Creation d'une entitee BienImmobilierHabitationNonPrincipale pour une version de droit et un membre de famille Il
     * n'est pas possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param bienImmobilierHabitationNonPrincipale
     *            L'entite bienImmobilierHabitationNonPrincipale a creer
     * @return L'entitee bienImmobilierHabitationNonPrincipale creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    BienImmobilierHabitationNonPrincipale createBienImmobilierHabitationNonPrincipale(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Creation d'une entitee BienImmobilierNonHabitable pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param bienImmobilierNonHabitable
     *            L'entite bienImmobilierNonHabitable a creer
     * @return L'entitee bienImmobilierNonHabitable creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    BienImmobilierNonHabitable createBienImmobilierNonHabitable(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Creation d'une entitee bienImmobilierServantHabitationPrincipale pour une version de droit et un membre de
     * famille Il n'est pas possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param bienImmobilierServantHabitationPrincipale
     *            L'entite bienImmobilierServantHabitationPrincipale a creer
     * @return L'entitee bienImmobilierServantHabitationPrincipale creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    BienImmobilierServantHabitationPrincipale createBienImmobilierServantHabitationPrincipale(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Creation d'une entitee CapitalLPP pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param capitalLPP
     *            L'entite capitalLPP a creer
     * @return L'entitee capitalLPP creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    CapitalLPP createCapitalLPP(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            CapitalLPP capitalLPP) throws CapitalLPPException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Creation d'une entitee compteBancaireCCP pour une version de droit et un membre de famille Il n'est pas possible
     * de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param compteBancaireCCP
     *            L'entite compteBancaireCCP a creer
     * @return L'entitee compteBancaireCCP creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CompteBancaireCCP createCompteBancaireCCP(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, CompteBancaireCCP compteBancaireCCP)
            throws CompteBancaireCCPException, JadePersistenceException, DroitException, DonneeFinanciereException;

    public String[] calculDroitPlageCalcul(Droit droit, boolean retroactif) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException, CalculException, JadePersistenceException, DemandeException;

    /**
     * Creation d'une entitee ContratEntretienViager pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param newContratEntretienViager
     *            L'entite newContratEntretienViager a creer
     * @return L'entitee newContratEntretienViager creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ContratEntretienViager createContratEntretienViager(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, ContratEntretienViager newContratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Creation d'une entitee cotisationsPsal pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param cotisationsPsal
     *            L'entite cotisationsPsal a creer
     * @return L'entitee cotisationsPsal creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CotisationsPsal createCotisationsPsal(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, CotisationsPsal cotisationsPsal)
            throws CotisationsPsalException, JadePersistenceException, DroitException, DonneeFinanciereException;

    public DessaisissementFortune createDessaisissementFortune(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, DessaisissementFortune dessaisissementFortune)
            throws DroitException, JadePersistenceException, DessaisissementFortuneException, DonneeFinanciereException;

    public DessaisissementRevenu createDessaisissementRevenu(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, DessaisissementRevenu dessaisissementRevenu) throws DroitException,
            JadePersistenceException, DessaisissementRevenuException, DonneeFinanciereException;

    /**
     * Creation du droit initial d'une demande La creation d'un droit initial n'est passible que pour une demande qui ne
     * contient pas de droit
     * 
     * @param demande
     *            La demande pour laquelle un droit initial doit etre cree
     * @return le droit initial
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DonneesPersonnellesException
     */
    public Droit createDroitInitial(Demande demande) throws DroitException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DonneesPersonnellesException;

    /**
     * @param demande
     *            , la demande sur laquelle se base le droit
     * @param fratrie
     *            , la fratrie, liste des enfants plus le requérant(enfant)
     * @return le Droit
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DonneesPersonnellesException
     */
    public Droit createDroitInitialForFratrie(Demande demande, ArrayList<MembreFamilleVO> fratrie)
            throws DroitException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            DonneesPersonnellesException;

    /**
     * Creation d'une entitee ijApg pour une version de droit et un membre de famille Il n'est pas possible de modifier
     * une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param ijApg
     *            L'entite ijApg a creer
     * @return L'entitee ijApg creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     * @throws IjApgException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public IjApg createIjApg(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille, IjApg ijApg)
            throws DroitException, JadePersistenceException, DonneeFinanciereException, IjApgException;

    /**
     * Création d'une indemnite journaliere AI
     * 
     * @param droit
     * @param droitMembreFamille
     * @param newIndemniteJournaliereAi
     * @return L'indemnite crée
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws IndemniteJournaliereAiException
     * @throws DonneeFinanciereException
     */
    IndemniteJournaliereAi createIndemniteJournaliereAi(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, IndemniteJournaliereAi newIndemniteJournaliereAi)
            throws DroitException, JadePersistenceException, IndemniteJournaliereAiException, DonneeFinanciereException;

    /**
     * Creation d'une entitee Loyer pour une version de droit et un membre de famille Il n'est pas possible de modifier
     * une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param Loyer
     *            L'entite loyer a creer
     * @return L'entitee autresRevenus creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws LoyerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Loyer createLoyer(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            Loyer loyer) throws LoyerException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Creation d'une entitee marchandise en stock pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param droitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param newMarchandisesStock
     *            L'entite marchandisesStock a creer
     * @return L'entitee marchandisesStock creee
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    MarchandisesStock createMarchandisesStock(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, MarchandisesStock newMarchandisesStock)
            throws MarchandisesStockException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Creation d'une entitee numeraire pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param numeraire
     *            L'entite marchandisesStock a creer
     * @return L'entitee numeraire creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Numeraire createNumeraire(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, Numeraire numeraire) throws DroitException,
            JadePersistenceException, NumeraireException, DonneeFinanciereException;

    public PensionAlimentaire createPensionAlimentaire(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, PensionAlimentaire pensionAlimentaire)
            throws PensionAlimentaireException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Creation d'une entitee pretEnversTiers pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droitMembreFamilleEtendu
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param newPretEnversTiers
     *            L'entite pretEnversTiers a creer
     * @return L'entitee pretEnversTiers creee
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PretEnversTiers createPretEnversTiers(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, PretEnversTiers newPretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Création d'une rente AVSAi
     * 
     * @param droit
     * @param droitMembreFamille
     * @param newRenteAvsAi
     * @return La renet avsAi crée
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws RenteAvsAiException
     * @throws DonneeFinanciereException
     */
    RenteAvsAi createRenteAvsAi(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            RenteAvsAi newRenteAvsAi) throws DroitException, JadePersistenceException, RenteAvsAiException,
            DonneeFinanciereException;

    /**
     * Creation d'une entitee RevenuActiviteLucrativeDependante pour une version de droit et un membre de famille Il
     * n'est pas possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param droitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param revenuActiviteLucrativeDependante
     *            L'entite revenuActiviteLucrativeDependante a creer
     * @return L'entitee revenuActiviteLucrativeDependante creee
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeDependante createRevenuActiviteLucrativeDependante(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Creation d'une entitee RevenuActiviteLucrativeIndependante pour une version de droit et un membre de famille Il
     * n'est pas possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param droitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param revenuActiviteLucrativeIndependante
     *            L'entite revenuActiviteLucrativeIndependante a creer
     * @return L'entitee revenuActiviteLucrativeIndependante creee
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeIndependante createRevenuActiviteLucrativeIndependante(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Creation d'une entitee RevenuHypothetique pour une version de droit et un membre de famille Il n'est pas possible
     * de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param droitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param newRevenuHypothetique
     *            L'entite RevenuHypothetique a creer
     * @return L'entitee RevenuHypothetique creee
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuHypothetique createRevenuHypothetique(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, RevenuHypothetique newRevenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException, DroitException, DonneeFinanciereException;

    public SimpleLibelleContratEntretienViager createSimpleLibelleContratEntretienViager(
            String idContratEntretienViager, SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager)
            throws DroitException, JadePersistenceException, SimpleLibelleContratEntretienViagerException;

    public SimpleTypeFraisObtentionRevenu createSimpleTypeFraisObtentionRevenu(
            String idRevenuActiviteLucrativeDependante, SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws DroitException, JadePersistenceException, SimpleTypeFraisObtentionRevenuException;

    /**
     * Creation d'une entitee taxeJournaliereHome pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param autresRevenus
     *            L'entite taxeJournaliereHome a creer
     * @return L'entitee taxeJournaliereHome creee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TaxeJournaliereHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */

    public TaxeJournaliereHome createTaxeJournaliereHome(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, TaxeJournaliereHome taxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Creation d'une entitee titre pour une version de droit et un membre de famille Il n'est pas possible de modifier
     * une version d'un droit validé par une decision
     * 
     * @param droit
     * @param droitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pretEnversTiers doit etre
     *            cree
     * @param titre
     *            L'entite titre a creer
     * @return L'entitee titre creee
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    Titre createTitre(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            Titre titre) throws TitreException, JadePersistenceException, DroitException, DonneeFinanciereException;

    public TypeFraisObtentionRevenu createTypeFraisObtentionRevenu(String idRevenuActiviteLucrativeDependante,
            TypeFraisObtentionRevenu typeFraisObtentionRevenu) throws DroitException, JadePersistenceException,
            TypeFraisObtentionRevenuException, DonneeFinanciereException;

    Vehicule createVehicule(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            Vehicule newVehicule) throws DroitException, JadePersistenceException, VehiculeException,
            DonneeFinanciereException;

    /**
     * Supression d'une allocation impotent
     * 
     * @param droit
     * @param allocationImpotent
     * @return L'allocation supprimé
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws AllocationImpotentException
     */
    AllocationImpotent deleteAllocationImpotent(ModificateurDroitDonneeFinanciere droit,
            AllocationImpotent allocationImpotent) throws DroitException, JadePersistenceException,
            AllocationImpotentException;

    public AllocationsFamiliales deleteAllocationsFamiliales(ModificateurDroitDonneeFinanciere droit,
            AllocationsFamiliales allocationsFamiliales) throws AllocationsFamilialesException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Suppression d'une entitee assuranceRenteViagere pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param assuranceRenteViagere
     *            L'entite assuranceRenteViagere a supprimer
     * @return L'entitee assuranceRenteViagere supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AssuranceRenteViagere deleteAssuranceRenteViagere(ModificateurDroitDonneeFinanciere droit,
            AssuranceRenteViagere assuranceRenteViagere) throws DroitException, JadePersistenceException,
            AssuranceRenteViagereException;

    /**
     * Suppression d'une entitee assuranceVie pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param assuranceVie
     *            L'entite assuranceVie a supprimer
     * @return L'entitee assuranceVie supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    AssuranceVie deleteAssuranceVie(ModificateurDroitDonneeFinanciere droit, AssuranceVie assuranceVie)
            throws AssuranceVieException, JadePersistenceException, DroitException, DonneeFinanciereException;

    public AutreApi deleteAutreApi(ModificateurDroitDonneeFinanciere droit, AutreApi autreApi) throws DroitException,
            JadePersistenceException, AutreApiException;

    AutreFortuneMobiliere deleteAutreFortuneMobiliere(ModificateurDroitDonneeFinanciere droit,
            AutreFortuneMobiliere autreFortuneM) throws DroitException, JadePersistenceException,
            AutreFortuneMobiliereException;

    public AutreRente deleteAutreRente(ModificateurDroitDonneeFinanciere droit, AutreRente autreRente)
            throws DroitException, JadePersistenceException, AutreRenteException;

    /**
     * Suppression d'une entitee autresDettesProuvees pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param autresDettesProuvees
     *            L'entite autresDettesProuvees a supprimer
     * @return L'entitee autresDettesProuvees supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    AutresDettesProuvees deleteAutresDettesProuvees(ModificateurDroitDonneeFinanciere droit,
            AutresDettesProuvees autresDettesProuvees) throws AutresDettesProuveesException, JadePersistenceException,
            DroitException, DonneeFinanciereException;

    /**
     * Suppression d'une entitee autresRevenus pour une version de droit et un membre de famille Il n'est pas possible
     * de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param autresRevenus
     *            L'entite autresRevenus a supprimer
     * @return L'entitee autresRevenus supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutresRevenus deleteAutresRevenus(ModificateurDroitDonneeFinanciere droit, AutresRevenus autresRevenus)
            throws AutresRevenusException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Suppression d'une entitee betail pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param betail
     *            L'entite betail a supprimer
     * @return L'entitee betail supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Betail deleteBetail(ModificateurDroitDonneeFinanciere droit, Betail betail) throws DroitException,
            JadePersistenceException, BetailException;

    /**
     * Suppression d'une entitee bienImmobilierHabitationNonPrincipale pour une version de droit et un membre de famille
     * Il n'est pas possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param bienImmobilierHabitationNonPrincipale
     *            L'entite bienImmobilierHabitationNonPrincipale a supprimer
     * @return L'entitee bienImmobilierHabitationNonPrincipale supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    BienImmobilierHabitationNonPrincipale deleteBienImmobilierHabitationNonPrincipale(
            ModificateurDroitDonneeFinanciere droit,
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Suppression d'une entitee bienImmobilierNonHabitable pour une version de droit et un membre de famille Il n'est
     * pas possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param bienImmobilierNonHabitable
     *            L'entite bienImmobilierNonHabitable a supprimer
     * @return L'entitee bienImmobilierNonHabitable supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    BienImmobilierNonHabitable deleteBienImmobilierNonHabitable(ModificateurDroitDonneeFinanciere droit,
            BienImmobilierNonHabitable bienImmobilierNonHabitable) throws BienImmobilierNonHabitableException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Suppression d'une entitee bienImmobilierServantHabitationPrincipale pour une version de droit et un membre de
     * famille Il n'est pas possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param bienImmobilierServantHabitationPrincipale
     *            L'entite bienImmobilierServantHabitationPrincipale a supprimer
     * @return L'entitee bienImmobilierServantHabitationPrincipale supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    BienImmobilierServantHabitationPrincipale deleteBienImmobilierServantHabitationPrincipale(
            ModificateurDroitDonneeFinanciere droit,
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Suppression d'une entitee capitalLPP pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param capitalLPP
     *            L'entite capitalLPP a supprimer
     * @return L'entitee capitalLPP supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    CapitalLPP deleteCapitalLPP(ModificateurDroitDonneeFinanciere droit, CapitalLPP capitalLPP)
            throws CapitalLPPException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Suppression d'une entitee compteBancaireCCP pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param compteBancaireCCP
     *            L'entite compteBancaireCCP a supprimer
     * @return L'entitee compteBancaireCCP supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CompteBancaireCCP deleteCompteBancaireCCP(ModificateurDroitDonneeFinanciere droit,
            CompteBancaireCCP compteBancaireCCP) throws CompteBancaireCCPException, JadePersistenceException,
            DroitException, DonneeFinanciereException;

    /**
     * Suppression d'une entitee contratEntretienViager pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param contratEntretienViager
     *            L'entite contratEntretienViager a supprimer
     * @return L'entitee contratEntretienViager supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ContratEntretienViager deleteContratEntretienViager(ModificateurDroitDonneeFinanciere droit,
            ContratEntretienViager contratEntretienViager) throws ContratEntretienViagerException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Suppression d'une entitee cotisationsPsal pour une version de droit et un membre de famille Il n'est pas possible
     * de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param cotisationsPsal
     *            L'entite cotisationsPsal a supprimer
     * @return L'entitee cotisationsPsal supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CotisationsPsal deleteCotisationsPsal(ModificateurDroitDonneeFinanciere droit,
            CotisationsPsal cotisationsPsal) throws CotisationsPsalException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    public DessaisissementFortune deleteDessaisissementFortune(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, DessaisissementFortune dessaisissementFortune)
            throws DroitException, JadePersistenceException;

    public DessaisissementRevenu deleteDessaisissementRevenu(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, DessaisissementRevenu dessaisissementRevenu) throws DroitException,
            JadePersistenceException;

    /**
     * Suppression d'une entitee ijApg pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param ijApg
     *            L'entite ijApg a supprimer
     * @return L'entitee titre supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws IjApgException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public IjApg deleteIjApg(ModificateurDroitDonneeFinanciere droit, IjApg ijApg) throws IjApgException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Suppression d'une entité indemnité journaliere
     * 
     * @param droit
     * @param indemniteJournaliereAi
     * @return L'indenité journaliere supprimé
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws IndemniteJournaliereAiException
     */
    IndemniteJournaliereAi deleteIndemniteJournaliereAi(ModificateurDroitDonneeFinanciere droit,
            IndemniteJournaliereAi indemniteJournaliereAi) throws DroitException, JadePersistenceException,
            IndemniteJournaliereAiException;

    public Loyer deleteLoyer(ModificateurDroitDonneeFinanciere droit, Loyer loyer) throws DroitException,
            JadePersistenceException;

    /**
     * Suppression d'une entitee marchandisesStock pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param marchandisesStock
     *            L'entite marchandisesStock a supprimer
     * @return L'entitee marchandisesStock supprimee
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    MarchandisesStock deleteMarchandisesStock(ModificateurDroitDonneeFinanciere droit,
            MarchandisesStock marchandisesStock) throws MarchandisesStockException, JadePersistenceException,
            DroitException, DonneeFinanciereException;

    /**
     * Suppression d'une entitee numeraire pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param numeraire
     *            L'entite numeraire a supprimer
     * @return L'entitee numeraire supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Numeraire deleteNumeraire(ModificateurDroitDonneeFinanciere droit, Numeraire numeraire)
            throws DroitException, JadePersistenceException, NumeraireException;

    /**
     * Suppression d'une entitee PensionAlimentaire pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param pensionAlimentaire
     *            L'entite pensionAlimentaire a supprimer
     * @return L'entitee pensionAlimentaire supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PensionAlimentaire deletePensionAlimentaire(ModificateurDroitDonneeFinanciere droit,
            PensionAlimentaire pensionAlimentaire) throws PensionAlimentaireException, JadePersistenceException,
            DroitException, DonneeFinanciereException;

    /**
     * Suppression d'une entitee pretEnversTiers pour une version de droit et un membre de famille Il n'est pas possible
     * de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param pretEnversTiers
     *            L'entite pretEnversTiers a supprimer
     * @return L'entitee pretEnversTiers supprimee
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PretEnversTiers deletePretEnversTiers(ModificateurDroitDonneeFinanciere droit,
            PretEnversTiers pretEnversTiers) throws PretEnversTiersException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Suppression d'une rente avs-ai
     * 
     * @param droit
     * @param renteAvsAi
     * @return La rente AVSAi supprimé
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws RenteAvsAiException
     */
    RenteAvsAi deleteRenteAvsAi(ModificateurDroitDonneeFinanciere droit, RenteAvsAi renteAvsAi) throws DroitException,
            JadePersistenceException, RenteAvsAiException;

    /**
     * Suppression d'une entitee revenuActiviteLucrativeDependante pour une version de droit et un membre de famille Il
     * n'est pas possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param revenuActiviteLucrativeDependante
     *            L'entite revenuActiviteLucrativeDependante a supprimer
     * @return L'entitee revenuActiviteLucrativeDependante supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeDependante deleteRevenuActiviteLucrativeDependante(
            ModificateurDroitDonneeFinanciere droit, RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Suppression d'une entitee revenuActiviteLucrativeIndependante pour une version de droit et un membre de famille
     * Il n'est pas possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param revenuActiviteLucrativeIndependante
     *            L'entite revenuActiviteLucrativeIndependante a supprimer
     * @return L'entitee revenuActiviteLucrativeIndependante supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeIndependante deleteRevenuActiviteLucrativeIndependante(
            ModificateurDroitDonneeFinanciere droit,
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Suppression d'une entitee revenuHypothetique pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param revenuHypothetique
     *            L'entite revenuHypothetique a supprimer
     * @return L'entitee revenuHypothetique supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuHypothetique deleteRevenuHypothetique(ModificateurDroitDonneeFinanciere droit,
            RevenuHypothetique revenuHypothetique) throws RevenuHypothetiqueException, JadePersistenceException,
            DroitException, DonneeFinanciereException;

    public SimpleLibelleContratEntretienViager deleteSimpleLibelleContratEntretienViager(
            ModificateurDroitDonneeFinanciere droit,
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    public SimpleTypeFraisObtentionRevenu deleteSimpleTypeFraisObtentionRevenu(ModificateurDroitDonneeFinanciere droit,
            SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws SimpleTypeFraisObtentionRevenuException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Suppression d'une entitee taxeJournaliereHome pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param taxeJournaliereHome
     *            L'entite cotisationsPsal a supprimer
     * @return L'entitee taxeJournaliereHome supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TaxeJournaliereHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TaxeJournaliereHome deleteTaxeJournaliereHome(ModificateurDroitDonneeFinanciere droit,
            TaxeJournaliereHome taxeJournaliereHome) throws DroitException, JadePersistenceException;

    /**
     * Suppression d'une entitee titre pour une version de droit et un membre de famille Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param titre
     *            L'entite titre a supprimer
     * @return L'entitee titre supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    Titre deleteTitre(ModificateurDroitDonneeFinanciere droit, Titre titre) throws TitreException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Suppression d'une entitee typeFraisObtentionRevenu pour une version de droit et un membre de famille Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'entité à supprimer dépend
     * @param typeFraisObtentionRevenu
     *            L'entite typeFraisObtentionRevenu a supprimer
     * @return L'entitee typeFraisObtentionRevenu supprimee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeFraisObtentionRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TypeFraisObtentionRevenu deleteTypeFraisObtentionRevenu(ModificateurDroitDonneeFinanciere droit,
            TypeFraisObtentionRevenu typeFraisObtentionRevenu) throws TypeFraisObtentionRevenuException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    Vehicule deleteVehicule(ModificateurDroitDonneeFinanciere droit, Vehicule vehicule) throws DroitException,
            JadePersistenceException, VehiculeException;

    public boolean existDroit(String idDemande) throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeNoBusinessLogSessionError;

    /**
     * Permet de trouver le droit courant
     * 
     * @param idDroit
     * @return Droit current
     * @throws JadePersistenceException
     * @throws DroitException
     */
    public Droit getCurrentVersionDroit(String idDroit) throws DroitException, JadePersistenceException;

    /**
     * Supprime les pc Accordeé de la version du droit, les répartitions des créances et les desisions. Met à jours la
     * version du droit dans l'état "au calcule"
     * 
     * @param droit
     *            Le droit mis a jour
     * @return Le droit sauvegarde
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Droit processOnUpdateDonneFinanciere(Droit droit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une allocation impotent
     * 
     * @param idAllocationImpotent
     * @return
     * @throws JadePersistenceException
     */
    AllocationImpotent readAllocationImpotent(String idAllocationImpotent) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une AllocationsFamiliales
     * 
     * @param id
     *            L'identifiant de l'allocationsFamiliales
     * @return l'allocationsFamiliales chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AllocationsFamiliales readAllocationsFamiliales(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire une assurence rente viagère
     * 
     * @param id
     *            L'identifiant de la assurence rente viagère
     * @return l'assurence rente viagère chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AssuranceRenteViagere readAssuranceRenteViagere(String id) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une AssuranceVie
     * 
     * @param id
     *            L'identifiant de l'assuranceVie
     * @return l'assuranceVie chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    AssuranceVie readAssuranceVie(String id) throws JadePersistenceException, DroitException;

    public AutreApi readAutreApi(String id) throws JadePersistenceException;

    AutreFortuneMobiliere readAutreFortuneMobiliere(String id) throws JadePersistenceException;

    public AutreRente readAutreRente(String id) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une AutresDettesProuvees
     * 
     * @param id
     *            L'identifiant de AutresDettesProuvees
     * @return l'AutresDettesProuvees chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    AutresDettesProuvees readAutresDettesProuvees(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire une AutresRevenus
     * 
     * @param id
     *            L'identifiant de l'AutresRevenus
     * @return l'AutresRevenus chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AutresRevenus readAutresRevenus(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire une betail
     * 
     * @param id
     *            L'identifiant du betail
     * @return le betail chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Betail readBetail(String id) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire un BienImmobilierHabitationNonPrincipale
     * 
     * @param id
     *            L'identifiant du BienImmobilierHabitationNonPrincipale
     * @return le BienImmobilierHabitationNonPrincipale chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    BienImmobilierHabitationNonPrincipale readBienImmobilierHabitationNonPrincipale(String id)
            throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire un BienImmobilierNonHabitable
     * 
     * @param id
     *            L'identifiant du BienImmobilierNonHabitable
     * @return le BienImmobilierNonHabitable chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    BienImmobilierNonHabitable readBienImmobilierNonHabitable(String id) throws JadePersistenceException,
            DroitException;

    /**
     * Permet de charger en mémoire un BienImmobilierServantHabitationPrincipale
     * 
     * @param id
     *            L'identifiant du BienImmobilierServantHabitationPrincipale
     * @return le BienImmobilierServantHabitationPrincipale chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    BienImmobilierServantHabitationPrincipale readBienImmobilierServantHabitationPrincipale(String id)
            throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire une CapitalLPP
     * 
     * @param id
     *            L'identifiant du CapitalLPP
     * @return le CapitalLPP chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    CapitalLPP readCapitalLPP(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire une CompteBancaireCCP
     * 
     * @param id
     *            L'identifiant du CompteBancaireCCP
     * @return le CompteBancaireCCP chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CompteBancaireCCP readCompteBancaireCCP(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire une ContratEntretienViager
     * 
     * @param id
     *            L'identifiant du ContratEntretienViager
     * @return le ContratEntretienViager chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public ContratEntretienViager readContratEntretienViager(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire une CotisationsPsal
     * 
     * @param id
     *            L'identifiant du CotisationsPsal
     * @return le CotisationsPsal chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CotisationsPsal readCotisationsPsal(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de retrouve la version du droit courante
     * 
     * @param idDroit
     * @return
     * @throws DroitException
     * @throws JadePersistenceException
     */
    public VersionDroit readCurrentVersionDroit(String idDroit) throws DroitException, JadePersistenceException;

    public DessaisissementFortune readDessaisissementFortune(String id) throws JadePersistenceException;

    public DessaisissementRevenu readDessaisissementRevenu(String id) throws JadePersistenceException;

    public DonneesPersonnelles readDonneesPersonnelles(String id) throws DroitException, JadePersistenceException,
            DonneesPersonnellesException, JadeApplicationServiceNotAvailableException;

    public Droit readDroit(String idDroit) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire un droit
     * 
     * @param idDroit
     *            L'identifiant du droit à charger en mémoire
     * @return Le droit chargé en memoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service des droits
     */
    public ModificateurDroitDonneeFinanciere readDroitDonneeFinanciere(String idDroit) throws JadePersistenceException,
            DroitException;

    public Droit readDroitFromVersion(String idVersionDroit) throws DroitException, JadePersistenceException;

    public DroitMembreFamille readDroitMembreFamille(String idDroitMembreFamille) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire une IjApg
     * 
     * @param id
     *            L'identifiant de IjApg
     * @return IjApg chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public IjApg readIjApg(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire une indemnite journalier de l'AI
     * 
     * @param ididIndemniteJournaliereAi
     * @return L'indemnité Journaliere chargé en mémoire
     * @throws JadePersistenceException
     */
    IndemniteJournaliereAi readIndemniteJournaliereAi(String idIndemniteJournaliereAi) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une allocation impotent
     * 
     * @param idAllocationImpotent
     * @return
     * @throws JadePersistenceException
     */
    public Loyer readLoyer(String idLoyer) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une marchandise en stock
     * 
     * @param id
     *            L'identifiant de la marchandise en stock
     * @return la marchandise en stock chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    MarchandisesStock readMarchandisesStock(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire un numeraire
     * 
     * @param id
     *            L'identifiant du numeraire
     * @return le numeraire chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Numeraire readNumeraire(String id) throws JadePersistenceException;

    public PensionAlimentaire readPensionAlimentaire(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire un pret envers tiers
     * 
     * @param id
     *            L'identifiant du pret envers tiers
     * @return le pret envers tiers chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service des droits
     */
    public PretEnversTiers readPretEnversTiers(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire une une rente de l'avs-ai
     * 
     * @param idRenteAvsAi
     * @return La rente VAS-Ai chrgée en mémoire
     * @throws JadePersistenceException
     */
    RenteAvsAi readRenteAvsAi(String idRenteAvsAi) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire un pret RevenuActiviteLucrativeDependante tiers
     * 
     * @param id
     *            L'identifiant du RevenuActiviteLucrativeDependante
     * @return le RevenuActiviteLucrativeDependante chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service des droits
     */
    public RevenuActiviteLucrativeDependante readRevenuActiviteLucrativeDependante(String id)
            throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire un pret RevenuActiviteLucrativeIndependante tiers
     * 
     * @param id
     *            L'identifiant du RevenuActiviteLucrativeIndependante
     * @return le RevenuActiviteLucrativeIndependante chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service des droits
     */
    public RevenuActiviteLucrativeIndependante readRevenuActiviteLucrativeIndependante(String id)
            throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire un pret RevenuHypothetique tiers
     * 
     * @param id
     *            L'identifiant du RevenuHypothetique
     * @return le RevenuHypothetique chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service des droits
     */
    public RevenuHypothetique readRevenuHypothetique(String id) throws JadePersistenceException, DroitException;

    public SimpleTypeFraisObtentionRevenu readSimpleTypeFraisObtentionRevenu(String id) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une taxeJournaliereHome
     * 
     * @param id
     *            L'identifiant de la taxeJournaliereHome
     * @return la taxeJournaliereHome en stock chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     */

    public TaxeJournaliereHome readTaxeJournaliereHome(String idTaxeJournaliereHome) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire un pret Titre tiers
     * 
     * @param id
     *            L'identifiant du Titre
     * @return le Titre chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service des droits
     */
    Titre readTitre(String id) throws JadePersistenceException, DroitException;

    /**
     * Permet de charger en mémoire un pret Titre tiers
     * 
     * @param id
     *            L'identifiant du Titre
     * @return le Titre chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service des droits
     */
    public TypeFraisObtentionRevenu readTypeFraisObtentionRevenu(String id) throws JadePersistenceException;

    Vehicule readVehicule(String id) throws JadePersistenceException;

    public VersionDroit readVersionDroit(String idVersionDroit) throws JadePersistenceException, DroitException;

    /**
     * Point d'entrée publique pour la creation des données fincières devant être mise à jour pour une version de droit
     * 
     * @param versionDroit
     * @param donneeFinanciere
     * @param service
     * @throws DroitException
     */
    public void saveDonneefinanciereCalculMoisSuivant(AbstractDonneeFinanciereModel donneeFinanciere,
            JadeApplicationService service) throws DroitException;

    /**
     * Permet de chercher des TaxeJournaliereHome lié a un droit selon un modèle de critères.
     * 
     * @param TaxeJournaliereHome
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws TaxeJournaliereHomeException
     */
    public TaxeJournaliereHomeDroitSearch search(TaxeJournaliereHomeDroitSearch search) throws HomeException,
            JadePersistenceException, TaxeJournaliereHomeException;

    /**
     * * Permet de rechercher des AllocationsImpotentes selon le modèle de critères
     * 
     * @param searchModel
     * @return
     * @throws JadePersistenceException
     * @throws DroitException
     */
    AllocationImpotentSearch searchAllocationImpotent(AllocationImpotentSearch searchModel)
            throws JadePersistenceException, DroitException;

    /**
     * Permet de chercher des AllocationsFamiliales selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AllocationsFamilialesSearch searchAllocationsFamiliales(AllocationsFamilialesSearch searchModel)
            throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des assurances rente viagère selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AssuranceRenteViagereSearch searchAssuranceRenteViagere(AssuranceRenteViagereSearch searchModel)
            throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des AssuranceVie selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    AssuranceVieSearch searchAssuranceVie(AssuranceVieSearch searchModel) throws DroitException,
            JadePersistenceException;

    public AutreApiSearch searchAutreApi(AutreApiSearch searchModel) throws JadePersistenceException, DroitException;

    AutreFortuneMobiliereSearch searchAutreFortuneMobiliere(AutreFortuneMobiliereSearch searchModel)
            throws JadePersistenceException, DroitException;

    public AutreRenteSearch searchAutreRente(AutreRenteSearch searchModel) throws JadePersistenceException,
            DroitException;

    /**
     * Permet de chercher des AutresDettesProuvees selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    AutresDettesProuveesSearch searchAutresDettesProuvees(AutresDettesProuveesSearch searchModel)
            throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des AutresRevenus selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AutresRevenusSearch searchAutresRevenus(AutresRevenusSearch searchModel) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de chercher des betails selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public BetailSearch searchBetail(BetailSearch searchModel) throws JadePersistenceException, DroitException;

    /**
     * Permet de chercher des BienImmobilierHabitationNonPrincipale selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    BienImmobilierHabitationNonPrincipaleSearch searchBienImmobilierHabitationNonPrincipale(
            BienImmobilierHabitationNonPrincipaleSearch searchModel) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des BienImmobilierNonHabitable selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    BienImmobilierNonHabitableSearch searchBienImmobilierNonHabitable(BienImmobilierNonHabitableSearch searchModel)
            throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des BienImmobilierServantHabitationPrincipale selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    BienImmobilierServantHabitationPrincipaleSearch searchBienImmobilierServantHabitationPrincipale(
            BienImmobilierServantHabitationPrincipaleSearch searchModel) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de chercher des CapitalLPP selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    CapitalLPPSearch searchCapitalLPP(CapitalLPPSearch searchModel) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des CompteBancaireCCP selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CompteBancaireCCPSearch searchCompteBancaireCCP(CompteBancaireCCPSearch searchModel) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de chercher des ContratEntretienViager selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public ContratEntretienViagerSearch searchContratEntretienViager(ContratEntretienViagerSearch searchModel)
            throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des CotisationsPsal selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CotisationsPsalSearch searchCotisationsPsal(CotisationsPsalSearch searchModel) throws DroitException,
            JadePersistenceException;

    public DessaisissementFortuneSearch searchDessaisissementFortune(DessaisissementFortuneSearch searchModel)
            throws JadePersistenceException, DroitException;

    public DessaisissementFortuneAutoSearch searchDessaisissementFortuneAuto(
            DessaisissementFortuneAutoSearch searchModel) throws JadePersistenceException, DroitException;

    public DessaisissementRevenuSearch searchDessaisissementRevenu(DessaisissementRevenuSearch searchModel)
            throws JadePersistenceException, DroitException;

    public DessaisissementRevenuAutoSearch searchDessaisissementRevenuAuto(DessaisissementRevenuAutoSearch searchModel)
            throws JadePersistenceException, DroitException;

    public DonneesPersonnellesSearch searchDonneesPersonnelles(DonneesPersonnellesSearch search) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de chercher des droits selon un modèle de critères.
     * 
     * @param droitSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DroitSearch searchDroit(DroitSearch droitSearch) throws JadePersistenceException, DroitException;

    /**
     * Permet de chercher un droit pour les pages de donnée financière selon un modèle de critères.
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public ModificateurDroitDonneeFinanciereSearch searchDroitDonneeFinanciere(
            ModificateurDroitDonneeFinanciereSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher les membres de familles d'un requerant lié à un droit
     * 
     * @param membreSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DroitMembreFamilleSearch searchDroitMembreFamille(DroitMembreFamilleSearch membreSearch)
            throws JadePersistenceException, DroitException;

    /**
     * Permet de rechercher des DroitMembreFamilleEtendu selon le modèle de critères
     * 
     * @param droitMembreFamilleEtenduSearch
     * @return
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public DroitMembreFamilleEtenduSearch searchDroitMemebreFamilleEtendu(
            DroitMembreFamilleEtenduSearch droitMembreFamilleEtenduSearch) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet de chercher les donnéese de fortune particulière liées à un droit
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public FortuneParticuliereSearch searchFortuneParticuliere(FortuneParticuliereSearch search) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de chercher les donnéese de fortune usuelle liées à un droit
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public FortuneUsuelleSearch searchFortuneUsuelle(FortuneUsuelleSearch search) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de rechercher des habitats selon le modèle de critères
     * 
     * @param searchModel
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     * @throws DroitException
     */
    public HabitatSearch searchHabitat(HabitatSearch searchModel) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des betails selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public IjApgSearch searchIjApg(IjApgSearch searchModel) throws JadePersistenceException, DroitException;

    /**
     * Permet de rechercher des IndemnitesJournalieresAi selon le modèle de critères
     * 
     * @param searchModel
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     * @throws DroitException
     */
    IndemniteJournaliereAiSearch searchIndemniteJournaliereAi(IndemniteJournaliereAiSearch searchModel)
            throws JadePersistenceException, DroitException;

    /**
     * * Permet de rechercher des AllocationsImpotentes selon le modèle de critères
     * 
     * @param searchModel
     * @return
     * @throws JadePersistenceException
     * @throws DroitException
     */
    public LoyerSearch searchLoyer(AbstractDonneeFinanciereSearchModel searchModel) throws JadePersistenceException,
            DroitException;

    /**
     * Permet de chercher des marchandises en stock selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    MarchandisesStockSearch searchMarchandisesStock(MarchandisesStockSearch searchModel) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de chercher les membres de familles, avec leurs données personnelles, d'un requerant lié à un droit
     * 
     * @param membreSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public MembreFamilleEtenduSearch searchMembreFamilleEtendu(MembreFamilleEtenduSearch membreSearch)
            throws JadePersistenceException, DroitException;

    /**
     * Permet de chercher des numéraires selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public NumeraireSearch searchNumeraire(NumeraireSearch searchModel) throws JadePersistenceException, DroitException;

    /**
     * Permet de chercher des PensionAlimentaire selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PensionAlimentaireSearch searchPensionAlimentaire(PensionAlimentaireSearch searchModel)
            throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des prêts envers tiers selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PretEnversTiersSearch searchPretEnversTiers(PretEnversTiersSearch searchModel) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de rechercher des RentesAvsAi selon le modèle de critères
     * 
     * @param searchModel
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     * @throws DroitException
     */
    RenteAvsAiSearch searchRenteAvsAi(RenteAvsAiSearch searchModel) throws JadePersistenceException, DroitException;

    /**
     * Permet de rechercher des RenteIjApi selon le modèle de critères
     * 
     * @param searchModel
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     * @throws DroitException
     */
    public RenteIjApiSearch searchRenteIjApi(RenteIjApiSearch searchModel) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de chercher des RevenuActiviteLucrativeDependante selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public RevenuActiviteLucrativeDependanteSearch searchRevenuActiviteLucrativeDependante(
            RevenuActiviteLucrativeDependanteSearch searchModel) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des RevenuActiviteLucrativeIndependante selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public RevenuActiviteLucrativeIndependanteSearch searchRevenuActiviteLucrativeIndependante(
            RevenuActiviteLucrativeIndependanteSearch searchModel) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des RevenuHypothetique selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public RevenuHypothetiqueSearch searchRevenuHypothetique(RevenuHypothetiqueSearch searchModel)
            throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher les données de revenus dépenses liées à un droit
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public RevenusDepensesSearch searchRevenusDepenses(RevenusDepensesSearch search) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de chercher des SimpleLibelleContratEntretienViager selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleLibelleContratEntretienViagerSearch searchSimpleLibelleContratEntretienViager(
            SimpleLibelleContratEntretienViagerSearch searchModel) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des SimpleTypeFraisObtentionRevenu selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleTypeFraisObtentionRevenuSearch searchSimpleTypeFraisObtentionRevenu(
            SimpleTypeFraisObtentionRevenuSearch searchModel) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher les données de revenus dépenses liées à un droit
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public TaxeJournaliereHomeSearch searchTaxeJournaliereHome(TaxeJournaliereHomeSearch searchModel)
            throws JadePersistenceException, DroitException;

    /**
     * Permet de chercher des Titre selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    TitreSearch searchTitre(TitreSearch searchModel) throws DroitException, JadePersistenceException;

    VehiculeSearch searchVehicule(VehiculeSearch searchModel) throws JadePersistenceException, DroitException;

    public VersionDroitSearch searchVersionDroit(VersionDroitSearch search) throws DroitException,
            JadePersistenceException;

    /**
     * Supprime une version de droit. Si il y a une seule version le droit, les membres de familles et les données
     * personnel sont supprimé
     * 
     * @param droit
     * @return
     * @throws DonneeFinanciereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DroitException
     */
    Droit supprimerVersionDroit(Droit droit) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DroitException;

    public Droit synchroniseMembresFamille(Droit droit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, MembreFamilleException,
            DonneesPersonnellesException;

    /**
     * Mise à jour de l'allocation impotent
     * 
     * @param droit
     * @param droitMembreFamille
     * @param newAllocationImpotent
     * @return l'allocation impotent mise à jour
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws AllocationImpotentException
     * @throws DonneeFinanciereException
     */
    public AllocationImpotent updateAllocationImpotent(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, AllocationImpotent newAllocationImpotent) throws DroitException,
            JadePersistenceException, AllocationImpotentException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee allocationsFamiliales pour une version de droit Il n'est pas possible de modifier une
     * version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'allocationsFamiliales à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite assurance rente viagère
     *            doit etre modifié
     * @param allocationsFamiliales
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AllocationsFamiliales updateAllocationsFamiliales(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, AllocationsFamiliales allocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee assurance rente viagère pour une version de droit Il n'est pas possible de modifier une
     * version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'assurance rente viagère à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite assurance rente viagère
     *            doit etre modifié
     * @param assuranceRenteViagere
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AssuranceRenteViagere updateAssuranceRenteViagere(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, AssuranceRenteViagere assuranceRenteViagere)
            throws DroitException, JadePersistenceException, AssuranceRenteViagereException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee assuranceVie pour une version de droit Il n'est pas possible de modifier une version
     * d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'assuranceVie à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite assurance rente viagère
     *            doit etre modifié
     * @param assuranceVie
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    AssuranceVie updateAssuranceVie(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, AssuranceVie assuranceVie) throws AssuranceVieException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    public AutreApi updateAutreApi(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            AutreApi autreApi) throws DroitException, JadePersistenceException, DonneeFinanciereException;

    AutreFortuneMobiliere updateAutreFortuneMobiliere(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, AutreFortuneMobiliere autreFortuneMobiliere) throws DroitException,
            JadePersistenceException, AutreFortuneMobiliereException, DonneeFinanciereException;

    public AutreRente updateAutreRente(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            AutreRente autreRente) throws AutreRenteException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee autresDettesProuvees pour une version de droit Il n'est pas possible de modifier une
     * version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont l'autresDettesProuvees à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite assurance rente viagère
     *            doit etre modifié
     * @param autresDettesProuvees
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    AutresDettesProuvees updateAutresDettesProuvees(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, AutresDettesProuvees autresDettesProuvees)
            throws AutresDettesProuveesException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee autresRevenus pour une version de droit Il n'est pas possible de modifier une version
     * d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont autresRevenus à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite assurance rente viagère
     *            doit etre modifié
     * @param autresRevenus
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutresRevenus updateAutresRevenus(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, AutresRevenus autresRevenus) throws AutresRevenusException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee betail pour une version de droit Il n'est pas possible de modifier une version d'un
     * droit validé par une decision
     * 
     * @param droit
     *            le droit dont le betail à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite betail doit etre modifié
     * @param betail
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Betail updateBetail(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            Betail betail) throws DroitException, JadePersistenceException, BetailException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee bienImmobilierHabitationNonPrincipale pour une version de droit Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le bienImmobilierHabitationNonPrincipale à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite
     *            bienImmobilierHabitationNonPrincipale doit etre modifié
     * @param bienImmobilierHabitationNonPrincipale
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    BienImmobilierHabitationNonPrincipale updateBienImmobilierHabitationNonPrincipale(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee bienImmobilierNonHabitable pour une version de droit Il n'est pas possible de modifier
     * une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le bienImmobilierNonHabitable à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite betail doit etre modifié
     * @param bienImmobilierNonHabitable
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    BienImmobilierNonHabitable updateBienImmobilierNonHabitable(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee bienImmobilierServantHabitationPrincipale pour une version de droit Il n'est pas
     * possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le bienImmobilierServantHabitationPrincipale à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite
     *            bienImmobilierServantHabitationPrincipale doit etre modifié
     * @param bienImmobilierServantHabitationPrincipale
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    BienImmobilierServantHabitationPrincipale updateBienImmobilierServantHabitationPrincipale(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee capitalLPP pour une version de droit Il n'est pas possible de modifier une version d'un
     * droit validé par une decision
     * 
     * @param droit
     *            le droit dont le capitalLPP à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite capitalLPP doit etre
     *            modifié
     * @param capitalLPP
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    CapitalLPP updateCapitalLPP(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            CapitalLPP capitalLPP) throws CapitalLPPException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee compteBancaireCCP pour une version de droit Il n'est pas possible de modifier une
     * version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le compteBancaireCCP à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite compteBancaireCCP doit
     *            etre modifié
     * @param compteBancaireCCP
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CompteBancaireCCP updateCompteBancaireCCP(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, CompteBancaireCCP compteBancaireCCP)
            throws CompteBancaireCCPException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee contratEntretienViager pour une version de droit Il n'est pas possible de modifier une
     * version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le contratEntretienViager à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite contratEntretienViager
     *            doit etre modifié
     * @param contratEntretienViager
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ContratEntretienViager updateContratEntretienViager(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, ContratEntretienViager contratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee cotisationsPsal pour une version de droit Il n'est pas possible de modifier une version
     * d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le cotisationsPsal à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite betail doit etre modifié
     * @param cotisationsPsal
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CotisationsPsal updateCotisationsPsal(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, CotisationsPsal cotisationsPsal)
            throws CotisationsPsalException, JadePersistenceException, DroitException, DonneeFinanciereException;

    public DessaisissementFortune updateDessaisissementFortune(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, DessaisissementFortune dessaisissementFortune)
            throws DessaisissementFortuneException, DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DroitException;

    public DessaisissementRevenu updateDessaisissementRevenu(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, DessaisissementRevenu dessaisissementRevenu)
            throws DessaisissementRevenuException, DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DroitException;

    public DonneesPersonnelles updateDonneesPersonnelles(DonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, DossierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;

    /**
     * Mise a jour d'un droit Il n'est pas possible de modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            Le droit mis a jour
     * @return Le droit sauvegarde
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Droit updateDroit(Droit droit) throws DroitException, JadePersistenceException;

    /**
     * Sauvegarde d'une entitee ijApg pour une version de droit Il n'est pas possible de modifier une version d'un droit
     * validé par une decision
     * 
     * @param droit
     *            le droit dont le ijApg à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite ijApg doit etre modifié
     * @param ijApg
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws IjApgException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public IjApg updateIjApg(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille, IjApg ijApg)
            throws DroitException, JadePersistenceException, IjApgException, DonneeFinanciereException;

    /**
     * Mise à jour de l'indemniteJournaliere AI
     * 
     * @param droit
     * @param droitMembreFamille
     * @param newIndemniteJournaliereAi
     * @return L'indemnité journaliere mise à jour
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws IndemniteJournaliereAiException
     * @throws DonneeFinanciereException
     */
    public IndemniteJournaliereAi updateIndemniteJournaliereAi(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, IndemniteJournaliereAi newIndemniteJournaliereAi)
            throws DroitException, JadePersistenceException, IndemniteJournaliereAiException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee allocationsFamiliales pour une version de droit Il n'est pas possible de modifier une
     * version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le loyer à modifier dépend
     * @param loyer
     *            Contient la version du droit et le membre de famille pour lesquels l'entite loyer doit etre modifié
     * @param Loyer
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Loyer updateLoyer(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille, Loyer loyer)
            throws LoyerException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee marchandise en stock pour une version de droit Il n'est pas possible de modifier une
     * version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont la marchandise en stock à modifier dépend
     * @param droitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite marchandise en stock doit
     *            etre modifié
     * @param marchandisesStock
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    MarchandisesStock updateMarchandisesStock(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, MarchandisesStock marchandisesStock)
            throws MarchandisesStockException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee numeraire pour une version de droit Il n'est pas possible de modifier une version d'un
     * droit validé par une decision
     * 
     * @param droit
     *            le droit dont le numéraire à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite numeraire doit etre
     *            modifié
     * @param numeraire
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Numeraire updateNumeraire(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, Numeraire numeraire) throws DroitException,
            JadePersistenceException, NumeraireException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee pensionAlimentaire pour une version de droit Il n'est pas possible de modifier une
     * version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le pensionAlimentaire à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pensionAlimentaire doit
     *            etre modifié
     * @param pensionAlimentaire
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PensionAlimentaire updatePensionAlimentaire(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille instanceDroitMembreFamille, PensionAlimentaire pensionAlimentaire)
            throws PensionAlimentaireException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee pretEnversTiers pour une version de droit Il n'est pas possible de modifier une version
     * d'un droit validé par une decision
     * 
     * @param droitMembreFamilleEtendu
     *            Contient la version du droit
     * @param pretEnversTiers
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PretEnversTiers updatePretEnversTiers(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, PretEnversTiers pretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Mise à jour de la rente AVSAi
     * 
     * @param droit
     * @param droitMembreFamille
     * @param newRenteAvsAi
     * @return La rente AvsAi mise à jour
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws RenteAvsAiException
     * @throws DonneeFinanciereException
     */
    public RenteAvsAi updateRenteAvsAi(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            RenteAvsAi newRenteAvsAi) throws DroitException, JadePersistenceException, RenteAvsAiException,
            DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee revenuActiviteLucrativeDependante pour une version de droit Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le revenuActiviteLucrativeDependante à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite
     *            revenuActiviteLucrativeDependante doit etre modifié
     * @param revenuActiviteLucrativeDependante
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeDependante updateRevenuActiviteLucrativeDependante(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee revenuActiviteLucrativeIndependante pour une version de droit Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le revenuActiviteLucrativeIndependante à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite
     *            revenuActiviteLucrativeIndependante doit etre modifié
     * @param revenuActiviteLucrativeIndependante
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependante
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeIndependante updateRevenuActiviteLucrativeIndependante(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee revenuHypothetique pour une version de droit Il n'est pas possible de modifier une
     * version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le revenuHypothetique à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite revenuHypothetique doit
     *            etre modifié
     * @param revenuHypothetique
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuHypothetique updateRevenuHypothetique(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, RevenuHypothetique revenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee simpleLibelleContratEntretienViager pour une version de droit Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le simpleLibelleContratEntretienViager à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite pensionAlimentaire doit
     *            etre modifié
     * @param simpleLibelleContratEntretienViager
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SimpleLibelleContratEntretienViager
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleLibelleContratEntretienViager updateSimpleLibelleContratEntretienViager(
            ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee simpleTypeFraisObtentionRevenu pour une version de droit Il n'est pas possible de
     * modifier une version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le simpleTypeFraisObtentionRevenu à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite
     *            simpleTypeFraisObtentionRevenu doit etre modifié
     * @param simpleTypeFraisObtentionRevenu
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SimpleTypeFraisObtentionRevenu
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleTypeFraisObtentionRevenu updateSimpleTypeFraisObtentionRevenu(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws SimpleTypeFraisObtentionRevenuException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee taxeJournaliereHome pour une version de droit Il n'est pas possible de modifier une
     * version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le taxeJournaliereHome à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite revenuHypothetique doit
     *            etre modifié
     * @param taxeJournaliereHome
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TaxeJournaliereHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TaxeJournaliereHome updateTaxeJournaliereHome(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, TaxeJournaliereHome taxeJournaliereHome)
            throws TaxeJournaliereHomeException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee Titre pour une version de droit Il n'est pas possible de modifier une version d'un droit
     * validé par une decision
     * 
     * @param droit
     *            le droit dont le titre à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite titre doit etre modifié
     * @param titre
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    Titre updateTitre(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille instanceDroitMembreFamille,
            Titre titre) throws TitreException, JadePersistenceException, DroitException, DonneeFinanciereException;

    /**
     * Sauvegarde d'une entitee typeFraisObtentionRevenu pour une version de droit Il n'est pas possible de modifier une
     * version d'un droit validé par une decision
     * 
     * @param droit
     *            le droit dont le typeFraisObtentionRevenu à modifier dépend
     * @param instanceDroitMembreFamille
     *            Contient la version du droit et le membre de famille pour lesquels l'entite typeFraisObtentionRevenu
     *            doit etre modifié
     * @param typeFraisObtentionRevenu
     *            L'entite a sauvegarder
     * @return L'entite sauvegardee
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeFraisObtentionRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TypeFraisObtentionRevenu updateTypeFraisObtentionRevenu(ModificateurDroitDonneeFinanciere droit,
            DroitMembreFamille droitMembreFamille, TypeFraisObtentionRevenu typeFraisObtentionRevenu)
            throws TypeFraisObtentionRevenuException, JadePersistenceException, DroitException,
            DonneeFinanciereException;

    Vehicule updateVehicule(ModificateurDroitDonneeFinanciere droit, DroitMembreFamille droitMembreFamille,
            Vehicule vehicule) throws DroitException, JadePersistenceException, VehiculeException,
            DonneeFinanciereException;

    List<SimpleDroitMembreFamille> addMembreFamilleByIdMembreFamille(MembreFamilleToSync membreFamilleToSync)
            throws DonneesPersonnellesException, MembreFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DroitException;

    MembresFamillesToSynchronise resolveEnfantToSynchroniseByIdDemande(String idDemande)
            throws DonneesPersonnellesException, MembreFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DroitException;

    List<Droit> findCurrentVersionDroitByIdsDemande(List<String> idsDemande) throws DroitException,
            JadePersistenceException;

}
