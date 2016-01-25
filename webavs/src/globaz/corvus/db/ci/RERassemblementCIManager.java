package globaz.corvus.db.ci;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class RERassemblementCIManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Boolean forCIAdditionnelOnly = Boolean.FALSE;
    private String forCsEtat = "";
    private String forCsEtatDiffentDe = "";
    private String forDateClotureAAAAMM = "";
    private String forDateRassemblement = "";
    private String forIdCI = "";
    private String forIdParent = "";
    private String forIdTiers = "";

    private String forMotif = "";
    private String forMotifBetween = "";
    private Boolean forNoDateRevocation = Boolean.FALSE;
    private Boolean forRassWithoutParent = Boolean.FALSE;

    private String forRefARC = "";
    private String refARCEndWith = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERassemblementCI.TABLE_NAME_RCI);

        // jointure entre table des ci et table des rassemblements de ci
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RECompteIndividuel.TABLE_NAME_CI);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERassemblementCI.TABLE_NAME_RCI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERassemblementCI.FIELDNAME_ID_CI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RECompteIndividuel.TABLE_NAME_CI);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RECompteIndividuel.FIELDNAME_ID_CI);

        return fromClauseBuffer.toString();
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdTiers())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RECompteIndividuel.FIELDNAME_ID_TIERS);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdTiers()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtat())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERassemblementCI.FIELDNAME_ETAT);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtatDiffentDe())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERassemblementCI.FIELDNAME_ETAT);
            whereClause.append(" <> ");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtatDiffentDe()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMotif())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERassemblementCI.FIELDNAME_MOTIF);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForMotif()));
        }

        if (!JadeStringUtil.isEmpty(getForMotifBetween())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERassemblementCI.FIELDNAME_MOTIF);
            whereClause.append(" BETWEEN ");
            whereClause.append(getForMotifBetween());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdCI())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RECompteIndividuel.FIELDNAME_ID_CI);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdCI()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdParent())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERassemblementCI.FIELDNAME_ID_PARENT);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdParent()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateClotureAAAAMM())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERassemblementCI.FIELDNAME_DATE_CLOTURE);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForDateClotureAAAAMM()));
        }

        if (!JadeStringUtil.isEmpty(getForDateRassemblement())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT);
            whereClause.append("=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateRassemblement()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForRefARC())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERassemblementCI.FIELDNAME_REF_UNIQUE_ARC);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForRefARC()));
        }

        if ((getForRassWithoutParent() != null) && getForRassWithoutParent().booleanValue()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERassemblementCI.FIELDNAME_ID_PARENT);
            whereClause.append("= 0");
        }

        if (!JadeStringUtil.isIntegerEmpty(getRefARCEndWith())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            // (YOIREF-ROUND(YOIREF,-6))
            whereClause.append("(");
            whereClause.append(RERassemblementCI.FIELDNAME_REF_UNIQUE_ARC);
            whereClause.append("-ROUND(");
            whereClause.append(RERassemblementCI.FIELDNAME_REF_UNIQUE_ARC);
            whereClause.append(",-" + getRefARCEndWith().length() + "))");
            whereClause.append(" = ");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getRefARCEndWith()));
        }

        if ((forNoDateRevocation != null) && forNoDateRevocation.booleanValue()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("( ").append(RERassemblementCI.FIELDNAME_DATE_REVOCATION);
            whereClause.append(" IS NULL OR ");
            whereClause.append(RERassemblementCI.FIELDNAME_DATE_REVOCATION);
            whereClause.append(" = 0 )");
        }

        if ((forCIAdditionnelOnly != null) && forCIAdditionnelOnly.booleanValue()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(" ").append(RERassemblementCI.FIELDNAME_ID_PARENT);
            whereClause.append(" > 0 ");

        }

        return whereClause.toString();
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
        return new RERassemblementCI();
    }

    public Boolean getForCIAdditionnelOnly() {
        return forCIAdditionnelOnly;
    }

    /**
     * @return the forEtat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForCsEtatDiffentDe() {
        return forCsEtatDiffentDe;
    }

    public String getForDateClotureAAAAMM() {
        return forDateClotureAAAAMM;
    }

    public String getForDateRassemblement() {
        return forDateRassemblement;
    }

    public String getForIdCI() {
        return forIdCI;
    }

    public String getForIdParent() {
        return forIdParent;
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return the forMotif
     */
    public String getForMotif() {
        return forMotif;
    }

    public String getForMotifBetween() {
        return forMotifBetween;
    }

    public Boolean getForNoDateRevocation() {
        return forNoDateRevocation;
    }

    public Boolean getForRassWithoutParent() {
        return forRassWithoutParent;
    }

    public String getForRefARC() {
        return forRefARC;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut order by defaut
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return RERassemblementCI.FIELDNAME_ID_RCI;
    }

    public String getRefARCEndWith() {
        return refARCEndWith;
    }

    public void setForCIAdditionnelOnly(Boolean forCIAdditionnelOnly) {
        this.forCIAdditionnelOnly = forCIAdditionnelOnly;
    }

    /**
     * @param forStatus
     *            the forStatus to set
     */
    public void setForCsEtat(String forEtat) {
        forCsEtat = forEtat;
    }

    public void setForCsEtatDiffentDe(String forCsEtatDiffentDe) {
        this.forCsEtatDiffentDe = forCsEtatDiffentDe;
    }

    public void setForDateClotureAAAAMM(String forDateClotureAAAAMM) {
        this.forDateClotureAAAAMM = forDateClotureAAAAMM;
    }

    public void setForDateRassemblement(String forDateRassemblement) {
        this.forDateRassemblement = forDateRassemblement;
    }

    public void setForIdCI(String forIdCI) {
        this.forIdCI = forIdCI;
    }

    public void setForIdParent(String forIdParent) {
        this.forIdParent = forIdParent;
    }

    /**
     * @param forIdTiers
     *            the forIdTiers to set
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    /**
     * @param forMotif
     *            the forMotif to set
     */
    public void setForMotif(String forMotif) {
        this.forMotif = forMotif;
    }

    public void setForMotifBetween(String forMotifBetween) {
        this.forMotifBetween = forMotifBetween;
    }

    public void setForNoDateRevocation(Boolean forNoDateRevocation) {
        this.forNoDateRevocation = forNoDateRevocation;
    }

    public void setForRassWithoutParent(Boolean forRassWithoutParent) {
        this.forRassWithoutParent = forRassWithoutParent;
    }

    public void setForRefARC(String forRefARC) {
        this.forRefARC = forRefARC;
    }

    public void setRefARCEndWith(String refARCEndWith) {
        this.refARCEndWith = refARCEndWith;
    }

}
