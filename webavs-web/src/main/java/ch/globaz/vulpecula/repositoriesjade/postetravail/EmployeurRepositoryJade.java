package ch.globaz.vulpecula.repositoriesjade.postetravail;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurSearchComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurSearchSimpleModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.postetravail.EmployeurRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.EmployeurConverter;

/***
 * Implémentation Jade de {@link EmployeurRepository}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 20 déc. 2013
 * 
 */
public class EmployeurRepositoryJade extends RepositoryJade<Employeur, EmployeurComplexModel, EmployeurSimpleModel>
        implements EmployeurRepository {

    @Override
    public Employeur findByIdAffilie(final String id) {
        EmployeurSearchComplexModel search = new EmployeurSearchComplexModel();
        search.setForAffiliationId(id);
        return searchAndFetchFirst(search);
    }

    @Override
    public List<Employeur> findByIdConvention(final String idConvention, final Date dateDebut, final Date dateFin,
            final Collection<String> inPeriodicite) {
        EmployeurSearchComplexModel search = new EmployeurSearchComplexModel();
        search.setForIdConvention(idConvention);
        search.setInPeriodicite(inPeriodicite);
        search.setForDateDebut(dateDebut.getSwissValue());
        search.setForDateFin(dateFin.getSwissValue());
        return searchAndFetch(search);
    }

    @Override
    public List<Employeur> findByIdConvention(String idConvention, Collection<String> inPeriodicite) {
        EmployeurSearchComplexModel search = new EmployeurSearchComplexModel();
        search.setForIdConvention(idConvention);
        search.setInPeriodicite(inPeriodicite);
        return searchAndFetch(search);
    }

    @Override
    public List<Employeur> findByIdConvention(final String idConvention) {
        EmployeurSearchComplexModel search = new EmployeurSearchComplexModel();
        search.setForIdConvention(idConvention);
        return searchAndFetch(search);
    }

    @Override
    @Deprecated
    public List<Employeur> findByIdAffilie(final String idAffilie, final Date dateDebut, final Date dateFin,
            final Collection<String> inPeriodicite) {
        EmployeurSearchComplexModel search = new EmployeurSearchComplexModel();
        search.setForAffiliationId(idAffilie);
        search.setInPeriodicite(inPeriodicite);
        search.setForDateDebut(dateDebut.getSwissValue());
        search.setForDateFin(dateFin.getSwissValue());
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        return searchAndFetch(search);
    }

    @Override
    public List<Employeur> findEmployeursWithEdition() {
        EmployeurSearchComplexModel searchModel = new EmployeurSearchComplexModel();
        searchModel.setIsEditerSansTravailleur(true);
        return searchAndFetch(searchModel);
    }

    @Override
    public Employeur findById(final String id) {
        return findByIdAffilie(id);
    }

    @Override
    public Employeur findByNumAffilie(String numAffilie) {
        EmployeurSearchComplexModel search = new EmployeurSearchComplexModel();
        search.setForNumeroAffilie(numAffilie);
        return searchAndFetchFirst(search);
    }

    @Override
    public List<Employeur> findAll() {
        EmployeurSearchComplexModel search = new EmployeurSearchComplexModel();
        return searchAndFetch(search);
    }

    @Override
    public DomaineConverterJade<Employeur, EmployeurComplexModel, EmployeurSimpleModel> getConverter() {
        return new EmployeurConverter();
    }

    @Override
    public boolean hasEntryInDB(Employeur employeur) {
        EmployeurSearchSimpleModel searchModel = new EmployeurSearchSimpleModel();
        searchModel.setForIdAffiliation(employeur.getId());
        Employeur employeurFound = searchAndFetchFirst(searchModel);
        if (employeurFound == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasEntryInDB(String idEmployeur) {
        EmployeurSearchSimpleModel searchModel = new EmployeurSearchSimpleModel();
        searchModel.setForIdAffiliation(idEmployeur);
        Employeur employeurFound = searchAndFetchFirst(searchModel);
        if (employeurFound == null) {
            return false;
        }
        return true;
    }

    @Override
    public List<Employeur> findByPeriode(Date dateDebut, Date dateFin) {
        EmployeurSearchComplexModel searchModel = new EmployeurSearchComplexModel();
        searchModel.setForDateDebutGreaterOrEquals(dateDebut);
        searchModel.setForDateDebutLessOrEquals(dateFin);
        return searchAndFetch(searchModel);
    }

}
