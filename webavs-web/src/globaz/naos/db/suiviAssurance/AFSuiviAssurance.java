package globaz.naos.db.suiviAssurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.translation.CodeSystem;

/**
 * La classe définissant l'entité SuiviAssurance.
 * 
 * @author administrator
 */
public class AFSuiviAssurance extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAssurance _assurance = null;
    // Foreign Key
    private java.lang.String assuranceId = new String();
    private java.lang.String dateEffective = new String();
    private java.lang.String dateFin = new String();
    private java.lang.String datePrevue = new String();
    // Fields
    private java.lang.String statut = new String();

    // private FWParametersSystemCode csStatut = null;

    // DB
    // Primary Key
    private java.lang.String suiviAssuranceId = new String();

    /**
     * Constructeur d'AFSuiviAssurance.
     */
    public AFSuiviAssurance() {
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
        setSuiviAssuranceId(this._incCounter(transaction, "0"));
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFSUIVP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        suiviAssuranceId = statement.dbReadNumeric("MGISUI");
        assuranceId = statement.dbReadNumeric("MBIASS");
        statut = statement.dbReadNumeric("MGTSTA");
        datePrevue = statement.dbReadDateAMJ("MGDPRE");
        dateEffective = statement.dbReadDateAMJ("MGDEFF");
        dateFin = statement.dbReadDateAMJ("MGDFIN");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceId(), getSession().getLabel("710"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getStatut(), getSession().getLabel("720"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getDatePrevue(), getSession().getLabel("450"));

        validationOK &= _checkDate(statement.getTransaction(), getDatePrevue(), getSession().getLabel("1460"));

        try {

            String dateLimiteInf = "01.01.1900";
            String dateInitiale = JACalendar.todayJJsMMsAAAA();
            String dateLimiteSup = getSession().getApplication().getCalendar().addYears(dateInitiale, 1);

            if (validationOK) {
                // Regarde que la date prevue soit comprise entre le 01.01.1948
                // et la date du jour + 1 année

                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDatePrevue(), dateLimiteInf)) {
                    if (BSessionUtil.compareDateFirstGreater(getSession(), getDatePrevue(), dateLimiteSup)) {
                        _addError(statement.getTransaction(), getSession().getLabel("730"));
                        validationOK = false;
                    }
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("480"));
                    validationOK = false;
                }
            }

            if (!JadeStringUtil.isIntegerEmpty(getDateEffective())) {
                // Regarde que si la date effective soit comprise entre le
                // 01.01.1948 et la date du jour +1 année

                validationOK &= _checkDate(statement.getTransaction(), getDateEffective(), getSession()
                        .getLabel("1490"));

                if (validationOK) {
                    // String dateFinale =
                    // getSession().getApplication().getCalendar().addYears(dateInitiale,
                    // 1);
                    if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateEffective(), dateLimiteInf)) {
                        if (BSessionUtil.compareDateFirstGreater(getSession(), getDateEffective(), dateLimiteSup)) {
                            _addError(statement.getTransaction(), getSession().getLabel("740"));
                            validationOK = false;
                        }
                    } else {
                        _addError(statement.getTransaction(), getSession().getLabel("750"));
                        validationOK = false;
                    }
                }
            }

            if (!JadeStringUtil.isIntegerEmpty(getDateFin())) {

                validationOK &= _checkDate(statement.getTransaction(), getDateFin(), getSession().getLabel("180"));

                if (validationOK) {
                    // Regarde que si la date de fin soit comprise entre le
                    // 01.01.1948 et la date du jour +1 année
                    // elle est rempli que si le statut à la valeur
                    // "Affilié assuré"

                    if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateFin(), dateLimiteInf)) {
                        if (BSessionUtil.compareDateFirstGreater(getSession(), getDateFin(), dateLimiteSup)) {
                            _addError(statement.getTransaction(), getSession().getLabel("70"));
                            validationOK = false;
                        }
                    } else {
                        _addError(statement.getTransaction(), getSession().getLabel("80"));
                        validationOK = false;
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            validationOK = false;
        }

        // Regarde que si statut est différent de "affiliation" la date de fin
        // ne doit pas être renseigné
        if (!CodeSystem.STATUS_SUIVI_AFFILIATION.equalsIgnoreCase(getStatut())
                && !JadeStringUtil.isIntegerEmpty(getDateFin())) {
            _addError(statement.getTransaction(), getSession().getLabel("760"));
        }

    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MGISUI", this._dbWriteNumeric(statement.getTransaction(), getSuiviAssuranceId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MGISUI",
                this._dbWriteNumeric(statement.getTransaction(), getSuiviAssuranceId(), "SuiviAssuranceId"));
        statement.writeField("MBIASS",
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), "AssuranceId"));
        statement.writeField("MGTSTA", this._dbWriteNumeric(statement.getTransaction(), getStatut(), "Statut"));
        statement.writeField("MGDPRE", this._dbWriteDateAMJ(statement.getTransaction(), getDatePrevue(), "DatePrevue"));
        statement.writeField("MGDEFF",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEffective(), "DateEffective"));
        statement.writeField("MGDFIN", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "DateFin"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * Rechercher l'assurance du Suivie d'assurance en fonction de son ID.
     * 
     * @return l'assurance
     */
    public AFAssurance getAssurance() {

        // Si pas d'identifiant, pas d'objet
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

    public java.lang.String getAssuranceId() {
        return assuranceId;
    }

    public java.lang.String getDateEffective() {
        return dateEffective;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    public java.lang.String getDatePrevue() {
        return datePrevue;
    }

    public java.lang.String getStatut() {
        return statut;
    }

    public java.lang.String getSuiviAssuranceId() {
        return suiviAssuranceId;
    }

    /*
     * Le Code System de Statut
     * 
     * @return le Code System
     */
    /*
     * public FWParametersSystemCode getCsStatut() { // enregistrement déjà chargé? if (csStatut == null) { // liste pas
     * encore chargée, on la charge csStatut = new FWParametersSystemCode(); csStatut.getCode(getStatut()); } return
     * csStatut; }
     */

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAssuranceId(java.lang.String newAssuranceId) {
        assuranceId = newAssuranceId;
    }

    public void setDateEffective(java.lang.String newDateEffective) {
        dateEffective = newDateEffective;
    }

    public void setDateFin(java.lang.String newDateFin) {
        dateFin = newDateFin;
    }

    public void setDatePrevue(java.lang.String newDatePrevue) {
        datePrevue = newDatePrevue;
    }

    public void setStatut(java.lang.String newStatut) {
        statut = newStatut;
    }

    public void setSuiviAssuranceId(java.lang.String newSuiviAssuranceId) {
        suiviAssuranceId = newSuiviAssuranceId;
    }
}
