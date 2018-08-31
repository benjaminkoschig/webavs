package ch.globaz.vulpecula.repositoriesjade.congepaye;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.business.models.congepaye.CongePayeComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.CongePayeSearchComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.CongePayeSimpleModel;
import ch.globaz.vulpecula.business.models.congepaye.TauxCongePayeComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.TauxCongePayeSearchComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.TauxCongePayeSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.AnneeComptable;
import ch.globaz.vulpecula.domain.models.common.Date;
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
    public List<CongePaye> findByIdTravailleurAndPeriod(String idTravailleur, String dateDebut, String dateFin) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        searchComplexModel.setForIdTravailleur(idTravailleur);
        searchComplexModel.setForDateDebut(dateDebut);
        searchComplexModel.setForDateFin(dateFin);
        searchComplexModel.setWhereKey(CongePayeSearchComplexModel.WHERE_WITH_PERIODE_ABSENCE);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<CongePaye> findByIdTravailleurAndDatePassageFacturation(String idTravailleur, String dateDebut,
            String dateFin) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        searchComplexModel.setForIdTravailleur(idTravailleur);
        searchComplexModel.setForDateDebutPassage(dateDebut);
        searchComplexModel.setForDateFinPassage(dateFin);
        searchComplexModel.setWhereKey(CongePayeSearchComplexModel.WHERE_WITH_PERIODE_PASSAGE);
        List<CongePaye> congesPayes = searchAndFetch(searchComplexModel);
        loadTaux(congesPayes);
        return congesPayes;
    }

    @Override
    public List<CongePaye> findByIdTravailleurAndDatePassageFacturationAndIdEmployeur(String idTravailleur,
            String dateDebut, String dateFin, String idEmployeur) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        searchComplexModel.setForIdTravailleur(idTravailleur);
        searchComplexModel.setForDateDebutPassage(dateDebut);
        searchComplexModel.setForDateFinPassage(dateFin);
        searchComplexModel.setForIdEmployeur(idEmployeur);
        searchComplexModel.setWhereKey(CongePayeSearchComplexModel.WHERE_WITH_PERIODE_PASSAGE);
        List<CongePaye> congesPayes = searchAndFetch(searchComplexModel);
        loadTaux(congesPayes);
        return congesPayes;
    }

    @Override
    public List<CongePaye> findByIdTravailleurForDateVersement(String idTravailleur, String dateDebut, String dateFin) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        searchComplexModel.setForIdTravailleur(idTravailleur);
        searchComplexModel.setForDateDebutVersement(dateDebut);
        searchComplexModel.setForDateFinVersement(dateFin);
        searchComplexModel.setWhereKey(CongePayeSearchComplexModel.WHERE_WITH_PERIODE_VERSEMENT);
        List<CongePaye> congesPayes = searchAndFetch(searchComplexModel);
        loadTaux(congesPayes);
        return congesPayes;
    }

    @Override
    public List<CongePaye> findByIdTravailleurForDateVersementAndIdEmployeur(String idTravailleur, String dateDebut,
            String dateFin, String idEmployeur) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        searchComplexModel.setForIdTravailleur(idTravailleur);
        searchComplexModel.setForDateDebutVersement(dateDebut);
        searchComplexModel.setForDateFinVersement(dateFin);
        searchComplexModel.setForIdEmployeur(idEmployeur);
        searchComplexModel.setWhereKey(CongePayeSearchComplexModel.WHERE_WITH_PERIODE_VERSEMENT);
        List<CongePaye> congesPayes = searchAndFetch(searchComplexModel);
        loadTaux(congesPayes);
        return congesPayes;
    }

    @Override
    public List<CongePaye> findByIdEmployeurForDateVersementInAnnee(String idEmployeur, Annee annee) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        searchComplexModel.setForIdEmployeur(idEmployeur);
        searchComplexModel.setForDateDebutVersement(annee.getFirstDayOfYear());
        searchComplexModel.setForDateFinVersement(annee.getLastDayOfYear());
        searchComplexModel.setWhereKey(CongePayeSearchComplexModel.WHERE_WITH_PERIODE_VERSEMENT);
        List<CongePaye> congesPayes = searchAndFetch(searchComplexModel);
        loadTaux(congesPayes);
        return congesPayes;
    }

    @Override
    public List<CongePaye> findByIdEmployeurForDateVersementInAnnee(String idEmployeur, Date dateDebut, Date dateFin) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        searchComplexModel.setForIdEmployeur(idEmployeur);
        searchComplexModel.setForDateDebutVersement(dateDebut);
        searchComplexModel.setForDateFinVersement(dateFin);
        searchComplexModel.setWhereKey(CongePayeSearchComplexModel.WHERE_WITH_PERIODE_VERSEMENT);
        List<CongePaye> congesPayes = searchAndFetch(searchComplexModel);
        loadTaux(congesPayes);
        return congesPayes;
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
                CongePayeSearchComplexModel.ORDERBY_CONVENTION_RAISONSOCIALE_ASC);
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
        searchModel.setOrderKey(CongePayeSearchComplexModel.ORDERBY_CONVENTION_RAISONSOCIALE_ASC);
        List<CongePaye> congesPayes = searchAndFetch(searchModel);

        for (CongePaye congePaye : congesPayes) {
            ph.setParametresQualficiationTo(congePaye);
            loadTaux(congePaye);
        }

        return congesPayes;
    }

    private void loadTaux(List<CongePaye> congesPayes) {
        for (CongePaye congePaye : congesPayes) {
            loadTaux(congePaye);
        }
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
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
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
        searchModel.setOrderKey(CongePayeSearchComplexModel.ORDERBY_CONVENTION_RAISONSOCIALE_ASC);
        List<CongePaye> congesPayes = searchAndFetch(searchModel);

        for (CongePaye congePaye : congesPayes) {
            ph.setParametresQualficiationTo(congePaye);
            loadTaux(congePaye);
        }

        return congesPayes;
    }

    @Override
    public List<CongePaye> findSalairesPourAnnee(Annee annee, String idConvention) {
        CongePayeSearchComplexModel searchComplexModel = new CongePayeSearchComplexModel();
        if (!JadeStringUtil.isEmpty(idConvention)) {
            searchComplexModel.setForIdConvention(idConvention);
        }
        searchComplexModel.setForDateDebutVersement(annee.getFirstDayOfYear().toString());
        searchComplexModel.setForDateFinVersement(annee.getLastDayOfYear().toString());
        searchComplexModel.setForTraitementSalaires("traitement");
        searchComplexModel.setWhereKey(CongePayeSearchComplexModel.WHERE_WITH_PERIODE_VERSEMENT_TRAITEMENT_SALAIRES);

        List<CongePaye> listeCP = searchAndFetch(searchComplexModel);
        List<CongePaye> listeCPWithDependencies = new ArrayList<CongePaye>();

        for (CongePaye congePaye : listeCP) {
            loadDependancies(congePaye);
            listeCPWithDependencies.add(congePaye);
        }
        return listeCPWithDependencies;
    }

    @Override
    public List<CongePaye> findCPSoumisLPP(Annee annee) {
        AnneeComptable anneeComptable = new AnneeComptable(annee);
        CongePayeSearchComplexModel searchModel = new CongePayeSearchComplexModel();
        searchModel.setForDateDebutVersement(anneeComptable.getDateDebut());
        searchModel.setForDateFinVersement(anneeComptable.getDateFin());
        searchModel.setWhereKey(CongePayeSearchComplexModel.WHERE_WITH_PERIODE_VERSEMENT);
        List<CongePaye> list = searchAndFetch(searchModel);
        List<CongePaye> listSoumis = new ArrayList<CongePaye>();
        for (CongePaye congePaye : list) {
            loadDependancies(congePaye);
            if (congePaye.hasLPP()) {
                listSoumis.add(congePaye);
            }
        }
        Collections.sort(listSoumis, new ListeCPSoumisComparator());
        return listSoumis;
    }
}
