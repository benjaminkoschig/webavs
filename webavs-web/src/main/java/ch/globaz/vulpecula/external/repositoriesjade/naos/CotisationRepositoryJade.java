package ch.globaz.vulpecula.external.repositoriesjade.naos;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.vulpecula.external.models.CotisationComplexModel;
import ch.globaz.vulpecula.external.models.CotisationSearchComplexModel;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.repositories.affiliation.CotisationRepository;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.CotisationConverter;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;

/**
 * @author Luis Benimeli (LBE) | Créé le 18.05.2017
 *
 */
public class CotisationRepositoryJade implements CotisationRepository {

    @Override
    public List<Cotisation> findByIdAffilie(String idAffilie) {
        List<Cotisation> cotisations = new ArrayList<Cotisation>();
        try {
            CotisationSearchComplexModel searchModel = new CotisationSearchComplexModel();
            searchModel.setForIdAffilie(idAffilie);
            searchModel.setForDateFinGreaterEquals(Date.now().toString());
            searchModel = (CotisationSearchComplexModel) JadePersistenceManager.search(searchModel);
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                CotisationComplexModel cotisationComplexModel = (CotisationComplexModel) model;
                Cotisation cotisation = CotisationConverter.convertToDomain(cotisationComplexModel);
                cotisations.add(cotisation);
            }
        } catch (JadePersistenceException ex) {
            JadeLogger.error(ex, ex.getMessage());
        }
        return cotisations;
    }
}
