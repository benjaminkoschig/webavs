/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.basesindemnisation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAbstractManagerHierarchique;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJBaseIndemnisationManager extends PRAbstractManagerHierarchique {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = "";
    private String[] forCsEtats = null;
    private String forDateDebutPeriode = "";
    private String forDateFinPeriode = "";
    private String forIdCorrection = "";
    private String forIdParent = "";
    private String forIdPrononce = "";
    private boolean forIsActiveDuringPeriode = false;
    private String fromDateDebut = "";
    private String notForCsEtat = "";
    private String notForIdBaseIndemnisation = "";
    private String notForIdParent = "";

    private Boolean parentOnly;
    private String periodeDateDebut = "";
    private String periodeDateFin = "";

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

        if (!JAUtil.isDateEmpty(fromDateDebut)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE);
            whereClause.append(">=");
            whereClause.append(_dbWriteDateAMJ(transaction, fromDateDebut));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdPrononce)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_IDPRONONCE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdPrononce));
        }

        if (!JadeStringUtil.isIntegerEmpty(notForIdBaseIndemnisation)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);
            whereClause.append("<>");
            whereClause.append(_dbWriteNumeric(transaction, notForIdBaseIndemnisation));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdParent)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_IDPARENT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdParent));
        }

        if (!JadeStringUtil.isIntegerEmpty(notForIdParent)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_IDPARENT);
            whereClause.append("<>");
            whereClause.append(_dbWriteNumeric(transaction, notForIdParent));
        }

        if (parentOnly != null) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_IDPARENT);
            whereClause.append(parentOnly.booleanValue() ? "=" : "<>");
            whereClause.append(_dbWriteNumeric(transaction, "0"));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdCorrection)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_ID_CORRECTION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdCorrection));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_ETAT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forCsEtat));
        }

        if (forCsEtats != null) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("(");

            for (int idEtat = 0; idEtat < forCsEtats.length; ++idEtat) {
                if (idEtat > 0) {
                    whereClause.append(" OR ");
                }

                whereClause.append(IJBaseIndemnisation.FIELDNAME_ETAT);
                whereClause.append("=");
                whereClause.append(_dbWriteNumeric(transaction, forCsEtats[idEtat]));
            }

            whereClause.append(")");
        }

        if (!JadeStringUtil.isIntegerEmpty(notForCsEtat)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_ETAT);
            whereClause.append("<>");
            whereClause.append(_dbWriteNumeric(transaction, notForCsEtat));
        }

        if (!JAUtil.isDateEmpty(forDateDebutPeriode)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE);
            whereClause.append(">=");
            whereClause.append(_dbWriteDateAMJ(transaction, forDateDebutPeriode));
            whereClause.append(" AND ");
            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE);
            whereClause.append("<=");
            whereClause.append(_dbWriteDateAMJ(transaction, forDateFinPeriode));
        }

        if (forIsActiveDuringPeriode) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("(");
            // date de debut dans periode
            whereClause.append("(");
            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE);
            whereClause.append("<=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));
            whereClause.append(" AND ");
            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE);
            whereClause.append(">=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));
            whereClause.append(")");

            whereClause.append(" OR ");
            // date de fin dans periode
            whereClause.append("(");
            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE);
            whereClause.append("<=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), periodeDateFin));
            whereClause.append(" AND ");
            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE);
            whereClause.append(">=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));
            whereClause.append(")");

            whereClause.append(")");
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
        return new IJBaseIndemnisation();
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
     * getter pour l'attribut for cs etats.
     * 
     * @return la valeur courante de l'attribut for cs etats
     */
    public String[] getForCsEtats() {
        return forCsEtats;
    }

    /**
     * getter pour l'attribut for date debut periode.
     * 
     * @return la valeur courante de l'attribut for date debut periode
     */
    public String getForDateDebutPeriode() {
        return forDateDebutPeriode;
    }

    /**
     * getter pour l'attribut for date fin periode.
     * 
     * @return la valeur courante de l'attribut for date fin periode
     */
    public String getForDateFinPeriode() {
        return forDateFinPeriode;
    }

    /**
     * getter pour l'attribut for id correction.
     * 
     * @return la valeur courante de l'attribut for id correction
     */
    public String getForIdCorrection() {
        return forIdCorrection;
    }

    /**
     * getter pour l'attribut for id parent.
     * 
     * @return la valeur courante de l'attribut for id parent
     */
    public String getForIdParent() {
        return forIdParent;
    }

    /**
     * getter pour l'attribut for id prononce.
     * 
     * @return la valeur courante de l'attribut for id prononce
     */
    public String getForIdPrononce() {
        return forIdPrononce;
    }

    /**
     * @return
     */
    public String getFromDateDebut() {
        return fromDateDebut;
    }

    @Override
    public String getHierarchicalOrderBy() {

        return getOrderByDefaut();
    }

    /**
     * getter pour l'attribut not for cs etat.
     * 
     * @return la valeur courante de l'attribut not for cs etat
     */
    public String getNotForCsEtat() {
        return notForCsEtat;
    }

    /**
     * getter pour l'attribut not for id base indemnisation.
     * 
     * @return la valeur courante de l'attribut not for id base indemnisation
     */
    public String getNotForIdBaseIndemnisation() {
        return notForIdBaseIndemnisation;
    }

    /**
     * getter pour l'attribut not for id parent.
     * 
     * @return la valeur courante de l'attribut not for id parent
     */
    public String getNotForIdParent() {
        return notForIdParent;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut order by defaut
     * 
     * @see globaz.prestation.db.PRAbstractManager# Defaut()
     */
    @Override
    public String getOrderByDefaut() {
        return IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION;
    }

    /**
     * getter pour l'attribut parent only.
     * 
     * @return la valeur courante de l'attribut parent only
     */
    public String getParentOnly() {
        return (parentOnly != null) ? parentOnly.toString() : "";
    }

    /**
     * setter pour l'attribut for cs etat.
     * 
     * @param forCsEtat
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    /**
     * setter pour l'attribut for cs etats.
     * 
     * @param forCsEtats
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsEtats(String[] forCsEtats) {
        this.forCsEtats = forCsEtats;
    }

    /**
     * setter pour l'attribut for id correction.
     * 
     * @param forIdCorrection
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdCorrection(String forIdCorrection) {
        this.forIdCorrection = forIdCorrection;
    }

    /**
     * setter pour l'attribut for id parent.
     * 
     * @param forIdParent
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdParent(String forIdParent) {
        this.forIdParent = forIdParent;
    }

    /**
     * setter pour l'attribut for id prononce.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdPrononce(String string) {
        forIdPrononce = string;
    }

    /**
     * @param string
     */
    public void setForIsActiveDuringPeriode(String dateDebut, String dateFin) {
        forIsActiveDuringPeriode = true;
        periodeDateDebut = dateDebut;
        periodeDateFin = dateFin;
    }

    /**
     * setter pour l'attribut for periode.
     * 
     * @param forDateDebutPeriode
     *            une nouvelle valeur pour cet attribut
     * @param forDateFinPeriode
     *            une nouvelle valeur pour cet attribut
     */
    public void setForPeriode(String forDateDebutPeriode, String forDateFinPeriode) {
        this.forDateDebutPeriode = forDateDebutPeriode;
        this.forDateFinPeriode = forDateFinPeriode;
    }

    /**
     * @param string
     */
    public void setFromDateDebut(String string) {
        fromDateDebut = string;
    }

    /**
     * setter pour l'attribut not for cs etat.
     * 
     * @param notForCsEtat
     *            une nouvelle valeur pour cet attribut
     */
    public void setNotForCsEtat(String notForCsEtat) {
        this.notForCsEtat = notForCsEtat;
    }

    /**
     * setter pour l'attribut not for id base indemnisation.
     * 
     * @param notForIdBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setNotForIdBaseIndemnisation(String notForIdBaseIndemnisation) {
        this.notForIdBaseIndemnisation = notForIdBaseIndemnisation;
    }

    /**
     * setter pour l'attribut not for id parent.
     * 
     * @param notForIdParent
     *            une nouvelle valeur pour cet attribut
     */
    public void setNotForIdParent(String notForIdParent) {
        this.notForIdParent = notForIdParent;
    }

    /**
     * criteres pour retourner les peres, enfants, ou les deux.
     * 
     * @param parentOnly
     *            si "true", ne retourne que les peres (idParent==0), si "" retourne tout, sinon retourne les enfants
     *            (idParent!=0).
     */
    public void setParentOnly(String parentOnly) {
        this.parentOnly = ((parentOnly != null) && (parentOnly.length() > 0)) ? Boolean.valueOf(parentOnly) : null;
    }
}
