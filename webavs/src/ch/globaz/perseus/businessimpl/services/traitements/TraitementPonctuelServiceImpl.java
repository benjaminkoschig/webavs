/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.traitements;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.traitements.TraitementPonctuelService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author dde
 * 
 */
public class TraitementPonctuelServiceImpl extends PerseusAbstractServiceImpl implements TraitementPonctuelService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.traitements.TraitementAnnuelService#executerTraitementsAnnuels(globaz.globall
     * .db.BSession, globaz.jade.log.business.JadeBusinessLogSession)
     */
    @Override
    public List<Decision> executerTraitements(BSession session, JadeBusinessLogSession logSession, String mois,
            String texteDecision) throws PerseusException, JadePersistenceException {
        List<Decision> listDecisionsReturn = new ArrayList<Decision>();
        try {
            if (PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise(session)) {
                JadeThread.logError(this.getClass().getName(),
                        "Veuillez interdire la validation des décisions avant d'exécuter le traitement ponctuel");
            } else {
                String moisActuel = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
                if (!moisActuel.equals(mois)) {
                    JadeThread.logError(this.getClass().getName(),
                            "Le traitement ponctuel ne peut être fait que pour le mois en cours : " + moisActuel);
                } else {

                    // On prend toutes les demandes sans date de fin
                    DemandeSearchModel demandeSearchModel = new DemandeSearchModel();
                    demandeSearchModel.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
                    demandeSearchModel.setWhereKey(DemandeSearchModel.WITHOUT_DATEFIN);
                    demandeSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                    demandeSearchModel = PerseusServiceLocator.getDemandeService().search(demandeSearchModel);
                    logSession.info(this.getClass().getName(), demandeSearchModel.getSize()
                            + " demandes validées sans date de fin à traiter");

                    // Normalement il ne peut y avoir que des projets des partiels ou des ocrois
                    // Du coup on prend les décisions correspondantes
                    DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
                    decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
                    decisionSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                    decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);
                    Map<String, Decision> listDecisions = new HashMap<String, Decision>();
                    for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
                        Decision decision = (Decision) model;
                        if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(
                                decision.getSimpleDecision().getCsTypeDecision())
                                || CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(
                                        decision.getSimpleDecision().getCsTypeDecision())) {
                            listDecisions.put(decision.getDemande().getId(), decision);
                        }
                    }

                    Collection<String> dossiersOuverts = new ArrayList<String>();
                    List<String> nouvellesDemandes = new ArrayList<String>();
                    Map<String, Decision> anciennesDecisions = new HashMap<String, Decision>();
                    int demandesFermees = 0;
                    for (JadeAbstractModel model : demandeSearchModel.getSearchResults()) {
                        Demande demande = (Demande) model;
                        dossiersOuverts.add(demande.getDossier().getId());
                        try {
                            Decision decision = null;
                            if (listDecisions.containsKey(demande.getId())) {
                                decision = listDecisions.get(demande.getId());

                                // Si on a déjà lancer le traitement ne pas reprendre celles qui ont été renouvellées
                                if (!moisActuel.equals(demande.getSimpleDemande().getDateDebut().substring(3))) {

                                    boolean isOctroiComplet = CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(
                                            decision.getSimpleDecision().getCsTypeDecision());
                                    boolean isPartielSansRi = CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(
                                            decision.getSimpleDecision().getCsTypeDecision())
                                            && !demande.getSimpleDemande().getFromRI();

                                    // Copier les octrois
                                    if (isOctroiComplet || isPartielSansRi) {
                                        Demande demandeCopie = PerseusServiceLocator.getDemandeService()
                                                .copier(demande);
                                        nouvellesDemandes.add(demandeCopie.getId());
                                        anciennesDecisions.put(demandeCopie.getId(), decision);

                                        // Fermer toutes les anciennes demandes
                                        String dateFin = "01." + mois;
                                        dateFin = JadeDateUtil.addDays(dateFin, -1);
                                        demande.getSimpleDemande().setDateFin(dateFin);
                                        demande = PerseusServiceLocator.getDemandeService().update(demande);

                                        demandesFermees++;

                                    }

                                }
                            } else {
                                // C'est un projet si il n'y a pas de décision
                                // JadeThread.logError(this.getClass().getName(),
                                // "Pas de décision pour la demande n° " + demande.getId());
                            }
                        } catch (Exception e) {
                            logSession.error(this.getClass().getName(), "Erreur lors du traintement de la demande "
                                    + demande.getDossier().getDemandePrestation().getPersonneEtendue()
                                            .getPersonneEtendue().getNumAvsActuel() + " " + e.toString());
                        }
                    }

                    logSession.info(this.getClass().getName(), demandesFermees + " demandes ont été fermées");

                    // Traiter les demandes partielles fermées en fin d'année dernière
                    DemandeSearchModel demandeDecembreSearchModel = new DemandeSearchModel();
                    demandeDecembreSearchModel.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
                    demandeDecembreSearchModel.setForDateFin(JadeDateUtil.addYears("31.12." + mois.substring(3), -1));
                    demandeDecembreSearchModel.setWhereKey(DemandeSearchModel.WITH_ONLY_DATEFIN);
                    demandeDecembreSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                    demandeDecembreSearchModel = PerseusServiceLocator.getDemandeService().search(
                            demandeDecembreSearchModel);
                    int nbDemandesDecembre = 0;
                    for (JadeAbstractModel model : demandeDecembreSearchModel.getSearchResults()) {
                        Demande demande = (Demande) model;
                        try {
                            Decision decision = null;
                            if (listDecisions.containsKey(demande.getId())) {
                                decision = listDecisions.get(demande.getId());
                                // Si il ne vient pas du RI, que la demande n'a pas déjà été réouverte et que c'est un
                                // partiel
                                if (!demande.getSimpleDemande().getFromRI()
                                        && CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(
                                                decision.getSimpleDecision().getCsTypeDecision())
                                        && !dossiersOuverts.contains(demande.getDossier().getId())) {
                                    // Réouvrir la demande
                                    Demande demandeCopie = PerseusServiceLocator.getDemandeService().copier(demande);
                                    nouvellesDemandes.add(demandeCopie.getId());
                                    anciennesDecisions.put(demandeCopie.getId(), decision);
                                    dossiersOuverts.add(demandeCopie.getDossier().getId());
                                    nbDemandesDecembre++;
                                }
                            } else {
                                // C'était un projet ou un refus,...
                            }
                        } catch (Exception e) {
                            logSession.error(this.getClass().getName(), "Erreur lors du traintement de la demande "
                                    + demande.getDossier().getDemandePrestation().getPersonneEtendue()
                                            .getPersonneEtendue().getNumAvsActuel() + " " + e.toString());
                        }
                    }

                    logSession.info(this.getClass().getName(), nouvellesDemandes.size() + nbDemandesDecembre
                            + " demandes ont été réouvertes (fermées + partiel cloturé en fin d'année dernière");

                    PerseusServiceLocator.getPmtMensuelService().activerValidationDecision();

                    // On a fermé les demandes à fermer, il faut maintenant calculer et valider les nouvelles demandes
                    // en changeant aussi leur date
                    int nbDecisionPartielEnOcroi = 0;
                    for (String idDemande : nouvellesDemandes) {
                        Demande demande = PerseusServiceLocator.getDemandeService().read(idDemande);
                        try {
                            String dateDebut = "01." + mois;

                            demande.getSimpleDemande().setDateDebut(dateDebut);
                            demande.getSimpleDemande().setDateFin("");
                            demande.getSimpleDemande().setDateDepot(dateDebut);
                            demande.getSimpleDemande().setTypeDemande(
                                    CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem());
                            demande = PerseusServiceLocator.getDemandeService().update(demande);

                            // On fait le calcul
                            PCFAccordee pcfa = PerseusServiceLocator.getPCFAccordeeService().calculer(demande.getId());
                            // Reprendre la dernière demande (pour la synchro des enregistrements)
                            demande = pcfa.getDemande();

                            Decision oldDecision = anciennesDecisions.get(demande.getId());
                            // Reprendre les mesures d'encouragement de l'ancienne PCFA.
                            PCFAccordee oldPCFA = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                                    oldDecision.getDemande().getId());
                            pcfa = PerseusServiceLocator.getPCFAccordeeService().update(pcfa, "0", "0",
                                    oldPCFA.getCalcul().getDonneeString(OutputData.MESURE_COACHING));

                            Decision decision = new Decision();
                            decision.setDemande(demande);
                            // Service devrait faire ca mais bon je le fais
                            decision.getSimpleDecision().setIdDemande(demande.getId());
                            decision.setListCopies(oldDecision.getListCopies());
                            if (Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant()) > 0) {
                                decision.getSimpleDecision().setCsTypeDecision(
                                        CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
                            } else {
                                decision.getSimpleDecision().setCsTypeDecision(
                                        CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
                            }
                            decision.getSimpleDecision().setDateDocument(
                                    JadeDateUtil.getGlobazFormattedDate(new Date()));
                            decision.getSimpleDecision().setCsEtat(CSEtatDecision.PRE_VALIDE.getCodeSystem());
                            decision.getSimpleDecision().setDatePreparation(dateDebut);
                            decision.getSimpleDecision().setUtilisateurPreparation(
                                    oldDecision.getSimpleDecision().getUtilisateurPreparation());
                            decision.getSimpleDecision().setIdDomaineApplicatifAdresseCourrier(
                                    oldDecision.getSimpleDecision().getIdDomaineApplicatifAdresseCourrier());
                            decision.getSimpleDecision().setIdDomaineApplicatifAdressePaiement(
                                    oldDecision.getSimpleDecision().getIdDomaineApplicatifAdressePaiement());
                            decision.getSimpleDecision().setIdTiersAdresseCourrier(
                                    oldDecision.getSimpleDecision().getIdTiersAdresseCourrier());
                            decision.getSimpleDecision().setIdTiersAdressePaiement(
                                    oldDecision.getSimpleDecision().getIdTiersAdressePaiement());
                            decision.getSimpleDecision().setMontantToucheAuRI(
                                    oldDecision.getSimpleDecision().getMontantToucheAuRI());
                            decision.getSimpleDecision().setNumeroDecision(
                                    PerseusServiceLocator.getDecisionService().getNumeroDemandeCalculee(
                                            dateDebut.substring(6)));
                            decision.getSimpleDecision().setRemarquesGenerales(
                                    oldDecision.getSimpleDecision().getRemarquesGenerales());
                            decision.getSimpleDecision().setRemarqueUtilisateur(texteDecision);

                            decision = PerseusServiceLocator.getDecisionService().create(decision);

                            // Validation de la décision
                            decision = PerseusServiceLocator.getDecisionService()
                                    .valider(decision, session.getUserId());

                            // Si l'ancienne décision était un octroi complet on imprime la décision si le montant a
                            // changé
                            if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(
                                    oldDecision.getSimpleDecision().getCsTypeDecision())) {
                                Float newMontant = Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant());
                                Float oldMontant = Float.parseFloat(oldPCFA.getSimplePCFAccordee().getMontant());
                                Float newExcedant = Float.parseFloat(pcfa.getSimplePCFAccordee().getExcedantRevenu());
                                Float oldExcedant = Float
                                        .parseFloat(oldPCFA.getSimplePCFAccordee().getExcedantRevenu());
                                if (!newMontant.equals(oldMontant) || !newExcedant.equals(oldExcedant)) {
                                    listDecisionsReturn.add(decision);
                                }
                            } else {
                                // Si non c'est un partiel et on retourne la décision si elle est passé en octroi
                                if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(
                                        decision.getSimpleDecision().getCsTypeDecision())) {
                                    listDecisionsReturn.add(decision);
                                    nbDecisionPartielEnOcroi++;
                                }
                            }

                        } catch (Exception e) {
                            logSession.error(this.getClass().getName(), "Erreur lors du calcul ou de la validation "
                                    + demande.getDossier().getDemandePrestation().getPersonneEtendue()
                                            .getPersonneEtendue().getNumAvsActuel() + " " + e.toString());
                        }
                    }

                    logSession.info(this.getClass().getName(), nbDecisionPartielEnOcroi
                            + " partiels sont passés en octroi");
                    logSession
                            .info(this.getClass().getName(), listDecisionsReturn.size() + " octrois ont été imprimés");

                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("Service not available during traitementAnnuel : " + e.toString(), e);
        } catch (JadeApplicationException e) {
            throw new PaiementException("JadeApplicationException during traitementAnnuel : " + e.toString(), e);
        } catch (Exception e) {
            logSession.error(this.getClass().getName(), "Erreur : " + e.toString());
        }
        return listDecisionsReturn;
    }

}
