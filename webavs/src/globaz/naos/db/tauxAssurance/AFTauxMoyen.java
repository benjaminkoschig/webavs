package globaz.naos.db.tauxAssurance;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import java.math.BigDecimal;

/**
 * La classe définissant l'entité TauxMoyen, utilisée dans la gestion des taux variables par palier.
 * 
 * @author dgi
 */
public class AFTauxMoyen extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_PARAM_AFF_ID = "MAIAFF";
    public static final String FIELDNAME_PARAM_BLOCAGE = "AOBBLO";
    public static final String FIELDNAME_PARAM_MASSE = "AONMAS";
    public static final String FIELDNAME_PARAM_MOIS = "AONNMO";
    public static final String FIELDNAME_PARAM_PRMIARY = "AOITAU";
    public static final String FIELDNAME_PARAM_TAUX_ID = "MCITAU";
    // DB
    public static final String TABLE_NAME = "AFTAUMP";
    private AFAffiliation _affiliation = null;
    // Foreign Key
    private java.lang.String affiliationId = new String();
    private java.lang.Boolean blocage = new Boolean(false);
    // private java.lang.String tauxId = new String();
    // Fields
    private java.lang.String masseAnnuelle = new String();
    private java.lang.String nbrMois = "12";
    // liens
    private AFTauxAssurance taux = null;
    // Primary Key
    private java.lang.String tauxMoyenId = new String();

    /**
     * Constructeur d'AFTauxAssurance.
     */
    public AFTauxMoyen() {
        super();
        // setMethodsToLoad(IAFTauxAssuranceHelper.METHODS_TO_LOAD);
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        // ajouter le taux
        getTaux().add(transaction);
        // mise à jour de l'id sans afterUpdate
        wantCallMethodAfter(false);
        this.update(transaction);
        wantCallMethodAfter(true);
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
    }

    @Override
    protected void _afterRetrieveWithResultSet(BStatement statement) throws java.lang.Exception {
        // lire la remarque depuis la jointure
        taux = new AFTauxAssurance();
        taux.read(statement);
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getTaux().getTauxAssuranceId())) {
            transaction.disableSpy();
            getTaux().update(transaction);
            transaction.enableSpy();
        }
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setTauxMoyenId(this._incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getTaux().getTauxAssuranceId())) {
            transaction.disableSpy();
            getTaux().delete(transaction);
        }
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String fromClause = _getCollection() + AFTauxMoyen.TABLE_NAME;
        // ajout jointure sur assurance
        // fromClause += " INNER JOIN " + _getCollection() + "AFAFFIP ON " +
        // _getCollection() + AFTauxMoyen.TABLE_NAME + "." +
        // AFTauxMoyen.FIELDNAME_PARAM_AFF_ID + "=" + _getCollection() +
        // "AFAFFIP.MAIAFF";
        fromClause += " INNER JOIN " + _getCollection() + "AFTAUXP ON " + _getCollection() + AFTauxMoyen.TABLE_NAME
                + "." + AFTauxMoyen.FIELDNAME_PARAM_TAUX_ID + "=" + _getCollection() + "AFTAUXP.MCITAU";
        return fromClause;
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return AFTauxMoyen.TABLE_NAME;
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        tauxMoyenId = statement.dbReadNumeric(AFTauxMoyen.FIELDNAME_PARAM_PRMIARY);
        affiliationId = statement.dbReadNumeric(AFTauxMoyen.FIELDNAME_PARAM_AFF_ID);
        getTaux().setTauxAssuranceId(statement.dbReadNumeric(AFTauxMoyen.FIELDNAME_PARAM_TAUX_ID));
        masseAnnuelle = statement.dbReadNumeric(AFTauxMoyen.FIELDNAME_PARAM_MASSE);
        nbrMois = statement.dbReadNumeric(AFTauxMoyen.FIELDNAME_PARAM_MOIS);
        blocage = statement.dbReadBoolean(AFTauxMoyen.FIELDNAME_PARAM_BLOCAGE);
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        boolean validationOK = true;
        validationOK &= _propertyMandatory(statement.getTransaction(), getAffiliationId(),
                getSession().getLabel("TAUX_MOYEN_AFFILIATION"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceId(),
                getSession().getLabel("TAUX_MOYEN_ASSURANCE"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getAnnee(),
                getSession().getLabel("TAUX_MOYEN_ANNEE"));

        if (validationOK) {
            // il ne doit pas exister une autre entrée pour l'année en question
            AFTauxMoyenManager mgr = new AFTauxMoyenManager();
            mgr.setSession(getSession());
            mgr.setForIdAffiliation(getAffiliationId());
            mgr.setForAnnee(getAnnee());
            mgr.find();
            if ((mgr.size() != 0) && !getTauxMoyenId().equals(mgr.getFirstEntity().getId())) {
                _addError(statement.getTransaction(), getSession().getLabel("TAUX_MOYEN_EXIST"));
            }
        }

        // si taux non renseigné, la masse salariale et le nombre de mois sont
        // obligatoire
        if (validationOK && JadeStringUtil.isDecimalEmpty(getTauxTotal())) {
            validationOK &= _propertyMandatory(statement.getTransaction(), getNbrMois(),
                    getSession().getLabel("TAUX_MOYEN_NBR"));
            if (JadeStringUtil.isEmpty(getMasseAnnuelle())) {
                _addError(statement.getTransaction(), getSession().getLabel("TAUX_MOYEN_MASSE"));
                validationOK = false;
            }
            // si ok, calcul du taux
            if (validationOK) {
                initValeurTaux();
                if (JadeStringUtil.isDecimalEmpty(getTauxTotal())) {
                    _addError(statement.getTransaction(), getSession().getLabel("TAUX_MOYEN_TAUX"));
                }
            }
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(AFTauxMoyen.FIELDNAME_PARAM_PRMIARY,
                this._dbWriteNumeric(statement.getTransaction(), getTauxMoyenId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(AFTauxMoyen.FIELDNAME_PARAM_PRMIARY,
                this._dbWriteNumeric(statement.getTransaction(), getTauxMoyenId(), "tauxMoyenId"));
        statement.writeField(AFTauxMoyen.FIELDNAME_PARAM_AFF_ID,
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "affiliationId"));
        statement.writeField(AFTauxMoyen.FIELDNAME_PARAM_TAUX_ID,
                this._dbWriteNumeric(statement.getTransaction(), getTauxId(), "tauxId"));
        statement.writeField(AFTauxMoyen.FIELDNAME_PARAM_MASSE,
                this._dbWriteNumeric(statement.getTransaction(), getMasseAnnuelle(), "masseAnnuelle"));
        statement.writeField(AFTauxMoyen.FIELDNAME_PARAM_MOIS,
                this._dbWriteNumeric(statement.getTransaction(), getNbrMois(), "nbrMois"));
        statement.writeField(AFTauxMoyen.FIELDNAME_PARAM_BLOCAGE, this._dbWriteBoolean(statement.getTransaction(),
                getBlocage(), BConstants.DB_TYPE_BOOLEAN_CHAR, "blocage"));
    }

    /**
     * Rechercher l'Affiliation en fonction de son ID.
     * 
     * @return l'Affiliation
     */
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }

        if (_affiliation == null) {

            _affiliation = new AFAffiliation();
            _affiliation.setSession(getSession());
            _affiliation.setAffiliationId(getAffiliationId());
            try {
                _affiliation.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
        }
        return _affiliation;
    }

    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    public java.lang.String getAnnee() {
        if ((taux != null) && !JadeStringUtil.isEmpty(taux.getDateDebut())) {
            return taux.getDateDebut().substring(6);
        } else {
            return "";
        }
    }

    public String getAssuranceId() {
        if (taux != null) {
            return taux.getAssuranceId();
        } else {
            return "";
        }
    }

    public java.lang.Boolean getBlocage() {
        return blocage;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFTauxMoyenManager();
    }

    public java.lang.String getMasseAnnuelle() {
        return masseAnnuelle;
    }

    public java.lang.String getNbrMois() {
        return nbrMois;
    }

    public AFTauxAssurance getTaux() {
        if (taux == null) {
            taux = new AFTauxAssurance();
            taux.setSession(getSession());
            taux.setFraction("100");
            taux.setGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX);
            taux.setTypeId(CodeSystem.TYPE_TAUX_MOYEN);
            taux.setValeurEmploye("0");
            taux.setValeurEmployeur("0");
        }
        return taux;
    }

    public double getTauxDouble() {
        if (taux != null) {
            return taux.getTauxDouble();
        } else {
            return 0;
        }
    }

    public java.lang.String getTauxId() {
        if (taux != null) {
            return taux.getTauxAssuranceId();
        } else {
            return "";
        }
    }

    public java.lang.String getTauxMoyenId() {
        return tauxMoyenId;
    }

    public String getTauxTotal() {
        if (taux != null) {
            return taux.getValeurTotal();
        } else {
            return "";
        }
    }

    /**
     * Calcul le taux en fonction de la masse salariale et le nombre de mois facturés
     */
    private void initValeurTaux() throws Exception {
        // annualisation de la masse salariale
        BigDecimal masse = new BigDecimal(getMasseAnnuelle());
        if (!JadeStringUtil.isIntegerEmpty(getMasseAnnuelle())) {
            masse = masse.divide(new BigDecimal(getNbrMois()), 2, BigDecimal.ROUND_HALF_DOWN);
            masse = masse.multiply(new BigDecimal("12"));
        }
        // calcul de la cotisation annuelle avec les taux variables
        AFTauxVariableUtil tauxVarUtil = AFTauxVariableUtil.getInstance(getAssuranceId());
        String tauxResult = tauxVarUtil.getMontantCotisation(getSession(), masse.toString(), "31.12." + getAnnee());
        if (!JadeStringUtil.isIntegerEmpty(tauxResult)) {
            BigDecimal montantCal = new BigDecimal(JANumberFormatter.deQuote(tauxResult));
            // calcul du taux moyen
            setTauxMoyen(montantCal.multiply(new BigDecimal("100")).divide(masse, 5, BigDecimal.ROUND_HALF_EVEN)
                    .toString());
        } else {
            setTauxMoyen(tauxVarUtil.getTaux(getSession(), getMasseAnnuelle(), "31.12." + getAnnee()).getValeurTotal());
        }
    }

    public void setAffiliationId(java.lang.String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setAnnee(String annee) {
        getTaux().setDateDebut("01.01." + annee);
    }

    public void setAssuranceId(String assuranceId) {
        getTaux().setAssuranceId(assuranceId);
    }

    public void setBlocage(java.lang.Boolean blocage) {
        this.blocage = blocage;
    }

    public void setMasseAnnuelle(java.lang.String masseAnnuelle) {
        this.masseAnnuelle = JANumberFormatter.deQuote(masseAnnuelle);
    }

    public void setNbrMois(java.lang.String nbrMois) {
        this.nbrMois = nbrMois;
    }

    public void setTaux(AFTauxAssurance taux) {
        this.taux = taux;
    }

    public void setTauxMoyen(String taux) {
        getTaux().setValeurEmployeur(taux);
    }

    public void setTauxMoyenId(java.lang.String tauxMoyenId) {
        this.tauxMoyenId = tauxMoyenId;
    }
}
