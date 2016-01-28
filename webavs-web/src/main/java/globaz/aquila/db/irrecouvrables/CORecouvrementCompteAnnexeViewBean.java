package globaz.aquila.db.irrecouvrables;

import globaz.aquila.application.COApplication;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;
import globaz.osiris.external.IntRole;

/**
 * @author sch
 */
public class CORecouvrementCompteAnnexeViewBean extends CACompteAnnexeViewBean implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public IntRole getRole() {
        if (intRole == null) {
            try {
                CAApplication currentApplication = (CAApplication) ((COApplication) getSession().getApplication())
                        .getApplicationOsiris();
                intRole = (IntRole) GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(getSession(), IntRole.class);
                intRole.setISession(getSession());
                if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                    intRole.retrieve(getIdTiers(), getIdRole(), null);
                } else {
                    intRole.retrieve(getIdRole(), getIdExterneRole());
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return null;
            }
        }
        return intRole;
    }
}
