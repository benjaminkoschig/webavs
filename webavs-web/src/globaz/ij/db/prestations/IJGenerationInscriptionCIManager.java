/*
 * Créé le 20 juin 05
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
public class IJGenerationInscriptionCIManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String fields = null;
    private String forIdLot = "";
    private String forTypePaiement = "";
    private String fromClause = null;

    private boolean parentOnly = false;
    private boolean sansIdInscriptionCi = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (fields == null) {
            fields = IJGenerationInscriptionCI.createFields(_getCollection());
        }

        return fields;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = IJGenerationInscriptionCI.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (isParentOnly()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += " (" + _getCollection() + IJRepartitionPaiements.TABLE_NAME + "."
                    + IJRepartitionPaiements.FIELDNAME_IDPARENT + " = 0 OR " + _getCollection()
                    + IJRepartitionPaiements.TABLE_NAME + "." + IJRepartitionPaiements.FIELDNAME_IDPARENT
                    + " is NULL) ";
        }

        if (!JadeStringUtil.isIntegerEmpty(forTypePaiement)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJRepartitionPaiements.TABLE_NAME + "."
                    + IJRepartitionPaiements.FIELDNAME_TYPEPAIEMENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forTypePaiement);
        }

        if (sansIdInscriptionCi) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "(" + _getCollection() + IJRepartitionPaiements.TABLE_NAME + "."
                    + IJRepartitionPaiements.FIELDNAME_IDINSCRIPTIONCI + "=0 OR " + _getCollection()
                    + IJRepartitionPaiements.TABLE_NAME + "." + IJRepartitionPaiements.FIELDNAME_IDINSCRIPTIONCI
                    + " is NULL)";
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJPrestation.TABLE_NAME + "." + IJPrestation.FIELDNAME_IDLOT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdLot);
        }

        return sqlWhere;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJGenerationInscriptionCI();
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
     * getter pour l'attribut for type paiement
     * 
     * @return la valeur courante de l'attribut for type paiement
     */
    public String getForTypePaiement() {
        return forTypePaiement;
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
     * getter pour l'attribut parent only
     * 
     * @return la valeur courante de l'attribut parent only
     */
    public boolean isParentOnly() {
        return parentOnly;
    }

    /**
     * getter pour l'attribut sans id inscription ci
     * 
     * @return la valeur courante de l'attribut sans id inscription ci
     */
    public boolean isSansIdInscriptionCi() {
        return sansIdInscriptionCi;
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
     * setter pour l'attribut for type paiement
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypePaiement(String string) {
        forTypePaiement = string;
    }

    /**
     * setter pour l'attribut parent only
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setParentOnly(boolean b) {
        parentOnly = b;
    }

    /**
     * setter pour l'attribut sans id inscription ci
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setSansIdInscriptionCi(boolean b) {
        sansIdInscriptionCi = b;
    }
}
