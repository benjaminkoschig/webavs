/*
 * Créé le 8 sept. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAbstractManagerHierarchique;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJRepartitionPaiementsManager extends PRAbstractManagerHierarchique {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCompensation = "";
    private String forIdDomaineAdressePaiement = "";
    private String forIdParent = "";
    private String forIdPrestation = "";
    private String forIdTiersAdressePaiement = "";
    private Boolean forParentOnly;
    private String notForIdRepartitionPaiement = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        BTransaction transaction = statement.getTransaction();
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdPrestation)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJRepartitionPaiements.FIELDNAME_IDPRESTATION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdPrestation));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdParent)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJRepartitionPaiements.FIELDNAME_IDPARENT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdParent));
        }

        if (!JadeStringUtil.isIntegerEmpty(notForIdRepartitionPaiement)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJRepartitionPaiements.FIELDNAME_IDREPARTITION_PAIEMENT);
            whereClause.append("<>");
            whereClause.append(_dbWriteNumeric(transaction, notForIdRepartitionPaiement));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdCompensation)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJRepartitionPaiements.FIELDNAME_IDCOMPENSATION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdCompensation));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersAdressePaiement)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJRepartitionPaiements.FIELDNAME_IDTIERSADRESSEPAIEMENT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdTiersAdressePaiement));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDomaineAdressePaiement)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJRepartitionPaiements.FIELDNAME_IDDOMAINEADRESSEPAIEMENT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdDomaineAdressePaiement));
        }

        if (forParentOnly != null) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJRepartitionPaiements.FIELDNAME_IDPARENT);
            whereClause.append(forParentOnly.booleanValue() ? "=" : "<>");
            whereClause.append(_dbWriteNumeric(transaction, "0"));
        }

        return whereClause.toString();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJRepartitionPaiements();
    }

    /**
     * getter pour l'attribut for id compensation
     * 
     * @return la valeur courante de l'attribut for id compensation
     */
    public String getForIdCompensation() {
        return forIdCompensation;
    }

    /**
     * getter pour l'attribut sor id domaine adresse paiement
     * 
     * @return la valeur courante de l'attribut sor id domaine adresse paiement
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
     * getter pour l'attribut for id tiers adresse paiement
     * 
     * @return la valeur courante de l'attribut for id tiers adresse paiement
     */
    public String getForIdTiersAdressePaiement() {
        return forIdTiersAdressePaiement;
    }

    /**
     * getter pour l'attribut for parent only
     * 
     * @return la valeur courante de l'attribut for parent only
     */
    public String getForParentOnly() {
        return (forParentOnly != null) ? forParentOnly.toString() : "";
    }

    @Override
    public String getHierarchicalOrderBy() {
        return getOrderByDefaut();
    }

    /**
     * getter pour l'attribut not for id repartition paiement
     * 
     * @return la valeur courante de l'attribut not for id repartition paiement
     */
    public String getNotForIdRepartitionPaiement() {
        return notForIdRepartitionPaiement;
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
     * setter pour l'attribut for id compensation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdCompensation(String string) {
        forIdCompensation = string;
    }

    /**
     * setter pour l'attribut sor id domaine adresse paiement
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
     * @param forIdParent
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdParent(String forIdParent) {
        this.forIdParent = forIdParent;
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
     * setter pour l'attribut for id tiers adresse paiement
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdTiersAdressePaiement(String string) {
        forIdTiersAdressePaiement = string;
    }

    /**
     * setter pour l'attribut for parent only
     * 
     * @param forParentOnly
     *            une nouvelle valeur pour cet attribut
     */
    public void setForParentOnly(String forParentOnly) {
        this.forParentOnly = Boolean.valueOf(forParentOnly);
    }

    /**
     * setter pour l'attribut not for id repartition paiement
     * 
     * @param notForIdRepartitionPaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setNotForIdRepartitionPaiement(String notForIdRepartitionPaiement) {
        this.notForIdRepartitionPaiement = notForIdRepartitionPaiement;
    }

}
