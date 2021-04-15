package globaz.corvus.acor2020.business;

import java.util.ArrayList;
import java.util.List;

public class RCIContainer {

    private List idsRCI = new ArrayList();
    private int previousMotif = -1;

    public void addRCI(int motif, String idRCI) {

        if (previousMotif < 0) {
            previousMotif = motif;
            idsRCI.add(idRCI);
        } else {
            // Le motif du rci précédent est un extrait de CI.
            if (isExtrait(previousMotif)) {
                // Le motif du rci courant est un extrait de CI.
                if (isExtrait(motif)) {
                    previousMotif = motif;
                    return;
                }
                // Le motif du rci courant est un rassemblement définitif.
                else {
                    previousMotif = motif;
                    idsRCI.add(idRCI);
                }
            }
            // Le motif du rci précédent est un rassemblement définitif
            else {
                if (!isExtrait(motif)) {
                    idsRCI.add(idRCI);
                }
            }
        }
    }

    public void addRCIAdditionnel(int motif, String idRCI) {
        idsRCI.add(idRCI);
    }

    public List getIdsRCI() {
        return idsRCI;
    }

    private boolean isExtrait(int motif) {
        if ((motif >= 92) && (motif <= 99)) {
            return true;
        }
        return false;
    }
}
