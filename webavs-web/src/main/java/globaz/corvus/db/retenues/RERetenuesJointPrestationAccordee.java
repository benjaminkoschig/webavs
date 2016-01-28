/*
 * Créé le 23 août 07
 */

package globaz.corvus.db.retenues;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiers;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BStatement;

/**
 * @author BSC
 * 
 */

public class RERetenuesJointPrestationAccordee extends RERetenuesPaiement {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";

    public static final String FIELDNAME_NATIONALITE = "HNIPAY";

    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";

    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        // String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERetenuesPaiement.TABLE_NAME_RETENUES);

        // jointure entre table des retenues et table des prestations accordees
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERetenuesPaiement.TABLE_NAME_RETENUES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERetenuesPaiement.FIELDNAME_ID_RENTE_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        // jointure entre table des prestations accordees et table des numeros
        // AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.FIELDNAME_ID_TIERS_TI);

        // jointure entre table table des numeros AVS et table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des personnes et table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERetenuesJointPrestationAccordee.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String codePrestation = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateNaissance = "";
    private String idTiersBeneficiaireRA = "";
    private String nom = "";
    private String nss = "";
    private String prenom = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return RERetenuesJointPrestationAccordee.createFromClause(_getCollection());
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        super._readProperties(statement);

        nss = NSUtil.formatAVSUnknown(statement.dbReadString(REPrestAccJointInfoComptaJointTiers.FIELDNAME_NUM_AVS));
        nom = statement.dbReadString(REPrestAccJointInfoComptaJointTiers.FIELDNAME_NOM);
        prenom = statement.dbReadString(REPrestAccJointInfoComptaJointTiers.FIELDNAME_PRENOM);
        idTiersBeneficiaireRA = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        dateNaissance = statement.dbReadDateAMJ(REPrestAccJointInfoComptaJointTiers.FIELDNAME_DATENAISSANCE);
        csSexe = statement.dbReadNumeric(REPrestAccJointInfoComptaJointTiers.FIELDNAME_SEXE);
        csNationalite = statement.dbReadNumeric(REPrestAccJointInfoComptaJointTiers.FIELDNAME_NATIONALITE);
        codePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return
     */
    public String getIdTiersBeneficiaireRA() {
        return idTiersBeneficiaireRA;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * @param string
     */
    public void setIdTiersBeneficiaireRA(String string) {
        idTiersBeneficiaireRA = string;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}
