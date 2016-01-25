package globaz.corvus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BSC
 */
public class REDemandeRenteInvalidite extends REDemandeRente implements IPRCloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CODE_OFFICE_AI = "YDNTOA";
    public static final String FIELDNAME_CS_ATTEINTE = "YDTATT";
    public static final String FIELDNAME_CS_GENRE_PRONONCE_AI = "YDTGEP";
    public static final String FIELDNAME_CS_INFIRMITE = "YDTINF";
    public static final String FIELDNAME_DATE_DEBUT_RED_NON_COLLAB = "YDDDPC";
    public static final String FIELDNAME_DATE_FIN_RED_NON_COLLAB = "YDDFPC";
    public static final String FIELDNAME_DATE_PRONONCE_DERNIER_REVENU_AI = "YDDDDR";
    public static final String FIELDNAME_DATE_SURVENANCE_EVENEMENT_ASSURE = "YDDSEA";
    public static final String FIELDNAME_ID_DEMANDE_RENTE_INVALIDITE = "YDIRIN";
    public static final String FIELDNAME_NOMBRE_PAGE_MOTIVATION = "YDNNBP";
    public static final String FIELDNAME_POURCENTAGE_REDUCTION = "YDNPRE";
    public static final String FIELDNAME_RED_FAUTE_GRAVE = "YDNRFG";
    public static final String FIELDNAME_RED_NON_COLLABORATION = "YDNRNC";

    public static final String TABLE_NAME_DEMANDE_RENTE_INVALIDITE = "REDEINV";

    public static final String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(REDemandeRenteInvalidite.TABLE_NAME_DEMANDE_RENTE_INVALIDITE);

        // jointure avec la table des demandes de rentes
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClause.append(" ON ");
        fromClause.append(REDemandeRenteInvalidite.FIELDNAME_ID_DEMANDE_RENTE_INVALIDITE);
        fromClause.append("=");
        fromClause.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        return fromClause.toString();
    }

    private String codeOfficeAI = "";
    private String csAtteinte = "";
    private String csGenrePrononceAI = "";
    private String csInfirmite = "";
    private String dateDebutRedNonCollaboration = "";
    private String dateFinRedNonCollaboration = "";
    private String datePrononceDernierRevenuAI = "";
    private String dateSuvenanceEvenementAssure = "";
    private String nbPageMotivation = "";
    private String pourcentageReduction = "";
    private String pourcentRedFauteGrave = "";
    private String pourcentRedNonCollaboration = "";

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return REDemandeRenteInvalidite.createFromClause(_getCollection());
    }

    @Override
    protected String _getTableName() {
        return REDemandeRenteInvalidite.TABLE_NAME_DEMANDE_RENTE_INVALIDITE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idDemandeRente = statement.dbReadNumeric(REDemandeRenteInvalidite.FIELDNAME_ID_DEMANDE_RENTE_INVALIDITE);
        csGenrePrononceAI = statement.dbReadNumeric(REDemandeRenteInvalidite.FIELDNAME_CS_GENRE_PRONONCE_AI);
        csInfirmite = statement.dbReadNumeric(REDemandeRenteInvalidite.FIELDNAME_CS_INFIRMITE);
        csAtteinte = statement.dbReadNumeric(REDemandeRenteInvalidite.FIELDNAME_CS_ATTEINTE);
        codeOfficeAI = statement.dbReadNumeric(REDemandeRenteInvalidite.FIELDNAME_CODE_OFFICE_AI);
        pourcentageReduction = statement.dbReadNumeric(REDemandeRenteInvalidite.FIELDNAME_POURCENTAGE_REDUCTION);
        datePrononceDernierRevenuAI = statement
                .dbReadDateAMJ(REDemandeRenteInvalidite.FIELDNAME_DATE_PRONONCE_DERNIER_REVENU_AI);
        dateSuvenanceEvenementAssure = statement
                .dbReadDateAMJ(REDemandeRenteInvalidite.FIELDNAME_DATE_SURVENANCE_EVENEMENT_ASSURE);
        nbPageMotivation = statement.dbReadNumeric(REDemandeRenteInvalidite.FIELDNAME_NOMBRE_PAGE_MOTIVATION);

        pourcentRedFauteGrave = statement.dbReadNumeric(REDemandeRenteInvalidite.FIELDNAME_RED_FAUTE_GRAVE);
        pourcentRedNonCollaboration = statement.dbReadNumeric(REDemandeRenteInvalidite.FIELDNAME_RED_NON_COLLABORATION);
        dateDebutRedNonCollaboration = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(statement
                .dbReadNumeric(REDemandeRenteInvalidite.FIELDNAME_DATE_DEBUT_RED_NON_COLLAB));
        dateFinRedNonCollaboration = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(statement
                .dbReadNumeric(REDemandeRenteInvalidite.FIELDNAME_DATE_FIN_RED_NON_COLLAB));

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(REDemandeRenteInvalidite.FIELDNAME_ID_DEMANDE_RENTE_INVALIDITE,
                this._dbWriteNumeric(statement.getTransaction(), getIdDemandeRente()));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        if (_getAction() == BEntity.ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(REDemandeRenteInvalidite.FIELDNAME_ID_DEMANDE_RENTE_INVALIDITE,
                    this._dbWriteNumeric(statement.getTransaction(), getIdDemandeRente(), "idDemandeRente"));
        }

        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_CS_GENRE_PRONONCE_AI,
                this._dbWriteNumeric(statement.getTransaction(), csGenrePrononceAI, "csGenrePrononceAI"));
        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_CS_INFIRMITE,
                this._dbWriteNumeric(statement.getTransaction(), csInfirmite, "csInfirmite"));
        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_CS_ATTEINTE,
                this._dbWriteNumeric(statement.getTransaction(), csAtteinte, "csAtteinte"));
        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_CODE_OFFICE_AI,
                this._dbWriteNumeric(statement.getTransaction(), codeOfficeAI, "codeOfficeAI"));
        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_POURCENTAGE_REDUCTION,
                this._dbWriteNumeric(statement.getTransaction(), pourcentageReduction, "pourcentageReduction"));
        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_DATE_PRONONCE_DERNIER_REVENU_AI, this._dbWriteDateAMJ(
                statement.getTransaction(), datePrononceDernierRevenuAI, "datePrononceDernierRevenuAI"));
        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_DATE_SURVENANCE_EVENEMENT_ASSURE, this._dbWriteDateAMJ(
                statement.getTransaction(), dateSuvenanceEvenementAssure, "dateSuvenanceEvenementAssure"));
        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_NOMBRE_PAGE_MOTIVATION,
                this._dbWriteNumeric(statement.getTransaction(), nbPageMotivation, "nbPageMotivation"));

        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_RED_FAUTE_GRAVE,
                this._dbWriteNumeric(statement.getTransaction(), pourcentRedFauteGrave, "pourcentRedFauteGrave"));
        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_RED_NON_COLLABORATION, this._dbWriteNumeric(
                statement.getTransaction(), pourcentRedNonCollaboration, "pourcentRedNonCollaboration"));
        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_DATE_DEBUT_RED_NON_COLLAB, this._dbWriteNumeric(
                statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebutRedNonCollaboration)
                        + "00", "dateDebutRedNonCollaboration"));
        statement.writeField(REDemandeRenteInvalidite.FIELDNAME_DATE_FIN_RED_NON_COLLAB, this._dbWriteNumeric(
                statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFinRedNonCollaboration)
                        + "00", "dateFinRedNonCollaboration"));
    }

    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        REDemandeRenteInvalidite clone = new REDemandeRenteInvalidite();

        duplicateDemandeRente(clone, action);

        clone.setCsGenrePrononceAI(getCsGenrePrononceAI());
        clone.setCsInfirmite(getCsInfirmite());
        clone.setCsAtteinte(getCsAtteinte());
        clone.setCodeOfficeAI(getCodeOfficeAI());
        clone.setPourcentageReduction(getPourcentageReduction());
        clone.setNbPageMotivation(getNbPageMotivation());
        clone.setPourcentRedFauteGrave(getPourcentRedFauteGrave());
        clone.setPourcentRedNonCollaboration(getPourcentRedNonCollaboration());
        clone.setDateDebutRedNonCollaboration(getDateDebutRedNonCollaboration());
        clone.setDateFinRedNonCollaboration(getDateFinRedNonCollaboration());

        clone.setDatePrononceDernierRevenuAI(getDatePrononceDernierRevenuAI());
        clone.setDateSuvenanceEvenementAssure(getDateSuvenanceEvenementAssure());

        return clone;
    }

    public String getCodeOfficeAI() {
        return codeOfficeAI;
    }

    public String getCsAtteinte() {
        return csAtteinte;
    }

    public String getCsGenrePrononceAI() {
        return csGenrePrononceAI;
    }

    public String getCsInfirmite() {
        return csInfirmite;
    }

    public String getDateDebutRedNonCollaboration() {
        return dateDebutRedNonCollaboration;
    }

    public String getDateFinRedNonCollaboration() {
        return dateFinRedNonCollaboration;
    }

    public String getDatePrononceDernierRevenuAI() {
        return datePrononceDernierRevenuAI;
    }

    public String getDateSuvenanceEvenementAssure() {
        return dateSuvenanceEvenementAssure;
    }

    public String getNbPageMotivation() {
        return nbPageMotivation;
    }

    public List<REPeriodeInvalidite> getPeriodesInvalidite() throws Exception {
        List<REPeriodeInvalidite> result = new ArrayList<REPeriodeInvalidite>();
        REPeriodeInvaliditeManager mgr = new REPeriodeInvaliditeManager();
        mgr.setSession(getSession());
        mgr.setForIdDemandeRente(getIdDemandeRente());
        mgr.find();

        for (int i = 0; i < mgr.size(); i++) {
            REPeriodeInvalidite pi = (REPeriodeInvalidite) mgr.getEntity(i);
            result.add(pi);
        }

        return result;
    }

    public String getPourcentageReduction() {
        return pourcentageReduction;
    }

    public String getPourcentRedFauteGrave() {
        return pourcentRedFauteGrave;
    }

    public String getPourcentRedNonCollaboration() {
        return pourcentRedNonCollaboration;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCodeOfficeAI(String string) {
        codeOfficeAI = string;
    }

    public void setCsAtteinte(String string) {
        csAtteinte = string;
    }

    public void setCsGenrePrononceAI(String string) {
        csGenrePrononceAI = string;
    }

    public void setCsInfirmite(String string) {
        csInfirmite = string;
    }

    public void setDateDebutRedNonCollaboration(String dateDebutRedNonCollaboration) {
        this.dateDebutRedNonCollaboration = dateDebutRedNonCollaboration;
    }

    public void setDateFinRedNonCollaboration(String dateFinRedNonCollaboration) {
        this.dateFinRedNonCollaboration = dateFinRedNonCollaboration;
    }

    public void setDatePrononceDernierRevenuAI(String string) {
        datePrononceDernierRevenuAI = string;
    }

    public void setDateSuvenanceEvenementAssure(String dateSuvenanceEvenementAssure) {
        this.dateSuvenanceEvenementAssure = dateSuvenanceEvenementAssure;
    }

    public void setNbPageMotivation(String nbPageMotivation) {
        this.nbPageMotivation = nbPageMotivation;
    }

    public void setPourcentageReduction(String string) {
        pourcentageReduction = string;
    }

    public void setPourcentRedFauteGrave(String pourcentRedFauteGrave) {
        this.pourcentRedFauteGrave = pourcentRedFauteGrave;
    }

    public void setPourcentRedNonCollaboration(String pourcentRedNonCollaboration) {
        this.pourcentRedNonCollaboration = pourcentRedNonCollaboration;
    }
}
