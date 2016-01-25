package globaz.phenix.db.principale;

import globaz.globall.db.BConstants;
import globaz.jade.client.util.JadeStringUtil;

/*
 * Joint entre le fichier des communications fiscales et des décisions
 */
public class CPDecisionAffiliationManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAnneeDecision = "";
    private java.lang.String forEtat = "";
    private java.lang.String forExceptGenreAffilie = "";
    private String forExceptIdDecision = "";
    private String forExceptNoAffilie = "";
    private java.lang.String forExceptSpecification = "";
    private java.lang.String forExceptTypeDecision = "";
    private java.lang.String forGenreAffilie = "";
    private String forIdAffilie = "";
    private java.lang.String forIdDecision = "";
    private java.lang.String forIdPassage = "";
    private java.lang.String forIdTiers = "";
    private Boolean forIsComplementaire = Boolean.FALSE;
    private boolean forIsFacturation = false;
    private java.lang.String forNeTypeDecision = "";
    private String forNoAffilie = "";
    private String forResponsable = "";
    private java.lang.String forTypeDecision = "";
    private java.lang.String fromAnneeDecision = "";
    private java.lang.String fromNoAffilie = "";
    private java.lang.String fromPeriodeFacturation = "";
    private java.lang.String inEtat = "";
    private java.lang.String inGenreAffilie = "";
    private String inTypeDecision = "";
    private Boolean isActive = Boolean.FALSE;
    private Boolean isActiveOrRadie = Boolean.FALSE;
    private Boolean isRadie = Boolean.FALSE;
    private java.lang.String notInGenreAffilie = "";
    private String notInIdAffiliation = "";
    private java.lang.String order = "";
    private java.lang.String tillAnneeDecision = "";
    private java.lang.String tillNoAffilie = "";
    private java.lang.String tillPeriodeFacturation = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CPDECIP";
        String table2 = "AFAFFIP";

        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + ".MAIAFF=" + _getCollection() + table2 + ".MAIAFF)";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return getOrder();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String sqlWhere = "";

        if (getForIdAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.MAIAFF =" + getForIdAffilie() + "";
        }
        if (getNotInIdAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.MAIAFF not in (" + getNotInIdAffiliation() + ")";
        }

        // traitement du positionnement
        if (getForIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC=" + this._dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }
        if (getForExceptIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC<>" + this._dbWriteNumeric(statement.getTransaction(), getForExceptIdDecision());
        }
        // traitement du positionnement
        if (getForEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA=" + this._dbWriteNumeric(statement.getTransaction(), getForEtat());
        }
        // Pour un etat
        if (getInEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATETA IN (" + getInEtat() + ")";
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

        // Avec les décisions actives ou radiées
        if (Boolean.TRUE.equals(getIsActiveOrRadie())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (IAACTI = '1'or (IAACTI='2' and (IADDEB>=MADFIN AND MADFIN!=0)or IADFIN<=MADDEB))";
        }
        // Avec les décisions radiées
        if (Boolean.TRUE.equals(getIsRadie())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " ((IADDEB>=MADFIN AND MADFIN!=0)or IADFIN<=MADDEB)";
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
        if (getForAnneeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE=" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeDecision());
        }

        // traitement du positionnement
        if (getFromAnneeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromAnneeDecision());
        }
        // traitement du positionnement
        if (getTillAnneeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE<=" + this._dbWriteNumeric(statement.getTransaction(), getTillAnneeDecision());
        }
        // traitement du positionnement
        if (getFromPeriodeFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IADFAC>=" + this._dbWriteDateAMJ(statement.getTransaction(), getFromPeriodeFacturation());
        }
        // traitement du positionnement
        if (getTillPeriodeFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IADFAC<=" + this._dbWriteDateAMJ(statement.getTransaction(), getTillPeriodeFacturation());
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
        // Inclus dans une sélection
        if (getInTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE in (" + getInTypeDecision() + ")";
        }

        // traitement du positionnement
        if (getForNeTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE<>" + this._dbWriteNumeric(statement.getTransaction(), getForNeTypeDecision());
        }
        // traitement du positionnement
        if (getForGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATGAF=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAffilie());
        }
        // Différent d'un genre d'affilié (non actif, indépendant...)
        if (getForExceptGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATGAF<>" + this._dbWriteNumeric(statement.getTransaction(), getForExceptGenreAffilie());
        }
        // Pas égal à un type de décision (provisoire, définitive...)
        if (getForExceptTypeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATTDE <>" + this._dbWriteNumeric(statement.getTransaction(), getForExceptTypeDecision());
        }
        // traitement du positionnement
        if (getForResponsable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IARESP=" + this._dbWriteNumeric(statement.getTransaction(), getForResponsable());
        }

        // traitement du positionnement
        if (getFromNoAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF>=" + this._dbWriteString(statement.getTransaction(), getFromNoAffilie());
        }

        // traitement du positionnement
        if (getTillNoAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF<=" + this._dbWriteString(statement.getTransaction(), getTillNoAffilie());
        }
        // traitement du positionnement
        if (getForNoAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF=" + this._dbWriteString(statement.getTransaction(), getForNoAffilie());
        }
        // traitement du positionnement
        if (getForExceptNoAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF<>" + this._dbWriteString(statement.getTransaction(), getForExceptNoAffilie());
        }
        // Différent de la spécification
        if (getForExceptSpecification().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATSPE <> " + this._dbWriteNumeric(statement.getTransaction(), getForExceptSpecification());
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
        // Non inclus dans une sélection
        if (getNotInGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATGAF not in (" + getNotInGenreAffilie() + ")";
        }
        // Inclus dans une sélection
        if (getInGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IATGAF in (" + getInGenreAffilie() + ")";
        }

        // Avec les decisons qui doivent être facturées
        if (Boolean.TRUE.equals(getForIsComplementaire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IABCOM = "
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(true),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPDecisionAffiliation();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 13:17:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForAnneeDecision() {
        return forAnneeDecision;
    }

    public java.lang.String getForEtat() {
        return forEtat;
    }

    public java.lang.String getForExceptGenreAffilie() {
        return forExceptGenreAffilie;
    }

    public String getForExceptIdDecision() {
        return forExceptIdDecision;
    }

    public String getForExceptNoAffilie() {
        return forExceptNoAffilie;
    }

    /**
     * Returns the forExceptSpecification.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForExceptSpecification() {
        return forExceptSpecification;
    }

    public java.lang.String getForExceptTypeDecision() {
        return forExceptTypeDecision;
    }

    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public String getForIdAffilie() {
        return forIdAffilie;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdDecision() {
        return forIdDecision;
    }

    public java.lang.String getForIdPassage() {
        return forIdPassage;
    }

    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    public Boolean getForIsComplementaire() {
        return forIsComplementaire;
    }

    /**
     * Returns the forNeTypeDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNeTypeDecision() {
        return forNeTypeDecision;
    }

    public String getForNoAffilie() {
        return forNoAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 12:03:26)
     * 
     * @return string
     */
    public java.lang.String getForResponsable() {
        return forResponsable;
    }

    public java.lang.String getForTypeDecision() {
        return forTypeDecision;
    }

    public java.lang.String getFromAnneeDecision() {
        return fromAnneeDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.05.2003 14:43:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNoAffilie() {
        return fromNoAffilie;
    }

    public java.lang.String getFromPeriodeFacturation() {
        return fromPeriodeFacturation;
    }

    public java.lang.String getInEtat() {
        return inEtat;
    }

    public java.lang.String getInGenreAffilie() {
        return inGenreAffilie;
    }

    public String getInTypeDecision() {
        return inTypeDecision;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Boolean getIsActiveOrRadie() {
        return isActiveOrRadie;
    }

    public Boolean getIsRadie() {
        return isRadie;
    }

    public java.lang.String getNotInGenreAffilie() {
        return notInGenreAffilie;
    }

    public String getNotInIdAffiliation() {
        return notInIdAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.07.2003 10:21:10)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    public java.lang.String getTillAnneeDecision() {
        return tillAnneeDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.05.2003 14:43:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTillNoAffilie() {
        return tillNoAffilie;
    }

    public java.lang.String getTillPeriodeFacturation() {
        return tillPeriodeFacturation;
    }

    /**
     * @return
     */
    public boolean isForIsFacturation() {
        return forIsFacturation;
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

    /**
     * tri par id tiers ascendant
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdTiers() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder(_getCollection() + "CPDECIP.HTITIE");
        } else {
            setOrder(getOrder() + ", " + _getCollection() + "CPDECIP.HTITIE");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNoAffilie() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder(_getCollection() + "AFAFFIP.MALNAF ASC");
        } else {
            setOrder(getOrder() + ", " + _getCollection() + "AFAFFIP.MALNAF ASC");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
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
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 13:17:03)
     * 
     * @param newForAnneeDecision
     *            java.lang.String
     */
    public void setForAnneeDecision(java.lang.String newForAnneeDecision) {
        forAnneeDecision = newForAnneeDecision;
    }

    public void setForEtat(java.lang.String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForExceptGenreAffilie(java.lang.String forExceptGenreAffilie) {
        this.forExceptGenreAffilie = forExceptGenreAffilie;
    }

    public void setForExceptIdDecision(String forExceptIdDecision) {
        this.forExceptIdDecision = forExceptIdDecision;
    }

    public void setForExceptNoAffilie(String forExceptNoAffilie) {
        this.forExceptNoAffilie = forExceptNoAffilie;
    }

    /**
     * Sets the forExceptSpecification.
     * 
     * @param forExceptSpecification
     *            The forExceptSpecification to set
     */
    public void setForExceptSpecification(java.lang.String forExceptSpecification) {
        this.forExceptSpecification = forExceptSpecification;
    }

    public void setForExceptTypeDecision(java.lang.String forExceptTypeDecision) {
        this.forExceptTypeDecision = forExceptTypeDecision;
    }

    public void setForGenreAffilie(java.lang.String newForGenreAffilie) {
        forGenreAffilie = newForGenreAffilie;
    }

    public void setForIdAffilie(String forIdAffilie) {
        this.forIdAffilie = forIdAffilie;
    }

    /**
     * Setter
     */
    public void setForIdDecision(java.lang.String newForIdDecision) {
        forIdDecision = newForIdDecision;
    }

    public void setForIdPassage(java.lang.String newForIdPassage) {
        forIdPassage = newForIdPassage;
    }

    public void setForIdTiers(java.lang.String newForIdTiers) {
        forIdTiers = newForIdTiers;
    }

    public void setForIsComplementaire(Boolean forIsComplementaire) {
        this.forIsComplementaire = forIsComplementaire;
    }

    /**
     * @param b
     */
    public void setForIsFacturation(boolean b) {
        forIsFacturation = b;
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

    public void setForNoAffilie(String forNumAffilie) {
        forNoAffilie = forNumAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 12:03:26)
     * 
     * @param newForResponsable
     *            string
     */
    public void setForResponsable(java.lang.String newForResponsable) {
        forResponsable = newForResponsable;
    }

    public void setForTypeDecision(java.lang.String newForTypeDecision) {
        forTypeDecision = newForTypeDecision;
    }

    public void setFromAnneeDecision(java.lang.String fromAnneeDecision) {
        this.fromAnneeDecision = fromAnneeDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.05.2003 14:43:23)
     * 
     * @param newFromAffilieDebut
     *            java.lang.String
     */
    public void setFromNoAffilie(java.lang.String newFromAffilieDebut) {
        fromNoAffilie = newFromAffilieDebut;
    }

    public void setFromPeriodeFacturation(java.lang.String fromPeriodeFacturation) {
        this.fromPeriodeFacturation = fromPeriodeFacturation;
    }

    public void setInEtat(java.lang.String inEtat) {
        this.inEtat = inEtat;
    }

    public void setInGenreAffilie(java.lang.String inGenreAffilie) {
        this.inGenreAffilie = inGenreAffilie;
    }

    public void setInTypeDecision(String inTypeDecision) {
        this.inTypeDecision = inTypeDecision;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setIsActiveOrRadie(Boolean isActiveOrRadie) {
        this.isActiveOrRadie = isActiveOrRadie;
    }

    public void setIsRadie(Boolean isRadie) {
        this.isRadie = isRadie;
    }

    public void setNotInGenreAffilie(java.lang.String notInGenreAffilie) {
        this.notInGenreAffilie = notInGenreAffilie;
    }

    public void setNotInIdAffiliation(String notInIdAffiliation) {
        this.notInIdAffiliation = notInIdAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.07.2003 10:21:10)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

    public void setTillAnneeDecision(java.lang.String tillAnneeDecision) {
        this.tillAnneeDecision = tillAnneeDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.05.2003 14:43:40)
     * 
     * @param newFromAffilieFin
     *            java.lang.String
     */
    public void setTillNoAffilie(java.lang.String newFromAffilieFin) {
        tillNoAffilie = newFromAffilieFin;
    }

    public void setTillPeriodeFacturation(java.lang.String tillPeriodeFacturation) {
        this.tillPeriodeFacturation = tillPeriodeFacturation;
    }

}
