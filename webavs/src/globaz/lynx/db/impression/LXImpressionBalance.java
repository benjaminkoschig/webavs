package globaz.lynx.db.impression;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.utils.LXUtils;

public class LXImpressionBalance extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String complement;
    private String credit;
    private String debit;
    private String idExterne;
    private String idFournisseur;
    private String nom;
    private String solde;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setCredit(statement.dbReadNumeric(LXImpressionBalanceManager.FIELD_CREDIT, 2));
        setDebit(statement.dbReadNumeric(LXImpressionBalanceManager.FIELD_DEBIT, 2));
        setSolde(statement.dbReadNumeric(LXImpressionBalanceManager.FIELD_SOLDE, 2));
        setIdFournisseur(statement.dbReadNumeric(LXSection.FIELD_IDFOURNISSEUR));
        setIdExterne(statement.dbReadString(LXFournisseur.FIELD_IDEXTERNE));
        setNom(statement.dbReadString("HTLDE1"));
        setComplement(statement.dbReadString("HTLDE2"));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // nothing
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // nothing
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // nothing

    }

    // *******************************************************
    // Getter
    // *******************************************************}

    public String getComplement() {
        return complement;
    }

    public String getCredit() {
        return credit;
    }

    public String getCreditFormatted() {
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(getCredit()), true, true, false, 2);
    }

    public String getDebit() {
        return debit;
    }

    public String getDebitFormatted() {
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(getDebit()), true, true, false, 2);
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getNom() {
        return nom;
    }

    /**
     * Return une concaténation séparé d'un espace du nom et du complément
     */
    public String getNomComplet() {
        return LXUtils.getNomComplet(getNom(), getComplement());
    }

    public String getSolde() {
        return solde;
    }

    public String getSoldeFormatted() {
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(getSolde()), true, true, false, 2);
    }

    // *******************************************************
    // Setter
    // *******************************************************}

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setSolde(String solde) {
        this.solde = solde;
    }
}