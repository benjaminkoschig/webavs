package globaz.corvus.db.lots;

import globaz.corvus.api.lots.IRELot;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class RELotManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FOR_CS_TYPE_LOT_IN_DECISION_MENSUEL = "0A00";
    public static final String FOR_CS_TYPE_LOT_IN_DECISION_ALL = "0B00";

    private String forCsEtat = "";
    private String forCsEtatDiffentDe = "";

    private String forCsEtatIn = "";

    private String forCsLotOwner = "";
    private String forCsType = "";
    private String forCsTypeDiffentDe = "";
    private String forDateCreation = "";

    private String forDateEnvoiInMMxAAAA = "";
    private String fromDateCreation = "";

    private Boolean isTriParDateEnvoi = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {

        String fields = "";

        fields += RELot.FIELDNAME_DATE_CREATION + ", ";
        fields += RELot.FIELDNAME_DATE_ENVOI + ", ";
        fields += RELot.FIELDNAME_DESCRIPTION + ", ";
        fields += RELot.FIELDNAME_ETAT + ", ";
        fields += RELot.FIELDNAME_ID_JOURNAL_CA + ", ";
        fields += RELot.FIELDNAME_ID_LOT + ", ";
        fields += RELot.FIELDNAME_LOT_OWNER + ", ";
        fields += RELot.FIELDNAME_TYPE_LOT;

        if ((isTriParDateEnvoi() != null) && isTriParDateEnvoi().booleanValue()) {
            fields += ", ";
            fields += "CASE ";
            fields += "WHEN YTDENV IS NULL OR YTDENV = 0 ";
            fields += "THEN 99999999 ";
            fields += "ELSE YTDENV ";
            fields += "END AS DATE_ENVOI ";
        }
        return fields;
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RELot.TABLE_NAME_LOT);

        return fromClauseBuffer.toString();
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isBlankOrZero(getForCsType())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            if (RELotManager.FOR_CS_TYPE_LOT_IN_DECISION_ALL.equals(getForCsType())) {
                whereClause.append(RELot.FIELDNAME_TYPE_LOT);
                whereClause.append(" IN (");
                whereClause.append(IRELot.CS_TYP_LOT_DECISION + ", " + IRELot.CS_TYP_LOT_MENSUEL + ", "
                        + IRELot.CS_TYP_LOT_DEBLOCAGE_RA + ") ");
            } else if (RELotManager.FOR_CS_TYPE_LOT_IN_DECISION_MENSUEL.equals(getForCsType())) {
                whereClause.append(RELot.FIELDNAME_TYPE_LOT);
                whereClause.append(" IN (");
                whereClause.append(IRELot.CS_TYP_LOT_DECISION + ", " + IRELot.CS_TYP_LOT_MENSUEL + ") ");
            } else {
                whereClause.append(RELot.FIELDNAME_TYPE_LOT);
                whereClause.append("=");
                whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForCsType()));

            }
        } else {

        }
        if (!JadeStringUtil.isIntegerEmpty(getForCsEtat())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_ETAT);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsLotOwner())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_LOT_OWNER);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForCsLotOwner()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtatDiffentDe())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_ETAT);
            whereClause.append("<>");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtatDiffentDe()));
        }

        if (!JadeStringUtil.isEmpty(getForCsTypeDiffentDe())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_TYPE_LOT);
            whereClause.append(" <> ");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForCsTypeDiffentDe()));
        }

        if (!JadeStringUtil.isEmpty(getForCsEtatIn())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_ETAT);
            whereClause.append(" IN (");
            whereClause.append(getForCsEtatIn()).append(")");
        }

        if (!JadeStringUtil.isIntegerEmpty(getFromDateCreation())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_DATE_CREATION);
            whereClause.append(">=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), getFromDateCreation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateCreation())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_DATE_CREATION);
            whereClause.append(" = ");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), getFromDateCreation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateEnvoiInMMxAAAA())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RELot.FIELDNAME_DATE_ENVOI);
            whereClause.append(">=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(),
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDateEnvoiInMMxAAAA()) + "01"));

            whereClause.append(" AND ");
            JADate moisSuivant = null;
            try {
                moisSuivant = new JADate(getForDateEnvoiInMMxAAAA());
                JACalendar cal = new JACalendarGregorian();
                moisSuivant = cal.addMonths(moisSuivant, 1);
            } catch (JAException e) {
                ;
            }
            whereClause.append(RELot.FIELDNAME_DATE_ENVOI);
            whereClause.append("<");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), moisSuivant.toStrAMJ()));

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
        return new RELot();
    }

    /**
     * @return the forCsEtat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForCsEtatDiffentDe() {
        return forCsEtatDiffentDe;
    }

    public String getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForCsLotOwner() {
        return forCsLotOwner;
    }

    /**
     * @return
     */
    public String getForCsType() {
        return forCsType;
    }

    public String getForCsTypeDiffentDe() {
        return forCsTypeDiffentDe;
    }

    public String getForDateCreation() {
        return forDateCreation;
    }

    public String getForDateEnvoiInMMxAAAA() {
        return forDateEnvoiInMMxAAAA;
    }

    /**
     * @return the fromDateCreation
     */
    public String getFromDateCreation() {
        return fromDateCreation;
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
        if ((isTriParDateEnvoi() != null) && isTriParDateEnvoi().booleanValue()) {
            return "DATE_ENVOI DESC ";
        } else {
            return RELot.FIELDNAME_DATE_ENVOI + " DESC";
        }
    }

    public Boolean isTriParDateEnvoi() {
        return isTriParDateEnvoi;
    }

    /**
     * @param forCsEtat
     *            the forCsEtat to set
     */
    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForCsEtatDiffentDe(String forCsEtatDiffentDe) {
        this.forCsEtatDiffentDe = forCsEtatDiffentDe;
    }

    public void setForCsEtatIn(String forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForCsLotOwner(String forCsLotOwner) {
        this.forCsLotOwner = forCsLotOwner;
    }

    /**
     * @param string
     */
    public void setForCsType(String string) {
        forCsType = string;
    }

    public void setForCsTypeDiffentDe(String forCsTypeDiffentDe) {
        this.forCsTypeDiffentDe = forCsTypeDiffentDe;
    }

    public void setForDateCreation(String forDateCreation) {
        this.forDateCreation = forDateCreation;
    }

    public void setForDateEnvoiInMMxAAAA(String forDateEnvoiInMMxAAAA) {
        this.forDateEnvoiInMMxAAAA = forDateEnvoiInMMxAAAA;
    }

    /**
     * @param fromDateCreation
     *            the fromDateCreation to set
     */
    public void setFromDateCreation(String fromDateCreation) {
        this.fromDateCreation = fromDateCreation;
    }

    public void setIsTriParDateEnvoi(Boolean isTriParDateEnvoi) {
        this.isTriParDateEnvoi = isTriParDateEnvoi;
    }

}
