/*
 * Créé le 6 sept. 07
 */
package globaz.corvus.db.parametrages;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author JJE
 * 
 */
public class ReParametrageAPI extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DEBUT = "ZKDDEB";
    public static final String FIELDNAME_DATE_FIN = "ZKDFIN";
    public static final String FIELDNAME_ID_MONTANTAPI = "ZKIMAP";
    public static final String FIELDNAME_MONTANTAPI_MAX = "ZKMMAX";
    public static final String FIELDNAME_MONTANTAPI_MIN = "ZKMMIN";
    public static final String TABLE_NAME_MONTANTAPI = "REMNTAPI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDebut = "";
    private String dateFin = "";
    private String idMontantApi = "";
    private String montantApiMax = "";
    private String montantApiMin = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdMontantApi(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_MONTANTAPI;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idMontantApi = statement.dbReadNumeric(FIELDNAME_ID_MONTANTAPI);
        dateDebut = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(FIELDNAME_DATE_DEBUT));
        dateFin = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(FIELDNAME_DATE_FIN));
        montantApiMax = statement.dbReadNumeric(FIELDNAME_MONTANTAPI_MAX);
        montantApiMin = statement.dbReadNumeric(FIELDNAME_MONTANTAPI_MIN);

    }

    /**
     * (non-Javadoc)
     * 
     * @throws Exception
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean dateDebutValide = true;
        boolean dateFinValide = true;

        if (JadeStringUtil.isBlankOrZero(getDateDebut())) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DATE_DEB_NON_RENSEIGNEE"));
        }

        if (JadeStringUtil.isBlankOrZero(getDateFin())) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DATE_FIN_NON_RENSEIGNEE"));
        }

        if (JadeStringUtil.isBlankOrZero(getMontantApiMax())) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_MNT_MAX_NON_RESEIGNE"));
        }

        if (JadeStringUtil.isBlankOrZero(getMontantApiMin())) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_MNT_MIN_NON_RENSEIGNE"));
        }
        try {
            JADate dd = new JADate(getDateDebut());
            BSessionUtil.checkDateGregorian(getSession(), dd);

        } catch (Exception e) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DATE_DEB_NON_VALIDE"));
            dateDebutValide = false;
        }

        try {
            JADate df = new JADate(getDateFin());
            BSessionUtil.checkDateGregorian(getSession(), df);

        } catch (Exception e) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DATE_FIN_NON_VALIDE"));
            dateFinValide = false;
        }

        if (dateDebutValide) {
            if (getDateDebut().length() != 7) {
                _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DATE_DEB_NON_VALIDE"));
            }
        }

        if (dateFinValide) {
            if (getDateFin().length() != 7) {
                _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DATE_FIN_NON_VALIDE"));
            }
        }
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_MONTANTAPI,
                _dbWriteNumeric(statement.getTransaction(), idMontantApi, "idMontantApi"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_MONTANTAPI,
                _dbWriteNumeric(statement.getTransaction(), idMontantApi, "idMontantApi"));
        statement.writeField(
                FIELDNAME_DATE_DEBUT,
                _dbWriteNumeric(statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebut),
                        "dateDebut"));
        statement.writeField(
                FIELDNAME_DATE_FIN,
                _dbWriteNumeric(statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFin),
                        "dateFin"));
        statement.writeField(FIELDNAME_MONTANTAPI_MAX,
                _dbWriteNumeric(statement.getTransaction(), montantApiMax, "montantApiMax"));
        statement.writeField(FIELDNAME_MONTANTAPI_MIN,
                _dbWriteNumeric(statement.getTransaction(), montantApiMin, "montantApiMin"));
    }

    /**
     * @return
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    public String getIdMontantApi() {
        return idMontantApi;
    }

    /**
     * @return
     */
    public String getMontantApiMax() {
        return montantApiMax;
    }

    /**
     * @return
     */
    public String getMontantApiMin() {
        return montantApiMin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * @param string
     */
    public void setIdMontantApi(String string) {
        idMontantApi = string;
    }

    /**
     * @param string
     */
    public void setMontantApiMax(String string) {
        montantApiMax = string;
    }

    /**
     * @param string
     */
    public void setMontantApiMin(String string) {
        montantApiMin = string;
    }

}
