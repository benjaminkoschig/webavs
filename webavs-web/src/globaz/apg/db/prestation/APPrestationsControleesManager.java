/*
 * Créé le 3 juin 05
 */
package globaz.apg.db.prestation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APPrestationsControleesManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String fields = null;

    private String forEtat = "";

    private String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (fields == null) {
            fields = APPrestationsControlees.createFields(_getCollection());
        }

        return fields;
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = APPrestationsControlees.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(forEtat)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_ETAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forEtat);
        }

        return sqlWhere; // + " GROUP BY " +
        // APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APPrestationsControlees();
    }

    /**
     * getter pour l'attribut for etat
     * 
     * @return la valeur courante de l'attribut from etat
     */
    public String getForEtat() {
        return forEtat;
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
        return APPrestation.FIELDNAME_IDPRESTATIONAPG;
    }

    /**
     * setter pour l'attribut for etat
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtat(String string) {
        forEtat = string;
    }
}
