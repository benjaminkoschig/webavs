package globaz.osiris.db.utils;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;

/**
 * @author sel Créé le : 22 nov. 06
 */
public class CACumulOperationCotisationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ALIAS_A = "a";
    private static final String ALIAS_B = "b";
    protected static final String AND = " AND ";
    protected static final String AS = " AS ";
    protected static final String BETWEEN = " BETWEEN ";
    protected static final String FROM = " FROM ";
    protected static final String GROUP_BY = " GROUP BY ";
    private static final String LABEL_COMPTE_ANNEXE_NON_RENSEIGNE = "5106";
    private static final String LABEL_DATE_DEBUT_NON_RENSEIGNE = "7085";
    private static final String LABEL_DATE_FIN_NON_RENSEIGNE = "7086";
    private static final String LABEL_OPERATION_NON_RENSEIGNE = "5005";
    private static final String LABEL_RUBRIQUE_NON_RENSEIGNE = "5114";

    protected static final String SELECT = "SELECT ";
    protected static final String WHERE = " WHERE ";

    /**
     * Cumul des opérations comptabilisées pour un compte annexe, une rubrique, un type d'opération et une période
     * donnée.
     * 
     * @author: sel Créé le : 22 nov. 06
     * @param session
     * @param transaction
     * @param idCompteAnnexe
     * @param idRubrique
     * @param typeOperation
     * @param dateDebut
     *            au format yyyymmdd
     * @param dateFin
     *            au format yyyymmdd
     * @param dateRefOperation
     *            true : prend la date de l'opération, false : prend la date du journal.
     * @return le CumulOperation ou ""
     */
    public static String getCumulOperation(BSession session, BTransaction trans, String idCompteAnnexe,
            String idRubrique, String typeOperation, String dateDebut, String dateFin, boolean dateRefOperation) {

        String resultat = "";
        CACumulOperationCotisationManager manager = new CACumulOperationCotisationManager();
        manager.setSession(session);

        manager.setForCompteAnnexe(idCompteAnnexe);
        manager.setForRubrique(idRubrique);
        manager.setForTypeOperation(typeOperation);

        manager.setDateReferenceOperation(dateRefOperation);
        manager.setDateDebut(dateDebut);
        manager.setDateFin(dateFin);

        try {
            manager.find(trans);
        } catch (Exception e) {
            JadeCodingUtil.catchException(null, "getCumulOperation()", e);
        }

        if (manager.size() > 0) {
            resultat = ((CACumulOperationCotisation) manager.getFirstEntity()).getCumulOperation();
        }

        return resultat;
    }

    private String dateDebut;
    private String dateFin;

    private boolean dateReferenceOperation = false;
    private String forCompteAnnexe;
    private String forRubrique;

    private String forTypeOperation;

    public CACumulOperationCotisationManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        super._beforeFind(transaction);

        if (JadeStringUtil.isIntegerEmpty(getForCompteAnnexe())) {
            _addError(transaction,
                    getSession().getLabel(CACumulOperationCotisationManager.LABEL_COMPTE_ANNEXE_NON_RENSEIGNE));
        }
        if (JadeStringUtil.isIntegerEmpty(getForRubrique())) {
            _addError(transaction, getSession()
                    .getLabel(CACumulOperationCotisationManager.LABEL_RUBRIQUE_NON_RENSEIGNE));
        }
        if (JadeStringUtil.isEmpty(getForTypeOperation())) {
            _addError(transaction,
                    getSession().getLabel(CACumulOperationCotisationManager.LABEL_OPERATION_NON_RENSEIGNE));
        }
        if (JadeStringUtil.isEmpty(getDateDebut())) {
            _addError(transaction,
                    getSession().getLabel(CACumulOperationCotisationManager.LABEL_DATE_DEBUT_NON_RENSEIGNE));
        }
        if (JadeStringUtil.isEmpty(getDateFin())) {
            _addError(transaction, getSession()
                    .getLabel(CACumulOperationCotisationManager.LABEL_DATE_FIN_NON_RENSEIGNE));
        }
    }

    /**
     * @return
     */
    protected String _getFields() {
        StringBuffer sql = new StringBuffer("");
        sql.append(CACumulOperationCotisationManager.ALIAS_A + "." + CAOperation.FIELD_IDCOMPTEANNEXE);
        sql.append(", ");
        sql.append(CACumulOperationCotisationManager.ALIAS_A + "." + CAOperation.FIELD_IDCOMPTE
                + CACumulOperationCotisationManager.AS + CACumulOperationCotisation.IDRUBRIQUE);
        sql.append(", ");
        sql.append(CACumulOperationCotisationManager.ALIAS_A + "." + CAOperation.FIELD_IDTYPEOPERATION);
        sql.append(", ");
        sql.append("SUM(" + CACumulOperationCotisationManager.ALIAS_A + "." + CAOperation.FIELD_MONTANT + ") AS "
                + CACumulOperationCotisation.CUMUL_OPERATION);
        return sql.toString();
    }

    /**
     * @return
     */
    protected String _getFrom() {
        StringBuffer from = new StringBuffer("");
        from.append(_getCollection());
        from.append(CAOperation.TABLE_CAOPERP);
        from.append(" ");
        from.append(CACumulOperationCotisationManager.ALIAS_A);
        from.append(", ");
        from.append(_getCollection());
        from.append(CAJournal.TABLE_CAJOURP);
        from.append(" ");
        from.append(CACumulOperationCotisationManager.ALIAS_B);
        return from.toString();
    }

    /**
     * @return
     */
    protected String _getOrderBy() {
        StringBuffer sql = new StringBuffer("");
        sql.append(CACumulOperationCotisationManager.ALIAS_A);
        sql.append(".");
        sql.append(CAOperation.FIELD_IDCOMPTEANNEXE);
        sql.append(", ");
        sql.append(CACumulOperationCotisationManager.ALIAS_A + "." + CAOperation.FIELD_IDCOMPTE);
        sql.append(", ");
        sql.append(CACumulOperationCotisationManager.ALIAS_A + "." + CAOperation.FIELD_IDTYPEOPERATION);
        return sql.toString();
    }

    /**
     * Surcharge :
     * 
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     * @param statement
     * @return requete SQL
     */
    @Override
    protected String _getSql(BStatement statement) {
        StringBuffer sql = new StringBuffer("");
        sql.append(CACumulOperationCotisationManager.SELECT);
        sql.append(this._getFields());
        sql.append(CACumulOperationCotisationManager.FROM);
        sql.append(this._getFrom());
        sql.append(CACumulOperationCotisationManager.WHERE);
        sql.append(this._getWhere());
        sql.append(CACumulOperationCotisationManager.GROUP_BY);
        sql.append(_getOrderBy());
        return sql.toString();
    }

    /**
     * @return
     */
    protected String _getWhere() {
        StringBuffer sql = new StringBuffer("");
        String dateRef = "";
        if (isDateReferenceOperation()) {
            dateRef = CACumulOperationCotisationManager.ALIAS_A + "." + CAOperation.FIELD_DATE;
        } else {
            dateRef = CACumulOperationCotisationManager.ALIAS_B + "." + CAJournal.FIELD_DATE;
        }

        sql.append(CACumulOperationCotisationManager.ALIAS_A + "." + CAOperation.FIELD_IDJOURNAL + " = "
                + CACumulOperationCotisationManager.ALIAS_B + "." + CAJournal.FIELD_IDJOURNAL);
        sql.append(CACumulOperationCotisationManager.AND);
        sql.append(CACumulOperationCotisationManager.ALIAS_A + "." + CAOperation.FIELD_IDCOMPTEANNEXE + " = "
                + getForCompteAnnexe());
        sql.append(CACumulOperationCotisationManager.AND);
        sql.append(CACumulOperationCotisationManager.ALIAS_A + "." + CAOperation.FIELD_IDCOMPTE + " = "
                + getForRubrique());
        sql.append(CACumulOperationCotisationManager.AND);
        sql.append(CACumulOperationCotisationManager.ALIAS_A + "." + CAOperation.FIELD_IDTYPEOPERATION + " = '"
                + getForTypeOperation().toUpperCase() + "'");
        sql.append(CACumulOperationCotisationManager.AND);
        sql.append(CACumulOperationCotisationManager.ALIAS_B + "." + CAJournal.FIELD_ETAT + " = "
                + CAJournal.COMPTABILISE);
        sql.append(CACumulOperationCotisationManager.AND);
        sql.append(dateRef + CACumulOperationCotisationManager.BETWEEN + getDateDebut()
                + CACumulOperationCotisationManager.AND + getDateFin());
        return sql.toString();
    }

    /**
     * Surcharge :
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     * @return
     * @throws Exception
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CACumulOperationCotisation();
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the DateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the forCompteAnnexe
     */
    public String getForCompteAnnexe() {
        return forCompteAnnexe;
    }

    /**
     * @return the forRubrique
     */
    public String getForRubrique() {
        return forRubrique;
    }

    /**
     * @return the forTypeOperation
     */
    public String getForTypeOperation() {
        return forTypeOperation;
    }

    /**
     * @return the dateReferenceOperation
     */
    public boolean isDateReferenceOperation() {
        return dateReferenceOperation;
    }

    /**
     * @param DateDebut
     *            the DateDebut to set au format yyyymmdd
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param DateFin
     *            the DateFin to set au format yyyymmdd
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param dateReferenceOperation
     *            the dateReferenceOperation to set
     */
    public void setDateReferenceOperation(boolean dateReferenceOperation) {
        this.dateReferenceOperation = dateReferenceOperation;
    }

    /**
     * @param forCompteAnnexe
     *            the forCompteAnnexe to set
     */
    public void setForCompteAnnexe(String forCompteAnnexe) {
        this.forCompteAnnexe = forCompteAnnexe;
    }

    /**
     * @param forRubrique
     *            the forRubrique to set
     */
    public void setForRubrique(String forRubrique) {
        this.forRubrique = forRubrique;
    }

    /**
     * @param forTypeOperation
     *            the forTypeOperation to set
     */
    public void setForTypeOperation(String forTypeOperation) {
        this.forTypeOperation = forTypeOperation;
    }

}
