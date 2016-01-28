package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.math.BigDecimal;

public class CGAdvancedEcritureListViewBean extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String beginWithLibelle = "";
    private String forDate = "";
    private Boolean forEstActive = null;
    private Boolean forEstProvisoire = null;
    private String forIdCentreCharge = new String();
    private String forIdCompte = new String();
    private String forIdEnteteEcriture = new String();
    private String forIdExerciceComptable = new String();
    private String forIdJournal = new String();
    private String forIdMandat = new String();
    private String forIdNature = new String();
    private String forIdRemarque = new String();
    private String fromDate = "";
    private String fromIdExterne = "";
    private String fromLibelle = "";

    private String fromMontant = "";
    private String fromMontantMonnaieEtrangere = "";
    private String fromPiece = "";
    private String orderBy = "";
    private String reqCritere = "";
    private String reqLibelle = "";
    private String reqMontant = "";

    /**
     * Commentaire relatif au constructeur CGEcritureManager.
     */
    public CGAdvancedEcritureListViewBean() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_beforeFind(BTransaction)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        super._beforeFind(transaction);

        if (!JadeStringUtil.isBlank(getReqMontant())) {
            if (CGEcritureViewBean.CS_MONTANT_MONNAIE_ETRANGERE.equals(getReqMontant())) {
                setForIdNature(CGCompte.CS_MONNAIE_ETRANGERE);
            } else {
                setForIdNature(null);
            }
        }

        // Gestion des criteres de recherche
        if (!JadeStringUtil.isBlank(getReqCritere())) {
            if (CGEcritureViewBean.CS_TRI_DATE.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_DATE);
                if (!JadeStringUtil.isBlank(getReqLibelle())) {
                    setFromDate(getReqLibelle());
                }
            } else if (CGEcritureViewBean.CS_TRI_COMPTE.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.IDEXTERNE);
                if (!JadeStringUtil.isBlank(getReqLibelle())) {
                    setFromIdExterne(getReqLibelle());
                }
            } else if (CGEcritureViewBean.CS_TRI_LIBELLE.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_LIBELLE);
                if (!JadeStringUtil.isBlank(getReqLibelle())) {
                    setFromLibelle(getReqLibelle());
                }
            } else if (CGEcritureViewBean.CS_TRI_MONTANT_CHF.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_MONTANT);
                if (!JadeStringUtil.isBlank(getReqLibelle())) {
                    setFromMontant(getReqLibelle());
                }

            } else if (CGEcritureViewBean.CS_TRI_MONTANT_MONNAIE_ETR.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_MONTANTMONNAIE + " ASC");
                if (!JadeStringUtil.isBlank(getReqLibelle())) {
                    setFromMontantMonnaieEtrangere(getReqLibelle());
                }

            } else if (CGEcritureViewBean.CS_TRI_PIECE.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_PIECE);
                if (!JadeStringUtil.isBlank(getReqLibelle())) {
                    setFromPiece(getReqLibelle());
                }

            } else {
                setOrderBy(null);
            }
        }
    }

    @Override
    protected String _getFields(BStatement statement) {
        String fields = "IDEXTERNE, LIBELLEFR, LIBELLEDE, LIBELLEIT, CODEISOMONNAIE, IDECRITURE, " + _getCollection()
                + "CGECRIP.IDCOMPTE, IDNATURE, " + _getCollection() + "CGECRIP.IDENTETEECRITURE, " + _getCollection()
                + "CGECRIP.IDJOURNAL, " + _getCollection() + "CGECRIP.IDEXERCOMPTABLE";
        fields += ", " + _getCollection() + "CGECRIP.IDREMARQUE, IDCENTRECHARGE, " + _getCollection()
                + "CGECRIP.IDMANDAT, " + _getCollection() + "CGECRIP.DATE, " + _getCollection()
                + "CGECRIP.DATEVALEUR, " + _getCollection() + "CGECRIP.PIECE, " + _getCollection()
                + "CGECRIP.LIBELLE, MONTANT, MONTANTMONNAIE, COURSMONNAIE, CODEDEBITCREDIT, REFERENCEEXTERNE";
        fields += ", ESTPOINTEE, ESTPROVISOIRE, ESTACTIVE, ESTERREUR, IDLOG, IDLIVRE, IDTYPEECRITURE";

        return fields;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String sqlFrom = _getCollection() + "CGECRIP";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGPLANP ON " + _getCollection() + "CGPLANP.idcompte = "
                + _getCollection() + "CGECRIP.idcompte";
        sqlFrom += " AND " + _getCollection() + "CGPLANP.idexercomptable = " + _getCollection()
                + "CGECRIP.idexercomptable";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGCOMTP ON " + _getCollection() + "CGCOMTP.idcompte = "
                + _getCollection() + "CGECRIP.idcompte";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGECREP ON " + _getCollection() + "CGECREP.identeteecriture = "
                + _getCollection() + "CGECRIP.identeteecriture";
        return sqlFrom;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {

        String result = _getCollection() + "CGECREP.IDTYPEECRITURE ASC";

        if (getOrderBy() == null || getOrderBy().trim().length() == 0) {
            result += "";
        } else if (getOrderBy().equals(CGEcritureViewBean.IDEXTERNE)) {
            result += ", " + _getCollection() + "CGPLANP." + getOrderBy();
        } else if (getOrderBy().equals(CGEcritureViewBean.FIELD_MONTANT)) {
            result += ", " + "abs(" + _getCollection() + "CGECRIP." + getOrderBy() + ") ASC";
        } else if (getOrderBy().equals(CGEcritureViewBean.FIELD_DATE)) {
            result += "";
        } else {
            result += ", " + _getCollection() + "CGECRIP." + getOrderBy();
        }

        if (result.trim().length() > 0) {
            result += ", ";
        }

        result += _getCollection() + "CGECRIP.DATE, " + _getCollection() + "CGECRIP.IDENTETEECRITURE, "
                + _getCollection() + "CGECRIP.CODEDEBITCREDIT asc, " + _getCollection() + "CGECRIP.IDECRITURE ";

        return result;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdCompte() != null && getForIdCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "IDCOMPTE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCompte());
        }

        // traitement du positionnement
        if (getForIdEnteteEcriture() != null && getForIdEnteteEcriture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "IDENTETEECRITURE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdEnteteEcriture());
        }

        // traitement du positionnement
        if (getForIdJournal() != null && getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "IDJOURNAL="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }

        // traitement du positionnement
        if (getForIdExerciceComptable() != null && getForIdExerciceComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "IDEXERCOMPTABLE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdExerciceComptable());
        }

        // traitement du positionnement
        if (getForIdRemarque() != null && getForIdRemarque().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "IDREMARQUE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdRemarque());
        }

        // traitement du positionnement
        if (getForIdCentreCharge() != null && getForIdCentreCharge().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "IDCENTRECHARGE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCentreCharge());
        }

        // traitement du positionnement
        if (getForIdMandat() != null && getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "IDMANDAT="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        // traitement du positionnement
        if (getForIdNature() != null && getForIdNature().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGCOMTP." + "IDNATURE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdNature());
        }

        // traitement du positionnement
        if (getForDate() != null && getForDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "DATE="
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }

        if (getForEstActive() != null && getForEstActive() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "ESTACTIVE="
                    + _dbWriteBoolean(statement.getTransaction(), getForEstActive(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        if (getForEstProvisoire() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection()
                    + "CGECRIP."
                    + "ESTPROVISOIRE="
                    + _dbWriteBoolean(statement.getTransaction(), getForEstProvisoire(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        if (getFromDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP.DATE>="
                    + _dbWriteDateAMJ(statement.getTransaction(), getFromDate());
        }

        if (getFromIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGPLANP.IDEXTERNE>="
                    + _dbWriteString(statement.getTransaction(), getFromIdExterne());

        }

        if (getFromLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP.LIBELLE>="
                    + _dbWriteString(statement.getTransaction(), getFromLibelle());
        }

        if (getFromPiece().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP.PIECE>="
                    + _dbWriteString(statement.getTransaction(), getFromPiece());
        }

        if (getFromMontant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            BigDecimal positive = new BigDecimal(0);
            BigDecimal negative = new BigDecimal(0);

            try {
                positive = new BigDecimal(JANumberFormatter.deQuote(getFromMontant().trim()));
            } catch (Exception e) {
                positive = new BigDecimal(0);
            }
            positive = positive.abs();
            negative = positive.negate();

            sqlWhere += "(( " + _getCollection() + "CGECRIP.MONTANT>="
                    + _dbWriteNumeric(statement.getTransaction(), positive.toString());
            sqlWhere += " AND (" + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_DEBIT);
            sqlWhere += " OR  " + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_EXTOURNE_DEBIT) + " ))";

            sqlWhere += " OR ";

            sqlWhere += " ( " + _getCollection() + "CGECRIP.MONTANT<="
                    + _dbWriteNumeric(statement.getTransaction(), negative.toString());
            sqlWhere += " AND (" + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_CREDIT);
            sqlWhere += " OR  " + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_EXTOURNE_CREDIT) + " )))";
        }

        if (getFromMontantMonnaieEtrangere().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            BigDecimal positive = new BigDecimal(0);
            BigDecimal negative = new BigDecimal(0);

            try {
                positive = new BigDecimal(JANumberFormatter.deQuote(getFromMontantMonnaieEtrangere().trim()));
            } catch (Exception e) {
                positive = new BigDecimal(0);
            }
            positive = positive.abs();
            negative = positive.negate();

            sqlWhere += "(( " + _getCollection() + "CGECRIP.MONTANTMONNAIE>="
                    + _dbWriteNumeric(statement.getTransaction(), positive.toString());
            sqlWhere += " AND (" + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_DEBIT);
            sqlWhere += " OR  " + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_EXTOURNE_DEBIT) + " ))";

            sqlWhere += " OR ";

            sqlWhere += " ( " + _getCollection() + "CGECRIP.MONTANTMONNAIE<="
                    + _dbWriteNumeric(statement.getTransaction(), negative.toString());
            sqlWhere += " AND (" + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_CREDIT);
            sqlWhere += " OR  " + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_EXTOURNE_CREDIT) + " )))";
        }

        return sqlWhere;

    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGAdvancedEcritureViewBean();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 18:23:49)
     * 
     * @return String
     */
    public String getBeginWithLibelle() {
        return beginWithLibelle;
    }

    public String getCredit(int pos) {
        CGAdvancedEcritureViewBean entity = (CGAdvancedEcritureViewBean) getEntity(pos);
        if (entity.isAvoir()) {
            return entity.getMontantAffiche();
        }
        return "";

    }

    public String getCreditMonnaie(int pos) {
        CGAdvancedEcritureViewBean entity = (CGAdvancedEcritureViewBean) getEntity(pos);
        if (entity.isAvoir()) {
            return entity.getMontantAfficheMonnaie();
        }
        return "";

    }

    public String getDebit(int pos) {
        CGAdvancedEcritureViewBean entity = (CGAdvancedEcritureViewBean) getEntity(pos);
        if (entity.isDoit()) {
            return entity.getMontantAffiche();
        }
        return "";
    }

    public String getDebitMonnaie(int pos) {
        CGAdvancedEcritureViewBean entity = (CGAdvancedEcritureViewBean) getEntity(pos);
        if (entity.isDoit()) {
            return entity.getMontantAfficheMonnaie();
        }
        return "";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 07:58:59)
     * 
     * @return String
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 09:33:47)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getForEstActive() {
        return forEstActive;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 09:33:47)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getForEstProvisoire() {
        return forEstProvisoire;
    }

    public String getForIdCentreCharge() {
        return forIdCentreCharge;
    }

    /**
     * Getter
     */
    public String getForIdCompte() {
        return forIdCompte;
    }

    public String getForIdEnteteEcriture() {
        return forIdEnteteEcriture;
    }

    public String getForIdExerciceComptable() {
        return forIdExerciceComptable;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Returns the forIdNature.
     * 
     * @return String
     */
    public String getForIdNature() {
        return forIdNature;
    }

    public String getForIdRemarque() {
        return forIdRemarque;
    }

    /**
     * Returns the fromDate.
     * 
     * @return String
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Returns the fromIdExterne.
     * 
     * @return String
     */
    public String getFromIdExterne() {
        return fromIdExterne;
    }

    /**
     * Returns the fromLibelle.
     * 
     * @return String
     */
    public String getFromLibelle() {
        return fromLibelle;
    }

    /**
     * Returns the fromMontant.
     * 
     * @return String
     */
    public String getFromMontant() {
        return fromMontant;
    }

    /**
     * Returns the fromMontantMonnaieEtrangere.
     * 
     * @return String
     */
    public String getFromMontantMonnaieEtrangere() {
        return fromMontantMonnaieEtrangere;
    }

    /**
     * Returns the fromPiece.
     * 
     * @return String
     */
    public String getFromPiece() {
        return fromPiece;
    }

    /**
     * Returns the orderBy.
     * 
     * @return String
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Returns the reqCritere.
     * 
     * @return String
     */
    public String getReqCritere() {
        return reqCritere;
    }

    /**
     * Returns the reqLibelle.
     * 
     * @return String
     */
    public String getReqLibelle() {
        return reqLibelle;
    }

    /**
     * Returns the reqMontant.
     * 
     * @return String
     */
    public String getReqMontant() {
        return reqMontant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:26:10)
     * 
     * @return boolean
     */
    public boolean isForEstActive() {
        return (forEstActive != null) ? forEstActive.booleanValue() : false;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:26:37)
     * 
     * @return boolean
     */
    public boolean isForEstProvisoire() {
        return (forEstProvisoire != null) ? forEstProvisoire.booleanValue() : false;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 18:23:49)
     * 
     * @param newBeginWithLibelle
     *            String
     */
    public void setBeginWithLibelle(String newBeginWithLibelle) {
        beginWithLibelle = newBeginWithLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 07:58:59)
     * 
     * @param newForDate
     *            String
     */
    public void setForDate(String newForDate) {
        forDate = newForDate;
    }

    public void setForIdCentreCharge(String newForIdCentreCharge) {
        forIdCentreCharge = newForIdCentreCharge;
    }

    /**
     * Setter
     */
    public void setForIdCompte(String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }

    public void setForIdEnteteEcriture(String newForIdEnteteEcriture) {
        forIdEnteteEcriture = newForIdEnteteEcriture;
    }

    public void setForIdExerciceComptable(String newForIdExerciceComptable) {
        forIdExerciceComptable = newForIdExerciceComptable;
    }

    public void setForIdJournal(String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    public void setForIdMandat(String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    /**
     * Sets the forIdNature.
     * 
     * @param forIdNature
     *            The forIdNature to set
     */
    public void setForIdNature(String forIdNature) {
        this.forIdNature = forIdNature;
    }

    public void setForIdRemarque(String newForIdRemarque) {
        forIdRemarque = newForIdRemarque;
    }

    /**
     * Sets the fromDate.
     * 
     * @param fromDate
     *            The fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Sets the fromIdExterne.
     * 
     * @param fromIdExterne
     *            The fromIdExterne to set
     */
    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    /**
     * Sets the fromLibelle.
     * 
     * @param fromLibelle
     *            The fromLibelle to set
     */
    public void setFromLibelle(String fromLibelle) {
        this.fromLibelle = fromLibelle;
    }

    /**
     * Sets the fromMontant.
     * 
     * @param fromMontant
     *            The fromMontant to set
     */
    public void setFromMontant(String fromMontant) {
        this.fromMontant = fromMontant;
    }

    /**
     * Sets the fromMontantMonnaieEtrangere.
     * 
     * @param fromMontantMonnaieEtrangere
     *            The fromMontantMonnaieEtrangere to set
     */
    public void setFromMontantMonnaieEtrangere(String fromMontantEtrangere) {
        fromMontantMonnaieEtrangere = fromMontantEtrangere;
    }

    /**
     * Sets the fromPiece.
     * 
     * @param fromPiece
     *            The fromPiece to set
     */
    public void setFromPiece(String fromPiece) {
        this.fromPiece = fromPiece;
    }

    /**
     * Sets the orderBy.
     * 
     * @param orderBy
     *            The orderBy to set
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * Sets the reqCritere.
     * 
     * @param reqCritere
     *            The reqCritere to set
     */
    public void setReqCritere(String reqCritere) {
        this.reqCritere = reqCritere;
    }

    /**
     * Sets the reqLibelle.
     * 
     * @param reqLibelle
     *            The reqLibelle to set
     */
    public void setReqLibelle(String reqLibelle) {
        this.reqLibelle = reqLibelle;
    }

    /**
     * Sets the reqMontant.
     * 
     * @param reqMontant
     *            The reqMontant to set
     */
    public void setReqMontant(String reqMontant) {
        this.reqMontant = reqMontant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:26:10)
     * 
     * @param newForEstActive
     *            boolean
     */
    public void wantForEstActive(boolean newForEstActive) {
        forEstActive = new Boolean(newForEstActive);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 09:33:47)
     * 
     * @param newForEstActive
     *            java.lang.Boolean
     */
    void wantForEstActive(java.lang.Boolean newForEstActive) {
        forEstActive = newForEstActive;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:26:37)
     * 
     * @param newForEstProvisoire
     *            boolean
     */
    public void wantForEstProvisoire(boolean newForEstProvisoire) {
        forEstProvisoire = new Boolean(newForEstProvisoire);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 09:33:47)
     * 
     * @param newForEstProvisoire
     *            java.lang.Boolean
     */
    void wantForEstProvisoire(java.lang.Boolean newForEstProvisoire) {
        forEstProvisoire = newForEstProvisoire;
    }

}
