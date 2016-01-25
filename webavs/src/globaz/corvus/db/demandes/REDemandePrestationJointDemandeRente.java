/*
 * Créé le 9 janv. 07
 */
package globaz.corvus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSpy;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRHierarchique;

/**
 * @author scr
 * 
 *         Jointure entre les tables des demandes de rentes, les demandes et les tiers
 * 
 */
public class REDemandePrestationJointDemandeRente extends BEntity implements PRHierarchique {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_ETAT = "YATETA";
    public static final String FIELDNAME_CS_TYPE_CALCUL = "YATTYC";
    public static final String FIELDNAME_CS_TYPE_DEMANDE_RENTE = "YATTYD";
    public static final String FIELDNAME_DATE_DEBUT = "YADDEB";
    public static final String FIELDNAME_DATE_DEPOT = "YADDEP";
    public static final String FIELDNAME_DATE_FIN = "YADFIN";
    public static final String FIELDNAME_DATE_RECEPTION = "YADREC";
    public static final String FIELDNAME_DATE_TRAITEMENT = "YADTRA";
    public static final String FIELDNAME_ID_DEMANDE_PRESTATION = "YAIMDO";
    public static final String FIELDNAME_ID_DEMANDE_RENTE = "YAIDEM";
    public static final String FIELDNAME_ID_DEMANDE_RENTE_PARENT = "YAIDPA";
    public static final String FIELDNAME_ID_RENTE_CALCULEE = "YAIRCA";

    public static final String TABLE_NAME_DEMANDE_RENTE = "REDEREN";

    public static String createFromClause(String schema) {
        StringBuilder sql = new StringBuilder();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        sql.append(schema);
        sql.append(PRDemande.TABLE_NAME);

        // jointure entre table des demandes et table des demandes de rentes
        sql.append(innerJoin);
        sql.append(schema);
        sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        sql.append(on);
        sql.append(schema);
        sql.append(PRDemande.TABLE_NAME);
        sql.append(point);
        sql.append(PRDemande.FIELDNAME_IDDEMANDE);
        sql.append(egal);
        sql.append(schema);
        sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        sql.append(point);
        sql.append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);

        return sql.toString();
    }

    private String csEtatDemande = "";
    private String csTypeCalcul = "";
    private String csTypeDemande = "";
    private String dateDebut = "";
    private String dateDepot = "";
    private String dateFin = "";
    private String dateReception = "";
    private String dateTraitement = "";
    private transient String fromClause = null;
    private String idDemandePrestation = "";
    private String idDemandeRente = "";
    private String idParent = "";
    private String idRenteCalculee = "";
    private String idTiersRequerant = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = REDemandePrestationJointDemandeRente.createFromClause(_getCollection());
        }

        return fromClause;
    }

    @Override
    protected String _getTableName() {
        return REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idDemandeRente = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        idTiersRequerant = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
        idDemandePrestation = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);
        idParent = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE_PARENT);
        dateTraitement = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_TRAITEMENT);
        dateDepot = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_DEPOT);
        dateReception = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_RECEPTION);
        csEtatDemande = statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_ETAT);
        idRenteCalculee = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        csTypeCalcul = statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_TYPE_CALCUL);
        csTypeDemande = statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE);
        dateDebut = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_FIN);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien du tout... c'est en lecture seule.
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeRente, "idDemandeRente"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    @Override
    public BSpy getCreationSpy() {

        REDemandeRente demandeRente = new REDemandeRente();

        try {
            demandeRente = REDemandeRente.loadDemandeRente(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdDemandeRente(), getCsTypeDemande());
        } catch (Exception e) {
        }

        if (demandeRente instanceof REDemandeRenteAPI) {
            REDemandeRenteAPI rente = (REDemandeRenteAPI) demandeRente;
            return rente.getCreationSpy();
        } else if (demandeRente instanceof REDemandeRenteInvalidite) {
            REDemandeRenteInvalidite rente = (REDemandeRenteInvalidite) demandeRente;
            return rente.getCreationSpy();
        } else if (demandeRente instanceof REDemandeRenteSurvivant) {
            REDemandeRenteSurvivant rente = (REDemandeRenteSurvivant) demandeRente;
            return rente.getCreationSpy();
        } else if (demandeRente instanceof REDemandeRenteVieillesse) {
            REDemandeRenteVieillesse rente = (REDemandeRenteVieillesse) demandeRente;
            return rente.getCreationSpy();
        } else {
            return null;
        }

    }

    public String getCsEtatDemande() {
        return csEtatDemande;
    }

    public String getCsTypeCalcul() {
        return csTypeCalcul;
    }

    public String getCsTypeDemande() {
        return csTypeDemande;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDepot() {
        return dateDepot;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdDemandePrestation() {
        return idDemandePrestation;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    @Override
    public String getIdMajeur() {
        return idDemandeRente;
    }

    @Override
    public String getIdParent() {
        return idParent;
    }

    public String getIdRenteCalculee() {
        return idRenteCalculee;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    @Override
    public BSpy getSpy() {
        return super.getSpy();
    }

    public void setCsEtatDemande(String string) {
        csEtatDemande = string;
    }

    public void setCsTypeCalcul(String string) {
        csTypeCalcul = string;
    }

    public void setCsTypeDemande(String string) {
        csTypeDemande = string;
    }

    public void setDateDebut(String string) {
        dateDebut = string;
    }

    public void setDateDepot(String string) {
        dateDepot = string;
    }

    public void setDateFin(String string) {
        dateFin = string;
    }

    public void setDateReception(String string) {
        dateReception = string;
    }

    public void setDateTraitement(String string) {
        dateTraitement = string;
    }

    public void setFromClause(String string) {
        fromClause = string;
    }

    public void setIdDemandePrestation(String string) {
        idDemandePrestation = string;
    }

    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    public void setIdParent(String string) {
        idParent = string;
    }

    public void setIdRenteCalculee(String string) {
        idRenteCalculee = string;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

}
