/*
 * Créé le 07 nov. 07
 */
package globaz.corvus.db.recap;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;

/**
 * <H1>Description</H1>
 * 
 * @deprecated utiliser RERecapInfoManager
 * @author scr
 */
@Deprecated
public class REInfoRecapManagerOld extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forCodeRecap = "";

    // Format mm.aaaa
    private String forDate = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isBlankOrZero(forDate)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REInfoRecapOld.FIELDNAME_DATE_PMT
                    + "="
                    + _dbWriteNumeric(statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forDate));
        }

        if (!JadeStringUtil.isBlankOrZero(forCodeRecap)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REInfoRecapOld.FIELDNAME_CODE_RECAP + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCodeRecap);

        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REInfoRecapOld();
    }

    public String getForCodeRecap() {
        return forCodeRecap;
    }

    public String getForDate() {
        return forDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REInfoRecapOld.TABLE_NAME_INFO_RECAP;
    }

    public void setForCodeRecap(String forCodeRecap) {
        this.forCodeRecap = forCodeRecap;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

}
