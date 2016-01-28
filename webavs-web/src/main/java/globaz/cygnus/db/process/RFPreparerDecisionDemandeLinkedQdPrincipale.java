/*
 * Créé le 1 février 2010
 */
package globaz.cygnus.db.process;

import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author jje
 */
public class RFPreparerDecisionDemandeLinkedQdPrincipale extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String aliasName = "b";

    /**
     * Génération de la clause from pour la requête > des Qds jusqu'au tiers
     * 
     * @param schema
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        // String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";
        String as = " AS ";

        /*
         * SELECT EDIDEM,b.EQIQDB FROM CCVDWEB.RFDEMAN INNER JOIN CCVDWEB.RFDOSSI ON
         * CCVDWEB.RFDEMAN.EDIDOS=CCVDWEB.RFDOSSI.EAIDOS INNER JOIN CCVDWEB.RFAQPDO as b ON
         * CCVDWEB.RFDOSSI.EAIDOS=b.EQIDOS
         */

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(as);
        fromClauseBuffer.append(RFPreparerDecisionDemandeLinkedQdPrincipale.aliasName);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(RFPreparerDecisionDemandeLinkedQdPrincipale.aliasName);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssQdDossier.FIELDNAME_ID_DOSSIER);

        return fromClauseBuffer.toString();
    }

    private String csTypeRelation = "";
    private String idDemande = "";
    private String idQd = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private Boolean isComprisDansCalcul = Boolean.FALSE;

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getFields(BStatement statement) {

        String virgule = ",";
        String point = ".";

        StringBuffer fieldsBuffer = new StringBuffer();

        fieldsBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);
        fieldsBuffer.append(virgule);
        fieldsBuffer.append(RFPreparerDecisionDemandeLinkedQdPrincipale.aliasName);
        fieldsBuffer.append(point);
        fieldsBuffer.append(RFAssQdDossier.FIELDNAME_ID_QD);
        fieldsBuffer.append(virgule);
        fieldsBuffer.append(RFAssQdDossier.FIELDNAME_IS_COMPRIS_DANS_CALCUL);
        fieldsBuffer.append(virgule);
        fieldsBuffer.append(RFAssQdDossier.FIELDNAME_TYPE_RELATION);

        return fieldsBuffer.toString();
    }

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDemande = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_DEMANDE);
        idQd = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_ID_QD);
        isComprisDansCalcul = statement.dbReadBoolean(RFAssQdDossier.FIELDNAME_IS_COMPRIS_DANS_CALCUL);
        csTypeRelation = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_TYPE_RELATION);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    public String getCsTypeRelation() {
        return csTypeRelation;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdQd() {
        return idQd;
    }

    public Boolean getIsComprisDansCalcul() {
        return isComprisDansCalcul;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCsTypeRelation(String csTypeRelation) {
        this.csTypeRelation = csTypeRelation;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdQd(String idQd) {
        this.idQd = idQd;
    }

    public void setIsComprisDansCalcul(Boolean isComprisDansCalcul) {
        this.isComprisDansCalcul = isComprisDansCalcul;
    }

}