package globaz.ij.db.prononces;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.application.IJApplication;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJGrandeIJ extends IJPrononce implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_ID_PRONONCE_GRANDE_IJ = "XFIGIJ";

    /**
     */
    public static final String FIELDNAME_INDEMNITE_EXPLOITATION = "XFBINE";

    /**
     */
    public static final String FIELDNAME_MONTANT_INDEMNITE_ASSISTANCE = "XFMINA";

    /**
     */
    public static final String FIELDNAME_POURCENT_DEGRE_INCAPACITE_TRAVAIL = "XFMDIT";

    /**
     */
    public static final String TABLE_NAME_GRANDE_IJ = "IJGRANDE";

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
        fromClause.append(TABLE_NAME_GRANDE_IJ);

        // jointure avec la table des prononces
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_PRONONCE_GRANDE_IJ);
        fromClause.append("=");
        fromClause.append(FIELDNAME_ID_PRONONCE);

        return fromClause.toString();
    }

    private Boolean indemniteExploitation = Boolean.FALSE;
    private String montantIndemniteAssistance = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String pourcentDegreIncapaciteTravail = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);

        // effacement des situations professionnelles
        IJSituationProfessionnelleManager situationProfessionnelleManager = new IJSituationProfessionnelleManager();
        situationProfessionnelleManager.setSession(getSession());
        situationProfessionnelleManager.setForIdPrononce(getIdPrononce());
        situationProfessionnelleManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < situationProfessionnelleManager.size(); ++i) {
            IJSituationProfessionnelle situationProfessionnelle = (IJSituationProfessionnelle) situationProfessionnelleManager
                    .getEntity(i);
            situationProfessionnelle.delete(transaction);
        }
    }

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
        setCsTypeIJ(IIJPrononce.CS_GRANDE_IJ);
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
        return TABLE_NAME_GRANDE_IJ;
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
        idPrononce = statement.dbReadNumeric(FIELDNAME_ID_PRONONCE_GRANDE_IJ);
        montantIndemniteAssistance = statement.dbReadNumeric(FIELDNAME_MONTANT_INDEMNITE_ASSISTANCE, 2);
        indemniteExploitation = statement.dbReadBoolean(FIELDNAME_INDEMNITE_EXPLOITATION);
        pourcentDegreIncapaciteTravail = statement.dbReadNumeric(FIELDNAME_POURCENT_DEGRE_INCAPACITE_TRAVAIL, 2);
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
        statement.writeKey(FIELDNAME_ID_PRONONCE_GRANDE_IJ, getIdPrononce());
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
            statement.writeField(FIELDNAME_ID_PRONONCE_GRANDE_IJ,
                    _dbWriteNumeric(statement.getTransaction(), getIdPrononce()));
        }

        statement.writeField(FIELDNAME_MONTANT_INDEMNITE_ASSISTANCE,
                _dbWriteNumeric(statement.getTransaction(), montantIndemniteAssistance));
        statement.writeField(FIELDNAME_INDEMNITE_EXPLOITATION,
                _dbWriteBoolean(statement.getTransaction(), indemniteExploitation, BConstants.DB_TYPE_BOOLEAN_CHAR));
        statement.writeField(FIELDNAME_POURCENT_DEGRE_INCAPACITE_TRAVAIL,
                _dbWriteNumeric(statement.getTransaction(), pourcentDegreIncapaciteTravail));
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
        IJGrandeIJ clone = new IJGrandeIJ();

        duplicatePrononce(clone, action);

        clone.setMontantIndemniteAssistance(getMontantIndemniteAssistance());
        clone.setIndemniteExploitation(getIndemniteExploitation());
        clone.setPourcentDegreIncapaciteTravail(getPourcentDegreIncapaciteTravail());

        return clone;
    }

    /**
     * getter pour l'attribut indemnite exploitation
     * 
     * @return la valeur courante de l'attribut indemnite exploitation
     */
    public Boolean getIndemniteExploitation() {
        return indemniteExploitation;
    }

    /**
     * getter pour l'attribut montant indemnite assistance
     * 
     * @return la valeur courante de l'attribut montant indemnite assistance
     */
    public String getMontantIndemniteAssistance() {
        return montantIndemniteAssistance;
    }

    /**
     * getter pour l'attribut pourcent degre incapacite travail
     * 
     * @return la valeur courante de l'attribut pourcent degre incapacite travail
     */
    public String getPourcentDegreIncapaciteTravail() {
        return pourcentDegreIncapaciteTravail;
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
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public boolean is4emeRevision() throws Exception {
        String date4emeRevision = getSession().getApplication().getProperty(
                IJApplication.PROPERTY_DATE_DEBUT_4EME_REVISION);

        return BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebutPrononce(), date4emeRevision);
    }

    /**
     * setter pour l'attribut indemnite exploitation
     * 
     * @param indemniteExploitation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIndemniteExploitation(Boolean indemniteExploitation) {
        this.indemniteExploitation = indemniteExploitation;
    }

    /**
     * setter pour l'attribut montant indemnite assistance
     * 
     * @param montantIndemniteAssistance
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantIndemniteAssistance(String montantIndemniteAssistance) {
        this.montantIndemniteAssistance = montantIndemniteAssistance;
    }

    /**
     * setter pour l'attribut pourcent degre incapacite travail
     * 
     * @param pourcentDegreIncapaciteTravail
     *            une nouvelle valeur pour cet attribut
     */
    public void setPourcentDegreIncapaciteTravail(String pourcentDegreIncapaciteTravail) {
        this.pourcentDegreIncapaciteTravail = pourcentDegreIncapaciteTravail;
    }
}
