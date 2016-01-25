package globaz.helios.db.avs;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;

public class CGExtendedContrePartieCpteAffManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdExerciceComptable = new String();
    private java.lang.String forIdMandat = new String();
    private java.lang.String forIdPeriodeComptable = new String();
    private java.lang.Boolean forIsActive = null;
    private java.lang.Boolean forIsProvisoire = new Boolean(false);

    /**
     * Commentaire relatif au constructeur CGCompteOfasManager.
     */
    public CGExtendedContrePartieCpteAffManager() {
        super();
    }

    private String _buildJoin(boolean doit) {
        String sqlFrom = _getCollection() + "CGPERIP";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGJOURP on " + _getCollection()
                + "CGPERIP.idPeriodeComptable = " + _getCollection() + "CGJOURP.idPeriodeComptable";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGECRIP on  " + _getCollection() + "CGJOURP.idjournal = "
                + _getCollection() + "CGECRIP.idjournal";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGCOMTP on  " + _getCollection() + "CGECRIP.idcompte = "
                + _getCollection() + "CGCOMTP.idcompte";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGECREP on  " + _getCollection()
                + "CGECRIP.identeteecriture = " + _getCollection() + "CGECREP.identeteecriture";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGECRIP AS CONTRE_ECRITURE on ";
        if (doit) {
            sqlFrom += " (" + _getCollection() + "CGECREP.idContrepartieDoit=" + _getCollection()
                    + "CGECRIP.idEcriture AND CONTRE_ECRITURE.idEcriture=" + _getCollection()
                    + "CGECREP.idContrepartieAvoi)";
        } else {
            sqlFrom += " (" + _getCollection() + "CGECREP.idContrepartieAvoi=" + _getCollection()
                    + "CGECRIP.idEcriture AND CONTRE_ECRITURE.idEcriture=" + _getCollection()
                    + "CGECREP.idContrepartieDoit)";
        }
        sqlFrom += " INNER JOIN " + _getCollection()
                + "CGCOMTP AS CPT_CONTRE_ECRIT on CPT_CONTRE_ECRIT.idCompte = CONTRE_ECRITURE.idCompte";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGCPCOP on " + _getCollection()
                + "CGCPCOP.idCompte = CONTRE_ECRITURE.idCompte";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGOFCPP on " + _getCollection() + "CGOFCPP.idCompteOfas = "
                + _getCollection() + "CGCPCOP.idCompteOfas";
        sqlFrom += " INNER JOIN " + _getCollection() + "CGSECTP on CPT_CONTRE_ECRIT.idSecteurAVS = " + _getCollection()
                + "CGSECTP.idSecteurAVS ";
        return sqlFrom;

    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    private String _buildWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "(" + _getCollection() + "CGCOMTP.numerocompteavs <= 1109 AND " + _getCollection()
                + "CGCOMTP.numerocompteavs >= 1100) ";
        sqlWhere += " AND CPT_CONTRE_ECRIT.numerocompteavs <> 9200";
        sqlWhere += " AND CPT_CONTRE_ECRIT.numerocompteavs <> 9210";
        sqlWhere += " AND " + _getCollection() + "CGSECTP.idMandat="
                + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());

        if (getForIdPeriodeComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + "CGPERIP.IDPERIODECOMPTABLE ="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdPeriodeComptable());
        }

        if (getForIdExerciceComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + "CGPERIP.IDEXERCOMPTABLE ="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdExerciceComptable());
        }

        if (getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + "CGSECTP.IDMANDAT ="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        if (getForIsProvisoire() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection()
                    + "CGECRIP."
                    + "ESTPROVISOIRE="
                    + _dbWriteBoolean(statement.getTransaction(), getForIsProvisoire(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        if (getForIsActive() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "ESTACTIVE="
                    + _dbWriteBoolean(statement.getTransaction(), getForIsActive(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        sqlWhere += " GROUP BY ";
        sqlWhere += _getCollection() + "CGOFCPP.idexterne, ";
        sqlWhere += _getCollection() + "CGOFCPP.libellefr, ";
        sqlWhere += _getCollection() + "CGOFCPP.libellede, ";
        sqlWhere += _getCollection() + "CGOFCPP.libelleit ";

        return sqlWhere;
    }

    @Override
    protected String _getFields(BStatement statement) {
        return "idexterne, sum (montant) as montant, libellefr, libellede, libelleit";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String fields = super._getFields(statement);
        String from = "SELECT " + fields + " FROM ";
        String where = _buildWhere(statement);

        // Création de la partie gauche de l'union avec le doit
        String from1 = from + _buildJoin(true) + " WHERE " + where;
        String from2 = from + _buildJoin(false) + " WHERE " + where;
        String union = "(" + from1 + " UNION " + from2 + ") AS TMP";
        union += " GROUP BY idexterne,libellefr,libellede,libelleit";
        union += " ORDER BY idexterne";
        return union;

    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";

    }

    @Override
    protected String _getWhere(BStatement statement) {
        return "";
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGExtendedContrePartieCpteAff();
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
     * Returns the forIdMandat.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Returns the forIdPeriodeComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdPeriodeComptable() {
        return forIdPeriodeComptable;
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
     * Sets the forIdExerciceComptable.
     * 
     * @param forIdExerciceComptable
     *            The forIdExerciceComptable to set
     */
    public void setForIdExerciceComptable(java.lang.String forIdExerciceComptable) {
        this.forIdExerciceComptable = forIdExerciceComptable;
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
     * Sets the forIdPeriodeComptable.
     * 
     * @param forIdPeriodeComptable
     *            The forIdPeriodeComptable to set
     */
    public void setForIdPeriodeComptable(java.lang.String forIdPeriodeComptable) {
        this.forIdPeriodeComptable = forIdPeriodeComptable;
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

}
