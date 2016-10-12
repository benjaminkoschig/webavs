package globaz.corvus.module.compta;

/**
 * Module comptable de gestion des écritures comptable
 * 
 * Traitement des cas écriture comptable de type intérêt moratire
 * 
 * @author : scr
 * 
 * 
 */

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.process.RETraiterLotDecisionsProcess;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

public class REModCpt_IM extends AREModuleComptable implements IREModuleComptable {

    public REModCpt_IM(boolean isGenererEcritureComptable) throws Exception {
        super(isGenererEcritureComptable);
    }

    @Override
    public FWMemoryLog doTraitement(RETraiterLotDecisionsProcess process, APIGestionComptabiliteExterne compta,
            BSession session, BTransaction transaction, REDecisionEntity decision, String dateComptable, String idLot,
            String dateEcheance) throws Exception {
        return doTraitement(process, compta, session, transaction, decision, dateComptable, idLot, dateEcheance, null);
    }

    /**
     * Traitement des écritures comptables
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

        memoryLog.logMessage("Traitement comptable IM", FWMessage.INFORMATION, this.getClass().getName());

        memoryLog.logMessage("Bénéficiaire principal " + idExterneRole, FWMessage.INFORMATION, this.getClass()
                .getName());

        // récupération du compte annexe RENTIER
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

        RERenteAccordee ra = getRenteAccordee(session, transaction, decision);
        APIRubrique rubriqueIM = null;

        if (REPrestationsAccordees.GROUPE_API_AI == ra.getGroupeGenreRente()
                || REPrestationsAccordees.GROUPE_REO_AI == ra.getGroupeGenreRente()
                || REPrestationsAccordees.GROUPE_RO_AI == ra.getGroupeGenreRente()) {

            rubriqueIM = REModuleComptableFactory.getInstance().INTERET_MORATOIRE_AI;
        } else {
            rubriqueIM = REModuleComptableFactory.getInstance().INTERET_MORATOIRE_AVS;
        }

        /*
         * 
         * Ecriture de type IM
         */
        REOrdresVersements[] ovs = prst.getOrdresVersement(transaction);

        if (ovs == null) {
            throw new Exception(session.getLabel("ERREUR_AUCUN_OV_POUR_PREST") + prst.getIdPrestation() + "/"
                    + decision.getIdDecision());
        }
        for (REOrdresVersements ov : ovs) {
            // Recherche des ov de type IM
            if (IREOrdresVersements.CS_TYPE_INTERET_MORATOIRE.equals(ov.getCsType())) {
                FWCurrency montantAVerser = new FWCurrency(ov.getMontant());

                // Récupération d'une des RA de la décision.
                // Pour une décision, les RA sont obligatoirement sur la même
                // rubrique -> on prend la première que l'on trouve.

                // Ecriture normal (rente en cours)
                memoryLog.logMessage(doEcriture(session, compta, montantAVerser.toString(), rubriqueIM,
                        compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(), dateComptable, null));
            }
        }
        return memoryLog;
    }

    @Override
    public int getPriority() {
        // Auto-generated method stub
        return 500;
    }

}
