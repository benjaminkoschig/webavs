package globaz.osiris.db.historique;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class CAHistoriqueBulletinSolde extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_DATEHISTORIQUE = "DATEHISTORIQUE";
    public static final String FIELD_IDHISTORIQUE = "IDHISTORIQUE";
    public static final String FIELD_IDSECTION = "IDSECTION";
    public static final String FIELD_SOLDE = "SOLDE";

    public static final String TABLE_CAHIBSP = "CAHIBSP";

    private String dateHistorique = "";
    private String idHistorique = "";
    private String idSection = "";
    private String solde = "0.00";

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdHistorique(this._incCounter(transaction, idHistorique));
    }

    @Override
    protected String _getTableName() {
        return CAHistoriqueBulletinSolde.TABLE_CAHIBSP;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdHistorique(statement.dbReadNumeric(CAHistoriqueBulletinSolde.FIELD_IDHISTORIQUE));
        setIdSection(statement.dbReadNumeric(CAHistoriqueBulletinSolde.FIELD_IDSECTION));
        setDateHistorique(statement.dbReadDateAMJ(CAHistoriqueBulletinSolde.FIELD_DATEHISTORIQUE));
        setSolde(statement.dbReadNumeric(CAHistoriqueBulletinSolde.FIELD_SOLDE, 2));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // do nothing yet
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAHistoriqueBulletinSolde.FIELD_IDHISTORIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdHistorique(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAHistoriqueBulletinSolde.FIELD_IDHISTORIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdHistorique(), "idHistorique"));
        statement.writeField(CAHistoriqueBulletinSolde.FIELD_IDSECTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdSection(), "idSection"));
        statement.writeField(CAHistoriqueBulletinSolde.FIELD_DATEHISTORIQUE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateHistorique(), "dateHistorique"));
        statement.writeField(CAHistoriqueBulletinSolde.FIELD_SOLDE,
                this._dbWriteNumeric(statement.getTransaction(), getSolde(), "solde"));

    }

    public String getDateHistorique() {
        return dateHistorique;
    }

    public String getIdHistorique() {
        return idHistorique;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getSolde() {
        return solde;
    }

    public void setDateHistorique(String dateHistorique) {
        this.dateHistorique = dateHistorique;
    }

    public void setIdHistorique(String idHistorique) {
        this.idHistorique = idHistorique;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setSolde(String solde) {
        this.solde = solde;
    }

}
