package globaz.corvus.module.compta;

/**
 * Module comptable de gestion des écritures comptable Traite les cas de Compensation des dettes dans la CA.
 * 
 * @author : scr
 * 
 * 
 */
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.process.RETraiterLotDecisionsProcess;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;

public class REModCpt_CompensationCA extends AREModuleComptable implements IREModuleComptable {

    class InnerMontant {
        public FWCurrency montantDettesAvanceCA = new FWCurrency(0);
        public FWCurrency montantDettesDecisionCA = new FWCurrency(0);
        public FWCurrency montantDettesPrstBloqueeCA = new FWCurrency(0);
        public FWCurrency montantDettesRenteEnCours = new FWCurrency(0);
        public FWCurrency montantDettesRestitutionCA = new FWCurrency(0);
        public FWCurrency montantDettesRetourRenteCA = new FWCurrency(0);
    }

    public REModCpt_CompensationCA(boolean isGenererEcritureComptable) throws Exception {
        super(isGenererEcritureComptable);
    }

    /**
     * 
     * @param session
     * @param transaction
     * @param montantDisponible
     * @param ov
     * @param memoryLog
     * @param compta
     * @param rubriqueCompensation
     * @param section
     *            section vide, cad instantiation de la section, avec la sessionOsiris déjà settée. le retrieve se fait
     *            dans cette méthode.
     * @param dateComptable
     * @return le solde disponible pour la prochaine compensation
     * @throws Exception
     */
    private FWCurrency doCompensationDesRestitutions(BSession session, BTransaction transaction,
            String montantDisponible, REOrdresVersements ov, FWMemoryLog memoryLog,
            APIGestionComptabiliteExterne compta, APIRubrique rubriqueCompensation, APISection section,
            String dateComptable) throws Exception {

        FWCurrency soldeDisponible = new FWCurrency(montantDisponible);

        if (soldeDisponible.isNegative() || soldeDisponible.isZero()) {
            return soldeDisponible;
        }

        // Traitement des dettes comptable uniquement (compensées)
        if ((ov.getIsCompense() != null) && ov.getIsCompense()) {

            switch (ov.getCsTypeOrdreVersement()) {

                case DETTE_RENTE_AVANCES:
                case DETTE_RENTE_DECISION:
                case DETTE_RENTE_PRST_BLOQUE:
                case DETTE_RENTE_RESTITUTION:
                case DETTE_RENTE_RETOUR:

                    CASection sectionACompenserEntity = new CASection();
                    sectionACompenserEntity.setSession(session);
                    sectionACompenserEntity.setIdSection(ov.getIdSection());
                    sectionACompenserEntity.retrieve(transaction);

                    CACompteAnnexe compteAnnexeSectionACompenser = new CACompteAnnexe();
                    compteAnnexeSectionACompenser.setSession(session);
                    compteAnnexeSectionACompenser.setIdCompteAnnexe(sectionACompenserEntity.getIdCompteAnnexe());
                    compteAnnexeSectionACompenser.retrieve(transaction);

                    // récupération du compte annexe RENTIER
                    APICompteAnnexe compteAnnexe = compta.getCompteAnnexeByRole(
                            compteAnnexeSectionACompenser.getIdTiers(), compteAnnexeSectionACompenser.getIdRole(),
                            compteAnnexeSectionACompenser.getIdExterneRole());
                    APISection sectionACompenser = compta.getSectionByIdExterne(sectionACompenserEntity.getIdSection(),
                            sectionACompenserEntity.getIdTypeSection(), sectionACompenserEntity.getIdExterne());

                    memoryLog.logMessage("Bénéficiaire " + compteAnnexe.getIdExterneRole(), FWMessage.INFORMATION, this
                            .getClass().getName());

                    // Il y a des Dettes dans la CA
                    if (!JadeStringUtil.isBlankOrZero(ov.getMontant())) {
                        // Initialisation de la section
                        sectionACompenser.setIdSection(ov.getIdSection());
                        sectionACompenser.retrieve(transaction);

                        // Calcul du montant a verser
                        FWCurrency montant = new FWCurrency(ov.getMontant());
                        if (montant.compareTo(soldeDisponible) == 1) {
                            montant = new FWCurrency(soldeDisponible.toString());
                            soldeDisponible = new FWCurrency(0);
                        } else {
                            soldeDisponible.sub(montant);
                        }

                        // Ecriture en vert dans cas #32 et 36
                        // Restitution pour compensation des dettes en CA.
                        memoryLog
                                .logMessage(doEcriture(session, compta, montant.toString(), rubriqueCompensation,
                                        compteAnnexe.getIdCompteAnnexe(), sectionACompenser.getIdSection(),
                                        dateComptable, null));
                    }
                    break;

                default:
                    break;
            }
        }

        return new FWCurrency(soldeDisponible.toString());
    }

