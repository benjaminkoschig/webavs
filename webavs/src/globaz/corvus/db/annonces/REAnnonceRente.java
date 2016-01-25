/*
 * Créé le 22 août 07
 */
package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * Table de mapping entre les annonces et les rentes accordees.
 * 
 * @author BSC
 */

public class REAnnonceRente extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_ETAT = "ZMTETA";
    public static final String FIELDNAME_CS_TRAITEMENT = "ZMTTRA";
    public static final String FIELDNAME_ID_ANNONCE_HEADER = "ZMIANH";
    public static final String FIELDNAME_ID_ANNONCE_RENTE = "ZMIANR";
    public static final String FIELDNAME_ID_DECISION = "ZMIDEC";
    public static final String FIELDNAME_ID_RENTE_ACCORDEE = "ZMIRAC";
    public static final String TABLE_NAME_ANNONCE_RENTE = "REANREN";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtat = "";
    private String csTraitement = "";
    private String idAnnonceHeader = "";
    private String idAnnonceRente = "";
    private String idDecision = "";
    private String idRenteAccordee = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * initialise la valeur de Id annonce rente
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdAnnonceRente(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_ANNONCE_RENTE;
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
        idAnnonceRente = statement.dbReadNumeric(FIELDNAME_ID_ANNONCE_RENTE);
        idAnnonceHeader = statement.dbReadNumeric(FIELDNAME_ID_ANNONCE_HEADER);
        idRenteAccordee = statement.dbReadNumeric(FIELDNAME_ID_RENTE_ACCORDEE);
        csTraitement = statement.dbReadNumeric(FIELDNAME_CS_TRAITEMENT);
        csEtat = statement.dbReadNumeric(FIELDNAME_CS_ETAT);
        idDecision = statement.dbReadNumeric(FIELDNAME_ID_DECISION);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

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
        statement.writeKey(FIELDNAME_ID_ANNONCE_RENTE,
                _dbWriteNumeric(statement.getTransaction(), idAnnonceRente, "idAnnonce"));
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
        statement.writeField(FIELDNAME_ID_ANNONCE_RENTE,
                _dbWriteNumeric(statement.getTransaction(), idAnnonceRente, "idAnnonceRente"));
        statement.writeField(FIELDNAME_ID_ANNONCE_HEADER,
                _dbWriteNumeric(statement.getTransaction(), idAnnonceHeader, "idAnnonceHeader"));
        statement.writeField(FIELDNAME_ID_RENTE_ACCORDEE,
                _dbWriteNumeric(statement.getTransaction(), idRenteAccordee, "idRenteAccordee"));
        statement.writeField(FIELDNAME_CS_TRAITEMENT,
                _dbWriteNumeric(statement.getTransaction(), csTraitement, "csTraitement"));
        statement.writeField(FIELDNAME_CS_ETAT, _dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(FIELDNAME_ID_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));

    }

    /**
     * @return
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return
     */
    public String getCsTraitement() {
        return csTraitement;
    }

    /**
     * @return
     */
    public String getIdAnnonceHeader() {
        return idAnnonceHeader;
    }

    /**
     * @return
     */
    public String getIdAnnonceRente() {
        return idAnnonceRente;
    }

    public String getIdDecision() {
        return idDecision;
    }

    /**
     * @return
     */
    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * @param string
     */
    public void setCsEtat(String string) {
        csEtat = string;
    }

    /**
     * @param string
     */
    public void setCsTraitement(String string) {
        csTraitement = string;
    }

    /**
     * @param string
     */
    public void setIdAnnonceHeader(String string) {
        idAnnonceHeader = string;
    }

    /**
     * @param string
     */
    public void setIdAnnonceRente(String string) {
        idAnnonceRente = string;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * @param string
     */
    public void setIdRenteAccordee(String string) {
        idRenteAccordee = string;
    }

}
