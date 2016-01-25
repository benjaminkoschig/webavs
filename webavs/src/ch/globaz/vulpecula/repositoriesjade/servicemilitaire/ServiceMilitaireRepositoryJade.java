package ch.globaz.vulpecula.repositoriesjade.servicemilitaire;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.business.models.servicemilitaire.ServiceMilitaireComplexModel;
import ch.globaz.vulpecula.business.models.servicemilitaire.ServiceMilitaireSearchComplexModel;
import ch.globaz.vulpecula.business.models.servicemilitaire.ServiceMilitaireSimpleModel;
import ch.globaz.vulpecula.business.models.servicemilitaire.TauxServiceMilitaireComplexModel;
import ch.globaz.vulpecula.business.models.servicemilitaire.TauxServiceMilitaireSearchComplexModel;
import ch.globaz.vulpecula.business.models.servicemilitaire.TauxServiceMilitaireSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.models.servicemilitaire.TauxServiceMilitaire;
import ch.globaz.vulpecula.domain.repositories.servicemilitaire.ServiceMilitaireRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.prestations.ParamQualifHolder;
import ch.globaz.vulpecula.repositoriesjade.servicemilitaire.converters.ServiceMilitaireConverter;
import ch.globaz.vulpecula.repositoriesjade.servicemilitaire.converters.TauxServiceMilitaireConverter;

