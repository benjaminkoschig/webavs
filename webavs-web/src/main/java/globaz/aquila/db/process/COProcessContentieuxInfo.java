package globaz.aquila.db.process;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CARole;
import globaz.osiris.db.comptes.CASection;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COProcessContentieuxInfo extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -8524757642882359785L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private CARole caRole;
    private String categorieSection;
    private CACompteAnnexe compteAnnexe;
    private String dateDebutPeriodeSection;
    private String dateEcheance;
    private String dateFinPeriodeSection;
    private String dateSection;
    private String descriptionCA;
    private String idCompteAnnexe;
    private String idExterneRoleCA;

    private String idExterneSection;
    private String idRoleCA;
    private String idSection;
    private String idSequenceContentieux;
    private String idTiersCA;
    private String idTypeSection;

    private String roleDescriptionCA;

    private CASection section;
    private String solde;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe COProcessContentieuxInfo.
     */
    public COProcessContentieuxInfo() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return false
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @return false
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @return false
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSection = statement.dbReadNumeric(CASection.FIELD_IDSECTION);
        idCompteAnnexe = statement.dbReadNumeric(CASection.FIELD_IDCOMPTEANNEXE);
        idTypeSection = statement.dbReadNumeric(CASection.FIELD_IDTYPESECTION);
        dateEcheance = statement.dbReadDateAMJ(CASection.FIELD_DATEECHEANCE);
        solde = statement.dbReadNumeric(CASection.FIELD_SOLDE);
        dateSection = statement.dbReadDateAMJ(CASection.FIELD_DATESECTION);
        idExterneSection = statement.dbReadString(CASection.FIELD_IDEXTERNE);
        categorieSection = statement.dbReadNumeric(CASection.FIELD_CATEGORIESECTION);
        dateDebutPeriodeSection = statement.dbReadDateAMJ(CASection.FIELD_DATEDEBUTPERIODE);
        dateFinPeriodeSection = statement.dbReadDateAMJ(CASection.FIELD_DATEFINPERIODE);

        descriptionCA = statement.dbReadString(CACompteAnnexe.FIELD_DESCRIPTION);
        idExterneRoleCA = statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        idTiersCA = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDTIERS);
        idRoleCA = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDROLE);
        if (getCARole(getIdRoleCA()) != null) {
            roleDescriptionCA = getCARole(getIdRoleCA()).getDescription();
        } else {
            roleDescriptionCA = "";
        }

        idSequenceContentieux = statement.dbReadNumeric(CASection.FIELD_IDSEQCON);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    protected CARole getCARole(String idRole) {
        caRole = new CARole();
        caRole.setISession(getSession());
        caRole.setIdRole(idRole);
        try {
            caRole.retrieve();
            if (caRole.isNew()) {
                caRole = null;
            }

        } catch (Exception e) {
            caRole = null;
        }

        return caRole;
    }

    /**
     * getter pour l'attribut categorie section.
     * 
     * @return la valeur courante de l'attribut categorie section
     */
    public String getCategorieSection() {
        return categorieSection;
    }

    /**
     * Retrouve le compte annxe correspondant.
     */
    public CACompteAnnexe getCompteAnnexe() throws Exception {
        if (compteAnnexe == null) {
            compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setSession(getSession());
            compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());
            compteAnnexe.retrieve();

            if (compteAnnexe.hasErrors() || compteAnnexe.isNew()) {
                // TODO dda : 31 oct. 2008 add label
                throw new Exception("Compteannexe non résolu");
            }
        }

        return compteAnnexe;
    }

    /**
     * getter pour l'attribut date debut periode section.
     * 
     * @return la valeur courante de l'attribut date debut periode section
     */
    public String getDateDebutPeriodeSection() {
        return dateDebutPeriodeSection;
    }

    /**
     * getter pour l'attribut date echeance.
     * 
     * @return la valeur courante de l'attribut date echeance
     */
    public String getDateEcheance() {
        return dateEcheance;
    }

    /**
     * getter pour l'attribut date fin periode section.
     * 
     * @return la valeur courante de l'attribut date fin periode section
     */
    public String getDateFinPeriodeSection() {
        return dateFinPeriodeSection;
    }

    /**
     * getter pour l'attribut date section.
     * 
     * @return la valeur courante de l'attribut date section
     */
    public String getDateSection() {
        return dateSection;
    }

    /**
     * getter pour l'attribut description CA.
     * 
     * @return la valeur courante de l'attribut description CA
     */
    public String getDescriptionCA() {
        return descriptionCA;
    }

    /**
     * getter pour l'attribut id compte annexe.
     * 
     * @return la valeur courante de l'attribut id compte annexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * getter pour l'attribut id externe role CA.
     * 
     * @return la valeur courante de l'attribut id externe role CA
     */
    public String getIdExterneRoleCA() {
        return idExterneRoleCA;
    }

    /**
     * getter pour l'attribut id externe section.
     * 
     * @return la valeur courante de l'attribut id externe section
     */
    public String getIdExterneSection() {
        return idExterneSection;
    }

    public String getIdRoleCA() {
        return idRoleCA;
    }

    /**
     * getter pour l'attribut id section.
     * 
     * @return la valeur courante de l'attribut id section
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * getter pour l'attribut id sequence aquila.
     * 
     * @return la valeur courante de l'attribut id sequence aquila
     */
    public String getIdSequenceContentieux() {
        return idSequenceContentieux;
    }

    /**
     * getter pour l'attribut id tiers CA.
     * 
     * @return la valeur courante de l'attribut id tiers CA
     */
    public String getIdTiersCA() {
        return idTiersCA;
    }

    /**
     * getter pour l'attribut id type section.
     * 
     * @return la valeur courante de l'attribut id type section
     */
    public String getIdTypeSection() {
        return idTypeSection;
    }

    public String getRoleDescriptionCA() {
        return roleDescriptionCA;
    }

    /**
     * Retrouve la section correspondante.
     */
    public CASection getSection() throws Exception {
        if (section == null) {
            section = new CASection();
            section.setSession(getSession());
            section.setIdSection(getIdSection());
            section.retrieve();

            if (section.hasErrors() || section.isNew()) {
                // TODO dda : 31 oct. 2008 add label
                throw new Exception("section non résolue");
            }
        }

        return section;
    }

    /**
     * getter pour l'attribut solde.
     * 
     * @return la valeur courante de l'attribut solde
     */
    public String getSolde() {
        return solde;
    }

    /**
     * setter pour l'attribut categorie section.
     * 
     * @param categorieSection
     *            une nouvelle valeur pour cet attribut
     */
    public void setCategorieSection(String categorieSection) {
        this.categorieSection = categorieSection;
    }

    /**
     * setter pour l'attribut date debut periode section.
     * 
     * @param dateDebutPeriodeSection
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPeriodeSection(String dateDebutPeriodeSection) {
        this.dateDebutPeriodeSection = dateDebutPeriodeSection;
    }

    /**
     * setter pour l'attribut date echeance.
     * 
     * @param dateEcheance
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    /**
     * setter pour l'attribut date fin periode section.
     * 
     * @param dateFinPeriodeSection
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinPeriodeSection(String dateFinPeriodeSection) {
        this.dateFinPeriodeSection = dateFinPeriodeSection;
    }

    /**
     * setter pour l'attribut date section.
     * 
     * @param dateSection
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateSection(String dateSection) {
        this.dateSection = dateSection;
    }

    /**
     * setter pour l'attribut description CA.
     * 
     * @param descriptionCA
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescriptionCA(String descriptionCA) {
        this.descriptionCA = descriptionCA;
    }

    /**
     * setter pour l'attribut id compte annexe.
     * 
     * @param idCompteAnnexe
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * setter pour l'attribut id externe role CA.
     * 
     * @param idExterneRoleCA
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdExterneRoleCA(String idExterneRoleCA) {
        this.idExterneRoleCA = idExterneRoleCA;
    }

    /**
     * setter pour l'attribut id externe section.
     * 
     * @param idExterneSection
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdExterneSection(String idExterneSection) {
        this.idExterneSection = idExterneSection;
    }

    public void setIdRoleCA(String idRoleCA) {
        this.idRoleCA = idRoleCA;
    }

    /**
     * setter pour l'attribut id section.
     * 
     * @param idSection
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    /**
     * setter pour l'attribut id sequence aquila.
     * 
     * @param idSequenceAquila
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSequenceContentieux(String idSequenceAquila) {
        idSequenceContentieux = idSequenceAquila;
    }

    /**
     * setter pour l'attribut id tiers CA.
     * 
     * @param idTiersCA
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersCA(String idTiersCA) {
        this.idTiersCA = idTiersCA;
    }

    /**
     * setter pour l'attribut id type section.
     * 
     * @param idTypeSection
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTypeSection(String idTypeSection) {
        this.idTypeSection = idTypeSection;
    }

    public void setRoleDescriptionCA(String roleDescriptionCA) {
        this.roleDescriptionCA = roleDescriptionCA;
    }

    /**
     * setter pour l'attribut solde.
     * 
     * @param solde
     *            une nouvelle valeur pour cet attribut
     */
    public void setSolde(String solde) {
        this.solde = solde;
    }
}
