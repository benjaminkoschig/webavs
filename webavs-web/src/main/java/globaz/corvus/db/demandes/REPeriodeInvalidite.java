/*
 * Créé le 3 janv. 07
 */
package globaz.corvus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REPeriodeInvalidite extends BEntity implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_DATE_DEBUT_INVALIDITE = "YFDDDI";

    public static final String FIELDNAME_DATE_FIN_INVALIDITE = "YFDDFI";
    public static final String FIELDNAME_DEGRE_INVALIDITE = "YFNDIN";
    public static final String FIELDNAME_ID_DEMANDE_RENTE = "YFIDEM";
    // Nom des champs de la table
    public static final String FIELDNAME_ID_PERIODE_INVALIDITE = "YFIPIN";
    // Nom de la table
    public static final String TABLE_NAME = "REPEINV";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDebutInvalidite = "";
    private String dateFinInvalidite = "";
    private String degreInvalidite = "";
    private String idDemandeRente = "";
    private String idPeriodeInvalidite = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * initialise la valeur de Id période api
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdPeriodeInvalidite(_incCounter(transaction, "0"));
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
        return TABLE_NAME;
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

        idPeriodeInvalidite = statement.dbReadNumeric(FIELDNAME_ID_PERIODE_INVALIDITE);
        dateDebutInvalidite = statement.dbReadDateAMJ(FIELDNAME_DATE_DEBUT_INVALIDITE);
        dateFinInvalidite = statement.dbReadDateAMJ(FIELDNAME_DATE_FIN_INVALIDITE);
        degreInvalidite = statement.dbReadNumeric(FIELDNAME_DEGRE_INVALIDITE);
        idDemandeRente = statement.dbReadNumeric(FIELDNAME_ID_DEMANDE_RENTE);
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
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_PERIODE_INVALIDITE,
                _dbWriteNumeric(statement.getTransaction(), idPeriodeInvalidite, "idPeriodeInvalidite"));
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
        statement.writeField(FIELDNAME_ID_PERIODE_INVALIDITE,
                _dbWriteNumeric(statement.getTransaction(), idPeriodeInvalidite, "idPeriodeInvalidite"));
        statement.writeField(FIELDNAME_DATE_DEBUT_INVALIDITE,
                _dbWriteDateAMJ(statement.getTransaction(), dateDebutInvalidite, "dateDebutInvalidite"));
        statement.writeField(FIELDNAME_DATE_FIN_INVALIDITE,
                _dbWriteDateAMJ(statement.getTransaction(), dateFinInvalidite, "dateFinInvalidite"));
        statement.writeField(FIELDNAME_DEGRE_INVALIDITE,
                _dbWriteNumeric(statement.getTransaction(), degreInvalidite, "degreInvalidite"));
        statement.writeField(FIELDNAME_ID_DEMANDE_RENTE,
                _dbWriteNumeric(statement.getTransaction(), idDemandeRente, "idDemandeRente"));
    }

    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        REPeriodeInvalidite clone = new REPeriodeInvalidite();
        clone.setDateDebutInvalidite(getDateDebutInvalidite());
        clone.setDateFinInvalidite(getDateFinInvalidite());
        clone.setDegreInvalidite(getDegreInvalidite());
        clone.setIdDemandeRente(getIdDemandeRente());

        clone.wantCallValidate(false);
        return clone;

    }

    /**
     * getter pour l'attribut dateDebutInvalidite
     * 
     * @return la valeur courante de l'attribut dateDebutInvalidite
     */
    public String getDateDebutInvalidite() {
        return dateDebutInvalidite;
    }

    /**
     * getter pour l'attribut dateFinInvalidite
     * 
     * @return la valeur courante de l'attribut dateFinInvalidite
     */
    public String getDateFinInvalidite() {
        return dateFinInvalidite;
    }

    /**
     * getter pour l'attribut degreInvalidite
     * 
     * @return la valeur courante de l'attribut degreInvalidite
     */
    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    /**
     * getter pour l'attribut idDemandeRente
     * 
     * @return la valeur courante de l'attribut idDemandeRente
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * getter pour l'attribut idPeriodeInvalidite
     * 
     * @return la valeur courante de l'attribut idPeriodeInvalidite
     */
    public String getIdPeriodeInvalidite() {
        return idPeriodeInvalidite;
    }

    @Override
    public String getUniquePrimaryKey() {
        return getIdPeriodeInvalidite();
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
     * setter pour l'attribut dateDebutInvalidite.
     * 
     * @param dateDebutInvalidite
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutInvalidite(String string) {
        dateDebutInvalidite = string;
    }

    /**
     * setter pour l'attribut dateFinInvalidite.
     * 
     * @param dateFinInvalidite
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinInvalidite(String string) {
        dateFinInvalidite = string;
    }

    /**
     * setter pour l'attribut degreInvalidite.
     * 
     * @param degreInvalidite
     *            une nouvelle valeur pour cet attribut
     */
    public void setDegreInvalidite(String string) {
        degreInvalidite = string;
    }

    /**
     * setter pour l'attribut idDemandeRente.
     * 
     * @param idDemandeRente
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    /**
     * setter pour l'attribut idPeriodeInvalidite.
     * 
     * @param idPeriodeInvalidite
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPeriodeInvalidite(String string) {
        idPeriodeInvalidite = string;
    }

    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdPeriodeInvalidite(pk);

    }

}
