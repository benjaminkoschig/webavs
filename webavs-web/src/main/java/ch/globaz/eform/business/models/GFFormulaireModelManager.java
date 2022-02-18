package ch.globaz.eform.business.models;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class GFFormulaireModelManager extends PRAbstractManager  {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String forId = "";

    @Override
    public String getOrderByDefaut() {
        return GFFormulaireModel.FIELDNAME_FORMULAIRE_ID;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new GFFormulaireModel(getSession());
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forId)) {
            sqlWhere += " AND ";

            sqlWhere += GFFormulaireModel.FIELDNAME_FORMULAIRE_ID + "="
                    + _dbWriteString(statement.getTransaction(), forId);
        }

        return sqlWhere;
    }
}
