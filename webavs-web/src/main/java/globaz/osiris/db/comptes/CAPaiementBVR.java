package globaz.osiris.db.comptes;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.process.COSectionBulletinNeutreABloquerManager;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BConstants;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIPaiementBVR;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISlave;
import globaz.osiris.application.CAApplication;
import globaz.osiris.application.CAParametres;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.bulletinneutre.CAComptabiliserBulletinNeutre;
import globaz.osiris.db.contentieux.CADateExecutionSommationAquila;
import globaz.osiris.db.contentieux.CADateExecutionSommationAquilaManager;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.utils.CAUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.osiris.business.constantes.CAProperties;

/**
 * Date de création : (14.02.2002 10:06:48)
 * 
 * @author: Administrator
 */
public class CAPaiementBVR extends CAPaiement implements APIPaiementBVR {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DESC = " DESC";
    private static final String EXTOURNE_LIBELLE = "Extourne";
    private CAEcheancePlan echeance = null;
    private CATransactionBVR transactionBVR = new CATransactionBVR();

    public CAPaiementBVR() {
        super();
        setIdTypeOperation(APIOperation.CAPAIEMENTBVR);
    }

    public CAPaiementBVR(CAOperation parent) {
        super(parent);
        setIdTypeOperation(APIOperation.CAPAIEMENTBVR);
    }

    /**
     * @see globaz.osiris.db.comptes.CAEcriture#_afterActiver(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterActiver(BTransaction transaction) {
        super._afterActiver(transaction);

        if (isTaxeEstReporte()) {
            try {
                if (!CAProperties.MODE_AUTO_REPORT.getBooleanValue()) {
                    setSectionModeCompensation(transaction, APISection.MODE_REPORT);
                } else {
                    setSectionModeCompensation(transaction, APISection.MODE_COMPENSATION_STANDARD);
                }
            } catch (Exception e) {
                _addError(transaction, e.toString());
            }
        }
    }

    /**
     * Effectue des traitements après un ajout dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après l'ajout de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception en cas d'erreur fatale
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws java.lang.Exception {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        this._synchroValUtili(transaction);
        // Sauvegarder la transaction bvr
        if (!transaction.hasErrors()) {
            transactionBVR.setIdOperation(getIdOperation());
            transactionBVR.add(transaction);
            if (transaction.hasErrors()) {
                _addError(transaction, getSession().getLabel("7125"));
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // Laisser la superclasse effectuer son traitement
        super._afterRetrieve(transaction);
        initTransactionBVR(transaction);
    }

    /**
     * Effectue des traitements après une mise à jour dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après la mise à jour de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws java.lang.Exception {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        this._synchroValUtili(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // Laisser la superclasse s'initialiser
        super._beforeAdd(transaction);
        // Forcer le type d'opération
        setIdTypeOperation(APIOperation.CAPAIEMENTBVR);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        // Laisser la superclasse traiter l'événement
        super._beforeDelete(transaction);

        // Supprimer la transaction BVR
        if (!transaction.hasErrors()) {
            transactionBVR.delete(transaction);
            if (transaction.hasErrors()) {
                _addError(transaction, getSession().getLabel("7127"));
            }
        }
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_afterDesactiver(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDesactiver(BTransaction transaction) {
        super._beforeDesactiver(transaction);
        if (isTaxeEstReporte() && getSection().getIdModeCompensation().equals(APISection.MODE_REPORT)
                && JadeStringUtil.isBlankOrZero(getSection().getIdPassageComp())) {
            try {
                setSectionModeCompensation(transaction, APISection.MODE_COMPENSATION_STANDARD);
            } catch (Exception e) {
                _addError(transaction, e.toString());
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // Laisser la superclasse traiter l'événement
        super._beforeUpdate(transaction);

        // Mettre à jour la transaction BVR
        transactionBVR.update(transaction);
        if (transaction.hasErrors()) {
            _addError(transaction, getSession().getLabel("7125"));
        }
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_createExtourne(BTransaction, String)
     */
    @Override
    protected CAOperation _createExtourne(BTransaction transaction, String text) throws Exception {
        // Création d'un double de l'écriture
        CAPaiementBVR extourne = new CAPaiementBVR();
        extourne.dupliquer(this);
        // Libellé du texte si saisi
        if (!JadeStringUtil.isBlank(text) && (text.length() > 40)) {
            extourne.setLibelle(text.substring(0, 40));
        } else {
            extourne.setLibelle(text);
        }
        // Inverser le signe
        CAEcriture.inverserCodeDebitCredit(extourne);
        // Retourner l'opération
        return extourne;
    }

