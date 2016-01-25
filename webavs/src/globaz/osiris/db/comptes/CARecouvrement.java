package globaz.osiris.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRecouvrement;
import globaz.osiris.api.APISlave;
import globaz.osiris.utils.CAUtil;

/**
 * Insérez la description du type ici. Date de création : (14.02.2002 10:06:48)
 * 
 * @author: Administrator
 */
public class CARecouvrement extends CAPaiement implements APIRecouvrement {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Code refus débit direct
    public final static String CS_REFUS_01 = "245001";
    public final static String CS_REFUS_02 = "245002";
    public final static String CS_REFUS_03 = "245003";
    public final static String CS_REFUS_04 = "245004";
    public final static String CS_REFUS_05 = "245005";
    public final static String CS_REFUS_06 = "245006";
    public final static String CS_REFUS_07 = "245007";

    private CATransactionRecouvrement transactionRecouvrement = new CATransactionRecouvrement();

    public CARecouvrement() {
        super();
        setIdTypeOperation(APIOperation.CARECOUVREMENT);
    }

    public CARecouvrement(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CARECOUVREMENT);
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
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        this._synchroValUtili(transaction);
        // Sauvegarder la transaction bvr
        if (!transaction.hasErrors()) {
            transactionRecouvrement.setIdOperation(getIdOperation());
            transactionRecouvrement.add(transaction);
            if (transaction.hasErrors()) {
                _addError(transaction, getSession().getLabel("7160"));
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.02.2002 14:04:58)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Laisser la superclasse effectuer son traitement
        super._afterRetrieve(transaction);

        initTransactionRecouvremenet(transaction);
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
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        this._synchroValUtili(transaction);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 17:06:20)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // Laisser la superclasse s'initialiser
        super._beforeAdd(transaction);
        // Forcer le type d'opération
        setIdTypeOperation(APIOperation.CARECOUVREMENT);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 15:17:35)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Laisser la superclasse traiter l'événement
        super._beforeDelete(transaction);

        // Supprimer la transaction Recouvrement
        if (!transaction.hasErrors()) {
            transactionRecouvrement.delete(transaction);
            if (transaction.hasErrors()) {
                _addError(transaction, getSession().getLabel("7162"));
            }
        }
    }

    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws Exception {

        // Laisser la superclasse traiter l'événement
        super._beforeUpdate(transaction);

        if (getGenreTransaction().equals("084") && !getCodeRefus().equals(CARecouvrement.CS_REFUS_02)) {
            setEtat(APIOperation.ETAT_INACTIF);
        }

        // Mettre à jour la transaction Recouvrement
        transactionRecouvrement.update(transaction);
        if (transaction.hasErrors()) {
            _addError(transaction, getSession().getLabel("7160"));
        }
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_createExtourne(BTransaction, String)
     */
    @Override
    protected CAOperation _createExtourne(BTransaction transaction, String text) throws Exception {
        // Création d'un double de l'écriture
        CARecouvrement extourne = new CARecouvrement();
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
     */
    protected void _synchroChgValUtili(globaz.globall.db.BTransaction transaction) {
        if (isNew()) {
            if (!JadeStringUtil.isBlank(getNomEcran()) && (valeurUtilisateur == null)) {
                valeurUtilisateur = new java.util.Vector(13);
                // lecture du fichier
                globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
                valUtili.setSession(getSession());
                valeurUtilisateur = valUtili.retrieveValeur("CARecouvrement", getNomEcran());
                // chargement des propriétés internes si idExterneRoleEcran est
                // vide

                if (JadeStringUtil.isBlank(getIdExterneRoleEcran()) && !valeurUtilisateur.isEmpty()) {
                    if (valeurUtilisateur.size() >= 1) {
                        setIdExterneRoleEcran(valeurUtilisateur.elementAt(0));
                    }
                    if (valeurUtilisateur.size() >= 2) {
                        setIdRoleEcran(valeurUtilisateur.elementAt(1));
                    }
                    if (valeurUtilisateur.size() >= 3) {
                        setIdExterneSectionEcran(valeurUtilisateur.elementAt(2));
                    }
                    if (valeurUtilisateur.size() >= 4) {
                        setIdTypeSectionEcran(valeurUtilisateur.elementAt(3));
                    }
                    if (valeurUtilisateur.size() >= 5) {
                        setDate(valeurUtilisateur.elementAt(4));
                    }
                    if (valeurUtilisateur.size() >= 6) {
                        setIdExterneRubriqueEcran(valeurUtilisateur.elementAt(5));
                    }
                    if (valeurUtilisateur.size() >= 7) {
                        setCodeDebitCredit(valeurUtilisateur.elementAt(6));
                    }
                    if (valeurUtilisateur.size() >= 8) {
                        setLibelle(valeurUtilisateur.elementAt(7));
                    }
                    if (valeurUtilisateur.size() >= 9) {
                        setPiece(valeurUtilisateur.elementAt(8));
                    }
                    if (valeurUtilisateur.size() >= 10) {
                        setIdExterneCompteCourantEcran(valeurUtilisateur.elementAt(9));
                    }
                    if (valeurUtilisateur.size() >= 11) {
                        setIdExterneCompteCourantEcran(valeurUtilisateur.elementAt(10));
                    }
                    if (valeurUtilisateur.size() >= 12) {
                        setIdOrganeExecution(valeurUtilisateur.elementAt(11));
                    }

                }
            }
        }
    }

    /**
     * mise à jour du fichier AJPPVUT pour les valeur par défaut par utilisateur
     */
    @Override
    protected void _synchroValUtili(globaz.globall.db.BTransaction transaction) {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        if (valeurUtilisateur == null) {
            valeurUtilisateur = new java.util.Vector(12);
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
            valeurUtilisateur.add(10, getDateEcheance());
            valeurUtilisateur.add(11, getIdOrganeExecution());
            // mise à jour dans le fichier
            globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
            valUtili.setSession(getSession());
            valUtili.addValeur("CARecouvrement", getNomEcran(), valeurUtilisateur);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.02.2002 08:50:10)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {

        // Laisser la superclasse effectuer son traitement
        super._validate(statement);

        // Vérifier le type d'opération
        if (!isInstanceOrSubClassOf(APIOperation.CARECOUVREMENT)) {
            _addError(statement.getTransaction(), getSession().getLabel("5250"));
        }

    }

    /**
     * Validation des données Date de création : (30.01.2002 07:52:07)
     */
    @Override
    protected void _valider(globaz.globall.db.BTransaction transaction) {

        // Valider les données de la superclasse
        super._valider(transaction);

        // Organe d'exécution
        if (JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            getMemoryLog().logMessage("5178", null, FWMessage.ERREUR, this.getClass().getName());
        } else if (getOrganeExecution() == null) {
            getMemoryLog().logMessage("5179", null, FWMessage.ERREUR, this.getClass().getName());
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 17:40:50)
     */
    @Override
    protected APISlave createSlave() {
        CARecouvrement ecr = new CARecouvrement();
        ecr.dupliquer(this);
        return ecr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 10:55:05)
     * 
     * @param oper
     *            globaz.osiris.db.comptes.CAPaiement
     */
    @Override
    public void dupliquer(CAPaiement oper) {

        // Dupliquer les paramètres de la superclasse
        super.dupliquer(oper);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 10:55:05)
     * 
     * @param oper
     *            globaz.osiris.db.comptes.CAPaiement
     */
    public void dupliquer(CARecouvrement oper) {

        // Dupliquer les paramètres de la superclasse
        super.dupliquer(oper);

        // Dupliquer le recouvrement
        if (oper != null) {
            setGenreTransaction(oper.getGenreTransaction());
            setIdOrganeExecution(oper.getIdOrganeExecution());
            setReference(oper.getReference());
            setDateEcheance(oper.getDateEcheance());
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 10:40:04)
     * 
     * @return java.lang.String
     */
    public String getCodeRefus() {
        return transactionRecouvrement.getCodeRefus();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 10:40:04)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDateEcheance() {
        return transactionRecouvrement.getDateEcheance();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 10:40:04)
     * 
     * @return java.lang.String
     */
    @Override
    public String getGenreTransaction() {
        return transactionRecouvrement.getGenreTransaction();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 11:16:20)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdOrganeExecution() {
        return transactionRecouvrement.getIdOrganeExecution();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 11:21:46)
     * 
     * @return globaz.osiris.db.ordres.CAOrganeExecution
     */
    public globaz.osiris.db.ordres.CAOrganeExecution getOrganeExecution() {
        transactionRecouvrement.setSession(getSession());
        return transactionRecouvrement.getOrganeExecution();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 10:41:28)
     * 
     * @return java.lang.String
     */
    @Override
    public String getReference() {
        return transactionRecouvrement.getReference();
    }

    /**
     * Récupérer la transaction recouvrement.
     * 
     * @param transaction
     */
    public void initTransactionRecouvremenet(BTransaction transaction) {
        transactionRecouvrement = new CATransactionRecouvrement();
        transactionRecouvrement.setSession(getSession());
        transactionRecouvrement.setIdOperation(getIdOperation());
        try {
            transactionRecouvrement.retrieve(transaction);
            if (transaction.hasErrors() || transactionRecouvrement.isNew()) {
                _addError(transaction, getSession().getLabel("7161"));
            }
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }
    }

    /**
     * @author: sel Créé le : 28 sept. 06
     * @param newCodeRefus
     */
    public void setCodeRefus(String newCodeRefus) {
        if (!newCodeRefus.equals("245000")) { // Vérifie que le code de refus ne
            // soit pas 00
            transactionRecouvrement.setCodeRefus(newCodeRefus);
        } else {
            transactionRecouvrement.setCodeRefus("");
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 10:49:10)
     * 
     * @param newReference
     *            java.lang.String
     */
    @Override
    public void setDateEcheance(String newDateEcheance) {
        // if (isUpdatable())
        transactionRecouvrement.setDateEcheance(newDateEcheance);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 10:47:55)
     * 
     * @param newGenreTransaction
     *            java.lang.String
     */
    @Override
    public void setGenreTransaction(String newGenreTransaction) {
        // if (isUpdatable())
        transactionRecouvrement.setGenreTransaction(newGenreTransaction);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 11:16:38)
     * 
     * @param newIdOrganeExecution
     *            java.lang.String
     */
    @Override
    public void setIdOrganeExecution(String newIdOrganeExecution) {
        // if (isUpdatable())
        transactionRecouvrement.setIdOrganeExecution(newIdOrganeExecution);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 10:49:10)
     * 
     * @param newReference
     *            java.lang.String
     */
    @Override
    public void setReference(String newReference) {
        // if (isUpdatable())
        transactionRecouvrement.setReference(newReference);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Valide le paiement LSV sur la base des informations disponibles Date de création : (18.02.2002 14:23:43)
     */
    public void validerFromLSV(globaz.globall.db.BTransaction transaction) {

        // Remplir la date de valeur
        if (JadeStringUtil.isIntegerEmpty(getDate())) {
            setDate(getDateEcheance());
        }

        // Remplir la rubrique (celle de l'organe d'exécution)
        if (JadeStringUtil.isIntegerEmpty(getIdCompte())) {
            if (getOrganeExecution() != null) {
                setIdCompte(getOrganeExecution().getIdRubrique());
            }
        }

        // Exécuter une validation de test
        _valider(transaction);

        // Vérifier le solde de la section
        if (getSection() != null) {

            // Convertir les soldes
            FWCurrency fSoldeSection = new FWCurrency(getSection().getSolde());
            FWCurrency fMontant = new FWCurrency(getMontant());
            FWCurrency fSolde = new FWCurrency();
            fSolde.add(fSoldeSection);
            fSolde.add(fMontant);
            FWCurrency fTaxes = new FWCurrency(getSection().getTaxes());
            fTaxes.add(getSection().getFrais());
            fTaxes.add(getSection().getAmende());
            FWCurrency fInteret = new FWCurrency(getSection().getInterets());

            // Si le résultat n'est pas nul
            if (!fSolde.isZero()) {

                // Si la section est soldée
                if (fSoldeSection.isZero()) {
                    getMemoryLog().logMessage("5343", null, FWMessage.ERREUR, this.getClass().getName());
                } else if ((CAUtil.isSoldeSectionLessOrEqualTaxes(fSolde, fTaxes))) {
                    getMemoryLog().logMessage("5345", null, FWMessage.ERREUR, this.getClass().getName());
                } else if (fSolde.equals(fInteret)) {
                    getMemoryLog().logMessage("5346", null, FWMessage.AVERTISSEMENT, this.getClass().getName());
                } else if (!fSolde.isZero()) {
                    getMemoryLog().logMessage("5344", null, FWMessage.ERREUR, this.getClass().getName());
                }
            }

        }

        // L'opération est en erreur à partir de l'avertissement
        if (getMemoryLog().getErrorLevel().compareTo(FWMessage.AVERTISSEMENT) >= 0) {
            setEtat(APIOperation.ETAT_ERREUR);
        }

    }

}
