package globaz.corvus.module.compta;

/**
 * Module comptable de gestion des �critures comptable
 * 
 * Traitement des �critures comptables de type Impot � la source
 * 
 * @author : scr
 * 
 */
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.prestations.REPrestations;
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

public class REModCpt_IS extends AREModuleComptable implements IREModuleComptable {

    public REModCpt_IS(boolean isGenererEcritureComptable) throws Exception {
        super(isGenererEcritureComptable);
    }

    /**
     * Traitement des �critures comptables
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

        memoryLog.logMessage("Traitement comptable IS", FWMessage.INFORMATION, this.getClass().getName());

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

        // Ecritures sur la rubrique concern�e
        APIRubrique rubriqueIS = REModuleComptableFactory.getInstance().IMPOT_SOURCE;

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
            // Recherche des ov de type IS
            if (IREOrdresVersements.CS_TYPE_IMPOT_SOURCE.equals(ov.getCsType())) {
                FWCurrency montantAVerser = new FWCurrency(ov.getMontant());
                montantAVerser.negate();

                // R�cup�ration d'une des RA de la d�cision.
                // Pour une d�cision, les RA sont obligatoirement sur la m�me
                // rubrique -> on prend la premi�re que l'on trouve.

                // Ecriture normal (rente en cours)
                memoryLog.logMessage(doEcriture(session, compta, montantAVerser.toString(), rubriqueIS,
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
