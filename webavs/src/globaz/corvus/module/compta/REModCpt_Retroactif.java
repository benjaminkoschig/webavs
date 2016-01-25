package globaz.corvus.module.compta;

/**
 * Module comptable de gestion des �critures comptable Traitement des �critures comptables r�troactives.
 * 
 * @author : scr
 */
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.recap.IRERecap;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.ordresversements.RECompensationInterDecisionsManager;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions;
import globaz.corvus.process.RETraiterLotDecisionsProcess;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class REModCpt_Retroactif extends AREModuleComptable implements IREModuleComptable {

    public REModCpt_Retroactif(boolean isGenererEcritureComptable) throws Exception {
        super(isGenererEcritureComptable);
    }

    /**
     * Traitement des �critures comptables r�troactives.
     */
    @Override
    public FWMemoryLog doTraitement(RETraiterLotDecisionsProcess process, APIGestionComptabiliteExterne compta,
            BSession session, BTransaction transaction, REDecisionEntity decision, String dateComptable, String idLot,
            String dateEcheance) throws Exception {

        FWMemoryLog memoryLog = new FWMemoryLog();

        // creation de l'idExterneRole (qui est tout simplement le num�ro num�ro
        // AVS de l'assur�
        String idExterneRole = null;

        String idTiersBeneficiairePrincipal = getIdTiersBeneficiairePrincipal(decision, transaction);

        REPrestations prst = decision.getPrestation(transaction);
        idExterneRole = PRTiersHelper.getTiersParId(session, idTiersBeneficiairePrincipal).getProperty(
                PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

        memoryLog.logMessage(session.getLabel("TRAITEMENT_COMPTABLE_RETRO"), FWMessage.INFORMATION, this.getClass()
                .getName());

        memoryLog.logMessage("B�n�ficiaire principal " + idExterneRole, FWMessage.INFORMATION, this.getClass()
                .getName());

        // r�cup�ration du compte annexe RENTIER
        APICompteAnnexe compteAnnexe = compta.getCompteAnnexeByRole(idTiersBeneficiairePrincipal, IntRole.ROLE_RENTIER,
                idExterneRole);

        if (compteAnnexe == null) {
            throw new Exception(session.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"));
        }

        /*
         * 
         * Initialisation de la section
         */

        APISection sectionNormale = process.retrieveSection(transaction, decision.getIdDemandeRente(), idExterneRole,
                compteAnnexe.getIdCompteAnnexe(), APISection.ID_CATEGORIE_SECTION_DECISION);

        RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions renteAccordee = getRenteAccordee(session, transaction,
                decision);
        String genrePrestation = renteAccordee.getCodePrestation();
        String sousTypeGenrePrestation = renteAccordee.getSousTypeGenrePrestation();

        // R�cup�ration de la rubrique r�troactive
        APIRubrique rubriqueRetro = AREModuleComptable.getRubrique(genrePrestation, sousTypeGenrePrestation,
                AREModuleComptable.TYPE_RUBRIQUE_RETROACTIF);

        // Calcul du montant r�troactif.
        FWCurrency montantRetroactif = getMontantRetro(session, transaction, decision);

        // Recherche du montant des dettes
        FWCurrency montantDettes = new FWCurrency(0);
        REOrdresVersements[] ovs = prst.getOrdresVersement(transaction);

        FWCurrency montantIM = new FWCurrency("0");
        List idsRAACompenser = new ArrayList();
        boolean isCID = false;
        // En cas de CID, toujours appliquer la m�thode des montants BRUT.
        boolean isMethodeMontantBrut = false;

        for (REOrdresVersements ov2 : ovs) {
            REOrdresVersements ov = ov2;
            if (IREOrdresVersements.CS_TYPE_DETTE.equals(ov.getCsType())) {
                if ((ov.getIsValide() != null) && ov.getIsValide().booleanValue() && (ov.getIsCompense() != null)
                        && ov.getIsCompense().booleanValue()) {

                    // Peut arriver dans le cas des CID
                    if (!JadeStringUtil.isBlankOrZero(ov.getIdRenteAccordeeACompenserParOV())) {
                        idsRAACompenser.add(ov.getIdRenteAccordeeACompenserParOV());
                    }

                    montantDettes.add(ov.getMontant());
                    if ((ov.getIsCompensationInterDecision() != null)
                            && ov.getIsCompensationInterDecision().booleanValue()) {
                        isCID = true;
                        isMethodeMontantBrut = true;
                    } else {
                        RECompensationInterDecisionsManager mgr = new RECompensationInterDecisionsManager();
                        mgr.setSession(session);
                        mgr.setForIdOV(ov.getIdOrdreVersement());
                        mgr.find(transaction, 1);
                        if (mgr.getSize() > 0) {
                            isMethodeMontantBrut = true;
                        }
                    }
                }
            } else if (IREOrdresVersements.CS_TYPE_INTERET_MORATOIRE.equals(ov.getCsType())) {
                montantIM.add(ov.getMontant());
            }
        }

        // On soustrait le montant des IM au montant r�troactif.
        // montantRetroactif.sub(montantIM);

        /*
         * 
         * Il y a des dettes � traiter Selon DR 11216 et 11218.
         * 
         * Si nouvelle an ancienne decision sont du m�me genre de rente : Si le montant du r�tro de la nouvelle rente
         * est < que les dettes de l'ancienne rente, pas de r�troactif.
         * 
         * Sinon, on ne verse que la diff�rence.
         */

        if ((idsRAACompenser.size() > 0) || isCID) {

            RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions ra = getRenteAccordee(session, transaction, decision);
            int grpGenreRente = ra.getGroupeGenreRente();

            // On contr�le s'il faut appliquer la technique des montants brut ou
            // des montants nets.
            // Si au moins 1 des ra � compenser n'est pas dans le m�me groupe
            // que celles des RA de la d�cision
            // il faut appliquer la m�thode des montants brut.
            boolean isMethodeMontantNet = true;
            for (Iterator iterator = idsRAACompenser.iterator(); iterator.hasNext();) {
                String idRA = (String) iterator.next();

                RERenteAccordee raACompenser = new RERenteAccordee();
                raACompenser.setSession(session);
                raACompenser.setIdPrestationAccordee(idRA);
                raACompenser.retrieve(transaction);
                PRAssert.notIsNew(raACompenser, null);

                int grpGenreRenteDiminue = raACompenser.getGroupeGenreRente();
                if (grpGenreRenteDiminue != grpGenreRente) {
                    // -> technique des montants brut
                    isMethodeMontantNet = false;
                    break;
                }
            }
            // Traitement selon r�gle des montants BRUT s'il y a des
            // compensations inter-decision.
            if (isMethodeMontantBrut) {
                isMethodeMontantNet = false;
            }

            // Sont dans le m�me groupe. montant NET.
            if (isMethodeMontantNet) {
                // Ex. cas #25
                // Montant r�tro < montant dettes ancienne rente
                if (montantRetroactif.compareTo(montantDettes) == -1) {
                    memoryLog.logMessage(session.getLabel("ERREUR_DETTES_PLUSGRAND_RETRO"), FWMessage.INFORMATION, this
                            .getClass().getName());
                    ;
                    return memoryLog;
                }
                // Ex. cas #26
                // Montant r�tro > montant dettes ancienne rente
                else if (montantRetroactif.compareTo(montantDettes) == 1) {
                    memoryLog.logMessage(session.getLabel("ERREUR_DETTES_PLUSPETIT_RETRO"), FWMessage.INFORMATION, this
                            .getClass().getName());

                    montantRetroactif.sub(montantDettes);

                    // Ecriture Retroactif � verser
                    memoryLog.logMessage(doEcriture(session, compta, montantRetroactif.toString(), rubriqueRetro,
                            compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(), dateComptable, null));

                    int codeRecap = AREModuleComptable.getCodeRecap(genrePrestation,
                            IRERecap.GENRE_RECAP_PMT_RETROACTIF);
                    // Ecriture pour la r�cap
                    memoryLog.logMessage(this.doEcritureRecap(session, transaction, decision.getIdDecision(),
                            codeRecap, montantRetroactif, idTiersBeneficiairePrincipal, idLot));

                }
            }
            // Sont dans des groupes diff�rents (Montant BRUT).
            else {
                // //Montant r�tro < montant dettes ancienne rente
                // //Ex. cas 30
                // if (montantRetroactif.compareTo(montantDettes)==-1) {

                // Ecriture Retroactif � verser
                memoryLog.logMessage(doEcriture(session, compta, montantRetroactif.toString(), rubriqueRetro,
                        compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(), dateComptable, null));

                int codeRecap = AREModuleComptable.getCodeRecap(genrePrestation, IRERecap.GENRE_RECAP_PMT_RETROACTIF);
                // Ecriture pour la r�cap
                memoryLog.logMessage(this.doEcritureRecap(session, transaction, decision.getIdDecision(), codeRecap,
                        montantRetroactif, idTiersBeneficiairePrincipal, idLot));

                return memoryLog;
                // }
                //
                // //Montant r�tro > montant dettes ancienne rente
                // else if (montantRetroactif.compareTo(montantDettes)==1) {
                //
                // int codeRecap = getCodeRecap(genrePrestation,
                // IRERecap.GENRE_RECAP_PMT_RETROACTIF);
                // //Ecriture pour la r�cap
                // memoryLog.logMessage(
                // doEcritureRecap(session, transaction,
                // decision.getIdDemandeRente(), codeRecap, montantRetroactif,
                // idTiersBeneficiairePrincipal, null, idLot));
                //
                // }
            }
        }

        // Pas de dettes
        else {

            // Ecriture RETRO
            memoryLog.logMessage(doEcriture(session, compta, montantRetroactif.toString(), rubriqueRetro,
                    compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(), dateComptable, null));

            int codeRecap = AREModuleComptable.getCodeRecap(genrePrestation, IRERecap.GENRE_RECAP_PMT_RETROACTIF);
            // Ecriture pour la r�cap
            memoryLog.logMessage(this.doEcritureRecap(session, transaction, decision.getIdDecision(), codeRecap,
                    montantRetroactif, idTiersBeneficiairePrincipal, idLot));

        }

        return memoryLog;
    }

    @Override
    public int getPriority() {
        return 200;
    }

}
