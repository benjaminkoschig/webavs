/*
 * Created on Jul 18, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.naos.db.affiliation;

import globaz.globall.db.BStatement;

/**
 * @author cuva Created on Jul 18, 2006
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class AFAffiliationUnionCotisation extends AFAffiliationUnionTierCountByCanton {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String fromClause = null;

    private String nbAffilie = "";

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
        StringBuffer sbFields = new StringBuffer(50);
        sbFields.append(AFAffiliation.TABLE_NAME + ".");
        sbFields.append(AFAffiliation.FIELDNAME_AFFILIATION_TYPE);
        sbFields.append(", COUNT(*) AS " + AFAffiliationUnionTierCountByCanton.FIELDNAME_NB_AFFILIE);
        return sbFields.toString();
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
            fromClause = AFAffiliationUnionTierCountByCanton.createFromClause(_getCollection());
        }

        return fromClause;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    // /**
    // * DOCUMENT ME!
    // *
    // * @param schema DOCUMENT ME!
    // *
    // * @return DOCUMENT ME!
    // */
    // public static final String createFromClause(String schema) {
    // StringBuffer fromClauseBuffer = new StringBuffer();
    // String innerJoin = " INNER JOIN ";
    // String leftJoin = " LEFT JOIN ";
    // String on = " ON ";
    // String and = " AND ";
    // String point = ".";
    // String egal = "=";
    //
    // fromClauseBuffer.append(schema);
    // fromClauseBuffer.append(AFAffiliation.TABLE_NAME);
    // // Jointure entre table affiliation et Adhesion
    // fromClauseBuffer.append(innerJoin);
    // fromClauseBuffer.append(schema);
    // fromClauseBuffer.append(AFAdhesion.TABLE_NAME);
    // fromClauseBuffer.append(on);
    // fromClauseBuffer.append(schema);
    // fromClauseBuffer.append(AFAdhesion.TABLE_NAME);
    // fromClauseBuffer.append(point);
    // fromClauseBuffer.append(AFAdhesion.FIELDNAME_AFFILIATION_ID);
    // fromClauseBuffer.append(egal);
    // fromClauseBuffer.append(schema);
    // fromClauseBuffer.append(AFAffiliation.TABLE_NAME);
    // fromClauseBuffer.append(point);
    // fromClauseBuffer.append(AFAffiliation.FIELDNAME_AFFILIATION_ID);
    //
    // // Jointure entre table cotisation et Adhesion
    // fromClauseBuffer.append(innerJoin);
    // fromClauseBuffer.append(schema);
    // fromClauseBuffer.append(AFCotisation.TABLE_NAME);
    // fromClauseBuffer.append(on);
    // fromClauseBuffer.append(schema);
    // fromClauseBuffer.append(AFAdhesion.TABLE_NAME);
    // fromClauseBuffer.append(point);
    // fromClauseBuffer.append(AFAdhesion.FIELDNAME_ADHESION_ID);
    // fromClauseBuffer.append(egal);
    // fromClauseBuffer.append(schema);
    // fromClauseBuffer.append(AFCotisation.TABLE_NAME);
    // fromClauseBuffer.append(point);
    // fromClauseBuffer.append(AFCotisation.FIELDNAME_ADHESION_ID);
    //
    // return fromClauseBuffer.toString();
    // }
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

        nbAffilie = statement.dbReadNumeric(AFAffiliationUnionTierCountByCanton.FIELDNAME_NB_AFFILIE);
    }

    /**
     * @return
     */
    @Override
    public String getNbAffilie() {
        return nbAffilie;
    }

    /**
     * @param string
     */
    @Override
    public void setNbAffilie(String string) {
        nbAffilie = string;
    }

}
