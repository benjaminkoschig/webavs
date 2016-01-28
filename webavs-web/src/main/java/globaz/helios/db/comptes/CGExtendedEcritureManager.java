package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Insert the type's description here. Creation date: (04.07.2003 09:17:09)
 * 
 * @author: Administrator
 */
public class CGExtendedEcritureManager extends globaz.globall.db.BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forDateDebut = new String();
    private java.lang.String forDateFin = new String();
    private java.lang.String forIdCompte = new String();
    private java.lang.String forIdExerciceComptable = new String();
    private java.lang.String forIdListePeriodeComptable = new String();
    private java.lang.String forIdLivre = new String();
    private java.lang.String forIdMandat = new String();
    private java.lang.Boolean forIsActive = null;
    private java.lang.Boolean forIsProvisoire = null;
    private java.lang.String forNumeroCompteMax = new String();
    private java.lang.String forNumeroCompteMin = new String();
    private java.lang.String forPiece = new String();
    private String orderBy = "";
    private java.lang.String reqCritere = "";

    /**
     * CGExtendedEcritureManager constructor comment.
     */
    public CGExtendedEcritureManager() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_beforeFind(BTransaction)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        super._beforeFind(transaction);
        // Gestion des criteres de recherche
        if (!JadeStringUtil.isBlank(getReqCritere())) {
            if (CGEcritureViewBean.CS_TRI_DATE.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_DATE);
            } else if (CGEcritureViewBean.CS_TRI_COMPTE.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.IDEXTERNE);
            } else if (CGEcritureViewBean.CS_TRI_LIBELLE.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_LIBELLE);
            } else if (CGEcritureViewBean.CS_TRI_MONTANT_CHF.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_MONTANT);
            } else if (CGEcritureViewBean.CS_TRI_MONTANT_MONNAIE_ETR.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_MONTANTMONNAIE);
            } else if (CGEcritureViewBean.CS_TRI_PIECE.equals(getReqCritere())) {
                setOrderBy(CGEcritureViewBean.FIELD_PIECE);
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
        String sqlFrom = "";

        sqlFrom += _getCollection() + "CGECRIP";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGJOURP ON " + _getCollection() + "CGECRIP.idJournal = "
                + _getCollection() + "CGJOURP.idJournal ";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGPERIP ON " + _getCollection()
                + "CGJOURP.idPeriodeComptable = " + _getCollection() + "CGPERIP.idPeriodeComptable ";
        sqlFrom += " 			AND " + _getCollection() + "CGJOURP.IDEXERCOMPTABLE = " + _getCollection()
                + "CGECRIP.IDEXERCOMPTABLE ";
        sqlFrom += " 			AND " + _getCollection() + "CGPERIP.IDEXERCOMPTABLE = " + _getCollection()
                + "CGECRIP.IDEXERCOMPTABLE ";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGCOMTP ON " + _getCollection() + "CGECRIP.idcompte = "
                + _getCollection() + "CGCOMTP.idCompte ";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGPLANP ON " + _getCollection() + "CGECRIP.idcompte = "
                + _getCollection() + "CGPLANP.idCompte ";
        sqlFrom += " 			AND " + _getCollection() + "CGECRIP.idExerComptable = " + _getCollection()
                + "CGPLANP.idExerComptable ";
        sqlFrom += " LEFT OUTER JOIN " + _getCollection() + "CGCCHAP ON " + _getCollection()
                + "CGECRIP.idCentreCharge = " + _getCollection() + "CGCCHAP.idCentreCharge ";
        return sqlFrom;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (getOrderBy() == null || getOrderBy().trim().length() == 0) {
            return "";
        } else if (getOrderBy().equals(CGEcritureViewBean.IDEXTERNE)) {
            return _getCollection() + "CGPLANP." + getOrderBy();
        } else if (getOrderBy().equals(CGEcritureViewBean.FIELD_MONTANT)) {
            return "abs(" + _getCollection() + "CGECRIP." + getOrderBy() + ") DESC";
        } else {
            return _getCollection() + "CGECRIP." + getOrderBy();
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        String sqlWhere = "";

        if (getForIdExerciceComptable() != null && getForIdExerciceComptable().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "IDEXERCOMPTABLE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdExerciceComptable());
        }

        if (getForIdMandat() != null && getForIdMandat().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "IdMandat="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        if (getForIdCompte() != null && getForIdCompte().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGCOMTP." + "idCompte="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCompte());
        }

        if (getForIsProvisoire() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP.ESTPROVISOIRE = '"
                    + _dbWriteBoolean(statement.getTransaction(), getForIsProvisoire()) + "' ";
        }

        if (getForIsActive() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP.ESTACTIVE = '"
                    + _dbWriteBoolean(statement.getTransaction(), getForIsActive()) + "' ";
        }

        if (getForIdListePeriodeComptable() != null && getForIdListePeriodeComptable().length() != 0
                && !"0".equals(getForIdListePeriodeComptable())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGPERIP.idPeriodeComptable in (" + getForIdListePeriodeComptable() + ")";
        }

        if (getForIdLivre() != null && getForIdLivre().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "idLivre="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdLivre());
        }

        if (getForPiece() != null && getForPiece().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "piece="
                    + _dbWriteString(statement.getTransaction(), getForPiece());
        }

        if (getForDateDebut() != null && getForDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP.date >= "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateDebut());
        }

        if (getForDateFin() != null && getForDateFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP.date <= "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateFin());
        }

        if (getForNumeroCompteMin() != null && getForNumeroCompteMin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGPLANP.idExterne >= '" + getForNumeroCompteMin() + "' ";
        }
        if (getForNumeroCompteMax() != null && getForNumeroCompteMax().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGPLANP.idExterne <= '" + getForNumeroCompteMax() + "' ";
        }

        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += _getCollection() + "CGECRIP." + "ESTACTIVE="
                + _dbWriteBoolean(statement.getTransaction(), new Boolean(true), BConstants.DB_TYPE_BOOLEAN_CHAR);

        return sqlWhere;
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGExtendedEcriture();
    }

    /**
     * Returns the forDateDebut.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * Returns the forDateFin.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDateFin() {
        return forDateFin;
    }

    /**
     * Returns the forIdCompte.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCompte() {
        return forIdCompte;
    }

    /**
     * Returns the forIdExerciceComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdExerciceComptable() {
        return forIdExerciceComptable;
    }

    /**
     * Returns the forIdListePeriodeComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdListePeriodeComptable() {
        return forIdListePeriodeComptable;
    }

    /**
     * Returns the forIdLivre.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdLivre() {
        return forIdLivre;
    }

    /**
     * Returns the forIdMandat.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Returns the forIsActive.
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getForIsActive() {
        return forIsActive;
    }

    /**
     * Returns the forIsProvisoire.
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getForIsProvisoire() {
        return forIsProvisoire;
    }

    /**
     * Returns the forNumeroCompteMax.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNumeroCompteMax() {
        return forNumeroCompteMax;
    }

    /**
     * Returns the forNumeroCompteMin.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNumeroCompteMin() {
        return forNumeroCompteMin;
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
     * @return java.lang.String
     */
    public java.lang.String getReqCritere() {
        return reqCritere;
    }

    /**
     * Sets the forDateDebut.
     * 
     * @param forDateDebut
     *            The forDateDebut to set
     */
    public void setForDateDebut(java.lang.String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * Sets the forDateFin.
     * 
     * @param forDateFin
     *            The forDateFin to set
     */
    public void setForDateFin(java.lang.String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * Sets the forIdCompte.
     * 
     * @param forIdCompte
     *            The forIdCompte to set
     */
    public void setForIdCompte(java.lang.String forIdCompte) {
        this.forIdCompte = forIdCompte;
    }

    /**
     * Sets the forIdExerciceComptable.
     * 
     * @param forIdExerciceComptable
     *            The forIdExerciceComptable to set
     */
    public void setForIdExerciceComptable(java.lang.String forIdExerciceComptable) {
        this.forIdExerciceComptable = forIdExerciceComptable;
    }

    /**
     * Sets the forIdListePeriodeComptable.
     * 
     * @param forIdListePeriodeComptable
     *            The forIdListePeriodeComptable to set
     */
    public void setForIdListePeriodeComptable(java.lang.String forIdListePeriodeComptable) {
        this.forIdListePeriodeComptable = forIdListePeriodeComptable;
    }

    /**
     * Sets the forIdLivre.
     * 
     * @param forIdLivre
     *            The forIdLivre to set
     */
    public void setForIdLivre(java.lang.String forIdLivre) {
        this.forIdLivre = forIdLivre;
    }

    /**
     * Sets the forIdMandat.
     * 
     * @param forIdMandat
     *            The forIdMandat to set
     */
    public void setForIdMandat(java.lang.String forIdMandat) {
        this.forIdMandat = forIdMandat;
    }

    /**
     * Sets the forIsActive.
     * 
     * @param forIsActive
     *            The forIsActive to set
     */
    public void setForIsActive(java.lang.Boolean forIsActive) {
        this.forIsActive = forIsActive;
    }

    /**
     * Sets the forIsProvisoire.
     * 
     * @param forIsProvisoire
     *            The forIsProvisoire to set
     */
    public void setForIsProvisoire(java.lang.Boolean forIsProvisoire) {
        this.forIsProvisoire = forIsProvisoire;
    }

    /**
     * Sets the forNumeroCompteMax.
     * 
     * @param forNumeroCompteMax
     *            The forNumeroCompteMax to set
     */
    public void setForNumeroCompteMax(java.lang.String forNumeroCompteMax) {
        this.forNumeroCompteMax = forNumeroCompteMax;
    }

    /**
     * Sets the forNumeroCompteMin.
     * 
     * @param forNumeroCompteMin
     *            The forNumeroCompteMin to set
     */
    public void setForNumeroCompteMin(java.lang.String forNumeroCompteMin) {
        this.forNumeroCompteMin = forNumeroCompteMin;
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
    public void setReqCritere(java.lang.String reqCritere) {
        this.reqCritere = reqCritere;
    }

}
