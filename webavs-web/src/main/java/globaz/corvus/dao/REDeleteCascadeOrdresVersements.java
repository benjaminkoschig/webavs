package globaz.corvus.dao;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.decisions.IREPreparationDecision;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.ordresversements.RECompensationInterDecisions;
import globaz.corvus.db.ordresversements.RECompensationInterDecisionsManager;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;

/**
 * <H1>Description</H1>
 * <p>
 * Une classe qui propose des méthodes pour supprimer en cascade les objets liées
 * </p>
 * 
 * @author scr
 */

public final class REDeleteCascadeOrdresVersements {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Suppression de l'OV est des OV liées pour les dettes avec des compensations inter-décisions Pas de validation
     * dans cette méthode !!!!
     */
    public static final void supprimerOrdreVersementCascade_noCommit(BSession session, BITransaction transaction,
            REOrdresVersements ov, int validationLevel) throws Exception {

        PRAssert.notIsNew(ov, "Impossible de supprimer l'ov, isNew==true");

        if (IREOrdresVersements.CS_TYPE_DETTE.equals(ov.getCsType())) {

            // Recher des liens compensations inter-decision
            RECompensationInterDecisionsManager mgr = new RECompensationInterDecisionsManager();
            mgr.setSession(session);
            mgr.setForIdOV(ov.getIdOrdreVersement());
            mgr.find(transaction, BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                RECompensationInterDecisions cid = (RECompensationInterDecisions) mgr.getEntity(i);
                // pour chaque lien trouvé, supprimer l'ov de type compensation
                // inter-décision
                REOrdresVersements ovCID = new REOrdresVersements();
                ovCID.setSession(session);
                ovCID.setIdOrdreVersement(cid.getIdOVCompensation());
                ovCID.retrieve(transaction);
                PRAssert.notIsNew(ovCID, null);
                if (!ovCID.getIsCompensationInterDecision().booleanValue()) {
                    throw new Exception(
                            "Incohérance dans les données. L'ordre de versement trouvé n'est pas de type compensation inter-decision");
                }
                ovCID.delete(transaction);
                cid.delete(transaction);
            }

            // Recher des liens compensations inter-decision pour les dettes de
            // genre compensation inter-decision
            mgr.setForIdOV("");
            mgr.setForIdOVCompensation(ov.getIdOrdreVersement());
            mgr.find(transaction);
            if (mgr.size() > 1) {
                throw new Exception("Plusieurs liens de comp. inter-decisions trouvés. Données incohérantes");
            } else if (mgr.size() == 1) {

                // Bah, on est jamais trop sure...
                if (!ov.getIsCompensationInterDecision().booleanValue()) {
                    throw new Exception(
                            "Incohérance dans les données. L'ordre de versement trouvé n'est pas de type compensation inter-decision");
                }
                RECompensationInterDecisions cid = (RECompensationInterDecisions) mgr.getFirstEntity();
                cid.delete(transaction);

            }
        }
        ov.delete(transaction);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Suppression des Ordres de versements de type : Compensation Inter décisions, ainsi que les liens
     * (RECompensationInterDecisions). Pour une demande
     */
    public static final void supprimerOrdreVersementInterDecisionCascade_noCommit(BSession session,
            BITransaction transaction, REDemandeRente demande, int validationLevel) throws Exception {

        PRAssert.notIsNew(demande, "La demande n'est pas valable. isNew==true");

        // Recherche des prestations de la demandes
        REPrestationsManager mgr = new REPrestationsManager();
        mgr.setSession(session);
        mgr.setForIdDemandeRente(demande.getIdDemandeRente());
        mgr.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < mgr.size(); i++) {
            REPrestations prst = (REPrestations) mgr.getEntity(i);

            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession(session);
            decision.setIdDecision(prst.getIdDecision());
            decision.retrieve(transaction);
            PRAssert.notIsNew(decision, null);

            if (validationLevel >= IREValidationLevel.VALIDATION_LEVEL_MEDIUM) {
                if (!IREDecision.CS_ETAT_ATTENTE.equals(decision.getCsEtat())) {
                    // BZ7499
                    // On lance l'erreur uniquement dans le cas ou la décision n'est pas dans l'état courant
                    if (!IREPreparationDecision.CS_TYP_PREP_DECISION_COURANT.equals(decision.getCsTypeDecision())) {
                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session,
                                decision.getIdTiersBeneficiairePrincipal());
                        throw new Exception("La décision no " + decision.getIdDecision() + " de "
                                + tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM)
                                + " doit être en attente pour pouvoir y supprimer ces OV.");
                    }
                }
            }

            // Recherche de tous les OV de chaque prestations, et suppression si
            // sont de genre Inter Decision
            REOrdresVersements[] ovs = prst.getOrdresVersement((BTransaction) transaction);
            for (int j = 0; j < ovs.length; j++) {
                REOrdresVersements ov = ovs[j];
                if ((ov.getIsCompensationInterDecision() != null) && ov.getIsCompensationInterDecision().booleanValue()) {
                    REDeleteCascadeOrdresVersements.supprimerOrdreVersementCascade_noCommit(session, transaction, ov,
                            validationLevel);

                } else {
                    RECompensationInterDecisionsManager mgr2 = new RECompensationInterDecisionsManager();
                    mgr2.setSession(session);
                    mgr2.setForIdOV(ov.getIdOrdreVersement());
                    mgr2.find(transaction);
                    for (int k = 0; k < mgr2.size(); k++) {
                        RECompensationInterDecisions cid = (RECompensationInterDecisions) mgr2.get(k);
                        // On récupère l'autre ov de type Dette
                        REOrdresVersements ovc = new REOrdresVersements();
                        ovc.setSession(session);
                        ovc.setIdOrdreVersement(cid.getIdOVCompensation());
                        ovc.retrieve(transaction);
                        if (!ovc.isNew()) {
                            REDeleteCascadeOrdresVersements.supprimerOrdreVersementCascade_noCommit(session,
                                    transaction, ovc, validationLevel);
                        }
                    }

                }
            }
        }
    }

    private REDeleteCascadeOrdresVersements() {
        // peut pas creer d'instances
    }

}