    /**
     * Chargement des valeurs par défaut par utilisateur
     * 
     * @param transaction
     */
    @Override
    public void _synchroChgValUtili() {
        if (isNew()) {
            if (!JadeStringUtil.isBlank(getNomEcran()) && (valeurUtilisateur == null)) {
                valeurUtilisateur = new java.util.Vector<String>(18);
                // lecture du fichier
                globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
                valUtili.setSession(getSession());
                valeurUtilisateur = valUtili.retrieveValeur("CAPaiementBVR", getNomEcran());
                // chargement des propriétés internes si idExterneRoleEcran est vide

                if (JadeStringUtil.isBlank(getIdExterneRoleEcran()) && !valeurUtilisateur.isEmpty()) {
                    if (valeurUtilisateur.size() >= 1) {
                        getMapValeurUtilisateur().put("idExterneRoleEcran", valeurUtilisateur.elementAt(0));
                    }
                    if (valeurUtilisateur.size() >= 2) {
                        setIdRoleEcran(valeurUtilisateur.elementAt(1));
                    }
                    if (valeurUtilisateur.size() >= 3) {
                        getMapValeurUtilisateur().put("idExterneSectionEcran", valeurUtilisateur.elementAt(2));
                    }
                    if (valeurUtilisateur.size() >= 4) {
                        setIdTypeSectionEcran(valeurUtilisateur.elementAt(3));
                    }
                    if (valeurUtilisateur.size() >= 5) {
                        getMapValeurUtilisateur().put("date", valeurUtilisateur.elementAt(4));
                    }
                    if (valeurUtilisateur.size() >= 6) {
                        getMapValeurUtilisateur().put("idExterneRubriqueEcran", valeurUtilisateur.elementAt(5));
                    }
                    if (valeurUtilisateur.size() >= 7) {
                        setCodeDebitCredit(valeurUtilisateur.elementAt(6));
                    }
                    if (valeurUtilisateur.size() >= 8) {
                        getMapValeurUtilisateur().put("libelle", valeurUtilisateur.elementAt(7));
                    }
                    if (valeurUtilisateur.size() >= 9) {
                        getMapValeurUtilisateur().put("piece", valeurUtilisateur.elementAt(8));
                    }
                    if (valeurUtilisateur.size() >= 10) {
                        getMapValeurUtilisateur().put("idExterneCompteCourantEcran", valeurUtilisateur.elementAt(9));
                    }
                    if (valeurUtilisateur.size() >= 11) {
                        getMapValeurUtilisateur().put("dateTraitement", valeurUtilisateur.elementAt(10));
                    }
                    if (valeurUtilisateur.size() >= 12) {
                        getMapValeurUtilisateur().put("dateDepot", valeurUtilisateur.elementAt(11));
                    }
                    if (valeurUtilisateur.size() >= 13) {
                        getMapValeurUtilisateur().put("dateInscription", valeurUtilisateur.elementAt(12));
                    }
                    if (valeurUtilisateur.size() >= 14) {
                        setIdOrganeExecution(valeurUtilisateur.elementAt(13));
                    }

                    if (valeurUtilisateur.size() >= 15) {
                        getMapValeurUtilisateur().put("montantEcran", valeurUtilisateur.elementAt(14));
                    }

                    if (valeurUtilisateur.size() >= 16) {
                        getMapValeurUtilisateur().put("genreTransaction", valeurUtilisateur.elementAt(15));
                    }

                    if (valeurUtilisateur.size() >= 17) {
                        getMapValeurUtilisateur().put("referenceBVR", valeurUtilisateur.elementAt(16));
                    }

                    if (valeurUtilisateur.size() >= 18) {
                        getMapValeurUtilisateur().put("referenceInterne", valeurUtilisateur.elementAt(17));
                    }
                }
            }
        }
    }

