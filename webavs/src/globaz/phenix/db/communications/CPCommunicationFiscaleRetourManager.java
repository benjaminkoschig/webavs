package globaz.phenix.db.communications;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CPCommunicationFiscaleRetourManager extends globaz.globall.db.BManager implements java.io.Serializable {

    private static final long serialVersionUID = -5471919665794916292L;
    private boolean exceptEnEnquete = false;
    private String exceptIdRetour = "";
    private String exceptStatus = "";
    private String forAnnee = "";

    private String forGenreAffilie = "";

    private String forGenreTaxation = "";

    private String forIdAffiliation = "";
    private String forIdAffiliationConjoint = "";

    private String forIdCommunication = "";

    private String forIdIfd = "";

    private String forIdJournalRetour = "";

    private String forIdPlausibilite = "";

    private String forIdRetour = "";

    private String forIdTiers = "";
    private String forLtIdRetour = "";
    private String forNumAffilie = "";
    private String forNumAvs = "";
    private String forNumContibuable = "";
    private String forStatus = "";
    private String fromNumAffilie = "";
    private String fromNumContibuable = "";
    private String idJournalGreaterThan = "";
    private String idJournalLessThan = "";
    private String inStatus = "";
    private String likeNumAffilie = "";
    private String likeNumContribuable = "";
    private String notInStatus = "";
    private String orderBy = "";
    private String tillNumAffilie = "";
    private boolean whitAffiliation = false;
    private boolean whitAffiliationConjoint = false;
    private boolean whitJournal = false;
    private boolean whitPavsAffilie = false;
    private boolean whitPavsConjoint = false;
    private boolean whitPersAffilie = false;
    private boolean whitPersConjoint = false;

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */

    @Override
    protected String _getFields(BStatement statement) {
        String fields = "CPCRETP.IKIRET, CPCRETP.HTITIE, IKICON, IKIACJ, IKILOG, CPCRETP.ICIIFD, IKTGAF, CPCRETP.IBIDCF, IWRJOU,";
        fields += " CPCRETP.MAIAFF, IKDRET, IKANN1, IKREV1, IKDDE1, IKDFI1, IKREV2, IKDDE2, IKDFI2, IKCOT1, IKCOT2,";
        fields += " IKLTRI, IKCAPI, IKFORT, IKDFOR, IKTGTA, IKBGEN, IKTSTA, CPCRETP.IXIDPA, CPCRETP.PSPY, IKAREV, IKICON, IKIACJ, IKTCHC, IKTGCJ, IKRETY";
        if (isWhitPavsAffilie()) {
            fields += ", PAVSAF.HXNAVS HXNAVS, PAVSAF.HXNCON HXNCON";
        }
        if (isWhitPersAffilie()) {
            fields += ", PERSAF.HPTSEX HPTSEX, PERSAF.HPTETC HPTETC, PERSAF.HPDNAI HPDNAI";
        }
        if (isWhitAffiliation()) {
            fields += ", AFAFFIP.MALNAF, AFAFFIP.MADDEB, AFAFFIP.MADFIN";
        }
        if (isWhitPavsConjoint()) {
            fields += ", PAVSCJ.HXNAVS AVSCJT, PAVSCJ.HXNCON CONCJT";
        }
        if (isWhitPersConjoint()) {
            fields += ", PERSCJ.HPTSEX SEXCJT, PERSCJ.HPTETC ETCCJT, PERSCJ.HPDNAI NAICJT";
        }
        if (isWhitAffiliationConjoint()) {
            fields += ", AFFCJT.MALNAF NAFCJT, AFFCJT.MADDEB DEBCJT, AFFCJT.MADFIN FINCJT";
        }
        if (isWhitJournal()) {
            fields += ", CPJOURP.IWACAN";
        }
        if (!JadeStringUtil.isEmpty(getForIdPlausibilite())) {
            fields += ", CPLCRPP.IXIDPA";
        }
        return fields;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CPCRETP";
        String alias1 = "CPCRETP";
        String table2 = "TIPAVSP";
        String alias2 = "PAVSAF";
        String table3 = "TIPERSP";
        String alias3 = "PERSAF";
        String table4 = "AFAFFIP";
        String alias4 = "AFAFFIP";
        String table5 = "TIPAVSP";
        String alias5 = "PAVSCJ";
        String table6 = "TIPERSP";
        String alias6 = "PERSCJ";
        String table7 = "AFAFFIP";
        String alias7 = "AFFCJT";
        String table8 = "CPJOURP";
        String alias8 = "CPJOURP";
        String table9 = "CPLCRPP";
        String alias9 = "CPLCRPP";
        String from = _getCollection() + table1 + " " + alias1;
        if (isWhitPavsAffilie()) {
            from += " LEFT OUTER JOIN " + _getCollection() + table2 + " " + alias2 + " ON (" + alias2 + ".HTITIE="
                    + alias1 + ".HTITIE)";
        }
        if (isWhitPersAffilie()) {
            from += " LEFT OUTER JOIN " + _getCollection() + table3 + " " + alias3 + " ON (" + alias3 + ".HTITIE="
                    + alias1 + ".HTITIE)";
        }
        if (isWhitAffiliation()) {
            from += " LEFT OUTER JOIN " + _getCollection() + table4 + " " + alias4 + " ON (" + alias4 + ".MAIAFF="
                    + alias1 + ".MAIAFF)";
        }
        if (isWhitPavsConjoint()) {
            from += " LEFT OUTER JOIN " + _getCollection() + table5 + " " + alias5 + " ON (" + alias5 + ".HTITIE="
                    + alias1 + ".IKICON)";
        }
        if (isWhitPersConjoint()) {
            from += " LEFT OUTER JOIN " + _getCollection() + table6 + " " + alias6 + " ON (" + alias6 + ".HTITIE="
                    + alias1 + ".IKICON)";
        }
        if (isWhitAffiliationConjoint()) {
            from += " LEFT OUTER JOIN " + _getCollection() + table7 + " " + alias7 + " ON (" + alias7 + ".MAIAFF="
                    + alias1 + ".IKIACJ)";
        }
        if (isWhitJournal()) {
            from += " LEFT OUTER JOIN " + _getCollection() + table8 + " " + alias8 + " ON (" + alias8 + ".IWRJOU="
                    + alias1 + ".IWRJOU)";
        }
        if (!JadeStringUtil.isEmpty(getForIdPlausibilite())) {
            from += " LEFT OUTER JOIN " + _getCollection() + table9 + " " + alias9 + " ON (" + alias9 + ".IBIDCF="
                    + alias1 + ".IKIRET)";
        }

        return from;

    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return getOrderBy();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForNumAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HXNAVS=" + this._dbWriteString(statement.getTransaction(), getForNumAvs());
        }
        // traitement du positionnement
        if (getForIdPlausibilite().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPLCRPP.IXIDPA=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPlausibilite());
        }
        // traitement du positionnement
        if (getForIdCommunication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IKRET=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCommunication());
        }
        // traitement du positionnement
        if (getForIdIfd().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICIIFD=" + this._dbWriteNumeric(statement.getTransaction(), getForIdIfd());
        }
        // traitement du positionnement
        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }
        // traitement du positionnement
        if (getForIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }
        if (getForIdAffiliationConjoint().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IKIACJ="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliationConjoint());
        }
        // traitement du positionnement
        if (getForNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (isWhitAffiliationConjoint()) {
                sqlWhere += "(AFAFFIP.MALNAF=" + this._dbWriteString(statement.getTransaction(), getForNumAffilie())
                        + " OR AFFCJT.MALNAF=" + this._dbWriteString(statement.getTransaction(), getForNumAffilie())
                        + ")";
            } else {
                sqlWhere += "AFAFFIP.MALNAF=" + this._dbWriteString(statement.getTransaction(), getForNumAffilie());
            }
        }

        // traitement du positionnement
        if (getFromNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFAFFIP.MALNAF >=" + this._dbWriteString(statement.getTransaction(), getFromNumAffilie());
        }
        // traitement du positionnement
        if (getLikeNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (isWhitAffiliationConjoint()) {
                sqlWhere += "(AFAFFIP.MALNAF like "
                        + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumAffilie() + "%")
                        + " or AFFCJT.MALNAF like "
                        + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumAffilie() + "%") + ")";
            } else {
                sqlWhere += "AFAFFIP.MALNAF like "
                        + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumAffilie() + "%");
            }
        }
        // traitement du positionnement
        if (getForNumContibuable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HXNCON=" + this._dbWriteString(statement.getTransaction(), getForNumContibuable());
        }
        if (getLikeNumContribuable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HXNCON like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumContribuable() + "%");
        }
        // traitement du positionnement
        if (getFromNumContibuable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HXNCON >=" + this._dbWriteString(statement.getTransaction(), getFromNumContibuable());
        }
        // traitement du positionnement
        if (getTillNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFAFFIP.MALNAF <=" + this._dbWriteString(statement.getTransaction(), getTillNumAffilie());
        }
        // traitement du positionnement
        if (getForGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(IKTGAF=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAffilie())
                    + " OR IKTGCJ=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAffilie()) + ")";
        }
        // traitement du positionnement
        if (getForGenreTaxation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKTGTA=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreTaxation());
        }
        // traitement du positionnement
        if (getForIdJournalRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IWRJOU=" + this._dbWriteNumeric(statement.getTransaction(), getForIdJournalRetour());
        }
        // Except l'id retour
        if (getExceptIdRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IKIRET<>" + this._dbWriteNumeric(statement.getTransaction(), getExceptIdRetour());
        }
        // Inférieur l'id retour
        if (getForLtIdRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IKIRET <" + this._dbWriteNumeric(statement.getTransaction(), getForLtIdRetour());
        }
        // traitement du positionnement
        if (getForStatus().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKTSTA IN (" + getForStatus() + ")";
        }
        if (getForStatus().length() == 0) {
            // traitement du positionnement
            if (getExceptStatus().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "IKTSTA<>" + this._dbWriteNumeric(statement.getTransaction(), getExceptStatus());
            }
            // traitement du positionnement
            if (getInStatus().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "IKTSTA in (" + getInStatus() + ")";
            }
        }
        // traitement du positionnement
        if (getNotInStatus().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKTSTA not in (" + getNotInStatus() + ")";
        }
        // traitement du positionnement
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKANN1=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        // traitement du positionnement
        if (getForIdRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IKIRET=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRetour());
        }
        // traitement du positionnement
        if (getIdJournalGreaterThan().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IWRJOU > "
                    + this._dbWriteNumeric(statement.getTransaction(), getIdJournalGreaterThan());
        }
        if (getIdJournalLessThan().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CPCRETP.IWRJOU < " + this._dbWriteNumeric(statement.getTransaction(), getIdJournalLessThan());
        }
        if (isExceptEnEnquete()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKTSTA <> " + CPCommunicationFiscaleRetourViewBean.CS_ENQUETE;
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPCommunicationFiscaleRetourViewBean();
    }

    /**
     * @return
     */
    public String getExceptIdRetour() {
        return exceptIdRetour;
    }

    /**
     * @return
     */
    public String getExceptStatus() {
        return exceptStatus;
    }

    /**
     * @return
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * Getter
     */
    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * Getter
     */
    public java.lang.String getForGenreTaxation() {
        return forGenreTaxation;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public String getForIdAffiliationConjoint() {
        return forIdAffiliationConjoint;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdCommunication() {
        return forIdCommunication;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdIfd() {
        return forIdIfd;
    }

    /**
     * @return
     */
    public java.lang.String getForIdJournalRetour() {
        return forIdJournalRetour;
    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    public String getForIdRetour() {
        return forIdRetour;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    public String getForLtIdRetour() {
        return forLtIdRetour;
    }

    /**
     * @return
     */
    public java.lang.String getForNumAffilie() {
        return forNumAffilie;
    }

    public String getForNumAvs() {
        return forNumAvs;
    }

    /**
     * @return
     */
    public java.lang.String getForNumContibuable() {
        return forNumContibuable;
    }

    /**
     * @return
     */
    public String getForStatus() {
        return forStatus;
    }

    /**
     * @return
     */
    public java.lang.String getFromNumAffilie() {
        return fromNumAffilie;
    }

    /**
     * @return
     */
    public java.lang.String getFromNumContibuable() {
        return fromNumContibuable;
    }

    public String getIdJournalGreaterThan() {
        return idJournalGreaterThan;
    }

    public String getIdJournalLessThan() {
        return idJournalLessThan;
    }

    public String getInStatus() {
        return inStatus;
    }

    /**
     * @return
     */
    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    /**
     * @return
     */
    public String getLikeNumContribuable() {
        return likeNumContribuable;
    }

    public String getNotInStatus() {
        return notInStatus;
    }

    /**
     * @return
     */
    public java.lang.String getOrderBy() {
        return orderBy;
    }

    /**
     * @return
     */
    public java.lang.String getTillNumAffilie() {
        return tillNumAffilie;
    }

    public boolean isExceptEnEnquete() {
        return exceptEnEnquete;
    }

    public boolean isWhitAffiliation() {
        return whitAffiliation;
    }

    public boolean isWhitAffiliationConjoint() {
        return whitAffiliationConjoint;
    }

    public boolean isWhitJournal() {
        return whitJournal;
    }

    public boolean isWhitPavsAffilie() {
        return whitPavsAffilie;
    }

    public boolean isWhitPavsConjoint() {
        return whitPavsConjoint;
    }

    public boolean isWhitPersAffilie() {
        return whitPersAffilie;
    }

    public boolean isWhitPersConjoint() {
        return whitPersConjoint;
    }

    public void orderByErreur() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("CPCRETP.IXIDPA DESC");
        } else {
            setOrderBy(getOrderBy() + ", CPCRETP.IXIDPA DESC");
        }
    }

    /**
     * Tri par id communication Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdCommunicationRetour() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("CPCRETP.IKIRET ASC");
        } else {
            setOrderBy(getOrderBy() + ", CPCRETP.IKIRET ASC");
        }
    }

    public void orderByJournal() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("IWRJOU DESC");
        } else {
            setOrderBy(getOrderBy() + ", IWRJOU DESC");
        }
    }

    /**
     * Tri par n° d'affilié Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNumAffilie() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("AFAFFIP.MALNAF ASC");
        } else {
            setOrderBy(getOrderBy() + ", AFAFFIP.MALNAF ASC");
        }
    }

    /**
     * Tri par n° d'affilié Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNumAvs() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("HXNAVS ASC");
        } else {
            setOrderBy(getOrderBy() + ", HXNAVS ASC");
        }
    }

    /**
     * Tri par n° de contribuable Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNumContribuable() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("HXNCON ASC");
        } else {
            setOrderBy(getOrderBy() + ", HXNCON ASC");
        }
    }

    /**
     * Tri par annee Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNumIFD() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("ICNIFD ASC");
        } else {
            setOrderBy(getOrderBy() + ", ICNIFD ASC");
        }
    }

    public void orderByTaxation() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy("CPVCCOP.ILGPTA ASC, IKLTRI");
        } else {
            setOrderBy(getOrderBy() + ", CPVCCOP.ILGPTA ASC, IKLTRI");
        }
    }

    public void setExceptEnEnquete(boolean exceptEnEnquete) {
        this.exceptEnEnquete = exceptEnEnquete;
    }

    /**
     * @param string
     */
    public void setExceptIdRetour(String string) {
        exceptIdRetour = string;
    }

    /**
     * @param string
     */
    public void setExceptStatus(String string) {
        exceptStatus = string;
    }

    /**
     * @param string
     */
    public void setForAnnee(String string) {
        forAnnee = string;
    }

    /**
     * Setter
     */
    public void setForGenreAffilie(java.lang.String newForGenreAffilie) {
        forGenreAffilie = newForGenreAffilie;
    }

    /**
     * Setter
     */
    public void setForGenreTaxation(java.lang.String newForGenreTaxation) {
        forGenreTaxation = newForGenreTaxation;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public void setForIdAffiliationConjoint(String forIdAffiliationConjoint) {
        this.forIdAffiliationConjoint = forIdAffiliationConjoint;
    }

    /**
     * Setter
     */
    public void setForIdCommunication(java.lang.String newForIdCommunication) {
        forIdCommunication = newForIdCommunication;
    }

    /**
     * @param string
     */
    public void setForIdIfd(java.lang.String string) {
        forIdIfd = string;
    }

    /**
     * @param string
     */
    public void setForIdJournalRetour(java.lang.String string) {
        forIdJournalRetour = string;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

    public void setForIdRetour(String forIdRetour) {
        this.forIdRetour = forIdRetour;
    }

    /**
     * Setter
     */
    public void setForIdTiers(java.lang.String newForIdTiers) {
        forIdTiers = newForIdTiers;
    }

    public void setForLtIdRetour(String forLtIdRetour) {
        this.forLtIdRetour = forLtIdRetour;
    }

    /**
     * @param string
     */
    public void setForNumAffilie(java.lang.String string) {
        forNumAffilie = string;
    }

    public void setForNumAvs(String forNumAvs) {
        this.forNumAvs = forNumAvs;
    }

    /**
     * @param string
     */
    public void setForNumContibuable(java.lang.String string) {
        forNumContibuable = string;
    }

    /**
     * @param string
     */
    public void setForStatus(String string) {
        forStatus = string;
    }

    /**
     * @param string
     */
    public void setFromNumAffilie(java.lang.String string) {
        fromNumAffilie = string;
    }

    /**
     * @param string
     */
    public void setFromNumContibuable(java.lang.String string) {
        fromNumContibuable = string;
    }

    public void setIdJournalGreaterThan(String idJournalGreaterThan) {
        this.idJournalGreaterThan = idJournalGreaterThan;
    }

    public void setIdJournalLessThan(String idJournalLessThan) {
        this.idJournalLessThan = idJournalLessThan;
    }

    public void setInStatus(String inStatus) {
        this.inStatus = inStatus;
    }

    /**
     * @param string
     */
    public void setLikeNumAffilie(String string) {
        likeNumAffilie = string;
    }

    /**
     * @param string
     */
    public void setLikeNumContribuable(String string) {
        likeNumContribuable = string;
    }

    public void setNotInStatus(String notInStatus) {
        this.notInStatus = notInStatus;
    }

    /**
     * @param string
     */
    public void setOrderBy(java.lang.String string) {
        orderBy = string;
    }

    /**
     * @param string
     */
    public void setTillNumAffilie(java.lang.String string) {
        tillNumAffilie = string;
    }

    public void setWhitAffiliation(boolean whitAffiliation) {
        this.whitAffiliation = whitAffiliation;
    }

    public void setWhitAffiliationConjoint(boolean whitAffiliationConjoint) {
        this.whitAffiliationConjoint = whitAffiliationConjoint;
    }

    public void setWhitJournal(boolean whitJournal) {
        this.whitJournal = whitJournal;
    }

    public void setWhitPavsAffilie(boolean whitPavsAffilie) {
        this.whitPavsAffilie = whitPavsAffilie;
    }

    public void setWhitPavsConjoint(boolean whitPavsConjoint) {
        this.whitPavsConjoint = whitPavsConjoint;
    }

    public void setWhitPersAffilie(boolean whitPersAffilie) {
        this.whitPersAffilie = whitPersAffilie;
    }

    public void setWhitPersConjoint(boolean whitPersConjoint) {
        this.whitPersConjoint = whitPersConjoint;
    }
}
