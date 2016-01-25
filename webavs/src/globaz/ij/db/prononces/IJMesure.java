package globaz.ij.db.prononces;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJMesure extends BEntity implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 
     */
    public static final String FIELDNAME_DATE_DEBUT = "XDDDEB";

    /** 
     */
    public static final String FIELDNAME_DATE_FIN = "XDDFIN";

    /** 
     */
    public static final String FIELDNAME_ID_AGENT_EXECUTION = "XDIAGE";

    /** 
     */
    public static final String FIELDNAME_ID_MESURE = "XDIMES";

    /** 
     */
    public static final String FIELDNAME_ID_PRONONCE = "XDIPAI";

    /** 
     */
    public static final String TABLE_NAME = "IJMESURE";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient IJAgentExecution agentExecution = null;
    private String dateDebut = "";
    private String dateFin = "";
    private String idAgentExecution = "";
    private String idMesure = "";

    private String idPrononce = "";
    private transient IJPrononce prononce;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);

        if (loadAgentExecution() != null) {
            loadAgentExecution().delete(transaction);
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
        idMesure = _incCounter(transaction, "0");
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
        idMesure = statement.dbReadNumeric(FIELDNAME_ID_MESURE);
        dateDebut = statement.dbReadDateAMJ(FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(FIELDNAME_DATE_FIN);
        idAgentExecution = statement.dbReadNumeric(FIELDNAME_ID_AGENT_EXECUTION);
        idPrononce = statement.dbReadNumeric(FIELDNAME_ID_PRONONCE);
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
        statement.writeKey(FIELDNAME_ID_MESURE, idMesure);
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
        statement.writeField(FIELDNAME_ID_MESURE, _dbWriteNumeric(statement.getTransaction(), idMesure));
        statement.writeField(FIELDNAME_DATE_DEBUT, _dbWriteDateAMJ(statement.getTransaction(), dateDebut));
        statement.writeField(FIELDNAME_DATE_FIN, _dbWriteDateAMJ(statement.getTransaction(), dateFin));
        statement.writeField(FIELDNAME_ID_AGENT_EXECUTION,
                _dbWriteNumeric(statement.getTransaction(), idAgentExecution));
        statement.writeField(FIELDNAME_ID_PRONONCE, idPrononce);
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
        IJMesure clone = new IJMesure();

        clone.setDateDebut(getDateDebut());
        clone.setDateFin(getDateFin());
        clone.setIdAgentExecution(getIdAgentExecution());
        clone.setIdPrononce(getIdPrononce());

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);

        return clone;
    }

    /**
     * getter pour l'attribut date debut.
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin.
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin() {
        return dateFin;
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
     * getter pour l'attribut id mesure.
     * 
     * @return la valeur courante de l'attribut id mesure
     */
    public String getIdMesure() {
        return idMesure;
    }

    /**
     * getter pour l'attribut id prononce.
     * 
     * @return la valeur courante de l'attribut id prononce
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    /**
     * getter pour l'attribut unique primary key.
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdMesure();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJAgentExecution loadAgentExecution() throws Exception {
        if ((agentExecution == null) && !JadeStringUtil.isIntegerEmpty(idAgentExecution)) {
            agentExecution = new IJAgentExecution();
            agentExecution.setSession(getSession());
            agentExecution.setIdAgentExecution(idAgentExecution);
            agentExecution.retrieve();
        }

        return agentExecution;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJPrononce loadPrononce(BITransaction transaction) throws Exception {
        if ((prononce == null) && !JadeStringUtil.isIntegerEmpty(idPrononce)) {
            prononce = IJPrononce.loadPrononce(getSession(), transaction, getIdPrononce(), null); // deux
            // requetes
            // necessaires
        }

        return prononce;
    }

    /**
     * setter pour l'attribut date debut.
     * 
     * @param dateDebut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date fin.
     * 
     * @param dateFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
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
     * setter pour l'attribut id mesure.
     * 
     * @param idMesure
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdMesure(String idMesure) {
        this.idMesure = idMesure;
    }

    /**
     * setter pour l'attribut id prononce.
     * 
     * @param idPrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    /**
     * setter pour l'attribut unique primary key.
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdMesure(pk);
    }
}
