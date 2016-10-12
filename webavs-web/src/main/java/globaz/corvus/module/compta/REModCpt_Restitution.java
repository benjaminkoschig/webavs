package globaz.corvus.module.compta;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.recap.IRERecap;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.ordresversements.RECompensationInterDecisions;
import globaz.corvus.db.ordresversements.RECompensationInterDecisionsManager;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.RESoldePourRestitution;
import globaz.corvus.db.ordresversements.RESoldePourRestitutionManager;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.process.RETraiterLotDecisionsProcess;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.List;

/**
 * Module comptable de gestion des écritures comptable Traite les cas de Restitutions pour Dettes en cours. Gère
 * également les compensations entre secteurs.
 */
public class REModCpt_Restitution extends AREModuleComptable implements IREModuleComptable {

    public REModCpt_Restitution(boolean isGenererEcritureComptable) throws Exception {
        super(isGenererEcritureComptable);
    }

    @Override
    public FWMemoryLog doTraitement(RETraiterLotDecisionsProcess process, APIGestionComptabiliteExterne compta,
            BSession session, BTransaction transaction, REDecisionEntity decision, String dateComptable, String idLot,
            String dateEcheance) throws Exception {
        return doTraitement(process, compta, session, transaction, decision, dateComptable, idLot, dateEcheance, null);
    }

    /**
     * Traitement des écritures comptables rétroactives.
     * 
     * Si l'objet compta == null, seul les écritures pour la récap seront effectuées
     */

