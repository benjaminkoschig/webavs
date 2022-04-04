package globaz.ij.api.prononces;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
@AllArgsConstructor
public enum IJGenrePrestationPetiteIJ {

    OBSERVATION_INSTRUCTION( "1", "1"),
    DELAI_ATTENTE("2", "2"),
    MESURE_MEDICAL("3", "6"),
    FORMATION_SCOLAIRE("4", "4"),
    FORMATION_PROFESSIONELLE("5", "5"),
    RECLASSEMENT_PROFESSIONELLE( "6", "4"),
    ATTENTE( "7", "3"),
    MISE_AU_COURANT( "8", "8"),
    MESURE_REINSERTION( "9", "4");

    @Getter
    private String codeAnnnonce;
    @Getter
    private String codeMappingPetiteIJ;

    public static Optional<IJGenrePrestationPetiteIJ> findByCode(String code) {
        return (code == null) ? Optional.empty() : Arrays.stream(IJGenrePrestationPetiteIJ.values()).filter(genre -> code.equals(genre.codeAnnnonce)).findFirst();
    }

    public static String convertCode(String code) {
        Optional<IJGenrePrestationPetiteIJ> genre = findByCode(code);
        return genre.isPresent() ? genre.get().getCodeMappingPetiteIJ() : code;
    }

}

