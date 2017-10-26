package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class DonneeFinanciereHeader extends DonneeFinanciere {

    public DonneeFinanciereHeader(DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
    }

    public DonneeFinanciereHeader(RoleMembreFamille membreFamille, Date debut, Date fin, String id,
            String idDroitMembreFamille) {
        super(membreFamille, debut, fin, id, idDroitMembreFamille);
    }

    @Override
    protected void definedTypeDonneeFinanciere() {

    }

}
