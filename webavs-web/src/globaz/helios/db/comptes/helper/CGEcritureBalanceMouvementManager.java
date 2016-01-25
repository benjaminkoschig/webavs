package globaz.helios.db.comptes.helper;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.helios.db.comptes.CGEcritureViewBean;

/**
 * @author user
 * 
 *         SELECT WEBAVSI.CGECRIP.montant, WEBAVSI.CGECRIP.montantmonnaie, WEBAVSI.CGECRIP.coursmonnaie,
 *         WEBAVSI.CGECRIP.codedebitcredit FROM WEBAVSI.CGECRIP INNER JOIN WEBAVSI.CGJOURP AS J ON
 *         (J.IDJOURNAL=WEBAVSI.CGECRIP.IDJOURNAL AND J.IDEXERCOMPTABLE=1) INNER JOIN WEBAVSI.CGPERIP AS M ON
 *         (J.IDPERIODECOMPTABLE = M.IDPERIODECOMPTABLE) INNER JOIN WEBAVSI.CGPERIP AS E ON (E.IDPERIODECOMPTABLE=5 AND
 *         M.DATEFIN<=E.DATEFIN) WHERE J.IDTYPEJOURNAL<>713003 AND M.IDEXERCOMPTABLE=1 AND M.IDTYPEPERIODE<>709005 AND
 *         WEBAVSI.CGECRIP.IDCOMPTE=72 AND WEBAVSI.CGECRIP.IDMANDAT=900 OPTIMIZE FOR 1000 ROWS;
 */
public class CGEcritureBalanceMouvementManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String forIdCompte = "";
    String forIdExerciceComptable = "";
    String forIdMandat = "";
    String forIdPeriodeComptable = "";
    Boolean forIsActive = null;
    String forNotIdTypeJournal = "";
    String forNotIdTypePeriode = "";

    /**
     * Constructor for CGEcritureBalanceMouvementManager.
     */
    public CGEcritureBalanceMouvementManager() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        String field = _getCollection() + "CGECRIP.montant, " + _getCollection() + "CGECRIP.montantmonnaie, "
                + _getCollection() + "CGECRIP.coursmonnaie, " + _getCollection() + "CGECRIP.codedebitcredit";
        return field;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = _getCollection() + "CGECRIP ";
        from += "INNER JOIN " + _getCollection() + "CGJOURP AS J " + "ON (J.IDJOURNAL=" + _getCollection()
                + "CGECRIP.IDJOURNAL " + "AND J.IDEXERCOMPTABLE=" + getForIdExerciceComptable() + ") ";
        from += "INNER JOIN " + _getCollection() + "CGPERIP AS M "
                + "ON (J.IDPERIODECOMPTABLE = M.IDPERIODECOMPTABLE) ";
        from += "INNER JOIN " + _getCollection() + "CGPERIP AS E " + "ON (E.IDPERIODECOMPTABLE="
                + getForIdPeriodeComptable() + " " + "AND M.DATEFIN<=E.DATEFIN)";
        return from;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = "J.IDTYPEJOURNAL<>" + getForNotIdTypeJournal();
        where += " AND M.IDEXERCOMPTABLE=" + getForIdExerciceComptable();
        where += " AND M.IDTYPEPERIODE<>" + getForNotIdTypePeriode();
        where += " AND " + _getCollection() + "CGECRIP.IDCOMPTE=" + getForIdCompte();
        where += " AND " + _getCollection() + "CGECRIP.IDMANDAT=" + getForIdMandat();

        if (getForIsActive() != null) {
            if (where.length() != 0) {
                where += " AND ";
            }
            where += _getCollection() + "CGECRIP." + "ESTACTIVE="
                    + _dbWriteBoolean(statement.getTransaction(), getForIsActive(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        return where;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGEcritureViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getSql(BStatement)
     */

    /**
     * Returns the forIdCompte.
     * 
     * @return String
     */
    public String getForIdCompte() {
        return forIdCompte;
    }

    /**
     * Returns the forIdExerciceComptable.
     * 
     * @return String
     */
    public String getForIdExerciceComptable() {
        return forIdExerciceComptable;
    }

    /**
     * Returns the forIdMandat.
     * 
     * @return String
     */
    public String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Returns the forIdPeriodeComptable.
     * 
     * @return String
     */
    public String getForIdPeriodeComptable() {
        return forIdPeriodeComptable;
    }

    /**
     * Returns the forIsActive.
     * 
     * @return Boolean
     */
    public Boolean getForIsActive() {
        return forIsActive;
    }

    /**
     * Returns the forNotIdTypeJournal.
     * 
     * @return String
     */
    public String getForNotIdTypeJournal() {
        return forNotIdTypeJournal;
    }

    /**
     * Returns the forNotIdTypePeriode.
     * 
     * @return String
     */
    public String getForNotIdTypePeriode() {
        return forNotIdTypePeriode;
    }

    /**
     * Sets the forIdCompte.
     * 
     * @param forIdCompte
     *            The forIdCompte to set
     */
    public void setForIdCompte(String forIdCompte) {
        this.forIdCompte = forIdCompte;
    }

    /**
     * Sets the forIdExerciceComptable.
     * 
     * @param forIdExerciceComptable
     *            The forIdExerciceComptable to set
     */
    public void setForIdExerciceComptable(String forIdExerciceComptable) {
        this.forIdExerciceComptable = forIdExerciceComptable;
    }

    /**
     * Sets the forIdMandat.
     * 
     * @param forIdMandat
     *            The forIdMandat to set
     */
    public void setForIdMandat(String forIdMandat) {
        this.forIdMandat = forIdMandat;
    }

    /**
     * Sets the forIdPeriodeComptable.
     * 
     * @param forIdPeriodeComptable
     *            The forIdPeriodeComptable to set
     */
    public void setForIdPeriodeComptable(String forIdPeriodeComptable) {
        this.forIdPeriodeComptable = forIdPeriodeComptable;
    }

    /**
     * Sets the forIsActive.
     * 
     * @param forIsActive
     *            The forIsActive to set
     */
    public void setForIsActive(Boolean forIsActive) {
        this.forIsActive = forIsActive;
    }

    /**
     * Sets the forNotIdTypeJournal.
     * 
     * @param forNotIdTypeJournal
     *            The forNotIdTypeJournal to set
     */
    public void setForNotIdTypeJournal(String forNotIdTypeJournal) {
        this.forNotIdTypeJournal = forNotIdTypeJournal;
    }

    /**
     * Sets the forNotIdTypePeriode.
     * 
     * @param forNotIdTypePeriode
     *            The forNotIdTypePeriode to set
     */
    public void setForNotIdTypePeriode(String forNotIdTypePeriode) {
        this.forNotIdTypePeriode = forNotIdTypePeriode;
    }

}
