package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CIAnnonceCentraleBlobManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAnnonce;

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isBlankOrZero(forIdAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CIAnnonceCentraleBlob.COLUMN_NAME_ID_ANNONCE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdAnnonce());
            ;
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIAnnonceCentraleBlob();
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

}