    private InnerMontant doCumulMontant(InnerMontant im, REOrdresVersements ov) {

        if ((ov.getIsCompense() != null) && ov.getIsCompense()) {
            switch (ov.getCsTypeOrdreVersement()) {

                case DETTE:
                    im.montantDettesRenteEnCours.add(ov.getMontant());
                    break;

                case DETTE_RENTE_AVANCES:
                    im.montantDettesAvanceCA.add(ov.getMontant());
                    break;

                case DETTE_RENTE_DECISION:
                    im.montantDettesDecisionCA.add(ov.getMontant());
                    break;

                case DETTE_RENTE_PRST_BLOQUE:
                    im.montantDettesPrstBloqueeCA.add(ov.getMontant());
                    break;

                case DETTE_RENTE_RESTITUTION:
                    im.montantDettesRestitutionCA.add(ov.getMontant());
                    break;

                case DETTE_RENTE_RETOUR:
                    im.montantDettesRetourRenteCA.add(ov.getMontant());
                    break;

                default:
                    break;
            }
        }

        return im;
    }

    /**
     * 
     * @param session
     * @param memoryLog
     * @param compta
     * @param montantPrestation
     * @param rubriqueCompensation
     * @param compteAnnexeBP
     * @param sectionNormale
     * @param dateComptable
     * @param im
     * @return le montanDisponible à compenser
     * @throws Exception
     */
    private FWCurrency doEcrituresCompensations(BSession session, FWMemoryLog memoryLog,
            APIGestionComptabiliteExterne compta, String montantPrestation, APIRubrique rubriqueCompensation,
            APICompteAnnexe compteAnnexeBP, APISection sectionNormale, String dateComptable, InnerMontant im)
            throws Exception {

        FWCurrency soldeDisponible = new FWCurrency(montantPrestation);
        soldeDisponible.sub(im.montantDettesRenteEnCours);
        if (soldeDisponible.isNegative()) {
            throw new Exception("Montant disponible inférieur à 0");
        }

        FWCurrency result = new FWCurrency(soldeDisponible.toString());

        // ----------------------------------------------------------------------------------------
        FWCurrency montant = new FWCurrency(im.montantDettesAvanceCA.toString());
        if (montant.compareTo(soldeDisponible) == -1) {
            soldeDisponible.sub(montant);
        } else {
            montant = new FWCurrency(soldeDisponible.toString());
            soldeDisponible = new FWCurrency(0);
        }

        montant.negate();
        memoryLog.logMessage(doEcriture(session, compta, montant.toString(), rubriqueCompensation,
                compteAnnexeBP.getIdCompteAnnexe(), sectionNormale.getIdSection(), dateComptable, null));

        if (soldeDisponible.isZero()) {
            return result;
        }
        // ----------------------------------------------------------------------------------------
        montant = new FWCurrency(im.montantDettesRestitutionCA.toString());
        if (montant.compareTo(soldeDisponible) == -1) {
            soldeDisponible.sub(montant);
        } else {
            montant = new FWCurrency(soldeDisponible.toString());
            soldeDisponible = new FWCurrency(0);
        }

        montant.negate();
        memoryLog.logMessage(doEcriture(session, compta, montant.toString(), rubriqueCompensation,
                compteAnnexeBP.getIdCompteAnnexe(), sectionNormale.getIdSection(), dateComptable, null));

        if (soldeDisponible.isZero()) {
            return result;
        }
        // ----------------------------------------------------------------------------------------
        montant = new FWCurrency(im.montantDettesDecisionCA.toString());
        if (montant.compareTo(soldeDisponible) == -1) {
            soldeDisponible.sub(montant);
        } else {
            montant = new FWCurrency(soldeDisponible.toString());
            soldeDisponible = new FWCurrency(0);
        }
        montant.negate();

        memoryLog.logMessage(doEcriture(session, compta, montant.toString(), rubriqueCompensation,
                compteAnnexeBP.getIdCompteAnnexe(), sectionNormale.getIdSection(), dateComptable, null));

        if (soldeDisponible.isZero()) {
            return result;
        }
        // ----------------------------------------------------------------------------------------
        montant = new FWCurrency(im.montantDettesPrstBloqueeCA.toString());
        if (montant.compareTo(soldeDisponible) == -1) {
            soldeDisponible.sub(montant);
        } else {
            montant = new FWCurrency(soldeDisponible.toString());
            soldeDisponible = new FWCurrency(0);
        }
        montant.negate();

        memoryLog.logMessage(doEcriture(session, compta, montant.toString(), rubriqueCompensation,
                compteAnnexeBP.getIdCompteAnnexe(), sectionNormale.getIdSection(), dateComptable, null));

        if (soldeDisponible.isZero()) {
            return result;
        }
        // ----------------------------------------------------------------------------------------
        montant = new FWCurrency(im.montantDettesRetourRenteCA.toString());
        if (montant.compareTo(soldeDisponible) == -1) {
            soldeDisponible.sub(montant);
        } else {
            montant = new FWCurrency(soldeDisponible.toString());
            soldeDisponible = new FWCurrency(0);
        }

        montant.negate();
        memoryLog.logMessage(doEcriture(session, compta, montant.toString(), rubriqueCompensation,
                compteAnnexeBP.getIdCompteAnnexe(), sectionNormale.getIdSection(), dateComptable, null));

        return result;

    }

