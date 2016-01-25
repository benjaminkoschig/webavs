package globaz.apg.pojo;

import globaz.apg.enums.APCentreRegionauxServiceCivil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.enums.PRCanton;

public class APReferenceNumber {

    private enum APReferenceNumberFormatError {
        CAST_STRING_TO_INTEGER_EXCEPTION(
                "An exception was thrown when converting reference number from String to Integer."),
        REFERENCE_NUMBER_IS_EMPTY("The reference number is null or empty."),
        REFERENCE_NUMBER_STRING_TOO_SHORT("The reference number is too short. The minimum size is 4 position."),
        CODE_CANTON_INVALIDE("The code for 'canton' is invalid"),
        CODE_CENTRE_SERVICE_CIVIL_INVALID("The code for 'centre de service civil' is invalid."),
        WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_1("The reference number format seems invalid for cas 1."),
        WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_2("The reference number format seems invalid for cas 2."),
        WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_3("The reference number format seems invalid for cas 3."),
        WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_4("The reference number format seems invalid for cas 4."),
        WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_5("The reference number format seems invalid for cas 5."),
        WRONG_REFERENCE_NUMBER_FORMAT("The reference number format seems invalid. The minimum size is 4 position.");

        private String label;

        private APReferenceNumberFormatError(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private static final String _26001 = "26001";
    private static final String _26002 = "26002";
    private static final String _26991 = "26991";
    private static final String _26992 = "26992";
    private static final String _1 = "1";
    private static final String _2 = "2";

    private String referenceNumber;
    private int cas;
    private PRCanton codeCanton;
    private APCentreRegionauxServiceCivil codeCentreInstruction;
    private String npa;

    public APReferenceNumber() {
        clear();
    }

    /**
     * Efface les données interne suite à un décodage de referenceNumber
     */
    public void clear() {
        referenceNumber = null;
        cas = 0;
        codeCanton = null;
        codeCentreInstruction = null;
        npa = null;
    }

    public final String getReferenceNumber() {
        return referenceNumber;
    }

    public final void setReferenceNumber(String referenceNumber) throws Exception {
        clear();
        // 1er test le refNumber
        if (JadeStringUtil.isEmpty(referenceNumber)) {
            throwException(APReferenceNumberFormatError.REFERENCE_NUMBER_IS_EMPTY, referenceNumber);
        }

        // On supprime tous les points au cas ou ils auraient été saisit
        referenceNumber = referenceNumber.replace(".", "");

        // 2ème test; le refNumber ne doit pas être plus petit que 4 position
        if (referenceNumber.length() < 4) {
            throwException(APReferenceNumberFormatError.REFERENCE_NUMBER_STRING_TOO_SHORT, referenceNumber);
        }

        // La valeur doit être convertible sous forme de nombre entier. (Pas de caractère alfa dans le reference number)
        int refNumberAsInt = Integer.MAX_VALUE;
        try {
            refNumberAsInt = Integer.valueOf(referenceNumber);
        } catch (NumberFormatException exception) {
            throwException(APReferenceNumberFormatError.CAST_STRING_TO_INTEGER_EXCEPTION, referenceNumber);
        }

        // cas 4 ou 5 : ????????99?
        int beginIndex = referenceNumber.length() - 3;
        int endIndex = referenceNumber.length() - 1;
        if ((beginIndex < 0) || (endIndex < 0)) {
            throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT, referenceNumber);
        }
        // Forcément cas 3
        if ((referenceNumber.length() > 5) || APReferenceNumber._26002.equals(referenceNumber)) {
            validateCas3(referenceNumber);
        } else {

            String code99 = referenceNumber.substring(beginIndex, endIndex);
            // Cas 4 ou 5
            if ("99".equals(code99)) {

                beginIndex = referenceNumber.length() - 1;
                endIndex = referenceNumber.length();
                if ((beginIndex < 0) || (endIndex < 0)) {
                    throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT, referenceNumber);
                }
                String lastChar = referenceNumber.substring(beginIndex, endIndex);
                // Cas 4
                if (APReferenceNumber._1.equals(lastChar)) {
                    validateCas4(referenceNumber);
                }
                // Cas 5
                else if (APReferenceNumber._2.equals(lastChar)) {
                    validateCas5(referenceNumber);
                }
                // Cas pourris
                else {
                    throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT, referenceNumber);
                }

            }
            // Cas 1, 2, 3
            else {
                if (APReferenceNumber._26001.equals(referenceNumber)) {
                    validateCas2(referenceNumber);
                } else {
                    // Pour déterminer si c'est un cas 1 ou 2 on se base sur le dernier caractère
                    beginIndex = referenceNumber.length() - 1;
                    endIndex = referenceNumber.length();
                    if ((beginIndex < 0) || (endIndex < 0)) {
                        throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT, referenceNumber);
                    }
                    String lastChar = referenceNumber.substring(beginIndex, endIndex);
                    // Cas 1
                    if (APReferenceNumber._1.equals(lastChar)) {
                        validateCas1(referenceNumber);
                    }
                    // Cas 2
                    else if (APReferenceNumber._2.equals(lastChar)) {
                        validateCas2(referenceNumber);
                    } else {
                        throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT, referenceNumber);
                    }
                }
            }
        }
    }

    private void validateCas1(String referenceNumber) throws Exception {
        int length = referenceNumber.length();
        if ((length != 4) && (length != 5)) {
            throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_1, referenceNumber);
        }

        String lastNumber = referenceNumber.substring(length - 1, length);
        if (!APReferenceNumber._1.equals(lastNumber)) {
            throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_1, referenceNumber);
        }

        String codeCentreInstruction = null;
        String codeCanton = null;

        if (length == 4) {
            codeCanton = referenceNumber.substring(0, 1);
            codeCentreInstruction = referenceNumber.substring(1, 3);
        } else {
            codeCanton = referenceNumber.substring(0, 2);
            codeCentreInstruction = referenceNumber.substring(2, 4);
        }

        this.codeCanton = validateCanton(referenceNumber, codeCanton);
        this.codeCentreInstruction = validateCentreServiceCivil(referenceNumber, codeCentreInstruction);

        this.referenceNumber = referenceNumber;
        cas = 1;
    }

    private void validateCas2(String referenceNumber) throws Exception {
        if (APReferenceNumber._26001.equals(referenceNumber)) {
            this.referenceNumber = referenceNumber;
            cas = 2;
        } else {

            int length = referenceNumber.length();
            if ((length != 4) && (length != 5)) {
                throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_2, referenceNumber);
            }

            String lastNumber = referenceNumber.substring(length - 1, length);
            if (!APReferenceNumber._2.equals(lastNumber)) {
                throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_2, referenceNumber);
            }

            String codeCentreInstruction = null;
            String codeCanton = null;

            if (length == 4) {
                codeCanton = referenceNumber.substring(0, 1);
                codeCentreInstruction = referenceNumber.substring(1, 3);
            } else {
                codeCanton = referenceNumber.substring(0, 2);
                codeCentreInstruction = referenceNumber.substring(2, 4);
            }

            this.codeCanton = validateCanton(referenceNumber, codeCanton);
            this.codeCentreInstruction = validateCentreServiceCivil(referenceNumber, codeCentreInstruction);
            this.referenceNumber = referenceNumber;
            cas = 2;
        }
    }

    private void validateCas3(String referenceNumber) throws Exception {
        if (APReferenceNumber._26002.equals(referenceNumber)) {
            this.referenceNumber = referenceNumber;
            cas = 3;
        } else {

            int length = referenceNumber.length();
            if ((length != 6) && (length != 7)) {
                throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_3, referenceNumber);
            }

            String lastNumber = referenceNumber.substring(length - 1, length);
            if (!APReferenceNumber._1.equals(lastNumber)) {
                throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_3, referenceNumber);
            }

            String npa = null;
            String codeCanton = null;

            if (length == 6) {
                codeCanton = referenceNumber.substring(0, 1);
                npa = referenceNumber.substring(1, 5);
            } else {
                codeCanton = referenceNumber.substring(0, 2);
                npa = referenceNumber.substring(2, 6);
            }

            this.codeCanton = validateCanton(referenceNumber, codeCanton);
            this.npa = npa;
            this.referenceNumber = referenceNumber;
            cas = 3;
        }
    }

    private void validateCas4(String referenceNumber) throws Exception {
        if (APReferenceNumber._26991.equals(referenceNumber)) {
            this.referenceNumber = referenceNumber;
            cas = 4;
        } else {

            int length = referenceNumber.length();
            if ((length != 4) && (length != 5)) {
                throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_4, referenceNumber);
            }

            String lastNumber = referenceNumber.substring(length - 1, length);
            if (!APReferenceNumber._1.equals(lastNumber)) {
                throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_4, referenceNumber);
            }

            String codeCentreInstruction = null;
            String codeCanton = null;

            if (length == 4) {
                codeCanton = referenceNumber.substring(0, 1);
                codeCentreInstruction = referenceNumber.substring(1, 3);
            } else {
                codeCanton = referenceNumber.substring(0, 2);
                codeCentreInstruction = referenceNumber.substring(2, 4);
            }

            this.codeCanton = validateCanton(referenceNumber, codeCanton);
            this.codeCentreInstruction = validateCentreServiceCivil(referenceNumber, codeCentreInstruction);
            this.referenceNumber = referenceNumber;
            cas = 4;
        }
    }

    private void validateCas5(String referenceNumber) throws Exception {
        if (APReferenceNumber._26992.equals(referenceNumber)) {
            this.referenceNumber = referenceNumber;
            cas = 5;
        } else {

            int length = referenceNumber.length();
            if ((length != 4) && (length != 5)) {
                throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_5, referenceNumber);
            }

            String lastNumber = referenceNumber.substring(length - 1, length);
            if (!APReferenceNumber._2.equals(lastNumber)) {
                throwException(APReferenceNumberFormatError.WRONG_REFERENCE_NUMBER_FORMAT_FOR_CAS_5, referenceNumber);
            }

            String codeCentreInstruction = null;
            String codeCanton = null;

            if (length == 4) {
                codeCanton = referenceNumber.substring(0, 1);
                codeCentreInstruction = referenceNumber.substring(1, 3);
            } else {
                codeCanton = referenceNumber.substring(0, 2);
                codeCentreInstruction = referenceNumber.substring(2, 4);
            }

            this.codeCanton = validateCanton(referenceNumber, codeCanton);
            this.codeCentreInstruction = validateCentreServiceCivil(referenceNumber, codeCentreInstruction);
            this.referenceNumber = referenceNumber;
            cas = 5;
        }
    }

    /**
     * @param referenceNumber
     * @param codeCentreInstruction
     * @throws Exception
     */
    private APCentreRegionauxServiceCivil validateCentreServiceCivil(String referenceNumber,
            String codeCentreInstruction) throws Exception {
        APCentreRegionauxServiceCivil centre = APCentreRegionauxServiceCivil
                .getCentreServiceCivil(codeCentreInstruction);
        if (centre == null) {
            throwException(APReferenceNumberFormatError.CODE_CENTRE_SERVICE_CIVIL_INVALID, referenceNumber);
        }
        return centre;
    }

    public int getCas() {
        return cas;
    }

    public PRCanton getCodeCanton() {
        return codeCanton;
    }

    public APCentreRegionauxServiceCivil getCodeCentreInstruction() {
        return codeCentreInstruction;
    }

    public String getNpa() {
        return npa;
    }

    /**
     * @param referenceNumber
     * @param codeCanton
     * @param centre
     * @return
     * @throws Exception
     */
    private PRCanton validateCanton(String referenceNumber, String codeCanton) throws Exception {
        PRCanton canton = PRCanton.getCanton(codeCanton);
        if (canton == null) {
            throwException(APReferenceNumberFormatError.CODE_CANTON_INVALIDE, referenceNumber);
        }
        return canton;
    }

    private void throwException(APReferenceNumberFormatError error, String referenceNumber) throws Exception {
        throw new Exception(error.getLabel() + getRefNumberForamtted(referenceNumber));
    }

    private String getRefNumberForamtted(String referenceNumber) {
        return " ReferenceNumber : [" + referenceNumber + "]";
    }
}
