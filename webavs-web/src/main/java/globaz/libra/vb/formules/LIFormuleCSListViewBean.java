package globaz.libra.vb.formules;

import globaz.envoi.db.parametreEnvoi.access.ENDefinitionFormuleManager;
import globaz.envoi.db.parametreEnvoi.access.IENDefinitionFormuleDefTable;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author hpe
 * 
 */
public class LIFormuleCSListViewBean extends ENDefinitionFormuleManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Définit le mode qui prend toutes les valeurs */
    public static final int ALL = 1;

    /** Table : AIEPDEF */
    private String fromCsDocumentValue = new String();

    /**
     * @see globaz.globall.db.BManager#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(super._getFrom(statement));

        String innerJoin = " INNER JOIN ";
        String point = ".";
        String schema = _getCollection();

        // jointure sur FWCOUP pour recherche sur pcolut
        sqlFrom.append(innerJoin);
        sqlFrom.append(schema);
        sqlFrom.append("FWCOUP");
        sqlFrom.append(" ON ").append(schema).append("FWCOUP").append(point).append("PCOSID");
        sqlFrom.append("=").append(schema).append(IENDefinitionFormuleDefTable.TABLE_NAME).append(point)
                .append(IENDefinitionFormuleDefTable.CS_DOCUMENT);
        sqlFrom.append(" AND ").append(schema).append("FWCOUP").append(point).append("PLAIDE").append("=").append("'")
                .append(getSession().getIdLangue()).append("'");

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer(super._getWhere(statement));

        if (getFromCsDocumentValue().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection() + "FWCOUP.PCOLUT").append(">=")
                    .append(_dbWriteString(statement.getTransaction(), getFromCsDocumentValue()));
        }

        return sqlWhere.toString();
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * 
     * @throws Exception
     *             la création a échouée
     */
    @Override
    public BEntity _newEntity() throws Exception {
        return new LiFormuleCSViewBean();
    }

    /**
     * Returns the forCsDocumentValue.
     * 
     * @return String
     */
    public String getFromCsDocumentValue() {
        return fromCsDocumentValue;
    }

    /**
     * Sets the forCsDocumentValue.
     * 
     * @param forCsDocumentValue
     *            The forCsDocumentValue to set
     */
    public void setFromCsDocumentValue(String forCsDocumentValue) {
        fromCsDocumentValue = forCsDocumentValue;
    }
}