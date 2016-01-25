package globaz.corvus.db.avances;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * @author SCR
 */
public class REAvanceManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsDomaineAvance;
    private String forCsEtat1erAcomptes;
    private String forCsEtat1erAcomptesDifferentDe;
    private String forCsEtat1erAcomptesDifferentDeIn;
    private String forCsEtatAcomptes;
    private String forCsEtatAcomptesDifferentDe;
    private Boolean forDateDebut1erAcompteNotZero;
    private Boolean forDateDebutAcompteMensuelNotZero;
    private String forIdAvance;
    private String forIdTiersIn;
    private String fromDateDebut1erAcompte;
    private String fromDateDebutAcompte;
    private String untilDateDebut1erAcompte;
    private String untilDateDebutAcompte;

    public REAvanceManager() {
        super();

        forCsDomaineAvance = "";
        forCsEtat1erAcomptes = "";
        forCsEtat1erAcomptesDifferentDe = "";
        forCsEtat1erAcomptesDifferentDeIn = "";
        forCsEtatAcomptes = "";
        forCsEtatAcomptesDifferentDe = "";
        forDateDebut1erAcompteNotZero = Boolean.FALSE;
        forDateDebutAcompteMensuelNotZero = Boolean.FALSE;
        forIdAvance = "";
        forIdTiersIn = "";
        fromDateDebut1erAcompte = "";
        fromDateDebutAcompte = "";
        untilDateDebut1erAcompte = "";
        untilDateDebutAcompte = "";
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        if (!JadeStringUtil.isBlank(forIdAvance)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(REAvance.FIELDNAME_ID_AVANCE).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdAvance()));
        }

        if (!JadeStringUtil.isBlank(forIdTiersIn)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append(" IN(").append(forIdTiersIn)
                    .append(")");
        }

        if (!JadeStringUtil.isBlank(forCsEtat1erAcomptesDifferentDeIn)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(REAvance.FIELDNAME_ETAT_1ER_ACOMPTE);
            sql.append(" NOT IN(").append(forCsEtat1erAcomptesDifferentDeIn).append(")");
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtat1erAcomptes())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REAvance.FIELDNAME_ETAT_1ER_ACOMPTE).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtat1erAcomptes()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtat1erAcomptesDifferentDe())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REAvance.FIELDNAME_ETAT_1ER_ACOMPTE).append("<>")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtat1erAcomptesDifferentDe()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtatAcomptes())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REAvance.FIELDNAME_ETAT_ACOMPTES).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtatAcomptes()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtatAcomptesDifferentDe())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append(REAvance.FIELDNAME_ETAT_ACOMPTES).append("<>")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtatAcomptesDifferentDe()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getFromDateDebutAcompte())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            String date = "";
            try {
                date = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(getFromDateDebutAcompte());
                if (JadeStringUtil.isBlankOrZero(date)) {
                    date = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getFromDateDebutAcompte());
                }
            } catch (JAException e) {
                date = "29991231";
            }

            sql.append(REAvance.FIELDNAME_DATE_DEB_ACOMPTE).append(">=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), date));
        }

        if (!JadeStringUtil.isIntegerEmpty(getUntilDateDebutAcompte())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            String date = "";
            try {
                date = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(getUntilDateDebutAcompte());
                if (JadeStringUtil.isBlankOrZero(date)) {
                    date = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getUntilDateDebutAcompte());
                }
            } catch (JAException e) {
                date = "19000101";
            }

            // BZ 4946
            sql.append("(");
            sql.append(REAvance.FIELDNAME_DATE_DEB_ACOMPTE).append("<=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), date));
            sql.append(" AND ");
            sql.append(REAvance.FIELDNAME_DATE_DEB_ACOMPTE).append(">0");
            sql.append(")");
        }

        if (getForDateDebut1erAcompteNotZero().booleanValue()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REAvance.FIELDNAME_DATE_DEB_PMT_1ER_ACOMPTE).append(">0");
        }

        if (getForDateDebutAcompteMensuelNotZero().booleanValue()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REAvance.FIELDNAME_DATE_DEB_ACOMPTE).append(">0");
        }

        if (!JadeStringUtil.isIntegerEmpty(getFromDateDebut1erAcompte())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            String date = "";
            try {
                date = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(getFromDateDebut1erAcompte());
                if (JadeStringUtil.isBlankOrZero(date)) {
                    date = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getFromDateDebut1erAcompte());
                }
            } catch (JAException e) {
                date = "29991231";
            }

            sql.append(REAvance.FIELDNAME_DATE_DEB_PMT_1ER_ACOMPTE).append(">=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), date));
        }

        // ajout forCsDomaineAvance, INFOROM547
        if (!JadeStringUtil.isIntegerEmpty(getForCsDomaineAvance())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REAvance.FIELDNAME_DOMAINE_AVANCE).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsDomaineAvance()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getUntilDateDebut1erAcompte())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            String date = "";
            try {
                date = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(getUntilDateDebut1erAcompte());
                if (JadeStringUtil.isBlankOrZero(date)) {
                    date = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getUntilDateDebut1erAcompte());
                }
            } catch (JAException e) {
                date = "19000101";
            }

            // BZ 4946
            sql.append("(");
            sql.append(REAvance.FIELDNAME_DATE_DEB_PMT_1ER_ACOMPTE).append("<=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), date));
            sql.append(" AND ");
            sql.append(REAvance.FIELDNAME_DATE_DEB_PMT_1ER_ACOMPTE).append(">0");
            sql.append(" )");
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAvance();
    }

    public String getForCsDomaineAvance() {
        return forCsDomaineAvance;
    }

    public String getForCsEtat1erAcomptes() {
        return forCsEtat1erAcomptes;
    }

    public String getForCsEtat1erAcomptesDifferentDe() {
        return forCsEtat1erAcomptesDifferentDe;
    }

    public String getForCsEtat1erAcomptesDifferentDeIn() {
        return forCsEtat1erAcomptesDifferentDeIn;
    }

    public String getForCsEtatAcomptes() {
        return forCsEtatAcomptes;
    }

    public String getForCsEtatAcomptesDifferentDe() {
        return forCsEtatAcomptesDifferentDe;
    }

    public Boolean getForDateDebut1erAcompteNotZero() {
        return forDateDebut1erAcompteNotZero;
    }

    public Boolean getForDateDebutAcompteMensuelNotZero() {
        return forDateDebutAcompteMensuelNotZero;
    }

    public String getForIdAvance() {
        return forIdAvance;
    }

    public String getForIdTiersIn() {
        return forIdTiersIn;
    }

    public String getFromDateDebut1erAcompte() {
        return fromDateDebut1erAcompte;
    }

    public String getFromDateDebutAcompte() {
        return fromDateDebutAcompte;
    }

    @Override
    public String getOrderByDefaut() {
        return REAvance.FIELDNAME_DATE_DEB_ACOMPTE + " DESC";
    }

    public String getUntilDateDebut1erAcompte() {
        return untilDateDebut1erAcompte;
    }

    public String getUntilDateDebutAcompte() {
        return untilDateDebutAcompte;
    }

    public void setForCsDomaineAvance(String forCsDomaineAvance) {
        this.forCsDomaineAvance = forCsDomaineAvance;
    }

    public void setForCsEtat1erAcomptes(String forCsEtat1erAcomptes) {
        this.forCsEtat1erAcomptes = forCsEtat1erAcomptes;
    }

    public void setForCsEtat1erAcomptesDifferentDe(String forCsEtat1erAcomptesDifferentDe) {
        this.forCsEtat1erAcomptesDifferentDe = forCsEtat1erAcomptesDifferentDe;
    }

    public void setForCsEtat1erAcomptesDifferentDeIn(String forCsEtat1erAcomptesDifferentDeIn) {
        this.forCsEtat1erAcomptesDifferentDeIn = forCsEtat1erAcomptesDifferentDeIn;
    }

    public void setForCsEtatAcomptes(String forCsEtatAcomptes) {
        this.forCsEtatAcomptes = forCsEtatAcomptes;
    }

    public void setForCsEtatAcomptesDifferentDe(String forCsEtatAcomptesDifferentDe) {
        this.forCsEtatAcomptesDifferentDe = forCsEtatAcomptesDifferentDe;
    }

    public void setForDateDebut1erAcompteNotZero(Boolean forDateDebut1erAcompteNotZero) {
        this.forDateDebut1erAcompteNotZero = forDateDebut1erAcompteNotZero;
    }

    public void setForDateDebutAcompteMensuelNotZero(Boolean forDateDebutAcompteMensuelNotZero) {
        this.forDateDebutAcompteMensuelNotZero = forDateDebutAcompteMensuelNotZero;
    }

    public void setForIdAvance(String forIdAvance) {
        this.forIdAvance = forIdAvance;
    }

    public void setForIdTiersIn(String forIdTiersIn) {
        this.forIdTiersIn = forIdTiersIn;
    }

    public void setFromDateDebut1erAcompte(String fromDateDebut1erAcompte) {
        this.fromDateDebut1erAcompte = fromDateDebut1erAcompte;
    }

    public void setFromDateDebutAcompte(String fromDateDebutAcompte) {
        this.fromDateDebutAcompte = fromDateDebutAcompte;
    }

    public void setUntilDateDebut1erAcompte(String untilDateDebut1erAcompte) {
        this.untilDateDebut1erAcompte = untilDateDebut1erAcompte;
    }

    public void setUntilDateDebutAcompte(String untilDateDebutAcompte) {
        this.untilDateDebutAcompte = untilDateDebutAcompte;
    }
}
