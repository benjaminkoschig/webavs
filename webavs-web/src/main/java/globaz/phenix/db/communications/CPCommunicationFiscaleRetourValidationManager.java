package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CPCommunicationFiscaleRetourValidationManager extends CPCommunicationFiscaleRetourManager implements
        java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String exceptGrpTaxation = "";
    private String forGrpExtraction = "";
    private String forGrpTaxation = "";
    private String forIdValidationCommunication = "";
    private Boolean forImpression = Boolean.FALSE;
    private String forJournal = "";
    private String forIdPassage = "";
    private String forTypeDecision = "";
    private Boolean forValide = Boolean.FALSE;
    private String fromNumAffilieOrConjoint = "";
    private String inGrpTaxation = "";
    private Boolean isDevalidation = Boolean.FALSE;
    private String orderBy = "";
    private String tillNumAffilieOrConjoint = "";
    private Boolean forWantMiseEnGed = Boolean.FALSE;

    @Override
    protected String _getFields(BStatement statement) {
        // pour la table CPVCCOP
        String getFields = super._getFields(statement)
                + ", CPVCCOP.ILGPEV, CPVCCOP.ILGPTA, CPVCCOP.ILIDEP, CPVCCOP.IAIDEC, CPVCCOP.IKIRET, CPVCCOP.ILBVAL, CPVCCOP.ILDCAL";
        // pour la table CPDECIP
        getFields += ", DECCAL.HTITIE IDTDEC, DECCAL.IAANNE ANNEC, DECCAL.EBIPAS EBIPAS, DECCAL.IATGAF TGAFC, DECCAL.IABGED IABGED, DECCAL.EBIPAS EBIPAS, DECCAL.IADDEB DDEBC, DECCAL.IADFIN DFINC, DECCAL.IATTDE TTDEC";
        getFields += ", DECPRO.HTICJT CJTP, DECPRO.IAASSU DIVP, DECCAL.HTICJT CJTC, DECCAL.IAASSU DIVC";
        getFields += ", DECPRO.IATGAF TGAFP, DECPRO.IADDEB DDEBP, DECPRO.IADFIN DFINP, DECPRO.IATTDE TTDEP";
        // pour les tableaux
        getFields += ", DOENCAL.IDREV1 REV1C, DOENCAL.IDRAU1 RAU1C, DOENCAL.IDCOT1 COT1C, DOENCAL.IDCAPI CAPIC, DOENCAL.IDFORT FORTC";
        getFields += ", DOENPRO.IDREV1 REV1P, DOENPRO.IDRAU1 RAU1P, DOENPRO.IDCOT1 COT1P, DOENPRO.IDCAPI CAPIP, DOENPRO.IDFORT FORTP";
        getFields += ", ILITCO";
        return getFields;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = super._getFrom(statement);
        String table1 = "CPVCCOP";
        String table2 = "CPDECIP";
        String alias2 = "DECCAL";
        String table3 = "CPDOENP";
        String alias3 = "DOENCAL";
        String table4 = "CPDECIP";
        String alias4 = "DECPRO";
        String table5 = "CPDOENP";
        String alias5 = "DOENPRO";
        from += " LEFT OUTER JOIN " + _getCollection() + table1 + " " + table1 + " ON (" + table1 + ".IKIRET="
                + "CPCRETP.IKIRET)";
        from += " LEFT OUTER JOIN " + _getCollection() + table2 + " " + alias2 + " ON (" + alias2 + ".IAIDEC="
                + "CPVCCOP.IAIDEC)";
        from += " LEFT OUTER JOIN " + _getCollection() + table3 + " " + alias3 + " ON (" + alias3 + ".IAIDEC="
                + "CPVCCOP.IAIDEC)";
        from += " LEFT OUTER JOIN " + _getCollection() + table4 + " " + alias4 + " ON (" + alias4 + ".IAIDEC="
                + "CPVCCOP.ILIDEP)";
        from += " LEFT OUTER JOIN " + _getCollection() + table5 + " " + alias5 + " ON (" + alias5 + ".IAIDEC="
                + "CPVCCOP.ILIDEP)";
        from += " LEFT OUTER JOIN " + _getCollection() + "CPSECON CPSECON ON (CPSECON.IKIRET=CPCRETP.IKIRET)";

        return from;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (JadeStringUtil.isBlankOrZero(getOrderBy())) {
            return "AFAFFIP.MALNAF";
        } else {
            return getOrderBy();
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // appel du parent
        String sqlWhere = super._getWhere(statement);

        if (!getForImpression().booleanValue()) {
            // On prend que les validés
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ILITCO is not null";
            // traitement du positionnement
            if (!getIsDevalidation().booleanValue()) {
                if (!getForValide().booleanValue()) {
                    if (sqlWhere.length() != 0) {
                        sqlWhere += " AND ";
                    }
                    sqlWhere += "ILBVAL='1'";
                }
            }

            if (getIsDevalidation().booleanValue()) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "ILBVAL='2'";
            }
        }

        // Sélectionne les décisions pour lesquelles ont veut la mise en ged des documents de validation des
        // communications fiscales
        if (Boolean.TRUE.equals(getForWantMiseEnGed())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DECCAL.IABGED = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(true),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // traitement du positionnement
        if (getFromNumAffilieOrConjoint().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(AFAFFIP.MALNAF >="
                    + this._dbWriteString(statement.getTransaction(), getFromNumAffilieOrConjoint())
                    + " OR AFFCJT.MALNAF >="
                    + this._dbWriteString(statement.getTransaction(), getFromNumAffilieOrConjoint()) + ")";
        }
        // traitement du positionnement
        if (getTillNumAffilieOrConjoint().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(AFAFFIP.MALNAF <="
                    + this._dbWriteString(statement.getTransaction(), getTillNumAffilieOrConjoint())
                    + " OR AFFCJT.MALNAF <="
                    + this._dbWriteString(statement.getTransaction(), getTillNumAffilieOrConjoint()) + ")";
        }
        // traitement du positionnement
        if (getForTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DECCAL.IATTDE=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeDecision());
        }
        // traitement du positionnement
        if (getForGrpExtraction().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ILGPEV=" + this._dbWriteNumeric(statement.getTransaction(), getForGrpExtraction());
        }

        // traitement du positionnement
        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DECCAL.EBIPAS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }

        // traitement du positionnement
        if (getExceptGrpTaxation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ILGPEV <>" + this._dbWriteNumeric(statement.getTransaction(), getExceptGrpTaxation());
        }
        if (getInGrpTaxation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ILGPEV in (" + getInGrpTaxation() + ")";
        }
        // traitement du positionnement
        if (getForGrpTaxation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ILGPTA=" + this._dbWriteNumeric(statement.getTransaction(), getForGrpTaxation());
        }
        // traitement du positionnement
        if (getForJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IWRJOU=" + this._dbWriteNumeric(statement.getTransaction(), getForJournal());
        }
        // traitement du positionnement
        if (getForIdValidationCommunication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ILITCO=" + this._dbWriteNumeric(statement.getTransaction(), getForIdValidationCommunication());
        }
        return sqlWhere;
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPCommunicationFiscaleRetourValidation();
    }

    public String getExceptGrpTaxation() {
        return exceptGrpTaxation;
    }

    public String getForGrpExtraction() {
        return forGrpExtraction;
    }

    public String getForGrpTaxation() {
        return forGrpTaxation;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public String getForIdValidationCommunication() {
        return forIdValidationCommunication;
    }

    public Boolean getForImpression() {
        return forImpression;
    }

    public String getForJournal() {
        return forJournal;
    }

    public String getForTypeDecision() {
        return forTypeDecision;
    }

    public Boolean getForValide() {
        return forValide;
    }

    public Boolean getForWantMiseEnGed() {
        return forWantMiseEnGed;
    }

    public String getFromNumAffilieOrConjoint() {
        return fromNumAffilieOrConjoint;
    }

    public String getInGrpTaxation() {
        return inGrpTaxation;
    }

    public Boolean getIsDevalidation() {
        return isDevalidation;
    }

    @Override
    public java.lang.String getOrderBy() {
        return orderBy;
    }

    public String getTillNumAffilieOrConjoint() {
        return tillNumAffilieOrConjoint;
    }

    public void orderByGroupeTaxation() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("ILGPEV ASC, IKLTRI ASC");
        } else {
            setOrderBy(getOrderBy() + ", ILGPEV ASC, IKLTRI ASC");
        }
    }

    @Override
    public void orderByNumAvs() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("CPSECON.SEVNAVS ASC");
        } else {
            setOrderBy(getOrderBy() + ", CPSECON.SEVNAVS ASC");
        }
    }

    public void orderByNumAffilieCjt() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("NAFCJT ASC");
        } else {
            setOrderBy(getOrderBy() + ", NAFCJT ASC");
        }
    }

    public void orderByNumAvsCjt() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("AVSCJT ASC");
        } else {
            setOrderBy(getOrderBy() + ", AVSCJT ASC");
        }
    }

    public void orderByNumContribuableCjt() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("CONCJT ASC");
        } else {
            setOrderBy(getOrderBy() + ", CONCJT ASC");
        }
    }

    public void setExceptGrpTaxation(String exceptGrpTaxation) {
        this.exceptGrpTaxation = exceptGrpTaxation;
    }

    public void setForGrpExtraction(String forGrpExtraction) {
        this.forGrpExtraction = forGrpExtraction;
    }

    public void setForGrpTaxation(String forGrpTaxation) {
        this.forGrpTaxation = forGrpTaxation;
    }

    public void setForIdValidationCommunication(String forIdValidationCommunication) {
        this.forIdValidationCommunication = forIdValidationCommunication;
    }

    public void setForImpression(Boolean forImpression) {
        this.forImpression = forImpression;
    }

    public void setForJournal(String forJournal) {
        this.forJournal = forJournal;
    }

    public void setForTypeDecision(String forTypeDecision) {
        this.forTypeDecision = forTypeDecision;
    }

    public void setForValide(Boolean forValide) {
        this.forValide = forValide;
    }

    public void setFromNumAffilieOrConjoint(String fromNumAffilieOrConjoint) {
        this.fromNumAffilieOrConjoint = fromNumAffilieOrConjoint;
    }

    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    public void setInGrpTaxation(String inGrpTaxation) {
        this.inGrpTaxation = inGrpTaxation;
    }

    public void setIsDevalidation(Boolean isDevalidation) {
        this.isDevalidation = isDevalidation;
    }

    @Override
    public void setOrderBy(java.lang.String string) {
        orderBy = string;
    }

    public void setTillNumAffilieOrConjoint(String tillNumAffilieOrConjoint) {
        this.tillNumAffilieOrConjoint = tillNumAffilieOrConjoint;
    }

    public void setForWantMiseEnGed(Boolean forWantMiseEnGed) {
        this.forWantMiseEnGed = forWantMiseEnGed;
    }
}
