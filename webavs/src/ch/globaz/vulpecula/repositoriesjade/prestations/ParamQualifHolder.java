package ch.globaz.vulpecula.repositoriesjade.prestations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.business.models.qualification.ConventionQualificationSearchSimpleModel;
import ch.globaz.vulpecula.business.models.qualification.ConventionQualificationSimpleModel;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.prestations.Prestation;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.registre.converters.ConventionQualificationConverter;

public class ParamQualifHolder {
    protected Map<Pair<String, Qualification>, ConventionQualification> parametres = new HashMap<Pair<String, Qualification>, ConventionQualification>();

    /**
     * Ajoute le paramétrage de la convention au poste de travail.
     * 
     * @param prestation
     * @return
     */
    public void setParametresQualficiationTo(Prestation prestation) {
        if (parametres.isEmpty()) {
            find();
        }

        Pair<String, Qualification> pair = new Pair<String, Qualification>(prestation.getConventionEmployeur().getId(),
                prestation.getQualification());
        ConventionQualification parametre = parametres.get(pair);
        if (parametre != null) {
            prestation.getPosteTravail().setParametresQualifications(Arrays.asList(parametre));
        }
    }

    private void find() {
        ConventionQualificationSearchSimpleModel searchModel = new ConventionQualificationSearchSimpleModel();
        List<ConventionQualificationSimpleModel> conventionQualifications = RepositoryJade
                .searchForAndFetch(searchModel);
        for (ConventionQualificationSimpleModel conventionQualificationSimpleModel : conventionQualifications) {
            ConventionQualification conventionQualification = ConventionQualificationConverter.getInstance()
                    .convertToDomain(conventionQualificationSimpleModel);
            Pair<String, Qualification> pair = new Pair<String, Qualification>(
                    conventionQualification.getIdConvention(), conventionQualification.getQualification());
            parametres.put(pair, conventionQualification);
        }
    }
}
