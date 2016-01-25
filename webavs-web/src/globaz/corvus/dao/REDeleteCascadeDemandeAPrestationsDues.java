package globaz.corvus.dao;

import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.api.prestations.IREPrestations;
import globaz.corvus.db.annonces.REAnnonce51;
import globaz.corvus.db.annonces.REAnnonce53;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnonceRenteManager;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.creances.RECreanceAccordee;
import globaz.corvus.db.creances.RECreanceAccordeeManager;
import globaz.corvus.db.creances.RECreancier;
import globaz.corvus.db.creances.RECreancierManager;
import globaz.corvus.db.decisions.REAnnexeDecision;
import globaz.corvus.db.decisions.REAnnexeDecisionManager;
import globaz.corvus.db.decisions.RECopieDecision;
import globaz.corvus.db.decisions.RECopieDecisionManager;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.RERemarqueGroupePeriode;
import globaz.corvus.db.decisions.RERemarqueGroupePeriodeManager;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.decisions.REValidationDecisionsManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteSurvivant;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.demandes.REPeriodeAPI;
import globaz.corvus.db.demandes.REPeriodeAPIManager;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.demandes.REPeriodeInvaliditeManager;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.REOrdresVersementsManager;
import globaz.corvus.db.ordresversements.RESoldePourRestitution;
import globaz.corvus.db.ordresversements.RESoldePourRestitutionManager;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.db.rentesaccordees.REFactureARestituer;
import globaz.corvus.db.rentesaccordees.REFacturesARestituerManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATortManager;
import globaz.corvus.db.retenues.RERetenuesPaiement;
import globaz.corvus.db.retenues.RERetenuesPaiementManager;
import globaz.corvus.exceptions.REBusinessException;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.tools.PRAssert;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;

/**
 * <H1>Description</H1>
 * <p>
 * Une classe qui propose des méthodes pour supprimer en cascade les objets liées
 * </p>
 * Pour une suppression d'une demande de rente : --> Delete Demande de rente --> Delete Rente calculée --> Delete Bases
 * calculs --> Delete Rentes accordées --> Delete Annonces --> Delete Prestations dues --> Delete Decisions + Validation
 * de rente --> Delete Créanciers --> Delete Créances accordées --> Delete Info compta Pour une suppression d'une rente
 * calculée : --> Mise à jour de la demande (enlever l'idRenteCalculé) --> Delete Rente Calculée --> Delete Bases
 * calculs --> Delete Rentes accordées --> Delete Annonces --> Delete Prestations dues --> Delete Decisions + Validation
 * de rente Pour une suppression d'une base de calculs : --> Delete Base calcul --> Delete Rentes accordées --> Delete
 * Annonces --> Delete Prestations dues --> Delete Decisions + Validation de rente Pour une suppression de toutes les
 * bases de calculs d'une rente accordée : --> Delete Bases calculs --> Delete Rentes accordées --> Delete Annonces -->
 * Delete Prestations dues --> Delete Decisions + Validation de rente Pour une suppression d'une rente accordée : -->
 * Delete Rente accordée --> Delete Annonces --> Delete Prestations dues --> Delete Decisions + Validation de rente Pour
 * une suppression de toutes les rentes accordées d'une base de calcul : --> Delete Rentes accordées --> Delete Annonces
 * --> Delete Prestations dues --> Delete Decisions + Validation de rente Pour une suppression d'une prestation due :
 * --> Delete Prestation due --> Delete Decisions + Validation de rente Pour une suppression de toutes les prestations
 * dues d'une rente accordée : --> Delete Prestations dues --> Delete Decisions + Validation de rente
 * 
 * @author scr / hpe
 */

public final class REDeleteCascadeDemandeAPrestationsDues {

    public synchronized static void annuleTraitementValidationDecision(final BITransaction transaction,
            final BSession session, final REDecisionEntity decision) throws Exception {

        Map<String, String> idsPrstDuesParRenteAccordee = REDeleteCascadeDemandeAPrestationsDues.loadPrestationsDues(
                decision.getDecisionDepuis(), session, decision.getIdDecision());

        // Dévalidation de la décision, annuler les modifications faites lors de la validation.

        if (IREDecision.CS_TYPE_DECISION_COURANT.equals(decision.getCsTypeDecision())) {

            // Si courant, voici les changements d'états à faire :

            // 1. demandeRente --> Partiel en calculé
            // 2. raDecision --> Partiel en calculé
            // 3. pdDecision sans datefin --> Actif en attente
            // 4. décision --> Validé en attente (déjà fait lors de l'enregistrement des modifications)

            // 1. Chargement de la demande de rente
            REDemandeRente demande = new REDemandeRente();
            demande.setIdDemandeRente(decision.getIdDemandeRente());
            demande.setSession(session);
            demande.retrieve(transaction);

            if (demande.isNew()) {
                throw new Exception("Demande non trouvée pour la décision n°" + decision.getIdDecision());
            } else {
                demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
                demande.update(transaction);
            }

            // 2. Parcours de toutes les RA de la décisions
            for (String idRA : idsPrstDuesParRenteAccordee.keySet()) {

                RERenteAccordee ra = new RERenteAccordee();
                ra.setIdPrestationAccordee(idRA);
                ra.setSession(session);
                ra.retrieve(transaction);

                if (ra.isNew()) {
                    System.out.println("Rente accordée pas trouvée pour l'id = " + idRA);
                } else {
                    ra.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
                    ra.update(transaction);
                }

                // 3. Parcours des pd de la ra

                // MAJ des prestations dues de la rente accordée
                // Ne contient que le 'mensuel ($p)'
                String lstIdPrstDues = idsPrstDuesParRenteAccordee.get(idRA);
                List<String> idsPrstDues = JadeStringUtil.split(lstIdPrstDues, "-");

                for (String id : idsPrstDues) {
                    id = id.substring(0, id.length() - 1);

                    REPrestationDue pd = new REPrestationDue();
                    pd.setSession(session);
                    pd.setIdPrestationDue(id);
                    pd.retrieve(transaction);

                    if (pd.isNew()) {
                        String msg = "";
                        if (transaction.hasErrors()) {
                            msg = transaction.getErrors().toString();
                        }
                        throw new Exception("PD introuvable pour idPD/idRA/idDecision/lst = " + id + "/" + idRA + "/"
                                + decision.getIdDecision() + "/" + lstIdPrstDues + " " + msg);
                    } else {
                        // si sans date fin
                        if (JadeStringUtil.isBlankOrZero(pd.getDateFinPaiement())) {
                            pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
                            pd.update(transaction);
                        }
                    }
                }
            }

        } else if (IREDecision.CS_TYPE_DECISION_RETRO.equals(decision.getCsTypeDecision())) {

            // Si retro, voici les changements d'états à faire :

            // 1. demandeRente --> Validé en partiel
            // 2. raDecision --> Validé en partiel
            // 3. pdDecision avec datefin --> Traité en attente
            // 4. décision --> Validé en attente (déjà fait lors de l'enregistrement des modifications)

            // 1. Chargement de la demande de rente
            REDemandeRente demande = new REDemandeRente();
            demande.setIdDemandeRente(decision.getIdDemandeRente());
            demande.setSession(session);
            demande.retrieve(transaction);

            if (demande.isNew()) {
                throw new Exception("Demande non trouvée pour la décision n°" + decision.getIdDecision());
            } else {
                demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);
                demande.update(transaction);
            }

            // 2. Parcours de toutes les RA de la décisions
            for (String idRA : idsPrstDuesParRenteAccordee.keySet()) {

                RERenteAccordee ra = new RERenteAccordee();
                ra.setIdPrestationAccordee(idRA);
                ra.setSession(session);
                ra.retrieve(transaction);

                // Possible si la RA validée avec la décision de type courant a été diminuée avant la validation du
                // rétro.
                if (IREPrestationAccordee.CS_ETAT_DIMINUE.equals(ra.getCsEtat())) {
                    continue;
                } else {
                    if (ra.isNew()) {
                        System.out.println("Rente accordée pas trouvée pour l'id = " + idRA);
                    } else if (JadeStringUtil.isBlankOrZero(ra.getDateFinDroit())) {
                        ra.setCsEtat(IREPrestationAccordee.CS_ETAT_PARTIEL);
                        ra.update(transaction);
                    } else {
                        ra.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
                        ra.update(transaction);
                    }
                }

                // 3. Parcours des pd de la ra

                // MAJ des prestations dues de la rente accordée
                // Ne contient que le 'mensuel ($p)'
                String lstIdPrstDues = idsPrstDuesParRenteAccordee.get(idRA);
                List<String> idsPrstDues = JadeStringUtil.split(lstIdPrstDues, "-");

                for (String id : idsPrstDues) {
                    id = id.substring(0, id.length() - 1);

                    REPrestationDue pd = new REPrestationDue();
                    pd.setSession(session);
                    pd.setIdPrestationDue(id);
                    pd.retrieve(transaction);

                    if (pd.isNew()) {
                        String msg = "";
                        if (transaction.hasErrors()) {
                            msg = transaction.getErrors().toString();
                        }
                        throw new Exception("PD introuvable pour idPD/idRA/idDecision/lst = " + id + "/" + idRA + "/"
                                + decision.getIdDecision() + "/" + lstIdPrstDues + " " + msg);

                    } else {
                        // si sans date fin
                        if (!JadeStringUtil.isBlankOrZero(pd.getDateFinPaiement())) {
                            pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
                        } else {
                            pd.setCsEtat(IREPrestationDue.CS_ETAT_ACTIF);
                        }
                        pd.update(transaction);
                    }
                }
            }

        } else if (IREDecision.CS_TYPE_DECISION_STANDARD.equals(decision.getCsTypeDecision())) {

            // Si standard, voici les changements d'états à faire :

            // 1. demandeRente --> Validé en calculé
            // 2. raDecision --> Validé en calculé
            // 3. pdDecision avec datefin --> Traité en attente
            // pdDecision sans datefin --> Actif en attente
            // 4. décision --> Validé en attente (déjà fait lors de l'enregistrement des modifications)

            // 1. Chargement de la demande de rente
            REDemandeRente demande = new REDemandeRente();
            demande.setIdDemandeRente(decision.getIdDemandeRente());
            demande.setSession(session);
            demande.retrieve(transaction);

            if (demande.isNew()) {
                throw new Exception("Demande non trouvée pour la décision n°" + decision.getIdDecision());
            } else {
                demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
                demande.update(transaction);
            }

            // 2. Parcours de toutes les RA de la décisions
            for (String idRA : idsPrstDuesParRenteAccordee.keySet()) {

                RERenteAccordee ra = new RERenteAccordee();
                ra.setIdPrestationAccordee(idRA);
                ra.setSession(session);
                ra.retrieve(transaction);

                if (ra.isNew()) {
                    System.out.println("Rente accordée pas trouvée pour l'id = " + idRA);
                } else {
                    ra.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
                    ra.update(transaction);
                }

                // 3. Parcours des pd de la ra

                // MAJ des prestations dues de la rente accordée
                // Ne contient que le 'mensuel ($p)'
                String lstIdPrstDues = idsPrstDuesParRenteAccordee.get(idRA);
                List<String> idsPrstDues = JadeStringUtil.split(lstIdPrstDues, "-");

                for (String id : idsPrstDues) {
                    id = id.substring(0, id.length() - 1);

                    REPrestationDue pd = new REPrestationDue();
                    pd.setSession(session);
                    pd.setIdPrestationDue(id);
                    pd.retrieve(transaction);

                    if (pd.isNew()) {
                        String msg = "";
                        if (transaction.hasErrors()) {
                            msg = transaction.getErrors().toString();
                        }
                        throw new Exception("PD introuvable pour idPD/idRA/idDecision/lst = " + id + "/" + idRA + "/"
                                + decision.getIdDecision() + "/" + lstIdPrstDues + " " + msg);

                    } else {
                        pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
                        pd.update(transaction);
                    }
                }
            }

        } else {
            throw new Exception("Type de décision incorrect");
        }

