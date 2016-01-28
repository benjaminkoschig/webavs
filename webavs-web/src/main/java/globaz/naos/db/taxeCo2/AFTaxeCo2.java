package globaz.naos.db.taxeCo2;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.helper.IAFAffiliationHelper;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CARubriqueManager;
import globaz.pyxis.db.tiers.TITiers;
import java.io.Serializable;

public class AFTaxeCo2 extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_AFFILIATION_ID = "MAIAFF";
    public static final String FIELDNAME_ANNEE_MASSE = "MWDANN";
    public static final String FIELDNAME_ANNEE_REDISTRI = "MWDANR";
    public static final String FIELDNAME_ETAT = "MWTFAC";
    public static final String FIELDNAME_ID_RUBRIQUE = "MWIRUB";
    public static final String FIELDNAME_IDENTETE = "MWIENT";
    public static final String FIELDNAME_IDPASSAGE = "MWIPAS";
    public static final String FIELDNAME_MASSE = "MWMMAS";
    public static final String FIELDNAME_MOTIF_FIN = "MATMOT";
    public static final String FIELDNAME_NUM_AFFILIE = "MALNAF";
    public static final String FIELDNAME_PERIODICITE = "MATPER";
    public static final String FIELDNAME_TAUXFORCE = "MWMTAU";
    public static final String FIELDNAME_TAXE_CO2_ID = "MWIDTC";
    public final static String TABLE_FIELDS = AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_TAXE_CO2_ID + ", "
            + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_AFFILIATION_ID + ", " + AFTaxeCo2.TABLE_NAME + "."
            + AFTaxeCo2.FIELDNAME_ANNEE_MASSE + ", " + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_ANNEE_REDISTRI
            + ", " + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_MASSE + ", " + AFTaxeCo2.TABLE_NAME + "."
            + AFTaxeCo2.FIELDNAME_MOTIF_FIN + ", " + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_ID_RUBRIQUE
            + ", " + AFTaxeCo2.TABLE_NAME + ".PSPY" + ", " + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_ETAT
            + ", " + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_IDPASSAGE + ", " + AFTaxeCo2.TABLE_NAME + "."
            + AFTaxeCo2.FIELDNAME_TAUXFORCE + ", " + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_IDENTETE;
    // public final static String TABLE_FIELDS = AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_TAXE_CO2_ID + ", "
    // + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_AFFILIATION_ID + ", " + AFTaxeCo2.TABLE_NAME + "."
    // + AFTaxeCo2.FIELDNAME_ANNEE_MASSE + ", " + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_MASSE + ", "
    // + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_MOTIF_FIN + ", " + AFTaxeCo2.TABLE_NAME + "."
    // + AFTaxeCo2.FIELDNAME_ID_RUBRIQUE + ", " + AFTaxeCo2.TABLE_NAME + ".PSPY" + ", " + AFTaxeCo2.TABLE_NAME
    // + "." + AFTaxeCo2.FIELDNAME_ETAT + ", " + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_IDPASSAGE + ", "
    // + AFTaxeCo2.TABLE_NAME + "." + AFTaxeCo2.FIELDNAME_TAUXFORCE + ", " + AFTaxeCo2.TABLE_NAME + "."
    // + AFTaxeCo2.FIELDNAME_IDENTETE;
    public static final String TABLE_NAME = "AFTACOP";
    private AFAffiliation _affiliation = null;

    private AFAssurance _assuranceTaxeCo2 = null;
    private TITiers _tiers = null;
    // Fields
    private String affiliationId = new String();

    protected String anneeMasse = new String();
    protected String anneeRedistribution = new String();
    protected String etat = new String();
    protected String idEnteteFacture = new String();
    protected String idExterneRubrique = new String();
    protected String idPassage = new String();
    protected String idRubrique = new String();
    protected String idTiers = new String();
    protected String libelleRubrique = new String();
    protected String masse = new String();
    protected String montantCalculer = new String();
    protected String motifFin = new String();
    protected String numAffilie = new String();
    protected String periodicite = new String();
    protected String tauxForce = new String();
    // DB
    // Primary Key
    protected String taxeCo2Id = new String();

    /**
     * Constructeur de AFAffiliation
     */
    public AFTaxeCo2() {
        super();
        setMethodsToLoad(IAFAffiliationHelper.getterToLoad());
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setTaxeCo2Id(this._incCounter(transaction, "0"));
        AFAffiliationManager aff = new AFAffiliationManager();
        aff.setSession(getSession());
        aff.setForAffilieNumero(getNumAffilie());
        aff.setFromDateFin("0101" + getAnneeMasse());
        aff.setForDateDebutAffLowerOrEqualTo("3112" + getAnneeMasse());
        aff.setForTypesAffParitaires();
        aff.find();
        if (aff.size() > 0) {
            setAffiliationId(((AFAffiliation) aff.getFirstEntity()).getAffiliationId());
            setIdTiers(((AFAffiliation) aff.getFirstEntity()).getIdTiers());
        } else {
            AFAffiliationManager aff2 = new AFAffiliationManager();
            aff2.setSession(getSession());
            aff2.setForAffilieNumero(getNumAffilie());
            aff2.setFromDateFin("0101" + JACalendar.getYear(JACalendar.todayJJsMMsAAAA()));
            aff2.setForDateDebutAffLowerOrEqualTo("3112" + JACalendar.getYear(JACalendar.todayJJsMMsAAAA()));
            aff2.setForTypesAffParitaires();
            aff2.find();
            if (aff2.size() > 0) {
                setAffiliationId(((AFAffiliation) aff2.getFirstEntity()).getAffiliationId());
                setIdTiers(((AFAffiliation) aff2.getFirstEntity()).getIdTiers());
            } else {
                _addError(transaction, getSession().getLabel("ERREUR_NUM_AFFILIE") + getNumAffilie());
            }
        }
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return AFTaxeCo2.TABLE_FIELDS + ", AFAFFIP.MALNAF, AFAFFIP.MATPER ";
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFTACOP AS AFTACOP INNER JOIN " + _getCollection()
                + "AFAFFIP AS AFAFFIP ON (AFTACOP.MAIAFF=AFAFFIP.MAIAFF) ";

    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return AFTaxeCo2.TABLE_NAME; // "AFTACOP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        taxeCo2Id = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_TAXE_CO2_ID);
        affiliationId = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_AFFILIATION_ID);
        anneeMasse = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_ANNEE_MASSE);
        anneeRedistribution = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_ANNEE_REDISTRI);
        masse = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_MASSE);
        motifFin = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_MOTIF_FIN);
        idRubrique = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_ID_RUBRIQUE);
        numAffilie = statement.dbReadString(AFTaxeCo2.FIELDNAME_NUM_AFFILIE);
        periodicite = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_PERIODICITE);
        etat = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_ETAT);
        idPassage = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_IDPASSAGE);
        tauxForce = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_TAUXFORCE);
        idEnteteFacture = statement.dbReadNumeric(AFTaxeCo2.FIELDNAME_IDENTETE);
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        // Gestion de la mise en forme du taux forcé
        if (!JadeStringUtil.isBlankOrZero(getTauxForce()) && JadeStringUtil.endsWith(getTauxForce(), "-")) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_NEGATIF_DEVANT"));
        }
        if (!JadeStringUtil.isBlankOrZero(getTauxForce()) && !JadeStringUtil.startsWith(getTauxForce(), "-")) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_TAUX_NEGATIF"));
        }

        // On ne peut pas saisir un même affilié pour la même année
        AFTaxeCo2Manager taxeMana = new AFTaxeCo2Manager();
        taxeMana.setSession(getSession());
        taxeMana.setForAnneeMasse(getAnneeMasse());
        taxeMana.setForAffiliationId(getAffiliationId());
        taxeMana.setForNotTaxeCo2Id(getTaxeCo2Id());
        taxeMana.setForIdRubrique(getIdRubrique());
        taxeMana.find();
        if (taxeMana.size() > 0) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DOUBLE_SAISIE") + " : "
                    + getNumAffilie());
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MWIDTC", this._dbWriteNumeric(statement.getTransaction(), getTaxeCo2Id(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(AFTaxeCo2.FIELDNAME_TAXE_CO2_ID,
                this._dbWriteNumeric(statement.getTransaction(), getTaxeCo2Id(), "taxeCo2Id"));
        statement.writeField(AFTaxeCo2.FIELDNAME_AFFILIATION_ID,
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "affiliationId"));
        statement.writeField(AFTaxeCo2.FIELDNAME_ANNEE_MASSE,
                this._dbWriteNumeric(statement.getTransaction(), getAnneeMasse(), "anneeMasse"));
        statement.writeField(AFTaxeCo2.FIELDNAME_ANNEE_REDISTRI,
                this._dbWriteNumeric(statement.getTransaction(), getAnneeRedistribution(), "anneeRedistribution"));
        statement.writeField(AFTaxeCo2.FIELDNAME_MASSE,
                this._dbWriteNumeric(statement.getTransaction(), getMasse(), "masse"));
        statement.writeField(AFTaxeCo2.FIELDNAME_MOTIF_FIN,
                this._dbWriteNumeric(statement.getTransaction(), getMotifFin(), "motifFin"));
        statement.writeField(AFTaxeCo2.FIELDNAME_ID_RUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(AFTaxeCo2.FIELDNAME_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), getEtat(), "etat"));
        statement.writeField(AFTaxeCo2.FIELDNAME_IDPASSAGE,
                this._dbWriteNumeric(statement.getTransaction(), getIdPassage(), "idPassage"));
        statement.writeField(AFTaxeCo2.FIELDNAME_TAUXFORCE,
                this._dbWriteNumeric(statement.getTransaction(), getTauxForce(), "tauxForce"));
        statement.writeField(AFTaxeCo2.FIELDNAME_IDENTETE,
                this._dbWriteNumeric(statement.getTransaction(), getIdEnteteFacture(), "idEnteteFacture"));
    }

    /**
     * Rechercher l'affiliation en fonction de son ID.
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

    public String getAffiliationId() {
        return affiliationId;
    }

    public String getAnneeMasse() {
        return anneeMasse;
    }

    public String getAnneeRedistribution() {
        return anneeRedistribution;
    }

    public AFAssurance getAssuranceTaxeCo2() {
        if (_assuranceTaxeCo2 == null) {
            AFAssuranceManager assMana = new AFAssuranceManager();
            assMana.setSession(getSession());
            assMana.setForTypeAssurance(CodeSystem.TYPE_ASS_TAXE_CO2);
            try {
                assMana.find();
                if (assMana.size() > 0) {
                    _assuranceTaxeCo2 = new AFAssurance();
                    _assuranceTaxeCo2 = (AFAssurance) assMana.getFirstEntity();
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _assuranceTaxeCo2 = null;
            }
        }
        return _assuranceTaxeCo2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 11:24:17)
     * 
     * @return String
     */
    public String getDescriptionTiers() {
        if (!JadeStringUtil.isEmpty(getNumAffilie()) || !JadeStringUtil.isBlank(getNumAffilie())) {
            return getNumAffilie() + ", " + getNomTiers();
        } else {
            return "";
        }

    }

    public String getEtat() {
        return etat;
    }

    public String getIdEnteteFacture() {
        return idEnteteFacture;
    }

    public String getIdExterneRubrique() {
        if (JadeStringUtil.isEmpty(idExterneRubrique) && !JadeStringUtil.isEmpty(getIdRubrique())) {
            CARubriqueManager rubriqueMana = new CARubriqueManager();
            CARubrique rubrique = new CARubrique();
            rubriqueMana.setSession(getSession());
            rubriqueMana.setForIdRubrique(getIdRubrique());
            try {
                rubriqueMana.find();
                if (rubriqueMana.size() > 0) {
                    rubrique = (CARubrique) rubriqueMana.getFirstEntity();
                }
                setIdExterneRubrique(rubrique.getIdExterne());
                setLibelleRubrique(rubrique.getDescription(getSession().getIdLangueISO()));
            } catch (Exception e) {
                _addError(null, e.getMessage());
                idExterneRubrique = "";
            }
        }

        return idExterneRubrique;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 10:44:37)
     * 
     * @return String
     */
    public String getISOLangueTiers() {
        // Récupérer le tiers
        TITiers tiers = getTiers();
        if (tiers == null) {
            return "FR";
        } else if (tiers.getLangue().equalsIgnoreCase(TITiers.CS_FRANCAIS)) {
            return "FR";
        } else if (tiers.getLangue().equalsIgnoreCase(TITiers.CS_ALLEMAND)) {
            return "DE";
        } else if (tiers.getLangue().equalsIgnoreCase(TITiers.CS_ITALIEN)) {
            return "IT";
        } else {
            return "FR"; // default
        }
    }

    public String getLibelleRubrique() {
        return libelleRubrique;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFTaxeCo2Manager();
    }

    public String getMasse() {
        return JANumberFormatter.fmt(masse.toString(), true, true, false, 2);
    }

    public String getMontantCalculer() throws Exception {
        double masse = JadeStringUtil.toDouble(JANumberFormatter.deQuote(getMasse()));
        double taux = JadeStringUtil.toDouble(JANumberFormatter.deQuote(getAssuranceTaxeCo2().getTaux(
                "0101" + getAnneeRedistribution()).getTauxSansFraction()));
        double montant = JANumberFormatter.round((masse * taux) / 100, 0.05, 2, JANumberFormatter.NEAR);

        return "" + montant;
    }

    public String getMotifFin() {
        return motifFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 10:44:37)
     * 
     * @return String
     */
    public String getNomTiers() {
        // Récupérer le tiers
        TITiers tiers = getTiers();
        String designation1 = "";
        String designation2 = "";
        if (tiers != null) {
            designation1 = tiers.getDesignation1();
            designation2 = tiers.getDesignation2();
        }
        return designation1 + " " + designation2;
    }

    public java.lang.String getNumAffilie() {
        if (numAffilie.equals("") || numAffilie.equals(null)) {
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            aff.setAffiliationId(getAffiliationId());
            try {
                aff.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
            numAffilie = aff.getAffilieNumero();
        }
        return numAffilie;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public String getTauxForce() {
        return tauxForce;
    }

    public String getTaxeCo2Id() {
        return taxeCo2Id;
    }

    /**
     * Rechercher le tiers pour l'affiliation en fonction de son ID.
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

    public void setAffiliationId(java.lang.String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    public void setAnneeMasse(String anneeMasse) {
        this.anneeMasse = anneeMasse;
    }

    public void setAnneeRedistribution(String anneeRedistribution) {
        this.anneeRedistribution = anneeRedistribution;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setIdEnteteFacture(String idEnteteFacture) {
        this.idEnteteFacture = idEnteteFacture;
    }

    public void setIdExterneRubrique(String idExterneRubrique) {
        this.idExterneRubrique = idExterneRubrique;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLibelleRubrique(String libelleRubrique) {
        this.libelleRubrique = libelleRubrique;
    }

    public void setMasse(String masse) {
        this.masse = JANumberFormatter.deQuote(masse);
    }

    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public void setTauxForce(String tauxForce) {
        this.tauxForce = tauxForce;
    }

    public void setTaxeCo2Id(String taxeCo2Id) {
        this.taxeCo2Id = taxeCo2Id;
    }
}
