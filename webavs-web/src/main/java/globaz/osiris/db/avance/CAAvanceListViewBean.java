package globaz.osiris.db.avance;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrementManager;
import java.util.ArrayList;

/**
 * Class extends de CAPlanRecouvrementManager. <br>
 * List les plan de recouvrement uniquement de type avance.
 * 
 * @author dda
 */
public class CAAvanceListViewBean extends CAPlanRecouvrementManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public CAAvanceListViewBean() {
        super();

        ArrayList idModeRecouvrementIn = new ArrayList();
        idModeRecouvrementIn.add(CAPlanRecouvrement.CS_AVANCE_APG);
        idModeRecouvrementIn.add(CAPlanRecouvrement.CS_AVANCE_RENTES);
        idModeRecouvrementIn.add(CAPlanRecouvrement.CS_AVANCE_IJAI);
        idModeRecouvrementIn.add(CAPlanRecouvrement.CS_AVANCE_PTRA);

        setForIdModeRecouvrementIn(idModeRecouvrementIn);

        setForIdModeRecouvrementNotIn(null);
    }

    /**
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrementManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAAvanceViewBean();
    }

}
