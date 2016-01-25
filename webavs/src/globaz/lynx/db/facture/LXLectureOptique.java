package globaz.lynx.db.facture;

import globaz.globall.db.BStatement;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.informationcomptable.LXInformationComptableViewBean;
import globaz.lynx.utils.LXUtils;

public class LXLectureOptique extends LXInformationComptableViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String banqueClearing = "";
    private String banqueCompte = "";
    private String banqueDes1 = "";
    private String banqueDes2 = "";
    private String banqueSwift = "";

    private String complement = "";
    private String idAdressePmt = "";
    private String idExterne = "";
    private String idTiers = "";
    private String nom = "";
    private String nomBanque = "";
    private String numCompte = "";

    /**
     * @see globaz.lynx.db.informationcomptable.LXInformationComptable#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null; // unused
    }

    /**
     * @see globaz.lynx.db.informationcomptable.LXInformationComptable#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        setIdTiers(statement.dbReadNumeric(LXFournisseur.FIELD_IDTIERS));

        setIdExterne(statement.dbReadString(LXFournisseur.FIELD_IDEXTERNE));

        try {
            // Recuperation du nom et complement sur la table des tiers
            setNom(statement.dbReadString("HTLDE1"));
            setComplement(statement.dbReadString("HTLDE2"));
            setBanqueDes1(statement.dbReadString("bdes1"));
            setBanqueDes2(statement.dbReadString("bdes2"));
            setBanqueCompte(statement.dbReadString("bcompte"));
            setBanqueClearing(statement.dbReadString("bclearing"));
            setBanqueSwift(statement.dbReadString("bswift"));
            setIdAdressePmt(statement.dbReadNumeric("HIIADU"));
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * @see globaz.lynx.db.informationcomptable.LXInformationComptable#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // unused
    }

    /**
     * @see globaz.lynx.db.informationcomptable.LXInformationComptable#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // unused
    }

    /**
     * @see globaz.lynx.db.informationcomptable.LXInformationComptable#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // unused
    }

    /**
     * @return the banqueClearing
     */
    public String getBanqueClearing() {
        return banqueClearing;
    }

    /**
     * @return the banqueCompte
     */
    public String getBanqueCompte() {
        return banqueCompte;
    }

    /**
     * @return the banqueDes1
     */
    public String getBanqueDes1() {
        return banqueDes1;
    }

    /**
     * @return the banqueDes2
     */
    public String getBanqueDes2() {
        return banqueDes2;
    }

    /**
     * @return the banqueSwift
     */
    public String getBanqueSwift() {
        return banqueSwift;
    }

    public String getComplement() {
        return complement;
    }

    /**
     * @return the idAdressePmt
     */
    public String getIdAdressePmt() {
        return idAdressePmt;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNom() {
        return nom;
    }

    public String getNomBanque() {
        return nomBanque;
    }

    /**
     * Return une concaténation séparé d'un espace du nom et du complément
     */
    public String getNomComplet() {
        return LXUtils.getNomComplet(getNom(), getComplement());
    }

    public String getNumCompte() {
        return numCompte;
    }

    /**
     * @param banqueClearing
     *            the banqueClearing to set
     */
    public void setBanqueClearing(String banqueClearing) {
        this.banqueClearing = banqueClearing;
    }

    /**
     * @param banqueCompte
     *            the banqueCompte to set
     */
    public void setBanqueCompte(String banqueCompte) {
        this.banqueCompte = banqueCompte;
    }

    /**
     * @param banqueDes1
     *            the banqueDes1 to set
     */
    public void setBanqueDes1(String banqueDes1) {
        this.banqueDes1 = banqueDes1;
    }

    /**
     * @param banqueDes2
     *            the banqueDes2 to set
     */
    public void setBanqueDes2(String banqueDes2) {
        this.banqueDes2 = banqueDes2;
    }

    /**
     * @param banqueSwift
     *            the banqueSwift to set
     */
    public void setBanqueSwift(String banqueSwift) {
        this.banqueSwift = banqueSwift;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    /**
     * @param idAdressePmt
     *            the idAdressePmt to set
     */
    public void setIdAdressePmt(String idAdressePmt) {
        this.idAdressePmt = idAdressePmt;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNomBanque(String nomBanque) {
        this.nomBanque = nomBanque;
    }

    public void setNumCompte(String numCompte) {
        this.numCompte = numCompte;
    }

}
