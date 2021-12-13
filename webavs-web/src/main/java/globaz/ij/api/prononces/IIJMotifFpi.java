package globaz.ij.api.prononces;

import globaz.globall.db.BSession;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum IIJMotifFpi {

    FORMATION_PROFESSIONNELLE_SUPERIEURE("1", "MOTIF_FPI_SUPERIEURE"),
    FPI_AVEC_CONTRAT_APPRENTISSAGE("2", "MOTIF_FPI_AVEC_CONTRAT"),
    FPI_SANS_CONTRAT_APPRENTISSAGE("3", "MOTIF_FPI_SANS_CONTRAT");

    @Getter
    private String code;
    private String label;

    public static Optional<IIJMotifFpi> findByCode(String code) {
        return (code == null) ? Optional.empty() : Arrays.stream(IIJMotifFpi.values()).filter(motif -> code.equals(motif.code)).findFirst();
    }

    public final String getLabel(BSession session) {
        return session.getLabel(label);
    }

}
