package ch.globaz.vulpecula.web.views.registre;

import java.util.List;
import java.util.Vector;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.web.util.FormUtil;

/**
 * Services sur les conventions utilisés dans les vues.
 * 
 * @since Web@BMS 0.01.02
 */
public class ConventionViewService {
    public static Vector<String[]> getAllConventions() {
        Vector<String[]> listConventions = FormUtil.getEmptyList();

        List<Convention> conventions = VulpeculaRepositoryLocator.getConventionRepository().findAll();
        for (Convention convention : conventions) {
            String[] element = new String[2];
            element[0] = convention.getId();
            element[1] = (convention.getCode() != null ? convention.getCode() + " - " : "")
                    + convention.getDesignation();
            listConventions.add(element);
        }
        return listConventions;
    }
}
