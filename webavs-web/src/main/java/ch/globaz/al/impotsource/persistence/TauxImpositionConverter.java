package ch.globaz.al.impotsource.persistence;

import ch.globaz.al.impotsource.models.TauxImpositionSearchSimpleModel;
import ch.globaz.al.impotsource.models.TauxImpositionSimpleModel;
import ch.globaz.al.impotsource.domain.TauxImposition;
import ch.globaz.al.impotsource.domain.TypeImposition;
import ch.globaz.common.persistence.DomaineConverterJade;
import ch.globaz.common.persistence.DomaineJadeAbstractSearchModel;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;

public class TauxImpositionConverter implements
        DomaineConverterJade<TauxImposition, TauxImpositionSimpleModel> {

    private static final TauxImpositionConverter INSTANCE = new TauxImpositionConverter();

    public static TauxImpositionConverter getInstance() {
        return INSTANCE;
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
    public DomaineJadeAbstractSearchModel getSearchModel() {
        return new TauxImpositionSearchSimpleModel();
    }

}
