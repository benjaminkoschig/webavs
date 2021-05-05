package globaz.apg.api.droits;

import ch.globaz.common.util.Enums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
     * Converti le code syst�me en enum.
     *
     * @param codeSystem Le code syst�me � convertir.
     *
     * @return L'enum correspondante aux code syst�me.
     */
    public static APGenreService toEnumByCs(String codeSystem) {
        return Enums.toEnum(codeSystem, APGenreService.class, APGenreService::getCsType);
    }
}
