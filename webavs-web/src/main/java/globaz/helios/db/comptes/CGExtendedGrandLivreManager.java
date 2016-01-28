package globaz.helios.db.comptes;

public class CGExtendedGrandLivreManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private java.lang.String forDateDebut = new String();

    private java.lang.String forDateFin = new String();
    private java.lang.String forIdExerciceComptable = new String();
    private java.lang.String forIdListePeriodeComptable = new String();

    private java.lang.Boolean forIsActive = null;
    private java.lang.Boolean forIsProvisoire = null;
    private java.lang.String forNumeroCompteMax = new String();
    private java.lang.String forNumeroCompteMin = new String();

    /**
     * Commentaire relatif au constructeur CGCompteOfasManager.
     */
    public CGExtendedGrandLivreManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String sqlFrom = "";

        sqlFrom += _getCollection() + "CGCOMTP";

        sqlFrom += " INNER JOIN  " + _getCollection() + "CGECRIP ON  " + _getCollection() + "CGECRIP.idcompte =  "
                + _getCollection() + "CGCOMTP.idcompte";
        sqlFrom += " INNER JOIN  " + _getCollection() + "CGPLANP ON  " + _getCollection() + "CGPLANP.idCompte =  "
                + _getCollection() + "CGCOMTP.idCompte";
        sqlFrom += " INNER JOIN  " + _getCollection() + "CGEXERP ON  " + _getCollection()
                + "CGEXERP.idExerComptable =  " + _getCollection() + "CGPLANP.idExerComptable";
        sqlFrom += " AND  " + _getCollection() + "CGEXERP.idExerComptable =  " + _getCollection()
                + "CGECRIP.idExerComptable";
        sqlFrom += " INNER JOIN  " + _getCollection() + "CGJOURP ON  " + _getCollection() + "CGJOURP.idJournal =  "
                + _getCollection() + "CGECRIP.idJournal";
        sqlFrom += " INNER JOIN  " + _getCollection() + "CGECREP ON   " + _getCollection()
                + "CGECRIP.identeteecriture =  " + _getCollection() + "CGECREP.identeteecriture";
        sqlFrom += " LEFT OUTER JOIN  " + _getCollection() + "CGECRIP AS CONTRE_ECRITURE ON ";
        sqlFrom += " (( " + _getCollection() + "CGECREP.idContrepartieDoit= " + _getCollection()
                + "CGECRIP.idEcriture AND CONTRE_ECRITURE.idEcriture= " + _getCollection()
                + "CGECREP.idContrepartieAvoi)";
        sqlFrom += " OR ( " + _getCollection() + "CGECREP.idContrepartieAvoi= " + _getCollection()
                + "CGECRIP.idEcriture AND CONTRE_ECRITURE.idEcriture= " + _getCollection()
                + "CGECREP.idContrepartieDoit))";
        sqlFrom += " LEFT OUTER JOIN  " + _getCollection()
                + "CGCOMTP AS COMPTE_CONTRE_ECRI ON COMPTE_CONTRE_ECRI.idCompte = CONTRE_ECRITURE.idCompte";
        sqlFrom += " LEFT OUTER JOIN  " + _getCollection()
                + "CGPLANP AS PC_CPT_CTR_ECRIT on PC_CPT_CTR_ECRIT.idCompte = COMPTE_CONTRE_ECRI.idCompte";
        sqlFrom += "  AND PC_CPT_CTR_ECRIT.idExerComptable = " + _getCollection() + "CGEXERP.idExerComptable ";
        sqlFrom += "  AND " + _getCollection() + "CGEXERP.idExerComptable = " + _getCollection()
                + "CGEXERP.idExerComptable ";
        sqlFrom += " LEFT OUTER JOIN  " + _getCollection() + "CGCCHAP on  " + _getCollection()
                + "CGCCHAP.idCentreCharge = CONTRE_ECRITURE.idCentreCharge";
        sqlFrom += " INNER JOIN  " + _getCollection() + "CGPERIP ON  " + _getCollection()
                + "CGPERIP.idPeriodeComptable =  " + _getCollection() + "CGJOURP.idPeriodeComptable";

        return sqlFrom;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGPLANP.idExterne, " + _getCollection() + "CGECRIP.date, " + _getCollection()
                + "CGECRIP.idEcriture";

    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = _getCollection() + "CGEXERP.idExerComptable ="
                + _dbWriteNumeric(statement.getTransaction(), getForIdExerciceComptable());

        if (getForDateDebut() != null && getForDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND " + _getCollection() + "CGECRIP.date >= "
                        + _dbWriteDateAMJ(statement.getTransaction(), getForDateDebut());
            }
        }
        if (getForDateFin() != null && getForDateFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND " + _getCollection() + "CGECRIP.date <= "
                        + _dbWriteDateAMJ(statement.getTransaction(), getForDateDebut());
            }
        }

        if (getForIdListePeriodeComptable() != null && getForIdListePeriodeComptable().length() != 0
                && !"0".equals(getForIdListePeriodeComptable())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND " + _getCollection() + "CGPERIP.idPeriodeComptable in ("
                        + getForIdListePeriodeComptable() + ")";
            }
        }

        if (getForNumeroCompteMin() != null && getForNumeroCompteMin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND " + _getCollection() + "CGPLANP.idExterne >= '" + getForNumeroCompteMin() + "' ";
            }
        }
        if (getForNumeroCompteMax() != null && getForNumeroCompteMax().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND " + _getCollection() + "CGPLANP.idExterne <= '" + getForNumeroCompteMax() + "' ";
            }
        }

        if (isForIsProvisoire() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND " + _getCollection() + "CGECRIP.ESTPROVISOIRE = '"
                        + _dbWriteBoolean(statement.getTransaction(), isForIsProvisoire()) + "' ";
            }
        }

        if (getForIsActive() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND " + _getCollection() + "CGECRIP.ESTACTIVE = '"
                        + _dbWriteBoolean(statement.getTransaction(), getForIsActive()) + "' ";
            }
        }

        return sqlWhere;
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGExtendedGrandLivre();
    }

    /**
     * Returns the forDateMin.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * Returns the forDateMax.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDateFin() {
        return forDateFin;
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
     * Returns the forIsActive.
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getForIsActive() {
        return forIsActive;
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
     * Returns the forIsProvisoire.
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isForIsProvisoire() {
        return forIsProvisoire;
    }

    /**
     * Sets the forDateMax.
     * 
     * @param forDateMax
     *            The forDateMax to set
     */
    public void setForDateFin(java.lang.String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * Sets the forDateMin.
     * 
     * @param forDateMin
     *            The forDateMin to set
     */
    public void setForDateMin(java.lang.String forDateDebut) {
        this.forDateDebut = forDateDebut;
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

}
