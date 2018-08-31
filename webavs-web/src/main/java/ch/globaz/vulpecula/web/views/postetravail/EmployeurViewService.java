package ch.globaz.vulpecula.web.views.postetravail;

import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.employeur.EmployeurService;
import ch.globaz.vulpecula.domain.models.registre.TypeFacturation;

public class EmployeurViewService {
    private final EmployeurService employeurService = VulpeculaServiceLocator.getEmployeurService();

    public String changeTypeFacturation(String idEmployeur, String typeFacturationAsString) {
        TypeFacturation typeFacturation = TypeFacturation.fromValue(typeFacturationAsString);
        return employeurService.changeTypeFacturation(idEmployeur, typeFacturation);
    }

    public boolean changeEnvoiBVRSansDecompte(String idEmployeur, boolean activated) {
        return employeurService.changeEnvoiBVRSansDecompte(idEmployeur, activated);
    }
}
