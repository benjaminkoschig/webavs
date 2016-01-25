package globaz.draco.db.declaration;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class DSDecompteLtnBlobManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    private String forDateImpression = "";
    private String forIdBlob = "";

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        // recherche sur l'id blob
        if (getForIdBlob().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDBLOB=" + this._dbWriteString(statement.getTransaction(), getForIdBlob());
        }

        // recherche sur la date d'impression
        if (getForDateImpression().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DLTNDA=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateImpression());
        }

        // recherche sur l'année
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DLTNAN=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new DSDecompteLtnBlob();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForDateImpression() {
        return forDateImpression;
    }

    public String getForIdBlob() {
        return forIdBlob;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForDateImpression(String forDateImpression) {
        this.forDateImpression = forDateImpression;
    }

    public void setForIdBlob(String forIdBlob) {
        this.forIdBlob = forIdBlob;
    }

}
