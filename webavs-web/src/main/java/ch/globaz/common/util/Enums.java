package ch.globaz.common.util;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Class utilitaire pour traiter les enums.
 **/
public class Enums {

    /**
     * Cette fonction permet de convertir une enum depuis un codeSystem ou une autre valeur.
     *
     * @param codeSystem Le code système(ou autre objet) que l'on veut convertir en enum.
     * @param enumClass  Le type de l'enum sa class.
     * @param function   Fonction dans l'enum qui retourne de codeSystem
     * @param <T>        Le type de l'enum
     * @param <K>        Le type du codeSystem.
     *
     * @return L'enum.
     */
    public static <T extends Enum<?>, K> T toEnum(final K codeSystem, Class<T> enumClass, Function<T, K> function) {
        return Arrays.stream(enumClass.getEnumConstants())
                     .filter(typeDemande -> function.apply(typeDemande).equals(codeSystem))
                     .findFirst()
                     .orElseThrow(() -> new EnumConstantNotPresentException(enumClass, String.valueOf(codeSystem)));
    }
}
