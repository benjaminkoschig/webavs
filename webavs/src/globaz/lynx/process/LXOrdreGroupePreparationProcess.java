package globaz.lynx.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.codetva.LXCodeTva;
import globaz.lynx.db.codetva.LXCodeTvaManager;
import globaz.lynx.db.informationcomptable.LXInformationComptable;
import globaz.lynx.db.informationcomptable.LXInformationComptableManager;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.ordregroupe.LXOrdreGroupe;
import globaz.lynx.db.organeexecution.LXOrganeExecution;
import globaz.lynx.db.preparation.LXPreparationFactureManager;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.db.ventilation.LXVentilation;
import globaz.lynx.db.ventilation.LXVentilationManager;
import globaz.lynx.process.utils.LXHeliosUtils;
import globaz.lynx.utils.LXFournisseurUtil;
import globaz.lynx.utils.LXNoteDeCreditUtil;
import globaz.lynx.utils.LXSectionUtil;
import globaz.lynx.utils.LXSocieteDebitriceUtil;
import globaz.osiris.process.journal.CAUtilsJournal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Process de préparation de l'ordre groupé
 * 
 * @author SCO
 */
public class LXOrdreGroupePreparationProcess extends LXOrdreGroupeProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur LXOrdreGroupePreparationProcess.
     */
    public LXOrdreGroupePreparationProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur LXOrdreGroupePreparationProcess.
     * 
     * @param parent
     *            BProcess
     */
    public LXOrdreGroupePreparationProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur LXOrdreGroupePreparationProcess.
     * 
     * @param session
     *            BSession
     */
    public LXOrdreGroupePreparationProcess(BSession session) {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            if (isAborted()) {
                return false;
            }

            if (!LXOrdreGroupe.CS_ETAT_TRAITEMENT.equals(getOrdreGroupe().getCsEtat())) {
                updateEtatOrdreGroupe(LXOrdreGroupe.CS_ETAT_TRAITEMENT);
            }

            if (isAborted()) {
                return false;
            }

            if (executePreparation()) {
                updateEtatOrdreGroupe(LXOrdreGroupe.CS_ETAT_PREPARE);
                addMailInformations();
            } else {
                this._addError(getTransaction(), getSession().getLabel("AUCUN_PAIEMENT_CREE"));
            }
        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("PREPARATION_ERREUR"));
            this._addError(getTransaction(), e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * @see BProcess#_validate() throws Exception
     */
    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
            return;
        }

        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_ORDREGROUPE"));
            return;
        }

        if (!LXOrdreGroupe.CS_ETAT_TRAITEMENT.equals(getOrdreGroupe().getCsEtat())
                && !LXOrdreGroupe.CS_ETAT_OUVERT.equals(getOrdreGroupe().getCsEtat())) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_ORDREGROUPE_ETAT"));
            return;
        }

        if (JadeStringUtil.isBlank(getOrdreGroupe().getDateEcheance())) {
            this._addError(getTransaction(), getSession().getLabel("VAL_DATE_ECHEANCE"));
            return;
        }

        if (!new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), getTransaction(), getOrdreGroupe()
                .getDateEcheance(), getSociete().getIdMandat())) {
            return;
        }

        if (JadeStringUtil.isIntegerEmpty(getOrganeExecution().getIdCompteCredit())) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_ORGANEEXECUTION_COMPTE"));
            return;
        }
    }

    /**
     * Ajoute des informations dans l'email.
     */
    private void addMailInformations() throws Exception {
        getMemoryLog().logMessage(getSession().getLabel("TOTAL_PAIEMENTS") + "" + getOrdreGroupe().getTotalPaiement(),
                FWMessage.INFORMATION, this.getClass().getName());
        getMemoryLog().logMessage(getSession().getLabel("TOTAL_ESCOMPTES") + "" + getOrdreGroupe().getTotalEscompte(),
                FWMessage.INFORMATION, this.getClass().getName());
    }

    /**
     * Créer l'escompte et return l'id escompte afin de liée (idOperationLiee) le paiement
     * 
     * @param facture
     * @throws Exception
     * @return idEscompte
     */
    private String creationEscompte(LXOperation facture, String montant) throws Exception {

        // Contrôle qu'il existe un compte d'escompte
        String idCompteEscompte = getIdCompteEscompte(facture);

        LXOperation opeClone = (LXOperation) facture.clone();
        opeClone.setIdOperationSrc(facture.getIdOperation());
        opeClone.setIdOperation(null);
        opeClone.setIdJournal("0");
        opeClone.setIdOrdreGroupe(getIdOrdreGroupe());
        opeClone.setCsEtatOperation(LXOperation.CS_ETAT_OUVERT);
        opeClone.setDateEcheance("0");
        opeClone.setDateOperation(facture.getDateEcheance());
        opeClone.setIdOrganeExecution(getIdOrganeExecution());
        opeClone.setMontant(montant);

        String libelle = opeClone.getLibelle();
        String libelleEscompte = " (" + getSession().getLabel("IMP_ESCOMPTE") + ")";

        if (libelle.length() > (40 - libelleEscompte.length())) { // Taille du champs max en base = 40
            libelle = opeClone.getLibelle().substring(0, opeClone.getLibelle().length() - libelleEscompte.length());
        }
        opeClone.setLibelle(libelle + libelleEscompte);

        // Changement du type
        opeClone.setCsTypeOperation(LXOperation.CS_TYPE_ESCOMPTE);
        opeClone.add(getTransaction());

        if (opeClone.hasErrors()) {
            throw new Exception(opeClone.getErrors().toString());
        }

        if (opeClone.isNew()) {
            throw new Exception(getSession().getLabel("ESCOMPTE_NON_CREE"));
        }

        // Faire le calcul de la tva sur l'escompte
        FWCurrency montantCredit = new FWCurrency(montant);
        FWCurrency montantTva = new FWCurrency();
        if (!JadeStringUtil.isBlankOrZero(facture.getCsCodeTVA())) {
            montantTva = getMontantTva(montant, facture.getCsCodeTVA(), facture.getDateOperation());
            montantCredit.sub(montantTva); // On retire la tva du credit de
            // l'escompte
        }

        // somme des montants au crédit sur le compte d'escompte
        creationVentilations(montant, opeClone.getIdOperationSrc(), opeClone.getIdOperation(), idCompteEscompte,
                montantCredit.toString(), facture.getMontant(), facture.getIdSection());

        // SI on a de la tva, on crée une ventilation au credit de la valeur de
        // la tva
        if (!JadeStringUtil.isBlankOrZero(facture.getCsCodeTVA())) {
            String idCompteTva = getIdCompteTva(facture);
            if (montantTva.isNegative()) {
                montantTva.negate();
            }
            creationVentilation(opeClone.getIdOperation(), idCompteTva, CodeSystem.CS_CREDIT, montantTva.toString());
        }

        // TODO OPTIMISATION : Possible d'eviter la méthode isCreditEgalDebit ->
        // reduction du nombre de transaction vers le serveur
        if (!isCreditEgalDebit(opeClone.getIdOperation())) {
            getMemoryLog().logMessage(
                    getSession().getLabel("VENTILATION_PAIEMENT_NON_EQUILIBRE") + "("
                            + getNomFournisseur(facture.getIdSection()) + ", " + montant + ")",
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        return opeClone.getIdOperation();
    }

    // *******************************************************
    // Outils
    // *******************************************************

    /**
     * Permet la création d'un paiement
     * 
     * @param facture
     * @throws Exception
     */
    private int creationPaiement(LXOperation facture) throws Exception {
        int indexPaiement = 1;

        LXOperation paiement = (LXOperation) facture.clone();

        setMontantPmtAndCreateEscompte(paiement);

        if (new FWCurrency(paiement.getMontant()).isZero()) {
            // Si le montant est égal à zéro, il ni a pas besoin de créer un
            // paiement
            return 0;
        }

        // Incrementation du libellé
        if (!JadeStringUtil.isIntegerEmpty(facture.getCountPmt())) {
            indexPaiement = Integer.parseInt(facture.getCountPmt()) + 1;

            if (paiement.getLibelle().length() > 36) { // Taille du champs en base = 40, donc on coupe a 36 pour y
                                                       // placer le compteur
                paiement.setLibelle(paiement.getLibelle().substring(0, paiement.getLibelle().length() - 4) + " #"
                        + indexPaiement);
            } else {
                paiement.setLibelle(paiement.getLibelle() + " #" + indexPaiement);
            }
        }
        paiement.setIdOperationSrc(facture.getIdOperation());
        paiement.setIdOperation(null);
        paiement.setIdJournal("0");
        paiement.setIdOrdreGroupe(getIdOrdreGroupe());
        paiement.setCsEtatOperation(LXOperation.CS_ETAT_OUVERT);
        paiement.setDateEcheance("0");
        // Mettre la date de paiement forcée
        if (!JadeStringUtil.isBlank(getOrdreGroupe().getDatePaiement())) {
            paiement.setDateOperation(getOrdreGroupe().getDatePaiement());
        } else {
            paiement.setDateOperation(facture.getDateEcheance());
        }
        paiement.setIdOrganeExecution(getIdOrganeExecution());

        // Changement du type
        if (LXOperation.CS_TYPE_FACTURE_BVR_ORANGE.equals(facture.getCsTypeOperation())) {
            paiement.setCsTypeOperation(LXOperation.CS_TYPE_PAIEMENT_BVR_ORANGE);
        } else if (LXOperation.CS_TYPE_FACTURE_BVR_ROUGE.equals(facture.getCsTypeOperation())) {
            paiement.setCsTypeOperation(LXOperation.CS_TYPE_PAIEMENT_BVR_ROUGE);
        } else if (LXOperation.CS_TYPE_FACTURE_CAISSE.equals(facture.getCsTypeOperation())) {
            paiement.setCsTypeOperation(LXOperation.CS_TYPE_PAIEMENT_CAISSE);
        } else if (LXOperation.CS_TYPE_FACTURE_LSV.equals(facture.getCsTypeOperation())) {
            paiement.setCsTypeOperation(LXOperation.CS_TYPE_PAIEMENT_LSV);
        } else if (LXOperation.CS_TYPE_FACTURE_VIREMENT.equals(facture.getCsTypeOperation())) {
            paiement.setCsTypeOperation(LXOperation.CS_TYPE_PAIEMENT_VIREMENT);
        }

        paiement.add(getTransaction());

        if (paiement.hasErrors()) {
            throw new Exception(paiement.getErrors().toString());
        }

        if (paiement.isNew()) {
            throw new Exception(getSession().getLabel("PAIEMENT_NON_CREE"));
        }

        // Changement des ventilations
        // FWCurrency m = new FWCurrency(facture.getMontant());
        // m.negate();
        creationVentilations(paiement.getMontant(), paiement.getIdOperationSrc(), paiement.getIdOperation(),
                getOrganeExecution().getIdCompteCredit(), paiement.getMontant(), facture.getMontant(),
                facture.getIdSection());

        updateLinkEscompte(paiement);

        return indexPaiement;
    }

    /**
     * Permet la création d'une ventilation
     * 
     * @param idOperation
     * @param idCompte
     * @param codeDebitCredit
     * @param montant
     * @return
     * @throws Exception
     */
    private LXVentilation creationVentilation(String idOperation, String idCompte, String codeDebitCredit,
            String montant) throws Exception {

        LXVentilation ventilation = new LXVentilation();

        ventilation.setIdOperation(idOperation);
        ventilation.setIdCompte(idCompte);
        ventilation.setCodeDebitCredit(codeDebitCredit);
        ventilation.setMontant(montant);
        ventilation.add(getTransaction());

        if (ventilation.hasErrors()) {
            throw new Exception(ventilation.getErrors().toString());
        }

        if (ventilation.isNew()) {
            throw new Exception(getSession().getLabel("PREPARATION_VENTILATION_NON_CREE"));
        }

        return ventilation;
    }

    /**
     * Creation des ventilations du paiement<br>
     * - Les credits deviennent des debits<br>
     * - Un crédit est crée pour le compte de l'organe d'execution du montant du paiement<br>
     * 
     * @param forIdOperation
     * @param idNewOperation
     * @param idSection
     * @throws Exception
     */
    private void creationVentilations(String montant, String forIdOperation, String idNewOperation,
            String idCompteCredit, String montantCredit, String montantFactureSource, String idSection)
            throws Exception {
        // clone uniquement des crédits vers des débits
        // Le total des clones sera appliqué au crédit du compte défini dans
        // l'ordre groupé via l'organe d'exécution
        LXVentilationManager ventManager = new LXVentilationManager();
        ventManager.setSession(getSession());
        ventManager.setForIdOperation(forIdOperation);
        ventManager.setForCodeDebitCredit(CodeSystem.CS_CREDIT);
        ventManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        FWCurrency totalDebit = new FWCurrency();

        for (int i = 0; i < ventManager.size(); i++) {
            LXVentilation vent = (LXVentilation) ventManager.get(i);

            LXVentilation ventClone = (LXVentilation) vent.clone();
            ventClone.setIdVentilation(null);
            ventClone.setIdOperation(idNewOperation);

            if (isComptePassif(ventClone.getIdCompte())) {
                BigDecimal tmp = new BigDecimal(ventClone.getMontant());
                BigDecimal tmpFacture = new BigDecimal(montantFactureSource);
                if (tmp.compareTo(tmpFacture) != 0) {
                    tmp = tmp.divide(tmpFacture, 5, BigDecimal.ROUND_HALF_EVEN);
                    tmp = tmp.multiply(new BigDecimal(montant));
                } else {
                    tmp = new BigDecimal(montant);
                }

                tmp = tmp.negate();
                ventClone.setMontant(tmp.toString());

                ventClone.setCodeDebitCredit(CodeSystem.CS_DEBIT);
                ventClone.add(getTransaction());

                if (ventClone.hasErrors()) {
                    throw new Exception(ventClone.getErrors().toString());
                }

                if (ventClone.isNew()) {
                    throw new Exception(getSession().getLabel("PREPARATION_VENTILATION_NON_CREE"));
                }

                totalDebit.add(ventClone.getMontant());
            }
        }

        if (totalDebit.isZero()) {
            throw new Exception(getSession().getLabel("AUCUN_COMPTE_PASSIF"));
        }

        // Création du crédit
        BigDecimal tmp = new BigDecimal(montantCredit);
        tmp = tmp.negate();

        creationVentilation(idNewOperation, idCompteCredit, CodeSystem.CS_CREDIT, tmp.toString());
    }

    /**
     * Créer les paiements à partir des factures en tenant compte des escomptes, extournes et notes de crédit. Les
     * factures traitées (y compris celles extournées) sont mises à l'état "Préparé" afin de ne plus être repris dans un
     * autre OG.
     * 
     * @throws Exception
     * @return True si au moins une facture candidate a été résolue
     */
    private boolean executePreparation() throws Exception {
        BTransaction readTransaction = null;
        int countFacture = 0;

        try {
            readTransaction = (BTransaction) getSession().newTransaction();
            readTransaction.openTransaction();

            // -----------------------------------------------------
            // Faire la liste des opérations qui ont :
            // - Date échéance <= date d'échéance de l'ordre groupé
            // - Etat de l'opération = COMPTABILISE
            // - Type d'operation : facture
            // - ORgane execution = organe execution de l'ordre groupe || ordre
            // groupe = 0
            // - Que la facture n'a pas bloqué les paiements
            // -----------------------------------------------------
            LXPreparationFactureManager factureManager = getListeFactures(readTransaction);

            setProgressScaleValue(factureManager.size() + (int) (factureManager.size() * 0.20));
            countFacture = factureManager.size();

            // -----------------------------------------------------
            // Pour chaque operation
            // - Operation courante
            // | - Change etat "PREPARER"
            // | + UPDATE
            // - Clone de l'operation
            // | - idJournal = null
            // | - idOrdreGroupe = idOrdreGroupe courant
            // | - csEtat = "OUVERT"
            // | - idOperationLiee = idOperation
            // | - idOperation = null
            // | - Inverse le montant
            // | - csTypeOperation = CS_TYPE_PAIEMENT_.... (suivant le type de
            // facture de l'operation d'origine)
            // | - Changement des ventilations (inverser les ventilations)
            // | + ADD
            // -----------------------------------------------------
            for (int i = 0; (i < factureManager.size()) && !isAborted(); i++) {
                LXOperation facture = (LXOperation) factureManager.get(i);

                int nombrePmt = creationPaiement(facture);
                // Les cas traités sont mis à "Préparé" pour ne pas être repris dans un prochain traitement.
                updateEtatFacturePrepare(facture, nombrePmt);

                if (getTransaction().hasErrors() || getSession().hasErrors()) {
                    getTransaction().rollback();
                    throw new Exception(getSession().getLabel("PAIEMENT_NON_CREE"));
                } else {
                    getTransaction().commit();
                }

                incProgressCounter();
            }
        } catch (Exception e) {
            getTransaction().rollback();
            throw e;
        } finally {
            if (readTransaction != null) {
                try {
                    readTransaction.rollback();
                } finally {
                    readTransaction.closeTransaction();
                }
            }
        }

        return countFacture > 0;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("PREPARATION_ERREUR");
        } else {
            return getSession().getLabel("PREPARATION_OK");
        }
    }

    /**
     * Récupérer le compte d'escompte si null erreur
     * 
     * @param facture
     * @throws Exception
     */
    private String getIdCompteEscompte(LXOperation facture) throws Exception {

        LXInformationComptableManager managerInfo = getInformationComptableManager(facture);

        LXInformationComptable infoCmp = (LXInformationComptable) managerInfo.getFirstEntity();

        if (JadeStringUtil.isBlankOrZero(infoCmp.getIdCompteEscompte())) {
            throw new Exception(getMessageErreurCompteEscompte(infoCmp, facture.getIdOperation()));
        }

        return infoCmp.getIdCompteEscompte();
    }

    /**
     * Récupérer le compte de TVA si null erreur
     * 
     * @param facture
     * @throws Exception
     */
    private String getIdCompteTva(LXOperation facture) throws Exception {

        LXInformationComptableManager managerInfo = getInformationComptableManager(facture);

        LXInformationComptable infoCmp = (LXInformationComptable) managerInfo.getFirstEntity();

        if (JadeStringUtil.isBlankOrZero(infoCmp.getIdCompteTva())) {
            throw new Exception(getSession().getLabel("COMPTE_TVA_PAS_RENSEIGNE"));
        }

        return infoCmp.getIdCompteEscompte();
    }

    /**
     * PErmet la récupération du manager des infos comptable du fournisseur et de la société
     * 
     * @param facture
     * @return
     * @throws Exception
     */
    private LXInformationComptableManager getInformationComptableManager(LXOperation facture) throws Exception {
        LXSection section = LXSectionUtil.getSection(getSession(), getTransaction(), facture.getIdSection());

        LXInformationComptableManager managerInfo = new LXInformationComptableManager();
        managerInfo.setSession(getSession());
        managerInfo.setForIdFournisseur(section.getIdFournisseur());
        managerInfo.setForIdSociete(getOrdreGroupe().getIdSociete());
        managerInfo.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (getTransaction().hasErrors()) {
            throw new Exception(getTransaction().getErrors().toString());
        }

        if (managerInfo.hasErrors()) {
            throw new Exception(managerInfo.getErrors().toString());
        }

        if (managerInfo.size() != 1) {
            throw new Exception(getSession().getLabel("ERREUR_INFO_COMPTABLE") + getSociete().getNom() + " / "
                    + LXFournisseurUtil.getLibelleNomComplet(getSession(), section.getIdSection()));
        }

        return managerInfo;
    }

    /**
     * Recherche la liste des factures<br>
     * - de type : Facture<br>
     * - date d'echeance inferieur a la date d'echeance de l'ordre groupé<br>
     * - de type d'operation : Comptabilisé<br>
     * - dont les paiements sont pas bloqués<br>
     * 
     * @param transaction
     */
    private LXPreparationFactureManager getListeFactures(BTransaction transaction) throws Exception {
        LXPreparationFactureManager opeManager = new LXPreparationFactureManager();
        opeManager.setSession(getSession());

        // opeManager.setForDateFactureInferieure(this.getOrdreGroupe().getDateEcheance());
        opeManager.setForDateEcheanceInferieure(getOrdreGroupe().getDateEcheance());
        opeManager.setIsBloque(Boolean.FALSE);
        opeManager.setFounissseurIsBloque(Boolean.FALSE);

        ArrayList<String> forCsEtatIn = new ArrayList<String>();
        forCsEtatIn.add(LXOperation.CS_ETAT_COMPTABILISE);

        opeManager.setForCsEtatIn(forCsEtatIn);

        ArrayList<String> forCsTypeOperationIn = new ArrayList<String>();

        if (LXOrganeExecution.CS_GENRE_CAISSE.equals(getOrganeExecution().getCsGenre())) {
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_FACTURE_CAISSE);
        } else if (LXOrganeExecution.CS_GENRE_LSV.equals(getOrganeExecution().getCsGenre())) {
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_FACTURE_LSV);
        } else {
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_FACTURE_BVR_ORANGE);
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_FACTURE_BVR_ROUGE);
            forCsTypeOperationIn.add(LXOperation.CS_TYPE_FACTURE_VIREMENT);
        }

        opeManager.setForCsTypeOperationIn(forCsTypeOperationIn);

        opeManager.setForIdOrganeExecutionOrVide(getIdOrganeExecution());
        opeManager.setForIdSociete(getIdSociete());

        opeManager.find(transaction, BManager.SIZE_NOLIMIT);

        return opeManager;
    }

    /**
     * Return la liste des notes de crédits attachées à la facture.
     * 
     * @param facture
     * @param forEtat
     * @return
     * @throws Exception
     */
    private LXOperationManager getListeNotesDeCredit(LXOperation facture, String forEtat) throws Exception {
        ArrayList<String> forIdTypeOperationIn = new ArrayList<String>();
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);
        ArrayList<String> forCsEtatIn = new ArrayList<String>();
        forCsEtatIn.add(forEtat);

        LXOperationManager opeManager = new LXOperationManager();
        opeManager.setSession(getSession());
        opeManager.setForIdTypeOperationIn(forIdTypeOperationIn);
        opeManager.setForCsEtatIn(forCsEtatIn);

        opeManager.setForIdSection(facture.getIdSection());
        opeManager.setForIdOperationLiee(facture.getIdOperation());

        opeManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        return opeManager;
    }

    /**
     * Permet la création du message d'erreur pour l'abscence de compte escompte
     * 
     * @param infoCmp
     * @return
     * @throws Exception
     */
    private String getMessageErreurCompteEscompte(LXInformationComptable infoCmp, String idOperation) throws Exception {
        StringBuffer content = new StringBuffer();

        content.append(getSession().getLabel("COMPTE_ESCOMPTE_PAS_RENSEIGNE")).append(" (");
        content.append(getSession().getLabel("FACTURE")).append(" : ").append(idOperation).append(", ");
        content.append(getSession().getLabel("FOURNISSEUR")).append(" : ")
                .append(LXFournisseurUtil.getFournisseur(getSession(), infoCmp.getIdFournisseur()).getIdExterne())
                .append(", ");
        content.append(getSession().getLabel("SOCIETE_DEBITRICE"))
                .append(" : ")
                .append(LXSocieteDebitriceUtil.getSocieteDebitrice(getSession(), infoCmp.getIdSociete()).getIdExterne())
                .append(")");

        return content.toString();
    }

    /**
     * Si le taux d'escompte est renseigné, calculé le montant de l'escompte a créé.
     * 
     * @param facture
     * @return Un montant négatif.
     */
    private FWCurrency getMontantEscompte(LXOperation facture) {
        BigDecimal tmp = new BigDecimal(facture.getMontant());
        tmp = tmp.multiply(new BigDecimal(facture.getTauxEscompte()));
        tmp = tmp.divide(new BigDecimal(LXOrdreGroupeProcess.CENT_POUR_CENT), 5, BigDecimal.ROUND_HALF_EVEN);

        FWCurrency montant = new FWCurrency(tmp.toString());
        montant.negate();

        return montant;
    }

    /**
     * Permet de calculer la TVA sur l'escompte
     * 
     * @param montant
     * @param csCodeTva
     * @return
     * @throws Exception
     */
    private FWCurrency getMontantTva(String montant, String csCodeTva, String date) throws Exception {
        // montantTVA = montant - (montant * 100 / (100 +
        // parseFloat(tvaSelected.taux)));

        String tauxTva = getTauxTva(csCodeTva, date);

        BigDecimal calcul = new BigDecimal(tauxTva);
        calcul = calcul.add(new BigDecimal(LXOrdreGroupeProcess.CENT_POUR_CENT));

        BigDecimal montantNet = new BigDecimal(montant);
        montantNet = montantNet.multiply(new BigDecimal(LXOrdreGroupeProcess.CENT_POUR_CENT));

        montantNet = montantNet.divide(calcul, 2, BigDecimal.ROUND_HALF_EVEN);

        FWCurrency tva = new FWCurrency(montant);
        tva.sub(montantNet.doubleValue());

        return tva;

    }

    /**
     * Return le nom du fournisseur.
     * 
     * @param idSection
     * @return
     * @throws Exception
     */
    private String getNomFournisseur(String idSection) throws Exception {
        return LXFournisseurUtil.getLibelleNomComplet(getSession(),
                LXSectionUtil.getSection(getSession(), getTransaction(), idSection).getIdSection());
    }

    /**
     * Return le total des montants et paiements déjà liés à la facture de base au cas ou cette dernière serait payée en
     * plusieurs fois.
     * 
     * @param facture
     * @return
     * @throws Exception
     */
    private FWCurrency getPreviousMontantEscomptePaiement(LXOperation facture) throws Exception {
        LXOperationManager manager = new LXOperationManager();
        manager.setSession(getSession());

        manager.setForIdOperationSrc(facture.getIdOperation());

        ArrayList<String> listeEtat = new ArrayList<String>();
        listeEtat.add(LXOperation.CS_ETAT_COMPTABILISE);
        listeEtat.add(LXOperation.CS_ETAT_OUVERT);
        manager.setForCsEtatIn(listeEtat);

        return new FWCurrency(manager.getSum(LXOperation.FIELD_MONTANT).toString());
    }

    /**
     * Retourne le montant total des notes de crédits liés à la facture et déjà prise en compte lors du premier
     * paiement.
     * 
     * @param facture
     * @return
     * @throws Exception
     */
    private FWCurrency getPreviousMontantNoteDeCredit(LXOperation facture) throws Exception {
        LXOperationManager opeManager = getListeNotesDeCredit(facture, LXOperation.CS_ETAT_PREPARE);
        FWCurrency previousNoteDeCredit = new FWCurrency(opeManager.getSum(LXOperation.FIELD_MONTANT).toString());

        return previousNoteDeCredit;
    }

    /**
     * @param facture
     * @return la somme des notes de crédit liées à la facture.
     * @throws Exception
     */
    private FWCurrency getSommeNotesCredit(LXOperation facture) throws Exception {
        FWCurrency sommeNote = new FWCurrency();

        LXOperationManager opeManager = getListeNotesDeCredit(facture, LXOperation.CS_ETAT_COMPTABILISE);

        if (opeManager.size() > 0) {
            Iterator<LXOperation> it = opeManager.iterator();
            while (it.hasNext()) {
                LXOperation note = it.next();
                sommeNote.add(note.getMontant());

                updateEtatNoteDeCreditPrepare(note);
            }
        }
        return sommeNote;
    }

    /**
     * Récupérer le taux de tva suivant son code
     * 
     * @param facture
     * @throws Exception
     */
    private String getTauxTva(String csCodeTva, String date) throws Exception {

        LXCodeTvaManager tvaManager = new LXCodeTvaManager();
        tvaManager.setSession(getSession());
        tvaManager.setForCsCodeTVA(csCodeTva);
        tvaManager.setForDateBetween(date);
        tvaManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (getTransaction().hasErrors()) {
            throw new Exception(getTransaction().getErrors().toString());
        }

        if (tvaManager.hasErrors()) {
            throw new Exception(tvaManager.getErrors().toString());
        }

        if (tvaManager.size() != 1) {
            throw new Exception(getSession().getLabel("ERREUR_RECUP_TAUX_TVA"));
        }

        return ((LXCodeTva) tvaManager.getFirstEntity()).getTaux();
    }

    /**
     * Le compte est-il de type passif ?
     * 
     * @param idCompteCredit
     * @throws Exception
     */
    private boolean isComptePassif(String idCompteCredit) throws Exception {
        return LXHeliosUtils.getInstance().isComptePassif(getSession(), getTransaction(), getSociete().getIdMandat(),
                getOrdreGroupe().getDateEcheance(), idCompteCredit);
    }

    /**
     * Permet la comparaison des credit avec les debits.<br>
     * return vrai si egual et faux sinon
     * 
     * @param idOperation
     * @return
     * @throws Exception
     */
    private boolean isCreditEgalDebit(String idOperation) throws Exception {

        boolean egal = false;

        FWCurrency credit = new FWCurrency();
        FWCurrency debit = new FWCurrency();

        LXVentilationManager manager = new LXVentilationManager();
        manager.setSession(getSession());
        manager.setForIdOperation(idOperation);
        manager.find(getTransaction());

        for (int i = 0; i < manager.size(); i++) {
            LXVentilation vent = (LXVentilation) manager.getEntity(i);

            if (CodeSystem.CS_CREDIT.equals(vent.getCodeDebitCredit())) {
                credit.add(vent.getMontant());
            } else {
                debit.add(vent.getMontant());
            }
        }

        if (credit.compareTo(debit) == 0) {
            egal = true;
        }

        return egal;
    }

    /**
     * - On déduit les notes de crédit au montant de l'opération clonée. <br/>
     * - On déduit l'extourne - On calcul, crée et déduit l'escompte s'il y en a un. <br/>
     * - On inverse le signe du résultat. <br/>
     * 
     * @param facture
     *            clonnée
     * @throws Exception
     */
    private void setMontantPmtAndCreateEscompte(LXOperation facture) throws Exception {
        FWCurrency montantPaiement = new FWCurrency();
        montantPaiement.add(facture.getMontant());

        FWCurrency previousMontant = getPreviousMontantEscomptePaiement(facture);

        // Le calcul de l'escompte n'est effectué qu'une fois, lors du premier
        // paiement
        if (previousMontant.isZero()) {
            if (montantPaiement.isPositive() && !JadeStringUtil.isBlankOrZero(facture.getTauxEscompte())) {
                FWCurrency montant = getMontantEscompte(facture);

                String idEscompte = creationEscompte(facture, montant.toString());

                montantPaiement.add(montant);

                facture.setIdOperationLiee(idEscompte);
            }
        } else if (previousMontant.isNegative()) {
            montantPaiement.add(previousMontant);

            FWCurrency previousNoteDeCredit = getPreviousMontantNoteDeCredit(facture);

            if (previousNoteDeCredit.isNegative()) {
                montantPaiement.add(previousNoteDeCredit);
            }
        }

        FWCurrency saveMontantFactureMoinsEscompte = new FWCurrency(montantPaiement.toString());
        montantPaiement.add(getSommeNotesCredit(facture));

        if (montantPaiement.isNegative()) {
            updateMontantNotesCredit(facture, saveMontantFactureMoinsEscompte);

            // Remise à zéro pour ne pas créer de paiement.
            montantPaiement = new FWCurrency();
        } else if (montantPaiement.isPositive()) {
            montantPaiement.negate();
        }

        facture.setMontant(montantPaiement.toString());
    }

    /**
     * Mise a jour de l'operation<br>
     * - Passage a l'etat : PREPARE<br>
     * - Id ordre group : Sauvegarde de l'ordre groupé qui à mis à jour la facture. - countPmt : Mise a jour du nombre
     * de paiement de la facture
     * 
     * @param facture
     *            La facture à modifier
     * @param
     * @throws Exception
     */
    private void updateEtatFacturePrepare(LXOperation facture, int nombrePmt) throws Exception {
        // mise a jour de l'operation
        facture.setCsEtatOperation(LXOperation.CS_ETAT_PREPARE);
        facture.setIdOrdreGroupe(getIdOrdreGroupe());
        // Mise a jour du nombre de paiement
        facture.setCountPmt(Integer.toString(nombrePmt));

        facture.update(getTransaction());
    }

    /**
     * Mise à jour de l'état des notes de crédits liées à la facture.
     * 
     * @param note
     * @throws Exception
     */
    private void updateEtatNoteDeCreditPrepare(LXOperation note) throws Exception {
        note.setCsEtatOperation(LXOperation.CS_ETAT_PREPARE);
        note.setIdOrdreGroupe(getIdOrdreGroupe());
        note.update(getTransaction());

        LXOperation ndcLiee = LXNoteDeCreditUtil.getNoteCreditLieeSurSectionLiee(getSession(), getTransaction(),
                note.getIdOperationSrc(), note.getIdOperationLiee(), note.getIdSection());
        ndcLiee.setCsEtatOperation(LXOperation.CS_ETAT_PREPARE);
        ndcLiee.setIdOrdreGroupe(getIdOrdreGroupe());
        ndcLiee.update(getTransaction());
    }

    /**
     * Mise à jour de l'idOperationLiee de l'escompte afin de la faire pointée sur le paiement.
     * 
     * @param paiement
     * @throws Exception
     */
    private void updateLinkEscompte(LXOperation paiement) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(paiement.getIdOperationLiee())) {
            LXOperation escompte = new LXOperation();
            escompte.setSession(getSession());

            escompte.setIdOperation(paiement.getIdOperationLiee());

            escompte.retrieve(getTransaction());

            if (escompte.hasErrors()) {
                throw new Exception(escompte.getErrors().toString());
            }

            if (escompte.isNew()) {
                throw new Exception(getSession().getLabel("ESCOMPTE_NON_RESOLUE"));
            }

            escompte.setIdOperationLiee(paiement.getIdOperation());
            escompte.update(getTransaction());
        }
    }

    /**
     * Mise à jour des montants des notes de crédit si leur cumul est supérieur au montant de la facture retranchée de
     * son escompte. <br/>
     * Source :<br/>
     * Facture 100 CHF à 10% d'escompte<br/>
     * Note de crédit 95 CHF <br/>
     * <br/>
     * Calcul :<br/>
     * 100<br/>
     * - 10 (Escompte)<br/>
     * ----<br/>
     * 90<br/>
     * - 95 (Note de crédit liée)<br/>
     * ---<br/>
     * - 5<br/>
     * <br/>
     * => <br/>
     * Mise à jour note de crédit liée = 90 CHF et warning dans email 5 chf encore à liée<br/>
     * 
     * @param facture
     * @param montantMax
     * @throws Exception
     */
    private void updateMontantNotesCredit(LXOperation facture, FWCurrency montantMax) throws Exception {
        FWCurrency sommeNote = new FWCurrency();

        LXOperationManager opeManager = getListeNotesDeCredit(facture, LXOperation.CS_ETAT_PREPARE);

        if (opeManager.size() > 0) {
            Iterator<LXOperation> it = opeManager.iterator();
            while (it.hasNext()) {
                LXOperation note = it.next();
                FWCurrency montant = new FWCurrency(note.getMontant());
                montant.negate();
                sommeNote.add(montant);

                if (sommeNote.compareTo(montantMax) == 1) {
                    FWCurrency tmp = new FWCurrency(sommeNote.toString());
                    tmp.sub(montantMax);

                    montant.sub(tmp);

                    if (!montant.isPositive()) {
                        throw new Exception(getSession().getLabel("ERREUR_MISE_A_JOUR_NDC") + " / "
                                + getNomFournisseur(facture.getIdSection()));
                    }

                    montant.negate();
                    note.setMontant(montant.toString());
                    note.update(getTransaction());

                    LXOperation ndcLiee = LXNoteDeCreditUtil.getNoteCreditLieeSurSectionLiee(getSession(),
                            getTransaction(), note.getIdOperationSrc(), note.getIdOperationLiee(), note.getIdSection());
                    ndcLiee.setMontant(montant.toString());
                    ndcLiee.update(getTransaction());

                    getMemoryLog().logMessage(
                            getSession().getLabel("MONTANT_NDC_MODIFIE") + " / "
                                    + getNomFournisseur(facture.getIdSection()), FWMessage.AVERTISSEMENT,
                            this.getClass().getName());
                }
            }
        } else {
            throw new Exception(getSession().getLabel("MONTANT_INF_ZERO"));
        }
    }
}
