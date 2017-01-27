package ch.globaz.pegasus.business.services.synchronisation;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;

public class MembresFamillesToSynchronise {
    private final List<MembreFamilleVO> toAdd = new ArrayList<MembreFamilleVO>();
    private final List<MembreFamilleVO> toDelete = new ArrayList<MembreFamilleVO>();

    public MembresFamillesToSynchronise() {

    }

    public MembresFamillesToSynchronise(List<MembreFamilleVO> toAdd, List<MembreFamilleVO> toDelete) {
        if (toDelete != null) {
            this.toDelete.addAll(toDelete);
        }
        if (toAdd != null) {
            this.toAdd.addAll(toAdd);
        }
    }

    public List<MembreFamilleVO> getToDelete() {
        return toDelete;
    }

    public List<MembreFamilleVO> getToAdd() {
        return toAdd;
    }
}
