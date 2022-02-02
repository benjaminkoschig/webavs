package ch.globaz.eform.business.models;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

public class GFAttachementModelManager extends PRAbstractManager {

    private String forFormulaireId;

    @Override
    public String getOrderByDefaut() {
        return GFAttachementModel.FIELDNAME_ATTACHEMENT_ID;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new GFAttachementModel();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forFormulaireId)) {
            sqlWhere += " AND ";

            sqlWhere += GFFormulaireModel.FIELDNAME_FORMULAIRE_ID + "="
                    + _dbWriteString(statement.getTransaction(), forFormulaireId);
        }

        return sqlWhere;
    }

    public void setForFormulaireId(String theId){
        this.forFormulaireId = theId;
    }
}
