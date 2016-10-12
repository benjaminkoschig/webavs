package globaz.corvus.module.compta;

/**
 * Module comptable de gestion des �critures de la r�cap
 * 
 * Ce module est appel� lorsqu'un d�cision est prise pour une date post�rieur � la date du dernier pmt. Aucune �criture
 * comptable n'est g�n�r�e, seul une �criture pour la r�cap.
 * 
 * @author : scr
 * 
 * 
 */

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.recap.IRERecap;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.process.RETraiterLotDecisionsProcess;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;

public class REModRecapSansEcrituresComptables extends AREModuleComptable implements IREModuleComptable {

    public REModRecapSansEcrituresComptables(boolean isGenererEcritureComptable) throws Exception {
        super(isGenererEcritureComptable);
    }

    @Override
    public FWMemoryLog doTraitement(RETraiterLotDecisionsProcess process, APIGestionComptabiliteExterne compta,
            BSession session, BTransaction transaction, REDecisionEntity decision, String dateComptable, String idLot,
            String dateEcheance) throws Exception {
        return doTraitement(process, compta, session, transaction, decision, dateComptable, idLot, dateEcheance, null);
    }

    /**
     * Traitement des �critures comptables
     */
    @Override
    public FWMemoryLog doTraitement(RETraiterLotDecisionsProcess process, APIGestionComptabiliteExterne compta,
            BSession session, BTransaction transaction, REDecisionEntity decision, String dateComptable, String idLot,
            String dateEcheance, String idOrganeExecution) throws Exception {

        FWMemoryLog memoryLog = new FWMemoryLog();

        String idTiersBeneficiairePrincipal = getIdTiersBeneficiairePrincipal(decision, transaction);

        // REPrestations prst = decision.getPrestation(transaction);
        String idExterneRole = PRTiersHelper.getTiersParId(session, idTiersBeneficiairePrincipal).getProperty(
                PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

        memoryLog.logMessage("Traitement Ecriture Recap ", FWMessage.INFORMATION, this.getClass().getName());
        memoryLog.logMessage("B�n�ficiaire principal " + idExterneRole, FWMessage.INFORMATION, this.getClass()
                .getName());

        // La date de pmt de la r�cap correspond � la date de d�but du droit.

        REDemandeRente demande = new REDemandeRente();
        demande.setSession(decision.getSession());
        demande.setIdDemandeRente(decision.getIdDemandeRente());
        demande.retrieve();
        PRAssert.notIsNew(demande, null);

        FWCurrency montant = getMontantCourant(session, transaction, decision);

        String genrePrestation = getRenteAccordee(session, transaction, decision).getCodePrestation();
        int codeRecap = AREModuleComptable.getCodeRecap(genrePrestation, IRERecap.GENRE_RECAP_AUGMENTATION);

        // R�capitulation erron�e si prise de d�cision avec
        // "courant valid�" d�s le mois prochain
        // bz-3668
        String dateRecap = null;
        if (IREDecision.CS_TYPE_DECISION_COURANT.equals(decision.getCsTypeDecision())) {
            dateRecap = decision.getDecisionDepuis();
            memoryLog.logMessage(this.doEcritureRecap(session, transaction, codeRecap, montant,
                    idTiersBeneficiairePrincipal, dateRecap, idLot));
        } else {
            memoryLog.logMessage(this.doEcritureRecap(session, transaction, decision.getIdDecision(), codeRecap,
                    montant, idTiersBeneficiairePrincipal, idLot));
        }

        return memoryLog;
    }

    @Override
    public int getPriority() {
        // Auto-generated method stub
        return 0;
    }

}
