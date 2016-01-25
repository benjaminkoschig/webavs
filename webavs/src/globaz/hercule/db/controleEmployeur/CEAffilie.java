package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;

/**
 * @author MMO
 * @since 12 août 2010
 */
public class CEAffilie extends BEntity {

    private static final long serialVersionUID = 4187347638002840395L;

    public static final String F_AFF_DDEB = AFAffiliation.FIELDNAME_AFF_DDEBUT;
    public static final String F_AFF_DFIN = AFAffiliation.FIELDNAME_AFF_DFIN;
    public static final String F_AFF_NUMAFFILIE = "MALNAF";
    public static final String F_AFF_TYPE = AFAffiliation.FIELDNAME_AFFILIATION_TYPE;
    public static final String F_IDAFFILIATION = AFAffiliation.FIELDNAME_AFFILIATION_ID;
    public static final String F_IDTIER = "HTITIE";
    public static final String F_TIER_COMPLEMENTNOM = "HTLDE2";
    public static final String F_TIER_NOM = "HTLDE1";
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String LIKE = " LIKE ";
    public static final String ON = " ON ";
    public static final String T_AFF = AFAffiliation.TABLE_NAME;
    public static final String T_TIER = "TITIERP";

    private String dateDebutAffiliation = "";
    private String dateFinAffiliation = "";
    private String idAffiliation = "";
    private String idTiers = "";
    private String nom = "";
    private String numAffilie = "";
    private String typeAffiliation = "";
    private String codeSUVA = "";

    @Override
    protected boolean _allowAdd() {
        return false;
    }

    @Override
    protected boolean _allowDelete() {
        return false;
    }

    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    public String _getDescription() {
        return numAffilie + " " + nom + "\n" + dateDebutAffiliation + " - " + dateFinAffiliation;
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + CEAffilie.T_AFF);
        sqlFrom.append(CEAffilie.INNER_JOIN);
        sqlFrom.append(_getCollection() + CEAffilie.T_TIER);
        sqlFrom.append(CEAffilie.ON);
        sqlFrom.append(_getCollection() + CEAffilie.T_AFF + "." + CEAffilie.F_IDTIER + "=" + _getCollection()
                + CEAffilie.T_TIER + "." + CEAffilie.F_IDTIER);

        return sqlFrom.toString();
    }

    @Override
    protected String _getTableName() {
        return "AFAFFIP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAffiliation = statement.dbReadNumeric(CEAffilieManager.F_IDAFFILIATION);
        numAffilie = statement.dbReadString(CEAffilieManager.F_AFF_NUMAFFILIE);
        idTiers = statement.dbReadNumeric(CEAffilieManager.F_IDTIER);
        nom = statement.dbReadString(CEAffilieManager.F_TIER_NOM) + " "
                + statement.dbReadString(CEAffilieManager.F_TIER_COMPLEMENTNOM);
        typeAffiliation = statement.dbReadNumeric(CEAffilieManager.F_AFF_TYPE);
        dateDebutAffiliation = statement.dbReadDateAMJ(CEAffilieManager.F_AFF_DDEB);
        dateFinAffiliation = statement.dbReadDateAMJ(CEAffilieManager.F_AFF_DFIN);
        codeSUVA = statement.dbReadDateAMJ(CEAffilieManager.F_CODE_SUVA);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MAIAFF", this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateFinAffiliation() {
        if (JadeStringUtil.isBlankOrZero(dateFinAffiliation)) {
            return "*";
        }
        return dateFinAffiliation;
    }

    public String getIdAffiliation() {
        return idAffiliation;
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

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public String getTypeAffiliationLabel() throws Exception {
        return CodeSystem.getLibelle(getSession(), getTypeAffiliation());
    }

    public void setDateDebutAffiliation(String newDateDebutAffiliation) {
        dateDebutAffiliation = newDateDebutAffiliation;
    }

    public void setDateFinAffiliation(String newDateFinAffiliation) {
        dateFinAffiliation = newDateFinAffiliation;
    }

    public void setIdAffiliation(String newIdAffiliation) {
        idAffiliation = newIdAffiliation;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String newNom) {
        nom = newNom;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setTypeAffiliation(String newTypeAffiliation) {
        typeAffiliation = newTypeAffiliation;
    }

    /**
     * Getter de codeSUVA
     * 
     * @return the codeSUVA
     */
    public String getCodeSUVA() {
        return codeSUVA;
    }

    /**
     * Setter de codeSUVA
     * 
     * @param codeSUVA the codeSUVA to set
     */
    public void setCodeSUVA(String codeSUVA) {
        this.codeSUVA = codeSUVA;
    }

}
