package globaz.naos.process.impressionlistdeces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class TIDeceaseInformationManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idTiers = new String();
    private String lastExecutionDate = new String();
    private String lastModificationDate = new String();
    private String order = "";

    @Override
    protected String _getFrom(BStatement statement) {
        String from;
        from = _getCollection() + "TIHISTD ";
        return from;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return getOrder();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (getIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTIERS= " + this._dbWriteString(statement.getTransaction(), getIdTiers());
        }
        if (getLastExecutionDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "EXECDATE= " + this._dbWriteString(statement.getTransaction(), getLastExecutionDate());
        }
        if (getLastModificationDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MODIFDATE= " + this._dbWriteString(statement.getTransaction(), getLastModificationDate());
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new TIDeceaseInformation();
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLastExecutionDate() {
        return lastExecutionDate;
    }

    public String getLastModificationDate() {
        return lastModificationDate;
    }

    public String getOrder() {
        return order;
    }

    public void setIdTiers(String newIdTiers) {
        idTiers = newIdTiers;
    }

    public void setLastExecutionDate(String newLastExecutionDate) {
        lastExecutionDate = newLastExecutionDate;
    }

    public void setLastModificationDate(String newLastModificationDate) {
        lastModificationDate = newLastModificationDate;
    }

    public void setOrder(String newOrder) {
        order = newOrder;
    }

}
