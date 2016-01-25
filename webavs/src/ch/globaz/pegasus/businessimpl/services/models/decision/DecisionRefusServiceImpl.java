/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.business.models.decision.DecisionRefusSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.decision.DecisionRefusService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author SCE
 * 
 *         21 juil. 2010
 */
public class DecisionRefusServiceImpl extends PegasusAbstractServiceImpl implements DecisionRefusService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionRefusService
     * #count(ch.globaz.pegasus.business.models.decision.DecisionRefusSearch)
     */
    @Override
    public int count(DecisionRefusSearch search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count simpleDecisionsRefus, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionRefusService
     * #create(ch.globaz.pegasus.business.models.decision.DecisionRefus)
     */
    @Override
    public DecisionRefus create(DecisionRefus decision) throws JadePersistenceException, DecisionException,
            DemandeException, DossierException {

        if (decision == null) {
            throw new DecisionException("Unable to count, the search model passed is null!");
        }

        // creation header decision
        try {

            // Set tiers
            decision.getDecisionHeader().getSimpleDecisionHeader().setIdTiersBeneficiaire(getIdTiers(decision));

            // Set adresse Tiers courrier
            decision.getDecisionHeader().getSimpleDecisionHeader().setIdTiersCourrier(getIdTiers(decision));
            // Set etat - enregistré
            decision.getDecisionHeader().getSimpleDecisionHeader().setCsEtatDecision(IPCDecision.CS_ETAT_ENREGISTRE);
            // Set type
            decision.getDecisionHeader().getSimpleDecisionHeader().setCsTypeDecision(IPCDecision.CS_TYPE_REFUS_SC);
            // set genre, par défaut cs Decision
            decision.getDecisionHeader().getSimpleDecisionHeader().setCsGenreDecision(IPCDecision.CS_GENRE_DECISION);
            // creation header
            decision.setDecisionHeader(PegasusServiceLocator.getDecisionHeaderService().create(
                    decision.getDecisionHeader()));

            // set demande
            decision.getSimpleDecisionRefus().setIdDemandePc(decision.getDemande().getSimpleDemande().getId());

            // Set etat demande refusé
            decision.getDemande().getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_REFUSE);
            PegasusServiceLocator.getDemandeService().update(decision.getDemande());
            // creation decision refus
            decision.setSimpleDecisionRefus(PegasusImplServiceLocator.getSimpleDecisionRefusService().create(
                    decision.getSimpleDecisionRefus()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }

        return decision;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionRefusService
     * #delete(ch.globaz.pegasus.business.models.decision.DecisionRefus)
     */
    @Override
    public DecisionRefus delete(DecisionRefus decision) throws DecisionException, JadePersistenceException {
        if (decision == null) {
            throw new DecisionException("unable to delete decisionApresCalcul, the model passed is null");
        }

        try {
            PegasusImplServiceLocator.getSimpleDecisionRefusService().delete(decision.getSimpleDecisionRefus());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }
        return decision;
    }

    @Override
    public String getIdDecisionByIdDemande(String idDemande) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException {
        DecisionRefusSearch search = new DecisionRefusSearch();
        search.setForIdDemande(idDemande);
        search = PegasusServiceLocator.getDecisionRefusService().search(search);

        // Exception si aucun resultat, en theorie impossible...
        if ((search.getSearchResults().length == 0) || (search.getSearchResults().length > 1)) {
            throw new DecisionException("Can't getting the idDecision with the idDemande - the serach result is wrong!");
        }
        String idDecision = ((DecisionRefus) search.getSearchResults()[0]).getDecisionHeader()
                .getSimpleDecisionHeader().getId();
        return idDecision;
    }

    /**
     * Retourne l'id du tiers
     * 
     * @param decision
     * @return
     */
    private String getIdTiers(DecisionRefus decision) {
        return decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionRefusService #read(java.lang.String)
     */
    @Override
    public DecisionRefus read(String idDecision) throws JadePersistenceException, DecisionException {
        DecisionRefus decision = new DecisionRefus();
        decision.setId(idDecision);
        return (DecisionRefus) JadePersistenceManager.read(decision);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionRefusService
     * #search(ch.globaz.pegasus.business.models.decision.DecisionRefusSearch)
     */
    @Override
    public DecisionRefusSearch search(DecisionRefusSearch decisionRefusSearch) throws JadePersistenceException,
            DecisionException {
        if (decisionRefusSearch == null) {
            throw new DecisionException("Unable to search decision, the search model passed is null!");
        }
        return (DecisionRefusSearch) JadePersistenceManager.search(decisionRefusSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionRefusService
     * #update(ch.globaz.pegasus.business.models.decision.DecisionRefus)
     */
    @Override
    public DecisionRefus update(DecisionRefus decision) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException {

        // Set etat - enregistré
        decision.getDecisionHeader().getSimpleDecisionHeader().setCsEtatDecision(IPCDecision.CS_ETAT_ENREGISTRE);
        // Set valide par et date validation a 0
        decision.getDecisionHeader().getSimpleDecisionHeader().setDateValidation("");
        decision.getDecisionHeader().getSimpleDecisionHeader().setValidationPar("");

        // Motif sous motifs

        // header
        decision.getDecisionHeader().setSimpleDecisionHeader(
                PegasusImplServiceLocator.getSimpleDecisionHeaderService().update(
                        decision.getDecisionHeader().getSimpleDecisionHeader()));
        // decision refus
        decision.setSimpleDecisionRefus(PegasusImplServiceLocator.getSimpleDecisionRefusService().update(
                decision.getSimpleDecisionRefus()));

        return decision;
    }

    @Override
    public DecisionRefus updateForPrevalidation(DecisionRefus decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Set valide par et date validation remis à 0
        decision.getDecisionHeader().getSimpleDecisionHeader().setDateValidation("");
        decision.getDecisionHeader().getSimpleDecisionHeader().setValidationPar("");

        // header
        decision.getDecisionHeader().setSimpleDecisionHeader(
                PegasusImplServiceLocator.getSimpleDecisionHeaderService().update(
                        decision.getDecisionHeader().getSimpleDecisionHeader()));
        // decision refus
        decision.setSimpleDecisionRefus(PegasusImplServiceLocator.getSimpleDecisionRefusService().update(
                decision.getSimpleDecisionRefus()));

        return decision;
    }

    @Override
    public DecisionRefus updateForValidation(DecisionRefus decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DemandeException, DossierException {

        // header
        decision.getDecisionHeader().setSimpleDecisionHeader(
                PegasusImplServiceLocator.getSimpleDecisionHeaderService().update(
                        decision.getDecisionHeader().getSimpleDecisionHeader()));
        // decision refus
        decision.setSimpleDecisionRefus(PegasusImplServiceLocator.getSimpleDecisionRefusService().update(
                decision.getSimpleDecisionRefus()));

        // Mise à jour demande
        decision.getDemande().getSimpleDemande()
                .setDateFin(JadeDateUtil.convertDateMonthYear(decision.getSimpleDecisionRefus().getDateRefus()));
        decision.getDemande()
                .getSimpleDemande()
                .setDateDebut(
                        JadeDateUtil.convertDateMonthYear(decision.getDemande().getSimpleDemande().getDateDepot()));
        decision.getDemande().getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_REFUSE);

        decision.getDemande().setSimpleDemande(
                PegasusImplServiceLocator.getSimpleDemandeService().update(decision.getDemande().getSimpleDemande()));
        return decision;
    }

}
