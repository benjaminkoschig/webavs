package ch.globaz.vulpecula.documents.decompte;

import java.io.Serializable;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;

/**
 * @author Arnaud Geiser (AGE) | Créé le 27 mars 2014
 * 
 */
public class DecompteContainer implements Serializable {
    private static final long serialVersionUID = 5834906273460064555L;

    private final TypeDecompte typeDecompte;
    private Decompte decompte;
    private CotisationsInfo cotisationsInfo;

    public DecompteContainer(final TypeDecompte typeDecompte) {
        this.typeDecompte = typeDecompte;
    }

    public Decompte getDecompte() {
        return decompte;
    }

    public void setDecompte(final Decompte decompte) {
        this.decompte = decompte;
    }

    public CotisationsInfo getCotisationsInfo() {
        return cotisationsInfo;
    }

    public void setCotisationsInfo(final CotisationsInfo cotisationsInfo) {
        this.cotisationsInfo = cotisationsInfo;
    }

    public TypeDecompte getTypeDecompte() {
        return typeDecompte;
    }
}
