package globaz.corvus.module.compta;

/**
 * Module comptable de gestion des écritures comptable
 * 
 * Traitement des Ordres de versements pour les créanciers. Le bénéficiaire principal n'est pas pris en compte dans ce
 * traitement.
 * 
 * @author : scr
 * 
 * 
 */
import globaz.corvus.application.REApplication;
import globaz.corvus.db.creances.RECreanceAccordee;
import globaz.corvus.db.creances.RECreanceAccordeeManager;
import globaz.corvus.db.creances.RECreancier;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.process.RETraiterLotDecisionsProcess;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APISection;
import globaz.osiris.external.IntRole;
import globaz.prestation.tools.PRAssert;
import java.math.BigDecimal;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.domaine.Decision;
import ch.globaz.corvus.domaine.OrdreVersement;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class REModCpt_Creanciers extends AREModuleComptable implements IREModuleComptable {

    public REModCpt_Creanciers(boolean isGenererEcritureComptable) throws Exception {
        super(isGenererEcritureComptable);
    }

    /**
     * Traitement des Ordres de versements pour les créanciers. Le bénéficiaire principal n'est pas pris en compte dans
     * ce traitement.
     */
    @Override
    public FWMemoryLog doTraitement(RETraiterLotDecisionsProcess process, APIGestionComptabiliteExterne compta,
            BSession session, BTransaction transaction, REDecisionEntity decisionEntity, String dateComptable,
            String idLot, String dateEcheance) throws Exception {

        FWMemoryLog memoryLog = new FWMemoryLog();

        try {
            BSessionUtil.initContext(session, this);

            Decision decision = CorvusServiceLocator.getDecisionService().getDecision(
                    Long.parseLong(decisionEntity.getIdDecision()));

            PersonneAVS beneficiairePrincipalDecision = decision.getBeneficiairePrincipal();

            memoryLog.logMessage(session.getLabel("TRAITEMENT_COMPTABLE_CREANCIERS"), FWMessage.INFORMATION, this
                    .getClass().getName());
            memoryLog.logMessage("Bénéficiaire principal " + beneficiairePrincipalDecision.getNss(),
                    FWMessage.INFORMATION, this.getClass().getName());

            // récupération du compte annexe RENTIER
            APICompteAnnexe compteAnnexe = compta.getCompteAnnexeByRole(beneficiairePrincipalDecision.getId()
                    .toString(), IntRole.ROLE_RENTIER, beneficiairePrincipalDecision.getNss().toString());

            if (compteAnnexe == null) {
                throw new Exception(session.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"));
            }

            // Initialisation de la section
            APISection sectionNormale = process.retrieveSection(transaction, decision.getDemande().getId().toString(),
                    beneficiairePrincipalDecision.getNss().toString(), compteAnnexe.getIdCompteAnnexe(),
                    APISection.ID_CATEGORIE_SECTION_DECISION);

            for (OrdreVersement ov : decision.getOrdresVersementPourType(TypeOrdreVersement.CREANCIER,
                    TypeOrdreVersement.ASSURANCE_SOCIALE)) {

                if (ov.getMontantCompense().compareTo(BigDecimal.ZERO) > 0) {
                    // On récupère la réf. de pmt du créanciers...
                    RECreanceAccordeeManager craccMgr = new RECreanceAccordeeManager();
                    craccMgr.setSession(session);
                    craccMgr.setForIdOrdreVersement(ov.getId().toString());
                    craccMgr.find(transaction, 1);

                    if (craccMgr.isEmpty()) {
                        throw new Exception(session.getLabel("ERREUR_INCOHERENCE_DONNEES_CREANCES_OV")
                                + ov.getId().toString() + "/" + beneficiairePrincipalDecision.getNss());
                    }

                    RECreanceAccordee cracc = (RECreanceAccordee) craccMgr.getFirstEntity();

                    RECreancier creancier = new RECreancier();
                    creancier.setSession(session);
                    creancier.setIdCreancier(cracc.getIdCreancier());
                    creancier.retrieve(transaction);
                    PRAssert.notIsNew(creancier, session.getLabel("ERREUR_INCOHERENCE_DONNEES_CREANCES_OV")
                            + ov.getId().toString() + "/" + beneficiairePrincipalDecision.getNss());

                    String refPmt = creancier.getRefPaiement();
                    String motifVersement = getMotifVersement(session, refPmt, decision);

                    memoryLog.logMessage(doOrdreVersement(
                            session,
                            compta,
                            compteAnnexe.getIdCompteAnnexe(),
                            sectionNormale.getIdSection(),
                            ov.getMontantCompense().toString(),
                            loadAdressePaiement(session, transaction, dateEcheance, creancier.getIdTiersAdressePmt(),
                                    REApplication.CS_DOMAINE_ADRESSE_CORVUS).getIdAvoirPaiementUnique(),
                            motifVersement, dateEcheance, false));
                }
            }

        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        } finally {
            BSessionUtil.stopUsingContext(this);
        }

        return memoryLog;
    }

    @Override
    public int getPriority() {
        // Auto-generated method stub
        return 600;
    }

}
