package globaz.ij.api.prononces;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum IJGenrePrestation {

    EXAMEN_INSTRUCTION("1", true),
    DELAI_RECLASSEMENT("2", true),
    DELAI_RECHERCHE("3", true),
    IJ_ART14("4", true),
    FPI("5", false),
    MESURE_MEDICAL("6", false),
    FORMATION_PROFESSIONELLE("7", false),
    FORMATION_REPARATOIRE("8", false),
    IJ_ART22_12("9", false),
    IJ_ART22_14("10", false);

    @Getter
    private String codeAnnnonce;
    private boolean revenuDeterminant;

    public static Optional<IJGenrePrestation> findByCode(String code) {
        return (code == null) ? Optional.empty() : Arrays.stream(IJGenrePrestation.values()).filter(genre -> code.equals(genre.codeAnnnonce)).findFirst();
    }

    public static boolean calculRevenuDeterminant(String code) {
        Optional<IJGenrePrestation> genre = findByCode(code);
        return genre.isPresent() ? genre.get().revenuDeterminant : false;
    }

}

