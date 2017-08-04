package globaz.corvus.utils.decisions;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.creances.IRECreancier;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.prestations.IREPrestations;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.creances.RECreanceAccordee;
import globaz.corvus.db.creances.RECreanceAccordeeManager;
import globaz.corvus.db.creances.RECreancier;
import globaz.corvus.db.creances.RECreancierManager;
import globaz.corvus.db.decisions.REAnnexeDecision;
import globaz.corvus.db.decisions.REAnnexeDecisionManager;
import globaz.corvus.db.decisions.RECopieDecision;
import globaz.corvus.db.decisions.RECopieDecisionManager;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REDecisionsManager;
import globaz.corvus.db.decisions.RERemarqueGroupePeriode;
import globaz.corvus.db.decisions.RERemarqueGroupePeriodeManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.ordresversements.RECompensationInterDecisions;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.db.recap.access.RERecapInfo;
import globaz.corvus.db.recap.access.RERecapInfoManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDues;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.helpers.decisions.RECompensationInterDecisionComparator;
import globaz.corvus.helpers.decisions.REOVInterDecisionVO;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.corvus.vb.decisions.KeyPeriodeInfo;
import globaz.corvus.vb.decisions.REAnnexeDecisionViewBean;
import globaz.corvus.vb.decisions.RECopieDecisionViewBean;
import globaz.corvus.vb.decisions.REPreValiderDecisionViewBean;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.business.services.models.decisions.DecisionService;

/**
 * @author SCR
 */
public class REDecisionsUtil {

