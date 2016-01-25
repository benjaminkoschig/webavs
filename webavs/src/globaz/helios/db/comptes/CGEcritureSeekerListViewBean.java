package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;

public class CGEcritureSeekerListViewBean extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String beginWithLibelle = "";
    private java.lang.String forDate = "";
    private Boolean forEstActive = null;
    private Boolean forEstProvisoire = null;
    private java.lang.String forIdCentreCharge = new String();
    private java.lang.String forIdCompte = new String();
    private java.lang.String forIdEnteteEcriture = new String();
    private java.lang.String forIdExerciceComptable = new String();
    private java.lang.String forIdExterne = "";
    private java.lang.String forIdJournal = new String();
    private java.lang.String forIdMandat = new String();
    private java.lang.String forIdNature = new String();
    private java.lang.String forIdPeriodeComptable = "";
    private java.lang.String forIdRemarque = new String();
    private java.lang.String forLibelleLike = "";
    private java.lang.String forMontant = "";

    private java.lang.String forMontantMonnaieEtrangere = "";
    private java.lang.String forPiece = "";
    private java.lang.String fromDate = "";
    private String orderBy = "";
    private String reqComptabilite = "";
    private java.lang.String reqCritere = "";
    private java.lang.String reqLibelle = "";
    private java.lang.String reqMontant = "";

    /**
     * Commentaire relatif au constructeur CGEcritureManager.
     */
    public CGEcritureSeekerListViewBean() {
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
                    setForIdExterne(getReqLibelle());
                }
            } else if (CGEcritureViewBean.CS_TRI_LIBELLE.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_LIBELLE);
                if (!JadeStringUtil.isBlank(getReqLibelle())) {
                    setForLibelleLike(getReqLibelle());
                }
            } else if (CGEcritureViewBean.CS_TRI_MONTANT_CHF.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_MONTANT);
                if (!JadeStringUtil.isBlank(getReqLibelle())) {
                    setForMontant(getReqLibelle());
                }

            } else if (CGEcritureViewBean.CS_TRI_MONTANT_MONNAIE_ETR.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_MONTANTMONNAIE);
                if (!JadeStringUtil.isBlank(getReqLibelle())) {
                    setForMontantMonnaieEtrangere(getReqLibelle());
                }

            } else if (CGEcritureViewBean.CS_TRI_PIECE.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_PIECE);
                if (!JadeStringUtil.isBlank(getReqLibelle())) {
                    setForPiece(getReqLibelle());
                }

            } else {
                setOrderBy(null);
            }
        }
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
        sqlFrom += " INNER JOIN " + _getCollection() + "CGJOURP ON " + _getCollection() + "CGJOURP.idjournal = "
                + _getCollection() + "CGECRIP.idjournal";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGPERIP ON " + _getCollection()
                + "CGJOURP.idperiodecomptable = " + _getCollection() + "CGPERIP.idperiodecomptable";
        return sqlFrom;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {

        String result = "";

        if (getOrderBy() == null || getOrderBy().trim().length() == 0) {
            result = "";
        } else if (getOrderBy().equals(CGEcritureViewBean.IDEXTERNE)) {
            result = _getCollection() + "CGPLANP." + getOrderBy();
        } else if (getOrderBy().equals(CGEcritureViewBean.FIELD_MONTANT)) {
            result = "abs(" + _getCollection() + "CGECRIP." + getOrderBy() + ") DESC";
        } else if (getOrderBy().equals(CGEcritureViewBean.FIELD_DATE)) {
            result = "";
        } else {
            result = _getCollection() + "CGECRIP." + getOrderBy();
        }
        if (result.trim().length() > 0) {
            result += ", ";
        }

        result += _getCollection() + "CGECRIP.DATE, " + _getCollection() + "CGECRIP.IDENTETEECRITURE, "
                + _getCollection() + "CGECRIP.IDECRITURE ";
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

        if (!JadeStringUtil.isIntegerEmpty(getForIdPeriodeComptable())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGJOURP." + "IDPERIODECOMPTABLE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdPeriodeComptable());
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
            sqlWhere += _getCollection() + "CGECRIP.DATE=" + _dbWriteDateAMJ(statement.getTransaction(), getFromDate());
        }

        if (getForIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGPLANP.IDEXTERNE="
                    + _dbWriteString(statement.getTransaction(), getForIdExterne());

        }

        if (getForLibelleLike().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP.LIBELLE like '%" + getForLibelleLike() + "%'";
        }

        if (getForPiece().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP.PIECE=" + _dbWriteString(statement.getTransaction(), getForPiece());
        }

        // Si provisoire, on prend tout.
        if (getReqComptabilite() != null && getReqComptabilite().trim().length() != 0) {
            if (CodeSystem.CS_DEFINITIF.equals(getReqComptabilite())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "CGECRIP.ESTPROVISOIRE='2'";
            }
        }

        if (getForMontant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            BigDecimal positive = new BigDecimal(0);
            BigDecimal negative = new BigDecimal(0);

            try {
                positive = new BigDecimal(JANumberFormatter.deQuote(getForMontant().trim()));
            } catch (Exception e) {
                positive = new BigDecimal(0);
            }

            // positive = positive.abs();
            negative = positive.negate();

            sqlWhere += "(( " + _getCollection() + "CGECRIP.MONTANT="
                    + _dbWriteNumeric(statement.getTransaction(), positive.toString());
            sqlWhere += " AND (" + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_DEBIT);
            sqlWhere += " OR  " + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_EXTOURNE_DEBIT) + " ))";

            sqlWhere += " OR ";

            sqlWhere += " ( " + _getCollection() + "CGECRIP.MONTANT="
                    + _dbWriteNumeric(statement.getTransaction(), negative.toString());
            sqlWhere += " AND (" + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_CREDIT);
            sqlWhere += " OR  " + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_EXTOURNE_CREDIT) + " )))";
        }

        if (getForMontantMonnaieEtrangere().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            BigDecimal positive = new BigDecimal(0);
            BigDecimal negative = new BigDecimal(0);

            try {
                positive = new BigDecimal(JANumberFormatter.deQuote(getForMontantMonnaieEtrangere().trim()));
            } catch (Exception e) {
                positive = new BigDecimal(0);
            }
            // positive = positive.abs();
            negative = positive.negate();

            sqlWhere += "(( " + _getCollection() + "CGECRIP.MONTANTMONNAIE="
                    + _dbWriteNumeric(statement.getTransaction(), positive.toString());
            sqlWhere += " AND (" + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_DEBIT);
            sqlWhere += " OR  " + _getCollection() + "CGECRIP.CODEDEBITCREDIT="
                    + _dbWriteNumeric(statement.getTransaction(), CodeSystem.CS_EXTOURNE_DEBIT) + " ))";

            sqlWhere += " OR ";

            sqlWhere += " ( " + _getCollection() + "CGECRIP.MONTANTMONNAIE="
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
        return new CGEcritureSeekerViewBean();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 18:23:49)
     * 
     * @return java.lang.String
     */
    public java.lang.String getBeginWithLibelle() {
        return beginWithLibelle;
    }

    public String getCredit(int pos) {
        CGEcritureSeekerViewBean entity = (CGEcritureSeekerViewBean) getEntity(pos);
        if (entity.isAvoir()) {
            return entity.getMontantAffiche();
        }
        return "";

    }

    public String getCreditMonnaie(int pos) {
        CGEcritureSeekerViewBean entity = (CGEcritureSeekerViewBean) getEntity(pos);
        if (entity.isAvoir()) {
            return entity.getMontantAfficheMonnaie();
        }
        return "";

    }

    public String getDebit(int pos) {
        CGEcritureSeekerViewBean entity = (CGEcritureSeekerViewBean) getEntity(pos);
        if (entity.isDoit()) {
            return entity.getMontantAffiche();
        }
        return "";
    }

    public String getDebitMonnaie(int pos) {
        CGEcritureSeekerViewBean entity = (CGEcritureSeekerViewBean) getEntity(pos);
        if (entity.isDoit()) {
            return entity.getMontantAfficheMonnaie();
        }
        return "";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 07:58:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDate() {
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

    public java.lang.String getForIdCentreCharge() {
        return forIdCentreCharge;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdCompte() {
        return forIdCompte;
    }

    public java.lang.String getForIdEnteteEcriture() {
        return forIdEnteteEcriture;
    }

    public java.lang.String getForIdExerciceComptable() {
        return forIdExerciceComptable;
    }

    /**
     * Returns the forIdExterne.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdExterne() {
        return forIdExterne;
    }

    public java.lang.String getForIdJournal() {
        return forIdJournal;
    }

    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Returns the forIdNature.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdNature() {
        return forIdNature;
    }

    /**
     * Returns the forIdPeriodeComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdPeriodeComptable() {
        return forIdPeriodeComptable;
    }

    public java.lang.String getForIdRemarque() {
        return forIdRemarque;
    }

    /**
     * Returns the forLibelleLike.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForLibelleLike() {
        return forLibelleLike;
    }

    /**
     * Returns the forMontant.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForMontant() {
        return forMontant;
    }

    /**
     * Returns the forMontantMonnaieEtrangere.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForMontantMonnaieEtrangere() {
        return forMontantMonnaieEtrangere;
    }

    /**
     * Returns the forPiece.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForPiece() {
        return forPiece;
    }

    /**
     * Returns the fromDate.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDate() {
        return fromDate;
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
     * Returns the reqComptabilite.
     * 
     * @return String
     */
    public String getReqComptabilite() {
        return reqComptabilite;
    }

    /**
     * Returns the reqCritere.
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqCritere() {
        return reqCritere;
    }

    /**
     * Returns the reqLibelle.
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqLibelle() {
        return reqLibelle;
    }

    /**
     * Returns the reqMontant.
     * 
     * @return java.lang.String
     */
    public java.lang.String getReqMontant() {
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
     *            java.lang.String
     */
    public void setBeginWithLibelle(java.lang.String newBeginWithLibelle) {
        beginWithLibelle = newBeginWithLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 07:58:59)
     * 
     * @param newForDate
     *            java.lang.String
     */
    public void setForDate(java.lang.String newForDate) {
        forDate = newForDate;
    }

    public void setForIdCentreCharge(java.lang.String newForIdCentreCharge) {
        forIdCentreCharge = newForIdCentreCharge;
    }

    /**
     * Setter
     */
    public void setForIdCompte(java.lang.String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }

    public void setForIdEnteteEcriture(java.lang.String newForIdEnteteEcriture) {
        forIdEnteteEcriture = newForIdEnteteEcriture;
    }

    public void setForIdExerciceComptable(java.lang.String newForIdExerciceComptable) {
        forIdExerciceComptable = newForIdExerciceComptable;
    }

    /**
     * Sets the forIdExterne.
     * 
     * @param forIdExterne
     *            The forIdExterne to set
     */
    public void setForIdExterne(java.lang.String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    public void setForIdJournal(java.lang.String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    public void setForIdMandat(java.lang.String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    /**
     * Sets the forIdNature.
     * 
     * @param forIdNature
     *            The forIdNature to set
     */
    public void setForIdNature(java.lang.String forIdNature) {
        this.forIdNature = forIdNature;
    }

    /**
     * Sets the forIdPeriodeComptable.
     * 
     * @param forIdPeriodeComptable
     *            The forIdPeriodeComptable to set
     */
    public void setForIdPeriodeComptable(java.lang.String forIdPeriodeComptable) {
        this.forIdPeriodeComptable = forIdPeriodeComptable;
    }

    public void setForIdRemarque(java.lang.String newForIdRemarque) {
        forIdRemarque = newForIdRemarque;
    }

    /**
     * Sets the forLibelleLike.
     * 
     * @param forLibelleLike
     *            The forLibelleLike to set
     */
    public void setForLibelleLike(java.lang.String forLibelleLike) {
        this.forLibelleLike = forLibelleLike;
    }

    /**
     * Sets the forMontant.
     * 
     * @param forMontant
     *            The forMontant to set
     */
    public void setForMontant(java.lang.String forMontant) {
        this.forMontant = forMontant;
    }

    /**
     * Sets the forMontantMonnaieEtrangere.
     * 
     * @param forMontantMonnaieEtrangere
     *            The forMontantMonnaieEtrangere to set
     */
    public void setForMontantMonnaieEtrangere(java.lang.String forMontantMonnaieEtrangere) {
        this.forMontantMonnaieEtrangere = forMontantMonnaieEtrangere;
    }

    /**
     * Sets the forPiece.
     * 
     * @param forPiece
     *            The forPiece to set
     */
    public void setForPiece(java.lang.String forPiece) {
        this.forPiece = forPiece;
    }

    /**
     * Sets the fromDate.
     * 
     * @param fromDate
     *            The fromDate to set
     */
    public void setFromDate(java.lang.String fromDate) {
        this.fromDate = fromDate;
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
     * Sets the reqComptabilite.
     * 
     * @param reqComptabilite
     *            The reqComptabilite to set
     */
    public void setReqComptabilite(String reqComptabilite) {
        this.reqComptabilite = reqComptabilite;
    }

    /**
     * Sets the reqCritere.
     * 
     * @param reqCritere
     *            The reqCritere to set
     */
    public void setReqCritere(java.lang.String reqCritere) {
        this.reqCritere = reqCritere;
    }

    /**
     * Sets the reqLibelle.
     * 
     * @param reqLibelle
     *            The reqLibelle to set
     */
    public void setReqLibelle(java.lang.String reqLibelle) {
        this.reqLibelle = reqLibelle;
    }

    /**
     * Sets the reqMontant.
     * 
     * @param reqMontant
     *            The reqMontant to set
     */
    public void setReqMontant(java.lang.String reqMontant) {
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
