package ch.globaz.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class utilitaire pour faciliter les tests des enums.
 */
@SuppressWarnings("unused")
public class Enums {

    /**
     * Permet de tester un fonction d'une enum qui indique si il s'agit de la valeur de l'énum.
     * <pre>
     * Exemple: Fonction qui doit être testé : myEnum.isToto()
     * enum MyEnum {
     *     TOTO, TAT
     *     boolean isToto() {
     *         return this == TOTO;
     *     }
     * }
     * </pre>
     *
     * @param enumClass La class de l'enum à tester-
     * @param predicate La fonction de l'enum que l'on veut tester.
     * @param enumValid Etat de l'énum que la fonction test.
     * @param <T>       Type de l'enum que l'on veut tester.
     */
    public static <T extends Enum<T>> void assertFunctionEqualEnum(final Class<T> enumClass, final Predicate<T> predicate, final T enumValid) {

        assertThat(predicate.test(enumValid))
                .describedAs("L'enum %s n'est pas juste, devrait être true", enumValid)
                .isTrue();

        Arrays.stream(enumClass.getEnumConstants())
              .filter(currentEnum -> !enumValid.equals(currentEnum))
              .forEach(currentEnum -> assertFalse(predicate, currentEnum));

    }

    /**
     * Permet de tester un fonction d'une enum qui indique si il s'agit des enums qui respectent la fonction.
     * <pre>
     * Exemple: Fonction qui doit être testé : myEnum.isTotoTata()
     * enum MyEnum {
     *     TOTO, TAT
     *     boolean isTotoTata() {
     *         return this == TOTO || this == TATA;
     *     }
     * }
     * </pre>
     *
     * @param enumClass  La class de l'enum à tester-
     * @param predicate  La fonction de l'enum que l'on veut tester.
     * @param enumsValid Etat des énums que la fonction test.
     * @param <T>        Type de l'enum que l'on veut tester.
     */
    @SafeVarargs
    public static <T extends Enum<T>> void assertFunctionEqualEnum(final Class<T> enumClass, final Predicate<T> predicate, final T... enumsValid) {
        List<T> enums = Arrays.asList(enumsValid);

        assertThat(enums.stream().allMatch(predicate))
                .describedAs("Un ou des enum(s) parmis eux %s n'est / ne sont pas juste(s)",
                             enums.stream().map(Objects::toString).collect(Collectors.joining(",")))
                .isTrue();

        Arrays.stream(enumClass.getEnumConstants())
              .filter(currentEnum -> !enums.contains(currentEnum))
              .forEach(currentEnum -> assertFalse(predicate, currentEnum));

    }

    private static <T extends Enum<T>> void assertFalse(final Predicate<T> predicate, final T currentEnum) {
        assertThat(predicate.test(currentEnum))
                .describedAs("L'enum %s n'est pas juste, devrait être false", currentEnum)
                .isFalse();
    }
}

