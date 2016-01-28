/*
 * Created on Jul 14, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.naos.db.parametreAssurance;

import globaz.globall.db.BStatement;
import globaz.naos.db.assurance.AFAssurance;

/**
 * @author cuva Created on Jul 14, 2006
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class AFParametreAssuranceUnionAssurance extends AFParametreAssurance {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String and = " AND ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFParametreAssurance.TABLE_NAME);

        // Jointure entre table tier et personne
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAssurance.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAssurance.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAssurance.FIELD_ID_ASSURANCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFParametreAssurance.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFParametreAssurance.FIELDNAME_ASSUR_ID);

        return fromClauseBuffer.toString();
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String typeAssurance = null;

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getFields(BStatement statement) {
        // TODO Auto-generated method stub
        return super._getFields(statement);
    }

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = AFParametreAssuranceUnionAssurance.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * Surcharge.
     * 
     * @param statement
     * @throws Exception
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     **/
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        typeAssurance = statement.dbReadNumeric(AFAssurance.FIELD_TYPE_ASSURANCE);
    }

    /**
     * @return
     */
    public String getTypeAssurance() {
        return typeAssurance;
    }

    /**
     * @param string
     */
    public void setTypeAssurance(String string) {
        typeAssurance = string;
    }

}
