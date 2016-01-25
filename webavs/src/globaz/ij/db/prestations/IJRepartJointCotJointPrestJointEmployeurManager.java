/*
 * Créé le 31 août 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJRepartJointCotJointPrestJointEmployeurManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdLot = "";
    private String forIdPrestation = null;
    private boolean forParentOnly = false;
    private String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = IJRepartJointCotJointPrestJointEmployeur.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJPrestation.TABLE_NAME + "." + IJPrestation.FIELDNAME_IDLOT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdLot);
        }
        if (!JadeStringUtil.isIntegerEmpty(forIdPrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJPrestation.TABLE_NAME + "." + IJPrestation.FIELDNAME_IDPRESTATION + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdPrestation);
        }

        if (forParentOnly) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += " (" + _getCollection() + IJRepartitionPaiements.TABLE_NAME + "."
                    + IJRepartitionPaiements.FIELDNAME_IDPARENT + " = 0 OR " + _getCollection()
                    + IJRepartitionPaiements.TABLE_NAME + "." + IJRepartitionPaiements.FIELDNAME_IDPARENT
                    + " is NULL) ";
        }

        return sqlWhere;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJRepartJointCotJointPrestJointEmployeur();
    }

    /**
     * getter pour l'attribut for id lot
     * 
     * @return la valeur courante de l'attribut for id lot
     */
    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * getter pour l'attribut id prestation
     * 
     * @return la valeur courrante de l'attribut for id prestation
     */
    public String getForIdPrestation() {
        return forIdPrestation;
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
        return IJRepartitionPaiements.FIELDNAME_IDREPARTITION_PAIEMENT;
    }

    /**
     * getter pour l'attribut for parent only
     * 
     * @return la valeur courante de l'attribut for parent only
     */
    public boolean isForParentOnly() {
        return forParentOnly;
    }

    /**
     * setter pour l'attribut for id lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdLot(String string) {
        forIdLot = string;
    }

    /**
     * setter pour l'attribut for id prestation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdPrestation(String string) {
        forIdPrestation = string;
    }

    /**
     * setter pour l'attribut for parent only
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setForParentOnly(boolean b) {
        forParentOnly = b;
    }

}
