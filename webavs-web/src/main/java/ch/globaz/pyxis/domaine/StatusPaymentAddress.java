package ch.globaz.pyxis.domaine;

import globaz.jade.client.util.JadeStringUtil;

public enum StatusPaymentAddress {
    CS_ADRESSE_PAIEMENT_SANS_FORMAT(524001),
    CS_ADRESSE_PAIEMENT_IBAN_OK(524002),
    CS_ADRESSE_PAIEMENT_IBAN_INCORRECT(524003),
    CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_OK(524004),
    CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_INCORRECT(524005),
    CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_INCORRECT_AUTRE(524006),
    CS_ADRESSE_PAIEMENT_FUSION(524007),
    CS_ADRESSE_PAIEMENT_IBAN_BANQUE_OK(524008);

    private Integer systemCode;

    StatusPaymentAddress(int systemCode) {
        this.systemCode = systemCode;
    }

    private Integer getSystemCode() {
        return this.systemCode;
    }

    public static StatusPaymentAddress parse(String systemCode) {
        if (!JadeStringUtil.isDigit(systemCode)) {
            throw new IllegalArgumentException("La valeur [" + systemCode + "] n'est pas valide pour un code système de type [" + StatusPaymentAddress.class.getName() + "]");
        }

        return StatusPaymentAddress.valueOf(Integer.parseInt(systemCode));
    }

    public static StatusPaymentAddress valueOf(Integer systemCode) {
        if (systemCode != null) {
            for (StatusPaymentAddress status : StatusPaymentAddress.values()) {
                if (status.getSystemCode().equals(systemCode)) {
                    return status;
                }
            }
        }
        throw new IllegalArgumentException("La valeur [" + systemCode + "] n'est pas valide pour un code système de type [" + StatusPaymentAddress.class.getName() + "]");
    }
}


