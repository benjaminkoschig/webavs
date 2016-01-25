/*
 * Créé le 07 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class REFactureARestituer extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_CAT_SECTION = "ZPTCAS";
    public static final String FIELDNAME_CS_ETAT = "ZPTETA";
    public static final String FIELDNAME_CS_ROLE = "ZPTROL";
    public static final String FIELDNAME_ID_EXTERNE = "ZPLIDX";
    public static final String FIELDNAME_ID_FACTURE_A_RESTITUER = "ZPIFAC";
    public static final String FIELDNAME_ID_PASSAGE = "ZPIPAS";
    public static final String FIELDNAME_ID_RENTE_ACCORDEE = "ZPIRA";
    public static final String FIELDNAME_ID_TIERS_BENEF_PRINCIPAL = "ZPITIE";
    public static final String FIELDNAME_MONTANT_FACT_A_RESTITUER = "ZPMFAR";
    public static final String TABLE_NAME_FACTURE_A_RESTITUER = "REFACTAR";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csCatSection = "";
    private String csEtat = "";
    private String csRole = "";
    private String idExterne = "";
    private String idFactureARestituer = "";
    private String idPassage = "";
    private String idRenteAccordee = "";
    private String idTiersBenefPrincipal = "";
    private String montantFactARestituer = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * initialise la valeur de Id période api
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdFactureARestituer(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_FACTURE_A_RESTITUER;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idRenteAccordee = statement.dbReadNumeric(FIELDNAME_ID_RENTE_ACCORDEE);
        idFactureARestituer = statement.dbReadNumeric(FIELDNAME_ID_FACTURE_A_RESTITUER);
        idTiersBenefPrincipal = statement.dbReadNumeric(FIELDNAME_ID_TIERS_BENEF_PRINCIPAL);
        csCatSection = statement.dbReadNumeric(FIELDNAME_CS_CAT_SECTION);
        idExterne = statement.dbReadString(FIELDNAME_ID_EXTERNE);
        csRole = statement.dbReadNumeric(FIELDNAME_CS_ROLE);
        montantFactARestituer = statement.dbReadNumeric(FIELDNAME_MONTANT_FACT_A_RESTITUER);
        csEtat = statement.dbReadNumeric(FIELDNAME_CS_ETAT);
        idPassage = statement.dbReadNumeric(FIELDNAME_ID_PASSAGE);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
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
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_FACTURE_A_RESTITUER,
                _dbWriteNumeric(statement.getTransaction(), idFactureARestituer, "idFactureARestituer"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_RENTE_ACCORDEE,
                _dbWriteNumeric(statement.getTransaction(), idRenteAccordee, "idRenteAccordee"));
        statement.writeField(FIELDNAME_ID_FACTURE_A_RESTITUER,
                _dbWriteNumeric(statement.getTransaction(), idFactureARestituer, "idFactureARestituer"));
        statement.writeField(FIELDNAME_ID_TIERS_BENEF_PRINCIPAL,
                _dbWriteNumeric(statement.getTransaction(), idTiersBenefPrincipal, "idTiersBenefPrincipal"));
        statement.writeField(FIELDNAME_CS_CAT_SECTION,
                _dbWriteNumeric(statement.getTransaction(), csCatSection, "csCatSection"));
        statement.writeField(FIELDNAME_ID_EXTERNE, _dbWriteString(statement.getTransaction(), idExterne, "idExterne"));
        statement.writeField(FIELDNAME_CS_ROLE, _dbWriteNumeric(statement.getTransaction(), csRole, "csRole"));
        statement.writeField(FIELDNAME_MONTANT_FACT_A_RESTITUER,
                _dbWriteNumeric(statement.getTransaction(), montantFactARestituer, "montantFactARestituer"));
        statement.writeField(FIELDNAME_CS_ETAT, _dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(FIELDNAME_ID_PASSAGE, _dbWriteNumeric(statement.getTransaction(), idPassage, "idPassage"));

    }

    public String getCsCatSection() {
        return csCatSection;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsRole() {
        return csRole;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdFactureARestituer() {
        return idFactureARestituer;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTiersBenefPrincipal() {
        return idTiersBenefPrincipal;
    }

    public String getMontantFactARestituer() {
        return montantFactARestituer;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setCsCatSection(String csCatSection) {
        this.csCatSection = csCatSection;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsRole(String csRole) {
        this.csRole = csRole;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdFactureARestituer(String idFactureARestituer) {
        this.idFactureARestituer = idFactureARestituer;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdTiersBenefPrincipal(String idTiersBenefPrincipal) {
        this.idTiersBenefPrincipal = idTiersBenefPrincipal;
    }

    public void setMontantFactARestituer(String montantFactARestituer) {
        this.montantFactARestituer = montantFactARestituer;
    }
}
