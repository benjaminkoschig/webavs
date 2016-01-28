package globaz.prestation.db.infos;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAbstractManagerHierarchique;

/**
 * @author BSC
 */
public class PRInfoComplManager extends PRAbstractManagerHierarchique {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdInfoCompl = "";
    private String forTypeInfoCompl = "";

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableInformationComplementaire = _getCollection() + PRInfoCompl.TABLE_NAME;

        if (!JadeStringUtil.isEmpty(forIdInfoCompl)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(tableInformationComplementaire).append(".").append(PRInfoCompl.FIELDNAME_ID_INFO_COMPL)
                    .append("=").append(this._dbWriteNumeric(statement.getTransaction(), forIdInfoCompl));
        }

        if (!JadeStringUtil.isEmpty(forTypeInfoCompl)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(tableInformationComplementaire).append(".").append(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL)
                    .append("=").append(this._dbWriteNumeric(statement.getTransaction(), forTypeInfoCompl));
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new PRInfoCompl();
    }

    public String getForIdInfoCompl() {
        return forIdInfoCompl;
    }

    public String getForTypeInfoCompl() {
        return forTypeInfoCompl;
    }

    @Override
    public String getHierarchicalOrderBy() {
        return getOrderByDefaut();
    }

    @Override
    public String getOrderByDefaut() {
        return PRInfoCompl.FIELDNAME_ID_INFO_COMPL;
    }

    public void setForIdInfoCompl(String string) {
        forIdInfoCompl = string;
    }

    public void setForTypeInfoCompl(String string) {
        forTypeInfoCompl = string;
    }
}
