/*
 * Créé le 8 sept. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJPrestationManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = "";
    private String forCsType = "";
    private String forIdAnnonce = "";
    private String forIdBaseIndemnisation = "";
    private String forIdIJCalculee = "";
    private String forIdLot = "";
    private String notForCsType = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        BTransaction transaction = statement.getTransaction();
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrestation.FIELDNAME_CS_ETAT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forCsEtat));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdLot)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrestation.FIELDNAME_IDLOT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdLot));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdIJCalculee)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrestation.FIELDNAME_ID_IJCALCULEE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdIJCalculee));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdBaseIndemnisation)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrestation.FIELDNAME_ID_BASEINDEMNISATION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdBaseIndemnisation));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdAnnonce)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrestation.FIELDNAME_IDANNONCE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdAnnonce));
        }

        if (!JadeStringUtil.isIntegerEmpty(notForCsType)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrestation.FIELDNAME_CS_TYPE);
            whereClause.append("<>");
            whereClause.append(_dbWriteNumeric(transaction, notForCsType));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsType)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrestation.FIELDNAME_CS_TYPE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forCsType));
        }

        return whereClause.toString();
    }

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJPrestation();
    }

    /**
     * getter pour l'attribut for cs etat.
     * 
     * @return la valeur courante de l'attribut for cs etat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * getter pour l'attribut for cs type.
     * 
     * @return la valeur courante de l'attribut for cs type
     */
    public String getForCsType() {
        return forCsType;
    }

    /**
     * getter pour l'attribut for id annonce
     * 
     * @return la valeur courante de l'attribut for id annonce
     */
    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    /**
     * getter pour l'attribut for id base indemnisation.
     * 
     * @return la valeur courante de l'attribut for id base indemnisation
     */
    public String getForIdBaseIndemnisation() {
        return forIdBaseIndemnisation;
    }

    /**
     * getter pour l'attribut for id IJCalculee.
     * 
     * @return la valeur courante de l'attribut for id IJCalculee
     */
    public String getForIdIJCalculee() {
        return forIdIJCalculee;
    }

    /**
     * getter pour l'attribut for id lot.
     * 
     * @return la valeur courante de l'attribut for id lot
     */
    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * getter pour l'attribut not for cs type.
     * 
     * @return la valeur courante de l'attribut not for cs type
     */
    public String getNotForCsType() {
        return notForCsType;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut order by defaut
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return IJPrestation.FIELDNAME_IDPRESTATION;
    }

    /**
     * setter pour l'attribut for cs etat.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsEtat(String string) {
        forCsEtat = string;
    }

    /**
     * setter pour l'attribut for cs type.
     * 
     * @param forCsType
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsType(String forCsType) {
        this.forCsType = forCsType;
    }

    /**
     * setter pour l'attribut for id annonce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdAnnonce(String string) {
        forIdAnnonce = string;
    }

    /**
     * setter pour l'attribut for id base indemnisation.
     * 
     * @param forIdBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdBaseIndemnisation(String forIdBaseIndemnisation) {
        this.forIdBaseIndemnisation = forIdBaseIndemnisation;
    }

    /**
     * setter pour l'attribut for id IJCalculee.
     * 
     * @param forIdIJCalculee
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdIJCalculee(String forIdIJCalculee) {
        this.forIdIJCalculee = forIdIJCalculee;
    }

    /**
     * setter pour l'attribut for id lot.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdLot(String string) {
        forIdLot = string;
    }

    /**
     * setter pour l'attribut not for cs type.
     * 
     * @param notForCsEtat
     *            une nouvelle valeur pour cet attribut
     */
    public void setNotForCsType(String notForCsEtat) {
        notForCsType = notForCsEtat;
    }
}
