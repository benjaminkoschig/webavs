package globaz.ij.db.prononces;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisation;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisationManager;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJAgentExecution extends BEntity implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_ATTESTATIONS_GROUPEES = "XEBAGR";

    public static final String FIELDNAME_CODE_ISO_LANGUE_ATTESTATION = "XETCIL";
    public static final String FIELDNAME_CS_TYPE_ATTESTATION = "XETTAT";
    public static final String FIELDNAME_ID_AGENT_EXECUTION = "XEIAGE";
    public static final String FIELDNAME_ID_TIERS = "XEITIE";
    public static final String TABLE_NAME = "IJAGEXEC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean attestationsGroupees = Boolean.FALSE;
    private String csLangueAttestation = "";
    private String csTypeAttestation = "";
    private String idAgentExecution = "";
    private String idTiers = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // effacer les formulaires d'indemnisations
        IJFormulaireIndemnisationManager formulaires = new IJFormulaireIndemnisationManager();

        formulaires.setForIdInstitutionResponsable(getIdAgentExecution());
        formulaires.setSession(getSession());
        formulaires.find();

        for (int idFormulaire = 0; idFormulaire < formulaires.size(); ++idFormulaire) {
            ((IJFormulaireIndemnisation) formulaires.get(idFormulaire)).delete(transaction);
        }
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idAgentExecution = _incCounter(transaction, "0");
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
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
        idAgentExecution = statement.dbReadNumeric(FIELDNAME_ID_AGENT_EXECUTION);
        idTiers = statement.dbReadNumeric(FIELDNAME_ID_TIERS);
        attestationsGroupees = statement.dbReadBoolean(FIELDNAME_ATTESTATIONS_GROUPEES);
        csTypeAttestation = statement.dbReadNumeric(FIELDNAME_CS_TYPE_ATTESTATION);
        csLangueAttestation = statement.dbReadNumeric(FIELDNAME_CODE_ISO_LANGUE_ATTESTATION);
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
        // TODO Auto-generated method stub
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
        statement.writeKey(FIELDNAME_ID_AGENT_EXECUTION, idAgentExecution);
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
        statement.writeField(FIELDNAME_ID_AGENT_EXECUTION,
                _dbWriteNumeric(statement.getTransaction(), idAgentExecution, "idAgentExecution"));
        statement.writeField(FIELDNAME_ID_TIERS, _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(
                FIELDNAME_ATTESTATIONS_GROUPEES,
                _dbWriteBoolean(statement.getTransaction(), attestationsGroupees, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "attestationsGroupees"));
        statement.writeField(FIELDNAME_CS_TYPE_ATTESTATION,
                _dbWriteNumeric(statement.getTransaction(), csTypeAttestation, "csTypeAttestation"));
        statement.writeField(FIELDNAME_CODE_ISO_LANGUE_ATTESTATION,
                _dbWriteNumeric(statement.getTransaction(), csLangueAttestation, "csLangueAttestation"));
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
        IJAgentExecution clone = new IJAgentExecution();

        clone.setIdTiers(getIdTiers());
        clone.setAttestationsGroupees(getAttestationsGroupees());
        clone.setCsTypeAttestation(getCsTypeAttestation());
        clone.setCsLangueAttestation(getCsLangueAttestation());

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);

        return clone;
    }

    /**
     * getter pour l'attribut attestations groupees.
     * 
     * @return la valeur courante de l'attribut attestations groupees
     */
    public Boolean getAttestationsGroupees() {
        return attestationsGroupees;
    }

    /**
     * getter pour l'attribut code iso langue attestation.
     * 
     * @return la valeur courante de l'attribut code iso langue attestation
     */
    public String getCsLangueAttestation() {
        return csLangueAttestation;
    }

    /**
     * getter pour l'attribut cs type attestation.
     * 
     * @return la valeur courante de l'attribut cs type attestation
     */
    public String getCsTypeAttestation() {
        return csTypeAttestation;
    }

    /**
     * getter pour l'attribut id agent execution.
     * 
     * @return la valeur courante de l'attribut id agent execution
     */
    public String getIdAgentExecution() {
        return idAgentExecution;
    }

    /**
     * getter pour l'attribut id tiers.
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut unique primary key.
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdAgentExecution();
    }

    /**
     * setter pour l'attribut attestations groupees.
     * 
     * @param attestationsGroupees
     *            une nouvelle valeur pour cet attribut
     */
    public void setAttestationsGroupees(Boolean attestationsGroupees) {
        this.attestationsGroupees = attestationsGroupees;
    }

    /**
     * setter pour l'attribut code iso langue attestation.
     * 
     * @param codeIsoLangueAttestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsLangueAttestation(String codeIsoLangueAttestation) {
        csLangueAttestation = codeIsoLangueAttestation;
    }

    /**
     * setter pour l'attribut cs type attestation.
     * 
     * @param csTypeAttestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeAttestation(String csTypeAttestation) {
        this.csTypeAttestation = csTypeAttestation;
    }

    /**
     * setter pour l'attribut id agent execution.
     * 
     * @param idAgentExecution
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAgentExecution(String idAgentExecution) {
        this.idAgentExecution = idAgentExecution;
    }

    /**
     * setter pour l'attribut id tiers.
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * setter pour l'attribut unique primary key.
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdAgentExecution(pk);
    }
}
