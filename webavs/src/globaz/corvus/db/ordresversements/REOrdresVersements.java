package globaz.corvus.db.ordresversements;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.prestations.REPrestations;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRAssert;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;

/**
 * @author HPE
 */
public class REOrdresVersements extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_ID_RENTE_VERSEE_A_TORT = 1;

    public static final String FIELDNAME_ID_DOMAINE_APP = "YVIDOA";
    public static final String FIELDNAME_ID_EXTERNE = "YVIEXT";
    public static final String FIELDNAME_ID_ORDRE_VERSEMENT = "YVIOVE";
    public static final String FIELDNAME_ID_PRESTATION = "YVIPRE";
    public static final String FIELDNAME_ID_RENTE_ACCORDEE_COMPENSEE = "YVIRAC";
    public static final String FIELDNAME_ID_RENTE_ACCORDEE_DIMINUEE = "YVIRAD";
    public static final String FIELDNAME_ID_RENTE_VERSEE_A_TORT = "YVIRVT";
    public static final String FIELDNAME_ID_ROLE_CA = "YVIRCA";
    public static final String FIELDNAME_ID_TIERS = "YVITIE";
    public static final String FIELDNAME_ID_TIERS_ADRESSE_PMT = "YVITAP";
    public static final String FIELDNAME_IS_COMPENSATION_INTER_DECISION = "YVBCID";
    public static final String FIELDNAME_IS_COMPENSE = "YVBCMP";
    public static final String FIELDNAME_IS_VALIDE = "YVBVAL";
    public static final String FIELDNAME_MONTANT = "YVMMON";
    public static final String FIELDNAME_MONTANT_DETTE = "YVMMAC";
    public static final String FIELDNAME_NO_FACTURE = "YVLNOF";
    public static final String FIELDNAME_ROLE = "YVIROL";
    public static final String FIELDNAME_TYPE = "YVTTYP";

    public static final String TABLE_NAME_ORDRES_VERSEMENTS = "REORVER";

    private TypeOrdreVersement csTypeOrdreVersement;
    private String idDomaineApplication;
    private String idOrdreVersement;
    private String idPrestation;
    private String idRenteAccordeeACompenserParOV;
    private String idRenteAccordeeDiminueeParOV;
    private String idRenteVerseeATort;
    private String idRoleComptaAux;
    private String idSection;
    private String idTiers;
    private String idTiersAdressePmt;
    private Boolean isCompensationInterDecision;
    private Boolean isCompense;
    private Boolean isValide;
    private String montant;
    private String montantDette;
    private String noFacture;
    private String role;

    public REOrdresVersements() {
        super();

        csTypeOrdreVersement = null;
        idDomaineApplication = "";
        idOrdreVersement = "";
        idPrestation = "";
        idRenteAccordeeACompenserParOV = "";
        idRenteAccordeeDiminueeParOV = "";
        idRenteVerseeATort = "";
        idRoleComptaAux = "";
        idSection = "";
        idTiers = "";
        idTiersAdressePmt = "";
        isCompensationInterDecision = Boolean.FALSE;
        isCompense = Boolean.TRUE;
        isValide = Boolean.TRUE;
        montant = "";
        montantDette = "";
        noFacture = "";
        role = "";
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdOrdreVersement(this._incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        doCheckDecisionForReset();
    }

    @Override
    protected String _getTableName() {
        return REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        csTypeOrdreVersement = TypeOrdreVersement.parse(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_TYPE));
        idDomaineApplication = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_DOMAINE_APP);
        idOrdreVersement = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT);
        idPrestation = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_PRESTATION);
        idRenteAccordeeACompenserParOV = statement
                .dbReadNumeric(REOrdresVersements.FIELDNAME_ID_RENTE_ACCORDEE_COMPENSEE);
        idRenteAccordeeDiminueeParOV = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_RENTE_ACCORDEE_DIMINUEE);
        idRenteVerseeATort = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_RENTE_VERSEE_A_TORT);
        idRoleComptaAux = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_ROLE_CA);
        idSection = statement.dbReadString(REOrdresVersements.FIELDNAME_ID_EXTERNE);
        idTiers = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_TIERS);
        idTiersAdressePmt = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        isCompensationInterDecision = statement
                .dbReadBoolean(REOrdresVersements.FIELDNAME_IS_COMPENSATION_INTER_DECISION);
        isCompense = statement.dbReadBoolean(REOrdresVersements.FIELDNAME_IS_COMPENSE);
        isValide = statement.dbReadBoolean(REOrdresVersements.FIELDNAME_IS_VALIDE);
        montant = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_MONTANT);
        montantDette = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_MONTANT_DETTE);
        noFacture = statement.dbReadString(REOrdresVersements.FIELDNAME_NO_FACTURE);
        role = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ROLE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // MAJ de la décision en cas de modification de l'OV.
        // Si la décision est déjà validée, erreur.
        doCheckDecisionForReset();
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case ALTERNATE_KEY_ID_RENTE_VERSEE_A_TORT:
                statement.writeKey(REOrdresVersements.FIELDNAME_ID_RENTE_VERSEE_A_TORT,
                        this._dbWriteNumeric(statement.getTransaction(), idRenteVerseeATort, "idRenteVerseeATort"));
                break;

            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idOrdreVersement, "idOrdreVersement"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(REOrdresVersements.FIELDNAME_ID_DOMAINE_APP,
                this._dbWriteNumeric(statement.getTransaction(), idDomaineApplication, "idDomaineApplication"));
        statement.writeField(REOrdresVersements.FIELDNAME_ID_EXTERNE,
                this._dbWriteString(statement.getTransaction(), idSection, "idExterne"));
        statement.writeField(REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idOrdreVersement, "idOrdreVersement"));
        statement.writeField(REOrdresVersements.FIELDNAME_ID_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), idPrestation, "idPrestation"));
        statement.writeField(REOrdresVersements.FIELDNAME_ID_RENTE_ACCORDEE_COMPENSEE, this._dbWriteNumeric(
                statement.getTransaction(), idRenteAccordeeACompenserParOV, "idRenteAccordeeACompenserParOV"));
        statement.writeField(REOrdresVersements.FIELDNAME_ID_RENTE_ACCORDEE_DIMINUEE, this._dbWriteNumeric(
                statement.getTransaction(), idRenteAccordeeDiminueeParOV, "idRenteAccordeeDiminueeParOV"));
        statement.writeField(REOrdresVersements.FIELDNAME_ID_RENTE_VERSEE_A_TORT,
                this._dbWriteNumeric(statement.getTransaction(), idRenteVerseeATort, "idRenteVerseeATort"));
        statement.writeField(REOrdresVersements.FIELDNAME_ID_ROLE_CA,
                this._dbWriteNumeric(statement.getTransaction(), idRoleComptaAux, "idRoleComptaAux"));
        statement.writeField(REOrdresVersements.FIELDNAME_ID_TIERS,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(REOrdresVersements.FIELDNAME_ID_TIERS_ADRESSE_PMT,
                this._dbWriteNumeric(statement.getTransaction(), idTiersAdressePmt, "idTiersAdressePmt"));
        statement.writeField(REOrdresVersements.FIELDNAME_IS_COMPENSATION_INTER_DECISION, this._dbWriteBoolean(
                statement.getTransaction(), isCompensationInterDecision, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isCompensationInterDecision"));
        statement.writeField(REOrdresVersements.FIELDNAME_IS_COMPENSE, this._dbWriteBoolean(statement.getTransaction(),
                isCompense, BConstants.DB_TYPE_BOOLEAN_CHAR, "isCompense"));
        statement
                .writeField(REOrdresVersements.FIELDNAME_IS_VALIDE, this._dbWriteBoolean(statement.getTransaction(),
                        isValide, BConstants.DB_TYPE_BOOLEAN_CHAR, "isValide"));
        statement.writeField(REOrdresVersements.FIELDNAME_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), montant, "montant"));
        statement.writeField(REOrdresVersements.FIELDNAME_MONTANT_DETTE,
                this._dbWriteNumeric(statement.getTransaction(), montantDette, "montantDette"));
        statement.writeField(REOrdresVersements.FIELDNAME_NO_FACTURE,
                this._dbWriteString(statement.getTransaction(), noFacture, "noFacture"));
        statement.writeField(REOrdresVersements.FIELDNAME_ROLE,
                this._dbWriteNumeric(statement.getTransaction(), role, "role"));
        if (csTypeOrdreVersement != null) {
            statement.writeField(REOrdresVersements.FIELDNAME_TYPE, this._dbWriteNumeric(statement.getTransaction(),
                    csTypeOrdreVersement.getCodeSysteme().toString(), "csType"));
        }
    }

    public void doCheckDecisionForReset() throws Exception {

        REPrestations prst = new REPrestations();
        prst.setIdPrestation(getIdPrestation());
        prst.setSession(getSession());
        prst.retrieve();
        PRAssert.notIsNew(prst, null);

        REDecisionEntity decision = new REDecisionEntity();
        decision.setSession(getSession());
        decision.setIdDecision(prst.getIdDecision());
        decision.retrieve();
        PRAssert.notIsNew(decision, null);

        if (IREDecision.CS_ETAT_VALIDE.equals(decision.getCsEtat())) {
            throw new Exception("Mise à jours interdite, la décision est déjà validée. idDecision = "
                    + decision.getIdDecision());
        } else if (IREDecision.CS_ETAT_PREVALIDE.equals(decision.getCsEtat())) {
            decision.setCsEtat(IREDecision.CS_ETAT_ATTENTE);
            decision.update();
        }
    }

    public String getCsType() {
        if (csTypeOrdreVersement != null) {
            return csTypeOrdreVersement.getCodeSysteme().toString();
        }
        return "";
    }

    public TypeOrdreVersement getCsTypeOrdreVersement() {
        return csTypeOrdreVersement;
    }

    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdRenteAccordeeACompenserParOV() {
        return idRenteAccordeeACompenserParOV;
    }

    public String getIdRenteAccordeeDiminueeParOV() {
        return idRenteAccordeeDiminueeParOV;
    }

    public String getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    public String getIdRoleComptaAux() {
        return idRoleComptaAux;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePmt() {
        return idTiersAdressePmt;
    }

    public Boolean getIsCompensationInterDecision() {
        return isCompensationInterDecision;
    }

    public Boolean getIsCompense() {
        return isCompense;
    }

    public Boolean getIsValide() {
        return isValide;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantDette() {
        return montantDette;
    }

    public String getNoFacture() {
        return noFacture;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setCsType(String csType) {
        csTypeOrdreVersement = TypeOrdreVersement.parse(csType);
    }

    public void setCsTypeOrdreVersement(TypeOrdreVersement csTypeOrdreVersement) {
        this.csTypeOrdreVersement = csTypeOrdreVersement;
    }

    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdRenteAccordeeACompenserParOV(String idRenteAccordeeACompenserParOV) {
        this.idRenteAccordeeACompenserParOV = idRenteAccordeeACompenserParOV;
    }

    public void setIdRenteAccordeeDiminueeParOV(String idRenteAccordeeDiminueeParOV) {
        this.idRenteAccordeeDiminueeParOV = idRenteAccordeeDiminueeParOV;
    }

    public void setIdRenteVerseeATort(String idRenteVerseeATort) {
        this.idRenteVerseeATort = idRenteVerseeATort;
    }

    public void setIdRoleComptaAux(String idRoleComptaAux) {
        this.idRoleComptaAux = idRoleComptaAux;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdressePmt(String idTiersAdressePmt) {
        this.idTiersAdressePmt = idTiersAdressePmt;
    }

    public void setIsCompensationInterDecision(Boolean isCompensationInterDecision) {
        this.isCompensationInterDecision = isCompensationInterDecision;
    }

    public void setIsCompense(Boolean isCompense) {
        this.isCompense = isCompense;
    }

    public void setIsValide(Boolean isValide) {
        this.isValide = isValide;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantDette(String montantDette) {
        this.montantDette = montantDette;
    }

    /**
     * La taille du champs en base de données est de 255 caractères (VARCHAR)
     * Si <code>noFacture</code> dépasse 255 caractères, la valeur sera automatiquement tronquée
     * 
     * @param noFacture
     */
    public void setNoFacture(String noFacture) {
        if (noFacture != null && noFacture.length() >= 255) {
            noFacture = noFacture.substring(0, 254);
        }
        this.noFacture = noFacture;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
