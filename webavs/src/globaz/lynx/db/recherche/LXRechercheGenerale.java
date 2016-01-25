package globaz.lynx.db.recherche;

import globaz.globall.db.BStatement;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.utils.LXUtils;

public class LXRechercheGenerale extends LXSection {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_BASE = "BASE";
    public static final String FIELD_MOUVEMENT = "MOUVEMENT";
    public static final String FIELD_SOLDE = "SOLDE";

    private String base;
    private String baseProvisoire;
    private String complement = "";
    private Boolean estBloque = new Boolean(false);
    private Boolean estBloqueoperation = new Boolean(false);
    private String idExterneFournisseur;
    private String idSectionBase;
    private String idTiersFournisseur = null;
    private String mouvement;
    private String mouvementProvisoire;
    private String nom = "";
    private String referenceExterne;
    private String solde;
    private String soldeProvisoire;

    /**
     * @see globaz.lynx.db.operation.LXOperation#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdSectionBase(statement.dbReadNumeric(LXRechercheGeneraleManager.FIELD_IDSECTIONBASE));
        setBase(statement.dbReadNumeric(LXRechercheGenerale.FIELD_BASE, 2));
        setMouvement(statement.dbReadNumeric(LXRechercheGenerale.FIELD_MOUVEMENT, 2));
        setSolde(statement.dbReadNumeric(LXRechercheGenerale.FIELD_SOLDE, 2));
        setNom(statement.dbReadString("HTLDE1"));
        setComplement(statement.dbReadString("HTLDE2"));
        setIdExterne(statement.dbReadString(LXRechercheGeneraleManager.FIELD_IDEXTERNESECTION));
        setIdExterneFournisseur(statement.dbReadString(LXFournisseur.FIELD_IDEXTERNE));
        setIdSection(statement.dbReadNumeric(LXSection.FIELD_IDSECTION));
        setIdSociete(statement.dbReadNumeric(LXSection.FIELD_IDSOCIETE));
        setIdFournisseur(statement.dbReadNumeric(LXSection.FIELD_IDFOURNISSEUR));
        setIdJournal(statement.dbReadNumeric(LXSection.FIELD_IDJOURNAL));
        setCsTypeSection(statement.dbReadNumeric(LXSection.FIELD_CSTYPESECTION));
        setDateSection(statement.dbReadDateAMJ(LXSection.FIELD_DATESECTION));
        setReferenceExterne(statement.dbReadString(LXRechercheGeneraleManager.FIELD_IDREFERENCEEXTERNE));
        setEstBloque(statement.dbReadBoolean(LXFournisseur.FIELD_ESTBLOQUE));
        setEstBloqueoperation(statement.dbReadBoolean(LXRechercheGeneraleManager.FIELD_ESTBLOQUEOPERATION));
        setIdTiersFournisseur(statement.dbReadNumeric(LXFournisseur.FIELD_IDTIERS));
    }

    public String getBase() {
        return base;
    }

    public String getBaseProvisoire() {
        return baseProvisoire;
    }

    public String getComplement() {
        return complement;
    }

    /**
     * @return the estBloque
     */
    public Boolean getEstBloque() {
        return estBloque;
    }

    /**
     * @return the estBloqueoperation
     */
    public Boolean getEstBloqueoperation() {
        return estBloqueoperation;
    }

    public String getIdExterneFournisseur() {
        return idExterneFournisseur;
    }

    public String getIdSectionBase() {
        return idSectionBase;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiersFournisseur() {
        return idTiersFournisseur;
    }

    public String getMouvement() {
        return mouvement;
    }

    public String getMouvementProvisoire() {
        return mouvementProvisoire;
    }

    public String getNom() {
        return nom;
    }

    /**
     * Return une concaténation séparé d'un espace du nom et du complément
     */
    public String getNomCompletFournisseur() {
        return LXUtils.getNomComplet(getNom(), getComplement());
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public String getSolde() {
        return solde;
    }

    public String getSoldeProvisoire() {
        return soldeProvisoire;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setBaseProvisoire(String baseProvisoire) {
        this.baseProvisoire = baseProvisoire;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    /**
     * @param estBloque
     *            the estBloque to set
     */
    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }

    /**
     * @param estBloqueoperation
     *            the estBloqueoperation to set
     */
    public void setEstBloqueoperation(Boolean estBloqueoperation) {
        this.estBloqueoperation = estBloqueoperation;
    }

    public void setIdExterneFournisseur(String idExterneFournisseur) {
        this.idExterneFournisseur = idExterneFournisseur;
    }

    public void setIdSectionBase(String idSectionBase) {
        this.idSectionBase = idSectionBase;
    }

    /**
     * @param idTiers
     *            the idTiers to set
     */
    public void setIdTiersFournisseur(String idTiers) {
        idTiersFournisseur = idTiers;
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
    }

    public void setMouvementProvisoire(String mouvementProvisoire) {
        this.mouvementProvisoire = mouvementProvisoire;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public void setSolde(String solde) {
        this.solde = solde;
    }

    public void setSoldeProvisoire(String soldeProvisoire) {
        this.soldeProvisoire = soldeProvisoire;
    }
}
