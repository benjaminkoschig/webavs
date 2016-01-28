package ch.globaz.common.domaine;

import globaz.jade.client.util.JadeStringUtil;

public class CodeSystemEnumUtils {

    private CodeSystemEnumUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Retourne l'énuméré correspondant au code système. Renvoie une {@link IllegalArgumentException} si la chaîne de
     * caractère passée en paramètre est invalide (null ou vide) ou si le code système ne correspond pas à un valeur de
     * cette énuméré.
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
     * Retourne l'énuméré correspondant au code système. Renvoie une {@link IllegalArgumentException} si le paramètre
     * est null ou si le code système ne correspond pas à un valeur de cette énuméré.
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
     * Vérifie que le code système est définit dans l'énume
     * 
     * @param CodSystem value
     * @param Enum class du code système
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