        // Annulation de tout le traitement sur les annonces qui est fait lors de la validation selon type de décision

        // Si décision standard :
        // --> Enlever l'idDecision et le mois rapport dans les annonces liées aux ra de la décision
        if (decision.getCsTypeDecision().equals(IREDecision.CS_TYPE_DECISION_STANDARD)) {

            // Retrieve de toutes les annonceRentes avec l'idDecision
            REAnnonceRenteManager annMgr = new REAnnonceRenteManager();
            annMgr.setSession(session);
            annMgr.setForIdDecision(decision.getIdDecision());
            annMgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < annMgr.size(); i++) {
                REAnnonceRente annonceRente = (REAnnonceRente) annMgr.get(i);

                // Retrouver chaque annonce (41 ou 44)
                REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
                annonce44.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                annonce44.setSession(session);
                annonce44.retrieve(transaction);

                if (annonce44.isNew()) {

                    REAnnoncesAugmentationModification9Eme annonce41 = new REAnnoncesAugmentationModification9Eme();
                    annonce41.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                    annonce41.setSession(session);
                    annonce41.retrieve(transaction);

                    if (annonce41.isNew()) {
                        System.out.println("Annonces à modifier pas trouvées");
                    } else {
                        annonce41.setMoisRapport("");
                        annonce41.update(transaction);
                    }
                } else {
                    annonce44.setMoisRapport("");
                    annonce44.update(transaction);
                }
                annonceRente.setIdDecision("");
                annonceRente.update(transaction);
            }

