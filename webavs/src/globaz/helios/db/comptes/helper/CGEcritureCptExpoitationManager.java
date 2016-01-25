package globaz.helios.db.comptes.helper;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.helios.db.comptes.CGEcritureViewBean;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": SELECT WEBAVSI.CGECRIP.montant,
 *         WEBAVSI.CGECRIP.montantmonnaie, WEBAVSI.CGECRIP.coursmonnaie, WEBAVSI.CGECRIP.codedebitcredit,
 *         WEBAVSI.CGECRIP.referenceexterne FROM WEBAVSI.CGECRIP INNER JOIN WEBAVSI.CGCPCOP ON
 *         (WEBAVSI.CGCPCOP.IDCOMPTEOFAS=18) INNER JOIN WEBAVSI.CGCOMTP AS C ON (WEBAVSI.CGCPCOP.IDCOMPTE=C.IDCOMPTE AND
 *         WEBAVSI.CGECRIP.IDCOMPTE=C.IDCOMPTE) INNER JOIN WEBAVSI.CGJOURP AS J ON
 *         (WEBAVSI.CGECRIP.IDJOURNAL=J.IDJOURNAL) WHERE WEBAVSI.CGECRIP.IDEXERCOMPTABLE=1 AND
 *         WEBAVSI.CGECRIP.IDMANDAT=900 AND J.IDEXERCOMPTABLE=1 AND J.IDPERIODECOMPTABLE=5 AND J.IDTYPEJOURNAL<>713003
 *         OPTIMIZE FOR 1000 ROWS;
 * 
 */
public class CGEcritureCptExpoitationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCompteOfas = "";
    private String forIdExterneComptable = "";
    private String forIdMandat = "";
    private String forIdPeriodeComptable = "";
    private Boolean forIsActive = null;
    private Boolean forIsProvisoire = null;
    private String forNotIdTypeJournal = "";

    /**
     * Constructor for CGEcritureCptExpoitationManager.
     */
    public CGEcritureCptExpoitationManager() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        String fields = _getCollection() + "CGECRIP.montant, " + _getCollection() + "CGECRIP.montantmonnaie, "
                + _getCollection() + "CGECRIP.coursmonnaie, " + _getCollection() + "CGECRIP.codedebitcredit, "
                + _getCollection() + "CGECRIP.referenceexterne";
        return fields;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = _getCollection() + "CGECRIP " + "INNER JOIN " + _getCollection() + "CGCPCOP " + "ON ("
                + _getCollection() + "CGCPCOP.IDCOMPTEOFAS=" + getForIdCompteOfas() + ") " + "INNER JOIN "
                + _getCollection() + "CGCOMTP AS C " + "ON (" + _getCollection() + "CGCPCOP.IDCOMPTE=C.IDCOMPTE "
                + "AND " + _getCollection() + "CGECRIP.IDCOMPTE=C.IDCOMPTE) " + "INNER JOIN " + _getCollection()
                + "CGJOURP AS J " + "ON (" + _getCollection() + "CGECRIP.IDJOURNAL=J.IDJOURNAL)";
        return from;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = "";
        where += _getCollection() + "CGECRIP.IDEXERCOMPTABLE=" + getForIdExterneComptable() + " ";
        where += "AND " + _getCollection() + "CGECRIP.IDMANDAT=" + getForIdMandat() + " ";
        where += "AND J.IDEXERCOMPTABLE=" + getForIdExterneComptable() + " ";

        if (getForIdPeriodeComptable() != null && getForIdPeriodeComptable().length() != 0) {
            where += "AND J.IDPERIODECOMPTABLE=" + getForIdPeriodeComptable() + " ";
        }

        if (getForNotIdTypeJournal() != null && getForNotIdTypeJournal().length() != 0) {
            where += "AND J.IDTYPEJOURNAL<>" + getForNotIdTypeJournal() + " ";
        }

        if (getForIsActive() != null) {
            if (where.length() != 0) {
                where += " AND ";
            }
            where += _getCollection() + "CGECRIP." + "ESTACTIVE="
                    + _dbWriteBoolean(statement.getTransaction(), getForIsActive(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        if (getForIsProvisoire() != null) {
            if (where.length() != 0) {
                where += " AND ";
            }
            where += _getCollection()
                    + "CGECRIP."
                    + "ESTPROVISOIRE="
                    + _dbWriteBoolean(statement.getTransaction(), getForIsProvisoire(), BConstants.DB_TYPE_BOOLEAN_CHAR);
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

    /**
     * Returns the forIdCompteOfas.
     * 
     * @return String
     */
    public String getForIdCompteOfas() {
        return forIdCompteOfas;
    }

    /**
     * Returns the forIdExterneComptable.
     * 
     * @return String
     */
    public String getForIdExterneComptable() {
        return forIdExterneComptable;
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

    public Boolean getForIsProvisoire() {
        return forIsProvisoire;
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
     * Sets the forIdCompteOfas.
     * 
     * @param forIdCompteOfas
     *            The forIdCompteOfas to set
     */
    public void setForIdCompteOfas(String forIdCompteOfas) {
        this.forIdCompteOfas = forIdCompteOfas;
    }

    /**
     * Sets the forIdExterneComptable.
     * 
     * @param forIdExterneComptable
     *            The forIdExterneComptable to set
     */
    public void setForIdExterneComptable(String forIdExterneComptable) {
        this.forIdExterneComptable = forIdExterneComptable;
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

    public void setForIsProvisoire(Boolean forIsProvisoire) {
        this.forIsProvisoire = forIsProvisoire;
    }

    /**
     * Sets the forNotIdTypeJournal.
     * 
     * @param forNotIdTypeJournal
     *            The forNotIdTypeJournal to set
     */
    public void setForNotIdTypeJournal(String forIdTypeJournal) {
        forNotIdTypeJournal = forIdTypeJournal;
    }

}