    /**
     * Mise à jour du fichier AJPPVUT pour les valeur par défaut par utilisateur
     * 
     * @see globaz.osiris.db.comptes.CAPaiement#_synchroValUtili(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _synchroValUtili(BTransaction transaction) {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        if (valeurUtilisateur == null) {
            valeurUtilisateur = new java.util.Vector<String>(18);
        }
        if (!JadeStringUtil.isBlank(getNomEcran())) {
            // chargement des données à mémoriser dans le vecteur
            valeurUtilisateur.removeAllElements();
            valeurUtilisateur.add(0, getIdExterneRoleEcran());
            valeurUtilisateur.add(1, getIdRoleEcran());
            valeurUtilisateur.add(2, getIdExterneSectionEcran());
            valeurUtilisateur.add(3, getIdTypeSectionEcran());
            valeurUtilisateur.add(4, getDate());
            valeurUtilisateur.add(5, getIdExterneRubriqueEcran());
            valeurUtilisateur.add(6, getCodeDebitCredit());
            valeurUtilisateur.add(7, getLibelle());
            valeurUtilisateur.add(8, getPiece());
            valeurUtilisateur.add(9, getIdExterneCompteCourantEcran());
            valeurUtilisateur.add(10, getDateTraitement());
            valeurUtilisateur.add(11, getDateDepot());
            valeurUtilisateur.add(12, getDateInscription());
            valeurUtilisateur.add(13, getIdOrganeExecution());
            valeurUtilisateur.add(14, getMontantEcran());
            valeurUtilisateur.add(15, getGenreTransaction());
            valeurUtilisateur.add(16, getReferenceBVR());
            valeurUtilisateur.add(17, getReferenceInterne());
            // mise à jour dans le fichier
            globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
            valUtili.setSession(getSession());
            valUtili.addValeur("CAPaiementBVR", getNomEcran(), valeurUtilisateur);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        // Laisser la superclasse effectuer son traitement
        super._validate(statement);

        // Vérifier le type d'opération
        if (!isInstanceOrSubClassOf(APIOperation.CAPAIEMENTBVR)) {
            _addError(statement.getTransaction(), getSession().getLabel("5166"));
        }
    }

    /**
     * Validation des données
     * 
     * @see globaz.osiris.db.comptes.CAOperation#_valider(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _valider(BTransaction transaction) {
        // Valider les données de la superclasse
        super._valider(transaction);

        // Organe d'exécution
        if (JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            getMemoryLog().logMessage("5178", null, FWMessage.ERREUR, this.getClass().getName());
        } else if (getOrganeExecution() == null) {
            getMemoryLog().logMessage("5179", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Test si c'est une operation auxiliaire et si la section auxiliaire n'est pas saisie
        if (!getCodeMaster().equals(APIOperation.SLAVE) && isSectionPrincipale()
                && JadeStringUtil.isBlankOrZero(getIdSectionAux())) {
            getMemoryLog().logMessage("7395", null, FWMessage.ERREUR, this.getClass().getName());
        }
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws java.lang.Exception {
        // Laisser la superclasse effectuer son traitement
        super._writeProperties(statement);

        // Traitement interne
        statement.writeField(CAOperation.FIELD_TAXEESTREPORTE, this._dbWriteBoolean(statement.getTransaction(),
                getTaxeEstReporte(), BConstants.DB_TYPE_BOOLEAN_CHAR, "taxeEstReporte"));
    }

    /**
     * Vérifie la dernière étape contentieux de la section. <br>
     * A partir de l'étape "Réquisition de Poursuite", on affiche un message dans le log. Etape poursuite et plus
     */
    private boolean checkSectionEtatPoursuite() {
        // Etape poursuite et plus
        if ((getSection() != null) && !getSection().isNew() && getSection().isSectionAuxPoursuites(false)) {
            getMemoryLog().logMessage(getSession().getLabel("SECTION_POURSUITE"), FWMessage.ERREUR,
                    this.getClass().getName());
            return true;
        }
        return false;
    }

