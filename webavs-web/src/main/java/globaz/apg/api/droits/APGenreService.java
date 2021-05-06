package globaz.apg.api.droits;

import ch.globaz.common.util.Enums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum APGenreService {
    PROCHE_AIDANT(IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT);

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
     * Converti le code système en enum.
     *
     * @param codeSystem Le code système à convertir.
     *
     * @return L'enum correspondante aux code système.
     */
    public static Optional<APGenreService> toEnumByCs(String codeSystem) {
        return Enums.toEnumWithoutException(codeSystem, APGenreService.class, APGenreService::getCsType);
    }
}
