package globaz.apg.db.prestation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAbstractManagerHierarchique;

/**
 * Descpription
 * 
 * @author scr Date de création 31 mai 05
 */
public class APRepartitionPaiementsManager extends PRAbstractManagerHierarchique {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAffilie = null;
    private String forIdDomaineAdressePaiement = "";
    private String forIdParent = null;
    private String forIdPrestation = null;
    private String forIdTiers = null;
    private String forIdTiersAdressePaiement = "";
    private String forTypePaiement = "";
    private String forTypePrestation = null;
    boolean isParentOnly = false;
    private String notForIdRepartition = null;

    private String orForIdRepartition = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APPrestationManager.
     */
    public APRepartitionPaiementsManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + APRepartitionPaiements.TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
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

        if (!JadeStringUtil.isIntegerEmpty(getForIdAffilie())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_IDAFFILIE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdAffilie());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdPrestation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdPrestation());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdTiers())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_IDTIERS + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdParent())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_IDPARENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdParent());
        }

        if (!JadeStringUtil.isIntegerEmpty(orForIdRepartition)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " OR ";
            }

            sqlWhere += _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), orForIdRepartition);
        }

        if (!JadeStringUtil.isIntegerEmpty(notForIdRepartition)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT + "<>"
                    + _dbWriteNumeric(statement.getTransaction(), notForIdRepartition);
        }

        if (!JadeStringUtil.isIntegerEmpty(forTypePrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_TYPEPRESTATION + "="
                    + _dbWriteNumeric(statement.getTransaction(), forTypePrestation);
        }

        if (isParentOnly()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += " (" + _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_IDPARENT + " = 0 OR " + _getCollection()
                    + APRepartitionPaiements.TABLE_NAME + "." + APRepartitionPaiements.FIELDNAME_IDPARENT
                    + " is NULL) ";
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersAdressePaiement)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_IDTIERSADRESSEPAIEMENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdTiersAdressePaiement);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDomaineAdressePaiement)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_IDDOMAINE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDomaineAdressePaiement);
        }

        if (!JadeStringUtil.isIntegerEmpty(forTypePaiement)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + APRepartitionPaiements.TABLE_NAME + "."
                    + APRepartitionPaiements.FIELDNAME_TYPEPAIEMENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forTypePaiement);
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
        return new APRepartitionPaiements();
    }

    /**
     * getter pour l'attribut for id affilie
     * 
     * @return la valeur courante de l'attribut for id affilie
     */
    public String getForIdAffilie() {
        return forIdAffilie;
    }

    /**
     * getter pour l'attribut for id domaine adresse paiement
     * 
     * @return la valeur courante de l'attribut for id domaine adresse paiement
     */
    public String getForIdDomaineAdressePaiement() {
        return forIdDomaineAdressePaiement;
    }

    /**
     * getter pour l'attribut for id parent
     * 
     * @return la valeur courante de l'attribut for id parent
     */
    public String getForIdParent() {
        return forIdParent;
    }

    /**
     * getter pour l'attribut for id prestation
     * 
     * @return la valeur courante de l'attribut for id prestation
     */
    public String getForIdPrestation() {
        return forIdPrestation;
    }

    /**
     * getter pour l'attribut for id tiers
     * 
     * @return la valeur courante de l'attribut for id tiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * getter pour l'attribut for id tiers adresse paiement
     * 
     * @return la valeur courante de l'attribut for id tiers adresse paiement
     */
    public String getForIdTiersAdressePaiement() {
        return forIdTiersAdressePaiement;
    }

    /**
     * @return
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

    @Override
    public String getHierarchicalOrderBy() {
        return getOrderByDefaut();
    }

    /**
     * getter pour l'attribut not for id repartition
     * 
     * @return la valeur courante de l'attribut not for id repartition
     */
    public String getNotForIdRepartition() {
        return notForIdRepartition;
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
        return APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT;
    }

    /**
     * getter pour l'attribut for id repartition OR
     * 
     * @return la valeur courante de l'attribut for id repartition OR
     */
    public String getOrForIdRepartition() {
        return orForIdRepartition;
    }

    /**
     * getter pour l'attribut parent only
     * 
     * @return la valeur courante de l'attribut parent only
     */
    public boolean isParentOnly() {
        return isParentOnly;
    }

    /**
     * setter pour l'attribut for id affilie
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdAffilie(String string) {
        forIdAffilie = string;
    }

    /**
     * setter pour l'attribut for id domaine adresse paiement
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDomaineAdressePaiement(String string) {
        forIdDomaineAdressePaiement = string;
    }

    /**
     * setter pour l'attribut for id parent
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdParent(String string) {
        forIdParent = string;
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
     * setter pour l'attribut for id tiers
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdTiers(String string) {
        forIdTiers = string;
    }

    /**
     * setter pour l'attribut for id tiers adresse paiement
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdTiersAdressePaiement(String string) {
        forIdTiersAdressePaiement = string;
    }

    /**
     * @param string
     */
    public void setForTypePaiement(String string) {
        forTypePaiement = string;
    }

    /**
     * setter pour l'attribut for type prestation
     * 
     * @param forTypePrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypePrestation(String forTypePrestation) {
        this.forTypePrestation = forTypePrestation;
    }

    /**
     * setter pour l'attribut not for id repartition
     * 
     * @param notForIdRepartition
     *            une nouvelle valeur pour cet attribut
     */
    public void setNotForIdRepartition(String notForIdRepartition) {
        this.notForIdRepartition = notForIdRepartition;
    }

    /**
     * setter pour l'attribut for id repartition OR
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setOrForIdRepartition(String string) {
        orForIdRepartition = string;
    }

    /**
     * setter pour l'attribut parent only
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setParentOnly(boolean b) {
        isParentOnly = b;
    }

}
