package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.ENFormuleManager;
import globaz.envoi.db.parametreEnvoi.access.IENComplementFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENDefinitionFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENFormuleDefTable;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.leo.constantes.ILEConstantes;

/**
 * @author ald
 * @since le 21 mars 05
 * @revision SCO 14 juil. 2010
 */
public class LEFormuleListViewBean extends ENFormuleManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCategorie = new String();
    private String forCsDocument = new String();

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "*";
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormuleManager#_getFrom(globaz.globall.db.BStatement)
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

        from += (" LEFT OUTER JOIN " + _getCollection() + IENComplementFormuleDefTable.TABLE_NAME);
        from += (" ON ( " + _getCollection() + IENFormuleDefTable.TABLE_NAME + "." + IENFormuleDefTable.ID_FORMULE);
        from += (" = " + _getCollection() + IENComplementFormuleDefTable.TABLE_NAME + "."
                + IENComplementFormuleDefTable.ID_FORMULE + " AND " + _getCollection()
                + IENComplementFormuleDefTable.TABLE_NAME + "." + IENComplementFormuleDefTable.CS_TYPE_FORMULE + " = "
                + ILEConstantes.CS_CATEGORIE_GROUPE + " )");

        return from;
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormuleManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sql = new StringBuffer(super._getWhere(statement));
        if (getForCsDocument().trim().length() > 0) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append(IENDefinitionFormuleDefTable.CS_DOCUMENT).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsDocument()));
        }

        if (getForCategorie().trim().length() > 0) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append(IENComplementFormuleDefTable.CS_VALEUR + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForCategorie()));
        }
        return sql.toString();
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormuleManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LEFormuleViewBean();
    }

    public String getForCategorie() {
        return forCategorie;
    }

    /**
     * @return
     */
    public String getForCsDocument() {
        return forCsDocument;
    }

    public void setForCategorie(String forCategorie) {
        this.forCategorie = forCategorie;
    }

    /**
     * @param string
     */
    public void setForCsDocument(String string) {
        forCsDocument = string;
    }
}
