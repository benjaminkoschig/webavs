package globaz.naos.db.fact;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.pyxis.db.tiers.TITiers;
import java.math.BigDecimal;

/**
 * @author sau
 */
/**
 * Insérez la description du type ici. Date de création : (28.05.2002 09:11:43)
 * 
 * @author: Administrator
 */
public class AFFact extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAffiliation _affiliation = null;
    private AFCotisation _cotisation = null;
    private TITiers _tiers = null;
    // Foreign Key
    private java.lang.String affiliationId = new String();
    private java.lang.String cotisationId = new String();
    private java.lang.String dateCreation = new String();
    // Fields
    private java.lang.String dateDebut = new String();
    private java.lang.String dateFin = new String();
    private java.lang.Boolean extourner = new Boolean(false);
    // DB
    // Primary key
    private java.lang.String facturationId = new String();
    private java.lang.Boolean facturer = new Boolean(false);
    private java.lang.Boolean facturerControle = new Boolean(false);
    private java.lang.String fractionAncien = new String();
    private java.lang.String fractionNouveau = new String();
    private java.lang.String heureCreation = new String();
    private java.lang.String massePeriodiciteNouveau = new String();
    private java.lang.String montant = new String();

    private java.lang.String passageId = new String();
    private java.lang.String tauxAncien = new String();
    private java.lang.String tauxNouveau = new String();

    private java.lang.String typeFacturation = new String();

    // private FWParametersSystemCode csTypeFacturation = null;

    /**
     * Constructeur d'AFFact.
     */
    public AFFact() {
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
        setFacturationId(this._incCounter(transaction, "0"));

    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFFACTP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        facturationId = statement.dbReadNumeric("MJIFAC");
        affiliationId = statement.dbReadNumeric("MAIAFF");
        cotisationId = statement.dbReadNumeric("MEICOT");
        dateDebut = statement.dbReadDateAMJ("MJDDEB");
        dateFin = statement.dbReadDateAMJ("MJDFIN");
        tauxAncien = statement.dbReadNumeric("MJMTAA", 5);
        tauxNouveau = statement.dbReadNumeric("MJMTAN", 5);
        fractionAncien = statement.dbReadNumeric("MJMFRA", 5);
        fractionNouveau = statement.dbReadNumeric("MJMFRN", 5);
        massePeriodiciteNouveau = statement.dbReadNumeric("MJMMPN", 2);
        montant = statement.dbReadNumeric("MJMMON", 2);
        typeFacturation = statement.dbReadNumeric("MJTLOT");
        facturer = statement.dbReadBoolean("MJBFAC");
        passageId = statement.dbReadNumeric("MJIPAS");
        extourner = statement.dbReadBoolean("MJBEXT");
        dateCreation = statement.dbReadDateAMJ("MJDCRE");
        heureCreation = statement.dbReadNumeric("MJNCRE");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        // Contrôle que les champ obligatoire soit renseignés
        // _propertyMandatory(statement.getTransaction(),
        // getCotisation().getAssurance().getAssuranceLibelle(),getSession().getLabel("10"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getDateDebut(), getSession().getLabel("20"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getDateFin(), getSession().getLabel("30"));

        // Test validité des dates
        validationOK &= _checkDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("160"));
        validationOK &= _checkDate(statement.getTransaction(), getDateFin(), getSession().getLabel("180"));

        if (validationOK) {
            // regarde que la date de début ne soit pas plus petite que
            // 01.01.1948 et qu'elle ne soit pas plus grande
            // que la date du jour + 1 année
            String dateLimiteInf = "01.01.1900";
            String dateInitiale = JACalendar.todayJJsMMsAAAA();
            String dateLimitSup = getSession().getApplication().getCalendar().addYears(dateInitiale, 10);

            try {
                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateDebut, dateLimiteInf)) {
                    if (BSessionUtil.compareDateFirstGreater(getSession(), dateDebut, dateLimitSup)) {
                        _addError(statement.getTransaction(), getSession().getLabel("50"));
                    }
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("60"));
                }

                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateFin, dateLimiteInf)) {
                    if (BSessionUtil.compareDateFirstGreater(getSession(), dateFin, dateLimitSup)) {
                        _addError(statement.getTransaction(), getSession().getLabel("70"));
                    }
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("80"));
                }

                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateDebut, dateFin)) {
                    _addError(statement.getTransaction(), getSession().getLabel("90"));
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                validationOK = false;
            }
        }

        // Contrôle que si facturer égal true on ne peut plus rien modifier
        if (isFacturer().booleanValue()) {
            _addError(statement.getTransaction(), getSession().getLabel("40"));
        }

        if (isExtourner().booleanValue()) {

            if (!JadeStringUtil.isEmpty(getMassePeriodiciteNouveau())) {

                _addError(statement.getTransaction(), getSession().getLabel("100"));
            }
            if (!JadeStringUtil.isEmpty(getMontant())) {

                _addError(statement.getTransaction(), getSession().getLabel("110"));
            }
        }

        if (JadeStringUtil.isEmpty(getTauxAncien())) {

            if (!JadeStringUtil.isEmpty(getTauxNouveau())) {
                _addError(statement.getTransaction(), getSession().getLabel("120"));
            }
        } else {
            if (JadeStringUtil.isEmpty(getTauxNouveau())) {
                _addError(statement.getTransaction(), getSession().getLabel("120"));
            } else {
                // Taux renseigné, la fraction doit être plus grande ou égale au
                // taux
                BigDecimal bFraction = new BigDecimal(getFractionAncien());
                BigDecimal bTaux = new BigDecimal(getTauxAncien());

                if (bFraction.compareTo(bTaux) == -1) {
                    _addError(statement.getTransaction(), getSession().getLabel("130"));
                }
                // Taux renseigné, la fraction nouvelle doit être plus grande ou
                // égale au taux nouveau
                BigDecimal bFraction2 = new BigDecimal(getFractionNouveau());
                BigDecimal bTaux2 = new BigDecimal(getTauxNouveau());

                if (bFraction2.compareTo(bTaux2) == -1) {
                    _addError(statement.getTransaction(), getSession().getLabel("140"));
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
        statement.writeKey("MJIFAC", this._dbWriteNumeric(statement.getTransaction(), getFacturationId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MJIFAC",
                this._dbWriteNumeric(statement.getTransaction(), getFacturationId(), "FacturationId"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "AffiliationId"));
        statement.writeField("MEICOT",
                this._dbWriteNumeric(statement.getTransaction(), getCotisationId(), "CotisationId"));
        statement.writeField("MJDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "DateDebut"));
        statement.writeField("MJDFIN", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "DateFin"));
        statement.writeField("MJMTAA", this._dbWriteNumeric(statement.getTransaction(), getTauxAncien(), "TauxAncien"));
        statement.writeField("MJMTAN",
                this._dbWriteNumeric(statement.getTransaction(), getTauxNouveau(), "TauxNouveau"));
        statement.writeField("MJMFRA",
                this._dbWriteNumeric(statement.getTransaction(), getFractionAncien(), "FractionAncien"));
        statement.writeField("MJMFRN",
                this._dbWriteNumeric(statement.getTransaction(), getFractionNouveau(), "FractionNouveau"));
        statement.writeField("MJMMPN", this._dbWriteNumeric(statement.getTransaction(), getMassePeriodiciteNouveau(),
                "MassePeriodiciteNouveau"));
        statement.writeField("MJMMON", this._dbWriteNumeric(statement.getTransaction(), getMontant(), "Montant"));
        statement.writeField("MJTLOT",
                this._dbWriteNumeric(statement.getTransaction(), getTypeFacturation(), "TypeFacturation"));
        statement.writeField("MJBFAC", this._dbWriteBoolean(statement.getTransaction(), isFacturer(), "Facturer"));
        statement.writeField("MJIPAS", this._dbWriteNumeric(statement.getTransaction(), getPassageId(), "PassageId"));
        statement.writeField("MJBEXT", this._dbWriteBoolean(statement.getTransaction(), isExtourner(), "Extourner"));
        statement.writeField("MJDCRE",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCreation(), "DateCreation"));
        statement.writeField("MJNCRE",
                this._dbWriteNumeric(statement.getTransaction(), getHeureCreation(), "HeureCreation"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * Rechercher l'affiliation pour la facturation en fonction de son ID.
     * 
     * @return l'affiliation
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
                /*
                 * if (_affiliation.hasErrors()) _affiliation = null;
                 */
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

    /**
     * Rechercher la cotisation de la facturation en fonction de son ID.
     * 
     * @return la cotisation
     */
    public AFCotisation getCotisation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getCotisationId())) {
            return null;
        }

        if (_cotisation == null) {

            _cotisation = new AFCotisation();
            _cotisation.setSession(getSession());
            _cotisation.setCotisationId(getCotisationId());
            try {
                _cotisation.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _cotisation = null;
            }
        }
        return _cotisation;
    }

    public java.lang.String getCotisationId() {
        return cotisationId;
    }

    public java.lang.String getDateCreation() {
        return dateCreation;
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    public java.lang.String getFacturationId() {
        return facturationId;
    }

    public java.lang.String getFractionAncien() {
        return JANumberFormatter.fmt(fractionAncien.toString(), true, false, true, 5);
    }

    public java.lang.String getFractionNouveau() {
        return JANumberFormatter.fmt(fractionNouveau.toString(), true, false, true, 5);
    }

    public java.lang.String getHeureCreation() {
        return heureCreation;
    }

    public java.lang.String getMassePeriodiciteNouveau() {
        return JANumberFormatter.fmt(massePeriodiciteNouveau.toString(), true, false, true, 2);
    }

    public java.lang.String getMontant() {
        return JANumberFormatter.fmt(montant.toString(), true, false, true, 2);
    }

    public java.lang.String getPassageId() {
        return passageId;
    }

    public java.lang.String getTauxAncien() {
        return JANumberFormatter.fmt(tauxAncien.toString(), true, false, true, 5);
    }

    public java.lang.String getTauxNouveau() {
        return JANumberFormatter.fmt(tauxNouveau.toString(), true, false, true, 5);
    }

    /**
     * Rechercher le tiers pour l'affilation de la facturation.
     * 
     * @return le tiers
     */
    public TITiers getTiers() {

        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_affiliation == null) {
                getAffiliation();
                if (_affiliation == null) {
                    return null;
                }
            }
            _tiers = new TITiers();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(_affiliation.getIdTiers());
            try {
                _tiers.retrieve();
                /*
                 * if (_tiers.getSession().hasErrors()) _tiers = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }
        return _tiers;
    }

    public java.lang.String getTypeFacturation() {
        return typeFacturation;
    }

    public java.lang.Boolean isExtourner() {
        return extourner;
    }

    public java.lang.Boolean isFacturer() {
        return facturer;
    }

    public java.lang.Boolean isFacturerControle() {
        return facturerControle;
    }

    /*
     * Le Code System de TypeFacturation
     * 
     * @return le Code System
     */
    /*
     * public FWParametersSystemCode getCsTypeFacturation() { if (csTypeFacturation == null) { // liste pas encore
     * chargée, on la charge csTypeFacturation = new FWParametersSystemCode();
     * csTypeFacturation.getCode(getTypeFacturation()); } return csTypeFacturation; }
     */

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAffiliationId(java.lang.String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    public void setCotisationId(java.lang.String newCotisationId) {
        cotisationId = newCotisationId;
    }

    public void setDateCreation(java.lang.String newDateCreation) {
        dateCreation = newDateCreation;
    }

    public void setDateDebut(java.lang.String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateFin(java.lang.String newDateFin) {
        dateFin = newDateFin;
    }

    public void setExtourner(java.lang.Boolean newExtourner) {
        extourner = newExtourner;
    }

    public void setFacturationId(java.lang.String newFacturationId) {
        facturationId = newFacturationId;
    }

    public void setFacturer(java.lang.Boolean newFacturer) {
        facturer = newFacturer;
    }

    public void setFacturerControle(java.lang.Boolean newFacturerControle) {
        facturerControle = newFacturerControle;
    }

    public void setFractionAncien(java.lang.String newFractionAncien) {
        fractionAncien = JANumberFormatter.deQuote(newFractionAncien);
    }

    public void setFractionNouveau(java.lang.String newFractionNouveau) {
        fractionNouveau = JANumberFormatter.deQuote(newFractionNouveau);
    }

    public void setHeureCreation(java.lang.String newHeureCreation) {
        heureCreation = newHeureCreation;
    }

    public void setMassePeriodiciteNouveau(java.lang.String newMassePeriodiciteNouveau) {
        massePeriodiciteNouveau = JANumberFormatter.deQuote(newMassePeriodiciteNouveau);
    }

    public void setMontant(java.lang.String newMontant) {
        montant = JANumberFormatter.deQuote(newMontant);
    }

    public void setPassageId(java.lang.String newPassageId) {
        passageId = newPassageId;
    }

    public void setTauxAncien(java.lang.String newTauxAncien) {
        tauxAncien = JANumberFormatter.deQuote(newTauxAncien);
    }

    public void setTauxNouveau(java.lang.String newTauxNouveau) {
        tauxNouveau = JANumberFormatter.deQuote(newTauxNouveau);
    }

    public void setTypeFacturation(java.lang.String newTypeFacturation) {
        typeFacturation = newTypeFacturation;
    }
}
