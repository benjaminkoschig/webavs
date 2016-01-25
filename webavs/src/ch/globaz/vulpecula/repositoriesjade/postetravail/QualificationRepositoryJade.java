package ch.globaz.vulpecula.repositoriesjade.postetravail;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.models.qualification.ConventionQualificationSearchSimpleModel;
import ch.globaz.vulpecula.business.models.qualification.ConventionQualificationSimpleModel;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.repositories.postetravail.QualificationRepository;

/**
 * Implémentation Jade de {@link QualificationRepository}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 17 janv. 2014
 * 
 */
public class QualificationRepositoryJade implements QualificationRepository {

    @Override
    public List<Qualification> findByIdConvention(String idConvention) {
        List<Qualification> qualifications = new ArrayList<Qualification>();

        try {
            ConventionQualificationSearchSimpleModel searchModel = new ConventionQualificationSearchSimpleModel();
            searchModel.setForIdConvention(idConvention);
            searchModel = (ConventionQualificationSearchSimpleModel) JadePersistenceManager.search(searchModel);
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                ConventionQualificationSimpleModel conventionQualificationSimpleModel = (ConventionQualificationSimpleModel) model;
                Qualification qualification = Qualification.fromValue(conventionQualificationSimpleModel
                        .getQualification());
                qualifications.add(qualification);
            }
        } catch (JadePersistenceException e) {
            e.printStackTrace();
        }

        return qualifications;
    }

    @Override
    public List<String> findByIdConventionAsString(String idConvention) {
        List<String> qualificationsAsString = new ArrayList<String>();
        List<Qualification> qualifications = findByIdConvention(idConvention);
        for (Qualification qualification : qualifications) {
            qualificationsAsString.add(qualification.getValue());
        }
        return qualificationsAsString;
    }
}
