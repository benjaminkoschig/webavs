package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.IENDefinitionFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENFormuleDefTable;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.leo.constantes.ILEConstantes;

/**
 * @author SCO
 * @since 12 juil. 2010
 */
public class LESelectFormuleListViewBean extends BManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String FIELD_PCOLUT = "PCOLUT";
    private final static String FIELD_PCOSID = "PCOSID";
    private final static String FIELD_PLAIDE = "PLAIDE";
    private final static String TABLE_COU = "FWCOUP";

    private String fromCsDocumentValue = new String();

    /**
     * @see globaz.globall.db.BManager#_getFields(BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();

        sqlFields.append(_getCollection()).append(TABLE_COU).append(".").append(FIELD_PCOLUT).append(", ");
        sqlFields.append(_getCollection()).append(IENDefinitionFormuleDefTable.TABLE_NAME).append(".")
                .append(IENDefinitionFormuleDefTable.CS_DOCUMENT).append(", ");
        sqlFields.append(_getCollection()).append(IENDefinitionFormuleDefTable.TABLE_NAME).append(".")
                .append(IENDefinitionFormuleDefTable.ID_DEFINITION_FORMULE);

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection()).append(IENFormuleDefTable.TABLE_NAME);

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection()).append(IENDefinitionFormuleDefTable.TABLE_NAME).append(" ON ");
        sqlFrom.append(_getCollection()).append(IENFormuleDefTable.TABLE_NAME).append(".")
                .append(IENFormuleDefTable.ID_DEFINITION_FORMULE).append(" = ");
        sqlFrom.append(_getCollection()).append(IENDefinitionFormuleDefTable.TABLE_NAME).append(".")
                .append(IENDefinitionFormuleDefTable.ID_DEFINITION_FORMULE);

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection()).append(TABLE_COU).append(" ON ");
        sqlFrom.append(_getCollection()).append(TABLE_COU).append(".").append(FIELD_PCOSID).append(" = ");
        sqlFrom.append(_getCollection()).append(IENDefinitionFormuleDefTable.TABLE_NAME).append(".")
                .append(IENDefinitionFormuleDefTable.CS_DOCUMENT);

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer sqlOrder = new StringBuffer();
        sqlOrder.append(_getCollection()).append(TABLE_COU).append(".").append(FIELD_PCOLUT);
        return sqlOrder.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (sqlWhere.toString().length() != 0) {
            sqlWhere.append(" AND ");
        }
        sqlWhere.append(_getCollection()).append(TABLE_COU).append(".").append(FIELD_PLAIDE).append("=")
                .append(_dbWriteString(statement.getTransaction(), getSession().getIdLangue()));

        sqlWhere.append(" AND ");
        sqlWhere.append(_getCollection()).append(IENFormuleDefTable.TABLE_NAME).append(".")
                .append(IENFormuleDefTable.CS_LANGUE).append("=")
                .append(_dbWriteNumeric(statement.getTransaction(), ILEConstantes.CS_ALLEMAND)); // On
        // met
        // tjrs
        // en
        // allemand
        // car
        // tout
        // est
        // configuré
        // en
        // allendand
        // dans
        // leo

        if (getFromCsDocumentValue().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(TABLE_COU).append(".").append(FIELD_PCOLUT).append(">=")
                    .append(_dbWriteString(statement.getTransaction(), getFromCsDocumentValue()));
        }
        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LESelectFormuleViewBean();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getFromCsDocumentValue() {
        return fromCsDocumentValue;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setFromCsDocumentValue(String fromCsDocumentValue) {
        this.fromCsDocumentValue = fromCsDocumentValue;
    }

}
