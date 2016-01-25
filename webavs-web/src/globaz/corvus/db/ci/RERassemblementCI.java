/*
 * Créé le 15 fevr. 07
 */
package globaz.corvus.db.ci;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRDateFormater;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */

public class RERassemblementCI extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_CLOTURE = "YODCLO";
    public static final String FIELDNAME_DATE_RASSEMBLEMENT = "YODRAS";
    public static final String FIELDNAME_DATE_REVOCATION = "YODREV";
    public static final String FIELDNAME_DATE_TRAITEMENT = "YODTRA";
    public static final String FIELDNAME_ETAT = "YOTETA";
    public static final String FIELDNAME_ID_CI = "YOICI";
    public static final String FIELDNAME_ID_PARENT = "YOIPAR";
    public static final String FIELDNAME_ID_RCI = "YOIRCI";
    public static final String FIELDNAME_ID_TIERS_AYANT_DROIT = "YOITAD";
    public static final String FIELDNAME_IS_TIERS_AYANT_DROIT = "YOBTAD";
    public static final String FIELDNAME_MOTIF = "YOTMOT";
    public static final String FIELDNAME_REF_UNIQUE_ARC = "YOIREF";
    // public static final String FIELDNAME_STATUS = "YOTSTA";
    public static final String FIELDNAME_TRAITE = "YOBTRA";
    public static final String TABLE_NAME_RCI = "RERCI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtat = "";
    private String dateCloture = "";
    private String dateRassemblement = "";
    private String dateRevocation = "";
    private String dateTraitement = "";
    private String idCI = "";
    private String idParent = "";
    private String idRCI = "";
    private String idTiersAyantDroit = "";
    // private String csStatus = "";
    private Boolean isCiAdditionnelTraite = Boolean.FALSE;
    private Boolean isTiersAyantDroit = Boolean.TRUE;
    private String motif = "";
    private String referenceUniqueArc = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * initialise la valeur de Id période api
     * 
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdRCI(this._incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return RERassemblementCI.TABLE_NAME_RCI;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRCI = statement.dbReadNumeric(RERassemblementCI.FIELDNAME_ID_RCI);
        idCI = statement.dbReadNumeric(RERassemblementCI.FIELDNAME_ID_CI);
        idParent = statement.dbReadNumeric(RERassemblementCI.FIELDNAME_ID_PARENT);
        motif = statement.dbReadNumeric(RERassemblementCI.FIELDNAME_MOTIF);
        csEtat = statement.dbReadNumeric(RERassemblementCI.FIELDNAME_ETAT);
        dateRassemblement = statement.dbReadDateAMJ(RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT);
        dateRevocation = statement.dbReadDateAMJ(RERassemblementCI.FIELDNAME_DATE_REVOCATION);
        referenceUniqueArc = statement.dbReadNumeric(RERassemblementCI.FIELDNAME_REF_UNIQUE_ARC);
        // csStatus = statement.dbReadNumeric(FIELDNAME_STATUS);
        isCiAdditionnelTraite = statement.dbReadBoolean(RERassemblementCI.FIELDNAME_TRAITE);
        dateTraitement = statement.dbReadDateAMJ(RERassemblementCI.FIELDNAME_DATE_TRAITEMENT);
        dateCloture = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(RERassemblementCI.FIELDNAME_DATE_CLOTURE));
        isTiersAyantDroit = statement.dbReadBoolean(RERassemblementCI.FIELDNAME_IS_TIERS_AYANT_DROIT);
        idTiersAyantDroit = statement.dbReadNumeric(RERassemblementCI.FIELDNAME_ID_TIERS_AYANT_DROIT);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(RERassemblementCI.FIELDNAME_ID_RCI,
                this._dbWriteNumeric(statement.getTransaction(), idRCI, "idRCI"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RERassemblementCI.FIELDNAME_ID_RCI,
                this._dbWriteNumeric(statement.getTransaction(), idRCI, "idRCI"));
        statement.writeField(RERassemblementCI.FIELDNAME_ID_CI,
                this._dbWriteNumeric(statement.getTransaction(), idCI, "idCI"));
        statement.writeField(RERassemblementCI.FIELDNAME_ID_PARENT,
                this._dbWriteNumeric(statement.getTransaction(), idParent, "idParent"));
        statement.writeField(RERassemblementCI.FIELDNAME_MOTIF,
                this._dbWriteNumeric(statement.getTransaction(), motif, "motif"));
        statement.writeField(RERassemblementCI.FIELDNAME_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateRassemblement, "dateRassemblement"));
        statement.writeField(RERassemblementCI.FIELDNAME_DATE_REVOCATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateRevocation, "dateRevocation"));
        statement.writeField(RERassemblementCI.FIELDNAME_REF_UNIQUE_ARC,
                this._dbWriteNumeric(statement.getTransaction(), referenceUniqueArc, "referenceUniqueArc"));
        // statement.writeField(FIELDNAME_STATUS,
        // _dbWriteNumeric(statement.getTransaction(), csStatus, "csStatus"));
        statement.writeField(RERassemblementCI.FIELDNAME_TRAITE, this._dbWriteBoolean(statement.getTransaction(),
                isCiAdditionnelTraite, BConstants.DB_TYPE_BOOLEAN_CHAR, "isCiAdditionnelTraite"));
        statement.writeField(RERassemblementCI.FIELDNAME_DATE_TRAITEMENT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateTraitement, "dateTraitement"));
        statement.writeField(
                RERassemblementCI.FIELDNAME_DATE_CLOTURE,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateCloture), "dateCloture"));
        statement.writeField(RERassemblementCI.FIELDNAME_IS_TIERS_AYANT_DROIT, this._dbWriteBoolean(
                statement.getTransaction(), isTiersAyantDroit, BConstants.DB_TYPE_BOOLEAN_CHAR, "isTiersAyantDroit"));
        statement.writeField(RERassemblementCI.FIELDNAME_ID_TIERS_AYANT_DROIT,
                this._dbWriteNumeric(statement.getTransaction(), idTiersAyantDroit, "idTiersAyantDroit"));
    }

    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return
     */
    public String getDateCloture() {
        return dateCloture;
    }

    public String getDateRassemblement() {
        return dateRassemblement;
    }

    public String getDateRevocation() {
        return dateRevocation;
    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    public String getIdCI() {
        return idCI;
    }

    public String getIdParent() {
        return idParent;
    }

    public String getIdRCI() {
        return idRCI;
    }

    /**
     * @return
     */
    public String getIdTiersAyantDroit() {
        return idTiersAyantDroit;
    }

    public Boolean getIsCiAdditionnelTraite() {
        return isCiAdditionnelTraite;
    }

    /**
     * @return
     */
    public Boolean getIsTiersAyantDroit() {
        return isTiersAyantDroit;
    }

    public String getMotif() {
        return motif;
    }

    public String getReferenceUniqueArc() {
        return referenceUniqueArc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setCsEtat(String etat) {
        csEtat = etat;
    }

    /**
     * @param string
     */
    public void setDateCloture(String string) {
        dateCloture = string;
    }

    // public String getCsStatus() {
    // return csStatus;
    // }
    //
    //
    // public void setCsStatus(String status) {
    // this.csStatus = status;
    // }

    public void setDateRassemblement(String dateRassemblement) {
        this.dateRassemblement = dateRassemblement;
    }

    public void setDateRevocation(String dateRevocation) {
        this.dateRevocation = dateRevocation;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public void setIdCI(String idCI) {
        this.idCI = idCI;
    }

    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }

    public void setIdRCI(String idRCI) {
        this.idRCI = idRCI;
    }

    /**
     * @param string
     */
    public void setIdTiersAyantDroit(String string) {
        idTiersAyantDroit = string;
    }

    public void setIsCiAdditionnelTraite(Boolean isCiAdditionnelTraite) {
        this.isCiAdditionnelTraite = isCiAdditionnelTraite;
    }

    /**
     * @param boolean1
     */
    public void setIsTiersAyantDroit(Boolean boolean1) {
        isTiersAyantDroit = boolean1;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setReferenceUniqueArc(String referenceUniqueArc) {
        this.referenceUniqueArc = referenceUniqueArc;
    }

}
