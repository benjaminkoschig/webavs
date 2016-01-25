package globaz.naos.db.masse;

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

/**
 * Insérez la description du type ici. Date de création : (28.05.2002 09:11:43)
 * 
 * @author: Administrator
 */
public class AFMasse extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAffiliation _affiliation = null;
    // private AFAssurance _assurance;
    private AFCotisation _cotisation = null;
    private TITiers _tiers = null;

    // foreign key
    private java.lang.String affiliationId = new String();
    private java.lang.String cotisationId = new String();
    private java.lang.String dateDebut = new String();
    private java.lang.String dateFin = new String();
    // private java.lang.String assuranceId = new String();
    private java.lang.String libelleAssurance = new String();
    // DB
    // Primary Key
    private java.lang.String masseId = new String();

    private java.lang.String nouvelleMasseAnnuelle = new String();
    private java.lang.String nouvelleMassePeriodicite = new String();
    private java.lang.Boolean traitement = new Boolean(false);

    /**
     * Constructeur d'AFMasse.
     */
    public AFMasse() {
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
        setMasseId(this._incCounter(transaction, "0"));
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFMASSP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        affiliationId = statement.dbReadNumeric("MAIAFF");
        cotisationId = statement.dbReadNumeric("MEICOT");
        masseId = statement.dbReadNumeric("MLIMAS");
        dateDebut = statement.dbReadDateAMJ("MLDDEB");
        dateFin = statement.dbReadDateAMJ("MLDFIN");
        nouvelleMassePeriodicite = statement.dbReadNumeric("MLMMAP", 2);
        nouvelleMasseAnnuelle = statement.dbReadNumeric("MLMMAA", 2);
        traitement = statement.dbReadBoolean("MLBTRA");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        // _propertyMandatory(statement.getTransaction(),
        // getCotisation().getAssuranceId(), getSession().getLabel("520"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getCotisationId(), getSession().getLabel("660"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getDateDebut(), getSession().getLabel("20"));

        validationOK &= _checkRealDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("160"));

        try {
            if (validationOK) {
                int anneeDebut = JACalendar.getYear(getDateDebut());
                int anneeFin = JACalendar.getYear(getDateFin());

                if (!(anneeDebut == anneeFin) && !JadeStringUtil.isIntegerEmpty(getDateFin())) {
                    _addError(statement.getTransaction(), getSession().getLabel("670"));
                    validationOK = false;
                }

                if (!JadeStringUtil.isIntegerEmpty(getDateFin())) {
                    // Date de fin renseignée, test de la validité, test
                    // plus grand que de début ou égale à la date de début
                    validationOK &= _checkRealDate(statement.getTransaction(), getDateFin(),
                            getSession().getLabel("180"));

                    if (!BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), getDateFin())) {
                        _addError(statement.getTransaction(), getSession().getLabel("550"));
                        validationOK = false;
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            validationOK = false;
        }

        if (JadeStringUtil.isEmpty(getNouvelleMasseAnnuelle()) && JadeStringUtil.isEmpty(getNouvelleMassePeriodicite())) {
            _addError(statement.getTransaction(), getSession().getLabel("690"));
            validationOK = false;
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MLIMAS", this._dbWriteNumeric(statement.getTransaction(), getMasseId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MLIMAS", this._dbWriteNumeric(statement.getTransaction(), getMasseId(), "MasseId"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "AffiliationId"));
        statement.writeField("MEICOT",
                this._dbWriteNumeric(statement.getTransaction(), getCotisationId(), "CotisationId"));
        statement.writeField("MLDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "DateDebut"));
        statement.writeField("MLDFIN", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "DateFin"));
        statement.writeField("MLMMAA",
                this._dbWriteNumeric(statement.getTransaction(), getNouvelleMasseAnnuelle(), "NouvelleMasseAnnuelle"));
        statement.writeField("MLMMAP", this._dbWriteNumeric(statement.getTransaction(), getNouvelleMassePeriodicite(),
                "NouvelleMassePeriodicite"));
        statement.writeField("MLBTRA", this._dbWriteBoolean(statement.getTransaction(), isTraitement(), "Traitement"));
    }

    /*
     * Traitement qui permet de mettre à jour la masse de la périodicité.
     * 
     * @param transaction
     * 
     * @param dateFacturation
     * 
     * @param periodicite
     * 
     * @return
     * 
     * @throws Exception
     */
    /*
     * public String _idMasseMensuel(BTransaction transaction, String dateFacturation, String periodicite) throws
     * Exception { try { AFCotisationListViewBean vBean = new AFCotisationListViewBean();
     * vBean.setSession(getSession()); vBean.find(transaction); for (int i = 0; i < vBean.size(); i++) { }
     * 
     * if (periodicite.equalsIgnoreCase(CodeSystem.PERIODICITE_MENSUELLE)) { // String mois = new String(); // String
     * anneeMois = new String(); // mois=dateFacturation.substring(4,6); // anneeMois=dateFacturation.substring(0,4);
     * 
     * } if (periodicite.equalsIgnoreCase(CodeSystem.PERIODICITE_TRIMESTRIELLE)) { // String trim = new String(); //
     * String anneeTrim = new String(); // trim=dateFacturation.substring(4,6); //
     * anneeTrim=dateFacturation.substring(0,4);
     * 
     * } if (periodicite.equalsIgnoreCase(CodeSystem.PERIODICITE_ANNUELLE)) { // String annee = new String(); //
     * annee=dateFacturation.substring(0,4);
     * 
     * } if (periodicite.equalsIgnoreCase(CodeSystem.PERIODICITE_ANNUELLE_30_JUIN)) { // String annee2 = new String();
     * // annee2 = dateFacturation.substring(0,4);
     * 
     * }
     * 
     * } catch (Exception e) { JadeLogger.error(this, e); _addError(transaction, ""); } return ""; }
     */

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * Rechercher l'Affiliation de la Masse en fonction de son ID.
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
     * Rechercher l'Affiliation de la Masse en fonction de son ID.
     * 
     * @return l'Affiliation
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

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    // Other
    public java.lang.String getLibelleAssurance() {
        return libelleAssurance;
    }

    public java.lang.String getMasseId() {
        return masseId;
    }

    public java.lang.String getNouvelleMasseAnnuelle() {
        return JANumberFormatter.fmt(nouvelleMasseAnnuelle.toString(), true, false, true, 2);
    }

    public java.lang.String getNouvelleMassePeriodicite() {
        return JANumberFormatter.fmt(nouvelleMassePeriodicite.toString(), true, false, true, 2);
    }

    /**
     * Rechercher le tiers pour de la Masse en fonction de l'affiliation.
     * 
     * @return l'Affiliation
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

    public java.lang.Boolean isTraitement() {
        return traitement;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public void setAffiliationId(java.lang.String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    public void setCotisationId(java.lang.String newCotisationId) {
        cotisationId = newCotisationId;
    }

    public void setDateDebut(java.lang.String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateFin(java.lang.String newDateFin) {
        dateFin = newDateFin;
    }

    // Other
    public void setLibelleAssurance(java.lang.String newLibelleAssurance) {
        libelleAssurance = newLibelleAssurance;
    }

    public void setMasseId(java.lang.String newMasseId) {
        masseId = newMasseId;
    }

    public void setNouvelleMasseAnnuelle(java.lang.String newNouvelleMasseAnnuelle) {
        nouvelleMasseAnnuelle = JANumberFormatter.deQuote(newNouvelleMasseAnnuelle);
    }

    public void setNouvelleMassePeriodicite(java.lang.String newNouvelleMassePeriodicite) {
        nouvelleMassePeriodicite = JANumberFormatter.deQuote(newNouvelleMassePeriodicite);
    }

    public void setTraitement(java.lang.Boolean newTraitement) {
        traitement = newTraitement;
    }
}
