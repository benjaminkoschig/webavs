package ch.globaz.vulpecula.domain.models.common;

/**
 * Numéro de référence d'un bulletin de versement (BVR)
 * 
 * @since WebBMS 0.01.03
 */
public class NumeroReference implements ValueObject {
    private static final long serialVersionUID = 1L;
    private static final int MAX_LENGTH_REFERENCE = 26; // +1 du modulo de contrôle = les 27 positions

    private String numeroReference;

    public NumeroReference(String numero) {
        if (numero == null || numero.length() != MAX_LENGTH_REFERENCE) {
            throw new IllegalArgumentException("Numéro is null or too long !");
        }

        numeroReference = numero;
    }

    /**
     * @return the numeroReference
     */
    public String getValue() {
        return numeroReference;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
