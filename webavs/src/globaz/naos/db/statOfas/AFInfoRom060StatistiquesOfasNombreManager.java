package globaz.naos.db.statOfas;

import globaz.globall.db.BEntity;
import globaz.naos.db.affiliation.AFAffiliation;
import java.io.Serializable;

public class AFInfoRom060StatistiquesOfasNombreManager extends AFInfoRom060StatistiquesOfasManager implements
        Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Méthodes
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliation();
    }

}
