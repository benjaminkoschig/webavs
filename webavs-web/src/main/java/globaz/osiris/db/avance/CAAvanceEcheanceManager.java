package globaz.osiris.db.avance;

import globaz.globall.db.BEntity;
import globaz.osiris.db.access.recouvrement.CAEcheancePlanManager;

/**
 * Class extends de CAEcheancePlanManager. <br>
 * Fonctionnement identique au manager étendu hormis les nouvelles entités créées qui seront de type CAAvanceEcheance.
 * 
 * @author dda
 */
public class CAAvanceEcheanceManager extends CAEcheancePlanManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.osiris.db.access.recouvrement.CAEcheancePlanManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAAvanceEcheance();
    }

}
