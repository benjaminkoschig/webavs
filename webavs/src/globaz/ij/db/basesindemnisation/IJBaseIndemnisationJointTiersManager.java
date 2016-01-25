package globaz.ij.db.basesindemnisation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRAbstractManagerHierarchique;
import globaz.pyxis.db.tiers.ITITiersDefTable;

public class IJBaseIndemnisationJointTiersManager extends PRAbstractManagerHierarchique {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = "";
    private String[] forCsEtats = null;
    private String forDateDebutPeriode = "";
    private String forDateFinPeriode = "";
    private String forIdBaseIndemnisation;
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

    public IJBaseIndemnisationJointTiersManager() {
        forIdBaseIndemnisation = "";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String tableBaseIndemnisation = _getCollection() + IJBaseIndemnisation.TABLE_NAME;
        String tablePrononce = _getCollection() + IJPrononce.TABLE_NAME_PRONONCE;
        String tableDemande = _getCollection() + PRDemande.TABLE_NAME;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();
        sql.append(tablePrononce);

        sql.append(" INNER JOIN ");
        sql.append(tableBaseIndemnisation);
        sql.append(" ON ");
        sql.append(tableBaseIndemnisation).append(".").append(IJBaseIndemnisation.FIELDNAME_IDPRONONCE);
        sql.append("=");
        sql.append(tablePrononce).append(".").append(IJPrononce.FIELDNAME_ID_PRONONCE);

        sql.append(" INNER JOIN ");
        sql.append(tableDemande);
        sql.append(" ON ");
        sql.append(tablePrononce).append(".").append(IJPrononce.FIELDNAME_ID_DEMANDE);
        sql.append("=");
        sql.append(tableDemande).append(".").append(PRDemande.FIELDNAME_IDDEMANDE);

        sql.append(" INNER JOIN ");
        sql.append(tableTiers);
        sql.append(" ON ");
        sql.append(tableDemande).append(".").append(PRDemande.FIELDNAME_IDTIERS);
        sql.append("=");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);
        // sql.append(super._getFrom(statement));
        return sql.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        // TODO Auto-generated method stub
        return "";
    }

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
            whereClause.append(this._dbWriteDateAMJ(transaction, fromDateDebut));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdPrononce)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJPrononce.FIELDNAME_ID_PRONONCE);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(transaction, forIdPrononce));
        }

        if (!JadeStringUtil.isIntegerEmpty(notForIdBaseIndemnisation)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);
            whereClause.append("<>");
            whereClause.append(this._dbWriteNumeric(transaction, notForIdBaseIndemnisation));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdParent)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_IDPARENT);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(transaction, forIdParent));
        }

        if (!JadeStringUtil.isIntegerEmpty(notForIdParent)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_IDPARENT);
            whereClause.append("<>");
            whereClause.append(this._dbWriteNumeric(transaction, notForIdParent));
        }

        if (parentOnly != null) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_IDPARENT);
            whereClause.append(parentOnly.booleanValue() ? "=" : "<>");
            whereClause.append(this._dbWriteNumeric(transaction, "0"));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdCorrection)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_ID_CORRECTION);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(transaction, forIdCorrection));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_ETAT);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(transaction, forCsEtat));
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
                whereClause.append(this._dbWriteNumeric(transaction, forCsEtats[idEtat]));
            }

            whereClause.append(")");
        }

        if (!JadeStringUtil.isIntegerEmpty(notForCsEtat)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_ETAT);
            whereClause.append("<>");
            whereClause.append(this._dbWriteNumeric(transaction, notForCsEtat));
        }

        if (!JAUtil.isDateEmpty(forDateDebutPeriode)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE);
            whereClause.append(">=");
            whereClause.append(this._dbWriteDateAMJ(transaction, forDateDebutPeriode));
            whereClause.append(" AND ");
            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE);
            whereClause.append("<=");
            whereClause.append(this._dbWriteDateAMJ(transaction, forDateFinPeriode));
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
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));
            whereClause.append(" AND ");
            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE);
            whereClause.append(">=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));
            whereClause.append(")");

            whereClause.append(" OR ");
            // date de fin dans periode
            whereClause.append("(");
            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE);
            whereClause.append("<=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), periodeDateFin));
            whereClause.append(" AND ");
            whereClause.append(IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE);
            whereClause.append(">=");
            whereClause.append(this._dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));
            whereClause.append(")");

            whereClause.append(")");
        }
        if (!JadeStringUtil.isBlankOrZero(forIdBaseIndemnisation)) {
            String tableBaseIndemnisation = _getCollection() + IJBaseIndemnisation.TABLE_NAME;
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(tableBaseIndemnisation).append(".")
                    .append(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);
            whereClause.append("=");
            whereClause.append(forIdBaseIndemnisation);
        }

        return whereClause.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJBaseIndemnisationJointTiers();
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String[] getForCsEtats() {
        return forCsEtats;
    }

    public String getForDateDebutPeriode() {
        return forDateDebutPeriode;
    }

    public String getForDateFinPeriode() {
        return forDateFinPeriode;
    }

    public String getForIdCorrection() {
        return forIdCorrection;
    }

    public String getForIdParent() {
        return forIdParent;
    }

    public String getForIdPrononce() {
        return forIdPrononce;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    @Override
    public String getHierarchicalOrderBy() {

        return getOrderByDefaut();
    }

    public String getNotForCsEtat() {
        return notForCsEtat;
    }

    public String getNotForIdBaseIndemnisation() {
        return notForIdBaseIndemnisation;
    }

    public String getNotForIdParent() {
        return notForIdParent;
    }

    @Override
    public String getOrderByDefaut() {
        return IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION;
    }

    public String getParentOnly() {
        return (parentOnly != null) ? parentOnly.toString() : "";
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForCsEtats(String[] forCsEtats) {
        this.forCsEtats = forCsEtats;
    }

    public final void setForIdBaseIndemnisation(String forIdBaseIndemnisation) {
        this.forIdBaseIndemnisation = forIdBaseIndemnisation;
    }

    public void setForIdCorrection(String forIdCorrection) {
        this.forIdCorrection = forIdCorrection;
    }

    public void setForIdParent(String forIdParent) {
        this.forIdParent = forIdParent;
    }

    public void setForIdPrononce(String string) {
        forIdPrononce = string;
    }

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
