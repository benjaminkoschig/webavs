package ch.globaz.vulpecula.repositoriesjade.congepaye;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.models.congepaye.CongePayeComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.CongePayeSearchComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.CongePayeSimpleModel;
import ch.globaz.vulpecula.business.models.congepaye.TauxCongePayeComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.TauxCongePayeSearchComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.TauxCongePayeSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.congepaye.TauxCongePaye;
import ch.globaz.vulpecula.domain.repositories.congepaye.CongePayeRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.congepaye.converters.CongePayeConverter;
import ch.globaz.vulpecula.repositoriesjade.congepaye.converters.TauxCongePayeConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.prestations.ParamQualifHolder;

/**
 * Implémentation Jade du repository pour les conges payes
 * 
 * @since WebBMS 0.01.04
 */
public class CongePayeRepositoryJade extends RepositoryJade<CongePaye, CongePayeComplexModel, CongePayeSimpleModel>
        implements CongePayeRepository {

    @Override
    public DomaineConverterJade<CongePaye, CongePayeComplexModel, CongePayeSimpleModel> getConverter() {
        return new CongePayeConverter();
    }

    @Override
    public CongePaye create(CongePaye entity) {
        CongePaye congePaye = super.create(entity);

        for (TauxCongePaye tauxCongePaye : congePaye.getTauxCongePayes()) {
            TauxCongePayeSimpleModel tauxCongePayeSimpleModel = TauxCongePayeConverter.getInstance()
                    .convertToPersistence(tauxCongePaye);
            tauxCongePayeSimpleModel.setIdCongePaye(congePaye.getId());
            try {
                if (tauxCongePayeSimpleModel.getIdAssurance() != null
                        && tauxCongePayeSimpleModel.getIdAssurance().length() != 0) {
                    JadePersistenceManager.add(tauxCongePayeSimpleModel);
                }
            } catch (JadePersistenceException ex) {
                logger.error(ex.toString());
            }
        }

        return congePaye;
    }

    @Override
    public CongePaye findById(String id) {
        CongePayeSearchComplexModel searchModel = new CongePayeSearchComplexModel();
        searchModel.setForId(id);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<CongePaye> findByIdTravailleur(String idTravailleur) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        searchComplexModel.setForIdTravailleur(idTravailleur);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<CongePaye> findByIdTravailleurOrderByIdpassage(String idTravailleur) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        searchComplexModel.setForIdTravailleur(idTravailleur);
        searchComplexModel.setOrderKey(CongePayeSearchComplexModel.ORDERBY_ID_PASSAGE_FACTURATION_DESC);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<CongePaye> findForFacturation(String idPassage) {
        return findByIdPassage(idPassage);
    }

    @Override
    public List<CongePaye> findByIdPassage(String idPassage) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        searchComplexModel.setForIdPassage(idPassage);

        List<CongePaye> congesPayes = searchAndFetch(searchComplexModel);

        for (CongePaye congePaye : congesPayes) {
            loadDependancies(congePaye);
        }

        return congesPayes;
    }

    @Override
    public List<CongePaye> findByIdPassage(String idPassage, String idEmployeur) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        searchComplexModel.setForIdPassage(idPassage);
        searchComplexModel.setForIdEmployeur(idEmployeur);

        List<CongePaye> congesPayes = searchAndFetch(searchComplexModel);

        for (CongePaye congePaye : congesPayes) {
            loadDependancies(congePaye);
        }

        return congesPayes;
    }

    @Override
    public CongePaye findByIdWithDependancies(String idCongePaye) {
        CongePaye congePaye = findById(idCongePaye);
        loadDependancies(congePaye);
        return congePaye;
    }

    private void loadDependancies(CongePaye congePaye) {
        loadTaux(congePaye);
    }

    @Override
    public List<CongePaye> findBy(String idPassage, String idEmployeur, String idTravailleur, String idConvention) {
        return findBy(idPassage, idEmployeur, idTravailleur, idConvention,
                CongePayeSearchComplexModel.ORDER_BY_CONVENTION_ASC);
    }

    @Override
    public List<CongePaye> findBy(String idPassage, String idEmployeur, String idTravailleur, String idConvention,
            String orderBy) {
        ParamQualifHolder ph = new ParamQualifHolder();

        CongePayeSearchComplexModel searchModel = new CongePayeSearchComplexModel();
        searchModel.setForIdPassage(idPassage);
        searchModel.setForIdTravailleur(idTravailleur);
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForIdConvention(idConvention);
        List<CongePaye> congesPayes = searchAndFetch(searchModel);

        for (CongePaye congePaye : congesPayes) {
            ph.setParametresQualficiationTo(congePaye);
            loadTaux(congePaye);
        }

        return congesPayes;
    }

    private void loadTaux(CongePaye congePaye) {
        List<TauxCongePaye> taux = new ArrayList<TauxCongePaye>();

        TauxCongePayeSearchComplexModel searchModel = new TauxCongePayeSearchComplexModel();
        searchModel.setForIdCongePaye(congePaye.getId());
        try {
            JadePersistenceManager.search(searchModel);
            for (int i = 0; i < searchModel.getSize(); i++) {
                TauxCongePayeComplexModel tauxCongePayeComplexModel = (TauxCongePayeComplexModel) searchModel
                        .getSearchResults()[i];
                TauxCongePaye tauxCongePaye = TauxCongePayeConverter.getInstance().convertToDomain(
                        tauxCongePayeComplexModel);
                taux.add(tauxCongePaye);
            }
        } catch (JadePersistenceException ex) {
            logger.error(ex.getMessage());
        }
        congePaye.setTauxCongePayes(taux);
    }

    @Override
    public List<CongePaye> findBy(String idPassageFacturation, String idEmployeur, String idTravailleur,
            String idConvention, Periode periode) {
        ParamQualifHolder ph = new ParamQualifHolder();

        CongePayeSearchComplexModel searchModel = new CongePayeSearchComplexModel();
        if (periode != null) {
            searchModel.setForDateDebut(periode.getDateDebutAsSwissValue());
            searchModel.setForDateFin(periode.getDateFinAsSwissValue());
        }
        searchModel.setForIdTravailleur(idTravailleur);
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForIdConvention(idConvention);
        searchModel.setWhereKey(CongePayeSearchComplexModel.WHERE_WITHDATE);
        List<CongePaye> congesPayes = searchAndFetch(searchModel);

        for (CongePaye congePaye : congesPayes) {
            ph.setParametresQualficiationTo(congePaye);
            loadTaux(congePaye);
        }

        return congesPayes;
    }
}
