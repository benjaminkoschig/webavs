package ch.globaz.vulpecula.process.caissemaladie;

import java.util.Collection;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class SuiviCaisseMaladieStandardProcess extends SuiviCaisseMaladieAbstractProcess {
    private static final long serialVersionUID = -2306608365506273222L;

    @Override
    protected Map<Administration, Collection<SuiviCaisseMaladie>> getSuivis() {
        return VulpeculaServiceLocator.getSuiviCaisseMaladieService().findSuivisStandardNonEnvoyeesGroupByCaisseAF();
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.SUIVI_CAISSE_STANDARD_NAME;
    }

}