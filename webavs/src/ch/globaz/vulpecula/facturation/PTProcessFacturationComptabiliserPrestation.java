package ch.globaz.vulpecula.facturation;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.osiris.business.data.JournalConteneur;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.EcritureSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.util.RubriqueUtil;
import ch.globaz.vulpecula.util.RubriqueUtil.Compte;
import ch.globaz.vulpecula.util.RubriqueUtil.Convention;
import ch.globaz.vulpecula.util.RubriqueUtil.Prestation;

/**
 * Process appelé par le module de facturation PTComptabiliserPrestation.
 * 
 * @since WebBMS 0.01.04
 */
public class PTProcessFacturationComptabiliserPrestation extends PTProcessFacturation {
    private static final long serialVersionUID = -8634364084574688619L;

    private static final String CODE_ISO_CHF = "CHF";

    @Override
    protected void clean() {
    }

    @SuppressWarnings("finally")
    @Override
    protected boolean launch() {
        FAEnteteFactureManager entFactureManager = initEnteteFactureManager();
        BStatement statement = null;

        try {
            statement = entFactureManager.cursorOpen(getTransaction());

            JournalSimpleModel journal = CABusinessServiceLocator.getJournalService().createJournal(
                    getPassage().getLibelle(), getPassage().getDateFacturation());
            JournalConteneur jc = new JournalConteneur();
            jc.AddJournal(journal);
            try {
                // Commit le journal car si le premier cas plante, le journal ne sera pas créé
                JadeThread.commitSession();
                // Met à jour l'idJournal du passage de facturation
                getPassage().setIdJournal(jc.getJournalModel().getIdJournal());
            } catch (Exception e2) {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e2);
            }

            FAEnteteFacture entFacture = null;
            while (((entFacture = (FAEnteteFacture) entFactureManager.cursorReadNext(statement)) != null)
                    && (!entFacture.isNew()) && !isAborted()) {
                try {
                    createEcrituresByAfacts(entFacture, jc);
                } catch (Exception ee) {
                    JadeLogger.warn(JadePersistenceManager.class, "Problem to create ecritures by afacts, reason : "
                            + ee.toString());
                    break;
                }
            }

            CABusinessServiceLocator.getJournalService().comptabilise(
                    CABusinessServiceLocator.getJournalService().createJournalAndOperations(jc));
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur dans la création de l'écriture: " + e.getMessage(),
                    globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
            return false;
        } finally {
            // fermer le cursor du manager des entêtes de facture
            return closeAll(entFactureManager, statement);
        }
    }

    private FAEnteteFactureManager initEnteteFactureManager() {
        FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
        entFactureManager.setSession(getSession());
        entFactureManager.setForIdPassage(getPassage().getIdPassage());
        entFactureManager.setOrderBy(" IDEXTERNEROLE ASC, IDEXTERNEFACTURE ASC ");
        return entFactureManager;
    }

    private boolean closeAll(FAEnteteFactureManager entFactureManager, BStatement statement) {
        try {
            entFactureManager.cursorClose(statement);
            statement = null;
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur dans la fermeture du curseur: " + e.getMessage(),
                    globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
        }
        if (getTransaction().hasErrors()) {
            return false;
        } else {
            return true;
        }
    }

    private String computeIdTypeSection(FAEnteteFacture entFacture) {
        String idSousTypeFacture = entFacture.getIdSousType();
        String theIdTypeFacture = entFacture.getIdTypeFacture();
        String idTypeSection = "";
        if (!JadeStringUtil.isEmpty(idSousTypeFacture)) {
            if (APISection.ID_TYPE_SECTION_DECOMPTE_CAP_CGAS.equalsIgnoreCase(theIdTypeFacture)) {
                idTypeSection = APISection.ID_TYPE_SECTION_DECOMPTE_CAP_CGAS;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_ALLOCATIONS_FAMILIALES)) {
                idTypeSection = APISection.ID_TYPE_SECTION_AF;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_APG)) {
                idTypeSection = APISection.ID_TYPE_SECTION_APG;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_IJAI)) {
                idTypeSection = APISection.ID_TYPE_SECTION_IJAI;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_RESTITUTIONS)) {
                idTypeSection = APISection.ID_TYPE_SECTION_RESTITUTION;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT)) {
                idTypeSection = APISection.ID_TYPE_SECTION_ETUDIANTS;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_PREST_CONV_ABSENCE_JUSTIFIEE)) {
                idTypeSection = APISection.ID_TYPE_SECTION_PRESTATIONS_CONVENTIONNELLES;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_PREST_CONV_CONGE_PAYE)) {
                idTypeSection = APISection.ID_TYPE_SECTION_PRESTATIONS_CONVENTIONNELLES;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_PREST_CONV_SERVICE_MILITAIRE)) {
                idTypeSection = APISection.ID_TYPE_SECTION_PRESTATIONS_CONVENTIONNELLES;
            } else {
                idTypeSection = APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION;
            }
        }
        if (!JadeStringUtil.isEmpty(entFacture.getIdTypeFacture())
                && APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(entFacture.getIdTypeFacture())) {
            idTypeSection = APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE;
        }
        return idTypeSection;
    }

    /**
     * @param idExterneRole
     * @return
     */
    private Convention conventionFromIdExterneRole(String idExterneRole) {
        if (idExterneRole == null || idExterneRole.length() == 0) {
            getMemoryLog().logMessage("Error : idexternerole is null or empty !",
                    globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
            throw new IllegalArgumentException("Error : idexternerole is null or empty !");
        }

        String[] temp = idExterneRole.split("-");
        if (temp.length == 2) {
            return Convention.fromValue(temp[1]);
        }

        getMemoryLog().logMessage("Error : convention not found for idexternerole : " + idExterneRole,
                globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
        throw new IllegalStateException("Error : convention not found for idexternerole : " + idExterneRole);
    }

    /**
     * @param transaction
     * @param idModule
     * @return
     */
    private Prestation prestationFromModuleFacturation(BTransaction transaction, String idModule) {
        if (idModule == null || idModule.length() == 0) {
            getMemoryLog().logMessage("Error : idModule is null or empty !", globaz.framework.util.FWMessage.ERREUR,
                    this.getClass().getName());
            throw new IllegalArgumentException("Error : idModule is null or empty !");
        }

        FAModuleFacturation module = new FAModuleFacturation();
        module.setIdModuleFacturation(idModule);
        try {
            module.retrieve(transaction);
        } catch (Exception e) {
            getMemoryLog().logMessage("Error on FAModuleFacturation retrieve : " + e.getMessage(),
                    globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        if (getTransaction().hasErrors() || module.isNew()) {
            getMemoryLog().logMessage("Error module is new or transaction has errors " + getTransaction().getErrors(),
                    globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
            throw new IllegalStateException("Error module is new or transaction has errors "
                    + getTransaction().getErrors());
        }

        if (FAModuleFacturation.CS_MODULE_ABSENCES_JUSTIFIEES.equals(module.getIdTypeModule())) {
            return Prestation.ABSENCE_JUSTIFIEE;
        } else if (FAModuleFacturation.CS_MODULE_CONGE_PAYE.equals(module.getIdTypeModule())) {
            return Prestation.CONGE_PAYE;
        } else if (FAModuleFacturation.CS_MODULE_SERVICE_MILITAIRE.equals(module.getIdTypeModule())) {
            return Prestation.SERVICE_MILIATIRE;
        }

        getMemoryLog().logMessage("Error : prestation not found for idModule : " + idModule,
                globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
        throw new IllegalStateException("Error : prestation not found for idModule : " + idModule);
    }

    /**
     * @param entFacture
     * @param jc
     * @throws Exception
     */
    private void createEcrituresByAfacts(FAEnteteFacture entFacture, JournalConteneur jc) throws Exception {
        CompteAnnexeSimpleModel compteAnnexe = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(
                jc.getJournalModel().getId(), entFacture.getIdTiers(), entFacture.getIdRole(),
                entFacture.getIdExterneRole(), true);
        SectionSimpleModel section = CABusinessServiceLocator.getSectionService().getSectionByIdExterne(
                compteAnnexe.getIdCompteAnnexe(), computeIdTypeSection(entFacture), entFacture.getIdExterneFacture(),
                jc.getJournalModel());
        // On commit l'éventuelle création du compte annexe ou de la section afin d'éviter un lock lors de l'ajout des
        // écritures.
        BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().commit();

        FAAfactManager afactManager = initAfactManager(entFacture);
        BStatement stmt = afactManager.cursorOpen(getTransaction());
        FAAfact afact = null;
        Montant totalVersement = Montant.ZERO;
        while (((afact = (FAAfact) afactManager.cursorReadNext(stmt)) != null) && (!afact.isNew())) {
            Montant montantFacture = new Montant(afact.getMontantFacture());
            if (!montantFacture.isZero()) {

                totalVersement = totalVersement.add(treatBeneficiaire(afact, montantFacture));

                EcritureSimpleModel operationModel = new EcritureSimpleModel();
                operationModel.setLibelle(computeLibelleEcriture(afact.getLibelle()));
                operationModel.setCodeDebitCredit(computeCodeDebitCredit(montantFacture));
                operationModel.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
                operationModel.setIdSection(section.getIdSection());
                operationModel.setDate(getPassage().getDateFacturation());
                operationModel.setIdRubrique(afact.getIdRubrique());
                operationModel.setMontant(afact.getMontantFacture());
                operationModel.setIdCaisseProf(afact.getNumCaisse());
                operationModel.setMasse(afact.getMasseFacture());
                operationModel.setTaux(afact.getTauxFacture());
                operationModel.setAnnee(afact.getAnneeCotisation());

                jc.addEcriture(operationModel);
            }
        }

        if (!totalVersement.isZero()) {
            jc.addOrdreVersement(jc.getJournalModel().getIdJournal(), compteAnnexe.getIdCompteAnnexe(),
                    section.getIdSection(), entFacture.getIdAdressePaiement(), getPassage().getDateFacturation(),
                    totalVersement.getMontantAbsolu().getValueNormalisee(), CODE_ISO_CHF, CODE_ISO_CHF,
                    APIOperationOrdreVersement.VIREMENT, CAOrdreGroupe.NATURE_VERSEMENT_PRESTATIONS_CONVENTIONNELLES,
                    getPassage().getLibelle() + ": " + compteAnnexe.getIdExterneRole() + "/" + section.getIdExterne());
        }
    }

    /**
     * @param convention
     * @param idModuleFacturation
     * @return
     * @throws IllegalArgumentException
     * @throws GlobazTechnicalException
     */
    private ArrayList<String> retrieveIdsRubriquePartCotCharge(Convention convention, String idModuleFacturation)
            throws IllegalArgumentException, GlobazTechnicalException {

        if (convention == null) {
            throw new IllegalArgumentException("Error : convention is null.");
        }

        if (idModuleFacturation == null || idModuleFacturation.length() == 0) {
            throw new IllegalArgumentException("Error : idModuleFacturation is null or empty.");
        }

        ArrayList<String> idsRubriquesPartPatronales = new ArrayList<String>();
        String csRefRubrique = "";
        Prestation prest = prestationFromModuleFacturation(getTransaction(), idModuleFacturation);
        List<Compte> comptes = new ArrayList<Compte>();
        comptes.add(Compte.PARTS_PATRONALES_AVS);
        comptes.add(Compte.PARTS_PATRONALES_AC);
        if (Prestation.CONGE_PAYE.equals(prest)) {
            if (Convention.ELECTRICIEN.equals(convention)) {
                comptes.add(Compte.PARTS_PATRONALES_AF);
                comptes.add(Compte.PARTS_PATRONALES_CM);
                comptes.add(Compte.PARTS_PATRONALES_FCFP);
                comptes.add(Compte.PARTS_PATRONALES_LPP);
                comptes.add(Compte.PARTS_PATRONALES_RET);
            } else {
                comptes.clear();
            }
        }

        for (Compte compte : comptes) {
            try {
                csRefRubrique = RubriqueUtil.findReferenceRubriqueFor(prest, convention, compte);
                APIRubrique rubriquePartCot = null;
                rubriquePartCot = RubriqueUtil.retrieveRubriqueForReference(getSession(), csRefRubrique);
                idsRubriquesPartPatronales.add(rubriquePartCot.getIdRubrique());
            } catch (Exception e) {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
            }
        }
        return idsRubriquesPartPatronales;
    }

    /**
     * @param afact
     * @param montantFacture
     * @return
     */
    private Montant treatBeneficiaire(FAAfact afact, Montant montantFacture) {
        Montant totalVersement = Montant.ZERO;

        if (afact.getReferenceExterne() != null && afact.getReferenceExterne().length() != 0) {
            if (!Beneficiaire.NOTE_CREDIT.equals(parseBeneficiaire(afact.getReferenceExterne()))) {

                ArrayList<String> idRubriquePartCotCharge = retrieveIdsRubriquePartCotCharge(
                        parseConvention(afact.getReferenceExterne()), afact.getIdModuleFacturation());
                if (idRubriquePartCotCharge.contains(afact.getIdRubrique())) {
                    totalVersement = totalVersement.add(treatMontantFacture(
                            parseBeneficiaire(afact.getReferenceExterne()), montantFacture));
                } else {
                    return totalVersement.add(montantFacture);
                }
            }
        }
        return totalVersement;
    }

    /**
     * @param refereneceExterne
     * @return
     */
    private Beneficiaire parseBeneficiaire(String refereneceExterne) {
        if (refereneceExterne != null && refereneceExterne.length() != 0) {
            String[] codeBeneficiaire = refereneceExterne.split("-");
            if (codeBeneficiaire.length > 0 && codeBeneficiaire[0].length() > 0) {
                return Beneficiaire.fromValue(codeBeneficiaire[0]);
            }
        }
        return null;
    }

    /**
     * @param refereneceExterne
     * @return
     */
    private Convention parseConvention(String refereneceExterne) {
        if (refereneceExterne != null && refereneceExterne.length() != 0) {
            String[] codeBeneficiaire = refereneceExterne.split("-");
            if (codeBeneficiaire.length > 1 && codeBeneficiaire[1].length() > 0) {
                return Convention.fromValue(codeBeneficiaire[1]);
            }
        }
        return null;
    }

    /**
     * @param beneficiaire
     * @param montantFacture
     * @return
     */
    private Montant treatMontantFacture(Beneficiaire beneficiaire, Montant montantFacture) {
        Montant totalVersement = Montant.ZERO;
        if (Beneficiaire.EMPLOYEUR.equals(beneficiaire)) {
            return totalVersement.add(montantFacture);
        } else if (Beneficiaire.TRAVAILLEUR.equals(beneficiaire)) {
            return totalVersement.substract(montantFacture);
        }
        return totalVersement;
    }

    private FAAfactManager initAfactManager(FAEnteteFacture entFacture) {
        FAAfactManager afactManager = new FAAfactManager();
        afactManager.setSession(getSession());
        afactManager.setForIdPassage(getPassage().getIdPassage());
        afactManager.setForIdEnteteFacture(entFacture.getIdEntete());
        afactManager.setForAQuittancer(new Boolean(false));
        afactManager.setForNonComptabilisable("false");
        afactManager.setOrderBy(" IDENTETEFACTURE ASC, IDAFACT ASC ");
        return afactManager;
    }

    private String computeLibelleEcriture(String libelleCompta) {
        if (libelleCompta.length() > 39) {
            return libelleCompta.substring(0, 39);
        } else {
            return libelleCompta;
        }
    }

    private String computeCodeDebitCredit(Montant montant) {
        if (montant.isPositive()) {
            return APIEcriture.DEBIT;
        } else {
            return APIEcriture.CREDIT;
        }
    }
}
