package ch.globaz.common.domaine;

import globaz.jade.client.util.JadeStringUtil;

public class CodeSystemEnumUtils {

    private CodeSystemEnumUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Retourne l'�num�r� correspondant au code syst�me. Renvoie une {@link IllegalArgumentException} si la cha�ne de
     * caract�re pass�e en param�tre est invalide (null ou vide) ou si le code syst�me ne correspond pas � un valeur de
     * cette �num�r�.
     * 
     * @param <T>
     * @param codeSysteme
     * @return
     */
    public static <T extends Enum<T> & CodeSystemEnum<T>> T parse(String codeSysteme, Class<T> enumm) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("[codeSysteme:" + codeSysteme
                    + "] must be made only of digits for this enum " + enumm);
        }

        return CodeSystemEnumUtils.valueOfById(codeSysteme, enumm);
    }

    /**
     * Retourne l'�num�r� correspondant au code syst�me. Renvoie une {@link IllegalArgumentException} si le param�tre
     * est null ou si le code syst�me ne correspond pas � un valeur de cette �num�r�.
     * 
     * @param string
     * @return
     */
    public static <T extends Enum<T> & CodeSystemEnum<T>> T valueOfById(String id, Class<T> enumm) {
        Checkers.checkNotNull(id, "id");
        Checkers.checkNotNull(enumm, "enumm");

        for (T enu : enumm.getEnumConstants()) {
            if (enu.getValue().equals(id)) {
                return enu;
            }
        }

        throw new IllegalArgumentException("This id: " + id + " code systeme is not definded for this enum: "
                + enumm.getName());
    }

    /**
     * V�rifie que le code syst�me est d�finit dans l'�nume
     * 
     * @param CodSystem value
     * @param Enum class du code syst�me
     * @return
     */
    public static <T extends Enum<T> & CodeSystemEnum<T>> boolean isValid(String id, Class<T> enumm) {
        try {
            valueOfById(id, enumm);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}