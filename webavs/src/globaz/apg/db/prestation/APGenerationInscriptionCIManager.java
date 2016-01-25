/*
 * Créé le 20 juin 05
 */
package globaz.apg.db.prestation;

import globaz.apg.api.prestation.IAPPrestation;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APGenerationInscriptionCIManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String fields = null;
    private String forIdLot = "";
    private String forTypePaiement = "";
    private String forTypePrestation = "";
    private String fromClause = null;
    private boolean onlyStandardPrestation = false;

    private boolean parentOnly = false;
    private boolean sansIdInscriptionCi = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFields(final BStatement statement) {
        if (fields == null) {
            fields = APGenerationInscriptionCI.createFields(_getCollection());
        }

        return fields;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(final BStatement statement) {
        if (fromClause == null) {
            fromClause = APGenerationInscriptionCI.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getGroupBy(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getGroupBy(final BStatement statement) {

        String groupBy = "";

        groupBy += " GROUP BY ";
        groupBy += APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT;
        groupBy += ", ";
        groupBy += APRepartitionPaiements.FIELDNAME_IDTIERS;
        groupBy += ", ";
        groupBy += APPrestation.FIELDNAME_DATEDEBUT;
        groupBy += ", ";
        groupBy += APPrestation.FIELDNAME_DATEFIN;
        groupBy += ", ";
        groupBy += APRepartitionPaiements.FIELDNAME_MONTANTBRUT;
        groupBy += ", ";
        groupBy += APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT;
        groupBy += ", ";
        groupBy += APPrestation.FIELDNAME_IDRESTITUTION;
        groupBy += " ";

        return groupBy;
        // return super._getGroupBy(statement);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(final BStatement statement) {
        String sqlWhere = "";

        if (isParentOnly()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += " (" + _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_IDPARENT + " = 0 OR " + _getCollection()
                    + APRepartitionPaiements.TABLE_NAME + "." + APRepartitionPaiements.FIELDNAME_IDPARENT
                    + " is NULL) ";
        }

        if (!JadeStringUtil.isIntegerEmpty(forTypePrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_TYPEPRESTATION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forTypePrestation);
        }

        if (sansIdInscriptionCi) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "(" + _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_IDINSCRIPTIONCI + "=0 OR " + _getCollection()
                    + APRepartitionPaiements.TABLE_NAME + "." + APRepartitionPaiements.FIELDNAME_IDINSCRIPTIONCI
                    + " is NULL)";
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_IDLOT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdLot);
        }

        if (onlyStandardPrestation) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APPrestation.TABLE_NAME + "." + APPrestation.FIELDNAME_GENRE_PRESTATION
                    + "<>" + this._dbWriteNumeric(statement.getTransaction(), IAPPrestation.CS_GENRE_ACM_ALPHA)
                    + " AND " + _getCollection() + APPrestation.TABLE_NAME + "."
                    + APPrestation.FIELDNAME_GENRE_PRESTATION + "<>"
                    + this._dbWriteNumeric(statement.getTransaction(), IAPPrestation.CS_GENRE_LAMAT);
        }

        return sqlWhere + _getGroupBy(statement);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APGenerationInscriptionCI();
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
     * getter pour l'attribut for type prestation
     * 
     * @return la valeur courante de l'attribut for type prestation
     */
    public String getForTypePrestation() {
        return forTypePrestation;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT;
    }

    /**
     * @return
     */
    public boolean isOnlyStandardPrestation() {
        return onlyStandardPrestation;
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
    public void setForIdLot(final String string) {
        forIdLot = string;
    }

    /**
     * setter pour l'attribut for type paiement
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypePaiement(final String string) {
        forTypePaiement = string;
    }

    /**
     * setter pour l'attribut for type prestation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypePrestation(final String string) {
        forTypePrestation = string;
    }

    /**
     * @param b
     */
    public void setOnlyStandardPrestation(final boolean b) {
        onlyStandardPrestation = b;
    }

    /**
     * setter pour l'attribut parent only
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setParentOnly(final boolean b) {
        parentOnly = b;
    }

    /**
     * setter pour l'attribut sans id inscription ci
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setSansIdInscriptionCi(final boolean b) {
        sansIdInscriptionCi = b;
    }

}
