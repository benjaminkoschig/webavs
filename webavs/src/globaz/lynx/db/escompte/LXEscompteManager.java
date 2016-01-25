package globaz.lynx.db.escompte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.paiement.LXPaiementManager;
import globaz.lynx.db.section.LXSection;

public class LXEscompteManager extends LXPaiementManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.lynx.db.facture.LXFactureManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(".")
                .append(LXSection.FIELD_CSTYPESECTION).append(" = ")
                .append(_dbWriteNumeric(statement.getTransaction(), LXSection.CS_TYPE_FACTURE)).append(" ");

        String sqlWhereCommonPart = _setWhereCommonPart(statement);
        if (sqlWhereCommonPart.length() != 0) {
            sqlWhere.append(" AND ");
            sqlWhere.append(sqlWhereCommonPart);
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOrdreGroupe()) && JadeStringUtil.isDigit(getForIdOrdreGroupe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDORDREGROUPE).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdOrdreGroupe()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMontant()) && JadeStringUtil.isDigit(getForMontant())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_MONTANT).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForMontant()));
        }

        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }
        sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_CSTYPEOPERATION).append(" = ")
                .append(_dbWriteNumeric(statement.getTransaction(), LXOperation.CS_TYPE_ESCOMPTE));

        return sqlWhere.toString();
    }

    /**
     * @see globaz.lynx.db.facture.LXFactureManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXEscompte();
    }
}
