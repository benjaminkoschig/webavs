/*
 * Créé le 15 sept. 05
 */
package globaz.ij.db.prononces;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJMesureJointAgentExecution extends IJMesure {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_NAME);

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJAgentExecution.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_AGENT_EXECUTION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJAgentExecution.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJAgentExecution.FIELDNAME_ID_AGENT_EXECUTION);

        return fromClauseBuffer.toString();
    }

    private Boolean attestationsGroupees = Boolean.FALSE;
    private String codeIsoLangueAttestation = "";
    private String csTypeAttestation = "";
    private transient String fromClause = null;
    private String idTiers = "";

    private String nomAgentExecution = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private transient PRTiersWrapper tiers;

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);

        loadNom();
    }

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);

        IJAgentExecution agentExecution = new IJAgentExecution();

        agentExecution.setSession(getSession());
        agentExecution.setIdTiers(idTiers);
        agentExecution.setAttestationsGroupees(attestationsGroupees);
        agentExecution.setCsTypeAttestation(csTypeAttestation);
        agentExecution.setCsLangueAttestation(codeIsoLangueAttestation);
        agentExecution.add(transaction);
        setIdAgentExecution(agentExecution.getIdAgentExecution());
    }

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        super._beforeUpdate(transaction);

        IJAgentExecution agentExecution = new IJAgentExecution();

        agentExecution.setSession(getSession());
        agentExecution.setIdAgentExecution(getIdAgentExecution());
        agentExecution.retrieve(transaction);

        agentExecution.setIdTiers(idTiers);
        agentExecution.setAttestationsGroupees(attestationsGroupees);
        agentExecution.setCsTypeAttestation(csTypeAttestation);
        agentExecution.setCsLangueAttestation(codeIsoLangueAttestation);
        agentExecution.save(transaction);
    }

    /**
     * (non-Javadoc).
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(getCollection());
        }

        return fromClause;
    }

    /**
     * (non-Javadoc).
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idTiers = statement.dbReadNumeric(IJAgentExecution.FIELDNAME_ID_TIERS);
        attestationsGroupees = statement.dbReadBoolean(IJAgentExecution.FIELDNAME_ATTESTATIONS_GROUPEES);
        csTypeAttestation = statement.dbReadNumeric(IJAgentExecution.FIELDNAME_CS_TYPE_ATTESTATION);
        codeIsoLangueAttestation = statement.dbReadNumeric(IJAgentExecution.FIELDNAME_CODE_ISO_LANGUE_ATTESTATION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        super._validate(statement);
        _propertyMandatory(statement.getTransaction(), codeIsoLangueAttestation,
                getSession().getLabel("ERREUR_AGENT_EXECUTION_LANGUE_OBLIGATOIRE"));
        _propertyMandatory(statement.getTransaction(), idTiers,
                getSession().getLabel("ERREUR_AGENT_EXECUTION_TIERS_OBLIGATOIRE"));

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
    public String getCodeIsoLangueAttestation() {
        return codeIsoLangueAttestation;
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
     * getter pour l'attribut id tiers.
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut nom agent execution.
     * 
     * @return la valeur courante de l'attribut nom agent execution
     */
    public String getNomAgentExecution() {
        return nomAgentExecution;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String loadNom() throws Exception {
        if (loadTiers() != null) {
            return nomAgentExecution = loadTiers().getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + loadTiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        } else {
            return nomAgentExecution = "";
        }
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public PRTiersWrapper loadTiers() throws Exception {
        if ((tiers == null) && !JadeStringUtil.isIntegerEmpty(idTiers)) {
            tiers = PRTiersHelper.getTiersParId(getISession(), getIdTiers());

            if (tiers == null) {
                tiers = PRTiersHelper.getAdministrationParId(getISession(), getIdTiers());
            }
        }

        return tiers;
    }

    /**
     * setter pour l'attribut attestations groupees.
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setAttestationsGroupees(Boolean boolean1) {
        attestationsGroupees = boolean1;
    }

    /**
     * setter pour l'attribut code iso langue attestation.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeIsoLangueAttestation(String string) {
        codeIsoLangueAttestation = string;
    }

    /**
     * setter pour l'attribut cs type attestation.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeAttestation(String string) {
        csTypeAttestation = string;
    }

    /**
     * setter pour l'attribut id tiers.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * setter pour l'attribut nom agent execution.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNomAgentExecution(String string) {
        nomAgentExecution = string;
    }

}
