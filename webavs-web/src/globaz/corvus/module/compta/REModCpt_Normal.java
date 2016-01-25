package globaz.corvus.module.compta;

import globaz.corvus.api.recap.IRERecap;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.process.RETraiterLotDecisionsProcess;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntRole;
import java.math.BigDecimal;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.domaine.Decision;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.constantes.TypeTraitementDecisionRente;

/**
 * 
 * Module comptable de gestion des �critures comptable.<br/>
 * Traitement des cas �criture comptable pour le mois en cours, et uniquement pour l'assur� principal.<br/>
 * Ecriture comptable du mois en cours, s'il y a lieu.<br/>
 * Ordre de versement pour le b�n�ficiaire principal, s'il y a lieu (inclus le r�tro).
 * 
 * @author : scr
 */
public class REModCpt_Normal extends AREModuleComptable implements IREModuleComptable {

    public REModCpt_Normal(boolean isGenererEcritureComptable) throws Exception {
        super(isGenererEcritureComptable);
    }

    /**
     * Traitement des �critures comptables Si l'objet compta == null, seul les �critures pour la r�cap seront effectu�es
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

            // creation de l'idExterneRole (qui est tout simplement le num�ro num�ro
            // AVS de l'assur�
            String idExterneRole = decision.getBeneficiairePrincipal().getNss().toString();

            memoryLog.logMessage("Traitement comptable STANDARD", FWMessage.INFORMATION, this.getClass().getName());
            memoryLog.logMessage("B�n�ficiaire principal " + idExterneRole, FWMessage.INFORMATION, this.getClass()
                    .getName());

            // r�cup�ration du compte annexe RENTIER
            APICompteAnnexe compteAnnexe = null;
            APISection sectionNormale = null;

            if (compta != null) {
                compteAnnexe = compta.getCompteAnnexeByRole(decision.getBeneficiairePrincipal().getId().toString(),
                        IntRole.ROLE_RENTIER, idExterneRole);

                if (compteAnnexe == null) {
                    throw new Exception(session.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"));
                }

                // Initialisation de la section
                if (isGenererEcritureComptable) {
                    sectionNormale = process.retrieveSection(transaction, decision.getDemande().getId().toString(),
                            idExterneRole, compteAnnexe.getIdCompteAnnexe(), APISection.ID_CATEGORIE_SECTION_DECISION);
                }
                // Si aucune �criture comptable a g�n�rer, on instancie un objet de type section, vide pour �viter de
                // cr�er
                // des nullPointer
                // dans la suite des traitements. Ainsi, aucune section n'est cr��e.
                else {
                    sectionNormale = new CASection();
                }
            }

            FWCurrency montantCourant = getMontantCourant(session, transaction, decisionEntity);

            // Seul les d�cision de type Standard et Courant b�n�ficie du traitement dans le bloc ci-dessous.
            // Les d�cisions de type RETRO ne doivent pas l'appeler car les $p valid�e lors du courant seraient
            // pris en compte et comptabilis� � double.
            if (TypeTraitementDecisionRente.RETRO != decision.getTypeTraitement()) {

                /*
                 * 
                 * Il y a du courant... 2120.3000.0000
                 */
                if (!montantCourant.isZero()) {

                    RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions renteAccordee = getRenteAccordee(session,
                            transaction, decisionEntity);
                    String genrePrestation = renteAccordee.getCodePrestation();
                    String sousTypeGenrePrestation = renteAccordee.getSousTypeGenrePrestation();

                    if (compta != null) {

                        // Ecritures sur la rubrique concern�e
                        APIRubrique rubriqueNormal = AREModuleComptable.getRubrique(genrePrestation,
                                sousTypeGenrePrestation, AREModuleComptable.TYPE_RUBRIQUE_NORMAL);

                        // Ecriture normal (rente en cours)
                        memoryLog.logMessage(doEcriture(session, compta, montantCourant.toString(), rubriqueNormal,
                                compteAnnexe.getIdCompteAnnexe(), sectionNormale.getIdSection(), dateComptable, null));
                    }

                    int codeRecap = AREModuleComptable.getCodeRecap(genrePrestation, IRERecap.GENRE_RECAP_AUGMENTATION);

                    // Ecriture pour la r�cap
                    memoryLog.logMessage(this.doEcritureRecap(session, transaction, decision.getId().toString(),
                            codeRecap, montantCourant, decision.getBeneficiairePrincipal().getId().toString(), idLot));
                }
            }

            if (compta == null) {
                return memoryLog;
            }

            // Traitement des ordres de versement....

            if (decision.getSolde().compareTo(BigDecimal.ZERO) > 0) {
                // On utilisera l'ID Tiers adresse paiement de la prestation accord�e principale
                // de cette d�cision pour payer
                RenteAccordee renteAccordeePrincipale = decision.getRenteAccordeePrincipale();

                String motifVersement = getMotifVersement(session,
                        renteAccordeePrincipale.getReferencePourLePaiement(), decision);

                memoryLog.logMessage(doOrdreVersement(
                        session,
                        compta,
                        compteAnnexe.getIdCompteAnnexe(),
                        sectionNormale.getIdSection(),
                        decision.getSolde().toString(),
                        loadAdressePaiement(session, transaction, dateEcheance,
                                renteAccordeePrincipale.getAdresseDePaiement().getId().toString(),
                                REApplication.CS_DOMAINE_ADRESSE_CORVUS).getIdAvoirPaiementUnique(), motifVersement,
                        dateEcheance, false));
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
        return 100;
    }

}