            // Si décision retro :
            // --> Enlever l'idDecision et le mois rapport dans les annonces liées aux ra de la décision
            // --> Supprimer les annonces (copies) crées lors de la validation
        } else if (decision.getCsTypeDecision().equals(IREDecision.CS_TYPE_DECISION_RETRO)) {

            // 1. Supprimer les annonces (copies) crées lors de la validation
            // --> Retrouver toutes les annonces liées aux ra de la décision qui n'ont pas d'idDecision

            Iterator iter = idsPrstDuesParRenteAccordee.keySet().iterator();

            while (iter.hasNext()) {
                String idRA = (String) iter.next();

                // Mise à jour des annonces liées au ra
                REAnnonceRenteManager annonceMgr = new REAnnonceRenteManager();
                annonceMgr.setSession(session);
                annonceMgr.setForIdRenteAccordee(idRA);
                annonceMgr.find(transaction, BManager.SIZE_NOLIMIT);

                for (Iterator iterator = annonceMgr.iterator(); iterator.hasNext();) {
                    REAnnonceRente annonce = (REAnnonceRente) iterator.next();

                    if (IREAnnonces.CS_ETAT_OUVERT.equals(annonce.getCsEtat())) {
                        if (JadeStringUtil.isBlankOrZero(annonce.getIdDecision())) {
                            // Retrouver chaque annonce (41 ou 44)
                            REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
                            annonce44.setIdAnnonce(annonce.getIdAnnonceHeader());
                            annonce44.setSession(session);
                            annonce44.retrieve(transaction);

                            if (annonce44.isNew()) {

                                REAnnoncesAugmentationModification9Eme annonce41 = new REAnnoncesAugmentationModification9Eme();
                                annonce41.setIdAnnonce(annonce.getIdAnnonceHeader());
                                annonce41.setSession(session);
                                annonce41.retrieve(transaction);

                                if (annonce41.isNew()) {
                                    System.out.println("Annonces à modifier pas trouvées. idAnnonceH/idRA = "
                                            + annonce.getIdAnnonceHeader() + "/" + idRA);
                                } else {
                                    annonce41.delete(transaction);
                                }
                            } else {
                                annonce44.delete(transaction);
                            }

                            // Supprimer l'annonce de rente
                            annonce.delete(transaction);
                        }
                    }
                }
            }

            // 2. Enlever l'idDecision et le mois rapport dans les annonces liées aux ra de la décision

            // Retrieve de toutes les annonceRentes avec l'idDecision
            REAnnonceRenteManager annMgr = new REAnnonceRenteManager();
            annMgr.setSession(session);
            annMgr.setForIdDecision(decision.getIdDecision());
            annMgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iterator = annMgr.iterator(); iterator.hasNext();) {
                REAnnonceRente annonceRente = (REAnnonceRente) iterator.next();

                if (IREAnnonces.CS_ETAT_OUVERT.equals(annonceRente.getCsEtat())) {
                    // // Retrouver chaque annonce (41 ou 44)
                    REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
                    annonce44.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                    annonce44.setSession(session);
                    annonce44.retrieve(transaction);

                    if (annonce44.isNew()) {

                        REAnnoncesAugmentationModification9Eme annonce41 = new REAnnoncesAugmentationModification9Eme();
                        annonce41.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                        annonce41.setSession(session);
                        annonce41.retrieve(transaction);

                        if (annonce41.isNew()) {
                            System.out.println("Annonces à modifier pas trouvées. idAnnonceH = "
                                    + annonceRente.getIdAnnonceHeader());
                        } else {
                            annonce41.setMoisRapport("");
                            annonce41.update(transaction);
                        }
                    } else {
                        annonce44.setMoisRapport("");
                        annonce44.update(transaction);
                    }
                    annonceRente.setIdDecision("");
                    annonceRente.update(transaction);
                }
            }

            // Si décision courant :
            // --> Enlever l'idDecision, le mois rapport et le montant dans les annonces liées aux RA de la décision
        } else if (decision.getCsTypeDecision().equals(IREDecision.CS_TYPE_DECISION_COURANT)) {

            // Retrieve de toutes les annonceRentes avec l'idDecision
            REAnnonceRenteManager annMgr = new REAnnonceRenteManager();
            annMgr.setSession(session);
            annMgr.setForIdDecision(decision.getIdDecision());
            annMgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iterator = annMgr.iterator(); iterator.hasNext();) {
                REAnnonceRente annonceRente = (REAnnonceRente) iterator.next();

                // Retrouver chaque annonce (41 ou 44)
                REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
                annonce44.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                annonce44.setSession(session);
                annonce44.retrieve(transaction);

                if (annonce44.isNew()) {

                    REAnnoncesAugmentationModification9Eme annonce41 = new REAnnoncesAugmentationModification9Eme();
                    annonce41.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                    annonce41.setSession(session);
                    annonce41.retrieve(transaction);

                    if (annonce41.isNew()) {
                        System.out.println("Annonces à modifier pas trouvées");
                    } else {
                        annonce41.setMoisRapport("");
                        annonce41.update(transaction);
                    }
                } else {
                    annonce44.setMoisRapport("");
                    annonce44.update(transaction);
                }
                annonceRente.setIdDecision("");
                annonceRente.update(transaction);
            }

            // Sinon, problème....
        } else {
            throw new Exception("Erreur dans l'annulation du traitement de la validation (type de décision inconnue");
        }

        // Dernier traitement
        // Récupérer les eventuelles RA diminuée lors de la validation de la décision
        // Si existante :
        // MAJ de la date de fin et du code mutation
        // Suppression de son annonce de diminution liées
        // Suppression de la date de fin de la demande de rente liée à cette RA.

        // REPrestations prestation = decision.getPrestation((BTransaction) transaction);

        REPrestationsManager mgr = new REPrestationsManager();
        mgr.setSession(session);
        mgr.setForIdDecision(decision.getIdDecision());
        mgr.find(transaction, 1);

        // bz-5899
        if (mgr.size() == 0) {
            throw new Exception("ERR-15.01 : Aucune prestation trouvée pour cette décision. idDecision = "
                    + decision.getIdDecision());
        }

        for (int ii = 0; ii < mgr.size(); ii++) {
            REPrestations prestation = (REPrestations) mgr.getEntity(ii);

            REOrdresVersements[] ovs = null;
            try {
                ovs = prestation.getOrdresVersement((BTransaction) transaction);
                if (ovs == null) {
                    continue;
                }
            } catch (Exception e) {
                // On passe à l'élément suivant en cas d'erreur. Ne devrait jamais arriver !!!
                continue;
            }

            for (int i = 0; i < ovs.length; i++) {
                REOrdresVersements ov = ovs[i];

                if (ov.getCsTypeOrdreVersement() == TypeOrdreVersement.DIMINUTION_DE_RENTE) {
                    // Récupération de la ra diminuée
                    RERenteAccordee raDiminuee = new RERenteAccordee();
                    raDiminuee.setSession(session);
                    raDiminuee.setIdPrestationAccordee(ov.getIdRenteAccordeeDiminueeParOV());
                    raDiminuee.retrieve(transaction);
                    // Cette methode peut être appelée plusieurs fois pour la même décision,
                    // il n'est donc pas impossible que cette RA diminuée ait déjà été maj.
                    if (!raDiminuee.isNew() && IREPrestationAccordee.CS_ETAT_DIMINUE.equals(raDiminuee.getCsEtat())) {
                        raDiminuee.setDateFinDroit("");
                        raDiminuee.setCodeMutation("");
                        raDiminuee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
                        raDiminuee.update(transaction);

                        // Maj de la date de fin de la demande de rente.

                        REBasesCalcul bc = new REBasesCalcul();
                        bc.setSession(session);
                        bc.setIdBasesCalcul(raDiminuee.getIdBaseCalcul());
                        bc.retrieve(transaction);
                        PRAssert.notIsNew(bc, null);

                        RERenteCalculee rc = new RERenteCalculee();
                        rc.setSession(session);
                        rc.setIdRenteCalculee(bc.getIdRenteCalculee());
                        rc.retrieve(transaction);

                        REDemandeRente dem = new REDemandeRente();
                        dem.setSession(session);
                        dem.setIdRenteCalculee(rc.getIdRenteCalculee());
                        dem.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                        dem.retrieve(transaction);
                        PRAssert.notIsNew(dem, null);

                        dem.setDateFin("");
                        dem.update(transaction);

                        // suppression de l'annonce
                        REAnnonceRenteManager mar = new REAnnonceRenteManager();
                        mar.setSession(session);
                        mar.setForIdRenteAccordee(raDiminuee.getIdPrestationAccordee());
                        mar.find(transaction);
                        for (int j = 0; j < mar.size(); j++) {
                            REAnnonceRente ar = (REAnnonceRente) mar.get(j);
                            REAnnoncesDiminution10Eme arcDim = new REAnnoncesDiminution10Eme();
                            arcDim.setSession(session);
                            arcDim.setIdAnnonce(ar.getIdAnnonceHeader());
                            arcDim.retrieve(transaction);
                            if (arcDim.isNew()) {
                                REAnnoncesDiminution9Eme arcDim9 = new REAnnoncesDiminution9Eme();
                                arcDim9.setSession(session);
                                arcDim9.setIdAnnonce(ar.getIdAnnonceHeader());
                                arcDim9.retrieve(transaction);
                                if (arcDim9.isNew()) {
                                    ;
                                } else {
                                    arcDim9.delete(transaction);
                                }
                            } else {
                                arcDim.delete(transaction);
                            }
                            // 01.01.2013 LGA & RJE
                            // LES ANNONCES DE RENTES NE DOIVENT JAMAIS ÊTRE SUPPRIMEES
                            // Seules les annonces de diminutions sont supprimées ci-dessus les autres annonces
                            // ne doivent pas être touchées.
                            // ar.delete(transaction);
                        }
                    }
                }
            }

        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    protected static void assertNotIsNew(final BEntity entity, final String errorMsg) throws Exception {
        if (entity.isNew()) {
            throw new Exception(errorMsg == null ? "" : errorMsg + " " + entity.getClass().getName()
                    + " not Found. id=" + entity.getId());
        }
    }

    private static void chargeEtSupprimeAnnonces(final BSession session, final BITransaction transaction,
            final String idRenteAccordee) throws Exception {

        if (!JadeStringUtil.isIntegerEmpty(idRenteAccordee)) {
            // Processus de suppressions des annonces
            // 1. Retrouver l'annonce de rente (celle liée à la rente accordée)
            // 2. Charger l'annonce liée à l'annonce de rente (idLienAnnonce)
            // 3. Charger l'annonce liée à l'annonce trouvée au 2.
            // 4. Ainsi de suite, jusqu'à ce que l'IdLienAnnonce est vide

            String idLienAnnonce = "";

            REAnnonceRenteManager annonceRenteMan = new REAnnonceRenteManager();
            annonceRenteMan.setSession(session);
            annonceRenteMan.setForIdRenteAccordee(idRenteAccordee);
            annonceRenteMan.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iterator = annonceRenteMan.iterator(); iterator.hasNext();) {
                REAnnonceRente annonceRente = (REAnnonceRente) iterator.next();

                // Retrouver l'annonce, charger les informations nécessaires et deleter l'annonce
                if (!annonceRente.isNew()) {
                    REAnnonce51 annonce51 = new REAnnonce51();
                    annonce51.setSession(session);
                    annonce51.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                    annonce51.retrieve(transaction);

                    if (!annonce51.isNew()) {
                        idLienAnnonce = annonce51.getIdLienAnnonce();
                        annonce51.delete(transaction);
                    } else {
                        REAnnonce53 annonce53 = new REAnnonce53();
                        annonce53.setSession(session);
                        annonce53.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                        annonce53.retrieve(transaction);

                        if (!annonce53.isNew()) {
                            idLienAnnonce = annonce53.getIdLienAnnonce();
                            annonce53.delete(transaction);
                        } else {
                            REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
                            annonce44.setSession(session);
                            annonce44.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                            annonce44.retrieve(transaction);

                            if (!annonce44.isNew()) {
                                idLienAnnonce = annonce44.getIdLienAnnonce();
                                annonce44.delete(transaction);
                            } else {
                                REAnnoncesAugmentationModification9Eme annonce41 = new REAnnoncesAugmentationModification9Eme();
                                annonce41.setSession(session);
                                annonce41.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                                annonce41.retrieve(transaction);

                                if (!annonce41.isNew()) {
                                    idLienAnnonce = annonce41.getIdLienAnnonce();
                                    annonce41.delete(transaction);
                                } else {
                                    REAnnoncesDiminution9Eme annonce42 = new REAnnoncesDiminution9Eme();
                                    annonce42.setSession(session);
                                    annonce42.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                                    annonce42.retrieve(transaction);

                                    if (!annonce42.isNew()) {
                                        idLienAnnonce = annonce42.getIdLienAnnonce();
                                        annonce42.delete(transaction);
                                    } else {
                                        REAnnoncesDiminution10Eme annonce45 = new REAnnoncesDiminution10Eme();
                                        annonce45.setSession(session);
                                        annonce45.setIdAnnonce(annonceRente.getIdAnnonceHeader());
                                        annonce45.retrieve(transaction);

                                        if (!annonce45.isNew()) {
                                            idLienAnnonce = annonce45.getIdLienAnnonce();
                                            annonce45.delete(transaction);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Tout pendant que idLienAnnonce n'est pas vide, on efface les annonces...
            while (!JadeStringUtil.isIntegerEmpty(idLienAnnonce)) {

                REAnnonce51 annonce51 = new REAnnonce51();
                annonce51.setSession(session);
                annonce51.setIdAnnonce(idLienAnnonce);
                annonce51.retrieve(transaction);

                if (!annonce51.isNew()) {
                    idLienAnnonce = annonce51.getIdLienAnnonce();
                    annonce51.delete(transaction);
                } else {
                    REAnnonce53 annonce53 = new REAnnonce53();
                    annonce53.setSession(session);
                    annonce53.setIdAnnonce(idLienAnnonce);
                    annonce53.retrieve(transaction);

                    if (!annonce53.isNew()) {
                        idLienAnnonce = annonce53.getIdLienAnnonce();
                        annonce53.delete(transaction);
                    } else {
                        REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
                        annonce44.setSession(session);
                        annonce44.setIdAnnonce(idLienAnnonce);
                        annonce44.retrieve(transaction);

                        if (!annonce44.isNew()) {
                            idLienAnnonce = annonce44.getIdLienAnnonce();
                            annonce44.delete(transaction);
                        } else {
                            REAnnoncesAugmentationModification9Eme annonce41 = new REAnnoncesAugmentationModification9Eme();
                            annonce41.setSession(session);
                            annonce41.setIdAnnonce(idLienAnnonce);
                            annonce41.retrieve(transaction);

                            if (!annonce41.isNew()) {
                                idLienAnnonce = annonce41.getIdLienAnnonce();
                                annonce41.delete(transaction);
                            } else {
                                REAnnoncesDiminution9Eme annonce42 = new REAnnoncesDiminution9Eme();
                                annonce42.setSession(session);
                                annonce42.setIdAnnonce(idLienAnnonce);
                                annonce42.retrieve(transaction);

                                if (!annonce42.isNew()) {
                                    idLienAnnonce = annonce42.getIdLienAnnonce();
                                    annonce42.delete(transaction);
                                } else {
                                    REAnnoncesDiminution10Eme annonce45 = new REAnnoncesDiminution10Eme();
                                    annonce45.setSession(session);
                                    annonce45.setIdAnnonce(idLienAnnonce);
                                    annonce45.retrieve(transaction);

                                    if (!annonce45.isNew()) {
                                        idLienAnnonce = annonce45.getIdLienAnnonce();
                                        annonce45.delete(transaction);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            transaction.addErrors("Delete cascade error idRenteAccordee is empty");
        }
    }

    private static void chargeEtSupprimeBasesCalculs(final BSession session, final BITransaction transaction,
            final String idRenteCalculee) throws Exception {

        if (!JadeStringUtil.isIntegerEmpty(idRenteCalculee)) {

            REBasesCalculManager mgr = new REBasesCalculManager();
            mgr.setForIdRenteCalculee(idRenteCalculee);
            mgr.setSession(session);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iter = mgr.iterator(); iter.hasNext();) {
                REBasesCalcul bc = (REBasesCalcul) iter.next();
                REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeRentesAccordees(session, transaction,
                        bc.getIdBasesCalcul());

                // Test récup 10ème
                REBasesCalcul theBase = new REBasesCalculDixiemeRevision();
                theBase.setSession(session);
                theBase.setIdBasesCalcul(bc.getIdBasesCalcul());
                theBase.retrieve(transaction);
                if (theBase.isNew()) {
                    // Test récup 9ème
                    theBase = new REBasesCalculNeuviemeRevision();
                    theBase.setSession(session);
                    theBase.setIdBasesCalcul(bc.getIdBasesCalcul());
                    theBase.retrieve(transaction);
                }
                if (theBase.isNew()) {
                    throw new Exception("Base Calcul error : No revision found idBC=" + theBase.getIdBasesCalcul());
                }

                theBase.delete(transaction);

                if (!bc.isNew()) {
                    bc.delete(transaction);
                }
            }
        }
    }

    private static void chargeEtSupprimeCreancesAccordees(final BSession session, final BITransaction transaction,
            final RECreancier creancier) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(creancier.getIdCreancier())) {

            RECreanceAccordeeManager mgr = new RECreanceAccordeeManager();
            mgr.setSession(session);
            mgr.setForIdCreancier(creancier.getIdCreancier());
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
                RECreanceAccordee ca = (RECreanceAccordee) iterator.next();

                if (!ca.isNew()) {
                    REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeOrdresVersements(session, transaction,
                            ca.getIdOrdreVersement());
                    ca.delete(transaction);
                }
            }
        } else {
            transaction.addErrors("Delete cascade error idCreancier is empty");
        }
    }

    private static void chargeEtSupprimeCreancesAccordees(final BSession session, final BITransaction transaction,
            final RERenteAccordee renteAccordee) throws Exception {

        if (!JadeStringUtil.isIntegerEmpty(renteAccordee.getIdPrestationAccordee())) {

            RECreanceAccordeeManager mgr = new RECreanceAccordeeManager();
            mgr.setSession(session);
            mgr.setForIdRenteAccordee(renteAccordee.getIdPrestationAccordee());
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
                RECreanceAccordee ca = (RECreanceAccordee) iterator.next();

                if (!ca.isNew()) {
                    REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeOrdresVersements(session, transaction,
                            ca.getIdOrdreVersement());
                    ca.delete(transaction);
                }
            }
        } else {
            transaction.addErrors("Delete cascade error idPrestationAccordee is empty");
        }
    }

    private static void chargeEtSupprimeCreanciers(final BSession session, final BITransaction transaction,
            final String idDemandeRente) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(idDemandeRente)) {

            RECreancierManager mgr = new RECreancierManager();
            mgr.setSession(session);
            mgr.setForIdDemandeRente(idDemandeRente);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
                RECreancier creancier = (RECreancier) iterator.next();

                if (!creancier.isNew()) {
                    REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeCreancesAccordees(session, transaction,
                            creancier);
                    creancier.delete(transaction);
                }
            }
        } else {
            transaction.addErrors("Delete cascade error idDemandeRente is empty");
        }
    }

    /**
     * Suppression de la décision Suppr. annexe + copies Suppr. de la prestation Suppr. des remarques groupe période
     * Suppr. vald. décision Supprs. des OV pour chaque OV de type Dette : récupérer la RA à diminuer mettre date de fin
     * = null; mettre etat = VALIDE mettre code mutation = null
     * 
     * @param session
     * @param transaction
     * @param idPrestationDue
     * @throws Exception
     */
    private static void chargeEtSupprimeDecisionsCascade_noCommit(final BSession session,
            final BITransaction transaction, final String idDecision) throws Exception {

        if (!JadeStringUtil.isIntegerEmpty(idDecision)) {

            REValidationDecisionsManager mgr = new REValidationDecisionsManager();
            mgr.setSession(session);
            mgr.setForIdDecision(idDecision);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);
            boolean error = false;

            for (Iterator iter = mgr.iterator(); iter.hasNext();) {
                REValidationDecisions validDec = (REValidationDecisions) iter.next();
                if (!validDec.isNew()) {
                    REDecisionEntity decision = new REDecisionEntity();
                    decision.setSession(session);
                    decision.setIdDecision(validDec.getIdDecision());
                    decision.retrieve(transaction);

                    if (!decision.isNew() && !JadeStringUtil.isIntegerEmpty(decision.getIdDecision())) {

                        REDeleteCascadeDemandeAPrestationsDues.supprimerPrestationCascade_noCommit(session,
                                transaction, decision, IREValidationLevel.VALIDATION_LEVEL_ALL);

                        REAnnexeDecisionManager mgr3 = new REAnnexeDecisionManager();
                        mgr3.setSession(session);
                        mgr3.setForIdDecision(decision.getIdDecision());
                        mgr3.find(transaction);
                        for (Iterator iterator3 = mgr3.iterator(); iterator3.hasNext();) {
                            REAnnexeDecision annexe = (REAnnexeDecision) iterator3.next();
                            if (!annexe.isNew()) {
                                annexe.delete(transaction);
                            }
                        }

                        RECopieDecisionManager mgr4 = new RECopieDecisionManager();
                        mgr4.setSession(session);
                        mgr4.setForIdDecision(decision.getIdDecision());
                        mgr4.find(transaction);
                        for (Iterator iterator4 = mgr4.iterator(); iterator4.hasNext();) {
                            RECopieDecision copie = (RECopieDecision) iterator4.next();
                            if (!copie.isNew()) {
                                copie.delete(transaction);
                            }
                        }

                        RERemarqueGroupePeriodeManager remMgr = new RERemarqueGroupePeriodeManager();
                        remMgr.setSession(session);
                        remMgr.setForIdDecision(decision.getIdDecision());
                        remMgr.find(transaction, BManager.SIZE_NOLIMIT);
                        for (Iterator iterator = remMgr.iterator(); iterator.hasNext();) {
                            RERemarqueGroupePeriode rem = (RERemarqueGroupePeriode) iterator.next();
                            if (!rem.isNew()) {
                                rem.delete(transaction);
                            }
                        }

                        if (decision.getCsEtat().equals(IREDecision.CS_ETAT_VALIDE)) {

                            // Si la prestation de la décision n'est pas dans un lot définitif
                            REPrestations prestation = new REPrestations();
                            prestation.setSession(session);
                            prestation.setIdDecision(decision.getIdDecision());
                            prestation.setIdDemandeRente(decision.getIdDemandeRente());
                            prestation.retrieve(transaction);

                            if (prestation.isNew()) {

                                REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(transaction,
                                        session, decision);

                            } else {

                                if (JadeStringUtil.isIntegerEmpty(prestation.getIdLot())) {

                                    REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(
                                            transaction, session, decision);

                                } else {

                                    // Retrieve du lot
                                    RELot lot = new RELot();
                                    lot.setSession(session);
                                    lot.setIdLot(prestation.getIdLot());
                                    lot.retrieve(transaction);

                                    if (!lot.isNew()) {

                                        // si le lot est définitif
                                        if (lot.getCsEtatLot().equals(IRELot.CS_ETAT_LOT_VALIDE)) {

                                            // On peut pas modifier
                                            throw new Exception(session.getLabel("ERREUR_PREST_ETAT_VALIDE"));

                                        } else {

                                            REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(
                                                    transaction, session, decision);
                                        }
                                    }
                                }

                            }
                        }

                        decision.retrieve(transaction);
                        decision.delete(transaction);

                    }
                    validDec.delete(transaction);
                }
            }
            if (error) {
                throw new REAttemptDeletionDecisionValidateException();
            }
        } else {
            transaction.addErrors("Delete cascade error idDecision is empty");
        }
    }

    /**
     * Suppression de la décision Suppr. annexe + copies Suppr. de la presation Suppr. des remarques groupe période
     * Suppr. vald. décision Supprs. des OV pour chaque OV de type Dette : récupérer la RA à diminuer mettre date de fin
     * = null; mettre etat = VALIDE mettre code mutation = null
     * 
     * @param session
     * @param transaction
     * @param idPrestationDue
     * @throws Exception
     */
    private static void chargeEtSupprimeDecisionsParPrstDueCascade_noCommit(final BSession session,
            final BITransaction transaction, final String idPrestationDue) throws Exception {

        if (!JadeStringUtil.isIntegerEmpty(idPrestationDue)) {

            REValidationDecisionsManager mgr = new REValidationDecisionsManager();
            mgr.setSession(session);
            mgr.setForIdPrestationDue(idPrestationDue);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);
            boolean error = false;

            for (Iterator iter = mgr.iterator(); iter.hasNext();) {
                REValidationDecisions validDec = (REValidationDecisions) iter.next();
                if (!validDec.isNew()) {
                    REDecisionEntity decision = new REDecisionEntity();
                    decision.setSession(session);
                    decision.setIdDecision(validDec.getIdDecision());
                    decision.retrieve(transaction);

                    if (IREDecision.CS_ETAT_VALIDE.equals(decision.getCsEtat())) {
                        throw new REAttemptDeletionDecisionValidateException();
                    }

                    if (!decision.isNew() && !JadeStringUtil.isIntegerEmpty(decision.getIdDecision())) {

                        REDeleteCascadeDemandeAPrestationsDues.supprimerPrestationCascade_noCommit(session,
                                transaction, decision, IREValidationLevel.VALIDATION_LEVEL_ALL);

                        REAnnexeDecisionManager mgr3 = new REAnnexeDecisionManager();
                        mgr3.setSession(session);
                        mgr3.setForIdDecision(decision.getIdDecision());
                        mgr3.find(transaction);
                        for (Iterator iterator3 = mgr3.iterator(); iterator3.hasNext();) {
                            REAnnexeDecision annexe = (REAnnexeDecision) iterator3.next();
                            if (!annexe.isNew()) {
                                annexe.delete(transaction);
                            }
                        }

                        RECopieDecisionManager mgr4 = new RECopieDecisionManager();
                        mgr4.setSession(session);
                        mgr4.setForIdDecision(decision.getIdDecision());
                        mgr4.find(transaction);
                        for (Iterator iterator4 = mgr4.iterator(); iterator4.hasNext();) {
                            RECopieDecision copie = (RECopieDecision) iterator4.next();
                            if (!copie.isNew()) {
                                copie.delete(transaction);
                            }
                        }

                        RERemarqueGroupePeriodeManager remMgr = new RERemarqueGroupePeriodeManager();
                        remMgr.setSession(session);
                        remMgr.setForIdDecision(decision.getIdDecision());
                        remMgr.find(transaction, BManager.SIZE_NOLIMIT);
                        for (Iterator iterator = remMgr.iterator(); iterator.hasNext();) {
                            RERemarqueGroupePeriode rem = (RERemarqueGroupePeriode) iterator.next();
                            if (!rem.isNew()) {
                                rem.delete(transaction);
                            }
                        }

                        if (decision.getCsEtat().equals(IREDecision.CS_ETAT_VALIDE)) {

                            // Si la prestation de la décision n'est pas dans un lot définitif
                            REPrestations prestation = new REPrestations();
                            prestation.setSession(session);
                            prestation.setIdDecision(decision.getIdDecision());
                            prestation.setIdDemandeRente(decision.getIdDemandeRente());
                            prestation.retrieve(transaction);

                            if (prestation.isNew()) {

                                REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(transaction,
                                        session, decision);

                            } else {

                                if (JadeStringUtil.isIntegerEmpty(prestation.getIdLot())) {

                                    REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(
                                            transaction, session, decision);

                                } else {

                                    // Retrieve du lot
                                    RELot lot = new RELot();
                                    lot.setSession(session);
                                    lot.setIdLot(prestation.getIdLot());
                                    lot.retrieve(transaction);

                                    if (!lot.isNew()) {

                                        // si le lot est définitif
                                        if (lot.getCsEtatLot().equals(IRELot.CS_ETAT_LOT_VALIDE)) {

                                            // On peut pas modifier
                                            throw new Exception(session.getLabel("ERREUR_PREST_ETAT_VALIDE"));

                                        } else {

                                            REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(
                                                    transaction, session, decision);
                                        }
                                    }
                                }

                            }
                        }

                        decision.retrieve(transaction);
                        decision.delete(transaction);

                    }
                    validDec.delete(transaction);
                }
            }
            if (error) {
                throw new REAttemptDeletionDecisionValidateException();
            }
        } else {
            transaction.addErrors("Delete cascade error idPrestationDue is empty");
        }
    }

    private static void chargeEtSupprimeFactureARestituer(final BSession session, final BITransaction transaction,
            final String idRenteAccordee) throws Exception {

        if (!JadeStringUtil.isIntegerEmpty(idRenteAccordee)) {
            REFacturesARestituerManager mgr = new REFacturesARestituerManager();
            mgr.setSession(session);
            mgr.setForIdRA(idRenteAccordee);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
                REFactureARestituer far = (REFactureARestituer) iterator.next();

                if (!far.isNew()) {
                    REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeSoldePourRestitutionParIdFactARestituer(
                            session, transaction, far.getIdFactureARestituer());
                    far.delete(transaction);
                }
            }
        } else {
            transaction.addErrors("Delete cascade error idRenteAccordee is empty");
        }
    }

    private static void chargeEtSupprimeOrdresVersements(final BSession session, final BITransaction transaction,
            final String idOrdreVersement) throws Exception {

        // TODO VOIR AVEC SCR !!!
        // Ordres de versements ? supprimer aussi en même temps que les créances accordées
        // Et les prestations ??

    }

    /*
	 *
	 *
	 *
	 *
	 */
    private static void chargeEtSupprimePrestationsDues(final BSession session, final BITransaction transaction,
            final String idRenteAccordee) throws Exception {

        if (!JadeStringUtil.isIntegerEmpty(idRenteAccordee)) {

            REPrestationsDuesManager mgr = new REPrestationsDuesManager();
            mgr.setSession(session);
            mgr.setForIdRenteAccordes(idRenteAccordee);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iter = mgr.iterator(); iter.hasNext();) {
                REPrestationDue pd = (REPrestationDue) iter.next();
                if (!pd.isNew()) {
                    REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeDecisionsParPrstDueCascade_noCommit(session,
                            transaction, pd.getIdPrestationDue());
                    pd.delete(transaction);
                }
            }
        } else {
            transaction.addErrors("Delete cascade error idRenteAccordee is empty");
        }
    }

    private static void chargeEtSupprimeRentesAccordees(final BSession session, final BITransaction transaction,
            final String idBasesCalcul) throws Exception {

        if (!JadeStringUtil.isIntegerEmpty(idBasesCalcul)) {

            RERenteAccordeeManager mgr = new RERenteAccordeeManager();
            mgr.setForIdBaseCalcul(idBasesCalcul);
            mgr.setSession(session);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (RERenteAccordee ra : mgr.getContainerAsList()) {
                if (!ra.isNew()) {
                    REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimePrestationsDues(session, transaction,
                            ra.getIdPrestationAccordee());
                    REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeAnnonces(session, transaction,
                            ra.getIdPrestationAccordee());
                    REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeCreancesAccordees(session, transaction, ra);
                    REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeRetenues(session, transaction,
                            ra.getIdPrestationAccordee());
                    REDeleteCascadeDemandeAPrestationsDues.chargerEtSupprimerRenteVerseeATort(session, transaction,
                            ra.getIdPrestationAccordee());
                }

                REInformationsComptabilite ic = new REInformationsComptabilite();
                ic.setSession(session);
                ic.setIdInfoCompta(ra.getIdInfoCompta());
                ic.retrieve(transaction);

                if (!ic.isNew()) {
                    ic.delete(transaction);
                }

                if (!ra.isNew()) {
                    ra.delete(transaction);
                }
            }
        } else {
            transaction.addErrors("Delete cascade error idBaseCalcul is empty");
        }
    }

    private static void chargeEtSupprimeRetenues(final BSession session, final BITransaction transaction,
            final String idRenteAccordee) throws Exception {

        if (!JadeStringUtil.isIntegerEmpty(idRenteAccordee)) {
            RERetenuesPaiementManager mgr = new RERetenuesPaiementManager();
            mgr.setSession(session);
            mgr.setForIdRenteAccordee(idRenteAccordee);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
                RERetenuesPaiement rp = (RERetenuesPaiement) iterator.next();

                if (!rp.isNew()) {
                    REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeSoldePourRestitutionParIdRetenue(session,
                            transaction, rp.getIdRetenue());
                    rp.delete(transaction);
                }
            }
        } else {
            transaction.addErrors("Delete cascade error idRenteAccordee is empty");
        }
    }

    private static void chargeEtSupprimeSoldePourRestitutionParIdFactARestituer(final BSession session,
            final BITransaction transaction, final String idFAR) throws Exception {

        // Suppression du/des soldes pour restitution
        RESoldePourRestitutionManager mgr3 = new RESoldePourRestitutionManager();
        mgr3.setSession(session);
        mgr3.setForIdFactureARestituer(idFAR);
        mgr3.find(transaction);
        for (int i = 0; i < mgr3.size(); i++) {
            RESoldePourRestitution elm = (RESoldePourRestitution) mgr3.getEntity(i);
            elm.delete(transaction);
        }
    }

    private static void chargeEtSupprimeSoldePourRestitutionParIdPrestation(final BSession session,
            final BITransaction transaction, final String idPrestation) throws Exception {

        // Suppression du/des soldes pour restitution
        RESoldePourRestitutionManager mgr3 = new RESoldePourRestitutionManager();
        mgr3.setSession(session);
        mgr3.setForIdPrestation(idPrestation);
        mgr3.find(transaction);
        for (int i = 0; i < mgr3.size(); i++) {
            RESoldePourRestitution elm = (RESoldePourRestitution) mgr3.getEntity(i);
            elm.delete(transaction);
        }
    }

    private static void chargeEtSupprimeSoldePourRestitutionParIdRetenue(final BSession session,
            final BITransaction transaction, final String idRetenue) throws Exception {

        // Suppression du/des soldes pour restitution
        RESoldePourRestitutionManager mgr3 = new RESoldePourRestitutionManager();
        mgr3.setSession(session);
        mgr3.setForIdRetenue(idRetenue);
        mgr3.find(transaction);
        for (int i = 0; i < mgr3.size(); i++) {
            RESoldePourRestitution elm = (RESoldePourRestitution) mgr3.getEntity(i);
            elm.delete(transaction);
        }
    }

    private static void chargerEtSupprimerRenteVerseeATort(final BSession session, final BITransaction transaction,
            final String idPrestationAccordee) throws Exception {

        if (!JadeStringUtil.isBlank(idPrestationAccordee)) {

            // on ne supprime que les rentes versées à tort dont la rente accordée passée en paramètre est la rente du
            // nouveau droit
            RERenteVerseeATortManager mgr = new RERenteVerseeATortManager();
            mgr.setSession(session);
            mgr.setForIdRenteNouveauDroit(Long.parseLong(idPrestationAccordee));
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (RERenteVerseeATort uneRenteVerseeATort : mgr.getContainerAsList()) {
                uneRenteVerseeATort.delete(transaction);
            }

        } else {
            transaction.addErrors("Delete cascade error : idPrestationAccordee is empty");
        }
    }

    /**
     * Méthode qui va charger la demande de rente selon le type d'objet passé en paramètres
     */
    private static REDemandeRente loadDemandeRente(final BSession session, final BITransaction transaction,
            final Object object) throws Exception {

        REDemandeRente demandeRente = new REDemandeRente();

        // Selon le type d'objet passé en paramètres

        // Si demande de rente
        if (object instanceof REDemandeRente) {
            demandeRente = (REDemandeRente) object;

            // Si rente calculée
        } else if (object instanceof RERenteCalculee) {
            RERenteCalculee rc = (RERenteCalculee) object;

            demandeRente.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
            demandeRente.setIdRenteCalculee(rc.getIdRenteCalculee());
            // Si décision
        } else if (object instanceof REDecisionEntity) {
            REDecisionEntity d = (REDecisionEntity) object;

            demandeRente.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
            demandeRente.setIdDemandeRente(d.getIdDemandeRente());
            // Si base de calcul
        } else if (object instanceof REBasesCalcul) {
            REBasesCalcul bc = (REBasesCalcul) object;

            RERenteCalculee rc = new RERenteCalculee();
            rc.setSession(session);
            rc.setIdRenteCalculee(bc.getIdRenteCalculee());
            rc.retrieve(transaction);
            PRAssert.notIsNew(rc, null);

            demandeRente.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
            demandeRente.setIdRenteCalculee(rc.getIdRenteCalculee());

            // Si rente accordée
        } else if (object instanceof RERenteAccordee) {
            RERenteAccordee ra = (RERenteAccordee) object;

            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession(session);
            bc.setIdBasesCalcul(ra.getIdBaseCalcul());
            bc.retrieve(transaction);

            RERenteCalculee rc = new RERenteCalculee();
            rc.setSession(session);
            rc.setIdRenteCalculee(bc.getIdRenteCalculee());
            rc.retrieve(transaction);
            PRAssert.notIsNew(rc, null);

            demandeRente.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
            demandeRente.setIdRenteCalculee(rc.getIdRenteCalculee());

            // Si prestation due
        } else if (object instanceof REPrestationDue) {
            REPrestationDue pd = (REPrestationDue) object;

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(session);
            ra.setIdPrestationAccordee(pd.getIdRenteAccordee());
            ra.retrieve(transaction);

            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession(session);
            bc.setIdBasesCalcul(ra.getIdBaseCalcul());
            bc.retrieve(transaction);

            RERenteCalculee rc = new RERenteCalculee();
            rc.setSession(session);
            rc.setIdRenteCalculee(bc.getIdRenteCalculee());
            rc.retrieve(transaction);
            PRAssert.notIsNew(rc, null);

            demandeRente.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
            demandeRente.setIdRenteCalculee(rc.getIdRenteCalculee());

        }

        demandeRente.setSession(session);
        demandeRente.retrieve(transaction);

        return demandeRente;
    }

    /*
     * Charge les prestations dues de la décisions, par rente accordées Seule les prestations dues de type "MENSUEL"
     * sont retournées Liste des rentes accordées concernées par les prestations dues
     * 
     * Format :
     * 
     * Key = idRenteAccordée Value = idPrstDue-idPrstDue-idPrstDue
     */
    protected static Map<String, String> loadPrestationsDues(final String decisionDu, final BSession session,
            final String idDecision) throws Exception {

        Map<String, String> idsPrstDuesParRenteAccordee = new HashMap<String, String>();

        REValidationDecisionsManager mgr = new REValidationDecisionsManager();
        mgr.setSession(session);
        mgr.setForIdDecision(idDecision);
        mgr.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < mgr.size(); i++) {
            REValidationDecisions validRente = (REValidationDecisions) mgr.getEntity(i);

            REPrestationDue pd = new REPrestationDue();
            pd.setSession(session);
            pd.setIdPrestationDue(validRente.getIdPrestationDue());
            pd.retrieve();

            REDeleteCascadeDemandeAPrestationsDues.assertNotIsNew(pd, "014 - Entity not found.");

            if (idsPrstDuesParRenteAccordee.containsKey(pd.getIdRenteAccordee())) {
                String lst = idsPrstDuesParRenteAccordee.get(pd.getIdRenteAccordee());
                lst += pd.getIdPrestationDue() + "-";
                idsPrstDuesParRenteAccordee.put(pd.getIdRenteAccordee(), lst);
            } else {
                String elm = pd.getIdPrestationDue() + "-";
                idsPrstDuesParRenteAccordee.put(pd.getIdRenteAccordee(), elm);
            }
        }
        return idsPrstDuesParRenteAccordee;
    }

    /**
     * Pour supprimer une base de calcul, ainsi que tous les objets liés. On ne peut pas faire cette suppression si
     * l'état de demande est VALIDE, TRANSFERE ou PAYE La suppression d'une base de calcul entraine la suppression de :
     * - Base calcul - Rentes accordées - Annonces - Prestations dues Avec niveau de validation
     */
    public synchronized static final void supprimerBaseCalculCascade_noCommit(final BSession session,
            final BITransaction transaction, final REBasesCalcul baseCalcul) throws Exception {

        // Load de la demande de rente
        REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                baseCalcul);

        // Validation de la demande de rente
        REDeleteCascadeDemandeAPrestationsDues.validationDemandeRente(session, demandeRente, "la base de calculs");

        // Suppression de la base de calculs et des rentes accordées et des prestations dues de la base de calculs
        REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeRentesAccordees(session, transaction,
                baseCalcul.getIdBasesCalcul());

        // Test récup 10ème
        REBasesCalcul theBase = new REBasesCalculDixiemeRevision();
        theBase.setSession(session);
        theBase.setIdBasesCalcul(baseCalcul.getIdBasesCalcul());
        theBase.retrieve(transaction);
        if (theBase.isNew()) {
            // Test récup 9ème
            theBase = new REBasesCalculNeuviemeRevision();
            theBase.setSession(session);
            theBase.setIdBasesCalcul(baseCalcul.getIdBasesCalcul());
            theBase.retrieve(transaction);
        }
        if (theBase.isNew()) {
            throw new Exception("Base Calcul error : No revision found idBC=" + theBase.getIdBasesCalcul());
        }

        theBase.delete(transaction);

        if (!baseCalcul.isNew()) {
            baseCalcul.delete(transaction);
        }

    }

    /**
     * Réinitialise la demande de rente. Ne supprime pas la demande mais supprime toutes les entités générées lors de la
     * remontée du calcul ACOR
     * Supprime : </br>
     * -
     * La session sera récupérée depuis la demande.
     * La transaction utilisée sera celle rattachée à la session de la demande
     * 
     * @throws Exception
     */
    public synchronized static final void reinitiliserDemandeRente(final REDemandeRente demande) throws Exception {
        if (demande == null) {
            throw new IllegalArgumentException("reinitiliserDemandeRente : REDemandeRente argument can not be null");
        }
        BSession session = demande.getSession();
        BTransaction transaction = session.getCurrentThreadTransaction();

        RERenteCalculee renteCalculee = new RERenteCalculee();
        renteCalculee.setSession(session);
        renteCalculee.setIdRenteCalculee(demande.getIdRenteCalculee());
        renteCalculee.retrieve(transaction);

        if (!renteCalculee.isNew()) {

            REDeleteCascadeDemandeAPrestationsDues.validationDemandeRente(session, demande, "les bases de calculs");

            REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeBasesCalculs(session, transaction,
                    renteCalculee.getIdRenteCalculee());
        }
    }

    /**
     * Pour supprimer toutes les bases de calculs d'une rente calculée, ainsi que tous les objets liés. On ne peut pas
     * faire cette suppression si l'état de demande est VALIDE, TRANSFERE ou PAYE La suppression de ces bases de calculs
     * entraine la suppression de : - Bases calculs - Rentes accordées - Annonces - Prestations dues Avec niveau de
     * validation
     */
    public synchronized static final void supprimerBasesCalculCascade_noCommit(final BSession session,
            final BITransaction transaction, final RERenteCalculee renteCalculee, final int validationLevel)
            throws Exception {

        // Load de la demande de rente
        REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                renteCalculee);

        // Validation de la demande de rente
        REDeleteCascadeDemandeAPrestationsDues.validationDemandeRente(session, demandeRente, "les bases de calculs");

        // Suppression des bases de calculs, des rentes accordées et des prestations dues de la rente accordée
        REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeBasesCalculs(session, transaction,
                renteCalculee.getIdRenteCalculee());

        /*
         * TODO VOIR TRAITEMENT CORRECT A FAIRE Pour les demandes de rentes liées entre elles :
         * 
         * On regarde si la demande de la renteCalculee est la demande principale d'une demande secondaire, si c'est le
         * cas, on supprime les demandes secondaires.
         */
        /*
         * REDemandeRenteLieeManager mgr = new REDemandeRenteLieeManager(); mgr.setSession(session);
         * mgr.setForIdDemandeRentePrincipale(demandeRente.getIdDemandeRente()); mgr.find(transaction);
         * 
         * if (!mgr.isEmpty()){
         * 
         * for (Iterator iterator = mgr.iterator(); iterator.hasNext();) { REDemandeRenteLiee renteLiee =
         * (REDemandeRenteLiee) iterator.next();
         * 
         * // A) Si la demande liée est une copie on la supprime if (renteLiee.isSecondaireCopie().booleanValue()){
         * 
         * // 1) Effacer la demande secondaire REDemandeRente demande = new REDemandeRente();
         * demande.setIdDemandeRente(renteLiee.getIdDemandeRenteSecondaire()); demande.setSession(session);
         * demande.retrieve(transaction);
         * 
         * supprimerDemandeRenteCascade_noCommit(session, transaction, demande, validationLevel);
         * 
         * // 2) Effacer la renteLiee renteLiee.delete(transaction);
         * 
         * // B) Sinon on met de la demande en enregistré et on deleteCascade } else {
         * 
         * REDemandeRente demande = new REDemandeRente();
         * demande.setIdDemandeRente(renteLiee.getIdDemandeRenteSecondaire()); demande.setSession(session);
         * demande.retrieve(transaction);
         * 
         * RERenteCalculee rcal = new RERenteCalculee(); rcal.setSession(session);
         * rcal.setIdRenteCalculee(demande.getIdRenteCalculee()); rcal.retrieve(transaction);
         * 
         * if (!rcal.isNew()){ supprimerRenteCalculeeCascade_noCommit(session, transaction, rcal, validationLevel); }
         * 
         * demande.setIdRenteCalculee(""); demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
         * demande.update(transaction);
         * 
         * }
         * 
         * }
         * 
         * }
         */

    }

    /**
     * Pour supprimer toutes les decisions et objects liées sous jacents. On ne peut pas faire cette suppression si
     * l'état de demande est VALIDE, TRANSFERE ou PAYE Les décisions validées ne sont supprimées que si le lot n'est pas
     * parti en compta. La suppression de ces validations de décisions entraine la suppression de : Suppression d'une
     * décision : Traitement à faire : Suppression de la décision Suppr. annexe + copies Suppr. de la presation Suppr.
     * des remarques groupe période Suppr. vald. décision Supprs. des OV pour chaque OV de type Dette : récupérer la RA
     * à diminuer mettre date de fin = null; mettre etat = VALIDE mettre code mutation = null Avec niveau de validation
     */
    public synchronized static final void supprimerDecisionsCascade_noCommit(final BSession session,
            final BITransaction transaction, final REDecisionEntity decision, final int validationLevel)
            throws Exception {

        if (validationLevel >= IREValidationLevel.VALIDATION_LEVEL_HIGH) {

            // Load de la demande de rente
            REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                    decision);

            // Validation de la demande de rente
            REDeleteCascadeDemandeAPrestationsDues
                    .validationDemandeRente(session, demandeRente, "les prestations dues");
        } else if (validationLevel >= IREValidationLevel.VALIDATION_LEVEL_MEDIUM) {

            // Load de la demande de rente
            REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                    decision);

            // Validation de la demande de rente
            // Lors de la validation du RETRO, la demande peut se trouver dans l'état partiel.
            REDeleteCascadeDemandeAPrestationsDues.validationDemandeRenteLow(session, demandeRente,
                    "les prestations dues");

        }

        // Suppression des décisions
        REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeDecisionsCascade_noCommit(session, transaction,
                decision.getIdDecision());

    }

    /**
     * Pour supprimer toutes les decisions et objects liées sous jacents. On ne peut pas faire cette suppression si
     * l'état de demande est VALIDE, TRANSFERE ou PAYE Les décisions validées ne sont supprimées que si le lot n'est pas
     * parti en compta. La suppression de ces validations de décisions entraine la suppression de : Suppression d'une
     * décision : Traitement à faire : Suppression de la décision Suppr. annexe + copies Suppr. de la presation Suppr.
     * des remarques groupe période Suppr. vald. décision Supprs. des OV pour chaque OV de type Dette : récupérer la RA
     * à diminuer mettre date de fin = null; mettre etat = VALIDE mettre code mutation = null Avec niveau de validation
     */
    public synchronized static final void supprimerDecisionsCascade_noCommit(final BSession session,
            final BITransaction transaction, final REPrestationDue prestationDue, final int validationLevel)
            throws Exception {

        if (validationLevel >= IREValidationLevel.VALIDATION_LEVEL_HIGH) {

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(session);
            ra.setIdPrestationAccordee(prestationDue.getIdRenteAccordee());
            ra.retrieve(transaction);

            // Load de la demande de rente
            REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                    ra);

            // Validation de la demande de rente
            REDeleteCascadeDemandeAPrestationsDues
                    .validationDemandeRente(session, demandeRente, "les prestations dues");
        } else if (validationLevel >= IREValidationLevel.VALIDATION_LEVEL_MEDIUM) {
            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(session);
            ra.setIdPrestationAccordee(prestationDue.getIdRenteAccordee());
            ra.retrieve(transaction);

            // Load de la demande de rente
            REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                    ra);

            // Validation de la demande de rente
            // Lors de la validation du RETRO, la demande peut se trouver dans l'état partiel.
            REDeleteCascadeDemandeAPrestationsDues.validationDemandeRenteLow(session, demandeRente,
                    "les prestations dues");

        }

        // Suppression des décisions
        REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeDecisionsParPrstDueCascade_noCommit(session,
                transaction, prestationDue.getIdPrestationDue());

    }

    /**
     * Pour supprimer une demande de rente, ainsi que tous les objets liés. On ne peut pas faire cette suppression si
     * l'état de demande est PARTIEL, VALIDE, TRANSFERE ou PAYE La suppression d'une demande de rente entraine la
     * suppression de : - Demande de rente - Rente calculée - Bases calculs - Rentes accordées - Annonces - Prestations
     * dues Avec niveau de validation
     */
    public synchronized static final void supprimerDemandeRenteCascade_noCommit(final BSession session,
            final BITransaction transaction, REDemandeRente demandeRente, final int validationLevel) throws Exception {

        // Load de la demande de rente
        demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction, demandeRente);

        if ((demandeRente == null) || demandeRente.isNew()) {
            throw new Exception("Demande rente is null !!!");
        }

        // Validation de la demande de rente
        REDeleteCascadeDemandeAPrestationsDues.validationDemandeRente(session, demandeRente, "la demande de rente");

        // Suppression des créanciers et créances accordées
        REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeCreanciers(session, transaction,
                demandeRente.getIdDemandeRente());

        // Suppression de la demande et de la rente calculée, des bases de calculs, des rentes accordées et des
        // prestations dues de la demande
        RERenteCalculee rc = new RERenteCalculee();
        rc.setSession(session);
        rc.setIdRenteCalculee(demandeRente.getIdRenteCalculee());
        rc.retrieve(transaction);

        if (!rc.isNew()) {
            REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeBasesCalculs(session, transaction,
                    rc.getIdRenteCalculee());

            /*
             * TODO VOIR TRAITEMENT CORRECT A FAIRE Pour les demandes de rentes liées entre elles :
             * 
             * On regarde si la demande de la renteCalculee est la demande principale d'une demande secondaire, si c'est
             * le cas, on supprime les demandes secondaires.
             */
            /*
             * REDemandeRenteLieeManager mgr = new REDemandeRenteLieeManager(); mgr.setSession(session);
             * mgr.setForIdDemandeRentePrincipale(demandeRente.getIdDemandeRente()); mgr.find(transaction);
             * 
             * if (!mgr.isEmpty()){
             * 
             * for (Iterator iterator = mgr.iterator(); iterator.hasNext();) { REDemandeRenteLiee renteLiee =
             * (REDemandeRenteLiee) iterator.next();
             * 
             * // A) Si la demande liée est une copie on la supprime if (renteLiee.isSecondaireCopie().booleanValue()){
             * 
             * // 1) Effacer la demande secondaire REDemandeRente demande = new REDemandeRente();
             * demande.setIdDemandeRente(renteLiee.getIdDemandeRenteSecondaire()); demande.setSession(session);
             * demande.retrieve(transaction);
             * 
             * supprimerDemandeRenteCascade_noCommit(session, transaction, demande, validationLevel);
             * 
             * // 2) Effacer la renteLiee renteLiee.delete(transaction);
             * 
             * // B) Sinon on met de la demande en enregistré et on deleteCascade } else {
             * 
             * REDemandeRente demande = new REDemandeRente();
             * demande.setIdDemandeRente(renteLiee.getIdDemandeRenteSecondaire()); demande.setSession(session);
             * demande.retrieve(transaction);
             * 
             * RERenteCalculee rcal = new RERenteCalculee(); rcal.setSession(session);
             * rcal.setIdRenteCalculee(demande.getIdRenteCalculee()); rcal.retrieve(transaction);
             * 
             * if (!rcal.isNew()){ supprimerRenteCalculeeCascade_noCommit(session, transaction, rcal, validationLevel);
             * }
             * 
             * demande.setIdRenteCalculee(""); demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
             * demande.update(transaction);
             * 
             * }
             * 
             * }
             * 
             * }
             */
            rc.delete(transaction);
        }
        // Plausi...
        else {
            // Avant de le remplacer, on s'assure qu'il n'y a plus aucune base de calcul liée à cet ancien ID !!!
            if (!JadeStringUtil.isBlankOrZero(rc.getIdRenteCalculee())) {
                REBasesCalculManager mgr = new REBasesCalculManager();
                mgr.setSession(session);
                mgr.setForIdRenteCalculee(rc.getIdRenteCalculee());
                mgr.find(transaction, 2);
                if (!mgr.isEmpty()) {
                    throw new Exception(
                            "Incohérance dans les données, des bases de calculs existe pour idRenteCalculee/idDemandeRente = "
                                    + rc.getIdRenteCalculee() + "/" + demandeRente.getIdDemandeRente());
                }
            }

        }

        // Retrouver le type de demande de rente et supprimer
        if (demandeRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {

            REDemandeRenteAPI renteApi = new REDemandeRenteAPI();
            renteApi.setSession(session);
            renteApi.setIdDemandeRente(demandeRente.getIdDemandeRente());
            renteApi.retrieve(transaction);

            // Retrouver les périodes API et les supprimer
            REPeriodeAPIManager perAPIMgr = new REPeriodeAPIManager();
            perAPIMgr.setSession(session);
            perAPIMgr.setForIdDemandeRente(demandeRente.getIdDemandeRente());
            perAPIMgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iter = perAPIMgr.iterator(); iter.hasNext();) {
                REPeriodeAPI perAPI = (REPeriodeAPI) iter.next();

                if (!perAPI.isNew()) {
                    perAPI.delete(transaction);
                }

            }

            if (!renteApi.isNew()) {
                renteApi.delete(transaction);
            }

        } else if (demandeRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)) {

            REDemandeRenteInvalidite renteInv = new REDemandeRenteInvalidite();
            renteInv.setSession(session);
            renteInv.setIdDemandeRente(demandeRente.getIdDemandeRente());
            renteInv.retrieve(transaction);

            // Retrouver les périodes INV et les supprimer
            REPeriodeInvaliditeManager perINVMgr = new REPeriodeInvaliditeManager();
            perINVMgr.setSession(session);
            perINVMgr.setForIdDemandeRente(demandeRente.getIdDemandeRente());
            perINVMgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (Iterator iter = perINVMgr.iterator(); iter.hasNext();) {
                REPeriodeInvalidite perINV = (REPeriodeInvalidite) iter.next();

                if (!perINV.isNew()) {
                    perINV.delete(transaction);
                }

            }

            if (!renteInv.isNew()) {
                renteInv.delete(transaction);
            }

        } else if (demandeRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {

            REDemandeRenteSurvivant renteSur = new REDemandeRenteSurvivant();
            renteSur.setSession(session);
            renteSur.setIdDemandeRente(demandeRente.getIdDemandeRente());
            renteSur.retrieve(transaction);

            if (!renteSur.isNew()) {
                renteSur.delete(transaction);
            }

        } else if (demandeRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE)) {

            REDemandeRenteVieillesse renteVie = new REDemandeRenteVieillesse();
            renteVie.setSession(session);
            renteVie.setIdDemandeRente(demandeRente.getIdDemandeRente());
            renteVie.retrieve(transaction);

            if (!renteVie.isNew()) {
                renteVie.delete(transaction);
            }

        } else {
            throw new Exception("Type de demande inconnu idDemande=" + demandeRente.getIdDemandeRente());
        }

        // Retrouver l'informations complémentaires et les supprimer
        PRInfoCompl infoCompl = new PRInfoCompl();
        infoCompl.setSession(session);
        infoCompl.setIdInfoCompl(demandeRente.getIdInfoComplementaire());
        infoCompl.retrieve(transaction);

        if (!infoCompl.isNew()) {
            infoCompl.delete(transaction);
        }

        if (!demandeRente.isNew()) {
            demandeRente.delete(transaction);
        }

    }

    /**
     * Pour supprimer toutes les prestations d'une décisions ainsi que tous les objets liés. On ne peut pas faire cette
     * suppression si l'état de la prestation est CS_ETAT_PRE_DEFINITIF ou si l'état de la décisions est CS_ETAT_VALIDE,
     * ou si l'état du lot dans lequel se trouve la prestation est VALIDE La suppression de cette prestations entraine
     * la suppression de : - OV pour chaque OV de type Dette : récupérer la RA à diminuer mettre date de fin = null;
     * mettre etat = VALIDE mettre code mutation = null Avec niveau de validation
     */
    public synchronized static final void supprimerPrestationCascade_noCommit(final BSession session,
            final BITransaction transaction, final REDecisionEntity decision, final int validationLevel)
            throws Exception {

        if ((decision == null) || decision.isNew()) {
            throw new Exception("Decision is null !!!");
        }

        REPrestationsManager mgr2 = new REPrestationsManager();
        mgr2.setSession(session);
        mgr2.setForIdDecision(decision.getIdDecision());
        mgr2.find(transaction);
        // bz-5899
        // if (mgr2.size() > 1) {
        // throw new Exception("Cannot have more than 1 prestation for decision id = " + decision.getIdDecision());
        // }
        for (Iterator iterator = mgr2.iterator(); iterator.hasNext();) {
            REPrestations prst = (REPrestations) iterator.next();
            if (!prst.isNew()) {
                if (IREPrestations.CS_ETAT_PRE_DEFINITIF.equals(prst.getCsEtat())) {
                    throw new Exception(session.getLabel("ERREUR_PREST_ETAT_DEFINITIF"));
                }

                if (!JadeStringUtil.isBlankOrZero(prst.getIdLot())) {
                    RELot lot = new RELot();
                    lot.setSession(session);
                    lot.setIdLot(prst.getIdLot());
                    lot.retrieve(transaction);
                    PRAssert.notIsNew(lot,
                            "Incohérence dans les données. Impossible de retrouver le lot. idDecision/idLot = "
                                    + decision.getIdDecision() + "/" + prst.getIdLot());

                    if (IRELot.CS_ETAT_LOT_VALIDE.equals(lot.getCsEtatLot())) {
                        throw new Exception(session.getLabel("ERREUR_PREST_ETAT_VALID"));
                    }
                }

                // Supression des OV
                REOrdresVersementsManager mgr = new REOrdresVersementsManager();
                mgr.setSession(session);
                mgr.setForIdPrestation(prst.getIdPrestation());
                mgr.find(transaction, BManager.SIZE_NOLIMIT);

                for (int i = 0; i < mgr.size(); i++) {
                    REOrdresVersements ov = (REOrdresVersements) mgr.getEntity(i);

                    // MAJ de la RA qu'il fallait diminuer
                    if ((ov.getCsTypeOrdreVersement() == TypeOrdreVersement.DIMINUTION_DE_RENTE)
                            && IREDecision.CS_ETAT_VALIDE.equals(decision.getCsEtat())) {

                        RERenteAccordee ra = new RERenteAccordee();
                        ra.setSession(session);
                        ra.setIdPrestationAccordee(ov.getIdRenteAccordeeDiminueeParOV());
                        ra.retrieve(transaction);
                        PRAssert.notIsNew(ra, null);

                        ra.setDateFinDroit(null);
                        ra.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
                        ra.setCodeMutation(null);
                        ra.update(transaction);
                    }

                    if (!ov.isNew()) {
                        REDeleteCascadeOrdresVersements.supprimerOrdreVersementCascade_noCommit(session, transaction,
                                ov, validationLevel);
                    }
                }

                // Suppression du/des soldes pour restitution
                REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeSoldePourRestitutionParIdPrestation(session,
                        transaction, prst.getIdPrestation());
                prst.delete(transaction);
            }
        }
    }

    /**
     * Pour supprimer une prestation due, ainsi que tous les objets liés. On ne peut pas faire cette suppression si
     * l'état de demande est VALIDE, TRANSFERE ou PAYE La suppression d'une prestation due entraine la suppression de :
     * - Prestation due Avec niveau de validation
     */
    public synchronized static final void supprimerPrestationDueCascade_noCommit(final BSession session,
            final BITransaction transaction, final REPrestationDue prestationDue, final int validationLevel)
            throws Exception {

        // Load de la demande de rente
        REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                prestationDue);

        // Validation de la demande de rente
        REDeleteCascadeDemandeAPrestationsDues.validationDemandeRente(session, demandeRente, "la prestation due");

        // Suppression de la prestation due
        if (!prestationDue.isNew()) {
            prestationDue.delete(transaction);
        }

    }

    /**
     * Pour supprimer toutes les prestations dues d'une rente accordée, ainsi que tous les objets liés. On ne peut pas
     * faire cette suppression si l'état de demande est VALIDE, TRANSFERE ou PAYE La suppression de ces prestations dues
     * entraine la suppression de : - Prestations dues - Decisions Avec niveau de validation
     */
    public synchronized static final void supprimerPrestationsDuesCascade_noCommit(final BSession session,
            final BITransaction transaction, final RERenteAccordee renteAccordee, final int validationLevel)
            throws Exception {

        // Load de la demande de rente
        REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                renteAccordee);

        // Validation de la demande de rente
        REDeleteCascadeDemandeAPrestationsDues.validationDemandeRente(session, demandeRente, "les prestations dues");

        // Suppression des prestations dues
        REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimePrestationsDues(session, transaction,
                renteAccordee.getIdPrestationAccordee());

    }

    /**
     * Pour supprimer une rente accordée, ainsi que tous les objets liés. On ne peut pas faire cette suppression si
     * l'état de demande est VALIDE, TRANSFERE ou PAYE La suppression d'une rente accordée entraine la suppression de :
     * - Rente accordée - Annonces - Prestations dues Avec niveau de validation
     */
    public synchronized static final void supprimerRenteAccordeeCascade_noCommit(final BSession session,
            final BITransaction transaction, final RERenteAccordee renteAccordee, final int validationLevel)
            throws Exception {

        if (JadeStringUtil.isBlankOrZero(renteAccordee.getCsEtat())
                || IREPrestationAccordee.CS_ETAT_VALIDE.equals(renteAccordee.getCsEtat())
                || IREPrestationAccordee.CS_ETAT_PARTIEL.equals(renteAccordee.getCsEtat())
                || IREPrestationAccordee.CS_ETAT_DIMINUE.equals(renteAccordee.getCsEtat())) {
            throw new REBusinessException(session.getLabel("ERREUR_SUPPRESSION_RENTE_ACCORDEE_MAUVAIS_ETAT"));
        }

        // Load de la demande de rente
        REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                renteAccordee);

        // Validation de la demande de rente
        REDeleteCascadeDemandeAPrestationsDues.validationDemandeRente(session, demandeRente, "la rente accordée");

        RERenteAccordeeManager renteAccordeeManager = new RERenteAccordeeManager();
        renteAccordeeManager.setSession(session);
        renteAccordeeManager.setForIdBaseCalcul(renteAccordee.getIdBaseCalcul());
        renteAccordeeManager.find(transaction);

        // s'il n'y a plus que cette rente liée à la base de calcul, on utilise la suppression de la base de calcul
        if (renteAccordeeManager.size() == 1) {
            REBasesCalcul baseCalcul = new REBasesCalcul();
            baseCalcul.setSession(session);
            baseCalcul.setIdBasesCalcul(renteAccordee.getIdBaseCalcul());
            baseCalcul.retrieve(transaction);

            REDeleteCascadeDemandeAPrestationsDues
                    .supprimerBaseCalculCascade_noCommit(session, transaction, baseCalcul);
        } else {

            // Suppression de la rente accordée et des prestations dues de la rente accordée
            REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimePrestationsDues(session, transaction,
                    renteAccordee.getIdPrestationAccordee());

            REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeAnnonces(session, transaction,
                    renteAccordee.getIdPrestationAccordee());

            // Suppression des créances accordées
            REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeCreancesAccordees(session, transaction,
                    renteAccordee);

            // Suppression des retenues
            REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeRetenues(session, transaction,
                    renteAccordee.getIdPrestationAccordee());
            REDeleteCascadeDemandeAPrestationsDues.chargerEtSupprimerRenteVerseeATort(session, transaction,
                    renteAccordee.getIdPrestationAccordee());

            // Retrieve de l'info compta et suppression
            REInformationsComptabilite ic = new REInformationsComptabilite();
            ic.setSession(session);
            ic.setIdInfoCompta(renteAccordee.getIdInfoCompta());
            ic.retrieve(transaction);

            if (!ic.isNew()) {
                ic.delete(transaction);
            }

            if (!renteAccordee.isNew()) {
                renteAccordee.delete(transaction);
            }
        }

    }

    /**
     * Pour supprimer une rente accordée, ainsi que tous les objets liés. On ne peut pas faire cette suppression si
     * l'état de demande est VALIDE, TRANSFERE ou PAYE La suppression d'une rente accordée entraine la suppression de :
     * - Rente calculée (ne pas oublier d'effacer la ref. dans la demande de rente) - Bases calculs - Rentes accordées -
     * Annonces - Prestations dues Avec niveau de validation
     */
    public synchronized static final void supprimerRenteCalculeeCascade_noCommit(final BSession session,
            final BITransaction transaction, final RERenteCalculee renteCalculee, final int validationLevel)
            throws Exception {

        // Load de la demande de rente
        REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                renteCalculee);

        // Validation de la demande de rente
        REDeleteCascadeDemandeAPrestationsDues.validationDemandeRente(session, demandeRente, "la rente calculée");

        if (!renteCalculee.isNew()) {
            // Suppression de la rente calculée et des bases de calculs, des rentes accordées et des prestations dues de
            // la rente calculée
            REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeBasesCalculs(session, transaction,
                    renteCalculee.getIdRenteCalculee());

            /*
             * TODO VOIR TRAITEMENT CORRECT A FAIRE Pour les demandes de rentes liées entre elles :
             * 
             * On regarde si la demande de la renteCalculee est la demande principale d'une demande secondaire, si c'est
             * le cas, on supprime les demandes secondaires.
             */
            /*
             * REDemandeRenteLieeManager mgr = new REDemandeRenteLieeManager(); mgr.setSession(session);
             * mgr.setForIdDemandeRentePrincipale(demandeRente.getIdDemandeRente()); mgr.find(transaction);
             * 
             * if (!mgr.isEmpty()){
             * 
             * for (Iterator iterator = mgr.iterator(); iterator.hasNext();) { REDemandeRenteLiee renteLiee =
             * (REDemandeRenteLiee) iterator.next();
             * 
             * // A) Si la demande liée est une copie on la supprime if (renteLiee.isSecondaireCopie().booleanValue()){
             * 
             * // 1) Effacer la demande secondaire REDemandeRente demande = new REDemandeRente();
             * demande.setIdDemandeRente(renteLiee.getIdDemandeRenteSecondaire()); demande.setSession(session);
             * demande.retrieve(transaction);
             * 
             * supprimerDemandeRenteCascade_noCommit(session, transaction, demande, validationLevel);
             * 
             * // 2) Effacer la renteLiee renteLiee.delete(transaction);
             * 
             * // B) Sinon on met de la demande en enregistré et on deleteCascade } else {
             * 
             * REDemandeRente demande = new REDemandeRente();
             * demande.setIdDemandeRente(renteLiee.getIdDemandeRenteSecondaire()); demande.setSession(session);
             * demande.retrieve(transaction);
             * 
             * RERenteCalculee rcal = new RERenteCalculee(); rcal.setSession(session);
             * rcal.setIdRenteCalculee(demande.getIdRenteCalculee()); rcal.retrieve(transaction);
             * 
             * if (!rcal.isNew()){ supprimerRenteCalculeeCascade_noCommit(session, transaction, rcal, validationLevel);
             * }
             * 
             * demande.setIdRenteCalculee(""); demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
             * demande.update(transaction);
             * 
             * }
             * 
             * }
             * 
             * }
             */

            renteCalculee.delete(transaction);
        }

    }

    /**
     * Pour supprimer toutes les rentes accordées d'une base de calculs, ainsi que tous les objets liés. On ne peut pas
     * faire cette suppression si l'état de demande est VALIDE, TRANSFERE ou PAYE La suppression de ces rentes accordées
     * entraine la suppression de : - Rentes accordées - Annonces - Prestations dues Avec niveau de validation
     */
    public synchronized static final void supprimerRentesAccordeesCascade_noCommit(final BSession session,
            final BITransaction transaction, final REBasesCalcul baseCalcul, final int validationLevel)
            throws Exception {

        // Load de la demande de rente
        REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                baseCalcul);

        // Validation de la demande de rente
        REDeleteCascadeDemandeAPrestationsDues.validationDemandeRente(session, demandeRente, "les rentes accordées");

        // Suppression des rentes accordées et des prestations dues de chaque rente accordée
        REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeRentesAccordees(session, transaction,
                baseCalcul.getIdBasesCalcul());

    }

    /**
     * Pour supprimer une base de calcul, ainsi que tous les élément liés. Les tables qui sont impactées, sont :
     * #prestationAccordee (REPRACC)...........................................................................,
     * #basesCalcul (REBACAL)+ 9eme/10eme révision.............................................................,
     * **renteAccordee (REREACC)...............................................................................,
     * ***prestationDue (REPRSDU)..............................................................................,
     * ****decisions (REDECIS).................................................................................,
     * ***annonces (REANREN)...................................................................................,
     * ***creanceAccordee (RECRACC)............................................................................,
     * ***retenue (RERETEN)....................................................................................,
     * ****soldeRestitution (RESLDRST).........................................................................,
     * ***informationComptable (REINCOM).......................................................................,
     * 
     * @param session
     * @param transaction
     * @param baseCalcul
     */
    public synchronized static final void supprimerRenteVeuvePerdureCascade_noCommit(final BSession session,
            final BITransaction transaction, final REBasesCalcul baseCalcul) throws Exception {

        // Suppression de la prestationAccordée
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdBaseCalcul(baseCalcul.getIdBasesCalcul());
        ra.retrieve();

        if (!ra.isNew()) {
            REPrestationsAccordees prestAcc = new REPrestationsAccordees();
            prestAcc.setSession(session);
            prestAcc.setIdPrestationAccordee(ra.getId());
            prestAcc.retrieve();
            if (!prestAcc.isNew()) {
                prestAcc.delete(transaction);
            }
        }

        // Load de la demande de rente
        REDemandeRente demandeRente = REDeleteCascadeDemandeAPrestationsDues.loadDemandeRente(session, transaction,
                baseCalcul);

        // Validation de la demande de rente
        REDeleteCascadeDemandeAPrestationsDues.validationDemandeRente(session, demandeRente, "la base de calculs");

        // Suppression de la base de calculs et des rentes accordées et des prestations dues de la base de calculs
        REDeleteCascadeDemandeAPrestationsDues.chargeEtSupprimeRentesAccordees(session, transaction,
                baseCalcul.getIdBasesCalcul());

        // Test récup 10ème
        REBasesCalcul theBase = new REBasesCalculDixiemeRevision();
        theBase.setSession(session);
        theBase.setIdBasesCalcul(baseCalcul.getIdBasesCalcul());
        theBase.retrieve(transaction);
        if (theBase.isNew()) {
            // Test récup 9ème
            theBase = new REBasesCalculNeuviemeRevision();
            theBase.setSession(session);
            theBase.setIdBasesCalcul(baseCalcul.getIdBasesCalcul());
            theBase.retrieve(transaction);
        }
        if (theBase.isNew()) {
            throw new Exception("Base Calcul error : No revision found idBC=" + theBase.getIdBasesCalcul());
        }

        theBase.delete(transaction);

        if (!baseCalcul.isNew()) {
            baseCalcul.delete(transaction);
        }

    }

    private static void validationDemandeRente(final BSession session, final REDemandeRente demandeRente,
            final String elementSupprime) throws Exception {

        if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(demandeRente.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(demandeRente.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(demandeRente.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(demandeRente.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_PAYE.equals(demandeRente.getCsEtat())) {

            throw new Exception("Impossible de supprimer " + elementSupprime
                    + " pour une demande dans cet ETAT idDemande=" + demandeRente.getIdDemandeRente());
        }

    }

    private static void validationDemandeRenteLow(final BSession session, final REDemandeRente demandeRente,
            final String elementSupprime) throws Exception {

        if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(demandeRente.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(demandeRente.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(demandeRente.getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_PAYE.equals(demandeRente.getCsEtat())) {

            throw new Exception("Impossible de supprimer " + elementSupprime
                    + " pour une demande dans cet ETAT idDemande=" + demandeRente.getIdDemandeRente());
        }

    }

    private REDeleteCascadeDemandeAPrestationsDues() {
        // peut pas creer d'instances
    }

}