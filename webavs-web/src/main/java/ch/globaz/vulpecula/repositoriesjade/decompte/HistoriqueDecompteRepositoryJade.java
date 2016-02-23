package ch.globaz.vulpecula.repositoriesjade.decompte;

import globaz.jade.client.util.JadeStringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.NotImplementedException;
import ch.globaz.vulpecula.business.models.decomptes.HistoriqueDecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.HistoriqueDecompteSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.HistoriqueDecompteSimpleModel;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.repositories.decompte.HistoriqueDecompteRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.HistoriqueDecompteConverter;

public class HistoriqueDecompteRepositoryJade extends
        RepositoryJade<HistoriqueDecompte, HistoriqueDecompteComplexModel, HistoriqueDecompteSimpleModel> implements
        HistoriqueDecompteRepository {

    private static final String ORDER_BY_DATE_DESC = "orderByDateDesc";

    @Override
    public List<HistoriqueDecompte> findByIdDecompte(final String idDecompte) {
        HistoriqueDecompteSearchComplexModel searchModel = new HistoriqueDecompteSearchComplexModel();
        searchModel.setForIdDecompte(idDecompte);
        return searchAndFetch(searchModel);
    }

    @Override
    public HistoriqueDecompte findById(final String id) {
        throw new NotImplementedException("Méthode non implémentée");
    }

    @Override
    public HistoriqueDecompte update(final HistoriqueDecompte entity) {
        throw new UnsupportedOperationException("Il n'est pas possible de mettre à jour un historique de décompte");
    }

    @Override
    public void delete(final HistoriqueDecompte entity) {
        throw new UnsupportedOperationException("Il n'est pas possible de supprimer un historique de décompte");
    }

    @Override
    public List<HistoriqueDecompte> findByIdDecompteIn(final List<String> ids) {
        HistoriqueDecompteSearchComplexModel searchModel = new HistoriqueDecompteSearchComplexModel();
        searchModel.setForIdDecompteIn(ids);
        return searchAndFetch(searchModel);
    }

    @Override
    public DomaineConverterJade<HistoriqueDecompte, HistoriqueDecompteComplexModel, HistoriqueDecompteSimpleModel> getConverter() {
        return new HistoriqueDecompteConverter();
    }

    @Override
    public List<HistoriqueDecompte> findLastHistoriqueValidationDecompte(Decompte decompte) {
        HistoriqueDecompteSearchComplexModel searchModel = new HistoriqueDecompteSearchComplexModel();
        searchModel.setForIdDecompte(decompte.getId());
        searchModel.setForEtatsIn(Arrays.asList(EtatDecompte.RECTIFIE.getValue(), EtatDecompte.VALIDE.getValue(),
                EtatDecompte.RECEPTIONNE.getValue(), EtatDecompte.GENERE.getValue(), EtatDecompte.OUVERT.getValue()));
        searchModel.setOrderKey(ORDER_BY_DATE_DESC);
        List<HistoriqueDecompte> historiques = searchAndFetch(searchModel);
        Collections.sort(historiques, Collections.reverseOrder());
        return historiques;
    }

    @Override
    public List<HistoriqueDecompte> findLastHistoriqueDecompte(String idDecompte, String etat) {
        HistoriqueDecompteSearchComplexModel searchModel = new HistoriqueDecompteSearchComplexModel();
        searchModel.setForIdDecompte(idDecompte);
        if (!JadeStringUtil.isBlank(etat)) {
            searchModel.setForEtat(etat);
        }
        searchModel.setOrderKey(ORDER_BY_DATE_DESC);
        return searchAndFetch(searchModel);
    }
}
