package globaz.lynx.db.notedecredit;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.lynx.db.facture.LXFactureManager;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;

public class LXNoteDeCreditManager extends LXFactureManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = _getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_CSTYPESECTION + " = "
                + _dbWriteNumeric(statement.getTransaction(), LXSection.CS_TYPE_NOTEDECREDIT);

        sqlWhere += " AND (";

        sqlWhere += _getCollection() + LXOperation.TABLE_LXOPERP + "." + LXOperation.FIELD_CSTYPEOPERATION + " = "
                + _dbWriteNumeric(statement.getTransaction(), LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE);
        sqlWhere += " OR " + _getCollection() + LXOperation.TABLE_LXOPERP + "." + LXOperation.FIELD_CSTYPEOPERATION
                + " = " + _dbWriteNumeric(statement.getTransaction(), LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE);

        sqlWhere += ") AND " + _setWhereCommonPart(statement);

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXNoteDeCredit();
    }

}
