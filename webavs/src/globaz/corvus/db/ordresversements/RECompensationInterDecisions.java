package globaz.corvus.db.ordresversements;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.prestations.REPrestations;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;

/**
 * @author SCR
 */
public class RECompensationInterDecisions extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int ALTERNATE_KEY_ID_OV = 2;
    public static final int ALTERNATE_KEY_ID_OV_COMPENSATION = 1;

    public static final String FIELDNAME_ID_COMP_INTER_DEC = "ZXICID";
    public static final String FIELDNAME_ID_ORDRE_VERSEMENT = "ZXIOVE";
    public static final String FIELDNAME_ID_OV_COMPENSATION = "ZXIOVC";
    public static final String FIELDNAME_ID_TIERS = "ZXITIE";
    public static final String FIELDNAME_MONTANT = "ZXMMON";

    public static final String TABLE_NAME_COMP_INTER_DECISION = "RECPIDEC";

    private String idCompensationInterDecision;
    private String idOrdreVersement;
    private String idOVCompensation;
    private String idTiers;
    private String montant;

    public RECompensationInterDecisions() {
        super();

        idCompensationInterDecision = "";
        idOrdreVersement = "";
        idOVCompensation = "";
        idTiers = "";
        montant = "";
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCompensationInterDecision(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return RECompensationInterDecisions.TABLE_NAME_COMP_INTER_DECISION;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idCompensationInterDecision = statement.dbReadNumeric(RECompensationInterDecisions.FIELDNAME_ID_COMP_INTER_DEC);
        idOrdreVersement = statement.dbReadNumeric(RECompensationInterDecisions.FIELDNAME_ID_ORDRE_VERSEMENT);
        idOVCompensation = statement.dbReadNumeric(RECompensationInterDecisions.FIELDNAME_ID_OV_COMPENSATION);
        idTiers = statement.dbReadNumeric(RECompensationInterDecisions.FIELDNAME_ID_TIERS);
        montant = statement.dbReadNumeric(RECompensationInterDecisions.FIELDNAME_MONTANT);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        doCheckDecisionForReset(getIdOrdreVersement());
        doCheckDecisionForReset(getIdOVCompensation());
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case ALTERNATE_KEY_ID_OV_COMPENSATION:
                statement.writeKey(RECompensationInterDecisions.FIELDNAME_ID_OV_COMPENSATION,
                        this._dbWriteNumeric(statement.getTransaction(), getIdOVCompensation(), "idOVCompensation"));
                break;
            case ALTERNATE_KEY_ID_OV:
                statement.writeKey(RECompensationInterDecisions.FIELDNAME_ID_ORDRE_VERSEMENT,
                        this._dbWriteNumeric(statement.getTransaction(), getIdOrdreVersement(), "idOrdreVersement"));
                break;

            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(RECompensationInterDecisions.FIELDNAME_ID_COMP_INTER_DEC, this._dbWriteNumeric(
                statement.getTransaction(), idCompensationInterDecision, "idCompensationInterDecision"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RECompensationInterDecisions.FIELDNAME_ID_COMP_INTER_DEC, this._dbWriteNumeric(
                statement.getTransaction(), idCompensationInterDecision, "idCompensationInterDecision"));
        statement.writeField(RECompensationInterDecisions.FIELDNAME_ID_ORDRE_VERSEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idOrdreVersement, "idOrdreVersement"));
        statement.writeField(RECompensationInterDecisions.FIELDNAME_ID_OV_COMPENSATION,
                this._dbWriteNumeric(statement.getTransaction(), idOVCompensation, "idOVCompensation"));
        statement.writeField(RECompensationInterDecisions.FIELDNAME_ID_TIERS,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(RECompensationInterDecisions.FIELDNAME_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), montant, "montant"));

    }

    /**
     * MAJ de la décision en cas de modification de l'OV. Si la décision est déjà validée, erreur.
     */
    public void doCheckDecisionForReset(String idOrdreVersement) throws Exception {

        REOrdresVersements ov = new REOrdresVersements();
        ov.setSession(getSession());
        ov.setIdOrdreVersement(idOrdreVersement);
        ov.retrieve();

        REPrestations prst = new REPrestations();
        prst.setIdPrestation(ov.getIdPrestation());
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

    public String getIdCompensationInterDecision() {
        return idCompensationInterDecision;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public String getIdOVCompensation() {
        return idOVCompensation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontant() {
        return montant;
    }

    public String getTiersDescription() {
        try {
            PRTiersWrapper tw = PRTiersHelper.getTiersParId(getSession(), getIdTiers());
            return tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " " + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        } catch (Exception e) {
            return "Unable to retrieve info for tiers id = " + getIdTiers();
        }
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setIdCompensationInterDecision(String idCompensationInterDecision) {
        this.idCompensationInterDecision = idCompensationInterDecision;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setIdOVCompensation(String idOVCompensation) {
        this.idOVCompensation = idOVCompensation;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
