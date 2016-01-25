package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Entité pour le manager {@link CASectionJoinCompteAnnexeJoinTiersManager}.<br/>
 * Optimisé pour la recherche au travers d'un widget d'auto-complétion.
 * 
 * @author PBA
 */
public class CASectionJoinCompteAnnexeJoinTiers extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String categorieSection = "";
    private String descriptionCompteAnnexe = "";
    private String idCompteAnnexe = "";
    private String idExterne = "";
    private String idExterneRole = "";
    private String idRole = "";
    private String idSection = "";
    private String idTypeSection = "";
    private String role = "";
    private String solde = "";
    private String typeSection = "";

    public CASectionJoinCompteAnnexeJoinTiers() {
        super();
    }

    @Override
    protected String _getTableName() {
        return CASection.TABLE_CASECTP;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        categorieSection = statement
                .dbReadString(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_CATEGORIE_SECTION);
        descriptionCompteAnnexe = statement.dbReadString(CACompteAnnexe.FIELD_DESCRIPTION);
        idCompteAnnexe = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        idExterne = statement.dbReadString(CASection.FIELD_IDEXTERNE);
        idExterneRole = statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        idRole = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDROLE);
        idTypeSection = statement.dbReadNumeric(CASection.FIELD_IDTYPESECTION);
        role = statement.dbReadString(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_ROLE_COMPTE_ANNEXE);
        solde = statement.dbReadString(CASection.FIELD_SOLDE);
        typeSection = statement.dbReadString(CASectionJoinCompteAnnexeJoinTiersManager.ALIAS_LIBELLE_TYPE_SECTION);
        setIdSection(statement.dbReadString(CASection.FIELD_IDSECTION));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien, entité composée ne pouvant pas être sauvée en base de donnée
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // rien, entité composée ne pouvant pas être sauvée en base de donnée
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // rien, cette entité est composée
        throw new Exception("Cette entité ne peut pas être sauvée en base de donnée");
    }

    public String getCategorieSection() {
        return categorieSection;
    }

    public String getDescriptionCompteAnnexe() {
        return descriptionCompteAnnexe;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTypeSection() {
        return idTypeSection;
    }

    public String getRole() {
        return role;
    }

    public String getSolde() {
        return solde;
    }

    public String getTypeSection() {
        return typeSection;
    }

    public void setCategorieSection(String categorieSection) {
        this.categorieSection = categorieSection;
    }

    public void setDescriptionCompteAnnexe(String descriptionCompteAnnexe) {
        this.descriptionCompteAnnexe = descriptionCompteAnnexe;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTypeSection(String idTypeSection) {
        this.idTypeSection = idTypeSection;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSolde(String solde) {
        this.solde = solde;
    }

    public void setTypeSection(String typeSection) {
        this.typeSection = typeSection;
    }
}
