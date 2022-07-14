package globaz.osiris.db.comptes;

import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Entity retrouvée avec un JOIN des Comptes annexes avec les ebil et num avs. Prêt à être mis en CSV
 */
public class CACompteAnnexePourListeAffiliesEBill extends BEntity {

    public static final String NUMERO_INFOROM = "0346GCA";
    private String idTiers;
    private String numeroAffilie;
    private String nss;
    private String nom;
    private String codePostal;
    private String lieu;
    private String role;
    private String hasEBill;
    private String dateInscriptionEBill;
    private String eBillAccountId;
    private String emailEBill;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTiers = statement.dbReadString("IDTIERS");
        numeroAffilie = statement.dbReadString("MALNAF");
        nss = statement.dbReadString("HXNAVS");
        nom = statement.dbReadString("MADESL");
        role = getSession().getCodeLibelle(statement.dbReadString("IDROLE"));
        hasEBill = JadeStringUtil.isBlankOrZero(statement.dbReadString("EBILLID")) ?
                getSession().getLabel("NON"):
                getSession().getLabel("OUI");
        dateInscriptionEBill = statement.dbReadDateAMJ("EBILLDATEINSCRIPTION");
        eBillAccountId = statement.dbReadString("EBILLID");
        emailEBill = statement.dbReadString("EBILLMAIL");
    }

    @Override
    protected void _validate(BStatement bStatement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement bStatement) throws Exception {

    }

    @Override
    protected void _writeProperties(BStatement bStatement) throws Exception {

    }

    public String getIdTiers() {
        return idTiers;
    }

    @Column(name = "numero_Affilie", order = 1)
    @ColumnStyle(align = Align.LEFT)
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    @Column(name = "nss", order = 2)
    @ColumnStyle(align = Align.LEFT)
    public String getNss() {
        return nss;
    }

    @Column(name = "nom", order = 3)
    @ColumnStyle(align = Align.LEFT)
    public String getNom() {
        return nom;
    }

    @Column(name = "code_Postal", order = 4)
    @ColumnStyle(align = Align.LEFT)
    public String getCodePostal() {
        return codePostal;
    }

    @Column(name = "lieu", order = 5)
    @ColumnStyle(align = Align.LEFT)
    public String getLieu() {
        return lieu;
    }

    @Column(name = "role", order = 6)
    @ColumnStyle(align = Align.LEFT)
    public String getRole() {
        return role;
    }

    @Column(name = "inscrit_ebill", order = 7)
    @ColumnStyle(align = Align.LEFT)
    public String getHasEBill() {
        return hasEBill;
    }

    @Column(name = "date_inscription_ebill", order = 8)
    @ColumnStyle(align = Align.LEFT)
    public String getDateInscriptionEBill() {
        return dateInscriptionEBill;
    }

    @Column(name = "ebill_account_id", order = 9)
    @ColumnStyle(align = Align.LEFT)
    public String getEBillAccountId() {
        return eBillAccountId;
    }

    @Column(name = "email_ebill", order = 10)
    @ColumnStyle(align = Align.LEFT)
    public String getEmailEBill() {
        return emailEBill;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setHasEBill(String hasEBill) {
        this.hasEBill = hasEBill;
    }

    public void setDateInscriptionEBill(String dateInscriptionEBill) {
        this.dateInscriptionEBill = dateInscriptionEBill;
    }

    public void setEBillAccountId(String eBillAccountId) {
        this.eBillAccountId = eBillAccountId;
    }

    public void setEmailEBill(String emailEBill) {
        this.emailEBill = emailEBill;
    }
}
