package ch.globaz.pegasus.process.adaptation;

import java.util.List;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAIdMembreFamilleRetenuSearch;

public interface AdaptationRunFunction {
    public void run(PCAIdMembreFamilleRetenuSearch pcAIdMembreFamilleRetenuSearch, Droit droitACalculer,
            List<String> listeMb);
}
