package ch.globaz.vulpecula.businessimpl.services.decompte;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteSearchSimpleModel;
import ch.globaz.vulpecula.business.services.decompte.CotisationDecompteService;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.CotisationDecompteConverter;

public class CotisationDecompteServiceImpl implements CotisationDecompteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CotisationDecompteServiceImpl.class);

    private DecompteSalaireRepository decompteSalaireRepository;

    public CotisationDecompteServiceImpl(DecompteSalaireRepository decompteSalaireRepository) {
        this.decompteSalaireRepository = decompteSalaireRepository;
    }

    @Override
    public List<CotisationDecompte> getCotisationsDecompte(final String idDecompteSalaire) {
        DecompteSalaire decompteSalaire = decompteSalaireRepository.findById(idDecompteSalaire);

        List<CotisationDecompte> cotisationsDecompte = new ArrayList<CotisationDecompte>();
        CotisationDecompteSearchComplexModel searchModel = new CotisationDecompteSearchComplexModel();
        searchModel.setForIdLigneDecompte(idDecompteSalaire);
        try {
            JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException ex) {
            ex.printStackTrace();
        }
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            CotisationDecompteComplexModel cotisationDecompteComplexModel = (CotisationDecompteComplexModel) model;
            CotisationDecompte cotisationDecompte = CotisationDecompteConverter.convertToDomain(
                    cotisationDecompteComplexModel, decompteSalaire);
            cotisationsDecompte.add(cotisationDecompte);
        }

        return cotisationsDecompte;
    }

    @Override
    public Taux deleteCotisationDecompte(String idDecompteSalaire, String idCotisationDecompte) {
        CotisationDecompteSearchSimpleModel searchModel = new CotisationDecompteSearchSimpleModel();
        searchModel.setForId(idCotisationDecompte);
        try {
            JadePersistenceManager.delete(searchModel);
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }

        // Le décompte salaire est recherché et persisté à nouveau, ce qui aura pour effet de recalculer le taux
        // contribuable.
        DecompteSalaire decompteSalaire = decompteSalaireRepository.findById(idDecompteSalaire);
        decompteSalaireRepository.update(decompteSalaire);
        return decompteSalaire.getTauxContribuableForCaissesSociales();
    }

}
