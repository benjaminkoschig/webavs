package globaz.helios.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.helios.api.ICGJournal;
import globaz.helios.application.CGApplication;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.helios.parser.CGPieceIncrementor;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.HashSet;

public class CGEcritureViewBean extends CGNeedExerciceComptable implements ITreeListable, java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String COURS_MONNAIE_0 = "0.00000";
    public static final String CS_MONTANT_CHF = "722001";

    public static final String CS_MONTANT_MONNAIE_ETRANGERE = "722002";

    public static final String CS_TRI_COMPTE = "723003";
    // Ecriture tri
    public static final String CS_TRI_DATE = "723001";
    public static final String CS_TRI_LIBELLE = "723006";
    public static final String CS_TRI_MONTANT_CHF = "723004";
    public static final String CS_TRI_MONTANT_MONNAIE_ETR = "723005";
    public static final String CS_TRI_PIECE = "723002";
    public static final String CS_TYPE_ECRITURE_COLLECTIVE = "733002";
    public static final String CS_TYPE_ECRITURE_DETTE_AVOIR = "733003";
    /**
     * @deprecated Conservé pour lecture des anciennes écritures uniquement.
     * @since DDA Refactoring
     */
    @Deprecated
    public static final String CS_TYPE_ECRITURE_DOUBLE = "733001";
    public static final String FIELD_CODEDEBITCREDIT = "CODEDEBITCREDIT";
    public static final String FIELD_COURSMONNAIE = "COURSMONNAIE";
    public static final String FIELD_DATE = "DATE";
    public static final String FIELD_DATEVALEUR = "DATEVALEUR";
    public static final String FIELD_ESTACTIVE = "ESTACTIVE";
    public static final String FIELD_ESTERREUR = "ESTERREUR";
    public static final String FIELD_ESTPOINTEE = "ESTPOINTEE";
    public static final String FIELD_ESTPROVISOIRE = "ESTPROVISOIRE";
    public static final String FIELD_IDCENTRECHARGE = "IDCENTRECHARGE";
    public static final String FIELD_IDCOMPTE = "IDCOMPTE";
    public static final String FIELD_IDECRITURE = "IDECRITURE";
    public static final String FIELD_IDENTETEECRITURE = "IDENTETEECRITURE";
    public static final String FIELD_IDEXERCOMPTABLE = "IDEXERCOMPTABLE";
    public static final String FIELD_IDJOURNAL = "IDJOURNAL";

    public static final String FIELD_IDLIVRE = "IDLIVRE";

    // codes systemes
    // Ecriture type

    public static final String FIELD_IDLOG = "IDLOG";

    public static final String FIELD_IDMANDAT = "IDMANDAT";
    public static final String FIELD_IDREMARQUE = "IDREMARQUE";

    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_MONTANT = "MONTANT";
    public static final String FIELD_MONTANTMONNAIE = "MONTANTMONNAIE";
    public static final String FIELD_PIECE = "PIECE";
    public static final String FIELD_REFERENCEEXTERNE = "REFERENCEEXTERNE";
    public static final String IDEXTERNE = "IDEXTERNE";

    private static final String LABEL_PREFIXE = "ECRITURE_";
    public static final String TABLE_CGECRIP = "CGECRIP";

    /**
     * Date de création : (03.04.2003 18:03:58)
     * 
     * @return java.util.HashSet
     */
    public static HashSet getExcept() {
        HashSet hash = new HashSet();
        hash.add(CodeSystem.CS_EXTOURNE_CREDIT);
        hash.add(CodeSystem.CS_EXTOURNE_DEBIT);
        return hash;
    }

    private Boolean centreChargeAffiche = new Boolean(false);
    private String codeDebitCredit = new String();
    private CGPlanComptableViewBean compte = null;
    private CGEcritureViewBean contrepartie;
    private String coursMonnaie = CGEcritureViewBean.COURS_MONNAIE_0;
    private String date = new String();
    private String dateValeur = new String();
    private Boolean estActive = new Boolean(false);
    private Boolean estErreur = new Boolean(false);
    private Boolean estPointee = new Boolean(false);
    private Boolean estProvisoire = new Boolean(true);
    // traitement
    // ecriture
    // double
    private Boolean flagCentreCharge = new Boolean(false); // pour traitement
    private Boolean flagMonnaieEtrangere = new Boolean(false); // pour
    private String idCentreCharge = new String();
    private String idCompte = new String();
    private String idEcriture = new String();
    private String idEnteteEcriture = new String();
    private String idExterneCompte = "";
    private String idJournal = new String();
    private String idLivre = new String();
    private String idLog = new String();
    private String idMandat = new String();
    private String idRemarque = "0";
    private CGJournal journal = null;
    private String libelle = new String();
    private String libelleCompte = "";
    private String montant = new String();

    // ecriture double
    private Boolean montantEtrangerAffiche = new Boolean(false);
    private String montantMonnaie = new String();
    private String nextId = new String();
    private String piece = new String();
    private Boolean quittancer = new Boolean(false);
    private String referenceExterne = new String();
    private String remarque = "";

    private String soldeCompte = "";

    /**
     * Commentaire relatif au constructeur CGEcriture
     */
    public CGEcritureViewBean() {
        super();
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        super._afterRetrieve(transaction);

        if (hasCGRemarque()) {
            CGRemarque rem = retrieveCGRemarque();
            if (rem.isNew()) {
                setIdRemarque("0");
            } else {
                setRemarque(rem.getRemarque());
            }
        }

    }

    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        super._afterUpdate(transaction);
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        if ((nextId == null) || JadeStringUtil.isBlank(nextId)) {
            // incrémente de +1 le numéro
            setIdEcriture(this._incCounter(transaction, "0"));
        } else {
            setIdEcriture(nextId);
        }

        CGPieceIncrementor.setNextNumero(getSession(), transaction, getIdExerciceComptable(), getPiece());

        // id compte : il faut le préciser si on a uniquement l'id externe
        if (JadeStringUtil.isIntegerEmpty(getIdCompte())) {
            if (JadeStringUtil.isIntegerEmpty(getIdExterneCompte())) {
                _addError(transaction, label("COMPTE_ERROR_1"));
            } else {
                CGPlanComptableListViewBean manager = new CGPlanComptableListViewBean();
                manager.setSession(getSession());
                manager.setForIdExerciceComptable(getIdExerciceComptable());
                manager.setForIdExterne(getIdExterneCompte());
                manager.find(transaction);
                if (manager.size() != 1) {
                    _addError(transaction, label("COMPTE_ERROR_2"));
                } else {
                    setIdCompte(((CGPlanComptableViewBean) manager.getEntity(0)).getIdCompte());
                }
            }
        }

        if (!JadeStringUtil.isBlank(getRemarque())) {
            createRemarque(transaction);
        }
    }

    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        CGJournal journal = new CGJournal();
        journal.setSession(getSession());
        journal.setIdJournal(getIdJournal());
        journal.retrieve(transaction);

        if (journal.isNew()) {
            _addError(transaction, label("SUPPR_ECRIT_ERROR_1"));
            return;
        }

        if (journal.getIdEtat().equals(ICGJournal.CS_ETAT_COMPTABILISE)) {
            _addError(transaction, label("SUPPR_ECRIT_ERROR_2"));
            return;
        }

        // ou si l'utilisateur a les droits de chef comptable
        if (!(CGApplication.isUserChefComptable(getSession())) && !journal.isEstPublic().booleanValue()
                && !journal.getProprietaire().equals(getSession().getUserId())) {
            _addError(transaction, label("SUPPR_ECRIT_ERROR_3"));
            return;
        }

        if (!JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
            deleteRemarque(transaction);
        }
    }

    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // System.out.print(">UPD Ecr(" + getIdEcriture() + ") ");

        // id compte : il faut le préciser si on a uniquement l'id externe
        if (JadeStringUtil.isIntegerEmpty(getIdCompte())) {
            if (JadeStringUtil.isIntegerEmpty(getIdExterneCompte())) {
                _addError(transaction, label("COMPTE_ERROR_1"));
            } else {
                CGPlanComptableListViewBean manager = new CGPlanComptableListViewBean();
                manager.setSession(getSession());
                manager.setForIdExerciceComptable(getIdExerciceComptable());
                manager.setForIdExterne(getIdExterneCompte());
                manager.find(transaction);
                if (manager.size() != 1) {
                    _addError(transaction, label("COMPTE_ERROR_2"));
                } else {
                    setIdCompte(((CGPlanComptableViewBean) manager.getEntity(0)).getIdCompte());
                }
            }
        }

        updateOrCreateRemarque(transaction);
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        String fields = _getCollection() + _getTableName() + ".idecriture, " + _getCollection() + _getTableName()
                + ".idcompte, " + _getCollection() + _getTableName() + ".identeteecriture, " + _getCollection()
                + _getTableName() + ".idjournal, " + _getCollection() + _getTableName() + ".idexercomptable, "
                + _getCollection() + _getTableName() + ".idremarque, " + _getCollection() + _getTableName()
                + ".idcentrecharge, " + _getCollection() + _getTableName() + ".idmandat, " + _getCollection()
                + _getTableName() + ".date, " + _getCollection() + _getTableName() + ".datevaleur, " + _getCollection()
                + _getTableName() + ".piece, " + _getCollection() + _getTableName() + ".libelle, " + _getCollection()
                + _getTableName() + ".montant, " + _getCollection() + _getTableName() + ".montantmonnaie, "
                + _getCollection() + _getTableName() + ".coursmonnaie, " + _getCollection() + _getTableName()
                + ".codedebitcredit, " + _getCollection() + _getTableName() + ".referenceexterne, " + _getCollection()
                + _getTableName() + ".estpointee, " + _getCollection() + _getTableName() + ".estprovisoire, "
                + _getCollection() + _getTableName() + ".esterreur, " + _getCollection() + _getTableName()
                + ".estactive, " + _getCollection() + _getTableName() + ".idlog, " + _getCollection() + _getTableName()
                + ".idlivre, " + _getCollection() + _getTableName() + ".pspy";

        return fields;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CGEcritureViewBean.TABLE_CGECRIP;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idEcriture = statement.dbReadNumeric(CGEcritureViewBean.FIELD_IDECRITURE);
        idCompte = statement.dbReadNumeric(CGEcritureViewBean.FIELD_IDCOMPTE);
        idEnteteEcriture = statement.dbReadNumeric(CGEcritureViewBean.FIELD_IDENTETEECRITURE);
        idJournal = statement.dbReadNumeric(CGEcritureViewBean.FIELD_IDJOURNAL);
        idExerciceComptable = statement.dbReadNumeric(CGEcritureViewBean.FIELD_IDEXERCOMPTABLE);
        idRemarque = statement.dbReadNumeric(CGEcritureViewBean.FIELD_IDREMARQUE);
        idCentreCharge = statement.dbReadNumeric(CGEcritureViewBean.FIELD_IDCENTRECHARGE);
        idMandat = statement.dbReadNumeric(CGEcritureViewBean.FIELD_IDMANDAT);
        date = statement.dbReadDateAMJ(CGEcritureViewBean.FIELD_DATE);
        dateValeur = statement.dbReadDateAMJ(CGEcritureViewBean.FIELD_DATEVALEUR);
        piece = statement.dbReadString(CGEcritureViewBean.FIELD_PIECE);
        libelle = statement.dbReadString(CGEcritureViewBean.FIELD_LIBELLE);
        montant = statement.dbReadNumeric(CGEcritureViewBean.FIELD_MONTANT, 2);
        montantMonnaie = statement.dbReadNumeric(CGEcritureViewBean.FIELD_MONTANTMONNAIE, 2);
        coursMonnaie = statement.dbReadNumeric(CGEcritureViewBean.FIELD_COURSMONNAIE, 5);
        codeDebitCredit = statement.dbReadNumeric(CGEcritureViewBean.FIELD_CODEDEBITCREDIT);
        referenceExterne = statement.dbReadString(CGEcritureViewBean.FIELD_REFERENCEEXTERNE);
        estPointee = statement.dbReadBoolean(CGEcritureViewBean.FIELD_ESTPOINTEE);
        estProvisoire = statement.dbReadBoolean(CGEcritureViewBean.FIELD_ESTPROVISOIRE);
        estActive = statement.dbReadBoolean(CGEcritureViewBean.FIELD_ESTACTIVE);
        estErreur = statement.dbReadBoolean(CGEcritureViewBean.FIELD_ESTERREUR);
        idLog = statement.dbReadNumeric(CGEcritureViewBean.FIELD_IDLOG);
        idLivre = statement.dbReadNumeric(CGEcritureViewBean.FIELD_IDLIVRE);

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        super._validate(statement);

        // Si le montant est négatif, on le passe en extourne
        BigDecimal mtn = new BigDecimal(getMontantAffiche());
        if (mtn.floatValue() < 0) {
            if (CodeSystem.CS_CREDIT.equals(codeDebitCredit)) {
                setCodeDebitCredit(CodeSystem.CS_EXTOURNE_CREDIT);
            } else if (CodeSystem.CS_DEBIT.equals(codeDebitCredit)) {
                setCodeDebitCredit(CodeSystem.CS_EXTOURNE_DEBIT);
            }
        }

        /* compte : obligatoire */
        if (JadeStringUtil.isIntegerEmpty(getIdCompte())) {
            if (JadeStringUtil.isIntegerEmpty(getIdExterneCompte())) {
                _addError(statement.getTransaction(), label("COMPTE_ERROR_1"));
            } else {
                CGPlanComptableListViewBean manager = new CGPlanComptableListViewBean();
                manager.setSession(getSession());
                manager.setForIdExerciceComptable(getIdExerciceComptable());
                manager.setForIdExterne(getIdExterneCompte());
                manager.find(statement.getTransaction());
                if (manager.size() != 1) {
                    _addError(statement.getTransaction(), label("COMPTE_ERROR_2"));
                } else {
                    setIdCompte(((CGPlanComptableViewBean) manager.getEntity(0)).getIdCompte());
                }
            }
        }

        /*
         * le compte doit être ouvert dans le plan comptable de l'exercice et ne doit pas être verrouillé
         */
        CGPlanComptableViewBean plan = new CGPlanComptableViewBean();
        plan.setSession(getSession());
        plan.setIdExerciceComptable(getIdExerciceComptable());
        plan.setIdCompte(getIdCompte());

        plan.retrieve(statement.getTransaction());
        if (plan.isNew()) {
            // le compte ne fait pas partie du plan comptable l'exercice
            // comptable
            _addError(statement.getTransaction(), label("COMPTE_ERROR_3") + "id compte = " + getIdCompte());
        } else {
            /* le compte ne doit pas être verrouillé */
            if (plan.isEstVerrouille().booleanValue()) {
                _addError(statement.getTransaction(), label("COMPTE_ERROR_4"));
            }

            /* le compte ne doit pas être verrouillé aux écritures manuelles */
            if (plan.isEcritureManuelleEstVerrouille().booleanValue()) {
                CGJournal journal = new CGJournal();
                journal.setSession(getSession());
                journal.setIdJournal(getIdJournal());
                journal.retrieve(statement.getTransaction());

                if (journal.isNew()) {
                    _addError(statement.getTransaction(), label("SUPPR_ECRIT_ERROR_1"));
                    return;
                }

                if (CGJournal.CS_TYPE_MANUEL.equals(journal.getIdTypeJournal())) {
                    _addError(statement.getTransaction(), label("COMPTE_ERROR_5"));
                }
            }
        }

        if (JAUtil.isDateEmpty(getDate())) {
            _addError(statement.getTransaction(), label("DATE_NON_RENSEIGNE"));
        }
        if (JadeStringUtil.isBlank(getLibelle())) {
            _addError(statement.getTransaction(), label("LIBELLE_NON_RENSEIGNE"));
        }
        if ((getDateValeur() == null) || getDateValeur().equals("")) {
            setDateValeur(getDate());
        }

        if (!CGEcritureViewBean.CS_TYPE_ECRITURE_DETTE_AVOIR.equals(getEntete().getIdTypeEcriture())) {
            this.validateEcritureME(statement.getTransaction(), plan);
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(CGEcritureViewBean.FIELD_IDECRITURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEcriture(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(CGEcritureViewBean.FIELD_IDECRITURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEcriture(), "idEcriture"));
        statement.writeField(CGEcritureViewBean.FIELD_IDCOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField(CGEcritureViewBean.FIELD_IDENTETEECRITURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEnteteEcriture(), "idEnteteEcriture"));
        statement.writeField(CGEcritureViewBean.FIELD_IDJOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(CGEcritureViewBean.FIELD_IDEXERCOMPTABLE,
                this._dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), "idExerciceComptable"));
        statement.writeField(CGEcritureViewBean.FIELD_IDREMARQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField(CGEcritureViewBean.FIELD_IDCENTRECHARGE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCentreCharge(), "idCentreCharge"));
        statement.writeField(CGEcritureViewBean.FIELD_IDMANDAT,
                this._dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField(CGEcritureViewBean.FIELD_DATE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDate(), "date"));
        statement.writeField(CGEcritureViewBean.FIELD_DATEVALEUR,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateValeur(), "dateValeur"));
        statement.writeField(CGEcritureViewBean.FIELD_PIECE,
                this._dbWriteString(statement.getTransaction(), getPiece(), "piece"));
        statement.writeField(CGEcritureViewBean.FIELD_LIBELLE,
                this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(CGEcritureViewBean.FIELD_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), getMontantBase(), "montant"));
        statement.writeField(CGEcritureViewBean.FIELD_MONTANTMONNAIE,
                this._dbWriteNumeric(statement.getTransaction(), getMontantBaseMonnaie(), "montantMonnaie"));
        statement.writeField(CGEcritureViewBean.FIELD_COURSMONNAIE,
                this._dbWriteNumeric(statement.getTransaction(), getCoursMonnaie(), "coursMonnaie"));
        statement.writeField(CGEcritureViewBean.FIELD_CODEDEBITCREDIT,
                this._dbWriteNumeric(statement.getTransaction(), getCodeDebitCredit(), "codeDebitCredit"));
        statement.writeField(CGEcritureViewBean.FIELD_REFERENCEEXTERNE,
                this._dbWriteString(statement.getTransaction(), getReferenceExterne(), "referenceExterne"));
        statement.writeField(CGEcritureViewBean.FIELD_ESTPOINTEE, this._dbWriteBoolean(statement.getTransaction(),
                isEstPointee(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estPointee"));
        statement.writeField(CGEcritureViewBean.FIELD_ESTPROVISOIRE, this._dbWriteBoolean(statement.getTransaction(),
                isEstProvisoire(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estProvisoire"));
        statement.writeField(CGEcritureViewBean.FIELD_IDLOG,
                this._dbWriteNumeric(statement.getTransaction(), getIdLog(), "idLog"));
        statement.writeField(CGEcritureViewBean.FIELD_IDLIVRE,
                this._dbWriteNumeric(statement.getTransaction(), getIdLivre(), "idLivre"));

        statement.writeField(CGEcritureViewBean.FIELD_ESTACTIVE, this._dbWriteBoolean(statement.getTransaction(),
                isEstActive(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estPointee"));
        statement.writeField(CGEcritureViewBean.FIELD_ESTERREUR, this._dbWriteBoolean(statement.getTransaction(),
                isEstErreur(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estErreur"));

    }

    /**
     * Créer une nouvelle remarque
     * 
     * @param transaction
     * @throws Exception
     */
    private void createRemarque(globaz.globall.db.BTransaction transaction) throws Exception {
        CGRemarque remTemp = new CGRemarque();
        remTemp.setSession(getSession());
        remTemp.setRemarque(getRemarque());
        remTemp.add(transaction);

        setIdRemarque(remTemp.getIdRemarque());
    }

    /**
     * Suppression de la remarque liée.
     * 
     * @param transaction
     * @throws Exception
     */
    private void deleteRemarque(globaz.globall.db.BTransaction transaction) throws Exception {
        CGRemarque remTemp = new CGRemarque();
        remTemp.setSession(getSession());
        remTemp.setIdRemarque(getIdRemarque());
        remTemp.retrieve(transaction);

        if (!remTemp.isNew()) {
            remTemp.delete(transaction);
            setIdRemarque("0");
        }
    }

    public String generateNextId(globaz.globall.db.BTransaction transaction) throws Exception {
        nextId = this._incCounter(transaction, "0");
        return nextId;
    }

    /**
     * Date de création : (03.12.2002 12:43:53)
     * 
     * @return String
     */
    public String getAvoir() {

        if (CodeSystem.CS_CREDIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_CREDIT.equals(codeDebitCredit)) {
            return getMontantAffiche();
        } else {
            return "";
        }
    }

    /**
     * Date de création : (03.12.2002 12:43:53)
     * 
     * @return String
     */
    public String getAvoirMonnaie() {

        if (CodeSystem.CS_CREDIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_CREDIT.equals(codeDebitCredit)) {
            return getMontantAfficheMonnaie();
        } else {
            return "";
        }
    }

    /**
     * @return the centreChargeAffiche
     */
    public Boolean getCentreChargeAffiche() {
        return centreChargeAffiche;
    }

    @Override
    public BManager[] getChilds() {
        return null;
    }

    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    public String getCoursMonnaie() {
        return coursMonnaie;
    }

    public String getDate() {
        return date;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * Date de création : (03.12.2002 12:43:53)
     * 
     * @return String
     */
    public String getDoit() {

        if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit)) {
            return getMontantAffiche();
        } else {
            return "";
        }

    }

    /**
     * Date de création : (03.12.2002 12:43:53)
     * 
     * @return String
     */
    public String getDoitMonnaie() {

        if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit)) {
            return getMontantAfficheMonnaie();
        } else {
            return "";
        }

    }

    /**
     * @return
     */
    public CGEnteteEcritureViewBean getEntete() {
        if (!JadeStringUtil.isIntegerEmpty(getIdEnteteEcriture())) {
            CGEnteteEcritureViewBean entete = new CGEnteteEcritureViewBean();
            entete.setSession(getSession());
            entete.setIdEnteteEcriture(getIdEnteteEcriture());

            try {
                entete.retrieve();
            } catch (Exception e) {
                return null;
            }

            if (entete.isNew()) {
                return null;
            }

            return entete;
        } else {
            return null;
        }
    }

    /**
     * @return
     */
    public HashSet getExceptTypeAffichage() {
        HashSet except = new HashSet();
        // liste des cs qui ne devront pas figurer dans la liste
        try {
            if (!getExerciceComptable().getMandat().isUtiliseLivres().booleanValue()) {
                except.add(CodeSystem.CS_AFF_LIVRE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return except;
    }

    /**
     * Date de création : (28.03.2003 14:42:14)
     * 
     * @return String
     */
    public String getExternalReference() {
        return getDate() + " " + getIdExterneCompte() + " " + getLibelle() + " " + getMontantAffiche();
    }

    public String getIdCentreCharge() {
        return idCentreCharge;
    }

    public String getIdCompte() {
        return idCompte;
    }

    /**
     * Getter
     */
    public String getIdEcriture() {
        return idEcriture;
    }

    public String getIdEnteteEcriture() {
        return idEnteteEcriture;
    }

    @Override
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Date de création : (21.02.2003 16:29:08)
     * 
     * @return String
     */
    public String getIdExterne(globaz.globall.db.BTransaction transaction) {
        return retrieveCompte(transaction).getIdExterne();
    }

    /**
     * Date de création : (19.02.2003 14:28:02)
     * 
     * @return String
     */
    public String getIdExterneCompte() {
        return idExterneCompte;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdLivre() {
        return idLivre;
    }

    public String getIdLog() {
        return idLog;
    }

    public String getIdMandat() {
        return idMandat;
    }

    public String getIdRemarque() {
        return idRemarque;
    }

    /**
     * Date de création : (18.11.2002 18:13:41)
     * 
     * @return String
     */
    public String getJournalLibelle() {
        try {
            if (journal == null) {
                journal = new CGJournal();
                journal.setIdJournal(idJournal);
                journal.setSession(getSession());
                journal.retrieve();
            }
            return journal.getLibelle();
        } catch (Exception e) {
            e.printStackTrace();
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    @Override
    public String getLibelle() {
        return libelle;
    }

    /**
     * @return the libelleCompte
     */
    public String getLibelleCompte() {
        return libelleCompte;
    }

    public String getMontantAffiche() {
        BigDecimal bdMontant = JAUtil.createBigDecimal(montant);
        if (bdMontant != null) {
            if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit)) {
                String result = bdMontant.toString();
                return result;
            } else {
                String result = bdMontant.negate().toString();
                return result;
            }
        } else {
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    public String getMontantAfficheMonnaie() {
        BigDecimal bdMontant = JAUtil.createBigDecimal(montantMonnaie);
        if (bdMontant != null) {
            if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit)) {
                return bdMontant.toString();
            } else {
                return bdMontant.negate().toString();
            }
        } else {
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    public String getMontantBase() {
        // ???
        return montant;
    }

    public String getMontantBaseMonnaie() {
        return montantMonnaie;
    }

    /**
     * @return the montantEtrangerAffiche
     */
    public Boolean getMontantEtrangerAffiche() {
        return montantEtrangerAffiche;
    }

    public String getPiece() {
        return piece;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    /**
     * Date de création : (18.03.2003 11:29:48)
     * 
     * @return String
     */
    public String getRemarque() {
        return remarque;
    }

    /**
     * Date de création : (21.02.2003 16:31:21)
     * 
     * @return String
     */
    public String getSolde(globaz.globall.db.BTransaction transaction) {
        return retrieveCompte(transaction).getSolde();
    }

    /**
     * Date de création : (19.02.2003 15:13:29)
     * 
     * @return String
     */
    public String getSoldeCompte() {
        return soldeCompte;
    }

    /**
     * Date de création : (06.03.2003 09:35:03)
     * 
     * @return boolean
     */
    public boolean hasCGRemarque() {
        return ((getIdRemarque() != null) && !"0".equals(getIdRemarque()));
    }

    /**
     * @return
     */
    public boolean isAvoir() {
        if (CodeSystem.CS_CREDIT.equals(codeDebitCredit) || (CodeSystem.CS_EXTOURNE_CREDIT.equals(codeDebitCredit))) {
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    public boolean isDoit() {
        if (CodeSystem.CS_DEBIT.equals(codeDebitCredit) || (CodeSystem.CS_EXTOURNE_DEBIT.equals(codeDebitCredit))) {
            return true;
        }
        return false;
    }

    /**
     * L'entité est-elle égale à l'entité passée en paramètre ? <br/>
     * Utiliser pour n'effectuer que les mise à jours (depuis écran) nécessaire.
     * 
     * @param compareWith
     * @return
     */
    public boolean isEqualsTo(CGEcritureViewBean compareWith) {
        if (!getIdCompte().equals(compareWith.getIdCompte())) {
            return false;
        } else if (!getIdExterneCompte().equals(compareWith.getIdExterneCompte())) {
            return false;
        } else if (!getIdCentreCharge().equals(compareWith.getIdCentreCharge())) {
            return false;
        } else if (!getLibelle().equals(compareWith.getLibelle())) {
            return false;
        } else if (!getCodeDebitCredit().equals(compareWith.getCodeDebitCredit())) {
            return false;
        } else if (new FWCurrency(getMontantAffiche()).compareTo(new FWCurrency(compareWith.getMontantAffiche())) != 0) {
            return false;
        } else if (new FWCurrency(getMontantAfficheMonnaie()).compareTo(new FWCurrency(compareWith
                .getMontantAfficheMonnaie())) != 0) {
            return false;
        } else if (new BigDecimal(getCoursMonnaie()).compareTo(new BigDecimal(compareWith.getCoursMonnaie())) != 0) {
            return false;
        } else if (!getDate().equals(compareWith.getDate())) {
            return false;
        } else if (!getDateValeur().equals(compareWith.getDateValeur())) {
            return false;
        } else if (!getRemarque().equals(compareWith.getRemarque())) {
            return false;
        } else if (!getPiece().equals(compareWith.getPiece())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Date de création : (25.11.2002 13:48:22)
     * 
     * @return Boolean
     */
    public Boolean isEstActive() {
        return estActive;
    }

    /**
     * Date de création : (25.11.2002 13:48:50)
     * 
     * @return Boolean
     */
    public Boolean isEstErreur() {
        return estErreur;
    }

    /**
     * @return
     */
    public Boolean isEstPointee() {
        return estPointee;
    }

    /**
     * @return
     */
    public Boolean isEstProvisoire() {
        return estProvisoire;
    }

    /**
     * @return
     */
    public Boolean isFlagCentreCharge() {
        return flagCentreCharge;
    }

    /**
     * @return
     */
    public Boolean isFlagMonnaieEtrangere() {
        return flagMonnaieEtrangere;
    }

    /**
     * Date de création : (17.07.2003 16:25:29)
     * 
     * @return boolean
     */
    public boolean isForCompteAffillie(globaz.globall.db.BTransaction transaction) throws Exception {

        if (retrieveJournal().getExerciceComptable().getMandat().isEstComptabiliteAVS().booleanValue()) {
            if ((getIdExterneCompte() != null) && (getIdExterneCompte().trim().length() > 0)) {
                return "110".equals(getIdExterneCompte().substring(5, 8));
            } else {
                return "110".equals(getIdExterne(transaction).substring(5, 8));
            }
        }
        return false;
    }

    /**
     * @return
     */
    public boolean isJournalEditable() {
        try {
            retrieveJournal();

            try {
                CGGestionEcritureUtils.testSaisieAutresUtilisateurs(getSession(), journal);
            } catch (Exception e) {
                return false;
            }

            return ((journal != null) && !journal.isNew() && journal.isOuvert());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return
     */
    public Boolean isQuittancer() {
        return quittancer;
    }

    private String label(String code) {
        return getSession().getLabel(CGEcritureViewBean.LABEL_PREFIXE + code);
    }

    /**
     * Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    public CGRemarque retrieveCGRemarque() {
        if (hasCGRemarque()) {
            CGRemarque rem = new CGRemarque();
            try {
                rem.setSession(getSession());
                rem.setIdRemarque(getIdRemarque());
                rem.retrieve();
                return rem;
            } catch (Exception e) {
                e.printStackTrace();
                return rem;
            }
        } else {
            return null;
        }
    }

    /**
     * Date de création : (21.02.2003 15:14:38)
     * 
     * @return globaz.helios.db.comptes.CGPlanComptableViewBean
     */
    public CGPlanComptableViewBean retrieveCompte(globaz.globall.db.BTransaction transaction) {
        if ((compte == null) || !compte.getIdCompte().equals(getIdCompte())) {
            try {
                CGPlanComptableListViewBean manager = new CGPlanComptableListViewBean();
                manager.setSession(getSession());
                manager.setForIdCompte(getIdCompte());
                manager.setForIdExerciceComptable(getIdExerciceComptable());
                manager.find(transaction, 2);
                if (manager.size() != 1) {
                    throw (new Exception());
                }
                compte = (CGPlanComptableViewBean) manager.getEntity(0);
            } catch (Exception e) {
                // e.printStackTrace();
                compte = null;
            }

        }
        return compte;
    }

    /**
     * @return
     * @throws Exception
     */
    public CGEcritureViewBean retrieveContrepartie() throws Exception {

        if (contrepartie == null) {
            CGEnteteEcritureViewBean entete = new CGEnteteEcritureViewBean();
            entete.setIdEnteteEcriture(idEnteteEcriture);
            entete.setSession(getSession());
            entete.retrieve();

            if ((entete == null) || entete.isNew()) {
                throw (new Exception(label("ENTETE_INEXISTANTE")));
            }

            if (isDoit()) {
                contrepartie = entete.retrieveContrepartieAvoir();
            }
            if (isAvoir()) {
                contrepartie = entete.retrieveContrepartieDoit();
            }
        }
        return contrepartie;
    }

    /**
     * Date de création : (18.11.2002 18:13:41)
     * 
     * @return String
     */
    public CGJournal retrieveJournal() {
        try {
            if (journal == null) {
                journal = new CGJournal();
                journal.setIdJournal(idJournal);
                journal.setSession(getSession());
                journal.retrieve();
            }
            return journal;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param centreChargeAffiche
     *            the centreChargeAffiche to set
     */
    public void setCentreChargeAffiche(Boolean centreChargeAffiche) {
        this.centreChargeAffiche = centreChargeAffiche;
    }

    /**
     * @param newCodeDebitCredit
     */
    public void setCodeDebitCredit(String newCodeDebitCredit) {
        codeDebitCredit = newCodeDebitCredit;
    }

    /**
     * Date de création : (21.02.2003 15:14:38)
     * 
     * @param newCompte
     *            globaz.helios.db.comptes.CGPlanComptableViewBean
     */
    public void setCompte(CGPlanComptableViewBean newCompte) {
        compte = newCompte;
    }

    /**
     * @param newCoursMonnaie
     */
    public void setCoursMonnaie(String newCoursMonnaie) {
        coursMonnaie = newCoursMonnaie;
    }

    /**
     * @param newDate
     */
    public void setDate(String newDate) {
        date = newDate;
    }

    /**
     * @param newDateValeur
     */
    public void setDateValeur(String newDateValeur) {
        dateValeur = newDateValeur;
    }

    /**
     * @param newEstActive
     */
    public void setEstActive(Boolean newEstActive) {
        estActive = newEstActive;
    }

    /**
     * Date de création : (25.11.2002 13:48:50)
     * 
     * @param newEstErreur
     *            Boolean
     */
    public void setEstErreur(Boolean newEstErreur) {
        estErreur = newEstErreur;
    }

    /**
     * @param newEstPointee
     */
    public void setEstPointee(Boolean newEstPointee) {
        estPointee = newEstPointee;
    }

    /**
     * @param newEstProvisoire
     */
    public void setEstProvisoire(Boolean newEstProvisoire) {
        estProvisoire = newEstProvisoire;
    }

    /**
     * @param value
     */
    public void setFlagCentreCharge(Boolean value) {
        flagCentreCharge = value;
    }

    /**
     * @param value
     */
    public void setFlagMonnaieEtrangere(Boolean value) {
        flagMonnaieEtrangere = value;
    }

    /**
     * @param newIdCentreCharge
     */
    public void setIdCentreCharge(String newIdCentreCharge) {
        idCentreCharge = newIdCentreCharge;
    }

    /**
     * @param newIdCompte
     */
    public void setIdCompte(String newIdCompte) {
        idCompte = newIdCompte;
    }

    /**
     * Setter
     */
    public void setIdEcriture(String newIdEcriture) {
        idEcriture = newIdEcriture;
    }

    /**
     * @param newIdEnteteEcriture
     */
    public void setIdEnteteEcriture(String newIdEnteteEcriture) {
        idEnteteEcriture = newIdEnteteEcriture;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.helios.db.interfaces.CGNeedExerciceComptable#setIdExerciceComptable (java.lang.String)
     */
    @Override
    public void setIdExerciceComptable(String newIdExerciceComptable) {
        idExerciceComptable = newIdExerciceComptable;
    }

    /**
     * Date de création : (19.02.2003 14:28:02)
     * 
     * @param newIdExterneCompte
     *            String
     */
    public void setIdExterneCompte(String newIdExterneCompte) {
        idExterneCompte = newIdExterneCompte;
    }

    /**
     * @param newIdJournal
     */
    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * @param newIdLivre
     */
    public void setIdLivre(String newIdLivre) {
        idLivre = newIdLivre;
    }

    /**
     * @param newIdLog
     */
    public void setIdLog(String newIdLog) {
        idLog = newIdLog;
    }

    /**
     * @param newIdMandat
     */
    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * @param newIdRemarque
     */
    public void setIdRemarque(String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    /**
     * @param newLibelle
     */
    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
    }

    /**
     * @param libelleCompte
     *            the libelleCompte to set
     */
    public void setLibelleCompte(String libelleCompte) {
        this.libelleCompte = libelleCompte;
    }

    // !!! Attention, manipulation du montant !!!
    // En effet, un montant est donné POSITIF pour un débit et un crédit, et il
    // est enregistré NEGATIF pour un crédit
    // Donc quand c'est un crédit, on inverse le signe :D
    public void setMontant(String newMontant) throws Exception {
        montant = JANumberFormatter.deQuote(newMontant);
        BigDecimal bdMontant = JAUtil.createBigDecimal(montant);
        if (JadeStringUtil.isBlank(codeDebitCredit)) {
            throw new Exception(label("MONTANT_ERROR"));
        }
        if ((bdMontant != null)
                && (CodeSystem.CS_CREDIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_CREDIT
                        .equals(codeDebitCredit))) {
            montant = bdMontant.negate().toString();
        }
    }

    /**
     * @param newMontant
     */
    public void setMontantBase(String newMontant) {
        montant = newMontant;
    }

    /**
     * @param montantEtrangerAffiche
     *            the montantEtrangerAffiche to set
     */
    public void setMontantEtrangerAffiche(Boolean montantEtrangerAffiche) {
        this.montantEtrangerAffiche = montantEtrangerAffiche;
    }

    // !!! Attention, manipulation du montant !!!
    // En effet, un montant est donné POSITIF pour un débit et un crédit, et il
    // est enregistré NEGATIF pour un crédit
    // Donc quand c'est un crédit, on inverse le signe :D
    public void setMontantMonnaie(String newMontantMonnaie) throws Exception {
        montantMonnaie = JANumberFormatter.deQuote(newMontantMonnaie);
        BigDecimal bdMontant = JAUtil.createBigDecimal(montantMonnaie);
        if (JadeStringUtil.isBlank(codeDebitCredit)) {
            throw new Exception(label("MONTANT_ERROR"));
        }
        if ((bdMontant != null)
                && (CodeSystem.CS_CREDIT.equals(codeDebitCredit) || CodeSystem.CS_EXTOURNE_CREDIT
                        .equals(codeDebitCredit))) {
            montantMonnaie = bdMontant.negate().toString();
        }
    }

    /**
     * @param newMontantMonnaie
     * @throws Exception
     */
    public void setMontantMonnaieBase(String newMontantMonnaie) throws Exception {
        montantMonnaie = newMontantMonnaie;
    }

    /**
     * @param newPiece
     */
    public void setPiece(String newPiece) {
        piece = newPiece;
    }

    /**
     * @param value
     */
    public void setQuittancer(Boolean value) {
        quittancer = value;

    }

    /**
     * @param newReferenceExterne
     */
    public void setReferenceExterne(String newReferenceExterne) {
        referenceExterne = newReferenceExterne;
    }

    /**
     * Date de création : (18.03.2003 11:29:48)
     * 
     * @param newRemarque
     *            String
     */
    public void setRemarque(String newRemarque) {
        remarque = newRemarque;
    }

    /**
     * Date de création : (19.02.2003 15:13:29)
     * 
     * @param newSoldeCompte
     *            String
     */
    public void setSoldeCompte(String newSoldeCompte) {
        soldeCompte = newSoldeCompte;
    }

    /**
     * Mise à jour de la remarque ou créer la remarque si inexistant.
     * 
     * @param transaction
     * @throws Exception
     */
    private void updateOrCreateRemarque(globaz.globall.db.BTransaction transaction) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
            if (!JadeStringUtil.isBlank(getRemarque())) {
                createRemarque(transaction);
            }
        } else {
            CGRemarque remTemp = new CGRemarque();
            remTemp.setSession(getSession());
            remTemp.setIdRemarque(getIdRemarque());
            remTemp.retrieve(transaction);

            if (remTemp.isNew()) {
                createRemarque(transaction);
            } else if (!remTemp.getRemarque().equals(getRemarque())) {
                if (JadeStringUtil.isBlank(getRemarque())) {
                    remTemp.delete(transaction);
                    setIdRemarque("0");
                } else {
                    remTemp.setRemarque(getRemarque());
                    remTemp.update(transaction);
                }
            }
        }
    }

    public void validateEcritureME(BTransaction transaction) throws Exception {

        CGPlanComptableViewBean plan = new CGPlanComptableViewBean();
        // Récupération du compte liée à l'écriture
        if (JadeStringUtil.isBlank(getIdCompte())) {
            CGPlanComptableListViewBean manager = new CGPlanComptableListViewBean();
            manager.setSession(getSession());
            manager.setForIdExerciceComptable(getIdExerciceComptable());
            manager.setForIdExterne(getIdExterneCompte());
            manager.find(transaction, 2);
            if (manager.size() != 1) {
                throw new Exception("CGEcritureViewBean.validateEcritureME() : " + label("ECRITURE_COMPTE_ERROR_2")
                        + " " + getIdExterneCompte());
            } else {
                plan = (CGPlanComptableViewBean) manager.getEntity(0);
            }
        } else {

            plan.setSession(getSession());
            plan.setIdCompte(getIdCompte());
            plan.setIdExerciceComptable(getIdExerciceComptable());
            plan.retrieve(transaction);
        }

        if (plan.isNew()) {
            // le compte ne fait pas partie du plan comptable l'exercice
            // comptable
            _addError(transaction, label("COMPTE_ERROR_3") + "id compte = " + getIdCompte());
        } else {
            /* le compte ne doit pas être verrouillé */
            if (plan.isEstVerrouille().booleanValue()) {
                _addError(transaction, label("COMPTE_ERROR_4"));
            }
        }

        this.validateEcritureME(transaction, plan);
    }

    private void validateEcritureME(BTransaction transaction, CGPlanComptableViewBean plan) throws Exception {

        boolean isMontantMonnaieEtrangere = false;
        boolean isCours = false;
        boolean isMontantCHF = false;

        // un montant en monnaie étrangère est saisie
        if ((getMontantBaseMonnaie() != null) && (getMontantBaseMonnaie().trim().length() > 0)
                && !getMontantBaseMonnaie().startsWith("0.00") && !getMontantBaseMonnaie().equals("0")) {
            isMontantMonnaieEtrangere = true;
        }
        // le cours est saisie
        if ((getCoursMonnaie() != null) && (getCoursMonnaie().trim().length() > 0)
                && !getCoursMonnaie().startsWith("0.00") && !getCoursMonnaie().equals("0")) {
            isCours = true;
        }
        // un montant chf est saisi
        if ((getMontantBase() != null) && (getMontantBase().trim().length() > 0)
                && !getMontantBase().startsWith("0.00") && !getMontantBase().equals("0")) {
            isMontantCHF = true;
        }

        // Compte de nature monnaie étrangère !!!
        if (CGCompte.CS_MONNAIE_ETRANGERE.equals(plan.getIdNature())) {

            // Règle de plausibilités :
            // montant CHF + cours --> ok
            // montant CHF + montant Monnaie --> ok
            // montant Monnaie + cours --> ok
            // montant CHF + cours + montant monnaie --> ok
            // montant CHF sur comptes CHF ok

            // Calcul des valeurs manquantes
            // montant CHF / cours = montant monnaie
            // montant monnaie * cours = montant CHF
            // montant CHF / montant monnaie = cours

            // plausi : montant CHF + cours --> calcul montant monnaie
            // plausi : montant CHF + cours + montant monnaie

            // Il doit être possible de saisir un montant CHF uniquement sur un
            // compte en monnaie étrangère.
            // plausi: montant CHF <> 0 && cours == 0 && montant Monnaie == 0
            // --> ok

            if (isMontantCHF && (getCoursMonnaie() != null) && (getMontantBaseMonnaie() != null)
                    && JadeStringUtil.isIntegerEmpty(getCoursMonnaie())
                    && JadeStringUtil.isIntegerEmpty(getMontantBaseMonnaie())) {
                ;// OK rien à faire
            } else if (isMontantCHF && isMontantMonnaieEtrangere && isCours) {
                // OK, rien à faire
            } else if (isMontantCHF && isCours && !isMontantMonnaieEtrangere) {
                BigDecimal montant = new BigDecimal(getMontantBase());
                montant = montant.setScale(5);
                BigDecimal cours = new BigDecimal(getCoursMonnaie());
                montant = montant.divide(cours, BigDecimal.ROUND_HALF_DOWN);

                // Arrondi au centimes, sur 2 digit après la virgule.
                FWCurrency montantMonnaie = new FWCurrency(JANumberFormatter.format(montant.toString(), 0.01, 2,
                        JANumberFormatter.NEAR));
                setMontantMonnaieBase(montantMonnaie.toString());
            }
            // plausi : montant CHF + montant Monnaie --> calcul cours
            else if (isMontantCHF && isMontantMonnaieEtrangere && !isCours) {

                BigDecimal montant1 = new BigDecimal(getMontantBase());
                BigDecimal montant2 = new BigDecimal(getMontantBaseMonnaie());
                BigDecimal cours = montant1.divide(montant2, 5, BigDecimal.ROUND_HALF_EVEN);
                setCoursMonnaie(JANumberFormatter.format(cours.toString(), 0.00001, 5, JANumberFormatter.NEAR));
            }
            // plausi montant Monnaie * cours
            else if (isMontantMonnaieEtrangere && isCours && !isMontantCHF) {
                BigDecimal montantMonnaie = new BigDecimal(getMontantBaseMonnaie());
                BigDecimal cours = new BigDecimal(getCoursMonnaie());
                montantMonnaie = montantMonnaie.multiply(cours);

                // Arrondi au centimes, sur 2 digit après la virgule.
                FWCurrency montant = new FWCurrency(JANumberFormatter.format(montantMonnaie.toString(), 0.01, 2,
                        JANumberFormatter.NEAR));
                setMontantBase(montant.toString());
            }
            // error, des paramètres sont manquant
            else {
                _addError(transaction, label("ECR_CPT_MON_ETR_DATA_MISSING"));
            }
        } else {
            if (isMontantCHF) {
                setMontantMonnaie(null);
                setCoursMonnaie(null);
            }
            // Dans le cas d'une écriture double sur un compte en monnaie
            // étrangère et un compte
            // en monnaie CHF, lors de la validation du compte en monnaie CHF,
            // on passe par ici !!!
            // avec peut-être le montant en CHF non setté.
            else {
                if (!isMontantMonnaieEtrangere || !isCours) {
                    _addError(transaction, label("ECR_CPT_MON_ETR_DATA_MISSING"));
                }

                // montant CHF = montant monnaie * cours
                // calcul du montant CHF :
                BigDecimal montantMonnaie = new BigDecimal(getMontantBaseMonnaie());
                BigDecimal cours = new BigDecimal(getCoursMonnaie());
                montantMonnaie = montantMonnaie.multiply(cours);

                // Arrondi au centimes, sur 2 digit après la virgule.
                FWCurrency montant = new FWCurrency(JANumberFormatter.format(montantMonnaie.toString(), 0.01, 2,
                        JANumberFormatter.NEAR));
                setMontantBase(montant.toString());
            }
        }
    }

    /**
     * Date de création : (25.11.2002 13:48:22)
     * 
     * @param newEstActive
     *            Boolean
     */
    public void wantEstActive(Boolean newEstActive) {
        estActive = newEstActive;
    }

    /**
     * @param newEstProvisoire
     */
    public void wantEstProvisoire(Boolean newEstProvisoire) {
        estProvisoire = newEstProvisoire;
    }

}
