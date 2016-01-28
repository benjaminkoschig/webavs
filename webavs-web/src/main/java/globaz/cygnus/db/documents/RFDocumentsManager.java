/*
 * Cr�� le 02 aout 2010
 */
package globaz.cygnus.db.documents;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author fha
 */
public class RFDocumentsManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * G�n�ration de la clause from pour la requ�te
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDocuments.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private String forDocument = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe RFSousTypeDeSoinManager
     */
    public RFDocumentsManager() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * Red�finition de la m�thode _getWhere du parent afin de g�n�rer le WHERE de la requ�te en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forDocument)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDocuments.FIELDNAME_ID_DOCUMENT);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteString(statement.getTransaction(), forDocument));
        }

        return sqlWhere.toString();
    }

    /**
     * D�finition de l'entit� (RFSousTypeDeSoin)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDocuments();
    }

    public String getFromClause() {
        return fromClause;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return RFDocuments.FIELDNAME_ID_DOCUMENT;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}