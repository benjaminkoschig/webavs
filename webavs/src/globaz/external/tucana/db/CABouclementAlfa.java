package globaz.external.tucana.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import java.math.BigDecimal;

public class CABouclementAlfa extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeCotisation;
    private String canton;
    private String sumMasse;

    private String sumMontant;

    @Override
    protected String _getTableName() {
        // Not used here.
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setSumMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
        setSumMasse(statement.dbReadNumeric(CAOperation.FIELD_MASSE));
        setCanton(statement.dbReadString("PCOUID"));

        try {
            setAnneeCotisation(statement.dbReadNumeric(CAOperation.FIELD_ANNEECOTISATION));
        } catch (Exception e) {
            // Do nothing. Maybee column not present into select.
        }
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Nothing here.
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Nothing here.
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Nothing here.
    }

    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    public String getCanton() {
        return canton;
    }

    public String getSumMasse() {
        return sumMasse;
    }

    public String getSumMontant() {
        return sumMontant;
    }

    public BigDecimal getTaux() {
        BigDecimal montant = new BigDecimal(getSumMontant());
        BigDecimal masse = new BigDecimal(getSumMasse());

        BigDecimal result = montant.divide(masse, 5, BigDecimal.ROUND_HALF_EVEN);

        return result.multiply(new BigDecimal(100.00));
    }

    public void setAnneeCotisation(String anneeCotisation) {
        this.anneeCotisation = anneeCotisation;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setSumMasse(String sumMasse) {
        this.sumMasse = sumMasse;
    }

    public void setSumMontant(String sumMontant) {
        this.sumMontant = sumMontant;
    }

    public String toMyString() {
        return "Canton [" + getCanton() + "] sumMontant : " + getSumMontant();
    }

}
