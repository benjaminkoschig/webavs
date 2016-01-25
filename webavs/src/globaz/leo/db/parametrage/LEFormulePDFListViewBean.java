package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.ENFormuleManager;
import globaz.envoi.db.parametreEnvoi.access.IENDefinitionFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENFormuleDefTable;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.leo.constantes.ILEConstantes;

/**
 * @author SCO
 * @since 13 juil. 2010
 */
public class LEFormulePDFListViewBean extends ENFormuleManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsDocument = new String();

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = super._getFrom(statement);
        from += (" INNER JOIN " + _getCollection() + IENDefinitionFormuleDefTable.TABLE_NAME);
        from += (" ON " + _getCollection() + IENFormuleDefTable.TABLE_NAME + "." + IENFormuleDefTable.ID_DEFINITION_FORMULE);
        from += (" = " + _getCollection() + IENDefinitionFormuleDefTable.TABLE_NAME + "." + IENDefinitionFormuleDefTable.ID_DEFINITION_FORMULE);
        from += (" INNER JOIN " + _getCollection() + ILEConstantes.CODES_SYSTEMES_TABLE);
        from += (" ON " + _getCollection() + IENDefinitionFormuleDefTable.TABLE_NAME + "." + IENDefinitionFormuleDefTable.CS_DOCUMENT);
        from += (" = " + _getCollection() + ILEConstantes.CODES_SYSTEMES_TABLE + "." + ILEConstantes.CODES_SYSTEMES_ID);
        from += (" LEFT OUTER JOIN " + _getCollection() + ILEFormuleDefTable.TABLE_NAME);
        from += (" ON " + _getCollection() + IENFormuleDefTable.TABLE_NAME + "." + IENFormuleDefTable.ID_FORMULE);
        from += (" = " + _getCollection() + ILEFormuleDefTable.TABLE_NAME + "." + ILEFormuleDefTable.ID_FORMULE);
        return from;
    }

    /**
     * @param statement
     * @return
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sql = new StringBuffer(super._getWhere(statement));
        if (getForCsDocument().trim().length() > 0) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append(IENDefinitionFormuleDefTable.CS_DOCUMENT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsDocument()));
        }
        return sql.toString();
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LEFormulePDFViewBean();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCsDocument() {
        return forCsDocument;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCsDocument(String string) {
        forCsDocument = string;
    }
}
