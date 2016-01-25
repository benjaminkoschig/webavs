package globaz.lynx.db.extourne;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.facture.LXFactureManager;
import globaz.lynx.db.operation.LXOperation;

public class LXExtourneManager extends LXFactureManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forMontant;

    /**
     * @see globaz.lynx.db.facture.LXFactureManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_CSTYPEOPERATION).append(" = ")
                .append(_dbWriteNumeric(statement.getTransaction(), LXOperation.CS_TYPE_EXTOURNE));

        if (!JadeStringUtil.isIntegerEmpty(getForMontant()) && JadeStringUtil.isDigit(getForMontant())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_MONTANT).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForMontant()));
        }

        sqlWhere.append(" AND ").append(_setWhereCommonPart(statement));

        return sqlWhere.toString();
    }

    /**
     * @see globaz.lynx.db.facture.LXFactureManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXExtourne();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForMontant() {
        return forMontant;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForMontant(String forMontant) {
        this.forMontant = forMontant;
    }
}
