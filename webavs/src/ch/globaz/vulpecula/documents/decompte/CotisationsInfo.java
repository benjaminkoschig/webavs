package ch.globaz.vulpecula.documents.decompte;

import java.io.Serializable;
import ch.globaz.vulpecula.domain.models.common.Taux;

/**
 * @author Arnaud Geiser (AGE) | Créé le 27 mars 2014
 * 
 */
public class CotisationsInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Taux tauxAVS;
    private final Taux tauxAC;
    private final Taux tauxAC2;

    public CotisationsInfo(Taux tauxAVS, Taux tauxAC, Taux tauxAC2) {
        this.tauxAVS = tauxAVS;
        this.tauxAC = tauxAC;
        this.tauxAC2 = tauxAC2;
    }

    public Taux getTauxAVS() {
        return tauxAVS;
    }

    public Taux getTauxAC() {
        return tauxAC;
    }

    public Taux getTauxAC2() {
        return tauxAC2;
    }
}
