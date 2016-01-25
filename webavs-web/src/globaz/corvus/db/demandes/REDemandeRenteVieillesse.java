/*
 * Créé le 3 jan. 07
 */
package globaz.corvus.db.demandes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * @author BSC
 */
public class REDemandeRenteVieillesse extends REDemandeRente implements IPRCloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ANNEE_ANTICIPATION = "YCTNAA";
    public static final String FIELDNAME_DATE_REVOCATION_REQUERANT = "YADDRR";
    public static final String FIELDNAME_ID_DEMANDE_RENTE_VIEILLESSE = "YCIDVI";
    public static final String FIELDNAME_IS_AJOURNEMENT_REQUERANT = "YCBAJR";
    public static final String TABLE_NAME_DEMANDE_RENTE_VIEILLESSE = "REDEVIE";

    private String csAnneeAnticipation = "";
    private String dateRevocationRequerant = "";
    private Boolean isAjournementRequerant = Boolean.FALSE;

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tableDemandeRenteVieillesse = _getCollection()
                + REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE;
        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;

        sql.append(tableDemandeRenteVieillesse);

        // jointure avec la table des demandes de rentes
        sql.append(" INNER JOIN ").append(tableDemandeRente);
        sql.append(" ON ").append(tableDemandeRenteVieillesse).append(".")
                .append(REDemandeRenteVieillesse.FIELDNAME_ID_DEMANDE_RENTE_VIEILLESSE).append("=")
                .append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        return sql.toString();

    }

    @Override
    protected String _getTableName() {
        return REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idDemandeRente = statement.dbReadNumeric(REDemandeRenteVieillesse.FIELDNAME_ID_DEMANDE_RENTE_VIEILLESSE);
        csAnneeAnticipation = statement.dbReadNumeric(REDemandeRenteVieillesse.FIELDNAME_ANNEE_ANTICIPATION);
        isAjournementRequerant = statement.dbReadBoolean(REDemandeRenteVieillesse.FIELDNAME_IS_AJOURNEMENT_REQUERANT);
        dateRevocationRequerant = statement.dbReadDateAMJ(REDemandeRenteVieillesse.FIELDNAME_DATE_REVOCATION_REQUERANT);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(REDemandeRenteVieillesse.FIELDNAME_ID_DEMANDE_RENTE_VIEILLESSE,
                this._dbWriteNumeric(statement.getTransaction(), getIdDemandeRente()));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        if (_getAction() == BEntity.ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(REDemandeRenteVieillesse.FIELDNAME_ID_DEMANDE_RENTE_VIEILLESSE,
                    this._dbWriteNumeric(statement.getTransaction(), getIdDemandeRente(), "idDemandeRente"));
        }

        statement.writeField(REDemandeRenteVieillesse.FIELDNAME_ANNEE_ANTICIPATION,
                this._dbWriteNumeric(statement.getTransaction(), csAnneeAnticipation, "csAnneeAnticipation"));
        statement.writeField(REDemandeRenteVieillesse.FIELDNAME_IS_AJOURNEMENT_REQUERANT, this._dbWriteBoolean(
                statement.getTransaction(), isAjournementRequerant, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isAjournementRequerant"));
        statement.writeField(REDemandeRenteVieillesse.FIELDNAME_DATE_REVOCATION_REQUERANT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateRevocationRequerant, "dateRevocationRequerant"));
    }

    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        REDemandeRenteVieillesse clone = new REDemandeRenteVieillesse();

        duplicateDemandeRente(clone, action);

        clone.setCsAnneeAnticipation(getCsAnneeAnticipation());
        clone.setIsAjournementRequerant(getIsAjournementRequerant());
        clone.setDateRevocationRequerant(getDateRevocationRequerant());

        return clone;
    }

    public String getCsAnneeAnticipation() {
        return csAnneeAnticipation;
    }

    public String getDateRevocationRequerant() {
        return dateRevocationRequerant;
    }

    public Boolean getIsAjournementRequerant() {
        return isAjournementRequerant;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCsAnneeAnticipation(String csAnneeAnticipation) {
        this.csAnneeAnticipation = csAnneeAnticipation;
    }

    public void setDateRevocationRequerant(String dateRevocationRequerant) {
        this.dateRevocationRequerant = dateRevocationRequerant;
    }

    public void setIsAjournementRequerant(Boolean isAjournementRequerant) {
        this.isAjournementRequerant = isAjournementRequerant;
    }
}