public class ServiceMilitaireRepositoryJade extends
        RepositoryJade<ServiceMilitaire, ServiceMilitaireComplexModel, ServiceMilitaireSimpleModel> implements
        ServiceMilitaireRepository {

    @Override
    public ServiceMilitaire create(ServiceMilitaire serviceMilitaire) {
        ServiceMilitaire serviceMilitaireCreated = super.create(serviceMilitaire);
        for (TauxServiceMilitaire tauxServiceMilitaire : serviceMilitaireCreated.getTauxServicesMilitaires()) {
            tauxServiceMilitaire.setIdServiceMilitaire(serviceMilitaireCreated.getId());
            TauxServiceMilitaireSimpleModel tauxServiceMilitaireSimpleModel = TauxServiceMilitaireConverter
                    .getInstance().convertToPersistence(tauxServiceMilitaire);
            try {
                JadePersistenceManager.add(tauxServiceMilitaireSimpleModel);
            } catch (JadePersistenceException ex) {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
            }
        }
        return serviceMilitaireCreated;
    }

    @Override
    public ServiceMilitaire findById(String id) {
        ServiceMilitaireSearchComplexModel searchModel = new ServiceMilitaireSearchComplexModel();
        searchModel.setForId(id);
        ServiceMilitaire serviceMilitaire = searchAndFetchFirst(searchModel);
        loadDependencies(serviceMilitaire);
        return serviceMilitaire;
    }

    @Override
    public DomaineConverterJade<ServiceMilitaire, ServiceMilitaireComplexModel, ServiceMilitaireSimpleModel> getConverter() {
        return ServiceMilitaireConverter.getInstance();
    }

    @Override
    public List<ServiceMilitaire> findForFacturation(String idPassageFacturation) {
        List<ServiceMilitaire> servicesMilitaires = findByIdPassage(idPassageFacturation);
        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            loadTaux(serviceMilitaire);
            // PrestationsHelper.loadParametrage(serviceMilitaire);
        }
        return servicesMilitaires;
    }

    @Override
    public List<ServiceMilitaire> findByIdPassage(String idPassage) {
        ServiceMilitaireSearchComplexModel searchModel = new ServiceMilitaireSearchComplexModel();
        searchModel.setForIdPassage(idPassage);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<ServiceMilitaire> findByIdPassage(String idPassage, String idEmployeur) {
        ServiceMilitaireSearchComplexModel searchModel = new ServiceMilitaireSearchComplexModel();
        searchModel.setForIdPassage(idPassage);
        searchModel.setForIdEmployeur(idEmployeur);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<ServiceMilitaire> findByIdTravailleur(String idTravailleur) {
        ServiceMilitaireSearchComplexModel searchModel = new ServiceMilitaireSearchComplexModel();
        searchModel.setForIdTravailleur(idTravailleur);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<ServiceMilitaire> findByIdTravailleurOrderByIdpassage(String idTravailleur) {
        ServiceMilitaireSearchComplexModel searchModel = new ServiceMilitaireSearchComplexModel();
        searchModel.setForIdTravailleur(idTravailleur);
        searchModel.setOrderKey(ServiceMilitaireSearchComplexModel.ORDERBY_ID_PASSAGE_FACTURATION_DESC);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<ServiceMilitaire> findBy(String idPassageFacturation, String idEmployeur, String idTravailleur,
            String idConvention, String orderBy) {
        ParamQualifHolder holder = new ParamQualifHolder();

        ServiceMilitaireSearchComplexModel searchComplexModel = new ServiceMilitaireSearchComplexModel();
        searchComplexModel.setForIdPassage(idPassageFacturation);
        searchComplexModel.setForIdTravailleur(idTravailleur);
        searchComplexModel.setForIdEmployeur(idEmployeur);
        searchComplexModel.setForIdConvention(idConvention);
        searchComplexModel.setOrderKey(orderBy);
        List<ServiceMilitaire> servicesMilitaires = searchAndFetch(searchComplexModel);

        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            loadTaux(serviceMilitaire);
            holder.setParametresQualficiationTo(serviceMilitaire);
        }

        return servicesMilitaires;
    }

    @Override
    public List<ServiceMilitaire> findBy(String idPassageFacturation, String idEmployeur, String idTravailleur,
            String idConvention, Periode periode) {
        ParamQualifHolder holder = new ParamQualifHolder();

        ServiceMilitaireSearchComplexModel searchComplexModel = new ServiceMilitaireSearchComplexModel();
        if (JadeStringUtil.isBlankOrZero(idPassageFacturation)) {
            searchComplexModel.setForIdPassage(idPassageFacturation);
        }
        searchComplexModel.setForIdTravailleur(idTravailleur);
        searchComplexModel.setForIdEmployeur(idEmployeur);
        searchComplexModel.setForIdConvention(idConvention);
        searchComplexModel.setForDateDebut(periode.getDateDebutAsSwissValue());
        searchComplexModel.setForDateFin(periode.getDateFinAsSwissValue());
        searchComplexModel.setWhereKey(ServiceMilitaireSearchComplexModel.WHERE_WITHDATE);
        searchComplexModel.setOrderKey(ServiceMilitaireSearchComplexModel.ORDER_BY_CONVENTION_ASC);
        List<ServiceMilitaire> servicesMilitaires = searchAndFetch(searchComplexModel);

        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            loadTaux(serviceMilitaire);
            holder.setParametresQualficiationTo(serviceMilitaire);
        }

        return servicesMilitaires;
    }

    @Override
    public List<ServiceMilitaire> findBy(String idPassageFacturation, String idEmployeur, String idTravailleur,
            String idConvention) {
        return findBy(idPassageFacturation, idEmployeur, idTravailleur, idConvention,
                ServiceMilitaireSearchComplexModel.ORDER_BY_CONVENTION_ASC);
    }

    private void loadDependencies(ServiceMilitaire serviceMilitaire) {
        loadTaux(serviceMilitaire);
    }

    private void loadTaux(ServiceMilitaire serviceMilitaire) {
        List<TauxServiceMilitaire> taux = new ArrayList<TauxServiceMilitaire>();

        TauxServiceMilitaireSearchComplexModel searchModel = new TauxServiceMilitaireSearchComplexModel();
        searchModel.setForIdServiceMilitaire(serviceMilitaire.getId());
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            JadePersistenceManager.search(searchModel);
            for (int i = 0; i < searchModel.getSize(); i++) {
                TauxServiceMilitaireComplexModel tauxServiceMilitaireComplexModel = (TauxServiceMilitaireComplexModel) searchModel
                        .getSearchResults()[i];
                TauxServiceMilitaire tauxServiceMilitaire = TauxServiceMilitaireConverter.getInstance()
                        .convertToDomain(tauxServiceMilitaireComplexModel);
                taux.add(tauxServiceMilitaire);

            }
        } catch (JadePersistenceException ex) {
            logger.error(ex.getMessage());
        }
        serviceMilitaire.setTauxServicesMilitaires(taux);
    }
}
