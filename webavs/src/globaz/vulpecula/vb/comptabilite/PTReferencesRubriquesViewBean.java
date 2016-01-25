package globaz.vulpecula.vb.comptabilite;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.web.views.comptabilite.ReferencesRubriquesViewService;

public class PTReferencesRubriquesViewBean extends BJadeSearchObjectELViewBean {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String arg0) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getReferencesRubriquesViewService() {
        return ReferencesRubriquesViewService.class.getName();
    }

    public String getValidationMessageErreur() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("REFERENCE_RUBRIQUE_ERREUR");
    }
}