    /**
     * Retourne si la décision peut être modifiée</br> Une décision ne peut pas être modifié dans le cas suivant :</br>
     * - Doit être en état validé</br> - La prestation liée à la décision doit être dans un lot validé (payé)</br>
     * <strong> DANS TOUS LES AUTRES CAS CETTE METHODE RETOURNERA TRUE. </strong>
     * 
     * @param idDecision
     *            L'id de la décision à tester
     * @param session
     *            La session courante
     * @return True si la décision est modifiable
     * @throws Exception
     */
    public static boolean isDecisionModifiable(String idDecision, BSession session) throws Exception {
        // Retrieve de la décision
        REDecisionEntity decision = new REDecisionEntity();
        decision.setSession(session);
        decision.setIdDecision(idDecision);
        decision.retrieve();

        if (!decision.isNew() && decision.getCsEtat().equals(IREDecision.CS_ETAT_VALIDE)) {
            // Récupération de la prestation
            REPrestationsManager prestationMgr = new REPrestationsManager();
            prestationMgr.setSession(session);
            prestationMgr.setForIdDecision(decision.getIdDecision());
            prestationMgr.find(1);

            REPrestations prestation = null;
            if (!prestationMgr.isEmpty()) {
                prestation = (REPrestations) prestationMgr.getFirstEntity();
            }

            // Si la prestation de la décision n'est pas dans un lot définitif
            if ((prestation != null) && !prestation.isNew()) {
                if (!JadeStringUtil.isIntegerEmpty(prestation.getIdLot())) {

                    // Retrieve du lot
                    RELot lot = new RELot();
                    lot.setSession(session);
                    lot.setIdLot(prestation.getIdLot());
                    lot.retrieve();

                    if (!lot.isNew()) {
                        // si le lot est définitif on ne peut pas modifier la décision
                        if (lot.getCsEtatLot().equals(IRELot.CS_ETAT_LOT_VALIDE)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Méthode utilisée pour savoir si la préparation d'une décision peut-être réalisée sur une demande
     */
    public static boolean isPreparationDecisionAuthorise(BSession session, String idDemandeRente) {
        try {
            if (session == null) {
                return false;
            }
            if (!JadeNumericUtil.isIntegerPositif(idDemandeRente)) {
                return false;
            }

            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession(session);
            demandeRente.setIdDemandeRente(idDemandeRente);
            demandeRente.retrieve();

            if (demandeRente.isNew()) {
                return false;
            }

            String dateDernierPaiement = REPmtMensuel.getDateDernierPmt(session);

            if (REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT.equals(dateDernierPaiement)) {
                String message = session.getLabel("ERREUR_IMPOSSIBLE_RETROUVER_DATE_DERNIER_PAIEMENT");
                throw new Exception(message);
            }

            if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(demandeRente.getCsEtat())) {

                SimpleDateFormat formater = new SimpleDateFormat("yyyyMM");

                Date dateDernierPaiementMensuel = new SimpleDateFormat("MM.yyyy").parse(dateDernierPaiement);
                String dateDernierPaiementMensuelFormate = formater.format(dateDernierPaiementMensuel);
                int dateDernierPaiementInteger = Integer.valueOf(dateDernierPaiementMensuelFormate);

                Date dateTraitement = new SimpleDateFormat("dd.MM.yyyy").parse(demandeRente.getDateTraitement());
                String dateTraitementFormate = formater.format(dateTraitement);

                /*
                 * Si la date de traitement de la demande est dans le mois comptable on autorise la préparation des
                 * décisions
                 */
                if (dateDernierPaiementMensuelFormate.equals(dateTraitementFormate)) {
                    return true;
                }

                /*
                 * Sinon on contrôle la date de début de toutes les rentes accordées de la demande soit dans le futur
                 * par rapport à la date du dernier paiement
                 */
                else {
                    SimpleDateFormat reader = new SimpleDateFormat("MM.yyyy");
                    RERenteAccJoinTblTiersJoinDemRenteManager manager = new RERenteAccJoinTblTiersJoinDemRenteManager();
                    manager.setSession(session);
                    manager.setForNoDemandeRente(idDemandeRente);
                    manager.find(BManager.SIZE_NOLIMIT);

                    if (manager.getContainer().size() == 0) {
                        // TODO à valider
                        return false;
                    } else {
                        for (RERenteAccJoinTblTiersJoinDemandeRente rente : manager.getContainerAsList()) {
                            String dateDebutRenteAccordee = rente.getDateDebutDroit();
                            if (!JadeStringUtil.isBlankOrZero(dateDebutRenteAccordee)) {
                                Date dateDebutTmp = reader.parse(dateDebutRenteAccordee);
                                int dateDebutInteger = Integer.valueOf(formater.format(dateDebutTmp));
                                // La date de début de la RA doit être plus grande que la date du dernier pmt mensuel
                                if (dateDernierPaiementInteger >= dateDebutInteger) {
                                    return false;
                                }

                            } else {
                                // TODO à valider (cas anormal si pas de date de début...)
                                return false;
                            }

                        }
                        return true;
                    }
                }

            }
            // Si la demande est en courant validé, on affiche l'option
            else if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(demandeRente.getCsEtat())) {
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Permet de savoir si la préparation de la décision peut s'effectuer
     * 
     * @param session
     * @param idDemandeRente
     * @return
     */
    @Deprecated
    public static boolean isPreparationDecisionPossible(BSession session, String idDemandeRente) {
        try {
            // Recherche des dates
            // date de traitement, date du jour, date du dernier paiement, date début du droit
            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession(session);
            demandeRente.setIdDemandeRente(idDemandeRente);
            demandeRente.retrieve();

            if (demandeRente.isNew()) {
                return false;
            }

            String dateDernierPaiement = REPmtMensuel.getDateDernierPmt(session);
            JADate aujourdhui = JACalendar.today();
            aujourdhui.setDay(1);
            String dateDuJour = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(aujourdhui.toStrAMJ());

            if (!JadeDateUtil.isGlobazDate(dateDuJour) || !JadeDateUtil.isGlobazDate(dateDernierPaiement)) {
                return false;
            }

            // Si(
            // date de traitement = date du jour
            // et date de traitement = date du dernier paiement
            // )ou(
            // date de traitement < date du jour
            // et date de traitement < date du dernier paiement
            // et date début du droit > date du dernier paiement
            // )
            // la préparation de la décision peut s'effectuer
            return (JadeDateUtil.areDatesEquals(demandeRente.getDateTraitement(), dateDuJour) && JadeDateUtil
                    .areDatesEquals(demandeRente.getDateTraitement(), dateDernierPaiement))
                    || (JadeDateUtil.isDateBefore(demandeRente.getDateTraitement(), dateDuJour)
                            && JadeDateUtil.isDateBefore(demandeRente.getDateTraitement(), dateDernierPaiement) && JadeDateUtil
                                .isDateAfter(demandeRente.getDateDebut(), dateDernierPaiement));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Méthode qui permet de dévalider toutes les autres décisions de la demande de rente. Est utilisé lors de la
     * dévalidation d'une décision.
     * 
     * @param REDecisionEntity
     * @param BSession
     * @param BITransaction
     * @throws Exception
     */
    public List<String> devalideAutresDecisionsDemande(REDecisionEntity decision, BSession session,
            BITransaction transaction) throws Exception {

        List<String> idsTiersBPAutresDecisions = new ArrayList<String>();

        // Trouver toutes les autres décisions de la demande de rente
        REDecisionsManager mgr = new REDecisionsManager();
        mgr.setForIdDemandeRente(decision.getIdDemandeRente());
        mgr.setSession(session);
        mgr.find(transaction);

        for (Iterator<REDecisionEntity> iterator = mgr.iterator(); iterator.hasNext();) {
            REDecisionEntity deci = iterator.next();
            // si c'est la décision en paramètre, rien faire... (déjà fait)
            // Si la décision n'est pas du même type, ne rien faire. Une décision de type Retro ne peut dévalider une
            // décision de type courant !!!
            if (deci.getIdDecision().equals(decision.getIdDecision())
                    || !deci.getCsTypeDecision().equals(decision.getCsTypeDecision())) {
                // rien faire
            } else {
                // Pour chaque décision
                // 1) Rechercher s'il y a une prestation dans un lot pour cette décision,
                // si c'est le cas, sortir la prestation du lot
                REPrestationsManager mgrPrest = new REPrestationsManager();
                mgrPrest.setForIdDecision(deci.getIdDecision());
                mgrPrest.setSession(session);
                mgrPrest.find(transaction);

                for (Iterator<REPrestations> iterator2 = mgrPrest.iterator(); iterator2.hasNext();) {
                    REPrestations prest = iterator2.next();

                    if (!JadeStringUtil.isBlankOrZero(prest.getIdPrestation())) {
                        prest.setIdLot("");
                        prest.setCsEtat(IREPrestations.CS_ETAT_PRE_ATTENTE);
                        prest.update(transaction);
                    }
                }

                // 2) Mettre la décision dans l'état pré-validé
                deci.setDateValidation("");
                deci.setValidePar("");
                deci.setCsEtat(IREDecision.CS_ETAT_PREVALIDE);
                deci.update(transaction);

                idsTiersBPAutresDecisions.add(deci.getIdTiersBeneficiairePrincipal());

                // 3) Ttt de dévalidation d'une décision
                REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(transaction, session, deci);
            }
        }
        return idsTiersBPAutresDecisions;
    }

    /**
     * Compensation des Dettes de type Rentes en cours, inter décisions Entre les membres de la famille
     * 
     * @param session
     * @param transaction
     * @param idsPrestation
     *            id de toutes les prestations de la demande.
     * @throws Exception
     */
    public void doCompensationRECInterDecision(BSession session, BITransaction transaction, List<String> idsPrestations)
            throws Exception {
        // Initialisation du tableau
        Set<REOVInterDecisionVO> setOVInterDecision = new TreeSet<REOVInterDecisionVO>(
                new RECompensationInterDecisionComparator());

        Map<String, FWCurrency> mapSoldeDispoParIdPrestation = new HashMap<String, FWCurrency>();

        REDecisionEntity dec = new REDecisionEntity();

        // Pour chacune des prestations
        for (String idPrst : idsPrestations) {

            REPrestations prst = new REPrestations();
            prst.setSession(session);
            prst.setIdPrestation(idPrst);
            prst.retrieve(transaction);
            PRAssert.notIsNew(prst, null);

            // On récupère la décision
            dec = new REDecisionEntity();
            dec.setSession(session);
            dec.setIdDecision(prst.getIdDecision());
            dec.retrieve(transaction);
            PRAssert.notIsNew(dec, null);

            REOrdresVersements[] ovsDeLaPrestation = prst.getOrdresVersement((BTransaction) transaction);

            RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager mgr = new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager();
            mgr.setSession(session);
            mgr.setForIdDecision(dec.getIdDecision());
            mgr.setForIdTiersBeneficiaire(dec.getIdTiersBeneficiairePrincipal());
            mgr.find(transaction);

            // On récupère tous les id des rentes accordées de la décisions
            Set<Long> idsRADeLaDecision = new HashSet<Long>();
            for (Iterator<RERenteAccordeeJoinInfoComptaJoinPrstDues> iterator2 = mgr.iterator(); iterator2.hasNext();) {
                RERenteAccordeeJoinInfoComptaJoinPrstDues object = iterator2.next();
                idsRADeLaDecision.add(Long.parseLong(object.getIdPrestationAccordee()));
            }

            boolean dettesFound = false;

            // Recherche du montant alloué au bénéficiaire principal...
            // Il ne peut y avoir qu'un OV de type BENEFICIAIRE_PRINCIPAL
            FWCurrency montantBP = new FWCurrency("0");
            for (REOrdresVersements unOrdreDeVersement : ovsDeLaPrestation) {
                if (IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL.equals(unOrdreDeVersement.getCsType())) {
                    montantBP.add(unOrdreDeVersement.getMontant());
                    break;
                }
            }
            // TODO ajouter un test pour garantir qu0il n'y en ait pas 2 ???
            FWCurrency soldeDispoPourPrestation = new FWCurrency(montantBP.toString());

            // On parcours tous les OV de la prestation qu'on est en train de traiter

            for (REOrdresVersements unOrdreDeVersement : ovsDeLaPrestation) {
                if (IREOrdresVersements.CS_TYPE_DETTE.equals(unOrdreDeVersement.getCsType())
                        && (unOrdreDeVersement.getIsCompense() != null)
                        && unOrdreDeVersement.getIsCompense().booleanValue()) {

                    dettesFound = true;

                    REOVInterDecisionVO ovid = new REOVInterDecisionVO();
                    ovid.setIdPrestation(idPrst);
                    // ovid.setIdTiers(unOrdreDeVersement.getIdTiers());
                    ovid.setIdTiers(dec.getIdTiersBeneficiairePrincipal());
                    ovid.setMontantRetro(new FWCurrency(unOrdreDeVersement.getMontant()));
                    ovid.setIdOV(unOrdreDeVersement.getIdOrdreVersement());

                    FWCurrency solde = new FWCurrency(montantBP.toString());
                    solde.sub(new FWCurrency(unOrdreDeVersement.getMontantDette()));
                    ovid.setSolde(solde);
                    ovid.setPriority(String.valueOf(REBeneficiairePrincipal.getGroupLevel(session, transaction,
                            idsRADeLaDecision)));
                    setOVInterDecision.add(ovid);
                    // Si c'est une dette on soustrait le montant de la dette
                    // ET on créé un OV interDecision
                    soldeDispoPourPrestation.sub(unOrdreDeVersement.getMontantDette());
                } else if ((IREOrdresVersements.CS_TYPE_DETTE_RENTE_AVANCES.equals(unOrdreDeVersement.getCsType())
                        && (unOrdreDeVersement.getIsCompense() != null) && unOrdreDeVersement.getIsCompense()
                        .booleanValue())
                        || (IREOrdresVersements.CS_TYPE_DETTE_RENTE_DECISION.equals(unOrdreDeVersement.getCsType())
                                && (unOrdreDeVersement.getIsCompense() != null) && unOrdreDeVersement.getIsCompense()
                                .booleanValue())
                        || (IREOrdresVersements.CS_TYPE_DETTE_RENTE_PRST_BLOQUE.equals(unOrdreDeVersement.getCsType())
                                && (unOrdreDeVersement.getIsCompense() != null) && unOrdreDeVersement.getIsCompense()
                                .booleanValue())
                        || (IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION.equals(unOrdreDeVersement.getCsType())
                                && (unOrdreDeVersement.getIsCompense() != null) && unOrdreDeVersement.getIsCompense()
                                .booleanValue())
                        || (IREOrdresVersements.CS_TYPE_DETTE_RENTE_RETOUR.equals(unOrdreDeVersement.getCsType())
                                && (unOrdreDeVersement.getIsCompense() != null) && unOrdreDeVersement.getIsCompense()
                                .booleanValue())) {

                    // Si c'est une dette on soustrait le montant de la dette
                    soldeDispoPourPrestation.sub(unOrdreDeVersement.getMontantDette());

                }
            }

            // Maintenant on connaît le solde réel pour la prestation courante. on l'id de la prestation avec son
            // montant
            // disponible
            mapSoldeDispoParIdPrestation.put(idPrst, soldeDispoPourPrestation);

            // Si aucune dette n'a été trouvée pour la prestation en course, on créer un OVInterDecision
            if (!dettesFound) {
                REOVInterDecisionVO ovid = new REOVInterDecisionVO();
                ovid.setIdPrestation(idPrst);
                ovid.setIdTiers(dec.getIdTiersBeneficiairePrincipal());
                ovid.setMontantRetro(new FWCurrency(prst.getMontantPrestation()));

                ovid.setSolde(new FWCurrency(montantBP.toString()));
                ovid.setPriority(String.valueOf(REBeneficiairePrincipal.getGroupLevel(session, transaction,
                        idsRADeLaDecision)));
                setOVInterDecision.add(ovid);
            }
        }

        /*
         * 
         * Parcours du tableau des OVInterDecision et ajout des compensations inter décision
         */
        REOVInterDecisionVO[] ovsInterDecision = setOVInterDecision.toArray(new REOVInterDecisionVO[setOVInterDecision
                .size()]);
        for (int i = 0; i < ovsInterDecision.length; i++) {
            REOVInterDecisionVO ovidCourant = ovsInterDecision[i];

            // On contrôle s'il y a qqch à compenser !!!
            if (ovidCourant.getSolde().isNegative()) {
                // On compense tous ce que l'on peut sur les autres membres de la famille, si c'est possible !!!

                // Solde du montant a compenser
                FWCurrency soldeMontantACompenserN = new FWCurrency(ovidCourant.getSolde().toString());
                FWCurrency soldeMontantACompenserABS = new FWCurrency(soldeMontantACompenserN.toString());
                soldeMontantACompenserABS.abs();

                // On reparcours le tableau des OV inter décision
                for (int j = 0; j < ovsInterDecision.length; j++) {
                    REOVInterDecisionVO currentElem = ovsInterDecision[j];

                    // Pas d'argent pour compenser ? --> on passe donc a l'élément source suivant.
                    if (soldeMontantACompenserN.isZero() || soldeMontantACompenserN.isPositive()) {
                        break;
                    }

                    // On skip le cas en cours de traitement !!!
                    if (i == j) {
                        continue;
                    }

                    // SOlde positif -> on peut compenser
                    if (currentElem.getSolde().isPositive()) {
                        FWCurrency soldeCourantP = new FWCurrency(currentElem.getSolde().toString());

                        // Calcul du montant a compenser....
                        FWCurrency montantCompensableP = null;
                        if (soldeMontantACompenserABS.compareTo(soldeCourantP) == 1) {
                            montantCompensableP = new FWCurrency(soldeCourantP.toString());
                        } else {
                            montantCompensableP = new FWCurrency(soldeMontantACompenserABS.toString());
                        }

                        // calculer la somme des montant créancier

                        // double sommeMontantCreancier = 0;
                        //
                        // // récupérer tout les créancier
                        // RECreancierManager creMgr = new RECreancierManager();
                        // creMgr.setSession(session);
                        // creMgr.setForIdDemandeRente(dec.getIdDemandeRente().toString());
                        // creMgr.find(transaction);
                        //
                        // for (RECreancier cre : creMgr.getContainerAsList()) {
                        //
                        // // sauf les types impôts à la source
                        // if (!cre.getCsType().equals(IRECreancier.CS_IMPOT_SOURCE)) {
                        //
                        // // Si le montant revendiqué est à zéro, ne pas mettre dans les copies
                        // if (!(new FWCurrency(cre.getMontantRevandique()).isZero())) {
                        // sommeMontantCreancier = sommeMontantCreancier
                        // + Double.parseDouble(cre.getMontantRevandique());
                        // }
                        // }
                        // }

                        // YMA nouveau test début
                        double sommeMontantCreancier = 0;
                        BigDecimal montantReparti = new BigDecimal("0.00");
                        ;

                        // récupérer tout les créancier
                        RECreancierManager creMgr = new RECreancierManager();
                        creMgr.setSession(session);
                        creMgr.setForIdDemandeRente(dec.getIdDemandeRente().toString());
                        creMgr.find(transaction);

                        for (RECreancier cre : creMgr.getContainerAsList()) {

                            // sauf les types impôts à la source
                            if (!cre.getCsType().equals(IRECreancier.CS_IMPOT_SOURCE)) {

                                // récupérer montant reparti
                                RECreanceAccordeeManager caManager = new RECreanceAccordeeManager();
                                caManager.setSession(session);
                                caManager.setForIdCreancier(cre.getId());
                                try {
                                    montantReparti = caManager.getSum(RECreanceAccordee.FIELDNAME_MONTANT);
                                } catch (Exception e) {
                                    montantReparti = new BigDecimal("0.00");
                                }

                                sommeMontantCreancier = sommeMontantCreancier + montantReparti.doubleValue();

                            }
                        }

                        // YMA fin test

                        // definir le montant a compenser
                        if ((soldeCourantP.doubleValue() - sommeMontantCreancier) >= montantCompensableP.doubleValue()) {
                            montantCompensableP = montantCompensableP;
                        } else if ((soldeCourantP.doubleValue() - sommeMontantCreancier) >= 0) {
                            montantCompensableP = new FWCurrency(soldeCourantP.doubleValue() - sommeMontantCreancier);
                        } else {
                            montantCompensableP = new FWCurrency(0);
                        }

                        soldeMontantACompenserN.add(montantCompensableP);
                        soldeMontantACompenserABS = new FWCurrency(soldeMontantACompenserN.toString());
                        soldeMontantACompenserABS.abs();

                        // Mis à jours des éléments :

                        // MAJ de l'élément source
                        // On ne remet pas à jours le montant compensé, car plus utilisé à partir d'ici...
                        ovidCourant.setSolde(new FWCurrency(soldeMontantACompenserN.toString()));
                        ovsInterDecision[i] = ovidCourant;

                        // MAJ de l'élément current
                        String idOV = ovidCourant.getIdOV();
                        String idTiersCompensationInterDecision = currentElem.getIdTiers();

                        soldeCourantP.sub(montantCompensableP);
                        currentElem.setSolde(soldeCourantP);
                        ovsInterDecision[j] = currentElem;

                        // La CID ne peut pas se faire si les 2 décisions sont toutes les 2 en négatif

                        // Test inutile car ne devrait jamais arriver !! mais dans le doute !!!
                        if ((idOV != null) && !idOV.equals(currentElem.getIdOV())) {

                            REOrdresVersements ov1 = new REOrdresVersements();
                            ov1.setSession(session);
                            ov1.setIdOrdreVersement(currentElem.getIdOV());
                            ov1.retrieve(transaction);
                            if (mapSoldeDispoParIdPrestation.containsKey(ov1.getIdPrestation())) {

                                // Le solde de la 1ère décision est négatif
                                if (mapSoldeDispoParIdPrestation.get(ov1.getIdPrestation()).isNegative()) {

                                    ov1.setIdOrdreVersement(idOV);
                                    ov1.retrieve(transaction);
                                    if (mapSoldeDispoParIdPrestation.containsKey(ov1.getIdPrestation())) {
                                        // Les 2 sont négatif, pas de CID !!!!
                                        if (mapSoldeDispoParIdPrestation.get(ov1.getIdPrestation()).isNegative()) {

                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                        currentElem.addDettesExtACompenser(ovidCourant.getIdTiers(), idOV,
                                montantCompensableP.toString(), idTiersCompensationInterDecision);

                    }
                }
            }

        }

        try {
            // MAJ en DB des compensations inter décisions !!!!
            for (int k = 0; k < ovsInterDecision.length; k++) {
                REOVInterDecisionVO ov = ovsInterDecision[k];
                // On rajoute tous les OV de type DETTE, ajouté précédemment à la prestation

                List<REOVInterDecisionVO.ExtRenteEnCoursACompenser> dettesACompenser = ov.getDettesCompenseeExt();
                if (dettesACompenser != null) {
                    for (Iterator<REOVInterDecisionVO.ExtRenteEnCoursACompenser> iterator = dettesACompenser.iterator(); iterator
                            .hasNext();) {
                        REOVInterDecisionVO.ExtRenteEnCoursACompenser elm = iterator.next();

                        // Comme l'on créé des dettes pour tous les OV, même si tout est compensé,
                        // On fait ce test pour ne pas se créer un Dette inter décision sur soi même
                        // if (!ov.getIdTiers().equals(elm.getIdTiers())) {
                        REOrdresVersements o = new REOrdresVersements();
                        o.setSession(session);
                        o.setIdPrestation(ov.getIdPrestation());
                        o.setCsType(IREOrdresVersements.CS_TYPE_DETTE);
                        // Toujours mettre a null cette valeur pour ce genre de Dette (compensation inter décision).
                        // Autrement, générerait des erreurs dans les écritures de restitutions.
                        o.setIdRenteAccordeeDiminueeParOV(null);
                        o.setIdRenteAccordeeACompenserParOV(null);

                        if (elm.getMontantCompense().isZero()) {
                            o.setIsCompense(Boolean.FALSE);
                        } else {
                            o.setIsCompense(Boolean.TRUE);

                            o.setMontant(elm.getMontantCompense().toString());
                            o.setMontantDette(elm.getMontantCompense().toString());
                            o.setIdSection(null);

                            o.setIdTiers(elm.getIdTiers());
                            o.setIdTiersAdressePmt(null);
                            o.setIdDomaineApplication(null);
                            o.setIsCompensationInterDecision(Boolean.TRUE);
                            o.setIsValide(Boolean.TRUE);
                            o.add(transaction);

                            // Ajout des compensations interDecision

                            RECompensationInterDecisions cid = new RECompensationInterDecisions();
                            cid.setSession(session);
                            cid.setIdOrdreVersement(elm.getIdOV());
                            cid.setIdOVCompensation(o.getIdOrdreVersement());
                            cid.setIdTiers(elm.getIdTiersCompensationInterDecision());
                            cid.setMontant(elm.getMontantCompense().toString());
                            cid.add(transaction);
                            // }
                        }
                    }
                } else {
                    ;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doMAJDecisionAttente(BITransaction transaction, REDecisionEntity decision,
            REPreValiderDecisionViewBean vb) throws Exception {
        // pas de prestation, maj de l'état et de la remarque
        decision.setIsAvecBonneFoi(vb.getIsAvecBonneFoi());
        decision.setIsSansBonneFoi(vb.getIsSansBonneFoi());
        decision.setIsObliPayerCoti(vb.getIsObligPayerCoti());
        decision.setIsRemSuppVeuf(vb.getIsRemSuppVeuf());
        decision.setIsRemRedPlaf(vb.getIsRemRedPlaf());
        decision.setIsRemAnnDeci(vb.getIsRemAnnDeci());
        decision.setCsGenreDecision(vb.getCsGenreDecision());
        decision.setRemarqueDecision(vb.getRemarqueDecision());
        decision.setTraitePar(vb.getTraiterParDecision());
        decision.setCsEtat(IREDecision.CS_ETAT_ATTENTE);
        decision.setValidePar("");
        decision.setDateValidation("");
        decision.setIdTiersAdrCourrier(vb.getIdTierAdresseCourrier());
        // Inforom 500
        decision.setIsRemInteretMoratoires(vb.getIsInteretMoratoire());
        decision.update(transaction);
    }

    /**
     * Maj du solde à restituer, le cas echéant
     * 
     * @param session
     * @param transaction
     * @param idsPrestations
     * @throws Exception
     */
    public void doMAJSoldePourRestitution(BSession session, BITransaction transaction, List<String> idsPrestations)
            throws Exception {

        for (String idPrst : idsPrestations) {
            REPrestations prst = new REPrestations();
            prst.setSession(session);
            prst.setIdPrestation(idPrst);
            prst.retrieve(transaction);
            PRAssert.notIsNew(prst, null);

            try {
                BSessionUtil.initContext(session, this);

                DecisionService decisionService = CorvusServiceLocator.getDecisionService();
                decisionService.recalculerSoldePourRestitution(Long.parseLong(prst.getIdDecision()));
            } catch (Exception ex) {
                throw new RETechnicalException(ex);
            } finally {
                BSessionUtil.stopUsingContext(this);
            }
        }
    }

    public REPreValiderDecisionViewBean enregistrerModifs(REPreValiderDecisionViewBean vb, FWAction action,
            BSession session, BITransaction transaction) throws Exception {

        // Modifications possibles uniquement si la prestation de la décision se trouve dans un lot définitif

        // Retrieve de la décision
        REDecisionEntity decision = new REDecisionEntity();
        decision.setSession(session);
        decision.setIdDecision(vb.getIdDecision());
        decision.setId(vb.getIdDecision());
        decision.retrieve(transaction);

        if (!decision.isNew()) {
            if (decision.getCsEtat().equals(IREDecision.CS_ETAT_ATTENTE)) {

                // --> Mise à jour de la remarque de chaque "key"
                updateRemarqueKey(vb, session, transaction);

                // update de la demande (remarque uniquement)
                doMAJDecisionAttente(transaction, decision, vb);

            } else if (decision.getCsEtat().equals(IREDecision.CS_ETAT_PREVALIDE)) {

                // --> Mise à jour de la remarque de chaque "key"
                updateRemarqueKey(vb, session, transaction);

                // update de la demande (remarque + état en attente)
                doMAJDecisionAttente(transaction, decision, vb);

            } else if (decision.getCsEtat().equals(IREDecision.CS_ETAT_VALIDE)) {

                // Si la prestation de la décision n'est pas dans un lot définitif
                REPrestationsManager prestationMgr = new REPrestationsManager();
                prestationMgr.setSession(session);
                prestationMgr.setForIdDecision(decision.getIdDecision());
                prestationMgr.find(1);

                REPrestations prestation = new REPrestations();

                if (prestationMgr.isEmpty()) {

                    // pas de prestation, maj de l'état et de la remarque
                    doMAJDecisionAttente(transaction, decision, vb);

                    REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(transaction, session,
                            decision);

                    devalideAutresDecisionsDemande(decision, session, transaction);

                } else {
                    prestation = (REPrestations) prestationMgr.get(0);
                }

                if (prestation.isNew()) {

                    // // pas de prestation, maj de l'état et de la remarque
                    // REPreValiderDecisionHelper.doMAJDecisionAttente(transaction, decision, vb);

                    // sortir la prestation du lot
                    prestation.setIdLot("");
                    prestation.setCsEtat(IREPrestations.CS_ETAT_PRE_ATTENTE);
                    prestation.update(transaction);

                    // REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(transaction, session,
                    // decision);
                    //
                    // REPreValiderDecisionHelper.devalideAutresDecisionsDemande(decision, session, transaction);

                } else {
                    // On a une prestation
                    if (JadeStringUtil.isIntegerEmpty(prestation.getIdLot())) {

                        // pas de prestation dans un lot
                        doMAJDecisionAttente(transaction, decision, vb);

                        // maj de la prestation dans l'état en attente
                        prestation.setIdLot("");
                        prestation.setCsEtat(IREPrestations.CS_ETAT_PRE_ATTENTE);
                        prestation.update(transaction);

                        REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(transaction, session,
                                decision);

                        devalideAutresDecisionsDemande(decision, session, transaction);

                    } else {

                        // Retrieve du lot
                        RELot lot = new RELot();
                        lot.setSession(session);
                        lot.setIdLot(prestation.getIdLot());
                        lot.retrieve(transaction);

                        if (!lot.isNew()) {

                            // si le lot est définitif
                            if (lot.getCsEtatLot().equals(IRELot.CS_ETAT_LOT_VALIDE)) {

                                // nous sommes dans le mois comptable précédant ou plus petit
                                // que le mois de début de la rente

                                RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager mgr = new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager();
                                mgr.setSession(session);
                                mgr.setForIdDecision(decision.getIdDecision());
                                mgr.setUntilDateDebutDroit(REPmtMensuel.getDateDernierPmt(session));
                                mgr.find(transaction);

                                // si le mois de debut de la periode decision est plus grand que la date du dernier
                                // paiementon permet la devalidation
                                if (mgr.isEmpty()) {

                                    // bz-5091
                                    doMAJDecisionAttente(transaction, decision, vb);

                                    // sortir la prestation du lot
                                    prestation.setIdLot("");
                                    prestation.setCsEtat(IREPrestations.CS_ETAT_PRE_ATTENTE);
                                    prestation.update(transaction);

                                    REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(
                                            transaction, session, decision);

                                    List<String> idsTiersBPAutresDecisions = devalideAutresDecisionsDemande(decision,
                                            session, transaction);

                                    // On supprime la récap....
                                    // Plutôt que de supprimer, on reset la date de pmt, ce qui permet de garder
                                    // une trace, même si plus utilisé.
                                    RERecapInfoManager mgr2 = new RERecapInfoManager();
                                    mgr2.setSession(session);
                                    mgr2.setForIdTiers(decision.getIdTiersBeneficiairePrincipal());
                                    mgr2.setForRestoreTag(lot.getIdLot());
                                    mgr2.find(transaction);
                                    for (int i = 0; i < mgr2.size(); i++) {
                                        RERecapInfo ri = (RERecapInfo) mgr2.getEntity(i);
                                        ri.setDatePmt("");
                                        ri.update(transaction);
                                    }

                                    // bz-5091

                                    // On supprime la récap des autres décisions...
                                    for (Iterator<String> iterator = idsTiersBPAutresDecisions.iterator(); iterator
                                            .hasNext();) {
                                        String idTiersBP = iterator.next();

                                        if ((decision.getIdTiersBeneficiairePrincipal() != null)
                                                && !decision.getIdTiersBeneficiairePrincipal().equals(idTiersBP)) {

                                            // On supprime la récap des autres décisions...
                                            // Plutôt que de supprimer, on reset la date de pmt, ce qui permet de garder
                                            // une trace, même si plus utilisé.
                                            RERecapInfoManager mgr3 = new RERecapInfoManager();
                                            mgr3.setSession(session);
                                            mgr3.setForIdTiers(idTiersBP);
                                            mgr3.setForRestoreTag(lot.getIdLot());
                                            mgr3.find(transaction);
                                            for (int i = 0; i < mgr3.size(); i++) {
                                                RERecapInfo ri = (RERecapInfo) mgr3.getEntity(i);
                                                ri.setDatePmt("");
                                                ri.update(transaction);
                                            }
                                        }
                                    }

                                } else {
                                    // On peut pas modifier
                                    throw new Exception(session.getLabel("ERREUR_IMP_MOD_DEC_PREST_VALIDE_DATE"));
                                }

                            } else {

                                // On peut modifier (état + remarque)
                                doMAJDecisionAttente(transaction, decision, vb);

                                // sortir la prestation du lot
                                prestation.setIdLot("");
                                prestation.setCsEtat(IREPrestations.CS_ETAT_PRE_ATTENTE);
                                prestation.update(transaction);

                                REDeleteCascadeDemandeAPrestationsDues.annuleTraitementValidationDecision(transaction,
                                        session, decision);
                                devalideAutresDecisionsDemande(decision, session, transaction);
                            }
                        }
                    }
                }
            }
        }

        vb.setIdDecision(vb.getDecision().getIdDecision());

        // --> Mise à jour des annexes

        // 1. Effacer tout ce qu'il y a dans la base
        REAnnexeDecisionManager annexeMgr = new REAnnexeDecisionManager();
        annexeMgr.setSession(session);
        annexeMgr.setForIdDecision(vb.getIdDecision());
        annexeMgr.find(transaction);

        for (Iterator iterator2 = annexeMgr.iterator(); iterator2.hasNext();) {
            REAnnexeDecision annexeDb = (REAnnexeDecision) iterator2.next();

            annexeDb.delete(transaction);

        }

        // 2. Ajouter tout ce qui se trouve dans la liste
        List lstAnnexe = vb.getAnnexesList();

        for (Iterator iterator = lstAnnexe.iterator(); iterator.hasNext();) {
            REAnnexeDecisionViewBean annexe = (REAnnexeDecisionViewBean) iterator.next();

            annexe.setIdDecision(vb.getIdDecision());
            annexe.add(transaction);

        }

        // --> Mise à jour des copie

        // 1. Effacer tout ce qu'il y a dans la base
        RECopieDecisionManager copieMgr = new RECopieDecisionManager();
        copieMgr.setSession(session);
        copieMgr.setForIdDecision(vb.getIdDecision());
        copieMgr.find(transaction);

        for (Iterator iterator2 = copieMgr.iterator(); iterator2.hasNext();) {
            RECopieDecision copieDb = (RECopieDecision) iterator2.next();

            copieDb.delete(transaction);

        }

        // 2. Ajouter tout ce qui se trouve dans la liste
        List lstCopie = vb.getCopiesList();

        for (Iterator iterator = lstCopie.iterator(); iterator.hasNext();) {
            RECopieDecisionViewBean copie = (RECopieDecisionViewBean) iterator.next();
            copie.setSession(session);
            copie.setIdDecision(vb.getIdDecision());
            copie.add(transaction);

        }

        vb.setLstAnnexe(lstAnnexe);
        vb.setLstCopie(lstCopie);

        return vb;

    }

    /**
     * @param session
     * @param transaction
     * @param idTiers
     * @return Le montant mensuel de la RA du tiers passé en paramètre non encore validée. C'est à dire, la rente
     *         accordée dans l'état calculé ou partiel. Si plusieurs RA, on retournera en priorité celle n'étant pas de
     *         type API
     * @throws Exception
     */
    public FWCurrency getMontantRA(BSession session, BITransaction transaction, String idTiers) throws Exception {

        FWCurrency mnt = new FWCurrency(0);

        RERenteAccordeeManager mgr = new RERenteAccordeeManager();
        mgr.setSession(session);
        mgr.setForIdTiersBeneficiaire(idTiers);
        mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_CALCULE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
        mgr.find(transaction);

        RERenteAccordee selectedRA = null;
        for (int i = 0; i < mgr.size(); i++) {
            RERenteAccordee ra = (RERenteAccordee) mgr.get(i);

            if ((REPrestationsAccordees.GROUPE_API_AI == ra.getGroupeGenreRente())
                    || (REPrestationsAccordees.GROUPE_API_AVS == ra.getGroupeGenreRente())) {

                selectedRA = ra;
            } else {
                selectedRA = ra;
                break;
            }

        }
        if ((selectedRA != null) && !JadeStringUtil.isBlankOrZero(selectedRA.getMontantPrestation())) {
            mnt.add(selectedRA.getMontantPrestation());
        }
        return mnt;
    }

    public FWCurrency min(FWCurrency mnt1, FWCurrency mnt2) {

        FWCurrency m1 = new FWCurrency(0);
        FWCurrency m2 = new FWCurrency(0);

        if (mnt1 != null) {
            m1.add(mnt1);
        }
        if (mnt2 != null) {
            m2.add(mnt2);
        }

        if (m1.compareTo(m2) >= 1) {
            return m2;
        } else {
            return m1;
        }

    }

    public void updateRemarqueKey(REPreValiderDecisionViewBean vb, BSession session, BITransaction transaction)
            throws Exception {

        Map mapKey = vb.getMapKey();

        for (Iterator iterator = mapKey.keySet().iterator(); iterator.hasNext();) {

            String key = (String) iterator.next();

            KeyPeriodeInfo keyPeriodeInfo = (KeyPeriodeInfo) mapKey.get(key);

            RERemarqueGroupePeriodeManager remarqueMgr = new RERemarqueGroupePeriodeManager();
            remarqueMgr.setSession(session);
            remarqueMgr.setForIdDecision(vb.getIdDecision());
            remarqueMgr.setForDatedu(keyPeriodeInfo.dateDebut);
            remarqueMgr.setForDateAu(keyPeriodeInfo.dateFin);
            remarqueMgr.find(transaction);

            if (remarqueMgr.isEmpty()) {

                RERemarqueGroupePeriode remarque = new RERemarqueGroupePeriode();
                remarque.setSession(session);
                remarque.setDateAu(keyPeriodeInfo.dateFin);
                remarque.setDateDepuis(keyPeriodeInfo.dateDebut);
                remarque.setRemarque(keyPeriodeInfo.remarque);
                remarque.setIdDecision(vb.getIdDecision());
                remarque.add(transaction);

            }

            for (Iterator iterator2 = remarqueMgr.iterator(); iterator2.hasNext();) {
                RERemarqueGroupePeriode remarque = (RERemarqueGroupePeriode) iterator2.next();
                if (!remarque.isNew()) {
                    remarque.setRemarque(keyPeriodeInfo.remarque);
                    remarque.update(transaction);
                }
            }
        }

        // Rafraichir les copies

    }
}
