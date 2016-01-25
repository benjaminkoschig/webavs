package ch.globaz.vulpecula.repositoriesjade.is.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.is.TauxImpositionSearchSimpleModel;
import ch.globaz.vulpecula.business.models.is.TauxImpositionSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.is.TauxImposition;
import ch.globaz.vulpecula.domain.models.is.TypeImposition;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class TauxImpositionConverter implements
        DomaineConverterJade<TauxImposition, JadeComplexModel, TauxImpositionSimpleModel> {

    private static final TauxImpositionConverter INSTANCE = new TauxImpositionConverter();

    public static TauxImpositionConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public TauxImposition convertToDomain(JadeComplexModel model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TauxImpositionSimpleModel convertToPersistence(TauxImposition tauxImposition) {
        TauxImpositionSimpleModel tauxImpositionSimpleModel = new TauxImpositionSimpleModel();
        tauxImpositionSimpleModel.setId(tauxImposition.getId());
        tauxImpositionSimpleModel.setPeriodeDebut(tauxImposition.getPeriodeDebut().getSwissValue());
        if (tauxImposition.getPeriodeFin() != null) {
            tauxImpositionSimpleModel.setPeriodeFin(tauxImposition.getPeriodeFin().getSwissValue());
        }
        tauxImpositionSimpleModel.setCanton(tauxImposition.getCanton());
        tauxImpositionSimpleModel.setTypeImposition(tauxImposition.getTypeImposition().getValue());
        tauxImpositionSimpleModel.setTaux(tauxImposition.getTaux().getValue());
        tauxImpositionSimpleModel.setSpy(tauxImposition.getSpy());
        return tauxImpositionSimpleModel;
    }

    @Override
    public TauxImposition convertToDomain(TauxImpositionSimpleModel tauxImpositionSimpleModel) {
        TauxImposition tauxImposition = new TauxImposition();
        tauxImposition.setId(tauxImpositionSimpleModel.getId());
        tauxImposition.setPeriode(new Periode(tauxImpositionSimpleModel.getPeriodeDebut(), tauxImpositionSimpleModel
                .getPeriodeFin()));
        tauxImposition.setCanton(tauxImpositionSimpleModel.getCanton());
        tauxImposition.setTypeImposition(TypeImposition.fromValue(tauxImpositionSimpleModel.getTypeImposition()));
        tauxImposition.setTaux(new Taux(tauxImpositionSimpleModel.getTaux()));
        tauxImposition.setSpy(tauxImpositionSimpleModel.getSpy());
        return tauxImposition;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new TauxImpositionSearchSimpleModel();
    }

}
