/*
 * Créé le 29 dec. 06
 */
package globaz.corvus.db.demandes;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.clone.factory.IPRCloneable;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REDemandeRenteAPI extends REDemandeRente implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_CODE_OFFICE_AI = "YENTOA";

    public static final String FIELDNAME_CS_ATTEINTE = "YETATT";
    public static final String FIELDNAME_CS_GENRE_PRONONCE_AI = "YETGPR";
    public static final String FIELDNAME_CS_INFIRMITE = "YETINF";
    public static final String FIELDNAME_DATE_PRONONCE_DERNIER_REVENU_AI = "YEDDDR";
    public static final String FIELDNAME_DATE_SURVENANCE_EVENEMENT_ASSURE = "YEDSEA";
    // Nom des champs de la table
    public static final String FIELDNAME_ID_DEMANDE_RENTE_API = "YEIRAP";
    public static final String FIELDNAME_NOMBRE_PAGE_MOTIVATION = "YENNBP";
    // Nom de la table
    public static final String TABLE_NAME_DEMANDE_RENTE_API = "REDEAPI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME_DEMANDE_RENTE_API);

        // jointure avec la table des demandes de rentes
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_NAME_DEMANDE_RENTE);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_DEMANDE_RENTE_API);
        fromClause.append("=");
        fromClause.append(FIELDNAME_ID_DEMANDE_RENTE);

        return fromClause.toString();
    }

    private String codeOfficeAI = "";
    private String csAtteinte = "";
    private String csGenreDroitAPI = "";
    private String csGenrePrononceAI = "";
    private String csInfirmite = "";

    private String datePrononceDernierRevenuAI = "";
    private String dateSuvenanceEvenementAssure = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String nbPageMotivation = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_DEMANDE_RENTE_API;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idDemandeRente = statement.dbReadNumeric(FIELDNAME_ID_DEMANDE_RENTE_API);
        csGenrePrononceAI = statement.dbReadNumeric(FIELDNAME_CS_GENRE_PRONONCE_AI);
        csInfirmite = statement.dbReadNumeric(FIELDNAME_CS_INFIRMITE);
        csAtteinte = statement.dbReadNumeric(FIELDNAME_CS_ATTEINTE);
        codeOfficeAI = statement.dbReadNumeric(FIELDNAME_CODE_OFFICE_AI);

        datePrononceDernierRevenuAI = statement.dbReadDateAMJ(FIELDNAME_DATE_PRONONCE_DERNIER_REVENU_AI);
        dateSuvenanceEvenementAssure = statement.dbReadDateAMJ(FIELDNAME_DATE_SURVENANCE_EVENEMENT_ASSURE);
        nbPageMotivation = statement.dbReadNumeric(FIELDNAME_NOMBRE_PAGE_MOTIVATION);
    }

    /**
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
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_DEMANDE_RENTE_API,
                _dbWriteNumeric(statement.getTransaction(), getIdDemandeRente()));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(FIELDNAME_ID_DEMANDE_RENTE_API,
                    _dbWriteNumeric(statement.getTransaction(), getIdDemandeRente(), "idDemandeRente"));
        }

        statement.writeField(FIELDNAME_CS_GENRE_PRONONCE_AI,
                _dbWriteNumeric(statement.getTransaction(), csGenrePrononceAI, "csGenrePrononceAI"));
        statement.writeField(FIELDNAME_CS_INFIRMITE,
                _dbWriteNumeric(statement.getTransaction(), csInfirmite, "csInfirmite"));
        statement.writeField(FIELDNAME_CS_ATTEINTE,
                _dbWriteNumeric(statement.getTransaction(), csAtteinte, "csAtteinte"));
        statement.writeField(FIELDNAME_CODE_OFFICE_AI,
                _dbWriteNumeric(statement.getTransaction(), codeOfficeAI, "codeOfficeAI"));

        statement
                .writeField(
                        FIELDNAME_DATE_PRONONCE_DERNIER_REVENU_AI,
                        _dbWriteDateAMJ(statement.getTransaction(), datePrononceDernierRevenuAI,
                                "datePrononceDernierRevenuAI"));
        statement.writeField(
                FIELDNAME_DATE_SURVENANCE_EVENEMENT_ASSURE,
                _dbWriteDateAMJ(statement.getTransaction(), dateSuvenanceEvenementAssure,
                        "dateSuvenanceEvenementAssure"));
        statement.writeField(FIELDNAME_NOMBRE_PAGE_MOTIVATION,
                _dbWriteNumeric(statement.getTransaction(), nbPageMotivation, "nbPageMotivation"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        REDemandeRenteAPI clone = new REDemandeRenteAPI();

        duplicateDemandeRente(clone, action);

        clone.setCsGenrePrononceAI(getCsGenrePrononceAI());
        clone.setCsInfirmite(getCsInfirmite());
        clone.setCsAtteinte(getCsAtteinte());
        clone.setCodeOfficeAI(getCodeOfficeAI());
        clone.setDatePrononceDernierRevenuAI(getDatePrononceDernierRevenuAI());
        clone.setDateSuvenanceEvenementAssure(getDateSuvenanceEvenementAssure());
        clone.setNbPageMotivation(getNbPageMotivation());

        return clone;
    }

    /**
     * getter pour l'attribut codeOfficeAI
     * 
     * @return la valeur courante de l'attribut codeOfficeAI
     */
    public String getCodeOfficeAI() {
        return codeOfficeAI;
    }

    /**
     * getter pour l'attribut csAtteinte
     * 
     * @return la valeur courante de l'attribut csAtteinte
     */
    public String getCsAtteinte() {
        return csAtteinte;
    }

    /**
     * getter pour l'attribut csGenrePrononceAI
     * 
     * @return la valeur courante de l'attribut csAtteinte
     */
    public String getCsGenrePrononceAI() {
        return csGenrePrononceAI;
    }

    /**
     * getter pour l'attribut csInfirmite
     * 
     * @return la valeur courante de l'attribut csInfirmite
     */
    public String getCsInfirmite() {
        return csInfirmite;
    }

    /**
     * getter pour l'attribut datePrononceDernierRevenuAI
     * 
     * @return la valeur courante de l'attribut datePrononceDernierRevenuAI
     */
    public String getDatePrononceDernierRevenuAI() {
        return datePrononceDernierRevenuAI;
    }

    public String getDateSuvenanceEvenementAssure() {
        return dateSuvenanceEvenementAssure;
    }

    public String getNbPageMotivation() {
        return nbPageMotivation;
    }

    public Iterator getPeriodesAPIIterator() throws Exception {
        REPeriodeAPIManager mgr = new REPeriodeAPIManager();
        mgr.setSession(getSession());
        mgr.setForIdDemandeRente(getIdDemandeRente());
        mgr.find(BManager.SIZE_NOLIMIT);
        return mgr.iterator();
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
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * setter pour l'attribut codeOfficeAI.
     * 
     * @param codeOfficeAI
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeOfficeAI(String string) {
        codeOfficeAI = string;
    }

    /**
     * setter pour l'attribut csAtteinte.
     * 
     * @param csAtteinte
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsAtteinte(String string) {
        csAtteinte = string;
    }

    /**
     * setter pour l'attribut csGenrePrononceAI.
     * 
     * @param csGenrePrononceAI
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsGenrePrononceAI(String string) {
        csGenrePrononceAI = string;
    }

    /**
     * setter pour l'attribut csInfirmite.
     * 
     * @param csInfirmite
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsInfirmite(String string) {
        csInfirmite = string;
    }

    /**
     * setter pour l'attribut datePrononceDernierRevenuAI.
     * 
     * @param datePrononceDernierRevenuAI
     *            une nouvelle valeur pour cet attribut
     */
    public void setDatePrononceDernierRevenuAI(String string) {
        datePrononceDernierRevenuAI = string;
    }

    public void setDateSuvenanceEvenementAssure(String dateSuvenanceEvenementAssure) {
        this.dateSuvenanceEvenementAssure = dateSuvenanceEvenementAssure;
    }

    public void setNbPageMotivation(String nbPageMotivation) {
        this.nbPageMotivation = nbPageMotivation;
    }

}
