package globaz.phenix.db.principale;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CPDecisionAffiliationTiersManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String beforeAnneeDecision = "";
    private Boolean exceptTypeDecisionBlank = new Boolean(false);
    private java.lang.String forAnneeDecision = "";
    private java.lang.String forEtat = "";
    private java.lang.String forGenreAffilie = "";
    private java.lang.String forIdAffiliation = "";
    private boolean forIdAffilieDouble = false;
    private java.lang.String forIdDecision = "";
    private java.lang.String forIdPassage = "";
    private java.lang.String forIdTiers = "";
    private java.lang.String forInTypeDecision = "";
    // Facturation
    private boolean forIsFacturation = false;
    private boolean forIsNotFacturation = false;
    private java.lang.String forNeTypeDecision = "";
    private java.lang.String forNotEtat = "";
    private java.lang.String forNotInIdPassage = "";
    private java.lang.String forNotInTypeDecision = "";
    private java.lang.String forOrder = "";
    private java.lang.String forTypeDecision = "";
    private java.lang.String fromAffilie = "";
    private java.lang.String fromDateFacturation = "";
    private java.lang.String fromEtat = "";

    private java.lang.String fromHeureEtat = "";

    private java.lang.String fromIdDecision = "";

    private java.lang.String fromLtEtat = "";

    private Boolean isActive = Boolean.FALSE;
    private java.lang.String order = "";
    private Boolean selectMaxDateInformation = new Boolean(true);
    private boolean statistiques = false;
    private java.lang.String toDateFacturation = "";
    private java.lang.String toNumAffilie = "";
    private Boolean wantIndependant = Boolean.FALSE;

    private Boolean wantNonActif = Boolean.FALSE;

    /**
     * retourne la clause SELECT de la requete SQL (la table)
     */
    @Override
    protected String _getFields(BStatement statement) {
        String sqlSelect = "";
        if (isStatistiques()) {
            sqlSelect += "COUNT (*) AS NOMBRE, IATTDE, IARESP ";
        }

        else {
            sqlSelect += "* ";
        }
        return sqlSelect;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CPDECIP";
        String table2 = "TIPAVSP";
        String table3 = "TITIERP";
        String table4 = "AFAFFIP";
        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + ".HTITIE=" + _getCollection() + table2 + ".HTITIE)" + " INNER JOIN " + _getCollection()
                + table3 + " ON (" + _getCollection() + table1 + ".HTITIE=" + _getCollection() + table3 + ".HTITIE)"
                + " INNER JOIN " + _getCollection() + table4 + " AFAFFIP" + " ON ("
                // + _getCollection()
                + table4 + ".MAIAFF=" + _getCollection() + table1 + ".MAIAFF)";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getSelectMaxDateInformation().equals(new Boolean(true))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "cpdecip.iadinf=(select max(" + _getCollection() + "cpdecip.iadinf) from "
                    + _getCollection() + "cpdecip where " + _getCollection() + "cpdecip.htitie =" + _getCollection()
                    + "tipavsp.htitie) ";
            if (getForAnneeDecision().length() != 0) {
                sqlWhere += " AND  " + _getCollection() + "cpdecip.IAANNE ="
                        + this._dbWriteNumeric(statement.getTransaction(), getForAnneeDecision());
            }
        }
        // traitement du positionnement
        if (getFromLtEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA<" + this._dbWriteNumeric(statement.getTransaction(), getFromLtEtat());
        }
        // Avec les décisions actives
        if (Boolean.TRUE.equals(getIsActive())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IAACTI = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(true),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Avec les genres indépendants (rentier, tse etc)
        if (Boolean.TRUE.equals(getWantIndependant())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IATGAF IN (" + CPDecision.CS_INDEPENDANT + "," + CPDecision.CS_AGRICULTEUR + ","
                    + CPDecision.CS_RENTIER + "," + CPDecision.CS_TSE + ")";
        }
        if (Boolean.TRUE.equals(getWantNonActif())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IATGAF IN (" + CPDecision.CS_NON_ACTIF + "," + CPDecision.CS_ETUDIANT + ")";
        }
        // traitement du positionnement
        if (getFromIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.IAIDEC>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdDecision());
        }

        // traitement du positionnement
        if (getFromDateFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.IADFAC>="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateFacturation());
        }
        // traitement du positionnement
        if (getToDateFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.IADFAC<="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getToDateFacturation());
        }
        // traitement du positionnement
        if (getForIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.IAIDEC="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }

        // traitement du positionnement
        if (getForEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA=" + this._dbWriteNumeric(statement.getTransaction(), getForEtat());
        }

        // traitement du positionnement
        if (getFromEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA>=" + this._dbWriteNumeric(statement.getTransaction(), getFromEtat());
        }
        // traitement du positionnement
        if (getBeforeAnneeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE<=" + this._dbWriteNumeric(statement.getTransaction(), getBeforeAnneeDecision());
        }
        // traitement du positionnement
        if (getForAnneeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE =" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeDecision());
        }
        // traitement du positionnement
        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "EBIPAS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }

        // traitement du positionnement
        if (getForTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeDecision());
        }
        // Ne pas prendre les types de décision à blanc - Ex utilisé lors
        // du traitement de la facturation pour la CSC pour ne pas prendre
        // les sommations ou dispenses...
        if (getExceptTypeDecisionBlank().equals(new Boolean(true))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE<>0";
        }
        // traitement du positionnement
        if (getForNeTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE<>" + this._dbWriteNumeric(statement.getTransaction(), getForNeTypeDecision());
        }
        // traitement du positionnement
        if (getForNotInTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE NOT IN (" + getForNotInTypeDecision() + ")";
        }
        // traitement du positionnement
        if (getForInTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE IN (" + getForInTypeDecision() + ")";
        }
        // traitement du positionnement
        if (getForNotInIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "EBIPAS NOT IN (" + getForNotInIdPassage() + ")";
        }

        // traitement du positionnement
        if (getForGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATGAF=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAffilie());
        }

        // traitement du positionnement
        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.HTITIE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        // traitement du positionnement
        if (getForIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.MAIAFF="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }

        // traitement du positionnement
        if (getForNotEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA NOT IN (" + getForNotEtat() + ")";
        }
        // traitement du positionnement
        if (getToNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFAFFIP.MALNAF<=" + this._dbWriteString(statement.getTransaction(), getToNumAffilie());
        }
        // traitement du positionnement
        if (getFromAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF>=" + this._dbWriteString(statement.getTransaction(), getFromAffilie());
        }
        // Prendre les doublons d'affilie
        if (isForIdAffilieDouble()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF IN (SELECT MALNAF" + " FROM " + _getFrom(statement);
            setForIdAffilieDouble(false);
            String subWhere = _getWhere(statement);
            if (subWhere.length() > 0) {
                sqlWhere += " WHERE " + subWhere;
                setForIdAffilieDouble(true);
            }
        }
        // Avec les decisons qui doivent être facturées
        if (isForIsFacturation()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAFACT = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(true),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Sans les décisions qui doivent pas passer en facturation
        if (isForIsNotFacturation()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAFACT = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPDecisionAffiliationTiers();
    }

    /**
     * Returns the beforeAnneeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getBeforeAnneeDecision() {
        return beforeAnneeDecision;
    }

    /**
     * @return
     */
    public Boolean getExceptTypeDecisionBlank() {
        return exceptTypeDecisionBlank;
    }

    /**
     * Returns the forAnneeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForAnneeDecision() {
        return forAnneeDecision;
    }

    /**
     * Returns the forEtat.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForEtat() {
        return forEtat;
    }

    /**
     * Returns the forGenreAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * Returns the forIdAffiliation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAffiliation() {
        return forIdAffiliation;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * Returns the forIdPassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdPassage() {
        return forIdPassage;
    }

    /**
     * Returns the forIdTiers.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    public java.lang.String getForInTypeDecision() {
        return forInTypeDecision;
    }

    /**
     * Returns the forNeTypeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNeTypeDecision() {
        return forNeTypeDecision;
    }

    /**
     * Returns the forNotEtat.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNotEtat() {
        return forNotEtat;
    }

    /**
     * Returns the forNotInIdPassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNotInIdPassage() {
        return forNotInIdPassage;
    }

    /**
     * Returns the forNotInTypeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNotInTypeDecision() {
        return forNotInTypeDecision;
    }

    /**
     * @return
     */
    public java.lang.String getForOrder() {
        return forOrder;
    }

    /**
     * Returns the forTypeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForTypeDecision() {
        return forTypeDecision;
    }

    /**
     * Returns the fromAffilieDebut.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromAffilie() {
        return fromAffilie;
    }

    public java.lang.String getFromDateFacturation() {
        return fromDateFacturation;
    }

    public java.lang.String getFromEtat() {
        return fromEtat;
    }

    public java.lang.String getFromHeureEtat() {
        return fromHeureEtat;
    }

    /**
     * Getter
     */
    public java.lang.String getFromIdDecision() {
        return fromIdDecision;
    }

    /**
     * Returns the fromLtEtat.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromLtEtat() {
        return fromLtEtat;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * Returns the order.
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * Returns the selectMaxDateInformation.
     * 
     * @return Boolean
     */
    public Boolean getSelectMaxDateInformation() {
        return selectMaxDateInformation;
    }

    public java.lang.String getToDateFacturation() {
        return toDateFacturation;
    }

    /**
     * Returns the toNumAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getToNumAffilie() {
        return toNumAffilie;
    }

    public Boolean getWantIndependant() {
        return wantIndependant;
    }

    public Boolean getWantNonActif() {
        return wantNonActif;
    }

    /**
     * Returns the forIdAffilieDouble.
     * 
     * @return boolean
     */
    public boolean isForIdAffilieDouble() {
        return forIdAffilieDouble;
    }

    /**
     * @return
     */
    public boolean isForIsFacturation() {
        return forIsFacturation;
    }

    /**
     * @return
     */
    public boolean isForIsNotFacturation() {
        return forIsNotFacturation;
    }

    public boolean isStatistiques() {
        return statistiques;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnnee() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder(_getCollection() + "CPDECIP.IAANNE ASC");
        } else {
            setOrder(getOrder() + ", " + _getCollection() + "CPDECIP.IAANNE ASC");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnneeDesc() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder(_getCollection() + "CPDECIP.IAANNE DESC");
        } else {
            setOrder(getOrder() + ", " + _getCollection() + "CPDECIP.IAANNE DESC");
        }
    }

    // /** ALD Ajout
    public void orderByDateDecision() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IADINF DESC");
        } else {
            setOrder(getOrder() + ", IADINF DESC");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByDateDecisionASC() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder(_getCollection() + "CPDECIP.IADINF ASC");
        } else {
            setOrder(getOrder() + ", " + _getCollection() + "CPDECIP.IADINF ASC");
        }
    }

    public void orderByIdDecision() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder(_getCollection() + "CPDECIP.IAIDEC ASC");
        } else {
            setOrder(getOrder() + ", " + _getCollection() + "CPDECIP.IAIDEC ASC");
        }
    }

    public void orderByIdDecisionDesc() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder(_getCollection() + "CPDECIP.IAIDEC DESC");
        } else {
            setOrder(getOrder() + ", " + _getCollection() + "CPDECIP.IAIDEC DESC");
        }
    }

    public void orderByNumAffilie() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("AFAFFIP.MALNAF ASC");
        } else {
            setOrder(getOrder() + ", AFAFFIP.MALNAF ASC");
        }
    }

    /**
     * /** ALD ajout pour la liste des décisions 10.05.04 Insérez la description de la méthode ici. Date de création :
     * (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByTiers() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("AFAFFIP.MALNAF ASC");
        } else {
            setOrder(getOrder() + ", AFAFFIP.MALNAF ASC");
        }
    }

    public void orderByTypeDecision() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder(_getCollection() + "CPDECIP.IATTDE ASC");
        } else {
            setOrder(getOrder() + ", " + _getCollection() + "CPDECIP.IATTDE ASC");
        }
    }

    /**
     * ALD ajout pour la liste des décisions 10.05.04 Insérez la description de la méthode ici. Date de création :
     * (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByUser() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder(_getCollection() + "CPDECIP.IARESP ASC");
        } else {
            setOrder(getOrder() + ", " + _getCollection() + "CPDECIP.IARESP ASC");
        }
    }

    /**
     * Sets the beforeAnneeDecision.
     * 
     * @param beforeAnneeDecision
     *            The beforeAnneeDecision to set
     */
    public void setBeforeAnneeDecision(java.lang.String beforeAnneeDecision) {
        this.beforeAnneeDecision = beforeAnneeDecision;
    }

    /**
     * @param boolean1
     */
    public void setExceptTypeDecisionBlank(Boolean boolean1) {
        exceptTypeDecisionBlank = boolean1;
    }

    /**
     * Sets the forAnneeDecision.
     * 
     * @param forAnneeDecision
     *            The forAnneeDecision to set
     */
    public void setForAnneeDecision(java.lang.String forAnneeDecision) {
        this.forAnneeDecision = forAnneeDecision;
    }

    /**
     * Sets the forEtat.
     * 
     * @param forEtat
     *            The forEtat to set
     */
    public void setForEtat(java.lang.String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * Sets the forGenreAffilie.
     * 
     * @param forGenreAffilie
     *            The forGenreAffilie to set
     */
    public void setForGenreAffilie(java.lang.String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    /**
     * Sets the forIdAffiliation.
     * 
     * @param forIdAffiliation
     *            The forIdAffiliation to set
     */
    public void setForIdAffiliation(java.lang.String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    /**
     * Sets the forIdAffilieDouble.
     * 
     * @param forIdAffilieDouble
     *            The forIdAffilieDouble to set
     */
    public void setForIdAffilieDouble(boolean forIdAffilieDouble) {
        this.forIdAffilieDouble = forIdAffilieDouble;
    }

    /**
     * Setter
     */
    public void setForIdDecision(java.lang.String newForIdDecision) {
        forIdDecision = newForIdDecision;
    }

    /**
     * Sets the forIdPassage.
     * 
     * @param forIdPassage
     *            The forIdPassage to set
     */
    public void setForIdPassage(java.lang.String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    /**
     * Sets the forIdTiers.
     * 
     * @param forIdTiers
     *            The forIdTiers to set
     */
    public void setForIdTiers(java.lang.String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForInTypeDecision(java.lang.String forInTypeDecision) {
        this.forInTypeDecision = forInTypeDecision;
    }

    /**
     * @param b
     */
    public void setForIsFacturation(boolean b) {
        forIsFacturation = b;
    }

    /**
     * @param b
     */
    public void setForIsNotFacturation(boolean b) {
        forIsNotFacturation = b;
    }

    /**
     * Sets the forNeTypeDecision.
     * 
     * @param forNeTypeDecision
     *            The forNeTypeDecision to set
     */
    public void setForNeTypeDecision(java.lang.String forNeTypeDecision) {
        this.forNeTypeDecision = forNeTypeDecision;
    }

    /**
     * Sets the forNotEtat.
     * 
     * @param forNotEtat
     *            The forNotEtat to set
     */
    public void setForNotEtat(java.lang.String forNotEtat) {
        this.forNotEtat = forNotEtat;
    }

    /**
     * Sets the forNotInIdPassage.
     * 
     * @param forNotInIdPassage
     *            The forNotInIdPassage to set
     */
    public void setForNotInIdPassage(java.lang.String forNotInIdPassage) {
        this.forNotInIdPassage = forNotInIdPassage;
    }

    /**
     * Sets the forNotInTypeDecision.
     * 
     * @param forNotInTypeDecision
     *            The forNotInTypeDecision to set
     */
    public void setForNotInTypeDecision(java.lang.String forNotInTypeDecision) {
        this.forNotInTypeDecision = forNotInTypeDecision;
    }

    /**
     * @param string
     */
    public void setForOrder(java.lang.String forOrder) {
        this.forOrder = forOrder;
    }

    /**
     * Sets the forTypeDecision.
     * 
     * @param forTypeDecision
     *            The forTypeDecision to set
     */
    public void setForTypeDecision(java.lang.String forTypeDecision) {
        this.forTypeDecision = forTypeDecision;
    }

    /**
     * Sets the fromAffilieDebut.
     * 
     * @param fromAffilieDebut
     *            The fromAffilieDebut to set
     */
    public void setFromAffilie(java.lang.String fromAffilieDebut) {
        fromAffilie = fromAffilieDebut;
    }

    public void setFromDateFacturation(java.lang.String fromDateFacturation) {
        this.fromDateFacturation = fromDateFacturation;
    }

    public void setFromEtat(java.lang.String newFromEtat) {
        fromEtat = newFromEtat;
    }

    public void setFromHeureEtat(java.lang.String newFromHeureEtat) {
        fromHeureEtat = newFromHeureEtat;
    }

    /**
     * Setter
     */
    public void setFromIdDecision(java.lang.String newFromIdDecision) {
        fromIdDecision = newFromIdDecision;
    }

    /**
     * Sets the fromLtEtat.
     * 
     * @param fromLtEtat
     *            The fromLtEtat to set
     */
    public void setFromLtEtat(java.lang.String fromLtEtat) {
        this.fromLtEtat = fromLtEtat;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setOrder(java.lang.String order) {
        this.order = order;
    }

    /**
     * Sets the selectMaxDateInformation.
     * 
     * @param selectMaxDateInformation
     *            The selectMaxDateInformation to set
     */
    public void setSelectMaxDateInformation(Boolean selectMaxDateInformation) {
        this.selectMaxDateInformation = selectMaxDateInformation;
    }

    public void setStatistiques(boolean b) {
        statistiques = b;
    }

    public void setToDateFacturation(java.lang.String toDateFacturation) {
        this.toDateFacturation = toDateFacturation;
    }

    /**
     * Sets the toNumAffilie.
     * 
     * @param tillNumAffilie
     *            The tillNumAffilie to set
     */
    public void setToNumAffilie(java.lang.String toNumAffilie) {
        this.toNumAffilie = toNumAffilie;
    }

    public void setWantIndependant(Boolean wantIndependant) {
        this.wantIndependant = wantIndependant;
    }

    public void setWantNonActif(Boolean wantNonActif) {
        this.wantNonActif = wantNonActif;
    }

}
