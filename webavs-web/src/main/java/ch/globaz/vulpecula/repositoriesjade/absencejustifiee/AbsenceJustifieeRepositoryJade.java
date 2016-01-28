package ch.globaz.vulpecula.repositoriesjade.absencejustifiee;

import globaz.jade.client.util.JadeStringUtil;
import java.util.List;
import ch.globaz.vulpecula.business.models.absencejustifiee.AbsenceJustifieeComplexModel;
import ch.globaz.vulpecula.business.models.absencejustifiee.AbsenceJustifieeSearchComplexModel;
import ch.globaz.vulpecula.business.models.absencejustifiee.AbsenceJustifieeSimpleModel;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.repositories.absencejustifiee.AbsenceJustifieeRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.absencejustifiee.converters.AbsenceJustifieeConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.prestations.ParamQualifHolder;

public class AbsenceJustifieeRepositoryJade extends
        RepositoryJade<AbsenceJustifiee, AbsenceJustifieeComplexModel, AbsenceJustifieeSimpleModel> implements
        AbsenceJustifieeRepository {

    @Override
    public DomaineConverterJade<AbsenceJustifiee, AbsenceJustifieeComplexModel, AbsenceJustifieeSimpleModel> getConverter() {
        return new AbsenceJustifieeConverter();
    }

    @Override
    public AbsenceJustifiee findById(String id) {
        AbsenceJustifieeSearchComplexModel searchModel = new AbsenceJustifieeSearchComplexModel();
        searchModel.setForId(id);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<AbsenceJustifiee> findByIdTravailleur(String id) {
        AbsenceJustifieeSearchComplexModel searchComplexModel = new AbsenceJustifieeSearchComplexModel();
        searchComplexModel.setForIdTravailleur(id);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<AbsenceJustifiee> findByIdTravailleurOrderByIdpassage(String id) {
        AbsenceJustifieeSearchComplexModel searchComplexModel = new AbsenceJustifieeSearchComplexModel();
        searchComplexModel.setForIdTravailleur(id);
        searchComplexModel.setOrderKey(AbsenceJustifieeSearchComplexModel.ORDERBY_ID_PASSAGE_FACTURATION_DESC);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<AbsenceJustifiee> findDecomptesForFacturation(String idPassageFacturation) {
        return findByIdPassage(idPassageFacturation);
    }

    @Override
    public List<AbsenceJustifiee> findByIdPassage(String idPassageFacturation) {
        AbsenceJustifieeSearchComplexModel searchComplexModel = new AbsenceJustifieeSearchComplexModel();
        searchComplexModel.setForIdPassage(idPassageFacturation);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<AbsenceJustifiee> findByIdPassage(String idPassage, String idEmployeur) {
        AbsenceJustifieeSearchComplexModel searchComplexModel = new AbsenceJustifieeSearchComplexModel();
        searchComplexModel.setForIdPassage(idPassage);
        searchComplexModel.setForIdEmployeur(idEmployeur);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<AbsenceJustifiee> findBy(String idPassage, String idEmployeur, String idTravailleur, String idConvention) {
        return findBy(idPassage, idEmployeur, idTravailleur, idConvention, null);
    }

    @Override
    public List<AbsenceJustifiee> findBy(String idPassage, String idEmployeur, String idTravailleur,
            String idConvention, String orderBy) {
        return findBy(idPassage, idEmployeur, idTravailleur, idConvention, null, orderBy, null);
    }

    @Override
    public List<AbsenceJustifiee> findBy(String idPassage, String idEmployeur, String idTravailleur,
            String idConvention, Periode periode, String orderBy) {
        return findBy(idPassage, idEmployeur, idTravailleur, idConvention, periode, orderBy,
                AbsenceJustifieeSearchComplexModel.WHERE_WITHDATE);
    }

    public List<AbsenceJustifiee> findBy(String idPassage, String idEmployeur, String idTravailleur,
            String idConvention, Periode periode, String orderBy, String whereClause) {
        ParamQualifHolder ph = new ParamQualifHolder();
        AbsenceJustifieeSearchComplexModel searchModel = new AbsenceJustifieeSearchComplexModel();
        if (!JadeStringUtil.isBlankOrZero(idPassage)) {
            searchModel.setForIdPassage(idPassage);
        }
        searchModel.setForIdTravailleur(idTravailleur);
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForIdConvention(idConvention);
        if (periode != null) {
            searchModel.setForDateDebut(periode.getDateDebutAsSwissValue());
            searchModel.setForDateFin(periode.getDateFinAsSwissValue());
        }
        searchModel.setOrderKey(orderBy);
        searchModel.setWhereKey(whereClause);
        List<AbsenceJustifiee> absencesJustifiees = searchAndFetch(searchModel);

        for (AbsenceJustifiee absenceJustifiee : absencesJustifiees) {
            ph.setParametresQualficiationTo(absenceJustifiee);
        }

        return absencesJustifiees;
    }
}