    /**
     * Vérifier le solde de la section
     * 
     * @param isPlanPaiement
     */
    private void checkSoldeSection(boolean isPlanPaiement) {
        // Vérifier le solde de la section
        if (getSection() != null) {
            // Convertir les soldes
            FWCurrency fSoldeSection = new FWCurrency(getSection().getSolde());
            FWCurrency fMontant = new FWCurrency(getMontant());
            FWCurrency fSolde = new FWCurrency();
            FWCurrency fTaxes = new FWCurrency(getSection().getTaxes());
            FWCurrency fFrais = new FWCurrency(getSection().getFrais());
            FWCurrency fAmende = new FWCurrency(getSection().getAmende());
            FWCurrency fInteret = new FWCurrency(getSection().getInterets());
            FWCurrency fBase = new FWCurrency(getSection().getBase());

            fSolde.add(fSoldeSection);
            fSolde.add(fMontant);

            if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(getSection().getIdTypeSection())) {
                if (!fBase.isZero()) {
                    getMemoryLog().logMessage("7401", null, FWMessage.AVERTISSEMENT, this.getClass().getName());
                }
            } else {
                // Si le montant du résultat n'est pas égal à zéro
                if (!fSolde.isZero()) {
                    // Si le montant de la section est soldée
                    if (fSoldeSection.isZero()) {
                        setIdSectionSoldeIdentique();
                    } else {
                        // InfoRom427 - traitement taxe de sommation (solde inférieur ou égal aux taxes et positif)
                        // Si le montant du résultat est plus petit ou égal aux taxes et qu'il soit positif
                        if (CAUtil.isSoldeSectionLessOrEqualTaxes(fSolde, fTaxes)) {
                            traitementTaxesImpayees(fSolde);
                            // Taxes non payées
                            getMemoryLog().logMessage("5345", null, FWMessage.INFORMATION, this.getClass().getName());
                        } else if (fSolde.equals(fFrais)) {
                            // Frais et amendes non payés
                            getMemoryLog().logMessage("53451", null, FWMessage.ERREUR, this.getClass().getName());
                        } else if (fSolde.equals(fAmende)) {
                            getMemoryLog().logMessage("53451", null, FWMessage.ERREUR, this.getClass().getName());
                        } else if (fSolde.equals(fInteret)) {
                            // Si le solde de la section correspond aux intérets
                            // Intérêts non payés
                            getMemoryLog().logMessage("5346", null, FWMessage.AVERTISSEMENT, this.getClass().getName());
                        } else if (!fSolde.isZero() && !isPlanPaiement) {
                            // Traitement manuel
                            // Si la section n'est pas soldée après l'opération
                            // Le montant payé ne correspond pas au solde de la section
                            getMemoryLog().logMessage("5344", null, FWMessage.ERREUR, this.getClass().getName());
                        }
                    }
                }
            }
        }
    }

    /**
     * Créer l'écriture d'extourne.
     * 
     * @param ecr l'écriture à extourner.
     * @return l'écriture.
     */
    private CAEcriture createEcriture(CAEcriture ecr) {
        CAEcriture extourne = null;
        if (ecr != null) {
            extourne = ecr;
            extourne.setIdTypeOperation(ecr.getIdTypeOperation());
            extourne.setDate(getDate());
            extourne.setEtat(APIOperation.ETAT_OUVERT);
            extourne.setIdCompte(ecr.getIdCompte());
            extourne.setIdCompteAnnexe(ecr.getIdCompteAnnexe());
            extourne.setIdSection(ecr.getIdSection());
            extourne.setMontant(ecr.getMontant());
            extourne.setIdJournal(getIdJournal());
            extourne.setLibelle(CAPaiementBVR.EXTOURNE_LIBELLE);
        }
        return extourne;
    }

    /**
     * @see globaz.osiris.db.comptes.CAEcriture#createSlave()
     */
    @Override
    protected APISlave createSlave() {
        CAPaiementBVR ecr = new CAPaiementBVR();
        ecr.dupliquer(this);
        return ecr;
    }

    /**
     * @return
     * @throws Exception
     */
    private JADate dateExecutionEtapeTaxe() throws Exception {
        String date = "";
        if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            CADateExecutionSommationAquilaManager manager = new CADateExecutionSommationAquilaManager();
            manager.setSession(getSession());
            manager.setForIdSection(getSection().getIdSection());
            manager.setForSeqCon(getSection().getIdSequenceContentieux());
            manager.setForCsEtape(ICOEtape.CS_DECISION);
            manager.find();

            if (!manager.isEmpty()) {
                CADateExecutionSommationAquila ev = (CADateExecutionSommationAquila) manager.getFirstEntity();
                date = ev.getDateExecution();
            }
        }

        if (JadeStringUtil.isBlank(date)) {
            date = getSection()._getDateExecutionSommation();
        }

        return new JADate(date);
    }

    /**
     * @see globaz.osiris.db.comptes.CAPaiement#dupliquer(globaz.osiris.db.comptes.CAPaiement)
     */
    @Override
    public void dupliquer(CAPaiement oper) {
        // Dupliquer les paramètres de la superclasse
        super.dupliquer(oper);
    }

    /**
     * @param oper
     */
    public void dupliquer(CAPaiementBVR oper) {
        // Dupliquer les paramètres de la superclasse
        super.dupliquer(oper);

        // Dupliquer le paiement
        if (oper != null) {
            setDateDepot(oper.getDateDepot());
            setDateInscription(oper.getDateInscription());
            setDateTraitement(oper.getDateTraitement());
            setGenreTransaction(oper.getGenreTransaction());
            setIdOrganeExecution(oper.getIdOrganeExecution());
            setReferenceBVR(oper.getReferenceBVR());
            setReferenceInterne(oper.getReferenceInterne());
            setBankTransactionCode(oper.getBankTransactionCode());
            setAccountServicerReference(oper.getAccountServicerReference());
            setDebtor(oper.getDebtor());
        }
    }

    /**
     * Cette méthode permet de rechercher les sections plus anciennes d'un compte annexe qui ont un solde identique au
     * montant du paiement BVR à traiter. Les sections qui font partie d'un plan de recouvrement ne sont pas prise en
     * compte
     * 
     * @return CASectionManager
     */
    private CASectionManager findSectionSoldeIdentique() {
        CASectionManager sctManager = new CASectionManager();
        sctManager.setSession(getSession());
        sctManager.setForIdCompteAnnexe(getIdCompteAnnexe());
        sctManager.setForSolde(new BigDecimal(getMontant()).negate().toString());
        sctManager.setOrderBy(CASectionManager.ORDER_DATE);
        sctManager.setForIdPlanRecouvrement("0");
        try {
            sctManager.find();
        } catch (Exception e) {
            getMemoryLog().logMessage("5343", null, FWMessage.ERREUR, this.getClass().getName());
        }
        return sctManager;
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#getDateDepot()
     */
    @Override
    public String getDateDepot() {
        return transactionBVR.getDateDepot();
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#getDateInscription()
     */
    @Override
    public String getDateInscription() {
        return transactionBVR.getDateInscription();
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#getDateTraitement()
     */
    @Override
    public String getDateTraitement() {
        return transactionBVR.getDateTraitement();
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#getGenreTransaction()
     */
    @Override
    public String getGenreTransaction() {
        return transactionBVR.getGenreTransaction();
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#getIdOrganeExecution()
     */
    @Override
    public String getIdOrganeExecution() {
        return transactionBVR.getIdOrganeExecution();
    }

    /**
     * @return l'id plan de la section si existe, sinon l'id plan provenant de l'échéance référencée dans l'opération
     * @param transaction
     * @throws Exception
     */
    private String getIdPlan(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getSection().getIdPlanRecouvrement())) {
            return getSection().getIdPlanRecouvrement();
        }
        if (echeance == null) {
            echeance = new CAEcheancePlan();
            echeance.setSession(getSession());
            echeance.setIdEcheancePlan(getIdEcheancePlan());
            echeance.retrieve(transaction);
            if (echeance.isNew() || echeance.hasErrors()) {
                throw new Exception(getSession().getLabel("ECH_NON_RESOLU"));
            }
        }
        return echeance.getIdPlanRecouvrement();
    }

    /**
     * @return
     */
    public CAOrganeExecution getOrganeExecution() {
        transactionBVR.setSession(getSession());
        return transactionBVR.getOrganeExecution();
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#getReferenceBVR()
     */
    @Override
    public String getReferenceBVR() {
        return transactionBVR.getReferenceBVR();
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#getReferenceInterne()
     */
    @Override
    public String getReferenceInterne() {
        return transactionBVR.getReferenceInterne();
    }

    /**
     * @return la liste des rubriques de nature "Taxe de sommation".
     * @throws Exception
     */
    private List getRubriquesTaxeSommation() throws Exception {
        CARubriqueManager rubriques = new CARubriqueManager();
        rubriques.setSession(getSession());
        rubriques.setForNatureRubrique(APIRubrique.TAXE_SOMMATION);
        rubriques.find();
        List list = new ArrayList();
        Iterator itRub = rubriques.iterator();
        while (itRub.hasNext()) {
            CARubrique rub = (CARubrique) itRub.next();
            list.add(rub.getIdExterne());
        }
        return list;
    }

    /**
     * @return l'iterator du manager des ecritures comptabilisées pour les rubriques de nature "Taxe de sommation".
     * @throws Exception
     */
    private CAEcriture getTaxeEcriture() throws Exception {
        CAEcritureParNatureRubriqueManager mngEc = new CAEcritureParNatureRubriqueManager();
        mngEc.setSession(getSession());
        mngEc.setForIdCompteAnnexe(getIdCompteAnnexe());
        mngEc.setForIdSection(getIdSection());
        mngEc.setForIdJournalPassage(getIdJournal());
        mngEc.setConditionSelectEtat(true);
        // Comptabilisé
        // mngEc.setForEtat(APIOperation.ETAT_COMPTABILISE);
        // de nature taxe se sommation
        mngEc.setForRubriqueIn(getRubriquesTaxeSommation());

        // BZ 5224 : ajout order by
        mngEc.setOrderBy(CAOperationManager.ORDER_DATEOP_DESC);

        mngEc.find();
        return (CAEcriture) mngEc.getFirstEntity();
    }

    /**
     * Récupérer la transaction BVR.
     * 
     * @param transaction
     */
    public void initTransactionBVR(BTransaction transaction) {
        transactionBVR = new CATransactionBVR();
        transactionBVR.setSession(getSession());
        transactionBVR.setIdOperation(getIdOperation());
        try {
            transactionBVR.retrieve(transaction);
            if (transaction.hasErrors() || transactionBVR.isNew()) {
                _addError(transaction, getSession().getLabel("7126"));
            }
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }
    }

    /**
     * Sauve l'écriture en base
     * 
     * @param extourne l'écriture à extourner.
     * @throws Exception
     */
    private void saveEcriture(CAEcriture extourne) throws Exception {
        CAEcriture.inverserCodeDebitCredit(extourne);
        extourne.setMontant(extourne.getMontant());

        // Correction BZ 5224 : on force debit
        if (APIEcriture.EXTOURNE_CREDIT.equals(extourne.getCodeDebitCredit())) {
            extourne.setCodeDebitCredit(APIEcriture.EXTOURNE_DEBIT);
        }

        if (!getSession().hasErrors()) {
            extourne.add();
        }
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#setDateDepot(java.lang.String)
     */
    @Override
    public void setDateDepot(String newDateDepot) {
        transactionBVR.setDateDepot(newDateDepot);
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#setDateInscription(java.lang.String)
     */
    @Override
    public void setDateInscription(String newDateInscription) {
        transactionBVR.setDateInscription(newDateInscription);
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#setDateTraitement(java.lang.String)
     */
    @Override
    public void setDateTraitement(String newDateTraitement) {
        transactionBVR.setDateTraitement(newDateTraitement);
    }

    /**
     * Remplir la date de valeur
     */
    private void setDateValeur() {
        // Remplir la date de valeur
        if (JadeStringUtil.isIntegerEmpty(getDate())) {
            switch (CAApplication.getApplicationOsiris().getCAParametres().getDateValeurBVR()) {
                case CAParametres.BVR_DATE_DEPOT:
                    setDate(getDateDepot());
                    break;
                case CAParametres.BVR_DATE_INSCRIPTION:
                    setDate(getDateInscription());
                    break;
                default:
                    setDate(getDateTraitement());
                    break;
            }
        }
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#setGenreTransaction(java.lang.String)
     */
    @Override
    public void setGenreTransaction(String newGenreTransaction) {
        transactionBVR.setGenreTransaction(newGenreTransaction);
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#setIdOrganeExecution(java.lang.String)
     */
    @Override
    public void setIdOrganeExecution(String newIdOrganeExecution) {
        transactionBVR.setIdOrganeExecution(newIdOrganeExecution);
    }

    /**
     * Cette méthode permet de setter l'identifiant de la section qui à un solde identique au montant du paiement BVR,
     * qui n'est pas en poursuite
     */
    private void setIdSectionSoldeIdentique() {
        CASectionManager sctManager = findSectionSoldeIdentique();

        if (!sctManager.isEmpty()) {
            boolean sectionTrouvee = false;
            int i = 0;
            String idSectionSave = getIdSection();
            while (!sectionTrouvee && (i < sctManager.size())) {
                CASection sct = (CASection) sctManager.getEntity(i);
                setIdSection(sct.getIdSection());
                if (checkSectionEtatPoursuite()) {
                    sectionTrouvee = false;
                } else {
                    sectionTrouvee = true;
                }
                i++;
            }
            if (!sectionTrouvee) {
                setIdSection(idSectionSave);
                checkSectionEtatPoursuite();
                getMemoryLog().logMessage("5343", null, FWMessage.ERREUR, this.getClass().getName());
            }
        } else {
            getMemoryLog().logMessage("5343", null, FWMessage.ERREUR, this.getClass().getName());
        }
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#setReferenceBVR(java.lang.String)
     */
    @Override
    public void setReferenceBVR(String newReferenceBVR) {
        transactionBVR.setReferenceBVR(newReferenceBVR);
    }

    /**
     * @see globaz.osiris.api.APIPaiementBVR#setReferenceInterne(java.lang.String)
     */
    @Override
    public void setReferenceInterne(String newReferenceInterne) {
        transactionBVR.setReferenceInterne(newReferenceInterne);
    }

    /**
     * @param transaction
     * @throws Exception
     */
    private void setSectionModeCompensation(BTransaction transaction, String mode) throws Exception {
        CASection section = new CASection();
        section.setSession(getSession());
        section.setIdSection(getIdSection());
        section.retrieve(transaction);

        if (!transaction.hasErrors()) {
            try {
                section.setIdModeCompensation(mode);
                section.update(transaction);
            } catch (Exception e) {
                _addError(transaction, e.toString());
            }
        }
    }

    /**
     * Traitement des taxes impayées.
     * 
     * @param fSolde Le solde restant après déduction du montant payé.
     */
    private void traitementTaxesImpayees(FWCurrency fSolde) {
        try {
            if (!getSection().isSectionAuxPoursuites(false)) {
                JACalendar cal = new JACalendarGregorian();

                // Date de sommation + les jours de délai accepté pour un paiement BVR.
                JADate dateSommation = cal.addDays(dateExecutionEtapeTaxe(),
                        CAParametres.getDelaiPmtBvr(getSession().getCurrentThreadTransaction()));

                // Date de paiement enregistré depuis le BVR.
                JADate datePmt = new JADate(getDate());

                // Savoir si la sommation et le paiement se sont croisés
                if (cal.compare(datePmt, dateSommation) <= JACalendar.COMPARE_FIRSTLOWER) {
                    // La sommation et le paiement se sont croisés
                    // Parcourir les rubriques de natures Taxe de Sommation
                    CAEcriture ecr = getTaxeEcriture();
                    CAEcriture extourne = createEcriture(ecr);
                    // InfoRom427
                    // Nous extournons uniquement le solde restant (égal ou moins) de la taxe si un paiement
                    // a été un peu trop payer. exemple : 2000.- + 40.- taxe et l'affilié paie 2010.-, donc extourne
                    // 30.- et non 40.-
                    extourne.setMontant(fSolde.toString());

                    FWCurrency fTaxes = new FWCurrency(getSection().getTaxes());
                    // Si vrai : extourne / crée une E inverse pour chaque rubrique
                    if ((CAUtil.isSoldeSectionLessOrEqualTaxes(fSolde, fTaxes))
                            && (!JadeStringUtil.isEmpty(getIdJournal()))) {
                        saveEcriture(extourne);
                    } else {
                        getMemoryLog().logMessage("5344", null, FWMessage.ERREUR, this.getClass().getName());
                    }
                } else {
                    if (APISection.MODE_COMPENSATION_STANDARD.equals(getSection().getIdModeCompensation())) {
                        setTaxeEstReporte(Boolean.TRUE);
                    } else {
                        getMemoryLog().logMessage("5344", null, FWMessage.ERREUR, this.getClass().getName());
                    }
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage("5344", null, FWMessage.ERREUR,
                    this.getClass().getName() + ".traitementTaxesImpayees()");
        }
    }

    /**
     * Test si un décompte final, un bouclement d'acompte ou une taxation d'office (qui s'apparente à un décompte 13)
     * figure dans le compte pour la période couverte par le bulletin neutre.
     * 
     * @param transaction
     * @throws Exception
     */
    private void updateSectionStatutForDecompteFinal(BTransaction transaction) throws Exception {
        COSectionBulletinNeutreABloquerManager managerBN = CAComptabiliserBulletinNeutre
                .findBulletinNeutreAvecDecompteFinal(transaction, getIdCompteAnnexe(), getIdSection());
        if (!managerBN.isEmpty()) {
            CASection section = new CASection();
            section.setIdSection(getIdSection());
            section.retrieve(transaction);
            section.setStatutBN(APISection.STATUTBN_DECOMPTE_FINAL);
            section.update(transaction);
            getMemoryLog().logMessage(getSession().getLabel("DECOMPTE_FINAL_EXISTANT"), FWMessage.ERREUR,
                    this.getClass().getName());
        }
    }

    /**
     * @param transaction
     */
    public void validerFromBVR(BTransaction transaction, boolean isPlanPaiement) {
        // Remplir la date de valeur
        setDateValeur();

        // Remplir la rubrique (celle de l'organe d'exécution)
        if (JadeStringUtil.isIntegerEmpty(getIdCompte())) {
            if (getOrganeExecution() != null) {
                setIdCompte(getOrganeExecution().getIdRubrique());
            }
        }

        // Exécuter une validation de test
        _valider(transaction);

        if ((getSection() != null)
                && APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(getSection().getIdTypeSection())) {
            // Test si un décompte final, un bouclement d'acompte ou une taxation d'office (qui s'apparente à un
            // décompte 13) figure dans le compte pour la période couverte par le bulletin neutre.
            try {
                updateSectionStatutForDecompteFinal(transaction);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, this.getClass().getName());
            }
        }

        if ((getCompteAnnexe() != null) && getCompteAnnexe().isASurveiller().booleanValue()) {
            getMemoryLog().logMessage(
                    getSession().getLabel("SOUS_SURVEILLANCE") + "(" + getCompteAnnexe().getIdExterneRole() + ")",
                    FWMessage.ERREUR, this.getClass().getName());
        }

        if ((getCompteAnnexe() != null) && getCompteAnnexe().isVerrouille() && !getQuittanceLogEcran().booleanValue()) {
            getMemoryLog().logMessage(getSession().getLabel("VERROUILLE"), FWMessage.ERREUR, this.getClass().getName());
        }

        // Etape poursuite et plus
        checkSectionEtatPoursuite();
        // Vérifier le solde de la section
        checkSoldeSection(isPlanPaiement);

        // L'opération est en erreur à partir de l'avertissement
        if (getMemoryLog().getErrorLevel().compareTo(FWMessage.AVERTISSEMENT) >= 0) {
            setEtat(APIOperation.ETAT_ERREUR);
        }
    }

    @Override
    public String getDebtor() {
        return transactionBVR.getDebtor();
    }

    @Override
    public String getBankTransactionCode() {
        return transactionBVR.getBankTransactionCode();
    }

    @Override
    public String getAccountServicerReference() {
        return transactionBVR.getAccountServicerReference();
    }

    @Override
    public void setBankTransactionCode(String bankTransactionCode) {
        transactionBVR.setBankTransactionCode(bankTransactionCode);
    }

    @Override
    public void setDebtor(String debtor) {
        transactionBVR.setDebtor(debtor);
    }

    @Override
    public void setAccountServicerReference(String accountServicerReference) {
        transactionBVR.setAccountServicerReference(accountServicerReference);
    }

}
