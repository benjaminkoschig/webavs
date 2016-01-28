package globaz.naos.db.attestation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.api.APIEcriture;

/**
 * @author SCO
 * @since 05 juil. 2011
 */
public class AFEcritureAffilieForAttesPerso extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeCotisation;
    private String codeDebitCredit;
    private String date;
    private String idExterne;
    private String montant;

    @Override
    protected boolean _allowAdd() {
        return false;
    }

    @Override
    protected boolean _allowDelete() {
        return false;
    }

    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        montant = statement.dbReadNumeric("MONTANT");
        date = statement.dbReadDateAMJ("DATE");
        anneeCotisation = statement.dbReadNumeric("ANNEECOTISATION");
        idExterne = statement.dbReadString("IDEXTERNE");
        codeDebitCredit = statement.dbReadString("CODEDEBITCREDIT");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    public String getDate() {
        return date;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getMontant() {
        return montant;
    }

    public double getMontantSigne() {
        String montantSigne = JANumberFormatter.deQuote(montant);
        double montantTemp = Double.parseDouble(montantSigne);

        if (getCodeDebitCredit().equals(APIEcriture.CREDIT) || getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
            montantTemp = -Math.abs(montantTemp);
        } else {
            montantTemp = Math.abs(montantTemp);
        }

        return montantTemp;
    }

    public void setAnneeCotisation(String anneeCotisation) {
        this.anneeCotisation = anneeCotisation;
    }

    public void setCodeDebitCredit(String codeDebitCredit) {
        this.codeDebitCredit = codeDebitCredit;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
