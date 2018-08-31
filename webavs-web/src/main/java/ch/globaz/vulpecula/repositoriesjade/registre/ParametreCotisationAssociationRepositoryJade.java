package ch.globaz.vulpecula.repositoriesjade.registre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSearchComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.CategorieFactureAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;
import ch.globaz.vulpecula.domain.models.registre.TypeParamCotisationAP;
import ch.globaz.vulpecula.domain.repositories.registre.ParametreCotisationAssociationRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.registre.converters.ParametreCotisationAssociationConverter;

public class ParametreCotisationAssociationRepositoryJade
        extends
        RepositoryJade<ParametreCotisationAssociation, ParametreCotisationAssociationComplexModel, ParametreCotisationAssociationSimpleModel>
        implements ParametreCotisationAssociationRepository {

    @Override
    public DomaineConverterJade<ParametreCotisationAssociation, ParametreCotisationAssociationComplexModel, ParametreCotisationAssociationSimpleModel> getConverter() {
        return ParametreCotisationAssociationConverter.getInstance();
    }

    @Override
    public List<ParametreCotisationAssociation> findAll() {
        ParametreCotisationAssociationSearchComplexModel searchModel = new ParametreCotisationAssociationSearchComplexModel();
        return searchAndFetch(searchModel);
    }

    @Override
    public List<ParametreCotisationAssociation> findCotisationsForFourchette(ParametreCotisationAssociation cotisationCM) {
        ParametreCotisationAssociationSearchComplexModel searchModel = new ParametreCotisationAssociationSearchComplexModel();
        searchModel.setForIdCotisationAssociationProfessionnelle(cotisationCM
                .getIdCotisationAssociationProfessionnelle());
        return searchAndFetch(searchModel);
    }

    @Override
    public List<ParametreCotisationAssociation> findForFourchetteAndIdCotisation(
            String idCotisationAssociationProfessionnelle, Montant montant) {
        ParametreCotisationAssociationSearchComplexModel searchModel = new ParametreCotisationAssociationSearchComplexModel();
        searchModel.setForIdCotisationAssociationProfessionnelle(idCotisationAssociationProfessionnelle);
        searchModel.setForMontantFourchette(montant.getValue());
        return searchAndFetch(searchModel);
    }

    @Override
    public boolean isAuMoinsUnParametreForIdCotisation(String idCotisationAssociationProfessionnelle)
            throws JadePersistenceException {
        ParametreCotisationAssociationSearchComplexModel searchModel = new ParametreCotisationAssociationSearchComplexModel();
        searchModel.setForIdCotisationAssociationProfessionnelle(idCotisationAssociationProfessionnelle);
        Collection<String> categories = new ArrayList<String>();
        categories.add(CategorieFactureAssociationProfessionnelle.A_FACTURER.getValue());
        categories.add(CategorieFactureAssociationProfessionnelle.RABAIS_SPECIAL.getValue());
        searchModel.setForFacturerDefautIn(categories);
        if (JadePersistenceManager.count(searchModel) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ParametreCotisationAssociation> findForFourchetteInferieuresAndIdCotisation(
            String idCotisationAssociationProfessionnelle, Montant montant) {
        ParametreCotisationAssociationSearchComplexModel searchModel = new ParametreCotisationAssociationSearchComplexModel();
        searchModel.setForIdCotisationAssociationProfessionnelle(idCotisationAssociationProfessionnelle);
        searchModel.setForFourchetteInferieure(montant.getValue());
        searchModel.setForTypeParametre(TypeParamCotisationAP.TAUX_CUMULATIF.getValue());
        return searchAndFetch(searchModel);
    }
}
