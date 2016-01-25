package ch.globaz.vulpecula.repositoriesjade.servicemilitaire.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.vulpecula.business.models.servicemilitaire.TauxServiceMilitaireComplexModel;
import ch.globaz.vulpecula.business.models.servicemilitaire.TauxServiceMilitaireSearchSimpleModel;
import ch.globaz.vulpecula.business.models.servicemilitaire.TauxServiceMilitaireSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.servicemilitaire.TauxServiceMilitaire;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.AssuranceConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class TauxServiceMilitaireConverter implements
        DomaineConverterJade<TauxServiceMilitaire, TauxServiceMilitaireComplexModel, TauxServiceMilitaireSimpleModel> {

    private static final TauxServiceMilitaireConverter INSTANCE = new TauxServiceMilitaireConverter();

    public static TauxServiceMilitaireConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public TauxServiceMilitaire convertToDomain(TauxServiceMilitaireComplexModel tauxServiceMilitaireComplexModel) {
        TauxServiceMilitaireSimpleModel tauxServiceMilitaireSimpleModel = tauxServiceMilitaireComplexModel
                .getTauxServiceMilitaireSimpleModel();
        AssuranceSimpleModel assuranceSimpleModel = tauxServiceMilitaireComplexModel.getAssuranceSimpleModel();

        TauxServiceMilitaire tauxServiceMilitaire = convertToDomain(tauxServiceMilitaireSimpleModel);
        tauxServiceMilitaire.setAssurance(AssuranceConverter.convertToDomain(assuranceSimpleModel));

        return tauxServiceMilitaire;
    }

    @Override
    public TauxServiceMilitaireSimpleModel convertToPersistence(TauxServiceMilitaire entity) {
        TauxServiceMilitaireSimpleModel simpleModel = new TauxServiceMilitaireSimpleModel();
        simpleModel.setId(entity.getId());
        simpleModel.setIdAssurance(entity.getAssurance().getId());
        simpleModel.setIdServiceMilitaire(entity.getIdServiceMilitaire());
        simpleModel.setTaux(entity.getTauxValue());
        simpleModel.setSpy(entity.getSpy());
        return simpleModel;
    }

    @Override
    public TauxServiceMilitaire convertToDomain(TauxServiceMilitaireSimpleModel simpleModel) {
        TauxServiceMilitaire tauxServiceMilitaire = new TauxServiceMilitaire();
        tauxServiceMilitaire.setId(simpleModel.getId());
        tauxServiceMilitaire.setIdServiceMilitaire(simpleModel.getIdServiceMilitaire());
        tauxServiceMilitaire.setTaux(new Taux(simpleModel.getTaux()));
        tauxServiceMilitaire.setSpy(simpleModel.getSpy());
        return tauxServiceMilitaire;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new TauxServiceMilitaireSearchSimpleModel();
    }
}