    @Override
    public FWMemoryLog doTraitement(RETraiterLotDecisionsProcess process, APIGestionComptabiliteExterne compta,
            BSession session, BTransaction transaction, REDecisionEntity decision, String dateComptable, String idLot,
            String dateEcheance, String idOrganeExecution) throws Exception {

        FWMemoryLog memoryLog = new FWMemoryLog();

        // creation de l'idExterneRole (qui est tout simplement le numéro numéro
        // AVS de l'assuré
        String idExterneRole = null;

        String idTiersBeneficiairePrincipal = getIdTiersBeneficiairePrincipal(decision, transaction);

        REPrestations prst = decision.getPrestation(transaction);
        idExterneRole = PRTiersHelper.getTiersParId(session, idTiersBeneficiairePrincipal).getProperty(
                PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

        memoryLog.logMessage(session.getLabel("TRAITEMENT_COMPTABLE_RESTITUTIONS"), FWMessage.INFORMATION, this
                .getClass().getName());

        memoryLog.logMessage("Bénéficiaire principal " + idExterneRole, FWMessage.INFORMATION, this.getClass()
                .getName());

        APICompteAnnexe compteAnnexe = null;
        if (compta != null) {
            // récupération du compte annexe RENTIER
            compteAnnexe = compta.getCompteAnnexeByRole(idTiersBeneficiairePrincipal, IntRole.ROLE_RENTIER,
                    idExterneRole);

            if (compteAnnexe == null) {
                throw new Exception(session.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"));
            }
        }

        /*
         * 
         * Initialisation de la section
         */
        APISection sectionNormale = null;
        APISection sectionRestitution = null;
        RERenteAccordee ra = getRenteAccordee(session, transaction, decision);

        // Récupération de la rubrique rétroactive
        APIRubrique rubriqueCompensation = REModuleComptableFactory.getInstance().COMPENSATION;
        APIRubrique prestationARestituer = getRubriqueRestitution(ra);

        // Calcul du montant courant et rétroactif.
        FWCurrency montantRetroactif = getMontantRetro(session, transaction, decision);

        FWCurrency montantIM = new FWCurrency(0);

        // Recherche du montant des dettes
        FWCurrency montantDettes = new FWCurrency(0);
        FWCurrency montantDettesPourComparaison = new FWCurrency(0);
        // Cumul des montants des compensations inter-décisions.
        FWCurrency montantCID = new FWCurrency(0);

        REOrdresVersements[] ovs = prst.getOrdresVersement(transaction);

        List<String> idsRAACompenser = new ArrayList<String>();
        boolean isCID = false;
        // En cas de CID, toujours appliquer la méthode des montants BRUT.
        boolean isMethodeMontantBrut = false;

        for (REOrdresVersements ov : ovs) {

            switch (ov.getCsTypeOrdreVersement()) {
                case INTERET_MORATOIRE:
                    montantIM.add(ov.getMontant());
                    break;
                case DETTE:
                    if ((ov.getIsCompense() != null) && ov.getIsCompense()) {

                        // Peut arriver dans le cas des CID
                        if (!JadeStringUtil.isBlankOrZero(ov.getIdRenteAccordeeACompenserParOV())) {
                            idsRAACompenser.add(ov.getIdRenteAccordeeACompenserParOV());
                        }

                        montantDettes.add(ov.getMontant());
                        montantDettesPourComparaison.add(ov.getMontant());

                        if ((ov.getIsCompensationInterDecision() == null)
                                || !ov.getIsCompensationInterDecision().booleanValue()) {
                            RECompensationInterDecisionsManager mgr = new RECompensationInterDecisionsManager();
                            mgr.setSession(session);
                            mgr.setForIdOV(ov.getIdOrdreVersement());
                            mgr.find(transaction, BManager.SIZE_NOLIMIT);

                            for (int j = 0; j < mgr.size(); j++) {
                                RECompensationInterDecisions cid = (RECompensationInterDecisions) mgr.getEntity(j);
                                montantDettesPourComparaison.sub(cid.getMontant());
                                isMethodeMontantBrut = true;
                            }
                        } else if ((ov.getIsCompensationInterDecision() == null)
                                || ov.getIsCompensationInterDecision().booleanValue()) {
                            isCID = true;
                            isMethodeMontantBrut = true;
                            montantCID.add(ov.getMontant());
                        }
                    }
                    break;
                case DIMINUTION_DE_RENTE:

                    // Génération des écritures récap pour les diminutions de rentes
                    if ((ov.getIsCompense() != null) && ov.getIsCompense()) {
                        RERenteAccordee elm = new RERenteAccordee();
                        elm.setSession(session);
                        elm.setIdPrestationAccordee(ov.getIdRenteAccordeeDiminueeParOV());
                        elm.retrieve(transaction);
                        if (!IREPrestationAccordee.CS_ETAT_DIMINUE.equals(elm.getCsEtat())) {
                            throw new Exception(session.getLabel("ERREUR_RENTE_ACC_NO") + elm.getIdPrestationAccordee()
                                    + session.getLabel("DOIT_ETAT_DIMINUE"));
                        }

                        // Génération de l'écriture de récap

                        PRAssert.notIsNew(elm, null);

                        // Ecriture pour la récap
                        int codeRecap = AREModuleComptable.getCodeRecap(elm.getCodePrestation(),
                                IRERecap.GENRE_RECAP_DIMINUTION);

                        JACalendar cal = new JACalendarGregorian();
                        // dateRecap = date du dernier paiement + 1 mois.
                        String dateRecap = REPmtMensuel.getDateDernierPmt(session);
                        JADate dmr = new JADate(dateRecap);
                        dmr = cal.addMonths(dmr, 1);

                        RERenteAccordee ra2 = new RERenteAccordee();
                        ra2.setSession(session);
                        ra2.setIdPrestationAccordee(ov.getIdRenteAccordeeDiminueeParOV());
                        ra2.retrieve(transaction);
                        JADate dfRAaDiminuer = new JADate(ra2.getDateFinDroit());

                        // Cas des décisions prise en avances de 2 mois. Le mois de
                        // rapport de l'annonce de diminution
                        // doit être 1 mois plus grand que le mois de la diminution.
                        if ((cal.compare(dfRAaDiminuer, dmr) == JACalendar.COMPARE_FIRSTUPPER)
                                || (cal.compare(dfRAaDiminuer, dmr) == JACalendar.COMPARE_EQUALS)) {

                            dmr = cal.addMonths(dfRAaDiminuer, 1);
                        }

                        dateRecap = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dmr.toStrAMJ());

                        memoryLog.logMessage(this.doEcritureRecap(session, transaction, codeRecap,
                                new FWCurrency(elm.getMontantPrestation()), idTiersBeneficiairePrincipal, dateRecap,
                                idLot));
                    }
                    break;
                default:
                    break;
            }
        }

        // Si compta == null, aucune écriture comptable possible.
        // Seul les écritures RECAP sont créées.
        if (compta == null) {
            return memoryLog;
        }

        /*
         * 
         * Il y a des dettes à traiter Selon DR 11216 et ss
         */
        if ((idsRAACompenser.size() > 0) || isCID) {

            int grpGenreRente = ra.getGroupeGenreRente();

            // On contrôle s'il faut appliquer la technique des montants brut ou
            // des montants nets.
            // Si au moins 1 des ra à compenser n'est pas dans le même groupe
            // que celles des RA de la décision
            // il faut appliquer la méthode des montants brut.
            boolean isMethodeMontantNet = true;
            for (String idRA : idsRAACompenser) {

                RERenteAccordee raACompenser = new RERenteAccordee();
                raACompenser.setSession(session);
                raACompenser.setIdPrestationAccordee(idRA);
                raACompenser.retrieve(transaction);
                PRAssert.notIsNew(raACompenser, null);

                int grpGenreRenteDiminue = raACompenser.getGroupeGenreRente();
                if (grpGenreRenteDiminue != grpGenreRente) {
                    // -> technique des montants brut
                    isMethodeMontantNet = false;

                    prestationARestituer = getRubriqueRestitution(raACompenser);

                    break;
                }
            }

            // Traitement selon règle des montants BRUT s'il y a des
            // compensations inter-decision.
            if (isMethodeMontantBrut) {
                isMethodeMontantNet = false;
            }

            // Identification du cas à traiter
            // 1) Dette>(Retro + courant)
            // 2) Dette<=(Retro + courant)
            // 3) Dette<=Retro

            // Méthode du montant net : 11216 DR
            // Dette < RETRO : 11217 DR
            // Dette > RETRO : 11218 DR
            // Dette = RETRO : 11219 DR (aucune écriture retroactive ni
            // restitution)

            FWCurrency montantCourant = new FWCurrency(getMontantCourant(session, transaction, decision).toString());
            FWCurrency retroPlusCourant = new FWCurrency(montantCourant.toString());
            retroPlusCourant.add(montantRetroactif);

            int cas = 0;

            if (montantDettes.compareTo(montantRetroactif) <= 0) {
                cas = 3;
            } else if (montantDettes.compareTo(retroPlusCourant) <= 0) {
                cas = 2;
            } else {
                cas = 1;
            }

            /*
             * 
             * Initialisation de la section
             */

            if (isGenererEcritureComptable) {
                sectionNormale = process.retrieveSection(transaction, decision.getIdDemandeRente(), idExterneRole,
                        compteAnnexe.getIdCompteAnnexe(), APISection.ID_CATEGORIE_SECTION_DECISION);
            }
            // Si aucune écriture comptable a générer, on instancie un objet de type section, vide pour éviter de créer
            // des nullPointer
            // dans la suite des traitements. Ainsi, aucune section n'est créée.
            else {
                sectionNormale = new CASection();
            }
            // if (this.isGenererEcritureComptable) {
            // sectionRestitution = process.retrieveSection(transaction, decision.getIdDemandeRente(), idExterneRole,
            // compteAnnexe.getIdCompteAnnexe(), APISection.ID_CATEGORIE_SECTION_RESTITUTIONS);
            // } else {
            // sectionRestitution = new CASection();
            // }

            FWCurrency montantARestituer = new FWCurrency(montantDettes.toString());
            montantARestituer.sub(montantRetroactif);

            // Si méthode des montants net avec des IM, rajouter le montant IM
            // dans les restitutions, car
            // le montant rétroactif les contient déjà.

            // BZ 9870
            // if (isMethodeMontantNet && !montantIM.isZero()) {
            // montantARestituer.add(montantIM);
            // }

            // Recherche du solde pour restitution...
            RESoldePourRestitutionManager mgr = new RESoldePourRestitutionManager();
            mgr.setSession(session);
            mgr.setForIdPrestation(prst.getIdPrestation());
            mgr.find(transaction);

            FWCurrency montantRestitPourComp = new FWCurrency(0);
            for (int i = 0; i < mgr.size(); i++) {
                RESoldePourRestitution spr = (RESoldePourRestitution) mgr.get(i);
                montantRestitPourComp.add(spr.getMontant());
            }

            // Le montant retroactif, ne contient en fait pas le montant
            // courant.
            // Ceci pour identifier les cas à traiter.
            montantRestitPourComp.add(montantRetroactif);
            montantRestitPourComp.add(montantCourant);
            montantRestitPourComp.add(montantIM);

            if (cas == 1) {
                if (montantRestitPourComp.compareTo(montantDettesPourComparaison) != 0) {
                    throw new Exception(session.getLabel("ERREUR_INCOHERENCE_DONNEES_SOLDE") + montantRestitPourComp
                            + " <> " + montantDettesPourComparaison);
                }
            }
            // Montant rétro < montant dettes ancienne rente
            switch (cas) {
            // Dette <= Retro
                case 3:

                    if (!isMethodeMontantNet) {
                        memoryLog.logMessage("Dette<=Retro ", FWMessage.INFORMATION, this.getClass().getName());

                        montantDettes.negate();

                        montantCID.negate();
                        montantDettes.sub(montantCID);

                        memoryLog.logMessage(doEcriture(session, compta, montantDettes.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                                dateComptable, null));

                        // bz-6333 Ne créé la section de restit que si un montant doit être inscrit. Evite de créer des
                        // sct.
                        // de restit. vide
                        sectionRestitution = initSectionRestitution(sectionRestitution, montantDettes.toString(),
                                process, transaction, decision.getIdDemandeRente(), idExterneRole,
                                compteAnnexe.getIdCompteAnnexe());

                        memoryLog.logMessage(doEcriture(session, compta, montantDettes.toString(),
                                prestationARestituer, compteAnnexe.getIdCompteAnnexe(),
                                sectionRestitution.getIdSection(), dateComptable, null));

                        montantDettes.negate();
                        // ----------------------------------------------------------------------------->>
                        // Compensation entre secteur (compensation)
                        memoryLog.logMessage(doEcriture(session, compta, montantDettes.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(),
                                sectionRestitution.getIdSection(), dateComptable, null));
                        // -----------------------------------------------------------------------------<<
                    }
                    break;
                // Dette<=(Retro + courant)
                case 2:
                    memoryLog.logMessage("Dettes <= Retro+Courant ", FWMessage.INFORMATION, this.getClass().getName());

                    // FWCurrency montantDisponible = new
                    // FWCurrency(montantRetroactif.toString());
                    // montantDisponible.sub(getMontantCourant(session, transaction,
                    // decision).toString());

                    // ----------------------------------------------------------------------------->>
                    // Sont dans le même groupe
                    montantARestituer.negate();
                    montantDettes.negate();
                    if (isMethodeMontantNet) {

                        // ----------------------------------------------------------------------------->>
                        // Montant a restituer (compensation)
                        memoryLog.logMessage(doEcriture(session, compta, montantARestituer.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                                dateComptable, null));
                        // -----------------------------------------------------------------------------<<

                        // bz-6333 Ne créé la section de restit que si un montant doit être inscrit. Evite de créer des
                        // sct.
                        // de restit. vide
                        sectionRestitution = initSectionRestitution(sectionRestitution, montantDettes.toString(),
                                process, transaction, decision.getIdDemandeRente(), idExterneRole,
                                compteAnnexe.getIdCompteAnnexe());

                        // Demande de restitution
                        memoryLog.logMessage(doEcriture(session, compta, montantARestituer.toString(),
                                prestationARestituer, compteAnnexe.getIdCompteAnnexe(),
                                sectionRestitution.getIdSection(), dateComptable, null));

                        montantARestituer.negate();
                        montantDettes.negate();
                        // ----------------------------------------------------------------------------->>
                        // Compensation entre secteur (compensation)
                        memoryLog.logMessage(doEcriture(session, compta, montantARestituer.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(),
                                sectionRestitution.getIdSection(), dateComptable, null));
                        // -----------------------------------------------------------------------------<<
                    } else {

                        // ----------------------------------------------------------------------------->>
                        // Montant a restituer (compensation)

                        // Il faut encore déduire de ce montant, le cumul des
                        // montants inter-décision.
                        montantCID.negate();
                        montantDettes.sub(montantCID);

                        memoryLog.logMessage(doEcriture(session, compta, montantDettes.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                                dateComptable, null));

                        // -----------------------------------------------------------------------------<<

                        // bz-6333 Ne créé la section de restit que si un montant doit être inscrit. Evite de créer des
                        // sct.
                        // de restit. vide
                        sectionRestitution = initSectionRestitution(sectionRestitution, montantDettes.toString(),
                                process, transaction, decision.getIdDemandeRente(), idExterneRole,
                                compteAnnexe.getIdCompteAnnexe());

                        // Demande de restitution
                        memoryLog.logMessage(doEcriture(session, compta, montantDettes.toString(),
                                prestationARestituer, compteAnnexe.getIdCompteAnnexe(),
                                sectionRestitution.getIdSection(), dateComptable, null));

                        montantARestituer.negate();
                        montantDettes.negate();
                        // ----------------------------------------------------------------------------->>
                        // Compensation entre secteur (compensation)
                        memoryLog.logMessage(doEcriture(session, compta, montantDettes.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(),
                                sectionRestitution.getIdSection(), dateComptable, null));
                        // -----------------------------------------------------------------------------<<

                    }
                    break;
                // Dette>(Retro + courant)
                case 1:
                    memoryLog.logMessage("Dettes > Retro+Courant ", FWMessage.INFORMATION, this.getClass().getName());

                    // ----------------------------------------------------------------------------->>
                    montantARestituer.negate();
                    montantDettes.negate();

                    // Sont dans le même groupe
                    if (isMethodeMontantNet) {

                        // ----------------------------------------------------------------------------->>
                        // Montant a restituer (compensation) au débit
                        montantCourant.negate();
                        memoryLog.logMessage(doEcriture(session, compta, montantCourant.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                                dateComptable, null));

                        montantCourant.negate();
                        // -----------------------------------------------------------------------------<<

                        // bz-6333 Ne créé la section de restit que si un montant doit être inscrit. Evite de créer des
                        // sct.
                        // de restit. vide
                        sectionRestitution = initSectionRestitution(sectionRestitution, montantDettes.toString(),
                                process, transaction, decision.getIdDemandeRente(), idExterneRole,
                                compteAnnexe.getIdCompteAnnexe());

                        // Demande de restitution au débit
                        memoryLog.logMessage(doEcriture(session, compta, montantARestituer.toString(),
                                prestationARestituer, compteAnnexe.getIdCompteAnnexe(),
                                sectionRestitution.getIdSection(), dateComptable, null));

                        memoryLog.logMessage(doEcriture(session, compta, montantCourant.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(),
                                sectionRestitution.getIdSection(), dateComptable, null));

                    } else {

                        // ----------------------------------------------------------------------------->>
                        // Montant a restituer (compensation) au débit
                        montantCourant.negate();
                        memoryLog.logMessage(doEcriture(session, compta, montantCourant.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                                dateComptable, null));

                        montantCourant.negate();
                        // -----------------------------------------------------------------------------<<

                        // Demande de restitution au débit
                        montantRetroactif.negate();
                        memoryLog.logMessage(doEcriture(session, compta, montantRetroactif.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                                dateComptable, null));
                        montantRetroactif.negate();

                        // bz-6333 Ne créé la section de restit que si un montant doit être inscrit. Evite de créer des
                        // sct.
                        // de restit. vide
                        sectionRestitution = initSectionRestitution(sectionRestitution, montantDettes.toString(),
                                process, transaction, decision.getIdDemandeRente(), idExterneRole,
                                compteAnnexe.getIdCompteAnnexe());

                        memoryLog.logMessage(doEcriture(session, compta, montantDettes.toString(),
                                prestationARestituer, compteAnnexe.getIdCompteAnnexe(),
                                sectionRestitution.getIdSection(), dateComptable, null));

                        memoryLog.logMessage(doEcriture(session, compta, montantCourant.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(),
                                sectionRestitution.getIdSection(), dateComptable, null));

                        memoryLog.logMessage(doEcriture(session, compta, montantRetroactif.toString(),
                                rubriqueCompensation, compteAnnexe.getIdCompteAnnexe(),
                                sectionRestitution.getIdSection(), dateComptable, null));
                    }
                    break;
                default:
                    throw new Exception("Unsuported restitution case.");
            }
        }

        /**
         * 
         * Traitement des dettes de type compensation inter-décision...
         * 
         * 
         */
        // Il y a des dettes, provenant d'un autre tiers (un des membres de la
        // famille)
        // Cas des compensation inter-decision...

        for (int i = 0; i < ovs.length; i++) {
            REOrdresVersements ov = ovs[i];

            // bz-5816
            if (IREOrdresVersements.CS_TYPE_DETTE.equals(ov.getCsType())) {
                if (((ov.getIsValide() != null) && ov.getIsValide().booleanValue())
                        && ((ov.getIsCompense() != null) && ov.getIsCompense().booleanValue())
                        && ((ov.getIsCompensationInterDecision() != null) && ov.getIsCompensationInterDecision()
                                .booleanValue())) {

                    if ((sectionNormale == null) || JadeStringUtil.isBlankOrZero(sectionNormale.getIdSection())) {
                        sectionNormale = process.retrieveSection(transaction, decision.getIdDemandeRente(),
                                idExterneRole, compteAnnexe.getIdCompteAnnexe(),
                                APISection.ID_CATEGORIE_SECTION_DECISION);
                    }

                    // Récupération de l'idTiers du membre de la famille, pour
                    // qui se fait la compensation...
                    RECompensationInterDecisions cid = new RECompensationInterDecisions();
                    cid.setSession(session);
                    cid.setIdOVCompensation(ov.getIdOrdreVersement());
                    cid.setAlternateKey(RECompensationInterDecisions.ALTERNATE_KEY_ID_OV_COMPENSATION);
                    cid.retrieve(transaction);
                    PRAssert.notIsNew(cid, null);

                    // Récupéreation de l'ov sur lequel l'on souhaite y faire
                    // une compensation inter-décision...
                    // Ordre de versement de la contrepartie
                    REOrdresVersements ovCP = new REOrdresVersements();
                    ovCP.setSession(session);
                    ovCP.setIdOrdreVersement(cid.getIdOrdreVersement());
                    ovCP.retrieve(transaction);
                    PRAssert.notIsNew(ovCP, null);

                    // Prestation de la contrepartie
                    REPrestations prstCP = new REPrestations();
                    prstCP.setSession(session);
                    prstCP.setIdPrestation(ovCP.getIdPrestation());
                    prstCP.retrieve(transaction);

                    REDecisionEntity decisionCP = new REDecisionEntity();
                    decisionCP.setSession(session);
                    decisionCP.setIdDecision(prstCP.getIdDecision());
                    decisionCP.retrieve(transaction);

                    String idTiers = ovCP.getIdTiers();
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiers);

                    // Montant a restituer (compensation) au débit
                    // ----------------------------------------------------------------------------->>
                    FWCurrency dette = new FWCurrency(ov.getMontant());
                    dette.negate();

                    String libelle = session.getLabel("MOD_RST_COMPENS_POUR")
                            + tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                    memoryLog.logMessage(
                            session.getLabel("MOD_RST_COMPENS_POUR")
                                    + tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), FWMessage.INFORMATION,
                            this.getClass().getName());

                    memoryLog.logMessage(doEcriture(session, compta, dette.toString(), rubriqueCompensation,
                            compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(), dateComptable, libelle));
                    dette.negate();
                    // -----------------------------------------------------------------------------<<
                    String idExterneRole2 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                    PRTiersWrapper twBP = PRTiersHelper.getTiersParId(session, idTiersBeneficiairePrincipal);

                    libelle = session.getLabel("MOD_RST_COMPENSE_SUR") + " "
                            + twBP.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + twBP.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                    memoryLog.logMessage(session.getLabel("MOD_RST_COMPENSE_SUR") + " " + idExterneRole,
                            FWMessage.INFORMATION, this.getClass().getName());

                    // récupération du compte annexe RENTIER
                    APICompteAnnexe compteAnnexe2 = compta.getCompteAnnexeByRole(idTiers, IntRole.ROLE_RENTIER,
                            idExterneRole2);

                    if (compteAnnexe2 == null) {
                        throw new Exception(session.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"));
                    }

                    APISection sectionRestitution2 = process.retrieveSection(transaction,
                            decisionCP.getIdDemandeRente(), idExterneRole2, compteAnnexe2.getIdCompteAnnexe(),
                            APISection.ID_CATEGORIE_SECTION_RESTITUTIONS);

                    // Compensation entre secteur (compensation)
                    // ----------------------------------------------------------------------------->>
                    memoryLog.logMessage(doEcriture(session, compta, dette.toString(), rubriqueCompensation,
                            compteAnnexe2.getIdCompteAnnexe(), sectionRestitution2.getIdSection(), dateComptable,
                            libelle));
                    // -----------------------------------------------------------------------------<<
                }
            }
        }
        return memoryLog;
    }

