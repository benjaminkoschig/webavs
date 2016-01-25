package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CGEcritureListViewBean extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String beginWithLibelle = "";
    private String forCodeDebitCredit = new String();
    private String forDate = "";
    private Boolean forEstActive = null;
    private Boolean forEstProvisoire = null;
    private String forIdCentreCharge = new String();
    private String forIdCompte = new String();
    private String forIdEnteteEcriture = new String();
    private String forIdExerciceComptable = new String();
    private String forIdJournal = new String();
    private String forIdMandat = new String();
    private String forIdRemarque = new String();

    /**
     * Commentaire relatif au constructeur CGEcritureManager.
     */
    public CGEcritureListViewBean() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CGEcritureViewBean.TABLE_CGECRIP;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return CGEcritureViewBean.FIELD_IDECRITURE;
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
        if (!JadeStringUtil.isIntegerEmpty(getForIdCentreCharge())) {
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
        if (getForDate() != null && getForDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGECRIP." + "DATE<="
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

        if (!JadeStringUtil.isBlank(getForCodeDebitCredit())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CGEcritureViewBean.TABLE_CGECRIP + "."
                    + CGEcritureViewBean.FIELD_CODEDEBITCREDIT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForCodeDebitCredit());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGEcritureViewBean();
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
        CGEcritureViewBean entity = (CGEcritureViewBean) getEntity(pos);
        if (entity.isAvoir()) {
            return entity.getMontantAffiche();
        }
        return "";

    }

    public String getCreditMonnaie(int pos) {
        CGEcritureViewBean entity = (CGEcritureViewBean) getEntity(pos);
        if (entity.isAvoir()) {
            return entity.getMontantAfficheMonnaie();
        }
        return "";

    }

    public String getDebit(int pos) {
        CGEcritureViewBean entity = (CGEcritureViewBean) getEntity(pos);
        if (entity.isDoit()) {
            return entity.getMontantAffiche();
        }
        return "";
    }

    public String getDebitMonnaie(int pos) {
        CGEcritureViewBean entity = (CGEcritureViewBean) getEntity(pos);
        if (entity.isDoit()) {
            return entity.getMontantAfficheMonnaie();
        }
        return "";
    }

    public String getForCodeDebitCredit() {
        return forCodeDebitCredit;
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

    public String getForIdRemarque() {
        return forIdRemarque;
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

    public void setForCodeDebitCredit(String forCodeDebitCredit) {
        this.forCodeDebitCredit = forCodeDebitCredit;
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

    public void setForIdRemarque(String newForIdRemarque) {
        forIdRemarque = newForIdRemarque;
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
