package globaz.osiris.db.historique;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CAHistoriqueBulletinSoldeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdSection;

    @Override
    protected String _getOrder(BStatement statement) {
        return CAHistoriqueBulletinSolde.FIELD_DATEHISTORIQUE + " desc";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdSection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAHistoriqueBulletinSolde.FIELD_IDSECTION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSection());
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAHistoriqueBulletinSolde();
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

}
