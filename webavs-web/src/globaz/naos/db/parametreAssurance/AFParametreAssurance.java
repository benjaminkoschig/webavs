package globaz.naos.db.parametreAssurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.translation.CodeSystem;

/**
 * La classe définissant l'entité ParametreAssurance.
 * 
 * @author sau
 */
public class AFParametreAssurance extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ASSUR_ID = "MBIASS";
    public static final String FIELDNAME_PARAM_ASSUR_ID = "MXIPAR";
    public static final String TABLE_NAME = "AFPARAS";
    private AFAssurance _assurance = null;
    // Foreign Key
    private java.lang.String assuranceId = new String();
    // Fields
    private java.lang.String dateDebut = new String();
    private java.lang.String dateFin = new String();

    /*
     * private FWParametersSystemCode csCodeSexe = null; private FWParametersSystemCode csGenre = null;
     */

    private java.lang.String genre = new String();
    // DB
    // Primary Key
    private java.lang.String parametreAssuranceId = new String();

    private java.lang.String sexe = new String();

    private java.lang.String valeur = new String();
    private java.lang.String valeurAlpha = new String();
    private java.lang.String valeurNum = new String();

    /**
     * Constructeur d'AFParametreAssurance.
     */
    public AFParametreAssurance() {
        super();
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setParametreAssuranceId(this._incCounter(transaction, "0"));

        if (CodeSystem.GEN_PARAM_ASS_PLAFOND.equalsIgnoreCase(getGenre()) && (getAssurance() != null)
                && !JadeStringUtil.isBlankOrZero(getAssurance().getIdAssuranceReference())) {
            transaction.addErrors(getSession().getLabel("PLAFOND_INTERDIT_FOR_ASS_WITH_ASS_REF"));
        }

    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return AFParametreAssurance.TABLE_NAME; // "AFPARAS";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        parametreAssuranceId = statement.dbReadNumeric("MXIPAR");
        assuranceId = statement.dbReadNumeric("MBIASS");
        dateDebut = statement.dbReadDateAMJ("MXDDEB");
        // dateFin = statement.dbReadDateAMJ("MXDFIN");
        sexe = statement.dbReadNumeric("MXTSEX");
        genre = statement.dbReadNumeric("MXTGEN");
        valeurNum = statement.dbReadNumeric("MXNVAL", 2);
        valeurAlpha = statement.dbReadString("MXLVAL");
    };

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        // Contrôle que les champs obligatoires soient renseignés
        validationOK &= _propertyMandatory(statement.getTransaction(), getGenre(), getSession().getLabel("1320"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getDateDebut(), getSession().getLabel("20"));
        // Autoriser 0 pour la cotisation minimum
        if (!getGenre().equals(CodeSystem.GEN_PARAM_ASS_COTISATION_MIN)) {
            validationOK &= _propertyMandatory(statement.getTransaction(), valeur, getSession().getLabel("1330"));
        }

        validationOK &= _checkRealDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("160"));

        if (getGenre().equals(CodeSystem.GEN_PARAM_ASS_CODE_CALC_AGE_MIN)
                || getGenre().equals(CodeSystem.GEN_PARAM_ASS_CODE_CALC_AGE_MAX)
                || getGenre().equals(CodeSystem.GEN_PARAM_ASS_FACT_EMPLOYE)
                || getGenre().equals(CodeSystem.GEN_PARAM_ASS_REMISE)
                || getGenre().equals(CodeSystem.GEN_PARAM_ASS_REDUCTION)) {
            setValeurAlpha(valeur);
        } else if (getGenre().equals(CodeSystem.GEN_PARAM_ASS_EXCLUSION_CAT)) {
            // ne pas insérer d'exception de catégorie pour les assurance
            // référence
            if ((getAssurance() != null) && !JadeStringUtil.isIntegerEmpty(getAssurance().getIdAssuranceReference())) {
                _addError(statement.getTransaction(), getSession().getLabel("PARTICULARITE_ASSURANCE_EXCEPTION"));
                validationOK = false;
            } else {
                setValeurAlpha(getSession().getSystemCode("CICATPER", valeur));
            }
        } else {
            setValeurNum(valeur);
        }

        // _validationDate(statement.getTransaction());
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MXIPAR", this._dbWriteNumeric(statement.getTransaction(), getParametreAssuranceId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MXIPAR",
                this._dbWriteNumeric(statement.getTransaction(), getParametreAssuranceId(), "parametreAssuranceId"));
        statement.writeField("MBIASS",
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), "assuranceId"));
        statement.writeField("MXDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        // statement.writeField("MXDFIN",_dbWriteDateAMJ(statement.getTransaction(),
        // getDateFin(),"dateFin"));
        statement.writeField("MXTSEX", this._dbWriteNumeric(statement.getTransaction(), getSexe(), "sexe"));
        statement.writeField("MXTGEN", this._dbWriteNumeric(statement.getTransaction(), getGenre(), "genre"));
        statement.writeField("MXNVAL", this._dbWriteNumeric(statement.getTransaction(), getValeurNum(), "valeurNum"));
        statement
                .writeField("MXLVAL", this._dbWriteString(statement.getTransaction(), getValeurAlpha(), "valeurAlpha"));
    }

    /**
     * Rechercher l'assurance du parametre assurance en fonction de son ID.
     * 
     * @return l'assurance
     */
    public AFAssurance getAssurance() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAssuranceId())) {
            return null;
        }

        if (_assurance == null) {

            _assurance = new AFAssurance();
            _assurance.setSession(getSession());
            _assurance.setAssuranceId(getAssuranceId());
            try {
                _assurance.retrieve();
                /*
                 * if (_assurance.hasErrors()) _assurance = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _assurance = null;
            }
        }
        return _assurance;
    }

    /*
     * Control les Dates de Début et de Fin.
     * 
     * @param transaction
     * 
     * @throws java.lang.Exception
     */
    /*
     * private void _validationDate(BTransaction transaction) throws Exception {
     * 
     * // ********************************************************* // Test Date de Début, Date de Fin //
     * *********************************************************
     * 
     * AFParametreAssuranceListViewBean paramAssList = new AFParametreAssuranceListViewBean();
     * paramAssList.setForIdAssurance(getAssuranceId()); paramAssList.setForGenre(getGenre());
     * paramAssList.setSession(getSession()); paramAssList.find(transaction);
     * 
     * for (int i = 0; i < paramAssList.size(); i++) {
     * 
     * AFParametreAssurance paramAssurance = (AFParametreAssurance)paramAssList.getEntity(i);
     * 
     * // Ne pas tester le parametre avec lui meme if (! paramAssurance.getParametreAssuranceId
     * ().equalsIgnoreCase(getParametreAssuranceId())) {
     * 
     * // Teste le sexe if (JAUtil.isIntegerEmpty(getSexe()) || JAUtil.isIntegerEmpty(paramAssurance.getSexe()) ||
     * getSexe().equals(paramAssurance.getSexe())) {
     * 
     * if (JAUtil.isIntegerEmpty(getDateFin())) {
     * 
     * // Test si il y a déjà une affiliation sans une date de fin if
     * (JAUtil.isIntegerEmpty(paramAssurance.getDateFin())) { _addError(transaction,
     * FWMessageFormat.format(getSession().getLabel("1340"), CodeSystem.getLibelle(getSession(), getGenre())));
     * 
     * } else { // Test si il n'y a pas de chevauchement avec une affiliation if
     * (BSessionUtil.compareDateFirstLowerOrEqual(transaction.getSession(), getDateDebut(),
     * paramAssurance.getDateFin())) {
     * 
     * _addError(transaction, FWMessageFormat.format(getSession().getLabel("1350"),
     * getDateDebut(),paramAssurance.getDateDebut(), paramAssurance.getDateFin())); } } } else { if
     * (BSessionUtil.compareDateFirstLower(transaction.getSession(), getDateDebut(), getDateFin())) {
     * 
     * // Test si il n'y a pas de chevauchement pour un nouvelle affiliation // avec date de fin avec un affiliation
     * sans date de fin if (JAUtil.isIntegerEmpty(paramAssurance.getDateFin())) {
     * 
     * if (BSessionUtil.compareDateFirstGreaterOrEqual(transaction.getSession(), getDateFin(),
     * paramAssurance.getDateDebut())) {
     * 
     * _addError(transaction, FWMessageFormat.format(getSession().getLabel("1360"), getDateDebut(), getDateFin(),
     * paramAssurance.getDateDebut())); }
     * 
     * } else { if (BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(), paramAssurance.getDateDebut(),
     * paramAssurance.getDateFin(), getDateDebut()) || BSessionUtil.compareDateBetweenOrEqual(transaction.getSession(),
     * paramAssurance.getDateDebut(), paramAssurance.getDateFin(), getDateFin())) {
     * 
     * _addError(transaction, FWMessageFormat.format(getSession().getLabel("1370"), getDateDebut(), getDateFin(),
     * paramAssurance.getDateDebut(), paramAssurance.getDateFin())); } } } } } } } }
     */

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getAssuranceId() {
        return assuranceId;
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    public java.lang.String getGenre() {
        return genre;
    }

    public java.lang.String getParametreAssuranceId() {
        return parametreAssuranceId;
    }

    public java.lang.String getSexe() {
        return sexe;
    }

    public java.lang.String getValeur() {
        if (getGenre().equals(CodeSystem.GEN_PARAM_ASS_CODE_CALC_AGE_MIN)
                || getGenre().equals(CodeSystem.GEN_PARAM_ASS_CODE_CALC_AGE_MAX)
                || getGenre().equals(CodeSystem.GEN_PARAM_ASS_FACT_EMPLOYE)
                || getGenre().equals(CodeSystem.GEN_PARAM_ASS_REMISE)
                || getGenre().equals(CodeSystem.GEN_PARAM_ASS_REDUCTION)) {
            return getValeurAlpha();
        }
        if (getGenre().equals(CodeSystem.GEN_PARAM_ASS_EXCLUSION_CAT)) {
            return getSession().getCode(getValeurAlpha());
        } else {
            return getValeurNum();
        }
    }

    public java.lang.String getValeurAlpha() {
        return valeurAlpha;
    }

    public java.lang.String getValeurNum() {
        return JANumberFormatter.fmt(valeurNum.toString(), true, false, true, 2);
    }

    public void setAssuranceId(java.lang.String string) {
        assuranceId = string;
    }

    /*
     * Le Code System de CodeSexe
     * 
     * @return le Code System
     */
    /*
     * public FWParametersSystemCode getCsCodeSexe() { if (csCodeSexe == null) { // liste pas encore chargee, on la
     * charge csCodeSexe = new FWParametersSystemCode(); csCodeSexe.getCode(getSexe()); } return csCodeSexe; }
     */

    /*
     * Le Code System de Genre
     * 
     * @return le Code System
     */
    /*
     * public FWParametersSystemCode getCsGenre() { if (csGenre == null) { // liste pas encore chargee, on la charge
     * csGenre = new FWParametersSystemCode(); csGenre.getCode(getGenre()); } return csGenre; }
     */

    // *******************************************************
    // Setter
    // *******************************************************

    public void setDateDebut(java.lang.String string) {
        dateDebut = string;
    }

    public void setDateFin(java.lang.String string) {
        dateFin = string;
    }

    public void setGenre(java.lang.String string) {
        genre = string;
    }

    public void setParametreAssuranceId(java.lang.String string) {
        parametreAssuranceId = string;
    }

    public void setSexe(java.lang.String string) {
        sexe = string;
    }

    public void setValeur(java.lang.String string) {
        valeur = string;
    }

    public void setValeurAlpha(java.lang.String string) {
        valeurAlpha = string;
    }

    public void setValeurNum(java.lang.String string) {
        valeurNum = JANumberFormatter.deQuote(string);
    }

}
