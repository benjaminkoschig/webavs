/**
 * 
 */
package globaz.perseus.process.traitements;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.process.PFAbstractJob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author PCA
 * 
 */
public class PFAttributionNumeroOFSProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseMail = null;
    private HashMap<String, Demande> listeDemandeTraite = new HashMap<String, Demande>();
    private HashMap<String, String> numeroOFSpourDossier = new HashMap<String, String>();

    public void attribuerNumeroOFSDemandeEtDemandePrecedente(Demande demandePrecedente, Demande demande)
            throws Exception {
        if (hasSixMoisEntreDate(demande.getSimpleDemande().getDateDebut(), demandePrecedente.getSimpleDemande()
                .getDateFin())) {

            if (JadeStringUtil.isEmpty(demandePrecedente.getSimpleDemande().getNumeroOFS())) {
                String numeroOFSDemandePrecedente = numeroOFSAAttribuer(demandePrecedente, true);
                demandePrecedente = updateDemande(demandePrecedente, numeroOFSDemandePrecedente);
            }

            if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getNumeroOFS())) {
                String numeroOFSDemande = numeroOFSAAttribuer(demande, true);
                demande = updateDemande(demande, numeroOFSDemande);
            }

        } else {
            // Comme il n'y a pas six mois entre les deux demandes, attribuer un numéro identique à chaque demande. Si
            // l'une d'entre
            // elle en contien déjà un, alors le reprendre
            if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getNumeroOFS())
                    && JadeStringUtil.isEmpty(demandePrecedente.getSimpleDemande().getNumeroOFS())) {
                String numeroOFSDemande = numeroOFSAAttribuer(demande, false);
                demandePrecedente = updateDemande(demandePrecedente, numeroOFSDemande);
                demande = updateDemande(demande, numeroOFSDemande);
            } else if (JadeStringUtil.isEmpty(demandePrecedente.getSimpleDemande().getNumeroOFS())) {
                String numeroOFSDemande = numeroOFSAAttribuer(demande, false);
                demandePrecedente = updateDemande(demandePrecedente, numeroOFSDemande);
            } else if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getNumeroOFS())) {
                String numeroOFSDemandePrecedente = numeroOFSAAttribuer(demandePrecedente, false);
                demande = updateDemande(demande, numeroOFSDemandePrecedente);
            } else {
                String numeroOFDemandePrecedente = demandePrecedente.getSimpleDemande().getNumeroOFS();
                demande = updateDemande(demande, numeroOFDemandePrecedente);
            }
        }
    }

    public void attributerNumeroOFSDemande(Demande demande) throws Exception {
        if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getNumeroOFS())) {
            String numeroOFS = numeroOFSAAttribuer(demande, false);
            demande = updateDemande(demande, numeroOFS);
        }

    }

    private void doTraitement(Decision decision) throws Exception {
        Demande demandePrecedente = PerseusServiceLocator.getDecisionService()
                .getDemandePrecedenteValideDecisionOCtroiPrecedanteForNumOFS(decision.getDemande());

        Demande demande = null;

        if (listeDemandeTraite.containsKey(decision.getDemande().getSimpleDemande().getIdDemande())) {
            demande = listeDemandeTraite.get(decision.getDemande().getSimpleDemande().getIdDemande());
        } else {
            demande = decision.getDemande();
        }

        if (null != demandePrecedente) {
            if (listeDemandeTraite.containsKey(demandePrecedente.getSimpleDemande().getIdDemande())) {
                demandePrecedente = listeDemandeTraite.get(demandePrecedente.getSimpleDemande().getIdDemande());
            }

            attribuerNumeroOFSDemandeEtDemandePrecedente(demandePrecedente, demande);
        } else {
            // Pas de demande precedente, alors attribution d'un numéro OFS a la demande
            attributerNumeroOFSDemande(demande);
        }

    }

    public String getAdresseMail() {
        return adresseMail;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("JSP_PF_TRAITEMENT_ATTRIBUTION_NUMERO_OFS_DESCRIPTION");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return getSession().getLabel("JSP_PF_TRAITEMENT_ATTRIBUTION_NUMERO_OFS_DESCRIPTION");
    }

    private boolean hasSixMoisEntreDate(String dateDebut, String dateFin) {
        boolean hasSixMoisEntreDate = false;

        int nbMois = JadeDateUtil.getNbMonthsBetween(dateFin, dateDebut);
        if (nbMois >= 6) {
            hasSixMoisEntreDate = true;
        }

        return hasSixMoisEntreDate;
    }

    private DecisionSearchModel loadDecisionOctroiValidateSearch() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DecisionException {

        DecisionSearchModel decisionSearch = new DecisionSearchModel();
        decisionSearch.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        decisionSearch.setForCsTypeDecision(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
        decisionSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionSearch.setOrderKey(DecisionSearchModel.ORDER_BY_ID_DOSSIER);
        return PerseusServiceLocator.getDecisionService().search(decisionSearch);
    }

    private String numeroOFSAAttribuer(Demande demande, boolean hasEcartSixMois)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {
        String numeroOFS = "";

        if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getNumeroOFS())) {
            if (hasEcartSixMois) {
                numeroOFS = PerseusServiceLocator.getDemandeService().getNumeroOFSCalculee();
            } else {
                if (numeroOFSpourDossier.containsKey(demande.getSimpleDemande().getIdDossier())) {
                    numeroOFS = numeroOFSpourDossier.get(demande.getSimpleDemande().getIdDossier());
                } else {
                    numeroOFS = PerseusServiceLocator.getDemandeService().getNumeroOFSCalculee();
                }
            }

        } else {
            numeroOFS = demande.getSimpleDemande().getNumeroOFS();
        }

        return numeroOFS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.perseus.process.PFAbstractJob#process()
     */
    @Override
    protected void process() throws Exception {
        try {

            DecisionSearchModel decisionSearch = loadDecisionOctroiValidateSearch();

            for (JadeAbstractModel model : decisionSearch.getSearchResults()) {
                Decision decision = (Decision) model;
                doTraitement(decision);

                if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
                    logError(decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel());

                }

            }

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    "Erreur technique grave : " + e.toString() + " : " + e.getMessage());
            e.printStackTrace();
        }
        getLogSession().info(this.getClass().getName(), getName());
        logError(getSession().getLabel("PROCESS_ERREUR_MESSAGE"));
        List<String> email = new ArrayList<String>();
        email.add(adresseMail);
        this.sendCompletionMail(email);

    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    private Demande updateDemande(Demande demande, String numeroOFS) throws Exception {
        demande.getSimpleDemande().setNumeroOFS(numeroOFS);
        SimpleDemande simpleDem = PerseusImplServiceLocator.getSimpleDemandeService()
                .update(demande.getSimpleDemande());
        listeDemandeTraite.put(demande.getSimpleDemande().getIdDemande(), demande);
        numeroOFSpourDossier.put(demande.getSimpleDemande().getIdDossier(), numeroOFS);
        return demande;
    }
}
