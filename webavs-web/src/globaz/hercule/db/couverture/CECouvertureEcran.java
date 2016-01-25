package globaz.hercule.db.couverture;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Classe représentant toutes les informations d'une couverture
 * 
 * @author SCO
 * @since 1 sept. 2010
 */
public class CECouvertureEcran extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ANNEE = "CENANE";

    public static final String FIELD_COUVERTUREACTIVE = "CEBCAV";
    public static final String FIELD_DATE_DEBUT_AFFILIATION = "MADDEB";
    public static final String FIELD_DATE_FIN_AFFILIATION = "MADFIN";
    public static final String FIELD_DATEMODIFICATION = "CENDAT";
    public static final String FIELD_IDAFFILIE = "MAIAFF";
    public static final String FIELD_IDCOUVERTURE = "CEICOU";
    public static final String FIELD_NOM = "HTLDE1";
    public static final String FIELD_NUMAFFILIE = "MALNAF";
    public static final String FIELD_PRENOM = "HTLDE2";
    public static final String TABLE_CECOUVP = "CECOUVP";

    private String annee;
    private Boolean couvertureActive;
    private String dateDebutAffiliation;
    private String dateFinAffiliation;
    private String dateModification;
    private String idAffilie;
    private String idCouverture;
    private String idTiers;
    private String nom;
    private String numAffilie;

    /**
     * Constructeur de CECouvertureEcran
     */
    public CECouvertureEcran() {
        super();
        couvertureActive = new Boolean(false);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCouverture(this._incCounter(transaction, idCouverture));
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String sqlFrom = super._getFrom(statement);
        sqlFrom += (" INNER JOIN " + _getCollection() + "AFAFFIP AFF ON (" + _getCollection()
                + CECouvertureEcran.TABLE_CECOUVP + "." + CECouvertureEcran.FIELD_IDAFFILIE + " = aff.maiaff)");
        sqlFrom += (" INNER JOIN " + _getCollection() + "TITIERP TIERS ON (tiers.htitie = aff.htitie)");

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CECouvertureEcran.TABLE_CECOUVP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCouverture = statement.dbReadNumeric(CECouvertureEcran.FIELD_IDCOUVERTURE);
        idAffilie = statement.dbReadNumeric(CECouvertureEcran.FIELD_IDAFFILIE);
        numAffilie = statement.dbReadString(CECouvertureEcran.FIELD_NUMAFFILIE);
        annee = statement.dbReadNumeric(CECouvertureEcran.FIELD_ANNEE);
        dateModification = statement.dbReadNumeric(CECouvertureEcran.FIELD_DATEMODIFICATION);
        dateDebutAffiliation = statement.dbReadDateAMJ(CECouvertureEcran.FIELD_DATE_DEBUT_AFFILIATION);
        dateFinAffiliation = statement.dbReadDateAMJ(CECouvertureEcran.FIELD_DATE_FIN_AFFILIATION);
        couvertureActive = statement.dbReadBoolean(CECouvertureEcran.FIELD_COUVERTUREACTIVE);
        nom = statement.dbReadString(CECouvertureEcran.FIELD_NOM) + " "
                + statement.dbReadString(CECouvertureEcran.FIELD_PRENOM);
        idTiers = statement.dbReadString("HTITIE");
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Controle de l'id externe
        if (JadeStringUtil.isBlank(getIdAffilie())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_AFFILIE"));
        }
        // Controle de l'id tiers
        if (JadeStringUtil.isIntegerEmpty(getAnnee())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_ANNEE_COUVERTURE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CECouvertureEcran.FIELD_IDCOUVERTURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCouverture(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAnnee() {
        return annee;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getDateModification() {
        return dateModification;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdCouverture() {
        return idCouverture;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNom() {
        return nom;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public Boolean isCouvertureActive() {
        return couvertureActive;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    /**
     * @return le numéro + nom + prénom
     */
    public String returnDescription() {

        if (!JadeStringUtil.isBlankOrZero(numAffilie) && !JadeStringUtil.isBlankOrZero(nom)) {
            return numAffilie + " " + nom;
        }

        return "";
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCouvertureActive(Boolean couvertureActive) {
        this.couvertureActive = couvertureActive;
    }

    public void setDateDebutAffiliation(String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateFinAffiliation(String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateModification(String dateModification) {
        this.dateModification = dateModification;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdCouverture(String idCouverture) {
        this.idCouverture = idCouverture;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

}
