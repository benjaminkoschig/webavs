package globaz.prestation.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.function.Function;

@AllArgsConstructor
@Getter
public enum PRTypeDemande {

    APG(IPRDemande.CS_TYPE_APG),
    MATERNITE(IPRDemande.CS_TYPE_MATERNITE),
    PANDEMIE(IPRDemande.CS_TYPE_PANDEMIE),
    PATERNITE(IPRDemande.CS_TYPE_PATERNITE),
    PROCHE_AIDANT(IPRDemande.CS_TYPE_PROCHE_AIDANT);

    private final String csType;

    /**
     * Permet de savoir si l'enum est de type proche aidant.
     *
     * @return si l'enum est de type proche aidant.
     */
    public boolean isProcheAidant() {
        return this == PROCHE_AIDANT;
    }

    /**
     * Permet de savoir si l'enum est de type pand�mie.
     *
     * @return si l'enum est de type pand�mie.
     */
    public boolean isPandemie() {
        return this == PANDEMIE;
    }

    /**
     * Permet de savoir si l'enum est de type maternit�.
     *
     * @return si l'enum est de type maternit�.
     */
    public boolean isMaternite() {
        return this == MATERNITE;
    }

    /**
     * Permet de savoir si l'enum est de type APG.
     *
     * @return si l'enum est de type APG.
     */
    public boolean isApg() {
        return this == APG;
    }

    /**
     * Permet de savoir si l'enum est de type paternit�.
     *
     * @return si l'enum est de type /**.
     */
    public boolean isPaternite() {
        return this == PATERNITE;
    }

    /**
     * Converti le code syst�me en enum.
     *
     * @param codeSystem Le code syst�me � convertir.
     *
     * @return L'enum correspondante aux code syst�me.
     */
    public static PRTypeDemande toEnumByCs(String codeSystem) {
        return toEnum(codeSystem, PRTypeDemande.class, PRTypeDemande::getCsType);
    }

    private static <T extends Enum<?>> T toEnum(final String codeSystem, Class<T> enumClass, Function<T, String> function) {
        return Arrays.stream(enumClass.getEnumConstants())
                     .filter(typeDemande -> function.apply(typeDemande).equals(codeSystem))
                     .findFirst()
                     .orElseThrow(() -> new EnumConstantNotPresentException(enumClass, codeSystem));
    }
}
