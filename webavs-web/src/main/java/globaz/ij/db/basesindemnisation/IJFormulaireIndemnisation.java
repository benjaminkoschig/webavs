/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.basesindemnisation;

import globaz.globall.db.BEntity;
import globaz.ij.db.prononces.IJAgentExecution;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJFormulaireIndemnisation extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 
     */
    public static final String FIELDNAME_DATEENVOI = "XZDENV";

    /** 
     */
    public static final String FIELDNAME_DATERECEPTION = "XZDREC";

    /** 
     */
    public static final String FIELDNAME_ETAT = "XZTETA";

    /** 
     */
    public static final String FIELDNAME_IDFORMULAIREINDEMNISATION = "XZIFOI";

    /** 
     */
    public static final String FIELDNAME_IDINDEMNISATION = "XZIIND";

    /** 
     */
    public static final String FIELDNAME_IDINSTITUTIONRESPONSABLE = "XZIINR";

    /** 
     */
    public static final String FIELDNAME_NOMBRERAPPEL = "XZNNRA";

    /** 
     */
    public static final String TABLE_NAME = "IJFORIND";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient IJAgentExecution agent;
    private transient IJBaseIndemnisation base;
    private String dateEnvoi = "";
    private String dateReception = "";
    private String etat = "";
    private String idFormulaireIndemnisation = "";
    private String idIndemnisation = "";

    private String idInstitutionResponsable = "";
    private String nombreRappel = "";

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
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdFormulaireIndemnisation(_incCounter(transaction, "0"));
    }

    /**
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
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idFormulaireIndemnisation = statement.dbReadNumeric(FIELDNAME_IDFORMULAIREINDEMNISATION);
        idIndemnisation = statement.dbReadNumeric(FIELDNAME_IDINDEMNISATION);
        etat = statement.dbReadNumeric(FIELDNAME_ETAT);
        nombreRappel = statement.dbReadNumeric(FIELDNAME_NOMBRERAPPEL);
        dateEnvoi = statement.dbReadDateAMJ(FIELDNAME_DATEENVOI);
        dateReception = statement.dbReadDateAMJ(FIELDNAME_DATERECEPTION);
        idInstitutionResponsable = statement.dbReadNumeric(FIELDNAME_IDINSTITUTIONRESPONSABLE);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // il ne peut y avoir qu'un formulaire par base et par agent d'execution
        IJFormulaireIndemnisationManager formulaires = new IJFormulaireIndemnisationManager();

        formulaires.setForIdInstitutionResponsable(idInstitutionResponsable);
        formulaires.setForIdBaseIndemnisation(idIndemnisation);
        formulaires.setNotForIdFormulaireIndemnisation(getIdFormulaireIndemnisation());
        formulaires.setSession(getSession());

        if (formulaires.getCount() > 0) {
            _addError(statement.getTransaction(), getSession().getLabel("FORMULAIRE_DOUBLE"));
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
        statement.writeKey(FIELDNAME_IDFORMULAIREINDEMNISATION,
                _dbWriteNumeric(statement.getTransaction(), idFormulaireIndemnisation, "idFormulaireIndemnisation"));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_IDFORMULAIREINDEMNISATION,
                _dbWriteNumeric(statement.getTransaction(), idFormulaireIndemnisation, "idFormulaireIndemnisation"));
        statement.writeField(FIELDNAME_IDINDEMNISATION,
                _dbWriteNumeric(statement.getTransaction(), idIndemnisation, "idIndemnisation"));
        statement.writeField(FIELDNAME_ETAT, _dbWriteNumeric(statement.getTransaction(), etat, "etat"));
        statement.writeField(FIELDNAME_NOMBRERAPPEL,
                _dbWriteNumeric(statement.getTransaction(), nombreRappel, "nombreRappel"));
        statement.writeField(FIELDNAME_DATEENVOI, _dbWriteDateAMJ(statement.getTransaction(), dateEnvoi, "dateEnvoi"));
        statement.writeField(FIELDNAME_DATERECEPTION,
                _dbWriteDateAMJ(statement.getTransaction(), dateReception, "dateReception"));
        statement.writeField(FIELDNAME_IDINSTITUTIONRESPONSABLE,
                _dbWriteNumeric(statement.getTransaction(), idInstitutionResponsable, "idInstitutionResponsable"));
    }

    /**
     * getter pour l'attribut date envoi.
     * 
     * @return la valeur courante de l'attribut date envoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * getter pour l'attribut date reception.
     * 
     * @return la valeur courante de l'attribut date reception
     */
    public String getDateReception() {
        return dateReception;
    }

    /**
     * getter pour l'attribut etat.
     * 
     * @return la valeur courante de l'attribut etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * getter pour l'attribut id formulaire indemnisation.
     * 
     * @return la valeur courante de l'attribut id formulaire indemnisation
     */
    public String getIdFormulaireIndemnisation() {
        return idFormulaireIndemnisation;
    }

    /**
     * getter pour l'attribut id indemnisation.
     * 
     * @return la valeur courante de l'attribut id indemnisation
     */
    public String getIdIndemnisation() {
        return idIndemnisation;
    }

    /**
     * getter pour l'attribut id insttution responsable.
     * 
     * @return la valeur courante de l'attribut id insttution responsable
     */
    public String getIdInstitutionResponsable() {
        return idInstitutionResponsable;
    }

    /**
     * getter pour l'attribut nombre rappel.
     * 
     * @return la valeur courante de l'attribut nombre rappel
     */
    public String getNombreRappel() {
        return nombreRappel;
    }

    /** ajoute 1 au nombre de rappels. */
    public void incRappel() {
        nombreRappel = String.valueOf(JadeStringUtil.toInt(nombreRappel) + 1);
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJAgentExecution loadAgentExecution() throws Exception {
        if ((agent == null) && !JadeStringUtil.isIntegerEmpty(idInstitutionResponsable)) {
            agent = new IJAgentExecution();
            agent.setIdAgentExecution(idInstitutionResponsable);
            agent.setSession(getSession());
            agent.retrieve();
        }

        return agent;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJBaseIndemnisation loadBaseIndemnisation() throws Exception {
        if ((base == null) && !JadeStringUtil.isIntegerEmpty(idIndemnisation)) {
            base = new IJBaseIndemnisation();
            base.setIdBaseIndemisation(idIndemnisation);
            base.setSession(getSession());
            base.retrieve();
        }

        return base;
    }

    /**
     * setter pour l'attribut date envoi.
     * 
     * @param dateEnvoi
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    /**
     * setter pour l'attribut date reception.
     * 
     * @param dateReception
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    /**
     * setter pour l'attribut etat.
     * 
     * @param etat
     *            une nouvelle valeur pour cet attribut
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /**
     * setter pour l'attribut id formulaire indemnisation.
     * 
     * @param idFormulaireIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdFormulaireIndemnisation(String idFormulaireIndemnisation) {
        this.idFormulaireIndemnisation = idFormulaireIndemnisation;
    }

    /**
     * setter pour l'attribut id indemnisation.
     * 
     * @param idIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdIndemnisation(String idIndemnisation) {
        this.idIndemnisation = idIndemnisation;
    }

    /**
     * setter pour l'attribut id insttution responsable.
     * 
     * @param idInsttutionResponsable
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdInstitutionResponsable(String idInsttutionResponsable) {
        idInstitutionResponsable = idInsttutionResponsable;
    }

    /**
     * setter pour l'attribut nombre rappel.
     * 
     * @param nombreRappel
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreRappel(String nombreRappel) {
        this.nombreRappel = nombreRappel;
    }
}