    /**
     * Traitement des écritures comptables pmt avance.
     */
    @Override
    public FWMemoryLog doTraitement(RETraiterLotDecisionsProcess process, APIGestionComptabiliteExterne compta,
            BSession session, BTransaction transaction, REDecisionEntity decision, String dateComptable, String idLot,
            String dateEcheance) throws Exception {

        FWMemoryLog memoryLog = new FWMemoryLog();

        memoryLog.logMessage(session.getLabel("TRAITEMENT_COMPTABLE_COMPENSATION"), FWMessage.INFORMATION, this
                .getClass().getName());

        REPrestations prst = decision.getPrestation(transaction);
        REOrdresVersements[] ovs = prst.getOrdresVersement(transaction);

        // Calcul du montant courant et rétroactif.
        String idTiersBeneficiairePrincipal = getIdTiersBeneficiairePrincipal(decision, transaction);
        String idExterneRoleBP = PRTiersHelper.getTiersParId(session, idTiersBeneficiairePrincipal).getProperty(
                PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

        // Dettes par secteur
        FWCurrency montantPrestation = new FWCurrency(prst.getMontantPrestation());

        InnerMontant im = new InnerMontant();
        for (REOrdresVersements ov1 : ovs) {
            im = doCumulMontant(im, ov1);

        }

        memoryLog.logMessage(session.getLabel("TRAITEMENT_COMPTABLE_COMPENSATION"), FWMessage.INFORMATION, this
                .getClass().getName());

        memoryLog.logMessage("Bénéficiaire principal " + idExterneRoleBP, FWMessage.INFORMATION, this.getClass()
                .getName());

        /*
         * 
         * Exit door !!!!!!
         */
        // Les dettes sur des rentes en cours sont supérieures ou égales au
        // montant de la prestation.
        // Dans ce cas, aucune compensation sur les dettes de la compta ne peut
        // se faire, car plus rien à dispo.
        if (im.montantDettesRenteEnCours.compareTo(montantPrestation) >= 0) {
            memoryLog.logMessage(session.getLabel("INFO_AUCUNE_ECRITURE_A_GENERER"), FWMessage.INFORMATION, this
                    .getClass().getName());

            return memoryLog;
        }

        // récupération du compte annexe RENTIER
        APICompteAnnexe compteAnnexeBP = compta.getCompteAnnexeByRole(idTiersBeneficiairePrincipal,
                IntRole.ROLE_RENTIER, idExterneRoleBP);

        if (compteAnnexeBP == null) {
            throw new Exception(session.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"));
        }

        /*
         * 
         * Initialisation de la section
         */
        APISection sectionNormale = process.retrieveSection(transaction, decision.getIdDemandeRente(), idExterneRoleBP,
                compteAnnexeBP.getIdCompteAnnexe(), APISection.ID_CATEGORIE_SECTION_DECISION);

        APIRubrique rubriqueCompensation = REModuleComptableFactory.getInstance().COMPENSATION;
        /*
         * 
         * Compensations
         * 
         * Les montants à 0 ne génèreront aucunes écritures !!!
         */

        // Ecriture en bleu dans cas #32 et 36
        // Restitution pour compensation des dettes en CA.

        FWCurrency montantDisponible = doEcrituresCompensations(session, memoryLog, compta,
                montantPrestation.toString(), rubriqueCompensation, compteAnnexeBP, sectionNormale, dateComptable, im);

        /*
         * 
         * Compensation des restitutions
         */
        BISession sessionOsiris = PRSession.connectSession(session, CAApplication.DEFAULT_APPLICATION_OSIRIS);
        APISection section = (APISection) sessionOsiris.getAPIFor(APISection.class);

        for (REOrdresVersements ov : ovs) {
            montantDisponible = doCompensationDesRestitutions(session, transaction, montantDisponible.toString(), ov,
                    memoryLog, compta, rubriqueCompensation, section, dateComptable);
        }
        return memoryLog;
    }

    @Override
    public int getPriority() {
        return 700;
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

}