    @Override
    public int getPriority() {
        return 300;
    }

    protected APIRubrique getRubriqueRestitution(RERenteAccordee ra) throws Exception {

        switch (ra.getGroupeGenreRente()) {
            case REPrestationsAccordees.GROUPE_API_AI:
                return REModuleComptableFactory.getInstance().PRST_API_AI_RESTITUER;

            case REPrestationsAccordees.GROUPE_API_AVS:
                return REModuleComptableFactory.getInstance().PRST_API_AVS_RESTITUER;

            case REPrestationsAccordees.GROUPE_REO_AI:
                return REModuleComptableFactory.getInstance().PRST_AI_RESTITUER;

            case REPrestationsAccordees.GROUPE_REO_AVS:
                return REModuleComptableFactory.getInstance().PRST_AVS_RESTITUER;

            case REPrestationsAccordees.GROUPE_RO_AI:
                return REModuleComptableFactory.getInstance().PRST_AI_RESTITUER;

            case REPrestationsAccordees.GROUPE_RO_AVS:
                return REModuleComptableFactory.getInstance().PRST_AVS_RESTITUER;

            default:
                return null;
        }
    }

    // bz-6333
    private APISection initSectionRestitution(APISection sctRestit, String montant,
            RETraiterLotDecisionsProcess process, BTransaction transaction, String idDemandeRente,
            String idExterneRole, String idCompteAnnexe) throws Exception {

        if ((sctRestit != null) && !sctRestit.isNew()) {
            return sctRestit;
        } else {
            if (isGenererEcritureComptable && !JadeStringUtil.isDecimalEmpty(montant)) {
                sctRestit = process.retrieveSection(transaction, idDemandeRente, idExterneRole, idCompteAnnexe,
                        APISection.ID_CATEGORIE_SECTION_RESTITUTIONS);
            } else {
                sctRestit = new CASection();
            }
        }
        return sctRestit;
    }
}
