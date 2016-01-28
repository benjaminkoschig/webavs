package ch.globaz.vulpecula.repositoriesjade.congepaye.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.vulpecula.business.models.congepaye.TauxCongePayeComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.TauxCongePayeSearchSimpleModel;
import ch.globaz.vulpecula.business.models.congepaye.TauxCongePayeSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.congepaye.TauxCongePaye;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.AssuranceConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class TauxCongePayeConverter implements
        DomaineConverterJade<TauxCongePaye, TauxCongePayeComplexModel, TauxCongePayeSimpleModel> {
    private static final TauxCongePayeConverter INSTANCE = new TauxCongePayeConverter();

    public static TauxCongePayeConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public TauxCongePaye convertToDomain(TauxCongePayeComplexModel tauxCongePayeComplexModel) {
        TauxCongePayeSimpleModel tauxCongePayeSimpleModel = tauxCongePayeComplexModel.getTauxCongePayeSimpleModel();
        AssuranceSimpleModel assuranceSimpleModel = tauxCongePayeComplexModel.getAssuranceSimpleModel();

        TauxCongePaye tauxCongePaye = convertToDomain(tauxCongePayeSimpleModel);
        tauxCongePaye.setAssurance(AssuranceConverter.convertToDomain(assuranceSimpleModel));

        return tauxCongePaye;
    }

    @Override
    public TauxCongePayeSimpleModel convertToPersistence(TauxCongePaye entity) {
        TauxCongePayeSimpleModel tauxCongePayeSimpleModel = new TauxCongePayeSimpleModel();
        tauxCongePayeSimpleModel.setId(entity.getId());
        tauxCongePayeSimpleModel.setTaux(entity.getTaux().getValue());
        tauxCongePayeSimpleModel.setSpy(entity.getSpy());
        tauxCongePayeSimpleModel.setIdCongePaye(entity.getIdCongePaye());
        tauxCongePayeSimpleModel.setIdAssurance(entity.getIdAssurance());
        return tauxCongePayeSimpleModel;
    }

    @Override
    public TauxCongePaye convertToDomain(TauxCongePayeSimpleModel simpleModel) {
        TauxCongePaye tauxCongePaye = new TauxCongePaye();
        tauxCongePaye.setId(simpleModel.getId());
        tauxCongePaye.setTaux(new Taux(simpleModel.getTaux()));
        tauxCongePaye.setIdCongePaye(simpleModel.getIdCongePaye());
        tauxCongePaye.setSpy(simpleModel.getSpy());
        return tauxCongePaye;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new TauxCongePayeSearchSimpleModel();
    }

}
