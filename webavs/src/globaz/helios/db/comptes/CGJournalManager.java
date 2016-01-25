package globaz.helios.db.comptes;

import globaz.jade.client.util.JadeStringUtil;

public class CGJournalManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String exceptIdEtat = "";
    private String exceptIdJournal = "";
    private String exceptIdTypeJournal = "";
    private String forDate = "";
    private String forDateValeur = "";
    private String forIdEtat = "";
    private String forIdExerciceComptable = "";
    private String forIdPeriodeComptable = "";
    private String forIdTypeJournal = "";

    private String forProprietaire = "";

    private String forReferenceExterne;
    private String fromDate = "";
    private String fromLibelle = "";
    private String fromNumero = "";
    private String fromProprietaire = "";
    private String orderBy = "";
    private String untilDate = "";
    private String untilLibelle = "";

    private String untilNumero = "";

    private String untilProprietaire = "";

    /**
     * Getter
     */

    /**
     * Setter
     */

    /**
     * Commentaire relatif au constructeur CGModeleEcritureManager.
     */
    public CGJournalManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CGJournal.TABLE_NAME;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return orderBy;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForIdExerciceComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_IDEXERCOMPTABLE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdExerciceComptable());
        }

        if (!JadeStringUtil.isBlank(getForIdPeriodeComptable())) {
            if (!"0".equals(getForIdPeriodeComptable())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += CGJournal.FIELD_IDPERIODECOMPTABLE + "="
                        + _dbWriteNumeric(statement.getTransaction(), getForIdPeriodeComptable());
            }
        }

        if (getForIdEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_IDETAT + "=" + _dbWriteNumeric(statement.getTransaction(), getForIdEtat());
        }

        if (!JadeStringUtil.isBlank(getExceptIdEtat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_IDETAT + "<>" + _dbWriteNumeric(statement.getTransaction(), getExceptIdEtat());
        }

        if (getForIdTypeJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_IDTYPEJOURNAL + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTypeJournal());
        }

        if (getExceptIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_IDJOURNAL + "<>"
                    + _dbWriteNumeric(statement.getTransaction(), getExceptIdJournal());
        }

        if (getExceptIdTypeJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_IDTYPEJOURNAL + "<>"
                    + _dbWriteNumeric(statement.getTransaction(), getExceptIdTypeJournal());
        }

        if (getFromDate() != null && getFromDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_DATE + ">=" + _dbWriteDateAMJ(statement.getTransaction(), getFromDate());
        }

        if (getUntilDate() != null && getUntilDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_DATE + "<=" + _dbWriteDateAMJ(statement.getTransaction(), getUntilDate());
        }

        if (getFromLibelle() != null && getFromLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_LIBELLE + ">=" + _dbWriteString(statement.getTransaction(), getFromLibelle());
        }

        if (getUntilLibelle() != null && getUntilLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_LIBELLE + "<=" + _dbWriteString(statement.getTransaction(), getUntilLibelle());
        }

        if (getFromNumero() != null && getFromNumero().length() != 0 && getFromNumero().indexOf(".") == -1) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_NUMERO + ">=" + _dbWriteNumeric(statement.getTransaction(), getFromNumero());
        }

        if (getUntilNumero() != null && getUntilNumero().length() != 0 && getUntilNumero().indexOf(".") == -1) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_NUMERO + "<=" + _dbWriteNumeric(statement.getTransaction(), getUntilNumero());
        }

        if (getForDateValeur() != null && getForDateValeur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_DATEVALEUR + "="
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateValeur());
        }

        if (getForDate() != null && getForDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_DATE + "=" + _dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }

        if (!JadeStringUtil.isBlank(getFromProprietaire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_PROPRIETAIRE + ">="
                    + _dbWriteString(statement.getTransaction(), getFromProprietaire());
        }

        if (!JadeStringUtil.isBlank(getUntilProprietaire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_PROPRIETAIRE + "<="
                    + _dbWriteString(statement.getTransaction(), getUntilProprietaire());
        }

        if (!JadeStringUtil.isBlank(getForProprietaire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_PROPRIETAIRE + "="
                    + _dbWriteString(statement.getTransaction(), getForProprietaire());
        }

        if (!JadeStringUtil.isBlank(getForReferenceExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGJournal.FIELD_REFERENCEEXTERNE + "="
                    + _dbWriteString(statement.getTransaction(), getForReferenceExterne());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGJournal();
    }

    public String getExceptIdEtat() {
        return exceptIdEtat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 16:51:49)
     * 
     * @return String
     */
    public String getExceptIdJournal() {
        return exceptIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 16:31:56)
     * 
     * @return String
     */
    public String getExceptIdTypeJournal() {
        return exceptIdTypeJournal;
    }

    public String getForDate() {
        return forDate;
    }

    /**
     * Returns the forDateValeur.
     * 
     * @return String
     */
    public String getForDateValeur() {
        return forDateValeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 16:46:18)
     * 
     * @return String
     */
    public String getForIdEtat() {
        return forIdEtat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.11.2002 17:12:34)
     * 
     * @return String
     */
    public String getForIdExerciceComptable() {
        return forIdExerciceComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.12.2002 10:09:08)
     * 
     * @return String
     */
    public String getForIdPeriodeComptable() {
        return forIdPeriodeComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 15:58:39)
     * 
     * @return String
     */
    public String getForIdTypeJournal() {
        return forIdTypeJournal;
    }

    public String getForProprietaire() {
        return forProprietaire;
    }

    public String getForReferenceExterne() {
        return forReferenceExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 11:30:13)
     * 
     * @return String
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 11:32:18)
     * 
     * @return String
     */
    public String getFromLibelle() {
        return fromLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 11:30:28)
     * 
     * @return String
     */
    public String getFromNumero() {
        return fromNumero;
    }

    public String getFromProprietaire() {
        return fromProprietaire;
    }

    /**
     * Returns the untilDate.
     * 
     * @return String
     */
    public String getUntilDate() {
        return untilDate;
    }

    /**
     * Returns the untilLibelle.
     * 
     * @return String
     */
    public String getUntilLibelle() {
        return untilLibelle;
    }

    /**
     * Returns the untilNumero.
     * 
     * @return String
     */
    public String getUntilNumero() {
        return untilNumero;
    }

    public String getUntilProprietaire() {
        return untilProprietaire;
    }

    public void setExceptIdEtat(String exceptIdEtat) {
        this.exceptIdEtat = exceptIdEtat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 16:51:49)
     * 
     * @param newExceptIdJournal
     *            String
     */
    public void setExceptIdJournal(String newExceptIdJournal) {
        exceptIdJournal = newExceptIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 16:31:56)
     * 
     * @param newExceptIdTypeJournal
     *            String
     */
    public void setExceptIdTypeJournal(String newExceptIdTypeJournal) {
        exceptIdTypeJournal = newExceptIdTypeJournal;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    /**
     * Sets the forDateValeur.
     * 
     * @param forDateValeur
     *            The forDateValeur to set
     */
    public void setForDateValeur(String forDateValeur) {
        this.forDateValeur = forDateValeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 16:46:18)
     * 
     * @param newForIdEtat
     *            String
     */
    public void setForIdEtat(String newForIdEtat) {
        forIdEtat = newForIdEtat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.11.2002 17:12:34)
     * 
     * @param newForIdExerciceComptable
     *            String
     */
    public void setForIdExerciceComptable(String newForIdExerciceComptable) {
        forIdExerciceComptable = newForIdExerciceComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.12.2002 10:09:08)
     * 
     * @param newForIdPeriodeComptable
     *            String
     */
    public void setForIdPeriodeComptable(String newForIdPeriodeComptable) {
        forIdPeriodeComptable = newForIdPeriodeComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2003 15:58:39)
     * 
     * @param newForIdTypeJournal
     *            String
     */
    public void setForIdTypeJournal(String newForIdTypeJournal) {
        forIdTypeJournal = newForIdTypeJournal;
    }

    public void setForProprietaire(String forProprietaire) {
        this.forProprietaire = forProprietaire;
    }

    public void setForReferenceExterne(String forReferenceExterne) {
        this.forReferenceExterne = forReferenceExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 11:30:13)
     * 
     * @param newFromDate
     *            String
     */
    public void setFromDate(String newFromDate) {
        fromDate = newFromDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 11:32:18)
     * 
     * @param newFromLibelle
     *            String
     */
    public void setFromLibelle(String newFromLibelle) {
        fromLibelle = newFromLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 11:30:28)
     * 
     * @param newFromNumero
     *            String
     */
    public void setFromNumero(String newFromNumero) {
        fromNumero = newFromNumero;
    }

    public void setFromProprietaire(String fromProprietaire) {
        this.fromProprietaire = fromProprietaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.10.2002 16:50:07)
     * 
     * @param newOrderby
     *            String
     */
    public void setOrderby(String newOrderby) {
        orderBy = newOrderby;
    }

    /**
     * Sets the untilDate.
     * 
     * @param untilDate
     *            The untilDate to set
     */
    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }

    /**
     * Sets the untilLibelle.
     * 
     * @param untilLibelle
     *            The untilLibelle to set
     */
    public void setUntilLibelle(String untilLibelle) {
        this.untilLibelle = untilLibelle;
    }

    /**
     * Sets the untilNumero.
     * 
     * @param untilNumero
     *            The untilNumero to set
     */
    public void setUntilNumero(String untilNumero) {
        this.untilNumero = untilNumero;
    }

    public void setUntilProprietaire(String untipProprietaire) {
        untilProprietaire = untipProprietaire;
    }

}
