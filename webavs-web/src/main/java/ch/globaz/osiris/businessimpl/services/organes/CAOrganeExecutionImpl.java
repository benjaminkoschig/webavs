package ch.globaz.osiris.businessimpl.services.organes;

import globaz.globall.db.BSession;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import java.util.Vector;

public class CAOrganeExecutionImpl {

    public CAOrganeExecutionImpl() {

    }

    public Vector<String[]> getAllOrganesExecution(BSession session) {
        Vector<String[]> result = new Vector<String[]>();

        CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
        mgr.setForIdTypeTraitementOG(true);
        mgr.setSession(session);

        try {
            mgr.find();
        } catch (Exception e) {
            return result;
        }

        CAOrganeExecution organeExecution = null;
        for (int i = 0; i < mgr.size(); i++) {
            organeExecution = (CAOrganeExecution) mgr.getEntity(i);
            result.add(new String[] { organeExecution.getIdOrganeExecution(), organeExecution.getNom() });
        }

        return result;
    }
}
