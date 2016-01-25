/**
 * 
 */
package globaz.amal.process.annonce.cosama;

/**
 * @author dhi
 * 
 */
public abstract class AMCosamaRecord implements IAMCosamaRecord, Comparable<AMCosamaRecord> {

    /**
     * Default constructor
     * 
     * Type d'enregistrement :
     * 
     * 1 - Entête 2 - Détail 3 - Total
     * 
     * @return
     */
    public static AMCosamaRecord getInstance(String _typeEnregistrement) {
        if (_typeEnregistrement.equals(IAMCosamaRecord._TypeEnregistrementEnTete)) {
            return new AMCosamaRecordEnTete(_typeEnregistrement);
        } else if (_typeEnregistrement.equals(IAMCosamaRecord._TypeEnregistrementTotal)) {
            return new AMCosamaRecordTotal(_typeEnregistrement);
        } else if (_typeEnregistrement.equals(IAMCosamaRecord._TypeEnregistrementDetail)) {
            return new AMCosamaRecordDetail(_typeEnregistrement);
        } else {
            return null;
        }
    }

    /**
     * Type d'enregistrement
     */
    private String typeEnregistrement = null;

    /**
     * Formattage d'un champ en fonction de sa valeur et longueur max, avec caractère de remplissage
     * 
     * @param value
     *            valeur actuelle
     * @param filledChar
     *            caractère de remplissage
     * @param fieldLength
     *            Longueur du champ final
     * @param bStart
     *            rempli depuis le début si true, sinon depuis la fin
     * @return
     */
    protected String fillField(String value, char filledChar, int fieldLength, boolean bStart) {

        String finalValue = value;

        for (int iChar = 0; iChar < fieldLength - value.length(); iChar++) {
            if (bStart) {
                finalValue = filledChar + finalValue;
            } else {
                finalValue = finalValue + filledChar;
            }
        }
        return finalValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.tests.cosama.IAMCosamaRecord#formatFields()
     */
    @Override
    public void formatFields() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.tests.cosama.IAMCosamaRecord#getTypeEnregistrement()
     */
    @Override
    public String getTypeEnregistrement() {
        return typeEnregistrement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.tests.cosama.IAMCosamaRecord#parseLigne()
     */
    @Override
    public void parseLigne(String currentLigne) {
    }

    /**
     * Set le type d'enregistrement
     * 
     * @param _typeEnregistrement
     */
    protected void setTypeEnregistrement(String _typeEnregistrement) {
        typeEnregistrement = _typeEnregistrement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.tests.cosama.IAMCosamaRecord#writeLigne()
     */
    @Override
    public String writeLigne() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.tests.cosama.IAMCosamaRecord#writeLigne(boolean)
     */
    @Override
    public String writeLigne(boolean bWithSeparator) {
        return null;
    }

}
