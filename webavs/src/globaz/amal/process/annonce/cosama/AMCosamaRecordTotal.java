/**
 * 
 */
package globaz.amal.process.annonce.cosama;

/**
 * @author dhi
 * 
 */
public class AMCosamaRecordTotal extends AMCosamaRecord {

    private String montantTotalCumule = "";

    private String nombreArticles = "";

    /**
     * Default constructor (empêche la création)
     */
    private AMCosamaRecordTotal() {

    }

    /**
     * @param _typeEnregistrement
     */
    protected AMCosamaRecordTotal(String _typeEnregistrement) {
        setTypeEnregistrement(_typeEnregistrement);
    }

    @Override
    public int compareTo(AMCosamaRecord o) {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.tests.cosama.AMCosamaRecord#formatFields()
     */
    @Override
    public void formatFields() {
        setTypeEnregistrement(fillField(getTypeEnregistrement(), '0', 1, true));
        nombreArticles = fillField(nombreArticles, '0', 8, true);
        montantTotalCumule = fillField(montantTotalCumule, '0', 11, true);
    }

    /**
     * @return the montantTotalCumule
     */
    public String getMontantTotalCumule() {
        return montantTotalCumule;
    }

    /**
     * @return the nombreArticles
     */
    public String getNombreArticles() {
        return nombreArticles;
    }

    @Override
    public void parseLigne(String currentLigne) {
        // Nombre d'article
        if (currentLigne.length() > 8) {
            nombreArticles = currentLigne.substring(1, 9);
        }
        // Montant total cumulé
        if (currentLigne.length() > 17) {
            // Entier
            montantTotalCumule = currentLigne.substring(9, 18);
            // Centimes
            montantTotalCumule += currentLigne.substring(18);
        }
    }

    /**
     * @param montantTotalCumule
     *            the montantTotalCumule to set
     */
    public void setMontantTotalCumule(String montantTotalCumule) {
        this.montantTotalCumule = montantTotalCumule;
    }

    /**
     * @param nombreArticles
     *            the nombreArticles to set
     */
    public void setNombreArticles(String nombreArticles) {
        this.nombreArticles = nombreArticles;
    }

    @Override
    public String writeLigne() {
        return this.writeLigne(false);
    }

    @Override
    public String writeLigne(boolean bWithSeparator) {
        String fieldSeparator = "";
        String currencySeparator = "";
        if (bWithSeparator) {
            fieldSeparator = IAMCosamaRecord._FieldSeparator;
            currencySeparator = IAMCosamaRecord._CurrencySeparator;
        }
        String finalLine = "";

        finalLine += getTypeEnregistrement();
        finalLine += fieldSeparator;

        finalLine += getNombreArticles();
        finalLine += fieldSeparator;

        if (bWithSeparator && (getMontantTotalCumule().length() > 2)) {
            finalLine += getMontantTotalCumule().substring(0, getMontantTotalCumule().length() - 2);
            finalLine += currencySeparator;
            finalLine += getMontantTotalCumule().substring(getMontantTotalCumule().length() - 2);
        } else {
            finalLine += getMontantTotalCumule();
        }
        finalLine += fieldSeparator;

        return finalLine;
    }

}
