package globaz.osiris.db.suivipaiements;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;

public class CASuiviPaiementsAutresTachesSumMontant extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idSection;
    // CAOPERP variable
    private String sumMontant;

    /**
     * Return nothing. Entity est utilisé uniquement par un Inner-Join.
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        try {
            setSumMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
        } catch (Exception e) {
            // Do nothing. Maybee field aren't selected.
        }

        try {
            setIdSection(statement.dbReadNumeric(CAOperation.FIELD_IDSECTION));
        } catch (Exception e) {
            // Do nothing. Maybee field aren't selected.
        }
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Nothing.
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Nothing.
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Nothing.
    }

    /**
     * @return Returns the idSection.
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return Returns the sumMontant.
     */
    public String getSumMontant() {
        return sumMontant;
    }

    /**
     * @param idSection
     *            The idSection to set.
     */
    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    /**
     * @param sumMontant
     *            The sumMontant to set.
     */
    public void setSumMontant(String sumMontant) {
        this.sumMontant = sumMontant;
    }

    /**
     * @return
     * @author
     */
    public String toMyString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CASuiviPaiementsAutresTachesSumMontant[");
        buffer.append("idSection = ").append(idSection);
        buffer.append(" sumMontant = ").append(sumMontant);
        buffer.append("]");
        return buffer.toString();
    }

}
