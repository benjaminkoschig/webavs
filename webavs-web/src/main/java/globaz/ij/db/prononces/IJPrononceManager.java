package globaz.ij.db.prononces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJPrononceManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String date2AnsRetro = "";
    private String forCsEtat = "";
    private String[] forCsEtats = null;
    private String forIdCorrection = "";
    private String forIdParent = "";
    private String forIdParentCorrigerDepuis = "";
    private String forIdPrononce = "";
    private boolean forIsActiveDuringPeriode = false;
    private String fromDateDebut = "";
    private String fromDateFinSiRenseigne = "";
    private String periodeDateDebut = "";

    private String periodeDateFin = "";
    private String toDateFin = "";
    private String untilDateFin = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdPrononce)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrononce.FIELDNAME_ID_PRONONCE);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), forIdPrononce));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdParentCorrigerDepuis)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrononce.FIELDNAME_PARENT_CORRIGE_DEPUIS);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), forIdParentCorrigerDepuis));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdParent)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrononce.FIELDNAME_ID_PARENT);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), forIdParent));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdCorrection)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrononce.FIELDNAME_ID_CORRECTION);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), forIdCorrection));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrononce.FIELDNAME_CS_ETAT);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), forCsEtat));
        }

        if (!JAUtil.isDateEmpty(fromDateFinSiRenseigne)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("( ");
            whereClause.append(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
            whereClause.append(">=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), fromDateFinSiRenseigne));
            whereClause.append(" OR ");
            whereClause.append(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
            whereClause.append(" IS NULL ");
            whereClause.append(" OR ");
            whereClause.append(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
            whereClause.append(" = 0 ");
            whereClause.append(") ");

        }

        if (!JAUtil.isDateEmpty(untilDateFin)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
            whereClause.append("<=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), untilDateFin));
        }

        if ((forCsEtats != null) && (forCsEtats.length > 0)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("(");

            for (int idEtat = 0; idEtat < forCsEtats.length; ++idEtat) {
                if (idEtat > 0) {
                    whereClause.append(" OR ");
                }

                whereClause.append(IJPrononce.FIELDNAME_CS_ETAT);
                whereClause.append("=");
                whereClause.append(this._dbWriteNumeric(statement.getTransaction(), forCsEtats[idEtat]));
            }

            whereClause.append(")");
        }

        if (!JadeStringUtil.isEmpty(fromDateDebut)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("(");
            whereClause.append(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE);
            whereClause.append(">=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), fromDateDebut));
            whereClause.append(" OR ");
            whereClause.append(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), "0"));
            whereClause.append(")");
        }

        if (!JadeStringUtil.isEmpty(toDateFin)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("(");
            whereClause.append(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
            whereClause.append("<=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), toDateFin));
            whereClause.append(" OR ");
            whereClause.append(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), "0"));
            whereClause.append(")");
        }

        if (!JadeStringUtil.isEmpty(date2AnsRetro)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE);
            whereClause.append("<=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), date2AnsRetro));
        }

        if (forIsActiveDuringPeriode) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("(");
            // date de debut dans periode
            whereClause.append("(");
            whereClause.append(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE);
            whereClause.append(">=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));
            whereClause.append(" AND ");
            whereClause.append(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE);
            whereClause.append("<=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), periodeDateFin));
            whereClause.append(")");

            whereClause.append(" OR ");
            // date de fin dans periode
            whereClause.append("(");
            whereClause.append(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
            whereClause.append(">=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));
            whereClause.append(" AND ");
            whereClause.append(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
            whereClause.append("<=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), periodeDateFin));
            whereClause.append(")");

            whereClause.append(" OR ");
            // periode comprise dans date fin et date debut
            whereClause.append("(");
            whereClause.append("(");
            whereClause.append(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE);
            whereClause.append("<");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));

            whereClause.append(" OR ");

            whereClause.append(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), "0"));
            whereClause.append(")");
            whereClause.append(" AND ");
            whereClause.append("(");
            whereClause.append(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
            whereClause.append(">");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), periodeDateFin));

            whereClause.append(" OR ");

            whereClause.append(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), "0"));
            whereClause.append(")");
            whereClause.append(")");

            whereClause.append(")");
        }

        return whereClause.toString();
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
        return new IJPrononce();
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

    public String getForIdParentCorrigerDepuis() {
        return forIdParentCorrigerDepuis;
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

    /**
     * @return
     */
    public String getFromDateFinSiRenseigne() {
        return fromDateFinSiRenseigne;
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
        return IJPrononce.FIELDNAME_ID_PRONONCE;
    }

    /**
     * @return
     */
    public String getToDateFin() {
        return toDateFin;
    }

    /**
     * @return
     */
    public String getUntilDateFin() {
        return untilDateFin;
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
     * 
     * @param string
     */
    public void setForDateDebutAnterieurA(String string) {
        date2AnsRetro = string;
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

    public void setForIdParentCorrigerDepuis(String forIdAnnulerCorrigerParent) {
        forIdParentCorrigerDepuis = forIdAnnulerCorrigerParent;
    }

    /**
     * setter pour l'attribut for id prononce.
     * 
     * @param forIdPrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdPrononce(String forIdPrononce) {
        this.forIdPrononce = forIdPrononce;
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
     * @param string
     */
    public void setFromDateDebut(String string) {
        fromDateDebut = string;
    }

    /**
     * @param string
     */
    public void setFromDateFinSiRenseigne(String string) {
        fromDateFinSiRenseigne = string;
    }

    /**
     * @param string
     */
    public void setToDateFin(String string) {
        toDateFin = string;
    }

    /**
     * @param string
     */
    public void setUntilDateFin(String string) {
        untilDateFin = string;
    }
}
